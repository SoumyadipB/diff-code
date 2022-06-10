/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

public class VendorTechModel {

	public int getVendorID() {
		return vendorID;
	}

	public void setVendorID(int vendorID) {
		this.vendorID = vendorID;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public int getTechnologyID() {
		return technologyID;
	}

	public String getVendorTech() {
		return vendorTech;
	}

	public void setVendorTech(String vendorTech) {
		this.vendorTech = vendorTech;
	}

	public String getVendorTechID() {
		return vendorTechID;
	}

	public void setVendorTechID(String vendorTechID) {
		this.vendorTechID = vendorTechID;
	}

	public void setTechnologyID(int technologyID) {
		this.technologyID = technologyID;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	private int vendorID;
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	private String vendor;
	private int technologyID;
	private String technology;
	private String createdBy;
	public Integer getPositionID() {
		return positionID;
	}

	public void setPositionID(Integer positionID) {
		this.positionID = positionID;
	}

	private String vendorTech;
	private String vendorTechID;
	private Integer positionID;

}
