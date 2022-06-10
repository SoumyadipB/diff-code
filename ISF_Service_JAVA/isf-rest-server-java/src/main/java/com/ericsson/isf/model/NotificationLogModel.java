package com.ericsson.isf.model;

public class NotificationLogModel {
	private int notificationId;
	private String signum;
	private String notificationPurpose;
	private String notificationFrom;
	private String notificationTo;
	private String notificationReceivedDateTime;
	private String notificationSendDateTime;
	private boolean isActive;
	private String createdBy;
	private String modifiedBy;
	private String createdDate;
	private String modifiedDate;

	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getSignum() {
		return signum;
	}

	public void setSignum(String signum) {
		this.signum = signum;
	}

	public String getNotificationPurpose() {
		return notificationPurpose;
	}

	public void setNotificationPurpose(String notificationPurpose) {
		this.notificationPurpose = notificationPurpose;
	}

	public String getNotificationFrom() {
		return notificationFrom;
	}

	public void setNotificationFrom(String notificationFrom) {
		this.notificationFrom = notificationFrom;
	}

	public String getNotificationTo() {
		return notificationTo;
	}

	public void setNotificationTo(String notificationTo) {
		this.notificationTo = notificationTo;
	}

	public String getNotificationReceivedDateTime() {
		return notificationReceivedDateTime;
	}

	public void setNotificationReceivedDateTime(String notificationReceivedDateTime) {
		this.notificationReceivedDateTime = notificationReceivedDateTime;
	}

	public String getNotificationSendDateTime() {
		return notificationSendDateTime;
	}

	public void setNotificationSendDateTime(String notificationSendDateTime) {
		this.notificationSendDateTime = notificationSendDateTime;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
