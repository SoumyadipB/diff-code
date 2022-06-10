package com.ericsson.isf.model;

import java.util.List;

public class ProjectAllDetailsModel {
	private int projectID;
	private String projectName;
	private String projectType;
	private String cPM;
	private String rPM;
	private String projectCreator;
	private String status;
	private OpportunityDetailsModel opportunityDetails;
	private String startDate;
	private String endDate;
	private String projectDescription;
	private String productAreaID;
	private String documentType;
	private String documentLinks;
	private String projectDocumentID;
	private String operationalManager;
	private String pCode;
	private String createdBy;
	private List<ProjectDocumentModel> projectDocuments;
	private String serverBotOutputUrl;
	
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
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
	public String getrPM() {
		return rPM;
	}
	public void setrPM(String rPM) {
		this.rPM = rPM;
	}
	public String getProjectCreator() {
		return projectCreator;
	}
	public void setProjectCreator(String projectCreator) {
		this.projectCreator = projectCreator;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public OpportunityDetailsModel getOpportunityDetails() {
		return opportunityDetails;
	}
	public void setOpportunityDetails(OpportunityDetailsModel opportunityDetails) {
		this.opportunityDetails = opportunityDetails;
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
	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	public String getProductAreaID() {
		return productAreaID;
	}
	public void setProductAreaID(String productAreaID) {
		this.productAreaID = productAreaID;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getDocumentLinks() {
		return documentLinks;
	}
	public void setDocumentLinks(String documentLinks) {
		this.documentLinks = documentLinks;
	}
	public String getProjectDocumentID() {
		return projectDocumentID;
	}
	public void setProjectDocumentID(String projectDocumentID) {
		this.projectDocumentID = projectDocumentID;
	}
	public String getOperationalManager() {
		return operationalManager;
	}
	public void setOperationalManager(String operationalManager) {
		this.operationalManager = operationalManager;
	}
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public List<ProjectDocumentModel> getProjectDocuments() {
		return projectDocuments;
	}
	public void setProjectDocuments(List<ProjectDocumentModel> projectDocuments) {
		this.projectDocuments = projectDocuments;
	}
	public String getServerBotOutputUrl() {
		return serverBotOutputUrl;
	}
	public void setServerBotOutputUrl(String serverBotOutputUrl) {
		this.serverBotOutputUrl = serverBotOutputUrl;
	}
}
