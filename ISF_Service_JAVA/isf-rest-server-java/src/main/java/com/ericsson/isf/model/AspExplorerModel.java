package com.ericsson.isf.model;

import java.util.Date;

import com.ericsson.isf.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class AspExplorerModel {
	
    private String email;
    private String employeeName;
    private String country;
    private String signum;
    private String vendorCode;
    private String vendorName;
    private String contactNumber;
    private String city;
    private Date startDate;
    private Date endDate;
    private Boolean isProfileActive;
    private Boolean isLocked;
    private Boolean isResetRequired;
    private Boolean isActive;
    
    @JsonIgnore
    public Boolean getIsResetRequired() {
		return isResetRequired;
	}
	public void setIsResetRequired(Boolean isResetRequired) {
		this.isResetRequired = isResetRequired;
	}
	public Boolean getIsProfileActive() {
		return isProfileActive;
	}
	public void setIsProfileActive(Boolean isProfileActive) {
		this.isProfileActive = isProfileActive;
	}
	public Boolean getIsLocked() {
		return isLocked;
	}
	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	private String Status;
    
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=AppConstants.DEFAULT_DATE_FORMAT,timezone =AppConstants.TIMEZONE_IST)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=AppConstants.DEFAULT_DATE_FORMAT,timezone =AppConstants.TIMEZONE_IST)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
}
