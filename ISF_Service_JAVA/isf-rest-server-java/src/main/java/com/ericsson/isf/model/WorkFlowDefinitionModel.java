/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author eabhmoj
 */
public class WorkFlowDefinitionModel {
private int workFlowID;
private String workFlowName;
private int projectID;
private int subActivityID;
private String flowChartType;
private int versionNumber;
private String createdBy;
private Date createdOn;
private List<WorkFlowLinksModel> workFlowLinks;
private List<WorkFlowStepsModel> workFlowSteps;

    public int getWorkFlowID() {
        return workFlowID;
    }

    public void setWorkFlowID(int workFlowID) {
        this.workFlowID = workFlowID;
    }

    public String getWorkFlowName() {
        return workFlowName;
    }

    public void setWorkFlowName(String workFlowName) {
        this.workFlowName = workFlowName;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
    }

    public String getFlowChartType() {
        return flowChartType;
    }

    public void setFlowChartType(String flowChartType) {
        this.flowChartType = flowChartType;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public List<WorkFlowLinksModel> getWorkFlowLinks() {
        return workFlowLinks;
    }

    public void setWorkFlowLinks(List<WorkFlowLinksModel> workFlowLinks) {
        this.workFlowLinks = workFlowLinks;
    }

    public List<WorkFlowStepsModel> getWorkFlowSteps() {
        return workFlowSteps;
    }

    public void setWorkFlowSteps(List<WorkFlowStepsModel> workFlowSteps) {
        this.workFlowSteps = workFlowSteps;
    }

}
