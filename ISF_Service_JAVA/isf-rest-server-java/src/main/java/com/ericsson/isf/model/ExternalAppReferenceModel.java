package com.ericsson.isf.model;

import java.util.Date;

import com.ericsson.isf.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ExternalAppReferenceModel {

	private Integer referenceId;
	private Integer sourceId   ;
	private String  createdby;
	private Date  createdOn;
	private boolean isactive;
	private Integer projectId;
	private Integer externalProjectId;
	private String sourcename;
	
	
	public String getSourcename() {
		return sourcename;
	}
	public void setSourcename(String sourcename) {
		this.sourcename = sourcename;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public Integer getExternalProjectId() {
		return externalProjectId;
	}
	public void setExternalProjectId(Integer externalProjectId) {
		this.externalProjectId = externalProjectId;
	}
	public Integer getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}
	public Integer getSourceId() {
		return sourceId;
	}
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=AppConstants.UI_DATE_FORMAT,timezone =AppConstants.TIMEZONE_IST)
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public boolean isIsactive() {
		return isactive;
	}
	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}
	
	}
