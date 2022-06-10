/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.dao.ToolsMasterDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.ExternalActivitySubactivityModel;
import com.ericsson.isf.model.StandardToolsModel;
import com.ericsson.isf.model.ToolsModel;
import com.ericsson.isf.model.UserFeedbackModel;


/**
 *
 * @author esanpup
 */
@Service

public class ToolsMasterService {
	@Autowired
	private Environment environment;

	@Autowired
	private ToolsMasterDAO toolsMasterDAO;

	@Transactional("transactionManager")
	public void saveToolInventory(ToolsModel toolModel) {
		Boolean toolExists = this.toolsMasterDAO.checkIfToolExists(toolModel.getTool());
		if (!toolExists) {
			this.toolsMasterDAO.saveToolInventory(toolModel);
		} else {
			throw new ApplicationException(500, "Tool already Exists...!!!");
		}
	}

	@Transactional("transactionManager")
	public void updateToolInventory(ToolsModel toolModel) {
		this.toolsMasterDAO.updateToolInventory(toolModel);
	}
	
		public List<ToolsModel> getToolInventoryDetails() {
			List<ToolsModel> toolsModel = toolsMasterDAO.getToolInventoryDetails();
			return toolsModel;
	}
		
			public List<ToolsModel> getActiveToolInventoryDetails() {
				List<ToolsModel> toolsModel = toolsMasterDAO.getActiveToolInventoryDetails();
				return toolsModel;
		}

	public void deleteToolInventory(int toolID, String signumID, String activeStatus) {
		this.toolsMasterDAO.deleteToolInventory(toolID, signumID, activeStatus);
	}

	public List<ToolsModel> getToolInventoryDetailsByID(int toolID, String signumID) {
		List<ToolsModel> checkIfDetailsExists = toolsMasterDAO.getToolInventoryDetailsByID(toolID, signumID);
		return checkIfDetailsExists;
	}

	List<ToolsModel> getToolByTaskID(Integer taskID) {
		List<ToolsModel> toolsModel = toolsMasterDAO.getToolByTaskID(taskID);
		return toolsModel;
	}

	public List<StandardToolsModel> getStandardToolDetailsByID(int toolID) {
		return this.toolsMasterDAO.getStandardToolDetailsByID(toolID);
	}

	@Transactional("transactionManager")
	public void saveUserFeedback(UserFeedbackModel userFeedbackModel, MultipartFile userfeedbackfile) throws IOException {
		//String filepath;
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		if (userFeedbackModel != null)
			if (null != userfeedbackfile) {
				//AppUtil.ftpFileUpload(environment.getRequiredProperty("feedback.upload.basefolder"), "feedback" + "",
						//sdf.format(new Date()), userfeedbackfile);
				//filepath = environment.getRequiredProperty("feedback.upload.basefolder") + "/" + "feedback" + "/"
					//	+ sdf.format(new Date()) + "/" + userfeedbackfile.getOriginalFilename();
				//userFeedbackModel.setUploadFile(filepath);
				
				String fType= FilenameUtils.getExtension(userfeedbackfile.getOriginalFilename());
                 byte[] file;
				userFeedbackModel.setFileName(userfeedbackfile.getOriginalFilename());
				userFeedbackModel.setFileType(fType);
            	file=userfeedbackfile.getBytes();
            	userFeedbackModel.setDataFile(file);
			}
		this.toolsMasterDAO.saveUserFeedback(userFeedbackModel);
	}

	public List<UserFeedbackModel> getUserFeedback() {
		List<UserFeedbackModel> userFeedbackModel = toolsMasterDAO.getUserFeedback();
		return userFeedbackModel;
	}

	@Transactional("transactionManager")
	public void saveExternalActivity(ExternalActivitySubactivityModel externalActivitySubactivityModel) {
		Boolean activityExists = this.toolsMasterDAO.checkIfActivityExists(externalActivitySubactivityModel);

		if (Boolean.FALSE.equals(activityExists)) {
			this.toolsMasterDAO.saveExternalActivity(externalActivitySubactivityModel);
		} else {
			throw new ApplicationException(500, "Activity already Exists...!!!");
		}
	}

	@Transactional("transactionManager")
	public void updateExternalActivity(ExternalActivitySubactivityModel externalActivitySubactivityModel) {
		Boolean activityExists = this.toolsMasterDAO.checkIfActivityExists(externalActivitySubactivityModel);

		if (Boolean.FALSE.equals(activityExists)){
			this.toolsMasterDAO.updateExternalActivity(externalActivitySubactivityModel);
		} else {
			throw new ApplicationException(500, "Activity already Exists...!!!");
		}
	}

	public void deleteExternalActivity(int activityID) {
		this.toolsMasterDAO.deleteExternalActivity(activityID);
	}

	public List<ExternalActivitySubactivityModel> getExternalActivity() {
		List<ExternalActivitySubactivityModel> externalActivityModel = toolsMasterDAO.getExternalActivity();
		return externalActivityModel;
	}

	public List<Map<String, Object>> getToolType() {
		return toolsMasterDAO.getToolType();
	}
}
