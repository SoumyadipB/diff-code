/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author enalpra
 */
public class FlowChartStepDetailsModel {
    private int scopeTaskMappingID;
    private int projectID;
    private int scopeID;
    private int subActivityID;
    private Integer taskID;
    private String task;
    private String executionType;
    private float avgEstdEffort;
    private Integer toolID;
    private String tool;
    private int rpaID;
    private String rpaName;
    private int versionNo;
    private String flowChartStepID;
    private int subActivityFlowChartDefID;

    public String getRpaName() {
        return rpaName;
    }

    public void setRpaName(String rpaName) {
        this.rpaName = rpaName;
    }
    

    public int getRpaID() {
        return rpaID;
    }

    public void setRpaID(int rpaID) {
        this.rpaID = rpaID;
    }
    
    public int getScopeTaskMappingID() {
        return scopeTaskMappingID;
    }

    public void setScopeTaskMappingID(int scopeTaskMappingID) {
        this.scopeTaskMappingID = scopeTaskMappingID;
    }

    public int getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
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
    
    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public Integer getToolID() {
        return toolID;
    }

    public void setToolID(Integer toolID) {
        this.toolID = toolID;
    }

    

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
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

    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public String getFlowChartStepID() {
		return flowChartStepID;
	}

	public void setFlowChartStepID(String flowChartStepID) {
		this.flowChartStepID = flowChartStepID;
	}

	public int getSubActivityFlowChartDefID() {
        return subActivityFlowChartDefID;
    }

    public void setSubActivityFlowChartDefID(int subActivityFlowChartDefID) {
        this.subActivityFlowChartDefID = subActivityFlowChartDefID;
    }
    
        
    @Override
    public String toString() {
        return "FlowChartStepDetailsModel{" + "projectID=" + projectID + ", scopeID=" + scopeID + ", subActivityID=" + subActivityID + ", taskID=" + taskID + ", task=" + task + ", executionType=" + executionType + ", avgEstdEffort=" + avgEstdEffort + ", toolID=" + toolID + ", tool=" + tool + '}';
    }

    

        
}
