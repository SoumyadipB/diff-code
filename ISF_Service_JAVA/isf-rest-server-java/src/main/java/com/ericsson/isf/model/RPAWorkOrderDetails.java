/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ekarath
 */
public class RPAWorkOrderDetails {
    
    private int woPlanID;
    private String woName;
    private int projectID;
    private int subActivityID;
    private Date startDate;
    private Time startTime;
    private float avgEstdEffort;
    private String priority;
    private String assignedTO;
    private String createdBy;
    private Date createdDate;
    private boolean active;
    private String activity;
    private String subActivity;
    private List<WorkOrderModel> listOfWorkOrder;
    

    public int getWoPlanID() {
        return woPlanID;
    }

    public void setWoPlanID(int woPlanID) {
        this.woPlanID = woPlanID;
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

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
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

    public List<WorkOrderModel> getListOfWorkOrder() {
        return listOfWorkOrder;
    }

    public void setListOfWorkOrder(List<WorkOrderModel> listOfWorkOrder) {
        this.listOfWorkOrder = listOfWorkOrder;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getSubActivity() {
        return subActivity;
    }

    public void setSubActivity(String subActivity) {
        this.subActivity = subActivity;
    }

}
