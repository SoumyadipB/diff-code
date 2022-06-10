package com.ericsson.isf.model;

/**
 * @author ekarmuj 
 * Purpose: This model used for Instant feedback.
 */

public class InstantFeedbackModel {
	private int instantFeedbackID;
	private String feedbackText;
	private boolean isActive;
	private String feedbackType;
	private String createdBy;

	public int getInstantFeedbackID() {
		return instantFeedbackID;
	}

	public void setInstantFeedbackID(int instantFeedbackID) {
		this.instantFeedbackID = instantFeedbackID;
	}

	public String getFeedbackText() {
		return feedbackText;
	}

	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}


	public String getFeedbackType() {
		return feedbackType;
	}

	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	 public boolean getIsActive() {
	        return isActive;
	    }

	    public void setActive(boolean isActive) {
	        this.isActive = isActive;
	    }
	

}
