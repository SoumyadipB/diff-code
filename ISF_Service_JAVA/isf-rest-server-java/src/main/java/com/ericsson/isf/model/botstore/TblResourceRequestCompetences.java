package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TblResourceRequestCompetences generated by hbm2java
 */
@Entity
@Table(name = "TBL_ResourceRequestCompetences", schema = "transactionalData")
public class TblResourceRequestCompetences implements java.io.Serializable {

	private int resourceRequestCompetenceId;
	private TblCompetences tblCompetences;
	private TblResourceRequests tblResourceRequests;
	private Serializable competenceLevel;
	private Serializable createdBy;
	private Date createdOn;
	private Serializable lastModifiedBy;
	private Date lastModifiedOn;
	private Boolean active;

	public TblResourceRequestCompetences() {
	}

	public TblResourceRequestCompetences(int resourceRequestCompetenceId, Date createdOn, Date lastModifiedOn) {
		this.resourceRequestCompetenceId = resourceRequestCompetenceId;
		this.createdOn = createdOn;
		this.lastModifiedOn = lastModifiedOn;
	}

	public TblResourceRequestCompetences(int resourceRequestCompetenceId, TblCompetences tblCompetences,
			TblResourceRequests tblResourceRequests, Serializable competenceLevel, Serializable createdBy,
			Date createdOn, Serializable lastModifiedBy, Date lastModifiedOn, Boolean active) {
		this.resourceRequestCompetenceId = resourceRequestCompetenceId;
		this.tblCompetences = tblCompetences;
		this.tblResourceRequests = tblResourceRequests;
		this.competenceLevel = competenceLevel;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedOn = lastModifiedOn;
		this.active = active;
	}

	@Id

	@Column(name = "ResourceRequestCompetenceID", unique = true, nullable = false)
	public int getResourceRequestCompetenceId() {
		return this.resourceRequestCompetenceId;
	}

	public void setResourceRequestCompetenceId(int resourceRequestCompetenceId) {
		this.resourceRequestCompetenceId = resourceRequestCompetenceId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CompetenceID")
	public TblCompetences getTblCompetences() {
		return this.tblCompetences;
	}

	public void setTblCompetences(TblCompetences tblCompetences) {
		this.tblCompetences = tblCompetences;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ResourceRequestID")
	public TblResourceRequests getTblResourceRequests() {
		return this.tblResourceRequests;
	}

	public void setTblResourceRequests(TblResourceRequests tblResourceRequests) {
		this.tblResourceRequests = tblResourceRequests;
	}

	@Column(name = "CompetenceLevel")
	public Serializable getCompetenceLevel() {
		return this.competenceLevel;
	}

	public void setCompetenceLevel(Serializable competenceLevel) {
		this.competenceLevel = competenceLevel;
	}

	@Column(name = "CreatedBy")
	public Serializable getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Serializable createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedOn", nullable = false, length = 23)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "LastModifiedBy")
	public Serializable getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(Serializable lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LastModifiedOn", nullable = false, length = 23)
	public Date getLastModifiedOn() {
		return this.lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
