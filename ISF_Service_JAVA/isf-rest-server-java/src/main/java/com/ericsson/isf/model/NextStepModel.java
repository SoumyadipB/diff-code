package com.ericsson.isf.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NextStepModel {
	
	 @JsonProperty(value = "isInputRequired")
	 private String inputRequired ;
	 @JsonProperty(value = "isStepAutoSenseEnabled")
	 private String stepAutoSenseEnabled ;
	 private String nextExecutionType;
	 private String nextBotType ;
	 private String nextStepType;
	 private boolean startRule;
	 private boolean stopRule;
	 private String nextStepName;
	 private String nextStepID;
	 private String bookingID;
	 private String isRunOnServer;
	 private String nextTaskID;
	 private String bookingStatus;
	 private String nextStepRpaId;
	 
	 
	 
	 private int versionNumber;
	 private String ruleStatus;
	 private int currentStepTaskId;
	 private String cascadeInput;
	 private int projectID;
	 private String outputUpload;
	 private int botid;
	 private int rpaRequestID;
	 private String botName;
	 private double currentAvgExecutionTime;
	 private double rPAExecutionTime;
	 private String botLanguage;
	 private String targetExecutionFileName;
	 private String moduleClassName;
	 private String moduleClassMethod;
	 private String parallelWOExecution;
	 private String reuseFactor;
	 private String lineOfCode;
	 private String status;
	 private String createdBY;
	 private Date createdOn;
	 private String modifiedBy;
	 private Date modifiedOn;
	 private boolean isActive;
	 private boolean isAuditPass;
	 private int referenceBotId;
	 private int systemID;
	 private int instanceID;
	 private int id;
	 private String botType;
	 

	 
	public String getInputRequired() {
		return inputRequired;
	}
	public void setInputRequired(String inputRequired) {
		this.inputRequired = inputRequired;
	}
	public String getStepAutoSenseEnabled() {
		return stepAutoSenseEnabled;
	}
	public void setStepAutoSenseEnabled(String stepAutoSenseEnabled) {
		this.stepAutoSenseEnabled = stepAutoSenseEnabled;
	}
	public String getNextExecutionType() {
		return nextExecutionType;
	}
	public void setNextExecutionType(String nextExecutionType) {
		this.nextExecutionType = nextExecutionType;
	}
	public String getNextBotType() {
		return nextBotType;
	}
	public void setNextBotType(String nextBotType) {
		this.nextBotType = nextBotType;
	}
	public String getNextStepType() {
		return nextStepType;
	}
	public void setNextStepType(String nextStepType) {
		this.nextStepType = nextStepType;
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
	public String getNextStepName() {
		return nextStepName;
	}
	public void setNextStepName(String nextStepName) {
		this.nextStepName = nextStepName;
	}
	public String getNextStepID() {
		return nextStepID;
	}
	public void setNextStepID(String nextStepID) {
		this.nextStepID = nextStepID;
	}
	public String getBookingID() {
		return bookingID;
	}
	public void setBookingID(String bookingID) {
		this.bookingID = bookingID;
	}
	public String getIsRunOnServer() {
		return isRunOnServer;
	}
	public void setIsRunOnServer(String isRunOnServer) {
		this.isRunOnServer = isRunOnServer;
	}
	public String getNextTaskID() {
		return nextTaskID;
	}
	public void setNextTaskID(String nextTaskID) {
		this.nextTaskID = nextTaskID;
	}
	public String getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	public String getNextStepRpaId() {
		return nextStepRpaId;
	}
	public void setNextStepRpaId(String nextStepRpaId) {
		this.nextStepRpaId = nextStepRpaId;
	}
	public int getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getRuleStatus() {
		return ruleStatus;
	}
	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}
	public int getCurrentStepTaskId() {
		return currentStepTaskId;
	}
	public void setCurrentStepTaskId(int currentStepTaskId) {
		this.currentStepTaskId = currentStepTaskId;
	}
	public String getCascadeInput() {
		return cascadeInput;
	}
	public void setCascadeInput(String cascadeInput) {
		this.cascadeInput = cascadeInput;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getOutputUpload() {
		return outputUpload;
	}
	public void setOutputUpload(String outputUpload) {
		this.outputUpload = outputUpload;
	}
	public int getBotid() {
		return botid;
	}
	public void setBotid(int botid) {
		this.botid = botid;
	}
	public int getRpaRequestID() {
		return rpaRequestID;
	}
	public void setRpaRequestID(int rpaRequestID) {
		this.rpaRequestID = rpaRequestID;
	}
	public String getBotName() {
		return botName;
	}
	public void setBotName(String botName) {
		this.botName = botName;
	}
	public double getCurrentAvgExecutionTime() {
		return currentAvgExecutionTime;
	}
	public void setCurrentAvgExecutionTime(double currentAvgExecutionTime) {
		this.currentAvgExecutionTime = currentAvgExecutionTime;
	}
	public double getrPAExecutionTime() {
		return rPAExecutionTime;
	}
	public void setrPAExecutionTime(double rPAExecutionTime) {
		this.rPAExecutionTime = rPAExecutionTime;
	}
	public String getBotLanguage() {
		return botLanguage;
	}
	public void setBotLanguage(String botLanguage) {
		this.botLanguage = botLanguage;
	}
	public String getTargetExecutionFileName() {
		return targetExecutionFileName;
	}
	public void setTargetExecutionFileName(String targetExecutionFileName) {
		this.targetExecutionFileName = targetExecutionFileName;
	}
	public String getModuleClassName() {
		return moduleClassName;
	}
	public void setModuleClassName(String moduleClassName) {
		this.moduleClassName = moduleClassName;
	}
	public String getModuleClassMethod() {
		return moduleClassMethod;
	}
	public void setModuleClassMethod(String moduleClassMethod) {
		this.moduleClassMethod = moduleClassMethod;
	}
	public String getParallelWOExecution() {
		return parallelWOExecution;
	}
	public void setParallelWOExecution(String parallelWOExecution) {
		this.parallelWOExecution = parallelWOExecution;
	}
	public String getReuseFactor() {
		return reuseFactor;
	}
	public void setReuseFactor(String reuseFactor) {
		this.reuseFactor = reuseFactor;
	}
	public String getLineOfCode() {
		return lineOfCode;
	}
	public void setLineOfCode(String lineOfCode) {
		this.lineOfCode = lineOfCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedBY() {
		return createdBY;
	}
	public void setCreatedBY(String createdBY) {
		this.createdBY = createdBY;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isAuditPass() {
		return isAuditPass;
	}
	public void setAuditPass(boolean isAuditPass) {
		this.isAuditPass = isAuditPass;
	}
	public int getReferenceBotId() {
		return referenceBotId;
	}
	public void setReferenceBotId(int referenceBotId) {
		this.referenceBotId = referenceBotId;
	}
	public int getSystemID() {
		return systemID;
	}
	public void setSystemID(int systemID) {
		this.systemID = systemID;
	}
	public int getInstanceID() {
		return instanceID;
	}
	public void setInstanceID(int instanceID) {
		this.instanceID = instanceID;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBotType() {
		return botType;
	}
	public void setBotType(String botType) {
		this.botType = botType;
	}
	 
	 
}
