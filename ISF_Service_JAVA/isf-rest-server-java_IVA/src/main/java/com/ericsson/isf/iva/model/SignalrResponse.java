package com.ericsson.isf.iva.model;

public class SignalrResponse {
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "SignalR [message=" + message + "]";
	}

}