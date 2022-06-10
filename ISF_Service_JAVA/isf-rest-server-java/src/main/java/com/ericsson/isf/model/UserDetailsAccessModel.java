/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author ekarath
 */
@JsonInclude(Include.NON_NULL)
public class UserDetailsAccessModel {
 
    private String signumID;
    private String employeeName;
    private String unit;
    private String managerSignum;
    private String isLineManager;
    private String jobStage;
    private String jobRoleFamily;
    private String employeeEmailID;
    private String status;
    private String approvalStatus;
    private String approvedDate;
    private String approvedBy;
    private String personnelNumber;
    private String contactNumber;
    private String gender;
    private String costCenter;
    private String userImageUri;
    private String employeeType;
    private String aspStatus;
    private String employeeUpn;
    private List<AccessProfileModel> lstAccessProfileModel;
    private List<RoleModel> lstRoleModel;
    private List<OrganizationModel> lstOrganizationModel;
    private List<CapabilityModel> lstCapabilityModel;
    private String sessionId;
    private String sessionToken;
    private UserPreferencesModel userPreference;
    private String firstName;
    private String lastName;
    private String employeeObjectID;
    private String employeeGroup;
    
    
    
    public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public String getEmployeeUpn() {
		return employeeUpn;
	}

	public void setEmployeeUpn(String employeeUpn) {
		this.employeeUpn = employeeUpn;
	}

	public String getAspStatus() {
		return aspStatus;
	}

	public void setAspStatus(String aspStatus) {
		this.aspStatus = aspStatus;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String userType) {
		this.employeeType = userType;
	}

	public String getSignumID() {
        return signumID;
    }

    public void setSignumID(String signumID) {
        this.signumID = signumID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getManagerSignum() {
        return managerSignum;
    }

    public void setManagerSignum(String managerSignum) {
        this.managerSignum = managerSignum;
    }

    public String getIsLineManager() {
        return isLineManager;
    }

    public void setIsLineManager(String isLineManager) {
        this.isLineManager = isLineManager;
    }

    public String getJobStage() {
        return jobStage;
    }

    public void setJobStage(String jobStage) {
        this.jobStage = jobStage;
    }

    public String getJobRoleFamily() {
        return jobRoleFamily;
    }

    public void setJobRoleFamily(String jobRoleFamily) {
        this.jobRoleFamily = jobRoleFamily;
    }

    public String getEmployeeEmailID() {
        return employeeEmailID;
    }

    public void setEmployeeEmailID(String employeeEmailID) {
        this.employeeEmailID = employeeEmailID.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }


    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public List<AccessProfileModel> getLstAccessProfileModel() {
        return lstAccessProfileModel;
    }

    public void setLstAccessProfileModel(List<AccessProfileModel> lstAccessProfileModel) {
        this.lstAccessProfileModel = lstAccessProfileModel;
    }

 
    

    public List<RoleModel> getLstRoleModel() {
        return lstRoleModel;
    }

    public void setLstRoleModel(List<RoleModel> lstRoleModel) {
        this.lstRoleModel = lstRoleModel;
    }

    public List<OrganizationModel> getLstOrganizationModel() {
        return lstOrganizationModel;
    }

    public void setLstOrganizationModel(List<OrganizationModel> lstOrganizationModel) {
        this.lstOrganizationModel = lstOrganizationModel;
    }

    public List<CapabilityModel> getLstCapabilityModel() {
        return lstCapabilityModel;
    }

    public void setLstCapabilityModel(List<CapabilityModel> lstCapabilityModel) {
        this.lstCapabilityModel = lstCapabilityModel;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(String personnelNumber) {
        this.personnelNumber = personnelNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

	public String getUserImageUri() {
		return userImageUri;
	}

	public void setUserImageUri(String userImageUri) {
		this.userImageUri = userImageUri;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public UserPreferencesModel getUserPreference() {
		return userPreference;
	}

	public void setUserPreference(UserPreferencesModel userPreference) {
		this.userPreference = userPreference;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmployeeObjectID() {
		return employeeObjectID;
	}

	public void setEmployeeObjectID(String employeeObjectID) {
		this.employeeObjectID = employeeObjectID;
	}

	public String getEmployeeGroup() {
		return employeeGroup;
	}

	public void setEmployeeGroup(String employeeGroup) {
		this.employeeGroup = employeeGroup;
	}
    
    
}

