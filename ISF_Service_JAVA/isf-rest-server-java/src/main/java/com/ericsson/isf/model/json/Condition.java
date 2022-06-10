package com.ericsson.isf.model.json;

public class Condition {
	private String id;
	private String port;
	private String type;
	private String typeName;
	private boolean conditionType; // AND/OR
	private String header;


	

	public boolean isConditionType() {
		return conditionType;
	}

	public void setConditionType(boolean conditionType) {
		this.conditionType = conditionType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
