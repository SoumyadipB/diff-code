package com.ericsson.isf.model;

import java.util.Date;

public class ExceptionLogModel {
	private Integer wOID;
	private Integer taskID;
	private Integer botID;
	private Integer bookingID;
	private String signum;
	private int errorCode;
	private String errorMsg;
	public Integer getwOID() {
		return wOID;
	}
	public void setwOID(Integer wOID) {
		this.wOID = wOID;
	}
	public Integer getTaskID() {
		return taskID;
	}
	public void setTaskID(Integer taskID) {
		this.taskID = taskID;
	}
	public Integer getBotID() {
		return botID;
	}
	public void setBotID(Integer botID) {
		this.botID = botID;
	}
	public Integer getBookingID() {
		return bookingID;
	}
	public void setBookingID(Integer bookingID) {
		this.bookingID = bookingID;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
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
	
	
	

}
