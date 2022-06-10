package com.ericsson.isf.model;

//This class used to define rule.

public class AutoSenseRuleModel {
	private int ruleId;
	private String ruleName;
	private String ruleDescription;
	private String ruleStatus;
	private String ruleType;
	private String ruleJson;
	private String createdBy;
	private String modifiedBy;
	private String createdOn;
	private String modifiedOn;
	private boolean active;
	private int domainID;
	private int technologyID;
	private int taskID;
	private int subactivityID;
	private String domain;
	private String technology;
	private String task;
	private String subactivity;
	private String activity;
	private String subdomain;
	private int serviceAreaID;
	private String serviceArea;
	private String subServiceArea;
	private String parsedRuleJson;
	private String jsonManualValidation;
	private String nameAndCreatedBy;
	private String recordsTotal;
	private String recordsFiltered;
	
	public String getParsedRuleJson() {
		return parsedRuleJson;
	}

	public void setParsedRuleJson(String parsedRuleJson) {
		this.parsedRuleJson = parsedRuleJson;
	}

	public int getDomainID() {
		return domainID;
	}

	public void setDomainID(int domainID) {
		this.domainID = domainID;
	}

	public int getTechnologyID() {
		return technologyID;
	}

	public void setTechnologyID(int technologyID) {
		this.technologyID = technologyID;
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public int getSubactivityID() {
		return subactivityID;
	}

	public void setSubactivityID(int subactivityID) {
		this.subactivityID = subactivityID;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getRuleJson() {
		return ruleJson;
	}

	public void setRuleJson(String ruleJson) {
		this.ruleJson = ruleJson;
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

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getRuleId() {
		return ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleDescription() {
		return ruleDescription;
	}

	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}

	public String getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getSubactivity() {
		return subactivity;
	}

	public void setSubactivity(String subactivity) {
		this.subactivity = subactivity;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}

	public int getServiceAreaID() {
		return serviceAreaID;
	}

	public void setServiceAreaID(int serviceAreaID) {
		this.serviceAreaID = serviceAreaID;
	}

	public String getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	public String getSubServiceArea() {
		return subServiceArea;
	}

	public void setSubServiceArea(String subServiceArea) {
		this.subServiceArea = subServiceArea;
	}

	public String getJsonManualValidation() {
		return jsonManualValidation;
	}

	public void setJsonManualValidation(String jsonManualValidation) {
		this.jsonManualValidation = jsonManualValidation;
	}

	public String getNameAndCreatedBy() {
		return nameAndCreatedBy;
	}

	public void setNameAndCreatedBy(String nameAndCreatedBy) {
		this.nameAndCreatedBy = nameAndCreatedBy;
	}

	public String getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(String recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public String getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(String recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	

     
}
