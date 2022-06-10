package com.ericsson.isf.eventPublisher.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ericsson.isf.azure.service.AzureService;
import com.ericsson.isf.azure.service.AzureServiceBuilder;
import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.model.EventPublisherRequestModel;
import com.ericsson.isf.model.HeaderModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.service.BotService;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.ConfigurationFilesConstants;

@Service
public class EventPublisherServiceImpl {

	@Autowired
	private ApplicationConfigurations configurations;
	
	private final Logger LOG = LoggerFactory.getLogger(EventPublisherServiceImpl.class);

	public Response<String> sendRequestToExternalSources(
			EventPublisherRequestModel eventPublisherModel) {
		LOG.info("=========================== inside sendRequestToExternalSources+++++++++++++++++++++++++===================================== ");
		Response<String> finalResponse = new Response<String>();
		String response = StringUtils.EMPTY;
		LOG.info("=========================== before eventPublisherModel.getExternalSourceName()+++++++++++++++++++++++++===================================== "+eventPublisherModel.getExternalSourceName());
		if (eventPublisherModel.getExternalSourceName().equalsIgnoreCase(AppConstants.ERISITE)
				&& eventPublisherModel.getCategory().equalsIgnoreCase(AppConstants.WO_CLOSURE)) {
			LOG.info("=========================== inside eventPublisherModel.getExternalSourceName()+++++++++++++++++++++++++===================================== "+eventPublisherModel.getExternalSourceName());
			RestTemplate restTemplate = new RestTemplate();
			String url = eventPublisherModel.getUrl();
			
			HttpHeaders headers = new HttpHeaders();
			
			// Setting Static Headers
			setStaticHeadersForErisiteWOClosure(headers);

			// Setting Dynamic Headers
			if (CollectionUtils.isNotEmpty(eventPublisherModel.getHeaderModels())) {
				for (HeaderModel header : eventPublisherModel.getHeaderModels()) {
					headers.set(header.getHeaderKey(), header.getHeaderValue());
				}
			}

			try {
				HttpEntity<String> request = new HttpEntity<>(eventPublisherModel.getPayLoad(), headers);
				LOG.info("\nSignalR Server Request [ERISITE] (IF) ===================== : " + request.getBody());
				LOG.info("=========================== before configuration configurations.getBooleanProperty!!!!!!!!!!!!!!!!!!+++++++++++++++++++++++++===================================== "+configurations.getBooleanProperty(ConfigurationFilesConstants.IS_ERISITE_UP));
				if(configurations.getBooleanProperty(ConfigurationFilesConstants.IS_ERISITE_UP)) {

					LOG.info("\nUrl Called [ERISITE] ===================== : " + url);
					//response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
					response = restTemplate.postForObject(url, request, String.class);
				
				finalResponse.setResponseData(response);
				}
				else {
					LOG.info("======###################################################################inside else=====================");
					response=callSignalRClientForErisite(eventPublisherModel);
				}
				LOG.info("\nSignalR Server Response [ERISITE] ===================== : " + response);
			} catch (RestClientException ex) {
				finalResponse.addFormError("Error while Work Order Closure" + ex.getMessage());
				ex.printStackTrace();
			}
			// finalResponse.addFormMessage("success");
			return finalResponse;
		} else {
			finalResponse.addFormError("Invalid Source Name or Category");
			return finalResponse;
		}
	}

	private String callSignalRClientForErisite(EventPublisherRequestModel eventPublisherModel) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<EventPublisherRequestModel> request = new HttpEntity<>(eventPublisherModel);
		setHeaderModelForSignalRClient(eventPublisherModel);
//		String url=configurations
//				.getStringProperty(ConfigurationFilesConstants.SIGNALR_CLIENT_URL);
		LOG.info("\nSignalR Server Request [ERISITE] (ELSE) ===================== : " + request.toString());

		
		String server_url = configurations.getStringProperty(ConfigurationFilesConstants.SIGNALR_APPLICATION_URL) 
				+ AppConstants.CALL_SEND_REQUEST_TO_EXTERNAL_SOURCE;
		//ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
		LOG.info("\n SignalRClient Url Called [ERISITE] (ELSE) ===================== : " + server_url);
		String response = restTemplate.postForObject(server_url, request, String.class);
		return response;
		}


	private void setHeaderModelForSignalRClient(EventPublisherRequestModel eventPublisherModel) {
		List<HeaderModel> headers = eventPublisherModel.getHeaderModels();

		headers.add(
				new HeaderModel("consumerinterfaceid",
				configurations
						.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_CONSUMERINTERFACEID) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_CONSUMERINTERFACEID)
								: "SESRVAC290"));

		headers.add(new HeaderModel("providerinterfaceid",
				configurations
				.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_PROVIDERINTERFACEID) != null
						? configurations.getStringProperty(
								ConfigurationFilesConstants.ERISITE_HEADERS_PROVIDERINTERFACEID)
						: "sesrvac230"));

		headers.add(new HeaderModel("realizationid",
				configurations
				.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_PROVIDERINTERFACEID) != null
						? configurations.getStringProperty(
								ConfigurationFilesConstants.ERISITE_HEADERS_PROVIDERINTERFACEID)
						: "sesrvac230"));

		headers.add(new HeaderModel("senderid",
				configurations
				.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_CONSUMERINTERFACEID) != null
						? configurations.getStringProperty(
								ConfigurationFilesConstants.ERISITE_HEADERS_CONSUMERINTERFACEID)
						: "SESRVAC290"));

		headers.add(new HeaderModel("receiverid",
				configurations
				.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_RECIEVERID) != null
						? configurations.getStringProperty(
								ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_RECIEVERID)
						: "Test"));
		
		headers.add(new HeaderModel("interfaceid",
				configurations
				.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_INTERFACEID) != null
						? configurations.getStringProperty(
								ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_INTERFACEID)
						: "Nrm.Nro.Activity.CrudService"));
		
		headers.add(new HeaderModel("method",
				configurations.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_METHOD) != null
				? configurations.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_METHOD)
				: "NroActivityUpdate"));
		headers.add(new HeaderModel("documenttype",
				configurations.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_DOC_TYPE) != null
				? configurations
						.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_DOC_TYPE)
				: "NroActivityUpdate"));
		headers.add(new HeaderModel("documentnamespace",
				configurations
				.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_DOC_NAME_SPACE) != null
						? configurations.getStringProperty(
								ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_DOC_NAME_SPACE)
						: "urn:Namespaces.Ericsson.Com:Nrm:Nro:Activity:CrudService:v0001"));
		headers.add(new HeaderModel("partyid",
				configurations.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_PARTY_ID) != null
				? configurations
						.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_PARTY_ID)
				: "Erisite"));
		headers.add(new HeaderModel("partytype",
				configurations
				.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_PARTY_TYPE) != null
						? configurations.getStringProperty(
								ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_PARTY_TYPE)
						: "Mdm.CustomerName"));
		headers.add(new HeaderModel("messageflow",
				configurations
				.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_FLOW) != null
						? configurations.getStringProperty(
								ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_FLOW)
						: "Nro.UpdateActivity.MsgF"));
		headers.add(new HeaderModel("businessentitytype",
				configurations.getStringProperty(
						ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_BUSINESS_ENTITY_TYPE) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_BUSINESS_ENTITY_TYPE)
								: "NroActivityCrudService.Activity.ObjectId"));
		headers.add(new HeaderModel("MESSAGE_SERVICE_NAME",
				configurations.getStringProperty(
						ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_SERVICE_NAME) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_SERVICE_NAME)
								: "ActivityEvent"));
		headers.add(new HeaderModel("MESSAGE_REQUEST_TYPE",
				configurations.getStringProperty(
						ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_REQUEST_TYPE) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_REQUEST_TYPE)
								: "UpdateOnly"));
		headers.add(new HeaderModel("MESSAGE_SERVICE_TYPE",
				configurations.getStringProperty(
						ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_SERVICE_TYPE) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_SERVICE_TYPE)
								: "UpdateActivity"));
		headers.add(new HeaderModel("Content-Type",
				configurations
				.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_CONTENT_TYPE) != null
						? configurations.getStringProperty(
								ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_CONTENT_TYPE)
						: "application/xml"));
		headers.add(new HeaderModel("flowtrackingid", Integer.toString((new Random().nextInt(50000000) + 5000))));
		String plainCreds = configurations
				.getStringProperty(ConfigurationFilesConstants.ERISITE_AUTHENTICATION_USERNAME) + ":"
				+ configurations.getStringProperty(ConfigurationFilesConstants.ERISITE_AUTHENTICATION_PASSWORD);
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		headers.add(new HeaderModel("Authorization", "Basic " + base64Creds));
		
		eventPublisherModel.setHeaderModel(headers);

	}

	private void setStaticHeadersForErisiteWOClosure(HttpHeaders headers) {
		headers.set("consumerinterfaceid",
				configurations
						.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_CONSUMERINTERFACEID) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_CONSUMERINTERFACEID)
								: "SESRVAC290");
		headers.set("providerinterfaceid",
				configurations
						.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_PROVIDERINTERFACEID) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_PROVIDERINTERFACEID)
								: "sesrvac230");
		headers.set("realizationid",
				configurations
						.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_PROVIDERINTERFACEID) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_PROVIDERINTERFACEID)
								: "sesrvac230");
		headers.set("senderid",
				configurations
						.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_CONSUMERINTERFACEID) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_CONSUMERINTERFACEID)
								: "SESRVAC290");
		headers.set("receiverid",
				configurations
						.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_RECIEVERID) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_RECIEVERID)
								: "Test");
		headers.set("interfaceid",
				configurations
						.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_INTERFACEID) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_INTERFACEID)
								: "Nrm.Nro.Activity.CrudService");
		headers.set("method",
				configurations.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_METHOD) != null
						? configurations.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_METHOD)
						: "NroActivityUpdate");
		headers.set("documenttype",
				configurations.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_DOC_TYPE) != null
						? configurations
								.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_DOC_TYPE)
						: "NroActivityUpdate");
		headers.set("documentnamespace",
				configurations
						.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_DOC_NAME_SPACE) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_DOC_NAME_SPACE)
								: "urn:Namespaces.Ericsson.Com:Nrm:Nro:Activity:CrudService:v0001");
		headers.set("partyid",
				configurations.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_PARTY_ID) != null
						? configurations
								.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_PARTY_ID)
						: "Erisite");
		headers.set("partytype",
				configurations
						.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_PARTY_TYPE) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_PARTY_TYPE)
								: "Mdm.CustomerName");
		headers.set("messageflow",
				configurations
						.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_FLOW) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_FLOW)
								: "Nro.UpdateActivity.MsgF");
		headers.set("businessentitytype",
				configurations.getStringProperty(
						ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_BUSINESS_ENTITY_TYPE) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_BUSINESS_ENTITY_TYPE)
								: "NroActivityCrudService.Activity.ObjectId");
		headers.set("MESSAGE_SERVICE_NAME",
				configurations.getStringProperty(
						ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_SERVICE_NAME) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_SERVICE_NAME)
								: "ActivityEvent");
		headers.set("MESSAGE_REQUEST_TYPE",
				configurations.getStringProperty(
						ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_REQUEST_TYPE) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_REQUEST_TYPE)
								: "UpdateOnly");
		headers.set("MESSAGE_SERVICE_TYPE",
				configurations.getStringProperty(
						ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_SERVICE_TYPE) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_MESSAGE_SERVICE_TYPE)
								: "UpdateActivity");
		headers.set("Content-Type",
				configurations
						.getStringProperty(ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_CONTENT_TYPE) != null
								? configurations.getStringProperty(
										ConfigurationFilesConstants.ERISITE_HEADERS_WOCLOSURE_CONTENT_TYPE)
								: "application/xml");
		headers.set("flowtrackingid", Integer.toString((new Random().nextInt(50000000) + 5000)));

		String plainCreds = configurations
				.getStringProperty(ConfigurationFilesConstants.ERISITE_AUTHENTICATION_USERNAME) + ":"
				+ configurations.getStringProperty(ConfigurationFilesConstants.ERISITE_AUTHENTICATION_PASSWORD);
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		headers.add("Authorization", "Basic " + base64Creds);
	}

}
