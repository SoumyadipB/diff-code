/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author edhhklu
 */
public class AuditDataModel {
	@JsonProperty("id")
	String commentid; 
	
	Integer parent ;     
	
	@JsonProperty("created")
	Date dateCreated;
	
	
	Date modified  ;  
	
	@XmlElement
	@JsonProperty("content")
	String message; 
	
	String fileURL ;            
	
	long auditgroupid;
	
	String type;
	String fieldName;
	String oldValue;
	String newValue;
	String actorType;
	Integer sourceId;
	String refferer;
	@JsonProperty("fullname")
	String createdBy;
	
	String context;
	@JsonProperty("notificationFlag")
	Integer notification;
	Integer importance;
	Integer auditPageId;
	String additionalInfo;
	String commentCategory;
	int srid;
	String srCreator;
	
	public String getRefferer() {
		return refferer;
	}
	public void setRefferer(String refferer) {
		this.refferer = refferer;
	}
	
	public int getSrid() {
		return srid;
	}
	public void setSrid(int srid) {
		this.srid = srid;
	}
	public String getSrCreator() {
		return srCreator;
	}
	public void setSrCreator(String srCreator) {
		this.srCreator = srCreator;
	}
	public String getAuditGroupCategory() {
		return auditGroupCategory;
	}
	public void setAuditGroupCategory(String auditGroupCategory) {
		this.auditGroupCategory = auditGroupCategory;
	}
	String auditGroupCategory;
	
	

	public String getCommentCategory() {
		return commentCategory;
	}
	public void setCommentCategory(String commentCategory) {
		this.commentCategory = commentCategory;
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	
	public Integer getNotification() {
		return notification;
	}
	public void setNotification(Integer notification) {
		this.notification = notification;
	}
	public Integer getImportance() {
		return importance;
	}
	public void setImportance(Integer importance) {
		this.importance = importance;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getActorType() {
		return actorType;
	}
	public void setActorType(String actorType) {
		this.actorType = actorType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getAuditgroupid() {
		return auditgroupid;
	}
	public void setAuditgroupid(long auditgroupid) {
		this.auditgroupid = auditgroupid;
	}
	public Integer getParent() {
		return parent;
	}
	
	public String getCommentid() {
		return commentid;
	}
	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}
	public void setParent(Integer parent) {
		this.parent = parent;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}

	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFileURL() {
		return fileURL;
	}
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Integer getAuditPageId() {
		return auditPageId;
	}
	public void setAuditPageId(Integer auditPageId) {
		this.auditPageId = auditPageId;
	}
	public Integer getSourceId() {
		return sourceId;
	}
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}
	
	
}
