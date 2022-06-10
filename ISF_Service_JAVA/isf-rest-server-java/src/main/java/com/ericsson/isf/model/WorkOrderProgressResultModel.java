package com.ericsson.isf.model;

public class WorkOrderProgressResultModel {
	private String completedPercentage;
    private String progressDescription;
	public String getProgressDescription() {
		return progressDescription;
	}
	public void setProgressDescription(String progressDescription) {
		this.progressDescription = progressDescription;
	}
	public String getCompletedPercentage() {
		return completedPercentage;
	}
	public void setCompletedPercentage(String completedPercentage) {
		this.completedPercentage = completedPercentage;
	}

}
