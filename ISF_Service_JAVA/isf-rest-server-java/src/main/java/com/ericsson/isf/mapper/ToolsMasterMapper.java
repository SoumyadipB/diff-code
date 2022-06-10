  /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.model.ExternalActivitySubactivityModel;
import com.ericsson.isf.model.StandardToolsModel;
import com.ericsson.isf.model.ToolsModel;
import com.ericsson.isf.model.UserFeedbackModel;

/**
 *
 * @author esanpup
 */
public interface ToolsMasterMapper {

    public void saveToolInventory(@Param("toolModel") ToolsModel toolModel);

    public void updateToolInventory(@Param("toolModel") ToolsModel toolModel);

    public List<ToolsModel> getToolInventoryDetails();
    
    public List<ToolsModel> getActiveToolInventoryDetails();

    public void deleteToolInventory(@Param("toolID") int taskID, 
                                    @Param("signumID") String signumID, @Param("activeStatus") String activeStatus);

    public List<ToolsModel> getToolInventoryDetailsByID(@Param("toolID")int toolID, 
                                                        @Param("signumID") String signumID);

    public List<ToolsModel> getToolByTaskID(@Param("taskID") Integer taskID);
    
    public Boolean checkIfToolExists(@Param("tool") String tool);

    public List<StandardToolsModel> getStandardToolDetailsByID(@Param("toolID") int toolID);
    
    public int getToolsDetails(@Param("taskID") int taskID,@Param("subActivityID") int subActivityID);
    public void saveUserFeedback(@Param("userFeedbackModel") UserFeedbackModel userFeedbackModel1);
    public List<UserFeedbackModel> getUserFeedback();
    public void saveExternalActivity(@Param("externalActivitySubactivityModel") ExternalActivitySubactivityModel externalActivitySubactivityModel);
    public Boolean checkIfActivityExists(@Param("externalActivitySubactivityModel") ExternalActivitySubactivityModel externalActivitySubactivityModel);

    public void updateExternalActivity(@Param("externalActivitySubactivityModel") ExternalActivitySubactivityModel externalActivitySubactivityModel);
    public void deleteExternalActivity(@Param("activityID") int activityID);
    public List<ExternalActivitySubactivityModel> getExternalActivity();

	public List<Map<String, Object>> getToolType();
}
