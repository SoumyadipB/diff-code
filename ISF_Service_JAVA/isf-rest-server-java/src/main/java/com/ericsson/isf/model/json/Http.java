package com.ericsson.isf.model.json;

public class Http {
	private String action;
	
	public Http() {
		
	}
	public Http(String action) {
		this.action=action;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
