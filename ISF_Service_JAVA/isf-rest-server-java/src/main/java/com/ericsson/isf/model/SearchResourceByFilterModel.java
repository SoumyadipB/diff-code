package com.ericsson.isf.model;


public class SearchResourceByFilterModel {

	public int getResourceRequestID() {
		return resourceRequestID;
	}
	public void setResourceRequestID(int resourceRequestID) {
		this.resourceRequestID = resourceRequestID;
	}
	public int getDomainID() {
		return domainID;
	}
	public void setDomainID(int domainID) {
		this.domainID = domainID;
	}
	public int getSubDomainID() {
		return subDomainID;
	}
	public void setSubDomainID(int subDomainID) {
		this.subDomainID = subDomainID;
	}
	public int getSubServiceAreaID() {
		return subServiceAreaID;
	}
	public void setSubServiceAreaID(int subServiceAreaID) {
		this.subServiceAreaID = subServiceAreaID;
	}
	public int getTechnologyID() {
		return technologyID;
	}
	public void setTechnologyID(int technologyID) {
		this.technologyID = technologyID;
	}
	public int getJobStageID() {
		return jobStageID;
	}
	public void setJobStageID(int jobStageID) {
		this.jobStageID = jobStageID;
	}
	public int getJobRoleID() {
		return jobRoleID;
	}
	public void setJobRoleID(int jobRoleID) {
		this.jobRoleID = jobRoleID;
	}
	public int getCertificateID() {
		return certificateID;
	}
	public void setCertificateID(int certificateID) {
		this.certificateID = certificateID;
	}
	public int getCompetenceID() {
		return competenceID;
	}
	public void setCompetenceID(int competenceID) {
		this.competenceID = competenceID;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
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
	public float getHours() {
		return hours;
	}
	public void setHours(float hours) {
		this.hours = hours;
	}
	public String getManagerSignum() {
		return managerSignum;
	}
	public void setManagerSignum(String managerSignum) {
		this.managerSignum = managerSignum;
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
	public String getSubDomain() {
		return subDomain;
	}
	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}
	public String getJobStage() {
		return jobStage;
	}
	public void setJobStage(String jobStage) {
		this.jobStage = jobStage;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public int[] getCompetenceString() {
		return competenceString;
	}
	public void setCompetenceString(int[] competenceString) {
		this.competenceString = competenceString;
	}
	public String[] getCompetenceLevel() {
		return competenceLevel;
	}
	public void setCompetenceLevel(String[] competenceLevel) {
		this.competenceLevel = competenceLevel;
	}
	
	public String getResourceDescription() {
		return resourceDescription;
	}
	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}

	private int resourceRequestID;
	private int domainID;
	private int subDomainID;
	private int subServiceAreaID;
	private int technologyID;
	private int jobStageID;
	private int jobRoleID;
	private int certificateID;
	private int competenceID;
	private String signum;
	private String startDate;
	private String endDate;
	private float hours;
	private String managerSignum;
	private String domain;
	private String serviceArea;
	private String subDomain;
	private String jobStage;
	private String jobName;
	private String technology;
	private int [] competenceString;
	private String [] competenceLevel;
	private String resourceDescription;
}
