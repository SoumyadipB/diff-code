/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.sql.Time;
import java.util.Date;

/**
 *
 * @author esanpup
 */
public class AdhocWorkOrderModel {
    
    private int adhocWOID;
    private String woName;
    private int projectID;
    private int domainID;
    private int serviceAreaID;
    private int technologyID;

 
    private int subActivityID;
    private Date startDate;
    private Time startTime;
    private float avgEstdEffort;
    private String priority;
    private String assignedTO;
    private String createdBy;
    private Date createdDate;
    private boolean active;
    private float slaHrs ;

    public int getAdhocWOID() {
        return adhocWOID;
    }

    public void setAdhocWOID(int adhocWOID) {
        this.adhocWOID = adhocWOID;
    }

    public String getWoName() {
        return woName;
    }

    public void setWoName(String woName) {
        this.woName = woName;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getDomainID() {
        return domainID;
    }

    public void setDomainID(int domainID) {
        this.domainID = domainID;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
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

    public int getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public float getAvgEstdEffort() {
        return avgEstdEffort;
    }

    public void setAvgEstdEffort(float avgEstdEffort) {
        this.avgEstdEffort = avgEstdEffort;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAssignedTO() {
        return assignedTO;
    }

    public void setAssignedTO(String assignedTO) {
        this.assignedTO = assignedTO;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
            
       public float getSlaHrs() {
        return slaHrs;
    }

    public void setSlaHrs(float slaHrs) {
        this.slaHrs = slaHrs;
    }
    
    
}
