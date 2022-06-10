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
public class AccessProfileUserDetailModel {
    
    private int accessProfileID;
    private String accessProfileName;
    private int roleID;
    private String role;
    private int organisationID;
    private String organisation;
    private List<AccessUserProfileModel> lstAccessUserProfile;

    public int getAccessProfileID() {
        return accessProfileID;
    }

    public void setAccessProfileID(int accessProfileID) {
        this.accessProfileID = accessProfileID;
    }

    public String getAccessProfileName() {
        return accessProfileName;
    }

    public void setAccessProfileName(String accessProfileName) {
        this.accessProfileName = accessProfileName;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOrganisation() {
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

    public List<AccessUserProfileModel> getLstAccessUserProfile() {
        return lstAccessUserProfile;
    }

    public void setLstAccessUserProfile(List<AccessUserProfileModel> lstAccessUserProfile) {
        this.lstAccessUserProfile = lstAccessUserProfile;
    }
 
    
}

