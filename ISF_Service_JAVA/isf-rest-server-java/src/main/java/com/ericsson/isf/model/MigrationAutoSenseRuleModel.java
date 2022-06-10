package com.ericsson.isf.model;

public class MigrationAutoSenseRuleModel {
	private int ruleMigrationID;
	private String ruleName;
	private String ruleDescription;
	private String oldRuleJson;
	private String newRuleJson;
	private String ruleJsonForScanner;
	private String ruleJsonForValidation;
	private int serviceAreaID;
	private int domainID;
	private int subactivityID;
	private int technologyID;
	private int taskID;
	private boolean verifiedByISF;
	private boolean manualValidated;
	private boolean transferred;
	private String createdBy;
	private String modifiedBy;
	private String createdOn;
	private String modifiedOn;
	private String serviceArea;
	private String domain;
	private String technology;
	private String task;
	private String subactivity;
	private String activity;
	private String subdomain;
	private String subServiceArea;
	private String ruleType;
	private String ownerSignum;
	private String migrationFileName;
	
	public String getOwnerSignum() {
		return ownerSignum;
	}
	public void setOwnerSignum(String ownerSignum) {
		this.ownerSignum = ownerSignum;
	}
	public String getMigrationFileName() {
		return migrationFileName;
	}
	public void setMigrationFileName(String migrationFileName) {
		this.migrationFileName = migrationFileName;
	}
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public String getServiceArea() {
		return serviceArea;
	}
	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
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
	public String getSubServiceArea() {
		return subServiceArea;
	}
	public void setSubServiceArea(String subServiceArea) {
		this.subServiceArea = subServiceArea;
	}
	public int getRuleMigrationID() {
		return ruleMigrationID;
	}
	public void setRuleMigrationID(int ruleMigrationID) {
		this.ruleMigrationID = ruleMigrationID;
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
	public String getOldRuleJson() {
		return oldRuleJson;
	}
	public void setOldRuleJson(String oldRuleJson) {
		this.oldRuleJson = oldRuleJson;
	}
	public String getNewRuleJson() {
		return newRuleJson;
	}
	public void setNewRuleJson(String newRuleJson) {
		this.newRuleJson = newRuleJson;
	}
	public int getServiceAreaID() {
		return serviceAreaID;
	}
	public void setServiceAreaID(int serviceAreaID) {
		this.serviceAreaID = serviceAreaID;
	}
	public int getDomainID() {
		return domainID;
	}
	public void setDomainID(int domainID) {
		this.domainID = domainID;
	}
	public int getSubactivityID() {
		return subactivityID;
	}
	public void setSubactivityID(int subactivityID) {
		this.subactivityID = subactivityID;
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
	public boolean isVerifiedByISF() {
		return verifiedByISF;
	}
	public void setVerifiedByISF(boolean verifiedByISF) {
		this.verifiedByISF = verifiedByISF;
	}
	public boolean isManualValidated() {
		return manualValidated;
	}
	public void setManualValidated(boolean manualValidated) {
		this.manualValidated = manualValidated;
	}
	public boolean isTransferred() {
		return transferred;
	}
	public void setTransferred(boolean transferred) {
		this.transferred = transferred;
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
	public String getRuleJsonForScanner() {
		return ruleJsonForScanner;
	}
	public void setRuleJsonForScanner(String ruleJsonForScanner) {
		this.ruleJsonForScanner = ruleJsonForScanner;
	}
	public String getRuleJsonForValidation() {
		return ruleJsonForValidation;
	}
	public void setRuleJsonForValidation(String ruleJsonForValidation) {
		this.ruleJsonForValidation = ruleJsonForValidation;
	}
	
}
