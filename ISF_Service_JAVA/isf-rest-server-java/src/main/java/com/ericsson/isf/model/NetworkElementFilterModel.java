package com.ericsson.isf.model;


public class NetworkElementFilterModel {

	private int projectID;

	private String technologyIDs;
	private String domainIDs;

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public String getTechnologyIDs() {
		return technologyIDs;
	}

	public void setTechnologyIDs(String technologyIDs) {
		this.technologyIDs = technologyIDs;
	}

	public String getDomainIDs() {
		return domainIDs;
	}

	public void setDomainIDs(String domainIDs) {
		this.domainIDs = domainIDs;
	}

	public String getNodeNames() {
		return nodeNames;
	}

	public void setNodeNames(String nodeNames) {
		this.nodeNames = nodeNames;
	}

	private String nodeNames;
    

}
