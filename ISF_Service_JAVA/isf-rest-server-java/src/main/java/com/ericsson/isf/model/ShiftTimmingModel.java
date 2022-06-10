package com.ericsson.isf.model;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class ShiftTimmingModel {


	private int shiftID;
	private List<String> signum;
	public List<String> getSignum() {
		return signum;
	}
	public void setSignum(List<String> signum) {
		this.signum = signum;
	}
	private String createdBy;
	private Time shiftStartTime;
	private Time shiftEndTime;
	private Time shiftISTStartTime;
	private Time shiftISTEndTime;
	private Date createdOn;
	private int isActive;
	private String timeZone;
	private String status;
	private String startDate;
	private String shiftISTStartDate;
	private String shiftISTEndDate;
	public String getShiftISTStartDate() {
		return shiftISTStartDate;
	}
	public void setShiftISTStartDate(String shiftISTStartDate) {
		this.shiftISTStartDate = shiftISTStartDate;
	}
	public String getShiftISTEndDate() {
		return shiftISTEndDate;
	}
	public void setShiftISTEndDate(String shiftISTEndDate) {
		this.shiftISTEndDate = shiftISTEndDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	private String endDate;
	private String week;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getShiftID() {
		return shiftID;
	}
	public void setShiftID(int shiftID) {
		this.shiftID = shiftID;
	}

		public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Time getShiftStartTime() {
		return shiftStartTime;
	}
	public void setShiftStartTime(Time shiftStartTime) {
		this.shiftStartTime = shiftStartTime;
	}
	public Time getShiftEndTime() {
		return shiftEndTime;
	}
	public void setShiftEndTime(Time shiftEndTime) {
		this.shiftEndTime = shiftEndTime;
	}
	public Time getShiftISTStartTime() {
		return shiftISTStartTime;
	}
	public void setShiftISTStartTime(Time shiftISTStartTime) {
		this.shiftISTStartTime = shiftISTStartTime;
	}
	public Time getShiftISTEndTime() {
		return shiftISTEndTime;
	}
	public void setShiftISTEndTime(Time shiftISTEndTime) {
		this.shiftISTEndTime = shiftISTEndTime;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

}
