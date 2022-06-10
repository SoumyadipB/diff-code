package com.ericsson.isf.model;

public class ProjectModel {
	
	private int ProjectID;
	private String ProjectName;
	
	public int getProjectID() {
		return ProjectID;
	}
	public void setProjectID(int projectID) {
		ProjectID = projectID;
	}
	public String getProjectName() {
		return ProjectName;
	}
	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}
	
	public ProjectModel projectID(int projectID) {
		this.ProjectID = projectID;
		return this;
	}
	
}
