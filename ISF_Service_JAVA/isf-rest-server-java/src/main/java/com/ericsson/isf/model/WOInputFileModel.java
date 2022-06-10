package com.ericsson.isf.model;

public class WOInputFileModel {
	private int id;
	private String inputName;
	private String inputUrl;
	public String getInputName() {
		return inputName;
	}
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}
	public String getInputUrl() {
		return inputUrl;
	}
	public void setInputUrl(String inputUrl) {
		this.inputUrl = inputUrl;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public WOInputFileModel(String inputName, String inputUrl) {
		this.inputName = inputName;
		this.inputUrl = inputUrl;
	}
	public WOInputFileModel(int id, String inputName, String inputUrl) {
		this.id = id;
		this.inputName = inputName;
		this.inputUrl = inputUrl;
	}
	public WOInputFileModel() {
	}
	
	
}
