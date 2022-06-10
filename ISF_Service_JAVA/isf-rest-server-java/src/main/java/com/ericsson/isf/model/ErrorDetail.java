package com.ericsson.isf.model;

import java.io.Serializable;

public class ErrorDetail implements Serializable {
	private static final long serialVersionUID = 3763953665887959790L;
	private Integer errorCode;
	private String errorMessage;
	private String errorDescription;
	private String errorStackTrace;
	private StackTraceElement[] stackTraceElements;
	
	public ErrorDetail() {}
	public ErrorDetail(Integer errorCode, String errorMessage, String errorDescription, StackTraceElement[] stackTraceElements) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorDescription = errorDescription;
//		this.stackTraceElements = stackTraceElements;
//		this.errorStackTrace = Arrays.stream(stackTraceElements).map(st -> st.toString())
//				   										        .reduce("", (a,b) -> a + "\n" + b);
	}
	public ErrorDetail(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public String getErrorStackTrace() {
		return errorStackTrace;
	}
	public void setErrorStackTrace(String errorStackTrace) {
		this.errorStackTrace = errorStackTrace;
	}
	public StackTraceElement[] getStackTraceElements() {
		return stackTraceElements;
	}
	public void setStackTraceElements(StackTraceElement[] stackTraceElements) {
		this.stackTraceElements = stackTraceElements;
	}
}
