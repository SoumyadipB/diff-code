package com.ericsson.isf.model;

import java.util.List;

public class ColumnModel {
	private String columnName;
	private List<Object> inValues;
	
	public List<Object> getInValues() {
		return inValues;
	}
	public void setInValues(List<Object> inValues) {
		this.inValues = inValues;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = "["+columnName+"]";
	}

}
