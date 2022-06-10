package com.ericsson.isf.model.json;

import java.util.List;

public class Rule {
	
	private String combinator;
	
	private List<Activity> activities;
	
	public String getCombinator() {
		return combinator;
	}

	public void setCombinator(String combinator) {
		this.combinator = combinator;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	@Override
	public String toString() {
		return "RuleModel [combinator=" + combinator + ", activities=" + activities + "]";
	}
	
	
	
	
}
