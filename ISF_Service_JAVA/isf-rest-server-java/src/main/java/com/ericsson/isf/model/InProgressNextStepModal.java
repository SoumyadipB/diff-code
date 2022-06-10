package com.ericsson.isf.model;

public class InProgressNextStepModal {
	private Boolean isInputRequired;
	private Boolean isStepAutoSenseEnabled;
	private String nextExecutionType;
	private String nextBotType;
	private String nextStepType;
	private boolean startRule;
	private boolean stopRule;
	private String nextStepName;
	private String nextStepID;
	private int bookingID;
	private String isRunOnServer;
	private int nextTaskID;
	private String bookingStatus;
	private int nextStepRpaId;
	private String outputUpload;
	
	public String getOutputUpload() {
		return outputUpload;
	}
	public void setOutputUpload(String outputUpload) {
		this.outputUpload = outputUpload;
	}
	public Boolean getIsInputRequired() {
		return isInputRequired;
	}
	public void setIsInputRequired(Boolean isInputRequired) {
		this.isInputRequired = isInputRequired;
	}
	public Boolean getIsStepAutoSenseEnabled() {
		return isStepAutoSenseEnabled;
	}
	public void setIsStepAutoSenseEnabled(Boolean isStepAutoSenseEnabled) {
		this.isStepAutoSenseEnabled = isStepAutoSenseEnabled;
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
	public int getBookingID() {
		return bookingID;
	}
	public void setBookingID(int bookingID) {
		this.bookingID = bookingID;
	}
	public String getIsRunOnServer() {
		return isRunOnServer;
	}
	public void setIsRunOnServer(String isRunOnServer) {
		this.isRunOnServer = isRunOnServer;
	}
	public int getNextTaskID() {
		return nextTaskID;
	}
	public void setNextTaskID(int nextTaskID) {
		this.nextTaskID = nextTaskID;
	}
	public String getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	public int getNextStepRpaId() {
		return nextStepRpaId;
	}
	public void setNextStepRpaId(int nextStepRpaId) {
		this.nextStepRpaId = nextStepRpaId;
	}
}
