/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.AccessManagementDAO;
import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.BotStoreDao;
import com.ericsson.isf.dao.RpaDAO;
import com.ericsson.isf.dao.WOExecutionDAO;
import com.ericsson.isf.dao.WorkOrderPlanDao;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.BookingDetailsModel;
import com.ericsson.isf.model.BotConfig;
import com.ericsson.isf.model.BotInputFileModel;
import com.ericsson.isf.model.BulkWorkOrderCreationModel;
import com.ericsson.isf.model.BulkXlsParsedModel;
import com.ericsson.isf.model.CreateWorkOrderModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.ErrorDetailsModel;
import com.ericsson.isf.model.ExceptionLogModel;
import com.ericsson.isf.model.FcStepDetails;
import com.ericsson.isf.model.RPABookingModel;
import com.ericsson.isf.model.RPAWorkOrderDetails;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.SaveClosureDetailsForWOModel;
import com.ericsson.isf.model.SignalrModel;
import com.ericsson.isf.model.SoftHumanDumpModel;
import com.ericsson.isf.model.StepDetailsModel;
import com.ericsson.isf.model.TblRpaDeployedBotModel;
import com.ericsson.isf.model.TestingBotDetailsModel;
import com.ericsson.isf.model.VideoURLModel;
import com.ericsson.isf.model.WoAutoTaskInfoModel;
import com.ericsson.isf.model.WorkOrderBookingDetailsModel;
import com.ericsson.isf.model.WorkOrderModel;
import com.ericsson.isf.model.WorkOrderPlanNodesModel;
import com.ericsson.isf.model.WorkOrderPlanObjectModel;
import com.ericsson.isf.model.botstore.RpaApiResponse;
import com.ericsson.isf.model.botstore.ServerBotModel;
import com.ericsson.isf.model.botstore.TblRpaBotstaging;
import com.ericsson.isf.model.botstore.TblRpaBottesting;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.ericsson.isf.util.PlannedEndDateCal;
import com.microsoft.azure.storage.StorageException;

/**
 *
 * @author ekumvsu
 */
@Service
public class RpaService {

	private static final String SUB_ACTIVITY_ID = "SubActivityID";
	private static final String PLEASE_PROVIDE_VALUE = "Please provide valid %s!";
	private static final String BOT_EXECUTION_STARTED = "Bot Execution Started";
	private static final String SERVER = "server";
	@Autowired
	private WOExecutionDAO wOExecutionDAO;
	@Autowired
	private WorkOrderPlanDao workOrderPlanDao;
	@Autowired
	private RpaDAO rpaDAO;

	@Autowired
	private ApplicationConfigurations configurations;
	@Autowired
	BotStoreDao botStoreDAO;

	@Autowired
	private WorkOrderPlanService workOrderPlanService;

	@Autowired
	OutlookAndEmailService emailService;

	@Autowired
	ActivityMasterDAO activityMasterDAO;
	@Autowired
	ProjectService projectService;

	@Autowired
	WOExecutionService woExecutionService;

	@Autowired
	BotStoreService botStoreService;
	
	@Autowired
	private AppService appService;

	@Autowired
	private AccessManagementDAO accessManagementDAO;

	@Autowired
	private ActivityMasterService activityMasterService;

	@Autowired
	private ValidationUtilityService validationUtilityService;

	@Autowired
	private ExternalInterfaceManagmentService externalInterfaceManagmentService;

	private static final Logger LOG = LoggerFactory.getLogger(RpaService.class);
	private static final String RESPONSE_SUCCESS = "Success";
	private static final String RESPONSE_FAILED = "Failed";
	private static final String APIM_SUB_KEY="AzApimSubscriptionKey";
	private static final String NO_DETAILS_EXISTS= "No Details Exists for given botID";
	
	private static final String ERROR_TYPE = "errorType";
	private static final String ERROR_MESSAGE = "errorMessage";
	private static final String ERROR_DETAIL_ADDED_SUCCESSFULLY = "Error has been added successfully!";
	private static final String ERROR_DETAIL_NOT_ADDED_SUCCESSFULLY = "Error not added successfully!";
	private static final String PROVIDE_VALID_SIGNUM = "Please provide valid Signum!";
	private static final String PROVIDE_VALID_SOURCE = "Please provide valid Source!";
	private static final String ERROR_DETAIL_ALREADY_EXISTS = "This Error Detail already exists for given source!";
	private static final String SOURCE_ID = "sourceID";

	@Transactional("transactionManager")
	public String createRPAWorkOrder(int projectID, int subActivityID, String signumID, String nodeType,
			String nodeNames, String dateString, String woName) {
		Map<String, Integer> wfData = workOrderPlanDao.getWorkFlowVersion(projectID, subActivityID);
		int scopeID = rpaDAO.getScopeID(projectID, subActivityID);
		String woid = "";
		if (StringUtils.isBlank(signumID)) {
			signumID = "NULL";
		}
		if (!"NULL".equalsIgnoreCase(signumID)) {
			Boolean checkEmpTbl = this.accessManagementDAO.isSignumExistsEmp(signumID);
			if (!checkEmpTbl) {
				woid = "Signum is not valid,Please Enter valid Signum";
				return woid;
			}
		}
		List<String> lstSignum = new ArrayList<>();
		if (signumID != null && !"NULL".equalsIgnoreCase(signumID)) {
			lstSignum.add(signumID);
		}
		WorkOrderPlanNodesModel model = new WorkOrderPlanNodesModel();
		model.setNodeType(nodeType);
		model.setNodeNames(nodeNames);
		List<WorkOrderPlanNodesModel> listOfNodes = new ArrayList<>();
		listOfNodes.add(model);
		Date date = null;
		String newDate = "";
		String newTime = "";
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
			newDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
			newTime = new SimpleDateFormat("HH:mm:ss").format(date);
		} catch (ParseException ex) {
			LOG.info("Error:" + ex.getMessage());
		}

		CreateWorkOrderModel woPlanModel = new CreateWorkOrderModel();
		woPlanModel.setProjectID(projectID);
		woPlanModel.setSubActivityID(subActivityID);
		woPlanModel.setScopeID(scopeID);
		woPlanModel.setStartDate(newDate);
		woPlanModel.setStartTime(Time.valueOf(newTime));
		woPlanModel.setwOName(woName);
		woPlanModel.setLstSignumID(lstSignum);
		woPlanModel.setCreatedBy("GNET");
		woPlanModel.setPriority("Normal");
		woPlanModel.setType("AUTO");
		woPlanModel.setListOfNode(listOfNodes);
		woPlanModel.setPeriodicityDaily(null);
		woPlanModel.setPeriodicityWeekly(null);
		woPlanModel.setExecutionPlanId(0);
		woPlanModel.setWfVersion(wfData.get("VersionNumber"));
		woPlanModel.setFlowchartDefId(wfData.get("SubActivityFlowChartDefID"));
		if (woPlanModel.getLstSignumID().isEmpty()) {
			woPlanModel.setWoCount(1);
		}
		/*
		 * WorkOrderPlanService.CreateWoResponse response =
		 * this.workOrderPlanService.createWorkOrderPlan(woPlanModel); int woID =
		 * rpaDAO.getWorkOrderID(projectID, subActivityID, scopeID, woName,
		 * wfData.get("VersionNumber"), response.woPlanId, "AUTO");
		 * woid="Work Order is Created with WOID : "+Integer.toString(woID);
		 */
		return woid;
	}

	private static final String PRIORITY_NORMAL = "Normal";

	@Transactional("transactionManager")
	public Map<String, Object> createAutomatedWorkOrder(BulkWorkOrderCreationModel bulkModel) {

		Map<String, Object> response = new HashMap<>();
		boolean IsActive = false;

		try {

			IsActive = projectService.checkIfProjectActive(bulkModel.getProjectID());
			if (IsActive) {

				response = createBulkWorkOrderModel(bulkModel);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.info("createAutomatedWorkOrder issue:" + ex.getMessage());
			response.put("Failed", "Unable to create the WorkOrder");
		}
		return response;
	}

	private Map<String, Object> createBulkWorkOrderModel(BulkWorkOrderCreationModel bulkModel) {
		Map<String, Integer> wfData = new HashMap<>();
		Map<String, Object> response = new HashMap<>();
		Integer executionPlanID = 0;
		int sourceID = 0;
		if (bulkModel.getPriority() == null) {
			bulkModel.setPriority(PRIORITY_NORMAL);
		}

		if (bulkModel.getSubActivityID() != 0
				&& (bulkModel.getWorkFlowName().equalsIgnoreCase("NULL") || bulkModel.getWorkFlowName() == null)) {
			wfData = rpaDAO.getWorkFlowData(bulkModel.getProjectID(), bulkModel.getSubActivityID());
			bulkModel.setWorkFlowName(wfData.get("WorkFlowName").toString());
		}
		if (StringUtils.isBlank(bulkModel.getExecutionPlan())) {

			bulkModel.setExecutionPlan(
					StringUtils.isBlank(bulkModel.getScope_name()) ? StringUtils.EMPTY : bulkModel.getScope_name());
		}

		executionPlanID = rpaDAO.getExecutionPlan(bulkModel.getExecutionPlan(), bulkModel.getProjectID());
		if (executionPlanID == null) {
			executionPlanID = 0;
		}

		if (StringUtils.isBlank(bulkModel.getSource())) {

			bulkModel.setSource("ISF");
		}
		sourceID = rpaDAO.getExternalSource(bulkModel.getSource());
		if (StringUtils.isBlank(bulkModel.getCreatedBy())) {

			bulkModel.setCreatedBy(bulkModel.getSource());
		}
		try {
			rpaDAO.insertBulkWorkOrderDetails(bulkModel, sourceID, executionPlanID);
			rpaDAO.insertBulkWorkOrderHistory(bulkModel);
			response.put("Success",
					"Data saved," + bulkModel.getUploadedBy() + " will be notified once Work order is created ");

		} catch (Exception e) {
			response.put("Failed", "Data can not be saved");
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
		return response;
	}

	private static final String WO_STATUS_CLOSED = "closed";

	@Transactional("transactionManager")
	public String startAutomatedTask(int woID, String task, String dateStr){
		String bookingID = "";
		String referer = "GNET";
	try {

		Integer sourceID = rpaDAO.getExternalSource(referer);
			WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(woID);
			
			// checking work order detail in db
			if (woDetails == null) { 
				//throw new ApplicationException(200, "No record found for this given Work_order id:");
				return "No record found for this given Work_order id:";
			}
			
			//setProficiencyID
			if(woDetails.getStatus().equalsIgnoreCase(AppConstants.WO_STATUS_ASSIGNED)
					|| woDetails.getStatus().equalsIgnoreCase(AppConstants.ONHOLD)
					|| woDetails.getStatus().equalsIgnoreCase(AppConstants.WO_STATUS_REPOENED)) {
				
				int proficiencyID=0;
				
				if(woDetails.getProficiencyID()==null) {
					proficiencyID	=	appService.getProficiencyID(AppConstants.ASSESSED).getProficiencyID();
				}
				else {
					proficiencyID= -1;
				}
				//if user create WO but not assign anyone so status will be by default Assigned.
				if (StringUtils.isBlank(woDetails.getSignumID())) {
					//return bookingID = "Work Order Not Assigned Yet,Please Assign before start";
					return "Work Order Not Assigned Yet,Please Assign before start";
				}
				
				else {
				wOExecutionDAO.updateStatusandProficiencyId(woID,woDetails.getSignumID(),proficiencyID,(woDetails.getActualStartDate()==null)?true:false);
				}
			}
			
			if (WO_STATUS_CLOSED.equalsIgnoreCase(woDetails.getStatus())) {
				throw (new ApplicationException(200, "Work order is closed!"));
			}
			//if (StringUtils.isBlank(woDetails.getSignumID())) {
				//return bookingID = "Work Order Not Assigned Yet,Please Assign before start";
				//return "Work Order Not Assigned Yet,Please Assign before start";
			//}
			//validation to check for already started booking in same WO
			boolean isTaskStarted = wOExecutionDAO.checkStartedTaskInBooking(woID);
			if (isTaskStarted) {
				//throw new ApplicationException(500, "A Step is already started for WOID : " + woID);
				return "A Step is already started for WOID : " + woID;
			}
			Date date = PlannedEndDateCal.convertStringToDate(dateStr, AppConstants.UI_DATE_FORMAT);
			Map<String, Integer> data = this.rpaDAO.getProjAndSubActivityID(woID);
			// checking sub activity for given Work order
			if (data.get(SUB_ACTIVITY_ID) == null) {
				//throw new ApplicationException(500, "No SubActivity found for this Work Order");
				return "No SubActivity found for this Work Order";
			}
	
				Integer taskID = this.rpaDAO.getTaskID(data.get(SUB_ACTIVITY_ID), task.trim());
				if(taskID==null) {
					return "No taskId found for this given work order id";
				}
				this.rpaDAO.updateWOStatus(woID);

				String signumID = this.rpaDAO.getWOSignumID(woID);

				this.rpaDAO.startTask_bookingDetails(woID, taskID, date, 0, signumID, sourceID,SERVER,BOT_EXECUTION_STARTED);
				bookingID = this.rpaDAO.getBookingID(woID);

					saveStepDetails(woID, taskID, data.get("ProjectID"), data.get(SUB_ACTIVITY_ID), "STARTED",
							Integer.valueOf(bookingID));

	}catch (Exception e) {
		LOG.error(e.getMessage());
		bookingID=e.getMessage();
		return bookingID;
	}			

		return bookingID;
	}

	@Transactional("transactionManager")
	public String closeRpaWO(int woID, String signumID, String closedOn) {

		String response = "";
		String actualEndDate = "";
		closedOn = PlannedEndDateCal.convertDateToString(new Date(), AppConstants.UI_DATE_FORMAT);
		Boolean checkWOID = this.rpaDAO.checkIFWOIDExists(woID);
		if (checkWOID) {
			List<WorkOrderBookingDetailsModel> woBookingDetails = rpaDAO.getWorkOrderBookingDetailsById(woID);
			for (WorkOrderBookingDetailsModel workOrderBookingDetails : woBookingDetails) {
				if (!AppConstants.COMPLETED.equalsIgnoreCase(workOrderBookingDetails.getStatus())
						&& !AppConstants.CLOSED.equalsIgnoreCase(workOrderBookingDetails.getStatus())) {
					response = "Failed:Please complete all steps before closing work order.";
					return response;
				} else if (workOrderBookingDetails.getStatus().equalsIgnoreCase(WO_STATUS_CLOSED)) {
					response = "Work Order is Already CLOSED";
					return response;

				}
			}
			List<Map<String, Object>> stepDetails = workOrderPlanDao.getStepBookingDetailsByWoid(woID);
			//List<FcStepDetails> stepDetails = workOrderPlanDao.getStepBookingDetailsByWoid(woID);
			for (Map<String, Object> sd : stepDetails) {
				if (!AppConstants.COMPLETED.equalsIgnoreCase(sd.get("Status").toString())
						&& !AppConstants.CLOSED.equalsIgnoreCase(sd.get("Status").toString())) {
					response = "Failed:Please complete all steps before closing work order.";
					return response;
				}

			}
			WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(woID);
			if (StringUtils.isBlank(woDetails.getSignumID())) {
				response = "Work Order Not Assigned Yet";
				return response;

			}
			if (woDetails.getStatus().equalsIgnoreCase(WO_STATUS_CLOSED)) {
				response = "Work Order is Already CLOSED";
				return response;

			}
			if (stepDetails.isEmpty()) {
				actualEndDate = closedOn;
			}
			this.rpaDAO.closeRpaWO(woID, signumID.trim(), closedOn, actualEndDate);
			List<String> woplan = this.rpaDAO.getWOPlanbyWOID(woID);
			SaveClosureDetailsForWOModel saveClosureDetailsObject = new SaveClosureDetailsForWOModel();
			String signum = wOExecutionDAO.getProjectCreatorSignum(woID);
			saveClosureDetailsObject.setAcceptedOrRejectedBy(signum);
			saveClosureDetailsObject.setwOID(woID);
			saveClosureDetailsObject.setwOName(woplan.get(0));
			saveClosureDetailsObject.setLastModifiedBy(signumID.trim());
			this.wOExecutionDAO.saveClosureDetails_InsertDA(saveClosureDetailsObject);
			response = "WorkOrder Successfully Closed";

		} else {
			response = "Failed:Given Work OrderID doesnot Exist";
		}
		return response;
	}

	@Transactional("transactionManager")
	public String completeAutomatedTask(int woID, String task, String stringDate, String reason){
		String response = "";
		try {
			if (reason == null || reason.isEmpty()) {
				reason = "GNET : No Response Received from Bot";
			}
			WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(woID);
			if(woDetails==null) {
				response="No Details found for this given woId";
				return response;
			}
			if (WO_STATUS_CLOSED.equalsIgnoreCase(woDetails.getStatus())) {
				try {
					throw (new ApplicationException(500, "Work order is closed!"));
				}catch (Exception e) {
					response=e.getMessage();
					return response;
				}
			}
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = new Date();
			String stringCurrentDate = dateFormat.format(currentDate);
			Date date = PlannedEndDateCal.convertStringToDate(stringCurrentDate, "yyyy-MM-dd HH:mm:ss");
			RPABookingModel taskDetails = rpaDAO.getRPATaskID(woID, task.trim());
			String taskStartTime = taskDetails.getStartDate();
			Date startDate = PlannedEndDateCal.convertStringToDate(taskStartTime, "yyyy-MM-dd HH:mm:ss");
			long timeDiff = date.getTime() - startDate.getTime();
			double hours = (double) timeDiff / (3600 * 1000);
			if (hours < 0.0001) {
				hours = 0.0001;
			}

			String signumID = this.rpaDAO.getWOSignumID(woID);
			String botPlateform=StringUtils.EMPTY;
	           if(StringUtils.equalsIgnoreCase(taskDetails.getType(), "Booking")) {
	        	   botPlateform=SERVER;
	           }
	           rpaDAO.updateHoursForRPA(taskDetails.getBookingID(), hours, date, reason, signumID,botPlateform);
			Map<String, Integer> data = this.rpaDAO.getProjAndSubActivityID(woID);
			saveStepDetails(woID, taskDetails.getTaskID(), data.get("ProjectID"), data.get(SUB_ACTIVITY_ID),
					"COMPLETED", taskDetails.getBookingID());

			response = "Successfully completed the automatedTask";
		}catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		}	
		return response;
	}

	public List<RPAWorkOrderDetails> getRPAWorkOrderDetails(int projectID) {
		return this.rpaDAO.getRPAWorkOrderDetails(projectID);
	}

	private void saveStepDetails(int woID, int taskID, int projectID, int subActivityID, String status, int bookingID) {
		int versionNo = this.rpaDAO.getWOVersionNo(woID);
		String signumID = this.rpaDAO.getWOSignumID(woID);
		Integer flowchartDefID = this.rpaDAO.getFlowChartDefinitionID(projectID, subActivityID, versionNo, woID);
		if (flowchartDefID == null) {
			throw new ApplicationException(500, "Flow Chart Definition found Inactive for, this given work order id");
		}
		List<String> stepID = this.rpaDAO.getFlowChartStepID(flowchartDefID, taskID);
		for (String step : stepID) {
			StepDetailsModel stepDetailsModel=prepareDataForStepDetailModel(woID, taskID, bookingID, step, flowchartDefID, status, signumID,
					"Automatic");
			wOExecutionDAO.addStepDetailsForFlowChart(stepDetailsModel);
		}

	}
	
	private StepDetailsModel prepareDataForStepDetailModel(int woID, int taskID, int bookingID, String stepID,
			Integer flowchartdefid, String status, String signumID, String exeType) {
		StepDetailsModel stepDetailsModel =new StepDetailsModel();
		stepDetailsModel.setWoId(woID);
		stepDetailsModel.setTaskID(taskID);
		stepDetailsModel.setBookingId(bookingID);
		stepDetailsModel.setFlowChartStepId(stepID);
		stepDetailsModel.setFlowChartDefID(flowchartdefid);
		stepDetailsModel.setStatus(status);
		stepDetailsModel.setSignumId(signumID);
		stepDetailsModel.setExecutionType(exeType);
		stepDetailsModel.setDecisionValue(StringUtils.EMPTY);
		return stepDetailsModel;
	}


	public List<SoftHumanDumpModel> getRPADeployedDetails(int projectID) {
		return this.rpaDAO.getRPADeployedDetails(projectID);
	}

	private static final String INVALID_EMAIL = "Invalid email id. Please verify your email in ISF.";

	public void processBulkWorkOrderAttachement(byte[] fileData, String fromEmail, String filename) {
		String error = "";
		BulkXlsParsedModel response = null;
		EmployeeModel empDetails = null;
		try {
			empDetails = activityMasterDAO.getEmployeeByEmail(fromEmail);
		} catch (Exception e) {
			error = INVALID_EMAIL;
		}
		if (empDetails != null) {
			try {
				response = parseXls(fileData, 0, EXEC_PLAN_SOURCE_BULK_EMAIL);
				List<BulkWorkOrderCreationModel> processedData = response.getParsedData();
				for (BulkWorkOrderCreationModel buldWoInfo : processedData) {
					
					if(StringUtils.isNoneBlank(buldWoInfo.getAssignTO())) {
						if(StringUtils.equalsIgnoreCase(buldWoInfo.getAssignTO(), AppConstants.CH_ZERO)) {
							buldWoInfo.setAssignTO(AppConstants.NULL);
						}else {
							buldWoInfo.setAssignTO(buldWoInfo.getAssignTO().trim());
						}	
					} 
					
					buldWoInfo.setUploadedBy(empDetails.getSignum());
					createAutomatedWorkOrder(buldWoInfo);

				}
			} catch (ApplicationException ae) {
				error = ae.getErrorMessage();
			} catch (InvalidFormatException | POIXMLException e) {
				error = "Invalid file format.";
			} catch (Exception e) {
				LOG.error("", e);
				error = "Error processing attachement. Please validate the file as per template.";
			}
		} else {
			error = INVALID_EMAIL;
		}

		Map<String, Object> placeholders = new HashMap<>();
		placeholders.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, fromEmail);
		if (!"".equals(error)) {
			placeholders.put("error", error);
		}
		placeholders.put("processingDate", new Date());
		placeholders.put("filename", filename);
		if (response != null && response.getParsedData() != null) {
			placeholders.put("processedRecords", response.getParsedData().size());
		} else {
			placeholders.put("processedRecords", "-");
		}
//		if(configurations.getBooleanProperty(ConfigurationFilesConstants.WORKORDER_MAIL_ENABLED, false)) {
			emailService.sendMail(AppConstants.NOTIFICATION_ID_BULK_WO_EMAIL, AppConstants.NOTIFICATION_SUB_BULK_WO_EMAIL,
					placeholders);
//		}

	}
	private static final String EXEC_PLAN_SOURCE_BULK_EMAIL = "BULK_EMAIL";
	private static final String EXEC_PLAN_SOURCE_EMAIL = "EMAIL";
	private static final String FIRST_COL_HEADER = "ProjectID";
	private static final String SECOND_COL_HEADER = "Deliverable Plan Name";
	private static final String THIRD_COL_HEADER = "Node Name";
	private static final String FOURTH_COL_HEADER = "Node Type";
	private static final String FIFTH_COL_HEADER = "Start date";
	private static final String SIXTH_COL_HEADER = "Start Time";
	private static final String SEVENTH_COL_HEADER = "Priority";
	private static final String EIGHT_COL_HEADER = "AssignTo";
	private static final String NINTH_COL_HEADER = "Comment";
	private static final String TENTH_COL_HEADER = "WoName";
	private static final String ELEVENTH_COL_HEADER = "InputName";
	private static final String TWELTH_COL_HEADER = "InputUrl";
	// private static final String THIRTEENTH_COL_HEADER = "DoVolume";
	// private static final String FOURTEENTH_COL_HEADER = "ExternalSourceName";

	public BulkXlsParsedModel parseXls(byte[] fileData, int projectID, String source) throws Exception {
		List<BulkWorkOrderCreationModel> processedData = new ArrayList<>();
		XSSFWorkbook wBook = new XSSFWorkbook(new ByteArrayInputStream(fileData));
		Map<String, String> validationRecords = new HashMap<>();
		List<String> columnList = new LinkedList<String>();
		columnList.add(FIRST_COL_HEADER);
		columnList.add(SECOND_COL_HEADER);
		columnList.add(THIRD_COL_HEADER);
		columnList.add(FOURTH_COL_HEADER);
		columnList.add(FIFTH_COL_HEADER);
		columnList.add(SIXTH_COL_HEADER);
		columnList.add(SEVENTH_COL_HEADER);
		columnList.add(EIGHT_COL_HEADER);
		columnList.add(NINTH_COL_HEADER);
		columnList.add(TENTH_COL_HEADER);
		columnList.add(ELEVENTH_COL_HEADER);
		columnList.add(TWELTH_COL_HEADER);
		// columnList.add(THIRTEENTH_COL_HEADER);
		// columnList.add(FOURTEENTH_COL_HEADER);

		for (int i = 0; i < wBook.getNumberOfSheets(); i++) {

			XSSFSheet sheet = wBook.getSheetAt(i);
			Map<String, Cell> data;
			Row row;
			Iterator<Row> rowIterator = sheet.iterator();
			boolean isDataStart = false;
			boolean isColumnSequence = false;
			
			int rowId = 0;
			while (rowIterator.hasNext()) {
				rowprocess: {
					rowId++;
					row = rowIterator.next();
					data = new HashMap<>();

					if (row.getLastCellNum() < (Integer.parseInt(INPUT_URL_INDEX) + 1)) {
						throw new ApplicationException(500,
								"Columns missing in file, number of columns are fixed as per template file");
					}
					int cn = 0;
					
					// Check Column Sequence
					Cell cell = row.getCell(cn, row.RETURN_BLANK_AS_NULL);
					Cell cell1 = row.getCell(cn + 1, row.RETURN_BLANK_AS_NULL);
					if (cell != null && FIRST_COL_HEADER.equalsIgnoreCase(cell.toString()) && cell1 != null
							&& SECOND_COL_HEADER.equalsIgnoreCase(cell1.toString())) {

						while (cn < row.getLastCellNum()) {
							cell = row.getCell(cn, row.RETURN_BLANK_AS_NULL);
							if (cell != null && columnList.get(cn).equalsIgnoreCase(cell.toString())) {
								isColumnSequence = true;
							} else
								isColumnSequence = false;
							cn++;
							if (!isColumnSequence)
								break;
						}
					}

					for (cn = 0; cn < row.getLastCellNum(); cn++) {
						cell = row.getCell(cn, row.RETURN_BLANK_AS_NULL);						
						if (!isDataStart && cell != null && FIRST_COL_HEADER.equalsIgnoreCase(cell.toString())) {

							isDataStart = true;
							break rowprocess;
						}
						// code to get exact value if cell contains the Formula
						if(cell != null && cell.getCellType()==cell.CELL_TYPE_FORMULA) {
//							System.out.println("Formula is " + cell.getCellFormula());
							switch(cell.getCachedFormulaResultType()) {
								case Cell.CELL_TYPE_NUMERIC:
//									System.out.println("Last evaluated as: " + cell.getNumericCellValue());
									row.removeCell(cell);
									Cell updateCell=row.createCell(cn);
									updateCell.setCellValue(cell.getNumericCellValue());
//									System.out.println("cell1: "+cell);
									data.put(cn + "", updateCell);
									break;
								case Cell.CELL_TYPE_STRING:	
//									System.out.println("Last evaluated as: " + cell.getStringCellValue());
									row.removeCell(cell);
									Cell updateCell2=row.createCell(cn);
									updateCell2.setCellValue(cell.getStringCellValue());
//									System.out.println("cell1: "+cell);
									data.put(cn + "", updateCell2);
									break;
									
							}
						}
						
						
						
						if (isDataStart && cell != null) {
							if(cn == 2 ) {
								cell.setCellType(Cell.CELL_TYPE_STRING);
							}
							if (cn == 2  && cell.getStringCellValue().length() > 500) {
									String cellString = StringUtils.substring(cell.getStringCellValue(), 0, 500);
									cellString = StringUtils.substring(cellString, 0, cellString.lastIndexOf(","));
									cell.setCellValue(cellString);
									LOG.info(
											"Node name column contains more than 500 characters hence value will be truncated after 500 characters!");

							}
							else if (cn == 0 && cell.getCellType() == Cell.CELL_TYPE_NUMERIC && cell.getNumericCellValue() != 0) {
								if (StringUtils.equalsIgnoreCase(source, EXEC_PLAN_SOURCE_BULK_EMAIL)) {
									projectID = (int) cell.getNumericCellValue();
								}
							}
							else if (cn == 10) {
								workOrderPlanService.doLenthValidationForInputName(cell.getStringCellValue());
							}
							else if (cn == 11) {
								workOrderPlanService.doLenthValidationForInputUrl(cell.getStringCellValue());
							}
							else if (cn == 9) {
								workOrderPlanService.doLenthValidationForWoName(cell.getStringCellValue());
							}
							else if (cn == 8 && StringUtils.isNotBlank(cell.getStringCellValue())) {
								workOrderPlanService.doLenthValidationForComment(cell.getStringCellValue());
							}
							
							if(cell.getCellType()!=cell.CELL_TYPE_FORMULA) {
								data.put(cn + "", cell);	
							}
							
							
							
						}
						else if(cell==null && cn==2){
							throw new ApplicationException(500,
									"Node name can not be null");
						}
						
						else if(cell==null && cn==3){
							throw new ApplicationException(500,
									"Node type can not be null");
						}
						
						
					}
					if (isDataStart && data.size() > 0) {
						if (data.get(INPUT_URL_INDEX) != null) {

							Map<String, Object> map = activityMasterService
									.isValidTransactionalData(data.get(INPUT_URL_INDEX).toString(), projectID);
							if (!(boolean) map.get("result")) {
								throw new ApplicationException(500, map.get("message").toString());
							}
						}

						processedData.add(mapFields(data, wBook, projectID, source));
					}
				}
			}
			if (!isColumnSequence) {
				throw new ApplicationException(500,
						"Columns sequence is incorrect, sequence of columns is fixed as per template file");
			}
		}

		BulkXlsParsedModel response = new BulkXlsParsedModel();
		response.setParsedData(processedData);
		response.setValidationData(validationRecords);
		return response;
	}

	SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
	SimpleDateFormat TIMEFORMAT = new SimpleDateFormat("HH:mm");

	private static final String PROJECT_ID_INDEX = "0";
	private static final String PLAN_NAME_INDEX = "1";
	private static final String NODE_NAME_INDEX = "2";
	private static final String NODE_TYPE_INDEX = "3";
	private static final String START_DATE_INDEX = "4";
	private static final String START_TIME_INDEX = "5";
	private static final String PRIORITY_INDEX = "6";
	private static final String ASSIGN_TO_INDEX = "7";
	private static final String COMMENT_INDEX = "8";
	private static final String WONAME_INDEX = "9";
	private static final String INPUT_NAME_INDEX = "10";
	private static final String INPUT_URL_INDEX = "11";
	// private static final String DO_VOLUME_INDEX = "12";
	// private static final String EXTERNAL_SOURCE_NAME_INDEX = "13";
	private static final String BOT_CONFIG_SUCCESS = "Details updated successfully";
	private static final String BOT_CONFIG_FAIL = "Invalid details! Please check the details and then try again.";

	private BulkWorkOrderCreationModel mapFields(Map<String, Cell> mapData, Workbook wBook, int projectID, String source) {
		BulkWorkOrderCreationModel data = new BulkWorkOrderCreationModel();
		if (projectID != 0 && (int) Double.parseDouble(mapData.get(PROJECT_ID_INDEX).toString()) != projectID) {
			throw new ApplicationException(500,
					"Project ID provided in file is not valid, Only Project ID - " + projectID
							+ " is valid, Incorrect Project ID is - "
							+ (int) Double.parseDouble(mapData.get(PROJECT_ID_INDEX).toString()));
		}
		data.setProjectID((int) Double.parseDouble(mapData.get(PROJECT_ID_INDEX).toString()));

		if (mapData.get(NODE_NAME_INDEX) != null) {
			data.setNodeNames(mapData.get(NODE_NAME_INDEX).toString());
		}

		if (mapData.get(NODE_TYPE_INDEX) != null) {
			data.setNodeType(mapData.get(NODE_TYPE_INDEX).toString());
		}
		if (mapData.get(PRIORITY_INDEX) != null) {
			data.setPriority(mapData.get(PRIORITY_INDEX).toString());
		}
		if (mapData.get(COMMENT_INDEX) != null) {
			data.setComment(mapData.get(COMMENT_INDEX).toString());
		}
		if (mapData.get(ASSIGN_TO_INDEX) != null) {
			data.setAssignTO(mapData.get(ASSIGN_TO_INDEX).toString());
		}
		if (mapData.get(WONAME_INDEX) != null) {
			data.setWoName(mapData.get(WONAME_INDEX).toString());
		}
		if (mapData.get(WONAME_INDEX) == null) {
			data.setWoName(mapData.get(PLAN_NAME_INDEX).toString());
		}
		if (mapData.get(INPUT_NAME_INDEX) != null) {
			data.setInputName(mapData.get(INPUT_NAME_INDEX).toString());
		}
		if (mapData.get(INPUT_URL_INDEX) != null) {
			data.setInputUrl(mapData.get(INPUT_URL_INDEX).toString());

		}
		/*
		 * if (mapData.get(DO_VOLUME_INDEX) != null) { data.setDoVolume((int)
		 * Double.parseDouble(mapData.get(DO_VOLUME_INDEX).toString())); } if
		 * (mapData.get(EXTERNAL_SOURCE_NAME_INDEX) != null) {
		 * data.setExternalSourceName(mapData.get(EXTERNAL_SOURCE_NAME_INDEX).toString()
		 * ); }
		 */
		data.setExecutionPlan(mapData.get(PLAN_NAME_INDEX).toString());
		data.setSource(source);
		data.setCreatedBy(source);
		Date startDate = null;

		if (mapData.get(START_DATE_INDEX).getCellType() == Cell.CELL_TYPE_FORMULA) {
			FormulaEvaluator evaluator = wBook.getCreationHelper().createFormulaEvaluator();
			CellValue evaluate = evaluator.evaluate(mapData.get(START_DATE_INDEX));
			Date date = DateUtil.getJavaDate(evaluate.getNumberValue());
			startDate = date;
		} else {
			startDate = mapData.get(START_DATE_INDEX).getDateCellValue();
		}

		Date startTime = mapData.get(START_TIME_INDEX).getDateCellValue();
		data.setStartTime(TIMEFORMAT.format(startTime));
		data.setStartDate(FILE_DATE_FORMAT.format(startDate));

		return data;
	}

	private String validate(Map<String, Cell> mapData) {
		String error = "";
		if (mapData.get(PROJECT_ID_INDEX) == null || "".equals(mapData.get(PROJECT_ID_INDEX))) {
			error += "No Project id sppecified.\n";
		}
		if (mapData.get(PLAN_NAME_INDEX) == null || "".equals(mapData.get(PLAN_NAME_INDEX))) {
			error += "No execution plan sppecified.\n";
		}
		boolean isStartDate = true, isStartTime = true;
		if (mapData.get(START_DATE_INDEX) == null || "".equals(mapData.get(START_DATE_INDEX))) {
			error += "No Start Date sppecified.\n";
			isStartDate = false;
		}
		if (mapData.get(START_TIME_INDEX) == null || "".equals(mapData.get(START_TIME_INDEX))) {
			error += "No Start time sppecified.\n";
			isStartTime = false;
		}

		if (isStartDate && isStartTime) {
			try {
				mapData.get(START_DATE_INDEX).getDateCellValue();
				mapData.get(START_TIME_INDEX).getDateCellValue();
			} catch (Exception e) {
				error += "Invalid Start Date/Start Time.\n";
			}
		}

		return error;
	}

	private static final String REF_ID_TEST = "TEST_";

	@Transactional("transactionManager")
	public Map<String, String> stopStepByReferenceId(String referenceId, String status) {
		LOG.info("Stop step by refrence ID:" + referenceId);
		if (referenceId.startsWith(REF_ID_TEST)) {
			int testId = Integer.parseInt(referenceId.substring(REF_ID_TEST.length()));
			botStoreService.updateBotTestingResults(testId, 1);

			TblRpaBottesting testingDetails = botStoreService.getTestingDataById(testId);
			botStoreService.updateBotTestResults(testingDetails.getTblRpaRequest().getRpaRequestId(),
					testingDetails.getCreatedBy(), "TESTED");
		} else {

			BookingDetailsModel bSignalR = null;
			int queueBookingId = Integer.parseInt(referenceId);
			List<BookingDetailsModel> bookings = rpaDAO.getBookingsByReferenceId(queueBookingId);
			for (BookingDetailsModel b : bookings) {
				if (!AppConstants.COMPLETED.equalsIgnoreCase(b.getStatus())) {
					this.wOExecutionDAO.updateBookingsStatus("'" + b.getBookingID() + "'", AppConstants.COMPLETED,
							b.getType(), b.getReason());
					this.wOExecutionDAO.updateFcStepStatusByBookingIds("'" + b.getBookingID() + "'",
							AppConstants.COMPLETED);
					if (status != null) {
						if ("COMPLETED".equalsIgnoreCase(status)) {
							this.wOExecutionDAO.updateReasonByBookingIds("'" + b.getBookingID() + "'", "SUCCESS");
						} else {
							this.wOExecutionDAO.updateReasonByBookingIds("'" + b.getBookingID() + "'", status);
						}
					}
					if (b.getType().equalsIgnoreCase(AppConstants.BOOKING)) {
						bSignalR = b;
					}

				}
			}

			// SignalR implementation

			if (bSignalR != null) {
				String hour = wOExecutionDAO.getBookingHoursForStep(bSignalR.getFlowChartStepID(), bSignalR.getWoId());
				String flowChartType = wOExecutionDAO.getFlowChartType(bSignalR.getWoId());
				BookingDetailsModel bookingStatus = wOExecutionDAO.getBookingDetailsByWorkOrderID(bSignalR.getWoId());

				WoAutoTaskInfoModel woautotask = new WoAutoTaskInfoModel();
				woautotask.setBookingId(bSignalR.getBookingID());
				woautotask.setSignum(bSignalR.getSignumId());
				woautotask.setFlowChartStepId(bSignalR.getFlowChartStepID());
				
				if(StringUtils.equalsIgnoreCase(bookingStatus.getReason(),"Success")) {
					woautotask.setIconsOnNextStep(true);
				}
				else {
					woautotask.setIconsOnNextStep(false);
				}
				woautotask.setReason(bookingStatus.getReason());
			//	woautotask.setReason(AppConstants.SUCCESS);
				woautotask.setStatus(bookingStatus.getStatus());
			//	woautotask.setStatus(AppConstants.COMPLETED);
				woautotask.setWoId(bSignalR.getWoId());
				woautotask.setFlowChartDefID(bSignalR.getFlowChartDefID());
				woautotask.setHour(hour);
				woautotask.setExecutionType(bSignalR.getExecutionType());
				woautotask.setFlowChartType(flowChartType);
				woautotask.setOutputLink(bSignalR.getOutputLink());

				SignalrModel signalRModel = appService.returnSignalrConfiguration(woautotask,configurations.getStringProperty(ConfigurationFilesConstants.HUB_METHOD_NAME));
				appService.callSignalrApplicationToCallSignalRHub(signalRModel);
			}

		}

		Map<String, String> res = new HashMap<>();
		res.put(RESPONSE, SUCCESS);
		return res;
	}

	private static final String SUCCESS = "SUCCESS";
	private static final String RESPONSE = "response";
	private static final String EXCEPTION_LOGS_MSG = "Exception logs have been saved successfully!";
	private static final String ERROR_CODE = "errorCode";

	@Transactional("transactionManager")
	public Map<String, String> startStepByReferenceId(String referenceId) {
		LOG.info("Start Step by refrence ID:" + referenceId);
		String referer = "BOTNEST";
		Integer sourceID = rpaDAO.getExternalSource(referer);
		// stop the previous bookings and steps
		if (referenceId.startsWith(REF_ID_TEST)) {
			// TODO do we need to update anything on start
		} else {
			int queueBookingId = Integer.parseInt(referenceId);
			stopStepByReferenceId(referenceId, null);
			List<BookingDetailsModel> bookings = rpaDAO.getBookingsByReferenceId(queueBookingId);

			if (bookings.size() > 0) {
				WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(bookings.get(0).getWoId());
				List<FcStepDetails> stepDetails = wOExecutionDAO
						.selectFcStepDetailsByBookingIds("'" + referenceId + "'");
				
				ServerBotModel serverBotModel= new ServerBotModel();
				serverBotModel.setRefferer(referer);
				serverBotModel.setwOID(bookings.get(0).getWoId());
				serverBotModel.setFlowChartDefID(woDetails.getFlowchartdefid());
				woExecutionService.startWorkOrderTask(bookings.get(0).getWoId(), bookings.get(0).getTaskId(),
						stepDetails.get(0).getFlowChartStepId(), woDetails.getFlowchartdefid(),
						bookings.get(0).getSignumId(), "", "Automatic", "", queueBookingId, null, "Booking",
						serverBotModel, sourceID);
			}
		}
		Map<String, String> res = new HashMap<>();
		res.put(RESPONSE, SUCCESS);
		return res;
	}

	@Transactional(value = "transactionManager", propagation = Propagation.REQUIRES_NEW)
	public void saveBotConfig(BotConfig botData) {

		/* Gson gson = new Gson(); */
		/*
		 * String jsonEscaped = gson.toJson(botData.getJson());
		 * botData.setJson(jsonEscaped);
		 */
		rpaDAO.saveBotConfig(botData);
	}

	public BotConfig getBotConfig(String type, String referenceId) {
		return rpaDAO.getBotConfig(type, referenceId);
	}

	@Transactional(value = "transactionManager", propagation = Propagation.REQUIRES_NEW)
	public void updateBotConfig(BotConfig botData) {
		rpaDAO.updateBotConfig(botData);
	}

	public ResponseEntity<Response<Map<String, Object>>> getBotConfigForBooking(int botId, int bookingId) {
		Response<Map<String, Object>> res = new Response<>();
		Map<String,Object> data = new HashMap<String,Object>();
		try {
		TblRpaBotstaging botDetails = botStoreService.getRPAStagingData(botId);
		if(botDetails==null) {
			res.addFormMessage(NO_DETAILS_EXISTS);
			return new ResponseEntity<Response<Map<String, Object>>>(res,HttpStatus.OK);
		}
		data.put("botType", botDetails.getBotlanguage());
		BotConfig botConfig = null;
		if (bookingId > 0) {
			botConfig = getBotConfig(AppConstants.BOT_CONFIG_TYPE_WO_STEP, String.valueOf(bookingId));
		} else {
			botConfig = getBotConfig(AppConstants.BOT_CONFIG_TYPE_BOT, String.valueOf(botId));
		}
		data.put("botConfig", (botConfig == null) ? null  : botConfig.getJson());
		res.setResponseData(data);
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			res.addFormError(e.getMessage());
			return new ResponseEntity<Response<Map<String, Object>>>(res,HttpStatus.OK);
		}
		catch(Exception e) {
			res.addFormError(e.getMessage());
			return new ResponseEntity<Response<Map<String, Object>>>(res,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response<Map<String, Object>>>(res,HttpStatus.OK);
	}





	public List<HashMap<String, Object>> getBoTsForExplore(int taskId) {
		return rpaDAO.getBoTsForExplore(taskId);
	}

	public void createMailerForBot(int botId, int woid, String status, String signum) {
		emailService.sendMail(AppConstants.ISF_BOT_SUCCESS, enrichMailforBotSuccess(botId, woid, status, signum));

	}

	public Map<String, Object> enrichMailforBotSuccess(int botId, int woid, String status, String signum) {
		EmployeeModel employee = activityMasterDAO.getEmployeeBySignum(signum);
		// EmployeeModel manager =
		// activityMasterDAO.getEmployeeBySignum(req.getApprovedBy());
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("woid", woid);
		data.put("botId", botId);
		data.put("status", status);
		data.put("employee", employee.getEmployeeName());
		// data.put("managerName", manager.getEmployeeName());
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, employee.getEmployeeEmailId());
		// data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, manager.getEmployeeEmailId());
		return data;
	}

	public byte[] downloadTemplateFile(String templateName) {
		byte[] botJarFile = null;
		try {
			LOG.info("botDetail.getBotName()-->" + templateName);

			// below code to download file from DB location-->

			List<HashMap<String, Object>> list = rpaDAO.getTemplateFile(templateName);

			if (null != list && list.size() > 0) {
				botJarFile = (byte[]) list.get(0).get("templateFile");
				long size = botJarFile.length;
				if (botJarFile.length > 0) {
					LOG.info("File Type " + templateName + " is found.. Size: " + (double) size / (1024 * 1024));

					// String fPATH="C:\\Users\\essrmma\\Desktop\\Rupinder\\1604\\"+fileName;
					// FileUtils.writeByteArrayToFile(new File(fPATH), botJarFile);
				} else {
					LOG.info("File Type " + templateName + " is not found..");
					botJarFile = null;
				}
			}
		} catch (Exception e) {
			LOG.info("Error in botDownload(): " + e.getMessage());
			e.printStackTrace();
		}

		return botJarFile;

	}

	public String uploadTemplateFile(MultipartFile templateFile, String templateName, String signum) {
		byte[] inputDataFile = null;
		// byte [] outputDataFile = null;
		String status = "";
		try {
			if (templateFile != null) {

				inputDataFile = templateFile.getBytes();

			}

			rpaDAO.uploadTemplateFile(inputDataFile, templateName, signum);

			status = "SUCCESS";

		} catch (Exception e) {
			status = "Fail";
			LOG.info("Error in uploadBotInputOutput(): " + e.getMessage());
			e.printStackTrace();
			// res.setApiSuccess(false);
			// res.setResponseMsg("Error in :: "+e.getMessage());
		}

		return status;

	}

	/*
	 * public Response<Void> createWorkOrderPlan(WorkOrderPlanObjectModel
	 * woPlanObject) { Response<Void> apiResponse = new Response<>(); try {
	 * workOrderPlanService.validateWorkOrderPlanObjectModel(woPlanObject); int seq
	 * = 0; List<ExecutionPlanDetail> execPlanDetails = rpaDAO
	 * .getExecutionPlanByName(woPlanObject.getDeliverablePlanName(),
	 * woPlanObject.getProjectID());
	 * 
	 * if (execPlanDetails.isEmpty()) { throw new ApplicationException(500,
	 * ApplicationMessages.INVALID_DELIVERABLE_PLAN_NAME); }
	 * 
	 * int execPlanGroupId = -1; for (ExecutionPlanDetail execPlanDet :
	 * execPlanDetails) { if (execPlanDet.isRoot()) {
	 * 
	 * CreateWorkOrderModel wOPlanObjectRequestCopy = workOrderPlanService
	 * .getCreateWorkOrderModel(woPlanObject, execPlanDet, ++seq); CreateWoResponse
	 * response = workOrderPlanService
	 * .createWorkOrderAndWorkOrderPlan(wOPlanObjectRequestCopy); if
	 * (execPlanGroupId == -1) { execPlanGroupId = response.woPlanId; }
	 * ExecutionPlanFlow executionPlanFlow = new
	 * ExecutionPlanFlow(response.woPlanId,
	 * response.createdWorkOrders.get(0).getWoId(),
	 * execPlanDet.getExecutionPlanDetailId(), execPlanGroupId);
	 * workOrderPlanService.saveExecutionPlanFlow(executionPlanFlow); } }
	 * apiResponse.addFormMessage("Work order plan has been created successfully!");
	 * } catch (ApplicationException ae) { LOG.error(ae.getErrorMessage());
	 * apiResponse.addFormError(ae.getErrorMessage()); return apiResponse; } catch
	 * (Exception e) { e.printStackTrace();
	 * apiResponse.addFormError(e.getMessage()); return apiResponse; }
	 * LOG.info("TBL_WORK_ORDER_PLAN: SUCCESS"); return apiResponse; }
	 */

	
	  public Response<Void> createWorkOrderPlan(WorkOrderPlanObjectModel
	  workOrderPlanObjectModel) { 
		  Response<Void> apiResponse = new  Response<>();
	  
	  try 
	  {
		  doValidationForCreateStaginWorkOrder(workOrderPlanObjectModel);
		  BulkWorkOrderCreationModel bulkModel = getBulkWorkOrderCreationModel(workOrderPlanObjectModel); Map<String, Object>
	  response = createBulkWorkOrderModel(bulkModel); if
	  (response.containsKey(RESPONSE_SUCCESS)) {
	  apiResponse.addFormMessage((String) response.get(RESPONSE_SUCCESS)); } if
	  (response.containsKey(RESPONSE_FAILED)) { apiResponse.addFormError((String)
	  response.get(RESPONSE_FAILED)); } } catch (ApplicationException ae) {
	  LOG.error(ae.getErrorMessage());
	  apiResponse.addFormError(ae.getErrorMessage()); return apiResponse; 
	  } catch(Exception e) {
		  e.printStackTrace();
		  apiResponse.addFormError(e.getMessage()); return apiResponse; 
	  }
	  LOG.info("createWorkOrderPlan(): SUCCESS"); return apiResponse; }
	 


	private void doValidationForCreateStaginWorkOrder(WorkOrderPlanObjectModel workOrderPlanObjectModel) {
		if (StringUtils.isNotEmpty(workOrderPlanObjectModel.getInputUrl())) {
			Map<String, Object> map = activityMasterService.isValidTransactionalData(
					workOrderPlanObjectModel.getInputUrl(), workOrderPlanObjectModel.getProjectID());
			if (!(boolean) map.get(AppConstants.RESULT)) {
				throw new ApplicationException(org.springframework.http.HttpStatus.OK.value(),
						map.get(AppConstants.MESSAGE).toString());
			}
		}
		// validate Length
		workOrderPlanService.doLenthValidationForWoName(workOrderPlanObjectModel.getWoName());
		workOrderPlanService.doLenthValidationForInputName(workOrderPlanObjectModel.getInputName());
		workOrderPlanService.doLenthValidationForInputUrl(workOrderPlanObjectModel.getInputUrl());
		if(StringUtils.isNotBlank(workOrderPlanObjectModel.getComment())) {
		workOrderPlanService.doLenthValidationForComment(workOrderPlanObjectModel.getComment());
		}

	}

	public Response<Map<String, Object>> getRPADomain(String marketarea) {
		Response<Map<String, Object>> response = new Response<>();
		Map<String, Object> aspData = new HashMap<String, Object>();
		List<HashMap<String, Object>> domainData = null;
		
		boolean isSuccess = false;

			domainData = this.rpaDAO.getRPADomain(marketarea);
			domainData.removeAll(Collections.singleton(null));
			isSuccess = true;
		
		aspData.put("isSuccess", isSuccess);
		aspData.put("data", domainData);

		response.setResponseData(aspData);
		return response;

	}

	public Response<Map<String, Object>> getRPASubactivity(String marketarea, Integer domainId) {
		Response<Map<String, Object>> response = new Response<>();
		Map<String, Object> aspData = new HashMap<String, Object>();
		List<HashMap<String, Object>> activityData = null;
		String msg = null;
		boolean isSuccess = false;

		boolean isdomainAvailable = true;

		if (domainId == null || domainId == 0) {
			msg = "Domain ID required.";
			response.addFormError(msg);
			isdomainAvailable = false;
		}

		if (isdomainAvailable == true) {
			activityData = this.rpaDAO.getRPASubactivity(marketarea, domainId);
			isSuccess = true;
		}
		aspData.put("isSuccess", isSuccess);
		aspData.put("data", activityData);

		response.setResponseData(aspData);
		return response;

	}

	public Response<Map<String, Object>> getRPATechnology(Integer domainId, String market, Integer subActivityId) {
		Response<Map<String, Object>> response = new Response<>();
		Map<String, Object> aspData = new HashMap<String, Object>();
		List<HashMap<String, Object>> botTechnologies = null;
		String msg = null;
		boolean isSuccess = false;

			if (domainId == null || domainId == 0) {
			msg = "Domain ID required.";
			response.addFormError(msg);
		} else if (subActivityId == null || subActivityId == 0) {
			msg = "Activity/Subactivity ID required.";
			response.addFormError(msg);
		} else {
			botTechnologies = rpaDAO.getRPATechnology(domainId, market, subActivityId);
			isSuccess = true;
		}
		aspData.put("isSuccess", isSuccess);
		aspData.put("data", botTechnologies);

		response.setResponseData(aspData);
		return response;

	}

	/**
	 * 
	 * @param domainid
	 * @param technologyId
	 * @param subactivityId
	 * @param taskId
	 * @param marketarea
	 * @return Response<Map<String, Object>>
	 */
	public Response<Map<String, Object>> getRPABOTDetails(Integer domainid, Integer technologyId, Integer subactivityId,
			Integer taskId, String marketarea) {
		Response<Map<String, Object>> response = new Response<>();
		Map<String, Object> aspData = new HashMap<>();
		List<HashMap<String, Object>> botDetailData = null;

		botDetailData = this.rpaDAO.getRPABOTDetails(domainid, technologyId, subactivityId, taskId, marketarea);

		aspData.put(AppConstants.IS_SUCCESS, true);
		aspData.put(AppConstants.DATA_IN_RESPONSE, botDetailData);

		response.setResponseData(aspData);
		return response;

	}

	private BulkWorkOrderCreationModel getBulkWorkOrderCreationModel(
			WorkOrderPlanObjectModel workOrderPlanObjectModel) {
		
		String assignTo=workOrderPlanObjectModel.getAssignedTo();
		if(StringUtils.isNotEmpty(assignTo)) {
			assignTo=assignTo.trim();
		}

		BulkWorkOrderCreationModel bulkModel = new BulkWorkOrderCreationModel();
		bulkModel.setExecutionPlan(workOrderPlanObjectModel.getDeliverablePlanName());
		bulkModel.setProjectID(workOrderPlanObjectModel.getProjectID());
		bulkModel.setNodeNames(workOrderPlanObjectModel.getNodeName());
		bulkModel.setNodeType(workOrderPlanObjectModel.getNodeType());
		bulkModel.setPriority(workOrderPlanObjectModel.getPriority());
		bulkModel.setStartDate(workOrderPlanObjectModel.getStartDate());
		bulkModel.setStartTime(workOrderPlanObjectModel.getStartTime());
//		bulkModel.setAssignTO(workOrderPlanObjectModel.getAssignedTo());
		bulkModel.setAssignTO(assignTo);
		bulkModel.setComment(workOrderPlanObjectModel.getComment());
		bulkModel.setSource(workOrderPlanObjectModel.getSource());
		bulkModel.setUploadedBy(workOrderPlanObjectModel.getUploadedBy());
		if (StringUtils.isBlank(workOrderPlanObjectModel.getWoName())) {
			bulkModel.setWoName(workOrderPlanObjectModel.getDeliverablePlanName());
		} else {
			bulkModel.setWoName(workOrderPlanObjectModel.getWoName());
		}
		bulkModel.setScope_name(workOrderPlanObjectModel.getDeliverablePlanName());
		bulkModel.setInputName(workOrderPlanObjectModel.getInputName());
		bulkModel.setInputUrl(workOrderPlanObjectModel.getInputUrl());
		return bulkModel;
	}

	public TestingBotDetailsModel getTestingBotDetails(String rpaRequestId) {
		return this.rpaDAO.getTestingBotDetails(rpaRequestId);
	}

	public ResponseEntity<Response<Void>> saveExceptionLog(ExceptionLogModel exceptionLog) {
		Response<Void> response = new Response<>();
		try {
			validateSaveExceptionLog(exceptionLog);
			
			exceptionLog.setBookingID(exceptionLog.getBookingID());

			if (exceptionLog.getBookingID() != 0) {
				Integer maxBooking = this.rpaDAO.WOMaxBookingID(exceptionLog.getTaskID(), exceptionLog.getwOID(),
						exceptionLog.getSignum());
				exceptionLog.setBookingID(maxBooking);
			}
			this.rpaDAO.saveExceptionLog(exceptionLog);
			response.addFormMessage(EXCEPTION_LOGS_MSG);
		}
		catch(ApplicationException exe) {
			response.addFormMessage(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			LOG.info("Error: " + e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	private void validateSaveExceptionLog(ExceptionLogModel exceptionLog) {
		validationUtilityService.validateIntegerForNullOrZero(exceptionLog.getwOID(), AppConstants.WO_ID);
		validationUtilityService.validateIntegerForNullOrZero(exceptionLog.getTaskID(), AppConstants.TASK_ID);
		validationUtilityService.validateIntegerForNullOrZero(exceptionLog.getBookingID(), AppConstants.BOOKING_ID);
		validationUtilityService.validateIntegerForNullOrZero(exceptionLog.getBotID(), AppConstants.BOT_ID);
		validationUtilityService.validateIntForZero(exceptionLog.getErrorCode(), ERROR_CODE);
		validationUtilityService.validateStringForBlank(exceptionLog.getSignum(), AppConstants.SIGNUM);
		validateErrorCode(exceptionLog.getErrorCode());
		validationUtilityService.validateSignumForExistingEmployee(exceptionLog.getSignum());
	}

	private void validateErrorCode(int errorcode) {
		boolean isvalidErrorcode=this.rpaDAO.validateErrorCode(errorcode);
		if(Boolean.FALSE.equals(isvalidErrorcode)) {
			throw new ApplicationException(200, String.format(PLEASE_PROVIDE_VALUE, ERROR_CODE));
		}
		
	}

	 /**
     * Save Softhuman exception logs
     * @deprecated
     * This api is no longer acceptable to Save Softhuman exception logs.
     *
     * @param ExceptionLogModel 
     * @return Response<Void>
     */
	@Deprecated
	public Response<Void> SaveSHExceptionLog(ExceptionLogModel exceptionLog) {
		Response<Void> response = new Response<>();
		try {
			//exceptionLog.setLoggedOn(new Date());
			//exceptionLog.setLoggedBy(exceptionLog.getSignum());
			//exceptionLog.setExceptionLevel("Softhuman");
			exceptionLog.setBookingID(exceptionLog.getBookingID());

			if (exceptionLog.getBookingID() != 0) {
				Integer maxBooking = this.rpaDAO.WOMaxBookingID(exceptionLog.getTaskID(), exceptionLog.getwOID(),
						exceptionLog.getSignum());
				exceptionLog.setBookingID(maxBooking);
			}
			this.rpaDAO.SaveSHExceptionLog(exceptionLog);
			response.addFormMessage("SUCCESS");
			return response;
		} catch (Exception e) {
			LOG.info("Error: " + e.getMessage());
			response.addFormMessage("FAILURE");
			response.addFormError(e.getMessage());
			return response;
		}
	}
	/**
     * Save BOT exception logs
     * @deprecated
     * This api is no longer acceptable to Save BOT exception logs.
     *
     * @param ExceptionLogModel 
     * @return Response<Void>
     */
	@Deprecated
	public Response<Void> SaveBotExceptionLog(ExceptionLogModel exceptionLog) {
		Response<Void> response = new Response<>();

		try {
			//exceptionLog.setLoggedOn(new Date());
			//exceptionLog.setLoggedBy(exceptionLog.getSignum());
			//exceptionLog.setExceptionLevel("Bot");
			//exceptionLog.setBookingID(exceptionLog.getBookingID());

			if (exceptionLog.getBookingID() != 0) {
				Integer maxBooking = this.rpaDAO.WOMaxBookingID(exceptionLog.getTaskID(), exceptionLog.getwOID(),
						exceptionLog.getSignum());
				exceptionLog.setBookingID(maxBooking);
			}

			this.rpaDAO.SaveBotExceptionLog(exceptionLog);

			response.addFormMessage("SUCCESS");
			return response;
		} catch (Exception e) {
			LOG.info("Error : " + e.getMessage());
			response.addFormMessage("FAILURE");
			response.addFormError(e.getMessage());
			return response;
		}
	}

	@Transactional("txManager")
	public List<HashMap<String, Object>> getAllRPARequestDetails(int rpaRequestID) {

		return this.rpaDAO.getAllRPARequestDetails(rpaRequestID);
	}

	public RpaApiResponse updateVideoURL(VideoURLModel videoUrlModel) {
		RpaApiResponse res = new RpaApiResponse();

		int botId=videoUrlModel.getRpaRequestID();
		boolean isMacroBot=botStoreDAO.checkIfMacroBot(botId);
		if(isMacroBot) {
			res.setApiSuccess(false);
			res.setData(botId + "");
			res.setResponseMsg("Upload video URL functionality for Macro Bot is not supported.");
		}else {
			this.rpaDAO.updateVideoURL(videoUrlModel);
			res.setApiSuccess(true);
			res.setData(botId + "");
			res.setResponseMsg("Video Url updated succesfully");
		}
		return res;
	}

	/**
	 * 
	 * @param subActivityID
	 * @return List<HashMap<String, Object>>
	 */
	public List<HashMap<String, Object>> getTaskDetails(int subActivityID) {

		return rpaDAO.getTaskDetails(subActivityID);
	}

	public HashMap<String, Object> getRPAIsRunOnServer(int rpaId) {
		HashMap<String, Object> botData = rpaDAO.getRPAIsRunOnServer(rpaId);
		if (botData == null || botData.size() == 0) {
			botData = new HashMap<String, Object>();
			botData.put("isRunOnServer", 0);
			botData.put("BotType", "");
		}
		return botData;
	}

	public Response<Map<String, Object>> getRPATask(Integer domainId, String market, Integer subActivityId,
			Integer technologyId) {
		Response<Map<String, Object>> response = new Response<>();
		Map<String, Object> aspData = new HashMap<String, Object>();
		List<HashMap<String, Object>> botTasks = null;
		boolean isSuccess = false;
		String msg = null;

			if (domainId == null || domainId == 0) {
			msg = "Domain ID required.";
			response.addFormError(msg);
		} else if (technologyId == null || technologyId == 0) {
			msg = "Technology ID required.";
			response.addFormError(msg);
		} else if (subActivityId == null || subActivityId == 0) {
			msg = "Activity/Subactivity ID required.";
			response.addFormError(msg);
		} else {
			botTasks = rpaDAO.getRPATask(domainId, market, subActivityId, technologyId);
			isSuccess = true;
		}
		aspData.put("isSuccess", isSuccess);
		aspData.put("data", botTasks);

		response.setResponseData(aspData);
		return response;
	}

	public Response<Boolean> getIsUserValidForBotChanges(String signum, int botID) {
		Response<Boolean> apiResponse = new Response<>();
		try {
			apiResponse.setResponseData(rpaDAO.getIsUserValidForBotChanges(signum, botID));

		} catch (Exception e) {
			LOG.info("Error: " + e.getMessage());
			LOG.info("StackTrace: " + e.getStackTrace());
			apiResponse.addFormMessage("ERROR");
			apiResponse.addFormError(e.getMessage());
		}
		return apiResponse;
	}

	public Response<BotInputFileModel> getBotInputFile(String signum, int woid, int taskid, String stepid,
			int bookingid) throws IOException, URISyntaxException, StorageException {
		Response<BotInputFileModel> response = new Response<>();
		BotInputFileModel responseData = new BotInputFileModel();
		byte[] fileData = null;
		
		LOG.info("\nInside getBotInputFile METHOD");
		responseData=this.rpaDAO.getBotInputFile(signum,woid,taskid,stepid,bookingid);
		
		LOG.info("signum is --->" + signum);
		LOG.info("woid is --->" + woid);
		LOG.info("taskid is --->" + taskid);
		LOG.info("stepid is --->" + stepid);
		LOG.info("bookingid is --->" + bookingid);
		
		
		if(responseData==null) {
			response.addFormError("No File Data Exists");
			return response;
		}
		
		
			LOG.info("\nWoid is --->" + responseData.getWoid() + "--------->  TaskID : "+ responseData.getTaskid() 
			+ "--------->  StepID :" + responseData.getStepid() 
			+ "--------->  FileName :" + responseData.getFileName());
	
		

		/*
		 * if(StringUtils.contains(responseData.getFileName().trim(),
		 * StringUtils.SPACE)) { String inputFileName=URLEncoder.encode(
		 * responseData.getFileName().trim(), "UTF-8" );
		 * responseData.setFileName(inputFileName); }
		 */
		

		
		
		
		StringBuilder stringBuilderPath = new StringBuilder();
		stringBuilderPath.append(responseData.getWoid()).append("_").append(responseData.getTaskid()).append("_").append(responseData.getStepid())
		.append("/").append(responseData.getFileName());
		
//		StringBuilder stringBuilderPath = new StringBuilder();
//		stringBuilderPath.append(23672373).append("_").append(20173).append("_").append("233cc199-5135-46ce-8759-de833fb473c2")
//		.append("/").append("2482DotNet.zip");
		
		String path =stringBuilderPath.toString() ;
		LOG.info("path is --->" + path);
		responseData.setFileName(responseData.getFileName());
		try {
			LOG.info("path is INSIDE Try--->" + path);
			fileData = externalInterfaceManagmentService.downloadBOTInputFile(path);
			LOG.info("path is END Try--->" + path);
		} catch (Exception e) {
			LOG.info("Exception thrown with message :", e.getMessage());
		}

		if (fileData != null) {
			responseData.setFileData(fileData);
			response.setResponseData(responseData);
		} else {
			response.addFormError("No File Data Exists");
			return response;
		}
		
		return response;
	}

	public List<TblRpaDeployedBotModel> getBOTsForExploreAuditData() {
		return this.rpaDAO.getBOTsForExploreAuditData();
	}

	public Response<String> getNewAuthorizationTokenApiM() {
		Response<String> response= new Response<>();
		String identifier = "integration";    // Identifier
        //String key = "dl+PYwV/QQTEwsYHTZfWwEDn9PJ17gxKxPeU7uzxjHzlwqxpMVqfKaGTB9vyPheInLJGe+LRyZ2TbQkdMJRrDw==";    // primary key
		String key=appService.getSecretKey(APIM_SUB_KEY);
        final String expiry = LocalDateTime.now(Clock.systemUTC()).plusYears(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'0000Z'"));
        String sasToken = null;
       	//Step 1 .
	        String strToSign = identifier + "\n" + expiry; 
	    // Step 2
	        String signature = getHMAC512(key, strToSign);
	    //Step 4   //uid={identifier}&ex={expiry}&sn={Base64 encoded signature}
	        sasToken = "SharedAccessSignature uid=" +identifier+"&ex="+expiry+"&sn="+signature ;
	  
	    response.setResponseData(sasToken);
		return response;
}

	
	
	public static String getHMAC512(String key, String input) {
	    Mac sha256_HMAC = null;
	    String hash = null;
	    try {
	        sha256_HMAC = Mac.getInstance("HmacSHA512");
	        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA512");
	        sha256_HMAC.init(secret_key);
	        Encoder encoder = Base64.getEncoder();
   // Step 3
	        hash = new String(encoder.encode(sha256_HMAC.doFinal(input.getBytes("UTF-8"))));

	    } catch (InvalidKeyException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (IllegalStateException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }

	    return hash;
	}
	
	 public  Map<String, Object> getAllErrorDetails(Integer sourceID,DataTableRequest dataTableReq) {
			
			Map<String, Object> response = new HashMap<>();
			try {
			LOG.info("getAllErrorDetails:Start");
			
			if(sourceID!=null) {
            boolean checkSource=rpaDAO.isSourceExists(sourceID);
            if(!checkSource) {
    			throw new ApplicationException(200,PROVIDE_VALID_SOURCE);
            }
			}
            
			List<ErrorDetailsModel> errorDetailsList = rpaDAO.getAllErrorDetails(sourceID, dataTableReq);

			response.put(AppConstants.DATA_IN_RESPONSE, errorDetailsList);
			response.put(AppConstants.DRAW,dataTableReq.getDraw());

			if(errorDetailsList.size() != 0) {
				response.put(AppConstants.RECORD_TOTAL, errorDetailsList.get(0).getTotalCounts());
				response.put(AppConstants.RECORD_FILTERED, errorDetailsList.get(0).getTotalCounts());			
			} else {
				response.put(AppConstants.RECORD_TOTAL, 0);
				response.put(AppConstants.RECORD_FILTERED, 0);			
			}
			
            
            LOG.info("getAllErrorDetails:End");
		} catch (ApplicationException appexe) {
			response.put("errormsg", appexe.getMessage());
		} catch (Exception ex) {
			response.put("errormsg", ex.getMessage());
		}
			return response;
      }
	 
	 public ResponseEntity<Response<Void>> addErrorDetail(ErrorDetailsModel errorDetailsModel) {
		 Response<Void> result = new Response<>();
			try {
				LOG.info("addErrorDetail:Start");
				
				validationUtilityService.validateIntegerForNullOrZero(errorDetailsModel.getSourceID(),SOURCE_ID);
				
	            if(Boolean.TRUE.equals(rpaDAO.isErrorDetailExists(errorDetailsModel.getSourceID(),errorDetailsModel.getErrorType(),errorDetailsModel.getErrorMessage())) ) {
	            	throw new ApplicationException(200,ERROR_DETAIL_ALREADY_EXISTS) ;
	            }
	            
				if (Boolean.FALSE.equals(this.accessManagementDAO.isSignumExistsEmp(errorDetailsModel.getCreatedBy()))) {
					throw new ApplicationException(200,PROVIDE_VALID_SIGNUM);
				}
				
				validationUtilityService.validateStringForBlankAndLength(errorDetailsModel.getErrorType(), ERROR_TYPE,100);
				validationUtilityService.validateStringForBlankAndLength(errorDetailsModel.getErrorMessage(), ERROR_MESSAGE,250);
	            	
				boolean isSuccess = this.rpaDAO.addErrorDetail(errorDetailsModel);

				if (isSuccess) {
					result.addFormMessage(ERROR_DETAIL_ADDED_SUCCESSFULLY);
				} else {
					result.addFormMessage(ERROR_DETAIL_NOT_ADDED_SUCCESSFULLY);
				}
	            
				LOG.info("addErrorDetail:End");
			} catch (ApplicationException exe) {
				result.addFormError(exe.getMessage());
				return new ResponseEntity<>(result, HttpStatus.OK);
			} catch (Exception ex) {
				result.addFormError(ex.getMessage());
				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(result, HttpStatus.OK);
		}


	public ResponseEntity<Response<List<ErrorDetailsModel>>> getErrorDetailsByCode(int errorCode) {
		Response<List<ErrorDetailsModel>> response = new Response<>();
		try {
			validateErrorDetailsByCode(errorCode);
			List<ErrorDetailsModel> listofErrorDetails=this.rpaDAO.getErrorDetailsByCode(errorCode);
			response.setResponseData(listofErrorDetails);
			
		}
		catch(ApplicationException exe) {
			response.addFormError(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			LOG.info(String.format("Error: {0}" , e.getMessage()));
			response.addFormMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
		
		

	private void validateErrorDetailsByCode(int errorCode) {
		validationUtilityService.validateIntForZero(errorCode, ERROR_CODE);
		validateErrorCode(errorCode);
	}

}
