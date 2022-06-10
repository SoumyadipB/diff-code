package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErisiteAssignedIndividual {

	private String assigneeFullName;
	private String assigneeEmail;
    private String assigneeFirstName;
    private String assigneeLastName;
    private String assigneeWorkPhone;
    private String assigneeOrganization;
    private String assigneeOrganizationPath;
    
    @JsonProperty("info:AssigneeFullName")
	public String getAssigneeFullName() {
		return assigneeFullName;
	}
    @JsonProperty("info:AssigneeFullName")
	public void setAssigneeFullName(String assigneeFullName) {
		this.assigneeFullName = assigneeFullName;
	}
    @JsonProperty("info:AssigneeEmail")
	public String getAssigneeEmail() {
		return assigneeEmail;
	}
    @JsonProperty("info:AssigneeEmail")
	public void setAssigneeEmail(String assigneeEmail) {
		this.assigneeEmail = assigneeEmail;
	}
    @JsonProperty("info:AssigneeFirstName")
	public String getAssigneeFirstName() {
		return assigneeFirstName;
	}
    @JsonProperty("info:AssigneeFirstName")
	public void setAssigneeFirstName(String assigneeFirstName) {
		this.assigneeFirstName = assigneeFirstName;
	}
    @JsonProperty("info:AssigneeLastName")
	public String getAssigneeLastName() {
		return assigneeLastName;
	}
    @JsonProperty("info:AssigneeLastName")
	public void setAssigneeLastName(String assigneeLastName) {
		this.assigneeLastName = assigneeLastName;
	}
    @JsonProperty("info:AssigneeWorkPhone")
	public String getAssigneeWorkPhone() {
		return assigneeWorkPhone;
	}
    @JsonProperty("info:AssigneeWorkPhone")
	public void setAssigneeWorkPhone(String assigneeWorkPhone) {
		this.assigneeWorkPhone = assigneeWorkPhone;
	}
    @JsonProperty("info:AssigneeOrganization")
	public String getAssigneeOrganization() {
		return assigneeOrganization;
	}
    @JsonProperty("info:AssigneeOrganization")
	public void setAssigneeOrganization(String assigneeOrganization) {
		this.assigneeOrganization = assigneeOrganization;
	}
    @JsonProperty("info:AssigneeOrganizationPath")
	public String getAssigneeOrganizationPath() {
		return assigneeOrganizationPath;
	}
    @JsonProperty("info:AssigneeOrganizationPath")
	public void setAssigneeOrganizationPath(String assigneeOrganizationPath) {
		this.assigneeOrganizationPath = assigneeOrganizationPath;
	}
}
