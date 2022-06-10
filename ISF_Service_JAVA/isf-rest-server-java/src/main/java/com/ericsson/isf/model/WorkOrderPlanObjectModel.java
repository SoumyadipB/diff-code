/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author eakinhm
 */
public class WorkOrderPlanObjectModel {

	private String deliverablePlanName;
	private int projectID;
	private String nodeName;
	private String nodeType;
	private String startDate;
	private String startTime;
	private String priority;
	private String assignedTo;
	private String uploadedBy;
	private String comment;
	private String source;
	private String woName;
	private String inputName;
	private String inputUrl;
	
	public void setDeliverablePlanName(String deliverablePlanName) {
		this.deliverablePlanName = deliverablePlanName;
	}

	public String getDeliverablePlanName() {
		return deliverablePlanName;
	}
	
	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
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

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return "WorkOrderPlanObjectModel [deliverablePlanName=" + deliverablePlanName + ", projectID=" + projectID + ", nodeName=" + nodeName + ", nodeType=" + nodeType + ", startDate="
				+ startDate + ", startTime=" + startTime + ", priority=" + priority + ", assignedTo=" + assignedTo
				+ ", comment=" + comment + ", source=" + source + "]";
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getWoName() {
		return woName;
	}

	public void setWoName(String woName) {
		this.woName = woName;
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

}
