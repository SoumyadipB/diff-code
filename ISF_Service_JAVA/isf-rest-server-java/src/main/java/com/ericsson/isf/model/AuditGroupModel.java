/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author edhhklu
 */
public class AuditGroupModel {
	
	private String controller;
	private String apiEndpoint;
	
	private long auditGroupId; 
	private Integer auditPageId;
	private String auditGroupCategory;
	
	public String getAuditGroupCategory() {
		return auditGroupCategory;
	}
	public void setAuditGroupCategory(String auditGroupCategory) {
		this.auditGroupCategory = auditGroupCategory;
	}
	public long getAuditGroupId() {
		return auditGroupId;
	}
	public void setAuditGroupId(long auditGroupId) {
		this.auditGroupId = auditGroupId;
	}
	public Integer getAuditPageId() {
		return auditPageId;
	}
	public void setAuditPageId(Integer auditPageId) {
		this.auditPageId = auditPageId;
	}
	public String getController() {
		return controller;
	}
	public void setController(String controller) {
		this.controller = controller;
	}
	public String getApiEndpoint() {
		return apiEndpoint;
	}
	public void setApiEndpoint(String apiEndpoint) {
		this.apiEndpoint = apiEndpoint;
	} 
	
	
}
