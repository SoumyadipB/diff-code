package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblWorkOrderDeliveryAcceptanceId generated by hbm2java
 */
@Embeddable
public class TblWorkOrderDeliveryAcceptanceId implements java.io.Serializable {

	private int deliveryAcceptanceId;
	private Integer woid;
	private String woname;
	private String rating;
	private String acceptance;
	private String deliveryStatus;
	private String reason;
	private String comment;
	private String acceptedOrRejectedBy;
	private Date acceptedOrRejectedDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;

	public TblWorkOrderDeliveryAcceptanceId() {
	}

	public TblWorkOrderDeliveryAcceptanceId(int deliveryAcceptanceId) {
		this.deliveryAcceptanceId = deliveryAcceptanceId;
	}

	public TblWorkOrderDeliveryAcceptanceId(int deliveryAcceptanceId, Integer woid, String woname, String rating,
			String acceptance, String deliveryStatus, String reason, String comment, String acceptedOrRejectedBy,
			Date acceptedOrRejectedDate, String lastModifiedBy, Date lastModifiedDate) {
		this.deliveryAcceptanceId = deliveryAcceptanceId;
		this.woid = woid;
		this.woname = woname;
		this.rating = rating;
		this.acceptance = acceptance;
		this.deliveryStatus = deliveryStatus;
		this.reason = reason;
		this.comment = comment;
		this.acceptedOrRejectedBy = acceptedOrRejectedBy;
		this.acceptedOrRejectedDate = acceptedOrRejectedDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "DeliveryAcceptanceID", nullable = false)
	public int getDeliveryAcceptanceId() {
		return this.deliveryAcceptanceId;
	}

	public void setDeliveryAcceptanceId(int deliveryAcceptanceId) {
		this.deliveryAcceptanceId = deliveryAcceptanceId;
	}

	@Column(name = "WOID")
	public Integer getWoid() {
		return this.woid;
	}

	public void setWoid(Integer woid) {
		this.woid = woid;
	}

	@Column(name = "WOName", length = 1024)
	public String getWoname() {
		return this.woname;
	}

	public void setWoname(String woname) {
		this.woname = woname;
	}

	@Column(name = "Rating", length = 512)
	public String getRating() {
		return this.rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@Column(name = "Acceptance", length = 1024)
	public String getAcceptance() {
		return this.acceptance;
	}

	public void setAcceptance(String acceptance) {
		this.acceptance = acceptance;
	}

	@Column(name = "DeliveryStatus", length = 512)
	public String getDeliveryStatus() {
		return this.deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	@Column(name = "Reason", length = 1024)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "Comment", length = 500)
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "AcceptedOrRejectedBy", length = 512)
	public String getAcceptedOrRejectedBy() {
		return this.acceptedOrRejectedBy;
	}

	public void setAcceptedOrRejectedBy(String acceptedOrRejectedBy) {
		this.acceptedOrRejectedBy = acceptedOrRejectedBy;
	}

	@Column(name = "AcceptedOrRejectedDate", length = 23)
	public Date getAcceptedOrRejectedDate() {
		return this.acceptedOrRejectedDate;
	}

	public void setAcceptedOrRejectedDate(Date acceptedOrRejectedDate) {
		this.acceptedOrRejectedDate = acceptedOrRejectedDate;
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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblWorkOrderDeliveryAcceptanceId))
			return false;
		TblWorkOrderDeliveryAcceptanceId castOther = (TblWorkOrderDeliveryAcceptanceId) other;

		return (this.getDeliveryAcceptanceId() == castOther.getDeliveryAcceptanceId())
				&& ((this.getWoid() == castOther.getWoid()) || (this.getWoid() != null && castOther.getWoid() != null
						&& this.getWoid().equals(castOther.getWoid())))
				&& ((this.getWoname() == castOther.getWoname()) || (this.getWoname() != null
						&& castOther.getWoname() != null && this.getWoname().equals(castOther.getWoname())))
				&& ((this.getRating() == castOther.getRating()) || (this.getRating() != null
						&& castOther.getRating() != null && this.getRating().equals(castOther.getRating())))
				&& ((this.getAcceptance() == castOther.getAcceptance()) || (this.getAcceptance() != null
						&& castOther.getAcceptance() != null && this.getAcceptance().equals(castOther.getAcceptance())))
				&& ((this.getDeliveryStatus() == castOther.getDeliveryStatus())
						|| (this.getDeliveryStatus() != null && castOther.getDeliveryStatus() != null
								&& this.getDeliveryStatus().equals(castOther.getDeliveryStatus())))
				&& ((this.getReason() == castOther.getReason()) || (this.getReason() != null
						&& castOther.getReason() != null && this.getReason().equals(castOther.getReason())))
				&& ((this.getComment() == castOther.getComment()) || (this.getComment() != null
						&& castOther.getComment() != null && this.getComment().equals(castOther.getComment())))
				&& ((this.getAcceptedOrRejectedBy() == castOther.getAcceptedOrRejectedBy())
						|| (this.getAcceptedOrRejectedBy() != null && castOther.getAcceptedOrRejectedBy() != null
								&& this.getAcceptedOrRejectedBy().equals(castOther.getAcceptedOrRejectedBy())))
				&& ((this.getAcceptedOrRejectedDate() == castOther.getAcceptedOrRejectedDate())
						|| (this.getAcceptedOrRejectedDate() != null && castOther.getAcceptedOrRejectedDate() != null
								&& this.getAcceptedOrRejectedDate().equals(castOther.getAcceptedOrRejectedDate())))
				&& ((this.getLastModifiedBy() == castOther.getLastModifiedBy())
						|| (this.getLastModifiedBy() != null && castOther.getLastModifiedBy() != null
								&& this.getLastModifiedBy().equals(castOther.getLastModifiedBy())))
				&& ((this.getLastModifiedDate() == castOther.getLastModifiedDate())
						|| (this.getLastModifiedDate() != null && castOther.getLastModifiedDate() != null
								&& this.getLastModifiedDate().equals(castOther.getLastModifiedDate())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getDeliveryAcceptanceId();
		result = 37 * result + (getWoid() == null ? 0 : this.getWoid().hashCode());
		result = 37 * result + (getWoname() == null ? 0 : this.getWoname().hashCode());
		result = 37 * result + (getRating() == null ? 0 : this.getRating().hashCode());
		result = 37 * result + (getAcceptance() == null ? 0 : this.getAcceptance().hashCode());
		result = 37 * result + (getDeliveryStatus() == null ? 0 : this.getDeliveryStatus().hashCode());
		result = 37 * result + (getReason() == null ? 0 : this.getReason().hashCode());
		result = 37 * result + (getComment() == null ? 0 : this.getComment().hashCode());
		result = 37 * result + (getAcceptedOrRejectedBy() == null ? 0 : this.getAcceptedOrRejectedBy().hashCode());
		result = 37 * result + (getAcceptedOrRejectedDate() == null ? 0 : this.getAcceptedOrRejectedDate().hashCode());
		result = 37 * result + (getLastModifiedBy() == null ? 0 : this.getLastModifiedBy().hashCode());
		result = 37 * result + (getLastModifiedDate() == null ? 0 : this.getLastModifiedDate().hashCode());
		return result;
	}

}
