package com.ericsson.isf.mqttclient.profiles.configuration;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
public class AppConfig {


	@Value("${mqtt.client.url}")
	private String mqttUrl;

	@Value("${mqtt.client.username}")
	private String mqttUserName;

	@Value("${mqtt.client.password}")
	private String mqttPassword;

	@Value("${mqtt.client.clientId}")
	private String mqttClientId;
	
	@Value("${mqtt.client.keepalive}")
	private int mqttKeepalive;
	
	@Value("${mqtt.client.timeout}")
	private int mqttTimeout;
	
	public String getMqttUrl() {
		return mqttUrl;
	}

	public void setMqttUrl(String mqttUrl) {
		this.mqttUrl = mqttUrl;
	}

	public String getMqttUserName() {
		return mqttUserName;
	}

	public void setMqttUserName(String mqttUserName) {
		this.mqttUserName = mqttUserName;
	}

	public String getMqttPassword() {
		return mqttPassword;
	}

	public void setMqttPassword(String mqttPassword) {
		this.mqttPassword = mqttPassword;
	}

	public String getMqttClientId() {
		return mqttClientId;
	}

	public void setMqttClientId(String mqttClientId) {
		this.mqttClientId = mqttClientId;
	}

	public int getMqttKeepalive() {
		return mqttKeepalive;
	}

	public void setMqttKeepalive(int mqttKeepalive) {
		this.mqttKeepalive = mqttKeepalive;
	}

	public int getMqttTimeout() {
		return mqttTimeout;
	}

	public void setMqttTimeout(int mqttTimeout) {
		this.mqttTimeout = mqttTimeout;
	}
	
}
