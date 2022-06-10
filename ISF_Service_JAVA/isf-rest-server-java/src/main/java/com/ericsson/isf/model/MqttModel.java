package com.ericsson.isf.model;

public class MqttModel {
	private String signum;
	private String successLink;
	private String failureLink;
	private int successCount;
	private int failureCount;
	private String expiryTime;
	private String currentStatus;
	private String source;
	private int neUploadedId;
	private String origionalFileName;
	private int projectID;
	private String startTime;
	
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public int getNeUploadedId() {
		return neUploadedId;
	}
	public void setNeUploadedId(int neUploadedId) {
		this.neUploadedId = neUploadedId;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public String getSuccessLink() {
		return successLink;
	}
	public void setSuccessLink(String successLink) {
		this.successLink = successLink;
	}
	public String getFailureLink() {
		return failureLink;
	}
	public void setFailureLink(String failureLink) {
		this.failureLink = failureLink;
	}
	public int getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	public int getFailureCount() {
		return failureCount;
	}
	public void setFailureCount(int failureCount) {
		this.failureCount = failureCount;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getOrigionalFileName() {
		return origionalFileName;
	}
	public void setOrigionalFileName(String origionalFileName) {
		this.origionalFileName = origionalFileName;
	}
	public String getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	@Override
	public String toString() {
		return "MqttModel [signum=" + signum + ", successLink=" + successLink + ", failureLink=" + failureLink
				+ ", successCount=" + successCount + ", failureCount=" + failureCount + ", expiryTime=" + expiryTime
				+ ", currentStatus=" + currentStatus + ", source=" + source + ", neUploadedId=" + neUploadedId
				+ ", origionalFileName=" + origionalFileName + ", projectID=" + projectID + ", startTime=" + startTime
				+ "]";
	}
	
	
	
}
