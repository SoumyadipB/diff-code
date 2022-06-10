package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

public class ProjectCreationModel {
    private String projectID;
	private String projectName;
	private String projectType;
	private String cPM;
	private String cpmName;
	private String cpmMailId;
	private int opportunityID;
	private String opportunityCode;
	private String projectCreator;
	private String opportunityName;
	private int countryID;
	private int companyID;
	private int customerID;
	private String operationalManager;
	private int pCode;
	private int marketAreaID;
	private Date startDate;
	private Date endDate;
	private String projectDescription;
	private int productAreaID;
	private String createdBy;
	private Date createdOn;
	private int isActive;
	private String rPM;
	private List<ProjectDocumentModel> projectDocuments;
	public String getProjectID() {
		return projectID;
	}
	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getcPM() {
		return cPM;
	}
	public void setcPM(String cPM) {
		this.cPM = cPM;
	}
	public String getCpmName() {
		return cpmName;
	}
	public void setCpmName(String cpmName) {
		this.cpmName = cpmName;
	}
	public String getCpmMailId() {
		return cpmMailId;
	}
	public void setCpmMailId(String cpmMailId) {
		this.cpmMailId = cpmMailId;
	}
	public int getOpportunityID() {
		return opportunityID;
	}
	public void setOpportunityID(int opportunityID) {
		this.opportunityID = opportunityID;
	}
	public String getOpportunityCode() {
		return opportunityCode;
	}
	public void setOpportunityCode(String opportunityCode) {
		this.opportunityCode = opportunityCode;
	}
	public String getProjectCreator() {
		return projectCreator;
	}
	public void setProjectCreator(String projectCreator) {
		this.projectCreator = projectCreator;
	}
	public String getOpportunityName() {
		return opportunityName;
	}
	public void setOpportunityName(String opportunityName) {
		this.opportunityName = opportunityName;
	}
	public int getCountryID() {
		return countryID;
	}
	public void setCountryID(int countryID) {
		this.countryID = countryID;
	}
	public int getCompanyID() {
		return companyID;
	}
	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	public String getOperationalManager() {
		return operationalManager;
	}
	public void setOperationalManager(String operationalManager) {
		this.operationalManager = operationalManager;
	}
	public int getpCode() {
		return pCode;
	}
	public void setpCode(int pCode) {
		this.pCode = pCode;
	}
	public int getMarketAreaID() {
		return marketAreaID;
	}
	public void setMarketAreaID(int marketAreaID) {
		this.marketAreaID = marketAreaID;
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
	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	public int getProductAreaID() {
		return productAreaID;
	}
	public void setProductAreaID(int productAreaID) {
		this.productAreaID = productAreaID;
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
	public int isActive() {
		return isActive;
	}
	public void setActive(int isActive) {
		this.isActive = isActive;
	}
	public String getrPM() {
		return rPM;
	}
	public void setrPM(String rPM) {
		this.rPM = rPM;
	}
	public List<ProjectDocumentModel> getProjectDocuments() {
		return projectDocuments;
	}
	public void setProjectDocument(List<ProjectDocumentModel> projectDocuments) {
		this.projectDocuments = projectDocuments;
	}

	
}
