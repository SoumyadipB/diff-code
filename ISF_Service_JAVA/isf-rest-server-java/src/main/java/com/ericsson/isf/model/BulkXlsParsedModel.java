package com.ericsson.isf.model;

import java.util.List;
import java.util.Map;

public class BulkXlsParsedModel {
	
	private List<BulkWorkOrderCreationModel> parsedData;
	private Map<String,String> validationData;
	public List<BulkWorkOrderCreationModel> getParsedData() {
		return parsedData;
	}
	public void setParsedData(List<BulkWorkOrderCreationModel> parsedData) {
		this.parsedData = parsedData;
	}
	public Map<String, String> getValidationData() {
		return validationData;
	}
	public void setValidationData(Map<String, String> validationData) {
		this.validationData = validationData;
	}
	
	
	
}
