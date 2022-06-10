package com.ericsson.isf.iva.exception;

public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1233893004279065711L;
	private Integer errorCode;
	private String errorMessage;
	
	public ApplicationException() { }
	public ApplicationException(Integer errorCode, String errorMessage) {
		super(errorMessage);
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
}
