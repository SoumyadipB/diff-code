package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FlowChartCreationTmpId generated by hbm2java
 */
@Embeddable
public class FlowChartCreationTmpId implements java.io.Serializable {

	private String stepName;
	private String graphicalRepresentation;
	private String executionType;
	private String responsible;
	private String dependentStepName;
	private String ifDepnOnDecisionStep;
	private String isTaskOrSubTask;
	private String taskOrParentTaskMapped;

	public FlowChartCreationTmpId() {
	}

	public FlowChartCreationTmpId(String stepName, String graphicalRepresentation, String executionType,
			String responsible, String dependentStepName, String ifDepnOnDecisionStep, String isTaskOrSubTask,
			String taskOrParentTaskMapped) {
		this.stepName = stepName;
		this.graphicalRepresentation = graphicalRepresentation;
		this.executionType = executionType;
		this.responsible = responsible;
		this.dependentStepName = dependentStepName;
		this.ifDepnOnDecisionStep = ifDepnOnDecisionStep;
		this.isTaskOrSubTask = isTaskOrSubTask;
		this.taskOrParentTaskMapped = taskOrParentTaskMapped;
	}

	@Column(name = "StepName")
	public String getStepName() {
		return this.stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	@Column(name = "GraphicalRepresentation")
	public String getGraphicalRepresentation() {
		return this.graphicalRepresentation;
	}

	public void setGraphicalRepresentation(String graphicalRepresentation) {
		this.graphicalRepresentation = graphicalRepresentation;
	}

	@Column(name = "ExecutionType")
	public String getExecutionType() {
		return this.executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	@Column(name = "Responsible")
	public String getResponsible() {
		return this.responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	@Column(name = "DependentStepName")
	public String getDependentStepName() {
		return this.dependentStepName;
	}

	public void setDependentStepName(String dependentStepName) {
		this.dependentStepName = dependentStepName;
	}

	@Column(name = "IfDepnOnDecisionStep")
	public String getIfDepnOnDecisionStep() {
		return this.ifDepnOnDecisionStep;
	}

	public void setIfDepnOnDecisionStep(String ifDepnOnDecisionStep) {
		this.ifDepnOnDecisionStep = ifDepnOnDecisionStep;
	}

	@Column(name = "IsTaskOrSubTask")
	public String getIsTaskOrSubTask() {
		return this.isTaskOrSubTask;
	}

	public void setIsTaskOrSubTask(String isTaskOrSubTask) {
		this.isTaskOrSubTask = isTaskOrSubTask;
	}

	@Column(name = "TaskOrParentTaskMapped")
	public String getTaskOrParentTaskMapped() {
		return this.taskOrParentTaskMapped;
	}

	public void setTaskOrParentTaskMapped(String taskOrParentTaskMapped) {
		this.taskOrParentTaskMapped = taskOrParentTaskMapped;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof FlowChartCreationTmpId))
			return false;
		FlowChartCreationTmpId castOther = (FlowChartCreationTmpId) other;

		return ((this.getStepName() == castOther.getStepName()) || (this.getStepName() != null
				&& castOther.getStepName() != null && this.getStepName().equals(castOther.getStepName())))
				&& ((this.getGraphicalRepresentation() == castOther.getGraphicalRepresentation())
						|| (this.getGraphicalRepresentation() != null && castOther.getGraphicalRepresentation() != null
								&& this.getGraphicalRepresentation().equals(castOther.getGraphicalRepresentation())))
				&& ((this.getExecutionType() == castOther.getExecutionType())
						|| (this.getExecutionType() != null && castOther.getExecutionType() != null
								&& this.getExecutionType().equals(castOther.getExecutionType())))
				&& ((this.getResponsible() == castOther.getResponsible())
						|| (this.getResponsible() != null && castOther.getResponsible() != null
								&& this.getResponsible().equals(castOther.getResponsible())))
				&& ((this.getDependentStepName() == castOther.getDependentStepName())
						|| (this.getDependentStepName() != null && castOther.getDependentStepName() != null
								&& this.getDependentStepName().equals(castOther.getDependentStepName())))
				&& ((this.getIfDepnOnDecisionStep() == castOther.getIfDepnOnDecisionStep())
						|| (this.getIfDepnOnDecisionStep() != null && castOther.getIfDepnOnDecisionStep() != null
								&& this.getIfDepnOnDecisionStep().equals(castOther.getIfDepnOnDecisionStep())))
				&& ((this.getIsTaskOrSubTask() == castOther.getIsTaskOrSubTask())
						|| (this.getIsTaskOrSubTask() != null && castOther.getIsTaskOrSubTask() != null
								&& this.getIsTaskOrSubTask().equals(castOther.getIsTaskOrSubTask())))
				&& ((this.getTaskOrParentTaskMapped() == castOther.getTaskOrParentTaskMapped())
						|| (this.getTaskOrParentTaskMapped() != null && castOther.getTaskOrParentTaskMapped() != null
								&& this.getTaskOrParentTaskMapped().equals(castOther.getTaskOrParentTaskMapped())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getStepName() == null ? 0 : this.getStepName().hashCode());
		result = 37 * result
				+ (getGraphicalRepresentation() == null ? 0 : this.getGraphicalRepresentation().hashCode());
		result = 37 * result + (getExecutionType() == null ? 0 : this.getExecutionType().hashCode());
		result = 37 * result + (getResponsible() == null ? 0 : this.getResponsible().hashCode());
		result = 37 * result + (getDependentStepName() == null ? 0 : this.getDependentStepName().hashCode());
		result = 37 * result + (getIfDepnOnDecisionStep() == null ? 0 : this.getIfDepnOnDecisionStep().hashCode());
		result = 37 * result + (getIsTaskOrSubTask() == null ? 0 : this.getIsTaskOrSubTask().hashCode());
		result = 37 * result + (getTaskOrParentTaskMapped() == null ? 0 : this.getTaskOrParentTaskMapped().hashCode());
		return result;
	}

}