package com.ericsson.isf.model;

public class ResourceModel {

	
	
	private int ProjectID;
	private String projectName;
	
	private int resourceRequestID;
	private String Domain;
	private String SubDomain;
	private String ServiceArea;
	private String SubServiceArea;
	private String Technology;
	private String JobStage;
	private String fte;
	private int onsiteCount;
	private int remoteCount	;
	private String startDate;
	private String endDate;
	private String Priority;
	private String PositionStatus;
	private String AllocatedResource;
	private String Manager;
	private String Signum;
	
	private String JobRole;
	private String Competence;
	private String Ceritification;
	private String Availability;
	private String SearchResources;
	private String Reset;
	
	private int jobRoleID;
	private int jobStageID;
	
	private String resourceType;
	private String requestType;
	
	private String deliveryLocation;
	private String duration;
	

	private int hours;
	public int getJobRoleID() {
		return jobRoleID;
	}
	public void setJobRoleID(int jobRoleID) {
		this.jobRoleID = jobRoleID;
	}
	public int getJobStageID() {
		return jobStageID;
	}
	public void setJobStageID(int jobStageID) {
		this.jobStageID = jobStageID;
	}
	public int getResourceRequestID() {
		return resourceRequestID;
	}
	public void setResourceRequestID(int resourceRequestID) {
		this.resourceRequestID = resourceRequestID;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
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
	public String getDeliveryLocation() {
		return deliveryLocation;
	}
	public void setDeliveryLocation(String deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
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
	public String getFte() {
		return fte;
	}
	public void setFte(String fte) {
		this.fte = fte;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	
	
	public int getProjectID() {
		return ProjectID;
	}
	public void setProjectID(int projectID) {
		ProjectID = projectID;
	}
	public String getDomain() {
		return Domain;
	}
	public void setDomain(String domain) {
		Domain = domain;
	}
	public String getSubDomain() {
		return SubDomain;
	}
	public void setSubDomain(String subDomain) {
		SubDomain = subDomain;
	}
	public String getServiceArea() {
		return ServiceArea;
	}
	public void setServiceArea(String serviceArea) {
		ServiceArea = serviceArea;
	}
	public String getSubServiceArea() {
		return SubServiceArea;
	}
	public void setSubServiceArea(String subServiceArea) {
		SubServiceArea = subServiceArea;
	}
	public String getTechnology() {
		return Technology;
	}
	public void setTechnology(String technology) {
		Technology = technology;
	}
	public String getPriority() {
		return Priority;
	}
	public void setPriority(String priority) {
		Priority = priority;
	}
	public String getPositionStatus() {
		return PositionStatus;
	}
	public void setPositionStatus(String positionStatus) {
		PositionStatus = positionStatus;
	}
	public String getAllocatedResource() {
		return AllocatedResource;
	}
	public void setAllocatedResource(String allocatedResource) {
		AllocatedResource = allocatedResource;
	}
	public String getManager() {
		return Manager;
	}
	public void setManager(String manager) {
		Manager = manager;
	}
	public String getSignum() {
		return Signum;
	}
	public void setSignum(String signum) {
		Signum = signum;
	}
	public String getJobStage() {
		return JobStage;
	}
	public void setJobStage(String jobStage) {
		JobStage = jobStage;
	}
	public String getJobRole() {
		return JobRole;
	}
	public void setJobRole(String jobRole) {
		JobRole = jobRole;
	}
	public String getCompetence() {
		return Competence;
	}
	public void setCompetence(String competence) {
		Competence = competence;
	}
	public String getCeritification() {
		return Ceritification;
	}
	public void setCeritification(String ceritification) {
		Ceritification = ceritification;
	}
	public String getAvailability() {
		return Availability;
	}
	public void setAvailability(String availability) {
		Availability = availability;
	}
	public String getSearchResources() {
		return SearchResources;
	}
	public void setSearchResources(String searchResources) {
		SearchResources = searchResources;
	}
	public String getReset() {
		return Reset;
	}
	public void setReset(String reset) {
		Reset = reset;
	}
}
