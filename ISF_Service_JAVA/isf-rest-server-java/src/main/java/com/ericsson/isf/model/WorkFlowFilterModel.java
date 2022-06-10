package com.ericsson.isf.model;

public class WorkFlowFilterModel {
	private int workFlowId;
	private String workFlowName;
	private String workFlowVersion;
    private int subActivityFlowChartDefID;
    private String wFOwner;


	public String getwFOwner() {
		return wFOwner;
	}

	public void setwFOwner(String wFOwner) {
		this.wFOwner = wFOwner;
	}

	public int getWorkFlowId() {
		return workFlowId;
	}

	public void setWorkFlowId(int workFlowId) {
		this.workFlowId = workFlowId;
	}

	public String getWorkFlowName() {
		return workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	public String getWorkFlowVersion() {
		return workFlowVersion;
	}

	public void setWorkFlowVersion(String workFlowVersion) {
		this.workFlowVersion = workFlowVersion;
	}

	public int getSubActivityFlowChartDefID() {
		return subActivityFlowChartDefID;
	}

	public void setSubActivityFlowChartDefID(int subActivityFlowChartDefID) {
		this.subActivityFlowChartDefID = subActivityFlowChartDefID;
	}

}
