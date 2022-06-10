/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

/**
 *
 * @author eabhmoj
 */
public class ActivityScopeModel {
private int activityScopeID;
private int subActivityID;
private int avgEstdEffort;
private int projectScopeDetailID;

    public int getActivityScopeID() {
        return activityScopeID;
    }

    public void setActivityScopeID(int activityScopeID) {
        this.activityScopeID = activityScopeID;
    }

    public int getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
    }

    public int getAvgEstdEffort() {
        return avgEstdEffort;
    }

    public void setAvgEstdEffort(int avgEstdEffort) {
        this.avgEstdEffort = avgEstdEffort;
    }

    public int getProjectScopeDetailID() {
        return projectScopeDetailID;
    }

    public void setProjectScopeDetailID(int projectScopeDetailID) {
        this.projectScopeDetailID = projectScopeDetailID;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
private String createdBy;
private Date createdDate;
private String lastModifiedBy;
private Date lastModifiedDate;
private boolean active;
}
