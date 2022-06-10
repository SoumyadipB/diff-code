package com.ericsson.isf.model;

public class LocationTypeModel {
	private int previousDefaultID;
	private int currentLocationOptionID;
	private int locationTypeID;
	private String locationType;
	private boolean isSelected;
	private boolean isActive;
	private String modifiedBy;
	private String modifiedDate;
	private String createdBy;
	private String createdDate;
	
	
	public int getPreviousDefaultID() {
		return previousDefaultID;
	}
	public void setPreviousDefaultID(int previousDefaultID) {
		this.previousDefaultID = previousDefaultID;
	}
	public int getCurrentLocationOptionID() {
		return currentLocationOptionID;
	}
	public void setCurrentLocationOptionID(int currentLocationOptionID) {
		this.currentLocationOptionID = currentLocationOptionID;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	public void setisSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public boolean isActive() {
		return isActive;
	}
	public void setisActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public int getLocationTypeID() {
		return locationTypeID;
	}
	public void setLocationTypeID(int locationTypeID) {
		this.locationTypeID = locationTypeID;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	

}
