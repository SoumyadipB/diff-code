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
public class ProjectDefinedTaskModel {
    
    private int scopeTaskMappingID;
    private int projectID;
    private int scopeID;
    private int subActivityID;
    private int taskID;
    private String task;
    private String executionType;
    private float avgEstdEffort;
    private String tool;

    public int getScopeTaskMappingID() {
        return scopeTaskMappingID;
    }

    public void setScopeTaskMappingID(int scopeTaskMappingID) {
        this.scopeTaskMappingID = scopeTaskMappingID;
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

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }
}
