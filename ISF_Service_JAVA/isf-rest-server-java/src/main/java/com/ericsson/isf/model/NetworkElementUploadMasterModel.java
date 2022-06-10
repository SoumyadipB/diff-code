package com.ericsson.isf.model;

public class NetworkElementUploadMasterModel {
	
	private int countryCustomerGroupID;
	private String userUploaded;
	private String uploadStatus;
	private String employeeName;
	private int neUploadID;
	private int passedCount;
	private int failedCount;
	private int projectID;
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	private boolean active;
	
	public int getCountryCustomerGroupID() {
		return countryCustomerGroupID;
	}
	public void setCountryCustomerGroupID(int countryCustomerGroupID) {
		this.countryCustomerGroupID = countryCustomerGroupID;
	}
	public String getUserUploaded() {
		return userUploaded;
	}
	public void setUserUploaded(String userUploaded) {
		this.userUploaded = userUploaded;
	}
	public String getUploadStatus() {
		return uploadStatus;
	}
	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public int getNeUploadID() {
		return neUploadID;
	}
	public void setNeUploadID(int neUploadID) {
		this.neUploadID = neUploadID;
	}
	public int getPassedCount() {
		return passedCount;
	}
	public void setPassedCount(int passedCount) {
		this.passedCount = passedCount;
	}
	public int getFailedCount() {
		return failedCount;
	}
	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	

}
