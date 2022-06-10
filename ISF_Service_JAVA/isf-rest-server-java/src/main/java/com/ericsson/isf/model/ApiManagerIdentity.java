package com.ericsson.isf.model;

public class ApiManagerIdentity {
	private String id;
	private String provider;
	
	
	@Override
	public String toString() {
		return "ApiManagerIdentity [id=" + id + ", provider=" + provider + "]";
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
