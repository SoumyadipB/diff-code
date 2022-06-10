/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.ericsson.isf.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

import antlr.collections.List;

/**
 *
 * @author ekarath
 */
public class BotSavingModel {
	
	private String term;
	private int oldDefID;
	private String emeCalculationDefID;
	private String oldStepId;
	private int bOTID;
	private String bOTStepID;
	private String createdBy;
	private Date createdOn;
	private int projectID;
	private int subActivity;
	private String totalSavings;
	private String expectedSavings;
	private String remark;
	private String lastModifiedBy;
	private Date lastModifiedOn;
	private ArrayList<Map<String,String>> oldSteps;
	private ArrayList<Map<String,String>> newSteps;
	private ArrayList<String> versionList;
	private int versionCount;
	private String wfName;
	private String executionType;
	private String isSavingFlag;
	private String currentYearSavings;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date postPeriodStartDate;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date postPeriodEndDate;
	
	private int newDefID;
	private int newVersion;
	
	public int getNewVersion() {
		return newVersion;
	}
	public void setNewVersion(int newVersion) {
		this.newVersion = newVersion;
	}
	public int getNewDefID() {
		return newDefID;
	}
	public void setNewDefID(int newDefID) {
		this.newDefID = newDefID;
	}
	private int wfid;
	
	public int getOldDefID() {
		return oldDefID;
	}
	public void setOldDefID(int oldDefID) {
		this.oldDefID = oldDefID;
	}
	public String getOldStepId() {
		return oldStepId;
	}
	public void setOldStepId(String oldStepId) {
		this.oldStepId = oldStepId;
	}
	public int getbOTID() {
		return bOTID;
	}
	public void setbOTID(int bOTID) {
		this.bOTID = bOTID;
	}
	public String getbOTStepID() {
		return bOTStepID;
	}
	public void setbOTStepID(String bOTStepID) {
		this.bOTStepID = bOTStepID;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public int getSubActivity() {
		return subActivity;
	}
	public String getExpectedSavings() {
		return expectedSavings;
	}
	public void setExpectedSavings(String expectedSavings) {
		this.expectedSavings = expectedSavings;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setSubActivity(int subActivity) {
		this.subActivity = subActivity;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public String getCurrentYearSavings() {
		return currentYearSavings;
	}
	public void setCurrentYearSavings(String currentYearSavings) {
		this.currentYearSavings = currentYearSavings;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}
	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}
	public ArrayList<Map<String, String>> getOldSteps() {
		return oldSteps;
	}
	
	public String getIsSavingFlag() {
		return isSavingFlag;
	}
	public void setIsSavingFlag(String isSavingFlag) {
		this.isSavingFlag = isSavingFlag;
	}
	public void setOldSteps(ArrayList<Map<String, String>> oldSteps) {
		this.oldSteps = oldSteps;
	}
	public ArrayList<Map<String, String>> getNewSteps() {
		return newSteps;
	}
	public void setNewSteps(ArrayList<Map<String, String>> newSteps) {
		this.newSteps = newSteps;
	}
	public String getExecutionType() {
		return executionType;
	}
	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}
	public String getWfName() {
		return wfName;
	}
	public void setWfName(String wfName) {
		this.wfName = wfName;
	}
	public int getVersionCount() {
		return versionCount;
	}
	public void setVersionCount(int versionCount) {
		this.versionCount = versionCount;
	}
	public ArrayList<String> getVersionList() {
		return versionList;
	}
	public void setVersionList(ArrayList<String> versionList) {
		this.versionList = versionList;
	}
	public String getTotalSavings() {
		return totalSavings;
	}
	public void setTotalSavings(String totalSavings) {
		this.totalSavings = totalSavings;
	}

	public Date getPostPeriodStartDate() {
		return postPeriodStartDate;
	}
	public void setPostPeriodStartDate(Date postPeriodStartDate) {
		this.postPeriodStartDate = postPeriodStartDate;
	}
	public Date getPostPeriodEndDate() {
		return postPeriodEndDate;
	}
	public void setPostPeriodEndDate(Date postPeriodEndDate) {
		this.postPeriodEndDate = postPeriodEndDate;
	}
	
	public int getWfid() {
		return wfid;
	}
	public void setWfid(int wfid) {
		this.wfid = wfid;
	}
	public String getEmeCalculationDefID() {
		return emeCalculationDefID;
	}
	public void setEmeCalculationDefID(String emeCalculationDefID) {
		this.emeCalculationDefID = emeCalculationDefID;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}

}
