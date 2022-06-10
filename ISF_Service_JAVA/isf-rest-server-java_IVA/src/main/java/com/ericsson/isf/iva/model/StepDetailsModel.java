package com.ericsson.isf.iva.model;

import java.util.Date;

public class StepDetailsModel {
	
	int bookingId;
    int woId;
    int flowChartDefID;
    Date bookedOn;
    Integer taskID;
    String status;
    String decisionValue;
    String signumId;
    String executionType;
    int wOFCStepDetailsID;
    String flowChartStepId;
	public int getBookingId() {
		return bookingId;
	}
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	public int getWoId() {
		return woId;
	}
	public void setWoId(int woId) {
		this.woId = woId;
	}
	public int getFlowChartDefID() {
		return flowChartDefID;
	}
	public void setFlowChartDefID(int flowChartDefID) {
		this.flowChartDefID = flowChartDefID;
	}
	public Date getBookedOn() {
		return bookedOn;
	}
	public void setBookedOn(Date bookedOn) {
		this.bookedOn = bookedOn;
	}
	public Integer getTaskID() {
		return taskID;
	}
	public void setTaskID(Integer taskID) {
		this.taskID = taskID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDecisionValue() {
		return decisionValue;
	}
	public void setDecisionValue(String decisionValue) {
		this.decisionValue = decisionValue;
	}
	public String getSignumId() {
		return signumId;
	}
	public void setSignumId(String signumId) {
		this.signumId = signumId;
	}
	public String getExecutionType() {
		return executionType;
	}
	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}
	public int getwOFCStepDetailsID() {
		return wOFCStepDetailsID;
	}
	public void setwOFCStepDetailsID(int wOFCStepDetailsID) {
		this.wOFCStepDetailsID = wOFCStepDetailsID;
	}
	public String getFlowChartStepId() {
		return flowChartStepId;
	}
	public void setFlowChartStepId(String flowChartStepId) {
		this.flowChartStepId = flowChartStepId;
	}  
}
