package com.ericsson.isf.model;

import java.util.List;

/**
 * 
 * @author ekmbuma
 * 
 */

public class NetworkElementNewModel {
    private int networkElementID;
    private int projectID;
    private String domain;
    private String subDomain;
    private String technology;
    private String vendor;
    private String elementType;
    private String type;
    private String name;
    private String latitude;
    private String longitude;
    private String sector;
    private String Software_Release;
    private String market;
    private String uploadedON;
    private String uploadedBy;
    private String band;
    private String country;
    private String customer;
    private int pageLength;
    private int pageOffSet;
    private String sourceName;
    private String domainSubDomainName;
    private String technologyName;
    
	public int getNetworkElementID() {
		return networkElementID;
	}
	public void setNetworkElementID(int networkElementID) {
		this.networkElementID = networkElementID;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getSubDomain() {
		return subDomain;
	}
	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getSoftware_Release() {
		return Software_Release;
	}
	public void setSoftware_Release(String software_Release) {
		Software_Release = software_Release;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getUploadedON() {
		return uploadedON;
	}
	public void setUploadedON(String uploadedON) {
		this.uploadedON = uploadedON;
	}
	public String getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	public String getBand() {
		return band;
	}
	public void setBand(String band) {
		this.band = band;
	}
	
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public int getPageLength() {
		return pageLength;
	}
	public void setPageLength(int pageLength) {
		this.pageLength = pageLength;
	}
	public int getPageOffSet() {
		return pageOffSet;
	}
	public void setPageOffSet(int pageOffSet) {
		this.pageOffSet = pageOffSet;
	}
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	public String getDomainSubDomainName() {
		return domainSubDomainName;
	}
	public void setDomainSubDomainName(String domainSubDomainName) {
		this.domainSubDomainName = domainSubDomainName;
	}
	public String getTechnologyName() {
		return technologyName;
	}
	public void setTechnologyName(String technologyName) {
		this.technologyName = technologyName;
	}
	@Override
	public String toString() {
		return "NetworkElementNewModel [networkElementID=" + networkElementID + ", projectID=" + projectID + ", domain="
				+ domain + ", subDomain=" + subDomain + ", technology=" + technology + ", vendor=" + vendor
				+ ", elementType=" + elementType + ", type=" + type + ", name=" + name + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", sector=" + sector + ", Software_Release=" + Software_Release
				+ ", market=" + market + ", uploadedON=" + uploadedON + ", uploadedBy=" + uploadedBy + ", band=" + band
				+ ", country=" + country + ", customer=" + customer + ", pageLength=" + pageLength + ", pageOffSet="
				+ pageOffSet + ", sourceName=" + sourceName + "]";
	}
	
	
	

}
