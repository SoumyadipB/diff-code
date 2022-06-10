/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

/**
 *
 * @author esanpup
 */
public class WorkEffortModel {
    
    private int workEffortID;
    private int resourcePositionID;
    private Date startDate;
    private Date endDate;
    private int duration;
    private float fte_Percent;
    private float hours;
    private String signum;
    private String workEffortStatus;
    private boolean isActive;
    private int cRID;
    private String createdBy;
    private Date createdOn;
    private Date lastModifiedOn;
    private String lastModifiedBy;
    private String allocatedBy;
    private String positionStatus;

    public String getPositionStatus() {
		return positionStatus;
	}

	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getWorkEffortID() {
        return workEffortID;
    }

    public void setWorkEffortID(int workEffortID) {
        this.workEffortID = workEffortID;
    }

    public int getResourcePositionID() {
        return resourcePositionID;
    }

    public void setResourcePositionID(int resourcePositionID) {
        this.resourcePositionID = resourcePositionID;
    }

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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getFte_Percent() {
        return fte_Percent;
    }

    public void setFte_Percent(float fte_Percent) {
        this.fte_Percent = fte_Percent;
    }

    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    public String getSignum() {
        return signum;
    }

    public void setSignum(String signum) {
        this.signum = signum;
    }

    public String getWorkEffortStatus() {
        return workEffortStatus;
    }

    public void setWorkEffortStatus(String workEffortStatus) {
        this.workEffortStatus = workEffortStatus;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getcRID() {
        return cRID;
    }

    public void setcRID(int cRID) {
        this.cRID = cRID;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getAllocatedBy() {
        return allocatedBy;
    }

    public void setAllocatedBy(String allocatedBy) {
        this.allocatedBy = allocatedBy;
    }
    
    
    
    
}
