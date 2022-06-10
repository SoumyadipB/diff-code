package com.ericsson.isf.model.json;

import java.util.Arrays;

public class KeyMatchValue {

	private String[] application_name;
	
	private String application_title;
	
	private String sheet_name;
	
	private String text_search;
	
	private Operator operator;
	
	public KeyMatchValue() {
		this.application_title = "";
		this.sheet_name = "";
		this.text_search = "";
		this.operator = new Operator();
	}

	public String[] getApplication_name() {
		return application_name;
	}

	public void setApplication_name(String[] application_name) {
		this.application_name = application_name;
	}

	public String getApplication_title() {
		return application_title;
	}

	public void setApplication_title(String application_title) {
		this.application_title = application_title;
	}

	public String getSheet_name() {
		return sheet_name;
	}

	public void setSheet_name(String sheet_name) {
		this.sheet_name = sheet_name;
	}

	public String getText_search() {
		return text_search;
	}

	public void setText_search(String text_search) {
		this.text_search = text_search;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return "KeyMatchValue [application_name=" + Arrays.toString(application_name) + ", application_title="
				+ application_title + ", sheet_name=" + sheet_name + ", text_search=" + text_search + ", operator="
				+ operator + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	
}
