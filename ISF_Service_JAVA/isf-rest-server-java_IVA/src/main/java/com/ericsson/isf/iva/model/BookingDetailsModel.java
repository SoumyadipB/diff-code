/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.iva.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author EDHHKLU
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingDetailsModel {
	int bookingID;
	int woId;
	int taskId;
	Date startDate;
	Date endDate;
	float hours;
	Integer parentBookingDetailsId;
	String type;
	String status;
	String signumId;
	String reason;
	String outputLink;
	int parallelcount;
	int referenceId;
	int flowChartDefID;
	Integer sourceid;
	String effort;
	

	public void setEffort(String effort) {
		this.effort = effort;
	}
	public String getEffort() {
		return effort;
	}
	
	public Integer getSourceid() {
		return sourceid;
	}
	public void setSourceid(Integer sourceid) {
		this.sourceid = sourceid;
	}
	String flowChartStepID;
	String executionType;

	public String getExecutionType() {
		return executionType;
	}
	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}
	public int getFlowChartDefID() {
		return flowChartDefID;
	}
	public void setFlowChartDefID(int flowChartDefID) {
		this.flowChartDefID = flowChartDefID;
	}
	public String getFlowChartStepID() {
		return flowChartStepID;
	}
	public void setFlowChartStepID(String flowChartStepID) {
		this.flowChartStepID = flowChartStepID;
	}

	public int getBookingID() {
		return bookingID;
	}
	public void setBookingID(int bookingID) {
		this.bookingID = bookingID;
	}
	public int getWoId() {
		return woId;
	}
	public void setWoId(int woId) {
		this.woId = woId;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public float getHours() {
		return hours;
	}
	public void setHours(float hours) {
		this.hours = hours;
	}




	public Integer getParentBookingDetailsId() {
		return parentBookingDetailsId;
	}
	public void setParentBookingDetailsId(Integer parentBookingDetailsId) {
		this.parentBookingDetailsId = parentBookingDetailsId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSignumId() {
		return signumId;
	}
	public void setSignumId(String signumId) {
		this.signumId = signumId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getOutputLink() {
		return outputLink;
	}
	public void setOutputLink(String outputLink) {
		this.outputLink = outputLink;
	}
	public int getParallelcount() {
		return parallelcount;
	}
	public void setParallelcount(int parallelcount) {
		this.parallelcount = parallelcount;
	}
	public int getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(int referenceId) {
		this.referenceId = referenceId;
	}
	@Override
	public String toString() {
		return "BookingDetailsModel [bookingID=" + bookingID + ", woId=" + woId + ", taskId=" + taskId + ", startDate="
				+ startDate + ", endDate=" + endDate + ", hours=" + hours + ", parentBookingDetailsId="
				+ parentBookingDetailsId + ", type=" + type + ", status=" + status + ", signumId=" + signumId
				+ ", reason=" + reason + ", outputLink=" + outputLink + ", parallelcount=" + parallelcount
				+ ", referenceId=" + referenceId + ", flowChartDefID=" + flowChartDefID + ", sourceid=" + sourceid
				+ ", effort=" + effort + ", flowChartStepID=" + flowChartStepID + ", executionType=" + executionType
				+ "]";
	}



}
