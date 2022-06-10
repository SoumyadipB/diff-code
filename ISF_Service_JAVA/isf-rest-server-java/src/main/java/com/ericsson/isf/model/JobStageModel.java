package com.ericsson.isf.model;

import java.util.Date;

public class JobStageModel {
	private int jobStageID;
	private String jobStageName;
	private String createdBy;
	private Date createdOn;
	private String lastModifiedBy;
	private Date lastModifiedOn;
	private boolean isActive;
	
	public int getJobStageID() {
		return jobStageID;
	}
	public void setJobStageID(int jobStageID) {
		this.jobStageID = jobStageID;
	}
	public String getJobStageName() {
		return jobStageName;
	}
	public void setJobStageName(String jobStageName) {
		this.jobStageName = jobStageName;
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
	public String getLastModifiedBy() {
		return lastModifiedBy;
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
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
