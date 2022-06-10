package com.ericsson.isf.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;


@JsonAutoDetect

public class ChangeManagement {
	
	private String reason;
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public List<RaiseCRMannagmentModel> getRaiseCRMannagmentModel() {
		return raiseCRMannagmentModel;
	}
	public void setRaiseCRMannagmentModel(List<RaiseCRMannagmentModel> raiseCRMannagmentModel) {
		this.raiseCRMannagmentModel = raiseCRMannagmentModel;
	}
	private List<RaiseCRMannagmentModel> raiseCRMannagmentModel;
	
}
