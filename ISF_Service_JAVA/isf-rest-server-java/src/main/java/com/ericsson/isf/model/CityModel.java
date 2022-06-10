package com.ericsson.isf.model;

import java.util.Date;

public class CityModel {
	
	private int cityID;
	private int countryID;
	private String cityName;
	private String userAddress;
	private String createdBy;
	private String term;
	private String createdOn;
	private int currentLocationOptionID;
	private int locationTypeID;
	private String marketArea;
	private String customerName;
	private String startDate;
	private String endDate;
	private String countryCode;
	private boolean notificationFlag;
	private String contactNumber;
	private String currentLocationOption;

	
	

	public String getCurrentLocationOption() {
		return currentLocationOption;
	}
	public void setCurrentLocationOption(String currentLocationOption) {
		this.currentLocationOption = currentLocationOption;
	}
	public boolean isNotificationFlag() {
		return notificationFlag;
	}
	public void setNotificationFlag(boolean notificationFlag) {
		this.notificationFlag = notificationFlag;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
	public int getCountryID() {
		return countryID;
	}
	public void setCountryID(int countryID) {
		this.countryID = countryID;
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
	
	public int getCurrentLocationOptionID() {
		return currentLocationOptionID;
	}
	public void setCurrentLocationOptionID(int currentLocationOptionID) {
		this.currentLocationOptionID = currentLocationOptionID;
	}
	public int getLocationTypeID() {
		return locationTypeID;
	}
	public void setLocationTypeID(int locationTypeID) {
		this.locationTypeID = locationTypeID;
	}
	public String getMarketArea() {
		return marketArea;
	}
	public void setMarketArea(String marketArea) {
		this.marketArea = marketArea;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	
	
	
	
	
	

}
