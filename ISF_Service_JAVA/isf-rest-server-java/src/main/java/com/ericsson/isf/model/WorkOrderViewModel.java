/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

/**
 *
 * @author ekumvsu
 */
public class WorkOrderViewModel {
    
    private int wOID;
    private String wOName;
    private String startDate;
    private String endDate;
    private String assignedTo;
    private String status;
    private String woHours;
    private String woDays;
    private String subActivityID;
    private String nodeType;
    private String nodeCount;
    private String nodeNames;
    private String actualStartDate;
    private String actualEndDate;
    private String priority;
    private String createdby;
    private int proficiencyID;
    private String proficiencyName;
    public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	private List<WOBookingDetailsModel> listOfBookingDetails;

    public String getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(String actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public String getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(String actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public List<WOBookingDetailsModel> getListOfBookingDetails() {
        return listOfBookingDetails;
    }

    public void setListOfBookingDetails(List<WOBookingDetailsModel> listOfBookingDetails) {
        this.listOfBookingDetails = listOfBookingDetails;
    }

    public String getWoHours() {
        return woHours;
    }

    public void setWoHours(String woHours) {
        this.woHours = woHours;
    }

    public String getWoDays() {
        return woDays;
    }

    public void setWoDays(String woDays) {
        this.woDays = woDays;
    }

    public String getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(String subActivityID) {
        this.subActivityID = subActivityID;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(String nodeCount) {
        this.nodeCount = nodeCount;
    }

    public String getNodeNames() {
        return nodeNames;
    }

    public void setNodeNames(String nodeNames) {
        this.nodeNames = nodeNames;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

	public int getProficiencyID() {
		return proficiencyID;
	}

	public void setProficiencyID(int proficiencyID) {
		this.proficiencyID = proficiencyID;
	}

	public String getProficiencyName() {
		return proficiencyName;
	}

	public void setProficiencyName(String proficiencyName) {
		this.proficiencyName = proficiencyName;
	}

    
}
