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
public class FlowChartStepInformationModel {
    
    private int fcStepDetailsID;
    private int subActivityFlowChartDefID;
    private String stepID;
    private String stepName;
    private int taskID;
    private String task;
    private String executionType;
    private float avgEstdEffort;
    private int toolID;
    private int versionNumber;
    private int rpaID;
    private String masterTask;

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


    public String getStepID() {
		return stepID;
	}

	public void setStepID(String stepID) {
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

    public float getAvgEstdEffort() {
        return avgEstdEffort;
    }

    public void setAvgEstdEffort(float avgEstdEffort) {
        this.avgEstdEffort = avgEstdEffort;
    }

    public int getToolID() {
        return toolID;
    }

    public void setToolID(int toolID) {
        this.toolID = toolID;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public int getRpaID() {
        return rpaID;
    }

    public void setRpaID(int rpaID) {
        this.rpaID = rpaID;
    }

    public String getMasterTask() {
        return masterTask;
    }

    public void setMasterTask(String masterTask) {
        this.masterTask = masterTask;
    }
    
    
    
}
