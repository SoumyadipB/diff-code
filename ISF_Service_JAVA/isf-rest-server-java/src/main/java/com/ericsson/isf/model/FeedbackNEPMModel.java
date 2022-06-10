package com.ericsson.isf.model;

import java.util.List;

public class FeedbackNEPMModel {
	private String startDate;
	private String endDate; 
	private String feedbackStatus;
	private int projectID;
	private List<Integer> listOfWorkFlowId;
	private String createdDate;
	private String stepName;
	private String feedbackOn;
	private String feedbackType;
	private int sadCount;
	private String instantFeedback;
	private String feedbackComment;
	private String replyOnComment;
	private String userRole;
	private String workFlowName;
	private int totalCount;
	private int feedbackDetailID;
	private int feedbackActivityID;
	private String creatorSignum;
	private String createdBy;
	private String feedbackText;
	private String neComments;
	public String getNeComments() {
		return neComments;
	}
	public void setNeComments(String neComments) {
		this.neComments = neComments;
	}
	private List<Integer> listOfFlowchartdefId;
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public int getFeedbackActivityID() {
		return feedbackActivityID;
	}
	public void setFeedbackActivityID(int feedbackActivityID) {
		this.feedbackActivityID = feedbackActivityID;
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
	public String getFeedbackStatus() {
		return feedbackStatus;
	}
	public void setFeedbackStatus(String feedbackStatus) {
		this.feedbackStatus = feedbackStatus;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getFeedbackOn() {
		return feedbackOn;
	}
	public void setFeedbackOn(String feedbackOn) {
		this.feedbackOn = feedbackOn;
	}
	public String getFeedbackType() {
		return feedbackType;
	}
	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}
	public String getInstantFeedback() {
		return instantFeedback;
	}
	public void setInstantFeedback(String instantFeedback) {
		this.instantFeedback = instantFeedback;
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
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getWorkFlowName() {
		return workFlowName;
	}
	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<Integer> getListOfWorkFlowId() {
		return listOfWorkFlowId;
	}
	public void setListOfWorkFlowId(List<Integer> listOfWorkFlowId) {
		this.listOfWorkFlowId = listOfWorkFlowId;
	}
	public int getFeedbackDetailID() {
		return feedbackDetailID;
	}
	public void setFeedbackDetailID(int feedbackDetailID) {
		this.feedbackDetailID = feedbackDetailID;
	}
	public int getSadCount() {
		return sadCount;
	}
	public void setSadCount(int sadCount) {
		this.sadCount = sadCount;
	}
	public String getCreatorSignum() {
		return creatorSignum;
	}
	public void setCreatorSignum(String creatorSignum) {
		this.creatorSignum = creatorSignum;
	}
	public String getFeedbackText() {
		return feedbackText;
	}
	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}
	public List<Integer> getListOfFlowchartdefId() {
		return listOfFlowchartdefId;
	}
	public void setListOfFlowchartdefId(List<Integer> listOfFlowchartdefId) {
		this.listOfFlowchartdefId = listOfFlowchartdefId;
	}
	
	
}
