package com.ericsson.isf.model.json;

import java.util.List;

public class ParentRule {
	private String combinator;
//	private Rule[] rule;
	private List<Rule1> rule;
	
//	public Rule[] getRule() {
//		return rule;
//	}
//	public void setRule(Rule[] rule) {
//		this.rule = rule;
//	}
	public String getCombinator() {
		return combinator;
	}
	public void setCombinator(String combinator) {
		this.combinator = combinator;
	}
	public List<Rule1> getRule() {
		return rule;
	}
	public void setRule(List<Rule1> rule) {
		this.rule = rule;
	}
	
	
}
