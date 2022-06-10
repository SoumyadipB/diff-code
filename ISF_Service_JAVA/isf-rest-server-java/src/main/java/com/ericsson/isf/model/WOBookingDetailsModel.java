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
public class WOBookingDetailsModel {
    
    private int bookingID;
    private String bookingStartDate;
    private String bookingEndDate;
    private float hours;
    private int parentBookingDetailsID;
    private String type;

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public String getBookingStartDate() {
        return bookingStartDate;
    }

    public void setBookingStartDate(String bookingStartDate) {
        this.bookingStartDate = bookingStartDate;
    }

    public String getBookingEndDate() {
        return bookingEndDate;
    }

    public void setBookingEndDate(String bookingEndDate) {
        this.bookingEndDate = bookingEndDate;
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

    @Override
    public String toString() {
        return "WOBookingDetailsModel{" + "bookingID=" + bookingID + ", bookingStartDate=" + bookingStartDate + ", bookingEndDate=" + bookingEndDate + ", hours=" + hours + ", parentBookingDetailsID=" + parentBookingDetailsID + ", type=" + type + '}';
    }
    
}
