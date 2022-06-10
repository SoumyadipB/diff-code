package com.ericsson.isf.model;

import java.util.Arrays;
import java.util.List;

/**
 * This is a generic class used for mail templates
 * 
 * @author eakinhm
 *
 */
public class GenericMailModel {

	private String[] headers;
	private List<String[]> rows;
	private String caption;
	private String greetingName;
	private String bodyData;

	public String[] getHeaders() {
		return headers;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

	public List<String[]> getRows() {
		return rows;
	}

	public void setRows(List<String[]> rows) {
		this.rows = rows;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getGreetingName() {
		return greetingName;
	}

	public void setGreetingName(String greetingName) {
		this.greetingName = greetingName;
	}

	public String getBodyData() {
		return bodyData;
	}

	public void setBodyData(String bodyData) {
		this.bodyData = bodyData;
	}

	@Override
	public String toString() {
		return "GenericMailModel [headers=" + Arrays.toString(headers) + ", rows=" + rows + ", caption=" + caption
				+ ", greetingName=" + greetingName + ", bodyData=" + bodyData + "]";
	}

	public GenericMailModel(String[] headers, List<String[]> rows, String caption, String greetingName,
			String bodyData) {

		this.headers = headers;
		this.rows = rows;
		this.caption = caption;
		this.greetingName = greetingName;
		this.bodyData = bodyData;
	}

	public GenericMailModel(List<String[]> rows) {
		this.rows = rows;
	}

	public GenericMailModel(String caption, String greetingName, String bodyData) {

		this.caption = caption;
		this.greetingName = greetingName;
		this.bodyData = bodyData;
	}

	public GenericMailModel() {
	}

}
