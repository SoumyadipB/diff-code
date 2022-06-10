package com.ericsson.isf.model;

/**
 * 
 * @author eakinhm
 *
 */
public class DbJobsModel {

	int errorCode;
	String errorMsg;
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	@Override
	public String toString() {
		return "DbJobsModel [errorCode=" + errorCode + ", errorMsg=" + errorMsg + "]";
	}
	
	
}
