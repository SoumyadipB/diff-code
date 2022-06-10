package com.ericsson.isf.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalCompanyModel {

	@JsonProperty("ID") 
	public int iD;

	@JsonProperty("SystemRecordID") 
	public int systemRecordID;
	@JsonProperty("Name") 
	public String name;

	public int getiD() {
		return iD;
	}
	public void setiD(int iD) {
		this.iD = iD;
	}
	public int getSystemRecordID() {
		return systemRecordID;
	}
	public void setSystemRecordID(int systemRecordID) {
		this.systemRecordID = systemRecordID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
