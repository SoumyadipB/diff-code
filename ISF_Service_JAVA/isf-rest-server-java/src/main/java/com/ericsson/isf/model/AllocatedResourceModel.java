package com.ericsson.isf.model;

import java.util.Date;

public class AllocatedResourceModel {
	
	public int getWeid() {
		return weid;
	}
	public void setWeid(int weid) {
		this.weid = weid;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public String getAllocatedStatus() {
		return allocatedStatus;
	}
	public void setAllocatedStatus(String allocatedStatus) {
		this.allocatedStatus = allocatedStatus;
	}
	public String getLoggedInUser() {
		return loggedInUser;
	}
	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
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

	private int weid;

	@Override
	public String toString() {
		return "AllocatedResourceModel [weid=" + weid + ", signum=" + signum
				+ ", allocatedStatus=" + allocatedStatus + ", loggedInUser="
				+ loggedInUser + ", rpID=" + rpID + ", comments=" + comments
				+ "]";
	}
	private String resourceName;
	private String signum;
    private String allocatedStatus;
    private String loggedInUser;
    private Date startDate;
    private WorkEffortModel weDetails;
    
    private String commentsByFM;
    
    public String getCommentsByFM() {
		return commentsByFM;
	}
	public void setCommentsByFM(String commentsByFM) {
		this.commentsByFM = commentsByFM;
	}
	public WorkEffortModel getWeDetails() {
		return weDetails;
	}
	public void setWeDetails(WorkEffortModel weDetails) {
		this.weDetails = weDetails;
	}
	public Date getPositionStartDate() {
		return positionStartDate;
	}
	public void setPositionStartDate(Date positionStartDate) {
		this.positionStartDate = positionStartDate;
	}
	public Date getPositionEndDate() {
		return positionEndDate;
	}
	public void setPositionEndDate(Date positionEndDate) {
		this.positionEndDate = positionEndDate;
	}

	private Date positionStartDate;
    private Date positionEndDate;
    private Date endDate;
    private int rpID;
    private String comments;
    private String positionStatus;
    private long duration;
    private double hours;
    private double ftePercentage;
    
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public double getFtePercentage() {
		return ftePercentage;
	}
	public void setFtePercentage(double ftePercentage) {
		this.ftePercentage = ftePercentage;
	}
	public double getHours() {
		return hours;
	}
	public void setHours(double hours) {
		this.hours = hours;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public String getPositionStatus() {
		return positionStatus;
	}
	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public int getRpID() {
		return rpID;
	}
	public void setRpID(int rpID) {
		this.rpID = rpID;
	}
    
    
    
}
