package com.ericsson.isf.model;

public class ErrorDetailsModel {
  private int errorCode;
  private String errorType;
  private String errorMessage;
  private Integer sourceID;
  private String externalReference;
  private int totalCounts;
  private String createdBy;
  
  
public int getErrorCode() {
	return errorCode;
}
public void setErrorCode(int errorCode) {
	this.errorCode = errorCode;
}
public String getErrorType() {
	return errorType;
}
public void setErrorType(String errorType) {
	this.errorType = errorType;
}
public String getErrorMessage() {
	return errorMessage;
}
public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
}
public int getTotalCounts() {
	return totalCounts;
}
public void setTotalCounts(int totalCounts) {
	this.totalCounts = totalCounts;
}
public String getExternalReference() {
	return externalReference;
}
public void setExternalReference(String externalReference) {
	this.externalReference = externalReference;
}
public String getCreatedBy() {
	return createdBy;
}
public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}
public Integer getSourceID() {
	return sourceID;
}
public void setSourceID(Integer sourceID) {
	this.sourceID = sourceID;
}
  
}
