/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

import com.ericsson.isf.util.AppConstants;

/**
 *
 * @author EDHHKLU
 */
public class BookingDetailsModel {
	private int bookingID;
	private int woId;
	private int taskId;
	private Date startDate;
	private Date endDate;
	private float hours;
	private Integer parentBookingDetailsId;
	private String type;
	private String status;
	private String signumId;
	private String reason;
	private String outputLink;
	private int parallelcount;
	private int referenceId;
	private int flowChartDefID;
	private Integer sourceid;
	private String effort;
	private String decisionValue;
	private String flowChartStepID;
	private String executionType;
	private String botPlatform;
	
	public String getBotPlatform() {
		return botPlatform;
	}

	public void setBotPlatform(String botPlatform) {
		this.botPlatform = botPlatform;
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

	public void addParallelcount(int i) {
		this.setParallelcount(i);
	}

	public String getDecisionValue() {
		return decisionValue;
	}

	public void setDecisionValue(String decisionValue) {
		this.decisionValue = decisionValue;
	}
	
	public BookingDetailsModel wOID(int woId) {
		this.woId=woId;
		return this;
	}
	
	public BookingDetailsModel taskId(int taskId) {
		this.taskId=taskId;
		return this;
	}
	
	public BookingDetailsModel parentBookingDetailsId(Integer parentBookingDetailsId) {
		this.parentBookingDetailsId=parentBookingDetailsId;
		return this;
	}
	
	public BookingDetailsModel type(String type) {
		this.type=type;
		return this;
	}
	
	public BookingDetailsModel status(String status) {
		this.status=status;
		return this;
	}
	
	public BookingDetailsModel signumID(String signumID) {
		this.signumId=signumID;
		return this;
	}
	
	public BookingDetailsModel flowChartDefID(int flowChartDefID) {
		this.flowChartDefID=flowChartDefID;
		return this;
	}
	
	public BookingDetailsModel decisionValue(String decisionValue) {
		this.decisionValue=decisionValue;
		return this;
	}
	
	public BookingDetailsModel flowChartStepID(String flowChartStepID) {
		this.flowChartStepID=flowChartStepID;
		return this;
	}

	public BookingDetailsModel executionType(String executionType) {
		this.executionType=executionType;
		return this;
	}
	public BookingDetailsModel botPlatform(String botPlatform) {
		this.botPlatform=botPlatform;
		return this;
	}
	
	public BookingDetailsModel sourceId(Integer sourceId) {
		this.sourceid=sourceId;
		return this;
	}
}
