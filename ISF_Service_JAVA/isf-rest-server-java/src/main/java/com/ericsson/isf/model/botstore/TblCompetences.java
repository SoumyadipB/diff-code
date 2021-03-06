package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TblCompetences generated by hbm2java
 */
@Entity
@Table(name = "TBL_Competences", schema = "refData")
public class TblCompetences implements java.io.Serializable {

	private int competenceId;
	private Serializable competenceName;
	private Serializable competenceType;
	private Integer parentCompetenceId;
	private Serializable createdBy;
	private Date createdOn;
	private Serializable lastModifiedBy;
	private Date lastModifiedOn;
	private Boolean active;
	private Integer merlinExtId;
	private Set<TblResourceRequestCompetences> tblResourceRequestCompetenceses = new HashSet<TblResourceRequestCompetences>(
			0);

	public TblCompetences() {
	}

	public TblCompetences(int competenceId, Date createdOn, Date lastModifiedOn) {
		this.competenceId = competenceId;
		this.createdOn = createdOn;
		this.lastModifiedOn = lastModifiedOn;
	}

	public TblCompetences(int competenceId, Serializable competenceName, Serializable competenceType,
			Integer parentCompetenceId, Serializable createdBy, Date createdOn, Serializable lastModifiedBy,
			Date lastModifiedOn, Boolean active, Integer merlinExtId,
			Set<TblResourceRequestCompetences> tblResourceRequestCompetenceses) {
		this.competenceId = competenceId;
		this.competenceName = competenceName;
		this.competenceType = competenceType;
		this.parentCompetenceId = parentCompetenceId;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedOn = lastModifiedOn;
		this.active = active;
		this.merlinExtId = merlinExtId;
		this.tblResourceRequestCompetenceses = tblResourceRequestCompetenceses;
	}

	@Id

	@Column(name = "CompetenceID", unique = true, nullable = false)
	public int getCompetenceId() {
		return this.competenceId;
	}

	public void setCompetenceId(int competenceId) {
		this.competenceId = competenceId;
	}

	@Column(name = "CompetenceName")
	public Serializable getCompetenceName() {
		return this.competenceName;
	}

	public void setCompetenceName(Serializable competenceName) {
		this.competenceName = competenceName;
	}

	@Column(name = "CompetenceType")
	public Serializable getCompetenceType() {
		return this.competenceType;
	}

	public void setCompetenceType(Serializable competenceType) {
		this.competenceType = competenceType;
	}

	@Column(name = "ParentCompetenceID")
	public Integer getParentCompetenceId() {
		return this.parentCompetenceId;
	}

	public void setParentCompetenceId(Integer parentCompetenceId) {
		this.parentCompetenceId = parentCompetenceId;
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

	@Column(name = "MerlinExtID")
	public Integer getMerlinExtId() {
		return this.merlinExtId;
	}

	public void setMerlinExtId(Integer merlinExtId) {
		this.merlinExtId = merlinExtId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblCompetences")
	public Set<TblResourceRequestCompetences> getTblResourceRequestCompetenceses() {
		return this.tblResourceRequestCompetenceses;
	}

	public void setTblResourceRequestCompetenceses(Set<TblResourceRequestCompetences> tblResourceRequestCompetenceses) {
		this.tblResourceRequestCompetenceses = tblResourceRequestCompetenceses;
	}

}
