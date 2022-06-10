package com.ericsson.isf.model;

public class TimeZoneModel {

	private int timeZoneID;
	private String utcOffset;
	private String timeZone;
	private String exampleLocation;
	private String istOffset;
	private int isActive;
	
	public int getTimeZoneID() {
		return timeZoneID;
	}
	public void setTimeZoneID(int timeZoneID) {
		this.timeZoneID = timeZoneID;
	}
	public String getUtcOffset() {
		return utcOffset;
	}
	public void setUtcOffset(String utcOffset) {
		this.utcOffset = utcOffset;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getExampleLocation() {
		return exampleLocation;
	}
	public void setExampleLocation(String exampleLocation) {
		this.exampleLocation = exampleLocation;
	}
	public String getIstOffset() {
		return istOffset;
	}
	public void setIstOffset(String istOffset) {
		this.istOffset = istOffset;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
}
