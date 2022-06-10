package com.ericsson.erisite.report.model;

import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
public class WorkPlanFullModel {

	
	
	@JsonProperty("WorkPlanID") 
	public int workPlanID;
	
	@JsonProperty("WorkPlanName") 
	public String workPlanName;
	
	@JsonProperty("SystemRecordID") 
	public int systemRecordID;
	
	@JsonProperty("CustomerDeliverableID") 
	public int customerDeliverableID;
	
	
	@JsonProperty("Site") 
	SiteDataModel siteDatamodel;

	@JsonProperty("Project") 
	ProjectDataModel projectDataModel;

	@JsonProperty("Activity") 
	List< ActivityDataModel> activityDataModel;
	
	
	public int getSystemRecordID() {
		return systemRecordID;
	}

	public void setSystemRecordID(int systemRecordID) {
		this.systemRecordID = systemRecordID;
	}

	public int getCustomerDeliverableID() {
		return customerDeliverableID;
	}

	public void setCustomerDeliverableID(int customerDeliverableID) {
		this.customerDeliverableID = customerDeliverableID;
	}


	
	
	
	
	public int getWorkPlanID() {
		return workPlanID;
	}

	public void setWorkPlanID(int workPlanID) {
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
