package com.ericsson.isf.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.AccessManagementDAO;
import com.ericsson.isf.dao.AutoSenseDao;
import com.ericsson.isf.dao.ExternalInterfaceManagmentDAO;
import com.ericsson.isf.dao.FlowChartDAO;
import com.ericsson.isf.dao.NetworkElementDao;
import com.ericsson.isf.dao.ProjectDAO;
import com.ericsson.isf.dao.WOExecutionDAO;
import com.ericsson.isf.dao.WorkOrderPlanDao;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.BookingDetailsModel;
import com.ericsson.isf.model.DynamicMessageModel;
import com.ericsson.isf.model.NetworkElementNewModel;
import com.ericsson.isf.model.ParallelWorkOrderDetailsModel;
import com.ericsson.isf.model.PreviousStepDetailsModel;
import com.ericsson.isf.model.ProjectAllDetailsModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.SessionDataStoreModel;
import com.ericsson.isf.model.StepDetailsModel;
import com.ericsson.isf.model.TmpWorkflowStepAutoSenseRuleModel;
import com.ericsson.isf.model.WorkOrderModel;
import com.ericsson.isf.model.WorkOrderOutputFileModel;
import com.ericsson.isf.model.botstore.ServerBotModel;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.ApplicationMessages;
import com.ericsson.isf.util.ConfigurationFilesConstants;

/**
 * 
 * This is a common utility service class which provides all validation method
 * which are mostly used by modules.
 * 
 * @author eakinhm
 *
 */
@Service
public class ValidationUtilityService {

	private static final String LAST_MODIFIED_BY_SIGNUM_MUST_BE_PM_DR_OR_BOOKED_RESOURCE_FOR_THE_GIVEN_PROJECT = "lastModifiedBy Signum must be PM, active DR or Booked Resource for the given project!";
	private static final String LAST_MODIFIED_BY_SIGNUM_MUST_BE_PM_DR_OR_PREVIOUS_ASSIGNEE_OF_THE_WO_FOR_THE_GIVEN_PROJECT = "lastModifiedBy Signum must be PM, active DR of the project or previous Assignee of the WO!";
	private static final String PAGE_LENGTH_NOT_DEFINED = "page length not defined!!!!";
	private static final String PAGE_LENGTH_CHECK = "Page Length cannot be greater than ";
	private static final String PAGE_LENGTH2 = "Page Length";
	private static final String  INACTIVE_PROJECTID = "Project ID is Invalid/Inactive/Closed/Rejected. Please provide a valid Project ID.";
	private static final String WO_REOPENED_MSG_SRID = "WO cannot be reopened as it is linked to SRID";
	private static final String WO_REINSTATE_MSG_SRID = "WO cannot be reinstated as it is linked to SRID";
	private static final String NEID_COUNT_EXCEED_MSG = "Maximum limit exceeded- Maximum %s Network Elements allowed.";

	@Autowired
	private WorkOrderPlanService workOrderPlanService;

	@Autowired
	private WorkOrderPlanDao workOrderPlanDao;

	@Autowired
	private ActivityMasterService activityMasterService;

	@Autowired
	private AccessManagementDAO accessManagementDAO;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private WOExecutionService woExecutionService;

	@Autowired
	private AutoSenseDao autoSenseDao;

	@Autowired
	private FlowChartDAO flowChartDao;

	@Autowired
	private WOExecutionDAO wOExecutionDAO;

	@Autowired
	private ProjectDAO projectDao;
	
	@Autowired
	private ExternalInterfaceManagmentDAO externalInterfaceDao;
	
	@Autowired
    private ApplicationConfigurations configurations;

	@Autowired
	private NetworkElementDao networkElementDao;
	
	@Autowired
	private EnvironmentPropertyService environmentPropertyService;

	private static final String TASK_ID = "TaskID";
	private static final String VALIDATIONS = "validations";
	private static final String PLEASE_PROVIDE_VALUE = "Please provide valid %s!";
	private static final String WOID = "WOID: ";
	private static final String WORK_ORDER_ID = "Work Order ID";
	private static final String STEP_ID = "stepID";
	private static final String DECISION_VALUE = "Decision Value";
	private static final String ERROR_MSG_PREVIOUS_STEP = "Please complete previous step first!";
	private static final String PROJECTID_SUBACTIVITYID_LOGGEDINSIGNUM = "Project ID, Subactivity ID, Logged in signum";
	private static final Logger LOG = LoggerFactory.getLogger(ValidationUtilityService.class);

	private static final String SOURCENAME = "sourceName";

	private static final String SOURCEDOMAIN = "sourceDomain";

	private static final String IPADDRESS = "ipaddress";

	private static final String BROWSER = "browser";
	private static final String PAGE_LENGTH = "validatePageLengthForNullOrBlank";
	private static final String PAGE_OFFSET = "validateOffsetForNullOrBlank";
	private static final String PROJECT_ID = "validateProjectIDNullOrBlankOrDB";

	private static final String DOMAIN = "domain";
	private static final String SUB_DOMAIN = "subDomain";
	private static final String TECHNOLOGY = "technology";
	private static final String VENDOR = "vendor";
	private static final String ELEMENT_TYPE = "elementType";
	private static final String TYPE = "type";
	private static final String NAME = "name";
	private static final String MARKET = "market";
	private static final String UPLOADED_BY = "uploadedBy";
	private static final String CUSTOMER = "validateCustomerNullOrBlankOrDB";
	private static final String COUNTRY = "validateCountryNullOrBlankOrDB";
	private static final String SECTOR = "sector";
	private static final String LENGTH = " should be maximum 100 characters!";
	private static final String COMMA_EXCEPTION = "The Comma (,) cannot be entered for the field Type/Name/Market. Please Remove Comma and try again"; 
	

	public void validateWOIDForNullOrZero(Integer woID) {

		WorkOrderModel workOrderModel = getWorkOrderModel(woID);

		validateWOIDForNullOrZero(woID, workOrderModel);
	}

	public void validateWOIDForNullOrZero(Integer woID, WorkOrderModel workOrderModel) {

		validateIntegerForNullOrZero(woID, WORK_ORDER_ID);

		validateWorkOrderModelForNull(workOrderModel);
	}

	public void validateIntegerForNullOrZero(Integer integerValue, String variableName) {

		if (integerValue == null || integerValue == 0) {

			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}

	}

	public void validateIntForZero(int integerValue, String variableName) {

		if (integerValue == 0) {

			throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}

	}

	public void validateIntegerForNull(Integer integerValue, String variableName) {

		if (integerValue == null) {

			throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}

	}

	private void validateWorkOrderModelForNull(WorkOrderModel workOrderModel) {

		if (workOrderModel == null) {

			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, WORK_ORDER_ID));
		}
	}

	public void validateWOIDIsActive(int woID) {

		WorkOrderModel workOrderModel = getWorkOrderModel(woID);

		validateWOIDIsActive(workOrderModel);

	}

	public void validateWOIDIsActive(WorkOrderModel workOrderModel) {

		if (!workOrderModel.getActive()) {
			throw new ApplicationException(500, "Given woID is not active!");
		}

	}

	public void validateWOIDForClosedAndRejectedtatus(int woID) {

		WorkOrderModel workOrderModel = getWorkOrderModel(woID);

		validateWOIDForClosedAndRejectedStatus(workOrderModel);
	}

	public void validateWOIDForClosedAndRejectedStatus(WorkOrderModel workOrderModel) {

		if (StringUtils.equalsIgnoreCase(AppConstants.WO_STATUS_REJECTED, workOrderModel.getStatus())
				|| StringUtils.equalsIgnoreCase(AppConstants.CLOSED, workOrderModel.getStatus())) {

			throw new ApplicationException(500,
					"Work Order is either Rejected or already Closed, Kindly provide the correct WOID");

		}
	}

	public void validateStringForBlankAndLength(String stringValue, String variableName, int length) {

		validateStringForBlank(stringValue, variableName);

		validateLength(stringValue, length, variableName);
	}

	public void validateLength(String stringValue, int length, String variableName) {

		if (StringUtils.length(stringValue) > length) {

			throw new ApplicationException(500, variableName + " should be less than " + length + "!");
		}

	}

	public void validateStringForBlank(String stringValue, String variableName) {

		if (StringUtils.isBlank(stringValue)) {

			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}

	}

	public void validateIfWorkorderIsUnassigned(Integer woID) {

		WorkOrderModel workOrderModel = getWorkOrderModel(woID);

		validateIfWorkorderIsUnassigned(workOrderModel);
	}

	public WorkOrderModel getWorkOrderModel(Integer woID) {

		WorkOrderModel workOrderModel = workOrderPlanService.getWorkOrderDetailsById(woID);

		validateWorkOrderModelForNull(workOrderModel);

		return workOrderModel;
	}

	public void validateIfWorkorderIsUnassigned(WorkOrderModel workOrderModel) {

		if (StringUtils.isBlank(workOrderModel.getSignumID())) {

			throw new ApplicationException(500, "Given Work Order is unassigned!");
		}

	}

	public void validateWOIDForClosedDeferredAndRejectedStatus(Integer woID) {

		WorkOrderModel workOrderModel = getWorkOrderModel(woID);

		validateWOIDForClosedDeferredAndRejectedStatus(workOrderModel);
	}

	public void validateWOIDForClosedDeferredAndRejectedStatus(WorkOrderModel workOrderModel) {

		if (StringUtils.equalsIgnoreCase(AppConstants.WO_STATUS_REJECTED, workOrderModel.getStatus())
				|| StringUtils.equalsIgnoreCase(AppConstants.WO_STATUS_DEFERRED, workOrderModel.getStatus())
				|| StringUtils.equalsIgnoreCase(AppConstants.CLOSED, workOrderModel.getStatus())) {

			throw new ApplicationException(200,
					"Work Order is either Rejected, Deferred or already Closed, Kindly provide the correct WOID");

		} else if (StringUtils.equalsIgnoreCase(AppConstants.PLANNED, workOrderModel.getStatus())) {
			throw new ApplicationException(200, "Work Order is in Planned state, Kindly provide the correct WOID");
		}
	}

	public void validateExternalSourceNameWithWoID(Integer woID, String externalSourceName) {

		WorkOrderModel workOrderModel = getWorkOrderModel(woID);

		validateExternalSourceNameWithWoID(externalSourceName, workOrderModel);
	}

	public void validateExternalSourceNameWithWoID(String externalSourceName, WorkOrderModel workOrderModel) {

		if (!StringUtils.equalsIgnoreCase(externalSourceName, workOrderModel.getCreatedBy())) {

			throw new ApplicationException(500,
					WOID + workOrderModel.getwOID() + " is not created by given external source Name!");
		}

	}

	/**
	 * This method validate SignumID for blank string and valid signum against given
	 * woid
	 * 
	 * @param signumID
	 * @param woDetails
	 */
	public void validateSignumIDForBlankAndUnassigned(String signumID, Integer woID, String variableName) {

		WorkOrderModel workOrderModel = getWorkOrderModel(woID);

		// validate signum for blank string
		validateStringForBlank(signumID, variableName);

		// validate signumID is valid against given woid
		validateIfWorkOrderAssignedToSignum(signumID, workOrderModel);

	}

	/**
	 * This method validate SignumID for blank string and valid signum against given
	 * woid
	 * 
	 * @param signumID
	 * @param workOrderModel
	 */
	public void validateSignumIDForBlankAndUnassigned(String signumID, WorkOrderModel workOrderModel,
			String variableName) {

		// validate signum for blank string
		validateStringForBlank(signumID, variableName);

		// validate signumID is valid against given woid
		validateIfWorkOrderAssignedToSignum(signumID, workOrderModel);

	}

	/**
	 * This method validate if given signum is valid, against given woid
	 * 
	 * @param signumID
	 * @param woID
	 */
	public void validateIfWorkOrderAssignedToSignum(String signumID, Integer woID) {

		WorkOrderModel workOrderModel = getWorkOrderModel(woID);

		// validate signumID is valid against given woid
		validateIfWorkOrderAssignedToSignum(signumID, workOrderModel);
	}

	/**
	 * This method validate if given signum is valid, against given woid
	 * 
	 * @param signumID
	 * @param workOrderModel
	 */
	public void validateIfWorkOrderAssignedToSignum(String signumID, WorkOrderModel workOrderModel) {

		if (!StringUtils.equalsIgnoreCase(workOrderModel.getSignumID(), signumID)) {

			throw new ApplicationException(500, "Work Order is not assigned to signum " + signumID);
		}
	}

	public void validateSource(String source) {

		if (!workOrderPlanDao.isExternalSourceExists(source)) {

			throw new ApplicationException(500, ApplicationMessages.INVALID_SOURCE_NAME);
		}
	}

	public void validateReferer(String refferer) {

		if (!workOrderPlanDao.isExternalSourceExists(refferer)) {

			throw new ApplicationException(500, ApplicationMessages.INVALID_REFERER_NAME);
		}
	}

	public void validatePriorityForEMS(String priority) {
		if (getPriorityByName(priority).isEmpty() && !StringUtils.equalsIgnoreCase(AppConstants.LIVE, priority)) {
			throw new ApplicationException(500, ApplicationMessages.INVALID_PRIORITY_NAME);
		}
	}

	public void validatePriority(String priority) {
		if (getPriorityByName(priority).isEmpty()) {
			throw new ApplicationException(500, ApplicationMessages.INVALID_PRIORITY_NAME);
		}
	}

	private List<String> getPriorityByName(String priority) {
		return workOrderPlanDao.getPriorityByName(priority);
	}

	public void validateSignumForResignedStatus(String signum, String variableName) {

		if (StringUtils.isNotBlank(signum)
				&& CollectionUtils.isEmpty(activityMasterService.getEmployeesByFilter1(signum))) {
			throw new ApplicationException(500, variableName + " value: " + signum + " has resigned status!");
		}
	}

	public void validateSignumForExistingEmployee(String assignedTo) {
		if (StringUtils.isNotBlank(assignedTo) && !accessManagementDAO.isSignumExistsEmp(assignedTo)) {
			throw new ApplicationException(500, ApplicationMessages.INVALID_SIGNUM);
		}
	}

	public void validateServerBotModel(ServerBotModel serverBotModel) {
		validateWOIDForNullOrZero(serverBotModel.getwOID());

		if (StringUtils.isNotBlank(serverBotModel.getRefferer())
				&& !serverBotModel.getRefferer().equalsIgnoreCase(AppConstants.SOFT_HUMAN)
				&& !serverBotModel.getRefferer().equalsIgnoreCase("EMAIL")) {
			WorkOrderModel workOrderModel = workOrderPlanService.getWorkOrderDetailsById(serverBotModel.getwOID());
			if (!workOrderModel.getSignumID().equalsIgnoreCase(serverBotModel.getSignumID())) {
				throw new ApplicationException(500,
						"work order " + serverBotModel.getwOID() + "not assigned to " + serverBotModel.getSignumID());
			}
			validateIntegerForNullOrZero(serverBotModel.getSubActivityFlowChartDefID(),
					"Sub Activity Flow Chart Def ID.");
			List<LinkedHashMap<String, Object>> listOfWorkOrderModel = workOrderPlanService
					.getAllBookingDetails(serverBotModel.getwOID(), serverBotModel.getSignumID(), false, "");
			validateTaskId(serverBotModel, listOfWorkOrderModel);
			if (StringUtils.isEmpty(serverBotModel.getExecutionType())) {
				throw new ApplicationException(500,
						"Invalid Execution Type for workorder Id" + serverBotModel.getwOID());
			} else {
				validateExecutionType(serverBotModel.getExecutionType(), listOfWorkOrderModel);
			}

			if (StringUtils.isEmpty(serverBotModel.getStepID())) {
				throw new ApplicationException(500, ApplicationMessages.INVALID_STEPID);
			} else {
				validateStepIdAndName(serverBotModel.getStepID(), listOfWorkOrderModel);
			}

			if (!serverBotModel.getRefferer().equalsIgnoreCase("ui")
					&& workOrderModel.getFlowchartdefid() != serverBotModel.getSubActivityFlowChartDefID()) {
				throw new ApplicationException(500,
						"invalid Sub Activity Flow Chart Def ID." + serverBotModel.getFlowChartDefID());
			}

		}
	}

	private void validateStepIdAndName(String stepId, List<LinkedHashMap<String, Object>> listOfWorkOrderModel) {
		boolean matchExecutionType = false;
		for (int i = 0; i < listOfWorkOrderModel.size(); i++) {
			if (listOfWorkOrderModel.get(i).get("StepID") != null
					&& listOfWorkOrderModel.get(i).get("StepID").toString().equalsIgnoreCase(stepId)) {
				matchExecutionType = true;
				break;
			}
		}

		if (Boolean.FALSE.equals(matchExecutionType)) {
			throw new ApplicationException(500, ApplicationMessages.INVALID_STEPID);
		}

	}

	private void validateTaskId(ServerBotModel serverBotModel,
			List<LinkedHashMap<String, Object>> listOfWorkOrderModel) {
		validateIntegerForNullOrZero(serverBotModel.getTaskID(), "Task ID");
		validateTaskIdExist(serverBotModel.getTaskID(), listOfWorkOrderModel);

	}

	private void validateTaskIdExist(Integer taskId, List<LinkedHashMap<String, Object>> listOfWorkOrderModel) {
		boolean matchTaskIdResult = false;
		for (int i = 0; i < listOfWorkOrderModel.size(); i++) {
			if (listOfWorkOrderModel.get(i).get(TASK_ID) != null
					&& listOfWorkOrderModel.get(i).get(TASK_ID).equals(taskId)) {
				matchTaskIdResult = true;
				break;
			}
		}
		if (Boolean.FALSE.equals(matchTaskIdResult)) {
			throw new ApplicationException(500, ApplicationMessages.INVALID_TASKID);
		}

	}

	private void validateExecutionType(String executionType, List<LinkedHashMap<String, Object>> listOfWorkOrderModel) {
		boolean matchExecutionType = false;
		for (int i = 0; i < listOfWorkOrderModel.size(); i++) {
			if (listOfWorkOrderModel.get(i).get("ExecutionType") != null
					&& listOfWorkOrderModel.get(i).get("ExecutionType").toString().equalsIgnoreCase(executionType)) {
				matchExecutionType = true;
				break;
			}
		}

		if (Boolean.FALSE.equals(matchExecutionType)) {
			throw new ApplicationException(500, "Invalid Execution Type");
		}
	}

	/**
	 * This method validate if given lastModifiedBy is BLANK or UnassignedWO and set
	 * lastModifiedBy to SPM
	 * 
	 * @param workOrderOutputFileModel
	 * @param woDetails
	 */
	public void validateSignumIDForBlankLastModifiedByORUnassignedWO(WorkOrderOutputFileModel workOrderOutputFileModel,
			WorkOrderModel woDetails) {
		int projectId = woDetails.getProjectid();

		ProjectAllDetailsModel projectDetail = projectService.getProjectDetails(projectId);

		workOrderOutputFileModel.setLastModifiedBy(projectDetail.getProjectCreator());
	}

	/**
	 * This method validate for Blank Signum or UnassignedWO and set lastModifiedBy
	 * to SPM
	 * 
	 * @param workOrderModel
	 * @param workOrderOutputFileModel
	 */
	public void validateSignumIDForBlankAndUnassignedWOforOutputUrl(WorkOrderModel workOrderModel,
			WorkOrderOutputFileModel workOrderOutputFileModel) {

		if (StringUtils.isBlank(workOrderModel.getSignumID())) {

			// validate signum for blank string
			validateSignumIDForBlankLastModifiedByORUnassignedWO(workOrderOutputFileModel, workOrderModel);
		} else {
			// here SPM can be validated.
			boolean isSPM = validateLastModifiedByForSPM(workOrderOutputFileModel, workOrderModel);

			if (isSPM) {

				// if SPM signum is given in lastModifiedBy, then it's valid and update it to
				// SPM
				validateSignumIDForBlankLastModifiedByORUnassignedWO(workOrderOutputFileModel, workOrderModel);
			} else {

				// validate signumID is valid against given woid
				validateIfWorkOrderAssignedToSignum(workOrderOutputFileModel.getLastModifiedBy(), workOrderModel);
			}
		}
	}

	/**
	 * This method validate if given lastModifiedBy is SPM or not.
	 * 
	 * @param workOrderOutputFileModel
	 * @param woDetails
	 * @return boolean
	 */
	public boolean validateLastModifiedByForSPM(WorkOrderOutputFileModel workOrderOutputFileModel,
			WorkOrderModel woDetails) {
		int projectId = woDetails.getProjectid();

		ProjectAllDetailsModel projectDetail = projectService.getProjectDetails(projectId);

		if (projectDetail.getProjectCreator().equalsIgnoreCase(workOrderOutputFileModel.getLastModifiedBy())) {
			return true;
		} else {
			return false;
		}
	}

	public void validateStepIdAndExecutionType(String flowChartStepId, int flowChartDefID, String executionType) {

		String executionTypeValid = workOrderPlanService.validateStepIdAndExecutionType(flowChartStepId,
				flowChartDefID);
		if (StringUtils.isBlank(executionTypeValid)) {
			throw new ApplicationException(500, "Invalid FlowChartStepID");
		}
		if (!StringUtils.equalsIgnoreCase(executionType, executionTypeValid)) {
			throw new ApplicationException(500, "Invalid Execution Type for this StepID: " + flowChartStepId);

		}
	}

	/**
	 * This method check if a step is already started.
	 * 
	 * 
	 * @param serverBotModel
	 * @param woID
	 * @throws Exception
	 */
	public void validateParallelWorkOrderExecution(ServerBotModel serverBotModel, Integer woID) {

		WorkOrderModel workOrderModel = getWorkOrderModel(woID);

		validateParallelWorkOrderExecution(serverBotModel, workOrderModel);
	}

	/**
	 * This method check if a step is already started.
	 * 
	 * 
	 * @param serverBotModel
	 * @param woDetails
	 * @throws Exception
	 */
	public void validateParallelWorkOrderExecution(ServerBotModel serverBotModel, WorkOrderModel woDetails) {

		ParallelWorkOrderDetailsModel model = new ParallelWorkOrderDetailsModel();
		model.setSignumID(woDetails.getSignumID());
		model.setIsApproved("0");
		model.setExecutionType(serverBotModel.getExecutionType());
		model.setWoid(serverBotModel.getwOID());
		model.setTaskid(serverBotModel.getTaskID());
		model.setProjectID(woDetails.getProjectid());
		model.setVersionNO(woDetails.getWfVersion());
		model.setSubActivityFlowChartDefID(serverBotModel.getSubActivityFlowChartDefID());
		model.setStepID(serverBotModel.getStepID());

		// Added line of code to Optimize checkParallelWorkOrderDetails API
		Map<String, Object> data = new HashMap<>();
		ResponseEntity<Response<Map<String, Object>>> response = woExecutionService
				.checkParallelWorkOrderDetails(model);
		int statusCode = Integer.parseInt(response.getStatusCode().toString());
		if (StringUtils.isNoneEmpty(response.getBody().getFormErrors().get(0))) {
			if (statusCode == 200) {
				data.put(AppConstants.MSG, response.getBody().getFormErrors().get(0));
				data.put(AppConstants.EXECUTE_FLAG, false);
			} else if (statusCode == 500) {
				throw new ApplicationException(500, String.format("Exception in checkParallelWorkOrderDetails: %s",
						response.getBody().getFormErrors().get(0)));
			}
		} else {
			data = response.getBody().getResponseData();
		}

		if (data.get(AppConstants.EXECUTE_FLAG) != null && !(Boolean) (data.get(AppConstants.EXECUTE_FLAG))) {

			if (data.get(AppConstants.MSG) == null) {
				throw new ApplicationException(500, "Unable to start task!");
			} else {
				throw new ApplicationException(500, String.valueOf(data.get(AppConstants.MSG)));
			}

		}

	}

	public void validateSaveStepRuleInAutoSenseTmpTable(
			TmpWorkflowStepAutoSenseRuleModel tmpWorkFlowStepAutoSenseRule) {
		validateIntegerForNullOrZero(tmpWorkFlowStepAutoSenseRule.getRuleId(), AppConstants.RULE_ID);
		validateStringForBlank(tmpWorkFlowStepAutoSenseRule.getStepID(), AppConstants.STEP_ID);
		validateStringForBlank(tmpWorkFlowStepAutoSenseRule.getProjectIDSubactivityIDLoggedInSignum(),
				PROJECTID_SUBACTIVITYID_LOGGEDINSIGNUM);
		validateStringForBlank(tmpWorkFlowStepAutoSenseRule.getParseRuleJson(), AppConstants.RULE_JSON);
		validateStringForBlank(tmpWorkFlowStepAutoSenseRule.getTaskActionName(), AppConstants.TASK_ACTION_NAME);
		validateStringForBlank(tmpWorkFlowStepAutoSenseRule.getCreatedBy(), AppConstants.SIGNUM);
	}

	public void validateDeleteStepRuleInAutoSenseTmpTable(
			List<TmpWorkflowStepAutoSenseRuleModel> tmpWorkFlowStepAutoSenseRule) {
		for (int i = 0; i < tmpWorkFlowStepAutoSenseRule.size(); i++) {
			validateStringForBlank(tmpWorkFlowStepAutoSenseRule.get(i).getStepID(), AppConstants.STEP_ID);
			validateIntegerForNullOrZero(tmpWorkFlowStepAutoSenseRule.get(i).getTaskId(), AppConstants.TASK_ID);
			validateStringForBlank(tmpWorkFlowStepAutoSenseRule.get(i).getProjectIDSubactivityIDLoggedInSignum(),
					PROJECTID_SUBACTIVITYID_LOGGEDINSIGNUM);
			validateStringForBlank(tmpWorkFlowStepAutoSenseRule.get(i).getTaskActionName(),
					AppConstants.TASK_ACTION_NAME);
		}
	}

	public void validateStepIDForRuleDescription(String stepID) {

		if (StringUtils.isBlank(stepID)) {
			throw new ApplicationException(500, "Invalid FlowChartStepID");
		}
	}

	public void validateAutoSenseRulesForStepFromTemp(TmpWorkflowStepAutoSenseRuleModel tmpWorkFlowStepAutoSenseRule) {
		validateStringForBlank(tmpWorkFlowStepAutoSenseRule.getStepID(), AppConstants.STEP_ID);
		validateIntegerForNullOrZero(tmpWorkFlowStepAutoSenseRule.getTaskId(), AppConstants.TASK_ID);
		validateStringForBlank(tmpWorkFlowStepAutoSenseRule.getProjectIDSubactivityIDLoggedInSignum(),
				PROJECTID_SUBACTIVITYID_LOGGEDINSIGNUM);

	}

	public void validateIsWorkFlowAutoSenseEnabled(int flowchartDefId) {
		if (Boolean.FALSE.equals(autoSenseDao.isWorkFlowAutoSenseEnabled(flowchartDefId))) {
			;
			throw new ApplicationException(500, "Work Flow is not Auto-Sense enabled");
		}
	}

	public void validateTaskIDForStartTask(ServerBotModel serverBotModel, WorkOrderModel woDetails) {

		if (serverBotModel.getSubActivityFlowChartDefID() == null
				|| serverBotModel.getSubActivityFlowChartDefID() == 0) {

			serverBotModel.setSubActivityFlowChartDefID(woDetails.getFlowchartdefid());
		}

		int taskID = getTaskIDFromStepIDWfVersionAndDefID(serverBotModel, woDetails.getWfVersion());
		if (taskID != serverBotModel.getTaskID()) {
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append(TASK_ID).append(StringUtils.SPACE).append(serverBotModel.getTaskID())
					.append(" is invalid for Step ID ").append(serverBotModel.getStepID()).append(" and woID ")
					.append(serverBotModel.getwOID());
// change error code
			throw new ApplicationException(500, errorMessage.toString());
		}

	}

	// v1/validateTaskIDForStartTask
	public void validateTaskIDForStartTaskV1(ServerBotModel serverBotModel, WorkOrderModel woDetails) {

		if (serverBotModel.getSubActivityFlowChartDefID() == null
				|| serverBotModel.getSubActivityFlowChartDefID() == 0) {

			serverBotModel.setSubActivityFlowChartDefID(woDetails.getFlowchartdefid());
		}

		int taskID = getTaskIDFromStepIDWfVersionAndDefID(serverBotModel, woDetails.getWfVersion());
		if (taskID != serverBotModel.getTaskID()) {

			throw new ApplicationException(500,
					TASK_ID + StringUtils.SPACE + serverBotModel.getTaskID() + " is invalid for Step ID "
							+ serverBotModel.getStepID() + " and woID " + serverBotModel.getwOID());
		}

	}

	private int getTaskIDFromStepIDWfVersionAndDefID(ServerBotModel serverBotModel, int wfVersion) {
		Map<String, Object> stepData = flowChartDao.getStepExistingData(serverBotModel.getSubActivityFlowChartDefID(),
				serverBotModel.getStepID(), wfVersion);

		if (stepData == null) {

			throw new ApplicationException(500,
					serverBotModel.getStepID() + " does not exists for woID " + serverBotModel.getwOID());
		}

		Object taskIDObject = stepData.get(TASK_ID);
		if (taskIDObject == null) {

			throw new ApplicationException(500, serverBotModel.getStepID() + " does not have valid task ID");
		}
		return (int) taskIDObject;
	}

	public void validateIFPreviousStepCompleted(ServerBotModel serverBotModel, WorkOrderModel woDetails) {
           
		ResponseEntity<Response<PreviousStepDetailsModel>> stepModel = woExecutionService.checkIFPreviousStepCompleted(
				serverBotModel.getwOID(), serverBotModel.getTaskID(), woDetails.getFlowchartdefid(),
				serverBotModel.getStepID());
		if (!stepModel.getBody().getResponseData().isAllowed()) {

			throw new ApplicationException(500, "Please complete previous task/step or mark decision yes/no.");
		}

	}

	public void validateDeleteStepRuleInAutoSenseTmpTable(
			TmpWorkflowStepAutoSenseRuleModel tmpWorkflowStepAutoSenseRuleModel) {
		validateStringForBlank(tmpWorkflowStepAutoSenseRuleModel.getStepID(), AppConstants.STEP_ID);
		validateIntegerForNullOrZero(tmpWorkflowStepAutoSenseRuleModel.getTaskId(), AppConstants.TASK_ID);
		validateStringForBlank(tmpWorkflowStepAutoSenseRuleModel.getProjectIDSubactivityIDLoggedInSignum(),
				PROJECTID_SUBACTIVITYID_LOGGEDINSIGNUM);
		validateStringForBlank(tmpWorkflowStepAutoSenseRuleModel.getTaskActionName(), AppConstants.TASK_ACTION_NAME);
	}

	public void validateWorkOrderAssignedSignum(String signumID, int wOID) {

		WorkOrderModel workOrderModel = getWorkOrderModel(wOID);
		if (!StringUtils.equalsIgnoreCase(workOrderModel.getSignumID(), signumID)) {

			throw new ApplicationException(200, "Work Order is not assigned to signum " + signumID);
		}

	}

	public void validateFailureReason(String reason, String category) {
		if (!workOrderPlanService.validateFailureReason(reason, category)) {
			throw new ApplicationException(200, reason + " is not a valid reason");
		}

	}

	public void validateFlowChartDefID(int woId, int flowChartDefID) {

		WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(woId);
		if (woDetails.getFlowchartdefid() != flowChartDefID) {
			throw new ApplicationException(200,
					"flowChartDefID " + flowChartDefID + " is not associated to workOrder:" + woId);
		}
	}

	public void validateIfStepCompletedOrSkipped(int wOID, String stepid, String status, int taskId,
			int flowChartDefId) {
		List<String> bookingStatuses = workOrderPlanDao.getStepWOIDBookings(wOID, stepid);
		String excutionType = workOrderPlanDao.getExecutionType(stepid, taskId, flowChartDefId);
		boolean isSkippedPreviously = false;
		for (String bookingStatus : bookingStatuses) {
			if (StringUtils.equalsIgnoreCase(AppConstants.STARTED, bookingStatus)) {
				return;
			} else if (StringUtils.equalsIgnoreCase(AppConstants.COMPLETED, bookingStatus)) {
				isSkippedPreviously = true;
			}
		}

		if (StringUtils.equalsIgnoreCase(AppConstants.ONHOLD, status)) {
			throw new ApplicationException(200, "Given step is not started, couldn't be put ONHOLD.");
		} else if (bookingStatuses.size() > 0
				&& StringUtils.equalsIgnoreCase(AppConstants.SKIPPED, bookingStatuses.get(0))
				&& StringUtils.equalsIgnoreCase(AppConstants.SKIPPED, status)
				&& StringUtils.equalsIgnoreCase(AppConstants.MANUAL, excutionType)) {
			throw new ApplicationException(200, "Given Manual step is Already in  Skipped state , cant be skipped.");
		}

		if (StringUtils.equalsIgnoreCase(AppConstants.SKIPPED, status) && isSkippedPreviously
				&& !StringUtils.equalsIgnoreCase(AppConstants.ONHOLD, bookingStatuses.get(0))
				&& StringUtils.equalsIgnoreCase(AppConstants.MANUAL, excutionType)) {
			throw new ApplicationException(200, "Given Manual step is in Completed state , cant be skipped.");
		}

	}

	public void validateFailureReasonFromMasterData(String reason, String executionType) {
		String category = StringUtils.EMPTY;
		if (StringUtils.equalsIgnoreCase(AppConstants.MANUAL, executionType)) {
			category = AppConstants.MANUAL_STEP_CATEGORY;
		} else if (StringUtils.equalsIgnoreCase(AppConstants.AUTOMATIC, executionType)) {
			category = AppConstants.AUTOMATIC_STEP_CATEGORY;
		}
		validateFailureReason(reason, category);

	}

	public void validateStepIDforStepType(String flowChartStepId, int flowChartDefId) {

		boolean checkifStepTypeExist = workOrderPlanDao.validateStepIDforStepType(flowChartStepId, flowChartDefId);
		if (!checkifStepTypeExist) {
			throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALUE, STEP_ID));
		}

	}

	public void validateDecisionValue(StepDetailsModel stepDetailsModel) {
		if (StringUtils.equals(stepDetailsModel.getDecisionValue(), null)
				|| StringUtils.isEmpty(stepDetailsModel.getDecisionValue().trim())) {
			throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALUE, DECISION_VALUE));
		}

	}

	public void validateTaskIdONStepIdAndFlowChartdefId(String flowChartStepId, int flowChartDefID, int taskID) {
		int taskIdONStepIdAndFlowChartdefId = getTaskIdONStepIdAndFlowChartdefId(flowChartStepId, flowChartDefID);
		if (taskID != taskIdONStepIdAndFlowChartdefId) {
			throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALUE, TASK_ID));
		}

	}

	private int getTaskIdONStepIdAndFlowChartdefId(String flowChartStepId, int flowChartDefID) {
		return workOrderPlanService.getTaskIdONStepIdAndFlowChartdefId(flowChartStepId, flowChartDefID);

	}

	public void validateDecision(String decisionValue, List<String> parseJsonToGetDecisionValue) {
		boolean checkList = false;
		for (String decisionList : parseJsonToGetDecisionValue) {
			if (StringUtils.equalsIgnoreCase(decisionValue, decisionList)) {
				checkList = true;
				break;
			}
		}
		if (!checkList) {
			throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALUE, DECISION_VALUE));
		}
	}

	public void validateDecisionValueStepOrder(String flowChartStepId, int flowChartDefID, int woId,
			String executionType) {
		String previousStepStepId = workOrderPlanService.getPreviousStepStepId(flowChartStepId, flowChartDefID);
		if (!StringUtils.isEmpty(previousStepStepId)) {
			checkPreviousStepComplitionStatus(flowChartDefID, woId, previousStepStepId, executionType);
		}
	}

	private void checkPreviousStepComplitionStatus(int flowChartDefID, int woId, String previousStepStepId,
			String executionType) {
		boolean checkPreviousStepStarted = workOrderPlanService.checkPreviousStepCompleted(previousStepStepId,
				flowChartDefID, woId, executionType);
		if (!checkPreviousStepStarted) {
			throw new ApplicationException(200, ERROR_MSG_PREVIOUS_STEP);
		}
	}

	public void validateDecisionValueForNull(String decisionValue) {
		if (StringUtils.equals(decisionValue, null)) {
			throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALUE, DECISION_VALUE));
		}

	}

	public void validateCurrentStepForStart(ServerBotModel serverBotModel, WorkOrderModel woDetails)
			throws ParseException {
		BookingDetailsModel bookingDetailsModel = workOrderPlanService.getBookingDetailsOnWoidTaskIdDefIdStepId(
				serverBotModel.getwOID(), serverBotModel.getTaskID(), woDetails.getFlowchartdefid(),
				serverBotModel.getStepID());
		if (bookingDetailsModel == null) {
			String decisionValue = parseJsonToGetDecisionValue(woDetails.getFlowchartdefid(),
					serverBotModel.getStepID());
			if (StringUtils.isNotEmpty(decisionValue)) {
				boolean flag = false;
				String executionType = workOrderPlanDao.getExecutionType(serverBotModel.getStepID(),
						serverBotModel.getTaskID(), woDetails.getFlowchartdefid());

				ParallelWorkOrderDetailsModel model = new ParallelWorkOrderDetailsModel();
				model.setSignumID(woDetails.getSignumID());
				model.setIsApproved("");
				model.setExecutionType(executionType);
				model.setWoid(serverBotModel.getwOID());
				model.setTaskid(serverBotModel.getTaskID());
				model.setProjectID(woDetails.getProjectid());
				model.setVersionNO(woDetails.getWfVersion());
				model.setSubActivityFlowChartDefID(woDetails.getFlowchartdefid());
				model.setStepID(serverBotModel.getStepID());

				// Adding line of code to Optimize checkParallelWorkOrderDetails API
				Map<String, Object> checkParallelWorkOrderDetails = new HashMap<>();
				ResponseEntity<Response<Map<String, Object>>> response = woExecutionService
						.checkParallelWorkOrderDetails(model);
				int statusCode = Integer.parseInt(response.getStatusCode().toString());
				if (StringUtils.isNoneEmpty(response.getBody().getFormErrors().get(0))) {
					if (statusCode == 200) {
						checkParallelWorkOrderDetails.put(AppConstants.MSG, response.getBody().getFormErrors().get(0));
						checkParallelWorkOrderDetails.put(AppConstants.EXECUTE_FLAG, false);
					} else if (statusCode == 500) {
						throw new ApplicationException(500,
								String.format("Exception in checkParallelWorkOrderDetails %s",
										response.getBody().getFormErrors().get(0)));
					}
				} else {
					checkParallelWorkOrderDetails = response.getBody().getResponseData();
				}

				if (checkParallelWorkOrderDetails.containsKey(AppConstants.EXECUTE_FLAG)
						&& checkParallelWorkOrderDetails.get(AppConstants.EXECUTE_FLAG).equals(true)) {
					String previousStepStepId = workOrderPlanService.getPreviousStepStepId(serverBotModel.getStepID(),
							woDetails.getFlowchartdefid());
					boolean checkStepWithDecisionValue = workOrderPlanService.checkStepWithDecisionValue(
							previousStepStepId, woDetails.getFlowchartdefid(), woDetails.getwOID(), decisionValue);
					if (checkStepWithDecisionValue) {
						flag = true;
					}
				} else {
					if (StringUtils.equalsIgnoreCase(executionType, AppConstants.AUTOMATIC)) {
						throw new ApplicationException(200,
								"Automatic Task already running in this WOID. Please complete the previous step.");
					} else if (StringUtils.equalsIgnoreCase(executionType, AppConstants.MANUAL)) {
						throw new ApplicationException(200,
								"Manual Task already running in this WOID. Please complete the previous step.");
					}
				}
				if (!flag) {
					throw new ApplicationException(200, AppConstants.ERROR_STEP_MSG);
				}
			}
		}

	}

	private String parseJsonToGetDecisionValue(int flowChartDefID, String flowChartStepId) throws ParseException {
		String jsonValue = woExecutionService.getJsonValue(flowChartDefID);
		JSONObject jObject = new JSONObject(jsonValue);
		JSONArray arr = jObject.getJSONArray("cells");
		String decisionValue = StringUtils.EMPTY;
		for (int i = 0; i < arr.length(); i++) {
			if (arr.getJSONObject(i).has("labels")) {

				JSONArray array1 = arr.getJSONObject(i).getJSONArray("labels");
				if (StringUtils.equalsIgnoreCase(arr.getJSONObject(i).getJSONObject("target").getString("id"),
						flowChartStepId)) {
					for (int j = 0; j < array1.length(); j++) {
						decisionValue = array1.getJSONObject(j).getJSONObject("attrs").getJSONObject("text")
								.getString("text");
					}
				}

			}

		}
		return decisionValue;
	}

	public void validateDefaultDateFormat(String startDate, String endDate, String defaultDateFormat) {

		boolean startdateFlag = validateDate(startDate, defaultDateFormat);
		boolean endDateFlag = validateDate(endDate, defaultDateFormat);
		if (!startdateFlag || !endDateFlag) {
			throw new ApplicationException(200, AppConstants.NOT_VALID_DATE_FORMAT);
		}
	}

	// for validating date in yyyy-MM-dd format
	private boolean validateDate(String date, String defaultDateFormat) {
		if (date == null) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(defaultDateFormat);
		sdf.setLenient(false);
		try {
			Date date1 = sdf.parse(date);
			LOG.info("CHECK VALIDATE DATE {}", date1);

		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public void validateMessageField(DynamicMessageModel dynamicMessageModel) {
		validateStringForBlank(dynamicMessageModel.getMessage(), "Message");
		validateStringForBlank(dynamicMessageModel.getStartDate(), "StartDate");
		validateStringForBlank(dynamicMessageModel.getEndDate(), "EndDate");
		validateStringForBlank(dynamicMessageModel.getColor(), "Color");

	}

	public boolean validateProficiencyID(int proficiencyID) {
		boolean isValid = wOExecutionDAO.checkIFProficiencyIDExists(proficiencyID);

		if (isValid) {
			return true;
		} else {
			throw new ApplicationException(200, "Please provide valid proficiencyID");
		}
	}

	public void validateWoAssignForSignum(String signumId, String signumID2, int workOrderId) {
		if (!StringUtils.equalsIgnoreCase(signumId, signumID2)) {
			throw new ApplicationException(500, "Work order Id " + workOrderId + " not assigned to " + signumId);
		}

	}

	public void validateDefID(int flowchartdefid, int flowChartDefID2) {
		if (flowchartdefid != flowChartDefID2) {
			throw new ApplicationException(500, "Invalid FlowCahrtDefID");
		}
	}

	public void validateGenerateCustomAPIToken(SessionDataStoreModel sessionDataModel, String apiName) {
		apiName = (!apiName.equals("")) ? apiName : "/accessManagement/generateCustomAPIToken";

		List<String> json = accessManagementDAO.getValidateJsonForApi(apiName);
		if (json.isEmpty()) {

			throw new ApplicationException(500, "Invalid source Name or validateJson found!");
		}

		JSONObject obj = new JSONObject(json.get(0));
		JSONArray array = obj.getJSONArray(VALIDATIONS);

		if (array == null || array.length() == 0) {
			throw new ApplicationException(500, "Given Source name has invalid validateJson!");
		}

		for (int i = 0; i < array.length(); i++) {
			switch (String.valueOf(array.get(i))) {
			case "validateEmployeeTypeForNullOrBlank":
				validateStringForBlank(sessionDataModel.getEmployeeType(), AppConstants.EMPLOYEE_TYPE);
				break;
			case "validateemailIdForNullOrBlank":
				validateStringForBlank(sessionDataModel.getEmailID(), AppConstants.EMAIL);
				break;
			case "validatesourceNameForNullOrBlank":
				validateStringForBlank(sessionDataModel.getSourceName(), SOURCENAME);
				break;
			case "validatesourceDomainForNullOrBlank":
				validateStringForBlank(sessionDataModel.getSourceDomain(), SOURCEDOMAIN);
				break;
			case "validateipAddressForNullOrBlank":
				validateStringForBlank(sessionDataModel.getIpAddress(), IPADDRESS);
				break;
			case "validatebrowserForNullOrBlank":
				validateStringForBlank(sessionDataModel.getBrowser(), BROWSER);
				break;
			case "validatestatusForNullOrBlank":
				validateStringForBlank(sessionDataModel.getStatus(), AppConstants.STATUS);
				break;
			// Default case statement
			default:
				System.err.println("Consonant");
			}

		}

	}

	public void validateWorkorderForSrRequest(List<Integer> listofWoid) {
		String isWorkorderExistInSrRequest = workOrderPlanDao.checkWorkOrderForSrRequest(listofWoid);

		if (StringUtils.isNotEmpty(isWorkorderExistInSrRequest)) {
			throw new ApplicationException(200, WO_REOPENED_MSG_SRID);

		}
	}

	public void validateReinstateDeferedWorkorderForSrRequest(List<Integer> listofwoid) {
		String isWorkorderExistInSrRequest = workOrderPlanDao.checkWorkOrderForSrRequest(listofwoid);

		if (StringUtils.isNotEmpty(isWorkorderExistInSrRequest)) {
			throw new ApplicationException(200, WO_REINSTATE_MSG_SRID);

		}

	}

	public void validateProjectId(int projectId) {
		if (!projectDao.checkActiveProject(projectId)) {
			throw new ApplicationException(200, INACTIVE_PROJECTID);
		}
	}

	public void validateNetworkElementDetails(NetworkElementNewModel networkElement) {

		String apiName = StringUtils.EMPTY;
		apiName = (!apiName.equals("")) ? apiName : "/externalInterface/getNetworkElement";
		List<String> json = externalInterfaceDao.getValidateJsonForApi(apiName, networkElement.getSourceName());
		
		if (json.isEmpty()) {

			throw new ApplicationException(500, "Invalid source Name or validateJson found!");
		}

		JSONObject obj = new JSONObject(json.get(0));
		JSONArray array = obj.getJSONArray(VALIDATIONS);

		if (array == null || array.length() == 0) {
			throw new ApplicationException(500, "Given Source name has invalid validateJson!");
		}

		for (int i = 0; i < array.length(); i++) {
			switch (String.valueOf(array.get(i))) {

			case CUSTOMER:
				validateCustomer(networkElement.getCustomer());
				break;
			case COUNTRY:
				validateCountry(networkElement.getCountry());
				break;
			case PAGE_LENGTH:
				validatePageLength(networkElement.getPageLength());
				break;
			case PAGE_OFFSET:
				break;
			default: // default clause should be the last one

			}

		}
	}


	private void validatePageLength(int pageLength) {
		validateIntForZero(pageLength, PAGE_LENGTH2);
		Integer viewLenth=configurations.getIntegerProperty(ConfigurationFilesConstants.VIEW_NETWORK_ELEMENT_LENTH);
		 if (pageLength > viewLenth) {

			throw new ApplicationException(200, PAGE_LENGTH_CHECK+viewLenth);
		}

	}

	public void validateAppendNetworkElement(NetworkElementNewModel elements, String apiName) {
		apiName = (!apiName.equals("")) ? apiName : "/externalInterface/appendNetworkElement";

		
		List<String> json = externalInterfaceDao.getValidateJsonForApi(apiName, StringUtils.trim(elements.getSourceName()));
		if (json.isEmpty()) {
			throw new ApplicationException(500, "Invalid source Name or validateJson found!");
		}

		JSONObject obj = new JSONObject(json.get(0));
		JSONArray array = obj.getJSONArray(VALIDATIONS);

		if (array == null || array.length() == 0) {
			throw new ApplicationException(500, "Given Source name has invalid validateJson!");
		}

		for (int i = 0; i < array.length(); i++) {
			switch (String.valueOf(array.get(i))) {
			case "validatesourceNameForNullOrBlank":
				validateStringForBlank(elements.getSourceName(), SOURCENAME);
				break;
			case "validateProjectIDForNullOrBlankOrDB":
//				validateIntForZero(elements.getProjectID(), AppConstants.PROJECT_ID);
//				validateProjectId(elements.getProjectID());
				break;
			case "validateDomainForNullOrBlankOrDB":
				validateDomainForNullOrBlankOrDB(elements.getDomain(), DOMAIN);
				break;
			case "validateSubDomainForNullOrBlankOrDB":
				validateSubDomainForNullOrBlankOrDB(elements.getSubDomain(), SUB_DOMAIN);
				break;
			case "validateTechnologyForNullOrBlankOrDB":
				validateTechnologyForNullOrBlankOrDB(elements.getTechnology(), TECHNOLOGY);
				break;
			case "validateVendorForNullOrBlankOrDB":
				validateVendorForNullOrBlankOrDB(elements.getVendor(), VENDOR);
				break;
			case "validateElementTypeForNullOrBlankOrDB":
				validateElementTypeForNullOrBlankOrDB(elements.getElementType(), ELEMENT_TYPE);
				break;
				
			case "validateTypeForNullOrBlankOrDB":
				validateTypeForNullOrBlankOrDB(elements.getType(), TYPE);
				if (elements.getType().contains(",")) {
					throw new ApplicationException(200, COMMA_EXCEPTION );
				}
				break;
				
			case "validateNameForNullOrBlankOrDB":
				validateStringForBlank(elements.getName(), NAME);
				if (elements.getName().length() > 100) {
					throw new ApplicationException(200, NAME + LENGTH  );
				}
				if (elements.getName().contains(",")) {
					throw new ApplicationException(200, COMMA_EXCEPTION);
				}
				break;
				
			case "validateMarketForNullOrBlankOrDB":
				validateStringForBlank(elements.getMarket(), MARKET);
				if (elements.getMarket().length() > 100) {
					throw new ApplicationException(200, MARKET + LENGTH );
				}
				if (elements.getMarket().contains(",")) {
					throw new ApplicationException(200, COMMA_EXCEPTION);
				}
				break;
				
			case "validateUploadedByForNullOrBlankOrDB":
				validateUploadedByForNullOrBlankOrDB(elements.getUploadedBy(), UPLOADED_BY);
				
				break;
			// Default case statement
			default:
				System.err.println("Consonant");
			}

		}

	}

	public void validateDomainForNullOrBlankOrDB(String stringValue, String variableName) {

		if (StringUtils.isBlank(stringValue)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}

		String domain = networkElementDao.checkDomain(stringValue.trim());

		if (StringUtils.isBlank(domain)) {
			throw new ApplicationException(500, String.format("Invalid Domain", variableName));
		}

	}

	private void validateSubDomainForNullOrBlankOrDB(String stringValue, String variableName) {
		if (StringUtils.isBlank(stringValue)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}

		String subDomain = networkElementDao.checkSubDomain(stringValue.trim());

		if (StringUtils.isBlank(subDomain)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}
	}

	private void validateTechnologyForNullOrBlankOrDB(String stringValue, String variableName) {
		if (StringUtils.isBlank(stringValue)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}

		String technology = networkElementDao.checkTechnology(stringValue.trim());

		if (StringUtils.isBlank(technology)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}
	}

	private void validateVendorForNullOrBlankOrDB(String stringValue, String variableName) {
		if (StringUtils.isBlank(stringValue)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}

		String vendor = networkElementDao.checkVendor(stringValue.trim());

		if (StringUtils.isBlank(vendor)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}
	}

	private void validateElementTypeForNullOrBlankOrDB(String stringValue, String variableName) {
		if (StringUtils.isBlank(stringValue)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}

		String networkElementType = networkElementDao.checkNetworkElementType(stringValue.trim());

		if (StringUtils.isBlank(networkElementType)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}
	}

	private void validateTypeForNullOrBlankOrDB(String stringValue, String variableName) {
		if (StringUtils.isBlank(stringValue)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}

		if (stringValue.length() > 100) {
			throw new ApplicationException(500,
					"type should be maximum 100 characters!");
		}
		String networkSubElementType = networkElementDao.checkNetworkSubElementType(stringValue.trim());

		if (StringUtils.isBlank(networkSubElementType)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}
	}

	private void validateUploadedByForNullOrBlankOrDB(String stringValue, String variableName) {
		if (StringUtils.isBlank(stringValue)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}

		String uploadedBy = networkElementDao.checkUploadedBy(stringValue.trim());

		if (StringUtils.isBlank(uploadedBy)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}
	}
	private void validateCountry(String country) {
		validateStringForBlank(country,"Country");
		if(!externalInterfaceDao.isCountryExist(country)) {
			throw new ApplicationException(200,String.format(PLEASE_PROVIDE_VALUE,"Country"));
		}
	}

	private void validateCustomer(String customer) {
		validateStringForBlank(customer,"Customer");
		if(!externalInterfaceDao.isCustomerExist(customer)) {
			throw new ApplicationException(200,String.format(PLEASE_PROVIDE_VALUE,"Customer"));
		}
		
	}
	
	public String getCurrentEnvironment(String key) {
		return environmentPropertyService.getEnvironmentPropertyModelByKey(key).get(0).getValue();
	}

	public void validateSignumExistsEmpAndNotInResigned(String receiverID, String variableName) {
		if(!accessManagementDAO.isSignumExistsEmpAndNotInResigned(receiverID)) {
			throw new ApplicationException(500, String.format(PLEASE_PROVIDE_VALUE, variableName));
		}
		
	}

	public void validateLastModifiedByForPMDRNE(String lastModifiedBy, int projectID, int wOID) {
		boolean isValidLastModifiedBy=this.workOrderPlanDao.validateLastModifiedByForPMDRNE(lastModifiedBy,projectID,wOID);
		if(Boolean.FALSE.equals(isValidLastModifiedBy)) {
			throw new ApplicationException(200, LAST_MODIFIED_BY_SIGNUM_MUST_BE_PM_DR_OR_PREVIOUS_ASSIGNEE_OF_THE_WO_FOR_THE_GIVEN_PROJECT);
		}
		
	}

	public void validateLastModifiedForPMDRBookedResource(String lastModifiedBy, int projectID) {
		boolean isValidLastModifiedBy=this.workOrderPlanDao.validateLastModifiedForPMDRBookedResource(lastModifiedBy,projectID);
		if(Boolean.FALSE.equals(isValidLastModifiedBy)) {
			throw new ApplicationException(200, LAST_MODIFIED_BY_SIGNUM_MUST_BE_PM_DR_OR_BOOKED_RESOURCE_FOR_THE_GIVEN_PROJECT);
		}
		
	}

	public void validateNetworkElementCount(String nodeNames) {

		int maxLimit = configurations.getIntegerProperty(ConfigurationFilesConstants.MAX_NE_ALLOWED_FOR_WO);
		if (maxLimit > 0) {
			String[] neList = nodeNames.split("[,]", 0);

			if (neList.length > maxLimit) {
				throw new ApplicationException(500, String.format(NEID_COUNT_EXCEED_MSG,
						configurations.getIntegerProperty(ConfigurationFilesConstants.MAX_NE_ALLOWED_FOR_WO)));

			}
		}

	}
	
}
