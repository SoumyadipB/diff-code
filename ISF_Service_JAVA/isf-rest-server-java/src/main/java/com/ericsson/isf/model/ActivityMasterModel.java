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
public class ActivityMasterModel {
    
    private int domainID;
    private int serviceAreaID;
    private int technologyID;
    private String domain;
    private String serviceArea;
    private String technology;

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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }
    
    
    
}
