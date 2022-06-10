
package com.ericsson.isf.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.config.ApplicationConfigurations;

import com.ericsson.isf.dao.NetworkElementDao;

import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.helper.FileCSVHelper;

import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DownloadTemplateModel;

import com.ericsson.isf.model.MqttModel;
import com.ericsson.isf.model.MqttPublishModel;
import com.ericsson.isf.model.NetworkElementCountModel;
import com.ericsson.isf.model.NetworkElementDataTable;
import com.ericsson.isf.model.NetworkElementFilterModel;
import com.ericsson.isf.model.NetworkElementModel;
import com.ericsson.isf.model.NetworkElementNewModel;
import com.ericsson.isf.model.NetworkElementUploadMasterModel;
import com.ericsson.isf.model.NetworkElementValidateMasterModel;
import com.ericsson.isf.model.NetworkElemntViewModel;
import com.ericsson.isf.model.NodeFilterModel;
import com.ericsson.isf.model.RedisNotificationDataModel;
import com.ericsson.isf.model.Response;

import com.ericsson.isf.model.WebNotificationModel;

import com.ericsson.isf.model.WorkOrderNetworkElementModel;

import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AppUtil;
import com.ericsson.isf.util.ApplicationMessages;

import com.ericsson.isf.util.ConfigurationFilesConstants;

import com.ericsson.isf.util.FileUtil;
import com.ericsson.isf.util.IsfCustomIdInsert;
import com.ericsson.isf.util.NotificationUtils;

/**
 * @author ekarmuj
 *
 */

@Service
public class NetworkElementService {

	private static final String TABLENAME2 = "tablename";

	private static final String NETWORK_ELEMENT_DATA_TABLE = "NetworkElementValidation";


	private static final String YOUR_REQUEST_HAS_BEEN_EXPIRED = "Your request has been expired.";

	private static final String EXPIRED = "Expired";

	private static final String FAILED_PROCESS_MSG = "Your process has been failed due to some error!! Please try again.";

	private static final String SUCCESS = "Success";
	private static final String FILE = "file";

	private static final String NE = "NE";

	private static final String SIGNUM = "Signum";

	private static final String DATA_DELETED_SUCCESSFULLY = "Data Upload Cancelled";

	private static final String DATA_UPLOADED_SUCCESSFULLY = "Data Uploaded Successfully";

	private static final String CANCELLED = "Cancelled";

	private static final String COMPLETED = "Completed";

	private static final String CNEDB = "cnedb";

	private static final String NE_UPLOAD_ID = "NeUploadId";

	private static final String ROLE_VALIDATION_MSG = "The Given User Does not have PM/DR Role Or User is not PM/DR of the  ProjectID.";

	private static final String NETWORK_ELEMENT_MSG = "Network Element Added Successfully !!";

	private static final String VALIDATED = "Validated";

	private static final String ALREADY_INPROGRESS = " is already uploading NE against the same Country-Customer Group. Please contact ";
	private static final String WAIT_FOR_USER = " or wait for the user to complete the ongoing activity.";
	private static final String REFERENCE_MAPPING = "ReferenceMapping";
	private static final String FILE_UPLOAD_INPROGRESS = "File Upload is in Progress";
	private static final String INPROGRESS = "InProgress";
	private static final String ERROR_IN_PROC = "Error Occured!!";
	private static final String ISF_WEB = "ISF Web";
	private static final String NE_UPLOAD = "NE Upload";
	private static final String FILE_VALIDATED = "File Validated";
	private static final String NETWORK = "network";
	private static final String ROW_COUNT_EXCEED_MSG = "Maximum row limit exceeded. Allowed limit is %s.";
	private static final String INVALID_TEMPLATE = "Invalid file template. Please read 'Excel Entry Guidelines' and try again!";
	private static final String UPDATED_SUCCESSFULLY = "updated successfully!";
	private static final String ELEMENT_TYPE_MSG = "Network Elements with single Element Type can be selected only!";
	private static final String DELETED_MSG = "Deleted Successfully";
	
	private static final String TABLE_NAME ="TableName";
	private static final String NETWORK_ELEMENT_NAME="NetworkElementName";
	private static final String NETWORK_ELEMENT_ID="NetworkElementID";
	private static final String STATUS="Status";
	private static final String GROUP="Group";
	private static final String VALID="Valid";
	
	private static final Logger LOG = LoggerFactory.getLogger(NetworkElementService.class);

	private static final String FAILED = "Failed";
	private static final String PROVIDE_PROJECTID = "Please provide Project ID!";



	@Autowired /* Bind to bean/pojo */
	private NetworkElementDao networkElementDao;

	@Autowired
	private ValidationUtilityService validationUtilityService;

	@Autowired
	private ExternalInterfaceManagmentService externalInterfaceManagmentService;

	@Autowired
	private AppService appService;

	@Autowired
	private FlowChartService flowChartService;

	@Autowired
	private IsfCustomIdInsert isfCustomIdInsert;

	@Autowired
	private ApplicationConfigurations configurations;

	@Autowired
	private NotificationUtils notificationUtils;

	@Autowired
	private RedisService redisService;

	@Autowired
	private OutlookAndEmailService emailService;

	

	public ResponseEntity<Response<NetworkElements>> getNetworkElementDetailsv1(int projectID,
			DataTableRequest dataTableReq, int recordsTotal) {
		Response<NetworkElements> response = new Response<>();
		NetworkElements networkElements = new NetworkElements();
		List<NetworkElementModel> listOfNetworkElements = new ArrayList<>();
		try {
			LOG.info("getNetworkElementDetailsv1:Start");

			listOfNetworkElements = networkElementDao.getNetworkElementDetailsv1(projectID, dataTableReq, recordsTotal);
			networkElements.setData(listOfNetworkElements);
			networkElements.setDraw(dataTableReq.getDraw());

			if (CollectionUtils.isNotEmpty(listOfNetworkElements)) {

				networkElements.setRecordsTotal(listOfNetworkElements.get(0).getRecordsTotal());
				networkElements.setRecordsFiltered(listOfNetworkElements.get(0).getRecordsTotal());

			} else {
				networkElements.setRecordsTotal(0);
				networkElements.setRecordsFiltered(0);
				networkElements.setData(listOfNetworkElements);
				networkElements.setDraw(dataTableReq.getDraw());
			}

			response.setResponseData(networkElements);
			LOG.info("getNetworkElementDetailsv1:End");
		} catch (ApplicationException exe) {
			response.addFormError(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			response.addFormError(ex.getMessage());
			response.setValidationFailed(true);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response<NetworkElements>> getNetworkElementDetailsv2(int projectID,
			DataTableRequest dataTableReq) {
		Response<NetworkElements> response = new Response<>();
		NetworkElements networkElements = new NetworkElements();
		List<NetworkElementModel> listOfNetworkElements = new ArrayList<>();
		try {
			LOG.info("getNetworkElementDetailsv1:Start");

			listOfNetworkElements = networkElementDao.getNetworkElementDetailsv2(projectID, dataTableReq);
			networkElements.setData(listOfNetworkElements);
			networkElements.setDraw(dataTableReq.getDraw());

			if (CollectionUtils.isNotEmpty(listOfNetworkElements)) {

				networkElements.setRecordsTotal(listOfNetworkElements.get(0).getRecordsTotal());
				networkElements.setRecordsFiltered(listOfNetworkElements.get(0).getRecordsTotal());

			} else {
				networkElements.setRecordsTotal(0);
				networkElements.setRecordsFiltered(0);
				networkElements.setData(listOfNetworkElements);
				networkElements.setDraw(dataTableReq.getDraw());
			}

			response.setResponseData(networkElements);
			LOG.info("getNetworkElementDetailsv1:End");
		} catch (ApplicationException exe) {
			response.addFormError(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			response.addFormError(ex.getMessage());
			response.setValidationFailed(true);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Transactional("transactionManager")
	public ResponseEntity<Response<String>> appendNetworkElement(NetworkElementNewModel elements) {

		Response<String> response = new Response<>();
		String tableName = StringUtils.EMPTY;
		NetworkElementUploadMasterModel networkElementUploadMasterModel = new NetworkElementUploadMasterModel();

		try {
			LOG.info("appendNetworkElement:Start");
			boolean checkifRoleExist = roleValidation(elements);
			if (checkifRoleExist) {
				validationAfterRoleExist(elements);

				prepareNetworkElementModel(elements);

				multipleFieldValidation(elements);

				ResponseEntity<Response<String>> uploadStatus = this
						.checkStatusofNEUploadForUser(elements.getProjectID());

				if (uploadStatus.getBody().getResponseData() == null) {
					// Insert new role in master table
					
					int countryCustomerGroupID = networkElementDao
							.getCountryCustomerGroupIDByProjectID(elements.getProjectID());

					networkElementUploadMasterModel.setCountryCustomerGroupID(countryCustomerGroupID);
					networkElementUploadMasterModel.setUserUploaded(elements.getUploadedBy());
					networkElementUploadMasterModel.setUploadStatus(INPROGRESS);
					networkElementUploadMasterModel.setProjectID(elements.getProjectID());

					networkElementDao.insertStatusInUploadMasterTable(networkElementUploadMasterModel,
							configurations.getIntegerProperty(ConfigurationFilesConstants.CNEDB_EXPIRY_TIMEL),
							"AppendAPI.txt");

					int neUploadID = isfCustomIdInsert
							.generateCustomId(networkElementUploadMasterModel.getNeUploadID());
					networkElementUploadMasterModel.setNeUploadID(neUploadID);

					// tableName : file_NE_<Signum>_<NEUploadID>
					StringBuilder strbuilder = new StringBuilder(StringUtils.EMPTY);
					strbuilder.append(FILE).append(AppConstants.UNDERSCORE).append(NE).append(AppConstants.UNDERSCORE)
							.append(elements.getUploadedBy()).append(AppConstants.UNDERSCORE).append(neUploadID);
					tableName = strbuilder.toString();

					StringBuilder domainSubDomain = new StringBuilder(StringUtils.EMPTY);
					domainSubDomain.append(elements.getDomain()).append(AppConstants.FORWARD_SLASH)
							.append(elements.getSubDomain());
					elements.setDomainSubDomainName(domainSubDomain.toString());

					networkElementDao.insertIntoTempTable(elements, tableName);

					// Validation SP to be called here
					this.networkElementDao.validateNetworkElementFile(tableName, networkElementUploadMasterModel,
							elements.getProjectID());
					
					String messageDetails = networkElementDao.getValidationMessage(tableName);

					String procStatus = networkElementDao.migrateDataTempToMaster(elements.getUploadedBy(), neUploadID,
							elements.getProjectID(), COMPLETED, countryCustomerGroupID, tableName);

					if (StringUtils.equalsIgnoreCase(SUCCESS, procStatus)) {
						
						String success = networkElementDao.checkMigrationSuccess(neUploadID);
						
						if (StringUtils.equalsIgnoreCase(SUCCESS, success)) {
							response.setResponseData(NETWORK_ELEMENT_MSG);
							response.setValidationFailed(false);							
						}else {
							response.setResponseData(messageDetails);
							response.setValidationFailed(false);							
						}
						
					} else if (StringUtils.equalsIgnoreCase(FAILED, procStatus)) {
						response.setResponseData(ERROR_IN_PROC);
						response.setValidationFailed(false);
					}
				} else {
					response.setResponseData(uploadStatus.getBody().getResponseData());
					response.setValidationFailed(false);

				}
			} else {
				response.addFormError(ROLE_VALIDATION_MSG);
				response.setValidationFailed(true);

			}
			LOG.info("appendNetworkElement:End");

		} catch (ApplicationException exe) {
			response.addFormMessage(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.addFormError(e.getMessage());
			response.setValidationFailed(true);
			String currentUploadStatus = FAILED;
			networkElementDao.updateStatusInMasterTable(networkElementUploadMasterModel.getNeUploadID(), currentUploadStatus,
					networkElementUploadMasterModel.getUserUploaded(), tableName);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void prepareNetworkElementModel(NetworkElementNewModel elements) {
		elements.setUploadedBy(StringUtils.trim(elements.getUploadedBy()));
		elements.setDomain(StringUtils.trim(elements.getDomain()));
		elements.setSubDomain(StringUtils.trim(elements.getSubDomain()));
		elements.setTechnology(StringUtils.trim(elements.getTechnology()));
		elements.setVendor(StringUtils.trim(elements.getVendor()));
		elements.setElementType(StringUtils.trim(elements.getElementType()));
		elements.setType(StringUtils.trim(elements.getType()));
		elements.setMarket(StringUtils.trim(elements.getMarket()));
		elements.setName(StringUtils.trim(elements.getName()));
	}

	private void multipleFieldValidation(NetworkElementNewModel elements) {
		if (StringUtils.isNotEmpty(elements.getLongitude())) {
			elements.setLongitude(StringUtils.trim(elements.getLongitude()));
		}
		if (StringUtils.isNotEmpty(elements.getLatitude())) {
			elements.setLatitude(StringUtils.trim(elements.getLatitude()));
		}
	}

	private boolean roleValidation(NetworkElementNewModel elements) {
		validationUtilityService.validateStringForBlank(elements.getUploadedBy(), AppConstants.UPLOADED_BY);

		validationUtilityService.validateSignumForExistingEmployee(StringUtils.trim(elements.getUploadedBy()));
		validationUtilityService.validateIntForZero(elements.getProjectID(), AppConstants.PROJECT_ID);
		validationUtilityService.validateProjectId(elements.getProjectID());

		// check Role for PM and DR
		return networkElementDao.checkIFRoleExists(StringUtils.trim(elements.getUploadedBy()), elements.getProjectID());
	}

	private void validationAfterRoleExist(NetworkElementNewModel elements) {
		// Validation
		externalInterfaceManagmentService.validateAppendNetworkElement(elements,
				"/externalInterface/appendNetworkElement");
		
		if(elements.getLongitude()!= null && elements.getLongitude().length() == 0) {
			elements.setLongitude(null);
		}
		if(elements.getLatitude()!= null && elements.getLatitude().length() == 0) {
			elements.setLatitude(null);
		}

		if (StringUtils.isNotEmpty(elements.getLongitude()) && elements.getLongitude().length() > 256) {

			throw new ApplicationException(200, "Longitude" + AppConstants.LONGITUDE_LENGTH);
		}

		if (StringUtils.isNotEmpty(elements.getLatitude()) && elements.getLatitude().length() > 256) {

			throw new ApplicationException(200, "Latitude" + AppConstants.LONGITUDE_LENGTH);
		}

		validateDomainSubDomainCombination(StringUtils.trim(elements.getDomain()),
				StringUtils.trim(elements.getSubDomain()));

	}

	private void validateDuplicateNE(NetworkElementNewModel elements) {
		if (networkElementDao.validateDuplicateNE(elements)) {
			throw new ApplicationException(200, "Data is already present for given Combination !!");
		}

	}

	private void validateDomainSubDomainCombination(String domain, String subDomain) {
		if (!networkElementDao.checkDomainSubDomainCombination(domain, subDomain)) {
			throw new ApplicationException(200, "The Combination of Domain and SubDomain is not correct.");
		}

	}

	public DownloadTemplateModel downloadNetworkElementv1(int projectID, HttpServletResponse response)
			throws IOException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = "NetworkElement" + "-" + (new SimpleDateFormat(AppConstants.DATE_FORMAT).format(new Date()))
				+ AppConstants.XLSX;

		List<Map<String, Object>> networkElement = networkElementDao.downloadNetworkElementv1(projectID);

		byte[] fData = null;
		if (CollectionUtils.isNotEmpty(networkElement)) {
			fData = FileUtil.generateXlsFile(networkElement);

		}

		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);
		return downloadTemplateModel;
	}

	public DownloadTemplateModel downloadReferenceMappingFile() throws IOException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = REFERENCE_MAPPING + AppConstants.CHAR_HYPHEN
				+ (new SimpleDateFormat(AppConstants.DATE_FORMAT).format(new Date())) + AppConstants.XLSX;

		List<Map<String, Object>> referenceMapping = networkElementDao.downloadReferenceMappingFile();

		byte[] fData = null;
		if (CollectionUtils.isNotEmpty(referenceMapping)) {
			fData = FileUtil.generateXlsFile(referenceMapping);

		}

		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);
		return downloadTemplateModel;
	}

	public ResponseEntity<Response<MqttModel>> getUploadedFileStatus(int referenceId) {
		Response<MqttModel> result = new Response<>();
		try {
			LOG.info("getUploadedFileStatus:Start");
			validationUtilityService.validateIntForZero(referenceId, AppConstants.REFERENCE_ID);
			MqttModel mqttModel = networkElementDao.getUploadedFileStatus(referenceId, true);

			if (mqttModel == null) {
				result.addFormMessage(AppConstants.NO_DATA_FOUND);
				result.setResponseData(mqttModel);
			} else {
				mqttModel.setSource(NE);
				mqttModel.setNeUploadedId(referenceId);
				result.setResponseData(mqttModel);
			}
			LOG.info("getUploadedFileStatus:End");
		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	public ResponseEntity<Response<String>> checkStatusofNEUploadForUser(int projectID) {

		Response<String> response = new Response<>();
		try {
			validationUtilityService.validateIntForZero(projectID, AppConstants.PROJECT_ID);
			NetworkElementUploadMasterModel checkStatus = networkElementDao.checkStatusOfUserForNEUpload(projectID);
			if (checkStatus != null
					&& (StringUtils.equalsIgnoreCase(checkStatus.getUploadStatus(), AppConstants.INPROGRESS)
							|| StringUtils.equalsIgnoreCase(checkStatus.getUploadStatus(), VALIDATED))) {

				StringBuilder msg = new StringBuilder(StringUtils.EMPTY);
				msg = msg.append(checkStatus.getEmployeeName()).append(StringUtils.SPACE)
						.append(AppConstants.OPENING_BRACES).append(checkStatus.getUserUploaded())
						.append(AppConstants.CLOSING_BRACES).append(ALREADY_INPROGRESS)
						.append(checkStatus.getUserUploaded()).append(WAIT_FOR_USER);
				response.setResponseData(msg.toString());

			}

		} catch (ApplicationException exe) {
			response.addFormMessage(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.addFormError(e.getMessage());
			response.setValidationFailed(true);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public List<String> uploadSucessFailureExcel(NetworkElementUploadMasterModel networkElementUploadMasterModel,
			String fileTable, String currentUploadStatus) {
		validationUtilityService.validateIntForZero(networkElementUploadMasterModel.getNeUploadID(), NE_UPLOAD_ID);
		StringBuilder fileName = new StringBuilder(StringUtils.EMPTY);
		fileName.append(CNEDB);
		fileName.append(AppConstants.FORWARD_SLASH);
		fileName.append(networkElementUploadMasterModel.getUserUploaded());
		fileName.append(AppConstants.FORWARD_SLASH);
		String folderStructure = fileName.toString();

		List<String> list = new LinkedList<>();

		fileName = new StringBuilder(StringUtils.EMPTY);
		fileName.append(networkElementUploadMasterModel.getUserUploaded());
		fileName.append(AppConstants.DASH);
		fileName.append(networkElementUploadMasterModel.getNeUploadID());
		fileName.append(AppConstants.DASH);
		fileName.append(AppConstants.SUCCESS);
		fileName.append(AppConstants.XLSX);
		String successFileName = fileName.toString();

		fileName = new StringBuilder(StringUtils.EMPTY);
		fileName.append(networkElementUploadMasterModel.getUserUploaded());
		fileName.append(AppConstants.DASH);
		fileName.append(networkElementUploadMasterModel.getNeUploadID());
		fileName.append(AppConstants.DASH);
		fileName.append(AppConstants.FAILURE);
		fileName.append(AppConstants.XLSX);
		String failureFileName = fileName.toString();

		try {
			List<Map<String, Object>> successfile = networkElementDao.downloadSuccessFile(fileTable);
			List<Map<String, Object>> failureFile = networkElementDao.downloadFailureFile(fileTable);

			byte[] successFileData = null;
			byte[] failureFileData = null;
			String successLink = StringUtils.EMPTY;
			String failureLink = StringUtils.EMPTY;
			URI uri = null;
			if (CollectionUtils.isNotEmpty(successfile)) {
				successFileData = FileUtil.generateXlsFileCnedb(successfile, AppConstants.SUCCESS);
				uri = externalInterfaceManagmentService.uploadFileInCommonContainerFromByteArray(successFileData,
						folderStructure, successFileName);
				successLink = uri.toString();
				list.add(successLink);
			}

			if (CollectionUtils.isNotEmpty(failureFile)) {
				failureFileData = FileUtil.generateXlsFileCnedb(failureFile, AppConstants.FAILURE);
				uri = externalInterfaceManagmentService.uploadFileInCommonContainerFromByteArray(failureFileData,
						folderStructure, failureFileName);
				failureLink = uri.toString();
				list.add(failureLink);
			}

			networkElementDao.updateLinkInNeUploadMaster(networkElementUploadMasterModel.getNeUploadID(), successLink,
					failureLink);
			networkElementUploadMasterModel.setActive(true);
			callMqttClientAndPrepareModel(networkElementUploadMasterModel, currentUploadStatus);
		} catch (IOException ex) {
			LOG.error(ex.getMessage());
			failedCaseCnedb(networkElementUploadMasterModel, fileTable);

		}

		return list;
	}

	@Transactional("transactionManager")
	public ResponseEntity<Response<String>> validateNetworkElementFile(int projectID, String uploadedON,
			String uploadedBy, MultipartFile file) {

		Response<String> response = new Response<>();
		NetworkElementUploadMasterModel networkElementUploadMasterModel = new NetworkElementUploadMasterModel();
		String fileName = StringUtils.EMPTY;
		try {

			validationUtilityService.validateStringForBlank(uploadedBy, AppConstants.UPLOADED_BY);
			validationUtilityService.validateSignumForExistingEmployee(uploadedBy);
			validationUtilityService.validateIntForZero(projectID, AppConstants.PROJECT_ID);
			validationUtilityService.validateProjectId(projectID);

			NetworkElementUploadMasterModel checkStatus = networkElementDao.checkStatusOfUserForNEUpload(projectID);
			if (checkStatus != null
					&& (StringUtils.equalsIgnoreCase(checkStatus.getUploadStatus(), AppConstants.INPROGRESS)
							|| StringUtils.equalsIgnoreCase(checkStatus.getUploadStatus(), VALIDATED))) {

				StringBuilder msg = new StringBuilder(StringUtils.EMPTY);
				msg = msg.append(checkStatus.getEmployeeName()).append(StringUtils.SPACE)
						.append(AppConstants.OPENING_BRACES).append(checkStatus.getUserUploaded())
						.append(AppConstants.CLOSING_BRACES).append(ALREADY_INPROGRESS)
						.append(checkStatus.getUserUploaded()).append(WAIT_FOR_USER);

				response.addFormMessage(msg.toString());
				return new ResponseEntity<>(response, HttpStatus.OK);

			}

			int countryCustomerGroupID = networkElementDao.getCountryCustomerGroupIDByProjectID(projectID);

			networkElementUploadMasterModel.setCountryCustomerGroupID(countryCustomerGroupID);
			networkElementUploadMasterModel.setUserUploaded(uploadedBy);
			networkElementUploadMasterModel.setUploadStatus(INPROGRESS);
			networkElementUploadMasterModel.setProjectID(projectID);
			networkElementDao.insertStatusInUploadMasterTable(networkElementUploadMasterModel,
					configurations.getIntegerProperty(ConfigurationFilesConstants.CNEDB_EXPIRY_TIMEL),
					file.getOriginalFilename());

			int neUploadID = isfCustomIdInsert.generateCustomId(networkElementUploadMasterModel.getNeUploadID());
			networkElementUploadMasterModel.setNeUploadID(neUploadID);
			String currentUploadStatus = INPROGRESS;

			StringBuilder strbuilder = new StringBuilder(StringUtils.EMPTY);
			strbuilder.append(file.getName()).append(AppConstants.UNDERSCORE).append(NE).append(AppConstants.UNDERSCORE)
					.append(uploadedBy).append(AppConstants.UNDERSCORE).append(neUploadID);
			fileName = strbuilder.toString();

			String relativeFilePath = uploadedBy + AppConstants.FORWARD_SLASH + neUploadID;

			flowChartService.uploadExcelFile(file, relativeFilePath, fileName);

			StringBuilder strfilePath = new StringBuilder(StringUtils.EMPTY);
			strfilePath.append(appService.getConfigUploadFilePath()).append(AppConstants.FORWARD_SLASH)
					.append(relativeFilePath).append(AppConstants.FORWARD_SLASH).append(fileName);
			String filePath = strfilePath.toString();

			StringBuilder strCsvPath = new StringBuilder(StringUtils.EMPTY);
			strCsvPath.append(appService.getConfigUploadFilePath()).append(relativeFilePath)
					.append(AppConstants.FORWARD_SLASH).append(AppUtil.getFileNameWithTimestamp(NETWORK));

			String csvFilePath = strCsvPath.toString();
			
			Integer uploadLength=configurations.getIntegerProperty(ConfigurationFilesConstants.NETWORK_ELEMENT_UPLOAD_LENGTH);
			boolean rowcount = checkRowCountLimit(filePath, uploadLength);
			if (rowcount) {
				deleteStatusInUploadMasterTable(neUploadID, fileName);
				response.addFormError(String.format(ROW_COUNT_EXCEED_MSG, configurations.getIntegerProperty(ConfigurationFilesConstants.NETWORK_ELEMENT_UPLOAD_LENGTH)));
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

			AppUtil.convertCnedbExcelToCSV(filePath, csvFilePath);
			LOG.info("csv path is ---> {}", csvFilePath);

			Map<String, String> validationResult = FileCSVHelper.validateCNEDBCsvFile(csvFilePath);
			LOG.info("validation result is -----------> "+validationResult);
			if (validationResult != null && !validationResult.isEmpty()) {
				LOG.info("inside validation result if is -----------> "+neUploadID+" filename "+fileName);
				for (Map.Entry<String, String> entry : validationResult.entrySet()) {
					String key = entry.getKey();
					response.addFormError(validationResult.get(key));

				}
				deleteStatusInUploadMasterTable(neUploadID, fileName);

				return new ResponseEntity<>(response, HttpStatus.OK);
			}

			boolean isBulkUploadSuccess = appService.CsvBulkUploadNewGenWithSeperator(csvFilePath,
					AppConstants.CNEDB_UPLOAD_TEMPLATE_TABLE_NAME, fileName, AppConstants.CSV_CHAR_DOUBLE_SEMICOLON);
			LOG.info("isBulkUploadSuccess is ---> {}", isBulkUploadSuccess);
			if (!isBulkUploadSuccess) {
				deleteStatusInUploadMasterTable(neUploadID, fileName);
				response.addFormError(INVALID_TEMPLATE);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			WebNotificationModel webNotificationModel = prepareWebNotificationModel(uploadedBy, neUploadID,
					currentUploadStatus);

			networkElementDao.insertStatusInWebNotificationTable(webNotificationModel);
			networkElementUploadMasterModel.setActive(true);
			callMqttClientAndPrepareModel(networkElementUploadMasterModel, currentUploadStatus);
			// add redis implementation
			storeDataInRedis(file.getOriginalFilename(), neUploadID, uploadedBy);
			executeNextProcess(networkElementUploadMasterModel, isBulkUploadSuccess, fileName, projectID);
			response.setResponseData(FILE_UPLOAD_INPROGRESS);

		} catch (ApplicationException exe) {
			response.addFormMessage(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.addFormError(e.getMessage());
			response.setValidationFailed(true);
			failedCaseCnedb(networkElementUploadMasterModel, fileName);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	private Boolean checkRowCountLimit(String excelfileName, Integer uploadLength) throws IOException {
		XSSFWorkbook wBook = new XSSFWorkbook(new FileInputStream(excelfileName));
		boolean isrowExceed = false;
		for (int i = 0; i < wBook.getNumberOfSheets(); i++) {
			// Get first sheet from the workbook
			XSSFSheet sheet = wBook.getSheetAt(i);

			int rowTotal = sheet.getLastRowNum() + 1;
			if (rowTotal > uploadLength+1) {
				isrowExceed = true;
			}

		}
		return isrowExceed;
	}

	private void storeDataInRedis(String filename, int neUploadID, String uploadedBy) {
		RedisNotificationDataModel redisNotification = new RedisNotificationDataModel();
		redisNotification.setNeUploadedId(neUploadID);
		redisNotification.setFileName(filename);
		redisService.setValueBasedOnKey(notificationUtils.createKey(uploadedBy), redisNotification);
	}

	private void deleteStatusInUploadMasterTable(int neUploadID, String fileName) {
		networkElementDao.deleteStatusInUploadMasterTable(neUploadID, fileName);

	}

	private WebNotificationModel prepareWebNotificationModel(String uploadedBy, int neUploadID,
			String currentUploadStatus) {
		WebNotificationModel webNotificationModel = new WebNotificationModel();
		webNotificationModel.setAuditComments(currentUploadStatus);
		webNotificationModel.setNotificationSource(ISF_WEB);
		webNotificationModel.setWoid(0);
		webNotificationModel.setCreatedBy(uploadedBy);
		webNotificationModel.setReferenceId(neUploadID);
		webNotificationModel.setModule(NE_UPLOAD);
		return webNotificationModel;
	}

	private void executeNextProcess(NetworkElementUploadMasterModel networkElementUploadMasterModel,
			boolean isBulkUploadSuccess, String fileName, int projectID) {
		Runnable task2 = () -> executeNextProcessCnedb(networkElementUploadMasterModel, isBulkUploadSuccess, fileName,
				projectID);
		// start the thread
		new Thread(task2).start();

	}

	public void executeNextProcessCnedb(NetworkElementUploadMasterModel networkElementUploadMasterModel,
			boolean isBulkUploadSuccess, String fileName, int projectID) {
		LOG.info("second Thread calling");
		Response<String> response = new Response<>();
		try {
			if (isBulkUploadSuccess) {

				// Validation SP to be called here
				this.networkElementDao.validateNetworkElementFile(fileName, networkElementUploadMasterModel, projectID);
				String currentUploadStatus = VALIDATED;
				WebNotificationModel webNotificationModel = prepareWebNotificationModel(
						networkElementUploadMasterModel.getUserUploaded(),
						networkElementUploadMasterModel.getNeUploadID(), currentUploadStatus);

				updateInsertInWebNotificationTable(webNotificationModel);

				uploadSucessFailureExcel(networkElementUploadMasterModel, fileName, currentUploadStatus);
			} else {
				response.addFormError(ApplicationMessages.ERROR_BCP_FILE_UPLOAD);
			}
			response.addFormMessage(FILE_VALIDATED);

		}

		catch (Exception e) {
			failedCaseCnedb(networkElementUploadMasterModel, fileName);
			LOG.info("Got an exception. {}", e.getMessage());

		}

	}

	private void failedCaseCnedb(NetworkElementUploadMasterModel networkElementUploadMasterModel, String fileName) {
		String currentUploadStatus = FAILED;
		networkElementDao.updateStatusInMasterTable(networkElementUploadMasterModel.getNeUploadID(),
				currentUploadStatus, networkElementUploadMasterModel.getUserUploaded(), fileName);
		WebNotificationModel webNotificationModel = prepareWebNotificationModel(
				networkElementUploadMasterModel.getUserUploaded(), networkElementUploadMasterModel.getNeUploadID(),
				currentUploadStatus);
		updateInsertInWebNotificationTable(webNotificationModel);
		MqttPublishModel mqttPulisherModel = new MqttPublishModel();
		mqttPulisherModel.setMessage(FAILED_PROCESS_MSG);
		MqttModel mqttModel = networkElementDao
				.getPreviousUploadStatusCnedb(networkElementUploadMasterModel.getNeUploadID());

		mqttModel.setNeUploadedId(networkElementUploadMasterModel.getNeUploadID());
		mqttModel.setCurrentStatus(currentUploadStatus);

		prepareModelForMqttPublishModel(mqttPulisherModel, mqttModel);
		callMqttClient(mqttPulisherModel);

	}

	private void updateInsertInWebNotificationTable(WebNotificationModel webNotificationModel) {
		networkElementDao.updatePreviousStatusInWebNotificationTable(webNotificationModel);
		networkElementDao.insertStatusInWebNotificationTable(webNotificationModel);
	}

	@Transactional("transactionManager")
	public ResponseEntity<Response<String>> saveDeleteNeUploadFinalData(String signum, int projectID, String status,
			int neuploadId) {
		Response<String> response = new Response<>();
		NetworkElementUploadMasterModel networkElementUploadMasterModel = new NetworkElementUploadMasterModel();
		networkElementUploadMasterModel.setUserUploaded(signum);
		networkElementUploadMasterModel.setNeUploadID(neuploadId);

		StringBuilder strbuilder = new StringBuilder(StringUtils.EMPTY);
		strbuilder.append(FILE).append(AppConstants.UNDERSCORE).append(NE).append(AppConstants.UNDERSCORE)
				.append(signum).append(AppConstants.UNDERSCORE).append(neuploadId);
		String tableName = strbuilder.toString();
		try {
			MqttModel model = networkElementDao.getPreviousUploadStatusCnedb(neuploadId);
			if (StringUtils.equalsIgnoreCase(model.getCurrentStatus(), EXPIRED)) {
				response.addFormError(YOUR_REQUEST_HAS_BEEN_EXPIRED);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			if (StringUtils.equalsIgnoreCase(model.getCurrentStatus(), COMPLETED)) {
				response.addFormError("Invalid request!!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			if (StringUtils.equalsIgnoreCase(model.getCurrentStatus(), CANCELLED)) {
				response.addFormError("Invalid request!!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			if (StringUtils.equalsIgnoreCase(COMPLETED, status)) {
				int countryCustomerGroupID = networkElementDao.getCountryCustomerGroupIDByProjectID(projectID);
				// call stored procedure to migrate data from temp table to master table
				String procStatus = networkElementDao.migrateDataTempToMaster(signum, neuploadId, projectID, status,
						countryCustomerGroupID, tableName);
				if (StringUtils.equalsIgnoreCase(SUCCESS, procStatus)) {

					String currentUploadStatus = COMPLETED;
					WebNotificationModel webNotificationModel = prepareWebNotificationModel(signum, neuploadId,
							currentUploadStatus);
					updateInsertInWebNotificationTable(webNotificationModel);
					networkElementUploadMasterModel.setActive(false);
					
					callMqttClientAndPrepareModel(networkElementUploadMasterModel, currentUploadStatus);
					if (configurations.getBooleanProperty(ConfigurationFilesConstants.NETWORK_ELEMENT_MAIL_ENABLED)) {
						sendNetworkElementNotificationMail(projectID,countryCustomerGroupID, signum, model.getSuccessLink(),
								model.getSuccessCount(), model.getStartTime());
					}

					response.setResponseData(DATA_UPLOADED_SUCCESSFULLY);

				} else if (StringUtils.equalsIgnoreCase(FAILED, procStatus)) {
					failedCaseCnedb(networkElementUploadMasterModel, tableName);
					response.addFormError(ERROR_IN_PROC);
				}
			} else if (StringUtils.equalsIgnoreCase(CANCELLED, status)) {
				int countryCustomerGroupID = networkElementDao.getCountryCustomerGroupIDByProjectID(projectID);
				// call stored procedure to delete temp table
				String procStatus = networkElementDao.migrateDataTempToMaster(signum, neuploadId, projectID, status,
						countryCustomerGroupID, tableName);
				LOG.info("inside else if . migrate Data Temp to Master status is:::::::::::: "+procStatus);
				if (StringUtils.equalsIgnoreCase(SUCCESS, procStatus)) {
					String currentUploadStatus = CANCELLED;
					WebNotificationModel webNotificationModel = prepareWebNotificationModel(signum, neuploadId,
							currentUploadStatus);
					updateInsertInWebNotificationTable(webNotificationModel);
					networkElementUploadMasterModel.setActive(false);
					LOG.info("inside if . in success case status is:::::::::::: "+currentUploadStatus);
					callMqttClientAndPrepareModel(networkElementUploadMasterModel, currentUploadStatus);
					response.setResponseData(DATA_DELETED_SUCCESSFULLY);
				} else if (StringUtils.equalsIgnoreCase(FAILED, procStatus)) {
					failedCaseCnedb(networkElementUploadMasterModel, tableName);
					response.addFormError(ERROR_IN_PROC);
				}
			}
		} catch (ApplicationException exe) {
			response.addFormMessage(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.addFormError(e.getMessage());
			response.setValidationFailed(true);
			failedCaseCnedb(networkElementUploadMasterModel, tableName);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	private void callMqttClientAndPrepareModel(NetworkElementUploadMasterModel networkElementUploadMasterModel,
			String currentUploadStatus) {
		LOG.info("inside callMqttClientAndPrepareModel method::::::::::::::::::::::::::: ");
		LOG.info("current Upload Status is::::::::::::::::::::::::::: "+currentUploadStatus);
		MqttModel mqttModel = networkElementDao.getUploadedFileStatus(networkElementUploadMasterModel.getNeUploadID(),
				networkElementUploadMasterModel.isActive());

		mqttModel.setNeUploadedId(networkElementUploadMasterModel.getNeUploadID());
		mqttModel.setCurrentStatus(currentUploadStatus);
		MqttPublishModel mqttPulisherModel = new MqttPublishModel();
		LOG.info("inside callMqttClientAndPrepareModel status is:::::::::::: "+currentUploadStatus);
		prepareModelForMqttPublishModel(mqttPulisherModel, mqttModel);
		callMqttClient(mqttPulisherModel);
	}

	private MqttPublishModel prepareModelForMqttPublishModel(MqttPublishModel mqttPublishModel, MqttModel mqttModel) {
		if (!StringUtils.isEmpty(mqttModel.getSuccessLink())) {
			mqttModel.setSuccessLink(mqttModel.getSuccessLink());
		}
		if (!StringUtils.isEmpty(mqttModel.getFailureLink())) {
			mqttModel.setFailureLink(mqttModel.getFailureLink());
		}
		mqttModel.setExpiryTime(mqttModel.getExpiryTime());
		mqttPublishModel.setSignum(mqttModel.getSignum());
		mqttModel.setSource(NE);
		mqttModel.setNeUploadedId(mqttModel.getNeUploadedId());
		mqttPublishModel.setMessage(mqttModel);
		mqttPublishModel.setQos(1);
		//mqttPublishModel.setEnvName(configurations.getEnvironment());
		mqttPublishModel.setEnvName(configurations.getStringProperty(ConfigurationFilesConstants.ENV_NAME));
		return mqttPublishModel;
	}

	private String callMqttClient(MqttPublishModel mqttPulisherModel) {
		RestTemplate restTemplate = new RestTemplate();
		String url = configurations.getStringProperty(ConfigurationFilesConstants.MQTT_APPLICATION_URL)
				+ AppConstants.CALL_MQTT_API;
		String res = StringUtils.EMPTY;
		LOG.info("MQTT url called============:{}", url);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		LOG.info("MQTT payload is::::::::::::::::::::::::::============:{}", mqttPulisherModel.toString());
		headers.add(SIGNUM, mqttPulisherModel.getSignum());
		HttpEntity<MqttPublishModel> request = new HttpEntity<>(mqttPulisherModel, headers);

		try {
			res = restTemplate.postForObject(url, request, String.class);
			LOG.info("Response from MQTT is::::->{}", res);
		} catch (RestClientException ex) {
			LOG.debug(String.format("Exception thrown  %s", ex.getMessage()));
			res = ex.getMessage();
		}
		return res;
	}

	// used to send network element notification mail based on countryCustomerId and
	// uploadedBy.
	private Map<String, Object> sendNetworkElementNotificationMail(int projectID, int countryCustomerGroupID, String uploadedBy,
			String filePath, int successCount, String startTime) {
		Set<String> projectManagerSet = networkElementDao.getAllPmBasedOnCountryCustomerGroupId(countryCustomerGroupID);
		Set<String> deliveryResponsibleSet = networkElementDao
				.getAllDrBasedOnCountryCustomerGroupId(countryCustomerGroupID);
		HashMap<String,Object> countryCustomer = this.networkElementDao.getCountryCustomerByID(countryCustomerGroupID,projectID);
		
		deliveryResponsibleSet.removeAll(projectManagerSet);
		projectManagerSet.stream().filter(Objects::nonNull).collect(Collectors.toSet());
		deliveryResponsibleSet.stream().filter(Objects::nonNull).collect(Collectors.toSet());
		Map<String, Object> placeHolder = emailService.enrichMailforNetworkElemnt(uploadedBy, successCount, startTime,
				projectManagerSet, deliveryResponsibleSet,countryCustomer);
		emailService.sendMailWithAttachment(AppConstants.NETWORK_ELEMENT_UPLOAD, placeHolder, filePath);
		return placeHolder;
	}

	public ResponseEntity<Response<List<String>>> getNetworkElementNameByTerm(NodeFilterModel nodeFilterModel) {
		Response<List<String>> result = new Response<>();
		try {
			LOG.info("getNetworkElementNameByTerm:Start");
			validationUtilityService.validateIntForZero(nodeFilterModel.getProjectID(), AppConstants.PROJECT_ID_2);
			String techCommaSeparated = AppUtil
					.convertListToCommaSeparatedString(nodeFilterModel.getTechnologyIdList());
			String domainCommaSeparated = AppUtil.convertListToCommaSeparatedString(nodeFilterModel.getDomainIdList());
			List<String> networkElement = this.networkElementDao.getNetworkElementNameByTerm(
					nodeFilterModel.getProjectID(), techCommaSeparated, domainCommaSeparated, nodeFilterModel.getTerm(),
					nodeFilterModel.getCount());

			if (networkElement.isEmpty()) {
				result.addFormMessage(AppConstants.NO_DATA_FOUND);
				result.setResponseData(networkElement);
			} else {
				result.setResponseData(networkElement);
			}
			LOG.info("getNetworkElementNameByTerm:End");
		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	public ResponseEntity<Response<NetworkElementDataTable>> viewNetworkElementDetails(String tablename, String status,
			DataTableRequest dataTableRequest) {
		Response<NetworkElementDataTable> response = new Response<>();
		NetworkElementDataTable networkElements = new NetworkElementDataTable();
		List<NetworkElemntViewModel> listOfNetworkElements = new ArrayList<>();
		try {
			LOG.info("viewNetworkElementDetails:Start");

			listOfNetworkElements = networkElementDao.viewNetworkElementDetails(tablename, status, dataTableRequest);
			networkElements.setData(listOfNetworkElements);
			networkElements.setDraw(dataTableRequest.getDraw());

			if (CollectionUtils.isNotEmpty(listOfNetworkElements)) {

				networkElements.setRecordsTotal(listOfNetworkElements.get(0).getRecordsTotal());
				networkElements.setRecordsFiltered(listOfNetworkElements.get(0).getRecordsTotal());

			} else {
				networkElements.setRecordsTotal(0);
				networkElements.setRecordsFiltered(0);
				networkElements.setData(listOfNetworkElements);
				networkElements.setDraw(dataTableRequest.getDraw());
			}

			response.setResponseData(networkElements);
			LOG.info("viewNetworkElementDetails:End");
		} catch (ApplicationException exe) {
			response.addFormError(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			response.addFormError(ex.getMessage());
			response.setValidationFailed(true);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response<String>> validateCommaSeparatedNetworkElementData(
			NetworkElementFilterModel networkElementFilterModel, String signum) {
		Response<String> result = new Response<>();
		try {
			LOG.info("validateCommaSeparatedNetworkElementData:Start");
			validationUtilityService.validateIntForZero(networkElementFilterModel.getProjectID(),
					AppConstants.PROJECT_ID_2);
			validationUtilityService.validateSignumForExistingEmployee(signum);
			validationUtilityService.validateProjectId(networkElementFilterModel.getProjectID());
			validationUtilityService.validateStringForBlank(networkElementFilterModel.getNodeNames(),
					"NetworkElementNames");
			validationUtilityService.validateStringForBlank(networkElementFilterModel.getTechnologyIDs(),
					"TechnologyIds");
			validationUtilityService.validateStringForBlank(networkElementFilterModel.getDomainIDs(), "DomainIds");
			validationUtilityService.validateNetworkElementCount(networkElementFilterModel.getNodeNames());
			NetworkElementValidateMasterModel networkElementValidateMasterModel = new NetworkElementValidateMasterModel();
			networkElementValidateMasterModel.setSignum(signum);
			networkElementValidateMasterModel.setStatus(INPROGRESS);
			networkElementDao.insertStatusInValidateMasterTable(networkElementValidateMasterModel);

			int neValidateID = isfCustomIdInsert.generateCustomId(networkElementValidateMasterModel.getNeValidateID());
			int ccGroupID = appService.getCountryCustomerGroupIDByProjectID(networkElementFilterModel.getProjectID());
			// Call proc to Validate Network Element Data and prepare temp table
			String tableName = networkElementDao.validateCommaSeparatedNetworkElementData(signum,
					networkElementFilterModel.getTechnologyIDs(), networkElementFilterModel.getDomainIDs(),
					networkElementFilterModel.getProjectID(), neValidateID, networkElementFilterModel.getNodeNames(),
					AppConstants.WEB, null, null, ccGroupID);

			result.setResponseData(tableName);
			LOG.info("validateCommaSeparatedNetworkElementData:End");
		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	    @Transactional("transactionManager")
		public ResponseEntity<Response<Void>> updateRadioSelectionForNE(NetworkElemntViewModel networkElemntViewModel) {

		Response<Void> result = new Response<>();
		try {
			
			if(Boolean.TRUE.equals(networkElemntViewModel.isSelectMultiple())) {
				result=updateRadioSelectionForMultipleNE(networkElemntViewModel);
			}
			else{
			LOG.info("updateRadioSelectionForNE:Start");

			validationUtilityService.validateStringForBlank(networkElemntViewModel.getTableName(), TABLE_NAME);
			validationUtilityService.validateStringForBlank(networkElemntViewModel.getName(), NETWORK_ELEMENT_NAME);
			validationUtilityService.validateIntForZero(networkElemntViewModel.getNetworkElementId(),
					NETWORK_ELEMENT_ID);
			validationUtilityService.validateStringForBlank(networkElemntViewModel.getStatus(), STATUS);

			// For updating a radio selection as 1 only
			if (StringUtils.equalsIgnoreCase(networkElemntViewModel.getStatus(), GROUP)) {

				int radioSelection = networkElementDao.getRadioSelectionByID(
						networkElemntViewModel.getNetworkElementId(), networkElemntViewModel.getTableName());
				if (radioSelection == 1) {
					networkElementDao.updateInActiveRadioSelectionByID(networkElemntViewModel.getNetworkElementId(),
							networkElemntViewModel.getTableName());
				} else if (radioSelection == 0) {

					networkElementDao.updateInactiveRadioSelectionByNEname(networkElemntViewModel.getName(),
							networkElemntViewModel.getTableName());
					networkElementDao.updateActiveRadioSelectionByID(networkElemntViewModel.getName(),
							networkElemntViewModel.getNetworkElementId(), networkElemntViewModel.getTableName());
				}
			}

			if (StringUtils.equalsIgnoreCase(networkElemntViewModel.getStatus(), VALID)) {
				networkElementDao.updateActiveRadioSelectionForValid(networkElemntViewModel.getNetworkElementId(),
						networkElemntViewModel.getTableName());
			}
			result.addFormMessage(UPDATED_SUCCESSFULLY);
			LOG.info("updateRadioSelectionForNE:End");
			
			}
		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
		
		@Transactional("transactionManager")
		public Response<Void> updateRadioSelectionForMultipleNE(NetworkElemntViewModel networkElemntViewModel) {
			
			Response<Void> result = new Response<>();
		
		   LOG.info("updateRadioSelectionForMultipleNE:Start");
			
			validationUtilityService.validateStringForBlank(networkElemntViewModel.getTableName(), TABLE_NAME);
			validationUtilityService.validateStringForBlank(networkElemntViewModel.getName(), NETWORK_ELEMENT_NAME);
			validationUtilityService.validateStringForBlank(networkElemntViewModel.getListOfNetworkElementId(),
					NETWORK_ELEMENT_ID);
			validationUtilityService.validateStringForBlank(networkElemntViewModel.getStatus(), STATUS);

			if (StringUtils.equalsIgnoreCase(networkElemntViewModel.getStatus(), GROUP)) {
				
				if (networkElemntViewModel.getRadioSelectionForSelectAll()==0) {
					//0 set
					networkElementDao.updateInActiveRadioSelectionByIDForSelectAll(networkElemntViewModel.getListOfNetworkElementId(),
							networkElemntViewModel.getTableName());
				} else if (networkElemntViewModel.getRadioSelectionForSelectAll()==1) {
                    //0 set
					networkElementDao.updateInactiveRadioSelectionByNEnameForSelectAll(networkElemntViewModel.getName(),
							networkElemntViewModel.getTableName());
					//1 set
					networkElementDao.updateActiveRadioSelectionByIDForSelectAll(networkElemntViewModel.getListOfNetworkElementId(),
                      networkElemntViewModel.getTableName());
				}
			}

			if (StringUtils.equalsIgnoreCase(networkElemntViewModel.getStatus(), VALID)) {
				//0 or 1 set
				networkElementDao.updateActiveRadioSelectionForValidForSelectAll(networkElemntViewModel.getListOfNetworkElementId(),
						networkElemntViewModel.getTableName(),networkElemntViewModel.getRadioSelectionForSelectAll());
			}
			result.addFormMessage(UPDATED_SUCCESSFULLY);
			LOG.info("updateRadioSelectionForMultipleNE:End");
			return result;
		
		}


	public ResponseEntity<Response<WorkOrderNetworkElementModel>> checkNetworkElementType(String tablename) {
		Response<WorkOrderNetworkElementModel> result = new Response<>();
		try {
			List<String> elementType = networkElementDao.getDistinctNetworkElementType(tablename);
			WorkOrderNetworkElementModel workOrderNetworkElementModel = new WorkOrderNetworkElementModel();

			if (CollectionUtils.isNotEmpty(elementType)) {

				if (elementType.size() == 1) {
					WorkOrderNetworkElementModel networkElementModel = networkElementDao
							.getDistinctNetworkElementNameAndCount(tablename);
					workOrderNetworkElementModel.setNetworkElementType(elementType.get(0));
					workOrderNetworkElementModel.setNeTextName(networkElementModel.getNeTextName());
					workOrderNetworkElementModel.setCount(networkElementModel.getCount());
					result.setResponseData(workOrderNetworkElementModel);

				} else if (elementType.size() > 1) {
					result.addFormError(ELEMENT_TYPE_MSG);
				}
			}

		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

		public ResponseEntity<Response<Void>> deleteTempTableForNE(String tablename) {
		Response<Void> result = new Response<>();
		try {
			LOG.info("deleteTempTableForNE:Start");
			networkElementDao.deleteTempTableForNE(tablename);
			result.addFormMessage(DELETED_MSG);
			LOG.info("deleteTempTableForNE:End");
		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	public DownloadTemplateModel downloadNeDataOnStatus(String status, String tablename) throws IOException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();
		try {
			validationUtilityService.validateStringForBlank(status, AppConstants.STATUS);
			validationUtilityService.validateStringForBlank(tablename, TABLENAME2);
			StringBuilder stringBuilder = new StringBuilder(StringUtils.EMPTY);
			stringBuilder.append(NETWORK_ELEMENT_DATA_TABLE).append(AppConstants.DASH).append(status)
					.append(AppConstants.DASH)
					.append((new SimpleDateFormat(AppConstants.DATE_FORMAT).format(new Date())))
					.append(AppConstants.XLSX);

			List<Map<String, Object>> networkElement = networkElementDao.downloadNeDataOnStatus(status, tablename);
			byte[] fData = null;
			if (CollectionUtils.isNotEmpty(networkElement)) {
				fData = FileUtil.generateXlsFile(networkElement);

			}

			downloadTemplateModel.setpFileContent(fData);
			downloadTemplateModel.setpFileName(stringBuilder.toString());
		} catch (ApplicationException e) {
			downloadTemplateModel = null;
		}

		return downloadTemplateModel;
		
	}
	
	public  ResponseEntity<Response<Long>> getTotalNetworkElementCount(NetworkElementCountModel networkElementCountModel) {
		Response<Long> response=new Response<>();
		try {
			if(networkElementCountModel.getProjectID() == 0) {
				response.addFormError(PROVIDE_PROJECTID);
			}
			else {
				response.setResponseData(networkElementDao.getTotalNetworkElementCount(networkElementCountModel));	
			}
		}
		
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);
			}
			catch (Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);
		
	}
	
	
	
	@Transactional("transactionManager")
	public ResponseEntity<Response<Void>> resetSelectedRecordsForNE(NetworkElemntViewModel networkElemntViewModel) {

		Response<Void> result = new Response<>();
		try {
			
			LOG.info("resetSelectedRecordsForNE:Start");

			validationUtilityService.validateStringForBlank(networkElemntViewModel.getTableName(), TABLE_NAME);
			validationUtilityService.validateStringForBlank(networkElemntViewModel.getStatus(), STATUS);

				networkElementDao.resetSelectedRecordsForNE(networkElemntViewModel.getStatus(),
						networkElemntViewModel.getTableName());
			
			result.addFormMessage(UPDATED_SUCCESSFULLY);
			LOG.info("resetSelectedRecordsForNE:End");
			
		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
