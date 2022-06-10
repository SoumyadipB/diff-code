package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErisiteDocumentContainer {
   
	private String documentContainerId;
	private String documentType;
	private String documentName;
	private String documentStatus;
	private String attachedDate;
    private String documentFileData;
    private String documentFileUrl;
    
    @JsonProperty("info:DocumentContainerId")
	public String getDocumentContainerId() {
		return documentContainerId;
	}
    @JsonProperty("info:DocumentContainerId")
	public void setDocumentContainerId(String documentContainerId) {
		this.documentContainerId = documentContainerId;
	}
    @JsonProperty("info:DocumentType")
	public String getDocumentType() {
		return documentType;
	}
    @JsonProperty("info:DocumentType")
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
    @JsonProperty("info:DocumentName")
	public String getDocumentName() {
		return documentName;
	}
    @JsonProperty("info:DocumentName")
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
    @JsonProperty("info:DocumentStatus")
	public String getDocumentStatus() {
		return documentStatus;
	}
    @JsonProperty("info:DocumentStatus")
	public void setDocumentStatus(String documentStatus) {
		this.documentStatus = documentStatus;
	}
    @JsonProperty("info:AttachedDate")
	public String getAttachedDate() {
		return attachedDate;
	}
    @JsonProperty("info:AttachedDate")
	public void setAttachedDate(String attachedDate) {
		this.attachedDate = attachedDate;
	}
    @JsonProperty("info:DocumentFileData")
	public String getDocumentFileData() {
		return documentFileData;
	}
    @JsonProperty("info:DocumentFileData")
	public void setDocumentFileData(String documentFileData) {
		this.documentFileData = documentFileData;
	}
    @JsonProperty("info:DocumentFileUrl")
	public String getDocumentFileUrl() {
		return documentFileUrl;
	}
    @JsonProperty("info:DocumentFileUrl")
	public void setDocumentFileUrl(String documentFileUrl) {
		this.documentFileUrl = documentFileUrl;
	}

}
