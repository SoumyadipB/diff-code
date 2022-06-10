/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

import com.ericsson.isf.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

public class WorkOrderDetailsSearchModel {
	
	private int woId;
    private String woName;
    private String priority;
    private String priorityModifiedOn;
    private String status;
    private String loe;
    private Date startDate;
    private Date endDate;
    
    private String activity;
    private String subActivity;
    private String nodes;
    private String subActivityId;
    private String projectId;
    private String projectName;
    private String workFlowName;
    private String employeeName;
    private String versionNumber;
    //private List<WorkOrderNodesModel> listOfNode;
    private String woPlanId;
    private String slaHours;
    private String technology;
    private String signum;
    
    private int doid;
    private String deliverableUnitName;
    private String deliverableName;
    private int defID;
    private Boolean workOrderAutoSenseEnabled;
    private String type;
    private int proficiencyID;
    private int srID;
  
    private int totalCounts;
    private String nodeType;
    private String nodeNames;
    private String market;
    
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

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}
    
    public int getSrID() {
		return srID;
	}

	public void setSrID(int srID) {
		this.srID = srID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getDefID() {
		return defID;
	}

	public void setDefID(int defID) {
		this.defID = defID;
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

	public String getDeliverableUnitName() {
		return deliverableUnitName;
	}

	public void setDeliverableUnitName(String deliverableUnitName) {
		this.deliverableUnitName = deliverableUnitName;
	}

	public String getDeliverableName() {
		return deliverableName;
	}

	public void setDeliverableName(String deliverableName) {
		this.deliverableName = deliverableName;
	}

    
    public String getWoPlanId() {
		return woPlanId;
	}
	public void setWoPlanId(String woPlanId) {
		this.woPlanId = woPlanId;
	}
//	public List<WorkOrderNodesModel> getListOfNode() {
//		return listOfNode;
//	}
//	public void setListOfNode(List<WorkOrderNodesModel> listOfNode) {
//		this.listOfNode = listOfNode;
//	}
	public String getSubActivityId() {
		return subActivityId;
	}
	public void setSubActivityId(String subActivityId) {
		this.subActivityId = subActivityId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLoe() {
		return loe;
	}
	public void setLoe(String loe) {
		this.loe = loe;
	}
	//@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getSubActivity() {
		return subActivity;
	}
	public void setSubActivity(String subActivity) {
		this.subActivity = subActivity;
	}
	public String getNodes() {
		return nodes;
	}
	public void setNodes(String nodes) {
		this.nodes = nodes;
	}
	
	//@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	public String getSlaHours() {
		return slaHours;
	}
	public void setSlaHours(String slaHours) {
		this.slaHours = slaHours;
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

	public int getProficiencyID() {
		return proficiencyID;
	}

	public void setProficiencyID(int proficiencyID) {
		this.proficiencyID = proficiencyID;
	}

	public int getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(int totalCounts) {
		this.totalCounts = totalCounts;
	}
	
}
