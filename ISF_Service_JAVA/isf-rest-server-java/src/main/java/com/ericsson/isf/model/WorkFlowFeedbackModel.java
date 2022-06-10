package com.ericsson.isf.model;

public class WorkFlowFeedbackModel {
	
	private int feedbackDetailID;
	private int projectID;
	private int workOrderID;
	private String creatorName;
	private String signum;
	private int defID;
	private String wfName;
	private int wFID;
	private String stepID;
	private String stepName;
	private String feedbackOn;
	private String feedbackType;
	private String instantFeedback;
	private String CreatedOn;
	private boolean isActive;
	private String modifiedBy;
	private String modifiedON;
	private String userRole;
	private int instantFeedbackID;
	private String feedbackLevel;
	private String signumAndName;
	
	

	public String getSignumAndName() {
		return signumAndName;
	}

	public void setSignumAndName(String signumAndName) {
		this.signumAndName = signumAndName;
	}

	public String getFeedbackLevel() {
		return feedbackLevel;
	}

	public void setFeedbackLevel(String feedbackLevel) {
		this.feedbackLevel = feedbackLevel;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedON() {
		return modifiedON;
	}

	public void setModifiedON(String modifiedON) {
		this.modifiedON = modifiedON;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	private WorkFlowFeedbackActivityModel workFlowFeedbackActivityModel;
	
	public int getFeedbackDetailID() {
		return feedbackDetailID;
	}

	public void setFeedbackDetailID(int feedbackDetailID) {
		this.feedbackDetailID = feedbackDetailID;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public int getWorkOrderID() {
		return workOrderID;
	}

	public void setWorkOrderID(int workOrderID) {
		this.workOrderID = workOrderID;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getSignum() {
		return signum;
	}

	public void setSignum(String signum) {
		this.signum = signum;
	}

	public int getDefID() {
		return defID;
	}

	public void setDefID(int defID) {
		this.defID = defID;
	}

	public String getWfName() {
		return wfName;
	}

	public void setWfName(String wfName) {
		this.wfName = wfName;
	}

	public int getwFID() {
		return wFID;
	}

	public void setwFID(int wFID) {
		this.wFID = wFID;
	}

	public String getStepID() {
		return stepID;
	}

	public void setStepID(String stepID) {
		this.stepID = stepID;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getFeedbackOn() {
		return feedbackOn;
	}

	public void setFeedbackOn(String feedbackOn) {
		this.feedbackOn = feedbackOn;
	}

	public String getFeedbackType() {
		return feedbackType;
	}

	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}

	public String getInstantFeedback() {
		return instantFeedback;
	}

	public void setInstantFeedback(String instantFeedback) {
		this.instantFeedback = instantFeedback;
	}

	public String getCreatedOn() {
		return CreatedOn;
	}

	public void setCreatedOn(String createdOn) {
		CreatedOn = createdOn;
	}

	public WorkFlowFeedbackActivityModel getWorkFlowFeedbackActivityModel() {
		return workFlowFeedbackActivityModel;
	}

	public void setWorkFlowFeedbackActivityModel(WorkFlowFeedbackActivityModel workFlowFeedbackActivityModel) {
		this.workFlowFeedbackActivityModel = workFlowFeedbackActivityModel;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public int getInstantFeedbackID() {
		return instantFeedbackID;
	}

	public void setInstantFeedbackID(int instantFeedbackID) {
		this.instantFeedbackID = instantFeedbackID;
	}
	
}
