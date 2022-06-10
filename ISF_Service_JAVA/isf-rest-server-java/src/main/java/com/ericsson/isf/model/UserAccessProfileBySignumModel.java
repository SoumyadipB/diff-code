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
public class UserAccessProfileBySignumModel {
    
    private String employeeName;
 //   private String employeeGroup;
    private String signumID;
 //   private String emailID;
    private String personnelNumber;
//    private String contactNumber;
//    private String gender;
//    private String status;
    private String managerSignum;
    private String costCenter;
 //   private String jobRole;
    private String jobStage;
    
    private List<RoleModel> listOfRoles;
    
    private List<OrganizationModel> listOfOrgs;
    
    private List<CapabilityModel> listOfCaps;

    
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getSignumID() {
        return signumID;
    }

    public void setSignumID(String signumID) {
        this.signumID = signumID;
    }

    public String getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(String personnelNumber) {
        this.personnelNumber = personnelNumber;
    }

    public String getManagerSignum() {
        return managerSignum;
    }

    public void setManagerSignum(String managerSignum) {
        this.managerSignum = managerSignum;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getJobStage() {
        return jobStage;
    }

    public void setJobStage(String jobStage) {
        this.jobStage = jobStage;
    }

    public List<RoleModel> getListOfRoles() {
        return listOfRoles;
    }

    public void setListOfRoles(List<RoleModel> listOfRoles) {
        this.listOfRoles = listOfRoles;
    }

    public List<OrganizationModel> getListOfOrgs() {
        return listOfOrgs;
    }

    public void setListOfOrgs(List<OrganizationModel> listOfOrgs) {
        this.listOfOrgs = listOfOrgs;
    }

    public List<CapabilityModel> getListOfCaps() {
        return listOfCaps;
    }

    public void setListOfCaps(List<CapabilityModel> listOfCaps) {
        this.listOfCaps = listOfCaps;
    }
    
}
