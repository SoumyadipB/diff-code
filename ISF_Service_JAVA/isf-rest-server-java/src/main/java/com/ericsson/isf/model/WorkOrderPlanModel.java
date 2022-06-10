/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 *
 * @author eguphee
 */
public class WorkOrderPlanModel {
    
    private int wOPlanID;
    private int projectID;
    private int scopeID;
    private String periodicityDaily;
    private String periodicityWeekly;
    private Date startDate;
    private Time startTime;
    private Date endDate;
    private String signumID;
    private Boolean active;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private String priority;
    private String nodeList;
    private String type;
    private List<WorkOrderPlanNodesModel> listOfNode;
    private List<WorkOrderModel> listOfWorkOrder;
    private List<StandardActivityModel> subActivity;
    private List<ActivityMasterModel> activityDetails;
    private boolean isNodeWise;
	private int doVolume;
    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public List<WorkOrderPlanNodesModel> getListOfNode() {
        return listOfNode;
    }

    public String getNodeList() {
        return nodeList;
    }

    public void setNodeList(String nodeList) {
        this.nodeList = nodeList;
    }
    
    public void setListOfNode(List<WorkOrderPlanNodesModel> listOfNode) {
        this.listOfNode = listOfNode;
    }

    public List<StandardActivityModel> getSubActivity() {
        return subActivity;
    }

    public void setSubActivity(List<StandardActivityModel> subActivity) {
        this.subActivity = subActivity;
    }

    public List<ActivityMasterModel> getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(List<ActivityMasterModel> activityDetails) {
        this.activityDetails = activityDetails;
    }
    
    public List<WorkOrderModel> getListOfWorkOrder() {
        return listOfWorkOrder;
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

    public int getScopeID() {
        return scopeID;
    }

    public void setScopeID(int scopeID) {
        this.scopeID = scopeID;
    }

    public String getPeriodicityDaily() {
        return periodicityDaily;
    }

    public void setPeriodicityDaily(String periodicityDaily) {
        this.periodicityDaily = periodicityDaily;
    }

    public String getPeriodicityWeekly() {
        return periodicityWeekly;
    }

    public void setPeriodicityWeekly(String periodicityWeekly) {
        this.periodicityWeekly = periodicityWeekly;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSignumID() {
        return signumID;
    }

    public void setSignumID(String signumID) {
        this.signumID = signumID;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

	public boolean isNodeWise() {
		return isNodeWise;
	}

	public void setNodeWise(boolean isNodeWise) {
		this.isNodeWise = isNodeWise;
	}

	public int getDoVolume() {
		return doVolume;
	}

	public void setDoVolume(int doVolume) {
		this.doVolume = doVolume;
	}

	@Override
	public String toString() {
		return "WorkOrderPlanModel [wOPlanID=" + wOPlanID + ", projectID=" + projectID + ", scopeID=" + scopeID
				+ ", periodicityDaily=" + periodicityDaily + ", periodicityWeekly=" + periodicityWeekly + ", startDate="
				+ startDate + ", startTime=" + startTime + ", endDate=" + endDate + ", signumID=" + signumID
				+ ", active=" + active + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedDate=" + lastModifiedDate + ", priority="
				+ priority + ", nodeList=" + nodeList + ", type=" + type + ", listOfNode=" + listOfNode
				+ ", listOfWorkOrder=" + listOfWorkOrder + ", subActivity=" + subActivity + ", activityDetails="
				+ activityDetails + ", isNodeWise=" + isNodeWise + ", doVolume=" + doVolume + "]";
	}
    
}
