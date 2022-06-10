package com.ericsson.isf.model.json;

import java.util.List;

public class Rule1 {
	private String combinator;
//	private Activities[] activities;
	private List<Activities> activities;
	
	public String getCombinator() {
		return combinator;
	}
	public void setCombinator(String combinator) {
		this.combinator = combinator;
	}
//	public Activities[] getActivities() {
//		return activities;
//	}
//	public void setActivities(Activities[] activities) {
//		this.activities = activities;
//	}
	public List<Activities> getActivities() {
		return activities;
	}
	public void setActivities(List<Activities> activities) {
		this.activities = activities;
	}
	
	
}
