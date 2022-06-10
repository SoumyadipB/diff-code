package com.ericsson.isf.model;

public class CustomerModel {
private int customerID;
private String customerName;
private String country;
private int countryID;
public int isActive;
private String signum;

public String getSignum() {
	return signum;
}
public void setSignum(String signum) {
	this.signum = signum;
}
public int getIsActive() {
	return isActive;
}
public void setIsActive(int isActive) {
	this.isActive = isActive;
}
public int getCountryID() {
	return countryID;
}
public void setCountryID(int countryID) {
	this.countryID = countryID;
}
public int getCustomerID() {
	return customerID;
}
public void setCustomerID(int customerID) {
	this.customerID = customerID;
}
public String getCustomerName() {
	return customerName;
}
public void setCustomerName(String customerName) {
	this.customerName = customerName;
}
public String getCountry() {
	return country;
}
public void setCountry(String country) {
	this.country = country;
}
}
