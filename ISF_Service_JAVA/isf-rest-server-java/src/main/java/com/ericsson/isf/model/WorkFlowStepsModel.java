/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author eabhmoj
 */
public class WorkFlowStepsModel {
private int workFlowStepID;
private int taskID;
private int rpaID;
private int workFlowID;
private String taskName;
private String stepName;
private String executionType;
private int avgEstdEffort;
private String stepType;
private int size_Width;
private int size_Height;
private int position_X;
private int position_Y;
private int angle;
private int clientStepID;
private int z;
private String action;
private String createdBy;
private Date createdOn;
private List<WorkFlowStepAttrModel> workFlowStepAttr;

    public int getWorkFlowStepID() {
        return workFlowStepID;
    }

    public void setWorkFlowStepID(int workFlowStepID) {
        this.workFlowStepID = workFlowStepID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getRpaID() {
        return rpaID;
    }

    public void setRpaID(int rpaID) {
        this.rpaID = rpaID;
    }

    public int getWorkFlowID() {
        return workFlowID;
    }

    public void setWorkFlowID(int workFlowID) {
        this.workFlowID = workFlowID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public int getAvgEstdEffort() {
        return avgEstdEffort;
    }

    public void setAvgEstdEffort(int avgEstdEffort) {
        this.avgEstdEffort = avgEstdEffort;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public int getSize_Width() {
        return size_Width;
    }

    public void setSize_Width(int size_Width) {
        this.size_Width = size_Width;
    }

    public int getSize_Height() {
        return size_Height;
    }

    public void setSize_Height(int size_Height) {
        this.size_Height = size_Height;
    }

    public int getPosition_X() {
        return position_X;
    }

    public void setPosition_X(int position_X) {
        this.position_X = position_X;
    }

    public int getPosition_Y() {
        return position_Y;
    }

    public void setPosition_Y(int position_Y) {
        this.position_Y = position_Y;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getClientStepID() {
        return clientStepID;
    }

    public void setClientStepID(int clientStepID) {
        this.clientStepID = clientStepID;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public List<WorkFlowStepAttrModel> getWorkFlowStepAttr() {
        return workFlowStepAttr;
    }

    public void setWorkFlowStepAttr(List<WorkFlowStepAttrModel> workFlowStepAttr) {
        this.workFlowStepAttr = workFlowStepAttr;
    }

}
