package com.ericsson.isf.model;

import java.util.Date;

import com.ericsson.isf.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

public class DemandForecastModel {
	
	
	private int projectId;
	private String projectName;
	private String role;
	private Date month;
	private int positionCount;
	private String signum;
	
	
	
	
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public DemandForecastModel() {
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=AppConstants.DEMAND_DATE_FORMAT, timezone = AppConstants.TIMEZONE_IST)
	public Date getMonth() {
		return month;
	}
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=AppConstants.DEMAND_DATE_FORMAT, timezone = AppConstants.TIMEZONE_IST)
	public void setMonth(Date month) {
		this.month = month;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getPositionCount() {
		return positionCount;
	}
	public void setPositionCount(int positionCount) {
		this.positionCount = positionCount;
	}

}
