package com.ericsson.isf.model;

import java.util.Date;

public class UserPreferencesModel {
    
    private Integer preferencesId;
    private String userSignum;
    private String defaultName;
    private Integer defaultZoneId;
    private String defaultZoneValue;
    private Integer defaultProfileId;
    private String defaultProfileValue;
    private Integer defaultPageId;
    private String defaultPageName;
	private String createdBy;
	private Date createdOn;
	private String lastModifiedBy;
	private Date lastModifiedOn;
	private Integer isActive;

	
	public Integer getDefaultZoneId() {
		return defaultZoneId;
	}
	public void setDefaultZoneId(Integer defaultZoneId) {
		this.defaultZoneId = defaultZoneId;
	}
	public String getDefaultZoneValue() {
		return defaultZoneValue;
	}
	public void setDefaultZoneValue(String defaultZoneValue) {
		this.defaultZoneValue = defaultZoneValue;
	}
	public Integer getDefaultProfileId() {
		return defaultProfileId;
	}
	public void setDefaultProfileId(Integer defaultProfileId) {
		this.defaultProfileId = defaultProfileId;
	}
	public String getDefaultProfileValue() {
		return defaultProfileValue;
	}
	public void setDefaultProfileValue(String defaultProfileValue) {
		this.defaultProfileValue = defaultProfileValue;
	}
	public Integer getPreferencesId() {
		return preferencesId;
	}
	public void setPreferencesId(Integer preferencesId) {
		this.preferencesId = preferencesId;
	}
	public String getUserSignum() {
		return userSignum;
	}
	public void setUserSignum(String userSignum) {
		this.userSignum = userSignum;
	}
	public String getDefaultName() {
		return defaultName;
	}
	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
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
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	public Integer getDefaultPageId() {
		return defaultPageId;
	}
	public void setDefaultPageId(Integer defaultPageId) {
		this.defaultPageId = defaultPageId;
	}
	public String getDefaultPageName() {
		return defaultPageName;
	}
	public void setDefaultPageName(String defaultPageName) {
		this.defaultPageName = defaultPageName;
	}

}
