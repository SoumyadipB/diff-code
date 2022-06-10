package com.ericsson.isf.model;



public class RuleMigrationModel {
	
	private String oldJson;
	private String newJson;
	private String ruleName;
	private int ruleMigrationId;
	private String signum;
	private String fileName;
	private Integer taskId;
	private String ruleType;
	private String ownerSignum;
	private Integer serviceAreaId;
	private Integer domainId;
	private Integer subactivityId;
	private Integer technologyId;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public String getOwnerSignum() {
		return ownerSignum;
	}
	public void setOwnerSignum(String ownerSignum) {
		this.ownerSignum = ownerSignum;
	}
	public Integer getServiceAreaId() {
		return serviceAreaId;
	}
	public void setServiceAreaId(Integer serviceAreaId) {
		this.serviceAreaId = serviceAreaId;
	}
	public Integer getDomainId() {
		return domainId;
	}
	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}
	public Integer getSubactivityId() {
		return subactivityId;
	}
	public void setSubactivityId(Integer subactivityId) {
		this.subactivityId = subactivityId;
	}
	public String getOldJson() {
		return oldJson;
	}
	public void setOldJson(String oldJson) {
		this.oldJson = oldJson;
	}
	public String getNewJson() {
		return newJson;
	}
	public void setNewJson(String newJson) {
		this.newJson = newJson;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public int getRuleMigrationId() {
		return ruleMigrationId;
	}
	public void setRuleMigrationId(int ruleMigrationId) {
		this.ruleMigrationId = ruleMigrationId;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public Integer getTechnologyId() {
		return technologyId;
	}
	public void setTechnologyId(Integer technologyId) {
		this.technologyId = technologyId;
	}
	

}
