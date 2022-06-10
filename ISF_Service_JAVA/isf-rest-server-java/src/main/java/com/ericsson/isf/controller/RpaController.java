/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.ericsson.isf.model.BotConfig;
import com.ericsson.isf.model.BotInputFileModel;
import com.ericsson.isf.model.BulkWorkOrderCreationModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.ErrorDetailsModel;
import com.ericsson.isf.model.ExceptionLogModel;
import com.ericsson.isf.model.RPAWorkOrderDetails;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.SoftHumanDumpModel;
import com.ericsson.isf.model.TestingBotDetailsModel;
import com.ericsson.isf.model.VideoURLModel;
import com.ericsson.isf.model.WorkOrderPlanObjectModel;
import com.ericsson.isf.model.botstore.RpaApiResponse;
import com.ericsson.isf.service.BotStoreService;
import com.ericsson.isf.service.OutlookAndEmailService;
import com.ericsson.isf.service.RpaService;
import com.ericsson.isf.service.WorkOrderPlanService;
import com.microsoft.azure.storage.StorageException;

/**
 *
 * @author ekumvsu
 */
@RestController
@RequestMapping("/rpaController")
//@Validated
public class RpaController {

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

	@Autowired
	private RpaService rpaService;
	@Autowired
	private WorkOrderPlanService workOrderPlanService;

	@Autowired
	private BotStoreService botStoreService;

	@Autowired
	private OutlookAndEmailService emailService;

//Depricating this API  
	@RequestMapping(value = "/createRPAWorkOrder/{projectID}/{subActivityID}/{signumID}/{nodeType}/{nodeNames}/{date}/{woName}", method = RequestMethod.POST)
	public String createRPAWorkOrder(@PathVariable("projectID") int projectID,
			@PathVariable("subActivityID") int subActivityID, @PathVariable("signumID") String signumID,
			@PathVariable("nodeType") String nodeType, @PathVariable("nodeNames") String nodeNames,
			@PathVariable("date") String date, @PathVariable("woName") String woName) {

		String woID = "This API is deprecated,Please use this API EndPoint - externalInterface/createWorkOrderPlan";
//        try{
//           woID =  this.rpaService.createRPAWorkOrder(projectID,subActivityID,signumID,nodeType,nodeNames,date,woName);
//           LOG.info("ADHOC : SUCCESS");
//        }catch(Exception ex){
//            LOG.error("ERROR while creating RPAWO:"+ex.getMessage());
//            ex.printStackTrace();
//        }
		return woID;
	}

	@RequestMapping(value = "/startAutomatedTask", method = RequestMethod.POST)
	public String startAutomatedTask(@RequestParam(value = "woID")  Integer woID,
			@RequestParam(value = "taskName") String taskName,  String date) {
		String bookingValue = "BookingID:";
		
		bookingValue += this.rpaService.startAutomatedTask(woID, taskName, date);
		LOG.info("startWO for RPA is success... It's Booking ID: " + bookingValue);
		
		return bookingValue;
	}

	@RequestMapping(value = "/getInprogressTask/{signum}", method = RequestMethod.GET)
	public List<LinkedHashMap<String, Object>> getInprogressTask(@PathVariable("signum") String signum) {
		List<LinkedHashMap<String, Object>> result = this.workOrderPlanService.getInprogressTask(signum).getBody().getResponseData();
		return result;
	}

	@RequestMapping(value = "/getNextStepData/{stepID}/{defID}", method = RequestMethod.GET)
	public List<Map<String, Object>> getNextStepData(@PathVariable("stepID") String stepID,
			@PathVariable("defID") Integer defID) {
		List<Map<String, Object>> result = this.workOrderPlanService.getNextStepData(stepID, defID);
		return result;
	}

	@RequestMapping(value = "/closeRpaWO", method = RequestMethod.POST)
	public String closeRpaWO(@RequestParam(value = "woID") int woID, @RequestParam(value = "signumID") String signumID,
			@RequestParam(value = "closedOn") String closedOn) {
		return this.rpaService.closeRpaWO(woID, signumID, closedOn);
	}

	@RequestMapping(value = "/completeAutomatedTask", method = RequestMethod.POST)
	public String completeAutomatedTask(@RequestParam(value = "woID", required = true)  int woID,
			@RequestParam(value = "taskName", required = true)  String taskName,
			@RequestParam(value = "date", required = true) String date,
			@RequestParam(value = "reason", required = false) String reason) {
			LOG.info("complete Task : Success");
			return this.rpaService.completeAutomatedTask(woID, taskName, date, reason);
	}

	@RequestMapping(value = "/getRPAWorkOrderDetails/{projectID}", method = RequestMethod.GET)
	public List<RPAWorkOrderDetails> getRPAWorkOrderDetails(@PathVariable("projectID") int projectID) {
		List<RPAWorkOrderDetails> rpaAdhocWorkOrders = this.rpaService.getRPAWorkOrderDetails(projectID);
		return rpaAdhocWorkOrders;

	}

	@RequestMapping(value = "/getRPADeployedDetails/{projectID}", method = RequestMethod.GET)
	public List<SoftHumanDumpModel> getRPADeployedDetails(@PathVariable("projectID") int projectID) {
		return this.rpaService.getRPADeployedDetails(projectID);

	}

	/**
	 * @param rpaRequestID
	 * @return
	 */
	@RequestMapping(value = "/getAllRPARequestDetails/{rpaRequestID}", method = RequestMethod.GET)
	public List<HashMap<String, Object>> getAllRPARequestDetails(@PathVariable("rpaRequestID") int rpaRequestID) {

		return this.rpaService.getAllRPARequestDetails(rpaRequestID);
	}

	@RequestMapping(value = "/updateVideoURL", method = RequestMethod.POST)
	public RpaApiResponse updateVideoURL(@RequestBody VideoURLModel videoUrlModel) {
		
		return this.rpaService.updateVideoURL(videoUrlModel);

	}

	/**
	 * 
	 * @param signum
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "/getBotIDByWFSignum/{signum}", method = RequestMethod.GET)
	public Map<String, Object> getBotIDByWFSignum(@PathVariable("signum") String signum) {
		Map<String, Object> data = new HashMap<String, Object>();
		String msg = StringUtils.EMPTY;
		boolean isvalid = true;
		List<Map<String, Object>> result = this.workOrderPlanService.getBotIDByWFSignum(signum);

		if (result.isEmpty()) {
			msg = "No Bot found for the given Signum";
			isvalid = false;
		} else {
			msg = "Found";

			try {
				result = botStoreService.getBotCurrentStatusForApproval(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		data.put("isvalid", isvalid);
		data.put("msg", msg);
		data.put("data", result);

		return data;
	}

	@RequestMapping(value = "/getWoIDByProjectID/{projectID}/{subactivityID}/{subActivityFlowChartDefID}", method = RequestMethod.GET)
	public Map<String, Object> getWoIDByProjectID(@PathVariable("projectID") String projectID,
			@PathVariable("subactivityID") String subactivityID,
			@PathVariable("subActivityFlowChartDefID") String subActivityFlowChartDefID) {
		Map<String, Object> data = new HashMap<String, Object>();
		String msg = "";
		boolean isvalid = true;
		List<Map<String, Object>> result = this.workOrderPlanService.getWoIDByProjectID(projectID, subactivityID,
				subActivityFlowChartDefID);

		if (result.isEmpty()) {
			msg = "No Data Found!!";
			isvalid = false;
		} else {
			msg = "Found";
		}

		data.put("isvalid", isvalid);
		data.put("msg", msg);
		data.put("data", result);

		return data;
	}

	@RequestMapping(value = "/getLatestVersionOfWfBySubactivityID", method = RequestMethod.GET)
	public Map<String, Object> getLatestVersionOfWfBySubactivityID(@RequestParam(value = "projectID") String projectID,
			@RequestParam(value = "subactivityID") String subactivityID,
			@RequestParam(value = "subActivityFlowChartDefID") String subActivityFlowChartDefID,
			@RequestParam(value = "workFlowName") String workFlowName, @RequestParam(value = "wfid") int wfid) {
		Map<String, Object> data = new HashMap<String, Object>();
		String msg = "";
		boolean isvalid = true;
		List<Map<String, Object>> result = this.workOrderPlanService.getLatestVersionOfWfBySubactivityID(projectID,
				subactivityID, subActivityFlowChartDefID, workFlowName, wfid);

		if (result.isEmpty()) {
			msg = "No Data Found!!";
			isvalid = false;
		} else {
			msg = "Found";
		}

		data.put("isvalid", isvalid);
		data.put("msg", msg);
		data.put("data", result);

		return data;
	}

	@RequestMapping(value = "/createAutomatedWorkOrder", method = RequestMethod.POST)
	public Map<String, Object> createAutomatedWorkOrder(@RequestBody BulkWorkOrderCreationModel bulkModel) {
		Map<String, Object> response = new HashMap<>();
		response.put("Message",
				"This API is deprecated,Please use this API EndPoint - externalInterface/createStagingWorkOrderPlan");
		// return this.rpaService.createAutomatedWorkOrder(bulkModel);
		return response;
	}

	@RequestMapping(value = "/stopStepByReferenceId", method = RequestMethod.GET)
	public Map<String, String> stopStepByReferenceId(@RequestParam(value = "referenceId") String referenceId,
			@RequestParam(value = "status") String status) {
		return this.rpaService.stopStepByReferenceId(referenceId, status);
	}

	@RequestMapping(value = "/startStepByReferenceId", method = RequestMethod.GET)
	public Map<String, String> startStepByReferenceId(@RequestParam(value = "referenceId") String referenceId) {

		return this.rpaService.startStepByReferenceId(referenceId);
	}

	/**
	 * 
	 * @param type
	 * @param referenceId
	 * @return BotConfig
	 */
	@RequestMapping(value = "/getBotConfig", method = RequestMethod.GET)
	public BotConfig getBotConfig(@RequestParam(value = "type") String type,
			@RequestParam(value = "referenceId") String referenceId) {
		
		return this.rpaService.getBotConfig(type, referenceId);
	}

	@RequestMapping(value = "/getBoTsForExplore/{taskId}", method = RequestMethod.GET)
	public List<HashMap<String, Object>> getBoTsForExplore(@PathVariable("taskId") int taskId)
			throws IOException, SQLException {
		List<HashMap<String, Object>> rpaDeployedBotList = rpaService.getBoTsForExplore(taskId);
		return rpaDeployedBotList;
	}

	@RequestMapping(value = "/getBotConfigForBooking", method = RequestMethod.GET)
	public ResponseEntity<Response<Map<String, Object>>> getBotConfigForBooking(@RequestParam(value = "botId") int botId,
			@RequestParam(value = "bookingId") int bookingId) {
		return this.rpaService.getBotConfigForBooking(botId, bookingId);
	}

	@RequestMapping(value = "/createMailerForBot", method = RequestMethod.POST)
	public void createMailerForBot(@RequestParam(value = "botId") int botId, @RequestParam(value = "woid") int woid,
			@RequestParam(value = "status") String status, @RequestParam(value = "signum") String signum) {
		this.rpaService.createMailerForBot(botId, woid, status, signum);
	}

	@RequestMapping(value = "/downloadTemplateFile", method = RequestMethod.GET)
	private DownloadTemplateModel downloadTemplateFile(HttpServletResponse response,
			@RequestParam("templateName") String templateName) throws IOException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();
		try {

			byte[] file = rpaService.downloadTemplateFile(templateName);

			downloadTemplateModel.setpFileContent(file);
			downloadTemplateModel.setpFileName(templateName + ".xlsx");

//        	if(file.length>0)
//        	{
//        		String fileName=templateName;
//        		fileName=fileName+".xlsx";
//        		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        		response.setContentLength(file.length);
//        		response.setHeader("Content-Disposition","attachment;filename=\"" + fileName + "\"");
//        		
//        		
//        		
//        		
//        		InputStream is = new ByteArrayInputStream(file);
//        		is.read(file);
//        		ServletOutputStream op = response.getOutputStream();
//        		op.write(file);
//        		op.flush();
//        		 LOG.info("Download Request: SUCCESS");
//        	}
//        	else
//        	{
//        		 LOG.info("Download Request: FAIL");
//        	}
		} catch (Exception e) {
			LOG.info("Download Request: FAIL");
			e.printStackTrace();
		}

		return downloadTemplateModel;
	}

	@RequestMapping(value = "/downloadTemplateFileCsv", method = RequestMethod.GET)
	private DownloadTemplateModel downloadTemplateFileCsv(HttpServletResponse response,
			@RequestParam("templateName") String templateName) throws IOException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();
		try {

			byte[] file = rpaService.downloadTemplateFile(templateName);

			downloadTemplateModel.setpFileContent(file);
			downloadTemplateModel.setpFileName(templateName + ".csv");

//        	if(file.length>0)
//        	{
//        		String fileName=templateName;
//        		fileName=fileName+".xlsx";
//        		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        		response.setContentLength(file.length);
//        		response.setHeader("Content-Disposition","attachment;filename=\"" + fileName + "\"");
//        		
//        		
//        		
//        		
//        		InputStream is = new ByteArrayInputStream(file);
//        		is.read(file);
//        		ServletOutputStream op = response.getOutputStream();
//        		op.write(file);
//        		op.flush();
//        		 LOG.info("Download Request: SUCCESS");
//        	}
//        	else
//        	{
//        		 LOG.info("Download Request: FAIL");
//        	}
		} catch (Exception e) {
			LOG.info("Download Request: FAIL");
			e.printStackTrace();
		}

		return downloadTemplateModel;
	}
	
	
	@RequestMapping(value = "/uploadTemplateFile", method = RequestMethod.POST, consumes = "multipart/form-data")
	public String uploadTemplateFile(@RequestPart("templateFile") MultipartFile templateFile,
			@RequestParam("templateName") String templateName, @RequestParam("signum") String signum) {
		String status = rpaService.uploadTemplateFile(templateFile, templateName, signum);
		if (status.contains("SUCCESS")) {
			LOG.info("botUpload InputOutput Request: SUCCESS");
		} else {
			LOG.info("botUpload InputOutput Request: FAIL");
		}

		return status;
	}

	@RequestMapping(value = "/createWorkOrderPlan", method = RequestMethod.POST)
	public Response<Void> createWorkOrderPlan(@RequestBody WorkOrderPlanObjectModel workOrderPlanObjectModel)
			throws InterruptedException {
		
		return rpaService.createWorkOrderPlan(workOrderPlanObjectModel);
	}

	@RequestMapping(value = "/getRPADomain", method = RequestMethod.GET)
	public Response<Map<String, Object>> getRPADomain(
			@RequestParam(value = "marketarea", required = false) String marketarea) {
		return this.rpaService.getRPADomain(marketarea);
	}

	@RequestMapping(value = "/getRPASubactivity", method = RequestMethod.GET)
	public Response<Map<String, Object>> getRPASubactivity(
			@RequestParam(value = "marketarea", required = false) String marketarea,
			@RequestParam(value = "domainId", required = true) Integer domainId) {
		return this.rpaService.getRPASubactivity(marketarea, domainId);

	}

	@RequestMapping(value = "/getRPATechnology", method = RequestMethod.GET)
	public Response<Map<String, Object>> getRPATechnology(
			@RequestParam(value = "domainId", required = true) Integer domainId,
			@RequestParam(value = "market", required = false) String market,
			@RequestParam(value = "subActivityId", required = true) Integer subActivityId) {
		return rpaService.getRPATechnology(domainId, market, subActivityId);
	}

	@RequestMapping(value = "/getRPATask", method = RequestMethod.GET)
	public Response<Map<String, Object>> getRPATask(@RequestParam(value = "domainId", required = true) Integer domainId,
			@RequestParam(value = "market", required = false) String market,
			@RequestParam(value = "subActivityId", required = true) Integer subActivityId,
			@RequestParam(value = "technologyId", required = true) Integer technologyId) {
		return rpaService.getRPATask(domainId, market, subActivityId, technologyId);
	}

	/**
	 * 
	 * @param domainid
	 * @param technologyId
	 * @param subactivityId
	 * @param taskId
	 * @param marketarea
	 * @return Response<Map<String,Object>>
	 */
	@RequestMapping(value = "/getRPABOTDetails", method = RequestMethod.GET)
	public Response<Map<String, Object>> getRPABOTDetails(
			@RequestParam(value = "domainid", required = false) Integer domainid,
			@RequestParam(value = "technologyId", required = false) Integer technologyId,
			@RequestParam(value = "subactivityId", required = false) Integer subactivityId,
			@RequestParam(value = "taskId", required = false) Integer taskId,
			@RequestParam(value = "marketarea", required = false) String marketarea) {
		
		return this.rpaService.getRPABOTDetails(domainid, technologyId, subactivityId, taskId, marketarea);
	}

	@RequestMapping(value = "/getTestingBotDetails/{rpaRequestId}", method = RequestMethod.GET)
	public TestingBotDetailsModel getTestingBotDetails(@PathVariable("rpaRequestId") String rpaRequestId) {
		return this.rpaService.getTestingBotDetails(rpaRequestId);
	}

	@RequestMapping(value = "/saveExceptionLog", method = RequestMethod.POST)
	public ResponseEntity<Response<Void>> saveExceptionLog(@RequestBody ExceptionLogModel exceptionLog) {
		return this.rpaService.saveExceptionLog(exceptionLog);
	}
	
	 /**
     * Save Softhuman exception logs
     * @deprecated
     * This api is no longer acceptable to Save Softhuman exception logs.
     *
     * @param ExceptionLogModel 
     * @return Response<Void>
     */
	//@Deprecated
	/*
	 * @RequestMapping(value = "/saveSHExceptionLog", method = RequestMethod.POST)
	 * public Response<Void> SaveSHExceptionLog(@RequestBody ExceptionLogModel
	 * exceptionLog) { return this.rpaService.SaveSHExceptionLog(exceptionLog); }
	 */
	
	
	/**
     * Save BOT exception logs
     * @deprecated
     * This api is no longer acceptable to Save BOT exception logs.
     *
     * @param ExceptionLogModel 
     * @return Response<Void>
     */
	//@Deprecated
	/*
	 * @RequestMapping(value = "/saveBotExceptionLog", method = RequestMethod.POST)
	 * public Response<Void> SaveBotExceptionLog(@RequestBody ExceptionLogModel
	 * exceptionLog) { return this.rpaService.SaveBotExceptionLog(exceptionLog); }
	 */

	@RequestMapping(value = "/getTaskDetails/{subActivityID}", method = RequestMethod.GET)
	public List<HashMap<String, Object>> getTaskDetails(@PathVariable("subActivityID") int subActivityID) {
		return this.rpaService.getTaskDetails(subActivityID);
	}

//	@RequestMapping(value = "/readMailBox", method = RequestMethod.GET)
//	public void readMailbox() throws Exception {
//		 this.emailService.readMailbox();
//	}

	@RequestMapping(value = "/getRPAIsRunOnServer/{rpaRequestID}", method = RequestMethod.GET)
	public HashMap<String, Object> getRPAIsRunOnServer(@PathVariable("rpaRequestID") int rpaRequestID) {
		return this.rpaService.getRPAIsRunOnServer(rpaRequestID);
	}

	// checks if user is Bot developer of Bot or PM/DR of the project in which the
	// request was raised
	@RequestMapping(value = "/getIsUserValidForBotChanges", method = RequestMethod.GET)
	public Response<Boolean> getIsUserValidForBotChanges(@RequestParam("signum") String signum,
			@RequestParam("botID") int botID) {
		return this.rpaService.getIsUserValidForBotChanges(signum, botID);
	}

	@RequestMapping(value = "/getBotInputFile", method = RequestMethod.GET)
	public Response<BotInputFileModel> getBotInputFile(@RequestParam("signum") String signum, @RequestParam("woid") int woid,
			@RequestParam("taskid") int taskid, @RequestParam("stepid") String stepid,
			@RequestParam("bookingid") int bookingid) throws IOException, URISyntaxException, StorageException {
		return this.rpaService.getBotInputFile(signum, woid, taskid, stepid, bookingid);
	}
	
	@RequestMapping(value = "/getNewAuthorizationTokenApiM", method = RequestMethod.GET)
	public Response<String> getNewAuthorizationTokenApiM() {
		return this.rpaService.getNewAuthorizationTokenApiM();
	}
	
    /**
	 * API Name: rpaController/getAllErrorDetails
	 * @author elkpain
	 * @purpose This API gets all error details.
	 * @param sourceID
	 * @return Map<String, Object>
	 */
    @RequestMapping(value = "/getAllErrorDetails", method = RequestMethod.POST)
	public Map<String, Object> getAllErrorDetails(@RequestParam(value="sourceID", required = false) Integer sourceID, HttpServletRequest request) {
		
    	DataTableRequest dataTableReq = new DataTableRequest(request);
    	LOG.info("getAllErrorDetails :SUCCESS");
    	return this.rpaService.getAllErrorDetails(sourceID,dataTableReq);
    }
    
    
    /**
	 * API Name: rpaController/getErrorDetailsByCode
	 * @author ekarmuj
	 * @purpose This API used to gets  error details  based on errorCode.
	 * @param errorCode
	 * @return Response<List<ErrorDetailsModel>>
	 */
    
    @RequestMapping(value = "/getErrorDetailsByCode", method = RequestMethod.GET)
	public ResponseEntity<Response<List<ErrorDetailsModel>>> getErrorDetailsByCode(@RequestParam("errorCode") int errorCode)  {
		return this.rpaService.getErrorDetailsByCode(errorCode);
	}

    
    /**
	 * API Name: rpaController/addErrorDetail
	 * @author elkpain
	 * @purpose This API adds error detail.
	 * @param ErrorDetailsModel
	 * @return ResponseEntity<Response<Void>>
	 */
    @RequestMapping(value = "/addErrorDetail", method = RequestMethod.POST)
	public ResponseEntity<Response<Void>> addErrorDetail(@RequestBody ErrorDetailsModel errorDetailsModel) {
		
    	LOG.info("addErrorDetail :SUCCESS");
    	return this.rpaService.addErrorDetail(errorDetailsModel);
    }


}
