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
public class WorkFlowStepToolMappingModel {
    private int  wFS_Tool_MappingID;
private int flowChartStepID;
private int toolID;
private String createdBy;
private Date createdOn;

    public int getwFS_Tool_MappingID() {
        return wFS_Tool_MappingID;
    }

    public void setwFS_Tool_MappingID(int wFS_Tool_MappingID) {
        this.wFS_Tool_MappingID = wFS_Tool_MappingID;
    }

    public int getFlowChartStepID() {
        return flowChartStepID;
    }

    public void setFlowChartStepID(int flowChartStepID) {
        this.flowChartStepID = flowChartStepID;
    }

    public int getToolID() {
        return toolID;
    }

    public void setToolID(int toolID) {
        this.toolID = toolID;
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
