package com.ericsson.isf.model;

import java.util.Date;

public class UserProfileHistoryModel {

	

	private int systemID;
	private String signum;
	private Date accessTime;
	private int accessProfileID;
	private int instanceID;
	private int historyID;
	private String accessProfileName;

	public String getAccessProfileName() {
		return accessProfileName;
	}
	public void setAccessProfileName(String accessProfileName) {
		this.accessProfileName = accessProfileName;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public Date getAccessTime() {
		return accessTime;
	}
	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}
	public int getAccessProfileID() {
		return accessProfileID;
	}
	public void setAccessProfileID(int accessProfileID) {
		this.accessProfileID = accessProfileID;
	}
	public int getSystemID() {
		return systemID;
	}
	public void setSystemID(int systemID) {
		this.systemID = systemID;
	}
	public int getInstanceID() {
		return instanceID;
	}
	public void setInstanceID(int instanceID) {
		this.instanceID = instanceID;
	}
	public int getHistoryID() {
		return historyID;
	}
	public void setHistoryID(int historyID) {
		this.historyID = historyID;
	}
	


}
