package com.ericsson.isf.model;

public class AzureAppModel {

	private int azureAppID;
	private String appName;
	private String tenantID;
	private String clientID;
	private String clientSecret;
	private String redirectUrl;
	private boolean isActive;

	public int getAzureAppID() {
		return azureAppID;
	}

	public void setAzureAppID(int azureAppID) {
		this.azureAppID = azureAppID;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getTenantID() {
		return tenantID;
	}

	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
