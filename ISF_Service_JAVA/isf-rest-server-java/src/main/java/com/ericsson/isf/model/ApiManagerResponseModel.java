package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiManagerResponseModel {
	private String name;
	private String id;
	private ApiManagerUserModel apiManagerUserModel;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ApiManagerUserModel getApiManagerUserModel() {
		return apiManagerUserModel;
	}
	public void setApiManagerUserModel(ApiManagerUserModel apiManagerUserModel) {
		this.apiManagerUserModel = apiManagerUserModel;
	}
	@Override
	public String toString() {
		return "ApiManagerResponseModel [name=" + name + ", id=" + id + ", apiManagerUserModel=" + apiManagerUserModel
				+ "]";
	}
}
