package com.ericsson.isf.model;

import java.util.List;

public class ResoucePositionFmModel {
	private int workEffortID;
	private String pid_Weid;
	private int resourcePositionId;
	private int resourceRequestID;
	private int projectid;
	private String positionStatus;
	
	private String start_date;
	private String end_date;
	private String workEffortStatus;
	private String signum;
	private int availability;
	private String JobStage;
	private String managerName;
	private String domain_Subdomain;
	private String serviceArea_SubServiceArea;
	private String Technology;
	private String text;
	private String vendor;
	private List<String> vendor_tech;
	private List<String> vendor_techID;
	private String status;
	private int crid;
	private String isPendingCR;
	
	public int getCrid() {
		return crid;
	}
	public void setCrid(int crid) {
		this.crid = crid;
	}
	public String getPositionStatus() {
		return positionStatus;
	}
	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}
	public int getWorkEffortID() {
		return workEffortID;
	}
	public void setWorkEffortID(int workEffortID) {
		this.workEffortID = workEffortID;
	}
	public String getPid_Weid() {
		return pid_Weid;
	}
	public void setPid_Weid(String pid_Weid) {
		this.pid_Weid = pid_Weid;
	}
	public int getResourcePositionId() {
		return resourcePositionId;
	}
	public void setResourcePositionId(int resourcePositionId) {
		this.resourcePositionId = resourcePositionId;
	}
	public int getResourceRequestID() {
		return resourceRequestID;
	}
	public void setResourceRequestID(int resourceRequestID) {
		this.resourceRequestID = resourceRequestID;
	}
	public int getProjectid() {
		return projectid;
	}
	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getWorkEffortStatus() {
		return workEffortStatus;
	}
	public void setWorkEffortStatus(String workEffortStatus) {
		this.workEffortStatus = workEffortStatus;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public int getAvailability() {
		return availability;
	}
	public void setAvailability(int availability) {
		this.availability = availability;
	}
	public String getJobStage() {
		return JobStage;
	}
	public void setJobStage(String jobStage) {
		JobStage = jobStage;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getDomain_Subdomain() {
		return domain_Subdomain;
	}
	public void setDomain_Subdomain(String domain_Subdomain) {
		this.domain_Subdomain = domain_Subdomain;
	}
	public String getServiceArea_SubServiceArea() {
		return serviceArea_SubServiceArea;
	}
	public void setServiceArea_SubServiceArea(String serviceArea_SubServiceArea) {
		this.serviceArea_SubServiceArea = serviceArea_SubServiceArea;
	}
	public String getTechnology() {
		return Technology;
	}
	public void setTechnology(String technology) {
		Technology = technology;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public List<String> getVendor_tech() {
		return vendor_tech;
	}
	public void setVendor_tech(List<String> vendor_tech) {
		this.vendor_tech = vendor_tech;
	}
	public List<String> getVendor_techID() {
		return vendor_techID;
	}
	public void setVendor_techID(List<String> vendor_techID) {
		this.vendor_techID = vendor_techID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsPendingCR() {
		return isPendingCR;
	}
	public void setIsPendingCR(String isPendingCR) {
		this.isPendingCR = isPendingCR;
	}


	
	
	
}
