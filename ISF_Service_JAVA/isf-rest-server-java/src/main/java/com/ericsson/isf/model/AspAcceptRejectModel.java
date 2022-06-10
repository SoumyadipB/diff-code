package com.ericsson.isf.model;

import java.util.Date;

public class AspAcceptRejectModel {
	private String userSignum;
	private String managerSignum;
	private Date startDate;
	private Date endDate;
	private Boolean isProfileActiveFlag;
	private Boolean isActiveFlag;
	private String operation;
	private String rejectMessage;
	
	
	public Boolean getIsProfileActiveFlag() {
		return isProfileActiveFlag;
	}
	public void setIsProfileActiveFlag(Boolean isProfileActiveFlag) {
		this.isProfileActiveFlag = isProfileActiveFlag;
	}
	public Boolean getIsActiveFlag() {
		return isActiveFlag;
	}
	public void setIsActiveFlag(Boolean isActiveFlag) {
		this.isActiveFlag = isActiveFlag;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getRejectMessage() {
		return rejectMessage;
	}
	public void setRejectMessage(String rejectMessage) {
		this.rejectMessage = rejectMessage;
	}
	public String getUserSignum() {
		return userSignum;
	}
	public void setUserSignum(String aspSignum) {
		this.userSignum = aspSignum;
	}
	public String getManagerSignum() {
		return managerSignum;
	}
	public void setManagerSignum(String managerSignum) {
		this.managerSignum = managerSignum;
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
}
