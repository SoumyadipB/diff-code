package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SharePointModel {
	
	private String fileNamePattern;
	private Integer marketAreaID;
	private String marketArea; 
	private String clientID;
	private String secretKey;
	private String folderName;
	private String baseURL;
	private String viewName;
	private String modifiedBy;
	private String createdBy;
	private Date modifiedOn;
	private Date createdOn;
    private String separator;
    private String siteName;
    public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	private DynamicClause clause;
    private Map<String, String> dateFormats;
	
	public String getSeparator() {
		return separator;
	}
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
	public Integer getMarketAreaID() {
		return marketAreaID;
	}
	public void setMarketAreaID(Integer marketAreaID) {
		this.marketAreaID = marketAreaID;
	}
	public String getMarketArea() {
		return marketArea;
	}
	public void setMarketArea(String marketArea) {
		this.marketArea = marketArea;
	}
	public String getClientId() {
		return clientID;
	}
	public void setClientId(String clientId) {
		this.clientID = clientId;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getBaseURL() {
		return baseURL;
	}
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getFileNamePattern() {
		return fileNamePattern;
	}
	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}
	public DynamicClause getClause() {
		return clause;
	}
	public void setClause(DynamicClause clause) {
		this.clause = clause;
	}
	public Map<String, String> getDateFormats() {
		return dateFormats;
	}
	public void setDateFormats(Map<String, String> dateFormats) {
		this.dateFormats = dateFormats;
	}
	

}
