/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

import com.ericsson.isf.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;


public class WorkOrderDetailsModel {
    
	private int wOID;
	private String wOName;
	private String status;
	private String signumID;
	
	public int getwOID() {
		return wOID;
	}
	public void setwOID(int wOID) {
		this.wOID = wOID;
	}
	public String getwOName() {
		return wOName;
	}
	public void setwOName(String wOName) {
		this.wOName = wOName;
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
	

       
}
