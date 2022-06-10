package com.ericsson.isf.model;

public class UserFeedbackModel {
	
	private int feedbackID;
	private String title;
	private String textArea;
	private boolean rating;
	private String createdOn;
	private String submittedBy;
	private String url;
	private String uploadFile;
	private String fileName;
	private String fileType;
	private byte[] dataFile;

	public int getFeedbackID() {
		return feedbackID;
	}
	
	public void setFeedbackID(int feedbackID) {
		this.feedbackID = feedbackID;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTextArea() {
		return textArea;
	}

	public void setTextArea(String textArea) {
		this.textArea = textArea;
	}

	public boolean isRating() {
		return rating;
	}

	public void setRating(boolean rating) {
		this.rating = rating;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getDataFile() {
		return dataFile;
	}
	
	public void setDataFile(byte[] dataFile) {
		this.dataFile = dataFile;
	}
}
