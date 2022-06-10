package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactWorkgroupModel {

	@JsonProperty("ID") 
	public int iD;
	@JsonProperty("SystemRecordID") 
	public int systemRecordID;
	@JsonProperty("ASPBL") 
	public String aSPBL;
	@JsonProperty("Name") 
	public String name;
	@JsonProperty("ExternalCompany") 
	public ExternalCompanyModel externalCompany;

	
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
	public String getaSPBL() {
		return aSPBL;
	}
	public void setaSPBL(String aSPBL) {
		this.aSPBL = aSPBL;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ExternalCompanyModel getExternalCompany() {
		return externalCompany;
	}
	public void setExternalCompany(ExternalCompanyModel externalCompany) {
		this.externalCompany = externalCompany;
	}
	

}
