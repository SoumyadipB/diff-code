/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.ActivityMasterMapper;
import com.ericsson.isf.mapper.FlowChartMapper;
import com.ericsson.isf.mapper.WOExecutionMapper;
import com.ericsson.isf.model.AdhocWorkOrderCreationModel;
import com.ericsson.isf.model.AdhocWorkOrderModel;
import com.ericsson.isf.model.BookingDetailsModel;
import com.ericsson.isf.model.BulkWOCreationDetailsModel;
import com.ericsson.isf.model.ChildStepDetailsModel;
import com.ericsson.isf.model.CurrentWorkOrderModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DeliverableSuccessReasonModel;
import com.ericsson.isf.model.EventPublisherURLMappingModel;
import com.ericsson.isf.model.ExternalExecutionDetailsModel;
import com.ericsson.isf.model.FcStepDetails;
import com.ericsson.isf.model.FlowChartReverseTraversalModel;
import com.ericsson.isf.model.InProgressWorkOrderModel;
import com.ericsson.isf.model.LastStepDetailsModel;
import com.ericsson.isf.model.NextStepModel;
import com.ericsson.isf.model.ProficiencyTypeModal;
import com.ericsson.isf.model.ProjectQueueWorkOrderBasicDetailsModel;
import com.ericsson.isf.model.RpaModel;
import com.ericsson.isf.model.SaveClosureDetailsForWOModel;
import com.ericsson.isf.model.SaveWfUserProfResponseModel;
import com.ericsson.isf.model.SearchPlannedWOProjectModel;
import com.ericsson.isf.model.SharePointDetailModel;
import com.ericsson.isf.model.SourceSystemDetailsModel;
import com.ericsson.isf.model.StepDetailsModel;
import com.ericsson.isf.model.StepTaskModel;
import com.ericsson.isf.model.TaskModel;
import com.ericsson.isf.model.UserLoginModel;
import com.ericsson.isf.model.UserWorkFlowProficencyModel;
import com.ericsson.isf.model.WOWorkFlowModel;
import com.ericsson.isf.model.WoCompleteDetailsModel;
import com.ericsson.isf.model.WorkFlowBookingDetailsModel;
import com.ericsson.isf.model.WorkFlowDetailsModel;
import com.ericsson.isf.model.WorkFlowStepBookingDetailsModel;
import com.ericsson.isf.model.WorkFlowStepDataModel;
import com.ericsson.isf.model.WorkFlowStepDetailsModel;
import com.ericsson.isf.model.WorkFlowStepLinksDetailModel;
import com.ericsson.isf.model.WorkFlowUserProficiencyModel;
import com.ericsson.isf.model.WorkOrderBasicDetailsModel;
import com.ericsson.isf.model.WorkOrderColumnMappingModel;
import com.ericsson.isf.model.WorkOrderCompleteDetailsModel;
import com.ericsson.isf.model.WorkOrderDetailsModel;
import com.ericsson.isf.model.WorkOrderDetailsSearchModel;
import com.ericsson.isf.model.WorkOrderFailureReasonModel;
import com.ericsson.isf.model.WorkOrderModel;
import com.ericsson.isf.model.WorkOrderNodes;
import com.ericsson.isf.model.WorkOrderNodesModel;
import com.ericsson.isf.model.WorkOrderProgressModel;
import com.ericsson.isf.model.WorkOrderViewDetailsByWOIDModel;
import com.ericsson.isf.model.WorkflowStepDetailModel;
import com.ericsson.isf.model.botstore.ServerBotModel;
import com.ericsson.isf.util.IsfCustomIdInsert;

/**
 *
 * @author esanpup
 */
@Repository
public class WOExecutionDAO {

    @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;
    
    @Autowired
	private IsfCustomIdInsert isfCustomIdInsert;

	 public List<WorkOrderDetailsSearchModel> searchPlannedWorkOrders(String signum, String woStatus,String startDate,String endDate,DataTableRequest dataTableRequest) {
	        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	        return wOExecutionMapper.searchPlannedWorkOrders(signum, woStatus,startDate,endDate,dataTableRequest);
	    }
    
    public List<AdhocWorkOrderModel> getAdhocWorkOrderDetails(String signumID) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return woExecutionMapper.getAdhocWorkOrderDetails(signumID);
    }


    public List<AdhocWorkOrderModel> getAdhocWorkOrderDetailsByID(int adhocWOID,String signumID) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return woExecutionMapper.getAdhocWorkOrderDetailsByID(adhocWOID,signumID);
    }

    public void saveFeedback(String signumID, String feedback) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        woExecutionMapper.saveFeedback(signumID,feedback);
    }
    
    public Boolean checkIFWOIDExists(int woID) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return woExecutionMapper.checkIFWOIDExists(woID);
    }

    public void updateWorkOrderStatus(int woID, String signumID, String status, String comment) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        woExecutionMapper.updateWorkOrderStatus(woID,signumID,status,comment);
    }
    
    public void startWorkorder(int woID, String signum) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        woExecutionMapper.startWorkorder(woID, signum);
    }


    // Removing WO Duplicate functionality from 9.0
    /*
    public void createDuplicateWorkOrder(String wOID, String signum, String projectID, String comment) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.createDuplicateWorkOrder(wOID, signum, projectID, comment);
    }
    */

    public List<TaskModel> getTaskRelatedDetails(String taskID, String subActivityID, String projectID) {
        ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
        return activityMasterMapper.getTaskRelatedDetails(taskID, subActivityID, projectID);
    }

    public void updateStatusWO(int wOID){
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.updateStatusWO(wOID);
    }
    
    public void startWorkOrderTask(int wOID, int taskID, String signum) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.startWorkOrderTask(wOID, taskID, signum);
    }
    
    public void stopWorkOrderTask(String wOID,String taskID,String signum, int bookingID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.stopWorkOrderTask(wOID,taskID,signum,bookingID);
    }
	
    public List<InProgressWorkOrderModel> getInProgressWorkOrderDetails(String signumID){
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getInProgressWorkOrderDetails(signumID);
    }
    
    public List<WorkflowStepDetailModel> getWorkflowStepDetails(int subActivityFlowChartDefID){
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getWorkflowStepDetails(subActivityFlowChartDefID);
    }
    public List<WorkflowStepDetailModel> getWorkflowStepDetailsForComments(int subActivityFlowChartDefID){
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getWorkflowStepDetailsForComments(subActivityFlowChartDefID);
    }  
    
    public WorkflowStepDetailModel getStepDetailsByFcDefId(String stepid,String fcDefId){
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getStepDetailsByFcDefId(stepid,fcDefId);
    }
    
    public WorkflowStepDetailModel getStepDetailsByStepIdAndFcDefId(String stepid,String fcDefId){
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getStepDetailsByStepIdAndFcDefId(stepid,fcDefId);
    }
    
    public List<WorkOrderCompleteDetailsModel> getCompleteWorkOrderDetails(int wOID, String columnNames){
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getCompleteWorkOrderDetails(wOID, columnNames);
    }
    
    public Boolean checkWorkOrder(int wOID){
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.checkWorkOrder(wOID);
    }
    
    public void updateWorkOrderUserLevel(WorkOrderModel workOrderModel){
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.updateWorkOrderUserLevel(workOrderModel);
    }
    
     public void updateWFDefinition(Map<String,String> params){
        FlowChartMapper flowChartMapper = sqlSession.getMapper(FlowChartMapper.class);
        flowChartMapper.updateWFDefinition(params);
    }
    
    public int getEstdHrs(int subActivityID)
    {
    	
     
     int estdHrs;
     WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
     estdHrs=wOExecutionMapper.getEstdHrs(subActivityID);
     
     return estdHrs;
    }
    
    public void createAdhocWorkOrder(AdhocWorkOrderCreationModel adhocWOObject, Date endDate) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.createAdhocWorkOrder(adhocWOObject, endDate);
    }
    
    public void saveClosureDetails_WO(SaveClosureDetailsForWOModel saveClosureDetailsObject) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.saveClosureDetails_WO(saveClosureDetailsObject);
    }
    
    public void saveClosureDetails_InsertDA(SaveClosureDetailsForWOModel saveClosureDetailsObject) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.saveClosureDetails_InsertDA(saveClosureDetailsObject);
    }
    
    public WOWorkFlowModel getWOWorkFlow(int wOID){
        WOWorkFlowModel woModel = new WOWorkFlowModel();
        
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        int wfVersionNo = wOExecutionMapper.getWoWfVersionNo(wOID);
        if(wfVersionNo == 0){
            woModel = wOExecutionMapper.getWOWorkFlow(wOID);
        }else{
            woModel = wOExecutionMapper.getWOWorkFlowWithWFNo(wOID,wfVersionNo);
        }
        if(woModel.getFlowChartJSON() == null || woModel.getFlowChartJSON().isEmpty()){
            woModel.setUploadedJSON(false);
        }
        return woModel;
    }    
    
    public List<WorkFlowBookingDetailsModel> getBookingHours(int wOID){
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getBookingHours(wOID);
    }

    public List<CurrentWorkOrderModel> getCurrentWorkOrderDetails(String signumID, String isApproved) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getCurrentWorkOrderDetails(signumID, isApproved);
    }
    
    public List<Map<String,Object>> checkParallelWorkOrderDetails(String signumID, String isApproved) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.checkParallelWorkOrderDetails(signumID, isApproved);
    }

    public void deleteExistingNodes(int woid) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.deleteExistingNodes(woid);
    }
    
      public void deleteExistingNodesFromPlanNodes(int woPlanId) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.deleteExistingNodesFromPlanNodes(woPlanId);
    }
    
    public int getWoPlanID(int woid) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getWoPlanID(woid);
    }

    public void insertWorkOrderNode(int woid, String nodeType, String nodeNames, String createdBy) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.insertWorkOrderNode(woid,nodeType,nodeNames,createdBy);
    }
     public void insertWorkOrderNodeInPlanNodes(int woPlanId, String nodeType, String nodeNames, String createdBy) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.insertWorkOrderNodeInPlanNodes(woPlanId,nodeType,nodeNames,createdBy);
    }
    
    public List<WorkOrderViewDetailsByWOIDModel> getWorkOrderViewDetailsByWOID(int WOID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getWorkOrderViewDetailsByWOID(WOID);
    }
    
    public String getProjectIdByWoid(int WOID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getProjectIdByWoid(WOID);
    }

    public void updateStatusAndHours(int wOID, int taskID, int bookingID, String status, String reason, String signumID, String stepid, Integer sourceID, Integer defID, String executionType) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.updateStatusAndHours(wOID, taskID,bookingID,status,reason,signumID, stepid,sourceID,defID , executionType);
    }
    
    public void startWO(int wOID, String signumID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.startWO(wOID, signumID);
    }
    
    public void startWO_bookingDetails(int wOID, int taskID, String signumID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.startWO_bookingDetails(wOID, taskID, signumID);
    }
    
    public String getMaxBookingID(int wOID,int taskID,String signumID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getMaxBookingID(wOID,taskID,signumID);
    } 
    public Integer getMaxBookingIdByWoid(int wOID,String signumID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getMaxBookingIdByWoid(wOID,signumID);
    }
    
    public void closeWO_bookingID(int wOID,int taskID,int bookingID, String status,String signumID, String url) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.closeWO_bookingID(wOID,taskID,bookingID,status, signumID, url);
    }
            
    public void updateRpaFailureStatus(int wOID,int taskID,int bookingID, String status, ServerBotModel serverBotModel) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.updateRpaFailureStatus(wOID,taskID,bookingID,status, serverBotModel);
    }        
            
    
//    public void closeWO_wOID(int wOID, String signumID, String url) {
//        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
//        wOExecutionMapper.closeWO_wOID(wOID, signumID, url);
//    }

    public boolean checkIFPreviousStepCompleted(int WOID, int taskID,int subActivityDefID,String flowChartStepID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.checkIFPreviousStepCompleted(WOID,taskID,subActivityDefID,flowChartStepID);
    }
    
    public Boolean checkIFPreviousStepCompletedV2(int WOID, String flowChartStepID,int subActivityDefID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.checkIFPreviousStepCompletedV2(WOID,flowChartStepID,subActivityDefID);
    }
    public LastStepDetailsModel checkIFLastStep(int WOID, int subactivityDefID) {
         WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
         return wOExecutionMapper.checkIFLastStep(WOID,subactivityDefID);
    }
	public List<WorkOrderFailureReasonModel> getWOFailureReasons(String category){
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getWOFailureReasons(category);
    }

    public Boolean checkForType(int wOID, int taskID, int bookingID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.checkForType(wOID,taskID,bookingID);
    }

    public int getAdhocWorkOrderForRPA(String rpaID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getAdhocWorkOrderForRPA(rpaID);
    }

    public String getProjectCreatorSignum(int woid) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getProjectCreatorSignum(woid);
    }
    
    public void addStepDetailsForFlowChart(StepDetailsModel stepDetailsModel) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.addStepDetailsForFlowChart(stepDetailsModel);
    }
    //v1/addStepDetailsForFlowChart
    public void addStepDetailsForFlowChart(int wOID, int taskID, int bookingID, String stepID, int flowChartDefID, String status, String signumID,String decisionValue,String exeType) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.addStepDetailsForFlowChartV1(wOID,taskID,bookingID,stepID,flowChartDefID,status,signumID,decisionValue,exeType);
    }

    public List<WorkFlowStepBookingDetailsModel> getWorkFlowStepBookingDetails(int wOID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getWorkFlowStepBookingDetails(wOID);
    }

    public WorkFlowBookingDetailsModel getBookingID(int wOID, int taskID, String signum) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getBookingDetails(wOID, taskID, signum);
    }

    public Map<String, String> getWfVersionDetails(String wOID) {
      WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
      return wOExecutionMapper.getWfVersionDetails(wOID);
    }

    public int getWorkOrderDetails(int projectID, int subActivityID,int flowchartDefID ,int wfVersion, String eSignumID) {
      WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
      return wOExecutionMapper.getWorkOrderDetails(projectID,subActivityID,flowchartDefID,wfVersion,eSignumID);
    }

    public void updateWorkFlowVersion(int woID, int updatedVersion, String eSignumID,int subActivityFlowChartDefID) {
      WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
      wOExecutionMapper.updateWorkFlowVersion(woID,updatedVersion,eSignumID,subActivityFlowChartDefID);
    }

    public Map<String,String> getWorkOrderData(String wOID) {
      WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
      return wOExecutionMapper.getWorkOrderData(wOID);
    }

    public void updateRPABotStatus(int wOID, int taskID, int bookingID, String signumID, String status) {
       WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
       wOExecutionMapper.updateRPABotStatus(wOID,taskID,bookingID,signumID,status);
    }
    public void updateWOBooking(int wOID, int taskID, String bookingStatus, String signumID) {
         WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
         wOExecutionMapper.updateWOBooking(wOID,taskID,bookingStatus,signumID);
    }

    public String getEmployeeEmail(String signumID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getEmployeeEmail(signumID);
    }

    public String getFlowChartStepID(int woID, int taskID, int flowchartDefID, int bookingID, String signumID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getFlowChartStepID(woID,taskID,flowchartDefID,bookingID,signumID);
    }

    public String getBookingHoursForStep(String stepID, int woID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getBookingHoursForStep(stepID,woID);
    }

    public List<WorkFlowStepDetailsModel> getBookingStatusOfPreviousStep(StepDetailsModel stepDetailsModel) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getBookingStatusOfPreviousStep(stepDetailsModel);
    }
    //v1/getBookingStatusOfPreviousStep
    public List<WorkFlowStepDetailsModel> getBookingStatusOfPreviousStepV1(int wOID, int woFCID,int previousStepID,int flowChartDefID,int bookingID,String signumID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getBookingStatusOfPreviousStepV1(wOID,woFCID,previousStepID,flowChartDefID,bookingID,signumID);
    }

    public Map<String,Integer> getPreviousStepID(int stepID, int flowChartDefID,int wOID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getPreviousStepID(stepID,flowChartDefID,wOID);
    }

    public int getBookingIDOfPreviousStep(int previousStepID, int wOID, int woFCID, int flowChartDefID, String signumID) {
       WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getBookingIDOfPreviousStep(previousStepID,wOID,woFCID,flowChartDefID,signumID);
    }

    public void updateStepBookingStatus(int wOID, String stepID, int taskID, int flowChartDefID, int stepBookingID, String signumID,String bookingStatus) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
            wOExecutionMapper.updateStepBookingStatus(wOID,stepID,taskID,flowChartDefID,stepBookingID,signumID,bookingStatus);
    }

    public void checkAndUpdateStatusOfSameTask(int wOID, int taskID, int stepID, int flowChartDefID, String signumID) {
         WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
          wOExecutionMapper.checkAndUpdateStatusOfSameTask(wOID,taskID,stepID,flowChartDefID,signumID);
    }

    public int getSubActivityIDForWOID(int wOID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
          return wOExecutionMapper.getSubActivityIDForWOID(wOID);
    }

    public int getWFLatestVersion(int defID) {
         WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
          return wOExecutionMapper.getWFLatestVersion(defID);
    }

	public List<RpaModel> getParallelBotDetails(int taskid, int subActivityFlowChartDefID, String stepID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getParallelBotDetails(taskid, subActivityFlowChartDefID, stepID);
	}
//	public List<RpaModel> getRpaIDByWOID(Map<String, Object> map) {
//		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
//        return wOExecutionMapper.getRpaIDByWOID(map);
//	}

	public String getRpaDetails(int rpaId) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getRpaDetails(rpaId);
	}

	public int getProjectIDByTaskiD(int taskid) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getProjectIDByTaskiD(taskid);
	}

    public int getLatestBookingID(int woID, String signumID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getLatestBookingID(woID,signumID);
    }

    public void updateBookingStatus(int woID,int bookingID ,String signumID, String status, String comment) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        woExecutionMapper.updateBookingStatus(woID,bookingID,signumID,status,comment);
    }


    public void updateWOFlowChartStepDetails(int woID, int taskID, int bookingID, String flowChartStepID, int flowChartDefID, String status, String reason,String signumID) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        woExecutionMapper.updateWOFlowChartStepDetails(woID,taskID,bookingID,flowChartStepID,flowChartDefID,status,reason,signumID);
    }

    public Boolean checkBookingStatus(int woID, int bookingID) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return woExecutionMapper.checkBookingStatus(woID,bookingID);
    }

    public void updateBookingStatusInFlowChart(int woID, int bookingID, String signumID, String status) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        woExecutionMapper.updateBookingStatusInFlowChart(woID,bookingID,signumID,status);
    }

    public void saveUserWFProficency(UserWorkFlowProficencyModel userWorkFlowProficency,String lgSignum) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        woExecutionMapper.saveUserWFProficency(userWorkFlowProficency,lgSignum);
    }

    public void updateUserWFProficency(UserWorkFlowProficencyModel userWorkFlowProficency, String lgSignum) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        woExecutionMapper.updateUserWFProficency(userWorkFlowProficency,lgSignum);
    }

    public List<Map<String, String>> getUserWFProficency(String signumID,int projectID,int subActivityID) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return woExecutionMapper.getUserWFProficency(signumID,projectID,subActivityID);
    }

    public void updateWorkOrderWFVersion(int woID, int wfVersionNo, String signumID) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        woExecutionMapper.updateWorkOrderWFVersion(woID,wfVersionNo,signumID);
    }

    public LinkedList<WorkOrderProgressModel> getWorkOrders(String signumID, String status,String startDate,String endDate) {
         WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getWorkOrders(signumID,status,startDate,endDate);
    }

    public boolean checkIfParentBooked(int WOID,int subActivityDefID, String parentID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.checkIfParentBooked(WOID,subActivityDefID,parentID);
    }

    public List<Map<String,Object>> getWoProgressData(String woID) {
         WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
         return wOExecutionMapper.getWoProgressData(woID);
    }

    public List<WorkOrderNodes> getWONodeDetails(String woID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getWONodeDetails(woID);
    }

    public Boolean checkWorkOrderBookingStatus(int woid) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.checkWorkOrderBookingStatus(woid);
    }

    public WorkFlowBookingDetailsModel stopWOMaxBookingID(int woID, int taskID) {
         WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.stopWOMaxBookingID(woID,taskID);
    }
    public WorkFlowBookingDetailsModel stopWOMaxBookingIDNew(int woID, int taskID, String stepid) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
       return wOExecutionMapper.stopWOMaxBookingIDNew(woID,taskID, stepid);
   }
  
   public List<BookingDetailsModel> getStartedBookingsBySignum(String signum) {
       WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
      return wOExecutionMapper.getStartedBookingsBySignum(signum);
  }
   

   public int updateBookingsStatus(String bookingids,String status,String type, String reason) {
       WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
      return wOExecutionMapper.updateBookingsStatus(bookingids,status,type, reason);
  }
   public int updateBookingsStatusByIds(String bookingids,String status, String signumID) {
       WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
      return wOExecutionMapper.updateBookingsStatusByIds(bookingids,status,signumID);
  }
   public boolean insertBooking(List<BookingDetailsModel> currentBookingList) {

       WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
       boolean isSuccess = wOExecutionMapper.insertBooking(currentBookingList);

       // set computed id as booking id
       int bookingIdAndInstanceId = isfCustomIdInsert.generateCustomId(currentBookingList.get(0).getBookingID());
       currentBookingList.stream().forEach(bookingId -> bookingId.setBookingID(bookingIdAndInstanceId));

       return isSuccess;
   }
   
   public boolean addBooking(BookingDetailsModel booking) {

       WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
       boolean isSuccess = wOExecutionMapper.addBooking(booking);

       // set computed id as booking id
       booking.setBookingID(isfCustomIdInsert.generateCustomId(booking.getBookingID()));

       return isSuccess;
   }
   public boolean insertFcStepDetails(List<FcStepDetails> stepList) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
       return wOExecutionMapper.insertFcStepDetails(stepList);
   }
   public int updateFcStepStatusByBookingIds(String bookingid,String status) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
       return wOExecutionMapper.updateFcStepStatusByBookingIds(bookingid,status);
   }
   
   public List<FcStepDetails> selectFcStepDetailsByBookingIds(String bookingid) {
       WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
      return wOExecutionMapper.selectFcStepDetailsByBookingIds(bookingid);
  }

    public String getDecisionValueForStep(Integer stepID, int woID) {
         WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getDecisionValueForStep(stepID,woID);
    }
    
    public BookingDetailsModel getBookingDetailsByWoid(int woID,Integer taskID,String stepid) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
       return wOExecutionMapper.getBookingDetailsByWoid(woID, taskID,stepid);
   }
    public Integer getFlowChartStepByStepId(int woID,String stepid) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
       return wOExecutionMapper.getFlowChartStepByStepId(woID, stepid);
   }
    
    public StepDetailsModel getStepDetailsByStepId(int woID,String stepid) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
       return wOExecutionMapper.getStepDetailsByStepId(woID, stepid);
   }
    public Integer getTotalBookingHoursForStep(int woID,int stepid) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
       return wOExecutionMapper.getTotalBookingHoursForStep(woID, stepid);
   }

    public WorkFlowStepDataModel getWFStepData(String stepID, int defID, String executionType) {
         WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
       return wOExecutionMapper.getWFStepData(stepID, defID, executionType);
    }

    public List<Map<String, Object>> getWorkFlowStepDecisionValue(int wOID) {
         WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
           return wOExecutionMapper.getWorkFlowStepDecisionValue(wOID);
    }

	public List<Map<String, Object>> checkIFLastStepNew(int wOID, int subactivityDefID) {
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.checkIFLastStepNew(wOID, subactivityDefID);
		
	}

    public WorkFlowDetailsModel getWorkFlowNameForWoID(int wOID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	 return wOExecutionMapper.getWorkFlowNameForWoID(wOID);
    }

    public WOWorkFlowModel getExpertWorkFlow(int wOID, String wfName, int expertDefId) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getExpertWorkFlow(wOID, wfName, expertDefId);
    }

    public Map<String,Object> getExistingWFDefID(int wOID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getExistingWFDefID(wOID);
    }

    public void updateWODefID(int wOID, int flowChartDefID) {
       WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
       wOExecutionMapper.updateWODefID(wOID,flowChartDefID);
    }

	public Map<String, Object> getWOCreationDeatilsByWOName(String getwOName) {
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.getWOCreationDeatilsByWOName(getwOName);
	
	}

	public Map<String, Object> getWoDateDataByWoId(int getwOID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.getWoDateDataByWoId(getwOID);
	}
    
	
	public String getNodesByWoId(int wOID) {
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);		
		return wOExecutionMapper.getNodesByWoId(wOID);
	}
	
	public List<HashMap<String, Object>> getEfficiencyDevlieryIndexForUser(int woID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getEfficiencyDevlieryIndexForUser(woID);
	}

	public HashMap<String, Object> getWODetails(int getwOID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);		
		return wOExecutionMapper.getWODetails(getwOID);
	}

	public void populateEfficiencyData(LocalDate startDate,LocalDate endDate,int woID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		wOExecutionMapper.populateEfficiencyData(startDate,endDate,woID);
    
	}
    
	public void populateDeliveryData(LocalDate startDate,LocalDate endDate,int woID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		wOExecutionMapper.populateDeliveryData(startDate,endDate,woID);
		
	}

	public List<HashMap<String, Object>> getEfficiencyDevlieryPerformance(int projectID, String subActivity,
			int flowChartDefID, String signumID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getEfficiencyDevlieryPerformance(projectID,subActivity,flowChartDefID,signumID);
	}

	public List<WorkOrderBasicDetailsModel> getUnassignedWorkOrders(
			String signum, String startDate, String endDate, String priority, String status, String doStatus) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getUnassignedWorkOrders(signum, startDate,endDate,priority, status,doStatus);
	}
	public List<ProjectQueueWorkOrderBasicDetailsModel> getProjectQueueWorkOrders(String signum, String startDate, String endDate,
			String priority, String status, String doStatus,String projectIdList, DataTableRequest request,boolean checkSearch) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.getProjectQueueWorkOrders(signum, startDate,endDate,priority, status,doStatus,projectIdList, request,checkSearch);
	}

	public WorkOrderModel getWOData(int getwOID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getWOData(getwOID);
	}

	public boolean updateWoWithAsignedSignum(WorkOrderModel woData) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.updateWoWithAsignedSignum(woData);
		
	}

	public List<WorkOrderBasicDetailsModel> getAssignedWorkOrders(
			String signum, String woStatus, String startDate, String endDate) {
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.getAssignedWorkOrders(signum, woStatus,startDate,endDate);
	}

	public boolean updateWoSignumToNull(WorkOrderModel woData) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	    return wOExecutionMapper.updateWoSignumToNull(woData);		
	}

	public void insertWOTransferHistory(WorkOrderModel woData) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		wOExecutionMapper.insertWOTransferHistory(woData);				
	}

	public List<Map<String, Object>> checkTaskStatus(WorkOrderModel woData) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.checkTaskStatus(woData);			
	}

	public SourceSystemDetailsModel getSourceSystemDetails(String sourceSystemId) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.getSourceSystemDetails(sourceSystemId);			
	}

	public List<String> getIntegratedSource(String signum) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.getIntegratedSource(signum);
	}
	
	public List<WorkFlowStepLinksDetailModel> getFlowChartStepLinkDetailsByWoId(Integer woid) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.getFlowChartStepLinkDetailsByWoId(woid);
	}

	public boolean updateReferenceIDByBookingId(int bookingId,int referenceId,String signumID) {
       WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
       return wOExecutionMapper.updateReferenceIDByBookingId(bookingId,referenceId,signumID);
   }

	public boolean saveExternalExecDetails(int isfRefId, int externalRefId) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.saveExternalExecDetails(isfRefId, externalRefId);
		
	}
	
	public ExternalExecutionDetailsModel getExternalExecDetailsbyRefId(int isfRefId){
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.getExternalExecDetailsbyRefId(isfRefId);
	}
	
	public BookingDetailsModel getBookingDetailsByBookingId(int bookingId) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.getBookingDetailsByBookingId(bookingId);
		
	}
	
	public List<String> getMarketByWoid(Integer woid){
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.getMarketByWoid(woid);
	}

	public Map<String, Object> validateUserLoginHistory(UserLoginModel userLogin) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.validateUserLoginHistory(userLogin);
	}

	public HashMap<String, Object> getWODetailsByWoid(int woID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.getWODetailsByWoid(woID);
	}

	public int checkIfAllTaskCompleted(int woID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.checkIfAllTaskCompleted(woID);
	}
	
    public List<BookingDetailsModel> getAlreadyStartedBookingsByWoID(int woID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getAlreadyStartedBookingsByWoID(woID);
    }

	public HashMap<String, Object> isInstalledDesktopVersionUpdated(String signumID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.isInstalledDesktopVersionUpdated(signumID);
	}

	public WorkFlowBookingDetailsModel maxQueuedBookingID(Integer woID, Integer taskID) {
		// TODO Auto-generated method stub
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.maxQueuedBookingID(woID,taskID);
	}

	public String getFlowChartStepIDByBooking(Integer woID, Integer taskID, int bookingID, String signumID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.getFlowChartStepIDByBooking(woID,taskID,bookingID,signumID);
		// TODO Auto-generated method stub
		
	}

	public int updateOnlyBookingsStatusByIds(String bookingids,String status,String signumID) {
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	      return wOExecutionMapper.updateOnlyBookingsStatusByIds(bookingids,status,signumID);
	}

	public int updateReasonByBookingIds(String bookingid, String status) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	    return wOExecutionMapper.updateReasonByBookingIds(bookingid,status);
		
	}

	public int countInvalidStepsForClouser(SaveClosureDetailsForWOModel saveClosureDetailsForWOModel) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	    return wOExecutionMapper.countInvalidStepsForClouser(saveClosureDetailsForWOModel);
	}

	public List<BookingDetailsModel> getStartedBookingsBySignum1(String signumID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	    return wOExecutionMapper.getStartedBookingsBySignum1(signumID);
	}

	public String getWorkOrderPriority(int woID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	    return wOExecutionMapper.getWorkOrderPriority(woID);
	}

	public void updateWorkOrderPriority(int woID, String priority) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		wOExecutionMapper.updateWorkOrderPriority(woID, priority);
		
	}

	public void saveAutomaticStepClosureDetails(ServerBotModel serverBotModel, String hours, int bookingID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		wOExecutionMapper.saveAutomaticStepClosureDetails(serverBotModel, hours, bookingID) ;
	}

	public void updateOnlyBookingsReasonByIds(String bookingids, String reason, String signumID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		wOExecutionMapper.updateOnlyBookingsReasonByIds(bookingids,reason,signumID ) ;
		
	}

	public String getStepNameByBookingID(int bookingID) {
		// TODO Auto-generated method stub
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.getStepNameByBookingID(bookingID);
	}

	public int getLatestBookingIDNotCompleted(int woID) {
		// TODO Auto-generated method stub
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.getLatestBookingIDNotCompleted(woID);
	}

	public boolean isWoAssignedToSignum(Integer woId, String signumID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.isWoAssignedToSignum(woId, signumID);
	}

	public Integer getExpertDefID(String wfName, String wfid) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.getExpertDefID(wfName,wfid);
	}

	public Boolean checkWorkOrderBookingStatusReason(int wOID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.checkWorkOrderBookingStatusReason(wOID);
	}

	public Integer getMaxBookingId(int woID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getMaxBookingId(woID);
	}

	public List<DeliverableSuccessReasonModel> getWorkOrderSuccessReasons(int workOrderID) {
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.getWorkOrderSuccessReasons(workOrderID);
	}

	public void addDeliverableSuccessReason(String signum,
			DeliverableSuccessReasonModel deliverableSuccessReasonModel) {	
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     wOExecutionMapper.addDeliverableSuccessReason(signum,deliverableSuccessReasonModel);	
	}
	
	public List<Map<String, Object>> getWONodeDetailsByWOID(String woIDValue) {
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
         return wOExecutionMapper.getWONodeDetailsByWOID(woIDValue);
	}

	public void updateDeliverableSuccessReason(String signum,
			DeliverableSuccessReasonModel deliverableSuccessReasonModel) {
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     wOExecutionMapper.updateDeliverableSuccessReason(signum,deliverableSuccessReasonModel);
		
	}

	public boolean validateSuccessReason(DeliverableSuccessReasonModel deliverableSuccessReasonModel) {
	
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	    return  wOExecutionMapper.validateSuccessReason(deliverableSuccessReasonModel);
	}

	public List<DeliverableSuccessReasonModel> getAllSuccessReasons(int deliverableUnitId) {
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.getAllSuccessReasons(deliverableUnitId);
		
	}

	public void saveReasonStatusActive(String signum,DeliverableSuccessReasonModel deliverableSuccessReasonModel) {
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	      wOExecutionMapper.saveReasonStatusActive(signum,deliverableSuccessReasonModel);
	}

	public void saveReasonStatus(String signum,DeliverableSuccessReasonModel deliverableSuccessReasonModel) {
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	      wOExecutionMapper.saveReasonStatus(signum,deliverableSuccessReasonModel);
	}

	public String getExecutionType(int wOID, String stepid, int taskID, int bookingId) {
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.getExecutionType(wOID,stepid,taskID,bookingId);
	}

	public EventPublisherURLMappingModel getEventPublisherURLMappingModel(String sourceName, String category) {

		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	    return wOExecutionMapper.getEventPublisherURLMappingModel(sourceName, category);
	}

	public String getSadCountForStep(String stepID, int woID, String signum, int defID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getSadCountForStep(stepID,woID,signum, defID);
	}

	public String getWOAssignedTo(int wOID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getWOAssignedTo(wOID);
	}

	public String getFlowChartType(Integer wOID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getFlowChartType(wOID);
	}

	public boolean checkStartedTaskInBooking(int wOID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.checkStartedTaskInBooking(wOID);
	}

	public BookingDetailsModel getBookingDetailsandHours(int bookedID, String stepID, int wOID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getBookingDetailsandHours(bookedID, stepID, wOID);
		
	}

	public Integer getAutoSenseSourceIdOnSourceName(String sourceName) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getAutoSenseSourceIdOnSourceName(sourceName);
	}
	
	public StepDetailsModel getDecsionDetailsByChildStep(int flowChartDefID, String flowChartStepId) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getDecsionDetailsByChildStep(flowChartDefID, flowChartStepId);
	}

	public List<String> getMarkets(WorkOrderColumnMappingModel workOrderColumnMappingModel) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getMarkets(workOrderColumnMappingModel);
	}

	public LinkedList<WorkOrderProgressModel> getWorkOrderDetailsBySignum(String signumID, String status) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getWorkOrderDetailsBySignum(signumID, status);
	}


	public String getJsonValue(int flowChartDefID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getJsonValue(flowChartDefID);
	}

	public boolean addNewBooking(List<BookingDetailsModel> startedBookingsOfUser, int wOID, int parallelCount) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		wOExecutionMapper.addNewBooking(startedBookingsOfUser, wOID, parallelCount);
			for(BookingDetailsModel booking :startedBookingsOfUser) {
				System.out.println(booking.getBookingID());
				booking.setBookingID(isfCustomIdInsert.generateCustomId(booking.getBookingID()));
			}
		
        return true;
	}
	
	public void checkAndUpdateFlowChartDefID(int wOID, int flowChartDefID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.checkAndUpdateFlowChartDefID(wOID, flowChartDefID);
	}
	
	public void markWorkOrderAsStarted(int woID, String signum) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.markWorkOrderAsStarted(woID, signum);
	}
	public BookingDetailsModel getBookingDetailsByWorkOrderID(int woID) {
        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getBookingDetailsByWorkOrderID(woID);
    }

	public UserWorkFlowProficencyModel getWFUserProficiency(String signumID, int subactivityID, int wfID, int projectID, int defID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getWFUserProficiency(signumID, subactivityID, wfID, projectID, defID );
	}

	public SaveWfUserProfResponseModel saveWFUserProficiency(String signumID, WorkFlowUserProficiencyModel workFlowUserProficiencyModel, int subActivityId, int wfID, int woID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.saveWFUserProficiency(signumID,workFlowUserProficiencyModel, subActivityId, wfID, woID);
	}

	public boolean checkIFProficiencyIDExists(int proficiencyID) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return woExecutionMapper.checkIFProficiencyIDExists(proficiencyID);
	}

	public void updateStatusandProficiencyId(int wOID, String signumID, int proficiencyID, boolean updateActualStartDate) {
		WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        woExecutionMapper.updateStatusandProficiencyId(wOID, signumID, proficiencyID, updateActualStartDate);
	}

	public StepDetailsModel getDisabledStepDetails(int subActivityDefID, int wOID) {
		WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return woExecutionMapper.getDisabledStepDetails(subActivityDefID, wOID);
	}
	public List<ChildStepDetailsModel> getDescisionStepDetails(String stepID, Integer defID, Integer woID) {
		WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return woExecutionMapper.getDescisionStepDetails(stepID, defID, woID);
	}

	public List<WorkflowStepDetailModel> getAllStepsByWOID(int woID) 
	 {
		WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
       return woExecutionMapper.getAllStepsByWOID(woID);
	}

	public List<NextStepModel> getAllNextStepID(String stepID, Integer defID) {
		WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	       return woExecutionMapper.getAllNextStepID(stepID, defID);
	}

	public int getKpiValue(int subactivityID, int wfID, int projectID, String qualifyCount) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getKpiValue(subactivityID, wfID, projectID, qualifyCount);
	}

	public ProficiencyTypeModal getProficiencyDetailsByName(String proficiencyName) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getProficiencyDetailsByName(proficiencyName);
	}

	public String getWfNameByWfId(int subactivityID, int wfID, int projectID, int defID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getWfNameByWfId(subactivityID, wfID, projectID, defID);
	}

	public String getProficiencyNameWoID(int wOID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getProficiencyNameWoID(wOID);
	}

	public boolean checkIFPreviousStepCompletedQualifiedWo(int wOID, int subActivityDefID, String flowChartStepID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.checkIFPreviousStepCompletedQualifiedWo(wOID, subActivityDefID, flowChartStepID);
	}

	public int getSubActivityDefIDForWOID(int woID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getSubActivityDefIDForWOID(woID);
	}

	public StepTaskModel getCurrentStepDetails(String stepID, Integer defID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getCurrentStepDetails(stepID,defID);
	}

	public String getLastStepID(int subactivityDefID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getLastStepID(subactivityDefID);
	}

	public Integer getProficiencyId(String wOID, String signum) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getProficiencyId(wOID,signum);
	}

	public Map<String,FlowChartReverseTraversalModel> getAllWorkOrderStepsForTraversal(int wOID, int subactivityDefID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getAllWorkOrderStepsForTraversal(wOID,subactivityDefID);
	}
	
    public void updateBookingStatusOnStart(int woID,int bookingID ,String signumID, String status, String comment) {
        WOExecutionMapper woExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        woExecutionMapper.updateBookingStatusOnStart(woID,bookingID,signumID,status,comment);
    }
	

	public WorkFlowBookingDetailsModel maxDisabledOnHoldBookingID(Integer woID, Integer taskID) {
		// TODO Auto-generated method stub
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.maxDisabledOnHoldBookingID(woID,taskID);
	}

	public boolean checkIFManualDisabledStep(int subActivityDefID, String flowChartStepID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.checkIFManualDisabledStep(subActivityDefID, flowChartStepID);
	}

	public boolean checkIFPreviousStepCompletedDisabled(int wOID, int subActivityDefID, String flowChartStepID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.checkIFPreviousStepCompletedDisabled(wOID, subActivityDefID, flowChartStepID);
	}

	public String getExecutionTypeNew(int defID, String stepid) {
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.getExecutionTypeNew(defID,stepid);
	}

	public List<Map<String, Object>> downloadWorkHistoryData(String signum, String startDate, String endDate, String woStatus) {
		 WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	     return wOExecutionMapper.downloadWorkHistoryData(signum,startDate,endDate,woStatus);
	}	
	
	 public List<WorkOrderNodesModel> getListOfNode(int wOID) {
	        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	        return wOExecutionMapper.getListOfNode(wOID);
	 }

	public boolean isVersionMatched(String signum) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.isVersionMatched(signum);
	}
	
	public String getLatestVersionNumber() {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getLatestVersionNumber();
	}

	public boolean isInstalledLibraryVersionUpdated(String signumID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.isInstalledLibraryVersionUpdated(signumID);
	}
	
	public String getAssignedToSignumByWOID(int wOID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getAssignedToSignumByWOID(wOID);
	}
	
	public boolean validateWOStatus(int wOID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		   return  wOExecutionMapper.validateWOStatus(wOID);		
	}
	
	public List<Map<String, Object>> downloadProjectQueueWorkOrders(String signum, String startDate, String endDate,
			String priority, String status, String doStatus, String projectIdList,int length) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		return wOExecutionMapper.downloadProjectQueueWorkOrders(signum, startDate, endDate, priority, status, doStatus,
				projectIdList,length);
	}

	
	public void insertWorkOrderNetworkElement(String tablename, int wOID, String signum) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.insertWorkOrderNetworkElement(tablename,wOID,signum);
	}

	public void deleteExistingWorkOrderNECount(int wOID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.deleteExistingWorkOrderNECount(wOID);
		
	}

	public void insertWorkOrderNECount(int wOID, int count, String neTextName, String signum) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.insertWorkOrderNECount(wOID, count, neTextName, signum );
		
	}

	 public List<WoCompleteDetailsModel> getCompleteWoDetails(int wOID, String columnNames){
	        WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
	        return wOExecutionMapper.getCompleteWoDetails(wOID, columnNames);
	    }

	public List<String> getMarketsByWorkOrders(WorkOrderColumnMappingModel workOrderColumnMappingModel,
			int countryCustomerGroupId) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getMarketsByWorkOrders(workOrderColumnMappingModel,countryCustomerGroupId);
	}

	public void updatetWorkOrderNECount(int wOID, int count, String neTextName, String signum) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        wOExecutionMapper.updatetWorkOrderNECount(wOID, count, neTextName, signum );

		
	}
	
	public String getSharepointUrl() {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getSharepointUrl();
	}
	
	public int getMarketAreaIDByProjectID(int projectID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getMarketAreaIDByProjectID(projectID);
	}
	
	public SharePointDetailModel getDetailsByMarketID(int marketAreaID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getDetailsByMarketID(marketAreaID);
	} 
	
	public boolean validateSharePointDetailWithMarketArea(int marketAreaID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
		   return  wOExecutionMapper.validateSharePointDetailWithMarketArea(marketAreaID);		
	}
	
	public List<SharePointDetailModel> getSharepointConfigurationDetails() {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getSharepointConfigurationDetails();
	}

	public String getOutPutUploadByStepID(String stepID, int flowChartDefID) {
		WOExecutionMapper wOExecutionMapper = sqlSession.getMapper(WOExecutionMapper.class);
        return wOExecutionMapper.getOutPutUploadByStepID(stepID,flowChartDefID);
	} 
	
	

	
}
