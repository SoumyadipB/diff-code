package com.ericsson.isf.model;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class FeedbackStatusUpdateModel {

	private int feedbackActivityID;
	private int feedbackDetailID;
	private String statusUpdateComment;
	private String feedbackStatus;
	private String feedbackStatusNew;
	private String neComments;
	private String pmComments;
	
	public String getPmComments() {
		return pmComments;
	}
	public void setPmComments(String pmComments) {
		this.pmComments = pmComments;
	}
	public String getNeComments() {
		return neComments;
	}
	public void setNeComments(String neComments) {
		this.neComments = neComments;
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
	public String getStatusUpdateComment() {
		return statusUpdateComment;
	}
	public void setStatusUpdateComment(String statusUpdateComment) {
		this.statusUpdateComment = statusUpdateComment.trim();
		
	}
	public String getFeedbackStatus() {
		return feedbackStatus;
	}
	public void setFeedbackStatus(String feedbackStatus) {
		this.feedbackStatus = feedbackStatus;
	}
	public String getFeedbackStatusNew() {
		return feedbackStatusNew;
	}
	public void setFeedbackStatusNew(String feedbackStatusNew) {
		this.feedbackStatusNew = feedbackStatusNew;
	}
	
}
