package com.ericsson.isf.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_ISFLibraryVersionUserWise", schema = "transactionalData")
public class UserLibraryVersionModel {
	
	private long VersionID;
	private String signumId;
	private String libraryVersion;
	private Date updatedOn;
	private String createdBy;
	private Date createdDate;
	
	private boolean isActive;
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	
	public long getVersionID() {
		return VersionID;
	}
	public void setVersionID(long versionID) {
		VersionID = versionID;
	}
	public String getSignumId() {
		return signumId;
	}
	public void setSignumId(String signumId) {
		this.signumId = signumId;
	}
	
	@Column(name = "updatedOn" , insertable=false, updatable=false)
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getLibraryVersion() {
		return libraryVersion;
	}
	public void setLibraryVersion(String libraryVersion) {
		this.libraryVersion = libraryVersion;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	@Column(name = "createdDate" , insertable=false, updatable=false)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Column(name = "isActive", nullable = false)
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}	
	

}
