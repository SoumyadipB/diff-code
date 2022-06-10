package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErisiteActivityCoreDetails {
   
	private String erisiteBusinessObject;
	private String erisiteFormName;
	private String systemRecordId;
	private Integer activityId;
	private String recordName;

	private String createdDateTime;
	private String createdBy;
	private String modifiedDateTime;
	private String modifiedBy;
	private String recordState;
	private String status;
	private String activityUrl;
	private String systemGeography;
	private String recordLanguage;
	private String activityName;
	private String typeOfWork;
	private String globalProcessStep;
	private String globalPlanningObjectName;
	private String activityDescription;
	private String processBranch;
	private String tags;
	private String notes;
	private String eSRUrl;
	
	@JsonProperty("info:ErisiteBusinessObject")
	public String getErisiteBusinessObject() {
		return erisiteBusinessObject;
	}
	@JsonProperty("info:ErisiteBusinessObject")
	public void setErisiteBusinessObject(String erisiteBusinessObject) {
		this.erisiteBusinessObject = erisiteBusinessObject;
	}
	@JsonProperty("info:ErisiteFormName")
	public String getErisiteFormName() {
		return erisiteFormName;
	}
	@JsonProperty("info:ErisiteFormName")
	public void setErisiteFormName(String erisiteFormName) {
		this.erisiteFormName = erisiteFormName;
	}
	@JsonProperty("info:SystemRecordId")
	public String getSystemRecordId() {
		return systemRecordId;
	}
	@JsonProperty("info:SystemRecordId")
	public void setSystemRecordId(String systemRecordId) {
		this.systemRecordId = systemRecordId;
	}
	@JsonProperty("info:ActivityId")
	public Integer getActivityId() {
		return activityId;
	}
	@JsonProperty("info:ActivityId")
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	@JsonProperty("info:RecordName")
	public String getRecordName() {
		return recordName;
	}
	@JsonProperty("info:RecordName")
	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}
	@JsonProperty("info:CreatedDateTime")
	
	public String getCreatedDateTime() {
		return createdDateTime;
	}
	@JsonProperty("info:CreatedDateTime")
	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	
	@JsonProperty("info:CreatedBy")
	public String getCreatedBy() {
		return createdBy;
	}
	@JsonProperty("info:CreatedBy")
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@JsonProperty("info:ModifiedDateTime")
	
	public String getModifiedDateTime() {
		return modifiedDateTime;
	}
	@JsonProperty("info:ModifiedDateTime")
	public void setModifiedDateTime(String modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}
	@JsonProperty("info:ModifiedBy")
	public String getModifiedBy() {
		return modifiedBy;
	}
	@JsonProperty("info:ModifiedBy")
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	@JsonProperty("info:RecordState")
	public String getRecordState() {
		return recordState;
	}
	@JsonProperty("info:RecordState")
	public void setRecordState(String recordState) {
		this.recordState = recordState;
	}
	@JsonProperty("info:Status")
	public String getStatus() {
		return status;
	}
	@JsonProperty("info:Status")
	public void setStatus(String status) {
		this.status = status;
	}
	@JsonProperty("info:ActivityUrl")
	public String getActivityUrl() {
		return activityUrl;
	}
	@JsonProperty("info:ActivityUrl")
	public void setActivityUrl(String activityUrl) {
		this.activityUrl = activityUrl;
	}
	@JsonProperty("info:SystemGeography")
	public String getSystemGeography() {
		return systemGeography;
	}
	@JsonProperty("info:SystemGeography")
	public void setSystemGeography(String systemGeography) {
		this.systemGeography = systemGeography;
	}
	@JsonProperty("info:RecordLanguage")
	public String getRecordLanguage() {
		return recordLanguage;
	}
	@JsonProperty("info:RecordLanguage")
	public void setRecordLanguage(String recordLanguage) {
		this.recordLanguage = recordLanguage;
	}
	@JsonProperty("info:ActivityName")
	public String getActivityName() {
		return activityName;
	}
	@JsonProperty("info:ActivityName")
	public void setActivityName(String activityName) {
		this.activityName = activityName.trim();
	}
	@JsonProperty("info:TypeOfWork")
	public String getTypeOfWork() {
		return typeOfWork;
	}
	@JsonProperty("info:TypeOfWork")
	public void setTypeOfWork(String typeOfWork) {
		this.typeOfWork = typeOfWork;
	}
	@JsonProperty("info:GlobalProcessStep")
	public String getGlobalProcessStep() {
		return globalProcessStep;
	}
	@JsonProperty("info:GlobalProcessStep")
	public void setGlobalProcessStep(String globalProcessStep) {
		this.globalProcessStep = globalProcessStep;
	}
	@JsonProperty("info:GlobalPlanningObjectName")
	public String getGlobalPlanningObjectName() {
		return globalPlanningObjectName;
	}
	@JsonProperty("info:GlobalPlanningObjectName")
	public void setGlobalPlanningObjectName(String globalPlanningObjectName) {
		this.globalPlanningObjectName = globalPlanningObjectName;
	}
	@JsonProperty("info:ActivityDescription")
	public String getActivityDescription() {
		return activityDescription;
	}
	@JsonProperty("info:ActivityDescription")
	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}
	@JsonProperty("info:ProcessBranch")
	public String getProcessBranch() {
		return processBranch;
	}
	@JsonProperty("info:ProcessBranch")
	public void setProcessBranch(String processBranch) {
		this.processBranch = processBranch;
	}
	@JsonProperty("info:Tags")
	public String getTags() {
		return tags;
	}
	@JsonProperty("info:Tags")
	public void setTags(String tags) {
		this.tags = tags;
	}
	@JsonProperty("info:Notes")
	public String getNotes() {
		return notes;
	}
	@JsonProperty("info:Notes")
	public void setNotes(String notes) {
		this.notes = notes;
	}
	@JsonProperty("info:ESRUrl")
	public String geteSRUrl() {
		return eSRUrl;
	}
	@JsonProperty("info:ESRUrl")
	public void seteSRUrl(String eSRUrl) {
		this.eSRUrl = eSRUrl;
	}

}
