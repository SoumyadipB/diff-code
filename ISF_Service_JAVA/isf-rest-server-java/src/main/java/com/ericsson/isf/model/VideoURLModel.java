package com.ericsson.isf.model;

public class VideoURLModel {
	private int rpaRequestID;
	private String videoURL;
	public String getVideoURL() {
		return videoURL;
	}
	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}
	public int getRpaRequestID() {
		return rpaRequestID;
	}
	public void setRpaRequestID(int rpaRequestID) {
		this.rpaRequestID = rpaRequestID;
	}

}
