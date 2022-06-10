/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

/**
 *
 * @author ekumvsu
 */
public class WorkOrderViewActivityModel {

    private String subActivityID;    
    private String subActivityName;    
    private String subActivityStartDate;    
    private String subActivityEndDate;    
    private String subActivityWOCount;    
    private String subActivityHours;    
    private String subActivityDays;   
    private String workFlowName;
    private List<WorkOrderViewModel> listOfWorkOrder;

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

    public String getSubActivityWOCount() {
        return subActivityWOCount;
    }

    public void setSubActivityWOCount(String subActivityWOCount) {
        this.subActivityWOCount = subActivityWOCount;
    }

    public String getSubActivityHours() {
        return subActivityHours;
    }

    public void setSubActivityHours(String subActivityHours) {
        this.subActivityHours = subActivityHours;
    }

    public String getSubActivityDays() {
        return subActivityDays;
    }

    public void setSubActivityDays(String subActivityDays) {
        this.subActivityDays = subActivityDays;
    }

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

    public List<WorkOrderViewModel> getListOfWorkOrder() {
        return listOfWorkOrder;
    }

    public void setListOfWorkOrder(List<WorkOrderViewModel> listOfWorkOrder) {
        this.listOfWorkOrder = listOfWorkOrder;
    }

	public String getWorkFlowName() {
		return workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}
    
}
