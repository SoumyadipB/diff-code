/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author eefhiio
 */
public class WorkFlowBookingDetailsModel {
    private int wOID;
    private int taskID;
    private String effort;
    private int bookingID;
    private String status;
    private String outputLink;
    private String reason;
    private String type;
    private String stepID;
    private String executionType;
    
    public String getStepID() {
		return stepID;
	}

	public void setStepID(String stepID) {
		this.stepID = stepID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getOutputLink() {
        return outputLink;
    }

    public void setOutputLink(String outputLink) {
        this.outputLink = outputLink;
    }
    
    public int getwOID() {
        return wOID;
    }

    public void setwOID(int wOID) {
        this.wOID = wOID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getEffort() {
        return effort;
    }

    public void setEffort(String effort) {
        this.effort = effort;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
	public String toString() {
		return "WorkFlowBookingDetailsModel [wOID=" + wOID + ", taskID=" + taskID + ", effort=" + effort
				+ ", bookingID=" + bookingID + ", status=" + status + ", outputLink=" + outputLink + ", reason="
				+ reason + "]";
	}

	public String getExecutionType() {
		return executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}
    
    
}
