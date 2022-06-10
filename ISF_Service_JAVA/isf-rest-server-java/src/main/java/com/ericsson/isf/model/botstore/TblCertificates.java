package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

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
 * TblCertificates generated by hbm2java
 */
@Entity
@Table(name = "TBL_Certificates", schema = "refData")
public class TblCertificates implements java.io.Serializable {

	private int certificateId;
	private String certificateName;
	private String issuer;
	private String certificateType;
	private String createdBy;
	private Date createdOn;
	private String lastModifiedBy;
	private Date lastModifiedOn;
	private Boolean active;
	private int certificateextId;
	private String shortName;
	private String description;
	private Set<TblResourceRequestCertifications> tblResourceRequestCertificationses = new HashSet<TblResourceRequestCertifications>(
			0);

	public TblCertificates() {
	}

	public TblCertificates(int certificateId, Date createdOn, Date lastModifiedOn, int certificateextId) {
		this.certificateId = certificateId;
		this.createdOn = createdOn;
		this.lastModifiedOn = lastModifiedOn;
		this.certificateextId = certificateextId;
	}

	public TblCertificates(int certificateId, String certificateName, String issuer, String certificateType,
			String createdBy, Date createdOn, String lastModifiedBy, Date lastModifiedOn, Boolean active,
			int certificateextId, String shortName, String description,
			Set<TblResourceRequestCertifications> tblResourceRequestCertificationses) {
		this.certificateId = certificateId;
		this.certificateName = certificateName;
		this.issuer = issuer;
		this.certificateType = certificateType;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedOn = lastModifiedOn;
		this.active = active;
		this.certificateextId = certificateextId;
		this.shortName = shortName;
		this.description = description;
		this.tblResourceRequestCertificationses = tblResourceRequestCertificationses;
	}

	@Id

	@Column(name = "CertificateID", unique = true, nullable = false)
	public int getCertificateId() {
		return this.certificateId;
	}

	public void setCertificateId(int certificateId) {
		this.certificateId = certificateId;
	}

	@Column(name = "CertificateName", length = 250)
	public String getCertificateName() {
		return this.certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	@Column(name = "Issuer", length = 250)
	public String getIssuer() {
		return this.issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	@Column(name = "CertificateType", length = 250)
	public String getCertificateType() {
		return this.certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	@Column(name = "CreatedBy", length = 250)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
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

	@Column(name = "LastModifiedBy", length = 250)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
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

	@Column(name = "Active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "CERTIFICATEExtID", nullable = false)
	public int getCertificateextId() {
		return this.certificateextId;
	}

	public void setCertificateextId(int certificateextId) {
		this.certificateextId = certificateextId;
	}

	@Column(name = "ShortName", length = 250)
	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblCertificates")
	public Set<TblResourceRequestCertifications> getTblResourceRequestCertificationses() {
		return this.tblResourceRequestCertificationses;
	}

	public void setTblResourceRequestCertificationses(
			Set<TblResourceRequestCertifications> tblResourceRequestCertificationses) {
		this.tblResourceRequestCertificationses = tblResourceRequestCertificationses;
	}

}
