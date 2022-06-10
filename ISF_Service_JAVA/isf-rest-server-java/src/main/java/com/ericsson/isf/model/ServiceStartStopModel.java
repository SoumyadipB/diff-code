package com.ericsson.isf.model;

import org.springframework.validation.annotation.Validated;

import com.ericsson.isf.validator.NotBlankString;

@Validated
public class ServiceStartStopModel {
	
	//@Valid
	@NotBlankString(message="Please Provide procedure name!")
	private String procedureName; // Required
    
	private int queryTimeout;
	private String type;
	
	public int getQueryTimeout() {
		return queryTimeout;
	}

	public void setQueryTimeout(int queryTimeout) {
		this.queryTimeout = queryTimeout;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProcedureName() {
		return procedureName;
	}
	
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public ServiceStartStopModel() {
	}

	public ServiceStartStopModel(String procedureName) {
		this.procedureName = procedureName;
	}

	@Override
	public String toString() {
		return "ServiceStartStop [procedureName=" + procedureName + "]";
	}

}
