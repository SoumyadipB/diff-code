package com.ericsson.isf.model;

import java.util.List;

public class ProjectDetailsModel {
	private int projectID;
	private String projectName;
	private String projectType;
	private List<ScopeDetailModel> scopeDetails;
	
	private String type;
	private int id;

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
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public List<ScopeDetailModel> getScopeDetails() {
		return scopeDetails;
	}
	public void setScopeDetails(List<ScopeDetailModel> scopeDetails) {
		this.scopeDetails = scopeDetails;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	

	
}
