package com.ericsson.isf.model;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ResourceRequestCompetenceModel {
	private int resourceRequestCompetenceId;
	private String competenceLevel;
	private String createdBy;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date createdOn;
	private String lastModifiedBy;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date lastModifiedOn;
	private int competenceId;
	private int resourceRequestId;
	private boolean active;
	public int getResourceRequestCompetenceId() {
		return resourceRequestCompetenceId;
	}
	
	public void setResourceRequestCompetenceId(int resourceRequestCompetenceId) {
		this.resourceRequestCompetenceId = resourceRequestCompetenceId;
	}

	public String getCompetenceLevel() {
		return competenceLevel;
	}
	public void setCompetenceLevel(String competenceLevel) {
		this.competenceLevel = competenceLevel;
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
	public int getCompetenceId() {
		return competenceId;
	}
	public void setCompetenceId(int competenceId) {
		this.competenceId = competenceId;
	}
	public int getResourceRequestId() {
		return resourceRequestId;
	}
	public void setResourceRequestId(int resourceRequestId) {
		this.resourceRequestId = resourceRequestId;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

}
