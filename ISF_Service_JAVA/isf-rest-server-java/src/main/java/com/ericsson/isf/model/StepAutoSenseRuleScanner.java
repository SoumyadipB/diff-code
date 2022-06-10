package com.ericsson.isf.model;

public class StepAutoSenseRuleScanner {
	private int stepAutoSenseRuleScannerID;
	private int wOID;
	private String stepID;
	private String scanningStatus;
	private String taskActionName;
	private String signumId;
	private int fCStepDetailsID;
	private int subActivityFlowChartDefID;
	private String overrideActionName;
	private String source;
	
	public String getOverrideActionName() {
		return overrideActionName;
	}
	public void setOverrideActionName(String overrideActionName) {
		this.overrideActionName = overrideActionName;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getfCStepDetailsID() {
		return fCStepDetailsID;
	}
	public void setfCStepDetailsID(int fCStepDetailsID) {
		this.fCStepDetailsID = fCStepDetailsID;
	}
	
	public int getStepAutoSenseRuleScannerID() {
		return stepAutoSenseRuleScannerID;
	}
	public void setStepAutoSenseRuleScannerID(int stepAutoSenseRuleScannerID) {
		this.stepAutoSenseRuleScannerID = stepAutoSenseRuleScannerID;
	}
	
	public String getScanningStatus() {
		return scanningStatus;
	}
	public void setScanningStatus(String scanningStatus) {
		this.scanningStatus = scanningStatus;
	}
	public String getTaskActionName() {
		return taskActionName;
	}
	public void setTaskActionName(String taskActionName) {
		this.taskActionName = taskActionName;
	}
	public String getSignumId() {
		return signumId;
	}
	public void setSignumId(String signumId) {
		this.signumId = signumId;
	}
	public int getwOID() {
		return wOID;
	}
	public void setwOID(int wOID) {
		this.wOID = wOID;
	}
	public String getStepID() {
		return stepID;
	}
	public void setStepID(String stepID) {
		this.stepID = stepID;
	}
	public int getSubActivityFlowChartDefID() {
		return subActivityFlowChartDefID;
	}
	public void setSubActivityFlowChartDefID(int subActivityFlowChartDefID) {
		this.subActivityFlowChartDefID = subActivityFlowChartDefID;
	}
	

}
