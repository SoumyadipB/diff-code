package com.ericsson.erisite.report.model;
import java.util.Date;

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
public class ActivityDataModel {

	@JsonProperty("SystemRecordID")
	private int systemRecordID;

	@JsonProperty("ActivityID")
	private int id;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("RecordName")
	private String recordName;
	@JsonProperty("RecordState")
	private String recordState;
	@JsonProperty("FormName") 
	public String formName;
	@JsonProperty("TypeOfWork") 
	public String typeOfWork;
	@JsonProperty("GlobalProcessStep") 
	public String globalProcessStep;
	@JsonProperty("GlobalPOName") 
	public String globalPOName;
	@JsonProperty("AssignedWorkgroup") 
	public String assignedWorkgroup;
	@JsonProperty("Status") 
	public String status;
	@JsonProperty("SystemGeography") 
	public String systemGeography;
	@JsonProperty("PlannedEndDate") 
	public Date plannedEndDate;
	@JsonProperty("BaselineStartDate") 
	public Date baselineStartDate;
	@JsonProperty("ActualEndDate") 
	public Date actualEndDate;
	@JsonProperty("ActualStartDate") 
	public Date actualStartDate;
	@JsonProperty("ProcessBranch") 
	public int processBranch;
	@JsonProperty("ChangeCode") 
	public String changeCode;
	@JsonProperty("PlannedStartDate") 
	public Date plannedStartDate;
	@JsonProperty("ModifiedDateTime") 
	public Date modifiedDateTime;
	@JsonProperty("ActivityDescription") 
	public int activityDescription;
	@JsonProperty("MileageStopTime") 
	public int mileageStopTime;
	@JsonProperty("CreatedBy") 
	public String createdBy;
	@JsonProperty("MileageStartTime") 
	public int mileageStartTime;
	@JsonProperty("Mileage") 
	public int mileage;
	@JsonProperty("ForecastStartDate") 
	public Date forecastStartDate;
	@JsonProperty("ForecastEndDate") 
	public Date forecastEndDate;
	@JsonProperty("ModifiedBy") 
	public String modifiedBy;
	@JsonProperty("CreatedDateTime") 
	public Date createdDateTime;
	@JsonProperty("BaselineEndDate") 
	public Date baselineEndDate;
	@JsonProperty("RecordLanguage") 
	public String recordLanguage;
	@JsonProperty("MileageNotes") 
	public int mileageNotes;
	@JsonProperty("BusinessObject") 
	public String businessObject;
	@JsonProperty("Notes") 
	public int notes;


	@JsonProperty("Project") 
	ProjectDataModel projectDataModel;


	@JsonProperty("Workplan") 
	WorkPlanFullModel workPlanFullModel;



	public ProjectDataModel getProjectDataModel() {
		return projectDataModel;
	}
	public void setProjectDataModel(ProjectDataModel projectDataModel) {
		this.projectDataModel = projectDataModel;
	}
	public WorkPlanFullModel getWorkPlanFullModel() {
		return workPlanFullModel;
	}
	public void setWorkPlanFullModel(WorkPlanFullModel workPlanFullModel) {
		this.workPlanFullModel = workPlanFullModel;
	}

	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getTypeOfWork() {
		return typeOfWork;
	}
	public void setTypeOfWork(String typeOfWork) {
		this.typeOfWork = typeOfWork;
	}
	public String getGlobalProcessStep() {
		return globalProcessStep;
	}
	public void setGlobalProcessStep(String globalProcessStep) {
		this.globalProcessStep = globalProcessStep;
	}
	public String getGlobalPOName() {
		return globalPOName;
	}
	public void setGlobalPOName(String globalPOName) {
		this.globalPOName = globalPOName;
	}
	public String getAssignedWorkgroup() {
		return assignedWorkgroup;
	}
	public void setAssignedWorkgroup(String assignedWorkgroup) {
		this.assignedWorkgroup = assignedWorkgroup;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSystemGeography() {
		return systemGeography;
	}
	public void setSystemGeography(String systemGeography) {
		this.systemGeography = systemGeography;
	}
	public Date getPlannedEndDate() {
		return plannedEndDate;
	}
	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}
	public Date getBaselineStartDate() {
		return baselineStartDate;
	}
	public void setBaselineStartDate(Date baselineStartDate) {
		this.baselineStartDate = baselineStartDate;
	}
	public Date getActualEndDate() {
		return actualEndDate;
	}
	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	public Date getActualStartDate() {
		return actualStartDate;
	}
	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	public int getProcessBranch() {
		return processBranch;
	}
	public void setProcessBranch(int processBranch) {
		this.processBranch = processBranch;
	}
	public String getChangeCode() {
		return changeCode;
	}
	public void setChangeCode(String changeCode) {
		this.changeCode = changeCode;
	}
	public Date getPlannedStartDate() {
		return plannedStartDate;
	}
	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}
	public Date getModifiedDateTime() {
		return modifiedDateTime;
	}
	public void setModifiedDateTime(Date modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}
	public Object getActivityDescription() {
		return activityDescription;
	}
	public void setActivityDescription(int activityDescription) {
		this.activityDescription = activityDescription;
	}
	public Object getMileageStopTime() {
		return mileageStopTime;
	}
	public void setMileageStopTime(int mileageStopTime) {
		this.mileageStopTime = mileageStopTime;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Object getMileageStartTime() {
		return mileageStartTime;
	}
	public void setMileageStartTime(int mileageStartTime) {
		this.mileageStartTime = mileageStartTime;
	}
	public int getMileage() {
		return mileage;
	}
	public void setMileage(int mileage) {
		this.mileage = mileage;
	}
	public Date getForecastStartDate() {
		return forecastStartDate;
	}
	public void setForecastStartDate(Date forecastStartDate) {
		this.forecastStartDate = forecastStartDate;
	}
	public Date getForecastEndDate() {
		return forecastEndDate;
	}
	public void setForecastEndDate(Date forecastEndDate) {
		this.forecastEndDate = forecastEndDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public Date getBaselineEndDate() {
		return baselineEndDate;
	}
	public void setBaselineEndDate(Date baselineEndDate) {
		this.baselineEndDate = baselineEndDate;
	}
	public String getRecordLanguage() {
		return recordLanguage;
	}
	public void setRecordLanguage(String recordLanguage) {
		this.recordLanguage = recordLanguage;
	}
	public Object getMileageNotes() {
		return mileageNotes;
	}
	public void setMileageNotes(int mileageNotes) {
		this.mileageNotes = mileageNotes;
	}
	public String getBusinessObject() {
		return businessObject;
	}
	public void setBusinessObject(String businessObject) {
		this.businessObject = businessObject;
	}
	public Object getNotes() {
		return notes;
	}
	public void setNotes(int notes) {
		this.notes = notes;
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
	public String getRecordName() {
		return recordName;
	}
	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}
	public String getRecordState() {
		return recordState;
	}
	public void setRecordState(String recordState) {
		this.recordState = recordState;
	}

}
