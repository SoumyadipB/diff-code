package com.ericsson.isf.model;

/**
 * Purpose: This Class is a model class for local Url.
 * @author ekarmuj
 *
 */
public class LocalUrlModel {

    private int localUrlId;
	private String localUrlName;
	private String localUrlLink;
	private String signum;
	private int roleID;
	private String createdBy;
	private String createdOn;
	private String lastModifiedBy;
	private String lastModifiedOn;
	private String actionType;
	private boolean localUrlStatus;
	private int projectID;
	
	public int getLocalUrlId() {
		return localUrlId;
	}
	public void setLocalUrlId(int localUrlId) {
		this.localUrlId = localUrlId;
	}
	public String getLocalUrlName() {
		return localUrlName;
	}
	public void setLocalUrlName(String localUrlName) {
		this.localUrlName = localUrlName;
	}
	public String getLocalUrlLink() {
		return localUrlLink;
	}
	public void setLocalUrlLink(String localUrlLink) {
		this.localUrlLink = localUrlLink;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public int getRoleID() {
		return roleID;
	}
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getLastModifiedOn() {
		return lastModifiedOn;
	}
	public void setLastModifiedOn(String lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public boolean getLocalUrlStatus() {
		return localUrlStatus;
	}
	public void setLocalUrlStatus(boolean localUrlStatus) {
		this.localUrlStatus = localUrlStatus;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	
	

}
