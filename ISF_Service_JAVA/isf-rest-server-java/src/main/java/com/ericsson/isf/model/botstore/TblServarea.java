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
 * TblServarea generated by hbm2java
 */
@Entity
@Table(name = "TBL_SERVAREA", schema = "refData")
public class TblServarea implements java.io.Serializable {

	private int servAreaId;
	private String serviceArea;
	private Boolean active;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Serializable pcode;
	private Set<TblServicearea> tblServiceareas = new HashSet<TblServicearea>(0);

	public TblServarea() {
	}

	public TblServarea(int servAreaId) {
		this.servAreaId = servAreaId;
	}

	public TblServarea(int servAreaId, String serviceArea, Boolean active, String createdBy, Date createdDate,
			String lastModifiedBy, Date lastModifiedDate, Serializable pcode, Set<TblServicearea> tblServiceareas) {
		this.servAreaId = servAreaId;
		this.serviceArea = serviceArea;
		this.active = active;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.pcode = pcode;
		this.tblServiceareas = tblServiceareas;
	}

	@Id

	@Column(name = "ServAreaID", unique = true, nullable = false)
	public int getServAreaId() {
		return this.servAreaId;
	}

	public void setServAreaId(int servAreaId) {
		this.servAreaId = servAreaId;
	}

	@Column(name = "ServiceArea", length = 128)
	public String getServiceArea() {
		return this.serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	@Column(name = "Active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "CreatedBy", length = 128)
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

	@Column(name = "LastModifiedBy", length = 128)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
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

	@Column(name = "PCode")
	public Serializable getPcode() {
		return this.pcode;
	}

	public void setPcode(Serializable pcode) {
		this.pcode = pcode;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblServarea")
	public Set<TblServicearea> getTblServiceareas() {
		return this.tblServiceareas;
	}

	public void setTblServiceareas(Set<TblServicearea> tblServiceareas) {
		this.tblServiceareas = tblServiceareas;
	}

}
