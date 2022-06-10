package com.ericsson.isf.model;

public class RequestRoleAccessModel {
	
	private String signum;
	private int accessProfileID;
  
	
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public int getAccessProfileID() {
		return accessProfileID;
	}
	public void setAccessProfileID(int accessProfileID) {
		this.accessProfileID = accessProfileID;
	}
}
