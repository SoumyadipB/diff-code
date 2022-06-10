package com.ericsson.isf.model;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class CompetenceModel {
	private int competenceId;
	private String competenceName;
	private String competenceType;
	private int parentCompetenceId;
	private String createdBy;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date createdOn;
	
	private String lastModifiedBy;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date lastModifiedOn;
	
	private boolean active;

	public int getCompetenceId() {
		return competenceId;
	}

	public void setCompetenceId(int competenceId) {
		this.competenceId = competenceId;
	}

	public String getCompetenceName() {
		return competenceName;
	}

	public void setCompetenceName(String competenceName) {
		this.competenceName = competenceName;
	}

	public String getCompetenceType() {
		return competenceType;
	}

	public void setCompetenceType(String competenceType) {
		this.competenceType = competenceType;
	}

	public int getParentCompetenceId() {
		return parentCompetenceId;
	}

	public void setParentCompetenceId(int parentCompetenceId) {
		this.parentCompetenceId = parentCompetenceId;
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
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	

}
