/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.ericsson.isf.model.CreateWoResponse;
import com.ericsson.isf.model.CreateWorkOrderModel;
import com.ericsson.isf.model.CreateWorkOrderNetworkElementModel;
import com.ericsson.isf.model.DOWOModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DeleteWOListModel;
import com.ericsson.isf.model.ExecutionPlanDetail;
import com.ericsson.isf.model.ExecutionPlanModel;
import com.ericsson.isf.model.InProgressTaskModel;
import com.ericsson.isf.model.NodeFilterModel;
import com.ericsson.isf.model.NodeNameValidationModel;
import com.ericsson.isf.model.NodeNamesByFilterModel;
import com.ericsson.isf.model.ProjectModel;
import com.ericsson.isf.model.ProjectNodeTypeModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.TransferWorkOrderModel;
import com.ericsson.isf.model.WOInputFileModel;
import com.ericsson.isf.model.WOOutputFileResponseModel;
import com.ericsson.isf.model.WorkOrderCompleteDetailsModel;
import com.ericsson.isf.model.WorkOrderInputFileModel;
import com.ericsson.isf.model.WorkOrderModel;
import com.ericsson.isf.model.WorkOrderOutputFileModel;
import com.ericsson.isf.model.WorkOrderPlanModel;
import com.ericsson.isf.model.WorkOrderViewProjectModel;
import com.ericsson.isf.service.NetworkElementService;
import com.ericsson.isf.service.WorkOrderPlanService;
import com.ericsson.isf.service.audit.AuditEnabled;

/**
 * This Controller contains all the API's related to work order plan
 *
 * @author eguphee
 */
@RestController
@RequestMapping("/woManagement")
public class WOManagementController {
    
    private static final Logger LOG = LoggerFactory.getLogger(WOManagementController.class);

    @Autowired
    /*Bind to bean/pojo  */
    private WorkOrderPlanService workOrderPlanService;
    
    @Autowired /* Bind to bean/pojo */
	private NetworkElementService networkElementService;
	
	
    /**
     *
     * Purpose - This method is used for deleting work Order Plan.
     * <p>
     * Method Type - POST.
     * <p>
     * URL - "/woManagement/deleteWorkOrderPlan".
     * <p>
     * Input Info - Required any one parameter others must be null
     * (signumID/projectID/workOrderPlanID)
     * <p>
     * Input Type - JSON
     * <p>
     * Input Parameter Format - { "signumID" : "eguphee", projectID : 123
     * ,"wOPlanID" : 1, "lastModifiedBy" : "eguphee"}
     * <p>
     * Output Info - NA
     * <p>
     * Output Type - NA
     * <p>
     * Output Parameters - NA
     * <p>
     * Success Message - NA
     * <p>
     * Database Table Details- Update Active field to 0 in TAB_WORK_ORDER_PLAN
     * <p>
     * Error Message - NA
     * <p>
     * <p>
     */
    @RequestMapping(value = "/deleteWorkOrderPlan", method = RequestMethod.POST)
    public void deleteWorkOrderPlan(@RequestBody WorkOrderPlanModel wOPlanObject) {
        this.workOrderPlanService.deleteWorkOrderPlan(wOPlanObject);
        LOG.info("TBL_WORK_ORDER_PLAN: SUCCESS");
    }
    
     /*
    *
    * Purpose - This method is used for getting work Order Plan on the basis of signum or ProjectId.
    * <p>
    * Method Type - GET.
    * <p>
    * URL - "/woManagement/getWorkOrderPlanDetails?signum=&ProjectId=".
    * <p>
    * Input Parameter:-  signum or ProjectId
    *
     */
//    @RequestMapping(value = "/getWorkOrderPlanDetails", method = RequestMethod.GET)
//    public List<WorkOrderPlanModel> getWorkOrderPlanDetails(String signum, String ProjectId) {
//        int projId = 0;
//        if (!(ProjectId.equals("null") || ProjectId.isEmpty()) && (ProjectId.matches("[0-9]+"))) {
//            projId = Integer.parseInt(ProjectId);
//        }
//        List<WorkOrderPlanModel> woPlanDetails= this.workOrderPlanService.getWorkOrderPlanDetails(signum, projId);
//        LOG.info("TBL_WORK_ORDER_PLAN: WOPlan List: "+woPlanDetails);
//        return woPlanDetails;
//    }
    
      @RequestMapping(value = "/getWorkOrderPlanDetails/{woPlanID}/{projectID}/{signumID}", method = RequestMethod.GET)
    public List<WorkOrderPlanModel> getWorkOrderPlanDetails(@PathVariable("woPlanID") int woPlanID,
                                                            @PathVariable("projectID") int projectID,
                                                            @PathVariable("signumID") String signumID) {

    	  List<WorkOrderPlanModel> woPlanDetails= this.workOrderPlanService.getWorkOrderPlanDetails(woPlanID,projectID,signumID);
        return woPlanDetails;
    }

    /*
    *
    * Purpose - This method is used for updating work Order Plan.
    * <p>
    * Method Type - POST.
    * <p>
    * URL - "/woManagement/updateWorkOrderPlan".
    * <p>
    * Input Parameter:-   {
        "wOPlanID": 1,
        "subActivityID": 13,
        "priority": "High Severe",
        "periodicityDaily": null,
        "periodicityWeekly": "TUESDAY,SATURDAY",
        "startDate": "2017-11-08",
        "startTime": "18:00:00",
        "endDate": "2017-11-20",
        "wOName": "TWork",
        "signumID": "eguphee",
        "lastModifiedBy": "eguphee",
        "listOfNode": [
            {
               
                "nodeType": "dksjhdjkaE",
                "nodeNames": "msdbasjkd"
                
            }
        ]
    }
    *
     */
    @RequestMapping(value = "/updateWorkOrderPlan", method = RequestMethod.POST)
    public void updateWorkOrderPlan(@RequestBody WorkOrderPlanModel wOPlanObject) {
        this.workOrderPlanService.updateWorkOrderPlan(wOPlanObject);
        LOG.info("TBL_WORK_ORDER_PLAN: SUCCESS");
    }
	
    @RequestMapping(value = "/getWOPlanDetails/{woPlanId}", method = RequestMethod.GET)
    public Map<String,Object> getWOPlanDetails(@PathVariable("woPlanId") int woPlanId){
    	
    	//All this code should move to service
    	
    	
    	List<Map<String,Object>> domainDetail = this.workOrderPlanService.getDomainDetailsByWOPlanID(woPlanId);
    	List<Map<String,Object>> planData = this.workOrderPlanService.getPlanDataByWOPlanID(woPlanId);
    	List<Map<String,Object>> nodeType = this.workOrderPlanService.getNodeTypeByWOPlanID(woPlanId);
    	List<Map<String,Object>> nodeNames = this.workOrderPlanService.getNodeNamesByWOPlanID(woPlanId);
    	List<Map<String,Object>> assignedUsers = this.workOrderPlanService.getAssignedUsersByWOPlanID(woPlanId);
    	List<Map<String,Object>> checkBoxData = this.workOrderPlanService.getCheckBoxData(woPlanId);
    	ExecutionPlanModel execPlanDetails = this.workOrderPlanService.getExecutionPlandDetilsbyWoPlanId(woPlanId);
    	
    	boolean isChecked = false;
    	if (checkBoxData.size() > 0){
    		isChecked = true;
    	}else if (checkBoxData.size() == 0){
    		isChecked = false;
    	}
    	Map<String,Object> finalData = new HashMap<String, Object>();
    	finalData.put("domainDetails", domainDetail);
    	finalData.put("wOPlanDetails", planData);
    	finalData.put("nodeType", nodeType);
    	finalData.put("nodeNames", nodeNames);
    	finalData.put("assignedUsers", assignedUsers);
    	finalData.put("isChecked", isChecked);
    	finalData.put("execPlanDetails", execPlanDetails);
    	return finalData;
    }
    
    
    
    @RequestMapping(value = "/getWorkOrderViewDetails/{projectID}/{scope}/{activity}/{WOID}/{startDate}/{endDate}/{assignedTo}/{signum_LoggedIn}/{isWeekEndIncluded}/{isOddHoursIncluded}/{status}/{nodeName}/{assignedBy}", method = RequestMethod.GET)
    public List<WorkOrderViewProjectModel> getWorkOrderViewDetails(@PathVariable("projectID") String projectID,
            @PathVariable("scope") String scope,
            @PathVariable("activity") String activity,
            @PathVariable("WOID") String WOID,
            @PathVariable("startDate") String startDate,
            @PathVariable("endDate") String endDate,
            @PathVariable("assignedTo") String assignedTo,
            @PathVariable("signum_LoggedIn") String signum_LoggedIn,
            @PathVariable("isWeekEndIncluded") boolean isWeekEndIncluded,
            @PathVariable("isOddHoursIncluded") boolean isOddHoursIncluded,
            @PathVariable("status") String status,
            @PathVariable("nodeName") String nodeName,
            @RequestHeader("MarketArea") String marketArea,
            @PathVariable("assignedBy") String assignedBy
            ) {
        if(projectID.equalsIgnoreCase("Null") || scope.equalsIgnoreCase("Null") || activity.equalsIgnoreCase("Null") || WOID.equalsIgnoreCase("Null")
                || startDate.equalsIgnoreCase("Null") || endDate.equalsIgnoreCase("Null") || assignedTo.equalsIgnoreCase("Null") 
                || signum_LoggedIn.equalsIgnoreCase("Null")){
            throw new ApplicationException(500, "Invalid input... Input cannot be NULL !!!");
        }
        else{
            List<WorkOrderViewProjectModel> woViewDetails= this.workOrderPlanService.getWorkOrderViewDetails(projectID,scope,activity,WOID,startDate,endDate,assignedTo,signum_LoggedIn,isWeekEndIncluded,isOddHoursIncluded
            		,status,nodeName,marketArea,assignedBy);
            return woViewDetails;
        }
    }
    @RequestMapping(value = "/getNEIDByProjectID", method = RequestMethod.GET)
    public List<Map<String,Object>> getNEIDByProjectID(@RequestParam(value="projectID")int projectID,@RequestParam(value="term")String term) {
        return this.workOrderPlanService.getNEIDByProjectID(projectID,term);
    }


    @RequestMapping(value = "/getProjectBySignum/{signum}", method = RequestMethod.GET)
    public ResponseEntity<Response<List<ProjectModel>>> getProjectBySignum(
            @PathVariable("signum") String signum,@RequestHeader("MarketArea") String marketArea,@RequestHeader("Role") String role)
            {
             return workOrderPlanService.getProjectBySignum(signum, marketArea,role);
        
    }

     /*
    *
    * Purpose - This method is used for creating work Order Plan.
    * <p>
    * Method Type - POST.
    * <p>
    * URL - "/woManagement/createWorkOrderPlan".
    * <p>
    * Input Parameter:-   {
        "projectID": 1,
        "scopeID": 4,
        "subActivityID": 13,
        "priority": "High Severe",
        "periodicityDaily": null,
        "periodicityWeekly": "TUESDAY,SATURDAY",
        "startDate": "2017-11-08",
        "startTime": "18:00:00",
        "endDate": "2017-11-20",
        "wOName": "TWork",
        "signumID": "eguphee",
        "createdBy": "eguphee",
        "listOfNode": [
            {
               
                "nodeType": "dksjhdjkaE",
                "nodeNames": "msdbasjkd"
                
            }
        ]
    }
    *
     */
    @AuditEnabled
    @RequestMapping(value = "/createWorkOrderPlan", method = RequestMethod.POST)
    public CreateWoResponse createWorkOrderPlan(@RequestBody CreateWorkOrderModel wOPlanObject) throws Exception {
    	LOG.info("TBL_WORK_ORDER_PLAN: SUCCESS");
        return workOrderPlanService.createWorkOrdekOrExecutionPlan(wOPlanObject,false);
    }

   /* @RequestMapping(value = "/updateEditWorkOrderPlan", method = RequestMethod.POST)
    public void updateEditWorkOrderPlan(@RequestBody CreateWorkOrderModel wOPlanObject) throws InterruptedException {
        this.workOrderPlanService.updateEditWorkOrderPlan(wOPlanObject);
        LOG.info("TBL_WORK_ORDER_PLAN: SUCCESS");
    }*/
    
    
     /*
    *
    * Purpose - This method is used for searching work Order Plan.
    * <p>
    * Method Type - GET.
    * <p>
     */
    
    @RequestMapping(value = "/getWorkOrderPlans", method = RequestMethod.POST)
    public Map<String, Object> getWorkOrderPlans(
    		@RequestParam(value="projectID",required=true) int projectID,
            @RequestParam(value="startDate",required=false) String startDate,
            @RequestParam(value="endDate",required=false) String endDate,
            @RequestParam(value="woStatus",required=false) String woStatus,
            HttpServletRequest request) {
    	DataTableRequest req=new DataTableRequest(request);
    	
    	
        Map<String, Object> searchWOPlan = this.workOrderPlanService.getWorkOrderPlans(projectID, startDate,endDate,woStatus,req);
        return searchWOPlan;

    }
    
  
    
    @RequestMapping(value = "/isPlanEditable", method = RequestMethod.GET)
    public boolean isPlanEditable(@RequestParam(value="woPlanId",required=true) int woPlanId) {
       return this.workOrderPlanService.isPlanEditable(woPlanId);
    }
    
    
  
     @RequestMapping(value = "/deleteWorkOrder/{wOID}/{signumID}", method = RequestMethod.POST)
    public void deleteWorkOrder(@PathVariable ("wOID") int wOID,
                                @PathVariable ("signumID") String signumID) {
        if(wOID==0 || signumID.equalsIgnoreCase("Null")){
            throw new ApplicationException(500, "Invalid input... WOID cannot be 0 or SignumID cannot be NULL !!!");
        }
        else{
            this.workOrderPlanService.deleteWorkOrder(wOID, signumID);
            LOG.info("TBL_WORK_ORDER: SUCCESS");
        }
    }
    
   
    @RequestMapping(value = "/deleteWorkOrderList", method = RequestMethod.POST)
    public void deleteWorkOrderList(@RequestBody DeleteWOListModel deleteWOListModel) {
        if(deleteWOListModel.getWoID().isEmpty() || deleteWOListModel.getSignum().equalsIgnoreCase("Null")){
            throw new ApplicationException(500, "Invalid input... WOID cannot be 0 or SignumID cannot be NULL !!!");
        }
        else{
            this.workOrderPlanService.deleteWorkOrderList(deleteWOListModel);
            LOG.info("TBL_WORK_ORDER: SUCCESS");
        }
    }


    @RequestMapping(value = "/updateWorkOrder", method = RequestMethod.POST)
    public void updateWorkOrder(@RequestBody WorkOrderModel workOrderModel) {
        this.workOrderPlanService.updateWorkOrder(workOrderModel);
        LOG.info("TBL_WORK_ORDER: SUCCESS");

    }

    @RequestMapping(value = "/getWorkOrderDetails/{wOID}", method = RequestMethod.GET)
    public List<WorkOrderModel> getWorkOrderDetails(@PathVariable("wOID") int wOID) {
        if(wOID !=0){
            List<WorkOrderModel> woDetailsByID= this.workOrderPlanService.getWorkOrderDetails(wOID);
            return woDetailsByID;
        }
        else{
            throw new ApplicationException(500, "Invalid input... WOID cannot be 0 !!!");
        }
    }
    
    @AuditEnabled
    @RequestMapping(value = "/transferWorkOrder", method = RequestMethod.POST)
    public ResponseEntity<Response<Void>> transferWorkOrder(@RequestBody TransferWorkOrderModel transferWorkOrderModel) {
    	return workOrderPlanService.transferWorkOrder(transferWorkOrderModel);       
    }
    

    
    @RequestMapping(value = "/reopenDefferedWorkOrder", method = RequestMethod.POST)
    public CreateWoResponse reopenDefferedWorkOrder(@RequestParam(value="workOrderId",required=true) int workOrderId) {
        return workOrderPlanService.reopenDefferedWorkOrder(workOrderId);
    }
    @RequestMapping(value = "/closeDeferedWorkOrder", method = RequestMethod.POST)
    public int closeDeferedWorkOrder(@RequestParam(value="workOrderId",required=true) int workOrderId) {
        return this.workOrderPlanService.closeDeferedWorkOrder(workOrderId);
    }
    @RequestMapping(value = "/reinstateDeferedWorkOrder", method = RequestMethod.POST)
    public ResponseEntity<Response<Void>> reinstateDeferedWorkOrder(@RequestParam(value="workOrderId",required=true) int workOrderId) {
        return this.workOrderPlanService.reinstateDeferedWorkOrder(workOrderId);
    }
    
    @AuditEnabled   
    @RequestMapping(value = "/massUpdateWorkOrder", method = RequestMethod.POST)
    public boolean massUpdateWorkOrder(@RequestBody TransferWorkOrderModel transferWorkOrderModel) {
        if(transferWorkOrderModel.getSenderID().equalsIgnoreCase(transferWorkOrderModel.getReceiverID())){
            throw new ApplicationException(500,"Work order cannot be transferred to yourself");
        }else{
            return this.workOrderPlanService.massUpdateWorkOrder(transferWorkOrderModel);
        }
    }
    
    @RequestMapping(value = "/getPriority", method = RequestMethod.GET)
    public List<String> getPriority() {
        LOG.info("TBL_WORK_ORDER: Priority");
	return this.workOrderPlanService.getPriority();
    }
    
    @RequestMapping(value = "/getNodeType/{projectID}", method = RequestMethod.GET)
    public List<String> getNodeType(@PathVariable("projectID") Integer projectID) {
        LOG.info("TBL_NETWORK_ELEMENT: SUCCESS");
        long startTime=System.currentTimeMillis();
        LOG.info("START TIME: "+startTime);
        List<String> response=this.workOrderPlanService.getNodeType(projectID);
        long endTime=System.currentTimeMillis();
        LOG.info("END TIME: "+endTime);
        LOG.info("TOTAL TIME ELAPSED IN @getNodeType API : "+(endTime-startTime)+" ms");
//        return this.workOrderPlanService.getNodeType(projectID);
        return response;
    }
    
    @RequestMapping(value = "/getMarketArea", method = RequestMethod.GET)
    public List<String> getMarketArea(@RequestParam("projectID") int projectID,@RequestParam("nodeType") String nodeType,@RequestParam("type") String type) {
        LOG.info("TBL_NETWORK_ELEMENT: SUCCESS");
        return this.workOrderPlanService.getMarketArea(projectID,nodeType,type);
    }
   
    @RequestMapping(value = "/getVendor/{projectID}/{marketArea}/{nodeType}", method = RequestMethod.GET)
    public ResponseEntity<Response<List<String>>> getVendor(@PathVariable("projectID") int projectID,@PathVariable("marketArea") String marketArea,@PathVariable("nodeType") String nodeType) {
        LOG.info("TBL_NETWORK_ELEMENT: SUCCESS");
        return this.workOrderPlanService.getVendor(projectID,marketArea,nodeType);
    }
    
    @RequestMapping(value = "/getNodeNames/{projectID}/{type}/{elementType}", method = RequestMethod.GET)
    public List<ProjectNodeTypeModel> getNodeNames(@PathVariable("projectID") int projectID,
                                    @PathVariable("elementType") String elementType,@PathVariable("type") String type) {
        LOG.info("TBL_NETWORK_ELEMENT: SUCCESS");
        return this.workOrderPlanService.getNodeNames(projectID,elementType,type);
    }
    
      @RequestMapping(value = "/getNodeNamesValidate", method = RequestMethod.POST)
    public  Response<Void> getNodeNamesValidate(@RequestBody NodeNameValidationModel nodeNameValidationModel){
                                    
                                    return this.workOrderPlanService.getNodeNamesValidate(nodeNameValidationModel.getProjectID()
                                    		,nodeNameValidationModel.getElementType()
                                    		,nodeNameValidationModel.getType()
                                    		,nodeNameValidationModel.getVendor()
                                    		,nodeNameValidationModel.getMarket()
                                    		,nodeNameValidationModel.getTech()
                                    		,nodeNameValidationModel.getDomain()
                                    		,nodeNameValidationModel.getSubDomain()
                                    		,nodeNameValidationModel.getNodeNames());
    }
    
    @RequestMapping(value = "/getNodeNamesFilter/{projectID}/{elementType}/{type}/{vendor}/{market}/{tech}/{domain}/{subDomain}", method = RequestMethod.GET)
    public List<ProjectNodeTypeModel> getNodeNamesFilter(@PathVariable("projectID") int projectID,
                                    @PathVariable("elementType") String elementType,
                                    @PathVariable("type") String type,
                                    @PathVariable("vendor") String vendor,
                                    @PathVariable("market") String market,
                                    @PathVariable("tech") String tech,
                                    @PathVariable("domain") String domain,
                                    @PathVariable("subDomain") String subDomain) {
        
        LOG.info("TBL_NETWORK_ELEMENT: SUCCESS");
        return this.workOrderPlanService.getNodeNamesFilter(projectID,elementType,type,vendor,market,tech,domain,subDomain);
    }
    @RequestMapping(value = "/getNodeNamesByFilter", method = RequestMethod.POST)
    public List<ProjectNodeTypeModel> getNodeNamesByFilter(@RequestBody NodeNamesByFilterModel nodeNamesByFilterModel) {
        
        LOG.info("TBL_NETWORK_ELEMENT: SUCCESS");
        return this.workOrderPlanService.getNodeNamesByFilter(nodeNamesByFilterModel.getProjectID(),
        		nodeNamesByFilterModel.getElementType(),
        		nodeNamesByFilterModel.getType(),
        		nodeNamesByFilterModel.getVendor(),
        		nodeNamesByFilterModel.getMarket(),
        		nodeNamesByFilterModel.getTech(),
        		nodeNamesByFilterModel.getDomain(),
        		nodeNamesByFilterModel.getSubDomain(),
        		nodeNamesByFilterModel.getTerm());
    }
    @RequestMapping(value="/getWorkFlowVersionList/{projectID}/{subActivityID}",method=RequestMethod.GET)
    public List<Integer> getWorkFlowVersionList(@PathVariable("projectID") int projectID,
                                            @PathVariable("subActivityID") int subActivityID){
        return workOrderPlanService.getWorkFlowVersionList(projectID, subActivityID);
    }
    
    @RequestMapping(value="/updateWOWFVersion/{woID}/{wfVersion}/{subActivityFlowChartDefId}",method=RequestMethod.POST)
    public void updateWOWFVersion(@PathVariable("woID") int woID,
                                  @PathVariable("wfVersion") int wfVersion,
                                  @PathVariable("subActivityFlowChartDefId") int subActivityFlowChartDefId){
        workOrderPlanService.updateWOWFVersion(woID, wfVersion,subActivityFlowChartDefId);
    }
    
    @RequestMapping(value="/getLatestWorkFlowVersion/{projectID}/{subActivityID}",method=RequestMethod.GET)
    public int getLatestWorkFlowVersion(@PathVariable("projectID") int projectID,
                                            @PathVariable("subActivityID") int subActivityID){
        return workOrderPlanService.getLatestWorkFlowVersion(projectID, subActivityID);
    }
    
    
    @RequestMapping(value="/getWfListForPlan",method=RequestMethod.GET)
    public List<Map<String,Object>> getWfListForPlan(@RequestParam("projectID") Integer projectID, @RequestParam("sourceId") Integer sourceId){
        return workOrderPlanService.getWfListForPlan(projectID, sourceId);
    }
    
    
    @RequestMapping(value="/uploadFileForWOCreation/{projectID}/{createdBy}",method=RequestMethod.POST, consumes = "multipart/form-data")
    public Response<Void> uploadFileForWOCreation(@PathVariable("projectID") int projectID,
                                        @PathVariable("createdBy") String createdBy,
                                        @RequestParam("file") MultipartFile file) throws Exception {
    	
    	return  this.workOrderPlanService.uploadFileForWOCreation(projectID,createdBy,file);
		
    }

    @RequestMapping(value = "/saveExecutionPlan", method = RequestMethod.POST)
    public Response<Integer> saveExecutionPlan(@RequestBody ExecutionPlanModel executionPlanModel){
    	return this.workOrderPlanService.saveExecutionPlan(executionPlanModel);
    }
    
    @RequestMapping(value = "/getExecutionPlans", method = RequestMethod.GET)
    public List<ExecutionPlanModel> getExecutionPlans(@RequestParam(value="projectId",required=false) int projectId){
        
    	List<ExecutionPlanModel> planList = workOrderPlanService.getExecutionPlans(projectId);
    	List<ExecutionPlanDetail> planDetails;
    	for(ExecutionPlanModel planModel:planList){
    		planDetails=workOrderPlanService.getExecutionPlanDetailsByExecutionPlanId(planModel.getExecutionPlanId());
    		planModel.setTasks(planDetails);
    	}
    	
    	return planList;
    }
    
    @RequestMapping(value = "/getExecutionPlanDetailsByExecutionPlanId", method = RequestMethod.GET)
    public List<ExecutionPlanDetail> getExecutionPlanDetailsByExecutionPlanId(@RequestParam(value="projectId",required=false) int executionPlanId){
    	return this.workOrderPlanService.getExecutionPlanDetailsByExecutionPlanId(executionPlanId);
    }
    
    @RequestMapping(value = "/updateExecutionPlanStatus", method = RequestMethod.POST)
    public void updateExecutionPlanStatus(@RequestBody Map<String,String> reqParams){
    	this.workOrderPlanService.updateExecutionPlanStatus(Integer.parseInt(reqParams.get("executionPlanId"))
    			,Boolean.parseBoolean(reqParams.get("isActive")),reqParams.get("createdBy"));
    }
    
    @RequestMapping(value = "/searchExecutionPlans", method = RequestMethod.GET)
    public List<ExecutionPlanModel> searchExecutionPlans(@RequestParam(value="subactivityId",required=false) Integer subactivityId,
    		@RequestParam(value="workFlowVersionNo",required=false) Integer workFlowVersionNo){
    	return this.workOrderPlanService.searchExecutionPlans(subactivityId,workFlowVersionNo);
    }
    
    @RequestMapping(value = "/checkProjectEditByProjID/{projectID}", method = RequestMethod.GET)
    public List<WorkOrderPlanModel> checkProjectEditByProjID(@PathVariable("projectID") int projectID) {
        LOG.info("TBL_WORK_ORDER_PLAN: SUCCESS");
        return this.workOrderPlanService.checkProjectEditByProjID(projectID);        
    }
    
    @RequestMapping(value = "/isPlanInExecution", method = RequestMethod.GET)
    public boolean isPlanInExecution(@RequestParam(value="executionPlanId",required=true) long executionPlanId){
    	return this.workOrderPlanService.isPlanInExecution(executionPlanId);
    }
    
    @RequestMapping(value="/getSourcesForMapping",method=RequestMethod.GET)
    public List<Map<String, Object>> getSourcesForMapping(){
    	return workOrderPlanService.getSourcesForMapping();
    }
    
    @RequestMapping(value="/getSourcesForPlan",method=RequestMethod.GET)
    public List<Map<String, Object>> getSourcesForPlan(@RequestParam(value="projectId",required=true) int projectId){
    	return workOrderPlanService.getSourcesForPlan(projectId);
    }
    
    /**
     * 
     * @param signum
     * @return List<LinkedHashMap<String, Object>>
     */
    @RequestMapping(value = "/getInprogressTask/{signum}", method = RequestMethod.GET)
    public ResponseEntity<Response<List<LinkedHashMap<String, Object>>>> getInprogressTask(@PathVariable("signum") String signum){      
    	long startTime=System.currentTimeMillis();
    	LOG.info("START TIME: "+startTime+" ms");
    	ResponseEntity<Response<List<LinkedHashMap<String, Object>>>> response=this.workOrderPlanService.getInprogressTask(signum);
    	long endTime=System.currentTimeMillis();
    	LOG.info("END TIME: "+endTime+" ms");
    	LOG.info("TOTAL TIME ELAPSED IN @getInprogressTask API : "+(endTime-startTime)+" ms");
//    	return this.workOrderPlanService.getInprogressTask(signum);
    	return response;
    }
          
    /**
     * 
     * @param signum
     * @return List<LinkedHashMap<String, Object>>
     */
    @RequestMapping(value = "/v1/getInprogressTask/{signum}", method = RequestMethod.GET)
    public Response<List<InProgressTaskModel>> getInprogressTaskBySignum(@PathVariable("signum") String signum){      
    	long startTime=System.currentTimeMillis();
    	LOG.info("START TIME: "+startTime+" ms");
    	Response<List<InProgressTaskModel>> response= this.workOrderPlanService.getInprogressTaskBySignum(signum);
    	long endTime=System.currentTimeMillis();
    	LOG.info("END TIME: "+endTime+" ms");
    	LOG.info("TOTAL TIME ELAPSED IN v1/@getInprogressTask API : "+(endTime-startTime)+" ms");
    	return response;
    }
          
    @RequestMapping(value = "/getBookingDetails/{WoID}/{signum}", method = RequestMethod.GET)
    public List<LinkedHashMap<String, Object>> getBookingDetails(@PathVariable("WoID") int WoID,@PathVariable("signum") String signum){      
        List<LinkedHashMap<String, Object>>  result= this.workOrderPlanService.getBookingDetails(WoID, signum);
        return result;
    }
    
    @RequestMapping(value = "/saveDeliverablePlan",method = RequestMethod.POST)
    public Response<Integer> saveDeliverablePlan(@RequestBody ExecutionPlanModel executionPlanModel){
    	return this.workOrderPlanService.saveDeliverablePlan(executionPlanModel);	
    }
    
    @RequestMapping(value = "/getDeliverablePlan", method = RequestMethod.GET)
    public ExecutionPlanModel getDeliverablePlan(@RequestParam(value="scopeId",required=true) int scopeId){
    	ExecutionPlanModel deliverablePlan = workOrderPlanService.getDeliverablePlan(scopeId);
    	return deliverablePlan;
    }
    @RequestMapping(value = "/insertOutputFileWO", method = RequestMethod.POST)
    public Response<Void> insertOutputFileWO(@RequestBody WorkOrderOutputFileModel workOrderOutputFileModel) {
    	  LOG.info("TBL_WORK_ORDER_OUTPUT_FILE: SUCCESS");
        return this.workOrderPlanService.insertOutputFileWO(workOrderOutputFileModel);
      }
    @RequestMapping(value="/getWOOutputFile",method=RequestMethod.GET)
    public ResponseEntity<Response<List<WOOutputFileResponseModel>>> getWOOutputFile(@RequestParam(value="woid",required=true) int woid){
    	return workOrderPlanService.getWorkOrderOutputFile(woid);
    }
    @RequestMapping(value="/getWOInputFile",method=RequestMethod.GET)
    public ResponseEntity<Response<List<WOInputFileModel>>> getWOInputFile(@RequestParam(value="woid",required=true) int woid){
    	return workOrderPlanService.getWorkOrderInputFile(woid);
    }
    @RequestMapping(value="/getCountOfUnassignedWOByWOPLAN",method=RequestMethod.GET)
    public int getCountOfUnassignedWOByWOPLAN(@RequestParam(value="woplanid",required=true) int woplanid){
    	return workOrderPlanService.getCountOfUnassignedWOByWOPLAN(woplanid);
    }
    
    @RequestMapping(value = "/deleteInputFile/{id}/{signumID}", method = RequestMethod.POST)
    public void deleteInputFile(@PathVariable ("id") int id,
                                @PathVariable ("signumID") String signumID) {
        if(id==0 || signumID.equalsIgnoreCase("Null")){
            throw new ApplicationException(500, "Invalid input... ID cannot be 0 or SignumID cannot be NULL !!!");
        }
        else{
            this.workOrderPlanService.deleteInputFile(id, signumID);
            LOG.info("TBL_WORK_ORDER_INPUT_FILE: SUCCESS");
        }
    }
    @RequestMapping(value = "/deleteOutputFile/{id}/{signumID}", method = RequestMethod.POST)
    public void deleteOutputFile(@PathVariable ("id") int id,
                                @PathVariable ("signumID") String signumID) {
        if(id==0 || signumID.equalsIgnoreCase("Null")){
            throw new ApplicationException(500, "Invalid input... ID cannot be 0 or SignumID cannot be NULL !!!");
        }
        else{
            this.workOrderPlanService.deleteOutputFile(id, signumID);
            LOG.info("TBL_WORK_ORDER_OUTPUT_FILE: SUCCESS");
        }
    }
    @RequestMapping(value = "/editInputFile", method = RequestMethod.POST)
    public Response<Void> editInputFile(@RequestBody WorkOrderInputFileModel workOrderInputFileModel) {
         return this.workOrderPlanService.editInputFile(workOrderInputFileModel);
    }
    @RequestMapping(value = "/editOutputFile", method = RequestMethod.POST)
    public Response<Void> editOutputFile(@RequestBody WorkOrderOutputFileModel workOrderOutputFileModel) {
         return this.workOrderPlanService.editOutputFile(workOrderOutputFileModel);
    }
    
    @AuditEnabled
    @RequestMapping(value = "/editWOPriority/{woid}/{priority}/{signumID}/{actorType}", method = RequestMethod.POST)
    public void editWOPriority(@PathVariable ("woid") int woid,
    		                    @PathVariable ("priority") String priority,
                                @PathVariable ("signumID") String signumID,
                                @PathVariable ("actorType") String actorType, @RequestParam(value="comment",required=false) String comment ) {
       
        
            this.workOrderPlanService.editWOPriority(woid,priority,signumID,actorType);
         
    }
    
    @RequestMapping(value = "/getWorkOrdersByProjectID", method = RequestMethod.GET)
    public List<Map<String,Object>> getWorkOrdersByProjectID( @RequestParam(value ="projectID", required=false) String projectID,
    		@RequestParam(value ="projectScopeID", required=false) String projectScopeID, @RequestParam(value ="assignedBy", required=false) String assignedBy,
    		@RequestParam(value ="assignedTo", required=false) String assignedTo, @RequestParam(value ="nodeName", required=false) String nodeName, @RequestParam(value ="term", required=true) String term ,
    		@RequestHeader("signum") String signum,
    		@RequestHeader("MarketArea") String marketArea,@RequestHeader("Role") String role) {
     List<Map<String,Object>> woDetails= this.workOrderPlanService.getWorkOrdersByProjectID(projectID, term, projectScopeID, assignedBy, assignedTo, nodeName, marketArea, role, signum);
        return woDetails;
    }
    
    /**
     * 
     * @param projectID
     * @param scopeID
     * @return List<Map<String,Object>>
     */
    @RequestMapping(value="/getWFListForPlanDeliverablePlan", method = RequestMethod.GET)
    public List<Map<String,Object>> getWFListForDeliverablePlan(@RequestParam("projectID") int projectID, @RequestParam("scopeID") int scopeID){

    	return this.workOrderPlanService.getWFListForDeliverablePlan(projectID,scopeID);
    }
    
    /**
    *
    * Purpose - This method is used for creating work Order Plan in bulk.
    * <p>
    * Method Type - POST.
    * <p>
    * URL - "/woManagement/createBulkWorkOrder".
    *
    * @return Response<Void>
    * @throws InterruptedException
    */
    @AuditEnabled
    @RequestMapping(value = "/createBulkWorkOrder", method = RequestMethod.POST)
    public List<CreateWoResponse> createBulkWorkOrder(@RequestParam("source") String source) throws InterruptedException {
    	LOG.info("TBL_WORK_ORDER_PLAN: SUCCESS");
        return workOrderPlanService.createBulkWorkOrder(source);
    }
    /**
     * 
     * @param oldWorkOrderDetails
     * @return Response<String>
     */
    @RequestMapping(value="/saveEditedWorkOrderDetails", method = RequestMethod.POST)
    public Response<String> saveEditedWorkOrderDetails(@RequestBody WorkOrderCompleteDetailsModel oldWorkOrderDetails){

    	LOG.info("saveEditedWorkOrderDetails : SUCCESS");
    	return this.workOrderPlanService.saveEditedWorkOrderDetails(oldWorkOrderDetails);
    }
    
    
    /**
     * Purpose - This method is tells if an execution plan have subsequent execution plan.
     * 
     * <p>
     * Method Type - GET.
     * <p>
     * URL - "/woManagement/hasSubSequentPlanDetail".
     * 
     * @param wOID
     * @return boolean
     */
    @RequestMapping(value = "/hasSubSequentPlanDetail", method = RequestMethod.GET)
    public boolean hasSubSequentPlanDetail(@RequestParam(value="wOID") int wOID){
    	
    	return this.workOrderPlanService.hasSubSequentPlanDetail(wOID);
    }
    
	@RequestMapping(value = "/getWorkOrdersByDoid", method = RequestMethod.GET)
	public List<DOWOModel> getWorkOrdersByDoid(@RequestParam(value = "doid") int doid) {
		return this.workOrderPlanService.getWorkOrdersByDoid(doid);
	}

	/**
	 * Filter node name for given tech and domain in deliverable
	 * 
	 * @param nodeFilterModel
	 * @return List<ProjectNodeTypeModel>
	 */
    @RequestMapping(value = "/getNodeNamesForDeliverable", method = RequestMethod.POST)
	public List<ProjectNodeTypeModel> getNodeNamesForDeliverable(@RequestBody NodeFilterModel nodeFilterModel) {

		return this.workOrderPlanService.getNodeNamesForDeliverable(
				nodeFilterModel.getProjectID(),
				nodeFilterModel.getElementType(), 
				nodeFilterModel.getType(), 
				nodeFilterModel.getVendor(), 
				nodeFilterModel.getMarket(), 
				nodeFilterModel.getTechnologyIdList(), 
				nodeFilterModel.getDomainIdList(), 
				nodeFilterModel.getTerm());
	}
    
    /**
     * Validate node name with tech and domain
     * 
     * @param nodeFilterModel
     * @return Response<Void>
     */
    @RequestMapping(value = "/getNodeValidateForDeliverable", method = RequestMethod.POST)
    public  Response<Void> getNodeValidateForDeliverable(@RequestBody NodeFilterModel nodeFilterModel){
    	
    	return this.workOrderPlanService.getNodeValidateForDeliverable(nodeFilterModel.getProjectID(),
    			nodeFilterModel.getElementType(),
    			nodeFilterModel.getType(),
    			nodeFilterModel.getVendor(),
    			nodeFilterModel.getMarket(),
    			nodeFilterModel.getTechnologyIdList(),
    			nodeFilterModel.getDomainIdList(),
    			nodeFilterModel.getTerm(),
    			nodeFilterModel.getNodeNames());
    }
    
    /**
	 * API Name: woManagement/updateWorkOrderDetails
	 * @author elkpain
	 * @purpose To update WO details by given WOID.
	 * @param WorkOrderCompleteDetailsModel
	 * @return ResponseEntity<Response<String>>
	 * @throws Exception 
	 */
	@RequestMapping(value = "/updateWorkOrderDetails", method = RequestMethod.POST)
	public ResponseEntity<Response<String>> updateWorkOrderDetails(@RequestBody WorkOrderCompleteDetailsModel oldWorkOrderDetails) {
	
  	    LOG.info("updateWorkOrderDetails:SUCCESS");
  	    return workOrderPlanService.updateWorkOrderDetails(oldWorkOrderDetails);

	}
	
	/**
	 * API Name: woManagement/getProjectBySignumForProjectQueue/{signum}
	 * 
	 * @author elkpain
	 * @purpose To get project by signum for project queue dropdown.
	 * @param signum
	 * @return ResponseEntity<Response<List<ProjectModel>>>
	 * @throws Exception
	 */
	@GetMapping(value = "/getProjectBySignumForProjectQueue/{signum}")
	public ResponseEntity<Response<List<ProjectModel>>> getProjectBySignumForProjectQueue(@PathVariable("signum") String signum) {
		LOG.info("getProjectBySignumForProjectQueue:SUCCESS");
		return workOrderPlanService.getProjectBySignumForProjectQueue(signum);
	}
	
	

}
