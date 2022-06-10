/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;

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

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author ekumvsu
 */
public interface RpaMapper {
    
	public void startTask_bookingDetails(@Param("woID") int woID,
            @Param("taskID") int task,
            @Param("date") Date date,
            @Param("maxBookingID") int maxBookingID,
            @Param("signumID") String signumID,@Param("sourceID") Integer sourceID, @Param("botplatform") String botplatform,@Param("reason") String reason);
    
    public void updateWOStatus(@Param("woID") int woID);
    
//    public void updateWOActualStartDate(@Param("woID") int woID,
//                                        @Param("date") Date date);
    
    public String getBookingID(@Param("woID") int woID);
    
    public Boolean checkIFWOIDExists(@Param("woID") int woID);
    
    public void closeRpaWO(@Param("woID") int woID, 
                        @Param("signumID") String signumID,
                        @Param("closedOn") String closedOn,@Param("actualEndDate") String actualEndDate);

    public Map<String,Integer> getProjAndSubActivityID(@Param("woID") int woID);

    public Integer getTaskID(@Param("subActivityID") int subActivityID,@Param("task") String task);
    
    public RPABookingModel getRPATaskID(@Param("woID") int woID,
                        @Param("task") String task);
    
    public void updateHoursForRPA(@Param("bookingID") int bookingID,
            @Param("hours") double hours,
            @Param("endDate") Date endDate,@Param("reason") String reason,@Param("signumID") String signumID,@Param("botPlateform") String botPlateform);

    public List<RPAWorkOrderDetails> getRPAWorkOrderDetails(@Param("projectID") int projectID);

    public int getScopeID(@Param ("projectID") int projectID,@Param("subActivityID") int subActivityID);

    public int getWOVersionNo(@Param("woID") int woID);

    public int getFlowChartDefID(@Param ("projectID") int projectID,@Param("subActivityID") int subActivityID,@Param("versionNo") int versionNo);

    public List<String> getFlowChartStepID(@Param ("flowchartDefID") int flowchartDefID,@Param ("taskID") int taskID);

    public String getWOSignumID(@Param("woID") int woID);
    public List<SoftHumanDumpModel> getRPADeployedDetails(@Param("projectID") int projectID);

    public int getWorkOrderID(@Param ("projectID")int projectID,@Param("subActivityID") int subActivityID,
                              @Param("scopeID")int scopeID,@Param("woName") String woName,@Param("wfVersion") int wfVersion,
                              @Param("woPlanID") int woPlanID, @Param("type") String type);

	public Map<String, Integer> getWorkFlowData(@Param ("projectID") int projectID,@Param("subActivityID") int subActivityID);

	public Boolean checkIFWOExists(@Param ("projectID") int projectID,@Param ("scopeID")  int scopeID,
			                       @Param ("subActivityID") int subActivityID,@Param ("wfDefID")  Integer wfDefID,
			                       @Param ("nodeNames")String nodeNames);

	public void insertBulkWorkOrderDetails(@Param("bulkModel") BulkWorkOrderCreationModel bulkModel,@Param("sourceID") int sourceID,
										   @Param("executionPlanID") int executionPlanID);

	public void insertBulkWorkOrderHistory(@Param("bulkModel") BulkWorkOrderCreationModel bulkModel);

	public int getExternalSource(@Param("sourceName") String source);

	public Integer getExecutionPlan(@Param("executionPlan") String executionPlan,@Param("projectID") int projectID);

	public void saveBotConfig(@Param("botData") BotConfig botData);

	public BotConfig getBotConfig(@Param("type") String type, @Param("referenceId") String referenceId);
	
	List<BookingDetailsModel> getBookingsByReferenceId(@Param("referenceId") int referenceId);

	public void updateBotConfig(@Param("botData") BotConfig botData);

	public List<HashMap<String, Object>> getBoTsForExplore(@Param("taskId") int taskId);

	public List<HashMap<String, Object>> getTemplateFile(@Param("templateName") String templateName);

	public void uploadTemplateFile(@Param("inputDataFile") byte[] inputDataFile,@Param("templateName") String templateName,@Param("signum") String signum);

	public List<ExecutionPlanDetail> getExecutionPlanByName(@Param("executionPlanName") String executionPlanName, @Param("projectID")  int projectID);

	public List<String> getWOPlanbyWOID(@Param("woID") int woID);

	public List<HashMap<String, Object>> getRPADomain(@Param("marketarea")String marketarea);
	
	public List<HashMap<String, Object>> getRPASubactivity(@Param("marketarea")String marketarea, @Param("domainId")Integer domainId);

	public List<HashMap<String, Object>> getRPATechnology(@Param("domainId") int domainId,@Param("market") String market,@Param("subActivityId") int subActivityId);

	public List<HashMap<String, Object>> getRPABOTDetails(@Param("domainid")Integer domainid, @Param("technologyId")Integer technologyId,
			@Param("subactivityId")Integer subactivityId, @Param("taskId") Integer taskId, @Param("marketarea")String marketarea);

	public TestingBotDetailsModel getTestingBotDetails(@Param("rpaRequestId") String rpaRequestId);

	public void saveExceptionLog(@Param("exceptionLog") ExceptionLogModel exceptionLog);
	
	public void SaveSHExceptionLog(@Param("SHExceptionLog") ExceptionLogModel exceptionLog);
	
	public void SaveBotExceptionLog(@Param("BotExceptionLog") ExceptionLogModel exceptionLog);

	public Integer WOMaxBookingID(@Param("taskID") Integer taskID,@Param("wOID") Integer wOID,@Param("signum") String signum);

	public List<HashMap<String, Object>> getAllRPARequestDetails(@Param("rpaRequestID") int rpaRequestID);

	public void updateVideoURL(@Param("videoUrlModel") VideoURLModel videoUrlModel);

	public List<HashMap<String, Object>> getTaskDetails(@Param("subActivityID") int subActivityID);

	public List<HashMap<String, Object>> getRPATask(@Param("domainId") int domainId,@Param("market") String market,@Param("subActivityId") int subActivityId,@Param("technologyId") int technologyId);

	public boolean getProjectScopeActive(@Param("executionPlanName") String executionPlanName,@Param("projectID") int projectID);

	public HashMap<String,Object> getRPAIsRunOnServer(@Param("rpaId") int rpaId);

	public List<WorkOrderBookingDetailsModel> getWorkOrderBookingDetailsById(@Param("woekOrderdId") int woekOrderdId);

	public Integer getFlowChartDefinitionID(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID,@Param("versionNo") int versionNo, @Param("woID") int woID);

	public boolean getIsUserValidForBotChanges(@Param("signum") String signum,@Param("botID") int botID);

	public void saveInputZipFile(@Param("serverBotModel") ServerBotModel serverBotModel,@Param("file") byte[] file,@Param("fileName") String fileName,@Param("type") String type, @Param("serverBotInputUrl") String serverBotInputUrl, @Param("serverBotOutputUrl")String serverBotOutputUrl);

	public BotInputFileModel getBotInputFile(@Param("signum") String signum,@Param("woid") int woid,@Param("taskid") int taskid,@Param("stepid") String stepid,@Param("bookingid") int bookingid);

	public List<Map<Object, String>> getServerModelData(@Param("WOID") Integer WOID,@Param("signumID") String signumID);

	public List<TblRpaDeployedBotModel> getBOTsForExploreAuditData();

	public int getBotIdByStepId(@Param("flowChartDefID") Integer flowChartDefID,@Param("stepId") String stepId, @Param("WOID") Integer woId);

	public int getRpaIDByStepID(@Param("stepid")String stepid);

	public List<WorkOrderCurrentStepDetailsModel> getFlowChartStepDetails(@Param("flowchartdefid") int flowchartdefid,@Param("taskID") Integer taskID,@Param("woid") int woid);
	
	public List<ErrorDetailsModel> getAllErrorDetails(
			@Param("sourceID") Integer sourceID,@Param("dataTableReq") DataTableRequest dataTableReq);
	
	public Boolean isSourceExists(@Param("sourceID") Integer sourceID);
	
	public boolean addErrorDetail(@Param("errorDetailsModel") ErrorDetailsModel errorDetailsModel);
	
	public Boolean isErrorDetailExists(@Param("sourceID") Integer sourceID,@Param("errorType") String errorType,@Param("errorMessage") String errorMessage);

	public boolean validateErrorCode(@Param("errorcode")int errorcode);

	public List<ErrorDetailsModel> getErrorDetailsByCode(@Param("errorcode") int errorCode);
	
	public String getServerBotOutputUrl(@Param("projectID") int projectID);

}
	


