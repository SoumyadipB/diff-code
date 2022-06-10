/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

/**
 *
 * @author eguphee
 */
public class WorkOrderPlanNodesModel {
    
	private int wNID;
    private int wOPlanID;
    private String nodeType;
    private String nodeNames;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private int parentWorkOrderID;
    private String market;
    private Integer networkElementID;
    private boolean active;
    
    public WorkOrderPlanNodesModel() {}
    
    public WorkOrderPlanNodesModel(String nodeType, String nodeNames) {
		this.nodeType = nodeType;
		this.nodeNames = nodeNames;
	}
    
    public int getParentWorkOrderID() {
		return parentWorkOrderID;
	}

	public void setParentWorkOrderID(int parentWorkOrderID) {
		this.parentWorkOrderID = parentWorkOrderID;
	}

	public int getwNID() {
        return wNID;
    }

    public void setwNID(int wNID) {
        this.wNID = wNID;
    }

    public int getwOPlanID() {
        return wOPlanID;
    }

    public void setwOPlanID(int wOPlanID) {
        this.wOPlanID = wOPlanID;
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

   

	@Override
	public String toString() {
		return "WorkOrderPlanNodesModel [wNID=" + wNID + ", wOPlanID=" + wOPlanID + ", nodeType=" + nodeType
				+ ", nodeNames=" + nodeNames + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedDate=" + lastModifiedDate
				+ ", parentWorkOrderID=" + parentWorkOrderID + ", market=" + market + ", networkElementID="
				+ networkElementID + ", active=" + active + "]";
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public Integer getNetworkElementID() {
		return networkElementID;
	}

	public void setNetworkElementID(Integer networkElementID) {
		this.networkElementID = networkElementID;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

   
}
