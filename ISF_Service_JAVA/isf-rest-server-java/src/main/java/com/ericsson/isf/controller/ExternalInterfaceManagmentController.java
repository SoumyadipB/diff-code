/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.model.BookingDetailsModel;
import com.ericsson.isf.model.CreateWoResponse;
import com.ericsson.isf.model.CreateWorkOrderModel;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.ErisiteDataModel;
import com.ericsson.isf.model.EventPublisherRequestModel;
import com.ericsson.isf.model.FeedModel;
import com.ericsson.isf.model.NetworkElementNewModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.SaveClosureDetailsForWOModel;
import com.ericsson.isf.model.SharePointDetailModel;
import com.ericsson.isf.model.StepDetailsModel;
import com.ericsson.isf.model.ViewNetworkElementModel;
import com.ericsson.isf.model.WorkOrderCompleteDetailsModel;
import com.ericsson.isf.model.WorkOrderCurrentStepDetailResponseModel;
import com.ericsson.isf.model.WorkOrderCurrentStepDetailsModel;
import com.ericsson.isf.model.WorkOrderModel;
import com.ericsson.isf.model.WorkOrderOutputFileModel;
import com.ericsson.isf.model.WorkOrderPlanObjectModel;
import com.ericsson.isf.model.botstore.ServerBotModel;
import com.ericsson.isf.service.AccessManagementService;
import com.ericsson.isf.service.ExternalInterfaceManagmentService;
import com.ericsson.isf.service.NetworkElementService;
import com.ericsson.isf.service.ReportManagementService;
import com.ericsson.isf.service.RpaService;
import com.ericsson.isf.service.WorkOrderPlanService;
import com.ericsson.isf.service.audit.AuditEnabled;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.validator.ValidString;
import com.microsoft.azure.storage.StorageException;

/**
 *
 * @author ejangua
 */

@RestController
@RequestMapping("/externalInterface")
@Validated
public class ExternalInterfaceManagmentController {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalInterfaceManagmentController.class);

	@Autowired /* Bind to bean/pojo */
    private ExternalInterfaceManagmentService eriSiteManagmentService;
    @Autowired
    private ReportManagementService reportManagmentService;

    @Autowired
    private AccessManagementService accessManagementService;
    

    @Autowired
    private WorkOrderPlanService workOrderPlanService;

    @Autowired
    private ExternalInterfaceManagmentService externalInterfaceManagmentService;

    @Autowired
    private RpaService rpaService;
    
    @Autowired
	private NetworkElementService networkElementService;


    @RequestMapping(value = "/checkIsfHealth", method = RequestMethod.GET)
	public Map<String, Object> checkIsfHealth() {
        return this.eriSiteManagmentService.checkIsfHealth();
    }

    @RequestMapping(value = "/apiCheck", method = RequestMethod.GET)
    public boolean apiCheck() {
        return true;
    }

    @RequestMapping(value = "/generateWOrkOrder", method = RequestMethod.POST)
	public boolean generateWOrkOrder(@RequestBody ErisiteDataModel erisiteDataModel,
			@RequestHeader HttpHeaders headers) {
		 LOG.info("Test API /generateWOrkOrder CONTROLLER :ELKPAIN");
		return this.eriSiteManagmentService.generateWOrkOrder(erisiteDataModel, headers);
    }

    /**
     * Api name:externalInterface/getExternalProjectID
     * @author ekarmuj
     * purpose: Api used to get external project based on source id .
     * @param sourceID
     * @return ResponseEntity<Response<List<Map<String, Object>>>>
     */
    
    @RequestMapping(value = "/getExternalProjectID/{sourceID}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Map<String, Object>>>> getExternalProjectID(@PathVariable int sourceID) {
    	return this.eriSiteManagmentService.getExternalProjectID(sourceID);
    }

    @RequestMapping(value = "/getAllExternalProjectIDByIsfProject/{sourceID}/{isfProjectID}", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllExternalProjectIDByIsfProject(@PathVariable int sourceID,
			@PathVariable int isfProjectID) {
		return this.eriSiteManagmentService.getAllExternalProjectIDByIsfProject(isfProjectID, sourceID);
    }
    
    /**
   	 * API Name: externalInterface/getExternalProjectIDByIsfProject/{sourceID}/{isfProjectID}
   	 * @author elkpain
   	 * @purpose This API is used to get External ProjectID By IsfProject.
   	 * @return ResponseEntity<Response<List<Map<String, Object>>>>
   	 */
       
     @RequestMapping(value = "/getExternalProjectIDByIsfProject/{sourceID}/{isfProjectID}", method = RequestMethod.GET)
   	  public ResponseEntity<Response<List<Map<String, Object>>>> getExternalProjectIDByIsfProject(@PathVariable int sourceID,
   			@PathVariable int isfProjectID)  {
    	    LOG.info("getExternalProjectIDByIsfProject:Success");
    	   return this.eriSiteManagmentService.getExternalProjectIDByIsfProject(isfProjectID, sourceID);
   	}

    @RequestMapping(value = "/getActiveExternalProjectID/{source}/{projectID}", method = RequestMethod.GET)
	public List<Map<String, Object>> getActiveExternalProjectID(@PathVariable String source,
			@PathVariable int projectID) {
    	return this.eriSiteManagmentService.getActiveExternalProjectID(source, projectID);
    }

    @RequestMapping(value = "/getActiveExternalWorkPlanTemplateList/{source}/{externalProjectID}", method = RequestMethod.GET)
	public List<Map<String, Object>> getActiveExternalWorkPlanTemplateList(@PathVariable String source,
			@PathVariable int externalProjectID) {
    	return this.eriSiteManagmentService.getActiveExternalWorkPlanTemplateList(source, externalProjectID);
    }

    /**
   	 * API Name: externalInterface/getActiveExternalActivityList
   	 * @author elkpain
   	 * @purpose This API is used to get Active External Activity List.
   	 * @param sourceId,projectId,externalProjectId,extWoTemp
   	 * @return ResponseEntity<Response<List<Map<String, Object>>>>
   	 */
       
     @RequestMapping(value = "/getActiveExternalActivityList", method = RequestMethod.GET)
   	  public ResponseEntity<Response<List<Map<String, Object>>>> getActiveExternalActivityList(@RequestParam("sourceId") String sourceID,
  			@RequestParam("projectId") int projectID, @RequestParam("externalProjectId") int isfProjectID,
  			@RequestParam("extWoTemp") String workPlanTemplateName)  {
    	    LOG.info("getActiveExternalActivityList:Success");
    	   return this.eriSiteManagmentService.getActiveExternalActivityList(sourceID, projectID, isfProjectID,
   				workPlanTemplateName );
   	}

    @RequestMapping(value = "/getAllExternalActivityList/{sourceID}", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllExternalActivityList(@PathVariable String sourceID) {
    	return this.eriSiteManagmentService.getAllExternalActivityList(sourceID);
    }

	@RequestMapping(value = "/downloadSapBO", method = RequestMethod.GET)
	public void downloadSapBO(@RequestParam("wpId") String wpId, @RequestParam("instance") String instance) {
		LOG.info("Report fetching started for SAP BO");
		this.eriSiteManagmentService.downloadSapBO("1098911,1099058 ,1096673 ,1098982", "americas",
				AppConstants.NUMBER_5);
		this.eriSiteManagmentService.downloadSapBO("2233815 ,2233813 ,2239456 ,2233812", "americas",
				AppConstants.NUMBER_11);
		this.eriSiteManagmentService.downloadSapBO("410656,548682", "apac", AppConstants.NUMBER_5);
		this.eriSiteManagmentService.downloadSapBO("669369", "emea", AppConstants.NUMBER_5);
	}

    @RequestMapping(value = "/getTableauReport", method = RequestMethod.GET)
    public boolean getTableauReport() {
    	return this.reportManagmentService.getTableauReport();
    }

    @RequestMapping(value = "/downloadSAPBOFile", method = RequestMethod.GET)
	private DownloadTemplateModel downloadSAPBOFile(HttpServletResponse response,
			@RequestParam("fileName") String fileName) throws IOException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();
		try {

			List<HashMap<String, Object>> list = eriSiteManagmentService.downloadWorkInstructionFile(fileName);
			if (list.size() > 0) {
				byte[] file = (byte[]) list.get(0).get("erisiteFile");
      		downloadTemplateModel.setpFileContent(file);
      		downloadTemplateModel.setpFileName(fileName);
				if (file.length > 0) {
					// String fileName=fileName;
					// fileName=fileName+"."+workInstructionModel.getFileType();
					// if(workInstructionModel.getFileType()=="pdf") {
          		  response.setContentType("text/csv");
					// }
					// if(workInstructionModel.getFileType()=="xlsx") {
					// response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					// }
          		response.setContentLength(file.length);
					response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

          		InputStream is = new ByteArrayInputStream(file);
          		is.read(file);
         		ServletOutputStream op = response.getOutputStream();
          		op.write(file);
          		op.flush();
          		 LOG.info("Download Request: SUCCESS");
          	}
			} else {
         		 LOG.info("Download Request: FAIL");
          	}

		} catch (Exception e) {
      		LOG.info("Download Request: FAIL");
      		e.printStackTrace();
      	}

   	return downloadTemplateModel;
      }

    @RequestMapping(value = "/createMailerForSAP", method = RequestMethod.POST)
	public void createMailerForBot(@RequestParam(value = "reportid") String reportid,
			@RequestParam(value = "instance") String instance, @RequestParam(value = "status") String status) {
		this.eriSiteManagmentService.createMailerForSAP(reportid, instance, status);
    }

    @RequestMapping(value = "/getAllExternalSource", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllExternalSource(@RequestParam(value="pageCounter", required = false) Optional<Boolean> pageCounter) {
    	
    	boolean b = pageCounter.isPresent() ? pageCounter.get() : false;
    	return eriSiteManagmentService.getAllExternalSource(b);
    }

    @RequestMapping(value = "/getAllowedApiListForExternalSource", method = RequestMethod.GET)
	public List<String> getAllowedApiListForExternalSource(
			@RequestParam(value = "externalSourceName") String externalSourceName) {
    	return eriSiteManagmentService.getAllowedApiListForExternalSource(externalSourceName);
    }

    @RequestMapping(value = "/getActiveToken", method = RequestMethod.GET)
	public String getActiveToken(@RequestParam(value = "externalSourceName") String externalSourceName) {
    	return accessManagementService.getActiveToken(externalSourceName);
    }

    @AuditEnabled
    @RequestMapping(value = "/createWorkOrderPlan", method = RequestMethod.POST)
	public Response<CreateWoResponse> createWorkOrderPlan(@Valid @RequestBody CreateWorkOrderModel wOPlanObject)
			 {
		return workOrderPlanService.createWorkOrderPlanForExternal(wOPlanObject);
       // LOG.info("TBL_WORK_ORDER_PLAN: SUCCESS");

    }
    
    @AuditEnabled
    @RequestMapping(value = "/createWorkOrderPlanV1", method = RequestMethod.POST)
	public Response<CreateWoResponse> createWorkOrderPlanV1(@Valid @RequestBody CreateWorkOrderModel wOPlanObject)
			 {
    	LOG.info("BEGIN: createWorkOrderPlanV1");
		return workOrderPlanService.createWorkOrderPlanForExternal(wOPlanObject);
    }

	/**
	 * 
	 * Purpose - This method start a particular step of given work order.
	 * 
	 * <p>
	 * Method Type - POST.
	 * <p>
	 * URL - "/externalInterface/startTask".
	 * 
	 * @param serverBotModel
	 * @return Response<Map<String, Object>>
	 * @throws Exception 
	 * 
	 */
    @RequestMapping(value = "/startTask", method = RequestMethod.POST)
	public ResponseEntity<Response<Map<String, Object>>> startTask(@Valid @RequestBody ServerBotModel serverBotModel,
			@RequestHeader(value="apiName", required = false) String apiName)throws Exception{

    	return externalInterfaceManagmentService.startTask(serverBotModel, apiName);
    }
    
    /**
	 * 
	 * Purpose - This method start a particular step of given work order for API manager.
	 * 
	 * <p>
	 * Method Type - POST.
	 * <p>
	 * URL - "/externalInterface/startTaskV1".
	 * 
	 * @param serverBotModel
	 * @return Response<Map<String, Object>>
	 * @throws Exception 
	 * 
	 */
    @RequestMapping(value = "/startTaskV1", method = RequestMethod.POST)
	public ResponseEntity<Response<Map<String, Object>>> startTaskV1(@Valid @RequestBody ServerBotModel serverBotModel,
			@RequestHeader(value="apiName", required = false) String apiName)throws Exception{
    	LOG.info("BEGIN: startTaskV1");
    	return externalInterfaceManagmentService.startTask(serverBotModel, apiName);
    }
    
	/**
	 * Purpose - This method stop already started step of given work order.
	 * 
	 * <p>
	 * Method Type - POST.
	 * <p>
	 * URL - "/externalInterface/stopTask".
	 * 
	 * @param serverBotModel
	 * @return Response<Map<String, Object>>
	 * 
	 */
    @RequestMapping(value = "/stopTask", method = RequestMethod.POST)
	public ResponseEntity<Response<Map<String, Object>>> stopTask(@Valid @RequestBody ServerBotModel serverBotModel,
			@RequestHeader(value="apiName", required = false)String apiName) throws Exception{

    	return externalInterfaceManagmentService.stopTask(serverBotModel, apiName);

    }
    
    /**
	 * Purpose - This method stop already started step of given work order for API Manger.
	 * 
	 * <p>
	 * Method Type - POST.
	 * <p>
	 * URL - "/externalInterface/stopTaskV1".
	 * 
	 * @param serverBotModel
	 * @return Response<Map<String, Object>>
	 * 
	 */
    @RequestMapping(value = "/stopTaskV1", method = RequestMethod.POST)
	public ResponseEntity<Response<Map<String, Object>>> stopTaskV1(@Valid @RequestBody ServerBotModel serverBotModel,
			@RequestHeader(value="apiName", required = false)String apiName) throws Exception{
    	LOG.info("BEGIN: stopTaskV1");
    	return externalInterfaceManagmentService.stopTask(serverBotModel, apiName);

    }

	/**
	 * Purpose - This method close given work order.
	 * 
	 * <p>
	 * Method Type - POST.
	 * <p>
	 * URL - "/externalInterface/closeWO".
	 * 
	 * @param saveClosureDetailsObject
	 * @return Response<CreateWoResponse>
	 *
	 */
    @RequestMapping(value = "/closeWO", method = RequestMethod.POST)
	public ResponseEntity<Response<CreateWoResponse>> closeWO(@Valid @RequestBody SaveClosureDetailsForWOModel saveClosureDetailsObject)throws Exception{

    	return externalInterfaceManagmentService.closeWO(saveClosureDetailsObject);

    }
    
    /** Purpose - This method close given work order.
	 * 
	 * <p>
	 * Method Type - POST.
	 * <p>
	 * URL - "/externalInterface/closeWO".
	 * 
	 * @param saveClosureDetailsObject
	 * @return Response<CreateWoResponse>
	 *
	 */
   @RequestMapping(value = "/closeWOV1", method = RequestMethod.POST)
	public ResponseEntity<Response<CreateWoResponse>> closeWOV1(@Valid @RequestBody SaveClosureDetailsForWOModel saveClosureDetailsObject)throws Exception{
   		LOG.info("BEGIN: closeWOV1");
	   return externalInterfaceManagmentService.closeWO(saveClosureDetailsObject);

   }

    /**
     * 
     * @param workOrderPlanObjectModel
     * @return Response<Void>
	 *
     */
    @RequestMapping(value = "/createStagingWorkOrderPlan", method = RequestMethod.POST)
	public Response<Void> createStagingWorkOrderPlan(@RequestBody WorkOrderPlanObjectModel workOrderPlanObjectModel) throws Exception{
		return rpaService.createWorkOrderPlan(workOrderPlanObjectModel);
	}

	/**
	 * Purpose - This method updates output name and url for work orders.
	 * 
	 * <p>
	 * Method Type - POST.
	 * <p>
	 * URL - "/externalInterface/updateOutputFileLinkWO".
	 * 
	 * @param workOrderOutputFileModels
	 * @return Response<Void>
	 */
	@RequestMapping(value = "/updateOutputFileLinkWO", method = RequestMethod.POST)
	public Response<Void> updateOutputFileLinkWO(
			@RequestBody List<WorkOrderOutputFileModel> workOrderOutputFileModels) {

		return externalInterfaceManagmentService.updateOutputFileLinkWO(workOrderOutputFileModels);
	}
	
	/**
	 * Purpose - This method updates output name and url for work orders.
	 * 
	 * <p>
	 * Method Type - POST.
	 * <p>
	 * URL - "/externalInterface/updateOutputFileLinkWO".
	 * 
	 * @param workOrderOutputFileModels
	 * @return Response<Void>
	 */
	@RequestMapping(value = "/updateOutputFileLinkWOV1", method = RequestMethod.POST)
	public Response<Void> updateOutputFileLinkWOV1(
			@RequestBody List<WorkOrderOutputFileModel> workOrderOutputFileModels) {
		LOG.info("BEGIN updateOutputFileLinkWOV1");
		return externalInterfaceManagmentService.updateOutputFileLinkWO(workOrderOutputFileModels);
	}

	/**
	 * Purpose - This method add Comment And UpdatePriority for work orders.
	 * 
	 * <p>
	 * Method Type - POST.
	 * <p>
	 * URL - "/externalInterface/addCommentAndUpdatePriority".
	 * 
	 * @param workOrderModels
	 * @return Response<Void>
	 */
	@RequestMapping(value = "/addCommentAndUpdatePriority", method = RequestMethod.POST)
	public Response<Void> addCommentAndUpdatePriority(@RequestBody List<WorkOrderModel> workOrderModels) {

		return externalInterfaceManagmentService.addCommentAndUpdatePriority(workOrderModels);
	}

	/**
	 * Purpose - This method add Comment And UpdatePriority for work orders for API manager.
	 * 
	 * <p>
	 * Method Type - POST.
	 * <p>
	 * URL - "/externalInterface/addCommentAndUpdatePriority".
	 * 
	 * @param workOrderModels
	 * @return Response<Void>
	 */
	@RequestMapping(value = "/addCommentAndUpdatePriorityV1", method = RequestMethod.POST)
	public Response<Void> addCommentAndUpdatePriorityV1(@RequestBody List<WorkOrderModel> workOrderModels) {
		LOG.info("BEGIN: addCommentAndUpdatePriorityV1");
		return externalInterfaceManagmentService.addCommentAndUpdatePriority(workOrderModels);
	}

	/**
	 * Purpose - This method get Complete WorkOrder Details.
	 * 
	 * <p>
	 * Method Type - GET.
	 * <p>
	 * URL - "/externalInterface/getCompleteWorkOrderDetails".
	 * 
	 * @param woIDs
	 * @param requiredParameters
	 * @return Response<Void>
	 */
	@RequestMapping(value = "/getCompleteWorkOrderDetails", method = RequestMethod.GET)
	public Response<List<List<WorkOrderCompleteDetailsModel>>> getCompleteWorkOrderDetails(
			@Valid @ValidString(message = "Please Provide valid woID!") @RequestParam("woID") String woIDs 
			,@Valid @ValidString(message = "Please Provide valid requiredParameters!" , isRequired = false) 
			@RequestParam(value = "requiredParameters", required = false) String requiredParameters
			) {

		return externalInterfaceManagmentService.getCompleteWorkOrderDetails(woIDs, requiredParameters);
	}
	
	/**
	 * Purpose - This method get Complete WorkOrder Details.
	 * 
	 * <p>
	 * Method Type - GET.
	 * <p>
	 * URL - "/externalInterface/getCompleteWorkOrderDetails".
	 * 
	 * @param woIDs
	 * @param requiredParameters
	 * @return Response<Void>
	 */
	@RequestMapping(value = "/getCompleteWorkOrderDetailsV1", method = RequestMethod.GET)
	public Response<List<List<WorkOrderCompleteDetailsModel>>> getCompleteWorkOrderDetailsV1(
			@Valid @ValidString(message = "Please Provide valid woID!") @RequestParam("woID") String woIDs 
			,@Valid @ValidString(message = "Please Provide valid requiredParameters!" , isRequired = false) 
			@RequestParam(value = "requiredParameters", required = false) String requiredParameters
			) {
		LOG.info("BEGIN getCompleteWorkOrderDetailsV1");
		return externalInterfaceManagmentService.getCompleteWorkOrderDetails(woIDs, requiredParameters);
	}
	
	@AuditEnabled
	@RequestMapping(value = "/updateBookingDetailsStatus/{wOID}/{signumID}/{taskID}/{bookingID}/{status}/{reason}/{stepid}/{flowChartDefID}/{refferer}", method = RequestMethod.POST)
	public Response<Map<String, String>> updateBookingDetailsStatus(@PathVariable("wOID") int wOID,
			@PathVariable("signumID") String signumID, @PathVariable("taskID") int taskID,
			@PathVariable("bookingID") int bookingID, @PathVariable("status") String status,
			@PathVariable("reason") String reason, @PathVariable("stepid") String stepid,
			@PathVariable("flowChartDefID") int flowChartDefID,
			@PathVariable("refferer") String refferer, @RequestHeader(value="apiName", required = false) String apiName) {
		return externalInterfaceManagmentService.updateBookingDetailsStatus(wOID, signumID, taskID, bookingID, status, reason, stepid,flowChartDefID,refferer, apiName);
	}
	
	@RequestMapping(value = "/getWorkOrders", method = RequestMethod.GET)
	public Map<String, Object> getWorkOrders(@RequestParam(value = "signumID") String signumID,
			@RequestParam(value = "status") String status,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws URISyntaxException {
		return  externalInterfaceManagmentService.getWorkOrders(signumID, status, startDate, endDate);
	}
	
	@RequestMapping(value = "/getWOWorkFlow/{wOID}/{proficiencyID}", method = RequestMethod.GET)
	public Map<String, Object> getWOWorkFlow(@PathVariable("wOID") int wOID,
			@PathVariable("proficiencyID") int proficiencyID,
			@RequestHeader(value = "Signum") String signum) throws Exception {
		
		return externalInterfaceManagmentService.getWOWorkFlow(wOID, proficiencyID,signum);
	}
	
	 @RequestMapping(value = "/getInprogressTask/{signum}", method = RequestMethod.GET)
	    public List<LinkedHashMap<String, Object>> getInprogressTask(@PathVariable("signum") String signum){      

	    	return this.externalInterfaceManagmentService.getInprogressTask(signum);
	    }
	 
	 @RequestMapping(value = "/checkParallelWorkOrderDetails/{signumID}/{isApproved}/{executionType}/"
				+ "{woid}/{taskid}/{projectID}/{versionNO}/{subActivityFlowChartDefID}/{stepID}", method = RequestMethod.GET)
		public Map<String, Object> checkParallelWorkOrderDetails(@PathVariable("signumID") String signumID,
				@PathVariable("isApproved") String isApproved, @PathVariable("executionType") String executionType,
				@PathVariable("woid") int woid, @PathVariable("taskid") int taskid,
				@PathVariable("projectID") int projectID, @PathVariable("versionNO") int versionNO,
				@PathVariable("subActivityFlowChartDefID") int subActivityFlowChartDefID,
				@PathVariable("stepID") String stepID) throws Exception {
			Map<String, Object> details = externalInterfaceManagmentService.checkParallelWorkOrderDetails(signumID, isApproved,
					executionType, woid, taskid, projectID, versionNO, subActivityFlowChartDefID, stepID);
			return details;
		}
	 
	 @RequestMapping(value = "/getBookingDetailsByBookingId/{bookingId}", method = RequestMethod.GET)
		public Response<BookingDetailsModel> getBookingDetailsByBookingId(@PathVariable("bookingId") int bookingId) {
			return externalInterfaceManagmentService.getBookingDetailsByBookingId(bookingId);
		}
	 
	 @RequestMapping(value = "/addStepDetailsForFlowChart", method = RequestMethod.POST)
		public Response<Void> addStepDetailsForFlowChart(@RequestBody StepDetailsModel stepDetailsModel) {
		 return externalInterfaceManagmentService.addStepDetailsForFlowChart(stepDetailsModel);
		}
	 
	 @RequestMapping(value = "/callErisiteFromSignalR", method = RequestMethod.POST)
		public Response<ResponseEntity<String>> callErisiteFromSignalR(
				@RequestBody EventPublisherRequestModel eventPublisherModel){
			return externalInterfaceManagmentService.callErisiteFromSignalR(eventPublisherModel);
		}
	 
	/**
	 * Purpose - This method is used to schedule sap bo job.
	 * 
	 * <p>
	 * Method Type - POST.
	 * <p>
	 * URL - "/externalInterface/scheduleSapBoJob".
	 * 
	 */
	@RequestMapping(value = "/scheduleSapBoJob", method = RequestMethod.POST)
	public void scheduleSapBoJob() {
		externalInterfaceManagmentService.scheduleSapBoJob();
	}
	
	@RequestMapping(value = "/v1/scheduleSapBoJob", method = RequestMethod.POST)
	public void scheduleSapBoJobWithRefreshData() {
		externalInterfaceManagmentService.scheduleSapBoJobWithRefreshData();
	}
    /**
     * 
     * @param empFile
     * @param blobName
     * @return
     * @throws URISyntaxException
     * @throws StorageException
     */
	 @RequestMapping(value = "/uploadFileInContainer", method = RequestMethod.POST, consumes = "multipart/form-data")
		public URI uploadFileInContainer(@RequestPart("empFile") MultipartFile empFile,
				@RequestParam("blobName") String blobName) throws URISyntaxException, StorageException{
	        return externalInterfaceManagmentService.uploadFileInContainer(empFile, blobName);
	    }
	 /**
	  * 
	  * @param fileName
	  * @param rpaID
	  * @return URI
	  * @throws URISyntaxException
	  * @throws StorageException
	  * @throws IOException
	  * 
	  */
	@RequestMapping(value = "/DownloadFileFromContainer", method = RequestMethod.GET)
	public Response<URI> DownloadDeliveryExecutionBotFileFromContainer(@RequestParam("fileName") String fileName,
			@RequestParam("rpaID") int rpaID) throws URISyntaxException, StorageException, IOException {
		return externalInterfaceManagmentService.DownloadFileFromContainer(fileName, rpaID);
	}
	 @RequestMapping(value = "/downloadSuperBOT", method = RequestMethod.GET)
		public byte[] downloadSuperBOT(@RequestParam("fileName") String fileName) throws URISyntaxException, StorageException, IOException{
	         return externalInterfaceManagmentService.downloadSuperBOT(fileName);
	    }
	 
	 @RequestMapping(value = "/testConfig", method = RequestMethod.GET)
		public Map<String, Object> testConfig() {
	        return this.eriSiteManagmentService.testConfig();
	    }

	 @RequestMapping(value = "/revokeApiManagerExpiredUsers", method = RequestMethod.POST)
		public Response<Boolean> revokeApiManagerExpiredUsers() {
			return externalInterfaceManagmentService.revokeApiManagerExpiredUsers();
}
	 @RequestMapping(value="/getWOCurrentStepDetails",method=RequestMethod.POST)
	    public ResponseEntity<Response<WorkOrderCurrentStepDetailResponseModel>> getWOCurrentStepDetails(@RequestBody WorkOrderCurrentStepDetailsModel workOrderCurrentStepDetailsModel,@RequestHeader(value="apiName", required = false) String apiName){
	    	return externalInterfaceManagmentService.getWOCurrentStepDetails(workOrderCurrentStepDetailsModel,apiName);
	    }
	 
	 /**
	  * 
	  * @param stepDetailsModel
	  * @return
	  */
	 @RequestMapping(value = "/executeDecisionStep", method = RequestMethod.POST)
		public  ResponseEntity<Response<Void>> executeDecisionStep(@RequestBody StepDetailsModel stepDetailsModel,
				@RequestHeader(value="apiName", required = false) String apiName) {
		 LOG.info("executeDecisionStep :SUCCESS");
		 return externalInterfaceManagmentService.executeDecisionStep(stepDetailsModel,apiName);
		}
	
	
	/**
	 * Api name: /externalInterface/downloadJsonForReport 
	 * Purpose:  This api used to download Json data for PDB & erisite, parse the json and insert it into the database.
	 * @author etapawa
	 * @return ResponseEntity<Response<List<Map<String, Object>>>>
	 */
	@RequestMapping(value = "/downloadJsonForReport", method = RequestMethod.POST )
	public Response<Void> downloadJsonForReport() throws Exception {
		return reportManagmentService.downloadJsonForReport();
	}
	 
	/**
	 * Api name:/externalInterface/getNetworkElement
	 * purpose: This api used to view list of network element for external user.
	 * @author ekarmuj
	 * @param networkElement
	 * @return ResponseEntity<Response<List<ViewNetworkElementModel>>>
	 */
	 
	@RequestMapping(value = "/getNetworkElement", method = RequestMethod.POST)
	public ResponseEntity<Response<List<ViewNetworkElementModel>>> getNetworkElement(@RequestBody
			NetworkElementNewModel networkElement){
		LOG.info("getNetworkElement : SUCCESS ");
		return externalInterfaceManagmentService.getNetworkElement(networkElement);
	}
	
	 
	 /**
	  * @author ekmbuma
	  * @param NetworkElementNewModel
	  * @return ResponseEntity<Response<String>>
	  */
	 @RequestMapping(value = "/appendNetworkElement", method = RequestMethod.POST)
		public ResponseEntity<Response<String>> appendNetworkElement(@RequestBody NetworkElementNewModel elements){
		 LOG.info("appendNetworkElement :SUCCESS");
			return networkElementService.appendNetworkElement(elements);
		}
	 
	 /**
	  * Feed Story 
	  * @author edyudev
	  * @param FeedModel / Project
	  * @return Response<List<Map<String, String>>>
	  */
	 @RequestMapping(value = "/subscribeIsfFeeds", method = RequestMethod.POST)
	 public Response<List<Map<String, Object>>> subscribeIsfFeeds(@Valid @RequestBody FeedModel feedModel){
	 return eriSiteManagmentService.subscribeIsfFeeds(feedModel);
	 }
	 
	 /**
	  * @author emntiuk
	  * @purpose: This API is used to get Secret Key based on clientID
	  * @param clientID
	  * @return
	  */
	 @GetMapping(value = "/getSecretAuthorizationValue")
	 public ResponseEntity<Response<SharePointDetailModel>> getSecretAuthorizationValue(@RequestParam("siteName") String siteName){
	 return eriSiteManagmentService.getSecretAuthorizationValueForServerBot(siteName);
	 }
	 
	/**
	 * API name: /externalInterface/v1/updateWorkOrderDetails
	 * @author emntiuk
	 * @purpose This API is used for update a WO details including transfer WO,
	 *          delete WO and assigned To WO Functionality for External User Hence
	 *          made a new Version of this API
	 * @param oldWorkOrderDetails
	 * @return ResponseEntity<Response<String>>
	 */
	@PostMapping(value = "/v1/updateWorkOrderDetails")
	public ResponseEntity<Response<String>> updateWorkOrderDetailsv1(
			@RequestBody WorkOrderCompleteDetailsModel oldWorkOrderDetails) {

		LOG.info("updateWorkOrderDetailsv1:SUCCESS");
		return workOrderPlanService.updateWorkOrderDetailsv1(oldWorkOrderDetails);

	}
}
	 
