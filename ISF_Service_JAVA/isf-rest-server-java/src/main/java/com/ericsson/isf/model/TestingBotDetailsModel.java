package com.ericsson.isf.model;

import java.util.Date;

public class TestingBotDetailsModel {
	private String woName;
	private String currentDate;
	private String endDate;
	private String currentTime;
	private String market;
	private String node;
	private String projectName;
	private String wfName;
	private String rpaRequestId;
	private String customerName;
	private String projectId;
	
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getWfName() {
		return wfName;
	}
	public void setWfName(String wfName) {
		this.wfName = wfName;
	}
	public String getRpaRequestId() {
		return rpaRequestId;
	}
	public void setRpaRequestId(String rpaRequestId) {
		this.rpaRequestId = rpaRequestId;
	}
	public String getWoName() {
		return woName;
	}
	public void setWoName(String woName) {
		this.woName = woName;
	}
	public String getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getCurrentTime() {
		return currentTime;
	}
	public void setStartTime(String currentTime) {
		this.currentTime = currentTime;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
