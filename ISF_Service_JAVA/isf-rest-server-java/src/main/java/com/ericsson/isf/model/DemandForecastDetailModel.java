/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ericsson.isf.util.IsfCustomDateDeserializerForGantt;

import com.ericsson.isf.util.IsfCustomDateSerializerForGantt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/**
 *
 * @author edhhklu
 */

public class DemandForecastDetailModel {
		private Integer projectScopeId;
		private String resourceType;
		private Integer remoteCount;
		private Integer onsiteCount;
		private String vendor;
		private Integer jobRoleId;
		private Integer jobStageId;
		private Integer positionId;
		private String jobRoleName;
		
		
		private String jobStageName;
		private String projectScopeName;
		private Integer workEffortId;
		private String status;
		
		private Integer projectScopeDetailId;
		
		@JsonSerialize(using = IsfCustomDateSerializerForGantt.class)
	    @JsonDeserialize(using = IsfCustomDateDeserializerForGantt.class)
		private Date startDate;
		@JsonSerialize(using = IsfCustomDateSerializerForGantt.class)
	    @JsonDeserialize(using = IsfCustomDateDeserializerForGantt.class)
		private Date endDate;

		private String serviceArea;
		private String tech;
		
		private String domain_subDomain;
		
		private String spSource	;
		
		private double hours;
		private double fte;
		private double duration;
		
		
		private String country;
	
		private String city;
		private double latitude;
		private double longitude;
		
		private String description;
		public List<VendorTechModel> getVendorTechModel() {
			return vendorTechModel;
		}
		public void setVendorTechModel(List<VendorTechModel> vendorTechModel) {
			this.vendorTechModel = vendorTechModel;
		}
		private String timeZone;
		private String demandType;
		
		
		
		public String getDemandType() {
			return demandType;
		}
		public void setDemandType(String demandType) {
			this.demandType = demandType;
		}
		public Integer getDemandTypeID() {
			return demandTypeID;
		}
		public void setDemandTypeID(Integer demandTypeID) {
			this.demandTypeID = demandTypeID;
		}
		private Integer serviceAreaID;
		private Integer technologyID;
		private Integer domainID;
		private Integer demandTypeID;
		
		
		private List<Integer> toolList;
		private List<Map<String,Object>> returnTools;
		
		 private List<VendorTechModel> vendorTechModel;
		
		public List<Map<String, Object>> getReturnTools() {
			return returnTools;
		}
		public void setReturnTools(List<Map<String, Object>> returnTools) {
			this.returnTools = returnTools;
		}
		private Date scopeStartDate;
		
		public List<Integer> getToolList() {
			return toolList;
		}
		public void setToolList(List<Integer> toolList) {
			this.toolList = toolList;
		}
		public Integer getProjectScopeDetailId() {
			return projectScopeDetailId;
		}
		public void setProjectScopeDetailId(Integer projectScopeDetailId) {
			this.projectScopeDetailId = projectScopeDetailId;
		}
		public Integer getServiceAreaID() {
			return serviceAreaID;
		}
		public void setServiceAreaID(Integer serviceAreaID) {
			this.serviceAreaID = serviceAreaID;
		}
		public Integer getTechnologyID() {
			return technologyID;
		}
		public void setTechnologyID(Integer technologyID) {
			this.technologyID = technologyID;
		}
		public Integer getDomainID() {
			return domainID;
		}
		public void setDomainID(Integer domainID) {
			this.domainID = domainID;
		}
		public double getDuration() {
			return duration;
		}
		
		public void setDuration(double duration) {
			this.duration = duration;
		}
		
		public String getTimeZone() {
			return timeZone;
		}
		public void setTimeZone(String timeZone) {
			this.timeZone = timeZone;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public String getSpSource() {
			return spSource;
		}
		public void setSpSource(String spSource) {
			this.spSource = spSource;
		}
		public double getHours() {
			return hours;
		}
		public void setHours(double hours) {
			this.hours = hours;
		}
		public String getServiceArea() {
			return serviceArea;
		}
		public void setServiceArea(String serviceArea) {
			this.serviceArea = serviceArea;
		}
		public double getFte() {
			return fte;
		}
		public void setFte(double fte) {
			this.fte = fte;
		}
		
		
		public String getTech() {
			return tech;
		}
		public void setTech(String tech) {
			this.tech = tech;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		
		public String getJobRoleName() {
			return jobRoleName;
		}
		public String getDomain_subDomain() {
			return domain_subDomain;
		}
		public void setDomain_subDomain(String domain_subDomain) {
			this.domain_subDomain = domain_subDomain;
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
		
		
		public Integer getProjectScopeId() {
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
		public Integer getRemoteCount() {
			return remoteCount;
		}
		public void setRemoteCount(int remoteCount) {
			this.remoteCount = remoteCount;
		}
//		@JsonProperty("onsite /local")
		public Integer getOnsiteCount() {
			return onsiteCount;
		}
		public void setOnsiteCount(int onsiteCount) {
			this.onsiteCount = onsiteCount;
		}
		public String getVendor() {
			return vendor;
		}
		public void setVendor(String vendor) {
			this.vendor = vendor;
		}
		public Integer getJobRoleId() {
			return jobRoleId;
		}
		public void setJobRoleId(int jobRoleId) {
			this.jobRoleId = jobRoleId;
		}
		public Integer getJobStageId() {
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
		public Integer getWorkEffortId() {
			return workEffortId;
		}
		public void setWorkEffortId(Integer workEffortId) {
			this.workEffortId = workEffortId;
		}
		public Date getScopeStartDate() {
			return scopeStartDate;
		}
		public void setScopeStartDate(Date scopeStartDate) {
			this.scopeStartDate = scopeStartDate;
		}
		public void setProjectScopeId(Integer projectScopeId) {
			this.projectScopeId = projectScopeId;
		}
		public void setRemoteCount(Integer remoteCount) {
			this.remoteCount = remoteCount;
		}
		public void setOnsiteCount(Integer onsiteCount) {
			this.onsiteCount = onsiteCount;
		}
		public void setJobRoleId(Integer jobRoleId) {
			this.jobRoleId = jobRoleId;
		}
		public void setJobStageId(Integer jobStageId) {
			this.jobStageId = jobStageId;
		}
		
		
}
