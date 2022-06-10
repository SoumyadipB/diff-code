package com.ericsson.isf.model;

import java.util.Date;

public class ExternalSourceModel {
	
	private Integer sourceId;
	private String type;
	private String sourceName;
	private String createdby;
	private Date createdDate;
	private String referenceOwner;
	private String referenceSponser;
	private String description;

	public Integer getSourceId() {
		return sourceId;
	}
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}
	public String getType() {
		return type;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getReferenceOwner() {
		return referenceOwner;
	}
	public void setReferenceOwner(String referenceOwner) {
		this.referenceOwner = referenceOwner;
	}
	public String getReferenceSponser() {
		return referenceSponser;
	}
	public void setReferenceSponser(String referenceSponser) {
		this.referenceSponser = referenceSponser;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
