package com.ericsson.isf.restGraph.model;

import java.util.Arrays;

/**
 * 
 * @author eakinhm
 *
 */
public enum AzureAppDetailsEnum {

	ISF("ISF", "92e84ceb-fbfd-47ab-be52-080c6b87953f", "c6238ba0-d842-4b6a-ac9b-21e51d772c5f",
			"qAm8EHwAoWi4s1wVxMR3v/JBFHxzUqNOOIq9QvDQERv+VSBfBlP2bw==", "https://localhost");
	
	private String appDisplayName;
	private String tenantID;
	private String clientID;
	private String clientSecret;
	private String redirectUrl;
	
	private AzureAppDetailsEnum(String appDisplayName, String tenantID, String clientID, String clientSecret,
			String redirectUrl) {
		this.appDisplayName = appDisplayName;
		this.tenantID = tenantID;
		this.clientID = clientID;
		this.clientSecret = clientSecret;
		this.redirectUrl = redirectUrl;
	}

	public String getAppDisplayName() {
		return appDisplayName;
	}

	public String getTenantID() {
		return tenantID;
	}

	public String getClientID() {
		return clientID;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}
	
	public static AzureAppDetailsEnum getEnumValue(final String appDisplayName) {
		return Arrays.stream(AzureAppDetailsEnum.values()).filter
				(app-> app.getAppDisplayName().equalsIgnoreCase(appDisplayName)).findFirst().get();
	}
	
}
