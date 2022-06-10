package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErisiteDataModel {
   
	private String version;
	private ErisiteNroActivityEvent nroActivityEvent;
	private int woCreationID;
	
	
	@JsonProperty("info:NroActivityEvent")
	public ErisiteNroActivityEvent getNroActivityEvent() {
		return nroActivityEvent;
	}
	@JsonProperty("info:NroActivityEvent")
	public void setNroActivityEvent(ErisiteNroActivityEvent nroActivityEvent) {
		this.nroActivityEvent = nroActivityEvent;
	}
	
	@JsonProperty("@version")
	public String getVersion() {
		return version;
	}
	@JsonProperty("@version")
	public void setVersion(String version) {
		this.version = version;
	}
	public int getWoCreationID() {
		return woCreationID;
	}
	public void setWoCreationID(int woCreationID) {
		this.woCreationID = woCreationID;
	}
	
}
