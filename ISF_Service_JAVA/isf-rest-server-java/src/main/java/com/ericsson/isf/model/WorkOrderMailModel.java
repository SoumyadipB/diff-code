/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author esanpup
 */
public class WorkOrderMailModel {
    
    private String senderID;
    private String senderName;
    private String receiverID;
    private String receiverName;
    private String senderEmailID;
    private String receiverEmailID;
    private String status;
    private int woID;
    private String plannedStart;
    private String neID;
	public String getSenderID() {
		return senderID;
	}
	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getReceiverID() {
		return receiverID;
	}
	public void setReceiverID(String receiverID) {
		this.receiverID = receiverID;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getSenderEmailID() {
		return senderEmailID;
	}
	public void setSenderEmailID(String senderEmailID) {
		this.senderEmailID = senderEmailID;
	}
	public String getReceiverEmailID() {
		return receiverEmailID;
	}
	public void setReceiverEmailID(String receiverEmailID) {
		this.receiverEmailID = receiverEmailID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getWoID() {
		return woID;
	}
	public void setWoID(int woID) {
		this.woID = woID;
	}
	public String getPlannedStart() {
		return plannedStart;
	}
	public void setPlannedStart(String plannedStart) {
		this.plannedStart = plannedStart;
	}
	public String getNeID() {
		return neID;
	}
	public void setNeID(String neID) {
		this.neID = neID;
	}

	public WorkOrderMailModel(String senderID, String senderName, String receiverID, String receiverName,
			String senderEmailID, String receiverEmailID, String status, int woID, String plannedStart, String neID) {
		super();
		this.senderID = senderID;
		this.senderName = senderName;
		this.receiverID = receiverID;
		this.receiverName = receiverName;
		this.senderEmailID = senderEmailID;
		this.receiverEmailID = receiverEmailID;
		this.status = status;
		this.woID = woID;
		this.plannedStart = plannedStart;
		this.neID = neID;
	}

	public WorkOrderMailModel() {
	}

	@Override
	public String toString() {
		return "WorkOrderMailModel [senderID=" + senderID + ", senderName=" + senderName + ", receiverID=" + receiverID
				+ ", receiverName=" + receiverName + ", senderEmailID=" + senderEmailID + ", receiverEmailID="
				+ receiverEmailID + ", status=" + status + ", woID=" + woID + ", plannedStart=" + plannedStart
				+ ", neID=" + neID + "]";
	}
    
    
}
