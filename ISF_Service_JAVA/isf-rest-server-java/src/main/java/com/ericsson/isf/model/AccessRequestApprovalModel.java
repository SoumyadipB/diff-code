package com.ericsson.isf.model;

import java.util.Date;

import com.ericsson.isf.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

public class AccessRequestApprovalModel {
	private String signumId;
	private String approvalStatus;
	private String approvedBy;
	private String accessProfileId;
	private String accessProfileName;
	private Integer userAccessProfileId;
	private String organisation;
	private Date startDate;
	private Date endDate;
	private boolean active;
	private String employeeName;
	private String remark;
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getAccessProfileName() {
		return accessProfileName;
	}
	public void setAccessProfileName(String accessProfileName) {
		this.accessProfileName = accessProfileName;
	}
	public Integer getUserAccessProfileId() {
		return userAccessProfileId;
	}
	public void setUserAccessProfileId(Integer userAccessProfileId) {
		this.userAccessProfileId = userAccessProfileId;
	}
	public String getOrganisation() {
		return organisation;
	}
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getSignumId() {
		return signumId;
	}
	public void setSignumId(String signumId) {
		this.signumId = signumId;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public String getAccessProfileId() {
		return accessProfileId;
	}
	public void setAccessProfileId(String accessProfileId) {
		this.accessProfileId = accessProfileId;
	}
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=AppConstants.DEFAULT_DATE_FORMAT,timezone =AppConstants.TIMEZONE_IST)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDtae(Date startDtae) {
		this.startDate = startDtae;
	}
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=AppConstants.DEFAULT_DATE_FORMAT,timezone =AppConstants.TIMEZONE_IST)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
