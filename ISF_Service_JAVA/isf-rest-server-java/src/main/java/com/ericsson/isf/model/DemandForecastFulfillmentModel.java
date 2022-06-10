/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

import com.ericsson.isf.util.IsfCustomDateDeserializer;
import com.ericsson.isf.util.IsfCustomDateSerializerForGantt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/**
 *
 * @author edhhklu
 */
public class DemandForecastFulfillmentModel {
		private int projectScopeId;
		private String resourceType;
		private String remote_onsite;
		private String vendor;
		private int jobRoleId;
		private int jobStageId;
		private Integer positionId;
		private String jobRoleName;
		
		
		private String jobStageName;
		private String projectScopeName;
		private Integer workEffortId;
		private String positionStatus;
		private String text;
		
		public List<VendorTechModel> getLstVendorTechModel() {
			return lstVendorTechModel;
		}
		public void setLstVendorTechModel(List<VendorTechModel> lstVendorTechModel) {
			this.lstVendorTechModel = lstVendorTechModel;
		}

		private String domain;	
		private String subdomain;
		private String tech;	
		private String location;
		private String domainSubDomain;	
		private String jStageJRole;
		private String sAreaSubSArea;
		private String projectScopeDetailId;
		private Date scopeStartDate;
		private Date  scopeEndDate;
		private String requesttype;
		private String deliverableUnit;
		private String demandType;
		private List<VendorTechModel> lstVendorTechModel;
		
		
		public String getDemandType() {
			return demandType;
		}
		public void setDemandType(String demandType) {
			this.demandType = demandType;
		}
		public Date getScopeStartDate() {
			return scopeStartDate;
		}
		public void setScopeStartDate(Date scopeStartDate) {
			this.scopeStartDate = scopeStartDate;
		}
		public Date getScopeEndDate() {
			return scopeEndDate;
		}
		public void setScopeEndDate(Date scopeEndDate) {
			this.scopeEndDate = scopeEndDate;
		}
		public String getRequesttype() {
			return requesttype;
		}
		public void setRequesttype(String requesttype) {
			this.requesttype = requesttype;
		}
		public String getDeliverableUnit() {
			return deliverableUnit;
		}
		public void setDeliverableUnit(String deliverableUnit) {
			this.deliverableUnit = deliverableUnit;
		}

		private static final String LOCAL ="Local";
		   
		public String getProjectScopeDetailId() {
			return projectScopeDetailId;
		}
		public void setProjectScopeDetailId(String projectScopeDetailId) {
			this.projectScopeDetailId = projectScopeDetailId;
		}
		public String getsAreaSubSArea() {
			return sAreaSubSArea;
		}
		public void setsAreaSubSArea(String sAreaSubSArea) {
			this.sAreaSubSArea = sAreaSubSArea;
		}
		public String getDomainSubDomain() {
			return domainSubDomain;
		}
		public void setDomainSubDomain(String domainSubDomain) {
			this.domainSubDomain = domainSubDomain;
		}
		public String getjStageJRole() {
			return jStageJRole;
		}
		public void setjStageJRole(String jStageJRole) {
			this.jStageJRole = jStageJRole;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getDomain() {
			return domain;
		}
		public void setDomain(String domain) {
			this.domain = domain;
		}
		public String getSubdomain() {
			return subdomain;
		}
		public void setSubdomain(String subdomain) {
			this.subdomain = subdomain;
		}
		public String getTech() {
			return tech;
		}
		public void setTech(String tech) {
			this.tech = tech;
		}
		public String getSubServiceArea() {
			return subServiceArea;
		}
		public void setSubServiceArea(String subServiceArea) {
			this.subServiceArea = subServiceArea;
		}
		public String getFte() {
			return fte;
		}
		public void setFte(String fte) {
			this.fte = fte;
		}
		public String getSignum() {
			return signum;
		}
		public void setSignum(String signum) {
			this.signum = signum;
		}
		
		private String serviceArea;
		
		public String getServiceArea() {
			return serviceArea;
		}
		public void setServiceArea(String serviceArea) {
			this.serviceArea = serviceArea;
		}

		private String subServiceArea;
		private String fte;
		public String getPositionId_WorkEffortID() {
			return PositionId_WorkEffortID;
		}
		public void setPositionId_WorkEffortID(String positionId_WorkEffortID) {
			PositionId_WorkEffortID = positionId_WorkEffortID;
		}

		private String signum;
		private String PositionId_WorkEffortID;
		
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		@JsonSerialize(using = IsfCustomDateSerializerForGantt.class)
	    @JsonDeserialize(using = IsfCustomDateDeserializer.class)
		private Date startDate;
		@JsonSerialize(using = IsfCustomDateSerializerForGantt.class)
	    @JsonDeserialize(using = IsfCustomDateDeserializer.class)
		private Date endDate;
		
		public String getJobRoleName() {
			return jobRoleName;
		}
		public void setJobRoleName(String jobRoleName) {
			this.jobRoleName = jobRoleName;
		}
		
		public String getJobStageName() {
			return jobStageName;
		}
		public void setJobStageName(String jobStageName) {
			this.jobStageName = jobStageName;
		}
		public String getProjectScopeName() {
			return projectScopeName;
		}
		public void setProjectScopeName(String projectScopeName) {
			this.projectScopeName = projectScopeName;
		}
		
		
		public int getProjectScopeId() {
			return projectScopeId;
		}
		public void setProjectScopeId(int projectScopeId) {
			this.projectScopeId = projectScopeId;
		}
		public String getResourceType() {
			return resourceType;
		}
		public void setResourceType(String resourceType) {
			this.resourceType = resourceType;
		}
		
		
		public String getVendor() {
			return vendor;
		}
		public void setVendor(String vendor) {
			this.vendor = vendor;
		}
		public int getJobRoleId() {
			return jobRoleId;
		}
		public void setJobRoleId(int jobRoleId) {
			this.jobRoleId = jobRoleId;
		}
		public int getJobStageId() {
			return jobStageId;
		}
		public void setJobStageId(int jobStageId) {
			this.jobStageId = jobStageId;
		}
		@JsonProperty("start_date")
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
		
		@JsonProperty("end_date")
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
		public Integer getPositionId() {
			return positionId;
		}
		public void setPositionId(Integer positionId) {
			this.positionId = positionId;
		}
//		@JsonProperty("PositionId_WorkEffortID")
		public Integer getWorkEffortId() {
			return workEffortId;
		}
		public void setWorkEffortId(Integer workEffortId) {
			this.workEffortId = workEffortId;
		}
		public String getRemote_onsite() {
			if(remote_onsite.equalsIgnoreCase("onsite")) {
				remote_onsite=remote_onsite+"/"+LOCAL;
			}
			return remote_onsite;
		}
		public void setRemote_onsite(String remote_onsite) {
			this.remote_onsite = remote_onsite;
		}
		public String getPositionStatus() {
			return positionStatus;
		}
		public void setPositionStatus(String positionStatus) {
			this.positionStatus = positionStatus;
		}
		
		
}
