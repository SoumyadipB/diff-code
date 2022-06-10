package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WFStepInstructionModel {

	private int instructionID;
	 private int flowChartDefID;
	 private String urlName;
	 private String urlLink;
	 private String stepID;
	 private String reference;
	 private String instructionType;
	 private String createdBy;
	 private String modifiedBy;
	 private String createdDate;
	 private String modifiedDate;
	 private int fcStepDetailsID;
	 private String mode;
	 private boolean isWorkFlowUpdated;
	 private String state;
	 
	 public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public int getFcStepDetailsID() {
		return fcStepDetailsID;
	}
	public void setFcStepDetailsID(int fcStepDetailsID) {
		this.fcStepDetailsID = fcStepDetailsID;
	}
	
	 private boolean edited;
	 
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getInstructionType() {
		return instructionType;
	}
	public void setInstructionType(String instructionType) {
		this.instructionType = instructionType;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	private boolean deleted;
	

	 private boolean active;
	  
	 
	 public int getInstructionID() {
		return instructionID;
	}
	public void setInstructionID(int instructionID) {
		this.instructionID = instructionID;
	}
	public int getFlowChartDefID() {
		return flowChartDefID;
	}
	public void setFlowChartDefID(int flowChartDefID) {
		this.flowChartDefID = flowChartDefID;
	}
	public String getUrlName() {
		return urlName;
	}
	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
	public String getUrlLink() {
		return urlLink;
	}
	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}
	public String getStepID() {
		return stepID;
	}
	public void setStepID(String stepID) {
		this.stepID = stepID;
	}
	public boolean isEdited() {
		return edited;
	}
	public void setEdited(boolean edited) {
		this.edited = edited;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isWorkFlowUpdated() {
		return isWorkFlowUpdated;
	}
	public void setWorkFlowUpdated(boolean isWorkFlowUpdated) {
		this.isWorkFlowUpdated = isWorkFlowUpdated;
	}
	
	
	
	
}
