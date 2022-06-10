/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

/**
 *
 * @author ekarath
 */
public class AssignedUserModel {
    
    private String signumID;
    private String employeeName;
    private String unit;
    private String managerSignum;
    private String isLineManager;
    private String jobStage;
    private String jobRoleFamily;
    private String employeeEmailID;
    private String status;
    private String approvedStatus;
    private String approvedDate;
    private String approvedBy;
    private List<AccessProfileModel> lstAccessProfile;
    private List<RoleModel> lstRoleModel;
    private List<OrganizationModel> lstOrganizationModel;
    private List<CapabilityModel> lstCapabilityModel;

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
        this.employeeEmailID = employeeEmailID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovedStatus() {
        return approvedStatus;
    }

    public void setApprovedStatus(String approvedStatus) {
        this.approvedStatus = approvedStatus;
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

    public List<AccessProfileModel> getLstAccessProfile() {
        return lstAccessProfile;
    }

    public void setLstAccessProfile(List<AccessProfileModel> lstAccessProfile) {
        this.lstAccessProfile = lstAccessProfile;
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
    
}
