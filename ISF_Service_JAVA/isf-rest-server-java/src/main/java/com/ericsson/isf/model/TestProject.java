package com.ericsson.isf.model;

public class TestProject {
	private Integer projectID;
	private String name;
	private String type;
	
	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "TestProject [projectID=" + projectID + ", name=" + name + ", type=" + type + "]";
	}
}
