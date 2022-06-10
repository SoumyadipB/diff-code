package com.ericsson.isf.model;


public class SignalrModel {
	private String hubUrl;
	private String hubName;
	private String methodName;
	private Object payload;

	private String executionType;

	public SignalrModel methodName(String methodName) {
		this.methodName=methodName;
		return this;
	}
	
	public SignalrModel payload(Object payload) {
		this.payload=payload;
		return this;
	}
	
	public String getExecutionType() {
		return executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public String getHubUrl() {
		return hubUrl;
	}

	public void setHubUrl(String hubUrl) {
		this.hubUrl = hubUrl;
	}

	public String getHubName() {
		return hubName;
	}

	public void setHubName(String hubName) {
		this.hubName = hubName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	@Override
	public String toString() {
		return "SignalrModel [hubUrl=" + hubUrl + ", hubName=" + hubName + ", methodName=" + methodName + ", payload="
				+ payload + ", executionType=" + executionType + "]";
	}
}
