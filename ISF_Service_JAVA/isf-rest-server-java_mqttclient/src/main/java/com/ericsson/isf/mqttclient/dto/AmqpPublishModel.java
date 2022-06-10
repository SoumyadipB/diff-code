package com.ericsson.isf.mqttclient.dto;

public class AmqpPublishModel {
	private Object payload;
	private String serverBotInputUrl;
	private String serverBotOutputUrl;

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "AmqpPublishModel [payload=" + payload + ", serverBotInputUrl=" + serverBotInputUrl
				+ ", serverBotOutputUrl=" + serverBotOutputUrl + "]";
	}

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

	

}
