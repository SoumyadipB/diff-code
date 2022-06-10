/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author ekarath
 */
public class RoleModel {
    
    private int accessRoleID;
    private String role;
    private String alias;
    private boolean active;

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    
    
    
}
