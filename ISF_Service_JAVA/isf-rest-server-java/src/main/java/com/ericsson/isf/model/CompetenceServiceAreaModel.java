package com.ericsson.isf.model;

public class CompetenceServiceAreaModel {
	private int competenceID;
	private String competencyServiceArea;
	private String description;
	private int competenceTypeID;
	private int domainID;
	private int vendorID;
	private int technologyID;
	private int competenceGradeID;
	public int getCompetenceID() {
		return competenceID;
	}
	public void setCompetenceID(int competenceID) {
		this.competenceID = competenceID;
	}
	public String getCompetencyServiceArea() {
		return competencyServiceArea;
	}
	public void setCompetencyServiceArea(String competencyServiceArea) {
		this.competencyServiceArea = competencyServiceArea;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCompetenceTypeID() {
		return competenceTypeID;
	}
	public void setCompetenceTypeID(int competenceTypeID) {
		this.competenceTypeID = competenceTypeID;
	}
	public int getDomainID() {
		return domainID;
	}
	public void setDomainID(int domainID) {
		this.domainID = domainID;
	}
	public int getVendorID() {
		return vendorID;
	}
	public void setVendorID(int vendorID) {
		this.vendorID = vendorID;
	}
	public int getTechnologyID() {
		return technologyID;
	}
	public void setTechnologyID(int technologyID) {
		this.technologyID = technologyID;
	}
	public int getCompetenceGradeID() {
		return competenceGradeID;
	}
	public void setCompetenceGradeID(int competenceGradeID) {
		this.competenceGradeID = competenceGradeID;
	}

}
