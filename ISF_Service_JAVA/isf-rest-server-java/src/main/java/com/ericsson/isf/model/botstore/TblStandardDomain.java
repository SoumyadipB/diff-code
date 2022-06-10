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
 * TblStandardDomain generated by hbm2java
 */
@Entity
@Table(name = "Tbl_standardDomain", schema = "refData")
public class TblStandardDomain implements java.io.Serializable {

	private int parentDomainId;
	private String domain;
	private boolean active;
	private String createdBy;
	private Date createdOn;

	public TblStandardDomain() {
	}

	public TblStandardDomain(int parentDomainId, String domain, boolean active) {
		this.parentDomainId = parentDomainId;
		this.domain = domain;
		this.active = active;
	}

	public TblStandardDomain(int parentDomainId, String domain, boolean active, String createdBy, Date createdOn) {
		this.parentDomainId = parentDomainId;
		this.domain = domain;
		this.active = active;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	@Id

	@Column(name = "parentDomainID", unique = true, nullable = false)
	public int getParentDomainId() {
		return this.parentDomainId;
	}

	public void setParentDomainId(int parentDomainId) {
		this.parentDomainId = parentDomainId;
	}

	@Column(name = "domain", nullable = false, length = 100)
	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "active", nullable = false)
	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "createdBy", length = 7)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdOn", length = 23)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
