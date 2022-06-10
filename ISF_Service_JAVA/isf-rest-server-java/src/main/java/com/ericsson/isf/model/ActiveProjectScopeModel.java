package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author ekmbuma
 *
 */
public class ActiveProjectScopeModel {

	private int projectScopeID;
	private String scopeName;
	private Date startDate;
	private Date endDate;
	private int projectID;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;   
	private boolean active;
	private String requestType;
	private String loggedInUser;
	private String deliverableUnit;
	private String vendorCode;
	private String vendorName;
	private boolean isWorkFlowCreated;
	private boolean hasDeliverablePlan;
	private String source;
	private String sourceId;
	private String externalReference;
	private Integer deliverablePlanId;
	private Integer externalProjectId;
	private String deliverableStatus;
	private String externalWorkplanTemplate;
	private List<ScopeDetailsModel> scopeDetails;
	
	public int getProjectScopeID() {
		return projectScopeID;
	}
	public void setProjectScopeID(int projectScopeID) {
		this.projectScopeID = projectScopeID;
	}
	public String getScopeName() {
		return scopeName;
	}
	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
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
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getLoggedInUser() {
		return loggedInUser;
	}
	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
	public String getDeliverableUnit() {
		return deliverableUnit;
	}
	public void setDeliverableUnit(String deliverableUnit) {
		this.deliverableUnit = deliverableUnit;
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
	public boolean isWorkFlowCreated() {
		return isWorkFlowCreated;
	}
	public void setWorkFlowCreated(boolean isWorkFlowCreated) {
		this.isWorkFlowCreated = isWorkFlowCreated;
	}
	public boolean isHasDeliverablePlan() {
		return hasDeliverablePlan;
	}
	public void setHasDeliverablePlan(boolean hasDeliverablePlan) {
		this.hasDeliverablePlan = hasDeliverablePlan;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getExternalReference() {
		return externalReference;
	}
	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}
	public Integer getDeliverablePlanId() {
		return deliverablePlanId;
	}
	public void setDeliverablePlanId(Integer deliverablePlanId) {
		this.deliverablePlanId = deliverablePlanId;
	}
	public Integer getExternalProjectId() {
		return externalProjectId;
	}
	public void setExternalProjectId(Integer externalProjectId) {
		this.externalProjectId = externalProjectId;
	}
	public String getDeliverableStatus() {
		return deliverableStatus;
	}
	public void setDeliverableStatus(String deliverableStatus) {
		this.deliverableStatus = deliverableStatus;
	}
	public String getExternalWorkplanTemplate() {
		return externalWorkplanTemplate;
	}
	public void setExternalWorkplanTemplate(String externalWorkplanTemplate) {
		this.externalWorkplanTemplate = externalWorkplanTemplate;
	}
	public List<ScopeDetailsModel> getScopeDetails() {
		return scopeDetails;
	}
	public void setScopeDetails(List<ScopeDetailsModel> scopeDetails) {
		this.scopeDetails = scopeDetails;
	}
}
