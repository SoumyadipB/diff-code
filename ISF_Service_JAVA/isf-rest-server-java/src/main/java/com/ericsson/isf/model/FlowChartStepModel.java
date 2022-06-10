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
public class FlowChartStepModel {
    private String subActivityFlowChartStepID;
    private int subActivityFlowChartDefID;
    private String StepName;
    private String displayStepName;
    private String graphicalRepresentation;
    private String executionType;
    private String responsible;
    private String isTaskOrSubTask;
    private String taskOrParentTaskMapped;
    private int ordering;
    private String stepJSON;
    private String toolName;
    private int toolID;
    private String taskName;
    private int taskID;
    private String outputUpload;
    private String cascadeInput;
    
    public String getCascadeInput() {
		return cascadeInput;
	}

	public void setCascadeInput(String cascadeInput) {
		this.cascadeInput = cascadeInput;
	}
    public String getSubActivityFlowChartStepID() {
		return subActivityFlowChartStepID;
	}

	public void setSubActivityFlowChartStepID(String subActivityFlowChartStepID) {
		this.subActivityFlowChartStepID = subActivityFlowChartStepID;
	}

	public int getSubActivityFlowChartDefID() {
        return subActivityFlowChartDefID;
    }

    public void setSubActivityFlowChartDefID(int subActivityFlowChartDefID) {
        this.subActivityFlowChartDefID = subActivityFlowChartDefID;
    }

    public String getStepName() {
        return StepName;
    }

    public void setStepName(String StepName) {
        this.StepName = StepName;
    }

    public String getDisplayStepName() {
        return displayStepName;
    }

    public void setDisplayStepName(String displayStepName) {
        this.displayStepName = displayStepName;
    }

    public String getGraphicalRepresentation() {
        return graphicalRepresentation;
    }

    public void setGraphicalRepresentation(String graphicalRepresentation) {
        this.graphicalRepresentation = graphicalRepresentation;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getIsTaskOrSubTask() {
        return isTaskOrSubTask;
    }

    public void setIsTaskOrSubTask(String isTaskOrSubTask) {
        this.isTaskOrSubTask = isTaskOrSubTask;
    }

    public String getTaskOrParentTaskMapped() {
        return taskOrParentTaskMapped;
    }

    public void setTaskOrParentTaskMapped(String taskOrParentTaskMapped) {
        this.taskOrParentTaskMapped = taskOrParentTaskMapped;
    }

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public String getStepJSON() {
        return stepJSON;
    }

    public void setStepJSON(String stepJSON) {
        this.stepJSON = stepJSON;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
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

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
	
	public String getOutputUpload() {
		return outputUpload;
	}

	public void setOutputUpload(String outputUpload) {
		this.outputUpload = outputUpload;
	}


	@Override
	public String toString() {
		return "FlowChartStepModel [subActivityFlowChartStepID=" + subActivityFlowChartStepID
				+ ", subActivityFlowChartDefID=" + subActivityFlowChartDefID + ", StepName=" + StepName
				+ ", displayStepName=" + displayStepName + ", graphicalRepresentation=" + graphicalRepresentation
				+ ", executionType=" + executionType + ", responsible=" + responsible + ", isTaskOrSubTask="
				+ isTaskOrSubTask + ", taskOrParentTaskMapped=" + taskOrParentTaskMapped + ", ordering=" + ordering
				+ ", stepJSON=" + stepJSON + ", toolName=" + toolName + ", toolID=" + toolID + ", taskName=" + taskName
				+ ", taskID=" + taskID + ", outputUpload=" + outputUpload +
				", getSubActivityFlowChartStepID()="
				+ getSubActivityFlowChartStepID() + ", getSubActivityFlowChartDefID()=" + getSubActivityFlowChartDefID()
				+ ", getStepName()=" + getStepName() + ", getDisplayStepName()=" + getDisplayStepName()
				+ ", getGraphicalRepresentation()=" + getGraphicalRepresentation() + ", getExecutionType()="
				+ getExecutionType() + ", getResponsible()=" + getResponsible() + ", getIsTaskOrSubTask()="
				+ getIsTaskOrSubTask() + ", getTaskOrParentTaskMapped()=" + getTaskOrParentTaskMapped()
				+ ", getOrdering()=" + getOrdering() + ", getStepJSON()=" + getStepJSON() + ", getToolName()="
				+ getToolName() + ", getToolID()=" + getToolID() + ", getTaskName()=" + getTaskName() + ", getTaskID()="
				+ getTaskID() + ", getOutputUpload()=" + getOutputUpload() +
				", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}


}
