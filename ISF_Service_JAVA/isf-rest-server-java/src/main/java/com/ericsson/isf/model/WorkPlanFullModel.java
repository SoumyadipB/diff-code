package com.ericsson.isf.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkPlanFullModel {

	@JsonProperty("WorkPlanID") 
	public String workPlanID;
	
	@JsonProperty("WorkPlanName") 
	public String workPlanName;

	@JsonProperty("Site") 
	SiteDataModel siteDatamodel;

	@JsonProperty("Project") 
	ProjectDataModel projectDataModel;

	@JsonProperty("Activity") 
	List< ActivityDataModel> activityDataModel;
	
	
	
	public String getWorkPlanID() {
		return workPlanID;
	}

	public void setWorkPlanID(String workPlanID) {
		this.workPlanID = workPlanID;
	}

	public String getWorkPlanName() {
		return workPlanName;
	}

	public void setWorkPlanName(String workPlanName) {
		this.workPlanName = workPlanName;
	}

	public SiteDataModel getSiteDatamodel() {
		return siteDatamodel;
	}

	public void setSiteDatamodel(SiteDataModel siteDatamodel) {
		this.siteDatamodel = siteDatamodel;
	}

	public ProjectDataModel getProjectDataModel() {
		return projectDataModel;
	}

	public void setProjectDataModel(ProjectDataModel projectDataModel) {
		this.projectDataModel = projectDataModel;
	}

	public List<ActivityDataModel> getActivityDataModel() {
		return activityDataModel;
	}

	public void setActivityDataModel(List<ActivityDataModel> activityDataModel) {
		this.activityDataModel = activityDataModel;
	}

	

}
