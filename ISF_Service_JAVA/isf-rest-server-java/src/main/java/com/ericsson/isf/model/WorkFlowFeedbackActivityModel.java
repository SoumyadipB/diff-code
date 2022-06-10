package com.ericsson.isf.model;

public class WorkFlowFeedbackActivityModel {
	
	private int feedbackActivityID;
	private int feedbackDetailID;
	private String feedbackComment;
	private String replyOnComment;
	private String feedbackStatus;
	private int  sadCount;
	private String createdOn;
	private String userRole;
	private boolean isActive;
	private String signum;
	private String modifiedOn;
	private String modifiedBy;
	private String createdBy;
	private String stepID;
	private String stepName;
	private String creatorName;
	
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	private String instantFeedbackText;

	
	public String getInstantFeedbackText() {
		return instantFeedbackText;
	}
	public void setInstantFeedbackText(String instantFeedback) {
		this.instantFeedbackText = instantFeedback;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public int getFeedbackActivityID() {
		return feedbackActivityID;
	}
	public void setFeedbackActivityID(int feedbackActivityID) {
		this.feedbackActivityID = feedbackActivityID;
	}
	public int getFeedbackDetailID() {
		return feedbackDetailID;
	}
	public void setFeedbackDetailID(int feedbackDetailID) {
		this.feedbackDetailID = feedbackDetailID;
	}
	public String getFeedbackComment() {
		return feedbackComment;
	}
	public void setFeedbackComment(String feedbackComment) {
		this.feedbackComment = feedbackComment;
	}
	public String getReplyOnComment() {
		return replyOnComment;
	}
	public void setReplyOnComment(String replyOnComment) {
		this.replyOnComment = replyOnComment;
	}
	public String getFeedbackStatus() {
		return feedbackStatus;
	}
	public void setFeedbackStatus(String feedbackStatus) {
		this.feedbackStatus = feedbackStatus;
	}
	public int getSadCount() {
		return sadCount;
	}
	public void setSadCount(int sadCount) {
		this.sadCount = sadCount;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getStepID() {
		return stepID;
	}
	public void setStepID(String stepID) {
		this.stepID = stepID;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	
	
}
