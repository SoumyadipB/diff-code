package com.ericsson.isf.model;

public class BotInputFileModel {
	private int woid;
	private int taskid;
	private String stepid;
	private String signum;
	private String detailType;
	private String fileName;
	private byte[] fileData;
	private int bookingid;
	private int serverBotDetailsId;
	public int getWoid() {
		return woid;
	}
	public void setWoid(int woid) {
		this.woid = woid;
	}
	public int getTaskid() {
		return taskid;
	}
	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
	public String getStepid() {
		return stepid;
	}
	public void setStepid(String stepid) {
		this.stepid = stepid;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public String getDetailType() {
		return detailType;
	}
	public void setDetailType(String detailType) {
		this.detailType = detailType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public int getBookingid() {
		return bookingid;
	}
	public void setBookingid(int bookingid) {
		this.bookingid = bookingid;
	}
	public int getServerBotDetailsId() {
		return serverBotDetailsId;
	}
	public void setServerBotDetailsId(int serverBotDetailsId) {
		this.serverBotDetailsId = serverBotDetailsId;
	}
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

}
