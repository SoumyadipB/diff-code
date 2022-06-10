package com.ericsson.isf.model;

public class BookedResourcesModel {
	
	
	private int id;
	private int projectID;
	private int positionId;
	private int wfId;
	private String signum;
	private String Date;
	private float BlockedHrs;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public int getPositionId() {
		return positionId;
	}
	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}
	public int getWfId() {
		return wfId;
	}
	public void setWfId(int wfId) {
		this.wfId = wfId;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public float getBlockedHrs() {
		return BlockedHrs;
	}
	public void setBlockedHrs(float blockedHrs) {
		BlockedHrs = blockedHrs;
	}
	
	@Override
	public String toString() {
		return "BookedResources [id=" + id + ", projectID=" + projectID
				+ ", positionId=" + positionId + ", wfId=" + wfId + ", signum="
				+ signum + ", Date=" + Date + ", BlockedHrs=" + BlockedHrs
				+ "]";
	}
} 