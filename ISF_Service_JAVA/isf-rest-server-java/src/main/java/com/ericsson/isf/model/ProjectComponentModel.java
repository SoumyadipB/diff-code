/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

public class ProjectComponentModel {

	private Integer projectID;
	private Integer scopeID;
	private Integer scopeDetailID;
	private Integer activityID;
	private Integer subActivityID;
	private Integer woPlanID;
	private String type;
	private String loggedInUser;
	private Integer subActivityFlowChartDefID;
	private boolean active;
	private List<Integer> lstWoPlanID;
	private int executionPlanId;

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public Integer getScopeID() {
		return scopeID;
	}

	public void setScopeID(Integer scopeID) {
		this.scopeID = scopeID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getWoPlanID() {
		return woPlanID;
	}

	public void setWoPlanID(Integer woPlanID) {
		this.woPlanID = woPlanID;
	}

	public Integer getSubActivityID() {
		return subActivityID;
	}

	public void setSubActivityID(Integer subActivityID) {
		this.subActivityID = subActivityID;
	}

	public Integer getSubActivityFlowChartDefID() {
		return subActivityFlowChartDefID;
	}

	public void setSubActivityFlowChartDefID(Integer subActivityFlowChartDefID) {
		this.subActivityFlowChartDefID = subActivityFlowChartDefID;
	}

	public Integer getActivityID() {
		return activityID;
	}

	public void setActivityID(Integer activityID) {
		this.activityID = activityID;
	}

	public Integer getScopeDetailID() {
		return scopeDetailID;
	}

	public void setScopeDetailID(Integer scopeDetailID) {
		this.scopeDetailID = scopeDetailID;
	}

	public String getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Integer> getLstWoPlanID() {
		return lstWoPlanID;
	}

	public void setLstWoPlanID(List<Integer> lstWoPlanID) {
		this.lstWoPlanID = lstWoPlanID;
	}

	public int getExecutionPlanId() {
		return executionPlanId;
	}

	public void setExecutionPlanId(int executionPlanId) {
		this.executionPlanId = executionPlanId;
	}
	
}
