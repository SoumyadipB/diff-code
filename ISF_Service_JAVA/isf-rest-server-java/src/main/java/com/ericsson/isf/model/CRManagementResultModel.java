/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author esanpup
 */
public class CRManagementResultModel {
	
	
	

     
	private String currentSignumID;
	private String proposedSignumID;
	private String currentStartDate;
	private String proposedStartDate;
	private String currentEndDate;
	private String proposedEndDate;
    private int resourcePositionID;
     
     
    

	private int crID;
    private String previouscomment;
    private int resourceRequestID;
    private String requestedBy;
    private String requestedOn;
    private String status;
    private String comments;
    private String actionType;
    private String positionStatus;
    private Integer workEffortIdExisting;
    private int workEffortIdProposed;
    private String actionTakenBy;
    private String actionTakenon;
    private int projectID;

    
    
    
    public String getPreviouscomment() {
		return previouscomment;
	}

	public void setPreviouscomment(String previouscomment) {
		this.previouscomment = previouscomment;
	}

	public int getCrID() {
        return crID;
    }

    public void setCrID(int crID) {
        this.crID = crID;
    }

   public int getResourcePositionID() {
        return resourcePositionID;
    }

    public void setResourcePositionID(int resourcePositionID) {
        this.resourcePositionID = resourcePositionID;
    }

    public int getResourceRequestID() {
        return resourceRequestID;
    }

    public void setResourceRequestID(int resourceRequestID) {
        this.resourceRequestID = resourceRequestID;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(String requestedOn) {
        this.requestedOn = requestedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getPositionStatus() {
        return positionStatus;
    }

    public void setPositionStatus(String positionStatus) {
        this.positionStatus = positionStatus;
    }

   

    public Integer getWorkEffortIdExisting() {
		return workEffortIdExisting;
	}

	public void setWorkEffortIdExisting(Integer workEffortIdExisting) {
		this.workEffortIdExisting = workEffortIdExisting;
	}

	public int getWorkEffortIdProposed() {
        return workEffortIdProposed;
    }

    public void setWorkEffortIdProposed(int workEffortIdProposed) {
        this.workEffortIdProposed = workEffortIdProposed;
    }

    public String getActionTakenBy() {
        return actionTakenBy;
    }

    public void setActionTakenBy(String actionTakenBy) {
        this.actionTakenBy = actionTakenBy;
    }

    public String getActionTakenon() {
        return actionTakenon;
    }

    public void setActionTakenon(String actionTakenon) {
        this.actionTakenon = actionTakenon;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

	public String getCurrentSignumID() {
		return currentSignumID;
	}

	public void setCurrentSignumID(String currentSignumID) {
		this.currentSignumID = currentSignumID;
	}

	public String getProposedSignumID() {
		return proposedSignumID;
	}

	public void setProposedSignumID(String proposedSignumID) {
		this.proposedSignumID = proposedSignumID;
	}

	public String getCurrentStartDate() {
		return currentStartDate;
	}

	public void setCurrentStartDate(String currentStartDate) {
		this.currentStartDate = currentStartDate;
	}

	public String getProposedStartDate() {
		return proposedStartDate;
	}

	public void setProposedStartDate(String proposedStartDate) {
		this.proposedStartDate = proposedStartDate;
	}

	public String getCurrentEndDate() {
		return currentEndDate;
	}

	public void setCurrentEndDate(String currentEndDate) {
		this.currentEndDate = currentEndDate;
	}

	public String getProposedEndDate() {
		return proposedEndDate;
	}

	public void setProposedEndDate(String proposedEndDate) {
		this.proposedEndDate = proposedEndDate;
	}
    
}
