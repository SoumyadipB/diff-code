/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

/**
 *
 * @author eefhiio
 */
public class WorkOrderBookingDetailsModel {
    
    private int bookingID;
    private int wOID;
    private int taskID;
    private Date startDate;
    private Date endDate;
    private float hours;
    private int parentBookingDetailsID;
    private String type;
    private String status;
    private String signumID;

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    public int getParentBookingDetailsID() {
        return parentBookingDetailsID;
    }

    public void setParentBookingDetailsID(int parentBookingDetailsID) {
        this.parentBookingDetailsID = parentBookingDetailsID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "WorkOrderBookingDetailsModel{" + "bookingID=" + bookingID + ", wOID=" + wOID + ", taskID=" + taskID + ", startDate=" + startDate + ", endDate=" + endDate + ", hours=" + hours + ", parentBookingDetailsID=" + parentBookingDetailsID + ", type=" + type + ", status=" + status + ", signumID=" + signumID + '}';
    }
    
    
    
    
}
