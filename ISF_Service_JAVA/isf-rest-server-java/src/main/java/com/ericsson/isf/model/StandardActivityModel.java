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
public class StandardActivityModel {
    private int subActivityID;
    private int domainID;
    private int serviceAreaID;
    private int technologyID;
    private String activity;
    private String subActivityName;
    private double avgEstdEffort;
    private boolean active;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
    private String executionType;

    public int getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
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

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getSubActivityName() {
        return subActivityName;
    }

    public void setSubActivityName(String subActivityName) {
        this.subActivityName = subActivityName;
    }

//    public String getDomainName() {
//        return domainName;
//    }
//
//    public void setDomainName(String domainName) {
//        this.domainName = domainName;
//    }
//
//    public String getServiceAreaName() {
//        return serviceAreaName;
//    }
//
//    public void setServiceAreaName(String serviceAreaName) {
//        this.serviceAreaName = serviceAreaName;
//    }
//
//    public String getTechnologyName() {
//        return technologyName;
//    }
//
//    public void setTechnologyName(String technologyName) {
//        this.technologyName = technologyName;
//    }

    public double getAvgEstdEffort() {
        return avgEstdEffort;
    }

    public void setAvgEstdEffort(double avgEstdEffort) {
        this.avgEstdEffort = avgEstdEffort;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }
    
    @Override
    public String toString() {
        return "StandardActivityModel{" + "subActivityID=" + subActivityID + ", domainID=" + domainID + ", serviceAreaID=" + serviceAreaID + ", technologyID=" + technologyID + ", activity=" + activity + ", subActivityName=" + subActivityName + ", avgEstdEffort=" + avgEstdEffort + ", active=" + active + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedDate=" + lastModifiedDate + '}';
    }
 
}
