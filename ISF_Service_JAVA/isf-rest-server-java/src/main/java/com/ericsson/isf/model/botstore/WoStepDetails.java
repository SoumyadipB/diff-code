package com.ericsson.isf.model.botstore;

public class WoStepDetails {
    private int woID;
    private int taskID;
    private String stepID;
    private int flowChartDefID;
    private String signumID;
    private String decisionValue;
    private String executionType;
    private int flowChartStepID;
    private int bookingID;
    private String bookingType;

    public int getWoID() {
		return woID;
	}
	public void setWoID(int woID) {
		this.woID = woID;
	}
	public int getTaskID() {
		return taskID;
	}
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
	
	public String getStepID() {
		return stepID;
	}
	public void setStepID(String stepID) {
		this.stepID = stepID;
	}
	public int getFlowChartDefID() {
		return flowChartDefID;
	}
	public void setFlowChartDefID(int flowChartDefID) {
		this.flowChartDefID = flowChartDefID;
	}
	public String getSignumID() {
		return signumID;
	}
	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}
	public String getDecisionValue() {
		return decisionValue;
	}
	public void setDecisionValue(String decisionValue) {
		this.decisionValue = decisionValue;
	}
	public String getExecutionType() {
		return executionType;
	}
	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}
	public int getFlowChartStepID() {
		return flowChartStepID;
	}
	public void setFlowChartStepID(int flowChartStepID) {
		this.flowChartStepID = flowChartStepID;
	}
	public int getBookingID() {
		return bookingID;
	}
	public void setBookingID(int bookingID) {
		this.bookingID = bookingID;
	}
	public String getBookingType() {
		return bookingType;
	}
	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}
    
}
