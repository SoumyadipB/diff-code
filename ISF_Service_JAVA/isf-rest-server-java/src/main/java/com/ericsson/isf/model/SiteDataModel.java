package com.ericsson.isf.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteDataModel {
	
	@JsonProperty("SystemRecordID")
	private int systemRecordID;
	@JsonProperty("ID")
	private String id;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("HierarchyPath")
	private String hierarchyPath;
	@JsonProperty("NameByCustomer")
	private String nameByCustomer;
	@JsonProperty("IDByCustomer")
	private String idByCustomer;
	@JsonProperty("BusinessObject")
	private String businessObject;
	@JsonProperty("FormName")
	private String formName;
	@JsonProperty("RecordName")
	private String recordName;
	@JsonProperty("CreatedDateTime")
	private Date createdDateTime; 
	@JsonProperty("ModifiedDateTime")
	private Date modifiedDateTime;
	@JsonProperty("ModifiedBy")
	private String modifiedBy;
	@JsonProperty("RecordState")
	private String recordState;
	@JsonProperty("Status")
	private String status;
	
	public int getSystemRecordID() {
		return systemRecordID;
	}
	public void setSystemRecordID(int systemRecordID) {
		this.systemRecordID = systemRecordID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHierarchyPath() {
		return hierarchyPath;
	}
	public void setHierarchyPath(String hierarchyPath) {
		this.hierarchyPath = hierarchyPath;
	}
	public String getNameByCustomer() {
		return nameByCustomer;
	}
	public void setNameByCustomer(String nameByCustomer) {
		this.nameByCustomer = nameByCustomer;
	}
	public String getIdByCustomer() {
		return idByCustomer;
	}
	public void setIdByCustomer(String idByCustomer) {
		this.idByCustomer = idByCustomer;
	}
	public String getBusinessObject() {
		return businessObject;
	}
	public void setBusinessObject(String businessObject) {
		this.businessObject = businessObject;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getRecordName() {
		return recordName;
	}
	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public Date getModifiedDateTime() {
		return modifiedDateTime;
	}
	public void setModifiedDateTime(Date modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getRecordState() {
		return recordState;
	}
	public void setRecordState(String recordState) {
		this.recordState = recordState;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
