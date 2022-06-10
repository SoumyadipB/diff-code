/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.ericsson.isf.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author eabhmoj
 */
//@JsonInclude(Include.NON_NULL)
public class ProjectScopeModel {
    
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
	private String parentWorkplanTemplateName;
	
	private List<ScopeDomainProject> domainTechList;
	
	private String scopeType;
	private int scopeTypeId;
	private String method;
	private int methodID;
	private int operatorCountId;
	private int projectFinancialsID;
	private String remarks;
	private String projectFinancials;
	private String deliverable;
	private String vendor;
	private int deliverableUnitID;
	private int totalCounts;
	
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getProjectFinancialsID() {
		return projectFinancialsID;
	}

	public void setProjectFinancialsID(int projectFinancialsID) {
		this.projectFinancialsID = projectFinancialsID;
	}

	public int getOperatorCountId() {
		return operatorCountId;
	}

	public void setOperatorCountId(int operatorCountId) {
		this.operatorCountId = operatorCountId;
	}

	public int getScopeTypeId() {
		return scopeTypeId;
	}

	public void setScopeTypeId(int scopeTypeId) {
		this.scopeTypeId = scopeTypeId;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getMethodID() {
		return methodID;
	}

	public void setMethodID(int methodID) {
		this.methodID = methodID;
	}
	
	public String getScopeType() {
		return scopeType;
	}

	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
	}

	public String getParentWorkplanTemplateName() {
		return parentWorkplanTemplateName;
	}

	public void setParentWorkplanTemplateName(String parentWorkplanTemplateName) {
		this.parentWorkplanTemplateName = parentWorkplanTemplateName;
	}

	
	
	public List<ScopeDomainProject> getDomainTechList() {
		return domainTechList;
	}

	public void setDomainTechList(List<ScopeDomainProject> domainTechList) {
		this.domainTechList = domainTechList;
	}

	public Integer getExternalProjectId() {
		return externalProjectId;
	}

	public void setExternalProjectId(Integer externalProjectId) {
		this.externalProjectId = externalProjectId;
	}

	public Integer getDeliverablePlanId() {
		return deliverablePlanId;
	}

	public void setDeliverablePlanId(Integer deliverablePlanId) {
		this.deliverablePlanId = deliverablePlanId;
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

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public boolean isWorkFlowCreated() {
		return isWorkFlowCreated;
	}

	public void setWorkFlowCreated(boolean isWorkFlowCreated) {
		this.isWorkFlowCreated = isWorkFlowCreated;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorCode() {
    	return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
    	this.vendorCode = vendorCode;
    }

	public String getDeliverableUnit() {
		return deliverableUnit;
	}

	public void setDeliverableUnit(String deliverableUnit) {
		this.deliverableUnit = deliverableUnit;
	}

	public String getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

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
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=AppConstants.DEFAULT_DATE_FORMAT)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=AppConstants.DEFAULT_DATE_FORMAT)
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

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
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

	public int getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(int totalCounts) {
		this.totalCounts = totalCounts;
	}

	public String getProjectFinancials() {
		return projectFinancials;
	}

	public void setProjectFinancials(String projectFinancials) {
		this.projectFinancials = projectFinancials;
	}

	public String getDeliverable() {
		return deliverable;
	}

	public void setDeliverable(String deliverable) {
		this.deliverable = deliverable;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public int getDeliverableUnitID() {
		return deliverableUnitID;
	}

	public void setDeliverableUnitID(int deliverableUnitID) {
		this.deliverableUnitID = deliverableUnitID;
	}



}
