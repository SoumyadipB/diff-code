/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import com.ericsson.isf.mapper.AccessManagementMapper;
import com.ericsson.isf.mapper.RpaMapper;
import com.ericsson.isf.mapper.WOExecutionMapper;
import com.ericsson.isf.mapper.WorkOrderMapper;
import java.util.Date;
import java.util.HashMap;

import com.ericsson.isf.model.BookingDetailsModel;
import com.ericsson.isf.model.BotConfig;
import com.ericsson.isf.model.BotInputFileModel;
import com.ericsson.isf.model.BulkWorkOrderCreationModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.ErrorDetailsModel;
import com.ericsson.isf.model.ExceptionLogModel;
import com.ericsson.isf.model.ExecutionPlanDetail;
import com.ericsson.isf.model.RPABookingModel;
import com.ericsson.isf.model.RPAWorkOrderDetails;
import com.ericsson.isf.model.SoftHumanDumpModel;
import com.ericsson.isf.model.TblRpaDeployedBotModel;
import com.ericsson.isf.model.TestingBotDetailsModel;
import com.ericsson.isf.model.VideoURLModel;
import com.ericsson.isf.model.WorkFlowBookingDetailsModel;
import com.ericsson.isf.model.WorkOrderBookingDetailsModel;
import com.ericsson.isf.model.WorkOrderCurrentStepDetailsModel;
import com.ericsson.isf.model.botstore.ServerBotModel;
import com.ericsson.isf.model.botstore.TblRpaDeployedBot;
import com.ericsson.isf.model.botstore.TblRpaRequest;
import com.ericsson.isf.util.IsfCustomIdInsert;

import java.util.List;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ekumvsu
 */
@Repository
public class RpaDAO {
    @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;
    
    @Autowired
	private IsfCustomIdInsert isfCustomIdInsert;
    
    public void startTask_bookingDetails(int woID, int taskID, Date date, int maxBookingID, String signumID, Integer sourceID, String botplatform, String reason) {
        RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        rpaMapper.startTask_bookingDetails(woID, taskID, date, maxBookingID, signumID,sourceID,botplatform,reason);
        
    }
    
    public void updateWOStatus(int woID){
        RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        rpaMapper.updateWOStatus(woID);
    }
    
    public String getBookingID(int woID) {
        RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getBookingID(woID);
    }
    
    public Boolean checkIFWOIDExists(int woID) {
        RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.checkIFWOIDExists(woID);
    }
    
    
    public void closeRpaWO(int woID, String signumID, String closedOn, String actualEndDate) {
        RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        rpaMapper.closeRpaWO(woID, signumID, closedOn,actualEndDate);
    }

    public Map<String,Integer> getProjAndSubActivityID(int woID) {
        RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getProjAndSubActivityID(woID);
    }

    public Integer getTaskID(int subActivityID,String task) {
        RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getTaskID(subActivityID,task);
    }

    
    public RPABookingModel getRPATaskID(int woID, String task){
        RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getRPATaskID(woID,task);
    }
    
    public void updateHoursForRPA (int bookingID, double hours, Date date, String reason, String signumID, String botPlateform){
        RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        rpaMapper.updateHoursForRPA(bookingID,hours,date, reason,signumID,botPlateform);
    }
    
    public List<RPAWorkOrderDetails> getRPAWorkOrderDetails(int projectID) {
        RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getRPAWorkOrderDetails(projectID);
    }

     public int getScopeID(int projectID, int subActivityID) {
        RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getScopeID(projectID,subActivityID);
    }

    public int getWOVersionNo(int woID) {
        RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getWOVersionNo(woID);
    }

    public int getFlowChartDefID(int projectID, int subActivityID, int versionNo) {
       RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getFlowChartDefID(projectID,subActivityID,versionNo);
    }

    public List<String> getFlowChartStepID(int flowchartDefID, int taskID) {
        RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getFlowChartStepID(flowchartDefID,taskID);
    }

    public String getWOSignumID(int woID) {
        RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getWOSignumID(woID);
    }
    
     public List<SoftHumanDumpModel> getRPADeployedDetails( int projectID)
    {
      RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
      return rpaMapper.getRPADeployedDetails(projectID);
    }

    public int getWorkOrderID(int projectID, int subActivityID, int scopeID, String woName, int wfVersion,int woPlanID,String type) {
       RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
       return rpaMapper.getWorkOrderID(projectID,subActivityID,scopeID,woName,wfVersion,woPlanID,type);
    }

	public Map<String, Integer> getWorkFlowData(int projectID, int subActivityID) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getWorkFlowData(projectID,subActivityID);
	}

	public Boolean checkIFWOExists(int projectID, int scopeID, int subActivityID, Integer wfDefID, String nodeNames) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.checkIFWOExists(projectID,scopeID,subActivityID,wfDefID,nodeNames);
	}

	public void insertBulkWorkOrderDetails(BulkWorkOrderCreationModel bulkModel,int sourceID,int executionPlanID) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		rpaMapper.insertBulkWorkOrderDetails(bulkModel,sourceID,executionPlanID);
		int workOrderCreationIdAndInstanceId=isfCustomIdInsert.generateCustomId(bulkModel.getWoCreationID());
		bulkModel.setWoCreationID(workOrderCreationIdAndInstanceId);
	}

	public void insertBulkWorkOrderHistory(BulkWorkOrderCreationModel bulkModel) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		rpaMapper.insertBulkWorkOrderHistory(bulkModel);
		
	}

	public int getExternalSource(String source) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getExternalSource(source);
	}

	public Integer getExecutionPlan(String executionPlan,int projectID) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getExecutionPlan(executionPlan,projectID);
	}

	public void saveBotConfig(BotConfig botData) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		rpaMapper.saveBotConfig(botData);		
	}

	public BotConfig getBotConfig(String type, String referenceId) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getBotConfig(type,referenceId);		
	}
	public List<BookingDetailsModel> getBookingsByReferenceId(int referenceId) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getBookingsByReferenceId(referenceId);
	}

	public void updateBotConfig(BotConfig botData) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		rpaMapper.updateBotConfig(botData);		
	}

	public List<HashMap<String, Object>> getBoTsForExplore(int taskId) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getBoTsForExplore(taskId);
	}

	public List<HashMap<String, Object>> getTemplateFile(String templateName) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getTemplateFile(templateName);
	}

	public void uploadTemplateFile(byte[] inputDataFile, String templateName, String signum) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		rpaMapper.uploadTemplateFile(inputDataFile,templateName,signum);	
	}

	public List<ExecutionPlanDetail> getExecutionPlanByName(String executionPlanName, int projectID) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getExecutionPlanByName(executionPlanName,projectID);
	}

	public List<String> getWOPlanbyWOID(int woID) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getWOPlanbyWOID(woID);
	}

	public List<HashMap<String, Object>> getRPADomain(String marketarea) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getRPADomain(marketarea);

	}
	
	public List<HashMap<String, Object>> getRPASubactivity(String marketarea, Integer domainId) 
	{
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getRPASubactivity(marketarea, domainId);

	}

	public List<HashMap<String, Object>> getRPATechnology(int domainId, String market, int subActivityId) 
	{
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getRPATechnology(domainId, market, subActivityId);
	}

	public List<HashMap<String, Object>> getRPABOTDetails(Integer domainid, Integer technologyId, Integer subactivityId, 
			Integer taskId, String marketarea) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getRPABOTDetails(domainid, technologyId, subactivityId, taskId, marketarea);
	}

	public TestingBotDetailsModel getTestingBotDetails(String rpaRequestId)  {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getTestingBotDetails(rpaRequestId);
	}

	public void saveExceptionLog(ExceptionLogModel exceptionLog) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		rpaMapper.saveExceptionLog(exceptionLog);
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
	public void SaveSHExceptionLog(ExceptionLogModel exceptionLog) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		rpaMapper.SaveSHExceptionLog(exceptionLog);
	}
	
	public void SaveBotExceptionLog(ExceptionLogModel exceptionLog) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		rpaMapper.SaveBotExceptionLog(exceptionLog);
	}

	public Integer WOMaxBookingID(Integer taskID, Integer wOID, String signum) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.WOMaxBookingID(taskID, wOID, signum);
	}

	public List<HashMap<String, Object>> getAllRPARequestDetails(int rpaRequestID) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
	    return rpaMapper.getAllRPARequestDetails(rpaRequestID);
	}

	public void updateVideoURL(VideoURLModel videoUrlModel) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
	    rpaMapper.updateVideoURL(videoUrlModel);
	}

	public List<HashMap<String, Object>> getTaskDetails(int subActivityID) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
	    return rpaMapper.getTaskDetails(subActivityID);
	}

	public List<HashMap<String, Object>> getRPATask(int domainId, String market, int subActivityId, int technologyId) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getRPATask(domainId, market, subActivityId, technologyId);
	}

	public boolean getProjectScopeActive(String executionPlanName, int projectID) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getProjectScopeActive(executionPlanName, projectID);
	}
	public HashMap<String,Object> getRPAIsRunOnServer(int rpaId) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getRPAIsRunOnServer(rpaId);
		
	}

	public List<WorkOrderBookingDetailsModel> getWorkOrderBookingDetailsById(int woID) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getWorkOrderBookingDetailsById(woID);
	}

	public Integer getFlowChartDefinitionID(int projectID, int subActivityID, int versionNo, int woID) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getFlowChartDefinitionID(projectID,subActivityID,versionNo, woID);
	}

	public boolean getIsUserValidForBotChanges(String signum, int botID) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getIsUserValidForBotChanges(signum, botID);
	}

	public void saveInputZipFile(ServerBotModel serverBotModel, byte[] file, String fileName, String type, String serverBotInputUrl, String serverBotOutputUrl) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
	    rpaMapper.saveInputZipFile(serverBotModel,file,fileName,type,serverBotInputUrl, serverBotOutputUrl);
	}

	public BotInputFileModel getBotInputFile(String signum, int woid, int taskid, String stepid, int bookingid) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getBotInputFile(signum,woid,taskid,stepid,bookingid);
        }

	public List<Map<Object, String>> getServerModelData(Integer WOID, String signumID) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getServerModelData(WOID, signumID);
	}

	public List<TblRpaDeployedBotModel> getBOTsForExploreAuditData() {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getBOTsForExploreAuditData();
	}

	public int getBotIdByStepId(Integer flowChartDefID, String stepId, Integer woId) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getBotIdByStepId(flowChartDefID,stepId,woId);
	}

	public int getRpaIDByStepID(String stepid) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getRpaIDByStepID(stepid);
	}

	public List<WorkOrderCurrentStepDetailsModel> getFlowChartStepDetails(int flowchartdefid, Integer taskID, int woid) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
        return rpaMapper.getFlowChartStepDetails(flowchartdefid,taskID,woid);
        }
	
	public List<ErrorDetailsModel> getAllErrorDetails(Integer sourceID,DataTableRequest dataTableReq) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getAllErrorDetails(sourceID,dataTableReq);
	}
	
	public Boolean isSourceExists(Integer sourceID) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.isSourceExists(sourceID);
	}

	public boolean validateErrorCode(int errorcode) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
	   return  rpaMapper.validateErrorCode(errorcode);
		
	}

	public List<ErrorDetailsModel> getErrorDetailsByCode(int errorCode) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.getErrorDetailsByCode(errorCode);
		
	}
	
	public boolean addErrorDetail(ErrorDetailsModel errorDetailsModel) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.addErrorDetail(errorDetailsModel);		
	}	
	
	public Boolean isErrorDetailExists(Integer sourceID, String errorType, String errorMessage) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
		return rpaMapper.isErrorDetailExists(sourceID,errorType,errorMessage);
	}
	
	public String getServerBotOutputUrl(int projectID) {
		RpaMapper rpaMapper = sqlSession.getMapper(RpaMapper.class);
	    return rpaMapper.getServerBotOutputUrl(projectID);
	}

}
