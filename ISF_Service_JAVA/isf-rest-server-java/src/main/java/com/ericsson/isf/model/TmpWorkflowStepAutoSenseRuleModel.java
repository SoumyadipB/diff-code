package com.ericsson.isf.model;

/**
 * @author ekarmuj
 * Purpose: This Model used to define temporary workflow auto sense rule.
 */

public class TmpWorkflowStepAutoSenseRuleModel {
	private int ruleId;
	private String stepID;
	private String projectIDSubactivityIDLoggedInSignum;
	private boolean ruleStatus;
	private String taskActionName;
	private String parseRuleJson;
	private String createdBy;
	private String modifiedBy;
	private String createdOn;
	private String modifiedOn;
	private int tmpWfStepsAutoSenseRulesID;
	private boolean existing;
	
	private boolean experiencedFlow;
	private Integer taskId;
	
	
	public String getStepID() {
		return stepID;
	}

	public void setStepID(String stepID) {
		this.stepID = stepID;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	
	public int getRuleId() {
		return ruleId;
	}
	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}
	public String getProjectIDSubactivityIDLoggedInSignum() {
		return projectIDSubactivityIDLoggedInSignum;
	}
	public void setProjectIDSubactivityIDLoggedInSignum(String projectIDSubactivityIDLoggedInSignum) {
		this.projectIDSubactivityIDLoggedInSignum = projectIDSubactivityIDLoggedInSignum;
	}
	public boolean isRuleStatus() {
		return ruleStatus;
	}
	public void setRuleStatus(boolean ruleStatus) {
		this.ruleStatus = ruleStatus;
	}
	public String getTaskActionName() {
		return taskActionName;
	}
	public void setTaskActionName(String taskActionName) {
		this.taskActionName = taskActionName;
	}
	public String getParseRuleJson() {
		return parseRuleJson;
	}
	public void setParseRuleJson(String parseRuleJson) {
		this.parseRuleJson = parseRuleJson;
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
	public int getTmpWfStepsAutoSenseRulesID() {
		return tmpWfStepsAutoSenseRulesID;
	}
	public void setTmpWfStepsAutoSenseRulesID(int tmpWfStepsAutoSenseRulesID) {
		this.tmpWfStepsAutoSenseRulesID = tmpWfStepsAutoSenseRulesID;
	}
	public boolean getExisting() {
		return existing;
	}
	public void setExisting(boolean existing) {
		this.existing = existing;
	}

	public boolean isExperiencedFlow() {
		return experiencedFlow;
	}

	public void setExperiencedFlow(boolean experiencedFlow) {
		this.experiencedFlow = experiencedFlow;
	}
	
}
