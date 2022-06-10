/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author ekumvsu
 */
public class WorkOrderActivityDetailsModel {
    private String subActivityID;    
    private String subActivityName;    
    private String subActivityStartDate;    
    private String subActivityEndDate;   

    public String getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(String subActivityID) {
        this.subActivityID = subActivityID;
    }

    public String getSubActivityName() {
        return subActivityName;
    }

    public void setSubActivityName(String subActivityName) {
        this.subActivityName = subActivityName;
    }

    public String getSubActivityStartDate() {
        return subActivityStartDate;
    }

    public void setSubActivityStartDate(String subActivityStartDate) {
        this.subActivityStartDate = subActivityStartDate;
    }

    public String getSubActivityEndDate() {
        return subActivityEndDate;
    }

    public void setSubActivityEndDate(String subActivityEndDate) {
        this.subActivityEndDate = subActivityEndDate;
    }
    
}
