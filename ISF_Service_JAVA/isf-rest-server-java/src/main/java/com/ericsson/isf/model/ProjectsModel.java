package com.ericsson.isf.model;

public class ProjectsModel {

	
	private int projectID;
	private String projectName;
	private String projectCreator;
	public String getProjectCreator() {
		return projectCreator;
	}
	public void setProjectCreator(String projectCreator) {
		this.projectCreator = projectCreator;
	}
	private String Status;
	private String countryName;
	private String marketAreaName;
	private String startDate;
	private String endDate;
	private String createdBy;
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getMarketAreaName() {
		return marketAreaName;
	}
	public void setMarketAreaName(String marketAreaName) {
		this.marketAreaName = marketAreaName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	


}
