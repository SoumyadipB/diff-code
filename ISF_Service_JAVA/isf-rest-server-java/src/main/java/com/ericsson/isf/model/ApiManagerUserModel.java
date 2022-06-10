package com.ericsson.isf.model;

import java.util.List;

public class ApiManagerUserModel {
	private String firstName;
	private String lastName;
	private String email;
	private String state;
	private String appType;
	private List<ApiManagerIdentity> identities;
	private String signum;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<ApiManagerIdentity> getIdentities() {
		return identities;
	}
	public void setIdentities(List<ApiManagerIdentity> identities) {
		this.identities = identities;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	@Override
	public String toString() {
		return "ApiManagerUserModel [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", state=" + state + ", appType=" + appType + ", identities=" + identities + "]";
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}

}
