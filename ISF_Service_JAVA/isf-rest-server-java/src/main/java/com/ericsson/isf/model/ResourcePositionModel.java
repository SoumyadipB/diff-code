package com.ericsson.isf.model;

public class ResourcePositionModel {

	private int resourcePositionID;
	private int resourceRequestID;
	private String positionStatus;
	private boolean isActive;
	private String inActivatedOn;
	private String cRStatus;
	private String requestedBy;
	private String requestedOn;
	private String lastModifiedOn;
	private String lastModifiedBy;

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

	public String getPositionStatus() {
		return positionStatus;
	}

	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getcRStatus() {
		return cRStatus;
	}

	public void setcRStatus(String cRStatus) {
		this.cRStatus = cRStatus;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getInActivatedOn() {
		return inActivatedOn;
	}

	public void setInActivatedOn(String inActivatedOn) {
		this.inActivatedOn = inActivatedOn;
	}

	public String getRequestedOn() {
		return requestedOn;
	}

	public void setRequestedOn(String requestedOn) {
		this.requestedOn = requestedOn;
	}

	public String getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(String lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

}
