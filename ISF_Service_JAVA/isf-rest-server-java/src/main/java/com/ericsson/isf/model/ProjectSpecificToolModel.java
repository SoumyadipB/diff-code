package com.ericsson.isf.model;

public class ProjectSpecificToolModel {
	
	private int projectToolID;
	private int projectID;
	private int toolID;
	private int licenseTypeID;
	private int licenseOwnerID;
	private int accessMethodID;
	private String toolVersion;
	private boolean active;
	private String createdBy;
	private String createdDate;
	private String lastModifiedBy;
	private String lastModifiedDate;
	public int getProjectToolID() {
		return projectToolID;
	}
	public void setProjectToolID(int projectToolID) {
		this.projectToolID = projectToolID;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public int getToolID() {
		return toolID;
	}
	public void setToolID(int toolID) {
		this.toolID = toolID;
	}
	public int getLicenseTypeID() {
		return licenseTypeID;
	}
	public void setLicenseTypeID(int licenseTypeID) {
		this.licenseTypeID = licenseTypeID;
	}
	public int getLicenseOwnerID() {
		return licenseOwnerID;
	}
	public void setLicenseOwnerID(int licenseOwnerID) {
		this.licenseOwnerID = licenseOwnerID;
	}
	public int getAccessMethodID() {
		return accessMethodID;
	}
	public void setAccessMethodID(int accessMethodID) {
		this.accessMethodID = accessMethodID;
	}
	public String getToolVersion() {
		return toolVersion;
	}
	public void setToolVersion(String toolVersion) {
		this.toolVersion = toolVersion;
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
		return "ProjectSpecificToolModel [projectToolID=" + projectToolID + ", projectID=" + projectID + ", toolID="
				+ toolID + ", licenseTypeID=" + licenseTypeID + ", licenseOwnerID=" + licenseOwnerID
				+ ", accessMethodID=" + accessMethodID + ", toolVersion=" + toolVersion + ", active=" + active
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", lastModifiedBy=" + lastModifiedBy
				+ ", lastModifiedDate=" + lastModifiedDate + "]";
	}
	
	
	
	

}
