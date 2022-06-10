package com.ericsson.isf.model;

import java.util.List;

public class EricssonOrganizationModel {
	
    private String text;
    private String id;
    private List<EricssonSecondLevelUnitModel> children;
    
	
	
	public List<EricssonSecondLevelUnitModel> getChildren() {
		return children;
	}
	public void setChildren(List<EricssonSecondLevelUnitModel> children) {
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
		this.id = "P_"+id;
	}
	
}
