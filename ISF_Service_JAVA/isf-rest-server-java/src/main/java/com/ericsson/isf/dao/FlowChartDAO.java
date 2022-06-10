/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import com.ericsson.isf.mapper.AccessManagementMapper;
import com.ericsson.isf.mapper.FlowChartMapper;
import com.ericsson.isf.mapper.WOExecutionMapper;
import com.ericsson.isf.model.BotSavingModel;
import com.ericsson.isf.model.ErrorBean;
import com.ericsson.isf.model.FlowChartDefModel;
import com.ericsson.isf.model.FlowChartDependencyModel;
import com.ericsson.isf.model.FlowChartJsonModel;
import com.ericsson.isf.model.FlowChartPopulateDataModel;
import com.ericsson.isf.model.FlowChartSaveModel;
import com.ericsson.isf.model.FlowChartStepModel;
import com.ericsson.isf.model.KPIValueModel;
import com.ericsson.isf.model.KpiModel;
import com.ericsson.isf.model.LoeMeasurementCriterionModel;
import com.ericsson.isf.model.FlowChartStepDetailsModel;
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
import com.ericsson.isf.util.ConfigurationFilesConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ekarath
 */
@Repository
public class FlowChartDAO {

    @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;
  
    public List<ErrorBean> uploadExcel(String fileName, int projectID, int subActivityID, String signumID,String workFlowName) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         return flowChartMapper.uploadExcel(fileName ,projectID,subActivityID,signumID,workFlowName);
    }
    
    public List<Map<String, Object>> downloadNetworkElement(String domain, String technology, String vendor, String projectID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        List<Map<String, Object>> data = flowChartMapper.downloadNetworkElement(domain, technology, vendor, projectID);
        return data;
		
   }

    public List<FlowChartStepModel> getFlowChartStepDetails( int projectID,int subActivityID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getFlowChartStepDetails(projectID,subActivityID);
    }

    public void saveJSONForStepID(String json, int stepID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.saveJSONForStepID(json, stepID);
    }

    public List<FlowChartDependencyModel> getDependencyStep(int subActivityID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getDependencyStep(subActivityID);
    }

    public void saveJSONFromUI(FlowChartDefModel flowChartSaveModel,String type) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.saveJSONFromUI(flowChartSaveModel,type);
    }
    
    public void updateUiFlowChartStepDetails(Integer subActFCDefID,String stepId, String stepName, String taskId, String taskName, String executionType, String avgEstdEffort, String toolId,String reason){
       FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
       flowChartMapper.updateUiFlowChartStepDetails(subActFCDefID,stepId,stepName,taskId,taskName,executionType,avgEstdEffort,toolId,reason); 
    }

     public List<Map<String,Object>> viewFlowChartForSubActivity(int projectID,int subActivityID,int woID, int wfid) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.viewFlowChartForSubActivity(projectID,subActivityID,woID,wfid);
    }         

    public List<FlowChartStepModel> getStepJSON(int projectID,int subActivityID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getStepJSON(projectID,subActivityID);
    }

    public void saveJSONLinkForDependency(String jsonLink, int depID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.saveJSONLinkForDependency(jsonLink, depID);
    }

    public String getDetails() {
        sqlSession.getMapper(FlowChartMapper.class);
        String str = "Connected Successfully";
        return str;
    }

    public List<ScopeTaskMappingModel> getScopeTaskMapping(Integer projectID, Integer scopeID, Integer subactivityID, int taskID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getScopeTaskMapping(projectID, scopeID, subactivityID, taskID);
    }

    public void insertScopeTaskMapping(ScopeTaskMappingModel scopeTaskMapping) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.insertScopeTaskMapping(scopeTaskMapping);
    }

    public String insertNetworkelements(String filePath, int domain, int technology, int vendor, int projectID, String uploadedON, String uploadedBy) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.insertNetworkelements(filePath, domain, technology, vendor, projectID,uploadedON,uploadedBy);
    }

    public void updateProjectDefinedTask(FlowChartStepDetailsModel fcStepDetails) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.updateProjectDefinedTask(fcStepDetails);

    }
    
    public List<FlowChartStepDetailsModel> checkFLowEntry(int subActivityFlowID, int stepID, int taskID){
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.checkFLowEntry(subActivityFlowID,stepID,taskID);
    }

//    public List<FlowChartJsonModel> getDetailsForImportExistingWF(Integer projectID, Integer subActivityID,
//            Integer customerID, Integer countryID, Integer marketAreaID) {
//        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
//        return flowChartMapper.getDetailsForImportExistingWF(projectID, subActivityID, customerID, countryID, marketAreaID);
//    }
    
     public List<FlowChartJsonModel> getDetailsForImportExistingWF(Integer projectID,Integer subActivityID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getDetailsForImportExistingWF(projectID,subActivityID);
    }
	
	public List<WorkFlowAvailabilitySearchModel> searchWFAvailabilityforScope(String projectID, String domain,String subDomain,String serviceArea,String subServiceArea,String technology,String activity,String subActivity, String marketArea){
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.searchWFAvailabilityforScope(projectID, domain,subDomain,serviceArea,subServiceArea,technology,activity,subActivity,marketArea);
    }
	
	public void addWorkFlowToProject(FlowChartDefModel flowChartDefModel){
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.addWorkFlowToProject(flowChartDefModel);
    }
    
    public FlowChartDefModel checkFlowChartVersion(FlowChartDefModel flowChartDefModel){
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.checkFlowChartVersion(flowChartDefModel);
    }
    public void inActiveWorkflowForProject(int subActivityFlowChartDefID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.inActiveWorkflowForProject(subActivityFlowChartDefID);
    }

    public void saveTaskDetailsForStep(int subActivityFlowChartDefID, int subActivityFlowChartStepID, String stepName, int taskID, String task, String executionType, float avgEstdEffort, int toolID,int ordering,int versionNumber) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         flowChartMapper.saveTaskDetailsForStep(subActivityFlowChartDefID,subActivityFlowChartStepID,stepName,taskID,task,executionType,avgEstdEffort,toolID,ordering,versionNumber);
    }

    public List<String> getElementType(int projectID) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         return flowChartMapper.getElementType(projectID);
    }
            
            public List<String> getMarketDetails(int projectID) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         return flowChartMapper.getMarketDetails(projectID);
    }

         public List<FlowChartDefModel> getVersionName(int projectID,int subActivityID) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         return flowChartMapper.getVersionName(projectID,subActivityID);
    }

            public List<FlowChartDefModel> getVersionNameCurProjId(int flowChartDefID) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         return flowChartMapper.getVersionNameCurProjId(flowChartDefID);
    }
        public List<FlowChartDefModel> getWorkFlowVersionData(int projectID,int subActivityID, int wfid) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         return flowChartMapper.getWorkFlowVersionData(projectID,subActivityID,wfid);
    }
            
            
    public void insertFlowchartStepLinkDetails(Integer subActFCDefID, String sourceID, String targetID) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         flowChartMapper.insertFlowchartStepLinkDetails(subActFCDefID,sourceID,targetID);
    }

    public void deleteFlowchartStepDetails(Integer subActFCDefID,String stepID,int taskID,int versionNo) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         flowChartMapper.deleteFlowchartStepDetails(subActFCDefID,stepID,taskID,versionNo);
    }

    public ScopeTaskMappingModel getTaskDetailsForJSONStep(Integer projectID, Integer subactivityID, Integer taskID,Integer versionNo,String stepID,int flowChartDefID) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         return flowChartMapper.getTaskDetailsForJSONStep(projectID,subactivityID,taskID,versionNo,stepID,flowChartDefID);
    }

    public boolean chekIFDataCopiedToScopeTable(int projectID, int subActivityID) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         return flowChartMapper.chekIFDataCopiedToScopeTable(projectID,subActivityID);
    }

    public void deleteStepTaskDetails(int projectID, int subActivityID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.deleteStepTaskDetails(projectID,subActivityID);
    }

    public Map<String,Integer> getFlowDefIDForSubActivity(int projectID, int subActivityID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getFlowDefIDForSubActivity(projectID,subActivityID);
    }

   public List<ScopeTaskMappingModel> getProjectConfigTask(int projectID, int subActivityID,int versionNo) {
       FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getProjectConfigTask(projectID,subActivityID,versionNo);
    }

    public void updatePrjectConfigTaskDetails(int projectID, int subActivityID, int taskID, String executionType,int versionNumber) {
       FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
       flowChartMapper.updatePrjectConfigTaskDetails(projectID,subActivityID,taskID,executionType,versionNumber);
    }

    public int getWoWfVersionNo(int woID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getWoWfVersionNo(woID);
         
    }

    public List<Map<String,Object>> viewFlowChartForSubActivityWithVersion(int projectID, int subActivityID, int woID, int wfVersion, int wfid) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         return flowChartMapper.viewFlowChartForSubActivityWithVersion(projectID,subActivityID,woID,wfVersion,wfid);
    }

    public boolean checkFlowChartActiveStatus(int flowChartDefID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         return flowChartMapper.checkFlowChartActiveStatus(flowChartDefID);
    }

    public boolean getMaxVerionStatus(int projectID, int subActivityID) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getMaxVerionStatus(projectID,subActivityID);
    }
    
   public void insertInFlowChartStepDetails(Integer subActFCDefID,String stepId, String stepName, String taskId, String taskName, String executionType, 
                                              String toolId,String result,int versionNO,String masterTask,String stepType,String rpaID, String wiid, Map<String, Object> stepData, String outputUpload, String cascadeInput){
       FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
       flowChartMapper.insertInFlowChartStepDetails(subActFCDefID,stepId,stepName,taskId,taskName,executionType,
                                                  toolId,result,versionNO,masterTask,stepType,rpaID,wiid, stepData,outputUpload,cascadeInput); 
   }
    public void insertInFlowChartStepDetails1(FlowChartPopulateDataModel flowChartdataModel, Map<String, Object> stepData) {
    	FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
    	flowChartMapper.insertInFlowChartStepDetails1(flowChartdataModel,stepData);
		
	}
    public void insertJSONFromUI(FlowChartDefModel flowModel) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         flowChartMapper.insertJSONFromUI(flowModel);
    }

    public void updateActiveStatus(int projectID, int subActivityID, int subActivityDefID,String signumID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.updateActiveStatus(projectID,subActivityID,subActivityDefID,signumID);
    }

    public List<WorkFlowApprovalModel> getWorkFlowForApproval(int projectID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getWorkFlowForApproval(projectID);
    }

    public void rejectWorkFlow(int projectID, int subActivityID, int flowchartDefID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.rejectWorkFlow(projectID,subActivityID,flowchartDefID);
    }

    public Map<String, String> getEmployeeEmailDetails(String signumID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getEmployeeEmailDetails(signumID);
    }

    public FlowChartDefModel getFlowChartJSON(int projectID, int subActivityID, int wfVersion, String eSignumID, int wfid) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getFlowChartJSON(projectID,subActivityID,wfVersion,eSignumID,wfid);
    }

    public int getParentWorkFlowDefID(int flowchartDefID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getParentWorkFlowDefID(flowchartDefID);
    }
    
    public FlowChartDefModel getFlowchartDetails(int flowchartDefID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getFlowchartDetails(flowchartDefID);
    }

    public void updateParentWorkFlow(int projectID, int subActivityID, int parentWFDefID, String flowChartJSON) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.updateParentWorkFlow(projectID,subActivityID,parentWFDefID,flowChartJSON);
    }

    public void deactivateCustomVersion(int projectID, int subActivityID, int flowchartDefID,String mSignumID,String reason) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.deactivateCustomVersion(projectID,subActivityID,flowchartDefID,mSignumID,reason);
    }

    public int getUpdatedWorkFlowVersion(int projectID, int subActivityID, int parentWFDefID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getUpdatedWorkFlowVersion(projectID,subActivityID,parentWFDefID);
    }

    public void saveApprovalLogDetails(int flowchartDefID, String status, String managerSignumID, String employeeSignumID) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         flowChartMapper.saveApprovalLogDetails(flowchartDefID,status,managerSignumID,employeeSignumID);
    }

    public void inActiveCustomWFWO(int woID, int flowchartDefID, String eSignumID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         flowChartMapper.inActiveCustomWFWO(woID,flowchartDefID,eSignumID);
    }

    public void updateFlowChartStepDetails(int woID, int subActivityFlowChartDefID, String eSignumID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         flowChartMapper.updateFlowChartStepDetails(woID,subActivityFlowChartDefID,eSignumID);
    }

    public void deActivatePreviousVersion(int projectID, int subActivityID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.deActivatePreviousVersion(projectID,subActivityID);
    }

    public void updateWorkFlowStepDetails(int subActivityFlowChartDefID ,int flowchartDefID, int wfVersion, int updatedVersion) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.updateWorkFlowStepDetails(subActivityFlowChartDefID,flowchartDefID,wfVersion,updatedVersion);
    }


	public List<FlowChartDefModel> getFlowChartBySubActFCDefID(Integer subActFCDefID) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getFlowChartBySubActFCDefID(subActFCDefID);
	}

	public void updateVersionInFlowChart(int subActivityFlowChartStepID, int version) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.updateVersionInFlowChart(subActivityFlowChartStepID, version);
	}

	public void updatePrjectConfigTaskDetailsVersion(int projectID,int subActivityID, int parseInt, String executionType,int version) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.updatePrjectConfigTaskDetailsVersion( projectID, subActivityID,  parseInt,  executionType, version);
		
	}
	
	public boolean getWorkflow(int projectID, String workFlowName, int subActivityID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getWorkflow( projectID,  workFlowName, subActivityID);
	}

	public int getActiveFlowDefIDForSubActivity(int projectID, int subActivityID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getActiveFlowDefIDForSubActivity( projectID, subActivityID);
	}

    public int getWorkFlowVersion(int parentWFDefID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getWorkFlowVersion(parentWFDefID);
    }

    public void updateWorkFlowStepLinkDetails(int subActivityFlowChartDefID, int flowchartDefID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.updateWorkFlowStepLinkDetails(subActivityFlowChartDefID,flowchartDefID);
    }

    public void updateFlowChartStepDetailsTasks(FlowChartStepDetailsModel fcStepDetails) {
       FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.updateFlowChartStepDetailsTasks(fcStepDetails);
    }

    public int getStepAvgEffortDetails(FlowChartStepDetailsModel fcStepDetails) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getStepAvgEffortDetails(fcStepDetails);
    }

    public String getMasterTask(int taskID, int subActivityID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getMasterTask(taskID,subActivityID);
    }
   
 public int getSubActivityFlowchartDefIdByVersionNo(String workFlowName,int versionNO,int subActId,int projId) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getSubActivityFlowchartDefIdByVersionNo(workFlowName,versionNO,subActId,projId);
    }

    public int getWFVersioNo(int wFID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getWFVersioNo(wFID);
    }

    public boolean checkIFStepDataExists(Integer subActFCDefID,String stepId, String stepName, int taskId,int versionNO) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.checkIFStepDataExists(subActFCDefID,stepId,stepName,taskId,versionNO);
    }

    public void updateFlowChartStepDetailsValue(Integer subActFCDefID,String stepId, String stepName, String taskId, String taskName,
                                                String executionType,String toolId,String result,int versionNO,
                                                String masterTask,String rpaID, String wiid, String outputUpload,String cascadeInput) {
          FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         flowChartMapper.updateFlowChartStepDetailsValue(subActFCDefID,stepId,stepName,taskId,taskName,executionType,
                                                         toolId,result,versionNO,masterTask,rpaID,wiid,outputUpload, cascadeInput); 
    }
    public List<Map<String,Object>> getWfListForPlan(Integer projectId, Integer sourceId) {
       FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
       return flowChartMapper.getWfListForPlan(projectId, sourceId); 
    }
    
    public List<Map<String,Object>> getWfListForPlanForExternalSource(Integer projectId, Integer sourceId) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getWfListForPlanForExternalSource(projectId, sourceId); 
     }

    public Map<String,String> checkIFExecutionExists(int subActFCDefID, int taskID, String taskName, int versionNumber) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.checkIFExecutionExists(subActFCDefID,taskID,taskName,versionNumber);
    }

    public String insertNetworkelementsUpdate(String filePath, int domain, int technology, int vendor, int projectID, String uploadedON, String uploadedBy) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.insertNetworkelementsUpdate(filePath, domain, technology, vendor, projectID,uploadedON,uploadedBy);
    }
    
   
    
    public void saveWorkflowDefinition(WorkFlowDefinitionModel workFlowDefinitionModel, String signum)
    {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.saveWorkflowDefinition(workFlowDefinitionModel,signum);
    }

    public void saveworkFlowSteps(WorkFlowStepsModel workFlowSteps,int workFlowID,String signum)
    {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.saveworkFlowSteps(workFlowSteps,workFlowID,signum);
    }
     public void saveWorkFlowLinks(WorkFlowLinksModel workFlowLink,int workFlowID,String signum)
    {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.saveWorkFlowLinks(workFlowLink,workFlowID,signum);
    }
      public void saveWorkFlowStepAttr(WorkFlowStepAttrModel workFlowStepAttr,int flowChartStepId,String signum)
    {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.saveWorkFlowStepAttr(workFlowStepAttr,flowChartStepId,signum);
    }
      public WorkFlowDefinitionModel getJsonDataForWorkFlow( int projectID,int subActID, int versionNO)
      {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getJsonDataForWorkFlow(projectID, subActID, versionNO);
      }
      
    public void saveWorkFlowVertices(WorkFlowLinksVerticesModel vertice,int workFlow_LinkID,String signum)
    {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.saveWorkFlowVertices(vertice, workFlow_LinkID, signum);
    }

    public String getWorfFlowName(int flowchartDefID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getWorfFlowName(flowchartDefID);
    }

    public int getLatestDefID(int projectID, int subActivityID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getLatestDefID(projectID,subActivityID);
    }

    public void deleteEmptyData(int subaActivityDefID) {
      FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
      flowChartMapper.deleteEmptyData(subaActivityDefID);
    }

    public List<Map<String, Object>> getDefIDAndVersionName(int projectID, int subActivityID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getDefIDAndVersionName(projectID,subActivityID);
    }
    
    public List<Map<String, Object>> getStepDetails(int subActivityDefID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getStepDetails(subActivityDefID);
    }

    public boolean checkIFLinkDataExists(int subActFCDefID, String sourceID, String targetID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.checkIFLinkDataExists(subActFCDefID,sourceID,targetID);
    }

    public void deleteStepData(int subaActivityDefID) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         flowChartMapper.deleteStepData(subaActivityDefID);
         
    }

    public List<Map<String,String>> getWFOwners(int projectID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getWFOwners(projectID);
    }

    public void updateWFOwner(int flowChartDefID, String signumID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.updateWFOwner(flowChartDefID,signumID);
    }

    public List<Map<String, Object>> getFlowChartData(int flowChartDefID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getFlowChartData(flowChartDefID);
    }

    public String getWFName(int projectID, int subActivityID, int wfVersion, int wfid) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getWFName(projectID,subActivityID,wfVersion,wfid);
    }

    public int getExperiencedVersionNo(int projectID, int subActivityID, String wfName) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getExperiencedVersionNo(projectID,subActivityID,wfName);
    }
    
     public List<Map<String, Object>> getFlowChartForExperiencedMode(int projectID, int subActivityID, String wfName,int wfVersion, int wfid) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getFlowChartForExperiencedMode(projectID,subActivityID,wfName,wfVersion,wfid);
    }

    public void deleteFlowStepData(int subActivityDefID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         flowChartMapper.deleteFlowStepData(subActivityDefID);
    }

    public void deleteFlowStepLinkData(int subActivityDefID) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
          flowChartMapper.deleteFlowStepLinkData(subActivityDefID);
    }
    
     public void deleteWFDefData(int subActivityDefID) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
          flowChartMapper.deleteWFDefData(subActivityDefID);
    }

    public Map<String, Object> getStepExistingData(int subActFCDefID, String stepID, int versionNumber) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         return flowChartMapper.getStepExistingData(subActFCDefID,stepID,versionNumber);
    }

    public int getWorkFlowLatestVersion(int projectID, int subActivityID) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         return flowChartMapper.getWorkFlowLatestVersion(projectID,subActivityID);
    }

    public String getExistingWFName(int subActivityDefID) {
       FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
         return flowChartMapper.getExistingWFName(subActivityDefID);
    }

    public void deactivateParentVersion(int projectID, int subActivityID, int parentWFDefID, String mSignumID,String reason) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.deactivateParentVersion(projectID,subActivityID,parentWFDefID,mSignumID,reason);
    }

    public void insertDataToFlowChartStepDetails(int childFlowChartDefID, int parentFlowchartDefID, int childWFVersion , int parentWFVersion, String mSignumID ) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.insertDataToFlowChartStepDetails(childFlowChartDefID,parentFlowchartDefID,childWFVersion,parentWFVersion, mSignumID);
    }

    public void insertDataToFlowChartLinkDetails(int subActivityFlowChartDefID, int flowchartDefID) {
         FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.insertDataToFlowChartLinkDetails(subActivityFlowChartDefID,flowchartDefID);
    }

    public void checkWFNameExistsAndUpdate(int projectID, int subActivityID, String wfName,String signumID) {
          FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
          flowChartMapper.checkWFNameExistsAndUpdate(projectID,subActivityID,wfName,signumID);
    }

	public Map<String,Object> getAdditionalInfoOfWorkFlow(int subActivityID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getAdditionalInfoOfWorkFlow(subActivityID);
	}

	public List<Map<String, Object>> getAllFlowCharts(String MA) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getAllFlowCharts(MA);
	}

	public List<Map<String, Object>> getAllFlowChartsBySubActivityFlowChartDefID(
			int subActivityFlowChartDefID) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getAllFlowChartsBySubActivityFlowChartDefID(subActivityFlowChartDefID);
	}

	public Map<String, Object> getTaskNameByID(String taskID) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getTaskNameByID(taskID);
	}

	public Map<String, Object> getToolNameByID(String toolID) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getToolNameByID(toolID);
	}

	public List<Map<String, Object>> getDeletedSteps(int oldDefID, int newDefID) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getDeletedSteps(oldDefID, newDefID);
	}

	public List<Map<String, Object>> getAddedSteps(int oldDefID, int newDefID) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getAddedSteps(oldDefID, newDefID);
	}

	public Map<String, Object> getSavingForManualStep(String stepID, int oldDefID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getSavingForManualStep(stepID,oldDefID);
		
	}

	public Map<String, Object> getSavingForAutomaticStep(int oldDefID, String stepID) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getSavingForAutomaticStep(oldDefID, stepID);
	}

	public void deleteDefID(int defID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.deleteDefID(defID);
		
	}

	public void saveBotSavingDetailsAndHistoryNew(String newStepID, String newStepName,
			String type, BotSavingModel botSavingModel, String executionType, String savings) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.saveBotSavingDetailsAndHistoryNew(newStepID,newStepName,type,botSavingModel,executionType, savings);
		
	}
	
	public void saveBotSavingDetailsAndHistoryOld(String newStepID, String newStepName,
			String type, BotSavingModel botSavingModel, String executionType, String savings, String emeCalculationDefID) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.saveBotSavingDetailsAndHistoryOld(newStepID,newStepName,type,botSavingModel,executionType, savings, emeCalculationDefID);
		
	}

	public void saveBotSaving(BotSavingModel botSavingModel, String totalSavings) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.saveBotSaving(botSavingModel,totalSavings);
		
	}

	public void updateOldDefID(int defID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.updateOldDefID(defID);
	}

	public List<Map<String, Object>> checkWFNameExistsWithExpert(int projectID, int subActivityID,
			String wfName, String signumID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.checkWFNameExistsWithExpert(projectID,subActivityID,wfName,signumID);
		
	}

	public void deleteSubAcitvityDefID(int defID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.deleteSubAcitvityDefID(defID);
		
	}

	public List<Map<String, Object>> getWFVersionsFromSubactivityID(BotSavingModel botSavingModel,  int pageLength ) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		int pageSize = pageLength;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		return flowChartMapper.getWFVersionsFromSubactivityID(botSavingModel , rowBounds, pageSize);
	}

	public List<Map<String, Object>> getFlowChartByDefID(int subActivityID, int wfVersion, int projectID, int wfid) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getFlowChartByDefID(subActivityID,wfVersion, projectID,wfid);
	}

	public Map<String, Object> checkIfStepExperienced(String stepID, int oldDefID) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.checkIfStepExperienced(stepID,oldDefID);
	}

	public Map<String, Object> getSavingForAutomaticStepForOthers(
			BotSavingModel botSavingModel) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getSavingForAutomaticStepForOthers(botSavingModel);
	}

	public Map<String, Object> checkisCalculationNeeded(String stepID, int oldDefID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.checkisCalculationNeeded(stepID,oldDefID);
	}

	public List<Map<String, Object>> getDeployedBotList(String signumID) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getDeployedBotList(signumID);
	}

	public void updateStepDetailsSavings(BotSavingModel botSavingModel) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.updateStepDetailsSavings(botSavingModel);		
	}

	public void updateStepDetailsSavingsRemarks(BotSavingModel botSavingModel) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.updateStepDetailsSavingsRemarks(botSavingModel);		
		
	}

	public void deactiveBotStatusForOldReocrds(BotSavingModel botSavingModel) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.deactiveBotStatusForOldReocrds(botSavingModel);				
	}

	public String getPostSumSaving(int rpaID, int defID) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getPostSumSaving(rpaID,defID);		
        
	}

	public String getPreSumSaving(int rpaID, int defID) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getPreSumSaving(rpaID,defID);
        
	}

	public Map<String, Object> getSavingForAutomaticStepForNewDef(
			BotSavingModel botSavingModel) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getSavingForAutomaticStepForNewDef(botSavingModel);
	}

	public List<Map<String,Object>> getWFBySubActivityId(Integer subActivityId,Integer projectId){
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getWFBySubActivityId(subActivityId,projectId);
	}
	public boolean updatePostPeriodDates(BotSavingModel botSavingModel) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.updatePostPeriodDates(botSavingModel);
	}

	public Integer getPreAutomaticSumSaving(int rpaID, int defID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getPreAutomaticSumSaving(rpaID,defID);	
    }

	public String getAvgBookingHours(int rpaID, int defID) {
		// TODO Auto-generated method stub
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getAvgBookingHours(rpaID,defID);
	}


	public List<HashMap<String, Object>> getWFVersionDetails(int projectId, int subActivityId, int SubActflowChartDefId, int versionOld) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getWFVersionDetails(projectId, subActivityId, SubActflowChartDefId, versionOld);
	}
    
	public void updateWFVersionDetails(int executionPlanDetail, int versionNew, String taskJSON) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		flowChartMapper.updateWFVersionDetails(executionPlanDetail, taskJSON, versionNew);
	}

	public Integer getMaxWFID() {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getMaxWFID();
		
	}

	public List<Map<String, Object>> getFlowChartEditReason() {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getFlowChartEditReason();
	}

	public int getWFID(int subActivityFlowChartDefID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getWFID(subActivityFlowChartDefID);
	}

	public boolean getWorkflowForWFID(int projectID, String wfName, int subActivityID, int workFlowID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.getWorkflowForWFID(projectID,wfName,subActivityID,workFlowID);
		
	}

	public String getWFEditReason(int subActivityFlowChartDefID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getWFEditReason(subActivityFlowChartDefID);
	}

	public void deactivateCustomVersionOfWOStepDetails(int flowchartDefID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		flowChartMapper.deactivateCustomVersionOfWOStepDetails(flowchartDefID);
	}

	public boolean validateDRAndPM(int projectId, String signum) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.validateDRAndPM(projectId,signum);
	}

	public boolean validateOM(String marketArea, String signum) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.validateOM(marketArea,signum);
	}

	public boolean validateNE(int projectId, String signum) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.validateNE(projectId,signum);
	}

	public void revertOldDefIdForNewDefIdWO(int oldDefID, int newDefID, int lastUpdatedVersion) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		flowChartMapper.revertOldDefIdForNewDefIdWO(oldDefID,newDefID, lastUpdatedVersion);
		
	}
	public boolean isExist(int projectID, int subActivityID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.isExist(projectID,subActivityID);
	}

	public int getVersionNumber(int projectID, int subActivityID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getVersionNumber(projectID,subActivityID);
	}

	public boolean validateEU(String marketArea, String signum) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.validateEU(marketArea,signum);
	}

	public boolean validateAdmin(String marketArea, String signum) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.validateAdmin(marketArea,signum);
	}


	public void insertWorkflowStepAutoSenseRule(FlowChartPopulateDataModel flowChartdataModel) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.insertWorkflowStepAutoSenseRule(flowChartdataModel);
		
	}

	public void updateWorkOrderForAutoSense(int parentWFDefID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.updateWorkOrderForAutoSense(parentWFDefID);
		
	}

	public void insertCustomWorkflowStepAutoSenseRule(int parentWFDefID, int subActivityFlowchartDefIdAndInstanceId) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.insertCustomWorkflowStepAutoSenseRule(parentWFDefID,subActivityFlowchartDefIdAndInstanceId);
	}

	public void addInstructionURL(WFStepInstructionModel wfStepInstructionModel) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		flowChartMapper.addInstructionURL(wfStepInstructionModel);
		
	}

	
	public Integer getFcStepDeatilsID(String stepID, int flowChartDefID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getFcStepDeatilsID(stepID,flowChartDefID);
	}

	public void deleteFlowChartDefIDFromInstructionTable(int defID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		flowChartMapper.deleteFlowChartDefIDFromInstructionTable(defID);
		
		
	}
	
	public List<Map<String, Object>> getForwardReverseWFTransition() {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getForwardReverseWFTransition();
	} 
	
	public List<String> getListOfViewMode() {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getListOfViewMode();
	}
	
	public List<KPIValueModel> getListOfKPIsForWF(int proficiencyLevelSource) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getListOfKPIsForWF(proficiencyLevelSource);
	}

	public int resetProficiency(WorkflowProficiencyModel workFlowProficiencyModel, String userSignum, String loggedInSignum) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.resetProficiency(workFlowProficiencyModel,userSignum, loggedInSignum);
		
	} 
	
	public void saveKPIValuesOfWF(List<KpiModel> kpiModelList) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
	    flowChartMapper.saveKPIValuesOfWF(kpiModelList);
	}
	
	public void updatePreviousKPIValuesOfWF(int oldDefID, String signumID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		flowChartMapper.updatePreviousKPIValuesOfWF(oldDefID, signumID);
	}

	public void insertDummyDataOnStepDetails(String customStepId, String stepType,
			String loggedInSignum, String viewMode, String stepName, int subActivityFlowchartStepDefId, String disabledExcutionType, int versionNumber) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		flowChartMapper.insertDummyDataOnStepDetails(customStepId,stepType,loggedInSignum,viewMode,stepName,subActivityFlowchartStepDefId,disabledExcutionType, versionNumber);
		
	}
	
	public List<KPIValueModel> getListOfKPIsForEWF(Integer subactivityFlowChartDefID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getListOfKPIsForEWF(subactivityFlowChartDefID);
	}
	
	public List<KPIValueModel> getListOfAllKPIsForEWF() {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getListOfAllKPIsForEWF();
	}

	public void deleteFlowChartDefIDFromSaveKPItable( int newDefID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		flowChartMapper.deleteFlowChartDefIDFromSaveKPItable(newDefID);
		
		
	}

	public void updateUnassignedAndAssignedWf(int workFlowId, boolean flag, int versionNumber, String loggedInSignum) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		flowChartMapper.updateUnassignedAndAssignedWf(workFlowId,flag,versionNumber, loggedInSignum);
	}

	public void updateOldDefIDFromSaveKPItable(int oldDefID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		flowChartMapper.updateOldDefIDFromSaveKPItable(oldDefID);
		
	}

	public List<Map<String, Object>> getAllSignumForWfid(WorkflowProficiencyModel workFlowProficiencyModel) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getAllSignumForWfid(workFlowProficiencyModel);
		
	}

	public WorkflowProficiencyModel getLatestProficiencyofUser(WorkflowProficiencyModel workFlowProficiencyModel,
			String userSignum) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getLatestProficiencyofUser(workFlowProficiencyModel, userSignum );
	}

	public void deleteDefIDFromAutoSenseRule(int newDefID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		flowChartMapper.deleteDefIDFromAutoSenseRule(newDefID);
		
	}
	
	public String validateStepIDForDecision(StepDetailsModel stepDetailsModel) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.validateStepIDForDecision(stepDetailsModel);		
	}
	
	public Boolean checkifPenguinOrMacro(String rpaID) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.checkifPenguinOrMacro(rpaID);
	}


	public List<LoeMeasurementCriterionModel> getLoeMeasurementCriterion() {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		return flowChartMapper.getLoeMeasurementCriterion();		
	}
	
	 public Integer getParentLoeMeasurementCriterionID(int parentWFDefID) {
	        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
	        return flowChartMapper.getParentLoeMeasurementCriterionID(parentWFDefID);
	 }

	public Boolean isPreCondtionSatisfied(String stepID, int oldDefID, int count, int date) {
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        return flowChartMapper.isPreCondtionSatisfied(stepID, oldDefID, count, date);
	}

	public List<Map<String, Object>> getOldStepBotSavingDetails(BotSavingModel botSavingModel, String term, int pageLength) {
		FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
		int pageSize = pageLength;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		return flowChartMapper.getOldStepBotSavingDetails(botSavingModel,term, rowBounds, pageSize); 
	}


 
}
