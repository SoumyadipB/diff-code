package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblDomainSpocBackupId generated by hbm2java
 */
@Embeddable
public class TblDomainSpocBackupId implements java.io.Serializable {

	private int domainSpocId;
	private int marketAreaId;
	private int domainId;
	private int servAreaId;
	private String spoc;
	private String spocLevel;
	private boolean active;
	private String createdBy;
	private Date createdDate;

	public TblDomainSpocBackupId() {
	}

	public TblDomainSpocBackupId(int domainSpocId, int marketAreaId, int domainId, int servAreaId, String spoc,
			String spocLevel, boolean active, String createdBy, Date createdDate) {
		this.domainSpocId = domainSpocId;
		this.marketAreaId = marketAreaId;
		this.domainId = domainId;
		this.servAreaId = servAreaId;
		this.spoc = spoc;
		this.spocLevel = spocLevel;
		this.active = active;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
	}

	@Column(name = "DomainSpocID", nullable = false)
	public int getDomainSpocId() {
		return this.domainSpocId;
	}

	public void setDomainSpocId(int domainSpocId) {
		this.domainSpocId = domainSpocId;
	}

	@Column(name = "MarketAreaID", nullable = false)
	public int getMarketAreaId() {
		return this.marketAreaId;
	}

	public void setMarketAreaId(int marketAreaId) {
		this.marketAreaId = marketAreaId;
	}

	@Column(name = "DomainID", nullable = false)
	public int getDomainId() {
		return this.domainId;
	}

	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}

	@Column(name = "ServAreaID", nullable = false)
	public int getServAreaId() {
		return this.servAreaId;
	}

	public void setServAreaId(int servAreaId) {
		this.servAreaId = servAreaId;
	}

	@Column(name = "Spoc", nullable = false, length = 250)
	public String getSpoc() {
		return this.spoc;
	}

	public void setSpoc(String spoc) {
		this.spoc = spoc;
	}

	@Column(name = "SpocLevel", nullable = false, length = 20)
	public String getSpocLevel() {
		return this.spocLevel;
	}

	public void setSpocLevel(String spocLevel) {
		this.spocLevel = spocLevel;
	}

	@Column(name = "Active", nullable = false)
	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "CreatedBy", nullable = false, length = 7)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CreatedDate", nullable = false, length = 23)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblDomainSpocBackupId))
			return false;
		TblDomainSpocBackupId castOther = (TblDomainSpocBackupId) other;

		return (this.getDomainSpocId() == castOther.getDomainSpocId())
				&& (this.getMarketAreaId() == castOther.getMarketAreaId())
				&& (this.getDomainId() == castOther.getDomainId())
				&& (this.getServAreaId() == castOther.getServAreaId())
				&& ((this.getSpoc() == castOther.getSpoc()) || (this.getSpoc() != null && castOther.getSpoc() != null
						&& this.getSpoc().equals(castOther.getSpoc())))
				&& ((this.getSpocLevel() == castOther.getSpocLevel()) || (this.getSpocLevel() != null
						&& castOther.getSpocLevel() != null && this.getSpocLevel().equals(castOther.getSpocLevel())))
				&& (this.isActive() == castOther.isActive())
				&& ((this.getCreatedBy() == castOther.getCreatedBy()) || (this.getCreatedBy() != null
						&& castOther.getCreatedBy() != null && this.getCreatedBy().equals(castOther.getCreatedBy())))
				&& ((this.getCreatedDate() == castOther.getCreatedDate())
						|| (this.getCreatedDate() != null && castOther.getCreatedDate() != null
								&& this.getCreatedDate().equals(castOther.getCreatedDate())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getDomainSpocId();
		result = 37 * result + this.getMarketAreaId();
		result = 37 * result + this.getDomainId();
		result = 37 * result + this.getServAreaId();
		result = 37 * result + (getSpoc() == null ? 0 : this.getSpoc().hashCode());
		result = 37 * result + (getSpocLevel() == null ? 0 : this.getSpocLevel().hashCode());
		result = 37 * result + (this.isActive() ? 1 : 0);
		result = 37 * result + (getCreatedBy() == null ? 0 : this.getCreatedBy().hashCode());
		result = 37 * result + (getCreatedDate() == null ? 0 : this.getCreatedDate().hashCode());
		return result;
	}

}
