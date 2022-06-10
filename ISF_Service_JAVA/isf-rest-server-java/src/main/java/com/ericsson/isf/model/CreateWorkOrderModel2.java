/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author edhhklu
 */
public class CreateWorkOrderModel2 {

	private int wOPlanID;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private String signumID;
	private String createBy;
	private String priority;
	private int wfVersion;
	private String node;
	private int woId;
	private int projectID;
	private int flowchartDefId;
	private Integer parentWorkOrderId;
	private int currentCount;
	private String status;
	private int doID;
	private int subActivityID;
	private String woName;
	private float slaHrs;
	private String nodeType;
	private String activity;
	private String subActivity;
	private String uploadedBy;


	public CreateWorkOrderModel2() {
	}

	public CreateWorkOrderModel2(CreateWorkOrderModel createWorkOrderModel, int doID) {

		this.wOPlanID = createWorkOrderModel.getwOPlanID();
		this.plannedStartDate = createWorkOrderModel.getPlannedStartDate();
		this.plannedEndDate = createWorkOrderModel.getPlannedEndDate();

		if (CollectionUtils.isNotEmpty(createWorkOrderModel.getLstSignumID())
				&& StringUtils.isNotBlank(createWorkOrderModel.getLstSignumID().get(0))) {

			this.signumID = createWorkOrderModel.getLstSignumID().get(0);
		} else {
			this.signumID = null;
		}

		this.createBy = createWorkOrderModel.getCreatedBy();
		this.priority = createWorkOrderModel.getPriority();
		this.wfVersion = createWorkOrderModel.getWfVersion();
		if (CollectionUtils.isNotEmpty(createWorkOrderModel.getListOfNode())) {

			this.node = createWorkOrderModel.getListOfNode().get(0).getNodeNames();
			this.nodeType = createWorkOrderModel.getListOfNode().get(0).getNodeType();
		}
		this.projectID = createWorkOrderModel.getProjectID();
		this.flowchartDefId = createWorkOrderModel.getFlowchartDefId();
		this.doID = doID;
		this.woName = createWorkOrderModel.getwOName();
		this.subActivityID = createWorkOrderModel.getSubActivityID();
		this.slaHrs = createWorkOrderModel.getSlaHrs();
		this.parentWorkOrderId = createWorkOrderModel.getParentWOID();
		this.activity=createWorkOrderModel.getActivity();
		this.subActivity = createWorkOrderModel.getSubActivity();
		this.status = createWorkOrderModel.getStatus();
		this.uploadedBy = createWorkOrderModel.getUploadedBy();
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
	
	public Integer getParentWorkOrderId() {
		return parentWorkOrderId;
	}

	public void setParentWorkOrderId(Integer parentWorkOrderId) {
		this.parentWorkOrderId = parentWorkOrderId;
	}

	public int getFlowchartDefId() {
		return flowchartDefId;
	}

	public void setFlowchartDefId(int flowchartDefId) {
		this.flowchartDefId = flowchartDefId;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public int getWoId() {
		return woId;
	}

	public void setWoId(int woId) {
		this.woId = woId;
	}

	public int getwOPlanID() {
		return wOPlanID;
	}

	public void setwOPlanID(int wOPlanID) {
		this.wOPlanID = wOPlanID;
	}

	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public Date getPlannedEndDate() {
		return plannedEndDate;
	}

	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}

	public String getSignumID() {
		return signumID;
	}

	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public int getWfVersion() {
		return wfVersion;
	}

	public void setWfVersion(int wfVersion) {
		this.wfVersion = wfVersion;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getDoID() {
		return doID;
	}

	public void setDoID(int doID) {
		this.doID = doID;
	}

	public int getSubActivityID() {
		return subActivityID;
	}

	public void setSubActivityID(int subActivityID) {
		this.subActivityID = subActivityID;
	}

	public String getWoName() {
		return woName;
	}

	public void setWoName(String woName) {
		this.woName = woName;
	}

	public float getSlaHrs() {
		return slaHrs;
	}

	public void setSlaHrs(float slaHrs) {
		this.slaHrs = slaHrs;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getSubActivity() {
		return subActivity;
	}

	public void setSubActivity(String subActivity) {
		this.subActivity = subActivity;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	
}
