package com.ericsson.isf.model;

import java.util.Date;

public class ResourceStatusModel {
	private String resourceStatusID;
	private String resourceStatusName;
	private String signum;
	private String resignedOrTransferredDate;
	private String releaseDate;
	private String reason;
	private String createdBy;
	private Date createdOn;
	private String lastModifiedBy;
	private Date lastModifiedOn;
	public String getResourceStatusID() {
		return resourceStatusID;
	}
	public void setResourceStatusID(String resourceStatusID) {
		this.resourceStatusID = resourceStatusID;
	}
	public String getResourceStatusName() {
		return resourceStatusName;
	}
	public void setResourceStatusName(String resourceStatusName) {
		this.resourceStatusName = resourceStatusName;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public String getResignedOrTransferredDate() {
		return resignedOrTransferredDate;
	}
	public void setResignedOrTransferredDate(String resignedOrTransferredDate) {
		this.resignedOrTransferredDate = resignedOrTransferredDate;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
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

	
	
}
