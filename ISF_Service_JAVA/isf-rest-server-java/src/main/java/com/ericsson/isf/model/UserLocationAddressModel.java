package com.ericsson.isf.model;

import java.math.BigInteger;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserLocationAddressModel {
	
	private int userAddressID;
	private int countryID;
	private int cityID;
	private String userAddress;
	private String createdBy;
	private String createdOn;
	private String modifiedBy;
	private String modifiedOn;
	private int currentLocationOptionID;
	private int locationTypeID;
	private String marketArea;
	private String customerName;
	private Date startDate;
	private Date endDate;
	private String countryCode;
	
	
	private String contactNumber;
	
	
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
	
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
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
	public int getUserAddressID() {
		return userAddressID;
	}
	public void setUserAddressID(int userAddressID) {
		this.userAddressID = userAddressID;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public int getCountryID() {
		return countryID;
	}
	public void setCountryID(int countryID) {
		this.countryID = countryID;
	}
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
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
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	

}
