package com.ericsson.isf.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuditDataResponseModel {
	@JsonProperty("ErrorFlag")
	private boolean errorFlag;
	@JsonProperty("Error")
	private String error;
	private List<AuditDataModel> commentsData;
	private String toEmailDefaults;
	private String ccEmailDefaults;
	private Map<String,String> userProfileData;//Change to map

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<AuditDataModel> getCommentsData() {
		return commentsData;
	}

	public void setCommentsData(List<AuditDataModel> commentsData) {
		this.commentsData = commentsData;
	}

	public String getToEmailDefaults() {
		return toEmailDefaults;
	}

	public void setToEmailDefaults(String toEmailDefaults) {
		this.toEmailDefaults = toEmailDefaults;
	}

	public String getCcEmailDefaults() {
		return ccEmailDefaults;
	}

	public void setCcEmailDefaults(String ccEmailDefaults) {
		this.ccEmailDefaults = ccEmailDefaults;
	}

	public boolean isErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(boolean errorFlag) {
		this.errorFlag = errorFlag;
	}

	public Map<String,String> getUserProfileData() {
		return userProfileData;
	}

	public void setUserProfileData(Map<String, String> userImageData) {
		this.userProfileData = userImageData;
	}


}
