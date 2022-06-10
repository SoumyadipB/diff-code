/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

import com.ericsson.isf.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 * @author esanpup
 */
public class UserAccessProfileModel {
    
    private int userAccessProfileID;
    private String signumID;
    private int accessProfileID;
    private String approvalStatus;
    private String approvedBy;
    private Date approvedDate;
    private Date endDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    boolean active;
    private String organisation;
    private int organisationID;
    private int accessRoleID;
    private String role;
    private String accessProfileName;
    private String managerSignum;
    private int totalCounts;
    
    public int getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(int totalCounts) {
		this.totalCounts = totalCounts;
	}

	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=AppConstants.DEFAULT_DATE_FORMAT,timezone =AppConstants.TIMEZONE_IST)
    public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getManagerSignum() {
		return managerSignum;
	}

	public void setManagerSignum(String managerSignum) {
		this.managerSignum = managerSignum;
	}

	public String getOrganisation() {
		return organisation;
	}

	public String getAccessProfileName() {
        return accessProfileName;
    }

    public void setAccessProfileName(String accessProfileName) {
        this.accessProfileName = accessProfileName;
    }
    
    public String getOrganisationName() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public int getOrganisationID() {
        return organisationID;
    }

    public void setOrganisationID(int organisationID) {
        this.organisationID = organisationID;
    }

    public int getAccessRoleID() {
        return accessRoleID;
    }

    public void setAccessRoleID(int accessRoleID) {
        this.accessRoleID = accessRoleID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public int getUserAccessProfileID() {
        return userAccessProfileID;
    }

    public void setUserAccessProfileID(int userAccessProfileID) {
        this.userAccessProfileID = userAccessProfileID;
    }

    public String getSignumID() {
        return signumID;
    }

    public void setSignumID(String signumID) {
        this.signumID = signumID;
    }

    public int getAccessProfileID() {
        return accessProfileID;
    }

    public void setAccessProfileID(int accessProfileID) {
        this.accessProfileID = accessProfileID;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
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
