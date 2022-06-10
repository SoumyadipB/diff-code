package com.ericsson.isf.model;

public class ChangeRequestPositionModel {
	
	private int changeRequestID;
	private int resourcePositionID;
	private int resourceRequestID;
	private String signum;
	private String CR_Status;
	private String comments;
	private String actionType;
	private String positionStatus;
	private String createdBy;
	private String createdOn;
	private String lastModifiedBy;
	private String lastModifiedon;
	
	
	
	
	
	public int getChangeRequestID() {
		return changeRequestID;
	}
	public void setChangeRequestID(int changeRequestID) {
		this.changeRequestID = changeRequestID;
	}
	public int getResourcePositionID() {
		return resourcePositionID;
	}
	public void setResourcePositionID(int resourcePositionID) {
		this.resourcePositionID = resourcePositionID;
	}
	public int getResourceRequestID() {
		return resourceRequestID;
	}
	public void setResourceRequestID(int resourceRequestID) {
		this.resourceRequestID = resourceRequestID;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public String getCR_Status() {
		return CR_Status;
	}
	public void setCR_Status(String cR_Status) {
		CR_Status = cR_Status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getPositionStatus() {
		return positionStatus;
	}
	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getLastModifiedon() {
		return lastModifiedon;
	}
	public void setLastModifiedon(String lastModifiedon) {
		this.lastModifiedon = lastModifiedon;
	}
	
	
	
}
