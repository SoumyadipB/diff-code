package com.ericsson.isf.model;

public class AutoSenseRuleFlowchartModel {
	private int flowchartDefID;
	private int woID;
	private String stepID;
	private int taskID;
	private String signumID;
	private String overrideAction;
	private String parsedRuleJson;
	public int getFlowchartDefID() {
		return flowchartDefID;
	}
	public void setFlowchartDefID(int flowchartDefID) {
		this.flowchartDefID = flowchartDefID;
	}
	public int getWoID() {
		return woID;
	}
	public void setWoID(int woID) {
		this.woID = woID;
	}
	public String getStepID() {
		return stepID;
	}
	public void setStepID(String stepID) {
		this.stepID = stepID;
	}
	public int getTaskID() {
		return taskID;
	}
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
	public String getSignumID() {
		return signumID;
	}
	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}
	public String getOverrideAction() {
		return overrideAction;
	}
	public void setOverrideAction(String overrideAction) {
		this.overrideAction = overrideAction;
	}
	public String getParsedRuleJson() {
		return parsedRuleJson;
	}
	public void setParsedRuleJson(String parsedRuleJson) {
		this.parsedRuleJson = parsedRuleJson;
	}
	
		
}
