package com.ericsson.isf.restGraph.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author eakinhm
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attendee {

	private String type;
	private EmailAddress emailAddress;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public EmailAddress getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(EmailAddress emailAddress) {
		this.emailAddress = emailAddress;
	}

}
