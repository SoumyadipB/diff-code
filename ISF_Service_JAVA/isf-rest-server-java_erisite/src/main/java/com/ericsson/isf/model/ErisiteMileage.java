package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErisiteMileage {
	private String mileageStartTime;
	private String mileageStopTime;
	private String mileageNotes;
	private String mileage;
	
	@JsonProperty("info:MileageStartTime")
	public String getMileageStartTime() {
		return mileageStartTime;
	}
	@JsonProperty("info:MileageStartTime")
	public void setMileageStartTime(String mileageStartTime) {
		this.mileageStartTime = mileageStartTime;
	}
	@JsonProperty("info:MileageStopTime")
	public String getMileageStopTime() {
		return mileageStopTime;
	}
	@JsonProperty("info:MileageStopTime")
	public void setMileageStopTime(String mileageStopTime) {
		this.mileageStopTime = mileageStopTime;
	}
	@JsonProperty("info:MileageNotes")
	public String getMileageNotes() {
		return mileageNotes;
	}
	@JsonProperty("info:MileageNotes")
	public void setMileageNotes(String mileageNotes) {
		this.mileageNotes = mileageNotes;
	}
	@JsonProperty("info:Mileage")
	public String getMileage() {
		return mileage;
	}
	@JsonProperty("info:Mileage")
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	
}
