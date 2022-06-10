package com.ericsson.isf.model;

public class SharePointDetailModel {
	
	private String clientID;
	private String baseUrl;
	private String sitename;
	private String secretKey;
	private String marketAreaName;
	private int marketAreaId;
	private String aeskey;
	private String ivsKey;
	
	public String getIvsKey() {
		return ivsKey;
	}
	public void setIvsKey(String ivsKey) {
		this.ivsKey = ivsKey;
	}
	
	public String getAeskey() {
		return aeskey;
	}
	public void setAeskey(String aeskey) {
		this.aeskey = aeskey;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getSitename() {
		return sitename;
	}
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
	public int getMarketAreaId() {
		return marketAreaId;
	}
	public void setMarketAreaId(int marketAreaId) {
		this.marketAreaId = marketAreaId;
	}
	public String getMarketAreaName() {
		return marketAreaName;
	}
	public void setMarketAreaName(String marketAreaName) {
		this.marketAreaName = marketAreaName;
	}
}
