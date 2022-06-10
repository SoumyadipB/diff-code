package com.ericsson.isf.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.RpaDAO;

import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.AmqpPublishModel;
import com.ericsson.isf.model.EventPublisherRequestModel;

import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.botstore.ServerBotModel;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AppUtil;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.microsoft.azure.storage.StorageException;

@Service
public class BotService {
	private static final String ZIP = ".zip";

	private static final String API_MESSAGE = "API Message {}";

	private static final String API_RESPONSE = "API RESPONSE {}";

	private static final String RUN_SERVER_BOT = "RUN_SERVER_BOT";

	private final Logger LOG = LoggerFactory.getLogger(BotService.class);

	@Autowired
	private ApplicationConfigurations configurations;

	

	@Autowired
	private RpaDAO rpaDAO;

	@Autowired
	private ExternalInterfaceManagmentService externalInterfaceManagmentService;
	

	@Transactional("txManager")
	public String runServerBot(MultipartFile inputZip, String serverBotJsonStr, String uri) throws Exception {
		Response<String> apiResponse = new Response<>();
		File convFile = null;
		ServerBotModel serverBotModel = AppUtil.convertHtmlJsonToClassObject(serverBotJsonStr, ServerBotModel.class);
		String fileName = StringUtils.EMPTY;
		byte[] byteData = null;
		String serverBotOutputUrl=null;
		FileOutputStream fos = null;
		try {

			// AMQP Implementation with serverBot Input and OutPut URL feature
			if (configurations.getBooleanProperty(ConfigurationFilesConstants.SERVERBOT_EXECUTION_AMQP_ENABLED, true)) {

				if(!StringUtils.isEmpty(serverBotModel.getServerBotInputUrl())) {
					LOG.info("Inside ServerBotInputURL Not Empty case");
				 serverBotOutputUrl = prepareServerBotOutputURL(serverBotModel);
			     }
				
				// OutPutUpload No Case
				if (StringUtils.isNotEmpty(serverBotModel.getOutputUpload())
						&& StringUtils.equalsIgnoreCase(serverBotModel.getOutputUpload(), "NO")
						&& StringUtils.isEmpty(serverBotModel.getServerBotOutputUrl())
						&& StringUtils.isEmpty(serverBotModel.getServerBotInputUrl())) {

					LOG.info("Inside OutPut Upload No case");
					serverBotOutputUrl = null;
					serverBotModel.setServerBotInputUrl(null);
				}
				
				
				//Cascade case
				
				if (StringUtils.isEmpty(serverBotModel.getServerBotInputUrl())
						&& StringUtils.isEmpty(serverBotModel.getServerBotOutputUrl())
						&& StringUtils.isNotEmpty(serverBotModel.getOutputLink())) {
					LOG.info("Inside ServerBotInputURL Empty case");
					String serverBotOurPutURLForCascade = serverBotModel.getOutputLink();
					LOG.info("serverBotOutputURL for cascade Bot is {} ", serverBotOurPutURLForCascade);
					serverBotModel.setServerBotInputUrl(serverBotOurPutURLForCascade);
					if (!StringUtils.isEmpty(serverBotModel.getServerBotInputUrl())) {
						serverBotOutputUrl = prepareServerBotOutputURL(serverBotModel);
					}

				}
				
				// No Input Bot Case: We have only OutPut URL given by User
				if(StringUtils.isNotEmpty(serverBotModel.getServerBotOutputUrl())){
					LOG.info("Inside No Input Bot case");
					serverBotOutputUrl = serverBotModel.getServerBotOutputUrl();
					
				}
				

				rpaDAO.saveInputZipFile(serverBotModel, byteData, null, "input", serverBotModel.getServerBotInputUrl(),
						serverBotOutputUrl);

				AmqpPublishModel amqpPublishModel = new AmqpPublishModel();
				amqpPublishModel.setPayload(serverBotModel);

				if (StringUtils.isNotEmpty(serverBotModel.getServerBotInputUrl())) {
					amqpPublishModel.setServerBotInputUrl(serverBotModel.getServerBotInputUrl());
				}
				if (StringUtils.isNotEmpty(serverBotOutputUrl)) {
					amqpPublishModel.setServerBotOutputUrl(serverBotOutputUrl);
				}

				apiResponse = callAMQPClient(amqpPublishModel);
				LOG.info("after callAMQPClient  {}", apiResponse.toString());
				LOG.info(API_RESPONSE, apiResponse.getResponseData());
				LOG.info(API_MESSAGE, apiResponse.getFormMessages());

			}

			else {

				// Fallback case, if user provide Input zip as file
				// cascade bot case
				if (inputZip == null) {

					byteData = cascadeBotCase(serverBotModel, byteData);
				} else {
					LOG.info("Inside else condition if inputZip is not null: ");
					convFile = new File(configurations.getStringProperty(ConfigurationFilesConstants.FILE_PATH)
							+ inputZip.getOriginalFilename());

					LOG.info("conFile in the else condition is ---> {}", convFile);
					fos = new FileOutputStream(convFile);
					fos.write(inputZip.getBytes());

					fileName = inputZip.getOriginalFilename().trim();

					byteData = inputZip.getBytes();
					LOG.info("Inside else condition bydedata is  ", byteData);
					LOG.info("file name is {}", fileName);

				}

				if (convFile == null) {
					LOG.info("Inside if condition convFile is  null");
					byte[] botJarFile = null;
					List<HashMap<String, Object>> botFile = rpaDAO.getTemplateFile("NoInputBotDummy");
					if (null != botFile && botFile.size() > 0) {
						botJarFile = (byte[]) botFile.get(0).get("templateFile");
						if (botJarFile.length > 0) {
							fileName = AppUtil.getFileNameWithTimestamp("dummy");
							convFile = new File(configurations.getStringProperty(ConfigurationFilesConstants.FILE_PATH)
									+ fileName + ZIP);
							FileUtils.writeByteArrayToFile(convFile, botJarFile);
						} else {
							botJarFile = null;
						}
					}
					byteData = botJarFile;
					LOG.info("Inside if condition convFile is  null", byteData);
				}

				rpaDAO.saveInputZipFile(serverBotModel, byteData, fileName, "input", null, null);

				// SignalR Implementaion .
				apiResponse = callAndGetResponseFromEventPublisher(serverBotJsonStr, convFile, uri);
				LOG.info("after callAndGetResponseFromEventPublisher external {}", apiResponse);
				LOG.info(API_RESPONSE, apiResponse.getResponseData());
				LOG.info(API_MESSAGE, apiResponse.getFormMessages());
			}

		} catch (Exception e) {
			System.out.println("if Exception Thrown -> " + e.getMessage());
			apiResponse.addFormError(e.getMessage());
			LOG.error("Error while calling AMQP/SIGNALR {}", e.getMessage());
			e.printStackTrace();

		} finally {
			try {
				fos.close();
				convFile.delete();
			} catch (Exception e) {
				LOG.info(e.getMessage());
			}

		}

		String result = StringUtils.EMPTY;
		if (apiResponse.getFormErrorCount() == 0) {

			result = "success";
		} else {

			result = "fail";
		}

		return result;
	}

	private String prepareServerBotOutputURL(ServerBotModel serverBotModel) {
		String serverBotOutputUrl;
		String serverBotInputUrl =serverBotModel.getServerBotInputUrl().split(".zip")[0];
		    int index=serverBotInputUrl.lastIndexOf('/');
		    serverBotOutputUrl=serverBotInputUrl.substring(0,index+1);
		return serverBotOutputUrl;
	}

	private byte[] cascadeBotCase(ServerBotModel serverBotModel, byte[] byteData)
			throws IOException, URISyntaxException, StorageException {
		String outputFileName = serverBotModel.getOutputLink();
		LOG.info("output file name is {} ", outputFileName);

		StringBuilder stringBuilder = new StringBuilder();
		if (StringUtils.isNotEmpty(outputFileName)) {
			int botID = rpaDAO.getBotIdByStepId(serverBotModel.getSubActivityFlowChartDefID(),
					serverBotModel.getStepID(), serverBotModel.getwOID());
			LOG.info("Inside inputZip null and BOTID is: {}", botID);
			stringBuilder.append(botID).append("/").append(outputFileName);
			String path = stringBuilder.toString();
			LOG.info("path inside inputZip null  is {}", path);
			byteData = externalInterfaceManagmentService.downloadCascadeBOTInput(path);
			LOG.info("Inside inputZip null and after getting byteData: ", byteData);
		}
		return byteData;
	}

	private URI fileUploadContainer(String fileName, byte[] byteData, String path, String extension) {
		if(StringUtils.isNotEmpty(fileName)) {
			extension=FilenameUtils.getExtension(fileName);	
		}
	URI uploadedFilePath;
	//	if(StringUtils.equalsIgnoreCase(extension, "zip")) {
	if (configurations.getBooleanProperty(ConfigurationFilesConstants.SERVERBOT_EXECUTION_AMQP_ENABLED, true)) {
		if (StringUtils.equalsIgnoreCase(extension, "zip")) {
			uploadedFilePath = externalInterfaceManagmentService.uploadFileInContainerFromByteArray(byteData, path,
					fileName);
		} else {
			uploadedFilePath = externalInterfaceManagmentService.uploadFileInContainerFromByteArray(byteData, path,
					fileName + ZIP);
		}

	} else {	
		uploadedFilePath = externalInterfaceManagmentService.uploadFileInContainerFromByteArray(byteData, path,
				fileName);

	}
		
		LOG.info("after uploadFileInContainerFromByteArray  url is:::::::{}",uploadedFilePath);
		return uploadedFilePath;
	}

	private Response<String> callAMQPClient(AmqpPublishModel amqpPublishModel) {
		
		RestTemplate restTemplate = new RestTemplate();
		Response<String> response = new Response<>();
		String url = configurations.getStringProperty(ConfigurationFilesConstants.MQTT_APPLICATION_URL)
				+ AppConstants.CALL_AMQP_API;
		String res = StringUtils.EMPTY;
		LOG.info("AMQP url called============:{}", url);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		LOG.info("AMQP payload is::::::::::::::::::::::::::============:{}", amqpPublishModel);
		
		HttpEntity<AmqpPublishModel> request = new HttpEntity<>(amqpPublishModel, headers);

		try {
			res = restTemplate.postForObject(url, request, String.class);
			LOG.info("Response from AMQP is::::->{}", res);
			response.setResponseData(res);
		} catch (RestClientException ex) {
			LOG.debug(String.format("Exception thrown  %s", ex.getMessage()));
			res = ex.getMessage();
			response.addFormError(res);
		}
		return response;
	
		
	}

	private Response<String> callAndGetResponseFromEventPublisher(String serverBotJsonStr, File convFile, String uri)
			throws IOException {
		
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
		parts.add("botDetail", serverBotJsonStr);
		parts.add("file", new FileSystemResource(convFile.getAbsolutePath()));
        
		Response<String> response = new Response<>();
		String url = configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_SERVERBOTS_SERVER_URL) + uri;
		//String payLoad = AppUtil.convertClassObjectToJson(parts);
		String payLoad = AppUtil.convertClassObjectToJsonServerBOT(parts);//server bot issue
        
		EventPublisherRequestModel eventPublisherRequestModel = new EventPublisherRequestModel();
		eventPublisherRequestModel.setUrl(url);
		eventPublisherRequestModel.setPayLoad(payLoad);
		eventPublisherRequestModel.setSourceID(0L);
		eventPublisherRequestModel.setExternalSourceName(StringUtils.EMPTY);
		eventPublisherRequestModel.setHeaderModels(null);
		eventPublisherRequestModel.setMethodType(HttpMethod.POST);
		eventPublisherRequestModel.setCategory(RUN_SERVER_BOT);
		


		LOG.info("\neventPublisherRequestModel : {}" , eventPublisherRequestModel);
		
		RestTemplate restTemplate = new RestTemplate();
		String serverUrl = configurations.getStringProperty(ConfigurationFilesConstants.SIGNALR_APPLICATION_URL) + AppConstants.CALL_SIGNALR_CLIENT;
		String res = StringUtils.EMPTY;
		LOG.info("\n server_url called============:\n {}" , serverUrl);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<EventPublisherRequestModel> request = new HttpEntity<>(eventPublisherRequestModel, headers);
		LOG.info("\nServer Bot Request ===================================: {} " , request.getBody());
		
		try {
			res = restTemplate.postForObject(serverUrl, request, String.class);
			LOG.info("\nServer Bot Response =================================: {}" ,res);
			response.setResponseData(res);
		} catch (RestClientException ex) {
			LOG.debug(String.format("Exception thrown  %s", ex.getMessage()));
			res = ex.getMessage();
			response.addFormError(res);
		}
		return response;
	}

	public String getConfigUploadFilePathForBotStore() {
		return configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_UPLOAD_PATH);
	}

	public void uploadFile(MultipartFile file, String relativeFilePath, String fileName) {
		//
		String directory = getConfigUploadFilePathForBotStore() + "/" + relativeFilePath;
		File df = new File(directory);
		if (!df.exists()) {
			df.mkdirs();
		} else {
			try {
				FileUtils.deleteDirectory(df);
				df.mkdirs();
			} catch (Exception e) {
				throw new ApplicationException(500, "File upload failed with error - " + e.getMessage());
			}
		}

		if (file != null && !file.isEmpty()) {

			try {
				byte[] bytes = file.getBytes();
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(
						new File(getConfigUploadFilePathForBotStore() + "/" + relativeFilePath + "/" + fileName)));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				throw new ApplicationException(500, "File upload failed with error - " + e.getMessage());
			}
		}
	}
}
