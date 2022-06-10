package com.ericsson.isf.restGraph.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author eakinhm
 *
 */
public class RequestMessageBody {

	private boolean isRead;

	public RequestMessageBody(boolean isRead) {
		this.isRead = isRead;
	}

	@JsonProperty(value="isRead")   
	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	
	
}
