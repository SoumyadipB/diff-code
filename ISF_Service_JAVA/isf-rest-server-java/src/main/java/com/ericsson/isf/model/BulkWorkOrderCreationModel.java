package com.ericsson.isf.model;

public class BulkWorkOrderCreationModel {
	
	private int woCreationID;
	private int projectID;
	private int subActivityID;
	private String assignTO;
	private String nodeType;
	private String nodeNames;
	private String startDate;
	private String startTime;
	private String woName;
	private String workFlowName;
	private String source;
	private String executionPlan;
	private String uploadedBy;
	private int externalSourceID;
	private long sla;
	private String priority;
	private String comment;
	private String inputName;
	private String inputUrl;
	private int doVolume;
	private String createdBy;
	private String externalSourceName;
	
	private String scope_name;
	
	
	
	public String getScope_name() {
		return scope_name;
	}
	public void setScope_name(String scope_name) {
		this.scope_name = scope_name;
	} 
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public int getWoCreationID() {
		return woCreationID;
	}
	public void setWoCreationID(int woCreationID) {
		this.woCreationID = woCreationID;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public int getSubActivityID() {
		return subActivityID;
	}
	public void setSubActivityID(int subActivityID) {
		this.subActivityID = subActivityID;
	}
	public String getAssignTO() {
		return assignTO;
	}
	public void setAssignTO(String assignTO) {
		this.assignTO = assignTO;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getNodeNames() {
		return nodeNames;
	}
	public void setNodeNames(String nodeNames) {
		this.nodeNames = nodeNames;
	}
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getWoName() {
		return woName;
	}
	public void setWoName(String woName) {
		this.woName = woName;
	}
	public String getWorkFlowName() {
		return workFlowName;
	}
	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	
	public String getExecutionPlan() {
		return executionPlan;
	}
	public void setExecutionPlan(String executionPlan) {
		this.executionPlan = executionPlan;
	}
	public String getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	
	public int getExternalSourceID() {
		return externalSourceID;
	}
	public void setExternalSourceID(int externalSourceID) {
		this.externalSourceID = externalSourceID;
	}
	
	public long getSla() {
		return sla;
	}
	public void setSla(long sla) {
		this.sla = sla;
	}
	public String getInputName() {
		return inputName;
	}
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}
	public String getInputUrl() {
		return inputUrl;
	}
	public void setInputUrl(String inputUrl) {
		this.inputUrl = inputUrl;
	}
	public int getDoVolume() {
		return doVolume;
	}
	public void setDoVolume(int doVolume) {
		this.doVolume = doVolume;
	}
	@Override
	public String toString() {
		return "BulkWorkOrderCreationModel [woCreationID=" + woCreationID + ", projectID=" + projectID
				+ ", subActivityID=" + subActivityID + ", assignTO=" + assignTO + ", nodeType=" + nodeType
				+ ", nodeNames=" + nodeNames + ", startDate=" + startDate + ", startTime=" + startTime + ", woName="
				+ woName + ", workFlowName=" + workFlowName + ", source=" + source + ", executionPlan=" + executionPlan
				+ ", uploadedBy=" + uploadedBy + ", externalSourceID=" + externalSourceID + ", sla=" + sla + ", inputName=" + inputName +", inputUrl=" + inputUrl +",doVolume=" + doVolume+"]";
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getExternalSourceName() {
		return externalSourceName;
	}
	public void setExternalSourceName(String externalSourceName) {
		this.externalSourceName = externalSourceName;
	}
	
	
	
	
	
}
