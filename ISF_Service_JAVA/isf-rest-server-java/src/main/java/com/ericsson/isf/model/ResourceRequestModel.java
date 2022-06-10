package com.ericsson.isf.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ResourceRequestModel {

	private int resourceRequestID;
	private String resourceType;
	private String requestType;
	private int onsiteCount;
	private int remoteCount	;
	private String createdBy;
	
	private String createdOn;
	private String lastModifiedBy;
	
	
	private String lastModifiedOn;
	private int onsiteLocationID;
	private int projectID;
	private int projectScopeDetailID;
	private boolean active;
	
	private int jobRoleID;
	private int jobStageID;
	
	private String remoteLocation;
	private String[] vendors;
	

	public String[] getVendors() {
		return vendors;
	}
	public void setVendors(String[] vendors) {
		this.vendors = vendors;
	}
	public String getRemoteLocation() {
		return remoteLocation;
	}
	public void setRemoteLocation(String remoteLocation) {
		this.remoteLocation = remoteLocation;
	}
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
	public int getDomainID() {
		return domainID;
	}
	public void setDomainID(int domainID) {
		this.domainID = domainID;
	}
	public int getServiceAreaID() {
		return serviceAreaID;
	}
	public void setServiceAreaID(int serviceAreaID) {
		this.serviceAreaID = serviceAreaID;
	}
	public int getTechnologyID() {
		return technologyID;
	}
	public void setTechnologyID(int technologyID) {
		this.technologyID = technologyID;
	}

	private String jobRoleName;
	private String jobStageName;
	
	//CompetenceLevel	CompetenceName	CompetenceType	CertificateName	CertificateID	CertificateType
	
	//private String competenceLevel;
	//private String competenceName;
	//private String competenceType;
	//private String certificateName;
	//private String certificateID;
	//private String certificateType;
	private String deliveryLocation;
	
	private String domain;
	private String serviceArea;
	private String technology;
	private String subDomain;
	private String subServiceArea;
	//private int serviceAreaID;
	private int subServiceAreaId;
	
	
	
	public int getSubServiceAreaId() {
		return subServiceAreaId;
	}
	public void setSubServiceAreaId(int subServiceAreaId) {
		this.subServiceAreaId = subServiceAreaId;
	}

	private int domainID;
	private int serviceAreaID;
	private int technologyID;
	private double ftePercent;
	public double getFtePercent() {
		return ftePercent;
	}
	public void setFtePercent(double ftePercent) {
		this.ftePercent = ftePercent;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getServiceArea() {
		return serviceArea;
	}
	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public String getSubDomain() {
		return subDomain;
	}
	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}
	public String getSubServiceArea() {
		return subServiceArea;
	}
	public void setSubServiceArea(String subServiceArea) {
		this.subServiceArea = subServiceArea;
	}

	//List<HashMap<String,Object>> competenceList;
	List<CompetenceSubModel> competenceList;
	List<HashMap<String,Object>> certificateList;
	
	
	

	public List<HashMap<String, Object>> getCertificateList() {
		return certificateList;
	}
	public void setCertificateList(List<HashMap<String, Object>> certificateList) {
		this.certificateList = certificateList;
	}

	List<ResourcePositionWorkEffortModel> rpefList;
	
	
	
	public List<CompetenceSubModel> getCompetenceList() {
		return competenceList;
	}
	public void setCompetenceList(List<CompetenceSubModel> competenceList) {
		this.competenceList = competenceList;
	}
	/*public List<HashMap<String, Object>> getCompetenceList() {
		return competenceList;
	}
	public void setCompetenceList(List<HashMap<String, Object>> competenceList) {
		this.competenceList = competenceList;
	}*/
	//resourceRequestID	resourceType	requestType	onsiteCount	remoteCount	createdBy	createdOn	lastModifiedBy	lastModifiedOn	jobRoleName	jobStageName	onsiteLocationID	projectID	projectScopeDetailID	active	CompetenceLevel	CompetenceName	CompetenceType	CertificateName	CertificateID	CertificateType	Duration	hoursPerDay	FTE_Percent	issuedBy	DeliveryLocation
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
	public String getJobRoleName() {
		return jobRoleName;
	}
	public void setJobRoleName(String jobRoleName) {
		this.jobRoleName = jobRoleName;
	}
	public String getJobStageName() {
		return jobStageName;
	}
	public void setJobStageName(String jobStageName) {
		this.jobStageName = jobStageName;
	}
	/*public String getCompetenceName() {
		return competenceName;
	}
	public void setCompetenceName(String competenceName) {
		this.competenceName = competenceName;
	}
	public String getCompetenceType() {
		return competenceType;
	}
	public void setCompetenceType(String competenceType) {
		this.competenceType = competenceType;
	}
	*/
	/*public String getCertificateName() {
		return certificateName;
	}
	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}
	public String getCertificateID() {
		return certificateID;
	}
	public void setCertificateID(String certificateID) {
		this.certificateID = certificateID;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}*/
	public String getDeliveryLocation() {
		return deliveryLocation;
	}
	public void setDeliveryLocation(String deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
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
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getLastModifiedOn() {
		return lastModifiedOn;
	}
	public void setLastModifiedOn(String lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}
	
	public int getOnsiteLocationID() {
		return onsiteLocationID;
	}
	public void setOnsiteLocationID(int onsiteLocationID) {
		this.onsiteLocationID = onsiteLocationID;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public int getProjectScopeDetailID() {
		return projectScopeDetailID;
	}
	public void setProjectScopeDetailID(int projectScopeDetailID) {
		this.projectScopeDetailID = projectScopeDetailID;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public List<ResourcePositionWorkEffortModel> getRpefList() {
		return rpefList;
	}
	public void setRpefList(List<ResourcePositionWorkEffortModel> rpefList) {
		this.rpefList = rpefList;
	}	
	
}
