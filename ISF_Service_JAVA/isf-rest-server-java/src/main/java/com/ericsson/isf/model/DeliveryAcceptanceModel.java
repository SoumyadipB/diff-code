package com.ericsson.isf.model;

public class DeliveryAcceptanceModel {
  
	
	private int deliveryAcceptanceID;
	private int projectID;
	private String signumID;
	private boolean active;
	private String createdBy;
	private String createdDate;
	private String lastModifiedBy;
	private String lastModifiedDate;
	
	
	public int getDeliveryAcceptanceID() {
		return deliveryAcceptanceID;
	}
	public void setDeliveryAcceptanceID(int deliveryAcceptanceID) {
		this.deliveryAcceptanceID = deliveryAcceptanceID;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getSignumID() {
		return signumID;
	}
	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	
}
