/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.sql.Time;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author eguphee
 */

public class WorkOrderPlanModel2 {

	private int wOPlanID;
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	private int projectID;
	// private int scopeID;
	private int subActivityID;
	// private String periodicityDaily;
	// private String periodicityWeekly;
	private Date startDate;
	// private Time startTime;
	private Date endDate;
	private String wOName;
	// private String signumID;
	// private Boolean active;
	// private String createdBy;
	// private Date createdDate;
	// private String lastModifiedBy;
	private Date lastModifiedDate;
	// private String priority;
	// private String nodeList;
	private float slaHrs;
	private String type;
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String deliverableName;
	private String deliverableUnitName;
	private int totalCount;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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

	public float getSlaHrs() {
		return slaHrs;
	}

	public List<ActivityModel> getSubActivity() {
		return subActivity;
	}

	public void setSubActivity(List<ActivityModel> subActivity) {
		this.subActivity = subActivity;
	}

	public void setSlaHrs(float slaHrs) {
		this.slaHrs = slaHrs;
	}

	private List<WorkOrderModel> listOfWorkOrder;
	private List<ActivityModel> subActivity; 
	private List<ActivityMasterModel> activityDetails;
	// private List<NetworkElementModel> listOfMarket;

	public List<WorkOrderModel> getListOfWorkOrder() {
		return listOfWorkOrder;
	}

	public List<ActivityMasterModel> getActivityDetails() {
		return activityDetails;
	}

	public void setActivityDetails(List<ActivityMasterModel> activityDetails) {
		this.activityDetails = activityDetails;
	}

	public void setListOfWorkOrder(List<WorkOrderModel> listOfWorkOrder) {
		this.listOfWorkOrder = listOfWorkOrder;
	}

	public int getwOPlanID() {
		return wOPlanID;
	}

	public void setwOPlanID(int wOPlanID) {
		this.wOPlanID = wOPlanID;
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

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getwOName() {
		return wOName;
	}

	public void setwOName(String wOName) {
		this.wOName = wOName;
	}

	@Override
	public String toString() {
		return "WorkOrderPlanModel2 [wOPlanID=" + wOPlanID + ", projectID=" + projectID + ", subActivityID="
				+ subActivityID + ", wOName=" + wOName + ", lastModifiedDate=" + lastModifiedDate + ", slaHrs=" + slaHrs
				+ ", deliverableName=" + deliverableName + ", deliverableUnitName=" + deliverableUnitName
				+ ", listOfWorkOrder=" + listOfWorkOrder + ", subActivity=" + subActivity + ", activityDetails="
				+ activityDetails + "]";
	}

}
