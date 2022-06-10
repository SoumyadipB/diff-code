package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblWorkOrderBookingDetailsUpdatestatus27062018Id generated by hbm2java
 */
@Embeddable
public class TblWorkOrderBookingDetailsUpdatestatus27062018Id implements java.io.Serializable {

	private int bookingId;
	private Integer woid;
	private Integer taskId;
	private Date startDate;
	private Date endDate;
	private Double hours;
	private Integer parentBookingDetailsId;
	private String type;
	private String status;
	private String signumId;
	private String reason;
	private String outputLink;
	private Integer parallelcount;

	public TblWorkOrderBookingDetailsUpdatestatus27062018Id() {
	}

	public TblWorkOrderBookingDetailsUpdatestatus27062018Id(int bookingId) {
		this.bookingId = bookingId;
	}

	public TblWorkOrderBookingDetailsUpdatestatus27062018Id(int bookingId, Integer woid, Integer taskId, Date startDate,
			Date endDate, Double hours, Integer parentBookingDetailsId, String type, String status, String signumId,
			String reason, String outputLink, Integer parallelcount) {
		this.bookingId = bookingId;
		this.woid = woid;
		this.taskId = taskId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.hours = hours;
		this.parentBookingDetailsId = parentBookingDetailsId;
		this.type = type;
		this.status = status;
		this.signumId = signumId;
		this.reason = reason;
		this.outputLink = outputLink;
		this.parallelcount = parallelcount;
	}

	@Column(name = "BookingID", nullable = false)
	public int getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	@Column(name = "WOID")
	public Integer getWoid() {
		return this.woid;
	}

	public void setWoid(Integer woid) {
		this.woid = woid;
	}

	@Column(name = "TaskID")
	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	@Column(name = "StartDate", length = 23)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "EndDate", length = 23)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "Hours", precision = 53, scale = 0)
	public Double getHours() {
		return this.hours;
	}

	public void setHours(Double hours) {
		this.hours = hours;
	}

	@Column(name = "ParentBookingDetailsID")
	public Integer getParentBookingDetailsId() {
		return this.parentBookingDetailsId;
	}

	public void setParentBookingDetailsId(Integer parentBookingDetailsId) {
		this.parentBookingDetailsId = parentBookingDetailsId;
	}

	@Column(name = "Type", length = 512)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "Status", length = 512)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "SignumID", length = 512)
	public String getSignumId() {
		return this.signumId;
	}

	public void setSignumId(String signumId) {
		this.signumId = signumId;
	}

	@Column(name = "Reason", length = 1024)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "OutputLink")
	public String getOutputLink() {
		return this.outputLink;
	}

	public void setOutputLink(String outputLink) {
		this.outputLink = outputLink;
	}

	@Column(name = "parallelcount")
	public Integer getParallelcount() {
		return this.parallelcount;
	}

	public void setParallelcount(Integer parallelcount) {
		this.parallelcount = parallelcount;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblWorkOrderBookingDetailsUpdatestatus27062018Id))
			return false;
		TblWorkOrderBookingDetailsUpdatestatus27062018Id castOther = (TblWorkOrderBookingDetailsUpdatestatus27062018Id) other;

		return (this.getBookingId() == castOther.getBookingId())
				&& ((this.getWoid() == castOther.getWoid()) || (this.getWoid() != null && castOther.getWoid() != null
						&& this.getWoid().equals(castOther.getWoid())))
				&& ((this.getTaskId() == castOther.getTaskId()) || (this.getTaskId() != null
						&& castOther.getTaskId() != null && this.getTaskId().equals(castOther.getTaskId())))
				&& ((this.getStartDate() == castOther.getStartDate()) || (this.getStartDate() != null
						&& castOther.getStartDate() != null && this.getStartDate().equals(castOther.getStartDate())))
				&& ((this.getEndDate() == castOther.getEndDate()) || (this.getEndDate() != null
						&& castOther.getEndDate() != null && this.getEndDate().equals(castOther.getEndDate())))
				&& ((this.getHours() == castOther.getHours()) || (this.getHours() != null
						&& castOther.getHours() != null && this.getHours().equals(castOther.getHours())))
				&& ((this.getParentBookingDetailsId() == castOther.getParentBookingDetailsId())
						|| (this.getParentBookingDetailsId() != null && castOther.getParentBookingDetailsId() != null
								&& this.getParentBookingDetailsId().equals(castOther.getParentBookingDetailsId())))
				&& ((this.getType() == castOther.getType()) || (this.getType() != null && castOther.getType() != null
						&& this.getType().equals(castOther.getType())))
				&& ((this.getStatus() == castOther.getStatus()) || (this.getStatus() != null
						&& castOther.getStatus() != null && this.getStatus().equals(castOther.getStatus())))
				&& ((this.getSignumId() == castOther.getSignumId()) || (this.getSignumId() != null
						&& castOther.getSignumId() != null && this.getSignumId().equals(castOther.getSignumId())))
				&& ((this.getReason() == castOther.getReason()) || (this.getReason() != null
						&& castOther.getReason() != null && this.getReason().equals(castOther.getReason())))
				&& ((this.getOutputLink() == castOther.getOutputLink()) || (this.getOutputLink() != null
						&& castOther.getOutputLink() != null && this.getOutputLink().equals(castOther.getOutputLink())))
				&& ((this.getParallelcount() == castOther.getParallelcount())
						|| (this.getParallelcount() != null && castOther.getParallelcount() != null
								&& this.getParallelcount().equals(castOther.getParallelcount())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getBookingId();
		result = 37 * result + (getWoid() == null ? 0 : this.getWoid().hashCode());
		result = 37 * result + (getTaskId() == null ? 0 : this.getTaskId().hashCode());
		result = 37 * result + (getStartDate() == null ? 0 : this.getStartDate().hashCode());
		result = 37 * result + (getEndDate() == null ? 0 : this.getEndDate().hashCode());
		result = 37 * result + (getHours() == null ? 0 : this.getHours().hashCode());
		result = 37 * result + (getParentBookingDetailsId() == null ? 0 : this.getParentBookingDetailsId().hashCode());
		result = 37 * result + (getType() == null ? 0 : this.getType().hashCode());
		result = 37 * result + (getStatus() == null ? 0 : this.getStatus().hashCode());
		result = 37 * result + (getSignumId() == null ? 0 : this.getSignumId().hashCode());
		result = 37 * result + (getReason() == null ? 0 : this.getReason().hashCode());
		result = 37 * result + (getOutputLink() == null ? 0 : this.getOutputLink().hashCode());
		result = 37 * result + (getParallelcount() == null ? 0 : this.getParallelcount().hashCode());
		return result;
	}

}
