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
public class RPABookingModel {
    private int wOID;
    private int taskID;
    private float hours;
    private int bookingID;
    private String status;
    private String outputLink;
    private String startDate;
    private String endDate;
    private int parentBookingID;
    private String type;
    private String signumID;
    private String reason;

    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getParentBookingID() {
        return parentBookingID;
    }

    public void setParentBookingID(int parentBookingID) {
        this.parentBookingID = parentBookingID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSignumID() {
        return signumID;
    }

    public void setSignumID(String signumID) {
        this.signumID = signumID;
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
        return "RPABookingModel{" + "wOID=" + wOID + ", taskID=" + taskID + ", hours=" + hours + ", bookingID=" + bookingID + ", status=" + status + ", outputLink=" + outputLink + ", startDate=" + startDate + ", endDate=" + endDate + ", parentBookingID=" + parentBookingID + ", type=" + type + ", signumID=" + signumID + ", reason=" + reason + '}';
    }

    
    
    
}
