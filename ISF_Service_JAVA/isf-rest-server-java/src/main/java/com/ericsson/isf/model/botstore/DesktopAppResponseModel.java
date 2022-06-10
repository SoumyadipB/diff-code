package com.ericsson.isf.model.botstore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_ISFDesktopUpdates", schema = "refData")
public class DesktopAppResponseModel {
	
	private long isfDesktopUpdatesID;
	private String updatedOn;
	private String version;
	private String updateType;
	private boolean active;
	public String getUpdateType() {
		return updateType;
	}
	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	public long getIsfDesktopUpdatesID() {
		return isfDesktopUpdatesID;
	}
	public void setIsfDesktopUpdatesID(long isfDesktopUpdatesID) {
		this.isfDesktopUpdatesID = isfDesktopUpdatesID;
	}
	
	
	

}
