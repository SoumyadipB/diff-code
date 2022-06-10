/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author edhhklu
 */
public class ExecutionPlanFlow {
	private long executionPlanDetailId;
	private int executionPlanFlowId;
	private int woid;
	private int planId;
	private boolean isComplete;
	private boolean isNextCreated;
	private boolean isActive;
	private long execPlanGroupId;
	private int doID;

	public ExecutionPlanFlow() {
	}

	public ExecutionPlanFlow(int woPlanId, int woid, long executionPlanDetailId, long execPlanGroupId, int doID){
    	this.setPlanId(woPlanId);
    	this.setWoid(woid);
		this.setExecutionPlanDetailId(executionPlanDetailId);
		this.setExecPlanGroupId(execPlanGroupId);
		this.setDoID(doID);
    }

	public long getExecPlanGroupId() {
		return execPlanGroupId;
	}

	public void setExecPlanGroupId(long execPlanGroupId) {
		this.execPlanGroupId = execPlanGroupId;
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public long getExecutionPlanDetailId() {
		return executionPlanDetailId;
	}

	public void setExecutionPlanDetailId(long executionPlanDetailId) {
		this.executionPlanDetailId = executionPlanDetailId;
	}

	public int getExecutionPlanFlowId() {
		return executionPlanFlowId;
	}

	public void setExecutionPlanFlowId(int executionPlanFlowId) {
		this.executionPlanFlowId = executionPlanFlowId;
	}

	public int getWoid() {
		return woid;
	}

	public void setWoid(int woid) {
		this.woid = woid;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public boolean isNextCreated() {
		return isNextCreated;
	}

	public void setNextCreated(boolean isNextCreated) {
		this.isNextCreated = isNextCreated;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getDoID() {
		return doID;
	}

	public void setDoID(int doID) {
		this.doID = doID;
	}

}
