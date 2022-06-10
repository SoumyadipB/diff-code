/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;



/**
 *
 * @author ekarath
 */
public class TaskToolModel {
    
    private int toolID;
    private String createdBy;
    private int taskToolID;
    private int taskID;
    private String taskName;
    private String toolName;
    private boolean active;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
    private int avgEstdEffort;

    public int getAvgEstdEffort() {
        return avgEstdEffort;
    }

    public void setAvgEstdEffort(int avgEstdEffort) {
        this.avgEstdEffort = avgEstdEffort;
    }

    public int getTaskToolID() {
        return taskToolID;
    }

    public void setTaskToolID(int taskToolID) {
        this.taskToolID = taskToolID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getToolID() {
        return toolID;
    }

    public void setToolID(int toolID) {
        this.toolID = toolID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
}
