package com.ericsson.isf.model;

public class ApiManagerRequestModel {
	private ApiManagerUserModel properties;

	public ApiManagerUserModel getProperties() {
		return properties;
	}

	@Override
	public String toString() {
		return "ApiManagerRequestModel [properties=" + properties + "]";
	}

	public void setProperties(ApiManagerUserModel properties) {
		this.properties = properties;
	}

	public ApiManagerRequestModel(ApiManagerUserModel properties) {
		this.properties = properties;
	}
}
