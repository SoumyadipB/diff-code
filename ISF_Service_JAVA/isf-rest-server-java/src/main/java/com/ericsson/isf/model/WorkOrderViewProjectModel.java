/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;
import java.util.Map;

/**
 *
 * @author ekumvsu
 */
public class WorkOrderViewProjectModel {

    private String projectID;
    private String projectName;
    private String project_StartDate;
    private String project_EndDate;
    private String productArea;
    private String marketArea;
    private String country;
    private String operator;
    private String projectHours;
    private String projectDays;
    private String projectWOCount;
    private String nodeName;
    private List<WOOutputFileResponseModel> woOutputLink;
    
    public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	private List<WorkOrderViewScopeModel> listOfScope;

    public String getProjectWOCount() {
        return projectWOCount;
    }

    public void setProjectWOCount(String projectWOCount) {
        this.projectWOCount = projectWOCount;
    }

    public String getProjectHours() {
        return projectHours;
    }

    public void setProjectHours(String projectHours) {
        this.projectHours = projectHours;
    }

    public String getProjectDays() {
        return projectDays;
    }

    public void setProjectDays(String projectDays) {
        this.projectDays = projectDays;
    }

    public String getProject_StartDate() {
        return project_StartDate;
    }

    public void setProject_StartDate(String projectStartDate) {
        this.project_StartDate = projectStartDate;
    }

    public String getProject_EndDate() {
        return project_EndDate;
    }

    public void setProject_EndDate(String projectEndDate) {
        this.project_EndDate = projectEndDate;
    }

    public String getProductArea() {
        return productArea;
    }

    public void setProductArea(String productArea) {
        this.productArea = productArea;
    }

    public String getMarketArea() {
        return marketArea;
    }

    public void setMarketArea(String marketArea) {
        this.marketArea = marketArea;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
    
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

    public List<WorkOrderViewScopeModel> getListOfScope() {
        return listOfScope;
    }

    public void setListOfScope(List<WorkOrderViewScopeModel> listOfScope) {
        this.listOfScope = listOfScope;
    }

	public List<WOOutputFileResponseModel> getWoOutputLink() {
		return woOutputLink;
	}

	public void setWoOutputLink(List<WOOutputFileResponseModel> woOutputLink) {
		this.woOutputLink = woOutputLink;
	}

    
}
