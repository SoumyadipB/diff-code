package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErisiteAssignment {
  
	private String assignedGroup;
	private String assignedRole;
	private ErisiteAssignedIndividual erisiteAssignedIndividual;
	
	
	@JsonProperty("info:AssignedGroup")
	public String getAssignedGroup() {
		return assignedGroup;
	}
	@JsonProperty("info:AssignedGroup")
	public void setAssignedGroup(String assignedGroup) {
		this.assignedGroup = assignedGroup;
	}
	@JsonProperty("info:AssignedRole")
	public String getAssignedRole() {
		return assignedRole;
	}
	@JsonProperty("info:AssignedRole")
	public void setAssignedRole(String assignedRole) {
		this.assignedRole = assignedRole;
	}
	@JsonProperty("info:AssignedIndividual")
	public ErisiteAssignedIndividual getErisiteAssignedIndividual() {
		return erisiteAssignedIndividual;
	}
	@JsonProperty("info:AssignedIndividual")
	public void setErisiteAssignedIndividual(
			ErisiteAssignedIndividual erisiteAssignedIndividual) {
		this.erisiteAssignedIndividual = erisiteAssignedIndividual;
	}
}
