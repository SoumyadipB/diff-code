package com.ericsson.isf.model;

public class WorkFlowDetailsModel {
	
	
	private String workFlowName;
	private String type;
	private String flowChartDefId;
	private int wfid;
	private int versionNumber;
	
	public String getWorkFlowName() {
		return workFlowName;
	}
	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFlowChartDefId() {
		return flowChartDefId;
	}
	public void setFlowChartDefId(String flowChartDefId) {
		this.flowChartDefId = flowChartDefId;
	}
	public int getWfid() {
		return wfid;
	}
	public void setWfid(int wfid) {
		this.wfid = wfid;
	}
	public int getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	
	
	
}
