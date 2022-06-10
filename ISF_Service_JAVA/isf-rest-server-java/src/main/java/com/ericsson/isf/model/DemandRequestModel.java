/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

import com.ericsson.isf.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *
 * @author edhhklu
 */
public class DemandRequestModel {
		
		private int resourceRequestId; 
		private String resourceType;
		private String resourceDescription;
		private String createdBy;
		private int projScopeDetailId;
		private int projectId;
		private int jobRoleId;
		private int jobStageId;
		private Integer locationId;
		private int remoteCount;
		private int onsiteCount;
		@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM-dd-yyyy",timezone =AppConstants.TIMEZONE_IST)
		private Date startDate;
		@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM-dd-yyyy",timezone =AppConstants.TIMEZONE_IST)
		private Date endDate;
		private int duration;
		private float ftePercent;
		private int hours;
		private String remoteLocation;
		private String onsiteLocation;
		private String resourceCity;
		private String resourceCountry;
		private double resourceLat;
		private double resourceLng;
		private String resourceTimeZone;
		private String jobStageName;
		private String[] vendor;
		private String jobRoleName;
		private int resourceRequestWorkEffortID;
		
		public List<VendorTechModel> getVendorTechModel() {
			return vendorTechModel;
		}
		public void setVendorTechModel(List<VendorTechModel> vendorTechModel) {
			this.vendorTechModel = vendorTechModel;
		}
		private List<VendorTechModel> vendorTechModel;
		public String getResourceType() {
			return resourceType;
		}
		public void setResourceType(String resourceType) {
			this.resourceType = resourceType;
		}
		public String getResourceDescription() {
			return resourceDescription;
		}
		public void setResourceDescription(String resourceDescription) {
			this.resourceDescription = resourceDescription;
		}
		public String getCreatedBy() {
			return createdBy;
		}
		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}
		public int getProjScopeDetailId() {
			return projScopeDetailId;
		}
		public void setProjScopeDetailId(int projScopeDetailId) {
			this.projScopeDetailId = projScopeDetailId;
		}
		public int getProjectId() {
			return projectId;
		}
		public void setProjectId(int projectId) {
			this.projectId = projectId;
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

		
		
		public Integer getLocationId() {
			return locationId;
		}
		public void setLocationId(Integer locationId) {
			this.locationId = locationId;
		}
		public int getRemoteCount() {
			return remoteCount;
		}
		public void setRemoteCount(int remoteCount) {
			this.remoteCount = remoteCount;
		}
		public int getOnsiteCount() {
			return onsiteCount;
		}
		public void setOnsiteCount(int onsiteCount) {
			this.onsiteCount = onsiteCount;
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
		public int getDuration() {
			return duration;
		}
		public void setDuration(int duration) {
			this.duration = duration;
		}
		public float getFtePercent() {
			return ftePercent;
		}
		public void setFtePercent(float ftePercent) {
			this.ftePercent = ftePercent;
		}
		public int getHours() {
			return hours;
		}
		public void setHours(int hours) {
			this.hours = hours;
		}
		public String getRemoteLocation() {
			return remoteLocation;
		}
		public void setRemoteLocation(String remoteLocation) {
			this.remoteLocation = remoteLocation;
		}
		public String getOnsiteLocation() {
			return onsiteLocation;
		}
		public void setOnsiteLocation(String onsiteLocation) {
			this.onsiteLocation = onsiteLocation;
		}
		public String getResourceCity() {
			return resourceCity;
		}
		public void setResourceCity(String resourceCity) {
			this.resourceCity = resourceCity;
		}
		public String getResourceCountry() {
			return resourceCountry;
		}
		public void setResourceCountry(String resourceCountry) {
			this.resourceCountry = resourceCountry;
		}
		public double getResourceLat() {
			return resourceLat;
		}
		public void setResourceLat(double resourceLat) {
			this.resourceLat = resourceLat;
		}
		public double getResourceLng() {
			return resourceLng;
		}
		public void setResourceLng(double resourceLng) {
			this.resourceLng = resourceLng;
		}
		public String getResourceTimeZone() {
			return resourceTimeZone;
		}
		public void setResourceTimeZone(String resourceTimeZone) {
			this.resourceTimeZone = resourceTimeZone;
		}
		public String[] getVendor() {
			return vendor;
		}
		public void setVendor(String[] vendor) {
			this.vendor = vendor;
		}
		public int getResourceRequestId() {
			return resourceRequestId;
		}
		public void setResourceRequestId(int resourceRequestId) {
			this.resourceRequestId = resourceRequestId;
		}
		public String getJobStageName() {
			return jobStageName;
		}
		public void setJobStageName(String jobStageName) {
			this.jobStageName = jobStageName;
		}
		public String getJobRoleName() {
			return jobRoleName;
		}
		public void setJobRoleName(String jobRoleName) {
			this.jobRoleName = jobRoleName;
		}
		public int getResourceRequestWorkEffortID() {
			return resourceRequestWorkEffortID;
		}
		public void setResourceRequestWorkEffortID(int resourceRequestWorkEffortID) {
			this.resourceRequestWorkEffortID = resourceRequestWorkEffortID;
		}
}
