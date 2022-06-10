/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.ExternalInterfaceManagmentMapper;
import com.ericsson.isf.model.ErisiteDataModel;
import com.ericsson.isf.model.ExecutionPlanModel;
import com.ericsson.isf.model.NetworkElementNewModel;
import com.ericsson.isf.model.ViewNetworkElementModel;


/**
 *
 * @author esanpup
 */
@Repository
public class ExternalInterfaceManagmentDAO {
    
    @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;

	public List<Map<String, Object>> checkIsfHealth() {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
        return erisiteManagmentMapper.checkIsfHealth();
	}

	public boolean generateWOrkOrder(ErisiteDataModel erisiteDataModel, int externalSourceID, int isfProjectId) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.generateWOrkOrder(erisiteDataModel, externalSourceID, isfProjectId);
	}

	public List<Map<String, Object>> getExternalProjectID(int sourceID) {
		// TODO Auto-generated method stub
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getExternalProjectID(sourceID);
	}

	public List<Map<String, Object>> getActiveExternalProjectID(String source, int projectID) {
		// TODO Auto-generated method stub
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getActiveExternalProjectID(source, projectID);
	}

	public boolean insertBlkWoCreation(ErisiteDataModel erisiteDataModel, String tablename, long hour, int subActivity, int isfProjectID, int executionPlanID, String nodeName, String assignTo, String source_system_id, String message_batch_id, String transaction_id, String businessentityid, String assignedGroup, int externalSourceID, String actualWoName, String comments, String uploadedBy) {
		
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.insertBlkWoCreation(erisiteDataModel, tablename, hour, subActivity, isfProjectID, executionPlanID, nodeName, assignTo,  source_system_id, message_batch_id,transaction_id,businessentityid,assignedGroup,externalSourceID, actualWoName, comments, uploadedBy);
			
	}

	public List<Map<String, Object>> getActivitySubAcitvityMapping(int sourceID) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getActivitySubAcitvityMapping(sourceID);
		
	}

	public Map<String, Object> getProjectIDExternalProject(int activityId, int externalSourceID) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getProjectIDExternalProject(activityId, externalSourceID);
	}

	public List<Map<String, Object>> getActiveExternalActivityList(
			String sourceID, int projectID, int externalProjectID, String workPlanTemplateName) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getActiveExternalActivityList(sourceID, projectID, externalProjectID, workPlanTemplateName);
	}

	public void updateExecutionPlanWoCreation(ExecutionPlanModel executionPlanModel) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		erisiteManagmentMapper.updateExecutionPlanWoCreation(executionPlanModel);
		
	}

	public String getExistingExecutionPlanID(String activityName, int isfProjectID, String workPlanName, int parentProjectId, int externalSourceID) {
		// TODO Auto-generated method stub
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getExistingExecutionPlanID(activityName, isfProjectID, workPlanName, parentProjectId, externalSourceID);
	}

	public void insertBlkWoCreationHistory(ErisiteDataModel erisiteDataModel) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		erisiteManagmentMapper.insertBlkWoCreationHistory(erisiteDataModel);
		
	}

	public Map<String, Object> getNodeNamesByPlanID(String parentWorkPlanId, int externalSourceID) {
		// TODO Auto-generated method stub
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getNodeNamesByPlanID(parentWorkPlanId, externalSourceID);
	}

	public String getExistingAssignToByProject(int isfProjectID, int externalSourceID, int externalProjectID) {
		// TODO Auto-generated method stub
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getExistingAssignToByProject(isfProjectID, externalSourceID, externalProjectID);
	}


	public Map<String, Object> checkIfPredecessorsExists(String activityName, String status,
			String parentWorkPlanId, int externalProjectID, String templateName, int externalSourceID, int isfProjectID) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.checkIfPredecessorsExists(activityName,status,parentWorkPlanId,externalProjectID,templateName,
				externalSourceID, isfProjectID);
	}

	public String checkIfWOExists(long WOCreationID) {
		// TODO Auto-generated method stub
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.checkIfWOExists(WOCreationID);
	}

	public void updateForcastWoCreation(ErisiteDataModel erisiteDataModel, long hour1, String status, int externalSourceID, int isfProjectID, int executionPlanID) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		erisiteManagmentMapper.updateForcastWoCreation(erisiteDataModel,hour1,status, externalSourceID, isfProjectID,executionPlanID);
		
	}

	public void updateForcastWorkOrder(ErisiteDataModel erisiteDataModel,
			String woName) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		erisiteManagmentMapper.updateForcastWorkOrder(erisiteDataModel, woName);
	}

	public void updateWorkOrderStatus(String woName, ErisiteDataModel erisiteDataModel, int externalSourceID) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		erisiteManagmentMapper.updateWorkOrderStatus(woName,erisiteDataModel,externalSourceID);
	}

	public Map<String, Object> checkIsfSubactivityMapping	(String activityName, int parentProjectId, int externalSourceID) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.checkIsfSubactivityMapping(activityName,parentProjectId, externalSourceID);
	}

	public int getExternalSourceID(String source_system_id) {
		// TODO Auto-generated method stub
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getExternalSourceID(source_system_id);
	}

	public Map<String, Object> getExternalGroup(String planSourceid) {
		// TODO Auto-generated method stub
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getExternalGroup(planSourceid);
	}

	public List<Map<String, Object>> getAllExternalProjectIDByIsfProject(
			int isfProjectID, int sourceID) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getAllExternalProjectIDByIsfProject(isfProjectID, sourceID);
	}

	public List<Map<String, Object>> getExternalProjectIDByIsfProject(
			int isfProjectID, int sourceID) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getExternalProjectIDByIsfProject(isfProjectID, sourceID);
	}

	public List<Map<String, Object>> getAllExternalActivityList(String sourceID) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getAllExternalActivityList(sourceID);
	}

	public void insertActivityConfig(ExecutionPlanModel executionPlanModel) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		erisiteManagmentMapper.insertActivityConfig(executionPlanModel);
		
	}

	public Map<String, Object> getActivityConfig(ExecutionPlanModel executionPlanModel) {
		// TODO Auto-generated method stub
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getActivityConfig(executionPlanModel);
	}

	public void updateActivityConfig(ExecutionPlanModel executionPlanModel) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		erisiteManagmentMapper.updateActivityConfig(executionPlanModel);		
	}

	public void updateWoNameWoCreation(ErisiteDataModel erisiteDataModel,
			long hour, String status, int externalSourceID, String actualWoName, String tableName, String comments) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		erisiteManagmentMapper.updateWoNameWoCreation(erisiteDataModel,hour,status, externalSourceID,actualWoName,tableName,comments);
	}

	public void updateWorkOrderName(String woName,
			ErisiteDataModel erisiteDataModel, int externalSourceID,
			String actualWoName) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		erisiteManagmentMapper.updateWorkOrderName(woName,erisiteDataModel,externalSourceID, actualWoName);
		
	}

	public void updateWorkOrderComments(String actualName, String comments) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		erisiteManagmentMapper.updateWorkOrderComments(actualName, comments);
		
	}

	public Map<String, Object> getWoStatusByWoName(String woName) {
		// TODO Auto-generated method stub
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getWoStatusByWoName(woName);
	}

	public boolean dbFileUploadForSAP(byte[] dataFile, String fileName, String mimeType) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.dbFileUploadForSAP(dataFile,fileName,mimeType);
	}

	public List<HashMap<String, Object>> downloadWorkInstructionFile(String fileName) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.downloadWorkInstructionFile(fileName);
	}

	public void dbFileInsertForSAPBO(String filePath, String instance, int columnCount) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		erisiteManagmentMapper.dbFileInsertForSAPBO(filePath,instance, columnCount);
	}

	public List<Map<String, Object>> getActiveExternalWorkPlanTemplateList(String sourceID, int externalProjectID) {
		// TODO Auto-generated method stub
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getActiveExternalWorkPlanTemplateList(sourceID,externalProjectID);
	}

	public String getExistingExecutionPlanIDWithoutTemplate(String activityName, int isfProjectID,
			String parentWorkPlanTemplateName, int parentProjectId, int externalSourceID) {
		// TODO Auto-generated method stub
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getExistingExecutionPlanIDWithoutTemplate(activityName, isfProjectID, parentWorkPlanTemplateName, parentProjectId, externalSourceID);
	}
	
	public List<Map<String,Object>> getAllExternalSource() {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getAllExternalSource();
	}
	public List<Map<String,Object>> getAllExternalSourceErrorDictionary() {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getAllExternalSourceErrorDictionary();
	}
	
	public List<String> getAllowedApiListForExternalSource(String externalSourceName) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getAllowedApiListForExternalSource(externalSourceName);
	}

	public int getNotClosedWOsByName(String woName) {
		// TODO Auto-generated method stub
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getNotClosedWOsByName(woName);
	}

	public void updateWoName(String signumId, int projectId, int executionPlanId, String woNameRegex, int oldExecutionPlanId) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		erisiteManagmentMapper.updateWoName(signumId, projectId, executionPlanId, woNameRegex, oldExecutionPlanId);
		
	}

	public boolean validateTaskDetails(Integer taskID, int flowchartdefid, int wfVersion) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.validateTaskDetails(taskID,flowchartdefid,wfVersion);
	}

	public boolean checkExistingStepStarted(Integer wOID, Integer taskID, String stepID) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.checkExistingStepStarted(wOID,taskID,stepID);
	}

	public List<String> getValidateJsonForApi(String apiName, String externalSourceName) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getValidateJsonForApi(apiName,externalSourceName);
	}

	public Map<String, Object> getProjectIDExternalProjectV1(int parentProjectId, String parentWorkPlanTemplateName,
			String activityName) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getProjectIDExternalProjectV1(parentProjectId, parentWorkPlanTemplateName,
				activityName);
	}
	public List<ViewNetworkElementModel> getNetworkElement(NetworkElementNewModel networkElement) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getNetworkElement(networkElement);
	}

	public boolean isCountryExist(String country) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.isCountryExist(country);
	}

	public boolean isCustomerExist(String customer) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.isCustomerExist(customer);
	}

	

	public Map<String, Object> getProjectIDExternalProjectFromScope(int parentProjectId,
			String parentWorkPlanTemplateName, String activityName) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getProjectIDExternalProjectFromScope(parentProjectId, parentWorkPlanTemplateName,
				activityName);
	}

	public void updateExecutionPlanBulkWoCreation(long woCreationID) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		erisiteManagmentMapper.updateExecutionPlanBulkWoCreation( woCreationID);
	}

	public List<Map<String, Object>> checkIfExistsInCreation(String activityName,
			String parentWorkPlanId, int externalProjectID, String templateName, int externalSourceID, int isfProjectID) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.checkIfExistsInCreation(activityName,parentWorkPlanId,externalProjectID,templateName,
				externalSourceID, isfProjectID);
	}

	public boolean checkIfExternalProjectActive(int parentProjectId, int externalSourceID) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.checkIfExternalProjectActive(parentProjectId, externalSourceID);
	}
	
	/* 
	 * Feed Related DAO methods*/
	public List<Map<String, Object>> getSubscribeIsfFeeds(String columns, String projectId, String orderBy, String startDate, String endDate){
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getSubscribeIsfFeeds(columns, projectId, orderBy, startDate, endDate);
	}
	public Boolean validateMarketArea(String marketArea) {
        ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
        return erisiteManagmentMapper.validationMarketArea(marketArea);
    }
	public List<String> getCalculativeColums(String blankAlias,String alias) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getCalculativeColums(blankAlias,alias);
	}

	public List<Map<String, String>> validateAlias(String alias) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.validateAlias(alias);
	}
	public List<Map<String, String>> validateProjectIdFeed(String projectId) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.validateProjectIdFeed(projectId);
	}

	public String getOrderByColumn(String alias) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getOrderByColumn(alias);
	}
	public List<String> getAlias(String blankAlias,String alias) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.getAlias(blankAlias,alias);
	}
	public List<Map<String, Integer>> validateMarketAreaProjectId(String projectId,String marketArea) {
		ExternalInterfaceManagmentMapper erisiteManagmentMapper = sqlSession.getMapper(ExternalInterfaceManagmentMapper.class);
		return erisiteManagmentMapper.validateMarketAreaProjectId(projectId,marketArea);
	}
	
}
