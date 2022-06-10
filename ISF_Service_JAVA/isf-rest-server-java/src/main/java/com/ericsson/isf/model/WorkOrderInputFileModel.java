package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

public class WorkOrderInputFileModel {
	private int id;
	private int woid;
	private boolean active;
	private String createdBy;
	private Date createdOn;
	private String lastModifiedBy;
	private Date lastModifiedOn;
	private List<WOInputFileModel> file;
	private int projectID;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getWoid() {
		return woid;
	}
	public void setWoid(int woid) {
		this.woid = woid;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
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
	public List<WOInputFileModel> getFile() {
		return file;
	}
	public void setFile(List<WOInputFileModel> file) {
		this.file = file;
	}
	
	public WorkOrderInputFileModel(List<WOInputFileModel> file) {
		this.file = file;
	}
	
	
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public WorkOrderInputFileModel() {
	}
	public WorkOrderInputFileModel(int id, int woid, boolean active, String createdBy, Date createdOn,
			String lastModifiedBy, Date lastModifiedOn, List<WOInputFileModel> file) {
		this.id = id;
		this.woid = woid;
		this.active = active;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedOn = lastModifiedOn;
		this.file = file;
	}
	
	
}
