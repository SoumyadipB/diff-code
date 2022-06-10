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
public class CapabilityModel {

    private int capabilityPageID;
    private String capabilityPageName;
    private String capabilityPageGroup;

    public int getCapabilityPageID() {
        return capabilityPageID;
    }

    public void setCapabilityPageID(int capabilityPageID) {
        this.capabilityPageID = capabilityPageID;
    }

    public String getCapabilityPageName() {
        return capabilityPageName;
    }

    public void setCapabilityPageName(String capabilityPageName) {
        this.capabilityPageName = capabilityPageName;
    }

    public String getCapabilityPageGroup() {
        return capabilityPageGroup;
    }

    public void setCapabilityPageGroup(String capabilityPageGroup) {
        this.capabilityPageGroup = capabilityPageGroup;
    }
    
    
}