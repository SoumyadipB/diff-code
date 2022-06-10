package com.ericsson.isf.model;

public class MobileNotificationModel {
	private int referenceId;
	private String module;
	private String auditComments;
	private String toSignum;
	private String createdBy;
	private String createdDate;
	private int woid;
	
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
	public String getAuditComments() {
		return auditComments;
	}
	public void setAuditComments(String auditComments) {
		this.auditComments = auditComments;
	}
	public String getToSignum() {
		return toSignum;
	}
	public void setToSignum(String toSignum) {
		this.toSignum = toSignum;
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
	public int getWoid() {
		return woid;
	}
	public void setWoid(int woid) {
		this.woid = woid;
	}
	
}
