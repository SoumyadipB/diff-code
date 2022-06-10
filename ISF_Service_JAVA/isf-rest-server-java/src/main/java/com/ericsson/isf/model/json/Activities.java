package com.ericsson.isf.model.json;

import java.io.Serializable;
import java.util.Map;

import com.ericsson.isf.model.json.cascade.Child;


public class Activities implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5732828496629097452L;
	private String event_module;
	private String scanner_combinator;
	private String[] valid_apps;
	private String trigger_on_close;
	//private String conditions;
	private String look_back="";
	private String look_back_time="";
	private Observer observer;
	private Map<String, Object> conditions;
//	private Child child;
	
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
	public String getLook_back() {
		return look_back;
	}
	public void setLook_back(String look_back) {
		this.look_back = look_back;
	}
	public String getLook_back_time() {
		return look_back_time;
	}
	public void setLook_back_time(String look_back_time) {
		this.look_back_time = look_back_time;
	}
	public Observer getObserver() {
		return observer;
	}
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	public Map<String, Object> getConditions() {
		return conditions;
	}
	public void setConditions(Map<String, Object> map) {
		this.conditions = map;
	}
	public String getTrigger_on_close() {
		return trigger_on_close;
	}
	public void setTrigger_on_close(String trigger_on_close) {
		this.trigger_on_close = trigger_on_close;
	}
//	public Child getChild() {
//		return child;
//	}
//	public void setChild(Child child) {
//		this.child = child;
//	}
	
	
	
	
}
