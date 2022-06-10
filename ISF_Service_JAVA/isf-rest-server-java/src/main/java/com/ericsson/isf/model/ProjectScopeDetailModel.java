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
public class ProjectScopeDetailModel{
    
private int projectScopeDetailID;
private int projectScopeID;
private int domainID;
private int serviceAreaID;
private int technologyID;
private String createdBy;
private Date createdDate;
private String lastModifiedBy;
private Date lastModifiedDate;
private boolean active;
private String loggedInUser;

    public String getLoggedInUser() {
	return loggedInUser;
}

public void setLoggedInUser(String loggedInUser) {
	this.loggedInUser = loggedInUser;
}

	public int getProjectScopeDetailID() {
        return projectScopeDetailID;
    }

    public void setProjectScopeDetailID(int projectScopeDetailID) {
        this.projectScopeDetailID = projectScopeDetailID;
    }

    public int getProjectScopeID() {
        return projectScopeID;
    }

    public void setProjectScopeID(int projectScopeID) {
        this.projectScopeID = projectScopeID;
    }

    public int getDomainID() {
        return domainID;
    }

    public void setDomainID(int domainID) {
        this.domainID = domainID;
    }

    public int getServiceAreaID() {
        return serviceAreaID;
    }

    public void setServiceAreaID(int serviceAreaID) {
        this.serviceAreaID = serviceAreaID;
    }

    public int getTechnologyID() {
        return technologyID;
    }

    public void setTechnologyID(int technologyID) {
        this.technologyID = technologyID;
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


}
