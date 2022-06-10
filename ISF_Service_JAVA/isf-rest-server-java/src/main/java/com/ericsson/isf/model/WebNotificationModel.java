package com.ericsson.isf.model;

public class WebNotificationModel {
	private int notificationId;
	private String notificationSource;
	private String auditComments;
	private int referenceId;
	private String module;
	private int woid;
	private String createdDate;
	private boolean active;
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getToSignum() {
		return toSignum;
	}
	public void setToSignum(String toSignum) {
		this.toSignum = toSignum;
	}
	private String createdBy;
	private String toSignum;
	
	public int getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}
	
	public String getNotificationSource() {
		return notificationSource;
	}
	public void setNotificationSource(String notificationSource) {
		this.notificationSource = notificationSource;
	}
	public String getAuditComments() {
		return auditComments;
	}
	public void setAuditComments(String auditComments) {
		this.auditComments = auditComments;
	}
	public int getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(int referenceId) {
		this.referenceId = referenceId;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public int getWoid() {
		return woid;
	}
	public void setWoid(int woid) {
		this.woid = woid;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	

}
