package com.ericsson.isf.model;

public class RuleModel {
	private String taskActionName;
	private String parsedRuleJson;
	
	public String getTaskActionName() {
		return taskActionName;
	}
	public void setTaskActionName(String taskActionName) {
		this.taskActionName = taskActionName;
	}
	public String getParsedRuleJson() {
		return parsedRuleJson;
	}
	public void setParsedRuleJson(String parsedRuleJson) {
		this.parsedRuleJson = parsedRuleJson;
	}
	
	
}
