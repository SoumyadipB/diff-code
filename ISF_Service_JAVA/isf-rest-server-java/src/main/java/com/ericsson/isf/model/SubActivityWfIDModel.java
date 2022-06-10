package com.ericsson.isf.model;

public class SubActivityWfIDModel {
	
	private String signumID ;
	private int subactivityID ;
	private int wfID ;
	private int woID;
	
	public int getWoID() {
		return woID;
	}
	public void setWoID(int woID) {
		this.woID = woID;
	}
	public String getSignumID() {
		return signumID;
	}
	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}
	public int getSubactivityID() {
		return subactivityID;
	}
	public void setSubactivityID(int subactivityID) {
		this.subactivityID = subactivityID;
	}
	public int getWfID() {
		return wfID;
	}
	public void setWfID(int wfID) {
		this.wfID = wfID;
	}
}
