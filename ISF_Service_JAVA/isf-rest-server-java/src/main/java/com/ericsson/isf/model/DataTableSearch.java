package com.ericsson.isf.model;

import javax.servlet.http.HttpServletRequest;

/**
 * The Class DataTableColumnSpecs.
 *
 * @author edhhklu
 */
public class DataTableSearch {
	
	private String value;
	private boolean regex;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isRegex() {
		return regex;
	}
	public void setRegex(boolean regex) {
		this.regex = regex;
	}
	
	
}