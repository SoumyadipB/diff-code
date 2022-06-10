package com.ericsson.erisite.report.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectDataModel {
	
	@JsonProperty("SystemRecordID")
	private int systemRecordID;
	@JsonProperty("ProjectID")
	private int id;
	@JsonProperty("ProjectName")
	private String name;
	
	@JsonProperty("IntegratedScoping")
	private String integratedScoping;
	@JsonProperty("Customer")
	private String customer;
	@JsonProperty("ProjectExecutionCountry")
	private String projectExecutionCountry;
	
	public String getIntegratedScoping() {
		return integratedScoping;
	}
	public void setIntegratedScoping(String integratedScoping) {
		this.integratedScoping = integratedScoping;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getProjectExecutionCountry() {
		return projectExecutionCountry;
	}
	public void setProjectExecutionCountry(String projectExecutionCountry) {
		this.projectExecutionCountry = projectExecutionCountry;
	}
	

	public int getSystemRecordID() {
		return systemRecordID;
	}
	public void setSystemRecordID(int systemRecordID) {
		this.systemRecordID = systemRecordID;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
