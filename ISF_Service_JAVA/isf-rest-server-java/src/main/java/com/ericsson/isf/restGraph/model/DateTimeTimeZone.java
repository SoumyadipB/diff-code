package com.ericsson.isf.restGraph.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author eakinhm
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DateTimeTimeZone {
	private Date dateTime;
	private String timeZone;
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
}
