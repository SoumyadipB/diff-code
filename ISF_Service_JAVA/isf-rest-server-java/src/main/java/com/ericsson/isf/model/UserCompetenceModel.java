package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author eivsnia
 *
 */
public class UserCompetenceModel {
	
	private int id;
	private String userCompetanceID;
    private String loggedInSignum;
    private String requestedBySignum;
    private int vendorID;
    private String vendor;
    private int competanceID;
    private String competencyServiceArea;
    private int deliveryCompetanceID;
    private int baseline;
    private int competenceUpgradeID;
    private String competenceUpgrade;
    private int parentSystemID;
    private String lmSignum;
    private String slmSignum;
    private String status;
    private String changedBy;
    private Date createdon;
    private String createdBy;
    private int domainID;
    private String domain;
    private int technologyID;
    private String technology;
    private String ambition;
    private int competenceGradeID;
    private boolean isEditable;
    private String competenceGrade;
    private String competanceUpgradeLevel;
    private boolean active;
    private String isProgress;
    private float ojtHours;
    private float assessmentScore;
    private float deliveryScore;
    private float scopeComplexity;
    
    
	public int getCompetenceTypeID() {
		return competenceTypeID;
	}
	public void setCompetenceTypeID(int competenceTypeID) {
		this.competenceTypeID = competenceTypeID;
	}
	public String getCompetencyUpgrade() {
		return competencyUpgrade;
	}
	public void setCompetencyUpgrade(String competencyUpgrade) {
		this.competencyUpgrade = competencyUpgrade;
	}
	private int competenceTypeID;
    private String competencyUpgrade;
    
    private List<String> requestedStatus;
    
	public List<String> getRequestedStatus() {
		return requestedStatus;
	}
	public void setRequestedStatus(List<String> requestedStatus) {
		this.requestedStatus = requestedStatus;
	}
	public int getDomainID() {
		return domainID;
	}
	public void setDomainID(int domainID) {
		this.domainID = domainID;
	}
	public int getTechnologyID() {
		return technologyID;
	}
	public void setTechnologyID(int technologyID) {
		this.technologyID = technologyID;
	}

	public String getUserCompetanceID() {
		return userCompetanceID;
	}
	public void setUserCompetanceID(String userCompetanceID) {
		this.userCompetanceID = userCompetanceID;
	}
	public String getLoggedInSignum() {
		return loggedInSignum;
	}
	public void setLoggedInSignum(String loggedInSignum) {
		this.loggedInSignum = loggedInSignum;
	}
	public String getRequestedBySignum() {
		return requestedBySignum;
	}
	public void setRequestedBySignum(String requestedBySignum) {
		this.requestedBySignum = requestedBySignum;
	}
	public int getVendorID() {
		return vendorID;
	}
	public void setVendorID(int vendorID) {
		this.vendorID = vendorID;
	}
	public int getCompetanceID() {
		return competanceID;
	}
	public void setCompetanceID(int competanceID) {
		this.competanceID = competanceID;
	}
	public int getDeliveryCompetanceID() {
		return deliveryCompetanceID;
	}
	public void setDeliveryCompetanceID(int deliveryCompetanceID) {
		this.deliveryCompetanceID = deliveryCompetanceID;
	}
	public int getBaseline() {
		return baseline;
	}
	public void setBaseline(int baseline) {
		this.baseline = baseline;
	}
	public int getCompetenceUpgradeID() {
		return competenceUpgradeID;
	}
	public void setCompetenceUpgradeID(int competenceUpgradeID) {
		this.competenceUpgradeID = competenceUpgradeID;
	}
	public int getParentSystemID() {
		return parentSystemID;
	}
	public void setParentSystemID(int parentSystemID) {
		this.parentSystemID = parentSystemID;
	}
	public String getLmSignum() {
		return lmSignum;
	}
	public void setLmSignum(String lmSignum) {
		this.lmSignum = lmSignum;
	}
	public String getSlmSignum() {
		return slmSignum;
	}
	public void setSlmSignum(String slmSignum) {
		this.slmSignum = slmSignum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getChangedBy() {
		return changedBy;
	}
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}
	public Date getCreatedon() {
		return createdon;
	}
	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getAmbition() {
		return ambition;
	}
	public void setAmbition(String ambition) {
		this.ambition = ambition;
	}
	public int getCompetenceGradeID() {
		return competenceGradeID;
	}
	public void setCompetenceGradeID(int competenceGradeID) {
		this.competenceGradeID = competenceGradeID;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getCompetencyServiceArea() {
		return competencyServiceArea;
	}
	public void setCompetencyServiceArea(String competencyServiceArea) {
		this.competencyServiceArea = competencyServiceArea;
	}
	public String getCompetenceUpgrade() {
		return competenceUpgrade;
	}
	public void setCompetenceUpgrade(String competenceUpgrade) {
		this.competenceUpgrade = competenceUpgrade;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public boolean getIsEditable() {
		return isEditable;
	}
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	public String getCompetenceGrade() {
		return competenceGrade;
	}
	public void setCompetenceGrade(String competenceGrade) {
		this.competenceGrade = competenceGrade;
	}
	public String getCompetanceUpgradeLevel() {
		return competanceUpgradeLevel;
	}
	public void setCompetanceUpgradeLevel(String competanceUpgradeLevel) {
		this.competanceUpgradeLevel = competanceUpgradeLevel;
	}

	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public UserCompetenceModel(int id, String loggedInSignum, String requestedBySignum, int vendorID,
			int competanceID, int competenceUpgradeID, int parentSystemID, String status, String changedBy,
			String createdBy, int domainID, int technologyID, String ambition, int competenceGradeID,
			boolean isEditable, int competenceTypeID, List<String> requestedStatus, String lmSignum) {
		this.id = id;
		this.loggedInSignum = loggedInSignum;
		this.requestedBySignum = requestedBySignum;
		this.vendorID = vendorID;
		this.competanceID = competanceID;
		this.competenceUpgradeID = competenceUpgradeID;
		this.parentSystemID = parentSystemID;
		this.status = status;
		this.changedBy = changedBy;
		this.createdBy = createdBy;
		this.domainID = domainID;
		this.technologyID = technologyID;
		this.ambition = ambition;
		this.competenceGradeID = competenceGradeID;
		this.isEditable = isEditable;
		this.competenceTypeID = competenceTypeID;
		this.requestedStatus = requestedStatus;
		this.lmSignum = lmSignum;
	}

	public UserCompetenceModel(String loggedInSignum, String requestedBySignum, List<String> requestedStatus) {
		this.loggedInSignum = loggedInSignum;
		this.requestedBySignum = requestedBySignum;
		this.requestedStatus = requestedStatus;
	}

	public UserCompetenceModel() {
	}
	public String getIsProgress() {
		return isProgress;
	}
	public void setIsProgress(String isProgress) {
		this.isProgress = isProgress;
	}
	public float getOjtHours() {
		return ojtHours;
	}
	public void setOjtHours(float ojtHours) {
		this.ojtHours = ojtHours;
	}
	public float getAssessmentScore() {
		return assessmentScore;
	}
	public void setAssessmentScore(float assessmentScore) {
		this.assessmentScore = assessmentScore;
	}
	public float getDeliveryScore() {
		return deliveryScore;
	}
	public void setDeliveryScore(float deliveryScore) {
		this.deliveryScore = deliveryScore;
	}
	public float getScopeComplexity() {
		return scopeComplexity;
	}
	public void setScopeComplexity(float scopeComplexity) {
		this.scopeComplexity = scopeComplexity;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
