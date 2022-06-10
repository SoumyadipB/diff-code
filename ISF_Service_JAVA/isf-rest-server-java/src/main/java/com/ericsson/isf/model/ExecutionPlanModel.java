/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;


/**
 *
 * @author edhhklu
 */
public class ExecutionPlanModel {
	private int executionPlanId;
	private int externalProjectId;
    private int projectId;
    private String planName;
    private String planSourceid;
    private String planExternalReference;
    private int version;
    private boolean isActive;
    private String createdby;
    private String updatedBy;
    private String currentUser;
    private String externalWorkplanTemplate;
    private List<ExecutionPlanDetail> tasks;
    private int subactivityId;
    private LinkModel[] links;
    private boolean duplicateExecutionPlan;
    
    private int doid;
    private String deliverableUnitName;


    public int getDoid() {
		return doid;
	}
	public void setDoid(int doid) {
		this.doid = doid;
	}
	public String getDeliverableUnitName() {
		return deliverableUnitName;
	}
	public void setDeliverableUnitName(String deliverableUnitName) {
		this.deliverableUnitName = deliverableUnitName;
	}
	public boolean isDuplicateExecutionPlan() {
		return duplicateExecutionPlan;
	}
	public void setDuplicateExecutionPlan(boolean duplicateExecutionPlan) {
		this.duplicateExecutionPlan = duplicateExecutionPlan;
	}
	@JsonIgnore
    public String getLinksJson() {
    	Gson gson = new Gson();
    	return (links==null)?"":gson.toJson(links);
	}
    @JsonIgnore
    public void setLinksJson(String stringJson){
    	Gson gson = new Gson();
    	links = gson.fromJson(stringJson, LinkModel[].class);  
    	
    }
    
	public LinkModel[] getLinks() {
		return links;
	}
	public void setLinks(LinkModel[] links) {
		this.links = links;
	}
	public int getExecutionPlanId() {
		return executionPlanId;
	}
	public void setExecutionPlanId(int executionPlanId) {
		this.executionPlanId = executionPlanId;
	}
	public String getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
	
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getPlanSourceid() {
		return planSourceid;
	}
	public void setPlanSourceid(String planSourceid) {
		this.planSourceid = planSourceid;
	}
	public String getPlanExternalReference() {
		return planExternalReference;
	}
	public void setPlanExternalReference(String planExternalReference) {
		this.planExternalReference = planExternalReference;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public List<ExecutionPlanDetail> getTasks() {
		return tasks;
	}
	public void setTasks(List<ExecutionPlanDetail> tasks) {
		this.tasks = tasks;
	}
	public int getSubactivityId() {
		return subactivityId;
	}
	public void setSubactivityId(int subactivityId) {
		this.subactivityId = subactivityId;
	}
	public int getExternalProjectId() {
		return externalProjectId;
	}
	public void setExternalProjectId(int externalProjectId) {
		this.externalProjectId = externalProjectId;
	}
	public String getExternalWorkplanTemplate() {
		return externalWorkplanTemplate;
	}
	public void setExternalWorkplanTemplate(String externalWorkplanTemplate) {
		this.externalWorkplanTemplate = externalWorkplanTemplate;
	}
   
}
