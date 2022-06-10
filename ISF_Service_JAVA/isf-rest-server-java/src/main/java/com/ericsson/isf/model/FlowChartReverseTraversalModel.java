package com.ericsson.isf.model;

import java.util.List;

public class FlowChartReverseTraversalModel {
	private String stepID;
	private Integer stepProficiencyID;
	private List<String> parentsStepID;
	private String status;
	private boolean isVisited;
	private String executionType;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStepID() {
		return stepID;
	}
	public void setStepID(String stepID) {
		this.stepID = stepID;
	}
	public List<String> getParentsStepID() {
		return parentsStepID;
	}
	public void setParentsStepID(List<String> parentsStepID) {
		this.parentsStepID = parentsStepID;
	}
	public Integer getStepProficiencyID() {
		return stepProficiencyID;
	}
	public void setStepProficiencyID(Integer stepProficiencyID) {
		this.stepProficiencyID = stepProficiencyID;
	}
	public boolean isVisited() {
		return isVisited;
	}
	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}
	public String getExecutionType() {
		return executionType;
	}
	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}
	public FlowChartReverseTraversalModel executionType(String executionType) {
		this.executionType=executionType;
		return this;
	}
}
