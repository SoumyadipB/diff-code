package com.ericsson.isf.model;

public class ScopeDetailModel {
	private int projectID;
	private int scopeID;
	private String scopeName;
	private boolean scopeActive;
	private boolean haveExecutionPlan;
	
	public boolean isHaveExecutionPlan() {
		return haveExecutionPlan;
	}
	public void setHaveExecutionPlan(boolean haveExecutionPlan) {
		this.haveExecutionPlan = haveExecutionPlan;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public int getScopeID() {
		return scopeID;
	}
	public void setScopeID(int scopeID) {
		this.scopeID = scopeID;
	}
	public String getScopeName() {
		return scopeName;
	}
	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}
	public boolean getScopeActive() {
		return scopeActive;
	}
	public void setScopeActive(boolean scopeActive) {
		this.scopeActive = scopeActive;
	}
}
