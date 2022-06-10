package com.ericsson.isf.model;

import java.util.List;

public class EricssonSecondLevelUnitModel {

    private String text;
    private String id;
    private boolean active;
    private List<String> children ;
	
	public boolean getActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public List<String> getChildren() {
		return children;
	}
	public void setChildren(List<String> children) {
		this.children = children;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = "C_"+id;
	}
    
}
