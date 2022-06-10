package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErisiteReworkOrInterrupt {
 
	
	private String interruptGroup;
	private String interruptReason;
	private String interruptName;
	private String interruptDescription;
	private String timeLost;
	private String comments;
	private String rework;
	private String reworkName;
	private String reworkTime;
	private String reworkPerson;
	
	@JsonProperty("info:InterruptGroup")
	public String getInterruptGroup() {
		return interruptGroup;
	}
	@JsonProperty("info:InterruptGroup")
	public void setInterruptGroup(String interruptGroup) {
		this.interruptGroup = interruptGroup;
	}
	@JsonProperty("info:InterruptReason")
	public String getInterruptReason() {
		return interruptReason;
	}
	@JsonProperty("info:InterruptReason")
	public void setInterruptReason(String interruptReason) {
		this.interruptReason = interruptReason;
	}
	@JsonProperty("info:InterruptName")
	public String getInterruptName() {
		return interruptName;
	}
	@JsonProperty("info:InterruptName")
	public void setInterruptName(String interruptName) {
		this.interruptName = interruptName;
	}
	@JsonProperty("info:InterruptDescription")
	public String getInterruptDescription() {
		return interruptDescription;
	}
	@JsonProperty("info:InterruptDescription")
	public void setInterruptDescription(String interruptDescription) {
		this.interruptDescription = interruptDescription;
	}
	@JsonProperty("info:TimeLost")
	public String getTimeLost() {
		return timeLost;
	}
	@JsonProperty("info:TimeLost")
	public void setTimeLost(String timeLost) {
		this.timeLost = timeLost;
	}
	@JsonProperty("info:Comments")
	public String getComments() {
		return comments;
	}
	@JsonProperty("info:Comments")
	public void setComments(String comments) {
		this.comments = comments;
	}
	@JsonProperty("info:Rework")
	public String getRework() {
		return rework;
	}
	@JsonProperty("info:Rework")
	public void setRework(String rework) {
		this.rework = rework;
	}
	@JsonProperty("info:ReworkName")
	public String getReworkName() {
		return reworkName;
	}
	@JsonProperty("info:ReworkName")
	public void setReworkName(String reworkName) {
		this.reworkName = reworkName;
	}
	@JsonProperty("info:ReworkTime")
	public String getReworkTime() {
		return reworkTime;
	}
	@JsonProperty("info:ReworkTime")
	public void setReworkTime(String reworkTime) {
		this.reworkTime = reworkTime;
	}
	@JsonProperty("info:ReworkPerson")
	public String getReworkPerson() {
		return reworkPerson;
	}
	@JsonProperty("info:ReworkPerson")
	public void setReworkPerson(String reworkPerson) {
		this.reworkPerson = reworkPerson;
	}

}
