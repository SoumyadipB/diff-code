/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.ericsson.isf.model.BotSavingModel;
import com.ericsson.isf.model.ErrorBean;
import com.ericsson.isf.model.FlowChartDefModel;
import com.ericsson.isf.model.FlowChartDependencyModel;
import com.ericsson.isf.model.FlowChartJsonModel;
import com.ericsson.isf.model.FlowChartPopulateDataModel;
import com.ericsson.isf.model.FlowChartSaveModel;
import com.ericsson.isf.model.FlowChartStepDetailsModel;
import com.ericsson.isf.model.FlowChartStepModel;
import com.ericsson.isf.model.KPIValueModel;
import com.ericsson.isf.model.KpiModel;
import com.ericsson.isf.model.LoeMeasurementCriterionModel;
import com.ericsson.isf.model.ScopeTaskMappingModel;
import com.ericsson.isf.model.StepDetailsModel;
import com.ericsson.isf.model.UserProfileHistoryModel;
import com.ericsson.isf.model.WFStepInstructionModel;
import com.ericsson.isf.model.WorkFlowApprovalModel;
import com.ericsson.isf.model.WorkFlowAvailabilitySearchModel;
import com.ericsson.isf.model.WorkFlowDefinitionModel;
import com.ericsson.isf.model.WorkFlowLinksModel;
import com.ericsson.isf.model.WorkFlowLinksVerticesModel;
import com.ericsson.isf.model.WorkFlowStepAttrModel;
import com.ericsson.isf.model.WorkFlowStepsModel;
import com.ericsson.isf.model.WorkOrderModel;
import com.ericsson.isf.model.WorkflowProficiencyModel;

/**
 *
 * @author ekarath
 */
public interface FlowChartMapper {

    public List<ErrorBean> uploadExcel(@Param("FileTable") String FileTable, @Param("projectID") int projectID, @Param("subActivityID") int subActivityID, @Param("signumID") String signumID,@Param("workFlowName") String workFlowName);

    public List<FlowChartStepModel> getFlowChartStepDetails(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID);

    public void saveJSONForStepID(@Param("json") String json, @Param("stepID") int stepID);

    public List<FlowChartDependencyModel> getDependencyStep(@Param("subActivityID") int subActivityID);

    public void saveJSONFromUI(@Param("flowChartDefModel") FlowChartDefModel flowChartDefModel,
                               @Param("type") String type);

    public List<Map<String,Object>> viewFlowChartForSubActivity(@Param("projectID") int projectID, @Param("subActivityID") int subActivityID,
            @Param("woID") int woID,@Param("wfid") int wfid);

    public List<FlowChartStepModel> getStepJSON(@Param("projectID") int projectID, @Param("subActivityID") int subActivityID);

    public void saveJSONLinkForDependency(@Param("jsonLink") String jsonLink, @Param("depID") int depID);

    public List<ScopeTaskMappingModel> getScopeTaskMapping(@Param("projectID") Integer projectID, @Param("scopeID") Integer scopeID,
            @Param("subActivityID") Integer subactivityID, @Param("taskID") int taskID);

    public void insertScopeTaskMapping(@Param("ScopeTaskMappingResult") ScopeTaskMappingModel scopeTaskMapping);

    public String insertNetworkelements(@Param("FileTable") String FileTable, @Param("domain") int domain, @Param("technology") int technology,
            @Param("vendor") int vendor, @Param("projectID") int ProjectID,@Param("uploadedON") String uploadedON,@Param("uploadedBy") String uploadedBy);

    public void updateProjectDefinedTask(@Param("fcStepDetails") FlowChartStepDetailsModel fcStepDetails);
    
    public List<Map<String, Object>> downloadNetworkElement(@Param("domain") String domain,@Param("technology") String technology,@Param("vendor") String vendor,@Param("projectID") String projectID);

    public List<FlowChartStepDetailsModel> checkFLowEntry(@Param("projectID") int subActivityID, @Param("subActivityID") int stepID,
            @Param("taskID") int taskID);

//    public List<FlowChartJsonModel> getDetailsForImportExistingWF(@Param("projectID") Integer projectID, @Param("subActivityID") Integer subActivityID,
//            @Param("customerID") Integer customerID, @Param("countryID") Integer countryID, @Param("marketAreaID") Integer marketAreaID);

    public List<WorkFlowAvailabilitySearchModel> searchWFAvailabilityforScope(@Param("projectID") String projectID, @Param("domain") String domain, @Param("subDomain") String subDomain, @Param("serviceArea") String serviceArea,
            @Param("subServiceArea") String subServiceArea, @Param("technology") String technology,
            @Param("activity") String activity, @Param("subActivity") String subActivity,@Param("marketArea") String marketArea);

    public FlowChartDefModel checkFlowChartVersion(@Param("flowChartDefModel") FlowChartDefModel flowChartDefModel);

    public void addWorkFlowToProject(@Param("flowChartDefModel") FlowChartDefModel flowChartDefModel);

    public void inActiveWorkflowForProject(@Param("subActivityFlowChartDefID") int subActivityFlowChartDefID);

     public void updateWFDefinition(Map<String,String> params);
    
    public void insertInFlowChartStepDetails(@Param("subActFCDefID")Integer subActFCDefID, @Param("stepId") String stepId, 
                                             @Param("stepName") String stepName, @Param("taskId") String taskId,
                                             @Param("taskName") String taskName, @Param("exeType") String executionType,
                                             @Param("toolId") String toolId,
                                             @Param("reason") String reason,@Param("versionNO") int versionNO,
                                             @Param("masterTask") String masterTask,@Param("stepType") String stepType,
                                             @Param("rpaID") String rpaID,@Param("wiid") String wiid,@Param("stepData") Map<String, Object> stepData, @Param("outputUpload") String outputUpload,@Param("cascadeInput") String cascadeInput);
    
     public void updateUiFlowChartStepDetails(@Param("subActFCDefID")Integer subActFCDefID, @Param("stepId") String stepId, @Param("stepName") String stepName, @Param("taskId") String taskId, @Param("taskName") String taskName, @Param("exeType") String executionType, @Param("avgEft") String avgEstdEffort, @Param("toolId") String toolId, @Param("reason") String reason);
    
    public void saveTaskDetailsForStep(@Param("flowChartDefID") int flowChartDefID,@Param("flowChartStepID") int flowChartStepID, 
                                       @Param("stepName") String stepName,@Param("taskID") int taskID,@Param("task") String task, 
                                       @Param("executionType") String executionType,@Param("avgEstdEffort") float avgEstdEffort,
                                       @Param("toolID") int toolID,@Param("order") int ordering,@Param("versionNumber") int versionNumber);

    public List<String> getElementType(@Param("projectID") int projectID);
            
    public List<String> getMarketDetails(@Param("projectID") int projectID);    

   public List<FlowChartDefModel> getVersionName(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID);
   public List<FlowChartDefModel> getVersionNameCurProjId(@Param("flowChartDefID") int flowChartDefID);     
   
   public List<FlowChartDefModel> getWorkFlowVersionData(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID,@Param("wfid") int wfid);     

    public void insertFlowchartStepLinkDetails(@Param("subActivityDefID") Integer subActFCDefID,@Param("sourceID") String sourceID,@Param("targetID") String targetID);

    public void deleteFlowchartStepDetails(@Param("subActivityDefID") Integer subActivityDefID,@Param("stepID") String stepID,
                                           @Param("taskID") Integer taskID,@Param("versionNo") int versionNo);

    public List<FlowChartJsonModel> getDetailsForImportExistingWF(@Param("projectID")Integer projectID,@Param("subActivityID")Integer subActivityID);

    public ScopeTaskMappingModel getTaskDetailsForJSONStep(@Param("projectID") Integer projectID,@Param("subActivityID") Integer subactivityID,
                                                           @Param("taskID") Integer taskID,@Param("versionNo") Integer versionNo,
                                                           @Param("stepID") String stepID,@Param("flowChartDefID") Integer flowChartDefID);

    public boolean chekIFDataCopiedToScopeTable(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID);

    public void deleteStepTaskDetails(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID);

    public Map<String,Integer> getFlowDefIDForSubActivity(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID);
    
    public List<ScopeTaskMappingModel> getProjectConfigTask(@Param("projectID")int projectID, @Param("subActivityID") int subActivityID,
                                                            @Param("versionNo") int versionNo);

    public void updatePrjectConfigTaskDetails(@Param("projectID")int projectID, @Param("subActivityID") int subActivityID,
                                              @Param("taskID") int taskID,@Param("executionType") String executionType,@Param("versionNumber") int versionNumber);

    public List<Map<String,Object>> viewFlowChartForSubActivityWithVersion(@Param("projectID") int projectID, @Param("subActivityID") int subActivityID,
            @Param("woID") int woID, @Param("wfVersion") int wfVersion,@Param("wfid") int wfid);

    public boolean checkFlowChartActiveStatus(@Param("flowChartDefID") int flowChartDefID);

    public boolean getMaxVerionStatus(@Param("projectID") int projectID, @Param("subActivityID") int subActivityID);

//    public void insertJSONFromUI(@Param("projectID") int projectID, @Param("subActivityID") int subActivityID,
//                                 @Param("flowchartJSON") String flowchartJSON,@Param("signum") String signum,@Param("newVersion") int newVersion,
//                                 @Param("wfName") String wfName);

    public void insertJSONFromUI(@Param("flowModel") FlowChartDefModel flowModel);
    
    public void updateActiveStatus(@Param("projectID") int projectID, @Param("subActivityID") int subActivityID,
                                   @Param("subActivityDefID") int subActivityDefID,@Param("signumID") String signumID);

    public List<WorkFlowApprovalModel> getWorkFlowForApproval(@Param("projectID") int projectID);

    public void rejectWorkFlow(@Param("projectID") int projectID, @Param("subActivityID") int subActivityID,@Param("flowchartDefID") int flowchartDefID);

    public Map<String, String> getEmployeeEmailDetails(@Param("signumID") String signumID);

    public FlowChartDefModel getFlowChartJSON(@Param("projectID") int projectID, @Param("subActivityID") int subActivityID,@Param("wfVersion")int wfVersion,@Param("signumID") String eSignumID,@Param("wfid") int wfid);

    public int getParentWorkFlowDefID(@Param("flowchartDefID") int flowchartDefID);
    
    public FlowChartDefModel getFlowchartDetails(@Param("subactivityflowchartdefid") int subactivityflowchartdefid);

    public void updateParentWorkFlow(@Param("projectID") int projectID, @Param("subActivityID")int subActivityID,
                                     @Param("parentWFDefID") int parentWFDefID,@Param("flowChartJSON") String flowChartJSON);

    public void deactivateCustomVersion(@Param("projectID") int projectID, @Param("subActivityID")int subActivityID,
                                          @Param("flowchartDefID") int flowchartDefID, @Param("mSignumID") String mSignumID,
                                          @Param("reason") String reason);

    public int getUpdatedWorkFlowVersion(@Param("projectID") int projectID, @Param("subActivityID")int subActivityID,
                                         @Param("parentWFDefID") int parentWFDefID);

    public void saveApprovalLogDetails(@Param("flowchartDefID")int flowchartDefID, @Param("status")String status, 
                                       @Param("managerSignumID")String managerSignumID,@Param("employeeSignumID") String employeeSignumID);

    public void inActiveCustomWFWO(@Param("woID")int woID,@Param("flowchartDefID") int flowchartDefID,@Param("signumID") String eSignumID);

    public void updateFlowChartStepDetails(@Param("woID")int woID,@Param("subActivityFlowChartDefID") int flowchartDefID,@Param("signumID") String eSignumID);

    public void deActivatePreviousVersion(@Param("projectID") int projectID,
                                          @Param("subActivityID") int subActivityID);

    public void updateWorkFlowStepDetails(@Param("subActivityFlowChartDefID") int subActivityFlowChartDefID,@Param("flowchartDefID") int flowchartDefID,@Param("wfVersion") int wfVersion,@Param("updatedVersion") int updateVersion);

	public List<FlowChartDefModel> getFlowChartBySubActFCDefID(@Param("subActFCDefID")  Integer subActFCDefID);

	public void updateVersionInFlowChart(@Param("subActivityFlowChartStepID") int subActivityFlowChartStepID,@Param("version") int version);

	public void updatePrjectConfigTaskDetailsVersion(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID, @Param("taskID") int taskID, @Param("executionType")  String executionType, @Param("version") int version);

	public int getActiveFlowDefIDForSubActivity(@Param("projectID") int projectID,@Param("subActivityID")  int subActivityID);

    public int getWorkFlowVersion(@Param("parentWFDefID") int parentWFDefID);

    public void updateWorkFlowStepLinkDetails(@Param("subActivityFlowChartDefID") int subActivityFlowChartDefID,@Param("flowchartDefID") int flowchartDefID);

    public void updateFlowChartStepDetailsTasks(@Param("fcStepDetails") FlowChartStepDetailsModel fcStepDetails);

    public int getStepAvgEffortDetails(@Param("fcStepDetails") FlowChartStepDetailsModel fcStepDetails);

    public String getMasterTask(@Param("taskID") int taskID,@Param("subActivityID") int subActivityID);

    public int getSubActivityFlowchartDefIdByVersionNo(@Param("workFlowName") String workFlowName,@Param("versionNO") int versionNO,@Param("subActId") int subActId,@Param("projId") int projId);

    public int getWFVersioNo(@Param("wFID")int wFID);

    public boolean checkIFStepDataExists(@Param("subActFCDefID")Integer subActFCDefID, @Param("stepId") String stepId, @Param("stepName") String stepName, @Param("taskId") int taskId, @Param("versionNO") int versionNO);

    public void updateFlowChartStepDetailsValue(@Param("subActFCDefID")Integer subActFCDefID, @Param("stepId") String stepId, @Param("stepName") String stepName,
                                                @Param("taskId") String taskId, @Param("taskName") String taskName,@Param("exeType") String executionType,
                                                @Param("toolId") String toolId,@Param("reason") String reason,
                                                @Param("versionNo") int versionNo,@Param("masterTask") String masterTask,
                                                @Param("rpaID") String rpaID,@Param("wiid") String wiid, @Param("outputUpload") String outputUpload,
                                                @Param("cascadeInput") String cascadeInput);
    
    public List<Map<String,Object>> getWfListForPlan(@Param("projectId")Integer projectId,@Param("sourceId") Integer sourceId);
    
    public List<Map<String,Object>> getWfListForPlanForExternalSource(@Param("projectId")Integer projectId,@Param("sourceId") Integer sourceId);

    public Map<String,String> checkIFExecutionExists(@Param("subActFCDefID")Integer subActFCDefID,@Param("taskID") int taskID,
                                          @Param("taskName") String taskName,@Param("versionNo") int versionNumber);

   public String insertNetworkelementsUpdate(@Param("FileTable") String FileTable, @Param("domain") int domain,@Param("technology") int technology, @Param("vendor") int vendor, @Param("projectID") int projectID,@Param("uploadedON") String uploadedON,@Param("uploadedBy") String uploadedBy);
 
   
   public int saveWorkflowDefinition(@Param("workFlowDefinitionModel") WorkFlowDefinitionModel workFlowDefinitionModel,@Param("signum") String signum);
   
   public void saveworkFlowSteps(@Param("workFlowSteps") WorkFlowStepsModel workFlowSteps , @Param("workFlowID") int workFlowID,@Param("signum") String signum);
  
   public void saveWorkFlowLinks(@Param("workFlowLink") WorkFlowLinksModel workFlowLink,@Param("workFlowID") int workFlowID,@Param("signum") String signum);

   public void saveWorkFlowStepAttr(@Param("workFlowStepAttr") WorkFlowStepAttrModel workFlowStepAttr,@Param("flowChartStepId") int flowChartStepId,@Param("signum") String signum);
  
    public void saveWorkFlowVertices(@Param("vertice") WorkFlowLinksVerticesModel vertice,@Param("workFlow_LinkID") int workFlow_LinkID,@Param("signum") String signum);
   
   public WorkFlowDefinitionModel getJsonDataForWorkFlow(@Param("projectID") int projectID,@Param("subActID") int subActID,@Param("versionNO") int versionNO);

    public String getWorfFlowName(@Param("subActFCDefID") int flowchartDefID);

    public int getLatestDefID(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID);

    public void deleteEmptyData(@Param("subaActivityDefID") int subaActivityDefID);

    public List<Map<String, Object>> getDefIDAndVersionName(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID);

       public List<Map<String, Object>> getStepDetails(@Param("subActivityDefID") int subActivityDefID);

    public boolean checkIFLinkDataExists(@Param("subActivityDefID") int subActFCDefID,
                                         @Param("sourceID")String sourceID,
                                         @Param("targetID")String targetID);

    public void deleteStepData(@Param("subaActivityDefID") int subaActivityDefID);

    public List<Map<String,String>> getWFOwners(@Param("projectID") int projectID);

    public void updateWFOwner(@Param("subaActivityDefID") int flowChartDefID,@Param("signumID") String signumID);

    public List<Map<String, Object>> getFlowChartData(@Param("subaActivityDefID") int flowChartDefID);
    
    public String getWFName(@Param("projectID") int projectID, @Param("subActivityID") int subActivityID,@Param("wfVersion") int wfVersion,@Param("wfid") int wfid);

    public int getExperiencedVersionNo(@Param("projectID") int projectID, @Param("subActivityID") int subActivityID,@Param("wfName") String wfName);

    public List<Map<String, Object>> getFlowChartForExperiencedMode(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID, 
                                                                  @Param("wfName") String wfName, @Param("wfVersion") int wfVersion,@Param("wfid") int wfid);

    public void deleteFlowStepData(@Param("subActivityDefID") int subActivityDefID);

    public void deleteFlowStepLinkData(@Param("subActivityDefID") int subActivityDefID);
            
    public void deleteWFDefData(@Param("subActivityDefID") int subActivityDefID);

    public Map<String, Object> getStepExistingData(@Param("subActivityDefID") int subActFCDefID,
                                                   @Param("stepID")String stepID,
                                                   @Param("versionNumber") int versionNumber);

    public int getWorkFlowLatestVersion(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID);

    public String getExistingWFName(@Param("subActivityDefID") int subActivityDefID);

    public void deactivateParentVersion(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID,
                                        @Param("parentWFDefID") int parentWFDefID,@Param("mSignumID") String mSignumID,
                                        @Param("reason") String reason);

    public void insertDataToFlowChartStepDetails(@Param("childFlowChartDefID") int childFlowChartDefID,@Param("parentFlowChartDefID") int parentFlowchartDefID,
                                                 @Param("childWFVersion")int childWFVersion,@Param("parentWFVersion") int parentWFVersion,@Param("mSignumID") String mSignumID);

    public void insertDataToFlowChartLinkDetails(@Param("childFlowChartDefID") int subActivityFlowChartDefID,@Param("parentFlowChartDefID") int flowchartDefID);

    public void checkWFNameExistsAndUpdate(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID,
                                              @Param("wfName")String wfName,@Param("signumID") String signumID);

	public Map<String, Object> getAdditionalInfoOfWorkFlow(@Param("subActivityID")int subActivityID);

	public List<Map<String, Object>> getAllFlowCharts(@Param("MA") String  MA);

	public List<Map<String, Object>> getAllFlowChartsBySubActivityFlowChartDefID(
			@Param("subActivityFlowChartDefID") int subActivityFlowChartDefID);

	public Map<String, Object> getTaskNameByID(@Param("taskID") String taskID);

	public Map<String, Object> getToolNameByID(@Param("toolID") String toolID);

	public List<Map<String, Object>> getDeletedSteps(@Param("oldDefID") int oldDefID, @Param("newDefID") int newDefID);

	public List<Map<String, Object>> getAddedSteps(@Param("oldDefID") int oldDefID, @Param("newDefID") int newDefID);

	public Map<String, Object> getSavingForManualStep(@Param("stepID") String stepID,@Param("oldDefID") int oldDefID);

	public Map<String, Object> getSavingForAutomaticStep(@Param("oldDefID") int oldDefID, @Param("stepID") String stepID);

	public void deleteDefID(@Param("defID") int defID);

	public void saveBotSavingDetailsAndHistoryNew(@Param("newStepID") String newStepID,
			@Param("newStepName") String newStepName,@Param("type") String type,@Param("botSavingModel") BotSavingModel botSavingModel,@Param("executionType") String executionType,@Param("savings") String savings);

	
	public void saveBotSavingDetailsAndHistoryOld(@Param("newStepID") String newStepID,
			@Param("newStepName") String newStepName,@Param("type") String type,@Param("botSavingModel") BotSavingModel botSavingModel,@Param("executionType") String executionType,@Param("savings") String savings,@Param("emeCalculationDefID") String emeCalculationDefID);

	public void saveBotSaving(@Param("botSavingModel") BotSavingModel botSavingModel,@Param("totalSavings") String totalSavings);

	public void updateOldDefID(@Param("defID") int defID);

	public List<Map<String, Object>> checkWFNameExistsWithExpert(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID,
            @Param("wfName")String wfName,@Param("signumID") String signumID);

	public void deleteSubAcitvityDefID(@Param("defID") int defID);
	
	public List<Map<String, Object>> getWFVersionsFromSubactivityID(@Param("botSavingModel") BotSavingModel botSavingModel,RowBounds rowBounds,@Param("pageSize") int pageSize);


	public List<Map<String, Object>> getFlowChartByDefID(@Param("subActivityID") int subActivityID, @Param("wfVersion") int wfVersion,@Param("projectID") int projectID,@Param("wfid") int wfid);

	public Map<String, Object> checkIfStepExperienced(@Param("stepID")  String stepID,@Param("oldDefID") int oldDefID);

	public Map<String, Object> getSavingForAutomaticStepForOthers(@Param("botSavingModel") BotSavingModel botSavingModel);

	public Map<String,Object> checkisCalculationNeeded(@Param("stepID")  String stepID,@Param("oldDefID") int oldDefID);

	public List<Map<String, Object>> getDeployedBotList(@Param("signumID")   String signumID);

	public void updateStepDetailsSavings(@Param("botSavingModel") BotSavingModel botSavingModel);

	public void updateStepDetailsSavingsRemarks(@Param("botSavingModel") BotSavingModel botSavingModel);

	public void deactiveBotStatusForOldReocrds(@Param("botSavingModel") BotSavingModel botSavingModel);

	public String getPostSumSaving(@Param("rpaID") int rpaID,@Param("defID") int defID);

	public String getPreSumSaving(@Param("rpaID") int rpaID,@Param("defID") int defID);

	public Map<String, Object> getSavingForAutomaticStepForNewDef(
			@Param("botSavingModel")  BotSavingModel botSavingModel);

	public boolean updatePostPeriodDates(@Param("botSavingModel") BotSavingModel botSavingModel);
	
	public List<Map<String,Object>> getWFBySubActivityId(@Param("subActivityId") Integer subActivityId,@Param("projectId") Integer projectId);

	public Integer getPreAutomaticSumSaving(@Param("rpaID") int rpaID,@Param("defID") int defID);

	public String getAvgBookingHours(@Param("rpaID") int rpaID,@Param("defID") int defID);
	
	public List<HashMap<String, Object>> getWFVersionDetails(@Param("projectId")int projectId, 
			@Param("subActivityId")int subActivityId, @Param("SubActflowChartDefId")int SubActflowChartDefId, 
			@Param("versionOld")int versionOld);
	
	public void updateWFVersionDetails(@Param("executionPlanDetailId")int executionPlanDetailId, 
			@Param("taskJson")String taskJson, @Param("versionNew")int versionNew);

	public boolean getWorkflow(@Param("projectID") int projectID,@Param("WorkFlowName") String workFlowName,@Param("subActivityID") int subActivityID);

	public Integer getMaxWFID();

	public List<Map<String, Object>> getFlowChartEditReason();

	public int getWFID(@Param("subActivityFlowChartDefID") int subActivityFlowChartDefID);

	public boolean getWorkflowForWFID(@Param("projectID") int projectID,@Param("wfName") String wfName,@Param("subActivityID") int subActivityID,@Param("workFlowID") int workFlowID);

	public String getWFEditReason(@Param("subActivityFlowChartDefID") int subActivityFlowChartDefID);

	public void deactivateCustomVersionOfWOStepDetails(@Param("flowchartDefID") int flowchartDefID);

	public boolean validateDRAndPM(@Param("projectId")int projectId, @Param("signum") String signum);

	public boolean validateOM(@Param("marketArea")String marketArea, @Param("signum") String signum);

	public boolean validateNE(@Param("projectId")int projectId,@Param("signum") String signum);

	public void revertOldDefIdForNewDefIdWO(@Param("oldDefID")int oldDefID, @Param("newDefID")int newDefID,@Param("lastUpdatedVersion") int lastUpdatedVersion);

	public boolean isExist(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID);

	public int getVersionNumber(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID);

	public boolean validateEU(@Param("marketArea")String marketArea, @Param("signum") String signum);

	public boolean validateAdmin(@Param("marketArea")String marketArea, @Param("signum") String signum);

	public void insertWorkflowStepAutoSenseRule(@Param("flowChartdataModel") FlowChartPopulateDataModel flowChartdataModel);

	public void insertInFlowChartStepDetails1(@Param("flowChartdataModel") FlowChartPopulateDataModel flowChartdataModel, @Param("stepData") Map<String, Object> stepData);

	public void updateWorkOrderForAutoSense(@Param("parentWFDefID")int parentWFDefID);

	public void insertCustomWorkflowStepAutoSenseRule(@Param("parentWFDefID")int parentWFDefID,@Param("subActivityFlowchartDefId") int subActivityFlowchartDefId);

	public void addInstructionURL(@Param("wfStepInstructionModel") WFStepInstructionModel wfStepInstructionModel);

	
	public Integer getFcStepDeatilsID(@Param("stepID") String stepID, @Param("flowChartDefID") int flowChartDefID);

	public void deleteFlowChartDefIDFromInstructionTable(@Param("defID") int defID);
	
	public List<Map<String,Object>> getForwardReverseWFTransition();
	
	public List<String> getListOfViewMode();
	
	public List<KPIValueModel> getListOfKPIsForWF(@Param("proficiencyLevelSource") int proficiencyLevelSource);

	public int resetProficiency(@Param("workflowProficiencyModel")WorkflowProficiencyModel workflowProficiencyModel,@Param("userSignum") String userSignum ,@Param("loggedInSignum") String loggedInSignum);
	
	public void saveKPIValuesOfWF(@Param("kpiModelList") List<KpiModel> kpiModelList);
	
	public void updatePreviousKPIValuesOfWF(@Param("oldDefID") int oldDefID,@Param("signumID") String signumID);

	public void insertDummyDataOnStepDetails(@Param("customStepId") String customStepId,@Param("stepType") String stepType,@Param("loggedInSignum") String loggedInSignum,
			@Param("viewMode") String viewMode,@Param("stepName") String stepName,@Param("subActivityFlowchartStepDefId") int subActivityFlowchartStepDefId,
			@Param("disabledExcutionType") String disabledExcutionType,@Param("versionNumber")  int versionNumber);
	
	public List<KPIValueModel> getListOfKPIsForEWF(@Param("subactivityFlowChartDefID") Integer subactivityFlowChartDefID);
	
	public List<KPIValueModel>getListOfAllKPIsForEWF();

	public void deleteFlowChartDefIDFromSaveKPItable( @Param("newDefID") int newDefID);

	public void updateUnassignedAndAssignedWf(@Param("workFlowId") int workFlowId,@Param("flag") boolean flag ,@Param("versionNumber") int versionNumber,@Param("loggedInSignum") String loggedInSignum);

	public void updateOldDefIDFromSaveKPItable(@Param("oldDefID") int oldDefID);

	public List<Map<String, Object>> getAllSignumForWfid(@Param("workFlowProficiencyModel") WorkflowProficiencyModel workFlowProficiencyModel);


	public WorkflowProficiencyModel getLatestProficiencyofUser(@Param("workFlowProficiencyModel") WorkflowProficiencyModel workFlowProficiencyModel,
			 @Param("userSignum") String userSignum);

	public void deleteDefIDFromAutoSenseRule(@Param("newDefID") int newDefID);
	
	public String validateStepIDForDecision(@Param("stepDetailsModel") StepDetailsModel stepDetailsModel);

	public List<LoeMeasurementCriterionModel> getLoeMeasurementCriterion();
	
	public Integer getParentLoeMeasurementCriterionID(@Param("parentWFDefID") int parentWFDefID);
	
	public Boolean checkifPenguinOrMacro(@Param("rpaID") String rpaID);

	public Boolean isPreCondtionSatisfied(@Param("stepID") String stepID, @Param("oldDefID") int oldDefID, @Param("count") int count, @Param("date") int date);

	public List<Map<String, Object>> getOldStepBotSavingDetails(@Param("botSavingModel") BotSavingModel botSavingModel, @Param("term") String term,RowBounds rowBounds, @Param("pageLength") int pageLength);


	
}
