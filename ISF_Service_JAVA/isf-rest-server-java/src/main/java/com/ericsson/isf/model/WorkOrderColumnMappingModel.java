package com.ericsson.isf.model;

import java.util.List;

public class WorkOrderColumnMappingModel {
	private int wOID;
	private int projectID;
	private String wOName;
	private List<String> marketArea;
	private String flowchartdefID;
	private String startDate;
	private String signumID;
	private String workFlowName;
	private String customerName;
	private List<String> nodeNames;
	private String doID;
	private String subActivityName;
	
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getwOName() {
		return wOName;
	}
	public void setwOName(String wOName) {
		this.wOName = wOName;
	}
	public List<String> getMarketArea() {
		return marketArea;
	}
	public void setMarketArea(List<String> marketArea) {
		this.marketArea = marketArea;
	}
	public String getFlowchartdefID() {
		return flowchartdefID;
	}
	public void setFlowchartdefID(String flowchartdefID) {
		this.flowchartdefID = flowchartdefID;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getSignumID() {
		return signumID;
	}
	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}
	public String getWorkFlowName() {
		return workFlowName;
	}
	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public List<String> getNodeNames() {
		return nodeNames;
	}
	public void setNodeNames(List<String> nodeNames) {
		this.nodeNames = nodeNames;
	}
	public String getDoID() {
		return doID;
	}
	public void setDoID(String doID) {
		this.doID = doID;
	}
	public String getSubActivityName() {
		return subActivityName;
	}
	public void setSubActivityName(String subActivityName) {
		this.subActivityName = subActivityName;
	}
	public int getwOID() {
		return wOID;
	}
	public void setwOID(int wOID) {
		this.wOID = wOID;
	}
	
}
