package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErisiteChecklist {
 
	private String checklistItemName;
	private String checklistItemValue;
    private String checklistItemDatatype;
     
    @JsonProperty("info:ChecklistItemName") 
	public String getChecklistItemName() {
		return checklistItemName;
	}
    @JsonProperty("info:ChecklistItemName")
	public void setChecklistItemName(String checklistItemName) {
		this.checklistItemName = checklistItemName;
	}
    @JsonProperty("info:ChecklistItemValue")
	public String getChecklistItemValue() {
		return checklistItemValue;
	}
    @JsonProperty("info:ChecklistItemValue")
	public void setChecklistItemValue(String checklistItemValue) {
		this.checklistItemValue = checklistItemValue;
	}
    @JsonProperty("info:ChecklistItemDatatype")
	public String getChecklistItemDatatype() {
		return checklistItemDatatype;
	}
    @JsonProperty("info:ChecklistItemDatatype")
	public void setChecklistItemDatatype(String checklistItemDatatype) {
		this.checklistItemDatatype = checklistItemDatatype;
	}
}
