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
 * @author esanpup
 */
public class CRManagementModel {
    
    
    private String requestedBy;
    private Date requestedOn;
    private String nextStep;
    private int projectID;
    private int resourceRequestID;
    private int resourcePositionID;
    private String positionStatus;
    private int workEffortID;
    private String currentSignumID;
    private String proposedSignumID;
    private Date currentStartDate;
    private Date proposedStartDate;
    private Date currentEndDate;
    private Date proposedEndDate;
    private Date actionTakenOn;
    private String actionTakenBy;
    private String cRStatus;
    private String comments;
    private int cRID;
    private String loggedInUser;
    private  String createdBy;
    private String createdOn;
    private WorkEffortModel existingWeDetails;
    private WorkEffortModel proposedWeDetails;
    
    
    
    

	public WorkEffortModel getExistingWeDetails() {
		return existingWeDetails;
	}

	public void setExistingWeDetails(WorkEffortModel existingWeDetails) {
		this.existingWeDetails = existingWeDetails;
	}

	public WorkEffortModel getProposedWeDetails() {
		return proposedWeDetails;
	}

	public void setProposedWeDetails(WorkEffortModel proposedWeDetails) {
		this.proposedWeDetails = proposedWeDetails;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public int getcRID() {
		return cRID;
	}

	public void setcRID(int cRID) {
		this.cRID = cRID;
	}

	public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Date getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(Date requestedOn) {
        this.requestedOn = requestedOn;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getResourceRequestID() {
        return resourceRequestID;
    }

    public void setResourceRequestID(int resourceRequestID) {
        this.resourceRequestID = resourceRequestID;
    }

    public int getResourcePositionID() {
        return resourcePositionID;
    }

    public void setResourcePositionID(int resourcePositionID) {
        this.resourcePositionID = resourcePositionID;
    }

    public String getPositionStatus() {
        return positionStatus;
    }

    public void setPositionStatus(String positionStatus) {
        this.positionStatus = positionStatus;
    }

    public int getWorkEffortID() {
        return workEffortID;
    }

    public void setWorkEffortID(int workEffortID) {
        this.workEffortID = workEffortID;
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

    public Date getCurrentStartDate() {
        return currentStartDate;
    }

    public void setCurrentStartDate(Date currentStartDate) {
        this.currentStartDate = currentStartDate;
    }

    public Date getProposedStartDate() {
        return proposedStartDate;
    }

    public void setProposedStartDate(Date proposedStartDate) {
        this.proposedStartDate = proposedStartDate;
    }

    public Date getCurrentEndDate() {
        return currentEndDate;
    }

    public void setCurrentEndDate(Date currentEndDate) {
        this.currentEndDate = currentEndDate;
    }

    public Date getProposedEndDate() {
        return proposedEndDate;
    }

    public void setProposedEndDate(Date proposedEndDate) {
        this.proposedEndDate = proposedEndDate;
    }

    public Date getActionTakenOn() {
        return actionTakenOn;
    }

    public void setActionTakenOn(Date actionTakenOn) {
        this.actionTakenOn = actionTakenOn;
    }

    public String getActionTakenBy() {
        return actionTakenBy;
    }

    public void setActionTakenBy(String actionTakenBy) {
        this.actionTakenBy = actionTakenBy;
    }

    public String getcRStatus() {
        return cRStatus;
    }

    public void setcRStatus(String cRStatus) {
        this.cRStatus = cRStatus;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    
    
    
}
