/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author eakinhm
 */
public class DOIDModel {
	
	private int doID;
    private int wOPlanID;
    private String createdBy;
    private String createdOn;
    private boolean isActive;
    
	public int getDoID() {
		return doID;
	}
	public void setDoID(int doID) {
		this.doID = doID;
	}
	public int getwOPlanID() {
		return wOPlanID;
	}
	public void setwOPlanID(int wOPlanID) {
		this.wOPlanID = wOPlanID;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public DOIDModel(int wOPlanID, String createdBy, String createdOn) {
		this.wOPlanID = wOPlanID;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}
	
	public DOIDModel(int doID, int wOPlanID) {
		this.doID = doID;
		this.wOPlanID = wOPlanID;
	}
}
