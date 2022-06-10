/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.ericsson.isf.model.BotSavingModel;
import com.ericsson.isf.model.CustomStepJSONModel;
import com.ericsson.isf.model.FlowChartDefModel;
import com.ericsson.isf.model.FlowChartJsonModel;
import com.ericsson.isf.model.FlowChartSaveModel;
import com.ericsson.isf.model.FlowChartStepDetailsModel;
import com.ericsson.isf.model.KPIValueModel;
import com.ericsson.isf.model.LoeMeasurementCriterionModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.ScopeAndMasterTaskModel;
import com.ericsson.isf.model.TaskAndScopeMappingModel;
import com.ericsson.isf.model.WorkFlowApprovalModel;
import com.ericsson.isf.model.WorkFlowAvailabilitySearchModel;
import com.ericsson.isf.model.WorkFlowDefinitionModel;
import com.ericsson.isf.model.WorkflowProficiencyModel;
import com.ericsson.isf.service.FlowChartService;

import io.swagger.annotations.ApiOperation;

/**
 *
 * @author ekarath
 */
@RestController
@RequestMapping("/flowchartController")
public class FlowChartController {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @Autowired
    private FlowChartService flowChartService;

    @ApiOperation(value = "Upload WF FIle", notes = "Imports the file in Tmp table. Validate the Excel data.<br />"
    		+ "1. Task Validation.<br />"
    		+ "2. Tool Validation.<br />"
    		+ "3. Multiple Start Step.<br />"
    		+ "4. Execution Type.<br />"
    		+ "5. Graphical Representation Error.<br />"
    		+ "6. Decision box validation.<br />"
                + "7. More than one execution type for same task.<br />")
    @RequestMapping(value = "/uploadFileForFlowChart/{projectID}/{subActivityID}/{createdBy}/{workFlowName}", method = RequestMethod.POST, consumes = "multipart/form-data")
    public HashMap<String,Object> uploadFileForFlowChart(@PathVariable("projectID") int projectID,
            @PathVariable("subActivityID") int subActivityID,
            @PathVariable("createdBy") String createdBy,
            @PathVariable("workFlowName") String workFlowName,
            @RequestParam("file") MultipartFile file) throws IOException, SQLException {
        try{
            HashMap<String,Object> fileUpload = this.flowChartService.uploadFileForFlowChart(file, projectID, subActivityID, createdBy,workFlowName);
            LOG.info("Upload file to FlowChart: SUCCESS:"  + projectID);
            return fileUpload;
        } catch(Exception ex) {
            throw new ApplicationException(500, "Error while uploading the Workflow due to : "+ex.getMessage());
        }
    }
    
   
    
    @ApiOperation(value = "Save WorkFlow", notes = "Save the JSON and related data in WorkFlow tables.<br />"
    		+ "1. WF validation to be removed(This will be handled in UI).<br />"
    		+ "2. Create the New WorkFlow/ Update the existing WorkFlow header data.<br />"
                + "3. Tables affected TBL_SUBACTIVITY_FLOWCHART_DEF, TBL_FLOWCHART_STEP_DETAILS, TBL_WORK_ORDER_FLOWCHART_LINK_DETAILS")
    @RequestMapping(value = "/saveJSONFromUI", method = RequestMethod.POST)
    public Map<String,Object> saveJSONFromUI(@RequestBody FlowChartSaveModel flowChartSaveModel) {
    	LOG.info("saveJSONFromUI :Success");
        return flowChartService.saveJSONFromUI(flowChartSaveModel);
      
    }
    
    @RequestMapping(value = "/getAndSaveBotSavingSummary", method = RequestMethod.POST)
    public Map<String,Object> getAndSaveBotSavingSummary(@RequestBody BotSavingModel botSavingModel) {
        return flowChartService.getAndSaveBotSavingSummary(botSavingModel);
    }
    
    @RequestMapping(value = "/updatePostPeriodDates", method = RequestMethod.POST)
    public boolean updatePostPeriodDates(@RequestBody BotSavingModel botSavingModel) {
        return flowChartService.updatePostPeriodDates(botSavingModel);
		
    }
    
    @RequestMapping(value = "/getAndSaveBotSaving", method = RequestMethod.POST)
    public Map<String,Object> getAndSaveBotSaving(@RequestBody BotSavingModel botSavingModel) {
        return flowChartService.getAndSaveBotSaving(botSavingModel);
    }
    
    @RequestMapping(value = "/getSavingForAutomaticStepForOthers", method = RequestMethod.POST)
    public Map<String,Object> getSavingForAutomaticStepForOthers(@RequestBody BotSavingModel botSavingModel) {
        return flowChartService.getSavingForAutomaticStepForOthers(botSavingModel);
      
    }
    
    @RequestMapping(value = "/saveBotSavingDetailsAndHistory", method = RequestMethod.POST)
    public void saveBotSavingDetailsAndHistory(@RequestBody List<BotSavingModel> botSavingModels) {
        flowChartService.saveBotSavingDetailsAndHistory(botSavingModels);
      
    }
    
    @RequestMapping(value = "/deleteDefID", method = RequestMethod.POST)
    public void deleteDefID(@RequestBody BotSavingModel botSavingModel) {
        flowChartService.deleteDefID(botSavingModel);
      
    }
    
    @RequestMapping(value = "/getWFVersionsFromSubactivityID", method = RequestMethod.POST)
    public   ResponseEntity<Response<List<Map<String, Object>>>> getWFVersionsFromSubactivityID(
    		@RequestBody BotSavingModel botSavingModel) 
    {
    	LOG.info("get WF version from subactivity id :Success");
        return flowChartService.getWFVersionsFromSubactivityID(botSavingModel);
    }
    
    @RequestMapping(value = "/viewFlowChartForSubActivity/{projectID}/{subActivityID}/{woID}/{wfVersion}/{experiencedMode}/{wfid}", method = RequestMethod.GET)
    public List<Map<String, Object>> viewFlowChartForSubActivity(@PathVariable("projectID") int projectID, 
                                                           @PathVariable("subActivityID") int subActivityID,
                                                           @PathVariable("woID") int woID,
                                                           @PathVariable("wfVersion") int wfVersion,
                                                           @PathVariable("experiencedMode") boolean experiencedMode,
                                                           @PathVariable("wfid") int wfid) {
         return this.flowChartService.viewFlowChartForSubActivity(projectID, subActivityID,woID,wfVersion,experiencedMode,wfid);
    }
    
	@RequestMapping(value = "/getFlowChartByDefID/{subActivityID}/{wfVersion}/{projectID}/{wfid}", method = RequestMethod.GET)
	public List<Map<String, Object>> getFlowChartByDefID(@PathVariable("subActivityID") int subActivityID,
			@PathVariable("wfVersion") int wfVersion, @PathVariable("projectID") int projectID,
			@PathVariable("wfid") int wfid) {
		return this.flowChartService.getFlowChartByDefID(subActivityID, wfVersion, projectID,wfid);
	}
    
   


    @RequestMapping(value = "/getDetails", method = RequestMethod.GET)
    public String getDetails() {
        String details = this.flowChartService.getDetails();
        LOG.info("FlowChart Details: " + details);
        return details;
    }

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

    //Step task Details-->change in model... need to recode.
   @RequestMapping(value = "/getStepTaskDetails/{projectID}/{scopeID}/{subActivityID}/{versionNo}" ,method = RequestMethod.GET)
   public TaskAndScopeMappingModel getStepTaskDetails(@PathVariable("projectID") Integer projectID,
                                                      @PathVariable("scopeID") Integer scopeID,
                                                      @PathVariable("subActivityID") Integer subactivityID,
                                                      @PathVariable("versionNo") Integer versionNo){
       TaskAndScopeMappingModel stepTask= this.flowChartService.getStepTaskDetails(projectID,scopeID,subactivityID,versionNo);
       LOG.info("StepTaskDetails: "+stepTask +","+projectID +","+scopeID +"," +subactivityID,versionNo);
       return stepTask;
   }
   

   @RequestMapping(value = "/getTaskDetailsForJSONStep/{projectID}/{subActivityID}/{taskID}/{versionNo}/{stepID}/{flowChartDefID}" ,method = RequestMethod.GET)
   public ScopeAndMasterTaskModel getTaskDetailsForJSONStep(@PathVariable("projectID") Integer projectID,
                                                      @PathVariable("subActivityID") Integer subactivityID,
                                                      @PathVariable("taskID") Integer taskID,
                                                      @PathVariable("versionNo") Integer versionNo,
                                                      @PathVariable("stepID") String stepID,
                                                      @PathVariable("flowChartDefID") Integer flowChartDefID){
	ScopeAndMasterTaskModel stepTask= this.flowChartService.getTaskDetailsForJSONStep(projectID,subactivityID,taskID,versionNo,stepID,flowChartDefID);
       LOG.info("StepTaskDetails: "+stepTask +","+projectID  +subactivityID);
       return stepTask;
   }
   
    @RequestMapping(value = "/uploadAndSaveFile/{domain}/{technology}/{vendor}/{projectID}/{type}/{uploadedON}/{uploadedBy}", method = RequestMethod.POST, consumes = "multipart/form-data")
    public Response<Void> uploadFile(@PathVariable("domain") String domain, @PathVariable("technology") String technology,
            @PathVariable("vendor") String vendor, @PathVariable("projectID") String ProjectID,
            @RequestPart("file") MultipartFile file, @PathVariable("type") String type,@PathVariable("uploadedON") String uploadedON,@PathVariable("uploadedBy") String uploadedBy) throws IOException, SQLException {
        
        Response<Void> responseData = this.flowChartService.uploadAndSaveFile(domain, technology, vendor, file, ProjectID, type,uploadedON,uploadedBy);
        LOG.info("Upload File:" + domain + "," + technology + "," + vendor + "," + ProjectID );
		return responseData;
    }
    
    

    @RequestMapping(value = "/downloadFile/{fileType}", method = RequestMethod.GET)
    public String downloadFile(@PathVariable("fileType") String fileType) {
        String file_download = this.flowChartService.getFilePath(fileType);
        LOG.info("Download File Type: " + file_download);
        return file_download;
    }
    
    
    @RequestMapping(value = "/downloadNetworkElement/{domain}/{technology}/{vendor}/{projectID}", method = RequestMethod.GET)
    public boolean downloadNetworkElement(@PathVariable("domain") String domain, @PathVariable("technology") String technology,
            @PathVariable("vendor") String vendor, @PathVariable("projectID") String ProjectID, HttpServletResponse response) throws IOException, SQLException {
        
        return this.flowChartService.downloadNetworkElement(domain, technology, vendor, ProjectID, response);
    }

    @RequestMapping(value = "/updateProjectDefinedTask", method = RequestMethod.POST)
    public void updateProjectDefinedTask(@RequestBody FlowChartStepDetailsModel fcStepDetails) {
        this.flowChartService.updateProjectDefinedTask(fcStepDetails);
        LOG.info("TBL_SUBACTIVITY_FLOWCHART_DEF: SUCCESS");
    }

    
    @RequestMapping(value = "/getDetailsForImportExistingWF/{projectID}/{subActivityID}", method = RequestMethod.GET)
    public List<FlowChartJsonModel> getDetailsForImportExistingWF(@PathVariable("projectID") Integer projectID,@PathVariable("subActivityID") Integer subActivityID) {
        if (subActivityID != 0) {
            List<FlowChartJsonModel> detailsForImporting = this.flowChartService.getDetailsForImportExistingWF(projectID,subActivityID);
            return detailsForImporting;
        } else {
            throw new ApplicationException(500, "Invalid input... SubActivityID 0 !!!");
        }
    }
    
    @RequestMapping(value = "/searchWFAvailabilityforScope/{projectID}/{domain}/{subDomain}/{serviceArea}/{subServiceArea}/{technology}/{activity}/{subActivity}", method = RequestMethod.GET)
    public List<WorkFlowAvailabilitySearchModel> searchWFAvailabilityforScope(
    		@PathVariable("projectID") String projectID,
            @PathVariable("domain") String domain,
            @PathVariable("subDomain") String subDomain,
            @PathVariable("serviceArea") String serviceArea,
            @PathVariable("subServiceArea") String subServiceArea,
            @PathVariable("technology") String technology,
            @PathVariable("activity") String activity,
            @PathVariable("subActivity") String subActivity,
            @RequestHeader(value="MarketArea",required=false) String marketArea) {
    	try {
			activity = URLDecoder.decode(activity, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List<WorkFlowAvailabilitySearchModel> searchWF = this.flowChartService.searchWFAvailabilityforScope(projectID, domain, subDomain, serviceArea, subServiceArea, technology, activity, subActivity,marketArea);
        LOG.info("user_sp_searchWFAvailabilityforScope: SUCCESS :" + projectID + "," + domain + "," + subDomain + "," + serviceArea + "," + subServiceArea + "," + technology + "," + activity + "," + subActivity+","+marketArea);
        return searchWF;
    }

    @RequestMapping(value = "/addWorkFlowToProject", method = RequestMethod.POST)
    public void addWorkFlowToProject(@RequestBody FlowChartDefModel flowChartDefModel) {
        this.flowChartService.addWorkFlowToProject(flowChartDefModel);
        LOG.info("TBL_SUBACTIVITY_FLOWCHARTDEF: SUCCESS");
    }
    
    
    @RequestMapping(value = "/createJSONForStep", method = RequestMethod.POST)
    public String createJSONForStep(@RequestBody CustomStepJSONModel customModel) {
    return this.flowChartService.createJSONForStep(customModel);
    }
    
    @RequestMapping(value = "/getElementType/{projectID}", method = RequestMethod.GET)
    public List<String> getElementType(@PathVariable("projectID") int projectID) {
        LOG.info("getElementType: SUCCESS "+projectID);
        return this.flowChartService.getElementType(projectID);
  
    }
    
    @RequestMapping(value = "/getMarketDetails/{ProjectID}", method = RequestMethod.GET)
    public List<String> getMarketDetails(@PathVariable("ProjectID") int projectID) {
        LOG.info("getElementType: SUCCESS");
        return this.flowChartService.getMarketDetails(projectID);
  
    }
    
    @RequestMapping(value = "/getVersionName/{ProjectID}/{subActivityID}", method = RequestMethod.GET)
    public List<FlowChartDefModel> getVersionName(@PathVariable("ProjectID") int projectID,@PathVariable("subActivityID") int subActivityID) {
        
        return this.flowChartService.getVersionName(projectID,subActivityID);
  
    }
    
     @RequestMapping(value = "/getVersionNameCurProjId/{flowChartDefID}", method = RequestMethod.GET)
    public List<FlowChartDefModel> getVersionNameCurProjId(@PathVariable("flowChartDefID") int flowChartDefID) {
        return this.flowChartService.getVersionNameCurProjId(flowChartDefID);
  
    }
    
    @RequestMapping(value = "/getWorkFlowVersionData/{ProjectID}/{subActivityID}/{wfid}", method = RequestMethod.GET)
    public List<FlowChartDefModel> getWorkFlowVersionData(@PathVariable("ProjectID") int projectID,@PathVariable("subActivityID") int subActivityID,@PathVariable("wfid") int wfid) {
        return this.flowChartService.getWorkFlowVersionData(projectID,subActivityID,wfid);
  
    }
        
    @RequestMapping(value = "/getWorkFlowForApproval/{projectID}", method = RequestMethod.GET)
    public List<WorkFlowApprovalModel> getWorkFlowForApproval(@PathVariable("projectID") int projectID) {
        return this.flowChartService.getWorkFlowForApproval(projectID);
  
    }
    
    @RequestMapping(value = "/rejectWorkFlow", method = RequestMethod.GET)
    public void rejectWorkFlow(@RequestParam(value="projectID") int projectID,
                                                              @RequestParam(value="subActivityID") int subActivityID,
                                                              @RequestParam(value="flowchartDefID") int flowchartDefID,
                                                              @RequestParam(value="wfVersion") int wfVersion,
                                                              @RequestParam(value="managerSignumID") String managerSignumID,
                                                              @RequestParam(value="employeeSignumID") String employeeSignumID,
                                                              @RequestParam(value="reason") String reason)  {
         this.flowChartService.rejectWorkFlow(projectID,subActivityID,flowchartDefID,wfVersion,managerSignumID,employeeSignumID,reason);
  
    }
    
    @RequestMapping(value = "/approveWorkFlow", method = RequestMethod.POST)
    public Response<Void> approveWorkFlow(@RequestParam(value="projectID") int projectID,
                              @RequestParam(value="subActivityID") int subActivityID,
                              @RequestParam(value="flowchartDefID") int flowchartDefID,
                              @RequestParam(value="wfName") String wfName,
                              @RequestParam(value="wfVersion") int wfVersion,
                              @RequestParam(value="managerSignumID") String managerSignumID,
                              @RequestParam(value="employeeSignumID") String employeeSignumID,
                              @RequestParam(value="isCreateNew") boolean isCreateNew,
                              @RequestParam(value="newWFName") String newWFName,
                              @RequestParam(value="wfid") int wfid,
                              @RequestParam(value="wfEditReason") String wfEditReason,
                              @RequestBody String flowChartJSON) {
    	return this.flowChartService.approveWorkFlow(projectID,subActivityID,flowchartDefID,wfName,wfVersion,managerSignumID,employeeSignumID,
    			isCreateNew,newWFName,wfid,wfEditReason,flowChartJSON);
    }

    @RequestMapping(value = "/getExecutionPlanTasks", method = RequestMethod.GET)
    public Map<String,Object> getExecutionPlanTasks(@RequestParam(value="filterGroups",required=false) String filterGroups){
    	Map<String,Object> response= new HashMap<>();
    	List l=new ArrayList<>();
    	if(taskList.size()==0){
    		Map<String, Object> r1= new HashMap<>();
        	r1.put("id", "1");
        	r1.put("text", "test");
        	r1.put("start_date", "1");
        	r1.put("duration", "5");
        	l.add(r1);
        	response.put("data", l);
        	return response;
    	}
    	
    	response.put("data", taskList);
    	return response;
    }
    
    List taskList=new ArrayList<>();
    
    
    Map<String,Object> response= new HashMap<>();
    @RequestMapping(value = "/addExecutionPlanTask", method = RequestMethod.POST)

    public boolean addExecutionPlanTask(@RequestParam Map<String,String> allRequestParams){
        for(String k:allRequestParams.keySet()){
        	allRequestParams.put(k, allRequestParams.get(k).substring(allRequestParams.get(k).indexOf("_")+1));
        }
    	taskList.add(allRequestParams);
    	return true;
    }
   
        @RequestMapping(value = "/saveJsonForWorkFlow/{signum}", method = RequestMethod.POST)
        public void saveJsonForWorkFlow(@RequestBody List<WorkFlowDefinitionModel> workFlowDefinitionModel,@PathVariable("signum") String signum) {
        this.flowChartService.saveJsonForWorkFlow(workFlowDefinitionModel,signum);
    }
     @RequestMapping(value = "/getJsonDataForWorkFlow/{projectID}/{subActID}/{versionNO}", method = RequestMethod.GET)  
     public WorkFlowDefinitionModel getJsonDataForWorkFlow(@PathVariable("projectID") int projectID,
             @PathVariable("subActID") int subActID,
             @PathVariable("versionNO")  int versionNO)
      { 
        return flowChartService.getJsonDataForWorkFlow(projectID, subActID, versionNO);
      }
     
     @RequestMapping(value = "/getWFOwners/{projectID}", method = RequestMethod.GET)  
     public List<Map<String,String>> getWFOwners(@PathVariable("projectID") int projectID){ 
        return flowChartService.getWFOwners(projectID);
      }
    
    @RequestMapping(value = "/updateWFOwner/{flowChartDefID}/{signumID}", method = RequestMethod.POST)
    public void updateWFOwner(@PathVariable("flowChartDefID") int flowChartDefID,
                              @PathVariable("signumID") String signumID){
         flowChartService.updateWFOwner(flowChartDefID,signumID);
    } 
     
	@RequestMapping(value = "/downloadFlowChartData", method = RequestMethod.GET)
	public void downloadFlowChartData(
			HttpServletResponse response,
			@RequestParam(value = "flowChartDefID", required = true) int flowChartDefID)
			throws Exception {
		this.flowChartService.downloadFlowChartData(flowChartDefID, response);
	}

	@RequestMapping(value = "/getDeployedBotList/{signumID}", method = RequestMethod.GET)
	public List<Map<String, Object>> getDeployedBotList(@PathVariable("signumID") String signumID) {
		return this.flowChartService.getDeployedBotList(signumID);
	}
	
	@RequestMapping(value = "/getWFBySubActivityId", method = RequestMethod.GET)
	public List<Map<String, Object>> getWFBySubActivityId(@RequestParam("subActivityId") Integer subActivityId,@RequestParam("projectId") Integer projectId) {
		return this.flowChartService.getWFBySubActivityId(subActivityId,projectId);
	}
	@RequestMapping(value = "/getFlowChartEditReason", method = RequestMethod.GET)
	public List<Map<String, Object>> getFlowChartEditReason() {
		return this.flowChartService.getFlowChartEditReason();
	}
	
	@RequestMapping(value = "/checkUserAuthenticationForWorkFlow", method = RequestMethod.GET)
    public Response<Void> checkUserAuthenticationForWorkFlow(@RequestHeader("Signum") String signum,@RequestHeader("marketArea") String marketArea,
    		@RequestHeader("Role") String role, @RequestParam("projectId") int projectId) {   
       return this.flowChartService.checkUserAuthenticationForWorkFlow(signum,marketArea,role,projectId);
    }
	
	/**
	 * API Name: flowchartController/getForwardReverseWFTransition
	 * API Description: This API gets Assessed>>Experienced and Experienced>>Assessed transition.
	 * @author elkpain
	 * @purpose This API is used for getting Forward Reverse WF Transition.
	 * @return ResponseEntity<Response<List<Map<String, Object>>>>
	 */
	@RequestMapping(value = "/getForwardReverseWFTransition", method = RequestMethod.GET)
	public  ResponseEntity<Response<List<Map<String, Object>>>> getForwardReverseWFTransition() {
		
		LOG.info("get Forward Reverse WF Transition:Success");
		return this.flowChartService.getForwardReverseWFTransition();
	}

	/**
	 * @author emntiuk
	 * @purpose This API is used to get the list of All Proficiency Name from table refData.tbl_WF_Proficiency_Type
	 * @return
	 */
	@RequestMapping(value = "/getListOfViewMode", method = RequestMethod.GET)
	public ResponseEntity<Response<List<String>>> getListOfViewMode() {
            
		LOG.info("get List of View Mode :Success");
		return flowChartService.getListOfViewMode();
	}
	
	/**
	 * API Name: flowchartController/getListOfKPIs
	 * API Description: This API gets the list of KPIs for Assessed>>Experienced(forward),Experienced>>Assessed transition.
	 * @author elkpain
	 * @purpose This API is used to get list of KPIs for forward and reverse path.
	 * @Param  proficiencyLevelSource,saveMode,subactivityFlowChartDefID
	 * @return ResponseEntity<Response<List<Map<String, Object>>>>
	 */
	@RequestMapping(value = "/getListOfKPIs", method = RequestMethod.GET)
	public  ResponseEntity<Response<List<KPIValueModel>>> getListOfKPIs(@RequestParam(value = "proficiencyLevelSource", required = false) Integer proficiencyLevelSource,
			@RequestParam("saveMode") String saveMode,@RequestParam(value = "subactivityFlowChartDefID", required = false) Integer subactivityFlowChartDefID) {
		
		LOG.info("get list of KPIs:Success");
		return this.flowChartService.getListOfKPIs(proficiencyLevelSource,saveMode,subactivityFlowChartDefID);
	}
	/**
	 * API Name: flowchartController/resetProficiency
	 * API Description: This API used to reset Proficiency of a workflow.
	 * It reset the proficiency level to assessed .
	 * @author ekarmuj
	 * @purpose This API used to reset Proficiency of a workflow.
	 * @Param  workflowProficiencyModel
	 * @return ResponseEntity<Response<Void>>
	 */
	
	@RequestMapping(value = "/resetProficiency", method = RequestMethod.POST)
	public ResponseEntity<Response<Void>> resetProficiency(@RequestBody WorkflowProficiencyModel workflowProficiencyModel){
		return this.flowChartService.resetProficiency(workflowProficiencyModel);
		
	}
	
	
	@RequestMapping(value = "/getLoeMeasurementCriterion", method = RequestMethod.GET)
	public ResponseEntity<Response<List<LoeMeasurementCriterionModel>>> getLoeMeasurementCriterion() {
            
		LOG.info("get List of LOE Measurement criterion :Success");
		return flowChartService.getLoeMeasurementCriterion();
	}
	
	@RequestMapping(value = "/getOldStepBotSavingDetails", method = RequestMethod.POST)
	public  ResponseEntity<Response<List<Map<String, Object>>>> getOldStepBotSavingDetails(
	@RequestBody BotSavingModel botSavingModel,
	@RequestParam (value = "term", required = true) String term) 
			    {
			    	LOG.info("get WF version from subactivity id :Success");
			        return flowChartService.getOldStepBotSavingDetails(botSavingModel,term);
			    }
	
}
