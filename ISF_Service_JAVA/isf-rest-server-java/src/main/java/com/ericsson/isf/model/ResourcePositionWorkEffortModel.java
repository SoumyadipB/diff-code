package com.ericsson.isf.model;

import java.util.HashMap;
import java.util.List;

public class ResourcePositionWorkEffortModel {
	
	
	//private int resourcePositionID;
	//private String issuedBy;
	//private float fte_percent;
	//private String duration;
	private int rpefId;
	private String totalHours;
	//private String remote_onsite;
	private String startDate;
	private String endDate;
	
	
	//private String signum;
	//private String employeeName;
	//private String managerSignum;
	//private String managerName;
	private int remoteCount;
	private int onsiteCount;
	private List<HashMap<String,Object>> resourcePositionList;
	
	
	
	public int getRpefId() {
		return rpefId;
	}
	public void setRpefId(int rpefId) {
		this.rpefId = rpefId;
	}
	/*public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}*/
	/*public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getManagerSignum() {
		return managerSignum;
	}
	public void setManagerSignum(String managerSignum) {
		this.managerSignum = managerSignum;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}*/
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/*public String getRemote_onsite() {
		return remote_onsite;
	}
	public void setRemote_onsite(String remote_onsite) {
		this.remote_onsite = remote_onsite;
	}*/
	/*public int getResourcePositionID() {
		return resourcePositionID;
	}
	public void setResourcePositionID(int resourcePositionID) {
		this.resourcePositionID = resourcePositionID;
	}
	*/
	/*
	public String getIssuedBy() {
		return issuedBy;
	}
	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}
	public float getFte_percent() {
		return fte_percent;
	}
	public void setFte_percent(float fte_percent) {
		this.fte_percent = fte_percent;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}*/
	
	public List<HashMap<String, Object>> getResourcePositionList() {
		return resourcePositionList;
	}
	public String getTotalHours() {
		return totalHours;
	}
	public void setTotalHours(String totalHours) {
		this.totalHours = totalHours;
	}
	public void setResourcePositionList(List<HashMap<String, Object>> resourcePositionList) {
		this.resourcePositionList = resourcePositionList;
	}
	
	
}
