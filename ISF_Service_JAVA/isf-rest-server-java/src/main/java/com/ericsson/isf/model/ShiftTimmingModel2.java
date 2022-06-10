package com.ericsson.isf.model;

import java.sql.Time;
import java.util.Date;

public class ShiftTimmingModel2 {
	private int shiftID;
	private String signum;
	
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
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
	private String preferredTimeZone;
	private String startWeek;
	private String endWeek;
	
	public String getStart_IstStart_Date() {
		return start_IstStart_Date;
	}
	public void setStart_IstStart_Date(String start_IstStart_Date) {
		this.start_IstStart_Date = start_IstStart_Date;
	}
	public String getEnd_IstEnd_Date() {
		return end_IstEnd_Date;
	}
	public void setEnd_IstEnd_Date(String end_IstEnd_Date) {
		this.end_IstEnd_Date = end_IstEnd_Date;
	}
	private String start_IstStart_Date;
	private String end_IstEnd_Date;
	
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
	private String shiftISTStartDate;
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
	private String shiftISTEndDate;

	private String endDate;

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
	public String getPreferredTimeZone() {
		return preferredTimeZone;
	}
	public void setPreferredTimeZone(String preferredTimeZone) {
		this.preferredTimeZone = preferredTimeZone;
	}
	public String getStartWeek() {
		return startWeek;
	}
	public void setStartWeek(String startWeek) {
		this.startWeek = startWeek;
	}
	public String getEndWeek() {
		return endWeek;
	}
	public void setEndWeek(String endWeek) {
		this.endWeek = endWeek;
	}
	public ShiftTimmingModel toShiftTimmingModel() {
		ShiftTimmingModel shiftTimmingModel= new ShiftTimmingModel() ;
		shiftTimmingModel.setCreatedBy(this.createdBy);
		shiftTimmingModel.setCreatedOn(this.createdOn);
		shiftTimmingModel.setShiftISTEndDate(this.shiftISTEndDate);
		shiftTimmingModel.setEndDate(this.endDate);
		shiftTimmingModel.setIsActive(this.isActive);
		shiftTimmingModel.setShiftEndTime(this.shiftEndTime);
		shiftTimmingModel.setShiftID(this.shiftID);
		shiftTimmingModel.setShiftISTStartDate(this.shiftISTStartDate);
		shiftTimmingModel.setShiftISTStartTime(this.shiftISTStartTime);
		shiftTimmingModel.setShiftStartTime(this.shiftStartTime);
		shiftTimmingModel.setStartDate(this.startDate);
		shiftTimmingModel.setStatus(this.status);
		shiftTimmingModel.setTimeZone(this.timeZone);
		shiftTimmingModel.setWeek(this.startWeek);
		shiftTimmingModel.setShiftISTEndTime(this.shiftISTEndTime);
		
		return shiftTimmingModel;
	}
	

}
