/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author esanpup
 */
public class ScopeTaskMappingModel {
    
    private int scopeTaskMappingID;
    private int scopeID;
    private int projectID;
    private int subActivityID;
    private int taskID;
    private String task;
    private int toolID;
    private String tool;
    private String executionType;
    private float avgEstdEffort;
    private int rpaID;
    private String rpaName;
    private String masterTask;
    private int versionNumber;

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public int getScopeTaskMappingID() {
        return scopeTaskMappingID;
    }

    public void setScopeTaskMappingID(int scopeTaskMappingID) {
        this.scopeTaskMappingID = scopeTaskMappingID;
    }

    public int getScopeID() {
        return scopeID;
    }

    public void setScopeID(int scopeID) {
        this.scopeID = scopeID;
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

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getToolID() {
        return toolID;
    }

    public void setToolID(int toolID) {
        this.toolID = toolID;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public float getAvgEstdEffort() {
        return avgEstdEffort;
    }

    public void setAvgEstdEffort(float avgEstdEffort) {
        this.avgEstdEffort = avgEstdEffort;
    }

    public int getRpaID() {
        return rpaID;
    }

    public void setRpaID(int rpaID) {
        this.rpaID = rpaID;
    }

    public String getRpaName() {
        return rpaName;
    }

    public void setRpaName(String rpaName) {
        this.rpaName = rpaName;
    }

    public String getMasterTask() {
        return masterTask;
    }

    public void setMasterTask(String masterTask) {
        this.masterTask = masterTask;
    }

  
    
    
}
