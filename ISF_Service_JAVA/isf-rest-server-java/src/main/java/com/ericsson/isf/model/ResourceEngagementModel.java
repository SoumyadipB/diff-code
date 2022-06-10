/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author edhhklu
 */
public class ResourceEngagementModel {
    
    private int wOID;
    private Date plannedStartDate;
    private Date plannedEndDate;
    private Date actualStartDate;
    private Date actualEndDate;
    private String signumID;
    private String status;
    private double sla;
    private String type;
    private double bookinghours;
    private Date bookingStartDate;
    private Date bookingEndDate;
    private int flowchartDefId;
    private String employeeName;
    
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public int getFlowchartDefId() {
		return flowchartDefId;
	}
	public void setFlowchartDefId(int flowchartDefId) {
		this.flowchartDefId = flowchartDefId;
	}
	public Date getBookingStartDate() {
		return bookingStartDate;
	}
	public void setBookingStartDate(Date bookingStartDate) {
		this.bookingStartDate = bookingStartDate;
	}
	public Date getBookingEndDate() {
		return bookingEndDate;
	}
	public void setBookingEndDate(Date bookingEndDate) {
		this.bookingEndDate = bookingEndDate;
	}
	public double getBookinghours() {
		return bookinghours;
	}
	public void setBookinghours(double bookinghours) {
		this.bookinghours = bookinghours;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getSla() {
		return sla;
	}
	public void setSla(double sla) {
		this.sla = sla;
	}
	public int getwOID() {
		return wOID;
	}
	public void setwOID(int wOID) {
		this.wOID = wOID;
	}
	public Date getPlannedStartDate() {
		return plannedStartDate;
	}
	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}
	public Date getPlannedEndDate() {
		return plannedEndDate;
	}
	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}
	public Date getActualStartDate() {
		return actualStartDate;
	}
	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	public Date getActualEndDate() {
		return actualEndDate;
	}
	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	public String getSignumID() {
		return signumID;
	}
	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
            
    
}
