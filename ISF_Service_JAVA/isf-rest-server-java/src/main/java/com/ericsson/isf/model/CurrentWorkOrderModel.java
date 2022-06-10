/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ekarath
 */
public class CurrentWorkOrderModel {
    private int wOID;
    private String wOName;
    private int projectID;
    private int wOPlanID;
    private Date plannedStartDate;
    private Date plannedEndDate;
    private Date actualStartDate;
    private Date actualEndDate;
    private String signumID;
    private String status;
    private String priority;
    private Date closedOn;
    private boolean active;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private int adHocWoID;
    private String statusComment;
    private int subActivityID;
    private String subActivity;
    private float estimatedEffort;
    private String serverTime;
    private int subActivityFlowChartDefID;
    private int versionNumber;
    private String workFlowName;
    private List<StepTaskModel> listOfStepTaskDetails;
    private Map<String, Object> nodeValues;
    private String serverPlatform;
    private String outputUpload;

	public int getwOID() {
        return wOID;
    }

    public void setwOID(int wOID) {
        this.wOID = wOID;
    }

    public String getwOName() {
        return wOName;
    }

    public void setwOName(String wOName) {
        this.wOName = wOName;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getwOPlanID() {
        return wOPlanID;
    }

    public void setwOPlanID(int wOPlanID) {
        this.wOPlanID = wOPlanID;
    }

    public Date getPlannedStartDate() {
        return plannedStartDate;
    }

    public void setPlannedStartDate(Date plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    public Date getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(Date plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public String getSignumID() {
        return signumID;
    }

    public void setSignumID(String signumID) {
        this.signumID = signumID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(Date closedOn) {
        this.closedOn = closedOn;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getAdHocWoID() {
        return adHocWoID;
    }

    public void setAdHocWoID(int adHocWoID) {
        this.adHocWoID = adHocWoID;
    }

    public String getStatusComment() {
        return statusComment;
    }

    public void setStatusComment(String statusComment) {
        this.statusComment = statusComment;
    }

    public int getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
    }

    public String getSubActivity() {
        return subActivity;
    }

    public void setSubActivity(String subActivity) {
        this.subActivity = subActivity;
    }

    public float getEstimatedEffort() {
        return estimatedEffort;
    }

    public void setEstimatedEffort(float estimatedEffort) {
        this.estimatedEffort = estimatedEffort;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public int getSubActivityFlowChartDefID() {
        return subActivityFlowChartDefID;
    }

    public void setSubActivityFlowChartDefID(int subActivityFlowChartDefID) {
        this.subActivityFlowChartDefID = subActivityFlowChartDefID;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getWorkFlowName() {
        return workFlowName;
    }

    public void setWorkFlowName(String workFlowName) {
        this.workFlowName = workFlowName;
    }

    public List<StepTaskModel> getListOfStepTaskDetails() {
        return listOfStepTaskDetails;
    }

    public void setListOfStepTaskDetails(List<StepTaskModel> listOfStepTaskDetails) {
        this.listOfStepTaskDetails = listOfStepTaskDetails;
    }

    public Map<String, Object> getNodeValues() {
        return nodeValues;
    }

    public void setNodeValues(Map<String, Object> nodeValues) {
        this.nodeValues = nodeValues;
    }

	public String getServerPlatform() {
		return serverPlatform;
	}

	public void setServerPlatform(String serverPlatform) {
		this.serverPlatform = serverPlatform;
	}

	public String getOutputUpload() {
		return outputUpload;
	}

	public void setOutputUpload(String outputUpload) {
		this.outputUpload = outputUpload;
	}
    
}
