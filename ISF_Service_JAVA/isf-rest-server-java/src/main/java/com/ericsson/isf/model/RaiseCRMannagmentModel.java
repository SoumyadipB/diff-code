package com.ericsson.isf.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;


@JsonAutoDetect

public class RaiseCRMannagmentModel {

	public int getRpID() {
		return rpID;
	}
	public void setRpID(int rpID) {
		this.rpID = rpID;
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
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getLoggedInSignum() {
		return loggedInSignum;
	}
	public void setLoggedInSignum(String loggedInSignum) {
		this.loggedInSignum = loggedInSignum;
	}

	@Override
	public String toString() {
		return "RaiseCRMannagmentModel [comments=" + comments
				+ ", loggedInSignum=" + loggedInSignum + ", rpID=" + rpID
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", actionType=" + actionType + ", signum=" + signum + "]";
	}

	private String comments;
	private String loggedInSignum;
	private int rpID ;
	private String startDate;
	private String endDate;
	private String actionType;
	private String signum;
	private Integer weid;
	private WorkEffortModel weDetails;
	private String allocatedSignum;

	public String getAllocatedSignum() {
		return allocatedSignum;
	}
	public void setAllocatedSignum(String allocatedSignum) {
		this.allocatedSignum = allocatedSignum;
	}


	public WorkEffortModel getWeDetails() {
		return weDetails;
	}
	public void setWeDetails(WorkEffortModel weDetails) {
		this.weDetails = weDetails;
	}
	public Integer getWeid() {
		return weid;
	}
	public void setWeid(Integer weid) {
		this.weid = weid;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}


}
