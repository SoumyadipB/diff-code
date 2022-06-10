package com.ericsson.isf.model;

public class ExternalActivitySubactivityModel {
private int activityID;
private String extActivityID;
private int isfSubActivityID;
private int sourceID;
private String lastModifiedBy;
private String lastModifiedDate;
private String sourceName;
public String getSourceName() {
	return sourceName;
}
public void setSourceName(String sourceName) {
	this.sourceName = sourceName;
}
public int getActivityID() {
	return activityID;
}
public void setActivityID(int activityID) {
	this.activityID = activityID;
}
public String getExtActivityID() {
	return extActivityID;
}
public void setExtActivityID(String extActivityID) {
	this.extActivityID = extActivityID;
}
public int getIsfSubActivityID() {
	return isfSubActivityID;
}
public void setIsfSubActivityID(int isfSubactivityID) {
	this.isfSubActivityID = isfSubactivityID;
}
public int getSourceID() {
	return sourceID;
}
public void setSourceID(int sourceID) {
	this.sourceID = sourceID;
}
public String getLastModifiedBy() {
	return lastModifiedBy;
}
public void setLastModifiedBy(String lastModifiedBy) {
	this.lastModifiedBy = lastModifiedBy;
}
public String getLastModifiedDate() {
	return lastModifiedDate;
}
public void setLastModifiedDate(String lastModifiedDate) {
	this.lastModifiedDate = lastModifiedDate;
}
}
