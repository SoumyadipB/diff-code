/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

import com.ericsson.isf.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *
 * @author esudbhu
 */
public class ScopeDetailsModel {

	Integer projectScopeDetailID; 
	String scopeName;
	String deliverableUnit;
	String requestType;
	Date startDate;
	Date endDate;
	String domain;
	Integer domainID;
	String subDomain;
	String serviceArea;
	Integer serviceAreaID;
	String subServiceArea;
	String technology;
	Integer technologyID;
	String subActivityName;
	String activityName;
	Integer subActivityId;
	Integer activityId;
	String deliverableStatus;

	
	public String getDeliverableStatus() {
		return deliverableStatus;
	}


	public void setDeliverableStatus(String deliverableStatus) {
		this.deliverableStatus = deliverableStatus;
	}


	public Integer getDomainID() {
		return domainID;
	}


	public void setDomainID(Integer domainID) {
		this.domainID = domainID;
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

	public String getScopeName() {
		return scopeName;
	}

	
	public Integer getProjectScopeDetailID() {
		return projectScopeDetailID;
	}

	
	public void setProjectScopeDetailID(Integer projectScopeDetailID) {
		this.projectScopeDetailID = projectScopeDetailID;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	public String getDeliverableUnit() {
		return deliverableUnit;
	}

	private String deliverableUnitName;

	public String getDeliverableUnitName() {
		return deliverableUnitName;
	}

	public void setDeliverableUnitName(String deliverableUnitName) {
		this.deliverableUnitName = deliverableUnitName;
	}

	public void setDeliverableUnit(String deliverableUnit) {
		this.deliverableUnit = deliverableUnit;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.UI_DATE_FORMAT, timezone = AppConstants.TIMEZONE_IST)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.UI_DATE_FORMAT, timezone = AppConstants.TIMEZONE_IST)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSubDomain() {
		return subDomain;
	}

	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}

	public String getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	public String getSubServiceArea() {
		return subServiceArea;
	}

	public void setSubServiceArea(String subServiceArea) {
		this.subServiceArea = subServiceArea;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getSubActivityName() {
		return subActivityName;
	}

	public void setSubActivityName(String subActivityName) {
		this.subActivityName = subActivityName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Integer getSubActivityId() {
		return subActivityId;
	}

	public void setSubActivityId(Integer subActivityId) {
		this.subActivityId = subActivityId;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}



}
