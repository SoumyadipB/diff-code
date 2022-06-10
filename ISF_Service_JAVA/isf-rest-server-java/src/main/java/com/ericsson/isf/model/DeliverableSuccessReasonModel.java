package com.ericsson.isf.model;

import java.util.Date;

public class DeliverableSuccessReasonModel {
	
	private int successReasonId;
	private int deliverableUnitId;
	private String successReason;
	private String createdOn;
	private String modifiedOn;
	private String createdBy;
	private String modifiedBy;
	private boolean isActive;
	private String encodedSuccessReason;
	
	public String getEncodedSuccessReason() {
		return encodedSuccessReason;
	}
	public void setEncodedSuccessReason(String encodedSuccessReason) {
		this.encodedSuccessReason = encodedSuccessReason;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public int getSuccessReasonId() {
		return successReasonId;
	}
	public void setSuccessReasonId(int successReasonId) {
		this.successReasonId = successReasonId;
	}
	public int getDeliverableUnitId() {
		return deliverableUnitId;
	}
	public void setDeliverableUnitId(int deliverableUnitId) {
		this.deliverableUnitId = deliverableUnitId;
	}
	public String getSuccessReason() {
		return successReason;
	}
	public void setSuccessReason(String successReason) {
		this.successReason = successReason;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getModeifiedOn() {
		return modifiedOn;
	}
	public void setModeifiedOn(String modeifiedOn) {
		this.modifiedOn = modeifiedOn;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	
	

}
