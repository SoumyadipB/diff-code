package com.ericsson.isf.iva.profiles.configuration;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
public class AppConfig {

	@Value("${isf.api.url}")
	private String isfUrl;
	
	@Value("${app.message}")
	private String appMessage;
	
	@Value("${app.security.enabled}")
	private boolean isSecurityEnabled;
	
	@Value("${isf.signalR.hub.name}")
	private String signalrHubName;
	
	@Value("${isf.signalR.app.url}")
	private String signalrAppUrl;
	
	@Value("${isf.signalR.hub.url}")
	private String signalrHubUrl;
	
	public String getSignalrHubName() {
		return signalrHubName;
	}

	public void setSignalrHubName(String signalrHubName) {
		this.signalrHubName = signalrHubName;
	}

	public String getSignalrAppUrl() {
		return signalrAppUrl;
	}

	public void setSignalrAppUrl(String signalrAppUrl) {
		this.signalrAppUrl = signalrAppUrl;
	}

	public String getSignalrHubUrl() {
		return signalrHubUrl;
	}

	public void setSignalrHubUrl(String signalrHubUrl) {
		this.signalrHubUrl = signalrHubUrl;
	}

	private SecretKey secretKey;
	
	public SecretKey getSecretKey() {
		return secretKey;
	}

	@Autowired
	public void setSecretKey(@Value("${secert.jwt.key}") String secret) {
		byte[] decodedKey =Base64.getDecoder().decode(secret);
		this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
	}

	public String getIsfUrl() {
		return isfUrl;
	}

	public void setIsfUrl(String isfUrl) {
		this.isfUrl = isfUrl;
	}

	public String getAppMessage() {
		return appMessage;
	}

	public void setAppMessage(String appMessage) {
		this.appMessage = appMessage;
	}

	public boolean isSecurityEnabled() {
		return isSecurityEnabled;
	}

	public void setSecurityEnabled(boolean isSecurityEnabled) {
		this.isSecurityEnabled = isSecurityEnabled;
	}

}
