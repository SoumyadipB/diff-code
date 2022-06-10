/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.AccessManagementDAO;
import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.ExternalInterfaceManagmentDAO;
import com.ericsson.isf.dao.FlowChartDAO;
import com.ericsson.isf.dao.NetworkElementDao;
import com.ericsson.isf.dao.ProjectScopeDao;
import com.ericsson.isf.dao.RpaDAO;
import com.ericsson.isf.dao.WOExecutionDAO;
import com.ericsson.isf.dao.WorkOrderPlanDao;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.AuditDataModel;
import com.ericsson.isf.model.BookingDetailsModel;
import com.ericsson.isf.model.BulkWorkOrderCreationModel;
import com.ericsson.isf.model.BulkXlsParsedModel;
import com.ericsson.isf.model.CreateWoResponse;
import com.ericsson.isf.model.CreateWorkOrderModel;
import com.ericsson.isf.model.CreateWorkOrderModel2;
import com.ericsson.isf.model.DOIDModel;
import com.ericsson.isf.model.DOWOModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DeleteWOListModel;
import com.ericsson.isf.model.EmployeeBasicDetails;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.ExecutionPlanDetail;
import com.ericsson.isf.model.ExecutionPlanFlow;
import com.ericsson.isf.model.ExecutionPlanModel;
import com.ericsson.isf.model.FinalRecordsForWOCreationModel;
import com.ericsson.isf.model.GenericMailModel;
import com.ericsson.isf.model.InProgressNextStepModal;
import com.ericsson.isf.model.InProgressTaskModel;
import com.ericsson.isf.model.LinkModel;
import com.ericsson.isf.model.MailModel;

import com.ericsson.isf.model.NetworkElementValidateMasterModel;
import com.ericsson.isf.model.NextStepModel;
import com.ericsson.isf.model.NextSteps;
import com.ericsson.isf.model.ProjectModel;
import com.ericsson.isf.model.ProjectNodeTypeModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.SaveClosureDetailsForWOModel;
import com.ericsson.isf.model.ScopeDetailsModel;
import com.ericsson.isf.model.ServerTimeModel;
import com.ericsson.isf.model.StepDetailsModel;
import com.ericsson.isf.model.TransferWorkOrderModel;
import com.ericsson.isf.model.WOInputFileModel;
import com.ericsson.isf.model.WOOutputFileModel;
import com.ericsson.isf.model.WOOutputFileResponseModel;
import com.ericsson.isf.model.WorkFlowDetailsModel;
import com.ericsson.isf.model.WorkOrderCompleteDetailsModel;
import com.ericsson.isf.model.WorkOrderInputFileModel;
import com.ericsson.isf.model.WorkOrderMailModel;
import com.ericsson.isf.model.WorkOrderModel;
import com.ericsson.isf.model.WorkOrderOutputFileModel;
import com.ericsson.isf.model.WorkOrderPlanModel;
import com.ericsson.isf.model.WorkOrderPlanModel2;
import com.ericsson.isf.model.WorkOrderPlanNodesModel;
import com.ericsson.isf.model.WorkOrderViewProjectModel;
import com.ericsson.isf.model.botstore.TblRpaDeployedBot;
import com.ericsson.isf.service.audit.AuditDataProcessorRequestModel;
import com.ericsson.isf.service.audit.AuditManagementService;
import com.ericsson.isf.service.audit.AuditManager;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AppUtil;
import com.ericsson.isf.util.ApplicationMessages;
import com.ericsson.isf.util.BeanUtils;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.ericsson.isf.util.DateTimeUtil;
import com.ericsson.isf.util.IsfCustomIdInsert;
import com.ericsson.isf.util.PlannedEndDateCal;
import com.ericsson.isf.util.SortExecutionPlanDetailbyDateTime;


/**
 *
 * @author eguphee
 */
@Service
public class WorkOrderPlanService {


	private static final String WO_UPDATE_EXTERNAL_WO = "WO_UPDATE_External_WO";


	private static final String ASSIGNEE = "Assignee";


	private static final String WO_NAME = "WoName";


	private static final String PLEASE_PROVIDE_AT_LEAST_ONE_PARAMETER_TO_UPDATE = "Please provide at-least one parameter to update !";


	private static final String WO_STATUS_SHOULD_BE_REOPENED_OR_ASSIGNED_OR_UNASSIGNED_TO_DELETE_WO = "WO status should be Reopened or Deffered or Assigned or Unassigned to delete WO";

	
	private static final String CANNOT_TRANSFER_WORK_ORDER_AS_THE_STATUS_OF_ONE_OF_THE_STEPS_IS_STARTED = "Cannot Transfer Work Order as the Status of one of the steps is STARTED !!!";

	private static final String WO_STATUS_SHOULD_BE_EITHER_REOPENED_ON_HOLD_INPROGRESS_OR_ASSIGNED_OR_UNASSIGNED_TO_TRANSFER_WO = "WO status should be either Reopened, On hold, Inprogress, Assigned or Unassigned to transfer WO";

	private static final String WORK_ORDER_DOESN_T_EXISTS = "Work Order doesn't Exists...!!! ";

	

	private static final String WO_TRANSFER_SUCCESS = "WO Transfer Success!";

	private static final String ASSIGNED_TO_SIGNUM = "assignedToSignum";

	private static final String WORK_ORDER_DETAILS_UPDATED_SUCCESSFULLY = "WORK ORDER details updated successfully !!!";

	private static final String STOP_RULE = "stopRule";

	private static final String START_RULE = "startRule";

	private static final String COMMA_DOLLAR = ",$";

	private static final String TASK_ID = "TaskID";

	private static final String SERVER_TIME = "serverTime";

	private static final String WO_ID = "woID";

	private static final String ACTIVITY = "Activity";

	private static final String SUBACTIVITY = "SubActivity";

	private static final String INVALID_INPUT_PROJECT_ID_CANNOT_BE_0 = "Invalid input... ProjectID cannot be 0 !!!";

	private static final String RECORDS_TOTAL = "recordsTotal";

	private static final String RECORDS_FILTERED = "recordsFiltered";

	private static final String API_IS_GETTING_BELOW_ERROR_IN_CREATE_BULK_WORK_ORDER_API = " \n API is getting below error in createBulkWorkOrder API : \n ";

	private static final String ERROR_WHILE_CREATION_WO_IN_BULK_WO_CREATION_PROCESS = "Error while creating wo in bulk wo creation process";

	private static final String ERROR_CATEGORY = "Bulk wo creation error in API";
	
	private static final String PLEASE_PROVIDE_VALID = "Please provide valid %s!";
	
	private static final String WOID = "WOID";
	
	private static final String DATE = "Date";
	
	private static final String FORMAT = "Format";
	
	private static final String EXTERNAL_SOURCE = "External Source";
	
	private static final String WOSTATUS_NOT_IN_REJECTED_CLOSED_PLANNED_FOR_WO_NAME = "WOStatus cannot be rejected, closed or planned for updation of given WOName!";
	
	private static final String DATE_NOT_LESS_THAN_TODAY_DATE = "Given Date/Time should not be less than current date/Time!";
	
	private static final String WOSTATUS_IN_ASSIGNED_REOPENED_FOR_START_DATE = "WOStatus can be assigned or reopened only for updation of given StartDate!";
	
	private static final String EXTERNAL_SOURCE_CANNOT_BE_ERISITE = "External Source cannot be Erisite!";
	
	private static final String LAST_MODIFIED_BY = "LastModifiedBy";
	
	private static final String INVALID_SIGNUM_NOT_PM_DR ="Invalid Signum in lastModifiedBy,it is not active DR/PM of the project!";
	
	private static final String ERISITE_CREATED_WO_CANNOT_BE_UPDATED = "For Erisite created WO , WO Name cannot be updated!";
	
	private static final String INPROGRESS = "InProgress";
	

	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(WorkOrderPlanService.class);
	
	@Autowired
	private WOExecutionService woExecutionService;

	@Autowired
	private WorkOrderPlanDao workOrderPlanDao;

	@Autowired
	private ExternalInterfaceManagmentDAO erisiteManagmentDAO;

	@Autowired
	private ProjectScopeDao projectScopeDao;
	
	@Autowired
	private WOExecutionDAO wOExecutionDAO;

	@Autowired
	private ActivityMasterDAO activityMasterDAO;

	@Autowired
	private OutlookAndEmailService emailService;

	@Autowired
	private FlowChartDAO flowChartDAO;

	@Autowired
	private AuditManager auditManager;

	@Autowired
	private BotStoreService botStoreService;
	
	@Autowired
	private ProjectService projectService;

	@Autowired
	private RpaService rpaService;

	@Autowired
	private IsfCustomIdInsert isfCustomIdInsert;

	@Autowired
	private RpaDAO rpaDAO;

	@Autowired
	private AccessManagementDAO accessManagementDAO;

	@Autowired
	private ApplicationConfigurations configurations;

	@Autowired
	private ActivityMasterService activityMasterService;
	
	@Autowired
	private ValidationUtilityService validationUtilityService;
	

	@Autowired
	private AutoSenseService autoSenseService;
	
	@Autowired
	private AuditManagementService auditManagementService;

	@Autowired /* Bind to bean/pojo */
	private EnvironmentPropertyService environmentPropertyService;
	
	@Autowired
	private NetworkElementDao networkElementDao;
	
	@Autowired
	private AppService appService;
	
	
	private static final String SP = "DeliveryExecution/WorkorderAndTask";

	@Transactional("transactionManager")
	public void deleteWorkOrderPlan(WorkOrderPlanModel wOPlanObject) {
		List<WorkOrderModel> workOrder = new ArrayList<>();
		workOrder = workOrderPlanDao.checkNotStartedStatusOfWorkOrderPlan(wOPlanObject);
		if (!workOrder.isEmpty()) {
			workOrderPlanDao.deleteWorkOrderPlanDao(wOPlanObject);
		} else {
			throw new ApplicationException(500,
					"Work Order status is in progress, so you cannot delete this work order plan");
		}
	}

	public List<WorkOrderPlanModel> getWorkOrderPlanDetails(int woPlanID, int projectId, String signumID) {
		return workOrderPlanDao.getWorkOrderPlanDetails(woPlanID, projectId, signumID);
	}

	@Transactional("transactionManager")
	public void updateWorkOrderPlan(WorkOrderPlanModel wOPlanObject) {
		List<WorkOrderModel> workOrder = new ArrayList<>();
		workOrder = workOrderPlanDao.checkNotStartedStatusOfWorkOrderPlan(wOPlanObject);
		if (!workOrder.isEmpty()) {
			workOrderPlanDao.updateWorkOrderPlanDao(wOPlanObject);
		} else {
			throw new ApplicationException(500,
					"Work Order status is in progress, so you cannot update this work order plan");
		}
	}

	public int getEstdHrs(int projectID, int subActId) {
		int estdHrs;
		estdHrs = workOrderPlanDao.getEstdHrs(projectID, subActId);
		return estdHrs;
	}

	/**
	 * This method is responsible for creating wo plan, doid, wo and exececution
	 * plan flow
	 * 
	 * @param wOPlanObject
	 * @param doValidation
	 */
	@Transactional("transactionManager")
	public CreateWoResponse createWorkOrdekOrExecutionPlan(CreateWorkOrderModel wOPlanObject, boolean doValidation) {

		CreateWoResponse response = new CreateWoResponse();
		try {

			BeanUtils.trimAllStrings(wOPlanObject);
			String errorMsg = doBasicValidationAndPrepareCreateWorkOrderPlanModel(wOPlanObject);
			if (StringUtils.isNotBlank(errorMsg)) {
				response.setMsg(errorMsg);
				return response;
			}

			if (doValidation) {
				validateWorkOrderPlanObjectModel(wOPlanObject);
				}

			if (!StringUtils.equalsIgnoreCase(AppConstants.WEB, wOPlanObject.getExternalSourceName())) {

				wOPlanObject = getWorkOrderModelForBulkCreation(wOPlanObject);
			}

			createWorkOrderAndExecutionPlan(wOPlanObject, response);
			response.setMsg(AppConstants.WORK_ORDER_PLAN_HAS_BEEN_CREATED_SUCCESSFULLY);
		} catch (ApplicationException ae) {
			LOG.error(ae.getErrorMessage());
			response.setMsg(ae.getErrorMessage());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			response.setMsg("Global Exception Occured: "+e.getMessage());
			return response;
		}
		LOG.info("TBL_WORK_ORDER_PLAN: SUCCESS");
		return response;
	}

	
	/**
	 * This method is responsible for creating wo plan, doid, wo and exececution
	 * plan flow
	 * 
	 * @param wOPlanObject
	 * @param response
	 */
	@Transactional("transactionManager")
	public void createWorkOrderAndExecutionPlan(CreateWorkOrderModel wOPlanObject, CreateWoResponse response) {
		
		if (StringUtils.equalsIgnoreCase(AppConstants.WEB, wOPlanObject.getExternalSourceName())
				&& StringUtils.isNotEmpty(wOPlanObject.getTableName())) {

			String networkElement = networkElementDao.getDistinctNetworkElementName(wOPlanObject.getTableName());
			List<WorkOrderPlanNodesModel> listOfNode = new ArrayList<>();
			WorkOrderPlanNodesModel workOrderPlanModel = new WorkOrderPlanNodesModel();
			workOrderPlanModel.setNodeNames(networkElement);
			listOfNode.add(workOrderPlanModel);
			wOPlanObject.setListOfNode(listOfNode);

		}
		// get ExecutionPlanDetails for work order creation
		List<ExecutionPlanDetail> execPlanDetails = getExecutionPlanDetailsByExecutionPlanId(
				wOPlanObject.getExecutionPlanId());

		if (CollectionUtils.isEmpty(execPlanDetails)) {
			throw new ApplicationException(500, "No valid Execution Plan Detail exists!");
		}

		Collections.sort(execPlanDetails, new SortExecutionPlanDetailbyDateTime());
        
		// Prepare Temp Table for Node Validation and fetching NetworkElementID as per
		// CNEDB
		if (!StringUtils.equalsIgnoreCase(AppConstants.WEB, wOPlanObject.getExternalSourceName())
				&& CollectionUtils.isNotEmpty(wOPlanObject.getListOfNode())
				&& StringUtils.isNotBlank(wOPlanObject.getListOfNode().get(0).getNodeNames())
				&& StringUtils.isNotBlank(wOPlanObject.getListOfNode().get(0).getNodeType())) {
			prepareTableandNetworkElementID(wOPlanObject);

		}
		// prepare and CreateWorkOrderModel For WO Plan
		prepareCreateWorkOrderModelForWOPlan(wOPlanObject, execPlanDetails);

		// create workorder plan
		addWorkOrderPlan(wOPlanObject, execPlanDetails, response);

		// create doid -> reoccurrence, volume, nodewise
		createDOIDByNodeWiseAndDOVolume(wOPlanObject, execPlanDetails, response);
		
		if (StringUtils.isNotEmpty(wOPlanObject.getTableName())) {
			updateStatusInNEValidateMaster(wOPlanObject);
		}

		// update woplan start date, start time and end date
		updateWoPlanStartEndDateAndTime(response.getWoPlanId());

		// send Mail For Work Orders
		
		if (configurations.getBooleanProperty(ConfigurationFilesConstants.WORKORDER_MAIL_ENABLED, false)
				&&(StringUtils.equalsIgnoreCase(AppConstants.WEB, wOPlanObject.getExternalSourceName())
				|| !StringUtils.equalsIgnoreCase(AppConstants.GNET, wOPlanObject.getExternalSourceName()))) {

			sendMailForWorkOrders(wOPlanObject, response);
		}

		// do Auditing For Work Orders
		doAuditingForWorkOrders(wOPlanObject, response);

	}

	private void updateStatusInNEValidateMaster(CreateWorkOrderModel wOPlanObject) {
		
		int neValidateID = networkElementDao.getNeValidateIDByTableName(wOPlanObject.getTableName());
		
		networkElementDao.updateNeValidateMasterTable(neValidateID, wOPlanObject.getCreatedBy());
		
	}

	private void prepareTableandNetworkElementID(CreateWorkOrderModel wOPlanObject) {

		NetworkElementValidateMasterModel networkElementValidateMasterModel = new NetworkElementValidateMasterModel();
		networkElementValidateMasterModel.setSignum(wOPlanObject.getCreatedBy());
		networkElementValidateMasterModel.setStatus(INPROGRESS);
		networkElementDao.insertStatusInValidateMasterTable(networkElementValidateMasterModel);

		int neValidateID = isfCustomIdInsert.generateCustomId(networkElementValidateMasterModel.getNeValidateID());
		// Call proc to Validate Network Element Data and prepare temp table

		int ccGroupID = appService.getCountryCustomerGroupIDByProjectID(wOPlanObject.getProjectID());

		String tablename= networkElementDao.validateCommaSeparatedNetworkElementData(wOPlanObject.getCreatedBy(), null, null,
				wOPlanObject.getProjectID(), neValidateID, wOPlanObject.getListOfNode().get(0).getNodeNames(),
				wOPlanObject.getExternalSourceName(), wOPlanObject.getExecutionPlanName(),
				wOPlanObject.getListOfNode().get(0).getNodeType(), ccGroupID);
		
		wOPlanObject.setTableName(tablename);
		

	}

	/**
	 * adds DOID
	 * 
	 * @param wOPlanObject
	 * @param execPlanDetails
	 * @param response
	 */
	@Transactional("transactionManager")
	private void addDOID(CreateWorkOrderModel wOPlanObject, List<ExecutionPlanDetail> execPlanDetails,
			CreateWoResponse response) {

		// check reoccurance or not
		if (isNotReOccurrence(wOPlanObject)) {

			// then set planned end date for work order
			Date plannedEndDate = PlannedEndDateCal.calculateEndDateV2(wOPlanObject.getPlannedStartDate(),
					wOPlanObject.getSlaHrs());
			wOPlanObject.setEndDate(
					PlannedEndDateCal.convertDateToString(plannedEndDate, AppConstants.DEFAULT_DATE_FORMAT));
		}

		if (StringUtils.isNotBlank(wOPlanObject.getPeriodicityDaily())) {

			addDOIDByPeriodicityDaily(wOPlanObject, execPlanDetails, response);

		} else if (StringUtils.isNotBlank(wOPlanObject.getPeriodicityWeekly())) {

			addDOIDByPeriodicityWeekly(wOPlanObject, execPlanDetails, response);

		} else if (CollectionUtils.isNotEmpty(wOPlanObject.getPeriodicityHourly())) {

			addDOIDByPeriodicityHourly(wOPlanObject, execPlanDetails, response);
		} else {

			// create doid
			createDOID(wOPlanObject, execPlanDetails, response);
		}

	}

	/**
	 * This method is responsible for auditing of work orders
	 * 
	 * @param wOPlanObject
	 * @param response
	 */
	public void doAuditingForWorkOrders(CreateWorkOrderModel wOPlanObject, CreateWoResponse response) {

		AuditDataProcessorRequestModel auditDataRequest = new AuditDataProcessorRequestModel();
		auditDataRequest.setRequestBody(wOPlanObject);
		auditDataRequest.setResponse(response);
		auditDataRequest.setController("woManagement");
		auditDataRequest.setApiEndpoint("createWorkOrderPlan");

		auditManager.audit(auditDataRequest);

	}

	/**
	 * This method will send mail after creating work orders
	 * 
	 * @param wOPlanObject
	 * 
	 * @param sendMail
	 * @param response
	 * @param createWorkOrderModel
	 */
	@Transactional("transactionManager")
	public void sendMailForWorkOrders(CreateWorkOrderModel wOPlanObject, CreateWoResponse response) {

		try {
			Map<String, Object> placeHolder = enrichMailforWorkOrder(wOPlanObject, response);
			placeHolder.put(AppConstants.PAGE_LINK_STRING, SP);
			emailService.sendMail(AppConstants.NOTIFICATION_CREATE_WORKORDER, placeHolder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method creates DOID hourly
	 * 
	 * @param wOPlanObject
	 * @param execPlanDetails
	 * @param response
	 * @return boolean
	 */
	@Transactional("transactionManager")
	private void addDOIDByPeriodicityHourly(CreateWorkOrderModel wOPlanObject,
			List<ExecutionPlanDetail> execPlanDetails, CreateWoResponse response) {

		Calendar end = Calendar.getInstance();
		end.setTime(PlannedEndDateCal.convertStringToDate(wOPlanObject.getEndDate(), AppConstants.DEFAULT_DATE_FORMAT));
		setTimeZeroToDate(end);

		Calendar start = Calendar.getInstance();
		start.setTime(wOPlanObject.getPlannedStartDate());
		CreateWorkOrderModel wOPlanObjectCopy = new CreateWorkOrderModel(wOPlanObject);

		Calendar startDateWithOutTime = Calendar.getInstance();
		startDateWithOutTime.setTime(start.getTime());
		setTimeZeroToDate(startDateWithOutTime);

		while (start.getTime().before(end.getTime()) || startDateWithOutTime.equals(end)) {

			for (Time t : wOPlanObjectCopy.getPeriodicityHourly()) {

				wOPlanObjectCopy.setStartTime(t);
				String stringStartDate = PlannedEndDateCal.convertDateToString(start.getTime(),
						AppConstants.DEFAULT_DATE_FORMAT);
				wOPlanObjectCopy.setPlannedStartDate(
						DateTimeUtil.convertDateAndTimeToDate(stringStartDate, wOPlanObjectCopy.getStartTime()));
				createDOID(wOPlanObjectCopy, execPlanDetails, response);

			}
			setTimeZeroToDate(start);
			start.add(Calendar.DATE, 1);
			startDateWithOutTime.setTime(start.getTime());
			setTimeZeroToDate(startDateWithOutTime);

		}
	}

	/**
	 * 
	 * @param date
	 */
	public void setTimeZeroToDate(Calendar date) {

		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * This method creates DOID weekly
	 * 
	 * @param wOPlanObject
	 * @param execPlanDetails
	 * @param response
	 * @return boolean
	 */
	@Transactional("transactionManager")
	private void addDOIDByPeriodicityWeekly(CreateWorkOrderModel wOPlanObject,
			List<ExecutionPlanDetail> execPlanDetails, CreateWoResponse response) {

		Calendar startDate = Calendar.getInstance();
		startDate.setTime(wOPlanObject.getPlannedStartDate());
		CreateWorkOrderModel wOPlanObjectCopy = new CreateWorkOrderModel(wOPlanObject);
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(
				PlannedEndDateCal.convertStringToDate(wOPlanObject.getEndDate(), AppConstants.DEFAULT_DATE_FORMAT));

		Calendar startDateWithOutTime = Calendar.getInstance();
		startDateWithOutTime.setTime(startDate.getTime());
		setTimeZeroToDate(startDateWithOutTime);

		while (startDate.getTime().before(endDate.getTime()) || startDateWithOutTime.equals(endDate)) {

			wOPlanObjectCopy.setPlannedStartDate(startDate.getTime());
			String dayOfWeekStr = PlannedEndDateCal.getDayOfWeekStr(startDate);
			if (dayOfWeekStr.contains(wOPlanObjectCopy.getPeriodicityWeekly())) {
				createDOID(wOPlanObjectCopy, execPlanDetails, response);
				startDate.add(Calendar.WEEK_OF_YEAR, 1);
			} else {

				startDate.add(Calendar.DAY_OF_YEAR, 1);
			}
			startDateWithOutTime.setTime(startDate.getTime());
			setTimeZeroToDate(startDateWithOutTime);
		}
	}

	/**
	 * This method creates DOID daily
	 * 
	 * @param wOPlanObject
	 * @param execPlanDetails
	 * @param response
	 * @return boolean
	 */
	@Transactional("transactionManager")
	private void addDOIDByPeriodicityDaily(CreateWorkOrderModel wOPlanObject, List<ExecutionPlanDetail> execPlanDetails,
			CreateWoResponse response) {

		Date plannedStartDate = wOPlanObject.getPlannedStartDate();
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(plannedStartDate);
		CreateWorkOrderModel wOPlanObjectCopy = new CreateWorkOrderModel(wOPlanObject);
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(
				PlannedEndDateCal.convertStringToDate(wOPlanObject.getEndDate(), AppConstants.DEFAULT_DATE_FORMAT));

		Calendar startDateWithOutTime = Calendar.getInstance();
		startDateWithOutTime.setTime(startDate.getTime());
		setTimeZeroToDate(startDateWithOutTime);

		while (startDate.getTime().before(endDate.getTime()) || startDateWithOutTime.equals(endDate)) {

			wOPlanObjectCopy.setPlannedStartDate(startDate.getTime());
			if (wOPlanObjectCopy.getPeriodicityDaily().contains(PlannedEndDateCal.getDayOfWeekStr(startDate))) {
				createDOID(wOPlanObjectCopy, execPlanDetails, response);
			}
			startDate.add(Calendar.DATE, 1);
			startDateWithOutTime.setTime(startDate.getTime());
			setTimeZeroToDate(startDateWithOutTime);

		}
	}

	private boolean isNotReOccurrence(CreateWorkOrderModel wOPlanObject) {

		return wOPlanObject.getPeriodicityDaily() == null && wOPlanObject.getPeriodicityWeekly() == null
				&& (CollectionUtils.isEmpty(wOPlanObject.getPeriodicityHourly()));
	}

	private void setCreateWorkOrderModelType(CreateWorkOrderModel wOPlanObject) {

		if (StringUtils.equalsIgnoreCase(AppConstants.WEB, wOPlanObject.getExternalSourceName())) {
			wOPlanObject.setType(AppConstants.STATUS_PLANNED);
		} else {
			wOPlanObject.setType(AppConstants.AUTO);
		}

	}

	/**
	 * This method creates DOID By NodeWise And DO Volume
	 * 
	 * @param wOPlanObject
	 * @param execPlanDetails
	 * @param response
	 */
	@SuppressWarnings("serial")
	@Transactional("transactionManager")
	public void createDOIDByNodeWiseAndDOVolume(CreateWorkOrderModel wOPlanObject,
			List<ExecutionPlanDetail> execPlanDetails, CreateWoResponse response) {

		for (int i = 0; i < wOPlanObject.getDoVolume(); i++) {

			if (!wOPlanObject.isNodeWise() || wOPlanObject.getWoCreationID() != 0) {

				addDOID(wOPlanObject, execPlanDetails, response);
			} else {

				CreateWorkOrderModel wOPlanObjectCopy = new CreateWorkOrderModel(wOPlanObject);
                
				wOPlanObjectCopy.setCount(wOPlanObject.getCount());
				wOPlanObjectCopy.setNeTextName(wOPlanObject.getNeTextName());
				wOPlanObjectCopy.setElementType(wOPlanObject.getElementType());
				wOPlanObjectCopy.setExternalSourceName(wOPlanObject.getExternalSourceName());
				// create for every node separately

				String[] nodes = wOPlanObject.getListOfNode().get(0).getNodeNames().split(AppConstants.CSV_CHAR_COMMA);

				for (String node : nodes) {
					
				List<WorkOrderPlanNodesModel> list = new ArrayList<>();
					list.add(new WorkOrderPlanNodesModel(wOPlanObject.getListOfNode().get(0).getNodeType(), node));
					wOPlanObjectCopy.setListOfNode(list);
					wOPlanObjectCopy.setTableName(wOPlanObject.getTableName());

					addDOID(wOPlanObjectCopy, execPlanDetails, response);
				}
			}
		}
	}

	@Transactional("transactionManager")
	/**
	 * This method creates DOID and work orders
	 * 
	 * @param wOPlanObject
	 * @param execPlanDetails
	 * @param response
	 * @return DOIDModel
	 */
	private DOIDModel createDOID(CreateWorkOrderModel wOPlanObject, List<ExecutionPlanDetail> execPlanDetails,
			CreateWoResponse response) {

		DOIDModel doID = new DOIDModel(wOPlanObject.getwOPlanID(), wOPlanObject.getCreatedBy(),
				wOPlanObject.getCreatedDate());
		workOrderPlanDao.createDOID(doID);
		addWorkOrder(wOPlanObject, execPlanDetails, doID, response);
		return doID;
	}

	@Transactional("transactionManager")
	/**
	 * This method adds work orders and save execution plan flow
	 * 
	 * @param wOPlanObject
	 * @param execPlanDetails
	 * @param doID
	 * @param response
	 */
	private void addWorkOrder(CreateWorkOrderModel wOPlanObject, List<ExecutionPlanDetail> execPlanDetails,
			DOIDModel doID, CreateWoResponse response) {

		int execPlanGroupId = -1;
		int seq = 0;
		ExecutionPlanDetail previousExecPlanDet = new ExecutionPlanDetail();
		CreateWorkOrderModel previousCreateWorkOrderModel = new CreateWorkOrderModel();

		for (ExecutionPlanDetail currentExecPlanDet : execPlanDetails) {

			if (currentExecPlanDet.isRoot()) {

				CreateWorkOrderModel createWorkOrderModel = getCreateWorkOrderModelFromExecutionPlanDetail(
						currentExecPlanDet, previousExecPlanDet, wOPlanObject, previousCreateWorkOrderModel, ++seq);
				
				createWorkOrderModel.setTableName(wOPlanObject.getTableName());
				createWorkOrderModel.setCount(wOPlanObject.getCount());
				createWorkOrderModel.setNeTextName(wOPlanObject.getNeTextName());
				createWorkOrderModel.setElementType(wOPlanObject.getElementType());
				createWorkOrderModel.setExternalSourceName(wOPlanObject.getExternalSourceName());
				
				createWorkOrder(createWorkOrderModel, response, doID, execPlanGroupId, currentExecPlanDet);
				previousExecPlanDet = currentExecPlanDet;
				previousCreateWorkOrderModel = createWorkOrderModel;
			}
		}

	}

	@Transactional("transactionManager")
	private void prepareAndSaveExecutionPlanFlow(CreateWorkOrderModel2 createModel, ExecutionPlanDetail execPlanDet,
			int execPlanGroupId) {

		ExecutionPlanFlow executionPlanFlow = new ExecutionPlanFlow();
		if (execPlanGroupId == -1) {
			execPlanGroupId = createModel.getwOPlanID();
		}
		executionPlanFlow.setPlanId(createModel.getwOPlanID());
		executionPlanFlow.setWoid(createModel.getWoId());
		executionPlanFlow.setExecutionPlanDetailId(execPlanDet.getExecutionPlanDetailId());
		executionPlanFlow.setExecPlanGroupId(execPlanGroupId);
		executionPlanFlow.setDoID(createModel.getDoID());
		saveExecutionPlanFlow(executionPlanFlow);

	}

	@Transactional("transactionManager")
	private CreateWorkOrderModel getCreateWorkOrderModelFromExecutionPlanDetail(ExecutionPlanDetail currentExecPlanDet,
			ExecutionPlanDetail previousExecPlanDet, CreateWorkOrderModel wOPlanObject,
			CreateWorkOrderModel previousCreateWorkOrderModel, int seq) {

		CreateWorkOrderModel wOPlanObjectRequestCopy = new CreateWorkOrderModel(wOPlanObject);
		int defID = workOrderPlanDao.getWorkFlowDefID(wOPlanObject.getProjectID(),
				currentExecPlanDet.getSubActivityID(), currentExecPlanDet.getWorkFlowVersionNo(),
				getWFIDFromtaskJSON(currentExecPlanDet));

		wOPlanObjectRequestCopy.setSlaHrs(currentExecPlanDet.getDuration());
		wOPlanObjectRequestCopy.setWfVersion(currentExecPlanDet.getWorkFlowVersionNo());
		wOPlanObjectRequestCopy.setSubActivityID(currentExecPlanDet.getSubActivityID());
		wOPlanObjectRequestCopy.setScopeID(currentExecPlanDet.getScopeId());
		wOPlanObjectRequestCopy.setFlowchartDefId(defID);
		wOPlanObjectRequestCopy
				.setwOName(setWoNameWithSuffix(wOPlanObjectRequestCopy.getwOName(), currentExecPlanDet, seq));
		Map<String, Object> activityDetails = workOrderPlanDao.getActivity(wOPlanObjectRequestCopy.getSubActivityID())
				.get(0);
		wOPlanObjectRequestCopy.setActivity(String.valueOf(activityDetails.get(ACTIVITY)));
		wOPlanObjectRequestCopy.setSubActivity(String.valueOf(activityDetails.get(SUBACTIVITY)));

		// change in logic according to stat date and time TODO

		if (seq != 1) {

			Date date = getDateByCalculatingGapAndDurationBetweenWorkFlow(
					previousCreateWorkOrderModel.getPlannedStartDate(), currentExecPlanDet, previousExecPlanDet);
			wOPlanObjectRequestCopy.setPlannedStartDate(date);
		}

		setWOStatus(wOPlanObjectRequestCopy, wOPlanObject.getStatus());

		return wOPlanObjectRequestCopy;
	}

	private void setWOStatus(CreateWorkOrderModel wOPlanObjectRequestCopy, String status) {
		if (StringUtils.equalsIgnoreCase(AppConstants.ERISITE_STATUS, status)) {
			wOPlanObjectRequestCopy.setStatus(AppConstants.STATUS_PLANNED);
		} else {
			wOPlanObjectRequestCopy.setStatus(AppConstants.WO_STATUS_ASSIGNED);
		}
	}

	// getting activity on the basis of subactivityID

	public Date getDateByCalculatingGapAndDurationBetweenWorkFlow(Date date, ExecutionPlanDetail currentExecPlanDet,
			ExecutionPlanDetail previousExecPlanDet) {

		date = getDateByCalculatingGapBetweenWorkFlow(date, currentExecPlanDet, previousExecPlanDet);
		date = DateUtils.addHours(date, previousExecPlanDet.getDuration());
		return date;
	}

	private Date getDateByCalculatingGapBetweenWorkFlow(Date date, ExecutionPlanDetail currentExecPlanDet,
			ExecutionPlanDetail previousExecPlanDet) {

		int gapBetweenTasksInHours = ((currentExecPlanDet.getDay() - 1) * 24 + currentExecPlanDet.getHour())
				- ((previousExecPlanDet.getDay() - 1) * 24 + previousExecPlanDet.getHour()
						+ previousExecPlanDet.getDuration());

		date = DateUtils.addHours(date, gapBetweenTasksInHours);
		return date;
	}

	public String setWoNameWithSuffix(String woName, ExecutionPlanDetail execPlanDet, int seq) {

		StringBuilder finalWoName = new StringBuilder(StringUtils.EMPTY);

		finalWoName.append(woName).append(AppConstants.UNDERSCORE).append(execPlanDet.getExecutionPlanId())
				.append(AppConstants.UNDERSCORE).append(execPlanDet.getSubActivityID()).append(AppConstants.UNDERSCORE)
				.append(seq);

		return new String(finalWoName);
	}

	public String getWFIDFromtaskJSON(ExecutionPlanDetail execPlanDet) {
		JSONObject jsonObject = new JSONObject(execPlanDet.getTaskJson());
		String workFlow = jsonObject.getString("workflow");
		String arrData[] = workFlow.split("/");
		if(arrData.length<3) {
				throw new ApplicationException(500, "Some issue while retrieving WO Work Flow details, please update your deliverable Plan/ Contact ISF support!!!");
		}
		String wfid = arrData[0];
		return wfid;

	}



	public static class BulkWoResponse {

		private static Map<String, List<CreateWorkOrderModel2>> WORK_ORDERS_FOR_UPLOADED_BY = new HashMap<String, List<CreateWorkOrderModel2>>();

		public static Map<String, List<CreateWorkOrderModel2>> getWorkOrdersForUploadedBy() {
			return WORK_ORDERS_FOR_UPLOADED_BY;
		}
	}

	/**
	 * Enrich mail data for work orders
	 * 
	 * @param wOPlanObject
	 * 
	 * @param createWoRequest
	 * @param response
	 * @return Map<String, Object>
	 */
	@Transactional("transactionManager")
	public Map<String, Object> enrichMailforWorkOrder(CreateWorkOrderModel wOPlanObject, CreateWoResponse response) {

		Map<String, Object> data = new HashMap<String, Object>();

		CreateWorkOrderModel2 createWoRequest = response.getWorkOrderID().get(0);
//		CreateWorkOrderModel2 createWoRequest = response.getResponseData().get(0);
		if (StringUtils.equalsIgnoreCase(AppConstants.WEB, wOPlanObject.getExternalSourceName())) {

			data.put(AppConstants.CURRENT_USER, createWoRequest.getCreateBy());
		} else {
			data.put("createdBy", createWoRequest.getCreateBy());
		}
		data.put("response", response);
		data.put("request", createWoRequest);
		data.put("details", workOrderPlanDao.getAdditionalInfoOfPlan(response.getWoPlanId()));
		String toEmails = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(createWoRequest.getSignumID())) {

			EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(createWoRequest.getSignumID());
			toEmails = eDetails.getEmployeeEmailId();
		}
		if (StringUtils.isNotBlank(toEmails)) {
			data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, toEmails);
		}
		return data;

	}

	/**
	 * This method is inserting wo plan details
	 * 
	 * @param wOPlanObject
	 * @param execPlanDetails
	 * @param response
	 */
	@Transactional("transactionManager")
	public void addWorkOrderPlan(CreateWorkOrderModel wOPlanObject, List<ExecutionPlanDetail> execPlanDetails,
			CreateWoResponse response) {

		String signum = null;
		if (CollectionUtils.isNotEmpty(wOPlanObject.getLstSignumID())
				&& StringUtils.isNotBlank(wOPlanObject.getLstSignumID().get(0))) {
			signum = wOPlanObject.getLstSignumID().get(0);
		}
		workOrderPlanDao.addWorkOrderPlan(wOPlanObject, signum);
		
         // as per CNEDB concept
		 if(StringUtils.isNotEmpty(wOPlanObject.getTableName())) {
			 networkElementDao.addWorkOrderPlanNodes(wOPlanObject.getTableName(),wOPlanObject.getwOPlanID(),wOPlanObject.getCreatedBy());	 
		 }
		response.setWoPlanId(wOPlanObject.getwOPlanID());
	}

	/**
	 * This method prepare ingredients for work order plan creation
	 * 
	 * @param wOPlanObject
	 * @param execPlanDetails
	 */
	public void prepareCreateWorkOrderModelForWOPlan(CreateWorkOrderModel wOPlanObject,
			List<ExecutionPlanDetail> execPlanDetails) {

		if (CollectionUtils.isNotEmpty(wOPlanObject.getPeriodicityHourly())) {

			String listString = wOPlanObject.getPeriodicityHourly().stream().map(String::valueOf)
					.collect(Collectors.joining(AppConstants.CSV_CHAR_COMMA));
			wOPlanObject.setLstPeriodicityHourly(listString);
		}

		Date plannedStartDate = DateTimeUtil.convertDateAndTimeToDate(wOPlanObject.getStartDate(),
				wOPlanObject.getStartTime());
		wOPlanObject.setPlannedStartDate(plannedStartDate);

		if (StringUtils.isBlank(wOPlanObject.getEndDate())) {

			// if end date is empty then last workflow day and hour should be added in
			// planned start date of work order plan
			ExecutionPlanDetail executionPlanDetail = execPlanDetails.get(execPlanDetails.size() - 1);
			Date endDate = PlannedEndDateCal.calculateDateByAddingDayAndHour(plannedStartDate,
					executionPlanDetail.getDay(), executionPlanDetail.getHour());

			wOPlanObject.setEndDate(PlannedEndDateCal.convertDateToString(endDate, AppConstants.UI_DATE_FORMAT));
		}

		setCreateWorkOrderModelType(wOPlanObject);
	}

	/**
	 * This method is responsible for creating work orders
	 * 
	 * @param createWorkOrderModel
	 * @param response
	 * @param doID
	 * @param execPlanDet
	 * @param execPlanGroupId
	 */
	@Transactional("transactionManager")
	private void createWorkOrder(CreateWorkOrderModel createWorkOrderModel, CreateWoResponse response, DOIDModel doID,
			int execPlanGroupId, ExecutionPlanDetail execPlanDet) {

		// work flow sla hours should be added in planned start date of work order
		Date plannedEndDate = PlannedEndDateCal.calculateEndDateV2(createWorkOrderModel.getPlannedStartDate(),
				createWorkOrderModel.getSlaHrs());
		createWorkOrderModel.setPlannedEndDate(plannedEndDate);

		CreateWorkOrderModel2 createModel = new CreateWorkOrderModel2(createWorkOrderModel, doID.getDoID());
		
		workOrderPlanDao.createWorkOrder(createModel);

		if (StringUtils.isNotBlank(createWorkOrderModel.getTableName())) {
			networkElementDao.createNodesForWorkOrder(createModel, createWorkOrderModel.getTableName(),
					createModel.getNode().split(AppConstants.CSV_CHAR_COMMA));
		}
		
		// insert into transactionalData.TBL_WorkOrder_NECount
		
		if (StringUtils.isNotBlank(createWorkOrderModel.getTableName())) {
			if (StringUtils.equalsIgnoreCase(AppConstants.WEB, createWorkOrderModel.getExternalSourceName())
					&& (StringUtils.equalsIgnoreCase(createWorkOrderModel.getElementType(), AppConstants.NETWORK)
					|| StringUtils.equalsIgnoreCase(createWorkOrderModel.getElementType(), AppConstants.CLUSTER))) {
				networkElementDao.insertNetworkClusterNetworkElementCount(createModel, createWorkOrderModel.getCount(),
						createWorkOrderModel.getNeTextName());
			}
			else if (!StringUtils.equalsIgnoreCase(AppConstants.WEB, createWorkOrderModel.getExternalSourceName())
					&& (StringUtils.equalsIgnoreCase(createModel.getNodeType(), AppConstants.NETWORK)
					|| StringUtils.equalsIgnoreCase(createModel.getNodeType(), AppConstants.CLUSTER))) {
				networkElementDao.insertNetworkClusterNetworkElementCount(createModel, createWorkOrderModel.getCount(),
						createWorkOrderModel.getNeTextName());
			} else {
				if (StringUtils.equalsIgnoreCase(AppConstants.WEB, createWorkOrderModel.getExternalSourceName())
						&& !StringUtils.equalsIgnoreCase(createWorkOrderModel.getElementType(), AppConstants.NETWORK)
						&& ! StringUtils.equalsIgnoreCase(createWorkOrderModel.getElementType(), AppConstants.CLUSTER)) {
					networkElementDao.insertWorkOrderNeCount(createModel, createWorkOrderModel.getCount(),
							createWorkOrderModel.getNeTextName());
				}
				else if(!StringUtils.equalsIgnoreCase(AppConstants.WEB, createWorkOrderModel.getExternalSourceName())
					&& !StringUtils.equalsIgnoreCase(createModel.getNodeType(), AppConstants.NETWORK)
					&& !StringUtils.equalsIgnoreCase(createModel.getNodeType(), AppConstants.CLUSTER)) {
					networkElementDao.insertWorkOrderNeCount(createModel, createWorkOrderModel.getCount(),
							createWorkOrderModel.getNeTextName());
				}
			}
		}

		// save file inputs
		insertInputFileWO(createWorkOrderModel, createModel);

		if (execPlanDet.getExecutionPlanDetailId() != 0) {

			prepareAndSaveExecutionPlanFlow(createModel, execPlanDet, execPlanGroupId);
		}

		setResponse(createModel, response);
	}

	@SuppressWarnings("serial")
	private void setResponse(CreateWorkOrderModel2 createModel, CreateWoResponse response) {

		response.getWorkOrderID().add(createModel);
//		response.getResponseData().add(createModel);
		String uploadedBy = createModel.getUploadedBy();
		if (StringUtils.isNotBlank(uploadedBy)) {

			Map<String, List<CreateWorkOrderModel2>> workOrdersForUploadedBy = BulkWoResponse
					.getWorkOrdersForUploadedBy();
			if (workOrdersForUploadedBy.get(uploadedBy) == null
					|| CollectionUtils.isEmpty(workOrdersForUploadedBy.get(uploadedBy))) {

				workOrdersForUploadedBy.put(uploadedBy, new ArrayList<CreateWorkOrderModel2>() {
					{
						add(createModel);
					}
				});
			} else {

				workOrdersForUploadedBy.get(uploadedBy).add(createModel);
			}
		}

	}

	/**
	 * This method mark existing work flow as completed and create seq work order if
	 * any
	 * 
	 * @param saveClosureDetailsObject
	 */
	@Transactional("transactionManager")
	public CreateWoResponse createExecutionPlanFLow(SaveClosureDetailsForWOModel saveClosureDetailsObject) {

		ExecutionPlanFlow flowDetails = workOrderPlanDao.getExecutionFlowByWoid(saveClosureDetailsObject.getwOID());
		CreateWoResponse response = new CreateWoResponse();
		if (flowDetails == null || flowDetails.isComplete()) { //
			// this is not part of execution plan or this wo is restarted
			response.setMsg("Work order is either completed or invalid!");
			return response;
		}

		// mark current step as complete
		workOrderPlanDao.markExecutionPlanFlowComplete(flowDetails);

		if (!saveClosureDetailsObject.isCreateSubSequentWO()) {
			// end execution permanantly
			return response;
		}

		ExecutionPlanDetail currentStepDetails = workOrderPlanDao
				.getExecutionPlanDetailsByExecutionPlanDetailId(flowDetails.getExecutionPlanDetailId());
		ExecutionPlanModel planMaster = workOrderPlanDao
				.getActiveExecutionPlanByid(currentStepDetails.getExecutionPlanId());

		LinkModel[] links = planMaster.getLinks();
		List<Long> childTasks = getSubSequentPlanDetail(planMaster, currentStepDetails);

		if (CollectionUtils.isEmpty(childTasks)) {
			// no child task so flow for this wo ends here
			response.setMsg("No Subsequent plan found for given work order!");
			return response;
		} else {
			List<ExecutionPlanDetail> planStepDetails = workOrderPlanDao
					.getExecutionPlanDetailsByExecutionPlanId(currentStepDetails.getExecutionPlanId());
			Map<Long, ExecutionPlanDetail> taskNumberToPlanDetailMapping = new HashMap<>();

			for (ExecutionPlanDetail planDetail : planStepDetails) {
				taskNumberToPlanDetailMapping.put(planDetail.getId(), planDetail);
			}

			for (ExecutionPlanDetail planDetail : planStepDetails) {
				if (childTasks.contains(planDetail.getId())) {
					// check if all parent tasks are completed

					for (LinkModel link : links) {
						if (link.getTarget() == planDetail.getId()) {

							long parent = link.getSource();
							if (parent == currentStepDetails.getId()) {
								continue;
							}
							ExecutionPlanDetail parentPlanDetails = taskNumberToPlanDetailMapping.get(parent);
							ExecutionPlanFlow parentFlow = workOrderPlanDao.getExecutionPlanFlowByDOID(
									flowDetails.getDoID(), parentPlanDetails.getExecutionPlanDetailId());
							if (parentFlow == null) {
								continue;
							}

							if (!parentFlow.isComplete()) {
								response.setMsg(
										"All parent tasks are not completed so cannot create new wo right now!");
								return response;
								// all parent tasks are not completed so cannot create new wo right now

							}

						}
					}

					ExecutionPlanFlow plannedExecutionPlanFlow = workOrderPlanDao
							.getExecutionPlanFlowByDOID(flowDetails.getDoID(), planDetail.getExecutionPlanDetailId());

					if (plannedExecutionPlanFlow == null) {
						//createSubSequentWO(saveClosureDetailsObject, flowDetails, currentStepDetails, planDetail,
						//		response);
						createSubSequentWOrkOrder(saveClosureDetailsObject, flowDetails, currentStepDetails, planDetail,
										response);
					}

				}

			}

		}
		return response;

	}

	/**
	 * This method creates subsequent wo
	 * 
	 * @param saveClosureDetailsObject
	 * @param flowDetails
	 * @param currentStepDetails
	 * @param planDetail
	 * @param response
	 */
	private void createSubSequentWO(SaveClosureDetailsForWOModel saveClosureDetailsObject,
			ExecutionPlanFlow flowDetails, ExecutionPlanDetail currentStepDetails, ExecutionPlanDetail planDetail,
			CreateWoResponse response) {

		WorkOrderPlanModel existingWoPlan = workOrderPlanDao.getPlanDetailsById(flowDetails.getPlanId());
		WorkOrderModel workOrder = getWODataFromWorkOrderPlanModel(saveClosureDetailsObject.getwOID(), existingWoPlan);
		CreateWorkOrderModel createWorkOrderModel = getCreateWorkOrderModelFromWorkOrderPlanModel(existingWoPlan,
				planDetail, currentStepDetails, workOrder);

		response.setWoPlanId(flowDetails.getPlanId());
		DOIDModel doID = new DOIDModel(workOrder.getDoID(), flowDetails.getPlanId());
		createWorkOrder(createWorkOrderModel, response, doID, (int) flowDetails.getExecPlanGroupId(), planDetail);
	}
	
	private void createSubSequentWOrkOrder(SaveClosureDetailsForWOModel saveClosureDetailsObject,
			ExecutionPlanFlow flowDetails, ExecutionPlanDetail currentStepDetails, ExecutionPlanDetail planDetail,
			CreateWoResponse response) {

		WorkOrderPlanModel existingWoPlan = workOrderPlanDao.getPlanDetailsById(flowDetails.getPlanId());
		WorkOrderModel workOrder = getWODataFromWorkOrderPlanModel(saveClosureDetailsObject.getwOID(), existingWoPlan);
		CreateWorkOrderModel createWorkOrderModel = getCreateWorkOrderModelFromWorkOrderPlanModel(existingWoPlan,
				planDetail, currentStepDetails, workOrder);

		response.setWoPlanId(flowDetails.getPlanId());
		DOIDModel doID = new DOIDModel(workOrder.getDoID(), flowDetails.getPlanId());
		List<WorkOrderPlanNodesModel> listOfNode = workOrderPlanDao.getNodesByWOId(saveClosureDetailsObject.getwOID());
		createWorkOrderv1(createWorkOrderModel, response, doID, (int) flowDetails.getExecPlanGroupId(), planDetail
				,saveClosureDetailsObject.getwOID(),listOfNode);
	}

	private WorkOrderModel getWODataFromWorkOrderPlanModel(int woID, WorkOrderPlanModel existingWoPlan) {

		WorkOrderModel workOrder = existingWoPlan.getListOfWorkOrder().stream().filter(wo -> wo.getwOID() == woID)
				.findFirst().get();

		if (workOrder == null) {
			throw new ApplicationException(500, "Given work order id is not valid");
		}
		return workOrder;
	}

	/**
	 * 
	 * 
	 * @param existingWoPlan
	 * @param planDetail
	 * @param currentStepDetails
	 * @param workOrder
	 * @return CreateWorkOrderModel
	 */
	private CreateWorkOrderModel getCreateWorkOrderModelFromWorkOrderPlanModel(WorkOrderPlanModel existingWoPlan,
			ExecutionPlanDetail planDetail, ExecutionPlanDetail currentStepDetails, WorkOrderModel workOrder) {

		CreateWorkOrderModel createWoPlanRequest = new CreateWorkOrderModel();

		createWoPlanRequest.setFlowchartDefId(workOrderPlanDao.getWorkFlowDefID(existingWoPlan.getProjectID(),
				planDetail.getSubActivityID(), planDetail.getWorkFlowVersionNo(), getWFIDFromtaskJSON(planDetail)));
		createWoPlanRequest.setScopeID(planDetail.getScopeId());
		createWoPlanRequest.setSubActivityID(planDetail.getSubActivityID());
		createWoPlanRequest.setWfVersion(planDetail.getWorkFlowVersionNo());
		createWoPlanRequest.setSlaHrs(planDetail.getDuration());

		createWoPlanRequest.setProjectID(existingWoPlan.getProjectID());
		createWoPlanRequest.setPriority(workOrder.getPriority());
		createWoPlanRequest.setwOPlanID(workOrder.getwOPlanID());
		createWoPlanRequest.setwOName(setWoNameWithSuffix(getWoNameFromSuffixedWoName(workOrder.getwOName()),
				planDetail, planDetail.get$index() + 1));

		setListOfSignumIdInCreateWorkOrderModel(createWoPlanRequest, workOrder.getSignumID());

		Date startDate = getDateByCalculatingGapBetweenWorkFlow(new Date(), planDetail, currentStepDetails);

		createWoPlanRequest
				.setStartDate(PlannedEndDateCal.convertDateToString(startDate, AppConstants.DEFAULT_DATE_FORMAT));
		createWoPlanRequest.setPlannedStartDate(startDate);
		createWoPlanRequest.setStartTime(new Time(startDate.getTime()));
        if (CollectionUtils.isNotEmpty(workOrder.getListOfNode())) {
		String nodes = workOrder.getListOfNode().stream().map(node -> String.valueOf(node.getNodeNames()))
				.collect(Collectors.joining(AppConstants.CSV_CHAR_COMMA));
		setListOfNodeListInCreateWorkOrderModel(createWoPlanRequest, workOrder.getListOfNode().get(0).getNodeType(),
				nodes);
	}
        List<WOInputFileModel> woinput = getWOInputFile(workOrder.getwOID());
		setListOfInputFileListInCreateWorkOrderModel(createWoPlanRequest, woinput);
		createWoPlanRequest.setLastModifiedBy(existingWoPlan.getLastModifiedBy());
		createWoPlanRequest.setCreatedBy(existingWoPlan.getCreatedBy());
		createWoPlanRequest.setEndDate(StringUtils.EMPTY);
		createWoPlanRequest.setType(AppConstants.AUTO);
		createWoPlanRequest.setNodeWise(existingWoPlan.isNodeWise());
		setWOStatus(createWoPlanRequest, StringUtils.EMPTY);
		return createWoPlanRequest;
	}

	private void setListOfInputFileListInCreateWorkOrderModel(CreateWorkOrderModel createWoPlanRequest,
			List<WOInputFileModel> woinput) {
		if (CollectionUtils.isNotEmpty(woinput)) {
			List<WOInputFileModel> file = new ArrayList<WOInputFileModel>();
			for (WOInputFileModel input : woinput) {
				Object inputName = input.getInputName();
				Object inputUrl = input.getInputUrl();
				if (inputName != null && inputUrl != null) {

					file.add(new WOInputFileModel(inputName.toString(), inputUrl.toString()));
				}
			}
			WorkOrderInputFileModel workOrderInputFileModel = new WorkOrderInputFileModel(file);
			createWoPlanRequest.setWorkOrderInputFileModel(workOrderInputFileModel);

		}

	}

	public List<WorkOrderPlanModel> searchWorkOrderPlanDetails(int projectID, int domainID, int serviceAreaID,
			int technologyID, String activityName, int subActivityID) {
		List<WorkOrderPlanModel> workOrderPlan = new ArrayList<>();
		workOrderPlan = workOrderPlanDao.searchWorkOrderPlanDetails(projectID, domainID, serviceAreaID, technologyID,
				activityName, subActivityID);
		return workOrderPlan;
	}

	public boolean isPlanEditable(int woPlanId) {
		int count = workOrderPlanDao.isPlanEditable(woPlanId);
		return count == 0;
	}

	public Map<String, Object> getWorkOrderPlans(int projectID, String startDate, String endDate, String woStatus,
			DataTableRequest req) {

		Map<String, Object> res = new HashMap<>();
		List<WorkOrderPlanModel2> workOrderPlans = workOrderPlanDao.getWorkOrderPlans(projectID, startDate, endDate,
				woStatus, req);
		res.put("data", workOrderPlans);

		if (!workOrderPlans.isEmpty()) {
			res.put(RECORDS_TOTAL, workOrderPlans.get(0).getTotalCount());
			res.put(RECORDS_FILTERED, workOrderPlans.get(0).getTotalCount());
		} else {
			res.put(RECORDS_TOTAL, 0);
			res.put(RECORDS_FILTERED, 0);
		}

		return res;

	}

	public List<WorkOrderViewProjectModel> getWorkOrderViewDetails(String projectID, String scope, String activity,
			String WOID, String startDate, String endDate, String assignedTo, String signum_LoggedIn,
			boolean isWeekEndIncluded, boolean isOddHoursIncluded, String status, String nodeName, String marketArea,
			String assignedBy) {

		List<WorkOrderViewProjectModel> woViewDetails = workOrderPlanDao.getWorkOrderViewDetails(projectID, scope,
				activity, WOID, startDate, endDate, assignedTo, signum_LoggedIn, isWeekEndIncluded, isOddHoursIncluded,
				status, nodeName, marketArea, assignedBy);

		for (WorkOrderViewProjectModel model : woViewDetails) {
			List<WOOutputFileResponseModel> temp = new LinkedList<>();
			for (WOOutputFileResponseModel workOrder : model.getWoOutputLink()) {
				List<WOOutputFileResponseModel> woOutputFile = getWOOutputFile((Integer) workOrder.getWoid());
				temp.addAll(woOutputFile);
			}
			model.setWoOutputLink(temp);
		}

		return woViewDetails;
	}

	public List<Map<String, Object>> getNEIDByProjectID(int projectID, String term) {
		
		return workOrderPlanDao.getNEIDByProjectID(projectID, term);
	}

	@Transactional("transactionManager")
	/* Closes connection automatically */
	public void deleteWorkOrder(int wOID, String signumID) {
		Boolean checkStatus = workOrderPlanDao.checkNotStartedStatusOfWorkOrder(wOID);
		if (checkStatus) {
			workOrderPlanDao.deleteWONodes(wOID);
			workOrderPlanDao.deleteWorkOrder(wOID, signumID);

			int woplan = workOrderPlanDao.getWOPlanByWOID(wOID);

			Boolean checkplan = workOrderPlanDao.checkPlanStatus(woplan);
			if (!checkplan) {
				workOrderPlanDao.inactiveWOPlan(woplan);

			}
		} else {
			throw new ApplicationException(500, "Work Order already Started !!");
		}

	}

	@Transactional("transactionManager")
	/* Closes connecton automaticaly */
	public void updateWorkOrder(WorkOrderModel workOrderModel) {
		Boolean checkStatus = workOrderPlanDao.checkNotStartedStatusOfWorkOrder(workOrderModel.getwOID());
		if (checkStatus) {
			workOrderPlanDao.updateWorkOrder(workOrderModel);
		} else {
			throw new ApplicationException(500, "Work Order has already Started!!");
		}
	}

	public List<WorkOrderModel> getWorkOrderDetails(int wOID) {
		return workOrderPlanDao.getWorkOrderDetails(wOID);
	}

	private static final int ROOT_PARENT_WORKORDER_ID = 0;

	@Transactional("transactionManager")
	public CreateWoResponse reopenDefferedWorkOrder(int workOrderId) {
		CreateWoResponse response = new CreateWoResponse();
		try {
			List<Integer> listofwoid=new ArrayList<>();
			listofwoid.add(workOrderId);
			validationUtilityService.validateWorkorderForSrRequest(listofwoid);
			WorkOrderModel wo = workOrderPlanDao.getWorkOrderDetailsById(workOrderId);
			workOrderPlanDao.updateWorkOrderParentId(ROOT_PARENT_WORKORDER_ID, wo.getwOID());
			workOrderPlanDao.updatePreviousWOID(workOrderId);
            createDefferedWorkOrder(wo, response);

			int newWOID = response.getWorkOrderID().get(0).getWoId();

			MailModel model = getWoMailNotificationDetails(newWOID);
			if (model != null) {

				WorkOrderMailModel workOrderMailModel = new WorkOrderMailModel(model.getEmployeeSignum(),
						model.getEmployeeName(), model.getProjectCreatorSignum(), model.getProjectCreatorName(),
						model.getEmployeeEmailID(), model.getProjectCreatorEmailID(), "REOPENED", newWOID, AppConstants.NA,
						woExecutionService.getNodesByWoId(wo.getwOID()));

				woExecutionService.sendMail(workOrderMailModel, model);
				response.setMsg("WorkOrder ID " + workOrderId + " successfully Reopened");
			}
		}
		catch (ApplicationException  e) {
			response.setMsg(e.getMessage());
		}
		

		return response;

	}

	/**
	 * 
	 * @param wo
	 * @param response
	 */
	private void createDefferedWorkOrder(WorkOrderModel wo, CreateWoResponse response) {

		Calendar cal = Calendar.getInstance();
		CreateWorkOrderModel createModel = new CreateWorkOrderModel();

		createModel.setwOPlanID(wo.getwOPlanID());
		createModel.setPlannedStartDate(cal.getTime());
		setListOfSignumIdInCreateWorkOrderModel(createModel, wo.getSignumID());
		createModel.setCreatedBy(wo.getCreatedBy());
		createModel.setPriority(wo.getPriority());
		createModel.setWfVersion(wo.getWfVersion());
		createModel.setProjectID(wo.getProjectid());
		createModel.setFlowchartDefId(wo.getFlowchartdefid());
		createModel.setParentWOID(wo.getwOID());
		createModel.setSlaHrs(wo.getSlaHrs());
		createModel.setSubActivityID(wo.getSubActivityID());
		createModel.setwOName(wo.getwOName());
		createModel.setWorkOrderAutoSenseEnabled(wo.getWorkOrderAutoSenseEnabled());
		List<WorkOrderPlanNodesModel> listOfNode = workOrderPlanDao.getNodesByWOId(wo.getwOID());
		if(!CollectionUtils.isEmpty(listOfNode)) {
			String nodes = listOfNode.stream().map(node -> String.valueOf(node.getNodeNames()))
					.collect(Collectors.joining(AppConstants.CSV_CHAR_COMMA));
			setListOfNodeListInCreateWorkOrderModel(createModel,
					listOfNode.get(0).getNodeType(), nodes);
		}
		setListOfSignumIdInCreateWorkOrderModel(createModel, wo.getSignumID());
		setWOStatus(createModel, StringUtils.EMPTY);
		List<WOInputFileModel> woinput = getWOInputFile(wo.getwOID());
		setListOfInputFileListInCreateWorkOrderModel(createModel, woinput);
		DOIDModel doID = new DOIDModel(wo.getDoID(), wo.getwOPlanID());

		ExecutionPlanFlow flowDetails = workOrderPlanDao.getExecutionFlowByWoid(wo.getwOID());

		ExecutionPlanDetail currentStepDetails = new ExecutionPlanDetail();
		if (flowDetails != null) {

			currentStepDetails = workOrderPlanDao
					.getExecutionPlanDetailsByExecutionPlanDetailId(flowDetails.getExecutionPlanDetailId());
		}
		createWorkOrderv1(createModel, response, doID, wo.getwOPlanID(), currentStepDetails,wo.getwOID(),listOfNode);
	}

	@Transactional("transactionManager")
	public int closeDeferedWorkOrder(int workOrderId) {
		workOrderPlanDao.closeDeferedWorkOrder(workOrderId);
		return workOrderId;

	}

	@Transactional("transactionManager")
	public ResponseEntity<Response<Void>> reinstateDeferedWorkOrder(int workOrderId) {
		
		Response<Void> workOrderReinstate = new Response<>();
		try {
		List<Integer> listofwoid=new ArrayList<>();
		listofwoid.add(workOrderId);
		validationUtilityService.validateReinstateDeferedWorkorderForSrRequest(listofwoid);

		if (CollectionUtils.isNotEmpty(workOrderPlanDao.getDeferedWorkOrderDetails(workOrderId))) {
			workOrderPlanDao.setInprogressDeferedWorkOrder(workOrderId);
		} else {
			workOrderPlanDao.setAssignedDeferedWorkOrder(workOrderId);
		}

		MailModel model = getWoMailNotificationDetails(workOrderId);
		if (model != null) {

			WorkOrderMailModel workOrderMailModel = new WorkOrderMailModel(model.getEmployeeSignum(),
					model.getEmployeeName(), model.getProjectCreatorSignum(), model.getProjectCreatorName(),
					model.getEmployeeEmailID(), model.getProjectCreatorEmailID(), "REINSTATED", workOrderId,
					AppConstants.NA, woExecutionService.getNodesByWoId(workOrderId));

			woExecutionService.sendMail(workOrderMailModel, model);
			 workOrderReinstate.addFormMessage("WorkOrder ID " + workOrderId + " successfully reinstated");


		}
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			workOrderReinstate.addFormError(e.getMessage());
			return new ResponseEntity<>(workOrderReinstate, HttpStatus.OK);
		}
		catch(Exception ex) {
			workOrderReinstate.addFormError(ex.getMessage());
			return new ResponseEntity<>(workOrderReinstate, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return  new ResponseEntity<>(workOrderReinstate, HttpStatus.OK);

	}

	public ResponseEntity<Response<Void>> transferWorkOrder(TransferWorkOrderModel transferWorkOrderModel) {

		Response<Void> workOrderTransferSuccess=new Response<>();
		try {
			if(!this.accessManagementDAO.isSignumExistsEmp(transferWorkOrderModel.getReceiverID())) {
				throw new ApplicationException(500,"Invalid Signum ,Please provide valid Signum");
			}
			for (int woid : transferWorkOrderModel.getWoID()) {
				WorkOrderModel wodetails = workOrderPlanDao.getWorkOrderDetailsById(woid);
				if (wodetails!=null) {
					if (!workOrderPlanDao.checkIfStepInStartedState(woid)) {
					//	WorkOrderModel wodetails = workOrderPlanDao.getWorkOrderDetailsById(woid);	
						if (!AppConstants.CLOSED.equals(wodetails.getStatus())) {
							workOrderPlanDao.transferWorkOrder(woid, transferWorkOrderModel.getSenderID(),
									transferWorkOrderModel.getReceiverID());
							if (AppConstants.WO_STATUS_DEFERRED.equals(wodetails.getStatus())) {
								workOrderPlanDao.updateWorkOrderStatus(woid, transferWorkOrderModel.getSenderID(),AppConstants.WO_STATUS_ASSIGNED);
							}
							workOrderPlanDao.updateTransferWOLOG(woid, transferWorkOrderModel.getReceiverID(),transferWorkOrderModel.getSenderID());
							sendMailForTransferWO(transferWorkOrderModel, woid);
							workOrderTransferSuccess.addFormMessage(WO_TRANSFER_SUCCESS);
						} else {
							throw new ApplicationException(500,"Cannot Transfer Work Order as the Status of the WorkOrder is either CLOSED or DEFERRED !!!");
						}
					} else {
						throw new ApplicationException(500,CANNOT_TRANSFER_WORK_ORDER_AS_THE_STATUS_OF_ONE_OF_THE_STEPS_IS_STARTED);
					}
				} else {
					throw new ApplicationException(500, WORK_ORDER_DOESN_T_EXISTS);
				}
			}
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			workOrderTransferSuccess.addFormError(e.getMessage());
			return new ResponseEntity<Response<Void>>(workOrderTransferSuccess, HttpStatus.OK);
		}
		catch(Exception ex) {
			workOrderTransferSuccess.addFormError(ex.getMessage());
			return new ResponseEntity<Response<Void>>(workOrderTransferSuccess, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return  new ResponseEntity<Response<Void>>(workOrderTransferSuccess, HttpStatus.OK);
	}


private void callMailerForTransferWO(String templateId, String recieverSignum, String senderSignum , String loggedInSignum, String nodes, int woid, String stepName, String userComments) {
	new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				emailService.sendMail(templateId,enrichMailforTransferWO(senderSignum, recieverSignum, loggedInSignum, nodes, woid,stepName,userComments));
			} catch (Exception e) {
				LOG.debug(String.format("Exception thrown  %s", e.getMessage()));
			}

		}
	}).start();
	}

//	public void transferWorkOrder(TransferWorkOrderModel transferWorkOrderModel) {
//		List<String> mailBodies= new LinkedList<>();
//		Boolean checkEmpTbl = this.accessManagementDAO.isSignumExistsEmp(transferWorkOrderModel.getReceiverID());
//		EmployeeBasicDetails empDet = woPlanService.getEmpInfo(transferWorkOrderModel.getReceiverID());
//		EmployeeBasicDetails empDet2 = woPlanService.getEmpInfo(transferWorkOrderModel.getSenderID());
//		EmployeeBasicDetails empDet3 = woPlanService.getEmpInfo(transferWorkOrderModel.getLogedInSignum());
//		if(!checkEmpTbl) {
//			throw new ApplicationException(500,
//					"Invalid Signum ,Please provide valid Signum");
//		}
//		for (int woid : transferWorkOrderModel.getWoID()) {
//			WorkOrderModel wodetails = workOrderPlanDao.getWorkOrderDetailsById(woid);
//			if(wodetails.getActive()==false) {
//				transferWorkOrderModel=null;
//				throw new ApplicationException(500, "Work Order doesn't Exists...!!! ");
//			}
//			if (AppConstants.CLOSED.equals(wodetails.getStatus())) {
//				transferWorkOrderModel=null;
//				throw new ApplicationException(500,
//						"Cannot Transfer Work Order as the Status of the WorkOrder is either CLOSED or DEFERRED !!!");
//			}
//			boolean checkIfStepInStartedState = workOrderPlanDao.checkIfStepInStartedState(woid);
//			if (checkIfStepInStartedState) {
//				transferWorkOrderModel=null;
//				throw new ApplicationException(500,
//						"Cannot Transfer Work Order as the Status of one of the steps is STARTED !!!");
//			}
//			workOrderPlanDao.transferWorkOrder(woid, transferWorkOrderModel.getSenderID(),
//					transferWorkOrderModel.getReceiverID());
//			if (AppConstants.WO_STATUS_DEFERRED.equals(wodetails.getStatus())) {
//				workOrderPlanDao.updateWorkOrderStatus(woid, transferWorkOrderModel.getSenderID(),
//						AppConstants.WO_STATUS_ASSIGNED);
//			}
//			workOrderPlanDao.updateTransferWOLOG(woid, transferWorkOrderModel.getReceiverID(),
//					transferWorkOrderModel.getSenderID());
//			MailModel model = woPlanService.getWoMailNotificationDetails(woid);
//			if (model != null) {
//				mailBodies.add(mailUtil.generateMailBodyForWorkOrders(empDet.getSignum(),
//						empDet.getEmployeeName(), empDet2.getSignum(), empDet2.getEmployeeName(),
//						"TRANSFERRED", woid, "NA", woExecutionService.getNodesByWoId(woid))) ;
//			}
//		}
//		woPlanService.SendMailNotification(empDet.getEmployeeEmailId(), mailBodies,
//				empDet2.getEmployeeEmailId() + ";" + empDet3.getEmployeeEmailId());
//		transferWorkOrderModel=null;
//	}
	
	// private static final String BOOKING_STATUS_ONHOLD="ONHOLD";
//	@Transactional("transactionManager")
//	public void transferWorkOrder(TransferWorkOrderModel transferWorkOrderModel) {
//		List<String> mailBodies= new LinkedList<>();
//		Boolean checkEmpTbl = this.accessManagementDAO.isSignumExistsEmp(transferWorkOrderModel.getReceiverID());
//		EmployeeBasicDetails empDet = woPlanService.getEmpInfo(transferWorkOrderModel.getReceiverID());
//		EmployeeBasicDetails empDet2 = woPlanService.getEmpInfo(transferWorkOrderModel.getSenderID());
//		EmployeeBasicDetails empDet3 = woPlanService.getEmpInfo(transferWorkOrderModel.getLogedInSignum());
//		if(!checkEmpTbl) {
//			throw new ApplicationException(500,
//					"Invalid Signum ,Please provide valid Signum");
//		}
//		for (int woid : transferWorkOrderModel.getWoID()) {
//			boolean checkWOExists = workOrderPlanDao.checkIFWOExists(woid);
//			if (checkWOExists) {
//				boolean checkIfStepInStartedState = workOrderPlanDao.checkIfStepInStartedState(woid);
//				if (!checkIfStepInStartedState) {
//					WorkOrderModel wodetails = workOrderPlanDao.getWorkOrderDetailsById(woid);
//					if (!AppConstants.CLOSED.equals(wodetails.getStatus())) {
//						workOrderPlanDao.transferWorkOrder(woid, transferWorkOrderModel.getSenderID(),
//								transferWorkOrderModel.getReceiverID());
//						if (AppConstants.WO_STATUS_DEFERRED.equals(wodetails.getStatus())) {
//							workOrderPlanDao.updateWorkOrderStatus(woid, transferWorkOrderModel.getSenderID(),
//									AppConstants.WO_STATUS_ASSIGNED);
//						}
//						workOrderPlanDao.updateTransferWOLOG(woid, transferWorkOrderModel.getReceiverID(),
//								transferWorkOrderModel.getSenderID());
//						flag = true;
//						MailModel model = getWoMailNotificationDetails(woid);
////						EmployeeBasicDetails empDet = getEmpInfo(transferWorkOrderModel.getReceiverID());
////						EmployeeBasicDetails empDet2 = getEmpInfo(transferWorkOrderModel.getSenderID());
////						EmployeeBasicDetails empDet3 = getEmpInfo(transferWorkOrderModel.getLogedInSignum());
//						if (model != null) {
//							String mailBody = mailUtil.generateMailBodyForWorkOrders(empDet.getSignum(),
//									empDet.getEmployeeName(), empDet2.getSignum(), empDet2.getEmployeeName(),
//									"TRANSFERRED", woid, "NA", woExecutionService.getNodesByWoId(woid));
//							SendMailNotification(empDet.getEmployeeEmailId(), mailBody,
//									empDet2.getEmployeeEmailId() + ";" + empDet3.getEmployeeEmailId());
//						}
//					} else {
//						throw new ApplicationException(500,
//								"Cannot Transfer Work Order as the Status of the WorkOrder is either CLOSED or DEFERRED !!!");
//					}
//				} else {
//					throw new ApplicationException(500,
//							"Cannot Transfer Work Order as the Status of one of the steps is STARTED !!!");
//				}
//			} else {
//				throw new ApplicationException(500, "Work Order doesn't Exists...!!! ");
//			}
//			if (AppConstants.CLOSED.equals(wodetails.getStatus())) {
//				transferWorkOrderModel=null;
//				throw new ApplicationException(500,
//						"Cannot Transfer Work Order as the Status of the WorkOrder is either CLOSED or DEFERRED !!!");
//			}
//			boolean checkIfStepInStartedState = workOrderPlanDao.checkIfStepInStartedState(woid);
//			if (checkIfStepInStartedState) {
//				transferWorkOrderModel=null;
//				throw new ApplicationException(500,
//						"Cannot Transfer Work Order as the Status of one of the steps is STARTED !!!");	
//			}
//			workOrderPlanDao.transferWorkOrder(woid, transferWorkOrderModel.getSenderID(),
//					transferWorkOrderModel.getReceiverID());
//			if (AppConstants.WO_STATUS_DEFERRED.equals(wodetails.getStatus())) {
//				workOrderPlanDao.updateWorkOrderStatus(woid, transferWorkOrderModel.getSenderID(),
//						AppConstants.WO_STATUS_ASSIGNED);
//			}
//			workOrderPlanDao.updateTransferWOLOG(woid, transferWorkOrderModel.getReceiverID(),
//					transferWorkOrderModel.getSenderID());
//			MailModel model = woPlanService.getWoMailNotificationDetails(woid);
//			if (model != null) {
//				mailBodies.add(mailUtil.generateMailBodyForWorkOrders(empDet.getSignum(),
//						empDet.getEmployeeName(), empDet2.getSignum(), empDet2.getEmployeeName(),
//						"TRANSFERRED", woid, "NA", woExecutionService.getNodesByWoId(woid))) ;
//			}
//		}
//		woPlanService.SendMailNotification(empDet.getEmployeeEmailId(), mailBodies,
//				empDet2.getEmployeeEmailId() + ";" + empDet3.getEmployeeEmailId());
//		transferWorkOrderModel=null;
//	}

	
	
	private static final String pageLinkManager = "Project/DeliveryAcceptance";
	private static final String pageLinkNE ="DeliveryExecution/WorkorderAndTask" ;
	protected Map<String, Object> enrichMailforTransferWO(String senderSignum, String recieverSignum, String loggedInSignum, String nodes, int woid,String stepName, String userComments) {
		EmployeeBasicDetails empDet = getEmpInfo(senderSignum);
		EmployeeBasicDetails empDet2 = getEmpInfo(recieverSignum);
		EmployeeBasicDetails empDet3 = getEmpInfo(loggedInSignum);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(AppConstants.SENDER_NAME, empDet.getEmployeeName());
		data.put(AppConstants.WO_ID, woid);
		data.put(AppConstants.STEP_NAME, stepName);
		if(StringUtils.isEmpty(userComments)) {
			userComments=AppConstants.CHAR_HYPHEN;
		}
		data.put(AppConstants.USER_COMMENTS, userComments);
		if(StringUtils.isEmpty(nodes)) {
			nodes=AppConstants.CHAR_HYPHEN;
		}
		data.put(AppConstants.NE_ID, nodes);
		data.put(AppConstants.STATUS_STRING, AppConstants.WO_TRANSFERRED);
		data.put(AppConstants.RECIEVER_NAME, empDet2.getEmployeeName());
		data.put(AppConstants.RECIEVER_ID, empDet2.getSignum());
		data.put(AppConstants.SENDER_ID, empDet.getSignum());
		data.put(AppConstants.PAGE_LINK_MANAGER , pageLinkManager);
		data.put(AppConstants.PAGE_LINK_NE, pageLinkNE);
		data.put(AppConstants.ENVIRONMENT, environmentPropertyService.getEnvironmentPropertyModelByKey(AppConstants.DEPLOYED_ENV_KEY).get(0).getValue());
		
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO,(new StringBuilder()).append(empDet2.getEmployeeEmailId()).append(AppConstants.SEMICOLON ).append(empDet3.getEmployeeEmailId()));
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, empDet.getEmployeeEmailId());
		return data;
}

	private void SendMailNotification(String employeeEmailId,List<String> mailBodies, String receiverSignumID) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for(String mailBody:mailBodies) {
						sendMailNotification(employeeEmailId, mailBody, receiverSignumID);
					}
				} catch (Exception e) {
					LOG.debug(String.format("Exception thrown  %s", e.getMessage()));
				}

			}
		}).start();
	}

	public List<String> getPriority() {
		return workOrderPlanDao.getPriority();
	}

	public List<String> getNodeType(Integer projectID) {
		if (projectID == null || projectID==0) {
			throw new ApplicationException(500, INVALID_INPUT_PROJECT_ID_CANNOT_BE_0);
			
		} else {
			int countryCustomerGroupID = appService.getCountryCustomerGroupIDByProjectID(projectID);
			return workOrderPlanDao.getNodeType(countryCustomerGroupID);
		}
	}

	public List<String> getMarketArea(int projectID, String nodeType, String type) {
		if (projectID != 0) {
			int countryCustomerGroupID = appService.getCountryCustomerGroupIDByProjectID(projectID);
			return workOrderPlanDao.getMarketArea(countryCustomerGroupID, nodeType, type);
		} else {
			throw new ApplicationException(500, INVALID_INPUT_PROJECT_ID_CANNOT_BE_0);
		}
	}

	public ResponseEntity<Response<List<String>>> getVendor(int projectID, String marketArea, String nodeType) {
		Response<List<String>> vendor = new Response<>();
		try {
			if (projectID == 0) {
				vendor.addFormError(INVALID_INPUT_PROJECT_ID_CANNOT_BE_0);
			} else {
				int countryCustomerGroupID = appService.getCountryCustomerGroupIDByProjectID(projectID);
				vendor.setResponseData(workOrderPlanDao.getVendor(countryCustomerGroupID, marketArea, nodeType));
				LOG.info("TBL_NETWORK_ELEMENT: getVendor SUCCESS");
			}
		} 
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			vendor.addFormError(e.getMessage());
			return new ResponseEntity<>(vendor,
					org.springframework.http.HttpStatus.OK);
		}
		catch (Exception ex) {
			LOG.info(ex.getMessage());
			vendor.addFormError(ex.getMessage());
			return new ResponseEntity<>(vendor,
					org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(vendor, org.springframework.http.HttpStatus.OK);
	}

	public List<ProjectNodeTypeModel> getNodeNames(int projectID, String elementType, String type) {
		if (elementType.equalsIgnoreCase("Cluster")) {

			return workOrderPlanDao.getClusterNames(projectID, type);
		} else {
			if (type.equalsIgnoreCase("SITE")) {

				return workOrderPlanDao.getNodeNamesForSite(projectID, type);
			}
			if (type.equalsIgnoreCase("SECTOR")) {

				return workOrderPlanDao.getNodeNamesForSector(projectID, type);
			} else {

				return workOrderPlanDao.getNodeNames(projectID, elementType, type);
			}
		}
	}

	public List<ProjectNodeTypeModel> getNodeNamesFilter(int projectID, String elementType, String type, String vendor,
			String market, String tech, String domain, String subDomain) {
		if (elementType.equalsIgnoreCase("Cluster")) {

			return workOrderPlanDao.getClusterNamesFilter(projectID, type, vendor, market, tech, domain, subDomain);
		} else {
			if (type.equalsIgnoreCase("SITE")) {

				return workOrderPlanDao.getNodeNamesForSiteFilter(projectID, type, vendor, market, tech, domain,
						subDomain);
			}
			if (type.equalsIgnoreCase("SECTOR")) {

				return workOrderPlanDao.getNodeNamesForSectorFilter(projectID, type, vendor, market, tech, domain,
						subDomain);
			} else {
				return workOrderPlanDao.getNodeNamesFilter(projectID, elementType, type, vendor, market, tech, domain,
						subDomain);
			}
		}
	}

	public List<ProjectNodeTypeModel> getNodeNamesByFilter(int projectID, String elementType, String type,
			String vendor, String market, String tech, String domain, String subDomain, String term) {

		term = term.replaceAll(".", "[$0]");
		if (elementType.equalsIgnoreCase("Cluster")) {

			return workOrderPlanDao.getClusterNamesByFilter(projectID, type, vendor, market, tech, domain, subDomain,
					term);
		} else {
			return workOrderPlanDao.getNodeNamesByFilter(projectID, elementType, type, vendor, market, tech, domain,
					subDomain, term);

		}
	}

	public Response<Void> getNodeNamesValidate(int projectID, String elementType, String type, String vendor,
			String market, String tech, String domain, String subDomain, String nodeNames) {
		Response<Void> response = new Response<>();
		int nodePresent = 0;
		int nodeNotPresent = 0;
		String nodeAbsent = "";
		String nodepresent = "";
		String msgReturned = "";
		String msgPresent = "nodes validated ";
		String msgAbsent = "nodes not validated";
		String allNodes = "";

		if (elementType.equalsIgnoreCase("Cluster")) {

			List<String> nodesCluster = Arrays.asList(nodeNames.trim().split("\\s*,\\s*"));
			List<String> NodeNamesFilterValidate = workOrderPlanDao.getClusterNamesFilterValidate(projectID, type,
					vendor, market, tech, domain, subDomain, nodesCluster);

			if (NodeNamesFilterValidate.isEmpty() || NodeNamesFilterValidate.size() == 0) {

				nodeAbsent = allNodes + ',' + nodeAbsent;
				nodeAbsent = nodeAbsent.replaceAll(COMMA_DOLLAR, "");
				nodeNotPresent = 1;
			} else {
				for (String node : nodesCluster) {

					if (NodeNamesFilterValidate.contains(node)) {
						nodepresent = node + ',' + nodepresent;
						nodepresent = nodepresent.replaceAll(COMMA_DOLLAR, "");
						nodePresent = 1;
					} else {
						nodeAbsent = node + ',' + nodeAbsent;
						nodeAbsent = nodeAbsent.replaceAll(COMMA_DOLLAR, "");
						nodeNotPresent = 1;
					}

				}
			}

			msgPresent = msgPresent + " " + nodepresent;
			msgAbsent = msgAbsent + " " + nodeAbsent;
			if (nodeNotPresent == 1 && nodePresent == 1) {
				response.addFormError(msgAbsent);
			}
			if (nodeNotPresent == 0 && nodePresent == 1) {
				msgReturned = msgPresent;
			}
			if (nodeNotPresent == 1 && nodePresent == 0) {
				response.addFormError(msgAbsent);
			}
			response.addFormMessage(msgReturned);
			return response;
		} else {
			if (type.equalsIgnoreCase("SITE")) {

				List<String> nodeSite = Arrays.asList(nodeNames.trim().split("\\s*,\\s*"));

				List<String> NodeNamesFilterValidateList = workOrderPlanDao.getNodeNamesForSiteFilterValidate(projectID,
						type, vendor, market, tech, domain, subDomain, nodeSite);

				if (NodeNamesFilterValidateList.isEmpty() || NodeNamesFilterValidateList.size() == 0) {

					nodeAbsent = allNodes + ',' + nodeAbsent;
					nodeAbsent = nodeAbsent.replaceAll(COMMA_DOLLAR, "");
					nodeNotPresent = 1;
				} else {
					for (String node : nodeSite) {

						if (NodeNamesFilterValidateList.contains(node)) {
							nodepresent = node + ',' + nodepresent;
							nodepresent = nodepresent.replaceAll(COMMA_DOLLAR, "");
							nodePresent = 1;
						} else {
							nodeAbsent = node + ',' + nodeAbsent;
							nodeAbsent = nodeAbsent.replaceAll(COMMA_DOLLAR, "");
							nodeNotPresent = 1;
						}

					}
				}

				msgPresent = msgPresent + " " + nodepresent;
				msgAbsent = msgAbsent + " " + nodeAbsent;

				if (nodeNotPresent == 1 && nodePresent == 1) {
					response.addFormError(msgAbsent);
				}
				if (nodeNotPresent == 0 && nodePresent == 1) {
					msgReturned = msgPresent;
				}
				if (nodeNotPresent == 1 && nodePresent == 0) {
					response.addFormError(msgAbsent);
				}

				response.addFormMessage(msgReturned);
				return response;

			}
			if (type.equalsIgnoreCase("SECTOR")) {

				List<String> nodesSector = Arrays.asList(nodeNames.trim().split("\\s*,\\s*"));

				List<String> NodeNamesFilterValidateList = workOrderPlanDao.getNodeNamesForSectorFilterValidate(
						projectID, type, vendor, market, tech, domain, subDomain, nodesSector);

				if (NodeNamesFilterValidateList.isEmpty() || NodeNamesFilterValidateList.size() == 0) {

					nodeAbsent = allNodes + ',' + nodeAbsent;
					nodeAbsent = nodeAbsent.replaceAll(COMMA_DOLLAR, "");
					nodeNotPresent = 1;
				} else {
					for (String node : nodesSector) {

						if (NodeNamesFilterValidateList.contains(node)) {
							nodepresent = node + ',' + nodepresent;
							nodepresent = nodepresent.replaceAll(COMMA_DOLLAR, "");
							nodePresent = 1;
						} else {
							nodeAbsent = node + ',' + nodeAbsent;
							nodeAbsent = nodeAbsent.replaceAll(COMMA_DOLLAR, "");
							nodeNotPresent = 1;
						}
					}
				}

				msgPresent = msgPresent + " " + nodepresent;
				msgAbsent = msgAbsent + " " + nodeAbsent;

				if (nodeNotPresent == 1 && nodePresent == 1) {
					response.addFormError(msgAbsent);
				}
				if (nodeNotPresent == 0 && nodePresent == 1) {
					response.addFormMessage(msgPresent);
				}
				if (nodeNotPresent == 1 && nodePresent == 0) {
					response.addFormError(msgAbsent);
				}
				return response;

			} else {

				List<String> nodes = Arrays.asList(nodeNames.trim().split("\\s*,\\s*"));

				List<String> NodeNamesFilterValidate = workOrderPlanDao.getNodeNamesFilterValidate(projectID,
						elementType, type, vendor, market, tech, domain, subDomain, nodes);

				if (NodeNamesFilterValidate.isEmpty() || NodeNamesFilterValidate.size() == 0) {

					nodeAbsent = allNodes + ',' + nodeAbsent;
					nodeAbsent = nodeAbsent.replaceAll(COMMA_DOLLAR, "");
					nodeNotPresent = 1;
				} else {

					for (String node : nodes) {

						if (NodeNamesFilterValidate.contains(node)) {
							nodepresent = node + ',' + nodepresent;
							nodepresent = nodepresent.replaceAll(COMMA_DOLLAR, "");
							nodePresent = 1;
						} else {
							nodeAbsent = node + ',' + nodeAbsent;
							nodeAbsent = nodeAbsent.replaceAll(COMMA_DOLLAR, "");
							nodeNotPresent = 1;
						}
					}

				}

				msgPresent = msgPresent + " " + nodepresent;
				msgAbsent = msgAbsent + " " + nodeAbsent;

				if (nodeNotPresent == 1 && nodePresent == 1) {
					response.addFormError(msgAbsent);
				}
				if (nodeNotPresent == 0 && nodePresent == 1) {
					response.addFormMessage(msgPresent);
				}
				if (nodeNotPresent == 1 && nodePresent == 0) {
					response.addFormError(msgAbsent);
				}

			}

		}
		return response;
	}

	public ResponseEntity<Response<List<ProjectModel>>> getProjectBySignum(String signum, String marketArea, String role) {
		Response<List<ProjectModel>> response=new Response<List<ProjectModel>>();
		try {
			response.setResponseData(workOrderPlanDao.getProjectBySignum(signum, marketArea, role));
		}catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<List<ProjectModel>>>(response, HttpStatus.OK);
		}catch (Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<List<ProjectModel>>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response<List<ProjectModel>>>(response, HttpStatus.OK);
	}

	public MailModel getWoMailNotificationDetails(int woID) {
		return workOrderPlanDao.getWoMailNotificationDetails(woID);
	}

	public void sendMailNotification(String senderSignumID, String body, String receiverSignumID) {
		try {
			workOrderPlanDao.SendMailNotification(senderSignumID, body, receiverSignumID);
		} catch (Exception e) {
			LOG.error("Error Sending mail");
			e.printStackTrace();
		}

	}

	public List<Integer> getWorkFlowVersionList(int projectID, int subActivityID) {
		return workOrderPlanDao.getWorkFlowVersionList(projectID, subActivityID);
	}

	public void updateWOWFVersion(int woID, int wfVersion, int oldDefID) {
		workOrderPlanDao.updateWOWFVersion(woID, wfVersion, oldDefID);
		flowChartDAO.updateWorkOrderForAutoSense(oldDefID);
	}

	public int getLatestWorkFlowVersion(int projectID, int subActivityID) {
		return workOrderPlanDao.getLatestWorkFlowVersion(projectID, subActivityID);
	}

	public List<Map<String, Object>> getDomainDetailsByWOPlanID(int woPlanId) {
		return workOrderPlanDao.getDomainDetailsByWOPlanID(woPlanId);
	}

	public List<Map<String, Object>> getPlanDataByWOPlanID(int woPlanId) {
		return workOrderPlanDao.getPlanDataByWOPlanID(woPlanId);
	}

	public List<Map<String, Object>> getNodeTypeByWOPlanID(int woPlanId) {
		return workOrderPlanDao.getNodeTypeByWOPlanID(woPlanId);
	}

	public List<Map<String, Object>> getNodeNamesByWOPlanID(int woPlanId) {
		return workOrderPlanDao.getNodeNamesByWOPlanID(woPlanId);
	}

	public List<Map<String, Object>> getAssignedUsersByWOPlanID(int woPlanId) {
		return workOrderPlanDao.getAssignedUsersByWOPlanID(woPlanId);
	}

	public List<Map<String, Object>> getCheckBoxData(int woPlanId) {
		return workOrderPlanDao.getCheckBoxData(woPlanId);
	}

	@Transactional("transactionManager")
	public boolean massUpdateWorkOrder(TransferWorkOrderModel transferWorkOrderModel) {

		String senderID = transferWorkOrderModel.getSenderID();
		for (int woid : transferWorkOrderModel.getWoID()) {
			List<WorkOrderModel> updatedWoLIst = new ArrayList<WorkOrderModel>();

			List<WorkOrderModel> woDetailsList = workOrderPlanDao.getWorkOrderDetails(woid);
			if (woDetailsList.size() > 0) {
				WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetails(woid).get(0);
				updatedWoLIst.add(woDetails);
				if (AppConstants.WO_STATUS_REPOENED.equalsIgnoreCase(woDetails.getStatus())
						|| AppConstants.WO_STATUS_ASSIGNED.equalsIgnoreCase(woDetails.getStatus())
						|| AppConstants.WO_STATUS_DEFERRED.equalsIgnoreCase(woDetails.getStatus())) {

					Date newStartDate = null;
					Date endStartDate = null;
					if (transferWorkOrderModel.getStartDate() != null) {

						Calendar c = Calendar.getInstance();
						c.setTime(woDetails.getPlannedStartDate());
						c.set(Calendar.HOUR_OF_DAY, 0);
						c.set(Calendar.MINUTE, 0);
						c.set(Calendar.SECOND, 0);

						long diff = transferWorkOrderModel.getStartDate().getTime() - c.getTime().getTime();
						int diffDays = (int) (diff / (24 * 60 * 60 * 1000));

						Calendar startCal = Calendar.getInstance();
						startCal.setTime(woDetails.getPlannedStartDate());
						startCal.add(Calendar.DAY_OF_MONTH, diffDays);

						Calendar endCal = Calendar.getInstance();
						endCal.setTime(woDetails.getPlannedEndDate());
						endCal.add(Calendar.DAY_OF_MONTH, diffDays);
						newStartDate = startCal.getTime();
						endStartDate = endCal.getTime();
					}
					transferWorkOrderModel.setSenderID(woDetails.getSignumID());

					workOrderPlanDao.massUpdateWorkOrder(woid, transferWorkOrderModel.getSenderID(),
							transferWorkOrderModel.getReceiverID(), newStartDate, endStartDate);
					if (AppConstants.WO_STATUS_DEFERRED.equalsIgnoreCase(woDetails.getStatus())) {
						workOrderPlanDao.updateWorkOrderStatus(woid, transferWorkOrderModel.getSenderID(),
								AppConstants.WO_STATUS_ASSIGNED);
					}
					if (transferWorkOrderModel.getSenderID() != null) {
						workOrderPlanDao.updateTransferWOLOG(woid, transferWorkOrderModel.getReceiverID(),
								transferWorkOrderModel.getSenderID());
					}
				} else {
					throw new ApplicationException(500,
							"Cannot Mass Update Work Order as the Status of the WorkOrder is " + woDetails.getStatus());
				}
			} else {
				throw new ApplicationException(500, WORK_ORDER_DOESN_T_EXISTS);
			}
			Map<String, Object> placeHolder = enrichMailMassUpdate(transferWorkOrderModel, updatedWoLIst, senderID);
			placeHolder.put(AppConstants.PAGE_LINK_STRING, SP);
			emailService.sendMail(AppConstants.NOTIFICATION_MASS_UP_WO, placeHolder);
		}
		return true;
	}

	public Map<String, Object> enrichMailMassUpdate(TransferWorkOrderModel transferWorkOrderModel,
			List<WorkOrderModel> updatedWoList, String senderID) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			data.put("data", data);
			data.put("w", updatedWoList);
			data.put("r", transferWorkOrderModel);
			data.put(AppConstants.CURRENT_USER, senderID);
			if (transferWorkOrderModel.getReceiverID() != null) {
				EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(transferWorkOrderModel.getReceiverID());
				data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, eDetails.getEmployeeEmailId());
				EmployeeModel eDetails1 = activityMasterDAO
						.getEmployeeBySignum(transferWorkOrderModel.getLogedInSignum());
				data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, eDetails1.getEmployeeEmailId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;

	}

	public List<Map<String, Object>> getWfListForPlan(Integer projectId, Integer sourceId) {
		if (sourceId == 1) {
			return flowChartDAO.getWfListForPlan(projectId, sourceId);
		} else {
			return flowChartDAO.getWfListForPlanForExternalSource(projectId, sourceId);
		}
	}

	public List<Map<String, Object>> getSourcesForMapping() {
		return workOrderPlanDao.getSourcesForMapping();
	}

	public List<Map<String, Object>> getSourcesForPlan(int projectId) {
		return workOrderPlanDao.getSourcesForPlan(projectId);
	}

	public List<ExecutionPlanModel> searchExecutionPlans(Integer subactivityId, Integer workFlowVersionNo) {
		return workOrderPlanDao.searchExecutionPlans(subactivityId, workFlowVersionNo);
	}

	private static final String[] EXCEL_MIME_TYPES = {
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" };
	

	public Response<Void> uploadFileForWOCreation(int projectID, String createdBy, MultipartFile file)
			throws Exception {
		Response<Void> returnData = new Response<Void>();
		BulkXlsParsedModel response = null;
		Map<String, Object> responseData = new HashMap<>();
		try {
			if (Arrays.asList(EXCEL_MIME_TYPES).contains(file.getContentType())) {
				byte[] fileData = file.getBytes();
				response = rpaService.parseXls(fileData, projectID,createdBy);
				List<BulkWorkOrderCreationModel> processedData = response.getParsedData();
				for (BulkWorkOrderCreationModel buldWoInfo : processedData) {
					
					if(StringUtils.isNoneBlank(buldWoInfo.getAssignTO())) {
						if(StringUtils.equalsIgnoreCase(buldWoInfo.getAssignTO(), AppConstants.CH_ZERO)) {
							buldWoInfo.setAssignTO(AppConstants.NULL);
						}else {
							buldWoInfo.setAssignTO(buldWoInfo.getAssignTO().trim());
						}	
					}
					
					buldWoInfo.setUploadedBy(createdBy);
					if (StringUtils.isBlank(buldWoInfo.getExternalSourceName())
							|| StringUtils.equalsIgnoreCase(AppConstants.ISF, buldWoInfo.getExternalSourceName())) {
						buldWoInfo.setCreatedBy(createdBy);
						buldWoInfo.setSource(AppConstants.ISF);
					} else {
						buldWoInfo.setCreatedBy(buldWoInfo.getExternalSourceName());
						buldWoInfo.setSource(buldWoInfo.getExternalSourceName());
					}
					responseData = rpaService.createAutomatedWorkOrder(buldWoInfo);
				}
				if (responseData.containsKey(AppConstants.SUCCESS)) {

					returnData.addFormMessage(
							"File successfully updated. You will be notified once the work orders are created");
				}
				if (responseData.containsKey(AppConstants.FAILED)) {

					returnData.addFormError("Invalid File!! Please check number of  columns before uploading ");
				}

			} else {
				// throw new ApplicationException(500, "Invalid File Type ");
				returnData.addFormError("Invalid File Type!!,Please upload only xslx file");
			}
		} catch (Exception e) {
			returnData.addFormError(e.getMessage());
		}
		return returnData;
	}

	@Transactional("transactionManager")
	public Response<Integer> saveExecutionPlan(ExecutionPlanModel executionPlanModel) {
		Response<Integer> response = new Response<Integer>();

		List<ExecutionPlanDetail> masterExecPlanDetails = new ArrayList<>(executionPlanModel.getTasks());
		Map<String, Object> data = erisiteManagmentDAO.getExternalGroup(executionPlanModel.getPlanSourceid());
		if ("ERISITE".equals(data.get("externalGroup")) && !executionPlanModel.isDuplicateExecutionPlan()) {
			int woCount = erisiteManagmentDAO.getNotClosedWOsByName(executionPlanModel.getPlanExternalReference() + "_"
					+ executionPlanModel.getTasks().get(0).getExecutionPlanId());
			if (woCount > 0) {
				response.addFormError("WO's on previous Deliverable plan are active, count: " + woCount
						+ ". New Deliverable plan will get updated with existing WO's");
				response.setFormWarningCount(1);
				return response;
			}
		}

		// validations
		Date earliestStart = null;
		Map<Long, ExecutionPlanDetail> tasksMap = new HashMap<>();
		for (ExecutionPlanDetail execPlanDet : masterExecPlanDetails) {
			if (earliestStart == null || earliestStart.after(execPlanDet.getStart_date())) {
				earliestStart = execPlanDet.getStart_date();
			}

			tasksMap.put(execPlanDet.getId(), execPlanDet);
		}

		for (LinkModel link : executionPlanModel.getLinks()) {
			// if target is starting before end of source (ie child is starting before
			// parent ends)
			if (tasksMap.get(link.getTarget()) != null && tasksMap.get(link.getSource()) != null) {
				if (tasksMap.get(link.getTarget()).getStart_date()
						.before(tasksMap.get(link.getSource()).getEnd_date())) {
					response.addFormError(
							"No child subactivity should start before the end date of parent subactivity");
				}
			}
		}

		if (response.isValidationFailed()) {
			return response;
		}

		if (executionPlanModel.getExecutionPlanId() == 0) {
			// new executionPlan, so need to insert data

			workOrderPlanDao.setInactiveExistingExecPlan(executionPlanModel.getProjectId(),
					executionPlanModel.getPlanName());
			workOrderPlanDao.saveExecutionPlan(executionPlanModel);
			int executionPlanIdAndInstanceId = isfCustomIdInsert
					.generateCustomId(executionPlanModel.getExecutionPlanId());
			executionPlanModel.setExecutionPlanId(executionPlanIdAndInstanceId);
			workOrderPlanDao.saveExecutionPlanDetails(executionPlanModel);

			if ("ERISITE".equals(data.get("externalGroup"))) {
				executionPlanModel.setSubactivityId(executionPlanModel.getTasks().get(0).getSubActivityID());
				erisiteManagmentDAO.updateExecutionPlanWoCreation(executionPlanModel);

				// update WO names
				if (executionPlanModel.isDuplicateExecutionPlan()) {
					erisiteManagmentDAO.updateWoName(executionPlanModel.getCurrentUser(),
							executionPlanModel.getProjectId(), executionPlanModel.getExecutionPlanId(),
							executionPlanModel.getPlanExternalReference() + "_"
									+ executionPlanModel.getTasks().get(0).getExecutionPlanId(),
							executionPlanModel.getTasks().get(0).getExecutionPlanId());
				}

				Map<String, Object> configData = erisiteManagmentDAO.getActivityConfig(executionPlanModel);
				if (configData == null) {
					erisiteManagmentDAO.insertActivityConfig(executionPlanModel);
				} else {
					erisiteManagmentDAO.updateActivityConfig(executionPlanModel);
				}
			}
		} else {
			// existing plan. need to update detail steps

			List<ExecutionPlanDetail> existingPlanDetails = workOrderPlanDao
					.getExecutionPlanDetailsByExecutionPlanId(executionPlanModel.getExecutionPlanId());

			workOrderPlanDao.updateExecutionPlan(executionPlanModel);
			workOrderPlanDao.updateExecutionPlanDetails(executionPlanModel);

			// insert newly added tasks
			List<ExecutionPlanDetail> newlyAddedTasks = new ArrayList<>(masterExecPlanDetails);
			newlyAddedTasks.removeAll(existingPlanDetails);
			if (newlyAddedTasks.size() > 0) {
				executionPlanModel.setTasks(newlyAddedTasks);
				workOrderPlanDao.saveExecutionPlanDetails(executionPlanModel);
			}

			// delete removed tasks
			List<ExecutionPlanDetail> deletedTasks = new ArrayList<>(existingPlanDetails);
			deletedTasks.removeAll(masterExecPlanDetails);
			if (deletedTasks.size() > 0) {
				executionPlanModel.setTasks(deletedTasks);
				workOrderPlanDao.deleteExecutionPlanDetails(executionPlanModel);
			}
		}

		response.setResponseData(executionPlanModel.getExecutionPlanId());
		return response;

	}

	/**
	 * 
	 * @param projectId
	 * @return
	 */
	public List<ExecutionPlanModel> getExecutionPlans(int projectId) {

		return workOrderPlanDao.getExecutionPlans(projectId);
	}

	public List<ExecutionPlanDetail> getExecutionPlanDetailsByExecutionPlanId(int executionPlanId) {
		return workOrderPlanDao.getExecutionPlanDetailsByExecutionPlanId(executionPlanId);
	}

	public void updateExecutionPlanStatus(int executionPlanId, boolean isActive, String userName) {
		workOrderPlanDao.updateExecutionPlanStatus(executionPlanId, isActive, userName);
	}

	public List<WorkOrderPlanModel> checkProjectEditByProjID(int projectId) {
		return workOrderPlanDao.checkProjectEditByProjID(projectId);
	}

	public void saveExecutionPlanFlow(ExecutionPlanFlow executionPlanFlow) {
		workOrderPlanDao.saveExecutionPlanFlow(executionPlanFlow);
	}

	public void getExecutionPlanFlow(ExecutionPlanFlow executionPlanFlow) {
		workOrderPlanDao.saveExecutionPlanFlow(executionPlanFlow);
	}

	public boolean isPlanInExecution(long executionPlanId) {
		List<String> list = workOrderPlanDao.isPlanInExecution(executionPlanId);

		if (list == null || list.size() == 0) {
			return false;
		}

		return true;

	}

	public ExecutionPlanModel getExecutionPlandDetilsbyWoPlanId(long woId) {
		return workOrderPlanDao.getExecutionPlandDetilsbyPlanWoId(woId);

	}

	
	/**
	 * 
	 * @param signum
	 * @return List<LinkedHashMap<String, Object>>
	 */
	public ResponseEntity<Response<List<LinkedHashMap<String, Object>>>> getInprogressTask(String signum) {
		Response<List<LinkedHashMap<String, Object>>> response =new Response<List<LinkedHashMap<String,Object>>>(); 
try {		
		List<LinkedHashMap<String, Object>> result = workOrderPlanDao.getInprogressTask(signum);
		int taskId = 0;
		StringBuilder taskIdNo = new StringBuilder();
		StringBuilder woIDNo = new StringBuilder();
		int woID = 0;
		StringBuilder stepIDNo = new StringBuilder();
		String stepID = AppConstants.CHAR_ZERO;
		WorkFlowDetailsModel workFlowDetails= null;
		List<LinkedHashMap<String, Object>> resultListWithServerTime = new ArrayList<LinkedHashMap<String, Object>>();
		if (CollectionUtils.isNotEmpty(result)) {
			for (LinkedHashMap<String, Object> resultMap : result) {
				
				resultMap.put("NodeNames", resultMap.get("NodeNames").toString().replaceAll(StringUtils.SPACE, StringUtils.EMPTY).replace(AppConstants.SQUARE_OPEN_BRACKET, StringUtils.EMPTY).replace(AppConstants.SQUARE_CLOSE_BRACKET, StringUtils.EMPTY));
				
				if (woID!=(int)resultMap.get(WO_ID) || taskId!= (int)resultMap.get(TASK_ID)) {

					taskId = (int) resultMap.get(TASK_ID);
					woID = (int) resultMap.get(WO_ID);
					stepID = (String) resultMap.get("FlowChartStepID");
					
					taskIdNo.append(taskId).append(AppConstants.CSV_CHAR_COMMA);		
					woIDNo.append(woID).append(AppConstants.CSV_CHAR_COMMA);
					stepIDNo=stepIDNo.append(AppConstants.SINGLE_QUOTE).append(stepID).append(AppConstants.SINGLE_QUOTE).append(AppConstants.CSV_CHAR_COMMA);
					
					workFlowDetails = (WorkFlowDetailsModel) wOExecutionDAO.getWorkFlowNameForWoID(woID);
					resultMap.put("workFlow",new StringBuilder()
							.append(workFlowDetails.getWfid()).append(AppConstants.FORWARD_SLASH)
							.append(workFlowDetails.getWorkFlowName()).append(AppConstants.FORWARD_SLASH)
							.append(workFlowDetails.getVersionNumber()).toString());
					
					// set Rule type x
					//RuleModel rules = autoSenseService.getRulesByStepIDNew(stepID,Integer.parseInt(workFlowDetails.getFlowChartDefId()));
					Map<String,Object> rules=autoSenseService.getRulesByStepIDNew(stepID,Integer.parseInt(workFlowDetails.getFlowChartDefId()));
					
					resultMap.put(START_RULE, false);
					resultMap.put(STOP_RULE, false);
					
					if(rules!=null) {
//						if(StringUtils.equalsIgnoreCase(rules.getTaskActionName(), AppConstants.START)) {
//							resultMap.put(START_RULE, true);
//						}
//						if(StringUtils.equalsIgnoreCase(rules.getTaskActionName(), AppConstants.STOP)) {
//							resultMap.put(STOP_RULE, true);
//						}
						if(rules.containsKey(AppConstants.START)) {
							resultMap.put(START_RULE, true);
						}
						if(rules.containsKey(AppConstants.STOP)) {
							resultMap.put(STOP_RULE, true);
						}
					}
					
					
					if (StringUtils.equalsIgnoreCase(resultMap.get("ExecutionType").toString(), AppConstants.AUTOMATIC)) {
						TblRpaDeployedBot botDetails = botStoreService.getBotDetail(Integer.parseInt(resultMap.get("RpaID").toString()));
						resultMap.put("botType", botDetails.getBotlanguage());
					}
					
					getNextStepInfo(stepID, resultMap, rules);
					
					resultListWithServerTime.add(resultMap);
				}
			}
			String taskIde=taskIdNo.toString().replaceAll(COMMA_DOLLAR, StringUtils.EMPTY);
			String woIDe=woIDNo.toString().replaceAll(COMMA_DOLLAR, StringUtils.EMPTY);
			String stepIDe=stepIDNo.toString().replaceAll(COMMA_DOLLAR, StringUtils.EMPTY);
			
			List<ServerTimeModel> serverTimeList = workOrderPlanDao.getServerTimeByTaskID(taskIde,
					stepIDe, woIDe);

			for (ServerTimeModel serverTime : serverTimeList) {
				for (Map<String, Object> resultMap : resultListWithServerTime) {
					
					if (serverTime.getTaskId()==(int)resultMap.get(TASK_ID) && serverTime.getWoID() == (int)resultMap.get(WO_ID)) {
						resultMap.put(SERVER_TIME,serverTime.getServerTime());
						long stepTime = (long) (serverTime.getServerTime());
						Double totaltime = workOrderPlanDao.getWoTotalTime(resultMap.get(WO_ID).toString());
						resultMap.put("totalEffort", (totaltime == null) ? stepTime : totaltime);
					//	resultMap.put("totalEffort",serverTime.getTotalEffort() == null ? stepTime : serverTime.getTotalEffort());
					}
				}
			}
			response.setResponseData(resultListWithServerTime);
					
		}
		else {
			throw new ApplicationException(200, "No Data found for this Signum!");
		}
	}catch (ApplicationException e) {
		LOG.error(e.getMessage());
		response.addFormError(e.getMessage());
		return new ResponseEntity<Response<List<LinkedHashMap<String, Object>>>>(response, HttpStatus.OK);
	}
	catch (Exception e) {
		LOG.error(e.getMessage());
		response.addFormError(e.getMessage());
		return new ResponseEntity<Response<List<LinkedHashMap<String, Object>>>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<Response<List<LinkedHashMap<String, Object>>>>(response, HttpStatus.OK);
}

	public Response<List<InProgressTaskModel>> getInprogressTaskBySignum(String signum) {
		Response<List<InProgressTaskModel>> response = new Response<List<InProgressTaskModel>>();
		try {
			
			List<InProgressTaskModel> result = workOrderPlanDao.getInprogressTaskBySignum(signum);
			LOG.info("Signum: "+signum);
			LOG.info("result: "+result.size());
			//List<InProgressTaskModel> resultListWithServerTime = new ArrayList<>();
			if (!result.isEmpty()) {
				for (InProgressTaskModel mapResult : result) {
					// List<Object> nextDataValues = new ArrayList<>();
					Map<String, Object> rules = autoSenseService.getRulesByStepID(mapResult.getFlowChartStepID(),
							mapResult.getFlowChartDefID());

					if (rules.containsKey(AppConstants.START)) {
						mapResult.setStartRule(true);
					}
					if (rules.containsKey(AppConstants.STOP)) {
						mapResult.setStopRule(true);
					}
					if(mapResult.getTotalEffort()==0.0) {
						mapResult.setTotalEffort((long)mapResult.getServerTime());
					}

					// Get next step info for Inprogress task, will set the data in "mapResult"
					getNextStepInfoForSignumId(mapResult);

					// resultListWithServerTime.add(mapResult);

				}

				response.setResponseData(result);
				}
			else {
				response.setResponseData(result);
			}
			
		} catch (Exception ex) {
			response.addFormError(ex.getMessage());
			ex.printStackTrace();
		}
		
		return response;
	}
	

	private void getNextStepInfoForSignumId(InProgressTaskModel mapResult) {
		InProgressNextStepModal nextStepInfo;
		if(mapResult.getExecutionType().equalsIgnoreCase(AppConstants.MANUAL_DISABLED)) {
			String latestConpletedEnabledStepID =workOrderPlanDao.getLatestConpletedEnabledStep(mapResult.getWoID());
			nextStepInfo= workOrderPlanDao.getNextStepInfoWithProficiency(latestConpletedEnabledStepID,
					mapResult.getFlowChartDefID(),mapResult.getProficiencyType().getProficiencyID());
		}
		else {
			if(workOrderPlanDao.checkIfNextStepDisabled(mapResult.getFlowChartStepID(), 
					mapResult.getFlowChartDefID(), mapResult.getProficiencyType().getProficiencyLevel())) {
				StepDetailsModel stepDetailsModel= wOExecutionDAO.getDisabledStepDetails(mapResult.getFlowChartDefID(), mapResult.getWoID());
				nextStepInfo=new InProgressNextStepModal();
				nextStepInfo.setBookingID(stepDetailsModel.getBookingId());
				nextStepInfo.setBookingStatus(stepDetailsModel.getStatus());
				nextStepInfo.setNextExecutionType(stepDetailsModel.getExecutionType());
				nextStepInfo.setNextStepID(stepDetailsModel.getFlowChartStepId());
				nextStepInfo.setNextStepName(stepDetailsModel.getStepName());
				nextStepInfo.setNextStepType(stepDetailsModel.getStepType());
				nextStepInfo.setNextTaskID(stepDetailsModel.getTaskID());
			}
			else {
				nextStepInfo= workOrderPlanDao.getNextStepInfoForSignumId(mapResult.getFlowChartStepID(),mapResult.getFlowChartDefID());
				Map<String,Object> rules = autoSenseService.getRulesByStepID(nextStepInfo.getNextStepID(),(int) mapResult.getFlowChartDefID());
				if(rules.containsKey(AppConstants.START)) {
					nextStepInfo.setStartRule(true);
				}
				if(rules.containsKey(AppConstants.STOP)) {
					nextStepInfo.setStopRule(true);
				}
				}
			}
			
		
		List<InProgressNextStepModal> inProgressData=new ArrayList<InProgressNextStepModal>();
		inProgressData.add(nextStepInfo);
		mapResult.setNextSteps(inProgressData);
	}

	/**
	 * @modified ekmbuma
	 * @purpose To get next step info for Inprogress task
	 * 
	 * @param stepID
	 * @param mapResult
	 */
	private void getNextStepInfo(String stepID, LinkedHashMap<String, Object> resultMap, Map<String,Object> rules) {
		
		List<NextSteps> nextStepInfo = workOrderPlanDao.getNextStepData(stepID,(int) resultMap.get("FlowChartDefID"));
		
		if(CollectionUtils.isNotEmpty(nextStepInfo)) {
			if(rules!=null) {
//				if(StringUtils.equalsIgnoreCase(rules.getTaskActionName(), AppConstants.START)) {
//					nextStepInfo.get(0).setStartRule(true);
//				}
//				if(StringUtils.equalsIgnoreCase(rules.getTaskActionName(), AppConstants.STOP)) {
//					nextStepInfo.get(0).setStopRule(true);
//				}
				if(rules.containsKey(AppConstants.START)) {
					nextStepInfo.get(0).setStartRule(true);
				}
				if(rules.containsKey(AppConstants.STOP)) {
					nextStepInfo.get(0).setStopRule(true);
				}
			}else {
				nextStepInfo.get(0).setStartRule(false);
				nextStepInfo.get(0).setStopRule(false);
			}

		}
		resultMap.put("NextSteps",nextStepInfo);
	}

	private String removeExtraCommaValue(String data) {
		String commaValue = data;
		if (commaValue.endsWith(AppConstants.CSV_CHAR_COMMA)) {
			commaValue = commaValue.substring(0, commaValue.length() - 1);
		}
		return commaValue;
	}

	public List<LinkedHashMap<String, Object>> getBookingDetails(int WoID, String SignumID) {
		return workOrderPlanDao.getBookingDetails(WoID, SignumID);

	}
	@Transactional("transactionManager")
	public List<Map<String, Object>> getBotIDByWFSignum(String signum) {
		return workOrderPlanDao.getBotIDByWFSignum(signum);
	}

	public List<Map<String, Object>> getWoIDByProjectID(String projectID, String subactivityID,
			String subActivityFlowChartDefID) {
		return workOrderPlanDao.getWoIDByProjectID(projectID, subactivityID, subActivityFlowChartDefID);
	}

	public String getWoStartDate(int getwOID) {
		return workOrderPlanDao.getWoStartDate(getwOID);
	}

	public void updateWoStartDateByCurrentDate(int getwOID) {
		workOrderPlanDao.updateWoStartDateByCurrentDate(getwOID);

	}

	public void updateWoStartDates(int getwOID, String startDate) {
		workOrderPlanDao.updateWoStartDates(getwOID, startDate);

	}

	public List<Map<String, Object>> getWorkOrderDetailsByName(String woName, String signum) {
		return workOrderPlanDao.getWorkOrderDetailsByName(woName, signum);

	}

	public List<String> getWoMailNotificationDetailsForDAC(int woID) {
		return workOrderPlanDao.getWoMailNotificationDetailsForDAC(woID);

	}

	public List<Map<String, Object>> getLatestVersionOfWfBySubactivityID(String projectID, String subactivityID,
			String subActivityFlowChartDefID, String workFlowName, int wfid) {
		return workOrderPlanDao.getLatestVersionOfWfBySubactivityID(projectID, subactivityID, subActivityFlowChartDefID,
				workFlowName, wfid);
	}
	//@Transactional("transactionManager")
	public void deleteWorkOrderList(DeleteWOListModel deleteWOListModel) {
		List<Integer> woid = deleteWOListModel.getWoID();
		int chunkedListLength=0;
		int start=0;
		int end=0;
		Boolean checkStatus=false;
		while(chunkedListLength < woid.size()) {
			
			start = (chunkedListLength)%woid.size();
			end = Math.min(start+2000, woid.size());
			
			checkStatus = workOrderPlanDao.checkNotStartedStatusOfWorkOrder1(woid.subList(start, end));
			if(checkStatus==false)
				break;
			chunkedListLength=chunkedListLength+2000;
		}
		chunkedListLength=0;
		start=0;
		end=0;
			if (checkStatus) {
				while(chunkedListLength < woid.size()) {
					start = (chunkedListLength)%woid.size();
					end = Math.min(start+2000, woid.size());
					deleteWOListModel.setWoID(woid.subList(start, end));
					
				/*
				 * List<Integer> woplanlist = workOrderPlanDao.getWOPlanList(woid.subList(start,
				 * end)); if(!woplanlist.isEmpty()) {
				 * workOrderPlanDao.inactiveWOPlanList(woplanlist); }
				 */
					
					
					
					workOrderPlanDao.deleteWONodes1(deleteWOListModel);
					workOrderPlanDao.deleteWorkOrder1(deleteWOListModel);
					List<Integer> woplanlist = workOrderPlanDao.getWOPlanList(woid);
					for (int woplan : woplanlist) {
						Boolean checkplan = workOrderPlanDao.checkPlanStatus(woplan);
						if (!checkplan) {
							workOrderPlanDao.inactiveWOPlan(woplan);
						}
					}
					chunkedListLength=chunkedListLength+2000;
				}
				
			} else {
				throw new ApplicationException(500, "Work Order already Started !!");
			}
		
	}

	public EmployeeBasicDetails getEmpInfo(String lastModifiedBy) {
		return workOrderPlanDao.getEmpInfo(lastModifiedBy);
	}

	@Transactional("transactionManager")
	public Response<Integer> saveDeliverablePlan(ExecutionPlanModel executionPlanModel) {
		Integer scopeId = executionPlanModel.getTasks().get(0).getScopeId();
		if (executionPlanModel.getPlanSourceid() == null) {
			executionPlanModel.setPlanSourceid("" + 1);
		}
		Response<Integer> res = saveExecutionPlan(executionPlanModel);

		if (!res.isValidationFailed()) {
			workOrderPlanDao.deleteDeliverablePlanMapping(scopeId);
			workOrderPlanDao.addDeliverablePlanMapping(executionPlanModel.getExecutionPlanId(), scopeId,
					executionPlanModel.getCurrentUser());
		}
		return res;
	}

	public Response<Void> insertOutputFileWO(WorkOrderOutputFileModel workOrderOutputFileModel) {
		Response<Void> response = new Response<>();
		Map<String, Object> map = new HashMap<>();
		if (CollectionUtils.isNotEmpty(workOrderOutputFileModel.getFile())) {
			for (int i = 0; i < workOrderOutputFileModel.getFile().size(); i++) {
				map = activityMasterService.isValidTransactionalData(
						workOrderOutputFileModel.getFile().get(i).getOutputUrl(), workOrderOutputFileModel.getProjectID());
				if (!(boolean) map.get(AppConstants.RESULT)) {
					response.addFormError(map.get(AppConstants.MESSAGE).toString());
					break;
				}
			}
			if ((boolean) map.get(AppConstants.RESULT)) {
			List<String> workOrder = new ArrayList<>();
			workOrder = workOrderPlanDao.checkNameIsPresentOrNot(workOrderOutputFileModel.getFile(),
					workOrderOutputFileModel.getWoid());
			if (workOrder.isEmpty()) {
				workOrderPlanDao.insertOutputFileWO(workOrderOutputFileModel);
					response.addFormMessage("Successfully inserted!!");
			} else {
					response.addFormError("This file name is already present. Please Change the File Name !!");
			}
				
			}
		} else {
			List<String> workOrder = new ArrayList<>();
			workOrder = workOrderPlanDao.checkNameIsPresentOrNot(workOrderOutputFileModel.getFile(),
					workOrderOutputFileModel.getWoid());
			if (workOrder.isEmpty()) {
				workOrderPlanDao.insertOutputFileWO(workOrderOutputFileModel);
				response.addFormMessage("Successfully inserted!!");
			} else {
				response.addFormError("This file name is already present. Please Change the File Name !!");
		}
		}

		return response;
		
	}

	public ResponseEntity<Response<List<WOOutputFileResponseModel>>> getWorkOrderOutputFile(int woid) {
		Response<List<WOOutputFileResponseModel>> response=new Response();
		try {
		response.setResponseData(getWOOutputFile(woid));
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<List<WOOutputFileResponseModel>>>(response,HttpStatus.OK);
		}
		catch(Exception ex) {
			response.addFormError(ex.getMessage());
			return new ResponseEntity<Response<List<WOOutputFileResponseModel>>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response<List<WOOutputFileResponseModel>>>(response,HttpStatus.OK);
	}

	private List<WOOutputFileResponseModel> getWOOutputFile(int woid) {
		return workOrderPlanDao.getWOOutputFile(woid);
	}

	public ResponseEntity<Response<List<WOInputFileModel>>> getWorkOrderInputFile(int woid) {
		Response<List<WOInputFileModel>> response=new Response();
		try {
		response.setResponseData(getWOInputFile(woid));
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<List<WOInputFileModel>>>(response,HttpStatus.OK);
		}
		catch(Exception ex) {
			response.addFormError(ex.getMessage());
			return new ResponseEntity<Response<List<WOInputFileModel>>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response<List<WOInputFileModel>>>(response,HttpStatus.OK);
	}

	private List<WOInputFileModel> getWOInputFile(int woid) {
		return workOrderPlanDao.getWOInputFile(woid);
	}

	public int getCountOfUnassignedWOByWOPLAN(int woplanid) {
		return workOrderPlanDao.getCountOfUnassignedWOByWOPLAN(woplanid);
	}

	public static final String SUB_ACTIVITY_ID = "SubActivityID";
	public static final String ACTIVITY_SUB_ACTIVITY_NAME = "ActivitySubActivityName";
	public static final String ACTIVITY_SCOPE_ID = "ActivityScopeID";

	public ExecutionPlanModel getDeliverablePlan(int scopeId) {
		Integer executionPlanId = workOrderPlanDao.getExecutionPlanIdByScopeId(scopeId);
		ExecutionPlanModel execPlan = null;
		if (executionPlanId == null) {
			List<ExecutionPlanDetail> exePlanDetail = new ArrayList<>();
			List<Map<String, Object>> subActivities = projectScopeDao.getActivtiySubActivityByScope(scopeId);
			for (Map<String, Object> subActivity : subActivities) {
				ExecutionPlanDetail executionPlanDetail = new ExecutionPlanDetail();
				executionPlanDetail.setSubActivityID((Integer) subActivity.get(SUB_ACTIVITY_ID));
				executionPlanDetail.setSubActivityDetails((String) subActivity.get(ACTIVITY_SUB_ACTIVITY_NAME));
				executionPlanDetail.setActivityScopeId((Integer) subActivity.get(ACTIVITY_SCOPE_ID));
				executionPlanDetail.setStart_date(new Date());
				exePlanDetail.add(executionPlanDetail);
			}
			execPlan = new ExecutionPlanModel();
			execPlan.setTasks(exePlanDetail);
		} else {
			List<ExecutionPlanDetail> planDetails = workOrderPlanDao
					.getExecutionPlanDetailsByExecutionPlanId(executionPlanId);
			List<Map<String, Object>> subActivities = projectScopeDao.getActivtiySubActivityByScope(scopeId);
			for (Map<String, Object> subActivity : subActivities) {
				Integer subActivityId = (Integer) subActivity.get(SUB_ACTIVITY_ID);
				Boolean flag = true;
				for (ExecutionPlanDetail planDetail : planDetails) {
					if (planDetail.getSubActivityID() == subActivityId) {
						flag = false;
					}
				}
				if (flag) {
					ExecutionPlanDetail executionPlanDetail = new ExecutionPlanDetail();
					executionPlanDetail.setSubActivityID((Integer) subActivity.get(SUB_ACTIVITY_ID));
					executionPlanDetail.setSubActivityDetails((String) subActivity.get(ACTIVITY_SUB_ACTIVITY_NAME));
					executionPlanDetail.setStart_date(new Date());
					executionPlanDetail.setActivityScopeId((Integer) subActivity.get(ACTIVITY_SCOPE_ID));
					planDetails.add(executionPlanDetail);
				}
			}
			execPlan = workOrderPlanDao.getActiveExecutionPlanByid(executionPlanId);
			execPlan.setTasks(planDetails);
		}
		return execPlan;
	}

	public void deleteInputFile(int id, String signumID) {
		workOrderPlanDao.deleteInputFile(id, signumID);

	}

	public void deleteOutputFile(int id, String signumID) {
		workOrderPlanDao.deleteOutputFile(id, signumID);

	}

	public Response<Void> editInputFile(WorkOrderInputFileModel workOrderInputFileModel) {
		Response<Void> response = new Response<>();
		Map<String, Object> map = new HashMap<>();
		if (CollectionUtils.isNotEmpty(workOrderInputFileModel.getFile())) {
			for (int i = 0; i < workOrderInputFileModel.getFile().size(); i++) {
				map = activityMasterService.isValidTransactionalData(
						workOrderInputFileModel.getFile().get(i).getInputUrl(), workOrderInputFileModel.getProjectID());
				if (!(boolean) map.get(AppConstants.RESULT)) {
					response.addFormError(map.get(AppConstants.MESSAGE).toString());
					break;
				}
			}
			if ((boolean) map.get(AppConstants.RESULT)) {
		workOrderPlanDao.deleteInputFile1(workOrderInputFileModel);
		workOrderPlanDao.insertInputFileWO(workOrderInputFileModel);
				response.addFormMessage("Successfully inserted!!");
			}
		} else {
			workOrderPlanDao.deleteInputFile1(workOrderInputFileModel);
			workOrderPlanDao.insertInputFileWO(workOrderInputFileModel);
			response.addFormMessage("Successfully inserted!!");
		}

		return response;

	}

	public Response<Void> editOutputFile(WorkOrderOutputFileModel workOrderOutputFileModel) {
		Response<Void> response = new Response<>();
		Map<String, Object> map = new HashMap<>();
		if (CollectionUtils.isNotEmpty(workOrderOutputFileModel.getFile())) {
			for (int i = 0; i < workOrderOutputFileModel.getFile().size(); i++) {
				map = activityMasterService.isValidTransactionalData(
						workOrderOutputFileModel.getFile().get(i).getOutputUrl(),
						workOrderOutputFileModel.getProjectID());
				if (!(boolean) map.get(AppConstants.RESULT)) {
					response.addFormError(map.get(AppConstants.MESSAGE).toString());
					break;
				}
			}
			if ((boolean) map.get(AppConstants.RESULT)) {
		workOrderPlanDao.deleteOutputFile1(workOrderOutputFileModel);
		workOrderPlanDao.editOutputFile(workOrderOutputFileModel);
				response.addFormMessage("Successfully inserted!!");
	}
		} else {
			workOrderPlanDao.deleteOutputFile1(workOrderOutputFileModel);
			workOrderPlanDao.editOutputFile(workOrderOutputFileModel);
			response.addFormMessage("Successfully inserted!!");
		}

		return response;

	}

	private void insertInputFileWO(CreateWorkOrderModel createWorkOrderModel, CreateWorkOrderModel2 createModel) {
		WorkOrderInputFileModel inputFile = createWorkOrderModel.getWorkOrderInputFileModel();
		if (inputFile != null) {
			inputFile.setWoid(createModel.getWoId());
			inputFile.setCreatedBy(createModel.getCreateBy());
			workOrderPlanDao.insertInputFileWO(inputFile);
		}
	}

	private void validateWorkOrderPlanObjectModel(CreateWorkOrderModel woPlanObject) {

		List<String> json = workOrderPlanDao.getValidateJsonForExternalSource(woPlanObject.getExternalSourceName());

		if (CollectionUtils.isEmpty(json)) {

			throw new ApplicationException(500, "Invalid source Name or validateJson found!");
		}
		
		JSONObject obj = new JSONObject(json.get(0));
		JSONArray array = obj.getJSONArray("validations");

		if (array == null || array.length() == 0) {
			throw new ApplicationException(500, "Given Source name has invalid validateJson!");
		}
		for (int i = 0; i < array.length(); i++) {

			switch (String.valueOf(array.get(i))) {

			case "validateSource":

				// validate source name.
				validationUtilityService.validateSource(woPlanObject.getExternalSourceName());
				break;

			case "validateDeliverablePlan":

				// validate DeliverablePlan is inactive
				validateDeliverablePlan(woPlanObject.getScopeID());
				break;

			case "validatePriority":

				// validate priority.
				validationUtilityService.validatePriority(woPlanObject.getPriority());
				break;

			case "validateNodes":

				// validation for valid node name string and type as per CNEDB concept
				validateNodes(woPlanObject);
				break;

			case "validateWOName":

				// validate WO Name
				validateWOName(woPlanObject.getwOName());
				break;

			case "validateProjctID":

				// validate project id
				validateProjctID(woPlanObject.getProjectID());
				break;

			case "validateDate":

				// validate date
				validateDate(woPlanObject.getStartDate(), woPlanObject.getStartTime());
				break;

			case "validateSignumForExistingEmployee":

				// validate Signum For Existing Employee
				validationUtilityService.validateSignumForExistingEmployee(woPlanObject.getAssignedTo());
				break;

			case "validatePriorityForEMS":

				// validate priority.
				validationUtilityService.validatePriorityForEMS(woPlanObject.getPriority());
				break;

			case "validateMultipleWFList":

				// validation for GNET WF list(there should be only one WF in a deliverable
				// plan)
				validateMultipleWFList(woPlanObject);
				break;
				
			case "validateSignumForResignedStatus":

				// validate assigned to Signum For Blank And Existing Employee check
				validationUtilityService.validateSignumForResignedStatus(woPlanObject.getAssignedTo(),"assignedTo");
				
				// validate uploaded by Signum For Blank And Existing Employee check
				validationUtilityService.validateSignumForResignedStatus(woPlanObject.getAssignedTo(),"uploadedBy");
				break;
		 default :
			 break;
			}
		}
	}

	public void validateDate(String startDate, Time startTime) {

		// validate if date is in correct format.
		DateTimeUtil.convertStringToDate(startDate, AppConstants.DEFAULT_DATE_FORMAT);

		// validate if time is in correct format.
		DateTimeUtil.validateTimeInHourFormat(String.valueOf(startTime), AppConstants.TIME24HOURS_PATTERN);

		// validate if date and time is not less than today's date and time
		DateTimeUtil.validateDateTimeByCurrentDateTime(startDate + StringUtils.SPACE + startTime,
				AppConstants.UI_DATE_FORMAT);

	}

	private void validateMultipleWFList(CreateWorkOrderModel woPlanObject) {

		List<ExecutionPlanDetail> execPlanDetails = getExecutionPlanDetailsByExecutionPlanId(
				woPlanObject.getExecutionPlanId());
		if (CollectionUtils.size(execPlanDetails) > 1) {
			throw new ApplicationException(500,
					"This Deliverable Plan has more than one workflow,Please align only one workflow with your Deliverable Plan");
		}

	}

	private void validateNodes(CreateWorkOrderModel woPlanObject) {

		if (CollectionUtils.isNotEmpty(woPlanObject.getListOfNode())) {
			if (StringUtils.isNotBlank(woPlanObject.getListOfNode().get(0).getNodeNames())
					&& StringUtils.isNotBlank(woPlanObject.getListOfNode().get(0).getNodeType())) {

				woPlanObject.getListOfNode().stream()
						.forEach(workOrderPlanNodesModels -> Arrays
								.stream(workOrderPlanNodesModels.getNodeNames().split(AppConstants.CSV_CHAR_COMMA))
								.forEach(node -> {
									validateNodeNameString(node);
									
								}));
				//Added check for NodeName CNEDB Validation
				checkNodeNameandTypeExistsInCNEDB(woPlanObject.getProjectID(),woPlanObject.getListOfNode().get(0).getNodeType(),
						woPlanObject.getListOfNode().get(0).getNodeNames(), woPlanObject.getExecutionPlanName(), woPlanObject.getExternalSourceName());
				
			} else {
				throw new ApplicationException(500,
						"Node Names and Node Types cannot be blank/Null, please provide valid Node Names and Node Types!!");
			}
		} else {
			throw new ApplicationException(500,
					"Node Names and Node Types cannot be blank/Null, please provide valid Node Names and Node Types!!");
		}
		
		
	}

	private void checkNodeNameandTypeExistsInCNEDB(Integer projectID, String nodeType, String nodeNames,
			String executionPlanName, String source) {
		int countryCustomerGroupID = appService.getCountryCustomerGroupIDByProjectID(projectID);

		String status = networkElementDao.checkNodeNameandTypeExistsInCNEDB(countryCustomerGroupID, nodeNames, nodeType, source,
				executionPlanName, projectID);
		if (StringUtils.isNotEmpty(status) && StringUtils.equalsIgnoreCase(status, "Invalid")) {
			throw new ApplicationException(500, ApplicationMessages.INVALID_NODENAME_NODETYPE);
		}

	}

	public void validateWOName(String getwOName) {
		if (StringUtils.isBlank(getwOName)) {
			throw new ApplicationException(500, ApplicationMessages.INVALID_WO_NAME);
		}
	}

	public void validateDeliverablePlan(int scopeId) {
		if (scopeId == 0) {
			throw new ApplicationException(500, ApplicationMessages.INVALID_DELIVERABLE_PLAN_ID);
		}
		ScopeDetailsModel projectScopeDetails = activityMasterService.getScopeByScopeId(scopeId);
		if (projectScopeDetails == null) {
			throw new ApplicationException(500, ApplicationMessages.INACTIVE_DELIVERABLE_PLAN);
		} else {
			if (StringUtils.equalsIgnoreCase("inactive", projectScopeDetails.getDeliverableStatus())) {
				throw new ApplicationException(500, ApplicationMessages.INACTIVE_STATUS_DELIVERABLE_PLAN);
			}
		}
	}

	public void validateProjctID(int projectID) {

		if (projectID == 0) {
			throw new ApplicationException(500, ApplicationMessages.INVALID_PROJECTID);
		}
		if (CollectionUtils.isEmpty(projectService.getProjectByProjectID(projectID))) {
			throw new ApplicationException(500, ApplicationMessages.INACTIVE_PROJECTID);
		}
	}

	public void validateNodeNameString(String nodeName) {
		if (StringUtils.isBlank(nodeName) || StringUtils.trimToNull(nodeName).endsWith(AppConstants.CSV_CHAR_COMMA)) {
			throw new ApplicationException(500, ApplicationMessages.INVALID_NODENAME);
		}
	}

	public void validateNodeNameAndNodeType(String nodeName, String nodeType, int projectID) {
		if (StringUtils.isBlank(nodeName) || StringUtils.isBlank(nodeType)
				|| !workOrderPlanDao.isValidNodeNameAndNodeType(nodeName, nodeType, projectID)) {
			throw new ApplicationException(500, ApplicationMessages.INVALID_NODENAME_NODETYPE);
		}
	}

	public void editWOPriority(int woid, String priority, String signumID, String actorType) {
		 if(woid==0 || signumID.equalsIgnoreCase("Null")){
	            throw new ApplicationException(500, "Invalid input... WOID cannot be 0 or SignumID cannot be NULL !!!");
	        }
		 else {
		workOrderPlanDao.editWOPriority(woid, priority, signumID);
		LOG.info("Work Order Edit: SUCCESS");
		 }
	}

	public List<Map<String, Object>> getWorkOrdersByProjectID(String projectID, String woName, String projectScopeID,
			String assignedBy, String assignedTo, String nodeName, String marketArea, String role, String signum) {

		return workOrderPlanDao.getWorkOrdersByProjectID(projectID, woName, projectScopeID, assignedBy, assignedTo,
				nodeName, marketArea, role, signum);
	}

	public List<Map<String, Object>> getNextStepData(String stepID, Integer defID) {
		List<Map<String, Object>> nextDataValues = new ArrayList<Map<String,Object>>();
		
//		List<Map<String, Object>> nextStepInfo = workOrderPlanDao.getNextStepData(stepID, defID);
		List<NextStepModel> nextStepInfo = workOrderPlanDao.getNextStepDataModel(stepID, defID);

		//RuleModel rules = autoSenseService.getRulesByStepIDNew((String) nextStepInfo.get(0).getNextStepID(),defID);
		Map<String,Object> rules=autoSenseService.getRulesByStepIDNew((String) nextStepInfo.get(0).getNextStepID(),defID);

		Map<String,Object> temp = new HashMap<>();
		temp.put(START_RULE, false);
		temp.put(STOP_RULE, false);
		
		if(rules!=null) {
//			if(StringUtils.equalsIgnoreCase(rules.getTaskActionName(), AppConstants.START)) {
//				temp.put(START_RULE, true);
//			}
//			if(StringUtils.equalsIgnoreCase(rules.getTaskActionName(), AppConstants.STOP)) {
//				temp.put(STOP_RULE, true);
//			}
			if(rules.containsKey(AppConstants.START)) {
				temp.put(START_RULE, true);
			}
			if(rules.containsKey(AppConstants.STOP)) {
				temp.put(STOP_RULE, true);
			}
		}
		
		if (CollectionUtils.isNotEmpty(nextStepInfo)) {
			for (NextStepModel data : nextStepInfo) {
				Map<String, Object> nextStepData = new HashMap<>();
				nextStepData.put("TargetExecutionFileName", data.getTargetExecutionFileName());
				nextStepData.put("CreatedBY", data.getCreatedBY());
				nextStepData.put("ModuleClassMethod", data.getModuleClassMethod());
				nextStepData.put("ModifiedOn", data.getModifiedOn());
				nextStepData.put("BOTID", data.getBotid());
				nextStepData.put("InstanceID", data.getInstanceID());
				nextStepData.put("nextStepType", data.getNextStepType());
				nextStepData.put("currentStepTaskId", data.getCurrentStepTaskId());
				nextStepData.put("ProjectID", data.getProjectID());
				nextStepData.put("SystemID", data.getSystemID());
				nextStepData.put("rpaRequestID", data.getRpaRequestID());
				nextStepData.put("ModifiedBy", data.getModifiedBy());
				nextStepData.put("isActive", data.isActive());
				nextStepData.put("bookingID", data.getBookingID());
				nextStepData.put("referenceBotId", data.getReferenceBotId());
				nextStepData.put("bookingStatus", data.getBookingStatus());
				nextStepData.put("LineOfCode", data.getLineOfCode());
				nextStepData.put("ParallelWOExecution", data.getParallelWOExecution());
				nextStepData.put("CreatedOn", data.getCreatedOn());
				nextStepData.put("ID", data.getId());
				nextStepData.put("BOTLanguage", data.getBotLanguage());
				nextStepData.put("Status", data.getStatus());
				nextStepData.put("isInputRequired", data.getInputRequired());
				nextStepData.put("NextExecutionType", data.getNextExecutionType());
				nextStepData.put("CurrentAvgExecutionTime", data.getCurrentAvgExecutionTime());
				nextStepData.put("NextStepName", data.getNextStepName());
				nextStepData.put("RPAExecutionTime", data.getrPAExecutionTime());
				nextStepData.put("NextStepID", data.getNextStepID());
				nextStepData.put("VersionNumber", data.getVersionNumber());
				nextStepData.put("ModuleClassName", data.getModuleClassName());
				nextStepData.put("ReuseFactor", data.getReuseFactor());
				nextStepData.put("isRunOnServer", data.getIsRunOnServer());
				nextStepData.put("OutputUpload", data.getOutputUpload());
				nextStepData.put("botType", data.getBotType());
				nextStepData.put("NextTaskID", data.getNextTaskID());
				nextStepData.put("RuleStatus", data.getRuleStatus());
				nextStepData.put("CascadeInput", data.getCascadeInput());
				nextStepData.put("isAuditPass", data.isAuditPass());
				nextStepData.put("BOTName", data.getBotName());
				nextStepData.put("nextStepRpaId", data.getNextStepRpaId());				
				nextStepData.put("isStepAutoSenseEnabled", data.getStepAutoSenseEnabled());
				
				nextStepData.put(START_RULE, temp.get(START_RULE));
				nextStepData.put(STOP_RULE, temp.get(STOP_RULE));
				nextDataValues.add(nextStepData);
			}
		}
		return nextDataValues;
	}

	/**
	 * 
	 * @param projectID
	 * @param scopeID
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getWFListForDeliverablePlan(int projectID, int scopeID) {
		Integer executionPlanId = workOrderPlanDao.getExecutionPlanIdByScopeId(scopeID);
		List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();

		if (executionPlanId == null) {
			return response;
		} else {

			List<Map<String, Object>> wfnameList = workOrderPlanDao.getWFListForDeliverablePlan(executionPlanId);
			for (Map<String, Object> wfName : wfnameList) {
				String wfName1 = (String) wfName.get("workFlowName");
				Map<String, Object> wf = new HashMap<String, Object>();

				String[] split_wfName = wfName1.split("/");
				
				if(split_wfName.length !=3) {
					throw new ApplicationException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Error while getting Work Flow for the provided Deliverable plan, Please update Deliverable Plan or contact ISF Support");
				}
				
				wf.put("WFID", split_wfName[0]);
				wf.put("WorkFlowName", split_wfName[1]);
				wf.put("VersionNumber", split_wfName[2]);
				response.add(wf);
			}
			return response;
		}
	}

	/**
	 * 
	 * @param source 
	 * @param wOPlanObject
	 */
	@Transactional("transactionManager")
	public List<CreateWoResponse> createBulkWorkOrder(String source) {

		CreateWoResponse response = new CreateWoResponse();
		List<CreateWoResponse> responseList = new ArrayList<>();
		
		String externalSourceName = StringUtils.EMPTY;
		try {

			List<FinalRecordsForWOCreationModel> finalRecordsForWOCreationModels = workOrderPlanDao
					.getWorkOrderModelForBulkCreation(StringUtils.trimToEmpty(source));
			if (CollectionUtils.isEmpty(finalRecordsForWOCreationModels)) {
				response.setMsg("No data found in table transactionalData.tbl_FinalRecordsForWOCreation!");
				responseList.add(response);
				return responseList;
			}

			List<CreateWorkOrderModel> createWorkOrderModels = new ArrayList<>();
			GenericMailModel genericMailModel = new GenericMailModel(new ArrayList<>());
			BulkWoResponse.WORK_ORDERS_FOR_UPLOADED_BY = new HashMap<>();

			for (FinalRecordsForWOCreationModel finalRecordsForWOCreationModel : finalRecordsForWOCreationModels) {

				createWorkOrderModels = finalRecordsForWOCreationModel.getCreateWorkOrderModels();
				for (CreateWorkOrderModel woPlanObject : createWorkOrderModels) {

					externalSourceName = woPlanObject.getExternalSourceName();
					try {

						responseList.add(createWorkOrdersInBulk(woPlanObject));

					} catch (Exception e) {

						response.setMsg("Error occured for creation id: " + woPlanObject.getWoCreationID() + " -> "
								+ e.getMessage());
						saveErrorInErrorTable(woPlanObject, e);
						String errorMsg = StringUtils.EMPTY;

						if (e instanceof ApplicationException) {

							errorMsg = e.getMessage();
						} else {

							errorMsg = "Error Occured while creating Work Order!";
						}

						genericMailModel.getRows()
								.add(new String[] { String.valueOf(woPlanObject.getWoCreationID()), errorMsg });
						responseList.add(response);

						continue;
					}

				}
				performAfterStepsOfBulkWoCreation(finalRecordsForWOCreationModel.getWoHistoryID(),externalSourceName);

			}

			if (StringUtils.isBlank(response.getMsg())) {
				response.setMsg(AppConstants.WORK_ORDER_PLAN_HAS_BEEN_CREATED_SUCCESSFULLY);
			}
			
			if(configurations.getBooleanProperty(ConfigurationFilesConstants.WORKORDER_MAIL_ENABLED, false)) {
				sendMailforBulkWorkOrderWithTableBody(genericMailModel);

				sendMailToUploadedByForBulkWOCreation();
			}
		} catch (Exception ae) {
			LOG.error(ae.getMessage());
			response.setMsg(API_IS_GETTING_BELOW_ERROR_IN_CREATE_BULK_WORK_ORDER_API + ae.getMessage());
			sendMailforBulkWorkOrderWithoutTableBody(response.getMsg());
			responseList.add(response);
		}
		LOG.info("TBL_WORK_ORDER_PLAN: SUCCESS");
		return responseList;
	}

	private void sendMailToUploadedByForBulkWOCreation() {

		Map<String, List<CreateWorkOrderModel2>> workOrdersForUploadedBy = BulkWoResponse.getWorkOrdersForUploadedBy();
		for (Map.Entry<String, List<CreateWorkOrderModel2>> entry : workOrdersForUploadedBy.entrySet()) {

			try {
				Map<String, Object> placeHolder = enrichMailforBulkWorkOrder(entry.getKey(), entry.getValue());
				placeHolder.put(AppConstants.PAGE_LINK_STRING, SP);
				emailService.sendMail(AppConstants.BULK_WO_MAIL_UPLOADED_BY, placeHolder);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Map<String, Object> enrichMailforBulkWorkOrder(String uploadedBy, List<CreateWorkOrderModel2> workOrders) {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("workOrders", workOrders);
		EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(uploadedBy);
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, eDetails.getEmployeeEmailId());
		return data;
	}

	private void saveErrorInErrorTable(CreateWorkOrderModel woPlanObject, Exception e) {

		workOrderPlanDao.insertBulkWoErrorTable(woPlanObject, e.getMessage(), ERROR_CATEGORY);

	}

	@Transactional("transactionManager")
	private CreateWoResponse createWorkOrdersInBulk(CreateWorkOrderModel woPlanObject) throws Exception {

		CreateWoResponse response = createWorkOrdekOrExecutionPlan(woPlanObject, false);
		if (!response.getMsg().equalsIgnoreCase(AppConstants.WORK_ORDER_PLAN_HAS_BEEN_CREATED_SUCCESSFULLY)) {

			if(response.getMsg().contains("Global Exception Occured:")) {
				
				throw new Exception(response.getMsg());
			} else {
				
			throw new ApplicationException(500, response.getMsg());
		}
			
		}
		workOrderPlanDao.insertWOCreationTable(woPlanObject, response.getWorkOrderID());

		return response;
	}

	public void sendMailforBulkWorkOrderWithoutTableBody(String bodyData) {

		try {
			Map<String, Object> placeHolder = enrichMailforBulkWorkOrderWithoutTableBody(bodyData);
			emailService.sendMail(AppConstants.GENERIC_TEMPLATE_WITHOUT_TABLE_BODY, placeHolder);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void sendMailforBulkWorkOrderWithTableBody(GenericMailModel genericMailModel) {

		if (CollectionUtils.isNotEmpty(genericMailModel.getRows())) {

			try {
				Map<String, Object> placeHolder = enrichMailforBulkWorkOrderWithTableBody(genericMailModel);
				emailService.sendMail(AppConstants.GENERIC_TEMPLATE_WITH_TABLE_BODY, placeHolder);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void performAfterStepsOfBulkWoCreation(int woHistoryID, String modifiedBy) {

		workOrderPlanDao.markWoHistoryIDCompleted(woHistoryID,modifiedBy);
		workOrderPlanDao.deleteProcessedDataFromWOCreationTable(woHistoryID);
	}

	public CreateWorkOrderModel getWorkOrderModelForBulkCreation(CreateWorkOrderModel woPlanObject) {

		setWorkOrderInputFileModelInCreateWorkOrderModel(woPlanObject, woPlanObject.getInputName(),
				woPlanObject.getInputUrl());

		setListOfNodeListInCreateWorkOrderModel(woPlanObject, woPlanObject.getNodeType(), woPlanObject.getNodeNames());

	
		setListOfSignumIdInCreateWorkOrderModel(woPlanObject, woPlanObject.getAssignedTo());
		if (StringUtils.isBlank(woPlanObject.getCreatedBy())) {
			woPlanObject.setCreatedBy(woPlanObject.getExternalSourceName());
		}
		return woPlanObject;
	}

	public Map<String, Object> enrichMailforBulkWorkOrderWithTableBody(GenericMailModel genericMailModelTmp) {

		Map<String, Object> data = new HashMap<String, Object>();
		GenericMailModel genericMailModel = new GenericMailModel(new String[] { "Wo Creation ID", "Error Message" },
				genericMailModelTmp.getRows(), ERROR_WHILE_CREATION_WO_IN_BULK_WO_CREATION_PROCESS, StringUtils.EMPTY,
				" Please find errors below: ");
		data.put(AppConstants.GENERIC_MAIL_MODEL, genericMailModel);
		data.put(AppConstants.SUBJECT, ERROR_WHILE_CREATION_WO_IN_BULK_WO_CREATION_PROCESS);
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO,
				configurations.getStringProperty(ConfigurationFilesConstants.ISF_SUPPORT_MAIL));
		data.put(AppConstants.PAGE_LINK_STRING, SP);
		return data;

	}

	public Map<String, Object> enrichMailforBulkWorkOrderWithoutTableBody(String bodyData) {

		Map<String, Object> data = new HashMap<String, Object>();
		GenericMailModel genericMailModel = new GenericMailModel(ERROR_WHILE_CREATION_WO_IN_BULK_WO_CREATION_PROCESS,
				StringUtils.EMPTY, bodyData);
		data.put(AppConstants.GENERIC_MAIL_MODEL, genericMailModel);
		data.put(AppConstants.SUBJECT, ERROR_WHILE_CREATION_WO_IN_BULK_WO_CREATION_PROCESS);
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO,
				configurations.getStringProperty(ConfigurationFilesConstants.ISF_SUPPORT_MAIL));
		data.put(AppConstants.PAGE_LINK_STRING, SP);
		return data;

	}

	@SuppressWarnings("serial")
	private void setWorkOrderInputFileModelInCreateWorkOrderModel(CreateWorkOrderModel woPlanObject, String inputName,
			String inputUrl) {

		if (StringUtils.isNotBlank(inputName) && StringUtils.isNotBlank(inputUrl)) {

			WorkOrderInputFileModel workOrderInputFileModel = new WorkOrderInputFileModel(
					new ArrayList<WOInputFileModel>() {
						{
							add(new WOInputFileModel(inputName, inputUrl));
						}
					});
			woPlanObject.setWorkOrderInputFileModel(workOrderInputFileModel);
		}

	}

	@SuppressWarnings("serial")
	private void setListOfNodeListInCreateWorkOrderModel(CreateWorkOrderModel woPlanObject, String nodeType,
			String nodeNames) {
		if (StringUtils.isNotBlank(nodeNames)) {
			List<WorkOrderPlanNodesModel> listOfNode = new ArrayList<>();
			listOfNode.add(new WorkOrderPlanNodesModel(nodeType, nodeNames));
			woPlanObject.setListOfNode(listOfNode);
		}
	}

	@SuppressWarnings("serial")
	private void setListOfSignumIdInCreateWorkOrderModel(CreateWorkOrderModel woPlanObject, String assignTo) {

		if (StringUtils.isNotBlank(assignTo)) {

			woPlanObject.setLstSignumID(new ArrayList<String>() {
				{
					add(assignTo);
				}
			});
		}
	}

	/**
	 * 
	 * @param woPlanObject
	 * @return String
	 */
	public String doBasicValidationAndPrepareCreateWorkOrderPlanModel(CreateWorkOrderModel woPlanObject) {

		try {
			
			// validate length of wo
			doLenthValidationForWoName(woPlanObject.getwOName());
			
			// validate length of comment
			if(StringUtils.isNotBlank(woPlanObject.getComment())) {
			doLenthValidationForComment(woPlanObject.getComment());
			}
			
						
			// validate project id
			validateProjectId(woPlanObject);
			
			// validate Url
			if (woPlanObject.getWorkOrderInputFileModel()!= null && CollectionUtils.isNotEmpty(woPlanObject.getWorkOrderInputFileModel().getFile())) {
				
				for(WOInputFileModel woInputFileModel: woPlanObject.getWorkOrderInputFileModel().getFile()) {
					
					// validate length
					doLenthValidationForInputName(woInputFileModel.getInputName());
					doLenthValidationForInputUrl(woInputFileModel.getInputUrl());
					
					validateUrl(woInputFileModel.getInputUrl(),woPlanObject.getProjectID());
				}
			}
			
			// validate Execution Plan
			validateAndSetExecutionPlan(woPlanObject);

			// validate Deliverable Name For ProjectID
			validateDeliverableNameForProjectID(woPlanObject);

			// validate Signum List
			validateSignumList(woPlanObject);
			
			//validate Network Element Count
			if (!StringUtils.equalsIgnoreCase(AppConstants.WEB, woPlanObject.getExternalSourceName())) {

				validateNEIDCountForWO(woPlanObject);
			}
			
			// validate and set duplicate node names
			if (!StringUtils.equalsIgnoreCase(AppConstants.WEB, woPlanObject.getExternalSourceName())) {
			validateAndSetDuplicateNodeNames(woPlanObject);
			}
			
		
			
			return StringUtils.EMPTY;
		} catch (ApplicationException e) {
			return e.getErrorMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	private void validateNEIDCountForWO(CreateWorkOrderModel woPlanObject) {
		List<WorkOrderPlanNodesModel> listOfNode = woPlanObject.getListOfNode();
		if (CollectionUtils.isNotEmpty(listOfNode)) {

			validationUtilityService.validateNetworkElementCount(listOfNode.get(0).getNodeNames());
		}
	}

	public void doLenthValidationForInputUrl(String inputUrl) {
		validationUtilityService.validateLength(inputUrl, 2000, "inputUrl");
	}

	public void doLenthValidationForWoName(String woName) {
		validationUtilityService.validateLength(woName, 400, "woName");
	}
	
	public void doLenthValidationForInputName(String inputName) {
		validationUtilityService.validateLength(inputName, 100, "inputName");
	}
	
 	public void doLenthValidationForComment(String comment) {
		validationUtilityService.validateLength(comment, 1000, "comment");
	}

	private void validateAndSetDuplicateNodeNames(CreateWorkOrderModel woPlanObject) {

		List<WorkOrderPlanNodesModel> listOfNode = woPlanObject.getListOfNode();
		if (!CollectionUtils.isEmpty(listOfNode)) {

			Set<String> setOfNodeNames = new HashSet<>();
		

			String nodeNames = listOfNode.get(0).getNodeNames();
			if (StringUtils.isNotBlank(nodeNames)) {

				String[] nodeNamesArray = nodeNames.split(AppConstants.CSV_CHAR_COMMA);
				if (nodeNamesArray.length > 1) {

					Arrays.stream(nodeNamesArray).forEach(nodeName -> setOfNodeNames.add(nodeName));

					String nodeNamesFinal = StringUtils.join(setOfNodeNames, AppConstants.CSV_CHAR_COMMA);

					setListOfNodeListInCreateWorkOrderModel(woPlanObject, listOfNode.get(0).getNodeType(), nodeNamesFinal);
				}
			}
		}

	}

	public void validateUrl(String inputUrl, int projectId) {

		Map<String, Object> map = activityMasterService.isValidTransactionalData(inputUrl, projectId);
		if (!(boolean) map.get(AppConstants.RESULT)) {
			throw new ApplicationException(500, map.get(AppConstants.MESSAGE).toString());
		}

	}

	/**
	 * 
	 * @param woPlanObject
	 */
	private void validateSignumList(CreateWorkOrderModel woPlanObject) {

		if (CollectionUtils.isNotEmpty(woPlanObject.getLstSignumID())) {

			List<String> signumlList = new ArrayList<>(woPlanObject.getLstSignumID());
			if (signumlList.size() > 1) {
				throw new ApplicationException(500, "Only one signum is allowed while creating work orders!");
			}
		}
	}

	/**
	 * 
	 * @param woPlanObject
	 */
	private void validateAndSetExecutionPlan(CreateWorkOrderModel woPlanObject) {
		if (woPlanObject.getExecutionPlanId() == 0) {

			if (StringUtils.isBlank(woPlanObject.getExecutionPlanName())) {

				throw new ApplicationException(500, "Please provide execution ID or deliverable Name!");
			} else {
				
				
				int executionPlanId = rpaDAO.getExecutionPlan(woPlanObject.getExecutionPlanName(),
						woPlanObject.getProjectID());
				if (executionPlanId == 0) {
					throw new ApplicationException(500,
							"No valid details found for given deliverable Name and project ID!");
				}
				woPlanObject.setExecutionPlanId(executionPlanId);
			}
		} else {
			if (StringUtils.isBlank(woPlanObject.getExecutionPlanName())) {

				ExecutionPlanModel executionPlanModel = workOrderPlanDao
						.getActiveExecutionPlanByid(woPlanObject.getExecutionPlanId());
				if (executionPlanModel == null) {
					throw new ApplicationException(500, "Invalid Deliverable/ExecutionPlan Name!");
				}
				woPlanObject.setExecutionPlanName(executionPlanModel.getPlanName());
			}
		}
	}
	/**
	 * 
	 * @param woPlanObject
	 */
	public void validateProjectId(CreateWorkOrderModel woPlanObject){
		
		if(woPlanObject.getProjectID()==0) {
			throw new ApplicationException(500, "Please provide valid project ID");
		}
	}

	/**
	 * 
	 * @param woPlanObject
	 */
	private void validateDeliverableNameForProjectID(CreateWorkOrderModel woPlanObject) {

		if (woPlanObject.getScopeID() == 0) {
			Integer scopeID = workOrderPlanDao.getDeliverableIdByNameAndProjectID(woPlanObject.getExecutionPlanName(),
					woPlanObject.getProjectID());
			if (scopeID == null || scopeID == 0) {
				throw new ApplicationException(500, "Supplied Deliverable Name is not active for given Project ID!");
			}
			woPlanObject.setScopeID(scopeID);
		}
	}

	/**
	 * 
	 * @param oldWorkOrderDetails
	 * @return Response<String>
	 */
	public Response<String> saveEditedWorkOrderDetails(WorkOrderCompleteDetailsModel oldWorkOrderDetails) {

		Response<String> response = new Response<>();
		
		try {
			
			WorkOrderCompleteDetailsModel workOrderDetails = wOExecutionDAO
					.getCompleteWorkOrderDetails(oldWorkOrderDetails.getwOID(), AppConstants.ALL).get(0);
			
			oldWorkOrderDetails.setOldStartDate(workOrderDetails.getStartDate());
			oldWorkOrderDetails.setOldWoName(workOrderDetails.getwOName());

			String startDate = oldWorkOrderDetails.getStartDate();
			Date startTime = oldWorkOrderDetails.getStartTime();

			if (oldWorkOrderDetails.getStartDate() == null) {
				startDate = workOrderDetails.getStartDate();
			}

			if (oldWorkOrderDetails.getStartTime() == null) {
				startTime = workOrderDetails.getStartTime();
			}

			//String date = new SimpleDateFormat(AppConstants.DEFAULT_DATE_FORMAT).format(startDate) + " " + startTime;
			Date plannedStartDate = PlannedEndDateCal.convertStringToDate(startDate+" "+startTime, AppConstants.UI_DATE_FORMAT);
			Date plannedEndDate = PlannedEndDateCal.calculatePlannedEndDate(plannedStartDate,
					(int) workOrderDetails.getSlaHrs());

			//setting priority and signumID only when they are not empty
			if(StringUtils.isNotEmpty(oldWorkOrderDetails.getSignumID())&& StringUtils.isNotEmpty(oldWorkOrderDetails.getPriority())){
			workOrderDetails.setSignumID(oldWorkOrderDetails.getSignumID());
			workOrderDetails.setPriority(oldWorkOrderDetails.getPriority());
			}
			
			// validate length of woName
		    doLenthValidationForWoName(oldWorkOrderDetails.getwOName());
			
		    if(StringUtils.isNotBlank(oldWorkOrderDetails.getwOName())){
			oldWorkOrderDetails
					.setwOName(oldWorkOrderDetails.getwOName()+ getSuffixFromWoName(workOrderDetails.getwOName()));
		    }
		    else {
		    	if(StringUtils.isNotEmpty(oldWorkOrderDetails.getwOName())) {
		    	oldWorkOrderDetails
				.setwOName(oldWorkOrderDetails.getwOName().trim());
		    }
		    }

			boolean isUpdated = workOrderPlanDao.saveEditedWorkOrderDetails(oldWorkOrderDetails, plannedStartDate,
					plannedEndDate);
			
			if (isUpdated) {
				response.addFormMessage(WORK_ORDER_DETAILS_UPDATED_SUCCESSFULLY);
			} else {
				throw new ApplicationException(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, "Error in Editing Work Order Details");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			response.addFormError(e.getMessage());
		}
		
		LOG.info("saveEditedWorkOrderDetails :: Success");
		return response;
	}

	private String getSuffixFromWoName(String woName) {

		String inverseWoName = StringUtils.reverse(woName);
		int index = StringUtils.ordinalIndexOf(inverseWoName, AppConstants.UNDERSCORE, 3) + 1;
		String inversedSuffix = StringUtils.substring(inverseWoName, 0, index);
		return StringUtils.reverse(inversedSuffix);
	}

	private String getWoNameFromSuffixedWoName(String woName) {

		String inverseWoName = StringUtils.reverse(woName);
		int index = StringUtils.ordinalIndexOf(inverseWoName, AppConstants.UNDERSCORE, 3) + 1;
		String wOName = StringUtils.substring(inverseWoName, index, inverseWoName.length());
		return StringUtils.reverse(wOName);
	}

	/**
	 * 
	 * @param wOID
	 * @return boolean
	 */
	public boolean hasSubSequentPlanDetail(int wOID) {

		boolean ifExists=false;
		ExecutionPlanFlow flowDetails = workOrderPlanDao.getExecutionFlowByWoid(wOID);

		if(flowDetails==null){
			return ifExists;
		}
		else {
		ExecutionPlanDetail currentStepDetails = workOrderPlanDao
				.getExecutionPlanDetailsByExecutionPlanDetailId(flowDetails.getExecutionPlanDetailId());
		ExecutionPlanModel planMaster = workOrderPlanDao
				.getActiveExecutionPlanByid(currentStepDetails.getExecutionPlanId());

			ifExists	=	CollectionUtils.isNotEmpty(getSubSequentPlanDetail(planMaster, currentStepDetails));
	}
		return ifExists;
	}
	

	/**
	 * 
	 * @param planMaster
	 * @param currentStepDetails
	 * @return boolean
	 */

	private List<Long> getSubSequentPlanDetail(ExecutionPlanModel planMaster, ExecutionPlanDetail currentStepDetails) {

		LinkModel[] links = planMaster.getLinks();
		List<Long> childTasks = new ArrayList<Long>();

		for (LinkModel link : links) {
			if (link.getSource() == currentStepDetails.getId()) {
				childTasks.add(link.getTarget());
			}
		}
		return childTasks;
	}

//	Old code reverted to get the invalid json format same like release R-10.1
// old method return type CreateWoResponse	
	public Response<CreateWoResponse> createWorkOrderPlanForExternal(CreateWorkOrderModel wOPlanObject) {
		return new Response<CreateWoResponse>(createWorkOrdekOrExecutionPlan(wOPlanObject, true));
	}

	/**
	 * 
	 * @param woPlanID
	 */
	public void updateWoPlanStartEndDateAndTime(int woPlanID) {

		workOrderPlanDao.updateWoPlanStartEndDateAndTime(woPlanID);

	}

	public List<DOWOModel> getWorkOrdersByDoid(int doid) {
		return workOrderPlanDao.getWorkOrdersByDoid(doid);
	}

	/**
	 * 
	 * @param projectID
	 * @param elementType
	 * @param type
	 * @param vendor
	 * @param market
	 * @param technologyIdList
	 * @param domainIdList
	 * @param term
	 * @return List<ProjectNodeTypeModel>
	 */
	public List<ProjectNodeTypeModel> getNodeNamesForDeliverable(int projectID, String elementType, String type,
			String vendor, String market, List<String> technologyIdList, List<String> domainIdList, String term) {

		// term = term.replaceAll(".", "[$0]");

		String techCommaSeparated = AppUtil.convertListToCommaSeparatedString(technologyIdList);
		String domainCommaSeparated = AppUtil.convertListToCommaSeparatedString(domainIdList);

		return workOrderPlanDao.getNodeNamesForDeliverable(projectID, elementType, type, vendor, market,
				techCommaSeparated, domainCommaSeparated, term);
	}

	/**
	 * 
	 * @param projectID
	 * @param elementType
	 * @param type
	 * @param vendor
	 * @param market
	 * @param technologyIdList
	 * @param domainIdList
	 * @param term
	 * @param nodeNames
	 * @return Response<Void>
	 */
	public Response<Void> getNodeValidateForDeliverable(int projectID, String elementType, String type, String vendor,
			String market, List<String> technologyIdList, List<String> domainIdList, String term, String nodeNames) {
		// TODO Auto-generated method stub

		String techCommaSeparated = AppUtil.convertListToCommaSeparatedString(technologyIdList);
		String domainCommaSeparated = AppUtil.convertListToCommaSeparatedString(domainIdList);

		Response<Void> response = new Response<>();
		int nodePresent = 0;
		int nodeNotPresent = 0;
		String nodeAbsent = "";
		String nodepresent = "";
		String msgReturned = "";
		String msgPresent = "nodes validated ";
		String msgAbsent = "nodes not validated";
		String allNodes = "";

		List<String> nodeNameList = Arrays.asList(nodeNames.trim().split("\\s*,\\s*"));
		String nodeNamesCommaSeparated = AppUtil.convertListToCommaSeparatedString(nodeNameList);

		List<String> NodeNamesFilterValidate = workOrderPlanDao.getNodeValidateForDeliverable(projectID, elementType,
				type, vendor, market, techCommaSeparated, domainCommaSeparated, nodeNamesCommaSeparated);

		if (NodeNamesFilterValidate.isEmpty() || NodeNamesFilterValidate.size() == 0) {

			nodeAbsent = allNodes + ',' + nodeAbsent;
			nodeAbsent = nodeAbsent.replaceAll(COMMA_DOLLAR, "");
			nodeNotPresent = 1;
		} else {
			for (String node : nodeNameList) {

				if (NodeNamesFilterValidate.contains(node)) {
					nodepresent = node + ',' + nodepresent;
					nodepresent = nodepresent.replaceAll(COMMA_DOLLAR, "");
					nodePresent = 1;
				} else {
					nodeAbsent = node + ',' + nodeAbsent;
					nodeAbsent = nodeAbsent.replaceAll(COMMA_DOLLAR, "");
					nodeNotPresent = 1;
				}

			}
		}

		msgPresent = msgPresent + " " + nodepresent;
		msgAbsent = msgAbsent + " " + nodeAbsent;
		if (nodeNotPresent == 1 && nodePresent == 1) {
			response.addFormError(msgAbsent);
		}
		if (nodeNotPresent == 0 && nodePresent == 1) {
			msgReturned = msgPresent;
		}
		if (nodeNotPresent == 1 && nodePresent == 0) {
			response.addFormError(msgAbsent);
		}
		response.addFormMessage(msgReturned);
		return response;
	}

	public List<WorkOrderOutputFileModel> getWOOutputFileDetails(int woid) {

		return workOrderPlanDao.getWOOutputFileDetails(woid);
	}

	public List<WorkOrderOutputFileModel> getWOOutputFileDetailsByWoIDAndWoOutputFileModel(
			WOOutputFileModel woOutputFileModel, int woid) {

		return workOrderPlanDao.getWOOutputFileDetailsByWoIDAndWoOutputFileModel(woOutputFileModel, woid);
	}
	
	public WorkOrderModel getWorkOrderDetailsById(int wOID) {

		return workOrderPlanDao.getWorkOrderDetailsById(wOID);
	}
	public List<LinkedHashMap<String, Object>> getAllBookingDetails(int WoID, String SignumID, boolean flag, String stepId) {
		return workOrderPlanDao.getAllBookingDetails(WoID, SignumID,flag,stepId);

	}

	public String validateStepIdAndExecutionType(String flowChartStepId, int flowChartDefID) {
		return workOrderPlanDao.validateStepIdAndExecutionType(flowChartStepId,flowChartDefID);
		
	}

	public boolean validateFailureReason(String reason, String category) {
		return workOrderPlanDao.validateFailureReason(reason,category);
		
	}

	public int getTaskIdONStepIdAndFlowChartdefId(String flowChartStepId, int flowChartDefID) {
		return workOrderPlanDao.getTaskIdONStepIdAndFlowChartdefId(flowChartStepId,flowChartDefID);
	}

	public String getPreviousStepStepId(String flowChartStepId, int flowChartDefID) {
		return workOrderPlanDao.getPreviousStepStepId(flowChartStepId,flowChartDefID);
	}

	public boolean checkPreviousStepCompleted(String previousStepStepId, int flowChartDefID,int woId, String executionType) {
		return workOrderPlanDao.checkPreviousStepCompleted(previousStepStepId,flowChartDefID,woId,executionType);
	}

	public boolean checkDecisionStepComplitionStatus(int flowChartDefID, int woId,
			String previousStepStepId) {
		return workOrderPlanDao.checkDecisionStepComplitionStatus(previousStepStepId,flowChartDefID,woId);
	}

	public boolean checkStepWithDecisionValue(String previousStepStepId,int flowChartDefID,int woId, String decisionValue) {
		return workOrderPlanDao.checkStepWithDecisionValue(previousStepStepId,flowChartDefID,woId,decisionValue);
	}

	public BookingDetailsModel getBookingDetailsOnWoidTaskIdDefIdStepId(int wOID, int taskID,
			int flowchartdefid, String stepID) {
		return workOrderPlanDao.getBookingDetailsOnWoidTaskIdDefIdStepId(wOID,taskID,
				flowchartdefid, stepID);
	}
	
	@Transactional("transactionManager")
	public ResponseEntity<Response<String>> updateWorkOrderDetails(WorkOrderCompleteDetailsModel oldWorkOrderDetails) {
		
		Response<String> response = new Response<>();
 
		try {
			LOG.info("updateWorkOrderDetails:Start");

			validateUpdateWorkOrderDetails(oldWorkOrderDetails);

			WorkOrderCompleteDetailsModel workOrderDetails = wOExecutionDAO
					.getCompleteWorkOrderDetails(oldWorkOrderDetails.getwOID(), AppConstants.ALL).get(0);

			validateLastModifiedByForPMDR(oldWorkOrderDetails.getLastModifiedBy(),workOrderDetails.getProjectID());
			
			
			oldWorkOrderDetails.setOldStartDate(workOrderDetails.getStartDate());
			oldWorkOrderDetails.setOldWoName(workOrderDetails.getwOName());

			response= saveEditedWorkOrderDetails(oldWorkOrderDetails);

			addAuditDetails(oldWorkOrderDetails);


			LOG.info("updateWorkOrderDetails:End");
		}
		catch(ApplicationException exe) {
			response.addFormMessage(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	
	private void addAuditDetails(WorkOrderCompleteDetailsModel oldWorkOrderDetails) {
		AuditDataModel auditDataModel=new AuditDataModel();
		String woId=Integer.toString(oldWorkOrderDetails.getwOID());
		auditDataModel.setAuditPageId(Integer.valueOf(woId));
		auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
		auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE +woId);
		auditDataModel.setCreatedBy(oldWorkOrderDetails.getLastModifiedBy());
		auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
		auditDataModel.setActorType(AppConstants.ACTOT_TYPE_SP);
		auditDataModel.setCommentCategory(WO_UPDATE_EXTERNAL_WO);
		if(StringUtils.isNotEmpty(oldWorkOrderDetails.getwOName()) && StringUtils.isNotEmpty(oldWorkOrderDetails.getStartDate())) {
			auditDataModel.setFieldName(WO_NAME);
			auditDataModel.setNewValue(oldWorkOrderDetails.getwOName());
			auditDataModel.setOldValue(oldWorkOrderDetails.getOldWoName());
			auditManagementService.addToAudit(auditDataModel);
			
			auditDataModel.setFieldName("StartDate");
			auditDataModel.setNewValue(oldWorkOrderDetails.getStartDate());
			auditDataModel.setOldValue(oldWorkOrderDetails.getOldStartDate());
			auditManagementService.addToAudit(auditDataModel);
		}else if(StringUtils.isNotEmpty(oldWorkOrderDetails.getStartDate())) {
			auditDataModel.setFieldName("StartDate");
			auditDataModel.setNewValue(oldWorkOrderDetails.getStartDate());
			auditDataModel.setOldValue(oldWorkOrderDetails.getOldStartDate());
			auditManagementService.addToAudit(auditDataModel);
		}else if(StringUtils.isNotEmpty(oldWorkOrderDetails.getwOName())) {
			auditDataModel.setFieldName(WO_NAME);
			auditDataModel.setNewValue(oldWorkOrderDetails.getwOName());
			auditDataModel.setOldValue(oldWorkOrderDetails.getOldWoName());
			auditManagementService.addToAudit(auditDataModel);
		}
	}

	private void validateUpdateWorkOrderDetails(WorkOrderCompleteDetailsModel oldWorkOrderDetails) throws ParseException {
		
		//Validate wOID
		validationUtilityService.validateIntForZero(oldWorkOrderDetails.getwOID(),WOID);

		boolean isValidWoid=this.workOrderPlanDao.validateWoid(oldWorkOrderDetails.getwOID());
		if(Boolean.FALSE.equals(isValidWoid)) {
			throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALID,WOID));
		}
		
		//Validate wOName
		if(StringUtils.isNotEmpty(oldWorkOrderDetails.getwOName())) {
        //	validationUtilityService.validateLength(wODetailsModel.getwOName(),400,WO_NAME);
			boolean isValidWOStatusForWOName=this.workOrderPlanDao.validateWOStatusForWOName(oldWorkOrderDetails.getwOID());
			if(Boolean.FALSE.equals(isValidWOStatusForWOName)) {
				throw new ApplicationException(200,String.format(WOSTATUS_NOT_IN_REJECTED_CLOSED_PLANNED_FOR_WO_NAME));
			}
			
			//validate Erisite
			boolean isValidExternalSourceErisite=this.workOrderPlanDao.validateExternalSourceForErisite(oldWorkOrderDetails.getExternalSourceName());
			if(Boolean.TRUE.equals(isValidExternalSourceErisite)) {
				throw new ApplicationException(200, String.format(EXTERNAL_SOURCE_CANNOT_BE_ERISITE));
			}
			
		   String createdBy=this.workOrderPlanDao.getCreatedByOfWOID(oldWorkOrderDetails.getwOID());
		   boolean isValidExternalSourceErisiteWO=this.workOrderPlanDao.validateExternalSourceForErisiteWO(createdBy);
			if(Boolean.TRUE.equals(isValidExternalSourceErisiteWO)) {
				throw new ApplicationException(200, String.format(ERISITE_CREATED_WO_CANNOT_BE_UPDATED));
			}
			
		}
		
		//Validate startDate
		if(StringUtils.isNotEmpty(oldWorkOrderDetails.getStartDate())) {
			
			boolean isValidWOStatusForStartDate=this.workOrderPlanDao.validateWOStatusForStartDate(oldWorkOrderDetails.getwOID());
			if(Boolean.FALSE.equals(isValidWOStatusForStartDate)) {
				throw new ApplicationException(200, String.format(WOSTATUS_IN_ASSIGNED_REOPENED_FOR_START_DATE));
			}
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String inputDate=StringUtils.EMPTY;
			try {
			inputDate = sdf1.format(sdf1.parse(oldWorkOrderDetails.getStartDate()));
			if(Boolean.FALSE.equals(isValidFormat("yyyy-MM-dd HH:mm:ss",inputDate,Locale.ENGLISH))) {
				throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALID,DATE,FORMAT));
		    }
			}catch(Exception e) {
				throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALID,DATE,FORMAT));
			}

			Date todayDate = sdf1.parse(sdf1.format(new Date() ));
			Date startDate=sdf1.parse(inputDate);
			
			if(!(startDate.after(todayDate) || todayDate.equals(startDate)) ){			
				throw new ApplicationException(200, String.format(DATE_NOT_LESS_THAN_TODAY_DATE));
			}
			
		}
		
		   //Validate externalSourceName
		    validationUtilityService.validateStringForBlank(oldWorkOrderDetails.getExternalSourceName(),EXTERNAL_SOURCE);
			boolean isValidSource=this.workOrderPlanDao.validateSource(oldWorkOrderDetails.getExternalSourceName());
			if(Boolean.FALSE.equals(isValidSource)) {
				throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALID,EXTERNAL_SOURCE));
			}			
			
			//Validate lastModifiedBy
			validationUtilityService.validateStringForBlank(oldWorkOrderDetails.getLastModifiedBy(),LAST_MODIFIED_BY);

	 }
	
	private void validateLastModifiedByForPMDR(String signumID,int projectID) {
	boolean isValidLastModifiedBy=this.workOrderPlanDao.validateSignumForPMDR(signumID,projectID);
	if(Boolean.FALSE.equals(isValidLastModifiedBy)) {
		throw new ApplicationException(200, String.format(INVALID_SIGNUM_NOT_PM_DR));
	}
	}
	
	public static boolean isValidFormat(String format, String value, Locale locale) {
	    LocalDateTime ldt = null;
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
	    String result=StringUtils.EMPTY;
	    try {
//	    	OffsetDateTime odt = OffsetDateTime.parse ( value , DateTimeFormatter.ofPattern ( format) ) ;
	    	
	        ldt = LocalDateTime.parse(value, formatter);
	        result = ldt.format(formatter) ;
	        return result.equals(value);
	    } catch (DateTimeParseException e) {
	        try {
	            LocalDate ld = LocalDate.parse(value, formatter);
	            result = ld.format(formatter);
	            return result.equals(value);
	        } catch (DateTimeParseException exp) {
	            try {
	                LocalTime lt = LocalTime.parse(value, formatter);
	                result = lt.format(formatter);
	                return result.equals(value);
	            } catch (DateTimeParseException e2) {
	                // Debugging purposes
	                //e2.printStackTrace();
	            }
	        }
	    }
	    return result.equals(value);
	}
	
	/**
	 * Purose:  This method is replica of create work order.  Created for changes in CNEDB Story.
	 * @param createWorkOrderModel
	 * @param response
	 * @param doID
	 * @param execPlanGroupId
	 * @param execPlanDet
	 * @param woid 
	 * @param listOfNode 
	 * @param listOfNode 
	 */
	
	@Transactional("transactionManager")
	public void createWorkOrderv1(CreateWorkOrderModel createWorkOrderModel, CreateWoResponse response, DOIDModel doID,
			int execPlanGroupId, ExecutionPlanDetail execPlanDet, int woid, List<WorkOrderPlanNodesModel> listOfNode) {

		// work flow sla hours should be added in planned start date of work order
		Date plannedEndDate = PlannedEndDateCal.calculateEndDateV2(createWorkOrderModel.getPlannedStartDate(),
				createWorkOrderModel.getSlaHrs());
		createWorkOrderModel.setPlannedEndDate(plannedEndDate);

		CreateWorkOrderModel2 createModel = new CreateWorkOrderModel2(createWorkOrderModel, doID.getDoID());
		workOrderPlanDao.createWorkOrder(createModel);
		if(CollectionUtils.isNotEmpty(listOfNode)) {
			workOrderPlanDao.createWorkOrderNodes(createModel,listOfNode);
		}
		
		//insert data into TBL_WorkOrder_NECount
		networkElementDao.insertWorkOrderNeCount(createModel,woid);	

		// save file inputs
		insertInputFileWO(createWorkOrderModel, createModel);

		if (execPlanDet.getExecutionPlanDetailId() != 0) {

			prepareAndSaveExecutionPlanFlow(createModel, execPlanDet, execPlanGroupId);
		}

		setResponse(createModel, response);
	}
	
	
	public ResponseEntity<Response<List<ProjectModel>>> getProjectBySignumForProjectQueue(String signum) {
		Response<List<ProjectModel>> response=new Response<>();
		try {
			response.setResponseData(workOrderPlanDao.getProjectBySignumForProjectQueue(signum));
		}catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch (Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	 // Made a New Method for delete WO, It is internally used by externalInterface/v1/updateWorkOrderDetails API. 
	 //	It has some Message change and extra validation Functionality.
	private void deleteWOv1(int wOID, String signumID) {
		boolean checkStatus = workOrderPlanDao.checkNotStartedStatusOfWorkOrder(wOID);
		
		if (checkStatus) {

			workOrderPlanDao.deleteWONodes(wOID);
			workOrderPlanDao.deleteWorkOrder(wOID, signumID);

			int woplan = workOrderPlanDao.getWOPlanByWOID(wOID);

			boolean checkplan = workOrderPlanDao.checkPlanStatus(woplan);
			if (!checkplan) {
				workOrderPlanDao.inactiveWOPlan(woplan);

			}
		} else {
			throw new ApplicationException(500,  WO_STATUS_SHOULD_BE_REOPENED_OR_ASSIGNED_OR_UNASSIGNED_TO_DELETE_WO);
		}
	}


 // Made a New Method for transfer WO, It is internally used by externalInterface/v1/updateWorkOrderDetails API. 
 //	It has some Message change and extra validation Functionality.
	private void transferWO(TransferWorkOrderModel transferWorkOrderModel, Response<Void> workOrderTransferSuccess,
			int woid) {
		WorkOrderModel wodetails = workOrderPlanDao.getWorkOrderDetailsById(woid);
		if (wodetails != null) {

			if (!workOrderPlanDao.checkIfStepInStartedState(woid)) {

				if (StringUtils.equalsIgnoreCase(wodetails.getStatus(), AppConstants.REOPENED)
						|| StringUtils.equalsIgnoreCase(wodetails.getStatus(), AppConstants.ONHOLD)
						|| StringUtils.equalsIgnoreCase(wodetails.getStatus(), AppConstants.INPROGRESS)
						|| StringUtils.equalsIgnoreCase(wodetails.getStatus(),
								AppConstants.WO_STATUS_ASSIGNED)) {
					workOrderPlanDao.transferWorkOrder(woid, transferWorkOrderModel.getSenderID(),
							transferWorkOrderModel.getReceiverID());
//					if (AppConstants.ONHOLD.equals(wodetails.getStatus())) {
//						workOrderPlanDao.updateWorkOrderStatus(woid, transferWorkOrderModel.getSenderID(),
//								AppConstants.WO_STATUS_ASSIGNED);
//					}
					workOrderPlanDao.updateTransferWOLOG(woid, transferWorkOrderModel.getReceiverID(),
							transferWorkOrderModel.getSenderID());
					sendMailForTransferWO(transferWorkOrderModel, woid);
					workOrderTransferSuccess.addFormMessage(WO_TRANSFER_SUCCESS);
				} else {
					throw new ApplicationException(500,
							WO_STATUS_SHOULD_BE_EITHER_REOPENED_ON_HOLD_INPROGRESS_OR_ASSIGNED_OR_UNASSIGNED_TO_TRANSFER_WO);
				}
			} else {
				throw new ApplicationException(500,
						CANNOT_TRANSFER_WORK_ORDER_AS_THE_STATUS_OF_ONE_OF_THE_STEPS_IS_STARTED);
			}
		} else {
			throw new ApplicationException(500, WORK_ORDER_DOESN_T_EXISTS);
		}
	}

	private void sendMailForTransferWO(TransferWorkOrderModel transferWorkOrderModel, int woid) {
		if (configurations.getBooleanProperty(ConfigurationFilesConstants.WORKORDER_MAIL_ENABLED,
				false) && getWoMailNotificationDetails(woid) != null) {
			callMailerForTransferWO(AppConstants.ISF_WO_TRANSFER,
					transferWorkOrderModel.getReceiverID(), transferWorkOrderModel.getSenderID(),
					transferWorkOrderModel.getLogedInSignum(),
					woExecutionService.getNodesByWoId(woid), woid,
					transferWorkOrderModel.getStepName(), transferWorkOrderModel.getUserComments());
		}
	}

	public ResponseEntity<Response<String>> updateWorkOrderDetailsv1(
			WorkOrderCompleteDetailsModel oldWorkOrderDetails) {
		Response<String> response = new Response<>();

		try {
			LOG.info("New Versioning updateWorkOrderDetailsv1 API===================Start");
            prepareOldWorkOrderDetails(oldWorkOrderDetails);
            // Made a new Validation Method as there is some extra validation for inactivateWO Parameter
			validateUpdateWorkOrderDetailsv1(oldWorkOrderDetails);

			WorkOrderCompleteDetailsModel workOrderDetails = wOExecutionDAO
					.getCompleteWorkOrderDetails(oldWorkOrderDetails.getwOID(), AppConstants.ALL).get(0);

			if (StringUtils.isNotEmpty(oldWorkOrderDetails.getwOName())
					|| StringUtils.isNotEmpty(oldWorkOrderDetails.getStartDate())
					|| oldWorkOrderDetails.isInactivateWO()) {
				validateLastModifiedByForPMDR(oldWorkOrderDetails.getLastModifiedBy(), workOrderDetails.getProjectID());
			}
			
			// For delete a WO
			if (oldWorkOrderDetails.isInactivateWO()) {
				deleteWOv1(oldWorkOrderDetails.getwOID(),
						oldWorkOrderDetails.getLastModifiedBy());
				response.addFormMessage(WORK_ORDER_DETAILS_UPDATED_SUCCESSFULLY);
			}

			else {
				// Transfer WO Case
				validateAssignedToSignumandTransferWO(oldWorkOrderDetails, workOrderDetails);
				
				if (StringUtils.isNotEmpty(oldWorkOrderDetails.getwOName())
						|| StringUtils.isNotEmpty(oldWorkOrderDetails.getStartDate())
						|| StringUtils.isNotEmpty(oldWorkOrderDetails.getAssignedToSignum())) {

				oldWorkOrderDetails.setOldStartDate(workOrderDetails.getStartDate());
				oldWorkOrderDetails.setOldWoName(workOrderDetails.getwOName());
				oldWorkOrderDetails.setOldSignumID(workOrderDetails.getSignumID());

				response = saveEditedWorkOrderDetails(oldWorkOrderDetails);

				// Made a new Method for extra validation of auditing transfer and AssignedTo WO case
				addAuditDetailsForupdateWorkOrderDetailsv1(oldWorkOrderDetails);
				}
				else {
					throw new ApplicationException(500,  PLEASE_PROVIDE_AT_LEAST_ONE_PARAMETER_TO_UPDATE);
				}
				

				LOG.info("New Versioning updateWorkOrderDetailsv1 API===================End");
			}
		} catch (ApplicationException exe) {
			response.addFormError(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	private void addAuditDetailsForupdateWorkOrderDetailsv1(WorkOrderCompleteDetailsModel oldWorkOrderDetails) {
		AuditDataModel auditDataModel = new AuditDataModel();
		String woId = Integer.toString(oldWorkOrderDetails.getwOID());
		auditDataModel.setAuditPageId(Integer.valueOf(woId));
		auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
		auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER + AppConstants.UNDERSCORE + woId);
		auditDataModel.setCreatedBy(oldWorkOrderDetails.getLastModifiedBy());
		auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
		auditDataModel.setActorType(AppConstants.ACTOT_TYPE_SP);
		auditDataModel.setCommentCategory(WO_UPDATE_EXTERNAL_WO);
		if (StringUtils.isNotEmpty(oldWorkOrderDetails.getStartDate())) {
			auditDataModel.setFieldName(AppConstants.START_DATE);
			auditDataModel.setNewValue(oldWorkOrderDetails.getStartDate());
			auditDataModel.setOldValue(oldWorkOrderDetails.getOldStartDate());
			auditManagementService.addToAudit(auditDataModel);
		}
		if (StringUtils.isNotEmpty(oldWorkOrderDetails.getwOName())) {
			auditDataModel.setFieldName(WO_NAME);
			auditDataModel.setNewValue(oldWorkOrderDetails.getwOName());
			auditDataModel.setOldValue(oldWorkOrderDetails.getOldWoName());
			auditManagementService.addToAudit(auditDataModel);
		}
		if (StringUtils.isNotEmpty(oldWorkOrderDetails.getAssignedToSignum())) {
			auditDataModel.setFieldName(ASSIGNEE);
			auditDataModel.setNewValue(oldWorkOrderDetails.getAssignedToSignum());
			auditDataModel.setOldValue(oldWorkOrderDetails.getOldSignumID());
			auditManagementService.addToAudit(auditDataModel);
		}

	}

	private void validateAssignedToSignumandTransferWO(WorkOrderCompleteDetailsModel oldWorkOrderDetails,
			WorkOrderCompleteDetailsModel workOrderDetails) {
		if (StringUtils.isNotEmpty(oldWorkOrderDetails.getAssignedToSignum())) {
			validationUtilityService.validateSignumExistsEmpAndNotInResigned(oldWorkOrderDetails.getAssignedToSignum(),
					ASSIGNED_TO_SIGNUM);

			// AssignedTo WO case
			if (StringUtils.isNotEmpty(oldWorkOrderDetails.getAssignedToSignum())
					&& StringUtils.isEmpty(oldWorkOrderDetails.getwOName())
					&& StringUtils.isEmpty(oldWorkOrderDetails.getStartDate())
					&& !oldWorkOrderDetails.isInactivateWO()) {
				if (StringUtils.equalsIgnoreCase(oldWorkOrderDetails.getLastModifiedBy(),
						oldWorkOrderDetails.getAssignedToSignum())) {
					validationUtilityService.validateLastModifiedForPMDRBookedResource(
							oldWorkOrderDetails.getLastModifiedBy(), workOrderDetails.getProjectID());

				}

				// TrasferWo case
				else if (!StringUtils.equalsIgnoreCase(oldWorkOrderDetails.getLastModifiedBy(),
						oldWorkOrderDetails.getAssignedToSignum())) {
					validationUtilityService.validateLastModifiedByForPMDRNE(oldWorkOrderDetails.getLastModifiedBy(),
							workOrderDetails.getProjectID(), oldWorkOrderDetails.getwOID());

				}
			}

			prepareTransferWO(oldWorkOrderDetails);
		}
	}

	private void prepareTransferWO(WorkOrderCompleteDetailsModel oldWorkOrderDetails) {
		TransferWorkOrderModel transferWorkOrderModel = new TransferWorkOrderModel();
		transferWorkOrderModel.setReceiverID(oldWorkOrderDetails.getAssignedToSignum());
		transferWorkOrderModel.setSenderID(oldWorkOrderDetails.getLastModifiedBy());
		transferWorkOrderModel.setStepName(StringUtils.EMPTY);
		transferWorkOrderModel.setUserComments(StringUtils.EMPTY);
		transferWorkOrderModel.setLogedInSignum(oldWorkOrderDetails.getLastModifiedBy());
		Response<Void> workOrderTransferSuccess = new Response<>();
		transferWO(transferWorkOrderModel, workOrderTransferSuccess, oldWorkOrderDetails.getwOID());
	}

	private void validateUpdateWorkOrderDetailsv1(WorkOrderCompleteDetailsModel oldWorkOrderDetails) throws ParseException {
		//Validate wOID
		validationUtilityService.validateIntForZero(oldWorkOrderDetails.getwOID(),WOID);

		boolean isValidWoid=this.workOrderPlanDao.validateWoid(oldWorkOrderDetails.getwOID());
		if(Boolean.FALSE.equals(isValidWoid)) {
			throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALID,WOID));
		}
		
		//Validate wOName
		validateWONameForUpdateWorkOrderDetailsv1(oldWorkOrderDetails);
		
		//Validate startDate
		validateStartDateForUpdateWorkOdrerDetailsv1(oldWorkOrderDetails);
		
		   //Validate externalSourceName
		    validationUtilityService.validateStringForBlank(oldWorkOrderDetails.getExternalSourceName(),EXTERNAL_SOURCE);
			boolean isValidSource=this.workOrderPlanDao.validateSource(oldWorkOrderDetails.getExternalSourceName());
			if(Boolean.FALSE.equals(isValidSource)) {
				throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALID,EXTERNAL_SOURCE));
			}			
			
			//Validate lastModifiedBy
			validationUtilityService.validateStringForBlank(oldWorkOrderDetails.getLastModifiedBy(),LAST_MODIFIED_BY);


		
	}

	private void validateStartDateForUpdateWorkOdrerDetailsv1(WorkOrderCompleteDetailsModel oldWorkOrderDetails)
			throws ParseException {
		if(StringUtils.isNotEmpty(oldWorkOrderDetails.getStartDate()) && !oldWorkOrderDetails.isInactivateWO()) {
			
			boolean isValidWOStatusForStartDate=this.workOrderPlanDao.validateWOStatusForStartDate(oldWorkOrderDetails.getwOID());
			if(Boolean.FALSE.equals(isValidWOStatusForStartDate)) {
				throw new ApplicationException(200, String.format(WOSTATUS_IN_ASSIGNED_REOPENED_FOR_START_DATE));
			}
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String inputDate=StringUtils.EMPTY;
			try {
			inputDate = sdf1.format(sdf1.parse(oldWorkOrderDetails.getStartDate()));
			if(Boolean.FALSE.equals(isValidFormat("yyyy-MM-dd HH:mm:ss",inputDate,Locale.ENGLISH))) {
				throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALID,DATE,FORMAT));
		    }
			}catch(Exception e) {
				throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALID,DATE,FORMAT));
			}

			Date todayDate = sdf1.parse(sdf1.format(new Date() ));
			Date startDate=sdf1.parse(inputDate);
			
			if(!(startDate.after(todayDate) || todayDate.equals(startDate)) ){			
				throw new ApplicationException(200, String.format(DATE_NOT_LESS_THAN_TODAY_DATE));
			}
			
		}
	}

	private void validateWONameForUpdateWorkOrderDetailsv1(WorkOrderCompleteDetailsModel oldWorkOrderDetails) {
		if(StringUtils.isNotEmpty(oldWorkOrderDetails.getwOName())  && !oldWorkOrderDetails.isInactivateWO()) {
        
			boolean isValidWOStatusForWOName=this.workOrderPlanDao.validateWOStatusForWOName(oldWorkOrderDetails.getwOID());
			if(Boolean.FALSE.equals(isValidWOStatusForWOName)) {
				throw new ApplicationException(200,String.format(WOSTATUS_NOT_IN_REJECTED_CLOSED_PLANNED_FOR_WO_NAME));
			}
			
			//validate Erisite
			boolean isValidExternalSourceErisite=this.workOrderPlanDao.validateExternalSourceForErisite(oldWorkOrderDetails.getExternalSourceName());
			if(Boolean.TRUE.equals(isValidExternalSourceErisite)) {
				throw new ApplicationException(200, String.format(EXTERNAL_SOURCE_CANNOT_BE_ERISITE));
			}
			
		   String createdBy=this.workOrderPlanDao.getCreatedByOfWOID(oldWorkOrderDetails.getwOID());
		   boolean isValidExternalSourceErisiteWO=this.workOrderPlanDao.validateExternalSourceForErisiteWO(createdBy);
			if(Boolean.TRUE.equals(isValidExternalSourceErisiteWO)) {
				throw new ApplicationException(200, String.format(ERISITE_CREATED_WO_CANNOT_BE_UPDATED));
			}
			
		}
	}

	private void prepareOldWorkOrderDetails(WorkOrderCompleteDetailsModel oldWorkOrderDetails) {
		oldWorkOrderDetails.setwOName(StringUtils.trim(oldWorkOrderDetails.getwOName()));
		oldWorkOrderDetails.setLastModifiedBy(StringUtils.trim(oldWorkOrderDetails.getLastModifiedBy()));
		oldWorkOrderDetails.setExternalSourceName(StringUtils.trim(oldWorkOrderDetails.getExternalSourceName()));
		oldWorkOrderDetails.setAssignedToSignum(StringUtils.trim(oldWorkOrderDetails.getAssignedToSignum()));
	}
	
}
