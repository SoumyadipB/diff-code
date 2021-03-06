package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblDeliveryResponsibleId generated by hbm2java
 */
@Embeddable
public class TblDeliveryResponsibleId implements java.io.Serializable {

	private int deliveryResponsibleId;
	private Integer projectId;
	private String deliveryResponsible;
	private Boolean active;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String signumId;

	public TblDeliveryResponsibleId() {
	}

	public TblDeliveryResponsibleId(int deliveryResponsibleId) {
		this.deliveryResponsibleId = deliveryResponsibleId;
	}

	public TblDeliveryResponsibleId(int deliveryResponsibleId, Integer projectId, String deliveryResponsible,
			Boolean active, String createdBy, Date createdDate, String lastModifiedBy, Date lastModifiedDate,
			String signumId) {
		this.deliveryResponsibleId = deliveryResponsibleId;
		this.projectId = projectId;
		this.deliveryResponsible = deliveryResponsible;
		this.active = active;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.signumId = signumId;
	}

	@Column(name = "DeliveryResponsibleID", nullable = false)
	public int getDeliveryResponsibleId() {
		return this.deliveryResponsibleId;
	}

	public void setDeliveryResponsibleId(int deliveryResponsibleId) {
		this.deliveryResponsibleId = deliveryResponsibleId;
	}

	@Column(name = "ProjectID")
	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Column(name = "DeliveryResponsible", length = 512)
	public String getDeliveryResponsible() {
		return this.deliveryResponsible;
	}

	public void setDeliveryResponsible(String deliveryResponsible) {
		this.deliveryResponsible = deliveryResponsible;
	}

	@Column(name = "Active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "CreatedBy", length = 512)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CreatedDate", length = 23)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "LastModifiedBy", length = 512)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Column(name = "LastModifiedDate", length = 23)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "SignumID", length = 512)
	public String getSignumId() {
		return this.signumId;
	}

	public void setSignumId(String signumId) {
		this.signumId = signumId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblDeliveryResponsibleId))
			return false;
		TblDeliveryResponsibleId castOther = (TblDeliveryResponsibleId) other;

		return (this.getDeliveryResponsibleId() == castOther.getDeliveryResponsibleId())
				&& ((this.getProjectId() == castOther.getProjectId()) || (this.getProjectId() != null
						&& castOther.getProjectId() != null && this.getProjectId().equals(castOther.getProjectId())))
				&& ((this.getDeliveryResponsible() == castOther.getDeliveryResponsible())
						|| (this.getDeliveryResponsible() != null && castOther.getDeliveryResponsible() != null
								&& this.getDeliveryResponsible().equals(castOther.getDeliveryResponsible())))
				&& ((this.getActive() == castOther.getActive()) || (this.getActive() != null
						&& castOther.getActive() != null && this.getActive().equals(castOther.getActive())))
				&& ((this.getCreatedBy() == castOther.getCreatedBy()) || (this.getCreatedBy() != null
						&& castOther.getCreatedBy() != null && this.getCreatedBy().equals(castOther.getCreatedBy())))
				&& ((this.getCreatedDate() == castOther.getCreatedDate())
						|| (this.getCreatedDate() != null && castOther.getCreatedDate() != null
								&& this.getCreatedDate().equals(castOther.getCreatedDate())))
				&& ((this.getLastModifiedBy() == castOther.getLastModifiedBy())
						|| (this.getLastModifiedBy() != null && castOther.getLastModifiedBy() != null
								&& this.getLastModifiedBy().equals(castOther.getLastModifiedBy())))
				&& ((this.getLastModifiedDate() == castOther.getLastModifiedDate())
						|| (this.getLastModifiedDate() != null && castOther.getLastModifiedDate() != null
								&& this.getLastModifiedDate().equals(castOther.getLastModifiedDate())))
				&& ((this.getSignumId() == castOther.getSignumId()) || (this.getSignumId() != null
						&& castOther.getSignumId() != null && this.getSignumId().equals(castOther.getSignumId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getDeliveryResponsibleId();
		result = 37 * result + (getProjectId() == null ? 0 : this.getProjectId().hashCode());
		result = 37 * result + (getDeliveryResponsible() == null ? 0 : this.getDeliveryResponsible().hashCode());
		result = 37 * result + (getActive() == null ? 0 : this.getActive().hashCode());
		result = 37 * result + (getCreatedBy() == null ? 0 : this.getCreatedBy().hashCode());
		result = 37 * result + (getCreatedDate() == null ? 0 : this.getCreatedDate().hashCode());
		result = 37 * result + (getLastModifiedBy() == null ? 0 : this.getLastModifiedBy().hashCode());
		result = 37 * result + (getLastModifiedDate() == null ? 0 : this.getLastModifiedDate().hashCode());
		result = 37 * result + (getSignumId() == null ? 0 : this.getSignumId().hashCode());
		return result;
	}

}
