package com.ericsson.isf.model;

public class ProjectFilterModel {

	
	private Integer projectID;
	private String status   ;
	private Integer  countryID;
	private Integer  customerID;
	private Integer marketAreaID;
	private String projectName;
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	private Integer serviceAreaID;
	private String role ;
	private String signum;
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	
	
	public Integer getServiceAreaID() {
		return serviceAreaID;
	}
	public void setServiceAreaID(Integer serviceAreaID) {
		this.serviceAreaID = serviceAreaID;
	}
	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCountryID() {
		return countryID;
	}
	public void setCountryID(Integer countryID) {
		this.countryID = countryID;
	}
	public Integer getCustomerID() {
		return customerID;
	}
	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}
	public Integer getMarketAreaID() {
		return marketAreaID;
	}
	public void setMarketAreaID(Integer marketAreaID) {
		this.marketAreaID = marketAreaID;
	}
	
	
	
	
	
}
