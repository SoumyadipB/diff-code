package com.ericsson.isf.model;

import java.util.List;

public class WorkOrderCurrentStepDetailResponseModel {
	private List<WorkOrderCurrentStepDetailsModel> executedSteps;
	private List<WorkOrderCurrentStepDetailsModel> currentSteps;
	public List<WorkOrderCurrentStepDetailsModel> getExecutedSteps() {
		return executedSteps;
	}
	public void setExecutedSteps(List<WorkOrderCurrentStepDetailsModel> executedSteps) {
		this.executedSteps = executedSteps;
	}
	public List<WorkOrderCurrentStepDetailsModel> getCurrentSteps() {
		return currentSteps;
	}
	public void setCurrentSteps(List<WorkOrderCurrentStepDetailsModel> currentSteps) {
		this.currentSteps = currentSteps;
	}
	

}
