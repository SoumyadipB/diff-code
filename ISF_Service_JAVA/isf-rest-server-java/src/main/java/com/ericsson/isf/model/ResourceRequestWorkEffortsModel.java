package com.ericsson.isf.model;

import java.util.Date;

public class ResourceRequestWorkEffortsModel {

	public int getResourceRequestWorkEffortID() {
		return resourceRequestWorkEffortID;
	}
	public void setResourceRequestWorkEffortID(int resourceRequestWorkEffortID) {
		this.resourceRequestWorkEffortID = resourceRequestWorkEffortID;
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
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	public double getFtePercent() {
		return ftePercent;
	}
	public void setFtePercent(double ftePercent) {
		this.ftePercent = ftePercent;
	}
	public double getHours() {
		return hours;
	}
	public void setHours(double hours) {
		this.hours = hours;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}
	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	private int resourceRequestWorkEffortID;
	private Date startDate;
	private Date endDate;
    private double duration;
    private double ftePercent;
    private double hours;
    private String createdBy;
    private Date createdOn;
    private String lastModifiedBy;
    private Date lastModifiedOn;
    public int getResourceRequestID() {
		return resourceRequestID;
	}
	public void setResourceRequestID(int resourceRequestID) {
		this.resourceRequestID = resourceRequestID;
	}
	public int getWorkEffortID() {
		return workEffortID;
	}
	public void setWorkEffortID(int workEffortID) {
		this.workEffortID = workEffortID;
	}
	public int getResourcePositionID() {
		return resourcePositionID;
	}
	public void setResourcePositionID(int resourcePositionID) {
		this.resourcePositionID = resourcePositionID;
	}

	private int resourceRequestID;
    private int workEffortID;
    private int resourcePositionID;
}
