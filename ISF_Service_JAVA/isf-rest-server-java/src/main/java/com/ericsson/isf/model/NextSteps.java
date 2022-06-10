package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NextSteps {
	
	@JsonProperty(value = "isInputRequired")
	private boolean inputRequired ;
	@JsonProperty(value = "isStepAutoSenseEnabled")
	private boolean stepAutoSenseEnabled ;
	@JsonProperty(value = "NextExecutionType")
	 private String nextExecutionType;
	 private String nextBotType ;
	 private String nextStepType;
	 private boolean startRule;
	 private boolean stopRule;
	 @JsonProperty(value = "NextStepName")
	 private String nextStepName;
	 @JsonProperty(value = "NextStepID") 
	 private String nextStepID;
	 private String bookingID;
	 private String isRunOnServer;
	 @JsonProperty(value = "NextTaskID")
	 private String nextTaskID;
	 private String bookingStatus;
	 private String nextStepRpaId;
	 
		 
	public String getNextExecutionType() {
		return nextExecutionType;
	}
	public boolean isInputRequired() {
		return inputRequired;
	}
	public void setInputRequired(boolean inputRequired) {
		this.inputRequired = inputRequired;
	}
	public boolean isStepAutoSenseEnabled() {
		return stepAutoSenseEnabled;
	}
	public void setStepAutoSenseEnabled(boolean stepAutoSenseEnabled) {
		this.stepAutoSenseEnabled = stepAutoSenseEnabled;
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
}
