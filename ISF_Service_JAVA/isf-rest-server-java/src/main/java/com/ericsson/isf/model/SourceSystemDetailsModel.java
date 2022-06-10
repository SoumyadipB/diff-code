/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;


/**
 *
 * @author etapawa
 */
public class SourceSystemDetailsModel {
	
	private String sourceSystemId;
	private String externalGroup;
	private String validateJsonForWOCreation;
	private int sourceId;
	private String sourceName;
	
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getSourceSystemId() {
		return sourceSystemId;
	}
	public void setSourceSystemId(String sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}
	public String getExternalGroup() {
		return externalGroup;
	}
	public void setExternalGroup(String externalGroup) {
		this.externalGroup = externalGroup;
	}
	public String getValidateJsonForWOCreation() {
		return validateJsonForWOCreation;
	}
	public void setValidateJsonForWOCreation(String validateJsonForWOCreation) {
		this.validateJsonForWOCreation = validateJsonForWOCreation;
	}
	public int getSourceId() {
		return sourceId;
	}
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	
}
