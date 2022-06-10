/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.ericsson.isf.model.AdhocWorkOrderCreationModel;
import com.ericsson.isf.model.AdhocWorkOrderModel;
import com.ericsson.isf.model.AssignWOModel;
import com.ericsson.isf.model.BookingDetailsModel;
import com.ericsson.isf.model.CreateWoResponse;
import com.ericsson.isf.model.CurrentWorkOrderModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DeliverableSuccessReasonModel;
import com.ericsson.isf.model.DescisionStepDetailsModel;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.ExternalExecutionDetailsModel;
import com.ericsson.isf.model.InProgressWorkOrderModel;
import com.ericsson.isf.model.LastStepDetailsModel;
import com.ericsson.isf.model.ParallelWorkOrderDetailsModel;
import com.ericsson.isf.model.PreviousStepDetailsModel;
import com.ericsson.isf.model.ProjectQueueWoRequestModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.SaveClosureDetailsForWOInFWModel;
import com.ericsson.isf.model.SaveClosureDetailsForWOModel;
import com.ericsson.isf.model.SearchPlannedWOProjectModel;
import com.ericsson.isf.model.SharePointDetailModel;
import com.ericsson.isf.model.StepDetailsModel;
import com.ericsson.isf.model.TaskModel;
import com.ericsson.isf.model.UpdateBookingAndStepDetailsModel;
import com.ericsson.isf.model.UserLoginModel;
import com.ericsson.isf.model.UserWorkFlowProficencyModel;
import com.ericsson.isf.model.WoCompleteDetailsModel;
import com.ericsson.isf.model.WorkFlowBookingDetailsModel;
import com.ericsson.isf.model.WorkFlowUserProficiencyModel;
import com.ericsson.isf.model.WorkOrderBasicDetailsModel;
import com.ericsson.isf.model.WorkOrderColumnMappingModel;
import com.ericsson.isf.model.WorkOrderCompleteDetailsModel;
import com.ericsson.isf.model.WorkOrderFailureReasonModel;
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
import com.ericsson.isf.model.botstore.WoStepDetailsList;
import com.ericsson.isf.service.PlannedWOProject;
import com.ericsson.isf.service.WOExecutionService;
import com.ericsson.isf.service.audit.AuditEnabled;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AppUtil;

/**
 *
 * @author esanpup
 */
@RestController
@RequestMapping("/woExecution")
public class WOExecutionController {

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);
	private static final String BEGIN = "BEGIN %s";

	@Autowired
	private WOExecutionService woExecutionService;

	@RequestMapping(value = "/getHelpDocumentLink", method = RequestMethod.GET)
	public String getHelpDocumentLink() {
		String file_path = woExecutionService.getFilePath();
		LOG.info("Download link: " + file_path);
		return file_path;
	}

	/**
	 *
	 * Purpose - This method is used for searching planned work Orders (where Status
	 * is not in closed).
	 * <p>
	 * Method Type - POST.
	 * <p>
	 * URL -
	 * "/woManagement/searchPlannedWorkOrders/{priority}/{woStatus}/{endDate}/{woName}/{projectName}/{projectID}//{subDomain}/{technology}/{activity}/{subActivity}".
	 * <p>
	 * Input Parameter:- priority, woStatus, endDate, woName, projectName,
	 * projectID, subDomain, technology, activity, subActivity
	 *
	 */
	@RequestMapping(value = "/searchPlannedWorkOrders", method = RequestMethod.POST)
	public Map<String, Object> searchPlannedWorkOrders(
			@RequestParam(value = "signum", required = true) String signum,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "woStatus", required = false) String woStatus
			, HttpServletRequest request) {
		// return workOrderPlanService.searchWorkOrderPlanDetails(projectID,
		// domainID, serviceAreaID, technologyID, activityName, subActivityID);
		DataTableRequest dataTableRequest = new DataTableRequest(request);
   	    LOG.info("searchPlannedWorkOrders :SUCCESS");
		return woExecutionService.searchPlannedWorkOrders(signum, woStatus, startDate, endDate, dataTableRequest);

	}


	@RequestMapping(value = "/getAssignedWorkOrders", method = RequestMethod.GET)
	public List<WorkOrderBasicDetailsModel> getAssignedWorkOrders(
			@RequestParam(value = "signum", required = true) String signum,
			@RequestParam(value = "startDate", required = true) String startDate,
			@RequestParam(value = "endDate", required = true) String endDate,
			@RequestParam(value = "woStatus", required = true) String woStatus) {
		// return workOrderPlanService.searchWorkOrderPlanDetails(projectID,
		// domainID, serviceAreaID, technologyID, activityName, subActivityID);
		return woExecutionService.getAssignedWorkOrders(signum, woStatus, startDate, endDate);

	}

	@RequestMapping(value = "/getUnassignedWorkOrders", method = RequestMethod.GET)
	public Response<List<WorkOrderBasicDetailsModel>> getUnassignedWorkOrders(
			@RequestParam(value = "signum", required = true) String signum,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "priority", required = false) String priority,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "doStatus", required = false) String doStatus){
		// return workOrderPlanService.searchWorkOrderPlanDetails(projectID,
		// domainID, serviceAreaID, technologyID, activityName, subActivityID);
		return woExecutionService.getUnassignedWorkOrders(signum, startDate, endDate, priority, status, doStatus);

	}
	@RequestMapping(value = "/getProjectQueueWorkOrders", method = RequestMethod.POST)
	public Response<PlannedWOProject> getProjectQueueWorkOrders(HttpServletRequest request,
			@RequestParam(value = "signum", required = true) String signum,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "priority", required = false) String priority,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "doStatus", required = false) String doStatus,
			@RequestParam(value = "projectIdList", required = false) String projectIdList,
			@RequestParam(value = "checkSearch", required = false) boolean checkSearch
			) {
		return woExecutionService.getProjectQueueWorkOrders(signum, startDate, endDate, priority, status, doStatus,projectIdList, new DataTableRequest(request),checkSearch);
	}

	@RequestMapping(value = "/getAdhocWorkOrderDetails/{signumID}", method = RequestMethod.GET)
	public List<AdhocWorkOrderModel> getAdhocWorkOrderDetails(@PathVariable("signumID") String signumID) {
		if (signumID.equalsIgnoreCase("Null") || "".equals(signumID)) {
			throw new ApplicationException(500, "Invalid input... SignumID cannot be NULL !!!");
		} else {
			List<AdhocWorkOrderModel> adhocList = woExecutionService.getAdhocWorkOrderDetails(signumID);
			return adhocList;

		}
	}

	@RequestMapping(value = "/getAdhocWorkOrderDetailsByID/{adhocWOID}/{signumID}", method = RequestMethod.GET)
	public List<AdhocWorkOrderModel> getAdhocWorkOrderDetailsByID(@PathVariable("adhocWOID") int adhocWOID,
			@PathVariable("signumID") String signumID) {
		if (adhocWOID == 0 || signumID.equalsIgnoreCase("Null")) {
			throw new ApplicationException(500,
					"Invalid input... AdhocWOID cannot be 0 or SignumID cannot be NULL !!!");
		} else {
			List<AdhocWorkOrderModel> adhocListByID = woExecutionService.getAdhocWorkOrderDetailsByID(adhocWOID,
					signumID);
			return adhocListByID;
		}
	}

	@RequestMapping(value = "/saveFeedback/{signumID}/{feedback}", method = RequestMethod.POST)
	public void saveFeedback(@PathVariable("signumID") String signumID, @PathVariable("feedback") String feedback) {
		if (signumID.equalsIgnoreCase("Null") || signumID.equals(" ")) {
			throw new ApplicationException(500, "Invalid input... SignumID cannot be NULL !!!");
		} else {
			woExecutionService.saveFeedback(signumID, feedback);
		}
	}

	@AuditEnabled
	@RequestMapping(value = { "/updateWorkOrderStatus" }, method = RequestMethod.POST)
	public Response<CreateWoResponse> updateWorkOrderStatus(
			@RequestBody SaveClosureDetailsForWOModel saveClosureDetailsForWOModel) {

		LOG.info("TBL_WORK_ORDER: " + saveClosureDetailsForWOModel.getwOID() + AppConstants.CSV_CHAR_COMMA
				+ StringUtils.SPACE + saveClosureDetailsForWOModel.getSignumID() + StringUtils.SPACE
				+ AppConstants.CSV_CHAR_COMMA + StringUtils.SPACE + saveClosureDetailsForWOModel.getDeliveryStatus()
				+ AppConstants.CSV_CHAR_COMMA + StringUtils.SPACE + saveClosureDetailsForWOModel.getStatusComment());
		return woExecutionService.updateWorkOrderStatus(saveClosureDetailsForWOModel);

	}

	// Removing Work Order Duplicate functionality from 9.0
	/*
	 * @RequestMapping(value =
	 * "/createDuplicateWorkOrder/{wOID}/{projectID}/{signum}/{comment}", method =
	 * RequestMethod.POST) public void
	 * createDuplicateWorkOrder(@PathVariable("signum") String signum,
	 * 
	 * @PathVariable("wOID") String wOID,
	 * 
	 * @PathVariable("projectID") String projectID,
	 * 
	 * @PathVariable("comment") String comment) {
	 * woExecutionService.createDuplicateWorkOrder(wOID,signum,projectID, comment);
	 * }
	 */

	@RequestMapping(value = "/getTaskRelatedDetails/{subActivityID}/{projectID}/{taskID}", method = RequestMethod.GET)
	public List<TaskModel> getTaskRelatedDetails(@PathVariable("taskID") String taskID,
			@PathVariable("subActivityID") String subActivityID, @PathVariable("projectID") String projectID) {
		if (subActivityID.equalsIgnoreCase("Null") && projectID.equalsIgnoreCase("Null")
				&& taskID.equalsIgnoreCase("Null")) {
			throw new ApplicationException(500,
					"Invalid input... SubActivityID or ProjectID or TaskID cannot be NULL !!!");
		} else {
			return woExecutionService.getTaskRelatedDetails(taskID, subActivityID, projectID);
		}
	}

	@AuditEnabled
	@RequestMapping(value = "/startWorkOrderTask/{wOID}/{taskID}/{stepID}/{flowChartDefID}/{signumID}/{decisionValue}/{exeType}/{botPlatform}", method = RequestMethod.POST)
	public Map<String, Object> startWorkOrderTask(@PathVariable("wOID") int wOID, @PathVariable("taskID") int taskID,
			@PathVariable("stepID") String stepID, @PathVariable("flowChartDefID") int flowChartDefID,
			@PathVariable("signumID") String signumID, @PathVariable("decisionValue") String decisionValue,
			@PathVariable("exeType") String exeType, @PathVariable("botPlatform") String botPlatform,
			@RequestParam(value = "botConfigJson", required = false) String botConfigJson) {

		return woExecutionService.startWorkOrderTask(wOID, taskID, stepID, flowChartDefID, signumID, decisionValue,
				exeType, botPlatform, null, botConfigJson, "Booking", new ServerBotModel(),0);
		//need to change after discussion for last variable
	}

	@RequestMapping(value = "/getBookingID/{wOID}/{taskID}/{signum}", method = RequestMethod.GET)
	public WorkFlowBookingDetailsModel getBookingID(@PathVariable("wOID") int wOID, @PathVariable("taskID") int taskID,
			@PathVariable("signum") String signum) {
		return woExecutionService.getBookingID(wOID, taskID, signum);
	}

	@AuditEnabled
	@RequestMapping(value = "/stopWorkOrderTask", method = RequestMethod.POST)
	public Map<String, Object> stopWorkOrderTask(@RequestBody ServerBotModel serverBotModel) {
		long startTime=System.currentTimeMillis();
		LOG.info("START TIME: "+startTime);
		Map<String, Object> response=woExecutionService.stopWorkOrderTask(serverBotModel);
		long endTime=System.currentTimeMillis();
		LOG.info("END TIME: "+endTime);
		LOG.info("TOTAL TIME ELAPSED IN @stopWorkOrderTask API : "+(endTime-startTime)+" ms");
//		return woExecutionService.stopWorkOrderTask(serverBotModel);
		return response;
	}
	@AuditEnabled
	@RequestMapping(value = "/v1/stopWorkOrderTask", method = RequestMethod.POST)
	public Map<String, Object> stopWorkOrderTaskV1(@RequestBody ServerBotModel serverBotModel) {

		return woExecutionService.stopWorkOrderTask(serverBotModel);
	}

	@RequestMapping(value = "/stopWOMaxBookingID", method = RequestMethod.POST)
	public WorkFlowBookingDetailsModel stopWOMaxBookingID(@RequestBody ServerBotModel serverBotModel) {
		return woExecutionService.stopWOMaxBookingID(serverBotModel);
	}

	@RequestMapping(value = "/stopWOMaxBookingIDNew", method = RequestMethod.POST)
	public ResponseEntity<Response<WorkFlowBookingDetailsModel>> stopWOMaxBookingIDNew(@RequestBody ServerBotModel serverBotModel) {
		return woExecutionService.stopWOMaxBookingIDNew(serverBotModel);
	}

	@RequestMapping(value = "/getInProgressWorkOrderDetails/{signumID}", method = RequestMethod.GET)
	public Map<String, Object> getInProgressWorkOrderDetails(@PathVariable("signumID") String signumID) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<InProgressWorkOrderModel> workOrder = woExecutionService.getInProgressWorkOrderDetails(signumID);
		if (workOrder.isEmpty()) {
			result.put("msg", "Data Not Found For Selected Signum");
			result.put("data", workOrder);
		} else {
			result.put("data", workOrder);
		}
		return result;
	}

	@RequestMapping(value = "/getWorkflowStepDetails", method = RequestMethod.GET)
	public List<WorkflowStepDetailModel> getWorkflowStepDetails(
			@RequestParam(value = "subActivityFlowChartDefID", required = false) int subActivityFlowChartDefID) {
		return woExecutionService.getWorkflowStepDetails(subActivityFlowChartDefID);

	}

	@RequestMapping(value = "/getWorkflowStepDetailsForComments", method = RequestMethod.GET)
	public List<WorkflowStepDetailModel> getWorkflowStepDetailsForComments(
			@RequestParam(value = "subActivityFlowChartDefID", required = false) int subActivityFlowChartDefID) {
		return woExecutionService.getWorkflowStepDetailsForComments(subActivityFlowChartDefID);
	}

	/**
	 * @author ekmbuma [modifiedBy]
	 * 
	 * @param signumID
	 * @param isApproved
	 * @return List<CurrentWorkOrderModel>
	 */
	@RequestMapping(value = "/getCurrentWorkOrderDetails/{signumID}/{isApproved}", method = RequestMethod.GET)
	public List<CurrentWorkOrderModel> getCurrentWorkOrderDetails(@PathVariable("signumID") String signumID,
			@PathVariable("isApproved") String isApproved) {

		return woExecutionService.getCurrentWorkOrderDetails(signumID, isApproved);
	}

	@RequestMapping(value = "/pauseAllTaskOnLogout", method = RequestMethod.POST)
	public boolean pauseAllTaskOnLogout(@RequestBody UserLoginModel userLogin) throws InterruptedException {
		return woExecutionService.pauseAllTaskOnLogout(userLogin);
	}

//	@RequestMapping(value = "/checkParallelWorkOrderDetails/{signumID}/{isApproved}/{executionType}/"
//			+ "{woid}/{taskid}/{projectID}/{versionNO}/{subActivityFlowChartDefID}/{stepID}", method = RequestMethod.GET)
//	public Map<String, Object> checkParallelWorkOrderDetails(@PathVariable("signumID") String signumID,
//			@PathVariable("isApproved") String isApproved, @PathVariable("executionType") String executionType,
//			@PathVariable("woid") int woid, @PathVariable("taskid") int taskid,
//			@PathVariable("projectID") int projectID, @PathVariable("versionNO") int versionNO,
//			@PathVariable("subActivityFlowChartDefID") int subActivityFlowChartDefID,
//			@PathVariable("stepID") String stepID) {
//		Map<String, Object> details = woExecutionService.checkParallelWorkOrderDetails(signumID, isApproved,
//				executionType, woid, taskid, projectID, versionNO, subActivityFlowChartDefID, stepID);
//		return details;
//	}
	@RequestMapping(value = "/checkParallelWorkOrderDetails", method = RequestMethod.POST)
	public ResponseEntity<Response<Map<String, Object>>> checkParallelWorkOrderDetails(@RequestBody ParallelWorkOrderDetailsModel model) {
		long startTime=System.currentTimeMillis();
		LOG.info("START TIME: "+startTime);
		ResponseEntity<Response<Map<String, Object>>> response=woExecutionService.checkParallelWorkOrderDetails(model);
		long endTime=System.currentTimeMillis();
		LOG.info("END TIME: "+endTime);
		LOG.info("TOTAL TIME ELAPSED IN @checkParallelWorkOrderDetails API : "+(endTime-startTime)+" ms");
		return response;
	}

	@RequestMapping(value = "/getCompleteWorkOrderDetails/{wOID}", method = RequestMethod.GET)
	public Response<List<WorkOrderCompleteDetailsModel>> getCompleteWorkOrderDetails(@PathVariable("wOID") int wOID) {
		return woExecutionService.getCompleteWorkOrderDetails(wOID);
	}

	@RequestMapping(value = "/updateWorkOrderUserLevel", method = RequestMethod.POST)
	public void updateWorkOrderUserLevel(@RequestBody WorkOrderModel workOrderModel) {
		woExecutionService.updateWorkOrderUserLevel(workOrderModel);
		LOG.info("TBL_WORK_ORDER: SUCCESS");
	}

	@RequestMapping(value = "/updateWFDefinition/{wOID}/{signum}", method = RequestMethod.POST)
	public Map<String, Object> updateWFDefinition(@RequestBody String wFJson, @PathVariable("wOID") String wOID,
			@PathVariable("signum") String signum) {
		Map<String, Object> data = new HashMap<>();
		if (signum == null || "Null".equalsIgnoreCase(signum) || signum.equals("")) {
			data.put("Error", "Session is timedOut!!,Please login again");
		} else {
			data = woExecutionService.updateWFDefinition(wOID, wFJson, signum);
		}
		return data;
	}

	/*
	 *
	 * Purpose - This method is used for creating Adhoc work Order. <p> Method Type
	 * - POST. <p> URL - "/woExecution/createAdhocWorkOrder". <p> Input Parameter:-
	 * { "WOName": testWO, "projectID": 1, "Domain": testDomain, "Subdomain":
	 * testSubDomain, "Servicearea": testSA, "SubServiceArea": testSSA,
	 * "Technology": tech, "Activity": Activity1, "Subactivity": SA1, "Market":
	 * mkt1, "Vendor": testVendor1, "Type": Site/Node, "NodeNames": 'a', 'b'
	 * "startDate": "2017-12-01", "startTime": "18:00:00", "EndDate": "2017-12-04"
	 * "Estimated Effort": "Status": "createdBy": "eguphee", "CreatedDate":
	 * "2017-11-08" "Priority": "normal" // Planned End date should be calculated
	 * based on AvgEstdEffort/Estimated effort and periodicity should be single. }
	 *
	 */
	@RequestMapping(value = "/createAdhocWorkOrder", method = RequestMethod.POST)
	public void createAdhocWorkOrder(@RequestBody AdhocWorkOrderCreationModel adhocWOObject) {
		woExecutionService.createAdhocWorkOrder(adhocWOObject);
		LOG.info("TBL_ADHOC_WORK_ORDER: SUCCESS");
	}

	@AuditEnabled
	@RequestMapping(value = "/saveClosureDetailsForWO", method = RequestMethod.POST)
	public Response<CreateWoResponse> saveClosureDetailsForWO(
			@RequestBody SaveClosureDetailsForWOModel saveClosureDetailsObject) throws IOException {
		return woExecutionService.saveClosureDetailsForWO(saveClosureDetailsObject);
	}
	//V1/saveClosureDetailsForWO
	@AuditEnabled
	@RequestMapping(value = "/v1/saveClosureDetailsForWO", method = RequestMethod.POST)
	public Response<CreateWoResponse> saveClosureDetailsForWOv1(
			@RequestBody SaveClosureDetailsForWOModel saveClosureDetailsObject) throws IOException {
		return woExecutionService.saveClosureDetailsForWOv1(saveClosureDetailsObject);
	}

	/**
	 * To close wo from floating window with added SignalR functionality
	 * 
	 * @author ekmbuma
	 * @param saveClosureDetailsObject
	 * @return Response<CreateWoResponse>
	 * @throws IOException
	 */
	@AuditEnabled
	@RequestMapping(value = "/saveClosureDetailsForWOInFW", method = RequestMethod.POST)
	public Response<CreateWoResponse> saveClosureDetailsForWOInFW(
			@RequestBody SaveClosureDetailsForWOInFWModel saveClosureDetailsObject) throws IOException {
		return woExecutionService.saveClosureDetailsForWOInFW(saveClosureDetailsObject);
	}

	/**
	 * 
	 * @param wOID
	 * @param proficiencyID
	 * @return ResponseEntity<Response<Map<String, Object>>>
	 * @throws Exception 
	 */
	@RequestMapping(value = "/getWOWorkFlow/{wOID}/{proficiencyID}", method = RequestMethod.GET)
	public ResponseEntity<Response<Map<String, Object>>> getWOWorkFlow(@PathVariable("wOID") int wOID,
			@PathVariable("proficiencyID") int proficiencyID,
			@RequestHeader(value = "Signum") String signum) throws Exception {
		
		long startTime=System.currentTimeMillis();
		LOG.info("START TIME: "+startTime);
		ResponseEntity<Response<Map<String, Object>>> response=woExecutionService.getWOWorkFlow(wOID, proficiencyID,signum);
		long endTime=System.currentTimeMillis();
		LOG.info("END TIME: "+endTime);
		LOG.info("TOTAL TIME ELAPSED IN @getWOWorkFlow API : "+(endTime-startTime)+" ms");
		
		return response;
	}

	@AuditEnabled
	@RequestMapping(value = "/updateBookingDetailsStatus/{wOID}/{signumID}/{taskID}/{bookingID}/{status}/{reason}/{stepid}/{flowChartDefID}/{refferer}", method = RequestMethod.POST)
	public Map<String, String> updateBookingDetailsStatus(@PathVariable("wOID") int wOID,
			@PathVariable("signumID") String signumID, @PathVariable("taskID") int taskID,
			@PathVariable("bookingID") int bookingID, @PathVariable("status") String status,
			@PathVariable("reason") String reason, @PathVariable("stepid") String stepid,
			@PathVariable("flowChartDefID") int flowChartDefID,
			@PathVariable(value="refferer") String refferer,
			@RequestParam(value="proficiencyId") Optional<Integer> proficiencyId) {
		LOG.info("updateBookingDetailsStatus success... ");
		return woExecutionService.updateBookingDetailsStatus(wOID, signumID, taskID, bookingID, status, reason, stepid, flowChartDefID,refferer,proficiencyId);
	}

	@RequestMapping(value = "/updateWorkOrderNodes", method = RequestMethod.POST)
	public String updateWorkOrderNodes(@RequestBody WorkOrderNetworkElementModel workOrderNetworkElementModel) {
		LOG.info("TRANSACTIONDATA.TBL_WORK_ORDER_NODES: Success");
		return woExecutionService.updateWorkOrderNodes(workOrderNetworkElementModel);
	}

	@AuditEnabled
	@RequestMapping(value = "/assignWo", method = RequestMethod.POST)
	public List<Map<String, Object>> assignWo(@RequestBody AssignWOModel workOrderModel) {
		return woExecutionService.assignWo(workOrderModel);
	}

	@RequestMapping(value = "/updateWoSignumToNull", method = RequestMethod.POST)
	public List<Map<String, Object>> updateWoSignumToNull(@RequestBody List<WorkOrderModel> workOrderModel) {
		return woExecutionService.updateWoSignumToNull(workOrderModel);
	}

	@RequestMapping(value = "/getWorkOrderViewDetailsByWOID/{WOID}", method = RequestMethod.GET)
	public List<WorkOrderViewDetailsByWOIDModel> getWorkOrderViewDetailsByWOID(@PathVariable("WOID") int WOID) {
		List<WorkOrderViewDetailsByWOIDModel> woViewDetails = woExecutionService.getWorkOrderViewDetailsByWOID(WOID);

		return woViewDetails;
	}

	@RequestMapping(value = "/checkIFPreviousStepCompleted/{WOID}/{taskID}/{subactivityDefID}/{flowChartStepID}", method = RequestMethod.GET)
	public ResponseEntity<Response<PreviousStepDetailsModel>> checkIFPreviourStepCompleted(@PathVariable("WOID") int WOID,
			@PathVariable("taskID") int taskID, @PathVariable("subactivityDefID") int subactivityDefID,
			@PathVariable("flowChartStepID") String flowChartStepID) {
		LOG.info(String.format(BEGIN, "checkIFPreviousStepCompleted"));
		return woExecutionService.checkIFPreviousStepCompleted(WOID, taskID, subactivityDefID, flowChartStepID);

	}

	@RequestMapping(value = "/checkIFPreviousStepCompletedV2/{WOID}/{flowChartStepID}/{subactivityDefID}", method = RequestMethod.GET)
	public PreviousStepDetailsModel checkIFPreviousStepCompletedV2(@PathVariable("WOID") int WOID,
			@PathVariable("flowChartStepID") String flowChartStepID,
			@PathVariable("subactivityDefID") int subactivityDefID) {
		return woExecutionService.checkIFPreviousStepCompletedV2(WOID, flowChartStepID, subactivityDefID);

	}

	@RequestMapping(value = "/checkIFLastStep/{WOID}/{subactivityDefID}", method = RequestMethod.GET)
	public LastStepDetailsModel checkIFLastStep(@PathVariable("WOID") int WOID,
			@PathVariable("subactivityDefID") int subactivityDefID) {
		return woExecutionService.checkIFLastStep(WOID, subactivityDefID);

	}

	@RequestMapping(value = "/checkIFLastStepNew/{WOID}/{subactivityDefID}", method = RequestMethod.GET)
	public Response<Boolean> checkIFLastStepNew(@PathVariable("WOID") int WOID,
			@PathVariable("subactivityDefID") int subactivityDefID) {
		LOG.info("BEGIN: checkIFLastStepNew");
		return woExecutionService.checkIFLastStepNew(WOID, subactivityDefID);

	}

	@RequestMapping(value = "/getWorkOrderDetailsByName", method = RequestMethod.GET)
	public List<Map<String, Object>> getWorkOrderDetailsByName(@RequestParam("woName") String woName,
			@RequestParam("signum") String signum) {
		return woExecutionService.getWorkOrderDetailsByName(woName, signum);

	}

	@RequestMapping(value = "/getWOFailureReasons/{category}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<WorkOrderFailureReasonModel>>> getWOFailureReasons(@PathVariable("category") String category) {
		return woExecutionService.getWOFailureReasons(category);
	}

	@RequestMapping(value = "/startWO/{wOID}/{taskID}/{signumID}", method = RequestMethod.GET)
	public List<String> startWO(@PathVariable("wOID") int wOID, @PathVariable("taskID") int taskID,
			@PathVariable("signumID") String signumID) {
		List<String> bookingValue = woExecutionService.startWO(wOID, taskID, signumID);
		LOG.info("startWO success... It's Booking ID: " + bookingValue);
		return bookingValue;
	}

	@RequestMapping(value = "/updateWOBooking/{wOID}/{taskID}/{bookingStatus}/{signumID}", method = RequestMethod.GET)
	public List<String> updateWOBooking(@PathVariable("wOID") int wOID, @PathVariable("taskID") int taskID,
			@PathVariable("bookingStatus") String bookingStatus, @PathVariable("signumID") String signumID) {
		List<String> bookingValue = woExecutionService.updateWOBooking(wOID, taskID, bookingStatus, signumID);
		LOG.info("updateWoBooking success... It's Booking ID: " + bookingValue);
		return bookingValue;
	}

	@RequestMapping(value = "/closeWO/{wOID}/{taskID}/{bookingID}/{status}/{signumID}", method = RequestMethod.POST)
	public Map<String, String> closeWO(@PathVariable("wOID") int wOID, @PathVariable("taskID") int taskID,
			@PathVariable("bookingID") int bookingID, @PathVariable("status") String status,
			@PathVariable("signumID") String signumID, @RequestBody String url) {
		return woExecutionService.closeWO(wOID, taskID, bookingID, status, signumID, url);
	}

	@RequestMapping(value = "/addStepDetailsForFlowChart", method = RequestMethod.POST)
	public ResponseEntity<Response<Void>> addStepDetailsForFlowChart(@RequestBody StepDetailsModel stepDetailsModel) {
		LOG.info("addStepDetailsForFlowChart :BEGIN");
		return woExecutionService.addStepDetailsForFlowChart(stepDetailsModel);
	}
	// v1/addStepDetailsForFlowChart
	@RequestMapping(value = "/v1/addStepDetailsForFlowChart", method = RequestMethod.POST)
	public void addStepDetailsForFlowChartV1(@RequestBody StepDetailsModel stepDetailsModel) {
		woExecutionService.addStepDetailsForFlowChartV1(stepDetailsModel);
		LOG.info("add stepDetails for flowchart success... ");
	}

	@RequestMapping(value = "/pauseAllWOTasks/{reason}/{signumID}", method = RequestMethod.POST)
	public Map<String, String> pauseAllWOTasks(@PathVariable("reason") String reason,
			@PathVariable("signumID") String signumID,
			@RequestBody List<UpdateBookingAndStepDetailsModel> updateModel) {
		return woExecutionService.pauseAllWOTasks(updateModel, reason, signumID);
	}

	@RequestMapping(value = "/saveUserWFProficency/{lgSignum}", method = RequestMethod.POST)
	public String saveUserWFProficency(@PathVariable("lgSignum") String lgSignum,
			@RequestBody UserWorkFlowProficencyModel userWorkFlowProficency) {
		woExecutionService.saveUserWFProficency(userWorkFlowProficency, lgSignum);
		LOG.info("saveUserWFProficency success... ");
		return "Data Saved Successfully";
	}

	@RequestMapping(value = "/updateUserWFProficency/{lgSignum}", method = RequestMethod.POST)
	public String updateUserWFProficency(@PathVariable("lgSignum") String lgSignum,
			@RequestBody UserWorkFlowProficencyModel userWorkFlowProficency) {
		woExecutionService.updateUserWFProficency(userWorkFlowProficency, lgSignum);
		LOG.info("updateUserWFProficency success... ");
		return "Data updated Successfully";
	}

	@RequestMapping(value = "/getUserWFProficency/{signumID}/{projectID}/{subActivityID}", method = RequestMethod.GET)
	public List<Map<String, String>> getUserWFProficency(@PathVariable("signumID") String signumID,
			@PathVariable("projectID") int projectID, @PathVariable("subActivityID") int subActivityID) {
		return woExecutionService.getUserWFProficency(signumID, projectID, subActivityID);
	}

	@RequestMapping(value = "/updateWorkOrderWFVersion/{woID}/{wfVersionNo}/{signumID}", method = RequestMethod.POST)
	public String updateWorkOrderWFVersion(@PathVariable("woID") int woID, @PathVariable("wfVersionNo") int wfVersionNo,
			@PathVariable("signumID") String signumID) {
		woExecutionService.updateWorkOrderWFVersion(woID, wfVersionNo, signumID);
		LOG.info("updateWorkOrderWFVersion success... ");
		return "Data updated Successfully";
	}

	@RequestMapping(value = "/getWorkOrders", method = RequestMethod.GET)
	public ResponseEntity<Response<List<WorkOrderProgressModel>>> getWorkOrders(@RequestParam(value = "signumID") String signumID,
			@RequestParam(value = "status") String status,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws URISyntaxException {
		
		long startTime=System.currentTimeMillis();
		LOG.info("START TIME: "+startTime);
		ResponseEntity<Response<List<WorkOrderProgressModel>>> response=woExecutionService.getWorkOrders(signumID, status, startDate, endDate);
		
		long endTime=System.currentTimeMillis();
		LOG.info("END TIME: "+endTime);
		LOG.info("TOTAL TIME ELAPSED IN @getWorkOrders API : "+(endTime-startTime)+" ms");
		
//		return woExecutionService.getWorkOrders(signumID, status, startDate, endDate);
		return response;		
	}

	@RequestMapping(value = "/v1/getWorkOrders", method = RequestMethod.GET)
	public ResponseEntity<Response<LinkedList<WorkOrderProgressModel>>> getWorkOrdersDetailsBySignum(@RequestParam(value = "signumID") String signumID,
			@RequestParam(value = "status") String status,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws URISyntaxException {
		return woExecutionService.getWorkOrderDetailsBySignum(signumID, status, startDate, endDate);
	}

	@RequestMapping(value = "/startAllWorkOrderTasks", method = RequestMethod.POST)
	public Map<String, String> startAllWorkOrderTasks(@RequestBody WoStepDetailsList woList) {

		return woExecutionService.startAllWorkOrderTasks(woList);
	}

	/*
	 * @RequestMapping(value = "/startServerBot", method = RequestMethod.POST)
	 * public RpaApiResponse startServerBot(@RequestPart("inputZip") MultipartFile
	 * inputZip, @RequestParam("serverBotJsonStr") String serverBotJsonStr) {
	 * RpaApiResponse res= new RpaApiResponse();
	 * 
	 * try { Gson gson = new Gson(); JsonParser parser = new JsonParser();
	 * JsonObject object = (JsonObject) parser.parse(serverBotJsonStr);// response
	 * will be the json String ServerBotModel serverBotModel= gson.fromJson(object,
	 * ServerBotModel.class);
	 * 
	 * System.out.println(serverBotModel.getwOID());
	 * System.out.println(inputZip.getOriginalFilename());
	 * 
	 * } catch (Exception e){ e.printStackTrace(); res.setApiSuccess(false);
	 * res.setResponseMsg("Error in startServerBot:: "+e.getMessage()); }
	 * 
	 * return res; //return woExecutionService.startWorkOrderTask(wOID,
	 * taskID,stepID, flowChartDefID, signumID, decisionValue, exeType,
	 * botPlatform); }
	 */

	@RequestMapping(value = "/startServerBot", method = RequestMethod.POST)
	public RpaApiResponse startServerBot(@RequestParam(value = "inputZip", required = false) MultipartFile inputZip,
			@RequestParam("serverBotJsonStr") String serverBotJsonStr,
			@RequestParam("botConfigJson") String botConfigJson) {
		return woExecutionService.startServerBot(inputZip, serverBotJsonStr, botConfigJson);
	}

	@AuditEnabled
	@RequestMapping(value = "/startTask", method = RequestMethod.POST)
	public Map<String, Object> startTask(@RequestParam(value = "inputZip", required = false) MultipartFile inputZip,
			@RequestParam(value = "serverBotModel") String serverBotModel,
			@RequestParam(value = "botConfigJson", required = false) String botConfigJson) throws Exception {
		return woExecutionService.startTask(inputZip, AppUtil.convertHtmlJsonToClassObject(serverBotModel, ServerBotModel.class), botConfigJson);
		
	}

	@RequestMapping(value = "/getEfficiencyDevlieryIndexForUser", method = RequestMethod.GET)
	public HashMap<String, Object> getEfficiencyDevlieryIndexForUser(@RequestParam(value = "projectID") int projectID,
			@RequestParam(value = "subActivity", required = false) String subActivityID,
			@RequestParam(value = "flowChartDefID", required = false) int flowChartDefID,
			@RequestParam(value = "woID") int woID, @RequestParam(value = "markAsComplete") boolean markAsComplete,
			@RequestParam(value = "signumID", required = false) String signumID) {
		return woExecutionService.getEfficiencyDevlieryIndexForUser(projectID, subActivityID, flowChartDefID, woID,
				markAsComplete, signumID);
	}

	@RequestMapping(value = "/getEfficiencyDevlieryPerformance", method = RequestMethod.GET)
	public HashMap<String, Object> getEfficiencyDevlieryPerformance(@RequestParam(value = "projectID") int projectID,
			@RequestParam(value = "subActivity", required = false) String subActivityID,
			@RequestParam(value = "flowChartDefID", required = false) int flowChartDefID,
			@RequestParam(value = "woID") int woID, @RequestParam(value = "markAsComplete") boolean markAsComplete,
			@RequestParam(value = "signumID", required = false) String signumID) {
		return woExecutionService.getEfficiencyDevlieryPerformance(projectID, subActivityID, flowChartDefID, woID,
				markAsComplete, signumID);
	}
	/*Optimized code*/
	@RequestMapping(value = "/v1/getWorkOrderProgress", method = RequestMethod.GET)
	public ResponseEntity<Response<WorkOrderProgressResultModel>> getWorkOrderProgressV1(@RequestParam(value = "workOrderId") Integer workOrderId) {
		return woExecutionService.getWorkOrderProgressV1(workOrderId);
	}
	@RequestMapping(value = "/getWorkOrderProgress", method = RequestMethod.GET)
	public Response<HashMap<String, String>> getWorkOrderProgress(@RequestParam(value = "workOrderId") Integer workOrderId) {
		return woExecutionService.getWorkOrderProgress(workOrderId);
	}

	@RequestMapping(value = "/saveExternalExecDetails", method = RequestMethod.POST)
	public boolean saveExternalExecReference(@RequestBody ExternalExecutionDetailsModel externalRef) {
		return woExecutionService.saveExternalExecDetails(externalRef);
	}

	@RequestMapping(value = "/getCurrentWorkOrderList/{signumID}/{isApproved}", method = RequestMethod.GET)
	public List<CurrentWorkOrderModel> getCurrentWorkOrderDetailsUI(@PathVariable("signumID") String signumID,
			@PathVariable("isApproved") String isApproved) {
		List<CurrentWorkOrderModel> workOrder = woExecutionService.getCurrentWorkOrderList(signumID, isApproved);

		return workOrder;
	}

	@RequestMapping(value = "/getBookingDetailsByBookingId/{bookingId}", method = RequestMethod.GET)
	public BookingDetailsModel getBookingDetailsByBookingId(@PathVariable("bookingId") int bookingId) {
		return woExecutionService.getBookingDetailsByBookingId(bookingId);
	}

	@RequestMapping(value = "/getAlreadyStartedBookingsByWoID/{wOID}", method = RequestMethod.GET)
	public Response<Void> getAlreadyStartedBookingsByWoID(@PathVariable("wOID") int wOID) throws ParseException {
		return woExecutionService.getAlreadyStartedBookingsByWoID(wOID);

	}

	@RequestMapping(value = "/updateDeliverableOrderNodes", method = RequestMethod.POST)
	public Response<Void> updateDeliverableOrderNodes(@RequestBody WorkOrderNetworkElementModel  workOrderNetworkElementModel) {
		LOG.info("TRANSACTIONDATA.TBL_WORK_ORDER_NODES: Success");
		return woExecutionService.updateDeliverableOrderNodes(workOrderNetworkElementModel);
	}

	/**
	 * 
	 * @param workOrderID
	 * @return List<String>
	 */
	@RequestMapping(value = "/getWorkOrderSuccessReasons", method = RequestMethod.GET)
	public ResponseEntity<Response<List<DeliverableSuccessReasonModel>>> getWorkOrderSuccessReasons(
			@RequestParam(value = "wOID") int workOrderID) {

		return woExecutionService.getWorkOrderSuccessReasons(workOrderID);
	}

	/**
	 * 
	 * @param signum
	 * @param deliverableSuccessReasonModel
	 * @return Response<Void>
	 */
	@RequestMapping(value = "/addDeliverableSuccessReason", method = RequestMethod.POST)
	public Response<Void> addDeliverableSuccessReason(@RequestHeader(value = "Signum") String signum,
			@RequestBody DeliverableSuccessReasonModel deliverableSuccessReasonModel) {
		return woExecutionService.addDeliverableSuccessReason(signum, deliverableSuccessReasonModel);
	}

	@RequestMapping(value = "/getAllSuccessReasons", method = RequestMethod.GET)
	public List<DeliverableSuccessReasonModel> getAllSuccessReasons(
			@RequestParam(value = "deliverableUnitId") int deliverableUnitId) {

		return woExecutionService.getAllSuccessReasons(deliverableUnitId);
	}
	
	@RequestMapping(value = "/saveReasonStatus", method = RequestMethod.POST)
	public Response<Void> saveReasonStatus(@RequestHeader(value = "Signum") String signum,
			@RequestParam(value="flag",required=false) boolean flag,
			@RequestBody DeliverableSuccessReasonModel deliverableSuccessReasonModel) {		
	  		return this.woExecutionService.saveReasonStatus(signum,flag,deliverableSuccessReasonModel);
	  	
	}
	
	/**
	 * @Purpose-to make entry in work order flowchart step details for decision value selected
	 * on the basis child step ID and refactor UI flowchart accordingly
	 * @param stepDetailsModel
	 * @return
	 */
	@RequestMapping(value = "/addStepDetailsForFlowChartByChildStep", method = RequestMethod.POST)
	public Response<Void> addStepDetailsForFlowChartByChildStep(@RequestBody StepDetailsModel stepDetailsModel) {
		return woExecutionService.addStepDetailsForFlowChartByChildStep(stepDetailsModel);
	}
	
	/**
	 * @purpose to get markets,WOID by NodeName,
	 * @param workOrderNodesModel
	 * @return List<WorkOrderNodesModel>
	 */
	@RequestMapping(value = "/getMarkets", method = RequestMethod.POST)
	public Response<WorkOrderMarketModel> getMarkets(@RequestBody WorkOrderColumnMappingModel workOrderColumnMappingModel) {
		LOG.info("BEGIN:getMarkets for WOID:"+workOrderColumnMappingModel.getwOID());
		return woExecutionService.getMarkets(workOrderColumnMappingModel);
	}
	
	@RequestMapping(value = "/getMarketsByWorkOrders", method = RequestMethod.POST)
	public Response<List<WorkOrderMarketModel>> getMarketsByWorkOrders(@RequestBody List<WorkOrderColumnMappingModel> workOrders) {
		LOG.info(String.format(AppConstants.API_BEGIN, "getMarketsByWorkOrders"));
		return woExecutionService.getMarketsByWorkOrders(workOrders);
	}
	
	@RequestMapping(value = "/getWFUserProficiency", method = RequestMethod.GET)
	public Response<UserWorkFlowProficencyModel> getWFUserProficiency(
			@RequestParam(value = "signumID") String signumID,
			@RequestParam(value = "subactivityID") int subactivityID,
			@RequestParam(value = "WFID") int wfID,
			@RequestParam(value = "projectID") int projectID,
			@RequestParam(value = "defID") int defID) {

		return woExecutionService.getWFUserProficiency(signumID, subactivityID, wfID, projectID, defID);
	}
	
	@RequestMapping(value = "/saveWFUserProficiency", method = RequestMethod.POST)
	public Response<Void> saveWFUserProficiency(
			@RequestBody WorkFlowUserProficiencyModel workFlowUserProficiencyModel) {	
		
	  		return this.woExecutionService.saveWFUserProficiency(workFlowUserProficiencyModel);
	  	
	}
	

	@RequestMapping(value = "/getDecisionStepDetails/{stepID}/{defID}/{woID}", method = RequestMethod.GET)
	public Response<DescisionStepDetailsModel> getDecisionStepDetails(@PathVariable("stepID") String stepID,
			@PathVariable("defID") Integer defID,
			@PathVariable("woID") Integer woID) {
		return woExecutionService.getDecisionStepDetails(stepID, defID, woID);
	}
	
	/**
	 * @author ekmravu
	 * @purpose To fetch stepName & StepID on the basis of woid
	 * @param wOID
	 * @return ResponseEntity<Response<List<WorkflowStepDetailModel>>>
	 * @throws Exception 
	 */
	@RequestMapping(value = "/getAllStepsByWOID", method = RequestMethod.GET)
	public ResponseEntity<Response<List<WorkflowStepDetailModel>>> getAllStepsByWOID (
			@RequestParam(value = "woID") int woID){
		LOG.info(String.format(AppConstants.API_BEGIN, " getAllStepsByWOID - " + woID ));
		return woExecutionService.getAllStepsByWOID(woID);	
		}
	
	/**
	 * @author ekarmuj
	 * @return DownloadTemplateModel
	 * @throws IOException
	 */
	
	@RequestMapping(value = "/downloadWorkHistoryData", method = RequestMethod.GET)
	public DownloadTemplateModel downloadWorkHistoryData(@RequestParam(value = "signum", required = true) String signum,
	@RequestParam(value = "startDate", required = false) String startDate,
	@RequestParam(value = "endDate", required = false) String endDate,
	@RequestParam(value = "woStatus", required = false) String woStatus) throws IOException {
		LOG.info("downloadWorkHistoryData : Success");
		return this.woExecutionService.downloadWorkHistoryData(signum,startDate,endDate,woStatus);
	}
	
	/**
	 * @author elkpain
	 * @purpose To get list of node .
	 * @param wOID
	 * @return ResponseEntity<Response<List<WorkOrderNodesModel>>>
	 * @throws Exception 
	 */
	@RequestMapping(value = "/getListOfNode", method = RequestMethod.GET)
	public ResponseEntity<Response<List<WorkOrderNodesModel>>> getListOfNode(
			@RequestParam(value = "wOID", required = true) int wOID) {
	
  	    LOG.info("getListOfNode :SUCCESS");
		return woExecutionService.getListOfNode(wOID);

	}
	
	/**
	 * @purpose To get installed desktop version.
	 * @author ekarmuj
	 * @param signum
	 * @return ResponseEntity<Response<Boolean>>
	 */
	
	@RequestMapping(value = "/isInstalledDesktopVersionUpdated", method = RequestMethod.GET)
	public ResponseEntity<Response<Map<String, Object>>> isInstalledDesktopVersionUpdated(@RequestParam(value = "signum", required = true) String signum)
	{
		return woExecutionService.isInstalledDesktopVersionUpdated(signum);
	}
	
	/**
	 * Api name: woExecution/getCompleteWoDetails
	 * Purpose : This Api used to getCompleteWorkorder details from WEB(UI).Need to create because external
	 * user not get impacted with response.
	 * @param wOID
	 * @return Response<List<WoCompleteDetailsModel>> 
	 */
	
	@RequestMapping(value = "/getCompleteWoDetails/{wOID}", method = RequestMethod.GET)
	public Response<List<WoCompleteDetailsModel>> getCompleteWoDetails(@PathVariable("wOID") int wOID) {
		return woExecutionService.getCompleteWoDetails(wOID);
	}
	
    /**
	 * API Name: woExecution/getAssignedToSignumByWOID
	 * @author elkpain
	 * @purpose gets the signum of person to whom the wo is assigned.
	 * @param wOID
	 * @return ResponseEntity<Response<String>>
	 * @throws Exception 
	 */
	@GetMapping(value = "/getAssignedToSignumByWOID")
	public ResponseEntity<Response<String>> getAssignedToSignumByWOID(@RequestParam(value="wOID", required=true) int wOID) {
	
  	    LOG.info("getAssignedToSignumByWOID:SUCCESS");
  	    return woExecutionService.getAssignedToSignumByWOID(wOID);

	}	
	
	@RequestMapping(value = "/getProficiencyId", method = RequestMethod.GET)
	public ResponseEntity<Response<Integer>> getProficiencyId (
			@RequestParam(value="wOID", required=true) String wOID,
			@RequestParam(value="signum", required=false) String signum)
	{
		return woExecutionService.getProficiencyId(wOID,signum);
	}
	
	/**
	 * API Name: woExecution/downloadProjectQueueWorkOrders
	 * 
	 * @author elkpain
	 * @purpose downloads Project Queue Work Orders data.
	 * @return DownloadTemplateModel
	 * @throws IOException
	 */
	@GetMapping(value = "/downloadProjectQueueWorkOrders")
	public DownloadTemplateModel downloadWorkHistoryData(@RequestParam(value = "signum", required = true) String signum,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "priority", required = false) String priority,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "doStatus", required = false) String doStatus,
			@RequestParam(value = "projectIdList", required = false) String projectIdList) throws IOException {

		LOG.info("downloadProjectQueueWorkOrders : SUCCESS");
		return this.woExecutionService.downloadProjectQueueWorkOrders(signum, startDate, endDate, priority, status,
				doStatus, projectIdList);
	}

	
	/**
	 * API Name: woExecution/validateServerBotInputUrl
	 * 
	 * @author elkpain
	 * @purpose validates Input Url.
	 * @return void
	 * @throws IOException
	 */
	@GetMapping(value = "/validateServerBotInputUrl")
	public ResponseEntity<Response<Boolean>> validateServerBotInputUrl (
			@RequestParam(value="inputUrl", required=true) String inputUrl,
			@RequestParam(value="projectID", required=true) int projectID)
	{
		return woExecutionService.validateServerBotInputUrl(inputUrl,projectID);
	}
	
	/**
	 * API Name: woExecution/validateSharepointDetailsWithMarketArea
	 * 
	 * @author elkpain
	 * @purpose validates Input Url.
	 * @return void
	 * @throws IOException
	 */
	@GetMapping(value = "/validateSharepointDetailsWithMarketArea")
	public ResponseEntity<Response<Boolean>> validateSharepointDetailsWithMarketArea (
			@RequestParam(value="projectID", required=true) int projectID)
	{
		return woExecutionService.validateSharepointDetailsWithMarketArea(projectID);
	}
	
	
	/**
	 * API Name: woExecution/getSharepointUrl
	 * 
	 * @author elkpain
	 * @purpose get Url for sharepoint.
	 * @return void
	 * @throws IOException
	 */
	@GetMapping(value = "/getSharepointUrl")
	public ResponseEntity<Response<String>> getSharepointUrl()
	{
		return woExecutionService.getSharepointUrl();
	}
	
	
	/**
	 * API Name: woExecution/getSharepointConfigurationDetails
	 * 
	 * @author elkpain
	 * @purpose get Sharepoint Configuration Details.
	 * @return void
	 * @throws IOException
	 */
	@GetMapping(value = "/getSharepointConfigurationDetails")
	public ResponseEntity<Response<List<SharePointDetailModel>>> getSharepointConfigurationDetails()
	{
		return woExecutionService.getSharepointConfigurationDetails();
	}
	

	/**
	 * 
	 * @param stepID
	 * @return
	 */
	@GetMapping(value = "/getOutPutUploadByStepID")
	public ResponseEntity<Response<String>> getOutPutUploadByStepID(
			@RequestParam(value = "stepID", required = true) String stepID,
			@RequestParam(value = "flowChartDefID", required = true) int flowChartDefID) {
		return woExecutionService.getOutPutUploadByStepID(stepID, flowChartDefID);
	}
}
