package com.ericsson.isf.model;

public class LogLevelModel {
     private Integer LoggingID;
     private String LoggingLevel;
     private String LoggingDescription;
     private String userSignum;
	public Integer getLoggingID() {
		return LoggingID;
	}
	public void setLoggingID(Integer loggingID) {
		LoggingID = loggingID;
	}
	public String getLoggingLevel() {
		return LoggingLevel;
	}
	public void setLoggingLevel(String loggingLevel) {
		LoggingLevel = loggingLevel;
	}
	public String getLoggingDescription() {
		return LoggingDescription;
	}
	public void setLoggingDescription(String loggingDescription) {
		LoggingDescription = loggingDescription;
	}
	public String getUserSignum() {
		return userSignum;
	}
	public void setUserSignum(String userSignum) {
		this.userSignum = userSignum;
	}
     
     
}
