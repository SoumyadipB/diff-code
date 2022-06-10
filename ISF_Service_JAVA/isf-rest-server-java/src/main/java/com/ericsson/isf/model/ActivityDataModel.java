package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActivityDataModel {
    
    @JsonProperty("SystemRecordID")
	private int systemRecordID;
    
    
    @JsonProperty("ActivityID")
	private int id;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("RecordName")
	private String recordName;
	@JsonProperty("RecordState")
	private String recordState;

	
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
	public String getRecordName() {
		return recordName;
	}
	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}
	public String getRecordState() {
		return recordState;
	}
	public void setRecordState(String recordState) {
		this.recordState = recordState;
	}
	
}
