package com.ericsson.isf.model;

import java.util.List;

public class WorkFlowUserProficiencyModel {

	private String signumID ;
	
	private int projectID ;
	private String proficiencyMeasurement ;
	private String workFlowName;
	private String createdBy;
	private int woID ;
	
	public int getWoID() {
		return woID;
	}
	public void setWoID(int woID) {
		this.woID = woID;
	}
	private List<SubActivityWfIDModel> subActivityWfIDModel;
	
	public List<SubActivityWfIDModel> getSubActivityWfIDModel() {
		return subActivityWfIDModel;
	}
	public void setSubActivityWfIDModel(List<SubActivityWfIDModel> subActivityWfIDModel) {
		this.subActivityWfIDModel = subActivityWfIDModel;
	}
	public String getWorkFlowName() {
		return workFlowName;
	}
	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}
	public String getSignumID() {
		return signumID;
	}
	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getProficiencyMeasurement() {
		return proficiencyMeasurement;
	}
	public void setProficiencyMeasurement(String proficiencyMeasurement) {
		this.proficiencyMeasurement = proficiencyMeasurement;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	

}
