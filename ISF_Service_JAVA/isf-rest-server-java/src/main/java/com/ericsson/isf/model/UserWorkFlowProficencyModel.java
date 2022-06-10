/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author ekarath
 */
public class UserWorkFlowProficencyModel {
	private int wfUserProficenctID;
	private int projectID;
	private int subActivityID;
	private String signumID;

	private int wfId;
	private int displayedMode;
	private String workFlowName;
	private String proficiencyName;
	private String proficiencyLevel;
	private boolean multiMode;
	private String modifiedBy;
	private int kpiValue;
	private int woId;
	
	private String prevProficiencyName;

	public String getPrevProficiencyName() {
		return prevProficiencyName;
	}

	public void setPrevProficiencyName(String prevProficiencyName) {
		this.prevProficiencyName = prevProficiencyName;
	}

	public int getKpiValue() {
		return kpiValue;
	}

	public void setKpiValue(int kpiValue) {
		this.kpiValue = kpiValue;
	}

	public boolean isMultiMode() {
		return multiMode;
	}

	public void setMultiMode(boolean multiMode) {
		this.multiMode = multiMode;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getProficiencyName() {
		return proficiencyName;
	}

	public void setProficiencyName(String proficiencyName) {
		this.proficiencyName = proficiencyName;
	}

	public String getProficiencyLevel() {
		return proficiencyLevel;
	}

	public void setProficiencyLevel(String proficiencyLevel) {
		this.proficiencyLevel = proficiencyLevel;
	}

	public String getWorkFlowName() {
		return workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	public int getWfId() {
		return wfId;
	}

	public void setWfId(int wfId) {
		this.wfId = wfId;
	}

	public int getDisplayedMode() {
		return displayedMode;
	}

	public void setDisplayedMode(int displayedMode) {
		this.displayedMode = displayedMode;
	}


	public int getWfUserProficenctID() {
		return wfUserProficenctID;
	}

	public void setWfUserProficenctID(int wfUserProficenctID) {
		this.wfUserProficenctID = wfUserProficenctID;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public int getSubActivityID() {
		return subActivityID;
	}

	public void setSubActivityID(int subActivityID) {
		this.subActivityID = subActivityID;
	}

	public String getSignumID() {
		return signumID;
	}

	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}

	public int getWoId() {
		return woId;
	}

	public void setWoId(int woId) {
		this.woId = woId;
	}



}
