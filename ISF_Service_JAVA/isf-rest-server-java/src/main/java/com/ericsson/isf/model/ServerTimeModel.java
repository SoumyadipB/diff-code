package com.ericsson.isf.model;

public class ServerTimeModel {
	private int woID;
	private int taskId;
	private int serverTime;
	private Double totalEffort;
	
	public int getWoID() {
		return woID;
	}
	public void setWoID(int woID) {
		this.woID = woID;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public int getServerTime() {
		return serverTime;
	}
	public void setServerTime(int serverTime) {
		this.serverTime = serverTime;
	}
	public Double getTotalEffort() {
		return totalEffort;
	}
	public void setTotalEffort(Double totalEffort) {
		this.totalEffort = totalEffort;
	}
	
}
