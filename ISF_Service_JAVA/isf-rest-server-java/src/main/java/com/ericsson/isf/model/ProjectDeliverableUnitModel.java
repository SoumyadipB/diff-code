package com.ericsson.isf.model;

import java.util.Date;

public class ProjectDeliverableUnitModel {
    
    private int deliverableUnitID;
    private String deliverableUnitName;
    private String description;
    private int active;
	private String createdBy;
	private Date createdOn;
	private String lastModifiedBy;
	private Date lastModifiedOn;
	private boolean flag;
	private boolean method;
	private boolean operatorCount;
	
	
	public boolean isMethod() {
		return method;
	}
	public void setMethod(boolean method) {
		this.method = method;
	}
	public boolean isOperatorCount() {
		return operatorCount;
	}
	public void setOperatorCount(boolean operatorCount) {
		this.operatorCount = operatorCount;
	}
	
	public int getDeliverableUnitID() {
		return deliverableUnitID;
	}
	public void setDeliverableUnitID(int deliverableUnitID) {
		this.deliverableUnitID = deliverableUnitID;
	}
	public String getDeliverableUnitName() {
		return deliverableUnitName;
	}
	public void setDeliverableUnitName(String deliverableUnitName) {
		this.deliverableUnitName = deliverableUnitName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
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
	public boolean getFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
