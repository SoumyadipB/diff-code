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
public class UpdateBookingAndStepDetailsModel {
    
    private int woID;
    private int taskID;
    private int bookingID;
    private String flowChartStepID;
    private int flowChartDefID;
    private String executionType;

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

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

   
    public String getFlowChartStepID() {
		return flowChartStepID;
	}

	public void setFlowChartStepID(String flowChartStepID) {
		this.flowChartStepID = flowChartStepID;
	}

	public int getFlowChartDefID() {
        return flowChartDefID;
    }

    public void setFlowChartDefID(int flowChartDefID) {
        this.flowChartDefID = flowChartDefID;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }
    
    
}
