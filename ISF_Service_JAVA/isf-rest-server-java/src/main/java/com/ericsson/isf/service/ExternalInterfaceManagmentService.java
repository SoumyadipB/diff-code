/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.azure.keyVault.service.ClientSecretKeyValueProvider;
import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.AccessManagementDAO;
import com.ericsson.isf.dao.AppUtilDAO;
import com.ericsson.isf.dao.ExternalInterfaceManagmentDAO;
import com.ericsson.isf.dao.FlowChartDAO;
import com.ericsson.isf.dao.RpaDAO;
import com.ericsson.isf.dao.WOExecutionDAO;
import com.ericsson.isf.dao.WorkOrderPlanDao;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.Answer;
import com.ericsson.isf.model.AuditDataModel;
import com.ericsson.isf.model.AzureAppModel;
import com.ericsson.isf.model.BookingDetailsModel;
import com.ericsson.isf.model.ChildStepDetailsModel;
import com.ericsson.isf.model.CreateWoResponse;
import com.ericsson.isf.model.DeliveryResponsibleModel;
import com.ericsson.isf.model.ErisiteDataModel;
import com.ericsson.isf.model.EventPublisherRequestModel;
import com.ericsson.isf.model.FeedModel;
import com.ericsson.isf.model.HeaderModel;
import com.ericsson.isf.model.NetworkElementNewModel;
import com.ericsson.isf.model.ParallelWorkOrderDetailsModel;
import com.ericsson.isf.model.Parameter;
import com.ericsson.isf.model.Parameters;
import com.ericsson.isf.model.PreviousStepDetailsModel;
import com.ericsson.isf.model.Project;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.SaveClosureDetailsForWOModel;
import com.ericsson.isf.model.ScheduleParameterModel;
import com.ericsson.isf.model.SharePointDetailModel;
import com.ericsson.isf.model.StepDetailsModel;
import com.ericsson.isf.model.Value;
import com.ericsson.isf.model.Values;
import com.ericsson.isf.model.VaultModel;
import com.ericsson.isf.model.ViewNetworkElementModel;
import com.ericsson.isf.model.WOOutputFileModel;
import com.ericsson.isf.model.WorkOrderCompleteDetailsModel;
import com.ericsson.isf.model.WorkOrderCurrentStepDetailResponseModel;
import com.ericsson.isf.model.WorkOrderCurrentStepDetailsModel;
import com.ericsson.isf.model.WorkOrderModel;
import com.ericsson.isf.model.WorkOrderOutputFileModel;
import com.ericsson.isf.model.WorkOrderProgressModel;
import com.ericsson.isf.model.botstore.ServerBotModel;
import com.ericsson.isf.security.aes.AesUtil;
import com.ericsson.isf.service.audit.AuditDataProcessor;
import com.ericsson.isf.service.audit.AuditManagementService;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AppUtil;
import com.ericsson.isf.util.BeanUtils;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.ericsson.isf.util.HttpDownloadUtility;
import com.ericsson.isf.util.IsfCustomIdInsert;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

/**
 *
 * @author esanpup
 */
@Service
public class ExternalInterfaceManagmentService {

	private static final String SHARE_POINT = "SharePoint_";

	private static final String EXECUTED_STEP = "ExecutedStep";

	private static final String THIS_WO_DOES_NOT_HAVE_ANY_AUTOMATIC_STEP = "This WO does not have any automatic step for current task";

	private static final String ARROW = " -> ";

	private static final String WOID = "WOID: ";

	private static final String SUB_ACTIVITY_ID = "SubActivityID";

	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ExternalInterfaceManagmentService.class);

	private static final String TASK_ID = "TaskID";

	private static final String PLEASE_PROVIDE_VALUE = "Please provide %s!";

	private static final String IS_API_SUCCESS = "isApiSuccess";
	private static final String API_CONTROLLER="externalInterface";
	private static final String VALIDATIONS="validations";
	private static final String PRIORITY_COMMENT="either Priority or Comment";
	private static final String COMMENT="Comment";

	private static final String WOID_NULL_OR_ZERO="validateWOIDForNullOrZero";
	private static final String WOID_ACTIVE="validateWOIDIsActive";
	private static final String WO_UNASSIGNED="validateIfWorkorderIsUnassigned";
	private static final String WOID_CLOSED_DEFERRED_REJECTED="validateWOIDForClosedDeferredAndRejectedStatus";
	private static final String EXTERNAL_SOURCE_NAME_WITH_WOID="validateExternalSourceNameWithWoID";
	private static final String STRING_BLANK_STEPID="validateStringForBlankStepID";
	private static final String INTEGER_NULL_OR_ZERO_TASKID="validateIntegerForNullOrZeroTaskID";
	private static final String TASKID_FOR_START_TASK="validateTaskIDForStartTask";
	private static final String WO_ASSIGNED_TO_SIGNUM="validateIfWorkOrderAssignedToSignum";

	private static final String INTEGER_WOID_NULL_OR_ZERO="validateIntegerWOIDForNullOrZero";
	private static final String WO_ASSIGN_FOR_SIGNUM="validateWoAssignForSignum";
	private static final String STRING_BLANK_FLOWCHARTSTEPID="validateStringForBlankFlowChartStepId";
	private static final String INTEGER_FLOWCHARTDEFID_NULL_OR_ZERO="validateIntegerFlowChartDefIDForNullOrZero";
	private static final String STRING_BLANK_EXECUTIONTYPE="validateStringForBlankExecutionType";
	private static final String SIGNUM_EXISTING_EMPLOYEE="validateSignumForExistingEmployee";
	private static final String STRING_BLANK_DECISIONVALUE="validateStringForDecisionValue";
	private static final String DECISIONVALUE_LENGTH="validateLengthDecisionValue";
	private static final String DEFID_VALIDATE="validateDefID";
	private static final String STEPID_AND_EXECUTIONTYPE="validateStepIdAndExecutionType";
//	private static final String NO_DATA_FOUND="No Data Found!";

//	private static final String WOID_NULL_OR_ZERO = "validateWOIDForNullOrZero";
//	private static final String WOID_ACTIVE = "validateWOIDIsActive";
//	private static final String WO_UNASSIGNED = "validateIfWorkorderIsUnassigned";
//	private static final String WOID_CLOSED_DEFERRED_REJECTED = "validateWOIDForClosedDeferredAndRejectedStatus";
//	private static final String EXTERNAL_SOURCE_NAME_WITH_WOID = "validateExternalSourceNameWithWoID";
//	private static final String STRING_BLANK_STEPID = "validateStringForBlankStepID";
//	private static final String INTEGER_NULL_OR_ZERO_TASKID = "validateIntegerForNullOrZeroTaskID";
//	private static final String TASKID_FOR_START_TASK = "validateTaskIDForStartTask";
//	private static final String WO_ASSIGNED_TO_SIGNUM = "validateIfWorkOrderAssignedToSignum";
//	private static final String DECISION_VALUE = "Decision Value";

	//	private static final String VALIDATIONS="validations";
	private static final String STRING_BLANK_SIGNUMID = "validateStringForBlankSignumID";
	//	private static final String WOID_NULL_OR_ZERO="validateWOIDForNullOrZero";
	//	private static final String WOID_ACTIVE="validateWOIDIsActive";
	//	private static final String WO_UNASSIGNED="validateIfWorkorderIsUnassigned";
	//	private static final String WOID_CLOSED_DEFERRED_REJECTED="validateWOIDForClosedDeferredAndRejectedStatus";
	//	private static final String EXTERNAL_SOURCE_NAME_WITH_WOID="validateExternalSourceNameWithWoID";
	//	private static final String STRING_BLANK_STEPID="validateStringForBlankStepID";
	//	private static final String INTEGER_NULL_OR_ZERO_TASKID="validateIntegerForNullOrZeroTaskID";
	//	private static final String TASKID_FOR_START_TASK="validateTaskIDForStartTask";
	//	private static final String WO_ASSIGNED_TO_SIGNUM="validateIfWorkOrderAssignedToSignum";
	private static final String TASK_DETAILS = "validateTaskDetails";
	private static final String CHECK_IF_PREVIOUS_STEP_COMPLETED = "validateIFPreviousStepCompleted";
	private static final String CHECK_STEPID_FOR_DECISION = "validateStepIDForDecision";
	private static final String CHECK_DECISION_VALUE = "validateDecisionValue";

	private static final String TASK_NAME_NULL = "validateTaskName";
	private static final String NO_DATA_FOUND = "No Data Found!";
	private static String dataFormat="'%S'";
	private static String stringFormat="''%S''";
	private static final String RESULT="Result=0";
	private static final String COMMA=", ";
	private static String columnFormat="%S AS %S";

	@Autowired
	private ExternalInterfaceManagmentDAO erisiteManagmentDAO;

	@Autowired
	private ApplicationConfigurations configurations;

	@Autowired
	private AppService appService;

	@Autowired
	OutlookAndEmailService emailService;

	@Autowired /* Bind to bean/pojo */
	private AccessManagementService accessManagementService;

	@Autowired
	private IsfCustomIdInsert isfCustomIdInsert;

	@Autowired
	private WorkOrderPlanService workOrderPlanService;

	@Autowired
	private RpaDAO rpaDAO;

	@Autowired
	private WOExecutionService woExecutionService;

	@Autowired
	private WorkOrderPlanDao workOrderPlanDao;

	@Autowired
	private FlowChartDAO flowChartDao;

	@Autowired
	private ActivityMasterService activityMasterService;

	@Autowired
	private WOExecutionDAO woExecutionDAO;

	@Autowired
	private AuditManagementService auditManagementService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private AuditDataProcessor auditDataProcessor;

	@Autowired
	private ValidationUtilityService validationUtilityService;

	@Autowired
	private AccessManagementDAO accessManagementDAO;
	
	@Autowired
	private AzureService azureService;
	
	@Autowired
	private ClientSecretKeyValueProvider clientSecretKeyValueProvider;
	
	@Autowired
	private AppUtilDAO appUtilDAO;
	
	@Autowired
	private AesUtil aesUtil;
	
	@Autowired
	private RedisService redisService;

	@Autowired
	@Qualifier("testBlobContainer")
	private CloudBlobContainer testBlobContainer;
	
	@Autowired
	@Qualifier("commonBlobContainer")
	private CloudBlobContainer commonBlobContainer;

	@Autowired
	ApplicationConfigurations appConfig;

	public Map<String, Object> checkIsfHealth() {
		boolean isRunning = false;
		String msg = "";
		Map<String, Object> finalData = new HashMap<String, Object>();
		if (configurations.getStringProperty(ConfigurationFilesConstants.CURRENT_ENV).equalsIgnoreCase("ECN")) {
			finalData = redirectToCloud();
			LOG.info("Redirecting to Cloud URL :"
					+ configurations.getStringProperty(ConfigurationFilesConstants.CURRENT_ENV_URL));
		} else {

			try {
				List<Map<String, Object>> empDetails = this.erisiteManagmentDAO.checkIsfHealth();
				if (empDetails.size() > 0) {
					isRunning = true;
					msg = "ISF is UP and Running";
		}
			} catch (Exception e) {
				isRunning = false;
				msg = "ISF is down";
				e.printStackTrace();
			}
			finalData.put("msg", msg);
			finalData.put("isRunning", isRunning);

			return finalData;

		}
		return finalData;

	}

	private Map<String, Object> redirectToCloud() {
		RestTemplate restTemplate = new RestTemplate();
		String url = configurations.getStringProperty(ConfigurationFilesConstants.CURRENT_ENV_URL)+API_CONTROLLER+"/"+"checkIsfHealth";
		ParameterizedTypeReference<Map<String, Object>> typeRef = new ParameterizedTypeReference<Map<String, Object>>() {};
		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, null, typeRef);
		return response.getBody();
	}

	/**
	 * @param erisiteDataModel
	 * @param headers
	 * @return
	 */
	@Transactional("transactionManager")
	public boolean generateWOrkOrder(ErisiteDataModel erisiteDataModel, HttpHeaders headers) {
		LOG.info("Test API /generateWOrkOrder SERVICE THREAD :ELKPAIN");
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(configurations.getStringProperty(ConfigurationFilesConstants.CURRENT_ENV).equalsIgnoreCase("ECN")) {
					redirectToCloudForGenerateWorkOrder(erisiteDataModel,headers);
					LOG.info("Redirecting to Cloud URL :"+configurations.getStringProperty(ConfigurationFilesConstants.CURRENT_ENV_URL));
				}
				else {
					generateWOrkOrderThread(erisiteDataModel, headers);
				}
			}

		}).start();
		return true;
	}

	protected boolean redirectToCloudForGenerateWorkOrder(ErisiteDataModel erisiteDataModel, HttpHeaders headers) {
		LOG.info("Test API /generateWOrkOrder SERVICE REDIRECT CLOUD :ELKPAIN");
		RestTemplate restTemplate = new RestTemplate();
		String url = configurations.getStringProperty(ConfigurationFilesConstants.CURRENT_ENV_URL) + API_CONTROLLER
				+ "/" + "generateWOrkOrder";
		headers.add("Accept-Encoding", "identity");
		HttpEntity<ErisiteDataModel> request = new HttpEntity<>(erisiteDataModel, headers);
		ResponseEntity<Boolean> response=restTemplate.exchange(url, HttpMethod.POST, request, Boolean.class);
		return response.getBody();	
	}

	@Transactional("transactionManager")
	private boolean generateWOrkOrderThread(ErisiteDataModel erisiteDataModel, HttpHeaders headers) {
		
		LOG.info("Test API /generateWOrkOrder SERVICE MAIN THREAD :ELKPAIN");

		String source_system_id = headers.get("source_system_id") != null ? headers.get("source_system_id").get(0)
				: null;
		String message_batch_id = headers.get("message_batch_id") != null ? headers.get("message_batch_id").get(0)
				: null;
		String transaction_id = headers.get("transaction_id") != null ? headers.get("transaction_id").get(0) : null;
		String businessentityid = headers.get("businessentityid") != null ? headers.get("businessentityid").get(0)
				: null;
		String assignedGroup = erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails().getErisiteAssignment()
				.get(0).getAssignedGroup();
		int externalSourceID = 0;
		int workOrderIdAndInstanceId = 0;

		Map<String, Object>	 data = this.erisiteManagmentDAO.getProjectIDExternalProjectFromScope(erisiteDataModel
				.getNroActivityEvent().getErisiteActivityDetails().getErisiteParentReferences().getParentProjectId(),
				erisiteDataModel
				.getNroActivityEvent().getErisiteActivityDetails().getErisiteParentReferences().getParentWorkPlanTemplateName(),
				erisiteDataModel
				.getNroActivityEvent().getErisiteActivityDetails().getErisiteActivityCoreDetails().getActivityName());
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
			Date forecastStartDate = formatter.parse(erisiteDataModel.getNroActivityEvent()
					.getErisiteActivityDetails().getErisiteSchedule().getForecastStartDateTmp());
			Date forecastEndDate = formatter.parse(erisiteDataModel.getNroActivityEvent()
					.getErisiteActivityDetails().getErisiteSchedule().getForecastEndDateTmp());
			erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails().getErisiteSchedule()
			.setForecastStartDate(forecastStartDate);
			erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails().getErisiteSchedule()
			.setForecastEndDate(forecastEndDate);
			
			externalSourceID = this.erisiteManagmentDAO.getExternalSourceID(source_system_id);

		} catch (Exception e) {
		}

		int isfProjectId=0;
		if(data!=null) {

			isfProjectId	=	(int) data.get("ISFProjectID");
		}
		this.erisiteManagmentDAO.generateWOrkOrder(erisiteDataModel, externalSourceID,isfProjectId);


		long hour = 0;
		try {
			long diff = erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails().getErisiteSchedule()
					.getForecastEndDate().getTime()
					- erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails().getErisiteSchedule()
					.getForecastStartDate().getTime();
			hour = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
		} catch (Exception e2) {
			e2.printStackTrace();
			return true;
		}

		List<Map<String, Object>> mapping = this.erisiteManagmentDAO.getActivitySubAcitvityMapping(externalSourceID);
		boolean isFound = false;
		int subActivity = 0;
		if (mapping != null) {
			for (Map<String, Object> row : mapping) {
				if (row.get("ExtActivityID").equals(erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
						.getErisiteActivityCoreDetails().getActivityName())) {
					isFound = true;
					break;
				}
			}
		}

		int isfProjectID = 0;
		int executionPlanID = 0;
		String assignTo = "NULL";
		String uploadedBy = "";
		LOG.info("++++++++++++++++++++++++++++++Data check+++++++++++++++++++++++ "+data);
		if (data != null) {
			try {
				isfProjectID = (int) data.get("ISFProjectID");

				String tmpexecutionPlanID = this.erisiteManagmentDAO.getExistingExecutionPlanID(
						erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
						.getErisiteActivityCoreDetails().getActivityName(),
						isfProjectID,
						erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails().getErisiteParentReferences()
						.getParentWorkPlanTemplateName(),
						erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails().getErisiteParentReferences()
						.getParentProjectId(),
						externalSourceID);

				uploadedBy = this.erisiteManagmentDAO.getExistingAssignToByProject(isfProjectID, externalSourceID,
						erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails().getErisiteParentReferences()
						.getParentProjectId());
				if (tmpexecutionPlanID != null) {
					executionPlanID = Integer.parseInt(tmpexecutionPlanID);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		if(!this.erisiteManagmentDAO.checkIfExternalProjectActive(erisiteDataModel
				.getNroActivityEvent().getErisiteActivityDetails().getErisiteParentReferences().getParentProjectId(), externalSourceID)
				){
			isFound=false;
		}

		String nodeName = null;
		Map<String, Object> nodeDetails = new HashMap<String, Object>();
		try {
			nodeDetails = this.erisiteManagmentDAO.getNodeNamesByPlanID(erisiteDataModel.getNroActivityEvent()
					.getErisiteActivityDetails().getErisiteParentReferences().getParentWorkPlanId(), externalSourceID);
			if (nodeDetails != null) {
				nodeName = (String) nodeDetails.get("SITENAME");
			}
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		if (null == nodeName) {
			isFound = false;
		}
		String actualWoName = source_system_id + "_"
				+ erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails().getErisiteActivityCoreDetails()
				.getActivityId()
				+ "_"
				+ erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails().getErisiteParentReferences()
				.getParentWorkPlanId()
				+ "_" + erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
				.getErisiteActivityCoreDetails().getActivityName();
		String comments = null;
		if (nodeDetails != null) {

			actualWoName += (nodeDetails.get("Scope of Work 1") != null)
					? "_" + (String) nodeDetails.get("Scope of Work 1")
					: "";
					actualWoName += (nodeDetails.get("Scope of Work 2") != null)
							? "_" + (String) nodeDetails.get("Scope of Work 2")
							: "";
							actualWoName += (nodeDetails.get("Scope of Work 3") != null)
									? "_" + (String) nodeDetails.get("Scope of Work 3")
									: "";
									actualWoName += (nodeDetails.get("Scope of Work 4") != null)
											? "_" + (String) nodeDetails.get("Scope of Work 4")
											: "";
											actualWoName += (nodeDetails.get("Scope of Work 5") != null)
													? "_" + (String) nodeDetails.get("Scope of Work 5")
													: "";
													comments = (nodeDetails.get("IM Notes") != null) ? (String) nodeDetails.get("IM Notes") : null;
		}
		LOG.info("==========================isfound status========================================== "+isFound);
		if (isFound) {
			if ("Pending Predecessors".equals(erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
					.getErisiteActivityCoreDetails().getStatus())) {
				LOG.info("==========================inside Pending Predecessors========================================== ");
				Map<String, Object> woCreationData = new HashMap<String, Object>();
				woCreationData = this.erisiteManagmentDAO.checkIfPredecessorsExists(
						erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
						.getErisiteActivityCoreDetails().getActivityName(),
						erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
						.getErisiteActivityCoreDetails().getStatus(),
						erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
						.getErisiteParentReferences().getParentWorkPlanId(),
						erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
						.getErisiteParentReferences().getParentProjectId(),
						erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
						.getErisiteParentReferences().getParentWorkPlanTemplateName(),
						externalSourceID,
						isfProjectID);
				if (woCreationData != null && woCreationData.get("WOCreationID") != null) {
					LOG.info("==========================inside Pending Predecessors if condition========================================== ");
					String historyStatus = null;
					historyStatus = this.erisiteManagmentDAO
							.checkIfWOExists(Long.parseLong(woCreationData.get("WOCreationID").toString()));
					if ("INPROGRESS".equals(historyStatus.toUpperCase())) {
						LOG.info("==========================inside Pending Predecessors if condition INPROGRESS ========================================== ");
						this.erisiteManagmentDAO.updateForcastWoCreation(erisiteDataModel, hour,
								"Pending Predecessors", externalSourceID, isfProjectID,executionPlanID);
					} else if ("COMPLETED".equals(historyStatus.toUpperCase())) {
						LOG.info("==========================inside Pending Predecessors elseif condition COMPLETED ========================================== ");
						String woName = woCreationData.get("WorkOrderName") + "_"
								+ woCreationData.get("ExecutionPlanID");
						Map<String, Object> woStatus = this.erisiteManagmentDAO
								.getWoStatusByWoName((String) woCreationData.get("WorkOrderName"));
						if ((woStatus != null && "CLOSED".equals(woStatus.get("status"))) ||
								(woStatus != null && "DEFERRED".equals(woStatus.get("status")))	) {// user comment and status deferred//   changed by Raju
							LOG.info("==========================inside Pending Predecessors elseif if condition CLOSED/DEFERRED ========================================== ");
							this.erisiteManagmentDAO.insertBlkWoCreation(erisiteDataModel,
									"TBL_BULK_WORK_ORDER_CREATION", hour, subActivity, isfProjectID,
									executionPlanID, nodeName, assignTo, source_system_id, message_batch_id,
									transaction_id, businessentityid, assignedGroup, externalSourceID,
									actualWoName, comments,uploadedBy);
							workOrderIdAndInstanceId = isfCustomIdInsert
									.generateCustomId(erisiteDataModel.getWoCreationID());
							erisiteDataModel.setWoCreationID(workOrderIdAndInstanceId);
							this.erisiteManagmentDAO.insertBlkWoCreationHistory(erisiteDataModel);

						} else {
							LOG.info("==========================inside Pending Predecessors elseif else condition CLOSED/DEFERRED ========================================== ");
							this.erisiteManagmentDAO.updateForcastWorkOrder(erisiteDataModel, woName);
						}
					}
				} else {
					LOG.info("==========================inside Pending Predecessors else condition========================================== ");
					this.erisiteManagmentDAO.insertBlkWoCreation(erisiteDataModel,
							"TBL_BULK_WORK_ORDER_CREATION", hour, subActivity, isfProjectID, executionPlanID,
							nodeName, assignTo, source_system_id, message_batch_id, transaction_id,
							businessentityid, assignedGroup, externalSourceID, actualWoName, comments,uploadedBy);
					workOrderIdAndInstanceId = isfCustomIdInsert
							.generateCustomId(erisiteDataModel.getWoCreationID());
					erisiteDataModel.setWoCreationID(workOrderIdAndInstanceId);
					this.erisiteManagmentDAO.insertBlkWoCreationHistory(erisiteDataModel);
				}
			} else if ("Ready to Start".equalsIgnoreCase(erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
					.getErisiteActivityCoreDetails().getStatus().trim())) {
				LOG.info("==========================inside Ready to Start if condition========================================== ");
				Map<String, Object> woCreationData = new HashMap<String, Object>();
				woCreationData = this.erisiteManagmentDAO.checkIfPredecessorsExists(
						erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
						.getErisiteActivityCoreDetails().getActivityName(),
						"Pending Predecessors",
						erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
						.getErisiteParentReferences().getParentWorkPlanId(),
						erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
						.getErisiteParentReferences().getParentProjectId(),
						erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
						.getErisiteParentReferences().getParentWorkPlanTemplateName(),
						externalSourceID, isfProjectID);
				if (woCreationData != null && woCreationData.get("WOCreationID") != null) {
					LOG.info("==========================inside Ready to Start elseif if condition========================================== ");
					String historyStatus = this.erisiteManagmentDAO
							.checkIfWOExists(Long.parseLong(woCreationData.get("WOCreationID").toString()));

					if ("COMPLETED".equals(historyStatus)) {
						LOG.info("==========================inside Ready to Start elseif if inside if condition when status is completed========================================== ");
						String woName = woCreationData.get("WorkOrderName") + "_"
								+ woCreationData.get("ExecutionPlanID");
						Map<String, Object> woStatus = this.erisiteManagmentDAO
								.getWoStatusByWoName((String) woCreationData.get("WorkOrderName"));
						if ((woStatus != null && "CLOSED".equals(woStatus.get("status"))) ||
								(woStatus != null && "DEFERRED".equals(woStatus.get("status")))) {  //user comment and status deferred//   changed by Raju
							LOG.info("==========================inside Ready to Start elseif if inside if condition when status is CLOSED/DEFERRED========================================== ");
							this.erisiteManagmentDAO.insertBlkWoCreation(erisiteDataModel,
									"TBL_BULK_WORK_ORDER_CREATION", hour, subActivity, isfProjectID,
									executionPlanID, nodeName, assignTo, source_system_id, message_batch_id,
									transaction_id, businessentityid, assignedGroup, externalSourceID,
									actualWoName, comments,uploadedBy);
							workOrderIdAndInstanceId = isfCustomIdInsert
									.generateCustomId(erisiteDataModel.getWoCreationID());
							erisiteDataModel.setWoCreationID(workOrderIdAndInstanceId);
							this.erisiteManagmentDAO.insertBlkWoCreationHistory(erisiteDataModel);
						} else {
							LOG.info("==========================inside Ready to Start elseif if inside else condition when status is not CLOSED/DEFERRED========================================== ");
							this.erisiteManagmentDAO.updateWorkOrderStatus(woName, erisiteDataModel,
									externalSourceID);
						}
					} else if ("INPROGRESS".equals(historyStatus)) {
						LOG.info("==========================inside Ready to Start elseif if inside if condition when status is INPROGRESS========================================== ");
						this.erisiteManagmentDAO.updateForcastWoCreation(erisiteDataModel, hour,
								"Pending Predecessors", externalSourceID, isfProjectID,executionPlanID);
					}
					
					LOG.info("==========================++++++++++++++++++++++++++++++++++++++++++++++++========================================== ");
					this.erisiteManagmentDAO.insertBlkWoCreation(erisiteDataModel,
							"TBL_BULK_WORK_ORDER_CREATION_NOT_FOUND", hour, subActivity, isfProjectID,
							executionPlanID, nodeName, assignTo, source_system_id, message_batch_id,
							transaction_id, businessentityid, assignedGroup, externalSourceID, actualWoName,
							comments,uploadedBy);
					workOrderIdAndInstanceId = isfCustomIdInsert
							.generateCustomId(erisiteDataModel.getWoCreationID());
					erisiteDataModel.setWoCreationID(workOrderIdAndInstanceId);
				} else {
					LOG.info("==========================inside Ready to Start elseif else condition========================================== ");
					Map<String, Object> woCreationData1 = new HashMap<String, Object>();
					woCreationData1 = this.erisiteManagmentDAO.checkIfPredecessorsExists(
							erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
							.getErisiteActivityCoreDetails().getActivityName(),
							erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
							.getErisiteActivityCoreDetails().getStatus(),
							erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
							.getErisiteParentReferences().getParentWorkPlanId(),
							erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
							.getErisiteParentReferences().getParentProjectId(),
							erisiteDataModel.getNroActivityEvent().getErisiteActivityDetails()
							.getErisiteParentReferences().getParentWorkPlanTemplateName(),
							externalSourceID, isfProjectID);
					if (woCreationData1 != null && woCreationData1.get("WOCreationID") != null) {
						LOG.info("==========================inside Ready to Start elseif if inside else condition========================================== ");
						String historyStatus = null;
						historyStatus = this.erisiteManagmentDAO.checkIfWOExists(
								Long.parseLong(woCreationData1.get("WOCreationID").toString()));
						if ("INPROGRESS".equals(historyStatus.toUpperCase())) {
							LOG.info("==========================111111111111111111111111111========================================== ");
							this.erisiteManagmentDAO.updateForcastWoCreation(erisiteDataModel, hour,
									"Ready to Start", externalSourceID, isfProjectID,executionPlanID);
						} else if ("COMPLETED".equals(historyStatus.toUpperCase())) {
							LOG.info("==========================222222222222222222222222========================================== "+woCreationData1.get("WorkOrderName"));
							String woName = woCreationData1.get("WorkOrderName") + "_"
									+ woCreationData1.get("ExecutionPlanID");
							Map<String, Object> woStatus = this.erisiteManagmentDAO
									.getWoStatusByWoName((String) woCreationData1.get("WorkOrderName"));
							
							
							LOG.info("==========================RAJJJJJJJJJJJJJJJJJJJJJJ========================================== "+woStatus+  "    "+woStatus.get("status"));
							if ((woStatus != null && "CLOSED".equals(woStatus.get("status"))) || 
									(woStatus != null && "DEFERRED".equals(woStatus.get("status")))) {
								LOG.info("==========================3333333333333333333333333333333========================================== ");
								this.erisiteManagmentDAO.insertBlkWoCreation(erisiteDataModel,
										"TBL_BULK_WORK_ORDER_CREATION", hour, subActivity, isfProjectID,
										executionPlanID, nodeName, assignTo, source_system_id, message_batch_id,
										transaction_id, businessentityid, assignedGroup, externalSourceID,
										actualWoName, comments,uploadedBy);
								workOrderIdAndInstanceId = isfCustomIdInsert
										.generateCustomId(erisiteDataModel.getWoCreationID());
								erisiteDataModel.setWoCreationID(workOrderIdAndInstanceId);
								this.erisiteManagmentDAO.insertBlkWoCreationHistory(erisiteDataModel);
							} else {
								LOG.info("==========================4444444444444444444444444========================================== ");
								this.erisiteManagmentDAO.updateForcastWorkOrder(erisiteDataModel, woName);
							}
						}
					} else {
						LOG.info("==========================555555555555555555555555555555555555555555555555========================================== ");
						this.erisiteManagmentDAO.insertBlkWoCreation(erisiteDataModel,
								"TBL_BULK_WORK_ORDER_CREATION", hour, subActivity, isfProjectID,
								executionPlanID, nodeName, assignTo, source_system_id, message_batch_id,
								transaction_id, businessentityid, assignedGroup, externalSourceID, actualWoName,
								comments,uploadedBy);
						workOrderIdAndInstanceId = isfCustomIdInsert
								.generateCustomId(erisiteDataModel.getWoCreationID());
						erisiteDataModel.setWoCreationID(workOrderIdAndInstanceId);
						this.erisiteManagmentDAO.insertBlkWoCreationHistory(erisiteDataModel);
					}
				}
			}
		}
		return true;
	}

	public ResponseEntity<Response<List<Map<String, Object>>>> getExternalProjectID(int sourceID) {

		Response<List<Map<String, Object>>> response = new Response<>();
		try {
			LOG.info("Start Execution of getExternalProjectID Api");
			validationUtilityService.validateIntForZero(sourceID, AppConstants.SOURCE_ID);
			List<Map<String, Object>> listofExternalProject = this.erisiteManagmentDAO.getExternalProjectID(sourceID);
			if (listofExternalProject.isEmpty()) {
				response.addFormMessage(NO_DATA_FOUND);
				response.setResponseData(listofExternalProject);
			} else {
				response.setResponseData(listofExternalProject);
			}
			LOG.info("Stop Execution of getExternalProjectID Api");
		} catch (ApplicationException e) {
			response.addFormMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}	

	public List<Map<String, Object>> getActiveExternalProjectID(String source, int projectID) {
		return this.erisiteManagmentDAO.getActiveExternalProjectID(source, projectID);
	}

	public List<Map<String, Object>> getActiveExternalWorkPlanTemplateList(String sourceID, int externalProjectID) {

		return this.erisiteManagmentDAO.getActiveExternalWorkPlanTemplateList(sourceID, externalProjectID);
	}

	public ResponseEntity<Response<List<Map<String, Object>>>> getActiveExternalActivityList(String sourceID, int projectID,
			int externalProjectID, String workPlanTemplateName) {

		Response<List<Map<String, Object>>> result = new Response<>();
		try {
			LOG.info("getActiveExternalActivityList:Start");
			validationUtilityService.validateIntForZero(projectID, AppConstants.PROJECT_ID);
			validationUtilityService.validateStringForBlank(sourceID, AppConstants.SOURCE_ID);
			validationUtilityService.validateIntForZero(externalProjectID, AppConstants.EXTERNAL_PROJECT_ID);
			validationUtilityService.validateStringForBlank(workPlanTemplateName, AppConstants.WORK_PLAN_TEMPLATE_NAME);
			List<Map<String, Object>> activeExternalActivityList = this.erisiteManagmentDAO.getActiveExternalActivityList(sourceID, projectID, externalProjectID,workPlanTemplateName);

			if (activeExternalActivityList.isEmpty()) {
				result.addFormMessage(NO_DATA_FOUND);
				result.setResponseData(activeExternalActivityList);
			} else {
				result.setResponseData(activeExternalActivityList);
			}

			LOG.info("getActiveExternalActivityList:End");
		}
		catch (ApplicationException exe) {
			result.addFormMessage(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	// @Scheduled(fixedDelay = 14400000)
	public void scheduleSapBoJob() {
		if (configurations.getBooleanProperty(ConfigurationFilesConstants.SAPBO_SCHEDULER_ENABLED, false)) {
			LOG.info("Monitor Sap BO Report fetching started");
			downloadSapBO(configurations.getStringProperty(ConfigurationFilesConstants.SAPBO_AMERICAS5), "americas",
					AppConstants.NUMBER_5);
			downloadSapBO(configurations.getStringProperty(ConfigurationFilesConstants.SAPBO_AMERICAS11), "americas",
					AppConstants.NUMBER_11);
			downloadSapBO(configurations.getStringProperty(ConfigurationFilesConstants.SAPBO_APAC), "apac",
					AppConstants.NUMBER_5);
			downloadSapBO(configurations.getStringProperty(ConfigurationFilesConstants.SAPBO_EMEA), "emea",
					AppConstants.NUMBER_5);
		} else {
			LOG.info("Schedular is disabled");
		}
	}

	public boolean downloadSapBO(String wpId, String instance, int columnCount) throws JSONException {

		String wpID[];

		final String uri = "https://" + instance + "-int.erisite.ericsson.net/biprws/logon/long";

		String jsonStrng = "{\"userName\": \"svcessc2g1\",\"password\": \"O!CMN7vvGBVW6Fj5\",\"auth\": \"secEnterprise\"}";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Accept", "application/json");

		HttpEntity<String> request = new HttpEntity<>(jsonStrng, headers);

		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);

			if (response.getStatusCodeValue() == AppConstants.SAP_BO_RESPONSE_CODE) {
				LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + response.getStatusCodeValue());
				if (StringUtils.isBlank(response.getBody())) {
					String status = "No Data Found For ReportId:" + wpId;
					sendMailForSAPBOFailure(instance, wpId, status);
					LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + "-" + status);
				}
			} else {
				LOG.info(AppConstants.SAP_BO_FAILURE_RESPONSE + response.getStatusCodeValue());
				sendMailForSAPBOFailure(instance, wpId, AppConstants.SAP_BO_FAILURE_RESPONSE);
			}
		} catch (RestClientException e) {
			sendMailForSAPBOFailure(instance, wpId, AppConstants.SAP_BO_FAILURE_RESPONSE);
			//			e.printStackTrace();
		}

		if (response != null) {

			JSONObject jsonObj = new JSONObject(response.getBody());

			LOG.info("Logon success : Token generated is: " + jsonObj.get("logonToken"));

			if (null != jsonObj.get("logonToken") && jsonObj.get("logonToken").toString().trim().length() > 0) {

				int reportId = 0;
				int reportName = 0;
				if (wpId.trim().contains(","))
					wpID = wpId.trim().split("\\,");
				else {
					wpID = new String[1];
					wpID[0] = wpId.trim();
				}
				String wpIdName = "";

				for (int i = 0; i < wpID.length; i++) {

					wpIdName = wpID[i].trim();
					reportId = getReportID(wpIdName, jsonObj.get("logonToken").toString().trim(), instance);

					reportName = getReportName(wpIdName, jsonObj.get("logonToken").toString().trim(), instance);
					// TODO Remove this check
					if (reportName == 0)
						reportName = Integer.parseInt(wpIdName);

					downloadSapBOFile(jsonObj.get("logonToken").toString().trim(), String.valueOf(reportName), reportId,
							instance, wpIdName, columnCount);

				}

			} /*
			 * else if(wrong pswd verify) { //TODO mail for logon token }
			 */
			logoff(jsonObj.get("logonToken").toString().trim(), instance);
		}
		return true;
	}

	private int getReportName(String reportName, String token, String instance)
			throws NumberFormatException, JSONException {

		final String uri = "https://" + instance + "-int.erisite.ericsson.net/biprws/infostore/" + reportName.trim()
		+ "/children";
		LOG.info("uri :" + uri);
		int reportId = 0;
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		headers.set("Content-Type", "application/json");
		headers.set("X-SAP-LogonToken", "\"" + token + "\"");

		HttpEntity<String> request = new HttpEntity<>(headers);

		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
			if (response.getStatusCodeValue() == AppConstants.SAP_BO_RESPONSE_CODE) {
				LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + response.getStatusCodeValue());
				if (StringUtils.isBlank(response.getBody())) {
					String status = "No Data Found For ReportId:" + Integer.toString(reportId);
					sendMailForSAPBOFailure(instance, Integer.toString(reportId), status);
					LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + "-" + status);
				}
			} else {
				LOG.info(AppConstants.SAP_BO_FAILURE_RESPONSE + response.getStatusCodeValue());
				sendMailForSAPBOFailure(instance, Integer.toString(reportId), AppConstants.SAP_BO_FAILURE_RESPONSE);
			}
		} catch (RestClientException e) {
			sendMailForSAPBOFailure(instance, Integer.toString(reportId), AppConstants.SAP_BO_FAILURE_RESPONSE);
			//			e.printStackTrace();
			LOG.info(e.getMessage());
		}

		if (response != null) {
			JSONObject jsonObj = new JSONObject(response.getBody());
			// System.out.println("json Object: "+jsonObj);

			JSONArray jsonArray = null;
			JSONObject jsonObj1 = null;
			if (null != jsonObj.get("entries") && jsonObj.get("entries").toString().trim().length() > 0) {

				jsonArray = jsonObj.getJSONArray("entries");

				if (jsonArray.length() > 0) {

					jsonObj1 = jsonArray.getJSONObject(jsonArray.length() - 1);

					if (null != jsonObj1.get("id") && jsonObj1.get("id").toString().trim().length() > 0) {
						reportId = Integer.parseInt(jsonObj1.get("id").toString());
					}
				}
			}
		}
		return reportId;
	}

	private void sendMailForSAPBOFailure(String instance, String reportId, String status) {
		if (configurations.getBooleanProperty(ConfigurationFilesConstants.SAPBO_MAILER_ENABLED, false)) {
			emailService.sendMail(AppConstants.SAP_BO_FAILURE, enrichMailforAPIFailure(reportId, instance, status));
			LOG.info("SAP BO Mail Sent Succcessfully");
		}
	}

	private int getReportID(String reportName, String token, String instance) {

		final String uri = "https://" + instance + "-int.erisite.ericsson.net/biprws/raylight/v1/documents/"
				+ reportName.trim() + "/reports";
		LOG.info("uri :" + uri);
		int reportId = 0;
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		headers.set("Content-Type", "application/json");
		headers.set("X-SAP-LogonToken", "\"" + token + "\"");

		HttpEntity<String> request = new HttpEntity<>(headers);

		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
			if (response.getStatusCodeValue() == AppConstants.SAP_BO_RESPONSE_CODE) {
				LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + response.getStatusCodeValue());
				if (StringUtils.isBlank(response.getBody())) {
					String status = "No Data Found For ReportId:" + reportName;
					sendMailForSAPBOFailure(instance, reportName, status);
					LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + "-" + status);
				}
			} else {
				LOG.info(AppConstants.SAP_BO_FAILURE_RESPONSE + response.getStatusCodeValue());
				sendMailForSAPBOFailure(instance, reportName, AppConstants.SAP_BO_FAILURE_RESPONSE);
			}
		} catch (RestClientException e) {
			sendMailForSAPBOFailure(instance, reportName, AppConstants.SAP_BO_FAILURE_RESPONSE);
			LOG.info(e.getMessage());
			//			e.printStackTrace();
		}

		if (response != null) {
			JSONObject jsonObj = new JSONObject(response.getBody());
			JSONArray jsonArray = null;
			JSONObject jsonObj1 = null;
			JSONObject jsonObj2 = null;
			if (null != jsonObj.get("reports") && jsonObj.get("reports").toString().trim().length() > 0) {
				jsonObj2 = (JSONObject) jsonObj.get("reports");
				if (null != jsonObj2.get("report") && jsonObj2.get("report").toString().trim().length() > 0) {
					try {
						jsonObj1 = (JSONObject) jsonObj2.get("report");
						if (null != jsonObj1.get("id") && jsonObj1.get("id").toString().trim().length() > 0) {
							reportId = Integer.parseInt(jsonObj1.get("id").toString());
						}
					} catch (Exception e) {
						LOG.info("Json Structure error" + e.getMessage());
					} finally {
						jsonArray = jsonObj2.getJSONArray("report");
						if (jsonArray.length() > 0) {
							jsonObj1 = jsonArray.getJSONObject(jsonArray.length() - 1);

							if (null != jsonObj1.get("id") && jsonObj1.get("id").toString().trim().length() > 0) {
								reportId = Integer.parseInt(jsonObj1.get("id").toString());
							}
						}
					}
					// }
				}
			}
		}
		return reportId;
	}

	private boolean downloadSapBOFile(String token, String reportName, int reportId, String instance, String wpIdName,
			int columnCount) {

		final String uri = "https://" + instance + "-int.erisite.ericsson.net/biprws/raylight/v1/documents/"
				+ reportName + "/reports/" + reportId;

		LOG.info("uri :" + uri);
		boolean fileDownload = false;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "text/csv");
		headers.set("Content-Type", "application/json");
		headers.set("X-SAP-LogonToken", "\"" + token + "\"");

		HttpEntity<String> request = new HttpEntity<>(headers);

		ResponseEntity<byte[]> response = null;
		try {
			response = restTemplate.exchange(uri, HttpMethod.GET, request, byte[].class, "File5.iso");
			if (response.getStatusCodeValue() == AppConstants.SAP_BO_RESPONSE_CODE) {
				LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + response.getStatusCodeValue());
				if (response.getBody() == null) {
					String status = "No Data Found For ReportId:" + Integer.toString(reportId);
					sendMailForSAPBOFailure(instance, Integer.toString(reportId), status);
					LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + "-" + status);
				}

			} else {
				LOG.info(AppConstants.SAP_BO_FAILURE_RESPONSE + response.getStatusCodeValue());
				sendMailForSAPBOFailure(instance, Integer.toString(reportId), AppConstants.SAP_BO_FAILURE_RESPONSE);
			}
		} catch (RestClientException e) {
			//			e.printStackTrace();
			sendMailForSAPBOFailure(instance, Integer.toString(reportId), AppConstants.SAP_BO_FAILURE_RESPONSE);
			LOG.info(e.getMessage());
		}
		if (response != null) {
			fileDownload = true;
			try {
				if (dbFileUploadForSAPBO(// environment.getRequiredProperty("erisite.sapbo.basefolder"),
						response.getBody(), wpIdName + "_" + reportId, instance, columnCount)) {

					LOG.info("File Upload: Success");
				} else {
					LOG.info("File Upload: Failed");
				}
			} catch (Exception e) {
				LOG.info("File Upload: Failed");
			}
		}
		return fileDownload;
	}

	public boolean logoff(String token, String instance) {
		final String uri = "https://" + instance + "-int.erisite.ericsson.net/biprws/logoff";

		boolean logOff = false;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Accept", "application/json");
		headers.set("X-SAP-LogonToken", token);

		HttpEntity<String> request = new HttpEntity<>(headers);

		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
			if (response.getStatusCodeValue() == AppConstants.SAP_BO_RESPONSE_CODE) {
				LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + response.getStatusCodeValue());
				if (StringUtils.isBlank(response.getBody())) {
					String status = "No Data Found";
					sendMailForSAPBOFailure(instance, StringUtils.EMPTY, status);
					LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + "-" + status);
				}
			} else {
				LOG.info(AppConstants.SAP_BO_FAILURE_RESPONSE + response.getStatusCodeValue());
				sendMailForSAPBOFailure(instance, StringUtils.EMPTY, AppConstants.SAP_BO_FAILURE_RESPONSE);

			}
			logOff = true;
		} catch (RestClientException e) {
			sendMailForSAPBOFailure(instance, StringUtils.EMPTY, AppConstants.SAP_BO_FAILURE_RESPONSE);
			LOG.info(e.getMessage());
			//			e.printStackTrace();
		}

		LOG.info("Logoff success :");

		return logOff;
	}

	public List<Map<String, Object>> getAllExternalProjectIDByIsfProject(int isfProjectID, int sourceID) {
		return this.erisiteManagmentDAO.getAllExternalProjectIDByIsfProject(isfProjectID, sourceID);
	}

	public ResponseEntity<Response<List<Map<String, Object>>>> getExternalProjectIDByIsfProject(int isfProjectID, int sourceID) {
		Response<List<Map<String, Object>>> result = new Response<>();
		try {
			LOG.info("getExternalProjectIDByIsfProject:Start");
			validationUtilityService.validateIntForZero(isfProjectID, AppConstants.PROJECT_ID);
			validationUtilityService.validateIntForZero(sourceID, AppConstants.SOURCE_ID);
			List<Map<String, Object>> externalProjectIDByIsfProjectList = this.erisiteManagmentDAO.getExternalProjectIDByIsfProject(isfProjectID, sourceID);

			if (externalProjectIDByIsfProjectList.isEmpty()) {
				result.addFormMessage(NO_DATA_FOUND);
				result.setResponseData(externalProjectIDByIsfProjectList);
			} else {
				result.setResponseData(externalProjectIDByIsfProjectList);
			}

			LOG.info("getExternalProjectIDByIsfProject:End");
		}
		catch (ApplicationException exe) {
			result.addFormMessage(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	public List<Map<String, Object>> getAllExternalActivityList(String sourceID) {
		return this.erisiteManagmentDAO.getAllExternalActivityList(sourceID);
	}

	private boolean dbFileUploadForSAPBO(byte[] byteArray, String dir, String instance, int columnCount)
			throws  IOException {
		boolean done = false;
		String fileName = "SAP_BO_" + new Date().getTime() + "_" + dir;
		String dirPath = appService.getConfigUploadFilePath() + "SAPBO/";
		String filePath = dirPath + fileName + ".csv";
		Path path = null;

		try {
			byte[] dataFile = AppUtil.removeLeadingBlankLine(byteArray);
			Path dirpath = Paths.get(dirPath);
			boolean dirExists = Files.exists(dirpath);
			String tableName;
			if (!dirExists) {
				// Creating The New Directory Structure
				Files.createDirectories(dirpath);
			}
			path = Paths.get(filePath);
			Files.write(path, dataFile);

			if (columnCount == 5) {
				tableName = AppConstants.SAPBOTMP_TABLE_5COLUMNS;
			} else if (columnCount == 11) {
				tableName = AppConstants.SAPBOTMP_TABLE_11COLUMNS;
			} else {
				return false;
			}

			boolean isBulkUploadSuccess = appService.CsvBulkUploadSAPBOReport(filePath, tableName, fileName,
					AppConstants.SEMICOLON, instance);
			if (isBulkUploadSuccess) {
				erisiteManagmentDAO.dbFileInsertForSAPBO(fileName, instance, columnCount);
				done = true;
			}
		} catch (IOException | NumberFormatException | SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				appService.dropTable(fileName);
				Files.delete(path);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return done;
	}

	public List<HashMap<String, Object>> downloadWorkInstructionFile(String fileName) {
		return this.erisiteManagmentDAO.downloadWorkInstructionFile(fileName);
	}

	public Map<String, Object> enrichMailforAPIFailure(String reportid, String instance, String status) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (StringUtils.isEmpty(reportid)) {
			data.put("reportid", "logon failed");
		} else {

			data.put("reportid", reportid);
		}
		data.put("instance", instance);
		data.put("status", status);
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, AppConstants.ISF_SUPPORT_TEAM);
		return data;
	}

	public void createMailerForSAP(String reportid, String instance, String status) {

		sendMailForSAPBOFailure(instance, reportid, status);
		LOG.info("Success");

	}

	public List<Map<String, Object>> getAllExternalSource(boolean b) {
		if(b) {
			return erisiteManagmentDAO.getAllExternalSourceErrorDictionary();
		}
		return erisiteManagmentDAO.getAllExternalSource();
	}

	public List<String> getAllowedApiListForExternalSource(String externalSourceName) {
		return erisiteManagmentDAO.getAllowedApiListForExternalSource(externalSourceName);
	}

	public String getActiveToken(String externalSourceName) {
		return accessManagementService.getActiveToken(externalSourceName);
	}

	/**
	 * 
	 * @param apiName
	 * @param woID
	 * @param taskName
	 * @return Response<Map<String, Object>>
	 */
	public ResponseEntity<Response<Map<String, Object>>> startTask(final ServerBotModel serverBotModel, String apiName)
			throws Exception {

		Response<Map<String, Object>> apiResponse = new Response<Map<String, Object>>();
		try {
			int woID = serverBotModel.getwOID();
			WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(woID);
			if(woDetails==null) {
				throw new ApplicationException(200,"No work order details found for this work order id");
			}
			validateServerBotModelForStartTask(serverBotModel, woDetails, apiName);
			String executionType=getExecutionType(serverBotModel,woDetails);
			ServerBotModel finalServerBotModel = prepareServerBotModel(serverBotModel, woDetails,executionType);

			Map<String, Object> map = woExecutionService.startTask(null,
					finalServerBotModel, null);
			apiResponse.setResponseData(map);

			if (map.containsKey(IS_API_SUCCESS) && (boolean) map.get(IS_API_SUCCESS)) {

				auditDataProcessor.addStartTaskToAudit(finalServerBotModel);
			}
			LOG.info("startTask : Success");

		}catch (ApplicationException e) {
			LOG.info("Error in starttask:" + e.getMessage());
			LOG.error(e.getMessage());
			apiResponse.addFormError(e.getMessage());
			return new ResponseEntity<Response<Map<String, Object>>>(apiResponse, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			apiResponse.addErrorsDetail("500", "INTERNAL_SERVER_ERROR");
			apiResponse.addFormError(e.getMessage());
			return new ResponseEntity<Response<Map<String, Object>>>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Response<Map<String, Object>>>(apiResponse, HttpStatus.OK);
	}



	private void validateServerBotModelForStartTask(ServerBotModel serverBotModel, WorkOrderModel woDetails, String apiName) throws Exception{
		
		apiName=(apiName!= null)?apiName:"/externalInterface/startTask";

		List<String> json = workOrderPlanDao.getValidateJsonForApi(apiName, serverBotModel.getExternalSourceName());
		if (CollectionUtils.isEmpty(json)) {

			throw new ApplicationException(500, "Invalid source Name or validateJson found!");
		}

		JSONObject obj = new JSONObject(json.get(0));
		JSONArray array = obj.getJSONArray(VALIDATIONS);

		if (array == null || array.length() == 0) {
			throw new ApplicationException(500, "Given Source name has invalid validateJson!");
		}

		for(int i = 0; i < array.length(); i++) {

			switch (String.valueOf(array.get(i))) {

			case WOID_NULL_OR_ZERO:
				validationUtilityService.validateWOIDForNullOrZero(serverBotModel.getwOID(), woDetails);
				break;

			case WOID_ACTIVE:
				// validate WOID is Active
				validationUtilityService.validateWOIDIsActive(woDetails);
				break;

			case WO_UNASSIGNED:
				// validate If Work order Is Unassigned
				validationUtilityService.validateIfWorkorderIsUnassigned(woDetails);
				break;

			case WOID_CLOSED_DEFERRED_REJECTED:
				validationUtilityService.validateWOIDForClosedDeferredAndRejectedStatus(woDetails);
				break;

			case EXTERNAL_SOURCE_NAME_WITH_WOID:
				validationUtilityService.validateExternalSourceNameWithWoID(serverBotModel.getExternalSourceName(),
						woDetails);
				break;

			case STRING_BLANK_STEPID:
				validationUtilityService.validateStringForBlank(serverBotModel.getStepID(), "step ID");
				break;

			case INTEGER_NULL_OR_ZERO_TASKID:
				validationUtilityService.validateIntegerForNullOrZero(serverBotModel.getTaskID(), TASK_ID);
				break;

			case TASKID_FOR_START_TASK:
				validationUtilityService.validateTaskIDForStartTask(serverBotModel, woDetails);
				break;

			case "validateIFPreviousStepCompleted":
				validationUtilityService.validateIFPreviousStepCompleted(serverBotModel, woDetails);
				break;
			}
		}
		//commented as same validation is added in woExecution service start task method
		//validationUtilityService.validateParallelWorkOrderExecution(serverBotModel, woDetails);
		validationUtilityService.validateCurrentStepForStart(serverBotModel, woDetails);
	}

	/*
	 * private void validateTaskIDForStartTask(ServerBotModel serverBotModel,
	 * WorkOrderModel woDetails) {
	 * 
	 * if (serverBotModel.getSubActivityFlowChartDefID() == null ||
	 * serverBotModel.getSubActivityFlowChartDefID() == 0) {
	 * 
	 * serverBotModel.setSubActivityFlowChartDefID(woDetails.getFlowchartdefid()); }
	 * 
	 * JSONObject obj = new JSONObject(json.get(0)); JSONArray array =
	 * obj.getJSONArray(VALIDATIONS);
	 * 
	 * if (array == null || array.length() == 0) { throw new
	 * ApplicationException(500, "Given Source name has invalid validateJson!"); }
	 * 
	 * for(int i = 0; i < array.length(); i++) {
	 * 
	 * switch (String.valueOf(array.get(i))) {
	 * 
	 * case WOID_NULL_OR_ZERO:
	 * validationUtilityService.validateWOIDForNullOrZero(serverBotModel.getwOID(),
	 * woDetails); break;
	 * 
	 * case WOID_ACTIVE: // validate WOID is Active
	 * validationUtilityService.validateWOIDIsActive(woDetails); break;
	 * 
	 * case WO_UNASSIGNED: // validate If Work order Is Unassigned
	 * validationUtilityService.validateIfWorkorderIsUnassigned(woDetails); break;
	 * 
	 * case WOID_CLOSED_DEFERRED_REJECTED:
	 * validationUtilityService.validateWOIDForClosedDeferredAndRejectedStatus(
	 * woDetails); break;
	 * 
	 * case EXTERNAL_SOURCE_NAME_WITH_WOID:
	 * validationUtilityService.validateExternalSourceNameWithWoID(serverBotModel.
	 * getExternalSourceName(), woDetails); break;
	 * 
	 * case STRING_BLANK_STEPID:
	 * validationUtilityService.validateStringForBlank(serverBotModel.getStepID(),
	 * "step ID"); break;
	 * 
	 * case INTEGER_NULL_OR_ZERO_TASKID:
	 * validationUtilityService.validateIntegerForNullOrZero(serverBotModel.
	 * getTaskID(), TASK_ID); break;
	 * 
	 * case TASKID_FOR_START_TASK:
	 * validationUtilityService.validateTaskIDForStartTask(serverBotModel,
	 * woDetails); break;
	 * 
	 * case PREVIOUS_STEP_COMPLETED:
	 * validationUtilityService.validateIFPreviousStepCompleted(serverBotModel,
	 * woDetails); break; } } }
	 */

	private void validateAndSetServerBotModelForStopTask(ServerBotModel serverBotModel, WorkOrderModel woDetails,
			String apiName, String executionType) {

		apiName = (apiName != null) ? apiName : "/externalInterface/stopTask";

		List<String> json = workOrderPlanDao.getValidateJsonForApi(apiName, serverBotModel.getExternalSourceName());
		if (CollectionUtils.isEmpty(json)) {

			throw new ApplicationException(500, "Invalid source Name or validateJson found!");
		}

		JSONObject obj = new JSONObject(json.get(0));
		JSONArray array = obj.getJSONArray(VALIDATIONS);

		if (array == null || array.length() == 0) {
			throw new ApplicationException(500, "Given Source name has invalid validateJson!");
		}

		for(int i = 0; i < array.length(); i++) {
			switch (String.valueOf(array.get(i))) {

			case WOID_NULL_OR_ZERO:
				validationUtilityService.validateWOIDForNullOrZero(serverBotModel.getwOID(), woDetails);
				break;

			case WO_ASSIGNED_TO_SIGNUM:
				validationUtilityService.validateIfWorkOrderAssignedToSignum(serverBotModel.getSignumID(), woDetails);
				break;

			case WOID_ACTIVE:
				// validate WOID is Active
				validationUtilityService.validateWOIDIsActive(woDetails);
				break;

			case WO_UNASSIGNED:
				// validate If Work order Is Unassigned
				validationUtilityService.validateIfWorkorderIsUnassigned(woDetails);
				break;

			case WOID_CLOSED_DEFERRED_REJECTED:
				validationUtilityService.validateWOIDForClosedDeferredAndRejectedStatus(woDetails);
				break;

			case EXTERNAL_SOURCE_NAME_WITH_WOID:
				validationUtilityService.validateExternalSourceNameWithWoID(serverBotModel.getExternalSourceName(), woDetails);
				break;

			case INTEGER_NULL_OR_ZERO_TASKID:
				validationUtilityService.validateIntegerForNullOrZero(serverBotModel.getTaskID(), "task ID");
				break;

			case "validateTaskDetails":
				validateTaskDetails(serverBotModel.getTaskID(), woDetails.getFlowchartdefid(), woDetails.getWfVersion());
				break;

			case "validateReasonForStopTask":
				validateReasonForStopTask(serverBotModel);
				break;

			case "validateFailureReasonFromMasterData":
				validationUtilityService.validateFailureReasonFromMasterData(serverBotModel.getReason().trim(),executionType);
				break;
			}

		}

	}

	private void validateReasonForStopTask(ServerBotModel serverBotModel) {

		if (!StringUtils.equalsIgnoreCase(AppConstants.GNET, serverBotModel.getExternalSourceName())) {
			validationUtilityService.validateStringForBlank(serverBotModel.getReason(), "reason");
		} else {

			if (StringUtils.isBlank(serverBotModel.getReason())) {
				serverBotModel.setReason("GNET : No Response Received from Bot");
			}

			if (StringUtils.isBlank(serverBotModel.getStatus())) {
				serverBotModel.setStatus(AppConstants.SUCCESS);
			}

		}
		validationUtilityService.validateLength(serverBotModel.getReason(), 500, AppConstants.REASON);
	}

	private void validateTaskDetails(Integer taskID, int flowchartdefid, int wfVersion) {
		boolean taskValid = erisiteManagmentDAO.validateTaskDetails(taskID, flowchartdefid, wfVersion);
		if (!taskValid) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, "valid task ID"));
		}

	}

	private void validateSaveClosureDetailsForWOModelForCloseWo(SaveClosureDetailsForWOModel saveClosureDetailsObject,
			WorkOrderModel woDetails) {

		validationUtilityService.validateWOIDForNullOrZero(saveClosureDetailsObject.getwOID(), woDetails);

		// validate WOID is Active
		validationUtilityService.validateWOIDIsActive(woDetails);

		// validate If Work order Is Unassigned
		validationUtilityService.validateIfWorkorderIsUnassigned(woDetails);

		validationUtilityService.validateWOIDForClosedDeferredAndRejectedStatus(woDetails);

		// validate LastModifiedBy
		validationUtilityService.validateSignumIDForBlankAndUnassigned(saveClosureDetailsObject.getLastModifiedBy(), woDetails, "lastModifiedBy");

		validationUtilityService.validateSource(saveClosureDetailsObject.getExternalSourceName());

		//validationUtilityService.validateExternalSourceNameWithWoID(saveClosureDetailsObject.getExternalSourceName(), woDetails);


		validateDeliveryStatus(saveClosureDetailsObject.getDeliveryStatus());


		validateReason(saveClosureDetailsObject.getDeliveryStatus(),saveClosureDetailsObject.getReason());



		validateIfAllStepsCompleted(saveClosureDetailsObject.getDeliveryStatus(), woDetails);

	}

	private void validateReason(String deliveryStatus, String reason) {

		if (StringUtils.equalsIgnoreCase(deliveryStatus, AppConstants.FAILURE)) {

			if (StringUtils.isBlank(reason)) {

				throw new ApplicationException(500,
						String.format(PLEASE_PROVIDE_VALUE, "Reason for failure deliveryStatus"));
			} else {

				validationUtilityService.validateLength(reason.trim(), 500,AppConstants.REASON);
				validationUtilityService.validateFailureReason(reason.trim(),AppConstants.DELIVERY_FAILURE);
			}

		} else if (StringUtils.equalsIgnoreCase(deliveryStatus, AppConstants.SUCCESS)) {

			if (StringUtils.isNotBlank(reason)) {

				throw new ApplicationException(500, "Reason is not required for success deliveryStatus");
			}

		}


	}

	private void validateIfAllStepsCompleted(String deliveryStatus, WorkOrderModel woDetails) {

		if (StringUtils.equalsIgnoreCase(deliveryStatus, AppConstants.SUCCESS)) {

			List<Map<String, Object>> completedStepsData = woExecutionDAO.checkIFLastStepNew(woDetails.getwOID(),
					woDetails.getFlowchartdefid());
			if (CollectionUtils.isEmpty(completedStepsData)) {

				throw new ApplicationException(500, "Please complete all the tasks of this work order to close this.");
			}
		}

	}

	private void validateDeliveryStatus(String deliveryStatus) {

		if (StringUtils.isBlank(deliveryStatus)) {

			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, "deliveryStatus"));

		} else if (!StringUtils.equalsIgnoreCase(deliveryStatus, AppConstants.FAILURE)
				&& !StringUtils.equalsIgnoreCase(deliveryStatus, AppConstants.SUCCESS)) {

			throw new ApplicationException(500,
					String.format(PLEASE_PROVIDE_VALUE, "deliveryStatus as Success/Failure"));
		}

	}

	/**
	 * 
	 * @param woDetails
	 * @param executionType
	 * @param woID
	 * @param taskName
	 * @return ServerBotModel
	 */
	private ServerBotModel prepareServerBotModel(ServerBotModel serverBotModel, WorkOrderModel woDetails,
			String executionType) {

		int woID = serverBotModel.getwOID();

		int subActivityFlowChartDefID = woDetails.getFlowchartdefid();
		int versionNo = rpaDAO.getWOVersionNo(woID);
		if (subActivityFlowChartDefID == 0) {

			Map<String, Integer> data = rpaDAO.getProjAndSubActivityID(woID);
			Integer subActivityID = data.get(SUB_ACTIVITY_ID);
			Integer projectID = data.get(AppConstants.PROJECT_ID_2);
			subActivityFlowChartDefID = rpaDAO.getFlowChartDefID(projectID, subActivityID, versionNo);
		}


		ServerBotModel finalServerBotModel = new ServerBotModel(serverBotModel.getwOID(), woDetails.getSignumID(),
				serverBotModel.getTaskID(), executionType, AppConstants.BOOKING, subActivityFlowChartDefID,
				StringUtils.EMPTY, AppConstants.LOCAL, serverBotModel.getReason(), serverBotModel.getStepID(),
				serverBotModel.getExternalSourceName(), serverBotModel.getStatus());

		return finalServerBotModel;

		/*// check if(GNET)
		if (StringUtils.equalsIgnoreCase(serverBotModel.getExternalSourceName(), "GNET")) {
			ServerBotModel finalServerBotModel = new ServerBotModel(serverBotModel.getwOID(), woDetails.getSignumID(),
					serverBotModel.getTaskID(), AppConstants.AUTOMATIC, AppConstants.BOOKING, subActivityFlowChartDefID,
					StringUtils.EMPTY, AppConstants.LOCAL, serverBotModel.getReason(), serverBotModel.getStepID(),
					serverBotModel.getExternalSourceName(), serverBotModel.getStatus());

		/*
		 * // check if(GNET) if
		 * (StringUtils.equalsIgnoreCase(serverBotModel.getExternalSourceName(),
		 * "GNET")) { ServerBotModel finalServerBotModel = new
		 * ServerBotModel(serverBotModel.getwOID(), woDetails.getSignumID(),
		 * serverBotModel.getTaskID(), AppConstants.AUTOMATIC, AppConstants.BOOKING,
		 * subActivityFlowChartDefID, StringUtils.EMPTY, AppConstants.LOCAL,
		 * serverBotModel.getReason(), serverBotModel.getStepID(),
		 * serverBotModel.getExternalSourceName(), serverBotModel.getStatus());
		 * 
		 * return finalServerBotModel; } else { String executionType =
		 * StringUtils.EMPTY; String bookingType = StringUtils.EMPTY;
		 * 
		 * if (StringUtils.isBlank(serverBotModel.getExecutionType()) ||
		 * StringUtils.isBlank(serverBotModel.getBookingType())) {
		 * 
		 * List<Map<Object, String>> serverModelData =
		 * rpaDAO.getServerModelData(serverBotModel.getwOID(), woDetails.getSignumID());
		 * executionType = serverModelData.get(0).get("ExecutionType"); bookingType =
		 * serverModelData.get(0).get("Type");
		 * 
		 * } if (StringUtils.isBlank(serverBotModel.getRefferer())) {
		 * 
		 * throw new ApplicationException(500, "Please provide refferer"); }
		 * ServerBotModel finalServerBotModel = new
		 * ServerBotModel(serverBotModel.getwOID(), woDetails.getSignumID(),
		 * serverBotModel.getTaskID(), executionType, bookingType,
		 * subActivityFlowChartDefID, StringUtils.EMPTY, AppConstants.LOCAL,
		 * serverBotModel.getReason(), serverBotModel.getStepID(),
		 * serverBotModel.getExternalSourceName(), serverBotModel.getStatus());
		 * 
		 * return finalServerBotModel; }
		 *

			if (StringUtils.isBlank(serverBotModel.getExecutionType()) || StringUtils.isBlank(serverBotModel.getBookingType())) {

				List<Map<Object, String>> serverModelData = rpaDAO.getServerModelData(serverBotModel.getwOID(),
						woDetails.getSignumID());
				executionType = serverModelData.get(0).get("ExecutionType");
				bookingType = serverModelData.get(0).get("Type");

			}
			if (StringUtils.isBlank(serverBotModel.getRefferer())) {

				throw new ApplicationException(500, "Please provide refferer");
			}
			ServerBotModel finalServerBotModel = new ServerBotModel(serverBotModel.getwOID(), woDetails.getSignumID(),
					serverBotModel.getTaskID(), executionType, bookingType, subActivityFlowChartDefID,
					StringUtils.EMPTY, AppConstants.LOCAL, serverBotModel.getReason(), serverBotModel.getStepID(),
					serverBotModel.getExternalSourceName(), serverBotModel.getStatus());

			return finalServerBotModel;
		}*/


	}




	/**
	 * 
	 * @param apiName
	 * @param woID
	 * @param taskName
	 * @param reason
	 * @return Response<Map<String, Object>>
	 */
	public ResponseEntity<Response<Map<String, Object>>> stopTask(ServerBotModel serverBotModel, String apiName)
			throws Exception {

		Response<Map<String, Object>> apiResponse = new Response<Map<String, Object>>();
		try {
			Integer woId = serverBotModel.getwOID();
			if (woId == null) {
				throw new ApplicationException(200, "WoId found 'null' Expected java.lang.Integer");
			}

			WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(woId);
			if (woDetails == null) {
				throw new ApplicationException(200, "Work order details found null for this work order id");
			}
			String executionType = getExecutionType(serverBotModel, woDetails);
			validateAndSetServerBotModelForStopTask(serverBotModel, woDetails, apiName, executionType);
			serverBotModel.setReason(serverBotModel.getReason().trim());
			ServerBotModel finalServerBotModel = prepareServerBotModel(serverBotModel, woDetails,executionType);

			Map<String, Object> map = woExecutionService.stopWorkOrderTask(finalServerBotModel);
			apiResponse.setResponseData(map);

			if (map.containsKey(AppConstants.IS_SUCCESS) && (boolean) map.get(AppConstants.IS_SUCCESS)) {

				auditDataProcessor.addStopTaskToAudit(finalServerBotModel);
			}
			LOG.info("stopTask: Success");

		}catch (ApplicationException e) {
			LOG.error(e.getMessage());
			apiResponse.addFormError(e.getMessage());
			return new ResponseEntity<Response<Map<String, Object>>>(apiResponse, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			apiResponse.addErrorsDetail("500", "INTERNAL_SERVER_ERROR");
			apiResponse.addFormError(e.getMessage());
			return new ResponseEntity<Response<Map<String, Object>>>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response<Map<String, Object>>>(apiResponse, HttpStatus.OK);
	}

	private String getExecutionType(ServerBotModel serverBotModel, WorkOrderModel woDetails) {
		String executionType = StringUtils.EMPTY;

		if(StringUtils.isBlank(serverBotModel.getStepID())) {

			if (StringUtils.isBlank(serverBotModel.getExecutionType()) || StringUtils.isBlank(serverBotModel.getBookingType())) {

				List<Map<Object, String>> serverModelData = rpaDAO.getServerModelData(serverBotModel.getwOID(),
						woDetails.getSignumID());
				executionType = serverModelData.get(0).get("ExecutionType");

			}
		} else {

			Map<String, Object> stepData = flowChartDao.getStepExistingData(serverBotModel.getSubActivityFlowChartDefID(),
					serverBotModel.getStepID(), woDetails.getWfVersion());

			if (stepData == null) {

				throw new ApplicationException(500,
						"Step details does not exists for woID " + serverBotModel.getwOID());
			}
			Object exeType = stepData.get("ExecutionType");
			if(exeType == null) {

				throw new ApplicationException(500,
						"Step details does not exists for woID " + serverBotModel.getwOID());
			}

			executionType = String.valueOf(exeType);
		}

		return executionType;

	}



	/**
	 * 
	 * @param woID
	 * @param taskName
	 * @param reason
	 * @return Response<CreateWoResponse>
	 */
	public ResponseEntity<Response<CreateWoResponse>> closeWO(SaveClosureDetailsForWOModel saveClosureDetailsObject) {

		Response<CreateWoResponse> apiResponse = new Response<CreateWoResponse>();
		try {
			int woId = saveClosureDetailsObject.getwOID();
			if (woId == 0) {
				throw new ApplicationException(200, "WoId found 'null' Expected java.lang.Integer");
			}
			WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(woId);
			if (woDetails == null) {
				throw new ApplicationException(200, "Work order details found null for this work order id");
			}
			validateSaveClosureDetailsForWOModelForCloseWo(saveClosureDetailsObject, woDetails);
			if (StringUtils.isNotBlank(saveClosureDetailsObject.getReason())) {
				saveClosureDetailsObject.setReason(saveClosureDetailsObject.getReason().trim());
			}
			saveClosureDetailsObject.setwOName(woDetails.getwOName());
			saveClosureDetailsObject
					.setLastModifiedBy(StringUtils.upperCase(saveClosureDetailsObject.getLastModifiedBy()));
			apiResponse = woExecutionService.saveClosureDetailsForWO(saveClosureDetailsObject);

			if (apiResponse.getFormMessageCount() == 1) {

				auditDataProcessor.addSaveClosureWoToAudit(saveClosureDetailsObject);
			}
			LOG.info("closeWO: Success");
		} catch (ApplicationException e) {
			LOG.error(e.getMessage());
			apiResponse.addFormError(e.getMessage());
			return new ResponseEntity<Response<CreateWoResponse>>(apiResponse, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			apiResponse.addErrorsDetail("500", "INTERNAL_SERVER_ERROR");
			apiResponse.addFormError(e.getMessage());
			return new ResponseEntity<Response<CreateWoResponse>>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response<CreateWoResponse>>(apiResponse, HttpStatus.OK);
	}

	/**
	 * This method updates output name and url for work orders.
	 * 
	 * @param workOrderOutputFileModels
	 * @return Response<Void>
	 */
	public Response<Void> updateOutputFileLinkWO(List<WorkOrderOutputFileModel> workOrderOutputFileModels) {

		Response<Void> apiResponse = new Response<Void>();

		try {

			int count = 0;
			for (WorkOrderOutputFileModel workOrderOutputFileModel : workOrderOutputFileModels) {

				try {

					BeanUtils.trimAllStrings(workOrderOutputFileModel);

					// get work order details
					WorkOrderModel woDetails = workOrderPlanDao
							.getWorkOrderDetailsById(workOrderOutputFileModel.getWoid());

					// validate given details for updation
					validateWorkOrderOutputFileModel(workOrderOutputFileModel, woDetails);

					// update Output details
					saveOutputFileLinkWO(workOrderOutputFileModel);
					count++;
				} catch (Exception ex) {

					apiResponse.addFormError(WOID + workOrderOutputFileModel.getWoid() + ARROW + ex.getMessage());
					continue;
				}

			}

			if (count != 0) {
				apiResponse.addFormMessage(
						"OutputFileLink has been updated successfully for " + count + " work order(s)!");
			}
		} catch (Exception ex) {

			apiResponse.addFormError("Unexpected error occured while updating ouput link: " + ex.getMessage());
		}
		LOG.info("updateOutputFileLinkWO method Success");
		return apiResponse;
	}

	/**
	 * This method updates given Output details
	 * 
	 * @param woid
	 * @param workOrderOutputFileModel
	 */
	@Transactional("transactionManager")
	private void saveOutputFileLinkWO(WorkOrderOutputFileModel workOrderOutputFileModel) {


		// get existing output details for given woid
		String lastModifiedBy = StringUtils.upperCase(workOrderOutputFileModel.getLastModifiedBy());

		int noOfOutputFileInDB = 0;

		noOfOutputFileInDB = workOrderPlanDao.checkOutputFileCount(workOrderOutputFileModel);

		if(noOfOutputFileInDB + workOrderOutputFileModel.getFile().size() <= 25) {
			workOrderOutputFileModel.setCreatedBy(lastModifiedBy);
			workOrderOutputFileModel.setLastModifiedBy(lastModifiedBy);

			// insert given output details for given woid
			workOrderPlanDao.editOutputFile(workOrderOutputFileModel);
		} else {
			String dbUrlMsg = noOfOutputFileInDB + " URL is already added";
			String requiredUrlMsg = ". Only "+ (25-noOfOutputFileInDB) +" more can be added";

			if(noOfOutputFileInDB < 25) {
				throw new ApplicationException(500, dbUrlMsg + requiredUrlMsg + "(Max 25 URL). Please remove few URL from UI and try again.");
			}else {
				throw new ApplicationException(500, dbUrlMsg + "(Max 25 URL). Please remove few URL from UI and try again.");
			}
		}
	}

	/**
	 * This method validate given details for updation
	 * 
	 * @param workOrderOutputFileModel
	 * @param woDetails
	 * @throws Exception
	 */
	private void validateWorkOrderOutputFileModel(WorkOrderOutputFileModel workOrderOutputFileModel,
			WorkOrderModel woDetails) throws Exception {

		// validate woid
		validationUtilityService.validateWOIDForNullOrZero(workOrderOutputFileModel.getWoid(), woDetails);

		// validate woid is active
		validationUtilityService.validateWOIDIsActive(woDetails);

		if (StringUtils.isBlank(workOrderOutputFileModel.getLastModifiedBy())) {

			// validate LastModifiedBy if it it Blank
			validationUtilityService.validateSignumIDForBlankLastModifiedByORUnassignedWO(workOrderOutputFileModel,
					woDetails);
		} else {

			// validate LastModifiedBy for UnassignedWO
			validationUtilityService.validateSignumIDForBlankAndUnassignedWOforOutputUrl(woDetails,
					workOrderOutputFileModel);
		}
		// validate Output Url & Output Name
		validateWoOutputFileModel(workOrderOutputFileModel, woDetails);
	}

	/**
	 * This method validate for Output Url & Output Name
	 * 
	 * @param workOrderOutputFileModel
	 * @throws Exception
	 */
	private void validateWoOutputFileModel(WorkOrderOutputFileModel workOrderOutputFileModel, WorkOrderModel woDetails)
			throws Exception {

		List<WOOutputFileModel> woOutputFileModels = workOrderOutputFileModel.getFile();
		if (CollectionUtils.isEmpty(woOutputFileModels)) {

			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, "Output Url & Output Name"));
		} else {

			List<String> outputNames = new ArrayList<String>();
			for (WOOutputFileModel woOutputFileModel : woOutputFileModels) {

				BeanUtils.trimAllStrings(woOutputFileModel);

				String outputName = woOutputFileModel.getOutputName();
				String outputUrl = woOutputFileModel.getOutputUrl();

				if (StringUtils.isBlank(outputName) || StringUtils.isBlank(outputUrl)) {

					throw new ApplicationException(500,
							String.format(PLEASE_PROVIDE_VALUE, "Valid Output Url & Output Name"));
				} else {

					validationUtilityService.validateLength(outputName, 500, "OutputName");
					validationUtilityService.validateLength(outputUrl, 2000, "OutputUrl");
					outputNames.add(outputName);
					validateOutputUrl(outputUrl, woDetails.getProjectid());
				}

			}
		}
	}

	private void validateOutputName(WOOutputFileModel woOutputFileModel, int woid) {

		List<WorkOrderOutputFileModel> workOrderOutputFileModels = workOrderPlanService
				.getWOOutputFileDetailsByWoIDAndWoOutputFileModel(woOutputFileModel, woid);

		if (CollectionUtils.isNotEmpty(workOrderOutputFileModels)) {

			throw new ApplicationException(500,
					woOutputFileModel.getOutputName() + " is already present. Please Change the output Name!!");
		}

	}

	private void validateOutputUrl(String outputUrl, int projectId) {

		Map<String, Object> map = activityMasterService.isValidTransactionalData(outputUrl, projectId);

		if (map.containsKey(AppConstants.RESULT)) {

			if (!(boolean) map.get(AppConstants.RESULT)) {

				String errorMsg = StringUtils.EMPTY;

				if (map.containsKey(AppConstants.MESSAGE)) {

					errorMsg = (String) map.get(AppConstants.MESSAGE);
				} else {

					errorMsg = String.format(PLEASE_PROVIDE_VALUE, "Valid Output Url");
				}

				throw new ApplicationException(500, errorMsg);
			}
		} else {

			throw new ApplicationException(500, "Unable to validate Output Url!");
		}

	}

	/**
	 * 
	 * @param workOrderModels
	 * @return
	 */
	public Response<Void> addCommentAndUpdatePriority(List<WorkOrderModel> workOrderModels) {

		Response<Void> apiResponse = new Response<Void>();

		try {

			int count = 0;
			for (WorkOrderModel workOrderModel : workOrderModels) {

				try {

					BeanUtils.trimAllStrings(workOrderModel);

					// get work order details
					WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(workOrderModel.getwOID());

					AuditDataModel auditDataModel = new AuditDataModel();

					// validate given details
					validateWorkOrderModel(workOrderModel, woDetails, auditDataModel);

					// insert Comment And Update Priority
					insertCommentAndUpdatePriority(workOrderModel, woDetails, auditDataModel);
					count++;
				} catch (Exception ex) {

					apiResponse.addFormError(WOID + workOrderModel.getwOID() + ARROW + ex.getMessage());
					continue;
				}

			}

			if (count != 0) {
				apiResponse.addFormMessage(
						"Comments/Priority has been added and updated successfully for " + count + " work order(s)!");
			}
		} catch (Exception ex) {

			apiResponse.addFormError(
					"Unexpected error occured while Adding & Updating Comments/Priority: " + ex.getMessage());
		}
		LOG.info("addCommentAndUpdatePriority method Success");
		return apiResponse;
	}

	/**
	 * This method inserts Comment And Update Priority
	 * 
	 * @param workOrderModel
	 * @param woDetails
	 * @param auditDataModel
	 */
	@Transactional("transactionManager")
	private void insertCommentAndUpdatePriority(WorkOrderModel workOrderModel, WorkOrderModel woDetails,
			AuditDataModel auditDataModel) {

		if (StringUtils.isNoneEmpty(workOrderModel.getPriority())) {
			// update Work Order Priority
			workOrderPlanDao.editWOPriority(workOrderModel.getwOID(), workOrderModel.getPriority(),
					StringUtils.upperCase(workOrderModel.getSignumID()));

		}
		// add Comments
		auditManagementService.addComment(getAuditDataModel(workOrderModel, woDetails, auditDataModel));

	}

	private AuditDataModel getAuditDataModel(WorkOrderModel workOrderModel, WorkOrderModel woDetails,
			AuditDataModel auditDataModel) {

		auditDataModel.setAuditPageId(workOrderModel.getwOID());
		auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
		auditDataModel
		.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER + AppConstants.UNDERSCORE + workOrderModel.getwOID());
		auditDataModel.setCreatedBy(workOrderModel.getSignumID());
		auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
		if (StringUtils.isBlank(workOrderModel.getComment())) {
			auditDataModel.setAdditionalInfo(AppConstants.WO_PRIORITY_COMMENT);
		} else {
			auditDataModel.setAdditionalInfo(workOrderModel.getComment());
		}

		if(!StringUtils.isBlank(workOrderModel.getPriority())){
			auditDataModel.setCommentCategory(AppConstants.WO_PRIORITY_EDIT);
			auditDataModel.setFieldName(AppConstants.PRIORITY);
			auditDataModel.setNewValue(workOrderModel.getPriority());
			auditDataModel.setOldValue(woDetails.getPriority());
		}

		auditDataModel.setType(AppConstants.AUDIT_TYPE_AUDIT);
		return auditDataModel;
	}

	private void validateWorkOrderModel(WorkOrderModel workOrderModel, WorkOrderModel woDetails,
			AuditDataModel auditDataModel) {

		// validate WOID
		validationUtilityService.validateWOIDForNullOrZero(workOrderModel.getwOID(), woDetails);

		// validate WOID is Active
		validationUtilityService.validateWOIDIsActive(woDetails);

		// validate wo id status
		validationUtilityService.validateWOIDForClosedAndRejectedStatus(woDetails);

		validateSignumIDForBlankAndPMDR(workOrderModel.getSignumID(), woDetails, AppConstants.SIGNUM_ID, auditDataModel);

		validatePriortyAndComment(workOrderModel.getComment(),workOrderModel.getPriority());

		validatePriortyAndComment(workOrderModel.getComment(), workOrderModel.getPriority());

	}

	private void validatePriortyAndComment(String comment, String priority) {
		if (StringUtils.isBlank(comment) && StringUtils.isBlank(priority)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, PRIORITY_COMMENT));
		}

		if(!StringUtils.isBlank(comment)) {
			validationUtilityService.validateLength(comment,1200,COMMENT);
		}

		if(!StringUtils.isBlank(priority)) {
			validationUtilityService.validatePriorityForEMS(priority);
		}

	}

	private void validateSignumIDForBlankAndPMDR(String signumID, WorkOrderModel woDetails, String variableName,
			AuditDataModel auditDataModel) {

		// validate signum for blank string
		validationUtilityService.validateStringForBlank(signumID, variableName);

		// validate signumID is valid against given woid
		String role = validateAndGetIfWorkOrderAssignedToPMorDR(signumID, woDetails);
		auditDataModel.setActorType(role);

	}

	private String validateAndGetIfWorkOrderAssignedToPMorDR(String signumID, WorkOrderModel woDetails) {

		String ProjectCreator = projectService.getManagerByProjectId(String.valueOf(woDetails.getProjectid()));

		if(StringUtils.equalsIgnoreCase(ProjectCreator, signumID)) {

			return AppConstants.ACTOT_TYPE_PM;
		} else {

			List<DeliveryResponsibleModel> deliveryResponsibleModels = projectService
					.getDeliveryResponsibleByProjectIDandSignum(woDetails.getProjectid(), signumID);
			if(CollectionUtils.isEmpty(deliveryResponsibleModels)) {

				throw new ApplicationException(500, "Signum " + signumID + " should be PM/DR to update priority!");
			} else {

				return AppConstants.ACTOT_TYPE_DR;
			}
		}

	}

	/**
	 * This method get Complete WorkOrder Details.
	 * 
	 * @param woIDs
	 * @param requiredParameters
	 * @return
	 */
	public Response<List<List<WorkOrderCompleteDetailsModel>>> getCompleteWorkOrderDetails(String woIDs,
			String requiredParameters) {

		Response<List<List<WorkOrderCompleteDetailsModel>>> apiResponse = new Response<List<List<WorkOrderCompleteDetailsModel>>>();

		try {

			String woIDArray[] = woIDs.split(AppConstants.CSV_CHAR_COMMA);
			List<List<WorkOrderCompleteDetailsModel>> workOrderCompleteDetailsModels = new ArrayList<List<WorkOrderCompleteDetailsModel>>();
			int count = 0;

			for (String woIDStr : woIDArray) {

				try {

					int woID = Integer.parseInt(woIDStr.trim());
					String columnNames = StringUtils.EMPTY;

					// get work order details
					WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(woID);

					// validate if woid is blank
					validateForGetCompleteWorkOrderDetails(woID, woDetails);

					if (StringUtils.isBlank(requiredParameters)
							|| StringUtils.equalsIgnoreCase(AppConstants.ALL, requiredParameters)
							|| StringUtils.equalsIgnoreCase("*", requiredParameters)) {
						columnNames = AppConstants.ALL;
					} else {

						columnNames = requiredParameters;
					}
					workOrderCompleteDetailsModels.add(woExecutionDAO.getCompleteWorkOrderDetails(woID, columnNames));
					count++;
				} catch (Exception ex) {

					String errorMsg = ex.getMessage();
					if (ex instanceof NumberFormatException) {
						errorMsg = "woid is not a number!";
					} else if (ex instanceof BadSqlGrammarException) {
						errorMsg = ex.getCause().getMessage();
					}

					apiResponse.addFormError(WOID + woIDStr + ARROW + errorMsg);
					continue;
				}

			}

			if (count != 0) {
				apiResponse.setResponseData(workOrderCompleteDetailsModels);
			}

		} catch (Exception ex) {

			apiResponse.addFormError("Unexpected error occured while getting WorkOrder Details: " + ex.getMessage());
		}
		LOG.info("getCompleteWorkOrderDetails method Success");
		return apiResponse;
	}

	private void validateForGetCompleteWorkOrderDetails(int woID, WorkOrderModel woDetails) {

		// validate woid
		validationUtilityService.validateWOIDForNullOrZero(woID, woDetails);

		// validate WOID is Active
		validationUtilityService.validateWOIDIsActive(woDetails);

	}

	public Response<Map<String, String>> updateBookingDetailsStatus(int wOID, String signumID, int taskID,
			int bookingID, String status, String reason, String stepid, int flowChartDefID, String refferer,
			String apiName) {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(wOID);
			validateUpdateBookingDetails(wOID,signumID,taskID,bookingID,status,reason,stepid,woDetails,flowChartDefID,StringUtils.trimToEmpty(refferer), apiName);

			response.setResponseData(woExecutionService.updateBookingDetailsStatus(wOID, signumID, taskID, bookingID, status, reason, stepid,flowChartDefID,StringUtils.trimToEmpty(refferer),Optional.empty()));
		}
		catch(Exception e) {
			response.addFormError(e.getMessage());
		}
		return response;
	}

	private void validateReasonAndStatusForBlank(String reason, String status) {

		if (StringUtils.isBlank(status)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, "status"));
		} else {
			if (StringUtils.isBlank(reason)) {
				throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, "reason"));
			}
		}
	}

	private void validateUpdateBookingDetails(int wOID, String signumID, int taskID, int bookingID, String status,
			String reason, String stepid, WorkOrderModel woDetails, int flowChartDefID, String refferer,String apiName) {

		validationUtilityService.validateIntegerForNullOrZero(wOID, "wOID");
		validationUtilityService.validateWOIDIsActive(wOID);
		validationUtilityService.validateWOIDForClosedDeferredAndRejectedStatus(wOID);
		validationUtilityService.validateIfWorkorderIsUnassigned(wOID);
		validationUtilityService.validateIntegerForNullOrZero(taskID, "task ID");
		validationUtilityService.validateIntegerForNullOrZero(bookingID, "bookingID");
		validationUtilityService.validateStringForBlank(signumID, "signumID");
		validateReasonAndStatusForBlank(reason, status);
		validationUtilityService.validateStringForBlank(stepid, "stepid");
		validationUtilityService.validateSignumForExistingEmployee(signumID);
		ServerBotModel serverBotModel = new ServerBotModel();
		serverBotModel.setTaskID(taskID);
		serverBotModel.setwOID(wOID);
		serverBotModel.setStepID(stepid);
		validationUtilityService.validateTaskIDForStartTask(serverBotModel, woDetails);
		validationUtilityService.validateWOIDIsActive(woDetails);
		validationUtilityService.validateIfWorkorderIsUnassigned(woDetails);
		validationUtilityService.validateIntegerForNullOrZero(flowChartDefID, "flowChartDefID");
		validationUtilityService.validateStringForBlank(refferer, "refferer");
		// validate for valid referrer
		validateReferer(refferer);
	}

	public String getExecutionTypeOnStepIdTaskIdFlowChartDefId(String stepid, int taskID, int flowChartDefID) {
		return workOrderPlanDao.getExecutionType(stepid, taskID, flowChartDefID);
	}

	private void validateReferer(String refferer) {
		if (StringUtils.equalsIgnoreCase(refferer, "ui")) {
			refferer = "WEB";
		}
		validationUtilityService.validateReferer(refferer);

	}

	public Map<String, Object> getWorkOrders(String signumID, String status, String startDate, String endDate) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(signumID) && StringUtils.isNotBlank(status)) {
			if(this.accessManagementDAO.isSignumExistsEmp(signumID)) {

				ResponseEntity<Response<List<WorkOrderProgressModel>>> response_workOrder=woExecutionService.getWorkOrders(signumID, status, startDate, endDate);			
				List<WorkOrderProgressModel> workOrder=response_workOrder.getBody().getResponseData();
				if (workOrder.isEmpty()) {
					result.put("msg", "Data Not Found For Selected Signum");
					result.put("data", workOrder);
				} else {
					result.put("data", workOrder);
				}
			}
			else {
				result.put("msg", "Invalid Signum,Please enter valid signum");
			}
		} else {
			result.put("msg", "Signum/Status can not be null/blank");
		}
		return result;
	}

	public Map<String, Object> getWOWorkFlow(int wOID, int proficiencyID, String signum) throws Exception {
		Map<String, Object> data=new HashMap<String, Object>();
		ResponseEntity<Response<Map<String, Object>>> response=woExecutionService.getWOWorkFlow(wOID, proficiencyID,signum);

		int statusCode=response.getStatusCodeValue();

		if(StringUtils.isNoneEmpty(response.getBody().getFormErrors().get(0))) {
			if(statusCode==200) {
				data.put(AppConstants.FAILED, response.getBody().getFormErrors().get(0));
			}else if(statusCode==500) {
				throw new Exception(response.getBody().getFormErrors().get(0));
			}
		}else {
			data=response.getBody().getResponseData();	
		}
		return data;
	}

	public List<LinkedHashMap<String, Object>> getInprogressTask(String signum) {
		List<LinkedHashMap<String, Object>> response=new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> msg=new LinkedHashMap<String, Object>();
		if(StringUtils.isNotBlank(signum)) {
			if(this.accessManagementDAO.isSignumExistsEmp(signum)) {
				response= this.workOrderPlanService.getInprogressTask(signum).getBody().getResponseData();
			}
			else {
				msg.put("msg", "Invalid Signum,Please enter valid Signum");
				response.add(msg);
			}
		}
		else {
			msg.put("msg", "Signum can not be null/blank");
			response.add(msg);
		}
		return response;
	}

	public Map<String, Object> checkParallelWorkOrderDetails(String signumID, String isApproved, String executionType,
			int woid, int taskid, int projectID, int versionNO, int subActivityFlowChartDefID, String stepID) throws Exception {

		Map<String, Object> result=new HashMap<String, Object>();

		ParallelWorkOrderDetailsModel model=new ParallelWorkOrderDetailsModel();
		model.setSignumID(signumID);
		model.setIsApproved(isApproved);
		model.setExecutionType(executionType);
		model.setWoid(woid);
		model.setTaskid(taskid);
		model.setProjectID(projectID);
		model.setVersionNO(versionNO);
		model.setSubActivityFlowChartDefID(subActivityFlowChartDefID);
		model.setStepID(stepID);

		ResponseEntity<Response<Map<String, Object>>> response=woExecutionService.checkParallelWorkOrderDetails(model);

		int statusCode=Integer.parseInt(response.getStatusCode().toString());
		if(StringUtils.isNoneEmpty(response.getBody().getFormErrors().get(0))) {
			if(statusCode==200) {
				result.put(AppConstants.MSG, response.getBody().getFormErrors().get(0));
				result.put(AppConstants.EXECUTE_FLAG, false);
			} else if (statusCode == 500) {
				throw new Exception(response.getBody().getFormErrors().get(0));
			}
		} else {
			result = response.getBody().getResponseData();
		}
		return result;
	}

	public Response<BookingDetailsModel> getBookingDetailsByBookingId(int bookingId) {
		Response<BookingDetailsModel> response=new Response<BookingDetailsModel>();
		BookingDetailsModel bookingData=woExecutionService.getBookingDetailsByBookingId(bookingId);
		if(bookingData!=null) {	
			response.setResponseData(bookingData);
		}
		else {
			response.addFormError("This booking id does not exists!");
		}
		return response;
	}

	public Response<Void> addStepDetailsForFlowChart(StepDetailsModel stepDetailsModel) {
		Response<Void> response = new Response<>();
		WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(stepDetailsModel.getWoId());
		try {
			validateAddStepDetails(stepDetailsModel,woDetails);
			ResponseEntity<Response<Void>> res=woExecutionService.addStepDetailsForFlowChart(stepDetailsModel);
			if(res.getStatusCodeValue()==500) {
				response.setFormErrors(res.getBody().getFormErrors());
				return response;
			}
			response.addFormMessage("Step added Successfully.");
		}
		catch(Exception e) {
			response.addFormError(e.getMessage());
		}
		return response;

	}

	private void validateAddStepDetails(StepDetailsModel stepDetailsModel, WorkOrderModel woDetails) throws Exception {
		validationUtilityService.validateIntegerForNullOrZero(stepDetailsModel.getWoId(), "wOID");
		validationUtilityService.validateWOIDIsActive(stepDetailsModel.getWoId());
		validationUtilityService.validateWOIDForClosedDeferredAndRejectedStatus(stepDetailsModel.getWoId());
		validationUtilityService.validateIfWorkorderIsUnassigned(stepDetailsModel.getWoId());
		validationUtilityService.validateStringForBlank(stepDetailsModel.getSignumId(), "signumID");
		validateWoAssignForSignum(stepDetailsModel.getSignumId(), woDetails.getSignumID(), stepDetailsModel.getWoId());
		validationUtilityService.validateStringForBlank(stepDetailsModel.getFlowChartStepId(), "flowChartStepId");
		validationUtilityService.validateIntegerForNullOrZero(stepDetailsModel.getFlowChartDefID(), "FlowChartDefID");
		validationUtilityService.validateStringForBlank(stepDetailsModel.getExecutionType(), "ExecutionType");
		validationUtilityService.validateSignumForExistingEmployee(stepDetailsModel.getSignumId());
		validateDefID(woDetails.getFlowchartdefid(), stepDetailsModel.getFlowChartDefID());
		// StepID and executionType validation left
		validationUtilityService.validateStepIdAndExecutionType(stepDetailsModel.getFlowChartStepId(),
				stepDetailsModel.getFlowChartDefID(), stepDetailsModel.getExecutionType());
	}

	private List<String> parseJsonToGetDecisionValue(int flowChartDefID,String flowChartStepId) throws Exception {    
		String jsonValue=woExecutionDAO.getJsonValue(flowChartDefID);
		JSONObject jObject = new JSONObject(jsonValue);
		JSONArray arr = jObject.getJSONArray("cells");
		List<String> decisionValueList = new ArrayList<>();

		for (int i = 0; i < arr.length(); i++) {
			if (arr.getJSONObject(i).has("labels")) {

				JSONArray array1= arr.getJSONObject(i).getJSONArray("labels");
				//String previousStepStepId=workOrderPlanService.getPreviousStepStepId(flowChartStepId,flowChartDefID);

				if(StringUtils.equalsIgnoreCase(arr.getJSONObject(i).getJSONObject("source").getString("id"), flowChartStepId)) {
					for(int j=0;j<array1.length();j++) {
						decisionValueList.add(array1.getJSONObject(j).getJSONObject("attrs").getJSONObject("text").getString("text"));
					}
				}

			}			

		}
		return decisionValueList;
	}
	private void validateWoAssignForSignum(String signumId, String signumID2, int workOrderId) {
		if (!StringUtils.equalsIgnoreCase(signumId, signumID2)) {
			throw new ApplicationException(500, "Work order Id " + workOrderId + " not assigned to " + signumId);
		}

	}

	private void validateDefID(int flowchartdefid, int flowChartDefID2) {
		if (flowchartdefid != flowChartDefID2) {
			throw new ApplicationException(500, "Invalid FlowCahrtDefID");
		}
	}

	public Response<ResponseEntity<String>> callErisiteFromSignalR(EventPublisherRequestModel eventPublisherModel) {
		Response<ResponseEntity<String>> finalResponse = new Response<ResponseEntity<String>>();
		ResponseEntity<String> response = null;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		if (CollectionUtils.isNotEmpty(eventPublisherModel.getHeaderModels())) {
			for (HeaderModel header : eventPublisherModel.getHeaderModels()) {
				headers.set(header.getHeaderKey(), header.getHeaderValue());
			}
		}
		System.out.println(eventPublisherModel.toString());
		String url = eventPublisherModel.getUrl();

		try {
			HttpEntity<String> request = new HttpEntity<>(eventPublisherModel.getPayLoad(), headers);
			response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
			System.out.println(response);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finalResponse.setResponseData(response);
		// finalResponse.addFormError(e.getMessage());
		return finalResponse;
	}

	public URI uploadFileInContainer(MultipartFile empFile, String blobName) {
		URI uri = null;
		CloudBlockBlob blob = null;

		try {
			String fileName=null;
			if(blobName.toLowerCase().contains("input")) {
				fileName="input.zip";
			}else if(blobName.toLowerCase().contains("output")) {
				fileName="output.zip";
			}else if(blobName.toLowerCase().contains("code")) {
				fileName="code.zip";
			}else if(blobName.toLowerCase().contains("logic")) {
				fileName="logic.zip";
			}else if(blobName.toLowerCase().contains("exe")) {
				fileName="exe.zip";
			}else {
				fileName=empFile.getOriginalFilename();
			}

			blob = testBlobContainer.getBlockBlobReference(blobName+fileName);
			blob.upload(empFile.getInputStream(), -1);

			uri = blob.getUri();

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}

		return uri;
	}

	public URI uploadFileInContainerFromByteArray(byte[] bytesEmpFile, String blobName, String jarFileName) {
		URI uri = null;
		CloudBlockBlob blob = null;
		try {
			blob = testBlobContainer.getBlockBlobReference(blobName+jarFileName);
			blob.uploadFromByteArray(bytesEmpFile, 0, bytesEmpFile.length);
			uri = blob.getUri();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return uri;
	}

	public Response<URI> DownloadFileFromContainer(String fileName, int rpaID) throws IOException {
		Response<URI> response = new Response<>();
		StringBuilder filePath = new StringBuilder();
		try 
		{
			if (StringUtils.contains(fileName, "SuperBOT-Macro") || StringUtils.contains(fileName, "SuperBOT-Python")) {
				filePath.append("superbots").append(AppConstants.FORWARD_SLASH).append(fileName);
			} 
			else if (StringUtils.contains(fileName, ".jar") || StringUtils.contains(fileName, ".exe") || StringUtils.contains(fileName, ".whl"))
			{
				filePath.append(AppConstants.BOT_CONF_FILES).append(AppConstants.FORWARD_SLASH).append(rpaID)
				.append(AppConstants.FORWARD_SLASH).append("bot/").append(fileName);
			}
			else if (StringUtils.contains(fileName, "input") || StringUtils.contains(fileName, "output")
					|| StringUtils.contains(fileName, "code") || StringUtils.contains(fileName, "logic")) {
				filePath.append(AppConstants.BOT_CONF_FILES).append(AppConstants.FORWARD_SLASH).append(rpaID)
				.append(AppConstants.FORWARD_SLASH)
				.append(fileName.split(AppConstants.BACKWARD_DOUBLE_SLASH_DOT)[0])
				.append(AppConstants.FORWARD_SLASH).append(fileName);

			} else {
				filePath.append(AppConstants.OUTPUT_FILES).append(AppConstants.FORWARD_SLASH).append(rpaID)
				.append(AppConstants.FORWARD_SLASH).append(fileName);
			}
			response.setResponseData(testBlobContainer.getBlockBlobReference(filePath.toString()).getUri());
			response.addFormMessage("file downloaded successfully");

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		} catch (Exception e) {
			response.addFormError("file download fail");
		}

		return response;
	}

	public byte[] downloadSuperBOT(String fileName) throws IOException, URISyntaxException, StorageException {
		URI uri = null;
		CloudBlockBlob blob = null;

		try {
			blob = testBlobContainer.getBlockBlobReference(AppConstants.SUPER_BOTS + "/" + fileName);
			uri = blob.getUri();

			String fileURL = uri.toString();
			byte[] buffer = HttpDownloadUtility.downloadFile(fileURL);
			System.out.println(buffer.length);
			return buffer;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public byte[] downloadBOTFromContainer(String fileName, int rpa) throws IOException, URISyntaxException, StorageException {
		URI uri = null;
		try {
			Response<URI> response = DownloadFileFromContainer(fileName, rpa);

			uri	=	response.getResponseData();

			//			blob = cloudBlobContainer.getBlockBlobReference(AppConstants.SUPER_BOTS+"/"+fileName);
			//			uri =blob.getUri();

			String fileURL =uri.toString();
			byte[] buffer =HttpDownloadUtility.downloadFile(fileURL);
			System.out.println(buffer.length);
			return buffer;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}


	public byte[] downloadCascadeBOTInput(String path) throws IOException, URISyntaxException, StorageException {
		URI uri = null;
		CloudBlockBlob blob = null;

		try {
			blob = testBlobContainer.getBlockBlobReference(AppConstants.OUTPUT_FILES +"/"+path);
			uri =blob.getUri();

			String fileURL =uri.toString();
			byte[] buffer =HttpDownloadUtility.downloadFile(fileURL);
			if(buffer==null) {
				System.out.println(buffer.length);
			}

			return buffer;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		}catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public byte[] downloadBOTInputFile(String path) throws IOException, URISyntaxException, StorageException {
		URI uri = null;
		CloudBlockBlob blob = null;

		try {
			blob = testBlobContainer.getBlockBlobReference(AppConstants.SERVER_BOT_INPUTS +"/"+path);
			LOG.info("\n Blob : {}" , blob.getName());
			uri =blob.getUri();

			String fileURL =uri.toString();
			LOG.info("\n fileURL : {}" , fileURL);
			byte[] buffer =HttpDownloadUtility.downloadFile(fileURL);
			if(buffer==null) {
			}

			return buffer;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		}catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Map<String, Object> testConfig() {
		Map<String,Object> newResponse=new HashMap<String,Object>();
		newResponse.put("environment-name", configurations.getStringProperty(ConfigurationFilesConstants.NAME));
		newResponse.put("dbproperty-jdbc.dataSourceClassName",
				configurations.getStringArrayProperty(ConfigurationFilesConstants.SAPBO_AMERICAS5));
		newResponse.put("hibernateproperty-hibernate.dialect",
				configurations.getStringProperty(ConfigurationFilesConstants.HIBERNATE_DIALECT));
		newResponse.put("appproperty-accessprofile.accessdays",
				configurations.getStringProperty(ConfigurationFilesConstants.ACCESSPROFILE_ACCESSDAYS));
		newResponse.put("CONFIG_BASE_URL",
				System.getenv("CONFIG_BASE_URL"));
		return newResponse;
	}

	public Response<Boolean> revokeApiManagerExpiredUsers() {
		return accessManagementService.revokeApiManagerExpiredUsers();
	}

	public void scheduleSapBoJobWithRefreshData() {
		if (configurations.getBooleanProperty(ConfigurationFilesConstants.SAPBO_SCHEDULER_ENABLED, false)) {
			LOG.info("Monitor Sap BO Report fetching started");
			downloadSapBOWithRefreshData(
					configurations.getStringArrayProperty(ConfigurationFilesConstants.SAPBO_AMERICAS5),
					AppConstants.AMERICAS, AppConstants.NUMBER_5);
			downloadSapBOWithRefreshData(
					configurations.getStringArrayProperty(ConfigurationFilesConstants.SAPBO_AMERICAS11),
					AppConstants.AMERICAS, AppConstants.NUMBER_11);
			downloadSapBOWithRefreshData(configurations.getStringArrayProperty(ConfigurationFilesConstants.SAPBO_APAC),
					AppConstants.APAC, AppConstants.NUMBER_5);
			downloadSapBOWithRefreshData(configurations.getStringArrayProperty(ConfigurationFilesConstants.SAPBO_EMEA),
					AppConstants.EMEA, AppConstants.NUMBER_5);
		} else {
			LOG.info("Schedular is disabled");
		}
	}

	private boolean downloadSapBOWithRefreshData(String[] documentID, String instance, int columnCount) {
		ResponseEntity<String> response = null;
		try {
			// generating new LogOn Token
			response = getLogOnToken(instance);
			if (response.getStatusCodeValue() == AppConstants.SAP_BO_RESPONSE_CODE) {
				if (StringUtils.isBlank(response.getBody())) {
					String status = (new StringBuilder()).append("Token Not Generated for :")
							.append(documentID.toString()).toString();
					sendMailForSAPBOFailure(instance, documentID.toString(), status);
					LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + "-" + status);
				}
			} else {
				LOG.info(AppConstants.SAP_BO_FAILURE_RESPONSE + response.getStatusCodeValue());
				sendMailForSAPBOFailure(instance, documentID.toString(), AppConstants.SAP_BO_FAILURE_RESPONSE);
			}
		} catch (RestClientException e) {
			sendMailForSAPBOFailure(instance, documentID.toString(), AppConstants.SAP_BO_FAILURE_RESPONSE);
		}

		if (response != null) {

			JSONObject jsonObj = new JSONObject(response.getBody());

			LOG.info("Logon success : Token generated is: " + jsonObj.get(AppConstants.LOGON_TOKEN));

			if (null != jsonObj.get(AppConstants.LOGON_TOKEN)
					&& jsonObj.get(AppConstants.LOGON_TOKEN).toString().trim().length() > 0) {
				for (int i = 0; i < documentID.length; i++) {
					if (!StringUtils.equalsIgnoreCase(instance, AppConstants.APAC)) {
						// To Get Latest Scheduled ID
						int latestScheduleID = getLatestScheduledID(documentID[i].trim(),
								jsonObj.get(AppConstants.LOGON_TOKEN).toString().trim(), instance);
						// To Get Latest Scheduled Parameters
						ScheduleParameterModel scheduledParams = getLatestScheduledParameters(documentID[i].trim(),
								jsonObj.get(AppConstants.LOGON_TOKEN).toString().trim(), instance, latestScheduleID);
						// To Refresh Documents with latest parameters
						refreshDocuments(scheduledParams, documentID[i].trim(),
								jsonObj.get(AppConstants.LOGON_TOKEN).toString().trim(), instance);
					}
					// To Get Report ID
					int reportId = getReportIDWithRefreshData(documentID[i].trim(),
							jsonObj.get(AppConstants.LOGON_TOKEN).toString().trim(), instance);
					// To Get refreshedScheduledID
					int refreshedScheduledID = getRefreshedScheduledID(documentID[i].trim(),
							jsonObj.get(AppConstants.LOGON_TOKEN).toString().trim(), instance);
					if (refreshedScheduledID == 0)
						refreshedScheduledID = Integer.parseInt(documentID[i].trim());
					// downloading SAP BO File
					downloadSapBOFileWithRefreshData(jsonObj.get(AppConstants.LOGON_TOKEN).toString().trim(),
							String.valueOf(refreshedScheduledID), reportId, instance, documentID[i].trim(),
							columnCount);

				}

			}
			// LogOff Token
			logoffToken(jsonObj.get(AppConstants.LOGON_TOKEN).toString().trim(), instance);
		}
		return true;
	}

	private boolean logoffToken(String logonToken, String instance) {
		final String uri = (new StringBuilder()).append("https://").append(instance)
				.append("-int.erisite.ericsson.net/biprws/logoff").toString();
		boolean logOff = false;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = setHeaders(AppConstants.APPLICATION_JSON, AppConstants.APPLICATION_JSON, logonToken);

		HttpEntity<String> request = new HttpEntity<>(headers);

		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
			if (response.getStatusCodeValue() == AppConstants.SAP_BO_RESPONSE_CODE) {
				LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + response.getStatusCodeValue());
			} else {
				LOG.info(AppConstants.SAP_BO_FAILURE_RESPONSE + response.getStatusCodeValue());
				sendMailForSAPBOFailure(instance, StringUtils.EMPTY, AppConstants.SAP_BO_FAILURE_RESPONSE);

			}
			logOff = true;
		} catch (RestClientException e) {
			sendMailForSAPBOFailure(instance, StringUtils.EMPTY, AppConstants.SAP_BO_FAILURE_RESPONSE);
			LOG.info(e.getMessage());
		}

		LOG.info("Logoff success :");

		return logOff;

	}

	private boolean refreshDocuments(ScheduleParameterModel scheduledParams, String docID, String logonToken,
			String instance) {
		final String uri = (new StringBuilder()).append("https://").append(instance)
				.append("-int.erisite.ericsson.net/biprws/raylight/v1/documents/").append(docID.trim())
				.append("/parameters").toString();
		String xmlString = generateXMLForRefreshDocument(scheduledParams);
		LOG.info("uri :" + uri);
		LOG.info("xml :" + xmlString);
		boolean isRefreshed = false;
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = setHeaders(AppConstants.APPLICATION_XML, AppConstants.APPLICATION_JSON, logonToken);

		HttpEntity<String> request = new HttpEntity<>(xmlString, headers);

		ResponseEntity<String> response = null;
		try {
			LOG.info("Document Refreshing .....Please wait....!");
			response = restTemplate.exchange(uri, HttpMethod.PUT, request, String.class);
			if (response.getStatusCodeValue() == AppConstants.SAP_BO_RESPONSE_CODE) {
				LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + response.getStatusCodeValue());
				if (StringUtils.isBlank(response.getBody())) {
					String status = (new StringBuilder()).append("Document not refreshed for DocumentID:").append(docID)
							.toString();
					sendMailForSAPBOFailure(instance, docID, status);
					LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + "-" + status);
				}
			} else {
				LOG.info(AppConstants.SAP_BO_FAILURE_RESPONSE + response.getStatusCodeValue());
				sendMailForSAPBOFailure(instance, docID, AppConstants.SAP_BO_FAILURE_RESPONSE);
			}
		} catch (RestClientException e) {
			sendMailForSAPBOFailure(instance, docID, AppConstants.SAP_BO_FAILURE_RESPONSE);
			LOG.info(e.getMessage());
		}

		if (response != null) {
			JSONObject jsonObj = new JSONObject(response.getBody());
			if (null != jsonObj.get("success") && jsonObj.get("success").toString().trim().length() > 0) {
				isRefreshed = true;
				LOG.info("Document Refreshed .......! " + jsonObj.get("success").toString());
			}
		}
		return isRefreshed;
	}

	private String generateXMLForRefreshDocument(ScheduleParameterModel scheduledParams) {
		String xmlString = StringUtils.EMPTY;
		try {
			Value value = new Value(scheduledParams.getParameterIDValue(), scheduledParams.getParameterValue());
			Values values = new Values(value);
			Answer answer = new Answer("true", "Text", values);
			Parameter param = new Parameter("false", "context", scheduledParams.getParameterID(),
					scheduledParams.getParameterName(), answer);
			Parameters params = new Parameters(param);
			JAXBContext contextObj = JAXBContext.newInstance(Parameters.class);
			Marshaller marshallerObj = contextObj.createMarshaller();
			marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter sw = new StringWriter();
			marshallerObj.marshal(params, sw);
			xmlString = sw.toString();
		} catch (Exception ex) {
			LOG.info("Unable to generate XML to refresh Documents : " + ex.getMessage());
		}
		return xmlString;
	}

	private ScheduleParameterModel getLatestScheduledParameters(String docID, String logonToken, String instance,
			int latestScheduleID) {
		ScheduleParameterModel responseData = new ScheduleParameterModel();
		final String uri = (new StringBuilder()).append("https://").append(instance)
				.append("-int.erisite.ericsson.net/biprws/raylight/v1/documents/").append(docID.trim())
				.append("/schedules/").append(latestScheduleID).toString();
		LOG.info("uri :" + uri);
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = setHeaders(AppConstants.APPLICATION_JSON, AppConstants.APPLICATION_JSON, logonToken);

		HttpEntity<String> request = new HttpEntity<>(headers);

		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
			if (response.getStatusCodeValue() == AppConstants.SAP_BO_RESPONSE_CODE) {
				LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + response.getStatusCodeValue());
				if (StringUtils.isBlank(response.getBody())) {
					String status = (new StringBuilder())
							.append("Latest Scheduled Parameters not found for DocumentID:").append(docID).toString();
					sendMailForSAPBOFailure(instance, docID, status);
					LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + "-" + status);
				}
			} else {
				LOG.info(AppConstants.SAP_BO_FAILURE_RESPONSE + response.getStatusCodeValue());
				sendMailForSAPBOFailure(instance, docID, AppConstants.SAP_BO_FAILURE_RESPONSE);
			}
		} catch (RestClientException e) {
			sendMailForSAPBOFailure(instance, docID, AppConstants.SAP_BO_FAILURE_RESPONSE);
			LOG.info(e.getMessage());
		}

		if (response != null) {
			JSONObject jsonObj = new JSONObject(response.getBody());
			if (null != jsonObj.get(AppConstants.SCHEDULE)
					&& jsonObj.get(AppConstants.SCHEDULE).toString().trim().length() > 0) {
				JSONObject schedule = (JSONObject) jsonObj.get(AppConstants.SCHEDULE);
				if (schedule.has(AppConstants.PARAMETERS) && null != schedule.get(AppConstants.PARAMETERS)
						&& schedule.get(AppConstants.PARAMETERS).toString().trim().length() > 0) {
					JSONObject parameters = (JSONObject) schedule.get(AppConstants.PARAMETERS);
					if (null != parameters.get(AppConstants.PARAMETER)
							&& parameters.get(AppConstants.PARAMETER).toString().trim().length() > 0) {
						JSONArray jsonArray = parameters.getJSONArray(AppConstants.PARAMETER);
						if (jsonArray.length() > 0) {
							JSONObject parameter = jsonArray.getJSONObject(jsonArray.length() - 1);
							if (null != parameter.get(AppConstants.ID)
									&& parameter.get(AppConstants.ID).toString().trim().length() > 0) {
								responseData
								.setParameterID(Integer.parseInt(parameter.get(AppConstants.ID).toString()));
								responseData.setParameterName(parameter.get(AppConstants.NAME).toString());
								if (null != parameter.get(AppConstants.ANSWER)
										&& parameter.get(AppConstants.ANSWER).toString().trim().length() > 0) {
									JSONObject answer = (JSONObject) parameter.get(AppConstants.ANSWER);
									if (null != answer.get(AppConstants.VALUES)
											&& answer.get(AppConstants.VALUES).toString().trim().length() > 0) {
										JSONObject values = (JSONObject) answer.get(AppConstants.VALUES);
										if (null != values.get(AppConstants.VALUE)
												&& values.get(AppConstants.VALUE).toString().trim().length() > 0) {

											JSONArray valueJsonArray = values.getJSONArray(AppConstants.VALUE);
											if (valueJsonArray.length() > 0) {
												JSONObject value = valueJsonArray
														.getJSONObject(valueJsonArray.length() - 1);
												responseData.setParameterIDValue(
														value.get(AppConstants.AT_RATE_ID).toString());
												responseData
												.setParameterValue(value.get(AppConstants.DOLLAR).toString());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		LOG.info("LatestScheduledParameters ParameterID : " + responseData.getParameterID() + " ParameterName : "
				+ responseData.getParameterName() + " Parameter@ID : " + responseData.getParameterIDValue()
				+ " Value@$ : " + responseData.getParameterValue());
		return responseData;

	}

	private int getLatestScheduledID(String docID, String logonToken, String instance) {
		final String uri = (new StringBuilder()).append("https://").append(instance)
				.append("-int.erisite.ericsson.net/biprws/raylight/v1/documents/").append(docID.trim())
				.append("/schedules").toString();
		LOG.info("uri :" + uri);
		int latestScheduledID = 0;
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = setHeaders(AppConstants.APPLICATION_JSON, AppConstants.APPLICATION_JSON, logonToken);

		HttpEntity<String> request = new HttpEntity<>(headers);

		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
			if (response.getStatusCodeValue() == AppConstants.SAP_BO_RESPONSE_CODE) {
				LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + response.getStatusCodeValue());
				if (StringUtils.isBlank(response.getBody())) {
					String status = (new StringBuilder()).append("Latest Scheduled ID not found for Document ID : ")
							.append(docID).toString();
					sendMailForSAPBOFailure(instance, docID, status);
					LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + "-" + status);
				}
			} else {
				LOG.info(AppConstants.SAP_BO_FAILURE_RESPONSE + response.getStatusCodeValue());
				sendMailForSAPBOFailure(instance, docID, AppConstants.SAP_BO_FAILURE_RESPONSE);
			}
		} catch (RestClientException e) {
			sendMailForSAPBOFailure(instance, docID, AppConstants.SAP_BO_FAILURE_RESPONSE);
			LOG.info(e.getMessage());
		}

		if (response != null) {
			JSONObject jsonObj = new JSONObject(response.getBody());
			if (null != jsonObj.get(AppConstants.SCHEDULES)
					&& jsonObj.get(AppConstants.SCHEDULES).toString().trim().length() > 0) {
				JSONObject schedules = (JSONObject) jsonObj.get(AppConstants.SCHEDULES);
				if (null != schedules.get(AppConstants.SCHEDULE)
						&& schedules.get(AppConstants.SCHEDULE).toString().trim().length() > 0) {
					JSONArray jsonArray = schedules.getJSONArray(AppConstants.SCHEDULE);
					if (jsonArray.length() > 0) {
						JSONObject schedule = jsonArray.getJSONObject(jsonArray.length() - 1);
						if (null != schedule.get(AppConstants.ID)
								&& schedule.get(AppConstants.ID).toString().trim().length() > 0) {
							latestScheduledID = Integer.parseInt(schedule.get(AppConstants.ID).toString());
							LOG.info("latestScheduledID :" + latestScheduledID);
							LOG.info("ReportName :" + schedule.get("name").toString());// need to delete
						}
					}

				}
			}
		}
		return latestScheduledID;
	}

	private boolean downloadSapBOFileWithRefreshData(String logonToken, String refreshedScheduledID, int reportId,
			String instance, String docID, int columnCount) {
		final String uri = (new StringBuilder()).append("https://").append(instance)
				.append("-int.erisite.ericsson.net/biprws/raylight/v1/documents/").append(refreshedScheduledID)
				.append("/reports/").append(reportId).toString();
		LOG.info("uri :" + uri);
		boolean fileDownload = false;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = setHeaders(AppConstants.APPLICATION_JSON, AppConstants.TEXT_CSV, logonToken);

		HttpEntity<String> request = new HttpEntity<>(headers);

		ResponseEntity<byte[]> response = null;
		try {
			response = restTemplate.exchange(uri, HttpMethod.GET, request, byte[].class, "File5.iso");
			if (response.getStatusCodeValue() == AppConstants.SAP_BO_RESPONSE_CODE) {
				LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + response.getStatusCodeValue());
				if (response.getBody() == null) {
					String status = (new StringBuilder()).append("No Data Found For ReportId:")
							.append(Integer.toString(reportId)).append(" and RefreshedScheduledID : ")
							.append(refreshedScheduledID).toString();
					sendMailForSAPBOFailure(instance, Integer.toString(reportId), status);
					LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + "-" + status);
				}

			} else {
				LOG.info(AppConstants.SAP_BO_FAILURE_RESPONSE + response.getStatusCodeValue());
				sendMailForSAPBOFailure(instance, Integer.toString(reportId), AppConstants.SAP_BO_FAILURE_RESPONSE);
			}
		} catch (RestClientException e) {
			sendMailForSAPBOFailure(instance, Integer.toString(reportId), AppConstants.SAP_BO_FAILURE_RESPONSE);
			LOG.info(e.getMessage());
		}
		if (response != null) {
			fileDownload = true;
			try {
				if (dbFileUploadForSAPBO(// environment.getRequiredProperty("erisite.sapbo.basefolder"),
						response.getBody(), docID + "_" + reportId, instance, columnCount)) {

					LOG.info("File Upload: Success");
				} else {
					LOG.info("File Upload: Failed");
				}
			} catch (Exception e) {
				LOG.info("File Upload: Failed");
			}
		}
		return fileDownload;
	}

	private int getRefreshedScheduledID(String docID, String logonToken, String instance) {
		final String uri = (new StringBuilder()).append("https://").append(instance)
				.append("-int.erisite.ericsson.net/biprws/infostore/").append(docID.trim()).append("/children")
				.toString();
		LOG.info("uri :" + uri);
		int refreshedScheduledID = 0;
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = setHeaders(AppConstants.APPLICATION_JSON, AppConstants.APPLICATION_JSON, logonToken);

		HttpEntity<String> request = new HttpEntity<>(headers);

		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
			if (response.getStatusCodeValue() == AppConstants.SAP_BO_RESPONSE_CODE) {
				LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + response.getStatusCodeValue());
				if (StringUtils.isBlank(response.getBody())) {
					String status = (new StringBuilder()).append("Refreshed Scheduled ID not found for Document ID :")
							.append(docID).toString();
					sendMailForSAPBOFailure(instance, Integer.toString(refreshedScheduledID), status);
					LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + "-" + status);
				}
			} else {
				LOG.info(AppConstants.SAP_BO_FAILURE_RESPONSE + response.getStatusCodeValue());
				sendMailForSAPBOFailure(instance, Integer.toString(refreshedScheduledID),
						AppConstants.SAP_BO_FAILURE_RESPONSE);
			}
		} catch (RestClientException e) {
			sendMailForSAPBOFailure(instance, Integer.toString(refreshedScheduledID),
					AppConstants.SAP_BO_FAILURE_RESPONSE);
			LOG.info(e.getMessage());
		}

		if (response != null) {
			JSONObject jsonObj = new JSONObject(response.getBody());
			if (null != jsonObj.get(AppConstants.ENTRIES)
					&& jsonObj.get(AppConstants.ENTRIES).toString().trim().length() > 0) {

				JSONArray jsonArray = jsonObj.getJSONArray(AppConstants.ENTRIES);

				if (jsonArray.length() > 0) {

					JSONObject jsonObj1 = jsonArray.getJSONObject(jsonArray.length() - 1);

					if (null != jsonObj1.get(AppConstants.ID)
							&& jsonObj1.get(AppConstants.ID).toString().trim().length() > 0) {
						refreshedScheduledID = Integer.parseInt(jsonObj1.get(AppConstants.ID).toString());
					}
				}
			}
		}
		return refreshedScheduledID;
	}

	private int getReportIDWithRefreshData(String docID, String logonToken, String instance) {
		final String uri = (new StringBuilder()).append("https://").append(instance)
				.append("-int.erisite.ericsson.net/biprws/raylight/v1/documents/").append(docID.trim())
				.append("/reports").toString();
		LOG.info("uri :" + uri);
		int reportId = 0;
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = setHeaders(AppConstants.APPLICATION_JSON, AppConstants.APPLICATION_JSON, logonToken);

		HttpEntity<String> request = new HttpEntity<>(headers);

		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
			if (response.getStatusCodeValue() == AppConstants.SAP_BO_RESPONSE_CODE) {
				LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + response.getStatusCodeValue());
				if (StringUtils.isBlank(response.getBody())) {
					String status = (new StringBuilder()).append("Report ID not found for Document ID : ").append(docID)
							.toString();
					sendMailForSAPBOFailure(instance, docID, status);
					LOG.info(AppConstants.SAP_BO_SUCCESS_RESPONSE + "-" + status);
				}
			} else {
				LOG.info(AppConstants.SAP_BO_FAILURE_RESPONSE + response.getStatusCodeValue());
				sendMailForSAPBOFailure(instance, docID, AppConstants.SAP_BO_FAILURE_RESPONSE);
			}
		} catch (RestClientException e) {
			sendMailForSAPBOFailure(instance, docID, AppConstants.SAP_BO_FAILURE_RESPONSE);
			LOG.info(e.getMessage());
		}

		if (response != null) {
			JSONObject jsonObj = new JSONObject(response.getBody());
			JSONObject report = null;
			JSONObject reports = null;
			if (null != jsonObj.get(AppConstants.REPORTS)
					&& jsonObj.get(AppConstants.REPORTS).toString().trim().length() > 0) {
				reports = (JSONObject) jsonObj.get(AppConstants.REPORTS);
				if (null != reports.get(AppConstants.REPORT)
						&& reports.get(AppConstants.REPORT).toString().trim().length() > 0) {
					try {
						report = (JSONObject) reports.get(AppConstants.REPORT);
						if (null != report.get(AppConstants.ID)
								&& report.get(AppConstants.ID).toString().trim().length() > 0) {
							reportId = Integer.parseInt(report.get(AppConstants.ID).toString());
						}
					} catch (Exception e) {
						// LOG.info("Json Structure error" + e.getMessage());
					} finally {
						JSONArray jsonArray = reports.getJSONArray(AppConstants.REPORT);
						if (jsonArray.length() > 0) {
							report = jsonArray.getJSONObject(jsonArray.length() - 1);

							if (null != report.get(AppConstants.ID)
									&& report.get(AppConstants.ID).toString().trim().length() > 0) {
								reportId = Integer.parseInt(report.get(AppConstants.ID).toString());
							}
						}
					}
				}
			}
		}
		return reportId;
	}

	private ResponseEntity<String> getLogOnToken(String instance) {
		final String uri = (new StringBuilder()).append("https://").append(instance)
				.append("-int.erisite.ericsson.net/biprws/logon/long").toString();
		String jsonStrng = "{\"userName\": \"svcessc2g1\",\"password\": \"O!CMN7vvGBVW6Fj5\",\"auth\": \"secEnterprise\"}";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = setHeaders(AppConstants.APPLICATION_JSON, AppConstants.APPLICATION_JSON,
				StringUtils.EMPTY);
		HttpEntity<String> request = new HttpEntity<>(jsonStrng, headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		return response;
	}

	private HttpHeaders setHeaders(String contentType, String accept, String logonToken) {
		HttpHeaders headers = new HttpHeaders();
		if (StringUtils.isNotBlank(contentType)) {
			headers.set(AppConstants.CONTENT_TYPE, contentType);
		}
		if (StringUtils.isNotBlank(accept)) {
			headers.set(AppConstants.ACCEPT, accept);
		}
		if (StringUtils.isNotBlank(logonToken)) {
			headers.set(AppConstants.X_SAP_LOGON_TOKEN, "\"" + logonToken + "\"");
		}
		return headers;
	}

	public ResponseEntity<Response<WorkOrderCurrentStepDetailResponseModel>> getWOCurrentStepDetails(
			WorkOrderCurrentStepDetailsModel workOrderCurrentStepDetailsModel, String apiName) {
		Response<WorkOrderCurrentStepDetailResponseModel> responseData = new Response<WorkOrderCurrentStepDetailResponseModel>();
		WorkOrderCurrentStepDetailResponseModel response = new WorkOrderCurrentStepDetailResponseModel();
		try {
			WorkOrderModel woDetails = workOrderPlanDao
					.getWorkOrderDetailsById(workOrderCurrentStepDetailsModel.getWoid());
			validateGetWOCurrentStepDetails(workOrderCurrentStepDetailsModel, woDetails, apiName);
			Integer taskID = this.rpaDAO.getTaskID(woDetails.getSubActivityID(),
					workOrderCurrentStepDetailsModel.getTaskName().trim());
			List<WorkOrderCurrentStepDetailsModel> stepsList = this.rpaDAO.getFlowChartStepDetails(
					woDetails.getFlowchartdefid(), taskID, workOrderCurrentStepDetailsModel.getWoid());
			List<WorkOrderCurrentStepDetailsModel> executedSteps = new ArrayList<>();
			List<WorkOrderCurrentStepDetailsModel> currentSteps = new ArrayList<>();
			for (WorkOrderCurrentStepDetailsModel step : stepsList) {
				if (StringUtils.equalsIgnoreCase(step.getStepStatus(), EXECUTED_STEP)) {
					WorkOrderCurrentStepDetailsModel stepModel = new WorkOrderCurrentStepDetailsModel();
					stepModel.setStepId(step.getStepId());
					stepModel.setStepName(step.getStepName());
					stepModel.setTaskId(taskID);
					stepModel.setWoid(workOrderCurrentStepDetailsModel.getWoid());
					stepModel.setStepType(step.getStepType());
					stepModel.setTaskName(workOrderCurrentStepDetailsModel.getTaskName());
					stepModel.setSource(workOrderCurrentStepDetailsModel.getSource());
					stepModel.setStepStatus(step.getStepStatus());
					executedSteps.add(stepModel);
					currentSteps.add(stepModel);
				} else {
					WorkOrderCurrentStepDetailsModel stepModel = new WorkOrderCurrentStepDetailsModel();
					stepModel.setStepId(step.getStepId());
					stepModel.setStepName(step.getStepName());
					stepModel.setTaskId(taskID);
					stepModel.setWoid(workOrderCurrentStepDetailsModel.getWoid());
					stepModel.setStepType(step.getStepType());
					stepModel.setTaskName(workOrderCurrentStepDetailsModel.getTaskName());
					stepModel.setSource(workOrderCurrentStepDetailsModel.getSource());
					stepModel.setStepStatus(step.getStepStatus());
					currentSteps.add(stepModel);
				}

			}
			response.setExecutedSteps(executedSteps);
			response.setCurrentSteps(currentSteps);
			if (CollectionUtils.isEmpty(executedSteps) && CollectionUtils.isEmpty(currentSteps)) {
				responseData.addFormMessage(THIS_WO_DOES_NOT_HAVE_ANY_AUTOMATIC_STEP);
			}

		} catch (ApplicationException ex) {
			responseData.addFormError(ex.getMessage());
			LOG.info(ex.getMessage());
			return new ResponseEntity<Response<WorkOrderCurrentStepDetailResponseModel>>(responseData, HttpStatus.OK);
		} catch (Exception ex) {
			responseData.addFormError(ex.getMessage());
			LOG.info(ex.getMessage());
			return new ResponseEntity<Response<WorkOrderCurrentStepDetailResponseModel>>(responseData,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		responseData.setResponseData(response);
		return new ResponseEntity<Response<WorkOrderCurrentStepDetailResponseModel>>(responseData, HttpStatus.OK);
	}

	private void validateGetWOCurrentStepDetails(WorkOrderCurrentStepDetailsModel workOrderCurrentStepDetailsModel,
			WorkOrderModel woDetails, String apiName) {

		apiName = (apiName != null) ? apiName : "/externalInterface/getWOCurrentStepDetails";

		List<String> json = workOrderPlanDao.getValidateJsonForApi(apiName,
				workOrderCurrentStepDetailsModel.getSource());
		if (CollectionUtils.isEmpty(json)) {

			throw new ApplicationException(500, "Invalid source Name or validateJson found!");
		}

		JSONObject obj = new JSONObject(json.get(0));
		JSONArray array = obj.getJSONArray(VALIDATIONS);

		if (array == null || array.length() == 0) {
			throw new ApplicationException(500, "Given Source name has invalid validateJson!");
		}

		for (int i = 0; i < array.length(); i++) {

			switch (String.valueOf(array.get(i))) {

			case INTEGER_WOID_NULL_OR_ZERO:
				validationUtilityService.validateIntegerForNullOrZero(workOrderCurrentStepDetailsModel.getWoid(),
						"WOID");
				break;

			case WOID_ACTIVE:
				validationUtilityService.validateWOIDIsActive(workOrderCurrentStepDetailsModel.getWoid());
				break;

			case WO_UNASSIGNED:
				validationUtilityService.validateIfWorkorderIsUnassigned(workOrderCurrentStepDetailsModel.getWoid());
				break;

			case WOID_CLOSED_DEFERRED_REJECTED:
				validationUtilityService
				.validateWOIDForClosedDeferredAndRejectedStatus(workOrderCurrentStepDetailsModel.getWoid());
				break;

			case EXTERNAL_SOURCE_NAME_WITH_WOID:
				validationUtilityService
						.validateExternalSourceNameWithWoID(workOrderCurrentStepDetailsModel.getSource(), woDetails);
				break;

			case TASK_NAME_NULL:
				validationUtilityService.validateStringForBlank(workOrderCurrentStepDetailsModel.getTaskName(),
						"TaskName");
				break;
			case TASK_DETAILS:
				Integer taskID = this.rpaDAO.getTaskID(woDetails.getSubActivityID(),
						workOrderCurrentStepDetailsModel.getTaskName().trim());
				if (taskID == null || taskID == 0) {
					throw new ApplicationException(500, "Given TaskName is not valid!");
				}
				break;

			}
		}

	}

	public ResponseEntity<Response<Void>> executeDecisionStep(StepDetailsModel stepDetailsModel, String apiName) {
		Response<Void> responseData = new Response<>();

		try {
			WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(stepDetailsModel.getWoId());
			if (woDetails == null) {
				throw new ApplicationException(500, "No work order details found for this work order id");
			}

			if(stepDetailsModel.getTaskID() == null) {
				stepDetailsModel.setTaskID(0);
			}

			validateExecuteDecisionStepFromJSON(stepDetailsModel,woDetails,apiName);

			ResponseEntity<Response<Void>> res=woExecutionService.executeDecisionStep(stepDetailsModel);
			responseData.addFormMessage("Step added Successfully.");
		}
		catch (ApplicationException ex) {
			responseData.addFormError(ex.getMessage());
			LOG.info(ex.getMessage());
			return new ResponseEntity<Response<Void>>(responseData, HttpStatus.OK);
		} catch (Exception ex) {
			responseData.addFormError(ex.getMessage());
			LOG.info(ex.getMessage());
			return new ResponseEntity<Response<Void>>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response<Void>>(responseData, HttpStatus.OK);
	}


	public void validateIFPreviousStepCompleted(StepDetailsModel stepDetailsModel, WorkOrderModel woDetails) {

		ResponseEntity<Response<PreviousStepDetailsModel>> stepModel = woExecutionService.checkIFPreviousStepCompleted(stepDetailsModel.getWoId(),
				stepDetailsModel.getTaskID(), woDetails.getFlowchartdefid(), stepDetailsModel.getFlowChartStepId());

		if (!stepModel.getBody().getResponseData().isAllowed()) {

			throw new ApplicationException(500, "Please complete previous task/step or mark decision yes/no.");
		}

	}


	private void validateExecuteDecisionStepFromJSON(StepDetailsModel stepDetailsModel, WorkOrderModel woDetails, String apiName) throws Exception{

		apiName = (apiName != null) ? apiName : "/externalInterface/executeDecisionStep";

		List<String> json = workOrderPlanDao.getValidateJsonForApi(apiName, stepDetailsModel.getExternalSourceName());
		if (CollectionUtils.isEmpty(json)) {

			throw new ApplicationException(500, "Invalid source Name or validateJson found!");
		}

		JSONObject obj = new JSONObject(json.get(0));
		JSONArray array = obj.getJSONArray(VALIDATIONS);

		if (array == null || array.length() == 0) {
			throw new ApplicationException(500, "Given Source name has invalid validateJson!");
		}

		for(int i = 0; i < array.length(); i++) {

			switch (String.valueOf(array.get(i))) {

			case INTEGER_WOID_NULL_OR_ZERO:
				validationUtilityService.validateIntegerForNullOrZero(stepDetailsModel.getWoId(), "wOID");
				break;

			case WOID_ACTIVE:
				validationUtilityService.validateWOIDIsActive(stepDetailsModel.getWoId());
				break;

			case WO_UNASSIGNED:
				validationUtilityService.validateIfWorkorderIsUnassigned(stepDetailsModel.getWoId());
				break;

			case WOID_CLOSED_DEFERRED_REJECTED:
				validationUtilityService.validateWOIDForClosedDeferredAndRejectedStatus(stepDetailsModel.getWoId());
				break;

			case EXTERNAL_SOURCE_NAME_WITH_WOID:
				validationUtilityService.validateExternalSourceNameWithWoID(stepDetailsModel.getExternalSourceName(), woDetails);
				break;	

			case STRING_BLANK_SIGNUMID:
				validationUtilityService.validateStringForBlank(stepDetailsModel.getSignumId(), "signumID");
				break;	

			case WO_ASSIGN_FOR_SIGNUM:
				validationUtilityService.validateWoAssignForSignum(stepDetailsModel.getSignumId(),woDetails.getSignumID(),stepDetailsModel.getWoId());
				break;	

			case STRING_BLANK_FLOWCHARTSTEPID:
				validationUtilityService.validateStringForBlank(stepDetailsModel.getFlowChartStepId(),
						"flowChartStepId");
				break;

			case INTEGER_FLOWCHARTDEFID_NULL_OR_ZERO:
				validationUtilityService.validateIntegerForNullOrZero(stepDetailsModel.getFlowChartDefID(), "FlowChartDefID");
				break;	

			case STRING_BLANK_EXECUTIONTYPE:
				validationUtilityService.validateStringForBlank(stepDetailsModel.getExecutionType(), "ExecutionType");
				break;

			case SIGNUM_EXISTING_EMPLOYEE:
				validationUtilityService.validateSignumForExistingEmployee(stepDetailsModel.getSignumId());
				break;

			case STRING_BLANK_DECISIONVALUE:
				validationUtilityService.validateStringForBlank(stepDetailsModel.getDecisionValue(), "DecisionValue");
				break;

			case DECISIONVALUE_LENGTH:
				validationUtilityService.validateLength(stepDetailsModel.getDecisionValue(), 50, "DecisionValue");
				break;

			case DEFID_VALIDATE:
				validationUtilityService.validateDefID(woDetails.getFlowchartdefid(),
						stepDetailsModel.getFlowChartDefID());
				break;

			case STEPID_AND_EXECUTIONTYPE:
				validationUtilityService.validateStepIdAndExecutionType(stepDetailsModel.getFlowChartStepId(),
						stepDetailsModel.getFlowChartDefID(), stepDetailsModel.getExecutionType());
				break;

			case CHECK_STEPID_FOR_DECISION:
				validateStepIDForDecision(stepDetailsModel);
				break;	

			case CHECK_DECISION_VALUE:
				validateDecisionValue(stepDetailsModel, woDetails);
				break;

			case CHECK_IF_PREVIOUS_STEP_COMPLETED:
				validateIFPreviousStepCompleted(stepDetailsModel, woDetails);
				break;
			}
		}

	}

	private void validateStepIDForDecision(StepDetailsModel stepDetailsModel) {
		String executionType = this.flowChartDao.validateStepIDForDecision(stepDetailsModel);
		if (!StringUtils.equalsIgnoreCase("ericsson.Decision",executionType)) {
			throw new ApplicationException(500, "Invalid StepID for Decision Step.");
		}
	}

	private void validateDecisionValue(StepDetailsModel stepDetailsModel, WorkOrderModel woDetails) {
		List<ChildStepDetailsModel> descisionChildStepDetails =	woExecutionDAO.getDescisionStepDetails(stepDetailsModel.getFlowChartStepId(),stepDetailsModel.getFlowChartDefID(), woDetails.getwOID());
		int count=0;
		for(ChildStepDetailsModel ch : descisionChildStepDetails) {
			if(StringUtils.equalsIgnoreCase(stepDetailsModel.getDecisionValue(),ch.getLabelName())){
				count++;
				break;
			}
		}
		if(count==0) {
			throw new ApplicationException(500, "Invalid Decision value.");
		}
	}

	public ResponseEntity<Response<List<ViewNetworkElementModel>>> getNetworkElement(
			NetworkElementNewModel networkElement) {
		Response<List<ViewNetworkElementModel>> result = new  Response<>();
		List<ViewNetworkElementModel> networkElementList = new ArrayList<>();
		try {
			LOG.info("getNetworkElement:Start");
			
			String domainSubDomain = networkElement.getDomain();
			
			if(StringUtils.isNotEmpty(domainSubDomain)) {
				String[] domains = domainSubDomain.split(AppConstants.FORWARD_SLASH);
				
				if(domains.length > 1) {
					networkElement.setDomain(StringUtils.trim(domains[0]));
					networkElement.setSubDomain(StringUtils.trim(domains[1]));
				} else {
					result.addFormMessage("Please provide Domain and SubDomain");
					result.setResponseData(networkElementList);
					return new ResponseEntity<>(result, HttpStatus.OK);
				}				
			}

			validateNetworkElementDetails(networkElement);
			networkElementList = erisiteManagmentDAO.getNetworkElement(networkElement);
			
			if (networkElementList.isEmpty()) {
				result.addFormMessage(NO_DATA_FOUND);
				result.setResponseData(networkElementList);
			} else {
				result.setResponseData(networkElementList);
			}
			LOG.info("getNetworkElement:End");
		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	private void validateNetworkElementDetails(NetworkElementNewModel networkElement) {
		validationUtilityService.validateNetworkElementDetails(networkElement);

	}

	protected void validateAppendNetworkElement(NetworkElementNewModel elements, String apiName) {
		validationUtilityService.validateAppendNetworkElement(elements, apiName);
	}
	
	/*
	 * ISF feed Service Method*/
	public Response<List<Map<String, Object>>> subscribeIsfFeeds(FeedModel feed) {
		
		Response<List<Map<String, Object>>> apiResponse=new Response<>();
		
	try {	
		
		if(feed.getColumns().toString().equalsIgnoreCase(AppConstants.NULLV)) {
			throw new ApplicationException(200, "NULL value not excepted in Columns");
		}
		
		String columns=String.format(dataFormat, feed.getColumns().toString().replace(AppConstants.SQUARE_OPEN_BRACKET, StringUtils.EMPTY).replace(AppConstants.SQUARE_CLOSE_BRACKET, StringUtils.EMPTY));
		String orderBy=validateAndGetOrderByColumn(feed.getOrderBy().trim());
//		String startDate=String.format(dataFormat, feed.getEventStartDate().substring(0, 10));
//		String endDate=String.format(dataFormat, feed.getEventEndDate().substring(0, 10));
		String startDate=String.format(dataFormat, feed.getEventStartDate());
		String endDate=String.format(dataFormat, feed.getEventEndDate());
		
		if(feed.getFilters().isEmpty() || feed.getFilters() == null || feed.getFilters().toString().equalsIgnoreCase("[null]")) {
			throw new ApplicationException(200, "Filters field can't be blank");
		}
		
		List<Project> projects=feed.getFilters();
		StringBuilder projectId=new StringBuilder();		
	
		for(Project project : projects) {
			projectId.append(project.getProjectId()+AppConstants.CSV_CHAR_DOUBLE_SEMICOLON);
		}		
		String projectsId=String.format(dataFormat,(String) projectId.subSequence(0, projectId.toString().length()-1));


		if (projectId.toString().contains(AppConstants.SMALL_NULL_AS_A_STRING)) {
			throw new ApplicationException(200, "ProjectId can't be left Null/Blank");
		}
		String[] projectIdArr= projectId.toString().split(",");
		if(projectIdArr.length > 10) {
			throw new ApplicationException(200, "Project Id's count exceeds limit 10 ");
		}
		
		// Validation on user data from FeedModel
		validateSubscribeIsfFeeds(feed, columns, projectsId);
		
		String calcolumns=getCaculativeColumNames(columns, orderBy.replace(AppConstants.SINGLE_QUOTE, StringUtils.EMPTY));
		
		// calling main query to get Isf feed data
		List<Map<String, Object>> response=erisiteManagmentDAO.getSubscribeIsfFeeds(calcolumns, projectsId, orderBy, startDate, endDate);
		
		if(CollectionUtils.isEmpty(response)) {
			apiResponse.addFormMessage("No data Availbale for the given inputs");
		}
		
		apiResponse.setResponseData(response);
	}catch (ApplicationException e) {
		apiResponse.addFormError(e.getMessage());
	}catch (Exception e) {
		e.printStackTrace();
	}	
		return apiResponse;
	}
	
	/* 
	 * Validating User Inputs: Column name Alias, StartDate, 
	 * EndDate, Valid Project Id's, MarketAreas, no of Project */
	private boolean validateSubscribeIsfFeeds(FeedModel feed, String columns, String projectId) throws ParseException {
		boolean flag=false;
		
		//1. validation on startDate and EndDate
     		validateStartEndDate(feed);
		
		
		// 2. Validation on MarketArea
		Boolean isExist=erisiteManagmentDAO.validateMarketArea(feed.getMarketArea());
		if(Boolean.FALSE.equals(isExist)) {
			throw new ApplicationException(200, "MarketArea doesn't exist");
		}
		
		// 3. validation on Project Id's
			validateProjectIdFeed(projectId, feed.getMarketArea());
		
		// 4. Validation on Alias Columns
		String alias=columns.replace(COMMA, AppConstants.REPLACE_FORMAT);
		
		if(alias.equals(AppConstants.BLANK_VAL)) {
			flag=true;
		}else {
			List<Map<String, String>> result=erisiteManagmentDAO.validateAlias(String.format(stringFormat, alias));
			
			StringBuilder notExistingColumns=new StringBuilder();
			
			for(Map<String, String> map : result) {		
				if(map.toString().contains(RESULT)) {
					notExistingColumns.append(map.get("StringAlias")+COMMA);
				}
			}
			if(StringUtils.isNotEmpty(notExistingColumns)) {
				throw new ApplicationException(200, "Invalid column name! Either the Columns are not existing or it is not active: "+notExistingColumns.substring(0, notExistingColumns.length()-2));
			}else {
				flag=true;
			}	
		}
		
		
		LOG.info("Validation SUCCESS SubscribeIsfFeeds:: {}", flag);
		
		return flag;
	}
	/* get the list of All calculative Column name from mapping table*/
	private String getCaculativeColumNames(String alias, String orderBy){	
		
		List<String> listColumns=null;
		List<String> listAlias=null;
		
			
		if(alias.equalsIgnoreCase(AppConstants.BLANK_VAL)) {
			listColumns= erisiteManagmentDAO.getCalculativeColums(alias,alias);
			listAlias=erisiteManagmentDAO.getAlias(alias, alias);
		}else {
			listColumns= erisiteManagmentDAO.getCalculativeColums(AppConstants.NOTBLANK,alias.replace(COMMA, AppConstants.REPLACE_FORMAT2));
			listAlias=erisiteManagmentDAO.getAlias(AppConstants.NOTBLANK,alias.replace(COMMA, AppConstants.REPLACE_FORMAT2));
		}
		
		StringBuilder columns=new StringBuilder();
		for(int i=0;i<listColumns.size();i++) {
			
			String col=listColumns.get(i);
			
			if(col.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_PLANNEDSTARTDATE) 
					|| col.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_PLANNEDENDDATE)
					|| col.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_ACTUALSTARTDATE)
					|| col.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_ACTUALENDDATE)
					|| col.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_CREATED_DATE)
					|| col.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_LASTMODIFIEDDATE)
					|| col.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_PLAN_STARTTIME)
					|| col.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_PLAN_STARTDATE)
					|| col.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_PLAN_LASTMODIFIEDDATE)
					|| col.equalsIgnoreCase(AppConstants.TBL_AUDITDETAILS_DATECREATED)) {
				
				columns.append(AppConstants.CONVERT_VARCHAR+col+",121) AS "+listAlias.get(i)+COMMA);
				
			}else {
				columns.append(listColumns.get(i)+AppConstants.AS+listAlias.get(i)+COMMA);
			}
		}
		
		// Condition: if orderBy is Timestamp is should be concated with the calculative column
		// and if orderBy column doesn't belong to the calculative column then throw exception: ORDER BY items must appear in the select list if SELECT DISTINCT is specified.

		String selectColumns=columns.toString().toLowerCase()
				.replace(AppConstants.SQUARE_OPEN_BRACKET, StringUtils.EMPTY)
				.replace(AppConstants.SQUARE_CLOSE_BRACKET, StringUtils.EMPTY);
		
		String orderByColumn=orderBy.toLowerCase()
				.replace(AppConstants.SQUARE_OPEN_BRACKET, StringUtils.EMPTY)
				.replace(AppConstants.SQUARE_CLOSE_BRACKET, StringUtils.EMPTY);

		if(orderBy.contains(AppConstants.TIMESTAMP_CALCULATIVE_COL)) {
			columns.append(orderBy);
			columns.append(AppConstants.AS);
			columns.append(AppConstants.TIME_STAMP);
			return String.format(dataFormat, columns.toString());
		}
		else if(!selectColumns.contains(orderByColumn)) {
			throw new ApplicationException(200, "OrderBy field should contain column name provided in columns list.");
		}else {
			return String.format(dataFormat, columns.toString().substring(0, columns.length()-2));		
		}
		
	}
	/*Method to validate startDate and EndDate*/
	private boolean validateStartEndDate(FeedModel feed) throws ParseException {
		try {
	        String startDate = feed.getEventStartDate();
	        String endDate = feed.getEventEndDate();
	        SimpleDateFormat format = new SimpleDateFormat(AppConstants.UI_DATE_FORMAT);
	        Date date1 = format.parse(startDate);
	        Date date2 = format.parse(endDate);
	
	        Date previousStartDate = new Date( System.currentTimeMillis()  - (24 * 60 * 60 * 1000));
	        Date currentDateTime = new Date(System.currentTimeMillis());
	        long timeDifference = date2.getTime() - date1.getTime(); 
	        Integer timeInterval= appConfig.getIntegerProperty(ConfigurationFilesConstants.FEED_TIME_INTERVAL);
	       
	       
	       
	        // StartDate condition 
	        if(!previousStartDate.before(date1)) {
	            throw new ApplicationException(200,"Start Date shouldn't be less then the 24 hr of current date");
	        }
	        // End Date condition
	        if(!date1.before(date2)) {
	     	   throw new ApplicationException(200,"End date couldn't be less than or equal to Start Date");
	        }
	        else if(!date2.before(currentDateTime)){
	        	throw new ApplicationException(200,"End date should not be greater than the Current Date");
	        }
	        
	        // Difference between start date & endate is 5 min 
	        if(timeDifference > timeInterval) {
	            throw new ApplicationException(200,"Difference between start Date and End Date can't be greater than 5 minutes");
	        }
	        
	        
	        
		}catch (ParseException ex) {
			throw new ApplicationException(200,"Invalid Date format! ex: yyyy-MM-dd HH:mm:ss");
		}  
    return true;
    
    }
	private boolean validateProjectIdFeed(String projectId, String marketArea) {
		
		boolean flag=false;
		
		List<Map<String, String>> result=erisiteManagmentDAO.validateProjectIdFeed(projectId);
		
		StringBuilder notExistingProjectIds=new StringBuilder();
		for(Map<String, String> map : result) {		
			if(map.toString().contains(RESULT)) {
				notExistingProjectIds.append(map.get(AppConstants.STRINGPROJECTID)+COMMA);
			}
		}
		if(StringUtils.isNotEmpty(notExistingProjectIds)) {
			throw new ApplicationException(200, "Project Ids doesn't exists :"+notExistingProjectIds.substring(0, notExistingProjectIds.length()-1));
		}else {
			String manaprojectId=projectId.replace(AppConstants.CSV_CHAR_COMMA, AppConstants.COMMA_DOUBLE_QUOTE);
			List<Map<String, Integer>> resultMarket=erisiteManagmentDAO.validateMarketAreaProjectId(manaprojectId, String.format(dataFormat, marketArea));
			
			if(!resultMarket.toString().contains(AppConstants.SUCCESS_RESULT)) {
				StringBuilder projectIdNotBelongToMarketArea=new StringBuilder();
				
				for(Map<String, Integer> map : resultMarket) {			
					projectIdNotBelongToMarketArea.append(map.get(AppConstants.PROJECT_ID_2)+COMMA);
				}
				if(StringUtils.isNotEmpty(projectIdNotBelongToMarketArea)) {
					throw new ApplicationException(200, AppConstants.PROJECTID_MSG+projectIdNotBelongToMarketArea.substring(0, projectIdNotBelongToMarketArea.length()-2)+" doesn't belong to MarketArea: "+marketArea);
				}else {
			flag=true;
				}
			}else {
				flag=true;
			}
		}	
		
		return flag;
	}
	private String validateAndGetOrderByColumn(String alias) {
		if(alias.isEmpty()) {
			alias=AppConstants.TIMESTAMP;
		}else {
			alias=String.format(dataFormat, alias);
		}
		String result=erisiteManagmentDAO.getOrderByColumn(alias);
		
		if(result.equalsIgnoreCase(AppConstants.TBL_AUDITDETAILS_DATECREATED)
			|| result.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_PLANNEDSTARTDATE) 
			|| result.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_PLANNEDENDDATE)
			|| result.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_ACTUALSTARTDATE)
			|| result.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_ACTUALENDDATE)
			|| result.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_CREATED_DATE)
			|| result.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_LASTMODIFIEDDATE)
			|| result.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_PLAN_STARTTIME)
			|| result.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_PLAN_STARTDATE)
			|| result.equalsIgnoreCase(AppConstants.TBL_WORK_ORDER_PLAN_LASTMODIFIEDDATE)) {
			result=AppConstants.CONVERT_VARCHAR+result+",121)";
		}
		
		if(result.equalsIgnoreCase("Invalid OrderBy name!")) {
			throw new ApplicationException(200, result);
		}		
		return String.format(dataFormat, result);
	}
	
	public URI uploadFileInCommonContainerFromByteArray(byte[] bytesEmpFile, String blobName, String jarFileName) {
		URI uri = null;
		CloudBlockBlob blob = null;
		try {
			blob = commonBlobContainer.getBlockBlobReference(blobName+jarFileName);
			blob.uploadFromByteArray(bytesEmpFile, 0, bytesEmpFile.length);
			uri = blob.getUri();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException | IOException e) {
			LOG.error(e.getMessage());
		}
		return uri;
	}

	public ResponseEntity<Response<SharePointDetailModel>> getSecretAuthorizationValueForServerBot(String siteName) {
		Response<SharePointDetailModel> response = new Response<>();
		try {
			SharePointDetailModel sharePointDetailModel = new SharePointDetailModel();
			List<String> keys;
			String clientID = appUtilDAO.getClientIDbySiteName(siteName);
			keys = aesUtil.getAESKeyFromKeyVault();
			sharePointDetailModel.setAeskey(keys.get(0));
			sharePointDetailModel.setIvsKey(keys.get(1));
            StringBuilder sharePoint = new StringBuilder(SHARE_POINT);
            StringBuilder sharePointClient = sharePoint.append(clientID);
			Object secretFromRedis = redisService.getEmployeeSessionData(sharePointClient.toString());
			String encryptedClientID = aesUtil.encryptBase64(clientID);
			sharePointDetailModel.setClientID(encryptedClientID);
			if (secretFromRedis == null) {
				LOG.info("Inside secretFromRedis Null code Block =====================");

				String keyVaultName = configurations.getStringProperty(ConfigurationFilesConstants.AZURE_KEYVAULT_NAME);
				VaultModel vaultModel = azureService.getActiveSecretInfoByVaultNameAndSecretType(keyVaultName,
						clientID);
				if (vaultModel != null) {
					AzureAppModel azureAppModel = azureService.getInfoByAzureAppName(
							configurations.getStringProperty(ConfigurationFilesConstants.AZURE_APP_NAME));

					String secretValue = clientSecretKeyValueProvider.getSecretValue(azureAppModel, vaultModel);

					String encryptedSecretKey = aesUtil.encryptBase64(secretValue);
					redisService.setEmployeeEmailAndSignumFromRedis(sharePointClient.toString(), encryptedSecretKey);

					sharePointDetailModel.setSecretKey(encryptedSecretKey);
					response.setResponseData(sharePointDetailModel);

				}
			} else {

				LOG.info("Inside secretFromRedis Not Null code Block =====================");
				sharePointDetailModel.setSecretKey(secretFromRedis.toString());
				response.setResponseData(sharePointDetailModel);
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
}
