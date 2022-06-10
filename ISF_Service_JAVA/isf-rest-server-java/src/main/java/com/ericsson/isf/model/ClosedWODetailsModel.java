/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author ekumvsu
 */
public class ClosedWODetailsModel {
    private int wOID;
    private String wOName;
    private String nodeType;
    private String nodeName;
    private String closedOn;
    private String closedBy;
    private String comment;
    private String scopeName;
    private String plannedOn;
    private String workFlowName;
    private String signum;
    private String acceptedOrRejectedBy;
    private String acceptedOrRejectedDate;
    
    private int doid;
    private String deliverableUnitName;
    private int woplanid;

	private int wfID;
    private int projectID;
    private int proficiencyID;
    private String proficiencyName;
    
    public int getProficiencyID() {
		return proficiencyID;
	}

	public void setProficiencyID(int proficiencyID) {
		this.proficiencyID = proficiencyID;
	}

	public int getWfID() {
		return wfID;
	}

	public void setWfID(int wfID) {
		this.wfID = wfID;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
    
    public String getAcceptedOrRejectedDate() {
		return acceptedOrRejectedDate;
	}

	public void setAcceptedOrRejectedDate(String acceptedOrRejectedDate) {
		this.acceptedOrRejectedDate = acceptedOrRejectedDate;
	}

	public String getAcceptedOrRejectedBy() {
		return acceptedOrRejectedBy;
	}

	public void setAcceptedOrRejectedBy(String acceptedOrRejectedBy) {
		this.acceptedOrRejectedBy = acceptedOrRejectedBy;
	}

	public String getSignum() {
		return signum;
	}

	public void setSignum(String signum) {
		this.signum = signum;
	}

	private String createdBy;
    
    
    
    
    
    public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getWorkFlowName() {
		return workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	public String getPlannedOn() {
		return plannedOn;
	}

	public void setPlannedOn(String plannedOn) {
		this.plannedOn = plannedOn;
	}

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	private List<WorkOrderActivityDetailsModel> activityDetails;

    public List<WorkOrderActivityDetailsModel> getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(List<WorkOrderActivityDetailsModel> activityDetails) {
        this.activityDetails = activityDetails;
    }

    public String getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(String closedOn) {
        this.closedOn = closedOn;
    }
    
    public int getwOID() {
        return wOID;
    }

    public void setwOID(int wOID) {
        this.wOID = wOID;
    }

    public String getwOName() {
        return wOName;
    }

    public void setwOName(String wOName) {
        this.wOName = wOName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
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

	public int getWoplanid() {
		return woplanid;
	}

	public void setWoplanid(int woplanid) {
		this.woplanid = woplanid;
	}

	public String getProficiencyName() {
		return proficiencyName;
	}

	public void setProficiencyName(String proficiencyName) {
		this.proficiencyName = proficiencyName;
	}

	
    
    
    
    
}
