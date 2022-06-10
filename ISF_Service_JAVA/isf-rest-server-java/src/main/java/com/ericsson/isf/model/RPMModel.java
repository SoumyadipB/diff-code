package com.ericsson.isf.model;

public class RPMModel {
	
	private int RPMID;
	private int projectID;
	private String rpmSignumID;
	private boolean active;
	private String createdBy;
	private String createdDate;
	private String lastModifiedBy;
	private String lastModifiedDate;
	public int getRPMID() {
		return RPMID;
	}
	public void setRPMID(int rPMID) {
		RPMID = rPMID;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getRpmSignumID() {
		return rpmSignumID;
	}
	public void setRpmSignumID(String rpmSignumID) {
		this.rpmSignumID = rpmSignumID;
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
	@Override
	public String toString() {
		return "RPMModel [RPMID=" + RPMID + ", projectID=" + projectID + ", rpmSignumID=" + rpmSignumID + ", active="
				+ active + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", lastModifiedBy="
				+ lastModifiedBy + ", lastModifiedDate=" + lastModifiedDate + "]";
	}
	
	

}
