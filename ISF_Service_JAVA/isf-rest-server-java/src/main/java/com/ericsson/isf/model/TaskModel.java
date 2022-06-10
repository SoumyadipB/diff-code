/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;



/**
 *
 * @author esanpup
 */
public class TaskModel {
    private int taskID;
    private int subActivityID;
    private String task;
    private String executionType;
    private boolean active;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
    private float avgEstdEffort;

	private String description;
    private int ordering;
    private String masterTask;
    private List<ToolsModel> tools;
    private List<TaskToolModel> taskTool;
    private List<WorkOrderBookingDetailsModel> lstBookingDetailsModels;
    private int rpaID;
    private boolean isInputRequired;
    
    public int getRpaID() {
        return rpaID;
    }

    public void setRpaID(int rpaID) {
        this.rpaID = rpaID;
    }
    
    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean getIsInputRequired() {
		return isInputRequired;
	}

	public void setInputRequired(boolean isInputRequired) {
		this.isInputRequired = isInputRequired;
	}
    
    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
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

    public float getAvgEstdEffort() {
        return avgEstdEffort;
    }

    public void setAvgEstdEffort(float avgEstdEffort) {
        this.avgEstdEffort = avgEstdEffort;
    }

    public List<ToolsModel> getTools() {
        return tools;
    }

    public void setTools(List<ToolsModel> tools) {
        this.tools = tools;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TaskToolModel> getTaskTool() {
        return taskTool;
    }

    public void setTaskTool(List<TaskToolModel> taskTool) {
        this.taskTool = taskTool;
    }

    public List<WorkOrderBookingDetailsModel> getLstBookingDetailsModels() {
        return lstBookingDetailsModels;
    }

    public void setLstBookingDetailsModels(List<WorkOrderBookingDetailsModel> lstBookingDetailsModels) {
        this.lstBookingDetailsModels = lstBookingDetailsModels;
    }

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public String getMasterTask() {
        return masterTask;
    }

    public void setMasterTask(String masterTask) {
        this.masterTask = masterTask;
    }
    
    @Override
    public String toString() {
        return "TaskModel{" + "taskID=" + taskID + ", subActivityID=" + subActivityID + ", task=" + task + ", executionType=" + executionType + ", active=" + active + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedDate=" + lastModifiedDate + ", avgEstdEffort=" + avgEstdEffort + ", description=" + description + ", ordering=" + ordering + ", tools=" + tools + ", taskTool=" + taskTool + ", lstBookingDetailsModels=" + lstBookingDetailsModels + '}';
    }
    
    
    

   
    
    
     
    
}
