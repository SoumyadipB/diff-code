package com.ericsson.isf.model.json;

import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Activity {

	private static final long serialVersionUID = -5732828496629097452L;
	
	private String event_module;
	
	private String scanner_combinator;
	
	private String[] valid_apps;
	
	//private Child child;
	
	private Activity child;
	
	private Map<String, Object> conditions;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEvent_module() {
		return event_module;
	}

	public void setEvent_module(String event_module) {
		this.event_module = event_module;
	}

	public String getScanner_combinator() {
		return scanner_combinator;
	}

	public void setScanner_combinator(String scanner_combinator) {
		this.scanner_combinator = scanner_combinator;
	}

	public String[] getValid_apps() {
		return valid_apps;
	}

	public void setValid_apps(String[] valid_apps) {
		this.valid_apps = valid_apps;
	}

	@JsonProperty("child")
	public Activity getChild() {
		return child;
	}

	public void setChild(Activity child) {
		this.child = child;
	}

	@JsonProperty("conditions")
	public Map<String, Object> getConditions() {
		return conditions;
	}

	public void setConditions(Map<String, Object> conditions) {
		this.conditions = conditions;
	}

	@Override
	public String toString() {
		return "Activity [event_module=" + event_module + ", scanner_combinator=" + scanner_combinator + ", valid_apps="
				+ Arrays.toString(valid_apps) + ", child=" + child + ", conditions=" + conditions + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	
	
}
