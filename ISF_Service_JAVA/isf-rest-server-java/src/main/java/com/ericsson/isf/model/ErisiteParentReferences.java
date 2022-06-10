package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErisiteParentReferences {
	
	private int parentProjectId;
	private String parentProjectName;
	private String parentProjectRecordID;
	private String parentProjectIntegratedScoping;
	private String parentWorkPlanRecordID;
	private String parentWorkPlanId;
	private String parentWorkPlanName;
	private String parentWorkPlanTemplateName;
	private String parentWorkPlanTemplateRecordID;
	private String parentWorkplanDocumentsUrl;
	private String parentNetworkElementId;
	private String parentNetworkElementName;
	private String parentNetworkElementCustomerProvidedID;
	private String customer;
	private String parentProjectCountry;

	@JsonProperty("info:ParentProjectId")
	public int getParentProjectId() {
		return parentProjectId;
	}
	@JsonProperty("info:ParentProjectId")
	public void setParentProjectId(int parentProjectId) {
		this.parentProjectId = parentProjectId;
	}
	@JsonProperty("info:ParentProjectName")
	public String getParentProjectName() {
		return parentProjectName;
	}
	@JsonProperty("info:ParentProjectName")
	public void setParentProjectName(String parentProjectName) {
		this.parentProjectName = parentProjectName;
	}
	@JsonProperty("info:ParentProjectRecordID")
	public String getParentProjectRecordID() {
		return parentProjectRecordID;
	}
	@JsonProperty("info:ParentProjectRecordID")
	public void setParentProjectRecordID(String parentProjectRecordID) {
		this.parentProjectRecordID = parentProjectRecordID;
	}
	@JsonProperty("info:ParentProjectIntegratedScoping")
	public String getParentProjectIntegratedScoping() {
		return parentProjectIntegratedScoping;
	}
	@JsonProperty("info:ParentProjectIntegratedScoping")
	public void setParentProjectIntegratedScoping(
			String parentProjectIntegratedScoping) {
		this.parentProjectIntegratedScoping = parentProjectIntegratedScoping;
	}
	@JsonProperty("info:ParentWorkPlanRecordID")
	public String getParentWorkPlanRecordID() {
		return parentWorkPlanRecordID;
	}
	@JsonProperty("info:ParentWorkPlanRecordID")
	public void setParentWorkPlanRecordID(String parentWorkPlanRecordID) {
		this.parentWorkPlanRecordID = parentWorkPlanRecordID;
	}
	@JsonProperty("info:ParentWorkPlanId")
	public String getParentWorkPlanId() {
		return parentWorkPlanId;
	}
	@JsonProperty("info:ParentWorkPlanId")
	public void setParentWorkPlanId(String parentWorkPlanId) {
		this.parentWorkPlanId = parentWorkPlanId;
	}
	@JsonProperty("info:ParentWorkPlanName")
	public String getParentWorkPlanName() {
		return parentWorkPlanName;
	}
	@JsonProperty("info:ParentWorkPlanName")
	public void setParentWorkPlanName(String parentWorkPlanName) {
		this.parentWorkPlanName = parentWorkPlanName;
	}
	@JsonProperty("info:ParentWorkPlanTemplateName")
	public String getParentWorkPlanTemplateName() {
		return parentWorkPlanTemplateName;
	}
	@JsonProperty("info:ParentWorkPlanTemplateName")
	public void setParentWorkPlanTemplateName(String parentWorkPlanTemplateName) {
		this.parentWorkPlanTemplateName = parentWorkPlanTemplateName;
	}
	@JsonProperty("info:ParentWorkPlanTemplateRecordID")
	public String getParentWorkPlanTemplateRecordID() {
		return parentWorkPlanTemplateRecordID;
	}
	@JsonProperty("info:ParentWorkPlanTemplateRecordID")
	public void setParentWorkPlanTemplateRecordID(
			String parentWorkPlanTemplateRecordID) {
		this.parentWorkPlanTemplateRecordID = parentWorkPlanTemplateRecordID;
	}
	@JsonProperty("info:ParentWorkplanDocumentsUrl")
	public String getParentWorkplanDocumentsUrl() {
		return parentWorkplanDocumentsUrl;
	}
	@JsonProperty("info:ParentWorkplanDocumentsUrl")
	public void setParentWorkplanDocumentsUrl(String parentWorkplanDocumentsUrl) {
		this.parentWorkplanDocumentsUrl = parentWorkplanDocumentsUrl;
	}
	@JsonProperty("info:ParentNetworkElementId")
	public String getParentNetworkElementId() {
		return parentNetworkElementId;
	}
	@JsonProperty("info:ParentNetworkElementId")
	public void setParentNetworkElementId(String parentNetworkElementId) {
		this.parentNetworkElementId = parentNetworkElementId;
	}
	@JsonProperty("info:ParentNetworkElementName")
	public String getParentNetworkElementName() {
		return parentNetworkElementName;
	}
	@JsonProperty("info:ParentNetworkElementName")
	public void setParentNetworkElementName(String parentNetworkElementName) {
		this.parentNetworkElementName = parentNetworkElementName;
	}
	@JsonProperty("info:ParentNetworkElementCustomerProvidedID")
	public String getParentNetworkElementCustomerProvidedID() {
		return parentNetworkElementCustomerProvidedID;
	}
	@JsonProperty("info:ParentNetworkElementCustomerProvidedID")
	public void setParentNetworkElementCustomerProvidedID(
			String parentNetworkElementCustomerProvidedID) {
		this.parentNetworkElementCustomerProvidedID = parentNetworkElementCustomerProvidedID;
	}
	@JsonProperty("info:Customer")
	public String getCustomer() {
		return customer;
	}
	@JsonProperty("info:Customer")
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	@JsonProperty("info:ParentProjectCountry")
	public String getParentProjectCountry() {
		return parentProjectCountry;
	}
	@JsonProperty("info:ParentProjectCountry")
	public void setParentProjectCountry(String parentProjectCountry) {
		this.parentProjectCountry = parentProjectCountry;
	}
	

}
