package com.ericsson.isf.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_ISFDesktopVersionUserWise", schema = "transactionalData")
public class UserWiseDesktopVersionModel {

	private long VersionID;
	private String signumId;
	private String version;
	private Date updatedOn;
	
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
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Column(name = "updatedOn" , insertable=false, updatable=false)
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
}
