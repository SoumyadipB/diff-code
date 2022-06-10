package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblWorkOrderRpaExecutionLogId generated by hbm2java
 */
@Embeddable
public class TblWorkOrderRpaExecutionLogId implements java.io.Serializable {

	private int logId;
	private Integer woId;
	private Integer taskId;
	private Integer bookingId;
	private String signumId;
	private String status;
	private Date bookedOn;

	public TblWorkOrderRpaExecutionLogId() {
	}

	public TblWorkOrderRpaExecutionLogId(int logId) {
		this.logId = logId;
	}

	public TblWorkOrderRpaExecutionLogId(int logId, Integer woId, Integer taskId, Integer bookingId, String signumId,
			String status, Date bookedOn) {
		this.logId = logId;
		this.woId = woId;
		this.taskId = taskId;
		this.bookingId = bookingId;
		this.signumId = signumId;
		this.status = status;
		this.bookedOn = bookedOn;
	}

	@Column(name = "LogID", nullable = false)
	public int getLogId() {
		return this.logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	@Column(name = "WoID")
	public Integer getWoId() {
		return this.woId;
	}

	public void setWoId(Integer woId) {
		this.woId = woId;
	}

	@Column(name = "TaskID")
	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	@Column(name = "BookingID")
	public Integer getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	@Column(name = "SignumID", length = 1024)
	public String getSignumId() {
		return this.signumId;
	}

	public void setSignumId(String signumId) {
		this.signumId = signumId;
	}

	@Column(name = "Status", length = 1024)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "BookedOn", length = 23)
	public Date getBookedOn() {
		return this.bookedOn;
	}

	public void setBookedOn(Date bookedOn) {
		this.bookedOn = bookedOn;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblWorkOrderRpaExecutionLogId))
			return false;
		TblWorkOrderRpaExecutionLogId castOther = (TblWorkOrderRpaExecutionLogId) other;

		return (this.getLogId() == castOther.getLogId())
				&& ((this.getWoId() == castOther.getWoId()) || (this.getWoId() != null && castOther.getWoId() != null
						&& this.getWoId().equals(castOther.getWoId())))
				&& ((this.getTaskId() == castOther.getTaskId()) || (this.getTaskId() != null
						&& castOther.getTaskId() != null && this.getTaskId().equals(castOther.getTaskId())))
				&& ((this.getBookingId() == castOther.getBookingId()) || (this.getBookingId() != null
						&& castOther.getBookingId() != null && this.getBookingId().equals(castOther.getBookingId())))
				&& ((this.getSignumId() == castOther.getSignumId()) || (this.getSignumId() != null
						&& castOther.getSignumId() != null && this.getSignumId().equals(castOther.getSignumId())))
				&& ((this.getStatus() == castOther.getStatus()) || (this.getStatus() != null
						&& castOther.getStatus() != null && this.getStatus().equals(castOther.getStatus())))
				&& ((this.getBookedOn() == castOther.getBookedOn()) || (this.getBookedOn() != null
						&& castOther.getBookedOn() != null && this.getBookedOn().equals(castOther.getBookedOn())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getLogId();
		result = 37 * result + (getWoId() == null ? 0 : this.getWoId().hashCode());
		result = 37 * result + (getTaskId() == null ? 0 : this.getTaskId().hashCode());
		result = 37 * result + (getBookingId() == null ? 0 : this.getBookingId().hashCode());
		result = 37 * result + (getSignumId() == null ? 0 : this.getSignumId().hashCode());
		result = 37 * result + (getStatus() == null ? 0 : this.getStatus().hashCode());
		result = 37 * result + (getBookedOn() == null ? 0 : this.getBookedOn().hashCode());
		return result;
	}

}