/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 *
 * @author EDHHKLU
 */
public class StepDetailsModel {
	
	private int bookingId;
	private int woId;
	private int flowChartDefID;
	private Date bookedOn;
	private Integer taskID;
	private String status;
	private String decisionValue;
	private String signumId;
	private String executionType;
	private int wOFCStepDetailsID;
	private String flowChartStepId;
	private String refferer;
	private String stepName;
	private String stepType;
	
	private Integer sourceID;
	@Size(min = 3, message = "Invalid source name length, Length can't be less than 3")
	@NotEmpty(message = "source name can't be left blank ")
	private String externalSourceName;
    
	public String getRefferer() {
		return refferer;
	}
	public void setRefferer(String refferer) {
		this.refferer = refferer;
	}
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

	@Override
	public String toString() {
		return "StepDetailsModel [bookingId=" + bookingId + ", woId=" + woId + ", flowChartDefID=" + flowChartDefID
				+ ", bookedOn=" + bookedOn + ", taskID=" + taskID + ", status=" + status + ", decisionValue="
				+ decisionValue + ", signumId=" + signumId + ", executionType=" + executionType + ", wOFCStepDetailsID="
				+ wOFCStepDetailsID + ", flowChartStepId=" + flowChartStepId + ",sourceID=" + sourceID + ", externalSourceName=" + externalSourceName +"]";
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getStepType() {
		return stepType;
	}
	public void setStepType(String stepType) {
		this.stepType = stepType;
	}
	public Integer getSourceID() {
		return sourceID;
	}
	public void setSourceID(Integer sourceID) {
		this.sourceID = sourceID;
	}
	
	public String getExternalSourceName() {
		return externalSourceName;
	}

	public void setExternalSourceName(String externalSourceName) {
		this.externalSourceName = externalSourceName;
	}
    
}
