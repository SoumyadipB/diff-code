package com.ericsson.isf.model;



public class ChildStepDetailsModel {

	private String stepID ;
	private String stepName ;
	private String taskID ;
	private String labelName ;
	private String executionType ;
	private String stepType;
	
	private int isRunOnServer;
	private boolean isInputRequired;
	
	private boolean IsStepAutoSenseEnabled;
	private boolean startRule;
	private boolean stopRule;
	
	private String taskActionName;
	private String stepRpaId;
	private String bookingID;
	private String bookingStatus;
	private String botType;
	private String outputUpload;
	
	public String getOutputUpload() {
		return outputUpload;
	}
	public void setOutputUpload(String outputUpload) {
		this.outputUpload = outputUpload;
	}
	public String getBotType() {
		return botType;
	}
	public void setBotType(String botType) {
		this.botType = botType;
	}
	public String getStepRpaId() {
		return stepRpaId;
	}
	public void setStepRpaId(String stepRpaId) {
		this.stepRpaId = stepRpaId;
	}
	public String getBookingID() {
		return bookingID;
	}
	public void setBookingID(String bookingID) {
		this.bookingID = bookingID;
	}
	public String getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	private ProficiencyTypeModal proficiencyType;
	
	public ProficiencyTypeModal getProficiencyType() {
		return proficiencyType;
	}
	public void setProficiencyType(ProficiencyTypeModal proficiencyType) {
		this.proficiencyType = proficiencyType;
	}
	public boolean isInputRequired() {
		return isInputRequired;
	}
	public void setInputRequired(boolean isInputRequired) {
		this.isInputRequired = isInputRequired;
	}
	public String getTaskActionName() {
		return taskActionName;
	}
	public void setTaskActionName(String taskActionName) {
		this.taskActionName = taskActionName;
	}
	public int getIsRunOnServer() {
		return isRunOnServer;
	}
	public void setIsRunOnServer(int isRunOnServer) {
		this.isRunOnServer = isRunOnServer;
	}
	public boolean isIsStepAutoSenseEnabled() {
		return IsStepAutoSenseEnabled;
	}
	public void setIsStepAutoSenseEnabled(boolean isStepAutoSenseEnabled) {
		IsStepAutoSenseEnabled = isStepAutoSenseEnabled;
	}
	public boolean isStartRule() {
		return startRule;
	}
	public void setStartRule(boolean startRule) {
		this.startRule = startRule;
	}
	public boolean isStopRule() {
		return stopRule;
	}
	public void setStopRule(boolean stopRule) {
		this.stopRule = stopRule;
	}
	public String getStepType() {
		return stepType;
	}
	public void setStepType(String stepType) {
		this.stepType = stepType;
	}
	public String getExecutionType() {
		return executionType;
	}
	public void setExecutionType(String executionType) {
		this.executionType = executionType;
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
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	
}
