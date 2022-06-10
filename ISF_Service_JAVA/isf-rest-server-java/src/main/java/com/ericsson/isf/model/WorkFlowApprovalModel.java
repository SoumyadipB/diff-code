/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

/**
 *
 * @author ekarath
 */
public class WorkFlowApprovalModel {
    
    private int flowchartDefID;
    private int projectID;
    private int scopeID;
    private int subActivityID;
    private String scopeName;
    private String domain;
    private String subDomain;
    private String serviceArea;
    private String technology;
    private String activity;
    private String subActivity;
    private List<String> versionNo;
    private List<String> workFlowName;
    private String modifiedBy;
    private String modifiedOn;
    private String type;
    private String flowChartJSON;
    private int wfid;
    private int parentSubActFlowChartDefID;

    public int getFlowchartDefID() {
        return flowchartDefID;
    }

    public void setFlowchartDefID(int flowchartDefID) {
        this.flowchartDefID = flowchartDefID;
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

    public int getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
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

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getSubActivity() {
        return subActivity;
    }

    public void setSubActivity(String subActivity) {
        this.subActivity = subActivity;
    }

    public List<String> getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(List<String> versionNo) {
        this.versionNo = versionNo;
    }

    public List<String> getWorkFlowName() {
        return workFlowName;
    }

    public void setWorkFlowName(List<String> workFlowName) {
        this.workFlowName = workFlowName;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlowChartJSON() {
        return flowChartJSON;
    }

    public void setFlowChartJSON(String flowChartJSON) {
        this.flowChartJSON = flowChartJSON;
    }

	public int getWfid() {
		return wfid;
	}

	public void setWfid(int wfid) {
		this.wfid = wfid;
	}

	public int getParentSubActFlowChartDefID() {
		return parentSubActFlowChartDefID;
	}

	public void setParentSubActFlowChartDefID(int parentSubActFlowChartDefID) {
		this.parentSubActFlowChartDefID = parentSubActFlowChartDefID;
	}
    
}
