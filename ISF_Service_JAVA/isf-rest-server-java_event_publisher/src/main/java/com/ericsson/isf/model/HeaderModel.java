package com.ericsson.isf.model;

/**
 * This class is model class for headers of all event publisher APIs.
 * 
 * @author eakinhm
 *
 */
public class HeaderModel {

	private String headerKey;
	private String headerValue;

	public String getHeaderKey() {
		return headerKey;
	}

	public void setHeaderKey(String headerKey) {
		this.headerKey = headerKey;
	}

	public String getHeaderValue() {
		return headerValue;
	}

	public void setHeaderValue(String headerValue) {
		this.headerValue = headerValue;
	}

	@Override
	public String toString() {
		return "HeaderModel [headerKey=" + headerKey + ", headerValue=" + headerValue + "]";
	}
	
	public HeaderModel() {}

	public HeaderModel(String headerKey, String headerValue) {
		this.headerKey = headerKey;
		this.headerValue = headerValue;
	}

}
