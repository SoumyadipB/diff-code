package com.ericsson.isf.model.json;

public class Trigger {
	private Notification notifications;
	
	public Trigger() {		
	}
	
	public Trigger(Notification notifications) {
		this.notifications = notifications;
	}

	public Notification getNotifications() {
		return notifications;
	}

	public void setNotifications(Notification notifications) {
		this.notifications = notifications;
	}

		
	
}
