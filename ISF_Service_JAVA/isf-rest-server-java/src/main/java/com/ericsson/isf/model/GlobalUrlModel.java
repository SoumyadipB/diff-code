package com.ericsson.isf.model;

import javax.validation.constraints.NotEmpty;

/**
 * Purpose: This Class is a model class for global Url.
 * @author ekarmuj
 *
 */

public class GlobalUrlModel {
	
    private int globalUrlId;
	@NotEmpty
	private String urlName;
	@NotEmpty
	private String urlLink;
	private String signum;
	private int roleID;
	private String createdBy;
	private String createdOn;
	private String lastModifiedBy;
	private String lastModifiedOn;
	@NotEmpty
	private String actionType;
	private boolean urlStatus;
	
	public int getGlobalUrlId() {
		return globalUrlId;
	}
	public void setGlobalUrlId(int globalUrlId) {
		this.globalUrlId = globalUrlId;
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
	public boolean getStatus() {
		return urlStatus;
	}
	public void setStatus(boolean urlStatus) {
		this.urlStatus = urlStatus;
	}
	
}
