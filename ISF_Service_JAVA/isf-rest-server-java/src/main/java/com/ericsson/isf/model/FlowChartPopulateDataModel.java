package com.ericsson.isf.model;

public class FlowChartPopulateDataModel {
	private int flowChartDefId;
	private String projectIDSubactivityIDLoggedInSignum;
	private String loggedInSignum;
	private int subActivityID;
	private int subActivityFlowchartStepDefId;
	private String stepId;
	private String stepName;
	private String taskName;
	private String taskId;
	private String executionType;
	private String reason;
	private String toolId;
	private int versionNumber;
	private String masterTask;
	private String type;
	private String rpaId;
	private boolean fileBased;
	private String wiId;
	private int oldDefID;
	private int oldVersionNo;
	private String outputUpload;
	private String cascadeInput;
	private int fCStepDetailsID;
    private boolean expertType;
    private boolean experiencedView;
    public boolean isExperiencedView() {
		return experiencedView;
	}

	public void setExperiencedView(boolean experiencedView) {
		this.experiencedView = experiencedView;
	}

	private String viewMode;
    private int wFID;
    
	public int getwFID() {
		return wFID;
	}

	public void setwFID(int wFID) {
		this.wFID = wFID;
	}

	public boolean getExpertType() {
		return expertType;
	}

	public void setExpertType(boolean expertType) {
		this.expertType = expertType;
	}

	public int getSubActivityID() {
		return subActivityID;
	}

	public void setSubActivityID(int subActivityID) {
		this.subActivityID = subActivityID;
	}

	public int getSubActivityFlowchartStepDefId() {
		return subActivityFlowchartStepDefId;
	}

	public void setSubActivityFlowchartStepDefId(int subActivityFlowchartStepDefId) {
		this.subActivityFlowchartStepDefId = subActivityFlowchartStepDefId;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getExecutionType() {
		return executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getToolId() {
		return toolId;
	}

	public void setToolId(String toolId) {
		this.toolId = toolId;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getMasterTask() {
		return masterTask;
	}

	public void setMasterTask(String masterTask) {
		this.masterTask = masterTask;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRpaId() {
		return rpaId;
	}

	public void setRpaId(String rpaId) {
		this.rpaId = rpaId;
	}

	public boolean isFileBased() {
		return fileBased;
	}

	public void setFileBased(boolean fileBased) {
		this.fileBased = fileBased;
	}

	public String getWiId() {
		return wiId;
	}

	public void setWiId(String wiId) {
		this.wiId = wiId;
	}

	public int getOldDefID() {
		return oldDefID;
	}

	public void setOldDefID(int oldDefID) {
		this.oldDefID = oldDefID;
	}

	public int getOldVersionNo() {
		return oldVersionNo;
	}

	public void setOldVersionNo(int oldVersionNo) {
		this.oldVersionNo = oldVersionNo;
	}

	public String getOutputUpload() {
		return outputUpload;
	}

	public void setOutputUpload(String outputUpload) {
		this.outputUpload = outputUpload;
	}

	public String getCascadeInput() {
		return cascadeInput;
	}

	public void setCascadeInput(String cascadeInput) {
		this.cascadeInput = cascadeInput;
	}

	public int getfCStepDetailsID() {
		return fCStepDetailsID;
	}

	public void setfCStepDetailsID(int fCStepDetailsID) {
		this.fCStepDetailsID = fCStepDetailsID;
	}

	public int getFlowChartDefId() {
		return flowChartDefId;
	}

	public void setFlowChartDefId(int flowChartDefId) {
		this.flowChartDefId = flowChartDefId;
	}

   public String getProjectIDSubactivityIDLoggedInSignum() {
		return projectIDSubactivityIDLoggedInSignum;
	}

	public void setProjectIDSubactivityIDLoggedInSignum(String projectIDSubactivityIDLoggedInSignum) {
		this.projectIDSubactivityIDLoggedInSignum = projectIDSubactivityIDLoggedInSignum;
	}

	public String getLoggedInSignum() {
		return loggedInSignum;
	}

	public void setLoggedInSignum(String loggedInSignum) {
		this.loggedInSignum = loggedInSignum;
	}

	public String getViewMode() {
		return viewMode;
	}

	public void setViewMode(String viewMode) {
		this.viewMode = viewMode;
	}

}
