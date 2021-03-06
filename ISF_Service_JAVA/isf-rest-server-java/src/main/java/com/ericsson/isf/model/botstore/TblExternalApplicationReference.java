package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TblExternalApplicationReference generated by hbm2java
 */
@Entity
@Table(name = "TBL_ExternalApplicationReference", schema = "refData")
public class TblExternalApplicationReference implements java.io.Serializable {

	private int referenceId;
	private int sourceId;
	private Integer projectId;
	private Integer externalProjectId;
	private String createdby;
	private Date createdOn;
	private Boolean isactive;

	public TblExternalApplicationReference() {
	}

	public TblExternalApplicationReference(int referenceId, int sourceId, String createdby) {
		this.referenceId = referenceId;
		this.sourceId = sourceId;
		this.createdby = createdby;
	}

	public TblExternalApplicationReference(int referenceId, int sourceId, Integer projectId, Integer externalProjectId,
			String createdby, Date createdOn, Boolean isactive) {
		this.referenceId = referenceId;
		this.sourceId = sourceId;
		this.projectId = projectId;
		this.externalProjectId = externalProjectId;
		this.createdby = createdby;
		this.createdOn = createdOn;
		this.isactive = isactive;
	}

	@Id

	@Column(name = "referenceId", unique = true, nullable = false)
	public int getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(int referenceId) {
		this.referenceId = referenceId;
	}

	@Column(name = "sourceId", nullable = false)
	public int getSourceId() {
		return this.sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	@Column(name = "projectId")
	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Column(name = "externalProjectId")
	public Integer getExternalProjectId() {
		return this.externalProjectId;
	}

	public void setExternalProjectId(Integer externalProjectId) {
		this.externalProjectId = externalProjectId;
	}

	@Column(name = "createdby", nullable = false, length = 8)
	public String getCreatedby() {
		return this.createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdOn", length = 23)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "isactive")
	public Boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

}
