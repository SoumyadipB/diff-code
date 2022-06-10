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
import javax.persistence.UniqueConstraint;

/**
 * TblActivityscope generated by hbm2java
 */
@Entity
@Table(name = "TBL_ACTIVITYSCOPE", schema = "transactionalData", uniqueConstraints = @UniqueConstraint(columnNames = {
		"SubActivityID", "ProjectScopeDetailID", "Active" }))
public class TblActivityscope implements java.io.Serializable {

	private int activityScopeId;
	private TblProjectscopedetail tblProjectscopedetail;
	private int subActivityId;
	private double avgEstdEffort;
	private String createdBy;
	private Date createdDate;
	private Serializable lastModifiedBy;
	private Date lastModifiedDate;
	private Boolean active;

	public TblActivityscope() {
	}

	public TblActivityscope(int activityScopeId, int subActivityId, double avgEstdEffort) {
		this.activityScopeId = activityScopeId;
		this.subActivityId = subActivityId;
		this.avgEstdEffort = avgEstdEffort;
	}

	public TblActivityscope(int activityScopeId, TblProjectscopedetail tblProjectscopedetail, int subActivityId,
			double avgEstdEffort, String createdBy, Date createdDate, Serializable lastModifiedBy,
			Date lastModifiedDate, Boolean active) {
		this.activityScopeId = activityScopeId;
		this.tblProjectscopedetail = tblProjectscopedetail;
		this.subActivityId = subActivityId;
		this.avgEstdEffort = avgEstdEffort;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.active = active;
	}

	@Id

	@Column(name = "ActivityScopeID", unique = true, nullable = false)
	public int getActivityScopeId() {
		return this.activityScopeId;
	}

	public void setActivityScopeId(int activityScopeId) {
		this.activityScopeId = activityScopeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ProjectScopeDetailID")
	public TblProjectscopedetail getTblProjectscopedetail() {
		return this.tblProjectscopedetail;
	}

	public void setTblProjectscopedetail(TblProjectscopedetail tblProjectscopedetail) {
		this.tblProjectscopedetail = tblProjectscopedetail;
	}

	@Column(name = "SubActivityID", nullable = false)
	public int getSubActivityId() {
		return this.subActivityId;
	}

	public void setSubActivityId(int subActivityId) {
		this.subActivityId = subActivityId;
	}

	@Column(name = "AvgEstdEffort", nullable = false, precision = 53, scale = 0)
	public double getAvgEstdEffort() {
		return this.avgEstdEffort;
	}

	public void setAvgEstdEffort(double avgEstdEffort) {
		this.avgEstdEffort = avgEstdEffort;
	}

	@Column(name = "CreatedBy", length = 256)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedDate", length = 23)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "LastModifiedBy")
	public Serializable getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(Serializable lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LastModifiedDate", length = 23)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "Active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}