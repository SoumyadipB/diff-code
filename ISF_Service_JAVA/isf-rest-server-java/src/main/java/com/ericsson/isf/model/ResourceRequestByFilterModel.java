package com.ericsson.isf.model;

public class ResourceRequestByFilterModel {
	
	private int ProjectID;
	private String projectName;
	
	private int resourceRequestID;
	private String Domain;

	private String ServiceArea;

	private String Technology;
	private String JobStage;
	private String fte;
	private int onsiteCount;
	private int remoteCount	;
	private String startDate;
	private String endDate;
	
	public int getProjectID() {
		return ProjectID;
	}
	public void setProjectID(int projectID) {
		ProjectID = projectID;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getResourceRequestID() {
		return resourceRequestID;
	}
	public void setResourceRequestID(int resourceRequestID) {
		this.resourceRequestID = resourceRequestID;
	}
	public String getDomain() {
		return Domain;
	}
	public void setDomain(String domain) {
		Domain = domain;
	}
	public String getServiceArea() {
		return ServiceArea;
	}
	public void setServiceArea(String serviceArea) {
		ServiceArea = serviceArea;
	}
	public String getTechnology() {
		return Technology;
	}
	public void setTechnology(String technology) {
		Technology = technology;
	}
	public String getJobStage() {
		return JobStage;
	}
	public void setJobStage(String jobStage) {
		JobStage = jobStage;
	}
	public String getFte() {
		return fte;
	}
	public void setFte(String fte) {
		this.fte = fte;
	}
	public int getOnsiteCount() {
		return onsiteCount;
	}
	public void setOnsiteCount(int onsiteCount) {
		this.onsiteCount = onsiteCount;
	}
	public int getRemoteCount() {
		return remoteCount;
	}
	public void setRemoteCount(int remoteCount) {
		this.remoteCount = remoteCount;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
