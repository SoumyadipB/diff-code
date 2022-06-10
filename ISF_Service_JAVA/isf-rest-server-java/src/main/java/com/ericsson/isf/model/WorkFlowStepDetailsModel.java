/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

/**
 *
 * @author ekarath
 */
public class WorkFlowStepDetailsModel {
    
    private int woID;
    private int taskID;
    private String stepID;
    private int flowchartDefID;
    private String status;
    private String signumID;
    private String decisionValue;
    private int bookingID;
    private List<WorkFlowBookingDetailsModel> lstBooking;

    public int getWoID() {
        return woID;
    }

    public void setWoID(int woID) {
        this.woID = woID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }


    public String getStepID() {
		return stepID;
	}

	public void setStepID(String stepID) {
		this.stepID = stepID;
	}

	public int getFlowchartDefID() {
        return flowchartDefID;
    }

    public void setFlowchartDefID(int flowchartDefID) {
        this.flowchartDefID = flowchartDefID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSignumID() {
        return signumID;
    }

    public void setSignumID(String signumID) {
        this.signumID = signumID;
    }

    public String getDecisionValue() {
        return decisionValue;
    }

    public void setDecisionValue(String decisionValue) {
        this.decisionValue = decisionValue;
    }
    

    public List<WorkFlowBookingDetailsModel> getLstBooking() {
        return lstBooking;
    }

    public void setLstBooking(List<WorkFlowBookingDetailsModel> lstBooking) {
        this.lstBooking = lstBooking;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    @Override
    public String toString() {
        return "WorkFlowStepDetailsModel{" + "woID=" + woID + ", taskID=" + taskID + ", stepID=" + stepID + ", flowchartDefID=" + flowchartDefID + ", status=" + status + ", signumID=" + signumID + ", decisionValue=" + decisionValue + ", bookingID=" + bookingID + ", lstBooking=" + lstBooking + '}';
    }
    
}
