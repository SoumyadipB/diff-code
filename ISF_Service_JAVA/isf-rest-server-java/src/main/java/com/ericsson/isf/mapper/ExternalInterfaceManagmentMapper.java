/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.model.ErisiteDataModel;
import com.ericsson.isf.model.ExecutionPlanModel;
import com.ericsson.isf.model.NetworkElementNewModel;
import com.ericsson.isf.model.ViewNetworkElementModel;



/**
 *
 * @author esanpup
 */
public interface ExternalInterfaceManagmentMapper {

	public List<Map<String, Object>> checkIsfHealth();

	public boolean generateWOrkOrder(@Param("erisiteDataModel") ErisiteDataModel erisiteDataModel, @Param("externalSourceID") int externalSourceID,@Param("isfProjectId")  int isfProjectId);

	public List<Map<String, Object>> getExternalProjectID(@Param("sourceID") int sourceID);

	public List<Map<String, Object>> getActiveExternalProjectID(@Param("source") String source, @Param("projectID") int projectID);

	public boolean insertBlkWoCreation(
			@Param("erisiteDataModel") ErisiteDataModel erisiteDataModel,
			@Param("tablename") String tablename, @Param("hour") long hour,
			@Param("subActivity") int subActivity,
			@Param("isfProjectID") int isfProjectID,
			@Param("executionPlanID") int executionPlanID,
			@Param("nodeName") String nodeName,
			@Param("assignTo") String assignTo,
			@Param("source_system_id") String source_system_id,
			@Param("message_batch_id") String message_batch_id,
			@Param("transaction_id") String transaction_id, 
			@Param("businessentityid") String businessentityid,
			@Param("assignedGroup") String assignedGroup, @Param("externalSourceID") int externalSourceID, @Param("actualWoName") String actualWoName,@Param("comments") String comments, @Param("uploadedBy") String uploadedBy
			);

	public List<Map<String, Object>> getActivitySubAcitvityMapping(@Param("sourceID") int sourceID);

	public Map<String, Object> getProjectIDExternalProject(@Param("activityId") int activityId, @Param("externalSourceID") int externalSourceID);

	public List<Map<String, Object>> getActiveExternalActivityList(
			@Param("sourceID") String sourceID, @Param("projectID") int projectID,@Param("externalProjectID") int externalProjectID,@Param("workPlanTemplateName")  String workPlanTemplateName);

	public void getActiveExternalActivityList(@Param("executionPlanModel") ExecutionPlanModel executionPlanModel);

	public void updateExecutionPlanWoCreation(@Param("executionPlanModel") ExecutionPlanModel executionPlanModel);

	public String getExistingExecutionPlanID(@Param("activityName")  String activityName,@Param("isfProjectID")  int isfProjectID,@Param("workPlanName") String workPlanName,@Param("parentProjectId")  int parentProjectId,@Param("externalSourceID") int externalSourceID);

	public void insertBlkWoCreationHistory(@Param("erisiteDataModel") ErisiteDataModel erisiteDataModel);

	public Map<String, Object> getNodeNamesByPlanID(@Param("parentWorkPlanId")  String parentWorkPlanId,@Param("externalSourceID") int externalSourceID);

	public String getExistingAssignToByProject(@Param("isfProjectID") int isfProjectID, @Param("externalSourceID") int externalSourceID,@Param("externalProjectID") int externalProjectID);

	public Map<String, Object> checkIfPredecessorsExists(@Param("activityName") String activityName,@Param("status") String status,
			@Param("parentWorkPlanId") String parentWorkPlanId,@Param("externalProjectID") int externalProjectID,
			@Param("templateName") String templateName, @Param("externalSourceID") int externalSourceID,@Param("isfProjectID") int isfProjectID);

	public String checkIfWOExists(@Param("WOCreationID") long wOCreationID);

	public void updateForcastWoCreation(@Param("erisiteDataModel") ErisiteDataModel erisiteDataModel,
			@Param("hour1") long hour1, @Param("status") String status, @Param("externalSourceID") int externalSourceID,
			@Param("isfProjectID") int isfProjectID, @Param("executionPlanID") int executionPlanID);

	public void updateForcastWorkOrder(@Param("erisiteDataModel") ErisiteDataModel erisiteDataModel,
			@Param("woName") String woName);

	public void updateWorkOrderStatus(@Param("woName") String woName,@Param("erisiteDataModel") ErisiteDataModel erisiteDataModel,@Param("externalSourceID") int externalSourceID);

	public Map<String, Object> checkIsfSubactivityMapping(@Param("activityName") String activityName,
			@Param("parentProjectId") int parentProjectId, @Param("externalSourceID") int externalSourceID);

	public int getExternalSourceID(@Param("source_system_id") String source_system_id);

	public Map<String, Object> getExternalGroup(@Param("planSourceid") String planSourceid);

	public List<Map<String, Object>> getAllExternalProjectIDByIsfProject(@Param("isfProjectID")  int isfProjectID,@Param("sourceID")  int sourceID);

	public List<Map<String, Object>> getExternalProjectIDByIsfProject(@Param("isfProjectID")  int isfProjectID,@Param("sourceID")  int sourceID);

	public List<Map<String, Object>> getExternalProjectIDByIsfProject(@Param("sourceID")  String sourceID);

	public void insertActivityConfig(@Param("executionPlanModel") ExecutionPlanModel executionPlanModel);

	public List<Map<String, Object>> getAllExternalActivityList(@Param("sourceID") String sourceID);

	public Map<String, Object> getActivityConfig(@Param("executionPlanModel") ExecutionPlanModel executionPlanModel);

	public void updateActivityConfig(@Param("executionPlanModel") ExecutionPlanModel executionPlanModel);

	public void updateWoNameWoCreation(@Param("erisiteDataModel") ErisiteDataModel erisiteDataModel,@Param("hour1") long hour1, @Param("status") String status,@Param("externalSourceID") int externalSourceID,@Param("actualWoName") String actualWoName,@Param("tableName") String tableName,@Param("comments") String comments);

	public void updateWorkOrderName(@Param("woName") String woName,@Param("erisiteDataModel") ErisiteDataModel erisiteDataModel,@Param("externalSourceID") int externalSourceID,
			@Param("actualWoName") String actualWoName);

	public void updateWorkOrderComments(@Param("actualName") String actualName,@Param("comments") String comments);

	public Map<String, Object> getWoStatusByWoName(@Param("woName") String woName);

	public boolean dbFileUploadForSAP(@Param("dataFile") byte[] dataFile,@Param("fileName") String fileName,@Param("mimeType") String mimeType);

	public List<HashMap<String, Object>> downloadWorkInstructionFile(@Param("fileName") String fileName);

	public void dbFileInsertForSAPBO(@Param("fileTable") String filePath,@Param("instance") String instance,@Param("columnCount") int columnCount);

	public List<Map<String, Object>> getActiveExternalWorkPlanTemplateList(@Param("sourceID") String sourceID, @Param("externalProjectID") int externalProjectID);

	public String getExistingExecutionPlanIDWithoutTemplate(@Param("activityName")  String activityName,@Param("isfProjectID")  int isfProjectID,@Param("workPlanName") String workPlanName,@Param("parentProjectId")  int parentProjectId,@Param("externalSourceID") int externalSourceID);

	public List<Map<String,Object>> getAllExternalSource();
	public List<Map<String,Object>> getAllExternalSourceErrorDictionary();
	
	public List<String> getAllowedApiListForExternalSource(@Param("externalSourceName") String externalSourceName);

	public int getNotClosedWOsByName(@Param("woName") String woName);

	public void updateWoName(@Param("signumId") String signumId, @Param("projectId")int projectId, @Param("executionPlanId") int executionPlanId, @Param("woNameRegex") String woNameRegex,@Param("oldExecutionPlanId") int oldExecutionPlanId);

	public boolean validateTaskDetails(@Param("taskID") Integer taskID,@Param("flowchartdefid") int flowchartdefid,@Param("wfVersion") int wfVersion);

	public boolean checkExistingStepStarted(@Param("wOID") Integer wOID,@Param("taskID") Integer taskID,@Param("stepID") String stepID);

	public List<String> getValidateJsonForApi(@Param("apiName") String apiName,@Param("externalSourceName") String externalSourceName);

	public Map<String, Object> getProjectIDExternalProjectV1(@Param("parentProjectId") int parentProjectId, @Param("parentWorkPlanTemplateName") String parentWorkPlanTemplateName,
			@Param("activityName") 	String activityName);

	public Map<String, Object> getProjectIDExternalProjectFromScope(@Param("parentProjectId") int parentProjectId, @Param("parentWorkPlanTemplateName") String parentWorkPlanTemplateName,
			@Param("activityName") 	String activityName);

	public void updateExecutionPlanBulkWoCreation(@Param("woCreationID")long woCreationID);
	public List<ViewNetworkElementModel> getNetworkElement(@Param("networkElement") NetworkElementNewModel networkElement);

	public boolean isCountryExist(@Param("country") String country);

	public boolean isCustomerExist(@Param("customer") String customer);

	
	public List<Map<String, Object>> checkIfExistsInCreation(@Param("activityName") String activityName,
			@Param("parentWorkPlanId") String parentWorkPlanId,@Param("externalProjectID") int externalProjectID,
			@Param("templateName") String templateName, @Param("externalSourceID") int externalSourceID,@Param("isfProjectID") int isfProjectID);

	public boolean checkIfExternalProjectActive(@Param("parentProjectId") int parentProjectId, @Param("externalSourceID") int externalSourceID);
	
	
	
	
	/* 
	 * Feed Related Mapper methods*/
	public List<Map<String, Object>> getSubscribeIsfFeeds(@Param("columns") String columns,@Param("projectid") String ProjectId,
			@Param("order_by") String orderBy,@Param("event_start_date") String startDate,@Param("event_end_date") String endDate);
	public Boolean validationMarketArea(@Param("marketArea") String marketArea);
	public List<String> getCalculativeColums(@Param("blankAlias") String blankAlias,@Param("alias") String alias);
	public List<Map<String, String>> validateAlias(@Param("alias") String alias);
	public List<Map<String, String>> validateProjectIdFeed(@Param("projectIds") String projectId);
	public String getOrderByColumn(@Param("alias") String alias);
	public List<String> getAlias(@Param("blankAlias") String blankAlias,@Param("alias") String alias);
	public List<Map<String, Integer>> validateMarketAreaProjectId(@Param("projectIds") String projectIds,@Param("marketArea") String marketArea);
	
}
