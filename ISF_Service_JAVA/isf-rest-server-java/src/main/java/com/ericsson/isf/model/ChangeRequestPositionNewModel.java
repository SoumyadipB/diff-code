package com.ericsson.isf.model;

import java.util.Date;

public class ChangeRequestPositionNewModel {

	
	public int getCrID() {
		return crID;
	}
	public void setCrID(int crID) {
		this.crID = crID;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public int getWorkEffortIdExisting() {
		return workEffortIdExisting;
	}
	public void setWorkEffortIdExisting(int workEffortIdExisting) {
		this.workEffortIdExisting = workEffortIdExisting;
	}
	public int getWorkEffortIdProposed() {
		return workEffortIdProposed;
	}
	public void setWorkEffortIdProposed(int workEffortIdProposed) {
		this.workEffortIdProposed = workEffortIdProposed;
	}
	public String getActionTakenBy() {
		return actionTakenBy;
	}
	public void setActionTakenBy(String actionTakenBy) {
		this.actionTakenBy = actionTakenBy;
	}
	public Date getActionTakenon() {
		return actionTakenon;
	}
	public void setActionTakenon(Date actionTakenon) {
		this.actionTakenon = actionTakenon;
	}
	private int crID;
	private int resourcePositionID;
	private int resourceRequestID;
	private String createdBy;
	private Date createdOn;
	private String status;
	private String comments;
	private String actionType;
	private String positionStatus;
	private int workEffortIdExisting;
	private int workEffortIdProposed;
	private String actionTakenBy;
	private Date actionTakenon;
	
}
