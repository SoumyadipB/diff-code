package com.ericsson.isf.model.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParentRuleModel {
	
	private String combinator;

	
	@JsonProperty("rule")
	private List<Rule> rule;
	
	public ParentRuleModel(){
		this.rule = new ArrayList<Rule>();
	}
	
	public List<Rule> getRule() {
		return rule;
	}
	public void setRule(List<Rule> ruleModel) {
		this.rule = ruleModel;
	}
	public String getCombinator() {
		return combinator;
	}
	public void setCombinator(String combinator) {
		this.combinator = combinator;
	}

	
	@Override
	public String toString() {
		return "ParentRuleModel [combinator=" + combinator + ", rule=" + rule + "]";
	}
	
	
}
