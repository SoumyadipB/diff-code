/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

/**
 *
 * @author eabhmoj
 */
public class WorkOrderWorkFlowMappingModel {
    private int   wO_WF_MappingID;
private int wO_ID;
private int workFlowID;
private String createdBy;
private Date createdOn;

    public int getwO_WF_MappingID() {
        return wO_WF_MappingID;
    }

    public void setwO_WF_MappingID(int wO_WF_MappingID) {
        this.wO_WF_MappingID = wO_WF_MappingID;
    }

    public int getwO_ID() {
        return wO_ID;
    }

    public void setwO_ID(int wO_ID) {
        this.wO_ID = wO_ID;
    }

    public int getWorkFlowID() {
        return workFlowID;
    }

    public void setWorkFlowID(int workFlowID) {
        this.workFlowID = workFlowID;
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

}
