package com.ericsson.isf.model;

public class SignalrModelTest {

	private String hubUrl;
	private String hubName;
	private String methodName;
	private Object payload;
	

	private String executionType;
	private String callSignalRApp;
	
	public String getCallSignalRApp() {
		return callSignalRApp;
	}

	public void setCallSignalRApp(String callSignalRApp) {
		this.callSignalRApp = callSignalRApp;
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
