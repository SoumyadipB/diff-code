package com.ericsson.isf.model;

import java.util.Map;

public class EEDashboardCellDataModel {
	private String signum;
	private int backlogCounts;
	private Map<String, EEDashboardDataModel> dates;
	
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public int getBacklogCounts() {
		return backlogCounts;
	}
	public void setBacklogCounts(int backlogCounts) {
		this.backlogCounts = backlogCounts;
	}
	public Map<String, EEDashboardDataModel> getDates() {
		return dates;
	}
	public void setDates(Map<String, EEDashboardDataModel> dates) {
		this.dates = dates;
	}
	
	
}
