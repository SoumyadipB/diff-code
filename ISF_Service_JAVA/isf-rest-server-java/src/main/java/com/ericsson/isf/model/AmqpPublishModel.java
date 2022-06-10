package com.ericsson.isf.model;

public class AmqpPublishModel {

	private String serverBotInputUrl;
	private String serverBotOutputUrl;

	

	public String getServerBotInputUrl() {
		return serverBotInputUrl;
	}

	public void setServerBotInputUrl(String serverBotInputUrl) {
		this.serverBotInputUrl = serverBotInputUrl;
	}

	public String getServerBotOutputUrl() {
		return serverBotOutputUrl;
	}

	public void setServerBotOutputUrl(String serverBotOutputUrl) {
		this.serverBotOutputUrl = serverBotOutputUrl;
	}

	private Object payload;

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "AmqpPublishModel [serverBotInputUrl=" + serverBotInputUrl + ", serverBotOutputUrl=" + serverBotOutputUrl
				+ ", payload=" + payload + "]";
	}

	
	

}
