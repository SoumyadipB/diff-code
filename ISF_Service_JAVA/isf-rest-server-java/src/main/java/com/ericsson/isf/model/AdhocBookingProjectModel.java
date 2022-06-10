package com.ericsson.isf.model;

public class AdhocBookingProjectModel {
	private String actualDuration;
	private String actType;
	private String id;
	
	private String name;
	private String type;
	private int adhocBookingActivityID; 
	private int projectID; 
	private String projectName;
	
	public String getActualDuration() {
		return actualDuration;
	}
	public void setActualDuration(String actualDuration) {
		this.actualDuration = actualDuration;
	}
	public String getActType() {
		return actType;
	}
	public void setActType(String actType) {
		this.actType = actType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getAdhocBookingActivityID() {
		return adhocBookingActivityID;
	}
	public void setAdhocBookingActivityID(int adhocBookingActivityID) {
		this.adhocBookingActivityID = adhocBookingActivityID;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	

}
