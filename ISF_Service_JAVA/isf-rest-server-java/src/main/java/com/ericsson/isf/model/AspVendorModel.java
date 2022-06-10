package com.ericsson.isf.model;

/**
*
* @author esudbhu
*/

public class AspVendorModel {
	
	private String vendorCode;
	private String country;
	private String vendorName;
	private String vendorContactDetails;
	private String managerSignum;
	private int isActive;
	private String signum;
	private int aspVendorDetailId;
	
	public String getVendorContactDetails() {
		return vendorContactDetails;
	}

	public void setVendorContactDetails(String vendorContactDetails) {
		this.vendorContactDetails = vendorContactDetails;
	}

	public String getManagerSignum() {
		return managerSignum;
	}

	public void setManagerSignum(String managerSignum) {
		this.managerSignum = managerSignum;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	
	
	public String getVendorCode() {
		return vendorCode;
	}
	
	public String getCountry() {
		return country;
	}
	
	public String getVendorName() {
		return vendorName;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public String getSignum() {
		return signum;
	}

	public void setSignum(String signum) {
		this.signum = signum;
	}

	public int getAspVendorDetailId() {
		return aspVendorDetailId;
	}

	public void setAspVendorDetailId(int aspVendorDetailId) {
		this.aspVendorDetailId = aspVendorDetailId;
	}

}
