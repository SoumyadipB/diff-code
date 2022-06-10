package com.ericsson.isf.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_ABSENT)

public class ProjectScopeDetailMappingModel {
	
	List<ScopeDetailsModel> scopeDetailsModel;
	
	int projectID;
	int projectScopeID;
	String scopeName;
	public List<ScopeDetailsModel> getScopeDetailsModel() {
		return scopeDetailsModel;
	}
	public void setScopeDetailsModel(List<ScopeDetailsModel> scopeDetailsModel) {
		this.scopeDetailsModel = scopeDetailsModel;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public int getProjectScopeID() {
		return projectScopeID;
	}
	public void setProjectScopeID(int projectScopeID) {
		this.projectScopeID = projectScopeID;
	}
	public String getScopeName() {
		return scopeName;
	}
	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}
	
	

}
