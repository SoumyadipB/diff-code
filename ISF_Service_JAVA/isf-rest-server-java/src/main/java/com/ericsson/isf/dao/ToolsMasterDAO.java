/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.ToolsMasterMapper;
import com.ericsson.isf.model.ExternalActivitySubactivityModel;
import com.ericsson.isf.model.StandardToolsModel;
import com.ericsson.isf.model.ToolsModel;
import com.ericsson.isf.model.UserFeedbackModel;

/**
 *
 * @author esanpup
 */
@Repository
public class ToolsMasterDAO {
    
    @Qualifier("sqlSession") /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;

    public void saveToolInventory(ToolsModel toolModel) {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        toolsMasterMapper.saveToolInventory(toolModel);
    }

    public void updateToolInventory(ToolsModel toolModel) {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        toolsMasterMapper.updateToolInventory(toolModel);
    }

    public List<ToolsModel> getToolInventoryDetails() {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        return toolsMasterMapper.getToolInventoryDetails();
    }
    
    public List<ToolsModel> getActiveToolInventoryDetails() {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        return toolsMasterMapper.getActiveToolInventoryDetails();
    }
    public void deleteToolInventory(int toolID, String signumID, String activeStatus) {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        toolsMasterMapper.deleteToolInventory(toolID, signumID, activeStatus);
    }

    public List<ToolsModel> getToolInventoryDetailsByID(int toolID, String signumID) {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        return toolsMasterMapper.getToolInventoryDetailsByID(toolID,signumID);
    }

    public List<ToolsModel> getToolByTaskID(Integer taskID) {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        return toolsMasterMapper.getToolByTaskID(taskID);
    }
	
	public Boolean checkIfToolExists(String tool) {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        return toolsMasterMapper.checkIfToolExists(tool);
    }

    public List<StandardToolsModel> getStandardToolDetailsByID(int toolID) {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        return toolsMasterMapper.getStandardToolDetailsByID(toolID);
    }
    
    public int getToolsDetails(int taskID, int subActivityID) {
         ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
         return toolsMasterMapper.getToolsDetails(taskID,subActivityID);
    }
    public void saveUserFeedback(UserFeedbackModel userFeedbackModel1) {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        toolsMasterMapper.saveUserFeedback(userFeedbackModel1);
    }
    public List<UserFeedbackModel> getUserFeedback() {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        return toolsMasterMapper.getUserFeedback();
    }
    public void saveExternalActivity(ExternalActivitySubactivityModel externalActivitySubactivityModel) {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        toolsMasterMapper.saveExternalActivity(externalActivitySubactivityModel);
    }
    public Boolean checkIfActivityExists(ExternalActivitySubactivityModel externalActivitySubactivityModel) {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        return toolsMasterMapper.checkIfActivityExists(externalActivitySubactivityModel);
    }

    public void updateExternalActivity(ExternalActivitySubactivityModel externalActivitySubactivityModel) {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        toolsMasterMapper.updateExternalActivity(externalActivitySubactivityModel);
    }
    public void deleteExternalActivity(int activityID) {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        toolsMasterMapper.deleteExternalActivity(activityID);
    }
    public List<ExternalActivitySubactivityModel> getExternalActivity() {
        ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        return toolsMasterMapper.getExternalActivity();
    }

	public List<Map<String, Object>> getToolType() {
		ToolsMasterMapper toolsMasterMapper = sqlSession.getMapper(ToolsMasterMapper.class);
        return toolsMasterMapper.getToolType();
	}

}
