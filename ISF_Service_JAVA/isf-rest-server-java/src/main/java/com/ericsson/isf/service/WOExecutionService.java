/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.	
 */
package com.ericsson.isf.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.AccessManagementDAO;
import com.ericsson.isf.dao.AutoSenseDao;
import com.ericsson.isf.dao.BotStoreDao;
import com.ericsson.isf.dao.FlowChartDAO;
import com.ericsson.isf.dao.RpaDAO;
import com.ericsson.isf.dao.WOExecutionDAO;
import com.ericsson.isf.dao.WorkOrderPlanDao;
import com.ericsson.isf.eventPublisher.service.EventPublisherServiceImpl;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.AdhocWorkOrderCreationModel;
import com.ericsson.isf.model.AdhocWorkOrderModel;
import com.ericsson.isf.model.AssignWOModel;
import com.ericsson.isf.model.BookingDetailsModel;
import com.ericsson.isf.model.BotConfig;
import com.ericsson.isf.model.ChildStepDetailsModel;
import com.ericsson.isf.model.CreateWoResponse;
import com.ericsson.isf.model.CurrentWorkOrderModel;
import com.ericsson.isf.model.DOWOModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DeliverableSuccessReasonModel;
import com.ericsson.isf.model.DescisionStepDetailsModel;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.EventPublisherRequestModel;
import com.ericsson.isf.model.EventPublisherURLMappingModel;
import com.ericsson.isf.model.ExternalExecutionDetailsModel;
import com.ericsson.isf.model.FcStepDetails;
import com.ericsson.isf.model.FlowChartReverseTraversalModel;
import com.ericsson.isf.model.HeaderModel;
import com.ericsson.isf.model.InProgressWorkOrderModel;
import com.ericsson.isf.model.LastStepDetailsModel;
import com.ericsson.isf.model.MailModel;
import com.ericsson.isf.model.ParallelWorkOrderDetailsModel;
import com.ericsson.isf.model.PreviousStepDetailsModel;
import com.ericsson.isf.model.ProficiencyTypeModal;
import com.ericsson.isf.model.ProjectQueueWorkOrderBasicDetailsModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.RpaModel;
import com.ericsson.isf.model.SaveClosureDetailsForWOInFWModel;
import com.ericsson.isf.model.SaveClosureDetailsForWOModel;
import com.ericsson.isf.model.SaveWfUserProfResponseModel;
import com.ericsson.isf.model.SharePointDetailModel;
import com.ericsson.isf.model.SignalrModel;
import com.ericsson.isf.model.SourceSystemDetailsModel;
import com.ericsson.isf.model.StepDetailsModel;
import com.ericsson.isf.model.StepTaskModel;
import com.ericsson.isf.model.SubActivityWfIDModel;
import com.ericsson.isf.model.TaskModel;
import com.ericsson.isf.model.UpdateBookingAndStepDetailsModel;
import com.ericsson.isf.model.UserLoginModel;
import com.ericsson.isf.model.UserWorkFlowProficencyModel;
import com.ericsson.isf.model.WOWorkFlowModel;
import com.ericsson.isf.model.WoAutoTaskInfoModel;
import com.ericsson.isf.model.WoCompleteDetailsModel;
import com.ericsson.isf.model.WorkFlowBookingDetailsModel;
import com.ericsson.isf.model.WorkFlowStepBookingDetailsModel;
import com.ericsson.isf.model.WorkFlowStepDataModel;
import com.ericsson.isf.model.WorkFlowStepDetailsModel;
import com.ericsson.isf.model.WorkFlowStepLinksDetailModel;
import com.ericsson.isf.model.WorkFlowUserProficiencyModel;
import com.ericsson.isf.model.WorkOrderBasicDetailsModel;
import com.ericsson.isf.model.WorkOrderColumnMappingModel;
import com.ericsson.isf.model.WorkOrderCompleteDetailsModel;
import com.ericsson.isf.model.WorkOrderDetailsSearchModel;
import com.ericsson.isf.model.WorkOrderFailureReasonModel;
import com.ericsson.isf.model.WorkOrderMailModel;
import com.ericsson.isf.model.WorkOrderMarketModel;
import com.ericsson.isf.model.WorkOrderModel;
import com.ericsson.isf.model.WorkOrderNetworkElementModel;
import com.ericsson.isf.model.WorkOrderNodesModel;
import com.ericsson.isf.model.WorkOrderProgressModel;
import com.ericsson.isf.model.WorkOrderProgressResultModel;
import com.ericsson.isf.model.WorkOrderViewDetailsByWOIDModel;
import com.ericsson.isf.model.WorkflowStepDetailModel;
import com.ericsson.isf.model.botstore.RpaApiResponse;
import com.ericsson.isf.model.botstore.ServerBotModel;
import com.ericsson.isf.model.botstore.TblAdhocBooking;
import com.ericsson.isf.model.botstore.TblRpaDeployedBot;
import com.ericsson.isf.model.botstore.WoStepDetails;
import com.ericsson.isf.model.botstore.WoStepDetailsList;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AppUtil;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.ericsson.isf.util.DateTimeUtil;
import com.ericsson.isf.util.FileUtil;
import com.ericsson.isf.util.MailUtil;
import com.ericsson.isf.util.PlannedEndDateCal;
import com.ericsson.isf.util.SignalrUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author esanpup
 */
@Service
public class WOExecutionService {
	
	private static final String BOT_EXECUTION_STARTED = "Bot Execution Started";
	private static final String BOTNEST = "BOTNEST";
	private static final String SERVER = "server";
	private static final String PLEASE_CONFIGURE_MARKET_WITH_SHAREPOINT = "Please configure market with Sharepoint";
	private static final String ENTER_VALID_SHAREPOINT_URL_PLEASE_CHECK_YOUR_SITE_NAME_WITH_MARKET_AREA = "Enter valid sharepoint url (Please check your Site name with Market Area)";
	private static final String BUSINESSENTITYID = "businessentityid";
	private static final String SOURCE_SYSTEM_ID = "SOURCE_SYSTEM_ID";
	private static final String MESSAGE_BATCH_ID = "MESSAGE_BATCH_ID";
	private static final String TRANSACTION_ID = "TRANSACTION_ID";
	private static final String PARENT_WORK_PLAN_RECORD_ID = "PARENT_WORK_PLAN_RECORD_ID";
	private static final String TEST = "Test";
	private static final String EXECUTION_TYPE = "ExecutionType";
	private static final String WO_ID = "woID";
	private static final String TASK_ID = "TaskID";
	private static final String ATTRS = "attrs";
	private static final String ID = "id";
	private static final String NULL = "NULL";
	private static final String ZERO_STRING = "0";
	private static final String BOT_TYPE_BOTNEST = BOTNEST;
	private static final String QUEUE_ID = "QUEUE_ID";
	private static final String BOOKING_REASON_PARALLEL = "PARALLEL BOOKING";
	private static final String BOOKING_TYPE_LEADTIME = "LEADTIME";
	private static final String IS_API_SUCCESS="isApiSuccess";
	private static final String RESPONSE_MSG="responseMsg";
	private static final String SUCCESS_GET_MARKETS="SUCCESS:getMarkets for WOID:";
	private static final String ERROR_GET_MARKETS ="ERROR:getMarkets:";
	private static final String ERROR_GET_PROFICIENCY ="ERROR:getMarkets:";
	private static final String SUCCESS="SUCCESS!";

	private static final String TASKS_ARE_ALREADY_RUNNING="%s Tasks are already running";
	private static final String COMPLETE_THE_PREVIOUS_STEP="%s Task already running in this WOID. Please complete the previous step.";
	private static final String CAN_RUN_THE_TASK="Can Run the %s task";
	private static final String RPA_ID_IS_NOT="RPA ID is not %s";
	private static final String CONFIGURED_FOR_THE_GIVEN_DETAILS="configured for the given details";
	private static final String MAXAUTOMATICTASK="MaxAutomaticTask";
	private static final String MAXMANUALTASK="MaxManualTask";
	private static final String FLOWCHARTDEFID="FlowChartDefID";
	private static final String FLOWCHARTSTEPID="FlowChartStepID";
	private static final String DISABLED_MANUAL_COMPLETE_ERROR="Please Complete Disabled Manual Step ";
	private static final String NO_DATA_FOUND="NO DATA FOUND!";
	private static final String wO_ID="wOID";
	
	private static final String WO_STATUS_NOT_IN_ASSIGNED_OR_ONHOLD="The Work Order Status is not Assigned/OnHold, so the Work Order cannot be Assigned!";
	
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(WOExecutionService.class);

	@Autowired
	private OutlookAndEmailService emailService;
	
	@Autowired
	private WOExecutionDAO wOExecutionDAO;
	
	@Autowired
	private AutoSenseService autoSenseService;

	@Autowired
	private WorkOrderPlanService woPlanService;

	@Autowired /* Bind to bean/pojo */
	private ActivityMasterService activityMasterService;

	@Autowired
	private WorkOrderPlanDao workOrderPlanDao;
	@Autowired
	private RpaDAO rpaDAO;
	@Autowired
	private FlowChartDAO flowChartDao;

	@Autowired
	BotStoreService botStoreService;

	@Autowired
	RpaService rpaService;

	@Autowired
	private MailUtil mailUtil;

	@Autowired
	private ApplicationConfigurations configurations;

	@Autowired
	private AppService appService;

	@Autowired
	private BotService botService;

    @Autowired
    private WorkOrderPlanService workOrderPlanService;
    
	@Autowired
	private AccessManagementDAO accessManagementDAO;
	
	@Autowired
	private AdhocActivityService adhocActivityService;

	@Autowired
	private ValidationUtilityService validationUtilityService;
	
	@Autowired
	private AutoSenseDao autoSenseDao;

	@Autowired
	private EventPublisherServiceImpl eventPublisherServiceImpl;
	
	@Autowired
	private SignalrUtil signalrUtil;
	
	@Autowired
	ApplicationConfigurations appConfig;

	public String getFilePath() {
		return appService.getConfigHelpPath();

	}

	public Map<String, Object> searchPlannedWorkOrders(String signum, String woStatus, String startDate,
			String endDate,DataTableRequest dataTableRequest) {
		
		Map<String, Object> response = new HashMap<>();
		try {
			LOG.info("searchPlannedWorkOrders:Start");
	
		List<WorkOrderDetailsSearchModel> workOrderPlan = wOExecutionDAO.searchPlannedWorkOrders(signum, woStatus, startDate, endDate,dataTableRequest);
		
		response.put(AppConstants.DATA_IN_RESPONSE, workOrderPlan);
		response.put(AppConstants.DRAW,dataTableRequest.getDraw());

		if(!workOrderPlan.isEmpty()) {
			response.put(AppConstants.RECORD_TOTAL, workOrderPlan.get(0).getTotalCounts());
			response.put(AppConstants.RECORD_FILTERED, workOrderPlan.get(0).getTotalCounts());			
		} else {
			response.put(AppConstants.RECORD_TOTAL, 0);
			response.put(AppConstants.RECORD_FILTERED, 0);			
		}
		
        
        LOG.info("searchPlannedWorkOrders:End");
	    } catch (ApplicationException appexe) {
		  response.put("errormsg", appexe.getMessage());
	    } catch (Exception ex) {
		  response.put("errormsg", ex.getMessage());
	    }
	   return response;
		
	}


	public List<AdhocWorkOrderModel> getAdhocWorkOrderDetails(String signumID) {

		List<AdhocWorkOrderModel> checkUserDetails = wOExecutionDAO.getAdhocWorkOrderDetails(signumID);
		if (checkUserDetails.isEmpty()) {
			throw new ApplicationException(500, "No details exists for the User !!!");
		} else {
			return checkUserDetails;
		}

	}

	public List<AdhocWorkOrderModel> getAdhocWorkOrderDetailsByID(int adhocWOID, String signumID) {
		List<AdhocWorkOrderModel> checkUserDetails = wOExecutionDAO.getAdhocWorkOrderDetailsByID(adhocWOID, signumID);
		if (checkUserDetails.isEmpty()) {
			throw new ApplicationException(500, "No adhoc work order details exists for the adhocWOID " + adhocWOID
					+ " and signumID " + signumID + " !!!");
		} else {
			return checkUserDetails;
		}

	}

	@Transactional("transactionManager")
	public void saveFeedback(String signumID, String feedback) {
		wOExecutionDAO.saveFeedback(signumID, feedback);
	}

	@Transactional("transactionManager")
	public Response<CreateWoResponse> updateWorkOrderStatus(SaveClosureDetailsForWOModel saveClosureDetailsForWOModel) {

		int woID = saveClosureDetailsForWOModel.getwOID();
		String signumID = saveClosureDetailsForWOModel.getSignumID();
		String priority = saveClosureDetailsForWOModel.getPriority();
		String status = saveClosureDetailsForWOModel.getDeliveryStatus();
		String comment = saveClosureDetailsForWOModel.getStatusComment();
		Response<CreateWoResponse> response = new Response<CreateWoResponse>();
		int bookingID = wOExecutionDAO.getLatestBookingID(woID, signumID);

		try {

			if (wOExecutionDAO.checkIFWOIDExists(woID)) {

				WorkOrderModel workOrder = wOExecutionDAO.getWOData(woID);
				if (StringUtils.isNotBlank(priority) && StringUtils.equalsIgnoreCase(AppConstants.LIVE, priority)
						&& StringUtils.equalsIgnoreCase(AppConstants.ONHOLD, status)
						&& StringUtils.equalsIgnoreCase(AppConstants.INPROGRESS, workOrder.getStatus())) {

					workOrderPlanDao.editWOPriority(woID, AppConstants.NORMAL, signumID);
				}
				wOExecutionDAO.updateWorkOrderStatus(woID, signumID, status, comment);

				if (bookingID != 0 && StringUtils.equalsIgnoreCase(AppConstants.ONHOLD, status)) {

					wOExecutionDAO.updateBookingStatus(woID, bookingID, signumID, status, comment);
					wOExecutionDAO.updateBookingStatusInFlowChart(woID, bookingID, signumID, status);
				}
				if (StringUtils.equalsIgnoreCase(AppConstants.WO_STATUS_DEFERRED, status)) {
					
					updateWODeferredStatus(saveClosureDetailsForWOModel, response, bookingID);
					if(configurations.getBooleanProperty(ConfigurationFilesConstants.WORKORDER_MAIL_ENABLED, false)) {
						sendMailForWoDeferred(woID, status);
					}
				}
			} else {
				response.addFormError("Given WorkOrderID doesn't exist !!!");
			}
		} catch (ApplicationException e) {
			response.addFormError(e.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.addFormError(e.getMessage());
		}
		return response;
	}

	@Transactional("transactionManager")
	private void updateWODeferredStatus(SaveClosureDetailsForWOModel saveClosureDetailsForWOModel,
			Response<CreateWoResponse> response, int bookingID) {

		int woID = saveClosureDetailsForWOModel.getwOID();
		String signumID = saveClosureDetailsForWOModel.getSignumID();
		String comment = saveClosureDetailsForWOModel.getStatusComment();
		
		if (bookingID != 0) {

			wOExecutionDAO.updateBookingStatus(woID, bookingID, signumID, AppConstants.COMPLETED, comment);
			wOExecutionDAO.updateBookingStatusInFlowChart(woID, bookingID, signumID, AppConstants.COMPLETED);
			// here we need to code for subsequent WO creation if flag is on for subsequent
			// WO Creation
		}
		if (saveClosureDetailsForWOModel.isCreateSubSequentWO()) {
			CreateWoResponse createWoResponse = woPlanService.createExecutionPlanFLow(saveClosureDetailsForWOModel);
			response.setResponseData(createWoResponse);
		}
	}

	private void sendMailForWoDeferred(int woID, String status) {

		MailModel model = woPlanService.getWoMailNotificationDetails(woID);
		if (model != null) {

			WorkOrderMailModel workOrderMailModel = new WorkOrderMailModel(model.getProjectCreatorSignum(),
					model.getProjectCreatorName(), model.getEmployeeSignum(), model.getEmployeeName(),
					model.getProjectCreatorEmailID(), model.getEmployeeEmailID(), status, woID, AppConstants.NA,
					getNodesByWoId(woID));

			sendMailForWorkOrderDeferred(workOrderMailModel, model);

		}
	}

	private void sendMailForWorkOrderDeferred(WorkOrderMailModel workOrderMailModel, MailModel model) {
		// Lambda Runnable
				Runnable task2 = () -> {
					try {
						sendMail(workOrderMailModel, model);
					} catch (Exception e) {
						LOG.debug(String.format("Exception thrown  %s", e.getMessage()));
					}
				};

				// start the thread
				new Thread(task2).start();
		
	}

	// Removing WO Duplicate functionality from 9.0
	/*
	 * @Transactional("transactionManager") public void
	 * createDuplicateWorkOrder(String wOID, String signum, String projectID, String
	 * comment) { wOExecutionDAO.createDuplicateWorkOrder(wOID, signum, projectID,
	 * comment); }
	 */

	public List<TaskModel> getTaskRelatedDetails(String taskID, String subActivityID, String projectID) {
		List<TaskModel> task = new ArrayList<>();
		task = wOExecutionDAO.getTaskRelatedDetails(taskID, subActivityID, projectID);
		return task;
	}

	@Transactional("transactionManager")
	public Map<String, Object> startWorkOrderTask(int wOID, int taskID, String stepID, int flowChartDefID,
			String signumID, String decisionValue, String exeType, String botPlatform, Integer referenceId,
			String botConfigJson, String bookingType, ServerBotModel serverBotModel, Integer sourceID) {
		Map<String, Object> response = new HashMap<>();
		try {
			WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(wOID);
			if (!woDetails.getSignumID().equalsIgnoreCase(signumID)) {
				response.put(AppConstants.ERROR, AppConstants.TASK_SIGNUM_ERROR);
				return response;
			}
			if (AppConstants.CLOSED.equalsIgnoreCase(woDetails.getStatus())) {
				response.put(AppConstants.ERROR, AppConstants.WO_CLOSED_ERROR);
				return response;
			}

			List<BookingDetailsModel> alreadyStartedBookingsByWoID = wOExecutionDAO
					.getAlreadyStartedBookingsByWoID(wOID);
			if (alreadyStartedBookingsByWoID != null && !alreadyStartedBookingsByWoID.isEmpty()) {
				response.put(AppConstants.ERROR, AppConstants.WO_STARTED_ERROR);
				return response;
			}
			
			//checkAndUpdateFlowChartDefID(wOID, flowChartDefID);
			//wOExecutionDAO.updateStatusWO(wOID);
			int bookingID = startStep(wOID, taskID, stepID, flowChartDefID, signumID, decisionValue, exeType,
					botPlatform, bookingType, referenceId, botConfigJson, serverBotModel,sourceID);
			response.put(AppConstants.SUCCESS, AppConstants.STEP_STARTED);
			response.put(AppConstants.HOUR, wOExecutionDAO.getBookingHoursForStep(stepID, wOID));
			response.put(AppConstants.BOOKING_ID, bookingID);
		} catch (Exception ex) {
			LOG.info("Error in startStep: " + ex.getMessage()); 
			throw new ApplicationException(500, ex.getMessage());
		}
		return response;
	}

	@Transactional("transactionManager")
	private void checkAndUpdateFlowChartDefID(int wOID, int flowChartDefID) {
		wOExecutionDAO.checkAndUpdateFlowChartDefID(wOID, flowChartDefID);
	}

	@Transactional("transactionManager")
	private int startStep(int wOID, int taskID, String stepID, int flowChartDefID, String signumID,
			String decisionValue, String exeType, String botPlatform, String bookingType, Integer referenceId,
			String botConfigJson, ServerBotModel serverBotModel, Integer sourceID) {

		WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(wOID);
		if (AppConstants.CLOSED.equalsIgnoreCase(woDetails.getStatus())) {
			throw (new ApplicationException(500, AppConstants.WO_CLOSED_ERROR));
		}
	

		List<BookingDetailsModel> startedBookingsOfUser = wOExecutionDAO.getStartedBookingsBySignum(signumID);
		

		if(woDetails.getStatus().equalsIgnoreCase(AppConstants.WO_STATUS_ASSIGNED)
				|| woDetails.getStatus().equalsIgnoreCase(AppConstants.ONHOLD)
				|| woDetails.getStatus().equalsIgnoreCase(AppConstants.WO_STATUS_REPOENED)) {
			
			//if ProficiencyID is already set
			if(woDetails.getProficiencyID()!=null) {
				serverBotModel.setProficiencyID(-1);
			}
			else if(serverBotModel.getProficiencyID()==0) {
				serverBotModel.setProficiencyID(appService.getProficiencyID(AppConstants.ASSESSED).getProficiencyID());
			}
			wOExecutionDAO.updateStatusandProficiencyId(wOID,signumID,serverBotModel.getProficiencyID(),
					(woDetails.getActualStartDate()==null)?true:false);
		}
		
		Object[] startedBookingDetails=getStartedBookingDetailsForStartStep(wOID, startedBookingsOfUser);
		
		int parallelCount=1;
		String startedBookingIds=StringUtils.EMPTY;
		if(ArrayUtils.isNotEmpty(startedBookingDetails)) {
			if(startedBookingDetails[0] !=null && StringUtils.isNotEmpty(startedBookingDetails[0].toString())) {
				startedBookingIds = (String) startedBookingDetails[0];
			}
			if(startedBookingDetails[1] !=null  && StringUtils.isNotEmpty(startedBookingDetails[1].toString())) {
				parallelCount = Integer.parseInt((String) startedBookingDetails[1]);
			}
		}

		updateBookingsAndFcDetails(wOID, stepID, signumID);

		if (StringUtils.isEmpty(bookingType)) {
			bookingType = AppConstants.BOOKING;
		}
		if(StringUtils.equalsIgnoreCase(bookingType,  AppConstants.BOOKING_TYPE_QUEUED) && 
				StringUtils.equalsIgnoreCase(serverBotModel.getType(),  BOTNEST)) {
			serverBotModel.setReason(BOT_EXECUTION_STARTED);	
		}

		BookingDetailsModel newBooking = addBookings(wOID, taskID, bookingType, signumID, referenceId, parallelCount,
				startedBookingsOfUser,sourceID,startedBookingIds,serverBotModel, stepID, exeType);

		if (flowChartDefID == 0) {
			flowChartDefID = woDetails.getFlowchartdefid();
		}

		addFcStepDetailsForStartStep(startedBookingIds, startedBookingsOfUser, wOID, stepID, flowChartDefID, taskID,
				signumID, decisionValue, exeType, botPlatform, newBooking);

		if (StringUtils.isNotEmpty(botConfigJson)) {
			BotConfig botConfig = new BotConfig();
			botConfig.setJson(botConfigJson);
			botConfig.setReferenceId(newBooking.getBookingID() + StringUtils.EMPTY);
			botConfig.setSignum(signumID);
			botConfig.setType(AppConstants.BOT_CONFIG_TYPE_WO_STEP);
			rpaService.saveBotConfig(botConfig);
		}
		return newBooking.getBookingID();
	}
	
	
	@Transactional("transactionManager")
	public void updateBookingsAndFcDetails(int wOID, String stepID, String signumID) {

		StepDetailsModel stepDetails = wOExecutionDAO.getStepDetailsByStepId(wOID, stepID);
		if (stepDetails != null) {

			String bookingids = AppConstants.SINGLE_QUOTE + stepDetails.getBookingId() + AppConstants.SINGLE_QUOTE;
			if (AppConstants.SKIPPED.equals(stepDetails.getStatus())) {

				wOExecutionDAO.updateOnlyBookingsStatusByIds(bookingids, stepDetails.getStatus(), signumID);
			} else if (AppConstants.ONHOLD.equals(stepDetails.getStatus())) {

				wOExecutionDAO.updateOnlyBookingsStatusByIds(bookingids, AppConstants.COMPLETED, signumID);
				wOExecutionDAO.updateFcStepStatusByBookingIds(bookingids, AppConstants.COMPLETED);
			} else {

				wOExecutionDAO.updateOnlyBookingsStatusByIds(bookingids, stepDetails.getStatus(), signumID);
				wOExecutionDAO.updateFcStepStatusByBookingIds(bookingids, stepDetails.getStatus());
						//,AppConstants.COMPLETED);
			}
		}

	}
	
	@Transactional("transactionManager")
	public void addFcStepDetailsForStartStep(String startedBookingIds, List<BookingDetailsModel> startedBookingsOfUser,
			int wOID, String stepID, int flowChartDefID, int taskID, String signumID, String decisionValue,
			String exeType, String botPlatform, BookingDetailsModel newBooking) {

		Map<Integer, BookingDetailsModel> bookingMap = new HashMap<Integer, BookingDetailsModel>();
        if(CollectionUtils.isNotEmpty(startedBookingsOfUser)) {
		for (BookingDetailsModel booking : startedBookingsOfUser) {
			bookingMap.put(booking.getWoId(), booking);
		}
        }
		List<FcStepDetails> existingStepDetails = new ArrayList<>();
		List<FcStepDetails> copiedStepDetails = new ArrayList<>();
		if (!StringUtils.EMPTY.equals(startedBookingIds)) {
			existingStepDetails = wOExecutionDAO.selectFcStepDetailsByBookingIds(startedBookingIds);
			wOExecutionDAO.updateFcStepStatusByBookingIds(startedBookingIds, AppConstants.COMPLETED);
			for (FcStepDetails e : existingStepDetails) {
				e.setBookingID(bookingMap.get(e.getWoId()).getBookingID());
				if (!(stepID.equals(e.getFlowChartStepId()) && e.getWoId() == wOID)) {
					copiedStepDetails.add(e);
				}
			}
		}

		FcStepDetails newFcStepDetails = new FcStepDetails();
		newFcStepDetails.setWoId(wOID);

		newFcStepDetails.setFlowChartDefId(flowChartDefID);
		newFcStepDetails.setFlowChartStepId(stepID);
		newFcStepDetails.setTaskID(taskID);
		newFcStepDetails.setBookingID(newBooking.getBookingID());
		newFcStepDetails.setStatus(AppConstants.STARTED);
		newFcStepDetails.setSignumId(signumID);
		newFcStepDetails.setDecisionValue(decisionValue);
		newFcStepDetails.setExecutionType(exeType);
		newFcStepDetails.setBotPlatform(botPlatform);

		copiedStepDetails.add(newFcStepDetails);
		if (!copiedStepDetails.isEmpty()) {
			wOExecutionDAO.insertFcStepDetails(copiedStepDetails);
		}
	}

	@Transactional("transactionManager")
	public BookingDetailsModel addBookings(int wOID, int taskID, String bookingType, String signumID,
			Integer referenceId, int parallelCount, List<BookingDetailsModel> startedBookingsOfUser, Integer sourceID,
			String startedBookingIds, ServerBotModel serverBotModel, String stepID, String exeType) {

		if(!StringUtils.equalsIgnoreCase(serverBotModel.getRefferer(),AppConstants.SOFT_HUMAN)
				&& !StringUtils.equalsIgnoreCase(serverBotModel.getRefferer(),AppConstants.BOTNEST)) {
			if (wOExecutionDAO.checkStartedTaskInBooking(serverBotModel.getwOID())) {
				throw new ApplicationException(500, AppConstants.STEP_ALREADY_STARTED_ERROR_STRING + serverBotModel.getwOID());
			}
		}

		if (!StringUtils.EMPTY.equals(startedBookingIds)) {
			startedBookingsOfUser.stream().forEach(booking->wOExecutionDAO.updateBookingsStatus(
					new StringBuilder().append(AppConstants.SINGLE_QUOTE).append(booking.getBookingID()).append(AppConstants.SINGLE_QUOTE).toString(),
					AppConstants.COMPLETED, booking.getType(), booking.getReason()));
		}
		
		BookingDetailsModel newBooking = new BookingDetailsModel()
												.wOID(wOID)
												.taskId(taskID)
												.parentBookingDetailsId(wOExecutionDAO.getMaxBookingId(wOID))
												.type(bookingType)
												.status(AppConstants.STARTED)
												.signumID(signumID)
												.sourceId(sourceID)
												.flowChartDefID(serverBotModel.getFlowChartDefID())
												.decisionValue(serverBotModel.getDecisionValue())
												.flowChartStepID(stepID)
												.executionType(exeType)
												.botPlatform(serverBotModel.getBotPlatform());
		newBooking.setReason(serverBotModel.getReason());
		
		if (referenceId != 0) {
			newBooking.setReferenceId(referenceId);
		}
		
		startedBookingsOfUser.add(newBooking);
		startedBookingsOfUser.stream().forEach(booking->{
			booking.setParallelcount(parallelCount);
			wOExecutionDAO.addBooking(booking);
		});
		
		/*
		 * for (BookingDetailsModel booking : startedBookingsOfUser) {
		 * 
		 * booking.setParallelcount(parallelCount);
		 * 
		 * wOExecutionDAO.addBooking(booking); }
		 */
       
		if (referenceId == 0) {
			wOExecutionDAO.updateReferenceIDByBookingId(newBooking.getBookingID(), newBooking.getBookingID(), signumID);
		}
		return newBooking;
	}

	@Transactional("transactionManager")
	public void markBookingsCompleted(String startedBookingIds, List<BookingDetailsModel> startedBookingsOfUser) {

		if (!StringUtils.EMPTY.equals(startedBookingIds)) {
			for (BookingDetailsModel booking : startedBookingsOfUser) {
				wOExecutionDAO.updateBookingsStatus(
						AppConstants.SINGLE_QUOTE + booking.getBookingID() + AppConstants.SINGLE_QUOTE,
						AppConstants.COMPLETED, booking.getType(), booking.getReason());
			}
		}
	}

	@Transactional("transactionManager")
	public Object[] getStartedBookingDetailsForStartStep(int wOID,
			List<BookingDetailsModel> startedBookingsOfUser) {

		Object[] startedBookingDetails = new String[3];
//		Map<String, Object> startedBookingDetails = new HashMap<String, Object>();
		int parallelCount = 1;
//		String startedBookingIds = StringUtils.EMPTY;
		StringBuilder startedBookingIds = new StringBuilder();
        try {
		if (CollectionUtils.isNotEmpty(startedBookingsOfUser)) {

			for (BookingDetailsModel booking : startedBookingsOfUser) {
				if ((booking.getWoId() != wOID)) {
					booking.setEndDate(null);
					booking.setReason(BOOKING_REASON_PARALLEL);
					booking.setParentBookingDetailsId(booking.getBookingID());
//				/	startedBookingIds += AppConstants.SINGLE_QUOTE + booking.getBookingID() + "',";
					startedBookingIds.append(AppConstants.SINGLE_QUOTE).append(booking.getBookingID()).append(AppConstants.COMMA_SINGLE_QUOTE);
					parallelCount++;
				}
			}
			startedBookingDetails[0]=startedBookingIds.substring(0, startedBookingIds.length()-1).toString();
			startedBookingDetails[1]=String.valueOf(parallelCount);
//			startedBookingIds = StringUtils.substring(startedBookingIds, 0, StringUtils.defaultString(startedBookingIds).length() - 1);
		}
		
//		startedBookingDetails.put(STARTED_BOOKING_IDS, startedBookingIds.substring(0, startedBookingIds.length()-2).toString());
//		startedBookingDetails.put(AppConstants.PARALLEL_COUNT, parallelCount);
        }
        catch(Exception e) {
        	startedBookingDetails[2]=e.getMessage();
//        	startedBookingDetails.put(AppConstants.ERROR, e.getMessage());
        }
		return startedBookingDetails;

	}

	@Transactional("transactionManager")
	private String stopStep(ServerBotModel serverBotModel) {

		List<BookingDetailsModel> startedBookingsOfUser = wOExecutionDAO
				.getStartedBookingsBySignum(serverBotModel.getSignumID());
		String startedBookingIds=getStartedBookingDetailsForStopStep(startedBookingsOfUser, serverBotModel);

		return addFcStepDetailsForStopStep(startedBookingIds, startedBookingsOfUser, serverBotModel.getwOID());
	}

	@Transactional("transactionManager")
	public String getStartedBookingDetailsForStopStep(List<BookingDetailsModel> startedBookingsOfUser,
			ServerBotModel serverBotModel) {
		int parallelCount = 0;
		StringBuilder startedBookingIds = new StringBuilder();
		int wOID = serverBotModel.getwOID();
		String reason = serverBotModel.getReason();

		if (CollectionUtils.isNotEmpty(startedBookingsOfUser)) {
			ListIterator<BookingDetailsModel> iter = startedBookingsOfUser.listIterator();
			BookingDetailsModel booking;
			while(iter.hasNext()) {
				booking=iter.next();
				if ((booking.getWoId() == wOID)) {
					if (StringUtils.isNotBlank(reason)) {
						booking.setReason(reason);
					}

					wOExecutionDAO.updateBookingsStatus(
							new StringBuilder().append(AppConstants.SINGLE_QUOTE).append(booking.getBookingID()).append(AppConstants.SINGLE_QUOTE).toString(),
							AppConstants.COMPLETED, booking.getType(), booking.getReason());
					startedBookingIds.append(AppConstants.SINGLE_QUOTE).append(booking.getBookingID()).append(AppConstants.COMMA_SINGLE_QUOTE);
					iter.remove();
				} else {
					booking.setReason(BOOKING_REASON_PARALLEL);
					parallelCount++;
					
					wOExecutionDAO.updateBookingsStatus(
							new StringBuilder().append(AppConstants.SINGLE_QUOTE).append(booking.getBookingID()).append(AppConstants.SINGLE_QUOTE).toString(),
							AppConstants.COMPLETED, booking.getType(), booking.getReason());
					startedBookingIds.append(AppConstants.SINGLE_QUOTE).append(booking.getBookingID()).append(AppConstants.COMMA_SINGLE_QUOTE);
				}
			}
			
			
			if(CollectionUtils.isNotEmpty(startedBookingsOfUser)) {
				for(BookingDetailsModel bookingDetailsModel :startedBookingsOfUser) {
					bookingDetailsModel.setParallelcount(parallelCount);
					wOExecutionDAO.addBooking(bookingDetailsModel);
				}
			}
		}
		return startedBookingIds.substring(0, startedBookingIds.length()-1).toString();
	}

	@Transactional("transactionManager")
	public String addFcStepDetailsForStopStep(String startedBookingIds, List<BookingDetailsModel> startedBookingsOfUser,
			int wOID) {

		Map<Integer, BookingDetailsModel> bookingMap = new HashMap<Integer, BookingDetailsModel>();
		String stepID = StringUtils.EMPTY;

		for (BookingDetailsModel booking : startedBookingsOfUser) {
			bookingMap.put(booking.getWoId(), booking);
		}

		List<FcStepDetails> copiedStepDetails = new ArrayList<FcStepDetails>();
		if (!StringUtils.equalsIgnoreCase(StringUtils.EMPTY, startedBookingIds)) {

			List<FcStepDetails> existingStepDetails = wOExecutionDAO.selectFcStepDetailsByBookingIds(startedBookingIds);
			wOExecutionDAO.updateFcStepStatusByBookingIds(startedBookingIds, AppConstants.COMPLETED);

			for (FcStepDetails e : existingStepDetails) {

				if (e.getWoId() != wOID) {
					e.setBookingID(bookingMap.get(e.getWoId()).getBookingID());
					copiedStepDetails.add(e);
				} else {
					stepID = e.getFlowChartStepId();
				}
			}
		}
		if (CollectionUtils.isNotEmpty(copiedStepDetails)) {
			wOExecutionDAO.insertFcStepDetails(copiedStepDetails);
		}

		return stepID;

	}
	  
	@Transactional("transactionManager")
	public Map<String, Object> stopWorkOrderTask(ServerBotModel serverBotModel) {

		Map<String, Object> response = new HashMap<>();
		boolean isSuccess = true;
		String hours	=	StringUtils.EMPTY;
		try {
			WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(serverBotModel.getwOID());
			if(StringUtils.equalsIgnoreCase(serverBotModel.getRefferer(),AppConstants.AUTO_SENSE)) {
					validationUtilityService.validateTaskIDForStartTask(serverBotModel, woDetails);
			}
			WorkFlowBookingDetailsModel maxBookingID = wOExecutionDAO.stopWOMaxBookingID(serverBotModel.getwOID(),
					serverBotModel.getTaskID());
			hours = wOExecutionDAO.getBookingHoursForStep(serverBotModel.getStepID(), serverBotModel.getwOID());
			
			response	=	validateStepStatus(maxBookingID, response);
			if(MapUtils.isNotEmpty(response)) {
				return response;
			}

			validateAndSetResponseMessage(response,  serverBotModel, maxBookingID);

			String stepID = stopStep(serverBotModel);
			serverBotModel.setStepID(stepID);

			response.put(AppConstants.FLOWCHART_TYPE,  wOExecutionDAO.getFlowChartType(serverBotModel.getwOID()));
			
			isSuccess=updateFCStepDetailsAndBotStatus(response, serverBotModel, maxBookingID, woDetails, hours);
			
			wOExecutionDAO.updateRpaFailureStatus(serverBotModel.getwOID(), serverBotModel.getTaskID(),
					maxBookingID.getBookingID(), serverBotModel.getReason(), serverBotModel);
			hours = wOExecutionDAO.getBookingHoursForStep(serverBotModel.getStepID(), serverBotModel.getwOID());

			response.put(AppConstants.HOUR, hours);
			response.put(AppConstants.IS_SUCCESS, isSuccess);
		} catch (Exception e) {
			e.printStackTrace();
			response.put(AppConstants.IS_SUCCESS, false);
			response.put(AppConstants.MSG, e.getMessage());
		}
		return response;
	}
	// v1/stopInprogressTask
	@Transactional("transactionManager")
	public Map<String, Object> stopWorkOrderTaskV1(ServerBotModel serverBotModel) {

		Map<String, Object> response = new HashMap<>();
		WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(serverBotModel.getwOID());
		if(StringUtils.equalsIgnoreCase(serverBotModel.getRefferer(),AppConstants.AUTO_SENSE)) {
			try {
				validationUtilityService.validateTaskIDForStartTaskV1(serverBotModel, woDetails);
			} catch(Exception e) {
				response.put(AppConstants.IS_SUCCESS, false);
				response.put(AppConstants.MSG, e.getMessage());
				return response;
			}
		}
		
		String msg = StringUtils.EMPTY;
		boolean playNext = false;
		boolean isSuccess = true;
		
			WorkFlowBookingDetailsModel maxBookingID = wOExecutionDAO.stopWOMaxBookingID(serverBotModel.getwOID(),
					serverBotModel.getTaskID());
			String hours = wOExecutionDAO.getBookingHoursForStep(serverBotModel.getStepID(), serverBotModel.getwOID());
			if (maxBookingID == null) {

				response.put(AppConstants.MSG, "Please Start Step First!");
				response.put("playNext", playNext);
				response.put(AppConstants.IS_SUCCESS, false);
				return response;
			}
			if (StringUtils.equals(AppConstants.COMPLETED, maxBookingID.getStatus())
					|| StringUtils.equals(AppConstants.SKIPPED, maxBookingID.getStatus())) {

				response.put(AppConstants.MSG, "Step is already completed");
				response.put("playNext", playNext);
				response.put(AppConstants.IS_SUCCESS, false);
				return response;
			}
			if (StringUtils.equalsIgnoreCase(AppConstants.MANUAL, serverBotModel.getExecutionType())) {

				msg = "Manual Step Stopped!!";
				playNext = true;

			} else if (StringUtils.equalsIgnoreCase(AppConstants.AUTOMATIC, serverBotModel.getExecutionType())
					&& !StringUtils.equalsIgnoreCase(AppConstants.SOFT_HUMAN, serverBotModel.getRefferer())
					&& !StringUtils.equalsIgnoreCase(AppConstants.GNET, serverBotModel.getRefferer())) {

				if (StringUtils.equalsIgnoreCase(AppConstants.BOOKING_TYPE_QUEUED, maxBookingID.getType())) {

					msg = "Bot in queued state!!";
					playNext = false;
				} else if (StringUtils.equalsIgnoreCase(AppConstants.BOOKING, maxBookingID.getType())
						&& StringUtils.isBlank(maxBookingID.getReason())) {
					if(StringUtils.equalsIgnoreCase(AppConstants.VOICE, serverBotModel.getRefferer())) {
						msg="Step Stopped!!";
					}else {
						msg = "Bot is already executing. It is not recommended to stop it in between. Please wait!!!";
					}

					playNext = false;
				} else if (StringUtils.equalsIgnoreCase(AppConstants.BOOKING, maxBookingID.getType())
						&& StringUtils.equalsIgnoreCase(AppConstants.SUCCESS, maxBookingID.getReason())) {

					msg = "Task Closure Successful. Current Step Type was in Booking State!!";
					playNext = true;

				} else if (StringUtils.equalsIgnoreCase(AppConstants.BOOKING, maxBookingID.getType())
						&& StringUtils.isNotBlank(maxBookingID.getReason())
						&& !StringUtils.equalsIgnoreCase(AppConstants.SUCCESS, maxBookingID.getReason())) {

					msg = "Task not Closed Successful. Current Step Type was in Booking State and reason is not Successfull";
					playNext = false;
				}
			}

			String stepID = stopStep(serverBotModel);
			serverBotModel.setStepID(stepID);
		

			String status = StringUtils.EMPTY;
		
			response.put("flowChartType",  wOExecutionDAO.getFlowChartType(serverBotModel.getwOID()));
			if (StringUtils.equalsIgnoreCase(AppConstants.AUTOMATIC, serverBotModel.getExecutionType()) && 
					(StringUtils.equalsIgnoreCase(AppConstants.SOFT_HUMAN, serverBotModel.getRefferer())
							|| StringUtils.equalsIgnoreCase(AppConstants.VOICE, serverBotModel.getRefferer()))) {

				if (StringUtils.equalsIgnoreCase(AppConstants.SUCCESS, serverBotModel.getStatus())
						|| StringUtils.equals(ZERO_STRING, serverBotModel.getStatus())) {

					status = AppConstants.SUCCESS;
					isSuccess = true;
				} else {

					status = AppConstants.FAILED;
					isSuccess = false;
				}
				updateRPABotStatus(serverBotModel.getwOID(), serverBotModel.getTaskID(), maxBookingID.getBookingID(),
						serverBotModel.getSignumID(), status);
				updateFlowChartStepDetails(serverBotModel.getwOID(), serverBotModel.getTaskID(),
						maxBookingID.getBookingID(), AppConstants.COMPLETED, serverBotModel.getSignumID());

				// Fetching max bookingID again because in case of auto step parallel execution
				// sometimes max booking id differ
				maxBookingID = wOExecutionDAO.stopWOMaxBookingID(serverBotModel.getwOID(), serverBotModel.getTaskID());
				wOExecutionDAO.saveAutomaticStepClosureDetails(serverBotModel, hours, maxBookingID.getBookingID());
				
				
				SignalrModel signalRModel = appService.returnSignalrConfiguration(
						getWoAutoTaskInfoModel(serverBotModel, maxBookingID, woDetails.getFlowchartdefid(), response.get("flowChartType").toString()),
						configurations.getStringProperty(ConfigurationFilesConstants.HUB_METHOD_NAME));
				appService.callSignalrApplicationToCallSignalRHub(signalRModel);
			}else if (StringUtils.equalsIgnoreCase(AppConstants.MANUAL, serverBotModel.getExecutionType())
					|| StringUtils.equalsIgnoreCase(AppConstants.DESKTOP, serverBotModel.getRefferer())){
				
					SignalrModel signalrModel=getValidHubMethodsforSignalrCallBySourceStopTask(serverBotModel,
							maxBookingID,woDetails.getFlowchartdefid(), response.get("flowChartType").toString());
					appService.callSignalrApplicationToCallSignalRHub(signalrModel);
				
			}
			wOExecutionDAO.updateRpaFailureStatus(serverBotModel.getwOID(), serverBotModel.getTaskID(),
					maxBookingID.getBookingID(), serverBotModel.getReason(), serverBotModel);
			hours = wOExecutionDAO.getBookingHoursForStep(serverBotModel.getStepID(), serverBotModel.getwOID());
			response.put("hour", hours);
			response.put(AppConstants.MSG, msg);
			response.put("playNext", playNext);
			response.put(AppConstants.IS_SUCCESS, isSuccess);
		return response;
	}
	
	private Map<String, Object> validateStepStatus(WorkFlowBookingDetailsModel maxBookingID, Map<String, Object> response) {
		
		if (maxBookingID == null) {
			response.put(AppConstants.MSG, AppConstants.START_STEP_FIRST);
			response.put(AppConstants.PLAY_NEXT, false);
			response.put(AppConstants.IS_SUCCESS, false);
		}
		if (StringUtils.equals(AppConstants.COMPLETED, maxBookingID.getStatus())
				|| StringUtils.equals(AppConstants.SKIPPED, maxBookingID.getStatus())) {

			response.put(AppConstants.MSG, AppConstants.STEP_ALREADY_COMPLETED);
			response.put(AppConstants.PLAY_NEXT, false);
			response.put(AppConstants.IS_SUCCESS, false);
			
		}		
		return response;
	}

	private boolean updateFCStepDetailsAndBotStatus(Map<String, Object> response, ServerBotModel serverBotModel,
			WorkFlowBookingDetailsModel maxBookingID, WorkOrderModel woDetails, String hours) {
		String status = StringUtils.EMPTY;
		boolean isSuccess = true;
		
		if (StringUtils.equalsIgnoreCase(AppConstants.AUTOMATIC, serverBotModel.getExecutionType()) && 
				(StringUtils.equalsIgnoreCase(AppConstants.SOFT_HUMAN, serverBotModel.getRefferer())
						|| StringUtils.equalsIgnoreCase(AppConstants.VOICE, serverBotModel.getRefferer()))) {

			if (StringUtils.equalsIgnoreCase(AppConstants.SUCCESS, serverBotModel.getStatus())
					|| StringUtils.equals(ZERO_STRING, serverBotModel.getStatus())) {

				status = AppConstants.SUCCESS;
				isSuccess = true;
			} else {

				status = AppConstants.FAILED;
				isSuccess = false;
			}
			updateRPABotStatus(serverBotModel.getwOID(), serverBotModel.getTaskID(), maxBookingID.getBookingID(),
					serverBotModel.getSignumID(), status);
			updateFlowChartStepDetails(serverBotModel.getwOID(), serverBotModel.getTaskID(),
					maxBookingID.getBookingID(), AppConstants.COMPLETED, serverBotModel.getSignumID());

			// Fetching max bookingID again because in case of auto step parallel execution
			// sometimes max booking id differ
			maxBookingID = wOExecutionDAO.stopWOMaxBookingID(serverBotModel.getwOID(), serverBotModel.getTaskID());
			wOExecutionDAO.saveAutomaticStepClosureDetails(serverBotModel, hours, maxBookingID.getBookingID());

			WoAutoTaskInfoModel woAutoTaskInfo=getWoAutoTaskInfoModel(serverBotModel, maxBookingID, woDetails.getFlowchartdefid(),
					response.get(AppConstants.FLOWCHART_TYPE).toString());
			
			WorkFlowStepDataModel stepdetails=wOExecutionDAO.getWFStepData(serverBotModel.getStepID(),woDetails.getFlowchartdefid(),serverBotModel.getExecutionType());
			woAutoTaskInfo.setRpaId(stepdetails.getRpaID());
			woAutoTaskInfo.setBotType(stepdetails.getBotType());
			woAutoTaskInfo.setIsRunOnServer(stepdetails.getIsRunOnServer().equalsIgnoreCase("1")?true:false);
			woAutoTaskInfo.setIsInputRequired(stepdetails.isInputRequired());
			
			LOG.info("*****SignalR Model Payload [ StopTask] : *******" + woAutoTaskInfo.toString());
			SignalrModel signalRModel = signalrUtil.returnDefaultSignalrConfiguration
										.get()
										.methodName(configurations.getStringProperty(ConfigurationFilesConstants.HUB_METHOD_NAME))
										.payload(woAutoTaskInfo);
			
			
				if(signalRModel!=null) {
					LOG.info("*****SignalR Model Payload is not null-------------------------------------: *******" + signalRModel.toString());
					signalrUtil.callSignalrApplicationToCallSignalRHub(signalRModel);
				}
				else {
					LOG.info("*****signalRModel  are null ====================================== : *******" );
				}
				
		}else if (StringUtils.equalsIgnoreCase(AppConstants.MANUAL, serverBotModel.getExecutionType())
				|| StringUtils.equalsIgnoreCase(AppConstants.DESKTOP, serverBotModel.getRefferer())){

			SignalrModel signalrModel=getValidHubMethodsforSignalrCallBySourceStopTask(serverBotModel,
					maxBookingID,woDetails.getFlowchartdefid(), response.get(AppConstants.FLOWCHART_TYPE).toString());
					if(signalrModel!=null) {
						signalrUtil.callSignalrApplicationToCallSignalRHub(signalrModel);
					}
		}
		return isSuccess;
		
	}
	
	private void validateAndSetResponseMessage(Map<String, Object> response, ServerBotModel serverBotModel, WorkFlowBookingDetailsModel maxBookingID){

		String msg = StringUtils.EMPTY;
		boolean playNext = false;
		if (StringUtils.equalsIgnoreCase(AppConstants.MANUAL, serverBotModel.getExecutionType())) {
			msg = AppConstants.MANUAL_STEP_STOP_SUCCESS;
			playNext = true;
		}else if(StringUtils.equalsIgnoreCase(AppConstants.MANUAL_DISABLED, serverBotModel.getExecutionType())) {
			msg = AppConstants.MANUAL_DISABLED_STEP_STOP_SUCCESS;
			playNext = true;
		}else if (StringUtils.equalsIgnoreCase(AppConstants.AUTOMATIC, serverBotModel.getExecutionType())
				&& !StringUtils.equalsIgnoreCase(AppConstants.SOFT_HUMAN, serverBotModel.getRefferer())
				&& !StringUtils.equalsIgnoreCase(AppConstants.GNET, serverBotModel.getRefferer())) {

			if (StringUtils.equalsIgnoreCase(AppConstants.BOOKING_TYPE_QUEUED, maxBookingID.getType())) {

				msg = AppConstants.BOT_QUEUED_STATE;
				playNext = false;
			} else if (StringUtils.equalsIgnoreCase(AppConstants.BOOKING, maxBookingID.getType())
					&& StringUtils.isBlank(maxBookingID.getReason())) {
				if(StringUtils.equalsIgnoreCase(AppConstants.VOICE, serverBotModel.getRefferer())) {
					msg=AppConstants.STEP_STOPPED;
				}else {
					msg = AppConstants.ALREADY_EXECUTING;
				}
				playNext = false;
			} else if (StringUtils.equalsIgnoreCase(AppConstants.BOOKING, maxBookingID.getType())
					&& StringUtils.equalsIgnoreCase(AppConstants.SUCCESS, maxBookingID.getReason())) {

				msg = AppConstants.CLOSED_SUCCESSFULL;
				playNext = true;
			} else if (StringUtils.equalsIgnoreCase(AppConstants.BOOKING, maxBookingID.getType())
					&& StringUtils.isNotBlank(maxBookingID.getReason())
					&& !StringUtils.equalsIgnoreCase(AppConstants.SUCCESS, maxBookingID.getReason())) {

				msg = AppConstants.NOT_CLOSED_SUCCESSFULL;
				playNext = false;
			}
		}
		response.put(AppConstants.MSG, msg);
		response.put(AppConstants.PLAY_NEXT, playNext);
	}
	
	private SignalrModel getValidHubMethodsforSignalrCallBySourceStopTask(ServerBotModel serverBotModel, WorkFlowBookingDetailsModel maxBookingID,
			int flowchartdefid,String flowChartType) {
		
		SignalrModel signalRModel = null;
		switch(serverBotModel.getRefferer().toUpperCase()) {
			case AppConstants.DESKTOP:
				signalRModel= signalrUtil.returnDefaultSignalrConfiguration
										.get()
										.methodName(configurations.getStringProperty(ConfigurationFilesConstants.HUB_METHOD_NAME))
										.payload(getWoAutoTaskInfoModel(serverBotModel, maxBookingID, flowchartdefid, flowChartType));
				break;
			case AppConstants.VOICE:
				if(activityMasterService.isSignumExist(serverBotModel.getSignumID())) {
					signalRModel=signalrUtil.returnDefaultSignalrConfiguration
											.get()
											.methodName(configurations.getStringProperty(ConfigurationFilesConstants.HUB_METHOD_NAME))
											.payload(getWoAutoTaskInfoModel(serverBotModel, maxBookingID, flowchartdefid, flowChartType));
				}
				break;
			case AppConstants.AUTO_SENSE:
					signalRModel=signalrUtil.returnDefaultSignalrConfiguration
											.get()
											.methodName(AppConstants.UPDATE_WO_STATUS_WEB_FW)
											.payload(getWoAutoTaskInfoModel(serverBotModel, maxBookingID, flowchartdefid, flowChartType));
				break;
		
		}
		return signalRModel;
	}

	public WoAutoTaskInfoModel getWoAutoTaskInfoModel(ServerBotModel serverBotModel, WorkFlowBookingDetailsModel maxBookingID,int flowchartdefid,String flowChartType){
		
		maxBookingID = wOExecutionDAO.stopWOMaxBookingID(serverBotModel.getwOID(), serverBotModel.getTaskID());
		String hour = wOExecutionDAO.getBookingHoursForStep(serverBotModel.getStepID(), serverBotModel.getwOID());
		WoAutoTaskInfoModel woautotask= new WoAutoTaskInfoModel(serverBotModel.getSignumID(), serverBotModel.getwOID(), 
				maxBookingID.getBookingID(), serverBotModel.getStepID(), serverBotModel.getTaskID(), hour,maxBookingID.getStatus(), 
				serverBotModel.getReason(),  flowchartdefid,
				 serverBotModel.getExecutionType(),flowChartType,serverBotModel.getRefferer(),AppConstants.STOP);
		woautotask.setOutputLink(serverBotModel.getOutputLink());
		LOG.info(woautotask.toString());
		return woautotask;
	}

	public List<InProgressWorkOrderModel> getInProgressWorkOrderDetails(String signumID) {
		return wOExecutionDAO.getInProgressWorkOrderDetails(signumID);

	}

	public List<WorkflowStepDetailModel> getWorkflowStepDetails(int subActivityFlowChartDefID) {
		return wOExecutionDAO.getWorkflowStepDetails(subActivityFlowChartDefID);

	}

	public List<WorkflowStepDetailModel> getWorkflowStepDetailsForComments(int subActivityFlowChartDefID) {
		return wOExecutionDAO.getWorkflowStepDetailsForComments(subActivityFlowChartDefID);

	}

	public WorkflowStepDetailModel getStepDetailsByFcDefId(String stepId, String fcDefId) {
		return wOExecutionDAO.getStepDetailsByFcDefId(stepId, fcDefId);

	}

	public WorkflowStepDetailModel getStepDetailsByStepIdAndFcDefId(String stepId, String fcDefId) {
		return wOExecutionDAO.getStepDetailsByStepIdAndFcDefId(stepId, fcDefId);

	}

	@Transactional("transactionManager")
	public Response<List<WorkOrderCompleteDetailsModel>> getCompleteWorkOrderDetails(int wOID) {
		Response<List<WorkOrderCompleteDetailsModel>> response=new Response<>();
		try {
			List<WorkOrderCompleteDetailsModel> workOrder = wOExecutionDAO.getCompleteWorkOrderDetails(wOID, AppConstants.ALL);
			response.setResponseData(workOrder);
			// external Group set in model
			if (CollectionUtils.isEmpty(workOrder)) {
				throw new ApplicationException(200, "No data exists for given work order ID");
			}
		}catch (Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
		}
		return response;
	}

	@Transactional("transactionManager")
	public void updateWorkOrderUserLevel(WorkOrderModel workOrderModel) {
		Boolean checkStatus = wOExecutionDAO.checkWorkOrder(workOrderModel.getwOID());
		if (checkStatus) {
			throw new ApplicationException(500, "Work Order is Closed");
		} else {
			wOExecutionDAO.updateWorkOrderUserLevel(workOrderModel);
		}
	}

	@Transactional("transactionManager")
	public Map<String, Object> updateWFDefinition(String wOID, String wFJson, String signum) {
		Map<String, Object> wfData = new HashMap<>();
		if (!wOID.equalsIgnoreCase(ZERO_STRING)) {
			try {
				
				//check for penguin and macro bots
				if (Boolean.TRUE.equals(parseJsonToGetRPAID(wFJson))) {
					wfData.put("Error","PENGUIN AND MACRO BOTS ARE NOT ALLOWED!");
					return wfData;
				}
				
				
				MailModel model = null;
				Map<String, String> projectInfo = null;
				Map<String, String> data = wOExecutionDAO.getWfVersionDetails(wOID);
				Map<String, String> params = new HashMap<String, String>();
				params.put("wOID", wOID);
				params.put("wFJson", wFJson);
				params.put("signum", signum);
				params.put("version", data.get("WorkFlowVersion"));
				params.put("flowChartDefID", data.get("SubActivityFlowChartDefID"));
				params.put("subAct", ZERO_STRING);
				wOExecutionDAO.updateWFDefinition(params);
				int subActivityID = wOExecutionDAO.getSubActivityIDForWOID(Integer.valueOf(wOID));
				int latestVersioNo = wOExecutionDAO.getWFLatestVersion(Integer.parseInt(params.get("subAct")));
				
				
				parseJsonAndInsertStepsForWFDefinition(wFJson, Integer.parseInt(params.get("subAct")), latestVersioNo,
						subActivityID);
				try {
//                    int proficiencyId=wOExecutionDAO.getProficiencyId(wOID,signum);
					ProficiencyTypeModal proficiency = appService.getProficiencyID(AppConstants.ASSESSED);
                    
//					wfData = getWOWorkFlow(Integer.valueOf(wOID), false,signum);
					wfData = getWOWorkFlow(Integer.valueOf(wOID), proficiency.getProficiencyID(),signum).getBody().getResponseData();
					
					projectInfo = wOExecutionDAO.getWorkOrderData(wOID);
					model = woPlanService.getWoMailNotificationDetails(Integer.valueOf(wOID));
				} catch (Exception ex) {
					Logger.getLogger(WOExecutionService.class.getName()).log(Level.SEVERE, null, ex);
				}
				if (model != null && projectInfo != null 
						&& configurations.getBooleanProperty(ConfigurationFilesConstants.WORKORDER_MAIL_ENABLED, false)) {
					String mailBody = mailUtil.generateMailBodyForWFApproval(model.getProjectCreatorSignum(),
							model.getProjectCreatorName(), model.getEmployeeSignum(), model.getEmployeeName(),
							projectInfo.get("ProjectID"), projectInfo.get("SubActivity"));
					String drEmail = StringUtils.EMPTY;
					for (String emailIDs : model.getdR_EmailID()) {
						drEmail = emailIDs + AppConstants.SEMICOLON + drEmail;
					}
					woPlanService.sendMailNotification(model.getEmployeeEmailID(), mailBody, drEmail);
				}
				
			} catch (Exception ex) {
				LOG.error("ERROR while updateWorkFlow-->" + "SignumID:" + signum + ex.getMessage());
				ex.printStackTrace();
				wfData.put("Error",
						"Your Custom WorkFlow is Approved/Rejected.You can not edit again this Custom Workflow");
				return wfData;
			}
		} else {
			wfData.put("Error", "WOID cannot be NULL");
		}
		return wfData;
	}

	private void parseJsonAndInsertStepsForWFDefinition(String wFJson, Integer subActFCDefID, int versionNo,
			int subActivityID) {
		JSONObject jObject = new JSONObject(wFJson);
		JSONArray arr = jObject.getJSONArray("cells");

		for (int i = 0; i < arr.length(); i++) {
			String stepId = null;
			String stepName = null;
			String taskId = null;
			String taskName = null;
			String executionType = null;
			String toolId = null;
			String reason = null;
			String sourceID = null;
			String targetID = null;
			String rpaID = null;
			String attrType = null;
			String wiid = null;
			String outputUpload = "YES";
			String cascadeInput = AppConstants.NO;

			JSONObject types = (JSONObject) arr.get(i);
			if ("ericsson.Manual".equalsIgnoreCase((String) types.get("type"))
					|| "ericsson.Automatic".equalsIgnoreCase((String) types.get("type"))) {
				attrType = "bodyText";
			} else if ("ericsson.StartStep".equalsIgnoreCase((String) types.get("type"))
					|| "ericsson.EndStep".equalsIgnoreCase((String) types.get("type"))) {
				attrType = "text";
			} else if ("ericsson.Decision".equalsIgnoreCase((String) types.get("type"))) {
				attrType = "label";
			}

			if (arr.getJSONObject(i).has(ID)) {
				stepId = arr.getJSONObject(i).getString(ID);
			}
			if (types.has("outputUpload")) {
				outputUpload = types.getString("outputUpload");
			}
			if (types.has("cascadeInput")) {
				outputUpload = types.getString("cascadeInput");
			}
			if (arr.getJSONObject(i).has(ATTRS)) {
				if (arr.getJSONObject(i).getJSONObject(ATTRS).has(attrType)) {
					String step = arr.getJSONObject(i).getJSONObject(ATTRS).getJSONObject(attrType).getString("text");
					String part[] = step.split("\n\n");
					stepName = part[0];
				}
				if (arr.getJSONObject(i).getJSONObject(ATTRS).has("task")) {
					taskId = String.valueOf(
							arr.getJSONObject(i).getJSONObject(ATTRS).getJSONObject("task").getString("taskID"));
					if (arr.getJSONObject(i).getJSONObject(ATTRS).has("workInstruction")
							&& arr.getJSONObject(i).getJSONObject(ATTRS).getJSONObject("workInstruction").has("WIID")) {
						wiid = String.valueOf(
								arr.getJSONObject(i).getJSONObject(ATTRS).getJSONObject("workInstruction").get("WIID"));
					}

					taskName = arr.getJSONObject(i).getJSONObject(ATTRS).getJSONObject("task").getString("taskName");
					executionType = arr.getJSONObject(i).getJSONObject(ATTRS).getJSONObject("task")
							.getString("executionType");
					toolId = String.valueOf(
							arr.getJSONObject(i).getJSONObject(ATTRS).getJSONObject("task").getString("toolID"));
					if(arr.getJSONObject(i).getJSONObject(ATTRS).has("task") &&
							arr.getJSONObject(i).getJSONObject(ATTRS).getJSONObject("task").has("reason")) {
						reason = arr.getJSONObject(i).getJSONObject(ATTRS).getJSONObject("task").getString("reason");	
					}
					else {
						reason=NULL;
					}
					
					if (String.valueOf(
							arr.getJSONObject(i).getJSONObject(ATTRS).getJSONObject("tool").get("RPAID")) == null
							|| String.valueOf(
									arr.getJSONObject(i).getJSONObject(ATTRS).getJSONObject("tool").get("RPAID"))
									.equals(StringUtils.EMPTY)
							|| String.valueOf(
									arr.getJSONObject(i).getJSONObject(ATTRS).getJSONObject("tool").get("RPAID"))
									.equals("%RPATOOL_ID%")) {
						rpaID = ZERO_STRING;
					} else {
						rpaID = String
								.valueOf(arr.getJSONObject(i).getJSONObject(ATTRS).getJSONObject("tool").get("RPAID"));
					}
					String masterTask = flowChartDao.getMasterTask(Integer.parseInt(taskId), subActivityID);
					Map<String, Object> stepData = flowChartDao.getStepExistingData(subActFCDefID, stepId.trim(),
							versionNo);

					flowChartDao.insertInFlowChartStepDetails(subActFCDefID, stepId.trim(), stepName.trim(),
							taskId.trim(), taskName.trim(), executionType.trim(), toolId.trim(), StringUtils.trim(reason),
							versionNo, masterTask, (String) types.get("type"), rpaID, wiid, stepData, outputUpload,
							cascadeInput);
				} else if (!"app.Link".equalsIgnoreCase((String) types.get("type"))) {
					Map<String, Object> stepData = flowChartDao.getStepExistingData(subActFCDefID, stepId.trim(),
							versionNo);
					flowChartDao.insertInFlowChartStepDetails(subActFCDefID, stepId.trim(), stepName.trim(),
							ZERO_STRING, NULL, "Manual", ZERO_STRING, NULL, versionNo, NULL, (String) types.get("type"),
							ZERO_STRING, ZERO_STRING, stepData, outputUpload, cascadeInput);
				}
			}
			if (arr.getJSONObject(i).has("router")) {
				sourceID = arr.getJSONObject(i).getJSONObject("source").getString(ID);
				targetID = arr.getJSONObject(i).getJSONObject("target").getString(ID);
				flowChartDao.insertFlowchartStepLinkDetails(subActFCDefID, sourceID.trim(), targetID.trim());
			}
		}
	}

	public int getEstdHrs(int projectID, int subActivityID, int scopeID) {
		int estdHrs;
		if (scopeID != 0) {
			estdHrs = workOrderPlanDao.getEstdHrsForScope(projectID, subActivityID, scopeID);
		} else {
			estdHrs = workOrderPlanDao.getEstdHrs(projectID, subActivityID);
		}
		return estdHrs;
	}

	@Transactional("transactionManager")
	public void createAdhocWorkOrder(AdhocWorkOrderCreationModel adhocWOObject) {

		int subActivityID;
		int estdHrs;
		Date startDate;
		subActivityID = adhocWOObject.getSubActivityID();
		estdHrs = getEstdHrs(adhocWOObject.getProjectID(), subActivityID, adhocWOObject.getScopeID());
		startDate = adhocWOObject.getStartDate();
		Date endDate = PlannedEndDateCal.calculateEndDateV2(startDate, estdHrs);
		wOExecutionDAO.createAdhocWorkOrder(adhocWOObject, endDate);
	}

	@Transactional("transactionManager")
	public Response<CreateWoResponse> saveClosureDetailsForWO(SaveClosureDetailsForWOModel saveClosureDetailsObject)
			throws IOException {

		Response<CreateWoResponse> response ;

		response = validateSaveClosureDetailsForWOModelForClosure(saveClosureDetailsObject);

		if (response.getFormErrorCount() == 0) {
			
			setSaveClosureDetailsObject(saveClosureDetailsObject);

			wOExecutionDAO.saveClosureDetails_WO(saveClosureDetailsObject);
			saveClosureDetailsObject.setAcceptedOrRejectedBy(wOExecutionDAO.getProjectCreatorSignum(saveClosureDetailsObject.getwOID()));
			wOExecutionDAO.saveClosureDetails_InsertDA(saveClosureDetailsObject);
			

			// EXECUTION PLAN CHECK
			CreateWoResponse createWoResponse = woPlanService.createExecutionPlanFLow(saveClosureDetailsObject);
			response.addFormMessage(AppConstants.WO_CLOSED_SUCCESSFULLY);
			response.setResponseData(createWoResponse);
			if (saveClosureDetailsObject.getDeliveryStatus().equalsIgnoreCase(AppConstants.FAILURE)) {

				String startDate = woPlanService.getWoStartDate(saveClosureDetailsObject.getwOID());
				if (StringUtils.isBlank(startDate)) {
					woPlanService.updateWoStartDateByCurrentDate(saveClosureDetailsObject.getwOID());
				} else {
					woPlanService.updateWoStartDates(saveClosureDetailsObject.getwOID(), startDate);
				}
			}
			if(configurations.getBooleanProperty(ConfigurationFilesConstants.WORKORDER_MAIL_ENABLED, false)) {
				sendMailForClosureDetailsOfWO(saveClosureDetailsObject);
			}
			UpdateActivityForErisite(saveClosureDetailsObject);
		}

		return response;
	}
	//v1/saveClosureDetailsForWOv1
	@Transactional("transactionManager")
	public Response<CreateWoResponse> saveClosureDetailsForWOv1(SaveClosureDetailsForWOModel saveClosureDetailsObject)
			throws IOException {

		Response<CreateWoResponse> response ;

		response = validateSaveClosureDetailsForWOModelForClosure(saveClosureDetailsObject);

		if (response.getFormErrorCount() == 0) {
			
			setSaveClosureDetailsObject(saveClosureDetailsObject);

			wOExecutionDAO.saveClosureDetails_WO(saveClosureDetailsObject);
			String signum = wOExecutionDAO.getProjectCreatorSignum(saveClosureDetailsObject.getwOID());
			saveClosureDetailsObject.setAcceptedOrRejectedBy(signum);
			wOExecutionDAO.saveClosureDetails_InsertDA(saveClosureDetailsObject);
			

			// EXECUTION PLAN CHECK
			CreateWoResponse createWoResponse = woPlanService.createExecutionPlanFLow(saveClosureDetailsObject);
			response.addFormMessage("Work Order closed successfully !!");
			response.setResponseData(createWoResponse);
			if (saveClosureDetailsObject.getDeliveryStatus().equalsIgnoreCase(AppConstants.FAILURE)) {

				String startDate = woPlanService.getWoStartDate(saveClosureDetailsObject.getwOID());
				if (StringUtils.isBlank(startDate)) {
					woPlanService.updateWoStartDateByCurrentDate(saveClosureDetailsObject.getwOID());
				} else {
					woPlanService.updateWoStartDates(saveClosureDetailsObject.getwOID(), startDate);
				}
			}

			sendMailForClosureDetailsOfWO(saveClosureDetailsObject);

			UpdateActivityForErisite(saveClosureDetailsObject);
		}

		return response;
	}


	@Transactional("transactionManager")
	public Response<CreateWoResponse> saveClosureDetailsForWOInFW(SaveClosureDetailsForWOInFWModel saveClosureDetailsObject)
			throws IOException {

		Response<CreateWoResponse> response = new Response<CreateWoResponse>();

		response = validateSaveClosureDetailsForWOModelForClosure(saveClosureDetailsObject);

		if (response.getFormErrorCount() == 0) {
			
			setSaveClosureDetailsObject(saveClosureDetailsObject);

			wOExecutionDAO.saveClosureDetails_WO(saveClosureDetailsObject);
			String signum = wOExecutionDAO.getProjectCreatorSignum(saveClosureDetailsObject.getwOID());
			saveClosureDetailsObject.setAcceptedOrRejectedBy(signum);
			wOExecutionDAO.saveClosureDetails_InsertDA(saveClosureDetailsObject);
			
			// SignalR implementation
//        	if(StringUtils.equalsIgnoreCase(AppConstants.DESKTOP,saveClosureDetailsObject.getRefferer())) {
//        		Map<String, Object> obj = new HashMap<String, Object>();
//        		obj.put("Signum",saveClosureDetailsObject.getLastModifiedBy());
//        		obj.put("WoId", saveClosureDetailsObject.getwOID());
//        		obj.put("IsWorkOrderCompleted", true);
//        		
//    			SignalrModel signalRModel = returnSignalrConfigurationForWOCompletion(obj);
//    			callSignalrApplicationToCallSignalRHub(signalRModel);
//        	}


			// EXECUTION PLAN CHECK
			CreateWoResponse createWoResponse = woPlanService.createExecutionPlanFLow(saveClosureDetailsObject);
			response.addFormMessage("Work Order closed successfully !!");
			response.setResponseData(createWoResponse);
			if (saveClosureDetailsObject.getDeliveryStatus().equalsIgnoreCase(AppConstants.FAILURE)) {

				String startDate = woPlanService.getWoStartDate(saveClosureDetailsObject.getwOID());
				if (StringUtils.isBlank(startDate)) {
					woPlanService.updateWoStartDateByCurrentDate(saveClosureDetailsObject.getwOID());
				} else {
					woPlanService.updateWoStartDates(saveClosureDetailsObject.getwOID(), startDate);
				}
			}

			sendMailForClosureDetailsOfWO(saveClosureDetailsObject);

			UpdateActivityForErisite(saveClosureDetailsObject);
		}

		return response;
	}
	
	public SignalrModel returnSignalrConfigurationForWOCompletion(Map<String, Object> data) {
		SignalrModel signalRModel = new SignalrModel();
		signalRModel.setHubName(configurations.getStringProperty(ConfigurationFilesConstants.HUB_NAME));
		//signalRModel.setHubUrl(configurations.getStringProperty(ConfigurationFilesConstants.HUB_URL));
		//signalRModel.setHubUrl(validationUtilityService.getCurrentEnvironment("BaseUrl"));
		signalRModel.setHubUrl(System.getenv("CONFIG_BASE_URL"));
		signalRModel.setMethodName("UpdateWorkOrderCompletionToClient");
		signalRModel.setExecutionType(AppConstants.SIGNALR_EXECUTION_TYPE);
		signalRModel.setPayload(data);
		return signalRModel;
	}

	
	private void setSaveClosureDetailsObject(SaveClosureDetailsForWOModel saveClosureDetailsObject)
			throws UnsupportedEncodingException {

		if (StringUtils.isNotBlank(saveClosureDetailsObject.getReason())) {

			saveClosureDetailsObject.setReason(URLDecoder.decode(saveClosureDetailsObject.getReason(), AppConstants.UTF_8_STRING));
		}

		if (StringUtils.isNotBlank(saveClosureDetailsObject.getStatusComment())) {

			saveClosureDetailsObject
					.setStatusComment(URLDecoder.decode(saveClosureDetailsObject.getStatusComment(), AppConstants.UTF_8_STRING));
		}

	}

	private Response<CreateWoResponse> validateSaveClosureDetailsForWOModelForClosure(
			SaveClosureDetailsForWOModel saveClosureDetailsObject) {

		Response<CreateWoResponse> response = new Response<CreateWoResponse>();

		WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(saveClosureDetailsObject.getwOID());

		validateWOClosureForAll(woDetails, saveClosureDetailsObject, response);

		if (response.getFormErrorCount() > 0) {
			return response;
		}
		if (StringUtils.isBlank(saveClosureDetailsObject.getExternalSourceName())
				|| StringUtils.equalsIgnoreCase(saveClosureDetailsObject.getExternalSourceName(), AppConstants.WEB)) {
			validateWOClosureForNonExternalSources(woDetails, saveClosureDetailsObject, response);
		}
			return response;
	}

	private void validateWOClosureForNonExternalSources(WorkOrderModel woDetails,
			SaveClosureDetailsForWOModel saveClosureDetailsObject, Response<CreateWoResponse> response) {

		if (StringUtils.equalsIgnoreCase(AppConstants.FAILED, saveClosureDetailsObject.getDeliveryStatus())
				|| StringUtils.equalsIgnoreCase(AppConstants.SUCCESS, saveClosureDetailsObject.getDeliveryStatus())
						&& (StringUtils.isBlank(saveClosureDetailsObject.getReason()))) {

			response.addFormError(AppConstants.REASON_NULL_ERROR);

		} else if (StringUtils.equalsIgnoreCase(AppConstants.OTHERS, saveClosureDetailsObject.getReason())
				&& StringUtils.isBlank(saveClosureDetailsObject.getStatusComment())) {
			response.addFormError(AppConstants.COMMENT_MANDATORY_ERROR);

		} else if (StringUtils.equalsIgnoreCase(AppConstants.SUCCESS, saveClosureDetailsObject.getDeliveryStatus())) {

			if (wOExecutionDAO.checkWorkOrderBookingStatusReason(saveClosureDetailsObject.getwOID())) {
				response.addFormError(AppConstants.STEP_FAILED_ERROR);
			}
		}

	}

	private void validateWOClosureForAll(WorkOrderModel woDetails,
			SaveClosureDetailsForWOModel saveClosureDetailsObject, Response<CreateWoResponse> response) {

		if (StringUtils.isBlank(woDetails.getSignumID())) {
			response.addFormError(AppConstants.WO_UNASSIGNED);
		}
		if (wOExecutionDAO.checkWorkOrderBookingStatus(saveClosureDetailsObject.getwOID())) {
			response.addFormError(AppConstants.TASK_INPROGRESS_ERROR);
		}
		if (wOExecutionDAO.checkWorkOrder(saveClosureDetailsObject.getwOID())) {
			response.addFormError(AppConstants.WO_ALREADY_CLOSED_ERROR);
		}
	}

	private void UpdateActivityForErisite(SaveClosureDetailsForWOModel saveClosureDetailsObject) {
		Response<String> apiResponse = new Response<String>();
		try {

		String pattern = AppConstants.STRING_PATTERN;
		Matcher m = Pattern.compile(pattern).matcher(saveClosureDetailsObject.getwOName());
		String sourceSystemId = StringUtils.EMPTY;
		if (m.find()) {
			sourceSystemId = m.group(1);
		}
		SourceSystemDetailsModel sourceSystemDetails= wOExecutionDAO.getSourceSystemDetails(sourceSystemId);
		
//		Map<String, Object> sourceSystemDetails = wOExecutionDAO.getSourceSystemDetails(sourceSystemId);

			if (sourceSystemDetails != null && StringUtils
					.equalsIgnoreCase((String) sourceSystemDetails.getExternalGroup(), AppConstants.ERISITE)) {
//			Map<String, Object> completedDetails = wOExecutionDAO.checkIfAllTaskCompleted(saveClosureDetailsObject.getwOID());

			if (wOExecutionDAO.checkIfAllTaskCompleted(saveClosureDetailsObject.getwOID()) == 0) {
				pattern = AppConstants.STRING_PATTERN_MATCH;
					Matcher m1 = Pattern.compile(pattern).matcher(saveClosureDetailsObject.getwOName());
				String woName = StringUtils.EMPTY;
				if (m1.find()) {
					woName = m1.group(1);
				}
				//BulkWOCreationDetailsModel woData =  wOExecutionDAO.getWOCreationDeatilsByWOName(woName);
				Map<String, Object> woData = wOExecutionDAO.getWOCreationDeatilsByWOName(woName);
				//WorkOrderModel woDetails = wOExecutionDAO.getWODetails(saveClosureDetailsObject.getwOID());
				Map<String, Object> woDetails = wOExecutionDAO.getWODetails(saveClosureDetailsObject.getwOID());

					EventPublisherURLMappingModel eventPublisherURLMappingModel = getEventPublisherURLMappingModel(
							AppConstants.ERISITE, AppConstants.WO_CLOSURE);
					eventPublisherURLMappingModel.setPayload(getFinalPayLoad(woData, woDetails, eventPublisherURLMappingModel));

					// set dynamic headers
					List<HeaderModel> headerModels = getHeaders(woData, saveClosureDetailsObject);
					EventPublisherRequestModel eventPublisherRequestModel = new EventPublisherRequestModel(
							eventPublisherURLMappingModel.getSourceUrl(), eventPublisherURLMappingModel.getPayload(),
							eventPublisherURLMappingModel.getSourceID(), AppConstants.ERISITE, headerModels,
							HttpMethod.POST, eventPublisherURLMappingModel.getCategory());
					// Get OutlookService via Retrofit
//					EventPublisherService eventPublisherService = EventPublisherServiceBuilder.getEventPublisherService(
//							eventPublisherRequestModel,
//							configurations.getStringProperty(ConfigurationFilesConstants.EVENT_PUB_EXT_URL));

//					apiResponse = eventPublisherService.sendRequestToExternalSources(eventPublisherRequestModel)
//							.execute().body();
					apiResponse = eventPublisherServiceImpl.sendRequestToExternalSources(eventPublisherRequestModel);
				}
			}
		} catch (Exception e1) {
            LOG.info("inside catch block of UpdateActivityForErisite. getting error with error message as :"+e1.getMessage()+"---------"+e1.getCause());
			apiResponse.addFormMessage(e1.getMessage());
		}

		LOG.info(AppConstants.API_RESPONSE, apiResponse);
		System.out.println(apiResponse);
	}
	
	private List<HeaderModel> getHeaders(Map<String, Object> woData, SaveClosureDetailsForWOModel saveClosureDetailsObject) {

		List<HeaderModel> headers = new ArrayList<HeaderModel>();

		headers.add(
				new HeaderModel(TRANSACTION_ID, getStringFromMap(woData, StringUtils.lowerCase(TRANSACTION_ID), TEST)));

		headers.add(new HeaderModel(MESSAGE_BATCH_ID,
				getStringFromMap(woData, StringUtils.lowerCase(MESSAGE_BATCH_ID), TEST)));

		headers.add(new HeaderModel(SOURCE_SYSTEM_ID,
				getStringFromMap(woData, StringUtils.lowerCase(SOURCE_SYSTEM_ID), TEST)));

		headers.add(new HeaderModel(BUSINESSENTITYID, getStringFromMap(woData, BUSINESSENTITYID, TEST)));

		headers.add(new HeaderModel("enduserid", saveClosureDetailsObject.getLastModifiedBy()));

		return headers;
	}

	private String getFinalPayLoad(Map<String, Object> woData, Map<String, Object> woDetails,
			EventPublisherURLMappingModel eventPublisherURLMappingModel) throws ParseException  {
		
		//String payLoad = eventPublisherURLMappingModel.getPayload();
		
		String payLoad = "<?xml version=\"1.0\" encoding=\"utf-8\"?><info:UpdateNroActivity xmlns:info=\"urn:Namespaces.Ericsson.Com:Nrm:Nro:Activity:v0001\">  "
						+ "<info:HasFault>false</info:HasFault>  <info:ActivityDetails>    <info:ActivityCoreDetails>      <info:SystemRecordId>SYSTEM_RECORD_ID</info:SystemRecordId> "
						+ "     <info:RecordName>RECORD_NAME</info:RecordName>      <info:Status>COMPLETED</info:Status>    "
						+ "  <info:ESRUrl></info:ESRUrl>    </info:ActivityCoreDetails>   "
						+ " <info:ParentReferences>      <info:ParentProjectIntegratedScoping>PARENT_PROJECT_INTEGRATED_SCOPING</info:ParentProjectIntegratedScoping>     "
						+ " <info:ParentWorkPlanRecordID>PARENT_WORK_PLAN_RECORD_ID</info:ParentWorkPlanRecordID>    </info:ParentReferences>    <info:Schedule>      "
						+ "<info:ForecastStartDate>FORECASTSTARTDATE</info:ForecastStartDate>  <info:ActualStartDate>FORECASTSTARTDATE</info:ActualStartDate>     <info:ActualEndDate>ACTUALENDDATE</info:ActualEndDate>    </info:Schedule>"
						+ "    <info:Assignment>      <info:AssignedGroup>ASSIGNED_GROUP</info:AssignedGroup>  </info:Assignment>  </info:ActivityDetails></info:UpdateNroActivity>";

		
		payLoad = payLoad.replace("SYSTEM_RECORD_ID",
				getStringEscapeHtmlFromMap(woData, "SystemRecordId", NULL));

		payLoad = payLoad.replace("RECORD_NAME",
				getStringEscapeHtmlFromMap(woData, "RecordName", NULL));

		payLoad = payLoad.replace("PARENT_PROJECT_INTEGRATED_SCOPING",
				getStringEscapeHtmlFromMap(woData, "ParentProjectIntegratedScoping", NULL));

		payLoad = payLoad.replace(PARENT_WORK_PLAN_RECORD_ID,
				Integer.toString((int) woData.get("ParentWorkPlanId")));

		payLoad = payLoad.replace("ASSIGNED_GROUP",
				getStringEscapeHtmlFromMap(woData, "AssignedGroup", NULL));

		payLoad = payLoad.replace(PARENT_WORK_PLAN_RECORD_ID,
				getStringEscapeHtmlFromMap(woData, "ParentWorkPlanRecordID", NULL));

		payLoad = payLoad.replace("FORECASTSTARTDATE",
				 getStringDateFromMap(woDetails, "ActualStartDateUTC", NULL));

		payLoad = payLoad.replace("ACTUALENDDATE",
				getStringDateFromMap(woDetails, "ActualEndDateUTC", NULL));

		LOG.info("XML String", payLoad);
		System.out.println(payLoad);
		
		return payLoad;
	}

	private String getStringFromMap(Map<String, Object> map, String key, String defaultValue) {

		if(map.containsKey(key) && map.get(key) != null) {
			
			return (String) map.get(key);
		} else {
			
			return defaultValue;
				}
		
			}
	
	private String getStringEscapeHtmlFromMap(Map<String, Object> map, String key, String defaultValue) {

		if(map.containsKey(key) && map.get(key) != null) {
			
			return StringEscapeUtils.escapeHtml((String) map.get(key));
		} else {
			
			return defaultValue;
		}
		
	}
	
	private String getStringDateFromMap(Map<String, Object> map, String key, String defaultValue) throws ParseException {

		if(map.containsKey(key) && map.get(key) != null) {
			
			Date date = (Date)map.get(key);
			return DateTimeUtil.convertDateToString(date, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		} else {
			
			return defaultValue;
		}
		
	}

	private void sendMailForClosureDetailsOfWO(SaveClosureDetailsForWOModel saveClosureDetailsObject) {

		MailModel model = woPlanService.getWoMailNotificationDetails(saveClosureDetailsObject.getwOID());

		if (model != null) {

			List<String> integratedSource = wOExecutionDAO.getIntegratedSource(model.getCreatedBy_Signum());
			WorkOrderMailModel workOrderMailModel;

			if (CollectionUtils.isEmpty(integratedSource)) {

				workOrderMailModel = new WorkOrderMailModel(model.getCreatedBy_Signum(),
						model.getCreated_EmployeeName(), model.getEmployeeSignum(), model.getEmployeeName(),
						model.getCreated_EmployeeEmailID(), model.getEmployeeEmailID(), AppConstants.CLOSED,
						saveClosureDetailsObject.getwOID(), AppConstants.NA,
						getNodesByWoId(saveClosureDetailsObject.getwOID()));

			} else {

				workOrderMailModel = new WorkOrderMailModel(model.getProjectCreatorSignum(),
						model.getProjectCreatorName(), model.getEmployeeSignum(), model.getEmployeeName(),
						model.getEmployeeEmailID(), model.getProjectCreatorEmailID(), AppConstants.CLOSED,
						saveClosureDetailsObject.getwOID(), AppConstants.NA,
						getNodesByWoId(saveClosureDetailsObject.getwOID()));
			}

			sendMailForWorkOrderClosure(workOrderMailModel, model);

		}
	}

	private void sendMailForWorkOrderClosure(WorkOrderMailModel workOrderMailModel, MailModel model) {
		// Lambda Runnable
		Runnable task2 = () -> {
			try {
				sendMail(workOrderMailModel, model);
			} catch (Exception e) {
				LOG.debug(String.format("Exception thrown  %s", e.getMessage()));
			}
		};

		// start the thread
		new Thread(task2).start();

	}

	public void sendMail(WorkOrderMailModel workOrderMailModel, MailModel model) {
		String mailBody = mailUtil.generateMailBodyForWorkOrders(workOrderMailModel.getSenderID(),
				workOrderMailModel.getSenderName(), workOrderMailModel.getReceiverID(),
				workOrderMailModel.getReceiverName(), workOrderMailModel.getStatus(), workOrderMailModel.getWoID(),
				workOrderMailModel.getPlannedStart(), workOrderMailModel.getNeID());

		String drEmail = StringUtils.EMPTY;
		for (String emailIDs : model.getdR_EmailID()) {
			drEmail = emailIDs + AppConstants.SEMICOLON + drEmail;
		}
		woPlanService.sendMailNotification(workOrderMailModel.getSenderEmailID(), mailBody,
				workOrderMailModel.getReceiverEmailID() + AppConstants.SEMICOLON + drEmail);
	}

	@Transactional("transactionManager")
	public String getNodesByWoId(int wOID) {

		return wOExecutionDAO.getNodesByWoId(wOID);
	}

	/**
	 * 
	 * @param wOID
	 * @param proficiencyID
	 * @param signum 
	 * @return Map<String, Object>
	 * @throws Exception 
	 */
	@Transactional("transactionManager")
	public ResponseEntity<Response<Map<String, Object>>> getWOWorkFlow(int wOID, int proficiencyID, String signum) throws Exception {
		return checkWorkOrderWorkFlowMethod(wOID, proficiencyID,signum);
	}

	private void updateJSON(JSONObject types, String stepID, int subActivityDefID, String attrType, String sadCount,String executionType) {
		String toolID = AppConstants.TWO_AS_A_STRING;
		String toolName = AppConstants.NO_TOOL;
		String rpaID = ZERO_STRING;
		String rpaName = AppConstants.RPA;
		String isRunOnServer = ZERO_STRING;
		boolean isInputRequired = false;

		JSONObject attrs = (JSONObject) types.get(ATTRS);
		
		WorkFlowStepDataModel stepValues = wOExecutionDAO.getWFStepData(stepID, subActivityDefID,executionType);
		if (stepValues != null //&& !stepValues.isEmpty()
				) {
			//toolID = String.valueOf(stepValues.get(AppConstants.TOOL_ID));
			toolID = String.valueOf(stepValues.getToolId());
			//toolName = String.valueOf(stepValues.get(AppConstants.TOOL_NAME));
			toolName = String.valueOf(stepValues.getToolName());
			//rpaID = String.valueOf(stepValues.get(AppConstants.RPA_ID));
			//if(StringUtils.isNotBlank(stepValues.getRpaID())) {
			rpaID = String.valueOf(stepValues.getRpaID());
			//}
			//rpaName = String.valueOf(stepValues.get("RpaName"));
			//if(StringUtils.isNotBlank(stepValues.getRpaName())) {
			rpaName=String.valueOf(stepValues.getRpaName());
			//}
			//isRunOnServer = String.valueOf(stepValues.get(AppConstants.IS_RUN_ON_SERVER));
			//if(StringUtils.isNotBlank(stepValues.getIsRunOnServer())) {
			isRunOnServer=String.valueOf(stepValues.getIsRunOnServer());
			//}
//			if (stepValues.get(AppConstants.IS_INPUT_REQUIRED) != null)
//				isInputRequired = (boolean) stepValues.get(AppConstants.IS_INPUT_REQUIRED);
		isInputRequired=stepValues.isInputRequired();
		}	
		if (attrs.has(AppConstants.TASK)) {
			JSONObject task = (JSONObject) attrs.get(AppConstants.TASK);
			/* Unnecessary steps
			 * task.remove(AppConstants.TOOLID); task.remove(AppConstants.TOOL);
			 * task.remove(AppConstants.TOOLNAME);
			 */
			task.put(AppConstants.TOOLID, toolID);
			task.put(AppConstants.TOOLNAME, toolName);
			task.put(AppConstants.TOOLNAME, toolName);
//			task.put("sadCount", sadCount);
		}
		if (attrs.has(AppConstants.TOOL)) {
			JSONObject tool = (JSONObject) attrs.get(AppConstants.TOOL);
//			tool.remove(AppConstants.RPAID); Unnecessary step
			tool.put(AppConstants.RPAID, rpaID);
//			tool.remove(AppConstants.IS_RUN_ON_SERVER);
			tool.put(AppConstants.IS_RUN_ON_SERVER, isRunOnServer);
//			types.remove(AppConstants.RPAID_SMALL); Unnecessary step
			//types.put(AppConstants.RPAID_SMALL, rpaID + "@" + rpaName);
			types.put(AppConstants.RPAID_SMALL,(new StringBuilder()).append(rpaID).append("@").append(rpaName).toString());
//			tool.remove(AppConstants.IS_INPUT_REQUIRED); Unnecessary step
			tool.put(AppConstants.IS_INPUT_REQUIRED, isInputRequired);
		}
		JSONObject text = (JSONObject) attrs.get(attrType);
		String textData = text.getString(AppConstants.TEXT);
//		text.remove(textData); Unnecessary step
		try {
			int index = 0;
			if (textData.contains("AvgEstdEffort")) {
				textData = textData.substring(0, textData.indexOf("\n\nAvgEstdEffort"));
			}
			if (textData.contains(AppConstants.TOOL_NAME_COLON)) {
				index = textData.indexOf(AppConstants.TOOL_NAME_COLON);
				textData = textData.substring(0, index);
				//textData = textData + "Tool Name:" + toolName;
				textData = (new StringBuilder()).append(textData).append(AppConstants.TOOL_NAME_COLON).append(toolName).toString();
			} else if (textData.contains(AppConstants.TOOL_NAME_COLON)) {
				index = textData.indexOf(AppConstants.TOOL_NAME_COLON);
				textData = textData.substring(0, index);
				//textData = textData + "Tool Name:" + toolName;
				textData = (new StringBuilder()).append(textData).append(AppConstants.TOOL_NAME_COLON).append(toolName).toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
//		text.remove(AppConstants.TEXT);  Unnecessary step
		text.put(AppConstants.TEXT, textData);
		
	}

	
	public Map<String, String> updateBookingDetailsStatus(int wOID, String signumID, int taskID, int bookingID,
			String status, String reason, String stepid,int flowChartDefID, String refferer, Optional<Integer> proficiencyId) {
		Map<String, String> response = new HashMap<>();
		Integer sourceID=0;
		status = StringUtils.upperCase(status);
		if(StringUtils.isNotBlank(refferer)) {
			if(refferer.equalsIgnoreCase(AppConstants.UI)) {
				refferer=AppConstants.WEB;
		}
		sourceID = rpaDAO.getExternalSource(refferer);
		}

		WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(wOID);
		if (!woDetails.getSignumID().equalsIgnoreCase(signumID)) {
			response.put(AppConstants.ERROR, AppConstants.TASK_SIGNUM_ERROR);
			return response;
		}
		if (status.equalsIgnoreCase(AppConstants.SKIPPED)) {

			if (AppConstants.CLOSED.equalsIgnoreCase(woDetails.getStatus())) {
				response.put(AppConstants.ERROR, "Work order is closed!");
				return response;
			}
		}

		WorkFlowBookingDetailsModel maxBookingID = new WorkFlowBookingDetailsModel();

		Integer stepdetailId = wOExecutionDAO.getFlowChartStepByStepId(wOID, stepid);
		
		Integer defID = wOExecutionDAO.getSubActivityDefIDForWOID(wOID);

		maxBookingID = wOExecutionDAO.stopWOMaxBookingID(wOID, taskID);
		if (maxBookingID == null || stepdetailId == null) {
			maxBookingID = new WorkFlowBookingDetailsModel();
			maxBookingID.setBookingID(0);
		}
		Boolean checkType = wOExecutionDAO.checkForType(wOID, taskID, maxBookingID.getBookingID());
		String flowChartType = wOExecutionDAO.getFlowChartType(wOID);
		String executionType = StringUtils.EMPTY;
		if (!checkType) {
			String res = reason.replaceAll(AppConstants.CHAR_AT,AppConstants.FORWARD_SLASH);
			if (status.equalsIgnoreCase(AppConstants.SKIPPED) && (maxBookingID.getBookingID() == 0)) {
				// in case of skip if this is first step we need to set actual start date of wo
				// as well
				// for Qualified Workflow, if first step is skipped, flowChartDefID and version need to be updated. #1852629
				if(woDetails.getStatus().equalsIgnoreCase(AppConstants.WO_STATUS_ASSIGNED)
						|| woDetails.getStatus().equalsIgnoreCase(AppConstants.ONHOLD)
						|| woDetails.getStatus().equalsIgnoreCase(AppConstants.WO_STATUS_REPOENED)) {
					// if it is first step, mark work order to in-progress
					wOExecutionDAO.updateStatusandProficiencyId(wOID,signumID,
																(woDetails.getProficiencyID()==null)?proficiencyId
																										.orElse(appService.getProficiencyID(AppConstants.ASSESSED).getProficiencyID()):-1,
																		(woDetails.getActualStartDate()==null)?true:false);
				}
				
				maxBookingID = wOExecutionDAO.stopWOMaxBookingIDNew(wOID, taskID, stepid);
				
				String exType = wOExecutionDAO.getExecutionTypeNew(defID, stepid);
				
				wOExecutionDAO.updateStatusAndHours(wOID, taskID, 0, status, res, signumID, stepid,sourceID,defID,exType);
				
				List<LinkedHashMap<String, Object>> listOfWorkOrderModel = workOrderPlanService.getAllBookingDetails(wOID, signumID,true,stepid);
				if(listOfWorkOrderModel.get(0).get(AppConstants.EXECUTION_TYPE)!=null) {
					executionType=listOfWorkOrderModel.get(0).get(AppConstants.EXECUTION_TYPE).toString();
				}
				
				 StepDetailsModel stepDetailsModel = new StepDetailsModel();
					setStepDetailsModel(stepDetailsModel, wOID, signumID, taskID, 0, status, reason,
							stepid, woDetails.getFlowchartdefid(),executionType );
					if(flowChartDefID != 0 ) {
						stepDetailsModel.setFlowChartDefID(flowChartDefID);
					}
					addStepDetailsForFlowChart(stepDetailsModel);

			} else if (status.equalsIgnoreCase(AppConstants.SKIPPED) && AppConstants.COMPLETED.equals(maxBookingID.getStatus())) {
				if(flowChartDefID != 0 ) {
					checkAndUpdateFlowChartDefID(wOID, flowChartDefID);
					wOExecutionDAO.updateStatusWO(wOID);
				}
				markWorkOrderAsStarted(wOID, signumID);
				maxBookingID = wOExecutionDAO.stopWOMaxBookingIDNew(wOID, taskID, stepid);
				wOExecutionDAO.updateStatusAndHours(wOID, taskID, maxBookingID.getBookingID(), status, res, signumID,
						stepid,sourceID,defID,maxBookingID.getExecutionType());
			} else {
				maxBookingID = wOExecutionDAO.stopWOMaxBookingIDNew(wOID, taskID, stepid);
				wOExecutionDAO.updateStatusAndHours(wOID, taskID, maxBookingID.getBookingID(), status, res, signumID,
						stepid,sourceID,defID,maxBookingID.getExecutionType());
			}
			String hour = wOExecutionDAO.getBookingHoursForStep(stepid, wOID);
		
			if(maxBookingID!=null) {
				 executionType = maxBookingID.getExecutionType();
				 WorkFlowBookingDetailsModel maxBookingIDNew = wOExecutionDAO.stopWOMaxBookingIDNew(wOID, taskID, stepid);
				 StepDetailsModel stepDetailsModel = new StepDetailsModel();
				 maxBookingID = wOExecutionDAO.stopWOMaxBookingIDNew(wOID, taskID, stepid);
					setStepDetailsModel(stepDetailsModel, wOID, signumID, taskID, maxBookingIDNew.getBookingID(), status, reason,
							stepid, woDetails.getFlowchartdefid(), executionType);
					if(flowChartDefID != 0 ) {
						stepDetailsModel.setFlowChartDefID(flowChartDefID);
					}
					addStepDetailsForFlowChart(stepDetailsModel);
					if (StringUtils.equalsIgnoreCase(AppConstants.DESKTOP, refferer)||activityMasterService.isSignumExist(signumID)) {
						WoAutoTaskInfoModel woautotask = new WoAutoTaskInfoModel();
						
						setWoAuttoTaskModel(woautotask, wOID, signumID, taskID, maxBookingID.getBookingID(), status, reason,
								stepid, woDetails.getFlowchartdefid(), hour, executionType,flowChartType);
						SignalrModel signalRModel = appService.returnSignalrConfiguration(woautotask,configurations.getStringProperty(ConfigurationFilesConstants.HUB_METHOD_NAME));
						appService.callSignalrApplicationToCallSignalRHub(signalRModel);

					}
			}
			
			response.put(AppConstants.SUCCESS, "Updation Successfull");
			response.put(AppConstants.HOUR, hour);
		} else {
			response.put(AppConstants.ERROR, "Cannot update booking details as the Type is LEADTIME !!!");
			return response;
		}
		response.put(AppConstants.FLOWCHART_TYPE, flowChartType);
		return response;
	}

	private void setStepDetailsModel(StepDetailsModel stepDetailsModel, int wOID, String signumID, int taskID,
			int bookingID, String status, String reason, String stepid, int flowchartdefid, String executionType) {
		stepDetailsModel.setBookingId(bookingID);
		stepDetailsModel.setDecisionValue("");
		stepDetailsModel.setExecutionType(executionType);
		stepDetailsModel.setFlowChartDefID(flowchartdefid);
		stepDetailsModel.setFlowChartStepId(stepid);
		stepDetailsModel.setTaskID(taskID);
		stepDetailsModel.setSignumId(signumID);
		stepDetailsModel.setStatus(status);
		stepDetailsModel.setWoId(wOID);

	}

	private void setWoAuttoTaskModel(WoAutoTaskInfoModel woautotask, int wOID, String signumID, int taskID,
			int bookingID, String status, String reason, String stepid, int flowchartdefid, String hour,
			String executionType,String flowChartType) {
		woautotask.setBookingId(bookingID);
		woautotask.setSignum(signumID);
		woautotask.setFlowChartStepId(stepid);
		if(StringUtils.isNotEmpty(reason) && reason.equalsIgnoreCase(AppConstants.SUCCESS)) {
			woautotask.setIconsOnNextStep(true);
		}
		woautotask.setReason(reason);
		woautotask.setStatus(status);
		woautotask.setWoId(wOID);
		woautotask.setFlowChartDefID(flowchartdefid);
		woautotask.setHour(hour);
		woautotask.setExecutionType(executionType);
		woautotask.setFlowChartType(flowChartType);

	}

	// start wo if not already started or wo status in ONHOLD
	@Transactional("transactionManager")
	public void markWorkOrderAsStarted(int woID, String signum) {
		wOExecutionDAO.markWorkOrderAsStarted(woID, signum);
	}

	@Transactional("transactionManager")
	private void pauseStep(int wOID, int taskID, String signumID, String status, String reasson) {
		List<BookingDetailsModel> startedBookingList = wOExecutionDAO.getStartedBookingsBySignum(signumID);
		int parallelCount = startedBookingList.size() - 1;
		String startedBookingIds = StringUtils.EMPTY;
		Integer maxBookingId = 0;
		if (CollectionUtils.isNotEmpty(startedBookingList)) {
			for (BookingDetailsModel booking : startedBookingList) {
				if (taskID == booking.getTaskId()) {
					booking.setType(BOOKING_TYPE_LEADTIME);
				}
				startedBookingIds += AppConstants.SINGLE_QUOTE + booking.getBookingID() + "',";
				booking.setParallelcount(parallelCount);
				if (maxBookingId < booking.getBookingID()) {
					maxBookingId = booking.getBookingID();
				}
			}
			startedBookingIds = StringUtils.substring(startedBookingIds, 0, StringUtils.defaultString(startedBookingIds).length() - 1);
			wOExecutionDAO.updateBookingsStatus(startedBookingIds, AppConstants.COMPLETED, AppConstants.BOOKING,
					StringUtils.EMPTY);
		}
		if (CollectionUtils.isNotEmpty(startedBookingList)) {
			wOExecutionDAO.insertBooking(startedBookingList);
		}
		Map<Integer, BookingDetailsModel> bookingMap = new HashMap<Integer, BookingDetailsModel>();
		for (BookingDetailsModel c : startedBookingList) {
			if (AppConstants.STARTED.equals(c.getStatus())) {
				bookingMap.put(c.getWoId(), c);
			}
		}
		List<FcStepDetails> existingStepDetails = new ArrayList<>();
		if (!StringUtils.EMPTY.equals(startedBookingIds)) {
			existingStepDetails = wOExecutionDAO.selectFcStepDetailsByBookingIds(startedBookingIds);
			wOExecutionDAO.updateFcStepStatusByBookingIds(startedBookingIds, AppConstants.COMPLETED);
			for (FcStepDetails e : existingStepDetails) {
				e.setBookingID(bookingMap.get(e.getWoId()).getBookingID());
				if (taskID == e.getTaskID()) {
					e.setStatus(AppConstants.ONHOLD);
				}
			}
		}
		if (CollectionUtils.isNotEmpty(existingStepDetails)) {
			wOExecutionDAO.insertFcStepDetails(existingStepDetails);
		}
	}

	/**
	 * 
	 * @param signumID
	 * @param isApproved
	 * @return List<CurrentWorkOrderModel>
	 */
	public List<CurrentWorkOrderModel> getCurrentWorkOrderDetails(String signumID, String isApproved) {

		HashMap<String, Object> isInstalledDesktopVersionUpdated = wOExecutionDAO.isInstalledDesktopVersionUpdated(signumID);
		
		if ((Integer) isInstalledDesktopVersionUpdated.get("result") == 0) {
			List<CurrentWorkOrderModel> workOrder = new ArrayList<CurrentWorkOrderModel>();
			return workOrder;
		}
		if(!wOExecutionDAO.isInstalledLibraryVersionUpdated(signumID)) {
			List<CurrentWorkOrderModel> workOrder = new ArrayList<CurrentWorkOrderModel>();
			return workOrder;
		}
		
		return getCurrentWorkOrderList(signumID, isApproved);
	}

	public List<CurrentWorkOrderModel> getCurrentWorkOrderList(String signumID, String isApproved) {

		List<CurrentWorkOrderModel> currentWoData = new ArrayList<>();
		String woID = StringUtils.EMPTY;
		String woIDValue = StringUtils.EMPTY;

		List<CurrentWorkOrderModel> currentWoModel = wOExecutionDAO.getCurrentWorkOrderDetails(signumID, isApproved);
		for (CurrentWorkOrderModel woData : currentWoModel) {
			woID += woData.getwOID() + AppConstants.CSV_CHAR_COMMA;
			woIDValue = woID.replaceAll(",$", StringUtils.EMPTY);
		}

		if (!StringUtils.EMPTY.equals(woIDValue)) {
			List<Map<String, Object>> woNodes = wOExecutionDAO.getWONodeDetailsByWOID(woIDValue);
			for (CurrentWorkOrderModel woModel : currentWoModel) {
				List<String> nodeNames = new ArrayList<String>();
				if (woNodes.size() > 0) {
					Map<String, Object> woNodesData = new HashMap<>();
					for (Map<String, Object> woNodeModel : woNodes) {
						if (woModel.getwOID() == Integer.parseInt(woNodeModel.get("WOID").toString())) {
							woNodesData.put("nodeType", woNodeModel.get("NodeType").toString());
							if (!StringUtils.EMPTY.equals(woNodeModel.get("NodeNames").toString())) {
								nodeNames.add(woNodeModel.get("NodeNames").toString());
							}
						}
						woNodesData.put("nodeNames", nodeNames);
					}
					woModel.setNodeValues(woNodesData);
				}
				currentWoData.add(woModel);
			}
		}
		return currentWoData;
	}

	public ResponseEntity<Response<Map<String, Object>>> checkParallelWorkOrderDetails(ParallelWorkOrderDetailsModel model) {
		Response<Map<String, Object>> response=new Response<Map<String,Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Integer> currentAutomaticTaskRunning = new ArrayList<>();
		List<Integer> currentManualTaskRunning = new ArrayList<>();
		
		
try {	
			
		List<LinkedHashMap<String, Object>> workOrder = workOrderPlanDao.getInprogressTask(model.getSignumID());
		Map<String, Object> data = activityMasterService.getMaxTasksValueByProject(model.getProjectID(), model.getSignumID());
		int paralleTAsk = (int) data.get(MAXAUTOMATICTASK);
		int paralleManualTAsk = (int) data.get(MAXMANUALTASK);
		if (CollectionUtils.isEmpty(workOrder)) {
			result.put(AppConstants.MSG, "No Task running");
            result.put(AppConstants.EXECUTE_FLAG, true);
            response.setResponseData(result);
            return new ResponseEntity<Response<Map<String, Object>>>(response, HttpStatus.OK);
		}

		Map<Integer, String> tmpMap = new HashMap<Integer, String>();
		for (Map<String, Object> taskDetails : workOrder) {

			if (((String)taskDetails.get(EXECUTION_TYPE)).equalsIgnoreCase(AppConstants.AUTOMATIC)
					&& model.getExecutionType().equalsIgnoreCase(AppConstants.AUTOMATIC)) {
				
				  List<RpaModel> rpaIdList = wOExecutionDAO.getParallelBotDetails((int) taskDetails.get(TASK_ID),
						  (int) taskDetails.get(FLOWCHARTDEFID), (String) taskDetails.get(FLOWCHARTSTEPID));
				 
				if (CollectionUtils.isNotEmpty(rpaIdList)) {
					for (RpaModel rpaId : rpaIdList) {
						if (AppConstants.NO.equalsIgnoreCase(rpaId.getParallelWOExecution())) {
							throw new ApplicationException(200, "Currently running RPA ID is not compatible for parallel running");
						}
					}
				}
			}
			if (!taskDetails.get(AppConstants.PROJECT_ID_2).equals(model.getProjectID())) {
				// below constraints are project wise not user wise
				continue;
			}
			if (!tmpMap.containsKey(taskDetails.get(WO_ID))) {
				tmpMap.put((int) taskDetails.get(WO_ID),(String) taskDetails.get(EXECUTION_TYPE));
				
			}
		}

		for (Integer tmpWoid : tmpMap.keySet()) {

			if (tmpMap.get(tmpWoid).equalsIgnoreCase(AppConstants.AUTOMATIC)) {
				currentAutomaticTaskRunning.add(tmpWoid);
			} else if (tmpMap.get(tmpWoid).equalsIgnoreCase(AppConstants.MANUAL) || tmpMap.get(tmpWoid).equalsIgnoreCase(AppConstants.MANUAL_DISABLED)) {
				currentManualTaskRunning.add(tmpWoid);
			}
		}
         LOG.info("currentManualTaskRunning" + currentManualTaskRunning.size());
         
		if (currentAutomaticTaskRunning.size() >= paralleTAsk && model.getExecutionType().equalsIgnoreCase(AppConstants.AUTOMATIC)) {
			throw new ApplicationException(200, new StringBuilder().append(paralleTAsk).append(String.format(TASKS_ARE_ALREADY_RUNNING, AppConstants.AUTOMATIC)).toString());
		}

		if (currentManualTaskRunning.size() >= paralleManualTAsk 
				&& (model.getExecutionType().equalsIgnoreCase(AppConstants.MANUAL) || model.getExecutionType().equalsIgnoreCase(AppConstants.MANUAL_DISABLED))) {
			throw new ApplicationException(200, new StringBuilder().append(paralleManualTAsk).append(String.format(TASKS_ARE_ALREADY_RUNNING, AppConstants.MANUAL)).toString());
		}
				
		if (tmpMap.containsKey(model.getWoid())) {
			if (tmpMap.get(model.getWoid()).equalsIgnoreCase(AppConstants.AUTOMATIC)) {
				throw new ApplicationException(200, String.format(COMPLETE_THE_PREVIOUS_STEP, AppConstants.AUTOMATIC));
			} else if (tmpMap.get(model.getWoid()).equalsIgnoreCase(AppConstants.MANUAL)) {
				throw new ApplicationException(200, String.format(COMPLETE_THE_PREVIOUS_STEP, AppConstants.MANUAL));
			}
			else if(tmpMap.get(model.getWoid()).equalsIgnoreCase(AppConstants.MANUAL_DISABLED)) {
				throw new ApplicationException(200, String.format(COMPLETE_THE_PREVIOUS_STEP, AppConstants.DISABLED_MANUAL));
			}
		} else {
			if (AppConstants.MANUAL.equalsIgnoreCase(model.getExecutionType()) || AppConstants.MANUAL_DISABLED.equalsIgnoreCase(model.getExecutionType())) {
				if (currentManualTaskRunning.size() == 0) {
					result.put(AppConstants.MSG, String.format(CAN_RUN_THE_TASK, AppConstants.MANUAL));
					result.put(AppConstants.EXECUTE_FLAG, true);
				} else {
					result.put(AppConstants.MSG, "Can Run the Manual Task from different WOID");
					result.put(AppConstants.EXECUTE_FLAG, true);
				}
			} else if (AppConstants.AUTOMATIC.equalsIgnoreCase(model.getExecutionType())) {
				List<RpaModel> rpaIdList = wOExecutionDAO.getParallelBotDetails(model.getTaskid(), model.getSubActivityFlowChartDefID(),
						model.getStepID());
				if (CollectionUtils.isNotEmpty(rpaIdList )) {
					for (RpaModel rpaId : rpaIdList) {
						if (currentAutomaticTaskRunning.size() == 0) {
							result.put(AppConstants.MSG, String.format(CAN_RUN_THE_TASK, AppConstants.AUTOMATIC));
							result.put(AppConstants.EXECUTE_FLAG, true);
						} else if (AppConstants.YES.equalsIgnoreCase(rpaId.getParallelWOExecution()) && currentAutomaticTaskRunning.size() > 0) {
							result.put(AppConstants.MSG, String.format(CAN_RUN_THE_TASK, AppConstants.AUTOMATIC));
							result.put(AppConstants.EXECUTE_FLAG, true);
						} else if (AppConstants.NO.equalsIgnoreCase(rpaId.getParallelWOExecution())) {
							throw new ApplicationException(200, String.format(RPA_ID_IS_NOT, "compatible for parallel running"));
						} else {
							throw new ApplicationException(200, String.format(RPA_ID_IS_NOT, CONFIGURED_FOR_THE_GIVEN_DETAILS));
						}
					}
				} else {
					throw new ApplicationException(200, String.format(RPA_ID_IS_NOT, CONFIGURED_FOR_THE_GIVEN_DETAILS));
				}
			}
		}
			response.setResponseData(result);
		}
		catch (ApplicationException e) {
			 LOG.error(e.getMessage());
			 response.addFormError(e.getMessage());
			 return new ResponseEntity<Response<Map<String, Object>>>(response, HttpStatus.OK);
		}catch (Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<Map<String, Object>>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response<Map<String, Object>>>(response, HttpStatus.OK);
	}

	@Transactional("transactionManager")
	public String updateWorkOrderNodes(WorkOrderNetworkElementModel workOrderNetworkElementModel) {

		if (StringUtils.isNotEmpty(workOrderNetworkElementModel.getTablename())) {

			wOExecutionDAO.deleteExistingNodes(workOrderNetworkElementModel.getwOID());
			wOExecutionDAO.insertWorkOrderNetworkElement(workOrderNetworkElementModel.getTablename(),
					workOrderNetworkElementModel.getwOID(), workOrderNetworkElementModel.getSignum());
			wOExecutionDAO.deleteExistingWorkOrderNECount(workOrderNetworkElementModel.getwOID());
			wOExecutionDAO.insertWorkOrderNECount(workOrderNetworkElementModel.getwOID(),
					workOrderNetworkElementModel.getCount(), workOrderNetworkElementModel.getNeTextName(),
					workOrderNetworkElementModel.getSignum());
		}

		else if (StringUtils.isEmpty(workOrderNetworkElementModel.getTablename())) {
			wOExecutionDAO.updatetWorkOrderNECount(workOrderNetworkElementModel.getwOID(),
					workOrderNetworkElementModel.getCount(), workOrderNetworkElementModel.getNeTextName(),
					workOrderNetworkElementModel.getSignum());

		}

		List<String> markets = wOExecutionDAO.getMarketByWoid(workOrderNetworkElementModel.getwOID());

		return !markets.isEmpty() ? markets.toString() : null;

	}

	public List<WorkOrderViewDetailsByWOIDModel> getWorkOrderViewDetailsByWOID(int WOID) {
		return wOExecutionDAO.getWorkOrderViewDetailsByWOID(WOID);
	}

	public ResponseEntity<Response<PreviousStepDetailsModel>> checkIFPreviousStepCompleted(int WOID, int taskID,
			int subActivityDefID, String flowChartStepID) {
		Response<PreviousStepDetailsModel> response = new Response<>();
		PreviousStepDetailsModel stepModel = new PreviousStepDetailsModel();
		try {
			if(wOExecutionDAO.checkIFManualDisabledStep(subActivityDefID, flowChartStepID)) {
				
				StepDetailsModel stepDetails =  wOExecutionDAO.getDisabledStepDetails(subActivityDefID,WOID);
				if(StringUtils.equalsIgnoreCase(stepDetails.getStatus(), AppConstants.COMPLETED) ||
						StringUtils.equalsIgnoreCase(stepDetails.getStatus(), AppConstants.ONHOLD)	) {
					
					stepModel.setAllowed(true);
				}
				else {
					stepModel.setAllowed(wOExecutionDAO.checkIFPreviousStepCompletedDisabled(WOID, subActivityDefID, flowChartStepID));
				}
			} else {
				stepModel.setAllowed(
						wOExecutionDAO.checkIFPreviousStepCompletedQualifiedWo(WOID, subActivityDefID, flowChartStepID));
			}
			
			response.setResponseData(stepModel);
			LOG.info(AppConstants.SUCCESS);
		} catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<PreviousStepDetailsModel>>(response, HttpStatus.OK);
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			response.addFormError(ex.getMessage());
			return new ResponseEntity<Response<PreviousStepDetailsModel>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response<PreviousStepDetailsModel>>(response, HttpStatus.OK);
	}

	public PreviousStepDetailsModel checkIFPreviousStepCompletedV2(int WOID, String flowChartStepID,
			int subActivityDefID) {
		PreviousStepDetailsModel model = new PreviousStepDetailsModel();
		Boolean result = wOExecutionDAO.checkIFPreviousStepCompletedV2(WOID, flowChartStepID, subActivityDefID);
		model.setAllowed(result);
		return model;
	}

	public LastStepDetailsModel checkIFLastStep(int WOID, int subactivityDefID) {
		return wOExecutionDAO.checkIFLastStep(WOID, subactivityDefID);
	}

	public ResponseEntity<Response<List<WorkOrderFailureReasonModel>>> getWOFailureReasons(String category) {
		Response<List<WorkOrderFailureReasonModel>> response=new Response<>();
		try {
		response.setResponseData(wOExecutionDAO.getWOFailureReasons(category));
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<List<WorkOrderFailureReasonModel>>>(response,HttpStatus.OK);
		}
		catch(Exception ex) {
			response.addFormError(ex.getMessage());
			return new ResponseEntity<Response<List<WorkOrderFailureReasonModel>>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response<List<WorkOrderFailureReasonModel>>>(response,HttpStatus.OK);
	}

	@Transactional("transactionManager")
	public List<String> startWO(int wOID, int taskID, String signumID) {
		List<String> finalValue = new ArrayList<>();
		String bookingID = wOExecutionDAO.getMaxBookingID(wOID, taskID, signumID);
		String emailID = wOExecutionDAO.getEmployeeEmail(signumID);
		finalValue.add(bookingID);
		finalValue.add(emailID);
		return finalValue;
	}

	@Transactional("transactionManager")
	public Map<String, String> closeWO(int wOID, int taskID, int bookingID, String status, String signumID,
			String url) {

		Map<String, String> response = new HashMap<>();
		bookingID = wOExecutionDAO.getMaxBookingIdByWoid(wOID, signumID);
		if (status.equalsIgnoreCase("success") || status.equals(ZERO_STRING)) {
			wOExecutionDAO.closeWO_bookingID(wOID, taskID, bookingID, status, signumID, url);
			updateRPABotStatus(wOID, taskID, bookingID, signumID, AppConstants.SUCCESS);
			response.put(AppConstants.SUCCESS, AppConstants.SUCCESS);
		} else {
			ServerBotModel serverbotModel=new ServerBotModel();
			serverbotModel.setSignumID(signumID);
			wOExecutionDAO.closeWO_bookingID(wOID, taskID, bookingID, status, signumID, url);
			wOExecutionDAO.updateRpaFailureStatus(wOID, taskID, bookingID, status,  serverbotModel);
			updateRPABotStatus(wOID, taskID, bookingID, signumID, AppConstants.FAILED);
			response.put(AppConstants.FAILED, "Fail");
		}
		updateFlowChartStepDetails(wOID, taskID, bookingID, AppConstants.COMPLETED, signumID);
		return response;
	}

	@Transactional("transactionManager")
	public ResponseEntity<Response<Void>> addStepDetailsForFlowChart(StepDetailsModel stepDetailsModel) {
		Response<Void> response = new Response<>();
		try {
			List<WorkFlowStepDetailsModel> stepStatus = wOExecutionDAO.getBookingStatusOfPreviousStep(stepDetailsModel);
			for (WorkFlowStepDetailsModel stepModel : stepStatus) {
				for (WorkFlowBookingDetailsModel model : stepModel.getLstBooking()) {
					if (!stepModel.getStatus().equalsIgnoreCase(model.getStatus())
							&& stepModel.getTaskID() == model.getTaskID()
							&& stepModel.getBookingID() == model.getBookingID()) {
						wOExecutionDAO.updateStepBookingStatus(stepDetailsModel.getWoId(), stepModel.getStepID(),
								stepModel.getTaskID(), stepDetailsModel.getFlowChartDefID(), stepModel.getBookingID(),
								stepDetailsModel.getSignumId(), model.getStatus());
					}
				}
			}
			
			if (stepDetailsModel.getTaskID() != 0) {
				WorkFlowBookingDetailsModel maxBookingID = this.wOExecutionDAO
						.stopWOMaxBookingID(stepDetailsModel.getWoId(), stepDetailsModel.getTaskID());
				if (maxBookingID != null) {
					stepDetailsModel.setStatus(maxBookingID.getStatus());
					stepDetailsModel.setBookingId(maxBookingID.getBookingID());
					this.wOExecutionDAO.addStepDetailsForFlowChart(stepDetailsModel);
				} else {
					stepDetailsModel.setBookingId(0);
					this.wOExecutionDAO.addStepDetailsForFlowChart(stepDetailsModel);
				}

			} else if(stepDetailsModel.getTaskID() == 0 && !StringUtils.equalsIgnoreCase(stepDetailsModel.getExecutionType(), AppConstants.MANUAL_DISABLED)){
				stepDetailsModel.setTaskID(0);
				stepDetailsModel.setBookingId(0);
				stepDetailsModel.setStatus(AppConstants.COMPLETED);
				this.wOExecutionDAO.addStepDetailsForFlowChart(stepDetailsModel);
			}
			
			if(!StringUtils.isBlank(stepDetailsModel.getDecisionValue())
					&& StringUtils.equalsIgnoreCase(stepDetailsModel.getRefferer(), AppConstants.DESKTOP)) {
				LOG.info(String.format("info addStepDetailsForFlowChart signalR payload: %s", stepDetailsModel.toString()));
				SignalrModel signalrModel=signalrUtil.returnDefaultSignalrConfiguration
													.get()
													.methodName(AppConstants.DECSION_VALUE)
													.payload(stepDetailsModel);
				signalrUtil.callSignalrApplicationToCallSignalRHub(signalrModel);
			}
			LOG.info("addStepDetailsForFlowChart :SUCCESS");
		} catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<Void>>(response, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<Void>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.addFormMessage("Step Added Successfully !!");
		return new ResponseEntity<Response<Void>>(response, HttpStatus.OK);
		
	}
	// v1/addStepDetailsForFlowChart
	@Transactional("transactionManager")
	public void addStepDetailsForFlowChartV1(StepDetailsModel stepDetailsModel) {
		try {
			List<WorkFlowStepDetailsModel> stepStatus = wOExecutionDAO.getBookingStatusOfPreviousStepV1(
					stepDetailsModel.getWoId(), 0, 0, stepDetailsModel.getFlowChartDefID(), 0,
					stepDetailsModel.getSignumId());
			for (WorkFlowStepDetailsModel stepModel : stepStatus) {
				for (WorkFlowBookingDetailsModel model : stepModel.getLstBooking()) {
					if (!stepModel.getStatus().equalsIgnoreCase(model.getStatus())
							&& stepModel.getTaskID() == model.getTaskID()
							&& stepModel.getBookingID() == model.getBookingID()) {
						wOExecutionDAO.updateStepBookingStatus(stepDetailsModel.getWoId(), stepModel.getStepID(),
								stepModel.getTaskID(), stepDetailsModel.getFlowChartDefID(), stepModel.getBookingID(),
								stepDetailsModel.getSignumId(), model.getStatus());
					}
				}
			}
		} catch (Exception ex) {
			LOG.error("Error while updating previous step details:" + ex.getMessage());
			ex.printStackTrace();
		}
		if (stepDetailsModel.getTaskID() != 0) {
			WorkFlowBookingDetailsModel maxBookingID = this.wOExecutionDAO
					.stopWOMaxBookingID(stepDetailsModel.getWoId(), stepDetailsModel.getTaskID());
			if(maxBookingID!=null) {
			this.wOExecutionDAO.addStepDetailsForFlowChart(stepDetailsModel.getWoId(), stepDetailsModel.getTaskID(),
					maxBookingID.getBookingID(), stepDetailsModel.getFlowChartStepId(),
					stepDetailsModel.getFlowChartDefID(), maxBookingID.getStatus(), stepDetailsModel.getSignumId(),
					stepDetailsModel.getDecisionValue(), stepDetailsModel.getExecutionType());
			}else {
				this.wOExecutionDAO.addStepDetailsForFlowChart(stepDetailsModel.getWoId(), stepDetailsModel.getTaskID(),
						0, stepDetailsModel.getFlowChartStepId(),
						stepDetailsModel.getFlowChartDefID(), stepDetailsModel.getStatus(), stepDetailsModel.getSignumId(),
						stepDetailsModel.getDecisionValue(), stepDetailsModel.getExecutionType());
			}
			
		} else {
			this.wOExecutionDAO.addStepDetailsForFlowChart(stepDetailsModel.getWoId(), 0, 0,
					stepDetailsModel.getFlowChartStepId(), stepDetailsModel.getFlowChartDefID(), "COMPLETED",
					stepDetailsModel.getSignumId(), stepDetailsModel.getDecisionValue(),
					stepDetailsModel.getExecutionType());
		}
		
	}

	public WorkFlowBookingDetailsModel getBookingID(int wOID, int taskID, String signum) {
		return wOExecutionDAO.getBookingID(wOID, taskID, signum);
	}

	@Transactional("transactionManager")
	public List<String> updateWOBooking(int wOID, int taskID, String bookingStatus, String signumID) {
		List<String> finalValue = new ArrayList<>();
		wOExecutionDAO.updateWOBooking(wOID, taskID, bookingStatus, signumID);
		String bookingID = wOExecutionDAO.getMaxBookingID(wOID, taskID, signumID);
		String emailID = wOExecutionDAO.getEmployeeEmail(signumID);
		finalValue.add(bookingID);
		finalValue.add(emailID);
		return finalValue;
	}

	private void updateRPABotStatus(int wOID, int taskID, int bookingID, String signumID, String status) {
		wOExecutionDAO.updateRPABotStatus(wOID, taskID, bookingID, signumID, status);
	}

	private void updateFlowChartStepDetails(int woID, int taskID, int bookingID, String status, String signumID) {
		Map<String, Integer> data = rpaDAO.getProjAndSubActivityID(woID);
		// int versionNo = rpaDAO.getWOVersionNo(woID);
		// int flowchartDefID = rpaDAO.getFlowChartDefID(data.get("ProjectID"),
		// data.get("SubActivityID"), versionNo);
		String stepID = wOExecutionDAO.getFlowChartStepID(woID, taskID, data.get("flowchartdefid"), bookingID,
				signumID);
		StepDetailsModel stepDetailsModel=prepareDataForStepDetailModel(woID, taskID, bookingID, stepID, data.get("flowchartdefid"), status,
				signumID,  "Automatic");
		wOExecutionDAO.addStepDetailsForFlowChart(stepDetailsModel);
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

	@SuppressWarnings("null")
	@Transactional("transactionManager")
	public Map<String, String> pauseAllWOTasks(List<UpdateBookingAndStepDetailsModel> updateModel, String reason,
			String signumID) {
		Map<String, String> response = new HashMap<>();
		try {
			for (UpdateBookingAndStepDetailsModel model : updateModel) {
				Boolean checkStatus = wOExecutionDAO.checkBookingStatus(model.getWoID(), model.getBookingID());

				if (checkStatus) {
//					if(StringUtils.equals(model.getExecutionType(), "ManualDisabled")) {
//						ServerBotModel serverBotModel = new ServerBotModel(model.getWoID(),signumID,model.getTaskID(),model.getExecutionType(),
//								"BOOKING",model.getFlowChartDefID(),"","",reason,model.getFlowChartStepID(),"ui","ui");
//						stopWorkOrderTask(serverBotModel);
//					} else {
					
						wOExecutionDAO.updateBookingStatus(model.getWoID(), model.getBookingID(), signumID,
								AppConstants.ONHOLD, reason);
						wOExecutionDAO.updateWOFlowChartStepDetails(model.getWoID(), model.getTaskID(),
								model.getBookingID(), model.getFlowChartStepID(), model.getFlowChartDefID(),
								AppConstants.ONHOLD, reason, signumID);
//					}
					response.put(AppConstants.SUCCESS, "Pause All Successfull");
				}
			}
		} catch (Exception ex) {
			response.put(AppConstants.SUCCESS, ex.getMessage());
			return response;
		}
		return response;
	}

	@Transactional("transactionManager")
	public void saveUserWFProficency(UserWorkFlowProficencyModel userWorkFlowProficency, String lgSignum) {
		wOExecutionDAO.saveUserWFProficency(userWorkFlowProficency, lgSignum);
	}

	@Transactional("transactionManager")
	public void updateUserWFProficency(UserWorkFlowProficencyModel userWorkFlowProficency, String lgSignum) {
		wOExecutionDAO.updateUserWFProficency(userWorkFlowProficency, lgSignum);
	}

	public List<Map<String, String>> getUserWFProficency(String signumID, int projectID, int subActivityID) {
		List<Map<String, String>> userWFProficencyData;
		Map<String, String> data = new HashMap<>();
		userWFProficencyData = wOExecutionDAO.getUserWFProficency(signumID, projectID, subActivityID);
		if (userWFProficencyData.isEmpty()) {
			data.put("Error", "No Data Exists for given combination");
			userWFProficencyData.add(data);
			return userWFProficencyData;
		} else {
			return userWFProficencyData;
		}

	}

	@Transactional("transactionManager")
	public void updateWorkOrderWFVersion(int woID, int wfVersionNo, String signumID) {
		wOExecutionDAO.updateWorkOrderWFVersion(woID, wfVersionNo, signumID);
	}

	@Transactional("transactionManager")
	public ResponseEntity<Response<List<WorkOrderProgressModel>>> getWorkOrders(String signumID, String status, String startDate,
			String endDate) {
		Response<List<WorkOrderProgressModel>> response=new Response<List<WorkOrderProgressModel>>();
		
		try {
			String[] s = status.split(AppConstants.CSV_CHAR_COMMA);
			String newStatus = Stream.of(s).collect(Collectors.joining(AppConstants.COMMA_DOUBLE_QUOTE, AppConstants.SINGLE_QUOTE, AppConstants.SINGLE_QUOTE));
			
			LinkedList<WorkOrderProgressModel> workOrders = wOExecutionDAO.getWorkOrders(signumID, newStatus, startDate, endDate);
			
			if(CollectionUtils.isEmpty(workOrders)) {
				throw new ApplicationException(200, "Data Not Found For Selected Signum");
			}	
			else {				
				response.setResponseData(workOrders);
			}	
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<List<WorkOrderProgressModel>>>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<List<WorkOrderProgressModel>>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		/*
		 * COMMENTING AS PROGRESS IS NOT BEING DISPLAYED ON UI NOW List<Map<String,
		 * Object>> prgData = wOExecutionDAO.getWoProgressData(woIDValue); for
		 * (WorkOrderProgressModel woModel : workOrder) { if
		 * (!woModel.getStatus().equalsIgnoreCase("Assigned")) { for (Map<String,
		 * Object> prgModel : prgData) { if (woModel.getWoID() ==
		 * Integer.parseInt(prgModel.get("WOID").toString())) {
		 * woModel.setCompletedPercentage(Double.parseDouble(prgModel.get(
		 * "CompletedPercentage").toString()));
		 * woModel.setInProgressPrecentage(Double.parseDouble(prgModel.get(
		 * "InProgressPrecentage").toString())); } } } }
		 */

		// used to remove duplicate elements from workOrder list:
		/*
		 * List<WorkOrderProgressModel> workOrderUnique = workOrder.stream()
		 * .collect(collectingAndThen(toCollection(() -> new
		 * TreeSet<>(comparingInt(WorkOrderProgressModel::getWoID))), ArrayList::new));
		 */
		return new ResponseEntity<Response<List<WorkOrderProgressModel>>>(response, HttpStatus.OK);
	}
	
	@Transactional("transactionManager")
	public LinkedList<WorkOrderProgressModel> getWorkOrdersDetailsBySignum (String signumID, String status, String startDate,
			String endDate) {
		String[] s = status.split(",");
		String newStatus = Stream.of(s).collect(Collectors.joining("','", "'", "'"));
		LinkedList<WorkOrderProgressModel> workOrders = wOExecutionDAO.getWorkOrders(signumID, newStatus, startDate, endDate);
	
		
		return workOrders;

		// return workOrder;
	}
	
	
	public Response<Boolean> checkIFLastStepNew(int wOID, int subactivityDefID) {
		Response<Boolean> apiResponse = new Response<>();
		try {
			String proficiency = wOExecutionDAO.getProficiencyNameWoID(wOID);
			if(StringUtils.equalsIgnoreCase(proficiency, AppConstants.EXPERIENCED)) {
				String lastStepID,currStepID;
				Boolean branchContainsDisabled=false;
				lastStepID=wOExecutionDAO.getLastStepID(subactivityDefID);
				LOG.info("lastStepID:"+lastStepID);
				
				Map<String,FlowChartReverseTraversalModel> flowchartSteps=wOExecutionDAO.getAllWorkOrderStepsForTraversal(wOID, subactivityDefID);
				
				FlowChartReverseTraversalModel dummyStep=flowchartSteps.values().stream().filter(
						flowchartStep->StringUtils.equalsIgnoreCase(flowchartStep.getExecutionType(), AppConstants.MANUAL_DISABLED))
						.findAny().orElseGet(()->{ return new FlowChartReverseTraversalModel().executionType(AppConstants.MANUAL_DISABLED);});
				
				currStepID=lastStepID;
				FlowChartReverseTraversalModel flowChartReverse=null;
				
				Stack<String> stack= new Stack<>();
				stack.addAll(flowchartSteps.get(lastStepID).getParentsStepID());
				while(!stack.isEmpty() && flowchartSteps.containsKey(currStepID)) {
					if(stack.peek()==null) {
						stack.pop();
						branchContainsDisabled=false;
					}
					currStepID=stack.pop();
					flowChartReverse=flowchartSteps.get(currStepID);
					if(!flowChartReverse.isVisited()) {
						if(!checkIfStepNonDisabled(flowchartSteps.get(currStepID))) {
							branchContainsDisabled=true;
							
							for(int i=0;i<flowChartReverse.getParentsStepID().size();++i) {
								if(i>0) stack.push(null);
								stack.push(flowChartReverse.getParentsStepID().get(i));
							}
						}
						else if(StringUtils.equalsIgnoreCase(flowChartReverse.getStatus(), AppConstants.COMPLETED)
								|| StringUtils.equalsIgnoreCase(flowChartReverse.getStatus(), AppConstants.SKIPPED)) {
							if(branchContainsDisabled &&
									!StringUtils.equalsIgnoreCase(dummyStep.getStatus(), AppConstants.COMPLETED)
									&&  !StringUtils.equalsIgnoreCase(dummyStep.getStatus(), AppConstants.ONHOLD)) {
								throw new ApplicationException(200,DISABLED_MANUAL_COMPLETE_ERROR);
							}
							apiResponse.setResponseData(true);
							return apiResponse;
						}
					}
					
					flowchartSteps.get(currStepID).setVisited(true);
				}
				
				if(stack.isEmpty() 
						&& flowChartReverse.getParentsStepID().isEmpty()) {
					if(branchContainsDisabled &&
							!StringUtils.equalsIgnoreCase(dummyStep.getStatus(), AppConstants.COMPLETED)
							&&  !StringUtils.equalsIgnoreCase(dummyStep.getStatus(), AppConstants.ONHOLD)) {
						throw new ApplicationException(200,DISABLED_MANUAL_COMPLETE_ERROR);
					}
					apiResponse.setResponseData(true);
				}
				else {
					apiResponse.setResponseData(false);
				}
				stack=null;
			}
			else {
				apiResponse.setResponseData(!wOExecutionDAO.checkIFLastStepNew(wOID, subactivityDefID).isEmpty());
			}
		}
		catch(Exception e) {
			LOG.error(e.getMessage());
			apiResponse.setResponseData(false);
			apiResponse.addFormError(e.getMessage());
		}
		LOG.info("COMPLETED: checkIFLastStepNew");
		return apiResponse;
	}
	
	private boolean checkIfStepNonDisabled(FlowChartReverseTraversalModel flowChartReverseTraversal) {
		return flowChartReverseTraversal.getStepProficiencyID()==null ||flowChartReverseTraversal.getStepProficiencyID()==22;
	}

	public List<Map<String, Object>> getWorkOrderDetailsByName(String woName, String signum) {
		return woPlanService.getWorkOrderDetailsByName(woName, signum);
	}

	private String WF_STEP_TYPE_MANUAL = "ericsson.Manual";
	private String WF_STEP_TYPE_AUTOMATIC = "ericsson.Automatic";
	private String WF_STEP_TYPE_START = "ericsson.StartStep";
	private String WF_STEP_TYPE_START2 = "ericsson.StartStep";
	private String WF_STEP_TYPE_STOP = "ericsson.EndStep";
	private String WF_STEP_TYPE_DECISION = "ericsson.Decision";
	

	@Transactional("transactionManager")
	public ResponseEntity<Response<WorkOrderProgressResultModel>> getWorkOrderProgressV1(Integer workOrderId) {
		Response<WorkOrderProgressResultModel> apiResponse = new Response<>();
		WorkOrderProgressResultModel response = new WorkOrderProgressResultModel();
		try {
			List<WorkFlowStepLinksDetailModel> stepLinksDetail = wOExecutionDAO
					.getFlowChartStepLinkDetailsByWoId(workOrderId);
			List<FcStepDetails> stepBookingDetails = workOrderPlanDao.getStepBookingDetailsByWoidV1(workOrderId);
			WorkFlowStepLinksDetailModel startStep = null;
			WorkFlowStepLinksDetailModel stopStep = null;
			WorkFlowStepLinksDetailModel currentStep = null;

			Graph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);
			FcStepDetails lastStepDetails = null;
			if (stepBookingDetails.size() > 0) {
				lastStepDetails = stepBookingDetails.get(0);
			}
			for (WorkFlowStepLinksDetailModel stepLink : stepLinksDetail) {
				if (!g.containsVertex(stepLink.getSourceStepid())) {
					g.addVertex(stepLink.getSourceStepid());
				}
				if (WF_STEP_TYPE_START2.equalsIgnoreCase(stepLink.getSourceStepType())
						|| WF_STEP_TYPE_START.equalsIgnoreCase(stepLink.getSourceStepType())) {
					startStep = stepLink;
				}
				if (WF_STEP_TYPE_STOP.equalsIgnoreCase(stepLink.getSourceStepType())) {
					stopStep = stepLink;
				}

				if (stepBookingDetails != null && stepBookingDetails.size() > 0
						&& (stepLink.getSourceStepid().equals(lastStepDetails.getFlowChartStepId()))) {
					currentStep = stepLink;
				}
			}
			if (stopStep != null) {
				g.addVertex(stopStep.getSourceStepid());
			}
			for (WorkFlowStepLinksDetailModel stepLink : stepLinksDetail) {
				if (stepLink.getTargetStepId() != null) {
					g.addEdge(stepLink.getSourceStepid(), stepLink.getTargetStepId());
				}
			}

			if (currentStep == null) {
				currentStep = startStep;
			}

			AllDirectedPaths allPathFinder = new AllDirectedPaths<>(g);

			List<GraphPath<String, DefaultEdge>> startToCurrentPaths = allPathFinder
					.getAllPaths(startStep.getSourceStepid(), currentStep.getSourceStepid(), true, 30);

			List<GraphPath<String, DefaultEdge>> currentToEndPaths = allPathFinder
					.getAllPaths(currentStep.getSourceStepid(), stopStep.getSourceStepid(), true, 30);

			GraphPath<String, DefaultEdge> startToCurrentPath = null;
			GraphPath<String, DefaultEdge> currentToEndPath = null;

			for (GraphPath<String, DefaultEdge> pa : startToCurrentPaths) {
				if (startToCurrentPath == null || startToCurrentPath.getLength() < pa.getLength()) {
					startToCurrentPath = pa;
				}
			}

			for (GraphPath<String, DefaultEdge> pa : currentToEndPaths) {
				if (currentToEndPath == null || currentToEndPath.getLength() < pa.getLength()) {
					currentToEndPath = pa;
				}
			}

			Set<String> stepsFromStartToCurrent = new HashSet<>();
			Set<String> stepsFromCurrentToEnd = new HashSet<>();
			for (WorkFlowStepLinksDetailModel stepLink : stepLinksDetail) {
				if ((WF_STEP_TYPE_AUTOMATIC.equalsIgnoreCase(stepLink.getSourceStepType())
						|| WF_STEP_TYPE_MANUAL.equalsIgnoreCase(stepLink.getSourceStepType()))
						&& (stepLink.getSourceStepid() != startStep.getSourceStepid())
						&& (stepLink.getSourceStepid() != stopStep.getSourceStepid())) {
					if (startToCurrentPath.getVertexList().contains(stepLink.getSourceStepid())) {
						stepsFromStartToCurrent.add(stepLink.getSourceStepid());
					}
					if (currentToEndPath.getVertexList().contains(stepLink.getSourceStepid())) {
						stepsFromCurrentToEnd.add(stepLink.getSourceStepid());
					}
				}
			}
			int completedStepsCount = stepsFromStartToCurrent.size();
			int remainingStepsCount = stepsFromCurrentToEnd.size(); // remove stop's count

			if (lastStepDetails != null && !WF_STEP_TYPE_DECISION.equalsIgnoreCase(currentStep.getSourceStepType())
					&& (AppConstants.COMPLETED.equalsIgnoreCase(lastStepDetails.getStatus() + StringUtils.EMPTY)
							|| AppConstants.SKIPPED
									.equalsIgnoreCase(lastStepDetails.getStatus() + StringUtils.EMPTY))) {
				remainingStepsCount--;
			} else if (lastStepDetails != null
					&& (AppConstants.STARTED.equalsIgnoreCase(lastStepDetails.getStatus() + StringUtils.EMPTY)
							|| AppConstants.ONHOLD.equalsIgnoreCase(lastStepDetails.getStatus() + StringUtils.EMPTY))) {
				completedStepsCount--;
			}
			response.setCompletedPercentage(
					(100 * (completedStepsCount) / (completedStepsCount + remainingStepsCount)) + StringUtils.EMPTY);

			response.setProgressDescription(remainingStepsCount + " steps remaining out of total "
					+ (completedStepsCount + remainingStepsCount));
			apiResponse.setResponseData(response);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage());
			apiResponse.addFormError(e.getMessage());
			return new ResponseEntity<Response<WorkOrderProgressResultModel>>(apiResponse, HttpStatus.OK);
		} catch (Exception e) {
			apiResponse.setResponseData(response);
			apiResponse.addFormError(e.getMessage());
			LOG.error(e.getMessage());
			return new ResponseEntity<Response<WorkOrderProgressResultModel>>(apiResponse,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Response<WorkOrderProgressResultModel>>(apiResponse, HttpStatus.OK);

	}
/** Replica for Api: getWorkOrderProgress
 * */	
	@Transactional("transactionManager")
	public Response<HashMap<String, String>> getWorkOrderProgress(Integer workOrderId) {
		
		Response<HashMap<String, String>> apiResponse = new Response<>();
		HashMap<String, String> response = new HashMap<>();
		try {
			List<WorkFlowStepLinksDetailModel> stepLinksDetail = wOExecutionDAO
					.getFlowChartStepLinkDetailsByWoId(workOrderId);
			List<Map<String, Object>> stepBookingDetails = workOrderPlanDao.getStepBookingDetailsByWoid(workOrderId);
			WorkFlowStepLinksDetailModel startStep = null;
			WorkFlowStepLinksDetailModel stopStep = null;
			WorkFlowStepLinksDetailModel currentStep = null;

			Graph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);

			Map<String, Object> lastStepDetails = null;
			if (CollectionUtils.isNotEmpty(stepBookingDetails)) {
				lastStepDetails = stepBookingDetails.get(0);
			}
			for (WorkFlowStepLinksDetailModel stepLink : stepLinksDetail) {
				if (!g.containsVertex(stepLink.getSourceStepid())) {
					g.addVertex(stepLink.getSourceStepid());
				}
				if (WF_STEP_TYPE_START2.equalsIgnoreCase(stepLink.getSourceStepType())
						|| WF_STEP_TYPE_START.equalsIgnoreCase(stepLink.getSourceStepType())) {
					startStep = stepLink;
				}
				if (WF_STEP_TYPE_STOP.equalsIgnoreCase(stepLink.getSourceStepType())) {
					stopStep = stepLink;
				}

				if (stepBookingDetails != null && CollectionUtils.isNotEmpty(stepBookingDetails)
						&& (stepLink.getSourceStepid().equals(lastStepDetails.get("FlowChartStepID")))) {
					currentStep = stepLink;
				}
			}
			if (stopStep != null) {
				g.addVertex(stopStep.getSourceStepid());
			}
			for (WorkFlowStepLinksDetailModel stepLink : stepLinksDetail) {
				if (stepLink.getTargetStepId() != null) {
					g.addEdge(stepLink.getSourceStepid(), stepLink.getTargetStepId());
				}
			}

			if (currentStep == null) {
				currentStep = startStep;
			}

			AllDirectedPaths allPathFinder = new AllDirectedPaths<>(g);

            List<GraphPath<String, DefaultEdge>> startToCurrentPaths = allPathFinder
                                         .getAllPaths(startStep.getSourceStepid(), currentStep.getSourceStepid(), true, 30);

            List<GraphPath<String, DefaultEdge>> currentToEndPaths = allPathFinder
                                         .getAllPaths(currentStep.getSourceStepid(), stopStep.getSourceStepid(), true, 30);

			GraphPath<String, DefaultEdge> startToCurrentPath = null;
			GraphPath<String, DefaultEdge> currentToEndPath = null;

			for (GraphPath<String, DefaultEdge> pa : startToCurrentPaths) {
				if (startToCurrentPath == null || startToCurrentPath.getLength() < pa.getLength()) {
					startToCurrentPath = pa;
				}
			}

			for (GraphPath<String, DefaultEdge> pa : currentToEndPaths) {
				if (currentToEndPath == null || currentToEndPath.getLength() < pa.getLength()) {
					currentToEndPath = pa;
				}
			}

			Set<String> stepsFromStartToCurrent = new HashSet<>();
			for (WorkFlowStepLinksDetailModel stepLink : stepLinksDetail) {
				if ((WF_STEP_TYPE_AUTOMATIC.equalsIgnoreCase(stepLink.getSourceStepType())
						|| WF_STEP_TYPE_MANUAL.equalsIgnoreCase(stepLink.getSourceStepType()))
						&& startToCurrentPath.getVertexList().contains(stepLink.getSourceStepid())
						&& (stepLink.getSourceStepid() != startStep.getSourceStepid())
						&& (stepLink.getSourceStepid() != stopStep.getSourceStepid())) {
					stepsFromStartToCurrent.add(stepLink.getSourceStepid());
				}
			}
			Set<String> stepsFromCurrentToEnd = new HashSet<>();
			for (WorkFlowStepLinksDetailModel stepLink : stepLinksDetail) {
				if ((WF_STEP_TYPE_AUTOMATIC.equalsIgnoreCase(stepLink.getSourceStepType())
						|| WF_STEP_TYPE_MANUAL.equalsIgnoreCase(stepLink.getSourceStepType()))
						&& currentToEndPath.getVertexList().contains(stepLink.getSourceStepid())
						&& (stepLink.getSourceStepid() != startStep.getSourceStepid())
						&& (stepLink.getSourceStepid() != stopStep.getSourceStepid())) {
					stepsFromCurrentToEnd.add(stepLink.getSourceStepid());
				}
			}
			

			int completedStepsCount = stepsFromStartToCurrent.size();
			int remainingStepsCount = stepsFromCurrentToEnd.size(); // remove stop's count

			if (lastStepDetails != null && !WF_STEP_TYPE_DECISION.equalsIgnoreCase(currentStep.getSourceStepType())
					&& (AppConstants.COMPLETED
							.equalsIgnoreCase(lastStepDetails.get(AppConstants.STATUS) + StringUtils.EMPTY)
							|| "SKIPPED".equalsIgnoreCase(lastStepDetails.get(AppConstants.STATUS) + StringUtils.EMPTY))) {
				remainingStepsCount--;
			} else if (lastStepDetails != null
					&& ("STARTED".equalsIgnoreCase(lastStepDetails.get(AppConstants.STATUS) + StringUtils.EMPTY)
							|| AppConstants.ONHOLD
									.equalsIgnoreCase(lastStepDetails.get(AppConstants.STATUS) + StringUtils.EMPTY))) {
				completedStepsCount--;
			}
			response.put("completedPercentage",
					(100 * (completedStepsCount) / (completedStepsCount + remainingStepsCount)) + StringUtils.EMPTY);
			response.put("progressDescription",
					remainingStepsCount + " steps remaining out of total " + (completedStepsCount + remainingStepsCount));
			
			apiResponse.setResponseData(response);
		}catch (Exception e) {
			apiResponse.setResponseData(response);
			apiResponse.addFormError(e.getMessage());
			LOG.error(e.getMessage());
		}
		
		return apiResponse;

	}
	

	@Transactional("transactionManager")
	public Map<String, String> startAllWorkOrderTasks(WoStepDetailsList woList) {
		Map<String, String> response = new HashMap<>();
		try {
			for (WoStepDetails detail : woList.getWoList()) {
				checkAndUpdateFlowChartDefID(detail.getWoID(), detail.getFlowChartDefID());
				wOExecutionDAO.updateStatusWO(detail.getWoID());
				startStep(detail.getWoID(), detail.getTaskID(), detail.getStepID(), detail.getFlowChartDefID(),
						detail.getSignumID(), detail.getDecisionValue(), detail.getExecutionType(), null, null, null,
						null, new ServerBotModel(),0);
				//need to change after discussion for last variable
			}
			response.put(AppConstants.SUCCESS, "All WO/Steps Started");
			// response.put("hour",
			// wOExecutionDAO.getBookingHoursForStep(detail.getStepID(), detail.getwOID()));
		} catch (Exception ex) {
			response.put("Error", ex.getMessage());
			//throw ex;
		}
		return response;
	}

	@Transactional("transactionManager")
	public RpaApiResponse startServerBot(MultipartFile inputZip, String serverBotJsonStr, String botConfigJson) {
		RpaApiResponse res = new RpaApiResponse();

		try {
			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(serverBotJsonStr);
			ServerBotModel serverBotModel = gson.fromJson(object, ServerBotModel.class);

			TblRpaDeployedBot botDetails = botStoreService.getBotDetail(serverBotModel.getBotId());
			serverBotModel.setBotType(botDetails.getBotlanguage());
			serverBotModel.setIsECNConnected("true");
			serverBotJsonStr = gson.toJson(serverBotModel);
			
			Integer sourceID=0;
			if(StringUtils.isNotBlank(serverBotModel.getRefferer())) {
				String referer=serverBotModel.getRefferer();
			if(referer.equalsIgnoreCase("ui")) {
				referer="WEB";
			}
			sourceID = rpaDAO.getExternalSource(referer);
			}

			if (BOT_TYPE_BOTNEST.equalsIgnoreCase(botDetails.getBotlanguage())) {
				WorkOrderModel woDetails = workOrderPlanDao.getWorkOrderDetailsById(serverBotModel.getwOID());
				int queueBookingId = addMiscBookingType(serverBotModel.getwOID(), serverBotModel.getTaskID(),
						(serverBotModel.getStepID() == null) ? StringUtils.EMPTY
								: serverBotModel.getStepID() + StringUtils.EMPTY,
						woDetails.getFlowchartdefid(), serverBotModel.getSignumID(), "Automatic",
						serverBotModel.getBotPlatform(), AppConstants.BOOKING_TYPE_QUEUED,sourceID);

				wOExecutionDAO.updateReferenceIDByBookingId(queueBookingId, queueBookingId, serverBotModel.getSignumID());
				BotConfig botConfig = new BotConfig();
				botConfig.setDescription(botDetails.getBotid() + StringUtils.EMPTY);
				botConfig.setReferenceId(queueBookingId + StringUtils.EMPTY);
				botConfig.setType("WO_STEP");
				botConfig.setJson(botConfigJson);

				rpaService.saveBotConfig(botConfig);
				Map<String, String> dataMap = new HashMap<>();
				dataMap.put(QUEUE_ID, queueBookingId + StringUtils.EMPTY);
				res.setDataMap(dataMap);
				res.setApiSuccess(true);
			}else {
				String resp = botService.runServerBot(inputZip, serverBotJsonStr,
						"/server-bot-app-java-Bot/serverBotExecutor/runBot");
				if ("success".equalsIgnoreCase(resp)) {
                    try {
					startWorkOrderTask(serverBotModel.getwOID(), serverBotModel.getTaskID(), serverBotModel.getStepID(),
							serverBotModel.getSubActivityFlowChartDefID(), serverBotModel.getSignumID(),
							serverBotModel.getDecisionValue(), serverBotModel.getExeType(),
							serverBotModel.getBotPlatform(), null, null, null, new ServerBotModel(),sourceID);

					res.setApiSuccess(true);
					res.setResponseMsg("Server BOT started successfully.");
					LOG.info("Server BOT started successfully.");
                    }
                    catch(Exception e){
						res.setApiSuccess(false);
						res.setResponseMsg(
								e.getMessage());
						return res;
					}
				} else {
					res.setApiSuccess(false);
					res.setResponseMsg(
							"There is some server connectivity issue now, please try later OR you may run your BOT on Local.");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("There is an issue with Server now, please try later OR you may run your BOT on Local. Error:: "
					+ e.getMessage());
			res.setApiSuccess(false);
			res.setResponseMsg("There is an issue with Server now, please try later OR you may run your BOT on Local");
		}

		return res;
	}

	@Transactional("transactionManager")
	private int addMiscBookingType(int wOID, int taskID, String stepID, int flowChartDefID, String signumID,
			String exeType, String botPlatform, String bookingType, Integer sourceID) {

		// if it is first step, mark work order to in-progress
		markWorkOrderAsStarted(wOID, signumID);

		int parallelCount = 1;
		StepDetailsModel stepDetails = wOExecutionDAO.getStepDetailsByStepId(wOID, stepID);
		if (stepDetails != null && (AppConstants.ONHOLD.equals(stepDetails.getStatus())
				|| AppConstants.SKIPPED.equals(stepDetails.getStatus()))) {
			wOExecutionDAO.updateBookingsStatusByIds("'" + stepDetails.getBookingId() + "'", AppConstants.COMPLETED,signumID);
			wOExecutionDAO.updateFcStepStatusByBookingIds("'" + stepDetails.getBookingId() + "'",
					AppConstants.COMPLETED);
		}

		Integer maxBookingId = wOExecutionDAO.getMaxBookingIdByWoid(wOID, signumID);

		BookingDetailsModel newBooking = new BookingDetailsModel();
		newBooking.setParallelcount(parallelCount);
		newBooking.setWoId(wOID);
		newBooking.setTaskId(taskID);
		newBooking.setParentBookingDetailsId(maxBookingId);
		newBooking.setType(bookingType);
		newBooking.setStatus(AppConstants.STARTED);
		newBooking.setSignumId(signumID);
		newBooking.setStartDate(new Date());
		newBooking.setSourceid(sourceID);
		wOExecutionDAO.addBooking(newBooking);

		List<FcStepDetails> stepsToBeAdded = new ArrayList<>();

		FcStepDetails newFcStepDetails = new FcStepDetails();
		newFcStepDetails.setWoId(wOID);
		newFcStepDetails.setFlowChartDefId(flowChartDefID);
		newFcStepDetails.setFlowChartStepId(stepID);
		newFcStepDetails.setTaskID(taskID);
		newFcStepDetails.setBookingID(newBooking.getBookingID());
		newFcStepDetails.setStatus(AppConstants.STARTED);
		newFcStepDetails.setSignumId(signumID);
		newFcStepDetails.setExecutionType(exeType);
		newFcStepDetails.setBotPlatform(botPlatform);
		stepsToBeAdded.add(newFcStepDetails);
		wOExecutionDAO.insertFcStepDetails(stepsToBeAdded);
		return newBooking.getBookingID();
	}

	public HashMap<String, Object> getEfficiencyDevlieryIndexForUser(int projectID, String subActivity,
			int flowChartDefID, int woID, boolean markAsComplete, String signumID) {
		HashMap<String, Object> finalData = new HashMap<>();

		if (!markAsComplete) {
			List<HashMap<String, Object>> beforeExecutionData = wOExecutionDAO.getEfficiencyDevlieryIndexForUser(woID);
			finalData.put("BeforeExecution", beforeExecutionData);
		} else {
			List<HashMap<String, Object>> afterExecutionData = wOExecutionDAO.getEfficiencyDevlieryIndexForUser(woID);
			finalData.put("AfterExecution", afterExecutionData);
		}
		return finalData;
	}

	public HashMap<String, Object> getEfficiencyDevlieryPerformance(int projectID, String subActivity,
			int flowChartDefID, int woID, boolean markAsComplete, String signumID) {
		HashMap<String, Object> finalData = new HashMap<>();
		LocalDate endDate = LocalDate.now();
		LocalDate startDate = LocalDate.now().minusDays(90);

		if (!markAsComplete) {
			populateData(startDate, endDate, 0);
			List<HashMap<String, Object>> beforeExecutionData = wOExecutionDAO
					.getEfficiencyDevlieryPerformance(projectID, subActivity, flowChartDefID, signumID);
			finalData.put("BeforeExecution", beforeExecutionData);
		} else {
			List<HashMap<String, Object>> beforeExecutionData = wOExecutionDAO
					.getEfficiencyDevlieryPerformance(projectID, subActivity, flowChartDefID, signumID);
			finalData.put("BeforeExecution", beforeExecutionData);
			populateData(startDate, endDate, woID);
			List<HashMap<String, Object>> afterExecutionData = wOExecutionDAO
					.getEfficiencyDevlieryPerformance(projectID, subActivity, flowChartDefID, signumID);
			finalData.put("AfterExecution", afterExecutionData);
			List<Object> differenceExecutionData = calculateDifference(beforeExecutionData, afterExecutionData);
			finalData.put("DifferenceData", differenceExecutionData);
		}
		return finalData;
	}

	private List<Object> calculateDifference(List<HashMap<String, Object>> bExecutionData,
			List<HashMap<String, Object>> aExecutionData) {

		List<Object> differenceData = new ArrayList<Object>();

		for (HashMap<String, Object> beforeExecutionData : bExecutionData) {
			for (HashMap<String, Object> afterExecutionData : aExecutionData) {
				if (beforeExecutionData.get("EmployeeName").equals(afterExecutionData.get("EmployeeName"))) {
					double efvalue = (double) afterExecutionData.get("EFiciencyIndex")
							- (double) beforeExecutionData.get("EFiciencyIndex");
					double ftrvalue = (double) afterExecutionData.get("FTR") - (double) beforeExecutionData.get("FTR");
					double slavalue = (double) afterExecutionData.get("SLA") - (double) beforeExecutionData.get("SLA");
					double dlvalue = (double) afterExecutionData.get("DeliveryIndex")
							- (double) beforeExecutionData.get("DeliveryIndex");
					HashMap<String, Object> data = new HashMap<>();
					data.put("EmployeeName", afterExecutionData.get("EmployeeName"));
					data.put("EFiciencyIndex", efvalue);
					data.put("FTR", ftrvalue);
					data.put("SLA", slavalue);
					data.put("DeliveryIndex", dlvalue);
					differenceData.add(data);
				}
			}
		}
		return differenceData;
	}

	private void populateData(LocalDate startDate, LocalDate endDate, int woID) {
		wOExecutionDAO.populateEfficiencyData(startDate, endDate, woID);
		wOExecutionDAO.populateDeliveryData(startDate, endDate, woID);
	}

	@Transactional("transactionManager")
	public Response<List<WorkOrderBasicDetailsModel>> getUnassignedWorkOrders(String signum, String startDate, String endDate,
			String priority, String status, String doStatus) {
		
		Response<List<WorkOrderBasicDetailsModel>> response = new Response<>();
		try {
			validateInputParametersForGetUnassignedWorkOrders(startDate,endDate);
			response.setResponseData(wOExecutionDAO.getUnassignedWorkOrders(signum, startDate, endDate, priority, status, doStatus));
		}
		catch(Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return response;
		}
		return response;
	}
	
	public String formatProjectQueueInputs="'%S'";
	/**@author edyudev
	 * new API form getUnassignedWorkOrders
	 * */
	@Transactional("transactionManager")
	public Response<PlannedWOProject> getProjectQueueWorkOrders(String signum, String startDate,
			String endDate, String priority, String status, String doStatus,String projectIdList, DataTableRequest request,boolean checkSearch) {
		PlannedWOProject plannedWoProject=new PlannedWOProject();
		Response<PlannedWOProject> response = new Response<>();
		
		
		try {
			validateInputParametersForGetUnassignedWorkOrders(startDate,endDate);
			List<ProjectQueueWorkOrderBasicDetailsModel> listSearchPlannedWoProjectModel=wOExecutionDAO.getProjectQueueWorkOrders(
					String.format(formatProjectQueueInputs, signum),
					String.format(formatProjectQueueInputs, startDate),
					String.format(formatProjectQueueInputs, endDate),
					String.format(formatProjectQueueInputs, priority),
					String.format(formatProjectQueueInputs, status),
					String.format(formatProjectQueueInputs, doStatus),
					String.format(formatProjectQueueInputs, projectIdList), request,checkSearch);
			
			
			if(CollectionUtils.isEmpty(listSearchPlannedWoProjectModel)) {
				plannedWoProject.setRecordsTotal(0);
				plannedWoProject.setRecordsFiltered(0);
				plannedWoProject.setData(listSearchPlannedWoProjectModel);
				plannedWoProject.setDraw(request.getDraw());
			}else {
				plannedWoProject.setRecordsTotal(listSearchPlannedWoProjectModel.get(0).getRecordsTotal());
				plannedWoProject.setRecordsFiltered(listSearchPlannedWoProjectModel.get(0).getRecordsTotal());
				plannedWoProject.setData(listSearchPlannedWoProjectModel);
				plannedWoProject.setDraw(request.getDraw());
			}
			
			
			response.setResponseData(plannedWoProject);
		}
		catch(Exception e) {
			LOG.error(e.getMessage());
			response.addFormError("Error occurred");
			return response;
		}
		return response;
	}
    
	private void validateInputParametersForGetUnassignedWorkOrders(String startDate, String endDate) {
		validationUtilityService.validateDefaultDateFormat(startDate,endDate,AppConstants.DEFAULT_DATE_FORMAT);	
	}

	public List<Map<String, Object>> assignWo(AssignWOModel workOrderModel) {

		List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
		Map<String, Object> innerMap = new HashMap<String, Object>();
		for (WorkOrderModel woData : workOrderModel.getWorkOrderModel()) {
			Boolean checkEmpTbl = this.accessManagementDAO.isSignumExistsEmp(woData.getSignumID());
			if (!checkEmpTbl) {
				innerMap.put(AppConstants.STATUS, false);
				innerMap.put(AppConstants.MSG, "Invalid Signum,Please provide valid Signum ");
				response.add(innerMap);
				return response;
			}

			WorkOrderModel data = wOExecutionDAO.getWOData(woData.getwOID());
			innerMap.put("wOID", woData.getwOID());
			if (data.getActive()) {
				if (data.getSignumID() == null || data.getStatus().equals("ASSIGNED")
						|| data.getStatus().equals(AppConstants.ONHOLD)) {
					innerMap.put(AppConstants.STATUS, wOExecutionDAO.updateWoWithAsignedSignum(woData));
					innerMap.put(AppConstants.MSG, "Successfully Updated");
				} else {
					if (woData.getUiStatus() != null && "TRANSFER".equals(woData.getUiStatus())) {
						List<Map<String, Object>> taskDetails = wOExecutionDAO.checkTaskStatus(woData);
						// if (taskDetails.size() == 0 &&
						// data.getSignumID().equals(woData.getSenderSignum())){
						if (taskDetails.size() == 0) {
							innerMap.put(AppConstants.STATUS, wOExecutionDAO.updateWoWithAsignedSignum(woData));
							wOExecutionDAO.insertWOTransferHistory(woData);
							innerMap.put(AppConstants.MSG, "Successfully Updated");
						} else {
							innerMap.put(AppConstants.STATUS, false);
							innerMap.put(AppConstants.MSG,
									"Work Order cannot be transferred while in progress/WO Order is not assigned to you. Kindly Refresh the page.");
						}
					}
				}
			} else {
				innerMap.put(AppConstants.STATUS, false);
				innerMap.put(AppConstants.MSG, "The Selected WO was in Inactive. Kindly Refresh the page.");
			}

			response.add(innerMap);
			innerMap = new HashMap<String, Object>();
		}
		return response;
	}

	public List<WorkOrderBasicDetailsModel> getAssignedWorkOrders(String signum, String woStatus, String startDate,
			String endDate) {
		List<WorkOrderBasicDetailsModel> workOrderPlan = new ArrayList<>();
		return workOrderPlan = wOExecutionDAO.getAssignedWorkOrders(signum, woStatus, startDate, endDate);
	}

	public List<Map<String, Object>> updateWoSignumToNull(List<WorkOrderModel> workOrderModel) {

		List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
		Map<String, Object> innerMap = new HashMap<String, Object>();
		for (WorkOrderModel woData : workOrderModel) {
			innerMap.put(AppConstants.STATUS, wOExecutionDAO.updateWoSignumToNull(woData));
			innerMap.put("wOID", woData.getwOID());
			response.add(innerMap);
			innerMap = new HashMap<String, Object>();
		}
		return response;
	}

	public boolean saveExternalExecDetails(ExternalExecutionDetailsModel externalRef) {
		return wOExecutionDAO.saveExternalExecDetails(externalRef.getIsfReferenceId(),
				externalRef.getExternalReferenceId());
	}

	public boolean pauseAllTaskOnLogout(UserLoginModel userLogin) throws InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					autoSenseDao.completeSensedRules(userLogin.getSignumID(),"Logout","WEB");
					Thread.sleep(configurations.getIntegerProperty(ConfigurationFilesConstants.LOGOUT_TIME_WAIT));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pauseTaskAndAdhoc(userLogin);
			}
		}).start();
		return true;
	}

	public void pauseAdhoc(String signumID) {
		TblAdhocBooking tblAdhocBooking=adhocActivityService.getAdhocBookingForSignum(signumID);
		tblAdhocBooking.setLastModifiedBy(signumID);
		tblAdhocBooking.setComment("STOP");
		tblAdhocBooking.setBookedDuration("actual");
		adhocActivityService.saveAdhocBooking(tblAdhocBooking);
		
	}
	
	public void pauseTaskAndAdhoc(UserLoginModel userLogin) {
		Map<String, Object> tokenData = wOExecutionDAO.validateUserLoginHistory(userLogin);
		if (tokenData == null) {
			List<CurrentWorkOrderModel> workOrder = getCurrentWorkOrderList(userLogin.getSignumID(), "2");
			for (CurrentWorkOrderModel woDetails : workOrder) {
				int woID = woDetails.getwOID();
				int flowChartDefID = woDetails.getSubActivityFlowChartDefID();
				String stepID = woDetails.getListOfStepTaskDetails().get(0).getStepID();
				for (TaskModel taskModel : woDetails.getListOfStepTaskDetails().get(0).getListOfTaskModel()) {
					int taskID = taskModel.getTaskID();
					int bookingID = taskModel.getLstBookingDetailsModels().get(0).getBookingID();
					if ( wOExecutionDAO.checkBookingStatus(woID, bookingID)) {
							wOExecutionDAO.updateBookingStatus(woID, bookingID, userLogin.getSignumID(),
									AppConstants.ONHOLD, userLogin.getStatus());
							wOExecutionDAO.updateWOFlowChartStepDetails(woID, taskID, bookingID, stepID, flowChartDefID,
									AppConstants.ONHOLD, userLogin.getStatus(), userLogin.getSignumID());
//						}
					}
				}
			}
			//stop all adhoc activities on logout
			pauseAdhoc(userLogin.getSignumID());
		}
	}

	@Transactional("transactionManager")
	public Map<String, Object> startTask(MultipartFile inputZip,ServerBotModel serverBotModel, String botConfigJson)
			throws Exception {

		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String stepID = serverBotModel.getStepID();
			WorkOrderModel workOrderModel=new WorkOrderModel();
			int sourceID = getSourceId(serverBotModel);

			workOrderModel = validateServerBotModelStartTask(serverBotModel, workOrderModel);

			boolean flag = true;
			String msg = StringUtils.EMPTY;

			setSubActivityFlowChartDefID(serverBotModel, workOrderModel);

			if (AppConstants.BOT_PLATFORM_SERVER.equalsIgnoreCase(serverBotModel.getBotPlatform())) {
				//SB
				response=botPlatformServerStartStep(inputZip, serverBotModel, botConfigJson, response, stepID, sourceID, flag, msg);//changes
			} else {
				response = manualOrNonServerBotStartStep(serverBotModel, botConfigJson, response, stepID, sourceID,
						flag, msg);
			}

			response.put(AppConstants.FLOWCHART_TYPE, wOExecutionDAO.getFlowChartType(serverBotModel.getwOID())); ;

			SignalrModel signalrModel=getValidHubMethodsforSignalrCallBySource(serverBotModel, workOrderModel,response);
			if (signalrModel!=null){
				appService.callSignalrApplicationToCallSignalRHub(signalrModel);
			}
		} 
		catch(Exception e) {
			response.put(IS_API_SUCCESS, false);
			if(!response.containsKey(RESPONSE_MSG)) {
				response.put(RESPONSE_MSG, e.getMessage());
			}
		}
		return response;
	}
	
	/**
	 * @param serverBotModel
	 * @param botConfigJson
	 * @param response
	 * @param stepID
	 * @param sourceID
	 * @param flag
	 * @param msg
	 * @return
	 */
	private Map<String, Object> manualOrNonServerBotStartStep(ServerBotModel serverBotModel, String botConfigJson,
			Map<String, Object> response, String stepID, Integer sourceID, boolean flag, String msg) {
		int referenceID = 0;
		if (AppConstants.AUTOMATIC.equalsIgnoreCase(serverBotModel.getExecutionType())
				&& AppConstants.BOOKING.equalsIgnoreCase(serverBotModel.getBookingType())) {
			WorkFlowBookingDetailsModel maxBookingID = wOExecutionDAO.maxQueuedBookingID(serverBotModel.getwOID(),
					serverBotModel.getTaskID());
			
			if (maxBookingID != null) {

				StringBuilder bookingids= new StringBuilder();
				bookingids.append(AppConstants.SINGLE_QUOTE).append(maxBookingID.getBookingID()).append( AppConstants.SINGLE_QUOTE);
//				String bookingids = AppConstants.SINGLE_QUOTE + maxBookingID.getBookingID()
//				+ AppConstants.SINGLE_QUOTE;
				if (AppConstants.SKIPPED.equalsIgnoreCase(maxBookingID.getStatus())) {
					wOExecutionDAO.updateOnlyBookingsStatusByIds(bookingids.toString(), AppConstants.COMPLETED,serverBotModel.getSignumID());

				} else {
					wOExecutionDAO.updateBookingsStatusByIds(bookingids.toString(), AppConstants.COMPLETED,serverBotModel.getSignumID());
					wOExecutionDAO.updateOnlyBookingsReasonByIds(bookingids.toString(), serverBotModel.getReason(),serverBotModel.getSignumID());
					wOExecutionDAO.updateFcStepStatusByBookingIds(bookingids.toString(), AppConstants.COMPLETED);
				}

				referenceID = maxBookingID.getBookingID();
				if (StringUtils.equals(ZERO_STRING, serverBotModel.getStepID())) {
					stepID = wOExecutionDAO.getFlowChartStepIDByBooking(serverBotModel.getwOID(),
							serverBotModel.getTaskID(), maxBookingID.getBookingID(), serverBotModel.getSignumID());
				}
			}
		}
		try {
			response = startWorkOrderTask(serverBotModel.getwOID(), serverBotModel.getTaskID(), stepID,
					serverBotModel.getSubActivityFlowChartDefID(), serverBotModel.getSignumID(),
					serverBotModel.getDecisionValue(), serverBotModel.getExecutionType(),
					serverBotModel.getBotPlatform(), referenceID, botConfigJson, serverBotModel.getBookingType(),
					serverBotModel,sourceID);
			
			// check if any Disabled step is in ONHOLD, if yes then make it Completed on Start of Manual Step of same WOID.
//			if(flag && AppConstants.MANUAL.equalsIgnoreCase(serverBotModel.getExecutionType())) {
//				//find maxBooking ID for which status is ONHOLD and ExecutionType is DisabledManual by WOID
//				WorkFlowBookingDetailsModel maxOnHoldBookingID = wOExecutionDAO.maxDisabledOnHoldBookingID(serverBotModel.getwOID(),0);
//				
//				if(maxOnHoldBookingID != null) {
//					wOExecutionDAO.updateBookingStatusOnStart(serverBotModel.getwOID(), maxOnHoldBookingID.getBookingID(), serverBotModel.getSignumID(),
//							AppConstants.COMPLETED, "Manual Started");
//					wOExecutionDAO.updateWOFlowChartStepDetails(serverBotModel.getwOID(), 0, maxOnHoldBookingID.getBookingID(), maxOnHoldBookingID.getStepID(), serverBotModel.getFlowChartDefID(),
//							AppConstants.COMPLETED, "Manual Started", serverBotModel.getSignumID());													
//				}
//			}
			
			response.put(IS_API_SUCCESS, flag);
			response.put(RESPONSE_MSG, msg);
		}
		catch(Exception e){
		    response.put(IS_API_SUCCESS, false);
			response.put(RESPONSE_MSG, e.getMessage());
			return response;
		}
		return response;
	}
	
	
	/**
	 * @param inputZip
	 * @param serverBotModel
	 * @param botConfigJson
	 * @param response
	 * @param stepID
	 * @param sourceID
	 * @param flag
	 * @param msg
	 * @return 
	 */
	private Map<String, Object> botPlatformServerStartStep(MultipartFile inputZip, ServerBotModel serverBotModel, String botConfigJson,
			Map<String, Object> response, String stepID, Integer sourceID, boolean flag, String msg) {
		int referenceID = 0;
		if (AppConstants.AUTOMATIC.equalsIgnoreCase(serverBotModel.getExecutionType())
				&& AppConstants.BOOKING.equalsIgnoreCase(serverBotModel.getBookingType())) {
			WorkFlowBookingDetailsModel maxBookingID = wOExecutionDAO.maxQueuedBookingID(serverBotModel.getwOID(),
					serverBotModel.getTaskID());
			if (maxBookingID != null) {

				String bookingids = AppConstants.SINGLE_QUOTE + maxBookingID.getBookingID()
				+ AppConstants.SINGLE_QUOTE;
				if (AppConstants.SKIPPED.equalsIgnoreCase(maxBookingID.getStatus())) {
					wOExecutionDAO.updateOnlyBookingsStatusByIds(bookingids, AppConstants.COMPLETED,serverBotModel.getSignumID());
				} else {
					wOExecutionDAO.updateBookingsStatusByIds(bookingids, AppConstants.COMPLETED,serverBotModel.getSignumID());
					wOExecutionDAO.updateFcStepStatusByBookingIds(bookingids, AppConstants.COMPLETED);
				}
				// to be removed after step id and task id validation
				if (serverBotModel.getStepID().equals(ZERO_STRING)) {
					stepID = wOExecutionDAO.getFlowChartStepIDByBooking(serverBotModel.getwOID(),
							serverBotModel.getTaskID(), maxBookingID.getBookingID(), serverBotModel.getSignumID());
				}
				referenceID = maxBookingID.getBookingID();
			}
		}
		try {
			response = startWorkOrderTask(serverBotModel.getwOID(), serverBotModel.getTaskID(), stepID,
					serverBotModel.getSubActivityFlowChartDefID(), serverBotModel.getSignumID(),
					serverBotModel.getDecisionValue(), serverBotModel.getExecutionType(),
					serverBotModel.getBotPlatform(), referenceID, botConfigJson, serverBotModel.getBookingType(),
					serverBotModel,sourceID);
			response.put(IS_API_SUCCESS, flag);
			response.put(RESPONSE_MSG, msg);
		}
		catch(Exception e){
			response.put(IS_API_SUCCESS, false);
			response.put(RESPONSE_MSG, e.getMessage());
		}

		try {

			Gson gson = new GsonBuilder().disableHtmlEscaping().create();

			TblRpaDeployedBot botDetails = botStoreService.getBotDetail(serverBotModel.getBotId());
			serverBotModel.setBotType(botDetails.getBotlanguage());
			serverBotModel.setIsECNConnected(AppConstants.TRUE_STRING);
			serverBotModel.setqBookingId(String.valueOf(response.get(AppConstants.BOOKING_ID)));

			String serverBotJsonStr = gson.toJson(serverBotModel);
			String resp = null;


			if (BOT_TYPE_BOTNEST.equalsIgnoreCase(botDetails.getBotlanguage())) {
				response.put(IS_API_SUCCESS, true);
			} else {
				//SB
				resp = botService.runServerBot(inputZip, serverBotJsonStr,
						AppConstants.RUN_SERVER_BOT_ENDPOINT);
				if (StringUtils.equalsIgnoreCase(AppConstants.SUCCESS, resp)) {
					flag = true;
					msg = AppConstants.SERVER_BOT_START_SUCCESS;
				} else {
					flag = false;
					msg = AppConstants.SERVER_BOT_START_FAILURE;
				}
			}

		} catch (Exception e) {
			flag = false;
			msg = AppConstants.SERVER_BOT_START_FAILURE+e.getMessage();
			response.put(RESPONSE_MSG, msg);
			return response;
		}
		return response;
	}
	
	/**
	 * @param serverBotModel
	 * @param workOrderModel
	 */
	private void setSubActivityFlowChartDefID(ServerBotModel serverBotModel, WorkOrderModel workOrderModel) {
		if (serverBotModel.getFlowChartDefID()==null || serverBotModel.getFlowChartDefID() == 0) {
			serverBotModel.setFlowChartDefID(workOrderModel.getFlowchartdefid());
		}
		if(serverBotModel.getSubActivityFlowChartDefID()==null || serverBotModel.getSubActivityFlowChartDefID() == 0) {
			serverBotModel.setSubActivityFlowChartDefID(workOrderModel.getFlowchartdefid());
		}
	}
	
	/**
	 * @param serverBotModel
	 * @param response
	 * @param workOrderModel
	 * @return
	 * @throws Exception 
	 */
	private WorkOrderModel validateServerBotModelStartTask(ServerBotModel serverBotModel,
			WorkOrderModel workOrderModel) throws Exception {
			workOrderModel = validationUtilityService.getWorkOrderModel(serverBotModel.getwOID());
			validateServerBotModelForStartTask(serverBotModel,workOrderModel);
			
		return workOrderModel;
	}

	/**
	 * @param serverBotModel
	 * @return
	 */
	private int getSourceId(ServerBotModel serverBotModel  ) {
		if(StringUtils.isNotBlank(serverBotModel.getRefferer())) {
			String referer=serverBotModel.getRefferer();
			if(referer.equalsIgnoreCase(AppConstants.UI)) {//StrinUtils 
				referer=AppConstants.WEB;
			}
			return rpaDAO.getExternalSource(referer);
		}
		return 0;
	}
	
	private SignalrModel getValidHubMethodsforSignalrCallBySource(ServerBotModel serverBotModel, WorkOrderModel workOrderModel, Map<String, Object> response) {
		if(MapUtils.isEmpty(response) || Boolean.FALSE.equals((response.get(IS_API_SUCCESS)))) {
			return null;
		}
		
		SignalrModel signalRModel = null;
		switch(serverBotModel.getRefferer().toUpperCase()) {
			case AppConstants.DESKTOP:
				signalRModel=appService.returnSignalrConfiguration(getWoAutoTaskInfoModel(serverBotModel, workOrderModel, response)
						,configurations.getStringProperty(ConfigurationFilesConstants.HUB_METHOD_NAME));
				break;
			case AppConstants.VOICE:
				if(activityMasterService.isSignumExist(serverBotModel.getSignumID())) {
					signalRModel=appService.returnSignalrConfiguration(getWoAutoTaskInfoModel(serverBotModel, workOrderModel, response),configurations.getStringProperty(ConfigurationFilesConstants.HUB_METHOD_NAME));
				}
				break;
			case AppConstants.AUTO_SENSE:
				if(Boolean.TRUE.equals(workOrderModel.getWorkOrderAutoSenseEnabled())) {
						WoAutoTaskInfoModel woAutoTaskInfoModel=getWoAutoTaskInfoModel(serverBotModel, workOrderModel, response);
						Map<String,Object> rules = autoSenseService.getRulesByStepID(serverBotModel.getStepID(),workOrderModel.getFlowchartdefid());
						if(rules.containsKey(AppConstants.STOP)) {
							woAutoTaskInfoModel.setStopRule(true);
						}
						else {
							woAutoTaskInfoModel.setStopRule(false);
						}
						LOG.info(woAutoTaskInfoModel.toString());
						signalRModel=appService.returnSignalrConfiguration(woAutoTaskInfoModel,AppConstants.UPDATE_WO_STATUS_WEB_FW);
					}
				break;
		
		}
		return signalRModel;
	}

	public WoAutoTaskInfoModel getWoAutoTaskInfoModel(ServerBotModel serverBotModel, WorkOrderModel workOrderModel,Map<String, Object> response){
		
		String hour = wOExecutionDAO.getBookingHoursForStep(serverBotModel.getStepID(), serverBotModel.getwOID());
		return new WoAutoTaskInfoModel(serverBotModel.getSignumID(),serverBotModel.getwOID(),
					(int) response.get("bookingID"),serverBotModel.getStepID(),serverBotModel.getTaskID(), hour,AppConstants.STARTED, serverBotModel.getReason(),
					workOrderModel.getFlowchartdefid(), serverBotModel.getExecutionType(), response.get("flowChartType").toString(), serverBotModel.getRefferer(), AppConstants.START);
	}
	
	@Transactional("transactionManager")
	private void validateStatus(ServerBotModel serverBotModel) {
		WorkOrderModel workOrderModel=workOrderPlanService.getWorkOrderDetailsById(serverBotModel.getwOID());
		if(workOrderModel.getStatus().equalsIgnoreCase(AppConstants.WO_STATUS_REJECTED) ) {
			throw new ApplicationException(500, "workorder Id" + serverBotModel.getwOID()+"in rejected state");
		}else if(workOrderModel.getActive()==false) {
			throw new ApplicationException(500, "workorder Id" + serverBotModel.getwOID()+"in inactive state.");
		}
		else if(workOrderModel.getStatus().equalsIgnoreCase(AppConstants.CLOSED) ) {
			throw new ApplicationException(500, "workorder Id" + serverBotModel.getwOID()+"in closed state.");
		}
		else if(workOrderModel.getStatus().equalsIgnoreCase(AppConstants.WO_STATUS_DEFERRED) ) {
			throw new ApplicationException(500, "workorder Id" + serverBotModel.getwOID()+"in deffered state.");
		}
		
	}

	@Transactional("transactionManager")
	private void validateServerBotModel(ServerBotModel serverBotModel) {
		validationUtilityService.validateServerBotModel(serverBotModel);

	}

	@Transactional("transactionManager")
	private void validateServerBotModelForStartTask(ServerBotModel serverBotModel, WorkOrderModel workOrderModel) throws Exception {

		validationUtilityService.validateStringForBlank(serverBotModel.getSignumID(), AppConstants.SIGNUM_ID);
		
		validationUtilityService.validateIfWorkOrderAssignedToSignum(serverBotModel.getSignumID(), workOrderModel);
		
		validationUtilityService.validateWOIDForClosedDeferredAndRejectedStatus(workOrderModel);
		

		if(!StringUtils.equalsIgnoreCase(serverBotModel.getRefferer(),AppConstants.SOFT_HUMAN)) {
			if (wOExecutionDAO.checkStartedTaskInBooking(serverBotModel.getwOID())) {
				throw new ApplicationException(500, 
						new StringBuilder().append(AppConstants.STEP_ALREADY_STARTED_ERROR_STRING).append(serverBotModel.getwOID()).toString());
			}
			validationUtilityService.validateParallelWorkOrderExecution(serverBotModel, workOrderModel);
		}

		if(StringUtils.equalsIgnoreCase(serverBotModel.getRefferer(),AppConstants.AUTO_SENSE)) {
			validationUtilityService.validateTaskIDForStartTask(serverBotModel, workOrderModel);
			validationUtilityService.validateIFPreviousStepCompleted(serverBotModel, workOrderModel);
		}
		
		if(workOrderModel.getStatus().equalsIgnoreCase(AppConstants.WO_STATUS_ASSIGNED) 
				&& (StringUtils.equalsIgnoreCase(serverBotModel.getRefferer(), AppConstants.UI)
				|| StringUtils.equalsIgnoreCase(serverBotModel.getRefferer(), AppConstants.DESKTOP))) {
			validationUtilityService.validateIntForZero(serverBotModel.getProficiencyID(), "Proficiency ID");
		}
	}


	@Transactional("transactionManager")
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

	public BookingDetailsModel getBookingDetailsByBookingId(int bookingId) {
		return wOExecutionDAO.getBookingDetailsByBookingId(bookingId);
	}

	public WorkFlowBookingDetailsModel stopWOMaxBookingID(ServerBotModel serverBotModel) {

		return wOExecutionDAO.stopWOMaxBookingID(serverBotModel.getwOID(), serverBotModel.getTaskID());
	}

	public Response<Void> getAlreadyStartedBookingsByWoID(int wOID) {

		Response<Void> response = new Response<Void>();
		if (CollectionUtils.isNotEmpty(wOExecutionDAO.getAlreadyStartedBookingsByWoID(wOID))) {
			response.addFormError("Please complete ongoing task to mark Work Order On Hold or Deferred!");
		} else {
			response.addFormMessage("There is no step in Started state");
		}
		return response;
	}

	public String getStepNameByBookingID(int bookingID) {

		return wOExecutionDAO.getStepNameByBookingID(bookingID);
	}

	public ResponseEntity<Response<WorkFlowBookingDetailsModel>> stopWOMaxBookingIDNew(ServerBotModel serverBotModel) {
		Response<WorkFlowBookingDetailsModel> response = new Response<>();
		try {
			response.setResponseData(wOExecutionDAO.stopWOMaxBookingIDNew(serverBotModel.getwOID(),
					serverBotModel.getTaskID(), serverBotModel.getStepID()));
			LOG.info("stopWOMaxBookingIDNew : Success");
		} catch (ApplicationException e) {
			LOG.info(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<WorkFlowBookingDetailsModel>>(response, HttpStatus.OK);
		} catch (Exception ex) {
			LOG.info(ex.getMessage());
			response.addFormError(ex.getMessage());
			return new ResponseEntity<Response<WorkFlowBookingDetailsModel>>(response,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response<WorkFlowBookingDetailsModel>>(response, HttpStatus.OK);
	}

	@Transactional("transactionManager")
	public Response<Void> updateDeliverableOrderNodes(WorkOrderNetworkElementModel workOrderNetworkElementModel) {
		Response<Void> response = new Response<>();
		if (workOrderNetworkElementModel == null) {
			response.addFormError("Invalid input... Model cannot be Empty!!!");

		} else {

			try {
				List<DOWOModel> woListByDoid = workOrderPlanService
						.getWorkOrdersByDoid(workOrderNetworkElementModel.getDoid());
				if (CollectionUtils.isNotEmpty(woListByDoid)) {
					for (DOWOModel wo : woListByDoid) {
						if (StringUtils.isNotBlank(workOrderNetworkElementModel.getTablename())) {

							wOExecutionDAO.insertWorkOrderNetworkElement(workOrderNetworkElementModel.getTablename(),
									wo.getWoid(), workOrderNetworkElementModel.getSignum());

							wOExecutionDAO.insertWorkOrderNECount(wo.getWoid(), workOrderNetworkElementModel.getCount(),
									workOrderNetworkElementModel.getNeTextName(),
									workOrderNetworkElementModel.getSignum());
							response.addFormMessage("Nodes updated for WOID - " + wo.getWoid());

						}
					}
				}
			} catch (Exception e) {
				response.addFormError(e.getMessage());

			}

		}
		return response;
	}

	public ResponseEntity<Response<List<DeliverableSuccessReasonModel>>> getWorkOrderSuccessReasons(int workOrderID) {
		Response<List<DeliverableSuccessReasonModel>> workOrderSuccessReason=new Response<>();
		try {
		workOrderSuccessReason.setResponseData(wOExecutionDAO.getWorkOrderSuccessReasons(workOrderID));
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			workOrderSuccessReason.addFormError(e.getMessage());
			return new ResponseEntity<Response<List<DeliverableSuccessReasonModel>>>(workOrderSuccessReason, HttpStatus.OK);
		}
		catch(Exception ex) {
			workOrderSuccessReason.addFormError(ex.getMessage());
			return new ResponseEntity<Response<List<DeliverableSuccessReasonModel>>>(workOrderSuccessReason, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return  new ResponseEntity<Response<List<DeliverableSuccessReasonModel>>>(workOrderSuccessReason, HttpStatus.OK);
	}

	public Response<Void> addDeliverableSuccessReason(String signum,
			DeliverableSuccessReasonModel deliverableSuccessReasonModel) {
		Response<Void> response = new Response<Void>();
		try {
			// add new success reason
			if (deliverableSuccessReasonModel.getSuccessReasonId() == 0) {

				if (!validateSuccessReason(deliverableSuccessReasonModel)) {

					wOExecutionDAO.addDeliverableSuccessReason(signum, deliverableSuccessReasonModel);
					response.addFormMessage("Success Reason " + " added successfully for Deliverable unit ID - "
							+ deliverableSuccessReasonModel.getDeliverableUnitId());

				} else {
					response.addFormError("Duplicate Success reason for same Deliverable Unit ID not allowed ");
				}

			}
			// update success reason
			else {
				if (!validateSuccessReason(deliverableSuccessReasonModel)) {
					wOExecutionDAO.updateDeliverableSuccessReason(signum, deliverableSuccessReasonModel);
					response.addFormMessage("Success Reason " + " updated successfully for Deliverable unit ID - "
							+ deliverableSuccessReasonModel.getDeliverableUnitId());
				} else {
					response.addFormError(
							"Unable to Update the success reason : Duplicate reason not allowed for same deliverable unit");
				}

			}

		} catch (Exception e) {
			response.addFormError(e.getMessage());
		}

		return response;
	}

	public Response<Void> addStepDetailsForFlowChartByChildStep(StepDetailsModel stepDetailsModel) {
		
		Response<Void> apiResponse= new Response<>();
		try {
			validateAddStepDetailsForFlowChartByChildStep(stepDetailsModel);
			stepDetailsModel.setTaskID(0);
			stepDetailsModel.setBookingId(0);
			stepDetailsModel.setStatus(AppConstants.COMPLETED);
			stepDetailsModel.setExecutionType(AppConstants.MANUAL);
			wOExecutionDAO.addStepDetailsForFlowChart(stepDetailsModel);
	
			
			LOG.info(String.format("info addStepDetailsForFlowChartByChildStep signalR payload: %s", stepDetailsModel.toString()));
			SignalrModel signalrModel=appService.returnSignalrConfiguration(stepDetailsModel,
					AppConstants.DECSION_VALUE);
			appService.callSignalrApplicationToCallSignalRHub(signalrModel);
			LOG.info("addStepDetailsForFlowChartByChildStep :SUCCESS");
		}
		catch(Exception e) {
			apiResponse.addFormError(e.getMessage());
			LOG.error("Error in addStepDetailsForFlowChartByChildStep:"+e.getMessage());
		}
		return apiResponse;
	}
	
	private void validateAddStepDetailsForFlowChartByChildStep(StepDetailsModel stepDetailsModel) {
		validationUtilityService.validateIntForZero(stepDetailsModel.getWoId(),"Work Order ID");
		validationUtilityService.validateStringForBlank(stepDetailsModel.getSignumId(), "SignumID");
		validationUtilityService.validateStringForBlank(stepDetailsModel.getFlowChartStepId(), "flowChartStepId");
		StepDetailsModel decisionDetails=wOExecutionDAO.getDecsionDetailsByChildStep(stepDetailsModel.getFlowChartDefID(),
				stepDetailsModel.getFlowChartStepId());
		if(decisionDetails==null) {
			throw new ApplicationException(500, 
					String.format("%s does not exists for Flowchart %d",
							stepDetailsModel.getFlowChartStepId(),stepDetailsModel.getFlowChartDefID()));
		}
		stepDetailsModel.setFlowChartStepId(decisionDetails.getFlowChartStepId());
		stepDetailsModel.setDecisionValue(decisionDetails.getDecisionValue());
		
		
	}

	private boolean validateSuccessReason(DeliverableSuccessReasonModel deliverableSuccessReasonModel) {

		return wOExecutionDAO.validateSuccessReason(deliverableSuccessReasonModel);
	}

	public List<DeliverableSuccessReasonModel> getAllSuccessReasons(int deliverableUnitId) {

		return wOExecutionDAO.getAllSuccessReasons(deliverableUnitId);
	}

	public Response<Void> saveReasonStatus(String signum, boolean flag,
			DeliverableSuccessReasonModel deliverableSuccessReasonModel) {
		Response<Void> response = new Response<Void>();
		try {
			if (flag == true) {

				wOExecutionDAO.saveReasonStatusActive(signum, deliverableSuccessReasonModel);
				response.addFormMessage("Reason status Enabled ");
			} else {
				wOExecutionDAO.saveReasonStatus(signum, deliverableSuccessReasonModel);
				response.addFormMessage("Reason status Disabled ");
			}

		} catch (Exception e) {
			response.addFormError(e.getMessage());
		}

		return response;

	}
	
	public EventPublisherURLMappingModel getEventPublisherURLMappingModel(String sourceName, String category) {

		return wOExecutionDAO.getEventPublisherURLMappingModel(sourceName, category);
	}
	private ResponseEntity<Response<Map<String, Object>>> checkWorkOrderWorkFlowMethod(int wOID, int proficiencyID, String signum) 
			throws Exception {
		Response<Map<String, Object>> response=new Response<>();
		boolean checkStatus = wOExecutionDAO.checkIFWOIDExists(wOID);
		boolean validateProficiencyID = validationUtilityService.validateProficiencyID(proficiencyID);
		HashMap<String, Object> data = new HashMap<>();
		WOWorkFlowModel workFlow = new WOWorkFlowModel();
		
		// get ProficiencyID from the table using viewMode
		ProficiencyTypeModal expType = appService.getProficiencyID(AppConstants.EXPERIENCED);
		ProficiencyTypeModal assType = appService.getProficiencyID(AppConstants.ASSESSED);
		
		try {
			if (checkStatus && validateProficiencyID) {
				
				workFlow = wOExecutionDAO.getWOWorkFlow(wOID);
				workFlow.setUploadedJSON(checkStatus);
				int subActivityDefID = workFlow.getSubActivityDefID();
				List<WorkflowStepDetailModel> workflowStepDetailsForComments  = new ArrayList<>();
				String woStatus = workFlow.getStatus();
				Date actualStartDate = workFlow.getActualStartDate();
				
				//Integer srID = workOrderPlanDao.checkWorkOrderLinkedWithSRID(wOID);
				//workFlow.setSrID(srID);
				
				List<WorkFlowStepBookingDetailsModel> stepData = wOExecutionDAO.getWorkFlowStepBookingDetails(wOID);
				
				JSONObject obj = new JSONObject(workFlow.getFlowChartJSON());
				JSONArray cells = (JSONArray) obj.get(AppConstants.CELLS);
				long startTime = System.currentTimeMillis();
	
				String effort = StringUtils.EMPTY;
				String bookingStatus = StringUtils.EMPTY;
				String bookedID = StringUtils.EMPTY;
				String colorCode;
				String outputLink = StringUtils.EMPTY;
				String reason = StringUtils.EMPTY;
				String bookingType = StringUtils.EMPTY;
				String decisionValue;
				String toolID = AppConstants.TWO_AS_A_STRING;
				String toolName = AppConstants.NO_TOOL;
				String rpaID = ZERO_STRING;
				String isRunOnServer = ZERO_STRING;
				boolean isInputRequired = false;
				String attrType = null;
				String boxType = null;
				String outputUpload = StringUtils.EMPTY;
				String stepColor = StringUtils.EMPTY;
				
				for (int i = 0; i < cells.length(); i++) {
				
					JSONObject types = (JSONObject) cells.get(i);
					String stepID = (String) types.get(ID);
					JSONObject attrs = (JSONObject) types.get(ATTRS);
					String viewMode = StringUtils.EMPTY;
					boolean isValidForGreyCode = false;
					
					
					WorkflowStepDetailModel workFlowStepDetail= new WorkflowStepDetailModel();
					workFlowStepDetail.setStepID(stepID);
					
					// sad counts
					String sadCount=ZERO_STRING;
					if(!AppConstants.APP_LINK.equalsIgnoreCase((String) types.get(AppConstants.TYPE))
							&& !AppConstants.START_TYPE.equalsIgnoreCase((String) types.get(AppConstants.TYPE))
							&& !AppConstants.END_TYPE.equalsIgnoreCase((String) types.get(AppConstants.TYPE))) {
						 sadCount = wOExecutionDAO.getSadCountForStep(stepID, wOID,signum, subActivityDefID);
						if(StringUtils.isEmpty(sadCount)  || sadCount==null) {
							sadCount=ZERO_STRING;
						}
					}
					types.put(AppConstants.SAD_COUNT, sadCount);
					// check for Experience
					if(proficiencyID == expType.getProficiencyID()) {
						// check for ViewMode in flowchartJson
						if(types.has(AppConstants.VIEW_MODE)) {
							viewMode = (String) types.get(AppConstants.VIEW_MODE);
						}else {
							// OLD WORKFLOWS
							// status :: InProgress,Closed, Deffered, Rejected
							if(!woStatus.equalsIgnoreCase(AppConstants.WO_STATUS_ASSIGNED)) {
								viewMode = AppConstants.EXPERIENCED;
								
								// FRESH WO : ONHOLD or REOPENDED or DEFERRED without Start
								if(woStatus.equalsIgnoreCase("ONHOLD") || woStatus.equalsIgnoreCase("REOPENED") && actualStartDate == null) {
									viewMode = AppConstants.ASSESSED;
								}
								
								if(woStatus.equalsIgnoreCase("INPROGRESS") ||actualStartDate != null) {
									viewMode = AppConstants.ASSESSED;
								}
								
//								// Closed without Start -> no Entry in Booking Detail Table
//								if(woStatus.equalsIgnoreCase("CLOSED") && stepData.size() == 0) {
//									viewMode = AppConstants.ASSESSED;
//								}
								
							} else {
							// status :: Assigned
								viewMode = AppConstants.ASSESSED;
							}
						}
						// check for grey color.
						if (AppConstants.ERICSSON_MANUAL.equalsIgnoreCase((String) types.get(AppConstants.TYPE))
								&& validateGreyColor(viewMode, proficiencyID, expType, assType)) {
							stepColor = AppConstants.MANUAL_DISABLED_COLOR_CODE;
							JSONObject headerColorChange = (JSONObject) attrs.get("header");
							headerColorChange.put(AppConstants.FILL, stepColor);
							headerColorChange.put(AppConstants.STROKE, stepColor);

							JSONObject footerColorChange = (JSONObject) attrs.get("footer");
							footerColorChange.put(AppConstants.FILL, stepColor);
							footerColorChange.put(AppConstants.STROKE, stepColor);

							JSONObject bodyColorChange = (JSONObject) attrs.get("body");
							bodyColorChange.put(AppConstants.STROKE, stepColor);
						}
					}
					
					
                    if (attrs.has(AppConstants.TASK)) {
	 					JSONObject task1 = (JSONObject) attrs.get(AppConstants.TASK);
	 					workFlowStepDetail.setTaskName(task1.get(AppConstants.TASK_NAME).toString());
	 				}

					
					String executionType=StringUtils.EMPTY;
					if (!AppConstants.APP_LINK.equalsIgnoreCase((String) types.get(AppConstants.TYPE))
							&& !AppConstants.START_TYPE.equalsIgnoreCase((String) types.get(AppConstants.TYPE))
							&& !AppConstants.END_TYPE.equalsIgnoreCase((String) types.get(AppConstants.TYPE))) {
						
							if(AppConstants.ERICSSON_MANUAL.equalsIgnoreCase((String) types.get(AppConstants.TYPE))){
			                    executionType= (String) types.get(AppConstants.EXECUTIONTYPE);
			                    workFlowStepDetail.setExecutionType(executionType);
			                    workFlowStepDetail.setStepName((String)  types.get(AppConstants.NAME));
			                }
			                else if(AppConstants.ERICSSON_AUTOMATIC.equalsIgnoreCase((String) types.get(AppConstants.TYPE))){
		                        executionType= (String) types.get(AppConstants.EXEC_TYPE);
		                        workFlowStepDetail.setExecutionType(executionType);
		                        workFlowStepDetail.setStepName((String)  types.get(AppConstants.NAME));		                        
		                    }							
						
						if (AppConstants.ERICSSON_MANUAL.equalsIgnoreCase((String) types.get(AppConstants.TYPE))
								|| AppConstants.ERICSSON_AUTOMATIC.equalsIgnoreCase((String) types.get(AppConstants.TYPE))) {
							attrType = AppConstants.BODY_TEXT;
							boxType = AppConstants.BODY;
						} else if (AppConstants.ERICSSON_DECISION.equalsIgnoreCase((String) types.get(AppConstants.TYPE))) {
							attrType = AppConstants.LABEL;
						} else if (AppConstants.ERICSSON_STEP.equalsIgnoreCase((String) types.get(AppConstants.TYPE))
								|| AppConstants.ERICSSON_WEAK_ENTITY.equalsIgnoreCase((String) types.get(AppConstants.TYPE))
								|| AppConstants.ERD_RELATIONSHIPS.equalsIgnoreCase((String) types.get(AppConstants.TYPE))) {
							attrType = AppConstants.TEXT;
							boxType = AppConstants.RECT;
						}
						
						if (stepData != null && CollectionUtils.isNotEmpty(stepData)) {
							for (WorkFlowStepBookingDetailsModel stepModel : stepData) {
								bookedID = String.valueOf(stepModel.getBookingID());
								if(bookedID.equalsIgnoreCase(AppConstants.SMALL_NULL_AS_A_STRING)) {
							          bookedID=ZERO_STRING;
								}
								String hours=stepModel==null?ZERO_STRING:String.valueOf(stepModel.getEffort());
								if (stepID.equals(stepModel.getFlowChartStepId())) {
									 decisionValue = String.valueOf(stepModel.getDecisionValue());
									
									if (decisionValue.equalsIgnoreCase(AppConstants.NA) || StringUtils.isBlank(decisionValue)) {
										effort = hours;
										WorkFlowStepDataModel stepValues = wOExecutionDAO.getWFStepData(stepID,
												Integer.parseInt(stepModel.getFlowChartDefId()), executionType);
										if (stepValues != null) {
											toolID=String.valueOf(stepValues.getToolId());
											toolName=String.valueOf(stepValues.getToolName());
											rpaID=String.valueOf(stepValues.getRpaID());
											isRunOnServer=String.valueOf(stepValues.getIsRunOnServer());
											isInputRequired=stepValues.isInputRequired();	
											bookedID = String.valueOf(stepModel.getBookingID());
											bookingStatus = String.valueOf(stepModel.getStatus());
											outputLink = String.valueOf(stepModel.getOutputLink());
											reason = String.valueOf(stepModel.getReason());
											bookingType = String.valueOf(stepModel.getType());
											outputUpload=String.valueOf(stepValues.getOutputUpload());
										}
										types.put(AppConstants.OUTPUT_UPLOAD, outputUpload);
										if (!AppConstants.ERICSSON_DECISION.equalsIgnoreCase((String) types.get(AppConstants.TYPE))) {
											if (AppConstants.STARTED.equalsIgnoreCase(bookingStatus)) {
												colorCode = AppConstants.STARTED_COLOR_CODE;
											} else if (AppConstants.ONHOLD.equalsIgnoreCase(bookingStatus)) {
												colorCode = AppConstants.ONHOLD_COLOR_CODE;
											} else if (AppConstants.SKIPPED.equalsIgnoreCase(bookingStatus)) {
												colorCode = AppConstants.SKIPPED_COLOR_CODE;
											} else if (AppConstants.WO_STATUS_DEFERRED.equalsIgnoreCase(bookingStatus)) {
												colorCode = AppConstants.DEFERRED_COLOR_CODE;
											} else {
												colorCode = AppConstants.OTHER_COLOR_CODE;
											}
											if (attrs.has(AppConstants.TASK)) {
												JSONObject task = (JSONObject) attrs.get(AppConstants.TASK);
												Integer taskid = Integer
														.parseInt(task.get(AppConstants.TASKID).toString());
												if (task.has(AppConstants.TASKID)
														&& taskid == Integer.parseInt(stepModel.getTaskID())) {

													task.put(AppConstants.BOOKINGID, bookedID);
													task.put(AppConstants.REASON_SMALL, reason);
													task.put(AppConstants.BOOKING_TYPE, bookingType);
													task.put(AppConstants.STATUS_STRING, bookingStatus);
													task.put(AppConstants.OUTPUTLINK, outputLink);
													task.put(AppConstants.TOOLID, toolID);
													task.put(AppConstants.TOOLNAME, toolName);
													task.put(AppConstants.HOURS, effort);
												}

											}
											if (attrs.has(AppConstants.TOOL)) {
												JSONObject tool = (JSONObject) attrs.get(AppConstants.TOOL);
												tool.put(AppConstants.RPAID, rpaID);
												tool.put(AppConstants.IS_RUN_ON_SERVER, isRunOnServer);
												tool.put(AppConstants.IS_INPUT_REQUIRED, isInputRequired);
											}
	
												if (AppConstants.ERICSSON_AUTOMATIC
														.equalsIgnoreCase((String) types.get(AppConstants.TYPE))) {
													String rpa = (String) types.get(AppConstants.RPAID_SMALL);
													if (rpa.contains(AppConstants.REG_ZERO_AT)) {
														types.put(AppConstants.RPAID_SMALL, (new StringBuilder())
																.append(rpaID).append(AppConstants.CHAR_AT).append(AppConstants.RPA).toString());
													}
													if (stepModel != null && StringUtils.isNotBlank(stepModel.getReferenceid())) {
														types.put(AppConstants.ISF_REF_ID, stepModel.getReferenceid());
													}
													if (AppConstants.COMPLETED.equalsIgnoreCase(bookingStatus)
															&& !reason.equalsIgnoreCase(AppConstants.SUCCESS_CAPS)) {
														colorCode = AppConstants.COMPLETED_COLOR_CODE;
													}
												}
	
											JSONObject colorChange = (JSONObject) attrs.get(boxType);
											colorChange.put(AppConstants.FILL, colorCode);
										}
									}
									JSONObject text = (JSONObject) attrs.get(attrType);
									String textName = text.getString(AppConstants.TEXT);
										int index = 0;
										if (textName.contains(AppConstants.AVG_ESTD_EFFORT)) {
											textName = textName.substring(0, textName.indexOf("\n\nAvgEstdEffort"));
											
										}
										if (textName.contains(AppConstants.TOOL_NAME_COLON)) {
											index = textName.indexOf(AppConstants.TOOL_NAME_COLON);
											textName = textName.substring(0, index);
											textName = (new StringBuilder()).append(textName).append(AppConstants.TOOL_NAME_COLON).append(toolName).toString();
											
										} else if (textName.contains(AppConstants.TOOL_NAME_COLON)) {
											index = textName.indexOf(AppConstants.TOOL_NAME_COLON);
											textName = textName.substring(0, index);
											textName = (new StringBuilder()).append(textName).append(AppConstants.TOOL_NAME_COLON).append(toolName).toString();
											
										}
									if (!decisionValue.equals(StringUtils.EMPTY)
											&& !decisionValue.equals(AppConstants.NA)) {
										textName = (new StringBuilder()).append(textName).append("\nSelected:").append(decisionValue).toString();
										
									} else {
										if(!isValidForGreyCode)
											textName = (new StringBuilder()).append(textName).append("\n\nEffort:").append(effort).toString();
										
									}
									text.remove(attrType);
									text.put(AppConstants.TEXT, textName);
									
								} else {
										updateJSON(types, stepID, subActivityDefID, attrType,sadCount,executionType);
								}
							}
						} else {
							updateJSON(types, stepID, subActivityDefID, attrType, sadCount, executionType);
						}
					}
					if(StringUtils.isNotEmpty(workFlowStepDetail.getStepName())) {
					workflowStepDetailsForComments.add(workFlowStepDetail);
					}
					long elapsedTime = System.currentTimeMillis() - startTime;
					if(elapsedTime>20000) {
						throw new ApplicationException(HttpStatus.REQUEST_TIMEOUT.value(),"The server is taking too long to respond, please try again in sometime!");
					 }
					}
				workFlow.setFlowChartJSON(obj.toString());
				workFlow.setDisabledStepDetails(wOExecutionDAO.getDisabledStepDetails(subActivityDefID,wOID));
				workFlow.setWorkFlowSteps(workflowStepDetailsForComments);
				data.put(AppConstants.SUCCESS, workFlow);
				response.setResponseData(data);
			} else {
				throw new ApplicationException(200, "Invalid WOID");
				
			}
		}catch (ApplicationException e) {
		LOG.error(e.getMessage());
		response.addFormError(e.getMessage());
		return new ResponseEntity<Response<Map<String, Object>>>(response, HttpStatus.OK);
	}catch (Exception e) {
		LOG.error(e.getMessage());
		response.addFormError(e.getMessage());
		return new ResponseEntity<Response<Map<String, Object>>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}		
	return new ResponseEntity<Response<Map<String, Object>>>(response, HttpStatus.OK);	
	}

	public String getJsonValue(int flowChartDefID) {
		return wOExecutionDAO.getJsonValue(flowChartDefID);
	}

	public Response<WorkOrderMarketModel> getMarkets(WorkOrderColumnMappingModel workOrderColumnMappingModel) {
		
		Response<WorkOrderMarketModel> response = new Response<>();
		try {
			validateGetMarkets(workOrderColumnMappingModel);
			
			WorkOrderMarketModel workOrderMarketModel= new WorkOrderMarketModel();
			workOrderMarketModel.setwOID(workOrderColumnMappingModel.getwOID());
			workOrderMarketModel.setMarkets(wOExecutionDAO.getMarkets(workOrderColumnMappingModel));
			response.setResponseData(workOrderMarketModel);
			LOG.info(SUCCESS_GET_MARKETS+workOrderColumnMappingModel.getwOID());
}
		catch(Exception e) {
			response.addFormError(e.getMessage());
			LOG.info(ERROR_GET_MARKETS+e.getMessage());
			e.printStackTrace();
		}
		
		return response;
	}

	private void validateGetMarkets(WorkOrderColumnMappingModel workOrderColumnMappingModel) {
		validationUtilityService.validateIntForZero(workOrderColumnMappingModel.getwOID(),WO_ID);
		validationUtilityService.validateIntForZero(workOrderColumnMappingModel.getProjectID(), AppConstants.PROJECT_ID_2);
		if(workOrderColumnMappingModel.getNodeNames()==null || workOrderColumnMappingModel.getNodeNames().isEmpty()) {
			throw new ApplicationException(200, String.format(AppConstants.PLEASE_PROVIDE, "nodeNames"));
		}
		
	}
	
	public Response<List<WorkOrderMarketModel>> getMarketsByWorkOrders(List<WorkOrderColumnMappingModel> workOrders) {

		Response<List<WorkOrderMarketModel>> response = new Response<>();
		
		try {
			List<WorkOrderMarketModel> workOrderMarketModels= new LinkedList<>();
			WorkOrderMarketModel workOrderMarket;
			for(WorkOrderColumnMappingModel workOrderColumnMappingModel:workOrders) {
				try {
					validateGetMarkets(workOrderColumnMappingModel);
				}
				catch (Exception e) {
					response.addFormWarning(e.getMessage()+" for WOID: "+workOrderColumnMappingModel.getwOID());
					LOG.info(ERROR_GET_MARKETS + e.getMessage()+" for WOID: "+workOrderColumnMappingModel.getwOID());
					continue;
				}
				workOrderMarket= new WorkOrderMarketModel();
				workOrderMarket.setwOID(workOrderColumnMappingModel.getwOID());
				int countryCustomerGroupId=appService.getCountryCustomerGroupIDByProjectID(workOrderColumnMappingModel.getProjectID());
				workOrderMarket.setMarkets(wOExecutionDAO.getMarketsByWorkOrders(workOrderColumnMappingModel,countryCustomerGroupId));
				LOG.info(SUCCESS_GET_MARKETS + workOrderColumnMappingModel.getwOID());
				workOrderMarketModels.add(workOrderMarket);
			}
			response.setResponseData(workOrderMarketModels);
			workOrderMarketModels=null;
		}
		catch (Exception e) {
			response.addFormError(e.getMessage());
			LOG.info(ERROR_GET_MARKETS + e.getMessage());
		}
		return response;
	}

	public ResponseEntity<Response<LinkedList<WorkOrderProgressModel>>> getWorkOrderDetailsBySignum(String signumID, String status, String startDate, String endDate) {
		Response<LinkedList<WorkOrderProgressModel>> result = new Response<>();
		try {
		validationUtilityService.validateStringForBlank(signumID,AppConstants.SIGNUM);
		validationUtilityService.validateStringForBlank(status,AppConstants.STATUS);
		String[] s = status.split(",");
		String newStatus = Stream.of(s).collect(Collectors.joining("','", "'", "'"));
		LinkedList<WorkOrderProgressModel> workOrders = wOExecutionDAO.getWorkOrderDetailsBySignum(signumID, newStatus);
		if (workOrders.isEmpty()) {
			result.addFormError("Data Not Found For Selected Signum");
			result.setResponseData(workOrders);
		} else {
			result.setResponseData(workOrders);
		}
		}
		catch(ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<Response<LinkedList<WorkOrderProgressModel>>>(result, HttpStatus.OK);
		}
		catch(Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<Response<LinkedList<WorkOrderProgressModel>>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response<LinkedList<WorkOrderProgressModel>>>(result, HttpStatus.OK);
	}

	public Response<UserWorkFlowProficencyModel> getWFUserProficiency(String signumID, int subactivityID, int wfID, int projectID, int defID) {
		Response<UserWorkFlowProficencyModel> response = new Response<>();
		try {

			validationUtilityService.validateSignumForExistingEmployee(signumID);
			UserWorkFlowProficencyModel userWorkFlowProficencyModel	=	wOExecutionDAO.getWFUserProficiency(signumID, subactivityID,
					wfID, projectID, defID);
//			ProficiencyTypeModal proficiencyDetails	=	wOExecutionDAO.getProficiencyDetailsByName(AppConstants.EXPERIENCED);

//			if(!StringUtils.equalsIgnoreCase(userWorkFlowProficencyModel.getProficiencyName(), AppConstants.ASSESSED)) {
//				response.setResponseData(userWorkFlowProficencyModel);
//			}else if(StringUtils.equalsIgnoreCase(userWorkFlowProficencyModel.getProficiencyName(), AppConstants.ASSESSED) && 
//					userWorkFlowProficencyModel.getKpiValue()==0) {
//				userWorkFlowProficencyModel.setProficiencyName(proficiencyDetails.getProficiencyName());
//				userWorkFlowProficencyModel.setProficiencyLevel(String.valueOf(proficiencyDetails.getProficiencyLevel()));
//				userWorkFlowProficencyModel.setDisplayedMode(proficiencyDetails.getProficiencyID());
//				response.setResponseData(userWorkFlowProficencyModel);
//			}else {
				response.setResponseData(userWorkFlowProficencyModel);
//			}
			response.addFormMessage(AppConstants.SUCCESS);
			LOG.info(AppConstants.SUCCESS);
		}catch (Exception e) {
			e.printStackTrace();
			response.addFormError(e.getMessage());
			LOG.info(ERROR_GET_PROFICIENCY + e.getMessage());
			response.addFormMessage(AppConstants.FAILURE);
		}
		return response;
	}

	public boolean validateGreyColor(String viewMode, int profiencyID,ProficiencyTypeModal expType, ProficiencyTypeModal assType) {
		int stepProfiencyLevel = 0;
		int woProfiencyLevel = 0;
		
		List<String> viewModeItems = new ArrayList<String>(Arrays.asList(viewMode.split(",")));
		
		// get woProficiencyLevel
		if(profiencyID == assType.getProficiencyID()) {
			woProfiencyLevel = assType.getProficiencyLevel();
		} else if (profiencyID == expType.getProficiencyID()) {
			woProfiencyLevel = expType.getProficiencyLevel();
		}
		
		// get stepProfiencyLevel
		stepProfiencyLevel = viewModeItems.contains(AppConstants.EXPERIENCED) ? expType.getProficiencyLevel() : assType.getProficiencyLevel();

		return (stepProfiencyLevel < woProfiencyLevel);
	}
	
	

	public Response<DescisionStepDetailsModel> getDecisionStepDetails(String stepID, Integer defID, Integer woID) {

		Response<DescisionStepDetailsModel> response = new Response<>();
		try {
			DescisionStepDetailsModel descisionStepDetailsModel = new DescisionStepDetailsModel();
//			for(NextStepModel nsp:nextStepModel) {
//				if(nsp.getNextStepType().equalsIgnoreCase(AppConstants.ERICSSON_DECISION)) {
					List<ChildStepDetailsModel> descisionChildStepDetails =	wOExecutionDAO.getDescisionStepDetails(stepID, defID, woID);
					
					StepTaskModel currentStepDetails= wOExecutionDAO.getCurrentStepDetails(stepID, defID);

					for (ChildStepDetailsModel ch :descisionChildStepDetails) {
						if(ch.isStartRule() || ch.isStopRule()){
							ch.setIsStepAutoSenseEnabled(true);
						}else {
							ch.setIsStepAutoSenseEnabled(false);
						}
					}
					descisionStepDetailsModel.setChildStepDetailsModelList(descisionChildStepDetails);
					descisionStepDetailsModel.setDecisionStepName(currentStepDetails.getStepName());
					descisionStepDetailsModel.setSetDecisionStepId(currentStepDetails.getStepID());
					response.setResponseData(descisionStepDetailsModel);
//				}
//			}
		}catch (Exception e) {
			response.addFormError(e.getMessage());
		}
		return response;
	}

	public Response<Void> saveWFUserProficiency(WorkFlowUserProficiencyModel workFlowUserProficiencyModel) {
		Response<Void> response = new Response<Void>();
		try {
			List<SubActivityWfIDModel> subActivityWfIDModel = workFlowUserProficiencyModel.getSubActivityWfIDModel();

			for (SubActivityWfIDModel s : subActivityWfIDModel) {
				int defID = wOExecutionDAO.getSubActivityDefIDForWOID(s.getWoID());
				SaveWfUserProfResponseModel res = wOExecutionDAO.saveWFUserProficiency(s.getSignumID(),
						workFlowUserProficiencyModel, s.getSubactivityID(), s.getWfID(), s.getWoID());
				if (res != null && res.getFlag() != 0) {

					if (configurations.getBooleanProperty(ConfigurationFilesConstants.WORKORDER_MAIL_ENABLED, false) 
							&& !StringUtils.equals(AppConstants.SAVE_PROFICIENCY_WITHOUT_TRANSITION, res.getMsg())) {
						if (StringUtils.contains(res.getMsg(),
								workFlowUserProficiencyModel.getProficiencyMeasurement())) {
							UserWorkFlowProficencyModel userWorkFlowProficencyModel = wOExecutionDAO.getWFUserProficiency(
									s.getSignumID(), s.getSubactivityID(), s.getWfID(),
									workFlowUserProficiencyModel.getProjectID(), defID);
							userWorkFlowProficencyModel.setSignumID(s.getSignumID());
							userWorkFlowProficencyModel.setModifiedBy(workFlowUserProficiencyModel.getCreatedBy());
							userWorkFlowProficencyModel.setSubActivityID(s.getSubactivityID());
							userWorkFlowProficencyModel.setProjectID(workFlowUserProficiencyModel.getProjectID());
							userWorkFlowProficencyModel.setWfId(s.getWfID());
							userWorkFlowProficencyModel.setSubActivityID(s.getSubactivityID());
							userWorkFlowProficencyModel.setWoId(s.getWoID());
							
							Map<String, Object> placeHolder = emailService.enrichMailforSaveProficiency(
									userWorkFlowProficencyModel,
									workFlowUserProficiencyModel.getProficiencyMeasurement(),
									workFlowUserProficiencyModel.getCreatedBy());
							emailService.sendMail(AppConstants.NOTIFICATION_SAVE_PROFICIENCY, placeHolder);
						}
					}
					response.addFormMessage(res.getMsg());
				} else {
					response.addFormMessage(res.getMsg());
				}
			}
			LOG.info(AppConstants.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
		}
		return response;
	}
	public ResponseEntity<Response<List<WorkflowStepDetailModel>>> getAllStepsByWOID(int woID){
		Response<List<WorkflowStepDetailModel>> response= new Response<>();	
		try {
			response.setResponseData(wOExecutionDAO.getAllStepsByWOID(woID ));
			}
		
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<List<WorkflowStepDetailModel>>>(response,HttpStatus.OK);
		}
		catch(Exception ex) {
			LOG.error(ex.getMessage());
			response.addFormError(ex.getMessage());
			return new ResponseEntity<Response<List<WorkflowStepDetailModel>>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Response<List<WorkflowStepDetailModel>>>(response,HttpStatus.OK);
	}

	public ResponseEntity<Response<Void>> executeDecisionStep(StepDetailsModel stepDetailsModel) {
		Response<Void> response = new Response<>();
		try {
			if(stepDetailsModel.getTaskID() == 0 && !StringUtils.equalsIgnoreCase(stepDetailsModel.getExecutionType(), AppConstants.MANUAL_DISABLED)){
				stepDetailsModel.setTaskID(0);
				stepDetailsModel.setBookingId(0);
				stepDetailsModel.setStatus(AppConstants.COMPLETED);
				this.wOExecutionDAO.addStepDetailsForFlowChart(stepDetailsModel);
			}
			LOG.info("executeDecisionStep :SUCCESS");
		} catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<Void>>(response, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<Void>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.addFormMessage("Step Added Successfully !!");
		return new ResponseEntity<Response<Void>>(response, HttpStatus.OK);
		
	}

	public DownloadTemplateModel downloadWorkHistoryData(String signum, String startDate, String endDate, String woStatus) throws IOException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = "WorkHistoryDataExport" + "-" + (new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()))
				+ ".xlsx";

		List<Map<String, Object>> workhistoryData = wOExecutionDAO.downloadWorkHistoryData(
				signum,startDate,endDate,woStatus);
		byte[] fData = null;
		if (CollectionUtils.isNotEmpty(workhistoryData)) {
			fData = FileUtil.generateXlsFile(workhistoryData);
			
		}
		
		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);
		

		return downloadTemplateModel;
	}
	
	public ResponseEntity<Response<List<WorkOrderNodesModel>>> getListOfNode(int wOID) {
		Response<List<WorkOrderNodesModel>> response = new Response<>();
 
	   try {
		   LOG.info("getListOfNode:Start");
		   
		   validationUtilityService.validateIntForZero(wOID, wO_ID);
		   
			List<WorkOrderNodesModel> listofNode= wOExecutionDAO.getListOfNode(wOID);
			if (listofNode.isEmpty()||listofNode.contains(null)) {
				response.addFormMessage(NO_DATA_FOUND);
			} else {
				response.setResponseData(listofNode);
			}
			
			LOG.info("getListOfNode:End");
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

	public ResponseEntity<Response<Map<String, Object>>> isInstalledDesktopVersionUpdated(String signum) {
		Response<Map<String, Object>> response = new Response<>();
		Map<String, Object> map = new HashMap<String, Object>();
		 
		   try {
			   LOG.info("isInstalledDesktopVersionUpdated:Start");
			//   response.setResponseData(wOExecutionDAO.isVersionMatched(signum));
			   
			  Boolean result  = wOExecutionDAO.isVersionMatched(signum);
			  String versionNumber =  wOExecutionDAO.getLatestVersionNumber();
			   map.put("result", result );
			   map.put("VersionNumber", versionNumber);
			   response.setResponseData(map);
			LOG.info("isInstalledDesktopVersionUpdated:End");
				
			}
		   catch (ApplicationException e) {
				 LOG.error(e.getMessage());
				 response.addFormError(e.getMessage());
				 return new ResponseEntity<Response<Map<String, Object>>>(response , HttpStatus.OK);
			}catch (Exception e) {
				LOG.error(e.getMessage());
				response.addFormError(e.getMessage());
				return new ResponseEntity<Response<Map<String, Object>>>(response , HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<Response<Map<String, Object>>>(response , HttpStatus.OK);
	}
	
	private Boolean parseJsonToGetRPAID(String wFJson) throws Exception {

		JSONObject jObject = new JSONObject(wFJson);
		JSONArray arr = jObject.getJSONArray("cells");
		Boolean checkifPenguinOrMacro=false;
		for (int i = 0; i < arr.length(); i++) {
			String rpaID = null;
			JSONObject types = (JSONObject) arr.get(i);
			
			if("ericsson.Automatic".equalsIgnoreCase((String) types.get("type")) &&
					arr.getJSONObject(i).has("attrs") && arr.getJSONObject(i).getJSONObject("attrs").has("task")) {
					 
				rpaID = String.valueOf(arr.getJSONObject(i)
								.getJSONObject("attrs").getJSONObject("tool")
								.get("RPAID"));
				
			checkifPenguinOrMacro = this.flowChartDao.checkifPenguinOrMacro(rpaID);
			if(Boolean.TRUE.equals(checkifPenguinOrMacro)) {
				break;
			  }
			}
			
		  
		}
		return checkifPenguinOrMacro;
	}
	
	@Transactional("transactionManager")
	public ResponseEntity<Response<String>> getAssignedToSignumByWOID(int wOID) {
		
		Response<String> response = new Response<>();
 
	    try {
			LOG.info("getAssignedToSignumByWOID:Start");
			
			validationUtilityService.validateIntForZero(wOID, WO_ID);
			
			boolean isWOStatusValid=this.wOExecutionDAO.validateWOStatus(wOID);
			
			if(Boolean.FALSE.equals(isWOStatusValid)) {
				throw new ApplicationException(200,String.format(WO_STATUS_NOT_IN_ASSIGNED_OR_ONHOLD));
			}
			
			String assignedToSignumByWOID=null;
		    assignedToSignumByWOID=this.wOExecutionDAO.getAssignedToSignumByWOID(wOID);
			
			if (StringUtils.isEmpty(assignedToSignumByWOID)) {
				response.addFormMessage(NO_DATA_FOUND);
			} 
			else {
				response.addFormMessage(SUCCESS);
				response.setResponseData(assignedToSignumByWOID);
			}

			LOG.info("getAssignedToSignumByWOID:End");
			
		}
		catch(ApplicationException exe) {
			response.addFormWarning(exe.getMessage());
			response.setValidationFailed(true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<Response<Integer>> getProficiencyId(String wOID, String signum) {

		Response<Integer> response = new Response<>();
	    try {
			LOG.info("getProficiencyId : Start");
			if(StringUtils.equalsIgnoreCase(signum,"null"))
			{
				signum="";
			}
			
			Integer proficienyId = this.wOExecutionDAO.getProficiencyId(wOID,signum);
			
				response.addFormMessage(SUCCESS);
				response.setResponseData(proficienyId);
			    LOG.info("getProficiencyId : End");
			
		}
		catch(ApplicationException exe) {
			response.addFormWarning(exe.getMessage());
			response.setValidationFailed(true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	public DownloadTemplateModel downloadProjectQueueWorkOrders(String signum, String startDate, String endDate,
			String priority, String status, String doStatus, String projectIdList) throws IOException {

		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = "ProjectQueueDataExport" + "-"
				+ (new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date())) + ".xlsx";
		
		 int length= appConfig.getIntegerProperty(ConfigurationFilesConstants.PROJECT_QUEUE_DOWNLOAD_LENGTH);
		 
		List<Map<String, Object>> deliverableData = this.wOExecutionDAO.downloadProjectQueueWorkOrders(signum,
				startDate, endDate, priority, status, doStatus, projectIdList,length);

		byte[] fData = null;
		if (CollectionUtils.isNotEmpty(deliverableData)) {
			fData = FileUtil.generateXlsFile(deliverableData);

		}

		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);

		return downloadTemplateModel;
	}

	
@Transactional("transactionManager")
	public Response<List<WoCompleteDetailsModel>> getCompleteWoDetails(int wOID) {
		Response<List<WoCompleteDetailsModel>> response=new Response<>();
		try {
			List<WoCompleteDetailsModel> workOrder = wOExecutionDAO.getCompleteWoDetails(wOID, AppConstants.ALL);
			
			response.setResponseData(workOrder);
			// external Group set in model
			if (CollectionUtils.isEmpty(workOrder)) {
				throw new ApplicationException(200, "No data exists for given work order ID");
			}
		}catch (Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
		}
		return response;
	}


	@Transactional("transactionManager")
	public ResponseEntity<Response<Boolean>> validateServerBotInputUrl(String inputUrl, int projectID) {

		Response<Boolean> response = new Response<>();

		try {
			LOG.info("validateServerBotInputUrl:Start");

			int marketAreaID = wOExecutionDAO.getMarketAreaIDByProjectID(projectID);

			SharePointDetailModel sharePointDetailModel = wOExecutionDAO.getDetailsByMarketID(marketAreaID);

			if (sharePointDetailModel != null) {
				String prefix = sharePointDetailModel.getBaseUrl();
				if (!StringUtils.startsWithIgnoreCase(inputUrl, prefix)) {
					throw new ApplicationException(200,
							ENTER_VALID_SHAREPOINT_URL_PLEASE_CHECK_YOUR_SITE_NAME_WITH_MARKET_AREA);
				}
				String midpart = "sites" + "/" + sharePointDetailModel.getSitename();
				if (!((inputUrl.toLowerCase()).contains(midpart.toLowerCase()))) {
					throw new ApplicationException(200,
							ENTER_VALID_SHAREPOINT_URL_PLEASE_CHECK_YOUR_SITE_NAME_WITH_MARKET_AREA);
				}

				response.addFormMessage(SUCCESS);
				response.setResponseData(true);
			} else {
				throw new ApplicationException(200, PLEASE_CONFIGURE_MARKET_WITH_SHAREPOINT);
			}

			LOG.info("validateServerBotInputUrl:End");

		} catch (ApplicationException exe) {
			response.addFormError(exe.getMessage());
			response.setValidationFailed(true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

@Transactional("transactionManager")
public ResponseEntity<Response<Boolean>> validateSharepointDetailsWithMarketArea(int projectID) {
	
	Response<Boolean> response = new Response<>();

  try {
		LOG.info("validateSharepointDetailsWithMarketArea:Start");
	    
	    int marketAreaID=wOExecutionDAO.getMarketAreaIDByProjectID(projectID);    
	    
	    boolean isSharePointDetailWithMarketAreaValid=this.wOExecutionDAO.validateSharePointDetailWithMarketArea(marketAreaID);
		
		if(Boolean.FALSE.equals(isSharePointDetailWithMarketAreaValid)) {
			throw new ApplicationException(200, PLEASE_CONFIGURE_MARKET_WITH_SHAREPOINT);
		}
	    else {
	    	response.addFormMessage(SUCCESS);
	    	response.setResponseData(true);
	    }
		LOG.info("validateSharepointDetailsWithMarketArea:End");
		
	}
	catch(ApplicationException exe) {
		response.addFormError(exe.getMessage());
		response.setValidationFailed(true);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	catch (Exception e) {
		response.addFormError(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<>(response, HttpStatus.OK);
	
   }


@Transactional("transactionManager")
public ResponseEntity<Response<String>> getSharepointUrl() {
	
	Response<String> response = new Response<>();

  try {
		LOG.info("getSharepointUrl:Start");
	
		String result=wOExecutionDAO.getSharepointUrl();
		
		if (StringUtils.isEmpty(result)) {
			response.addFormMessage("No Data Found!");

		} else {
			response.addFormMessage(SUCCESS);
			response.setResponseData(result);
		}

		LOG.info("getSharepointUrl:End");
		
	}
	catch(ApplicationException exe) {
		response.addFormError(exe.getMessage());
		response.setValidationFailed(true);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	catch (Exception e) {
		response.addFormError(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<>(response, HttpStatus.OK);
	
   }


@Transactional("transactionManager")
public ResponseEntity<Response<List<SharePointDetailModel>>> getSharepointConfigurationDetails() {
	
	Response<List<SharePointDetailModel>> response = new Response<>();

  try {
		LOG.info("getSharepointConfigurationDetails:Start");
	
		List<SharePointDetailModel> listOfSharePointDetails=wOExecutionDAO.getSharepointConfigurationDetails();
		
		if (listOfSharePointDetails.isEmpty()) {
			response.addFormMessage("No Data Found!");

		} else {
			response.addFormMessage(SUCCESS);
			response.setResponseData(listOfSharePointDetails);
		}

		LOG.info("getSharepointConfigurationDetails:End");
		
	}
	catch(ApplicationException exe) {
		response.addFormError(exe.getMessage());
		response.setValidationFailed(true);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	catch (Exception e) {
		response.addFormError(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<>(response, HttpStatus.OK);
	
   }

	public ResponseEntity<Response<String>> getOutPutUploadByStepID(String stepID, int flowChartDefID) {
		Response<String> response = new Response<>();

		try {
			String outputUpload = wOExecutionDAO.getOutPutUploadByStepID(stepID, flowChartDefID);
			response.setResponseData(outputUpload);

		} catch (ApplicationException exe) {
			response.addFormError(exe.getMessage());
			response.setValidationFailed(true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


}


	



