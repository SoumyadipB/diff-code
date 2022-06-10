package com.ericsson.isf.model;

import java.util.List;

public class ProjectQueueWorkOrderBasicDetailsModel {
	private int woId;
	private String woName;
	private String priority;
	private String priorityModifiedOn;

	private String startDate;
	private String endDate;
	private String subActivity;

	private String projectId;
	private String projectName;
	private String workFlowName;
	private String employeeName;

	private List<WorkOrderNodesModel> listOfNode;
	private int woPlanId;

	private String technology;
	private String signum;

	private int doid;

	private String deliverableName;

	private Boolean workOrderAutoSenseEnabled;

	private int srID;
	private int defID;
	private int recordsTotal;

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public int getSrID() {
		return srID;
	}

	public void setSrID(int srID) {
		this.srID = srID;
	}

	public Boolean getWorkOrderAutoSenseEnabled() {
		return workOrderAutoSenseEnabled;
	}

	public void setWorkOrderAutoSenseEnabled(Boolean workOrderAutoSenseEnabled) {
		this.workOrderAutoSenseEnabled = workOrderAutoSenseEnabled;
	}

	public int getDoid() {
		return doid;
	}

	public void setDoid(int doid) {
		this.doid = doid;
	}

	public String getDeliverableName() {
		return deliverableName;
	}

	public void setDeliverableName(String deliverableName) {
		this.deliverableName = deliverableName;
	}

	public int getWoPlanId() {
		return woPlanId;
	}

	public void setWoPlanId(int woPlanId) {
		this.woPlanId = woPlanId;
	}

	public List<WorkOrderNodesModel> getListOfNode() {
		return listOfNode;
	}

	public void setListOfNode(List<WorkOrderNodesModel> listOfNode) {
		this.listOfNode = listOfNode;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public int getWoId() {
		return woId;
	}

	public void setWoId(int woId) {
		this.woId = woId;
	}

	public String getWoName() {
		return woName;
	}

	public void setWoName(String woName) {
		this.woName = woName;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getSubActivity() {
		return subActivity;
	}

	public void setSubActivity(String subActivity) {
		this.subActivity = subActivity;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getWorkFlowName() {
		return workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getSignum() {
		return signum;
	}

	public void setSignum(String signum) {
		this.signum = signum;
	}

	public String getPriorityModifiedOn() {
		return priorityModifiedOn;
	}

	public void setPriorityModifiedOn(String priorityModifiedOn) {
		this.priorityModifiedOn = priorityModifiedOn;
	}

	public int getDefID() {
		return defID;
	}

	public void setDefID(int defID) {
		this.defID = defID;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	

}
