package com.ericsson.isf.model;

public class ParallelWorkOrderDetailsModel {
	
	private String signumID;
	private String isApproved;
	private String executionType;
	private int woid;
	private int taskid;
	private int projectID;
	private int versionNO;
	private int subActivityFlowChartDefID;
	private String stepID;
	
	private Object[] arRpaIDByWOID=new Object[5];
	
	public String getSignumID() {
		return signumID;
	}
	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}
	public String getIsApproved() {
		return isApproved;
	}
	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}
	public String getExecutionType() {
		return executionType;
	}
	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}
	public int getWoid() {
		return woid;
	}
	public void setWoid(int woid) {
		this.woid = woid;
	}
	public int getTaskid() {
		return taskid;
	}
	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public int getVersionNO() {
		return versionNO;
	}
	public void setVersionNO(int versionNO) {
		this.versionNO = versionNO;
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
	public Object[] getArRpaIDByWOID() {
		return arRpaIDByWOID;
	}
	public void setArRpaIDByWOID(Object[] arRpaIDByWOID) {
		this.arRpaIDByWOID = arRpaIDByWOID;
	}
	
	
	
	
	
}
