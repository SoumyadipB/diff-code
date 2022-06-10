/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author eabhmoj
 */
public class ProjectsTableModel {
    private String projectID;
private String projectName;
private String projectType;
private String cPM;
private String projectCreator;
private int opportunityID;
private Date startDate;
private Date endDate;
private String projectDescription;
private String servAreaID;
private String createdBy;
private Date createdOn;
private String lastModifiedBy;
private Date lastModifiedOn;
private String status;
private String operationalManager;
private Boolean isDeleted;
private String rPM;
private List<ProjectDocumentModel> projectDocument;

    public List<ProjectDocumentModel> getProjectDocument() {
        return projectDocument;
    }

    public void setProjectDocument(List<ProjectDocumentModel> projectDocument) {
        this.projectDocument = projectDocument;
    }

    public String getProjectID() {
        return projectID;
    }

    /**
     * @param projectID the projectID to set
     */
    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return the projectType
     */
    public String getProjectType() {
        return projectType;
    }

    /**
     * @param projectType the projectType to set
     */
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    /**
     * @return the cPM
     */
    public String getcPM() {
        return cPM;
    }

    /**
     * @param cPM the cPM to set
     */
    public void setcPM(String cPM) {
        this.cPM = cPM;
    }

    /**
     * @return the projectCreator
     */
    public String getProjectCreator() {
        return projectCreator;
    }

    /**
     * @param projectCreator the projectCreator to set
     */
    public void setProjectCreator(String projectCreator) {
        this.projectCreator = projectCreator;
    }

    /**
     * @return the opportunityID
     */
    public int getOpportunityID() {
        return opportunityID;
    }

    /**
     * @param opportunityID the opportunityID to set
     */
    public void setOpportunityID(int opportunityID) {
        this.opportunityID = opportunityID;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the projectDescription
     */
    public String getProjectDescription() {
        return projectDescription;
    }

    /**
     * @param projectDescription the projectDescription to set
     */
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    /**
     * @return the servAreaID
     */
    public String getServAreaID() {
        return servAreaID;
    }

    /**
     * @param servAreaID the servAreaID to set
     */
    public void setServAreaID(String servAreaID) {
        this.servAreaID = servAreaID;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return the lastModifiedBy
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * @param lastModifiedBy the lastModifiedBy to set
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * @return the lastModifiedOn
     */
    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    /**
     * @param lastModifiedOn the lastModifiedOn to set
     */
    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the operationalManager
     */
    public String getOperationalManager() {
        return operationalManager;
    }

    /**
     * @param operationalManager the operationalManager to set
     */
    public void setOperationalManager(String operationalManager) {
        this.operationalManager = operationalManager;
    }

    /**
     * @return the isDeleted
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

	public String getrPM() {
		return rPM;
	}

	public void setrPM(String rPM) {
		this.rPM = rPM;
	}

    
}
