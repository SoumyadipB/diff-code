/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author ekumvsu
 */
public class SearchPlannedWOForTasksModel {
    
    int fcStepDetailsID;
    int subActivityFlowChartDefID;
    int stepID;
    String stepName;
    int taskID;
    String task;
    String executionType;
    String avgEstdEffort;
    int toolID;
    boolean active;
    Integer versionNO;

    public Integer getVersionNO() {
		return versionNO;
	}

	public void setVersionNO(Integer versionNO) {
		this.versionNO = versionNO;
	}

	public int getFcStepDetailsID() {
        return fcStepDetailsID;
    }

    public void setFcStepDetailsID(int fcStepDetailsID) {
        this.fcStepDetailsID = fcStepDetailsID;
    }

    public int getSubActivityFlowChartDefID() {
        return subActivityFlowChartDefID;
    }

    public void setSubActivityFlowChartDefID(int subActivityFlowChartDefID) {
        this.subActivityFlowChartDefID = subActivityFlowChartDefID;
    }

    public int getStepID() {
        return stepID;
    }

    public void setStepID(int stepID) {
        this.stepID = stepID;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
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

    public String getAvgEstdEffort() {
        return avgEstdEffort;
    }

    public void setAvgEstdEffort(String avgEstdEffort) {
        this.avgEstdEffort = avgEstdEffort;
    }

    public int getToolID() {
        return toolID;
    }

    public void setToolID(int toolID) {
        this.toolID = toolID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    
}
