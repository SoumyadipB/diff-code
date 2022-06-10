package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectDataModel {
	
	@JsonProperty("SystemRecordID")
	private int systemRecordID;
	
	
	@JsonProperty("ProjectID")
	private int id;
	@JsonProperty("ProjectName")
	private String name;

	
	
	public int getSystemRecordID() {
		return systemRecordID;
	}
	public void setSystemRecordID(int systemRecordID) {
		this.systemRecordID = systemRecordID;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
