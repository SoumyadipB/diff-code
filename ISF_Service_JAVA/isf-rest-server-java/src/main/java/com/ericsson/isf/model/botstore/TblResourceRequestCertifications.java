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
 * TblResourceRequestCertifications generated by hbm2java
 */
@Entity
@Table(name = "TBL_ResourceRequestCertifications", schema = "transactionalData")
public class TblResourceRequestCertifications implements java.io.Serializable {

	private int resourceRequestCertificationId;
	private TblCertificates tblCertificates;
	private TblResourceRequests tblResourceRequests;
	private Serializable createdBy;
	private Date createdOn;
	private Serializable lastModifiedBy;
	private Date lastModifiedOn;
	private Boolean active;

	public TblResourceRequestCertifications() {
	}

	public TblResourceRequestCertifications(int resourceRequestCertificationId, Date createdOn, Date lastModifiedOn) {
		this.resourceRequestCertificationId = resourceRequestCertificationId;
		this.createdOn = createdOn;
		this.lastModifiedOn = lastModifiedOn;
	}

	public TblResourceRequestCertifications(int resourceRequestCertificationId, TblCertificates tblCertificates,
			TblResourceRequests tblResourceRequests, Serializable createdBy, Date createdOn,
			Serializable lastModifiedBy, Date lastModifiedOn, Boolean active) {
		this.resourceRequestCertificationId = resourceRequestCertificationId;
		this.tblCertificates = tblCertificates;
		this.tblResourceRequests = tblResourceRequests;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedOn = lastModifiedOn;
		this.active = active;
	}

	@Id

	@Column(name = "ResourceRequestCertificationID", unique = true, nullable = false)
	public int getResourceRequestCertificationId() {
		return this.resourceRequestCertificationId;
	}

	public void setResourceRequestCertificationId(int resourceRequestCertificationId) {
		this.resourceRequestCertificationId = resourceRequestCertificationId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CertificateID")
	public TblCertificates getTblCertificates() {
		return this.tblCertificates;
	}

	public void setTblCertificates(TblCertificates tblCertificates) {
		this.tblCertificates = tblCertificates;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ResourceRequestID")
	public TblResourceRequests getTblResourceRequests() {
		return this.tblResourceRequests;
	}

	public void setTblResourceRequests(TblResourceRequests tblResourceRequests) {
		this.tblResourceRequests = tblResourceRequests;
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
