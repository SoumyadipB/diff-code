package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblWorkOrderBookingDetailsBeforedelete17062018 generated by hbm2java
 */
@Entity
@Table(name = "TBL_WORK_ORDER_BOOKING_DETAILS_Beforedelete17062018", schema = "transactionalData")
public class TblWorkOrderBookingDetailsBeforedelete17062018 implements java.io.Serializable {

	private TblWorkOrderBookingDetailsBeforedelete17062018Id id;

	public TblWorkOrderBookingDetailsBeforedelete17062018() {
	}

	public TblWorkOrderBookingDetailsBeforedelete17062018(TblWorkOrderBookingDetailsBeforedelete17062018Id id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "bookingId", column = @Column(name = "BookingID", nullable = false)),
			@AttributeOverride(name = "woid", column = @Column(name = "WOID")),
			@AttributeOverride(name = "taskId", column = @Column(name = "TaskID")),
			@AttributeOverride(name = "startDate", column = @Column(name = "StartDate", length = 23)),
			@AttributeOverride(name = "endDate", column = @Column(name = "EndDate", length = 23)),
			@AttributeOverride(name = "hours", column = @Column(name = "Hours", precision = 53, scale = 0)),
			@AttributeOverride(name = "parentBookingDetailsId", column = @Column(name = "ParentBookingDetailsID")),
			@AttributeOverride(name = "type", column = @Column(name = "Type", length = 512)),
			@AttributeOverride(name = "status", column = @Column(name = "Status", length = 512)),
			@AttributeOverride(name = "signumId", column = @Column(name = "SignumID", length = 512)),
			@AttributeOverride(name = "reason", column = @Column(name = "Reason", length = 1024)),
			@AttributeOverride(name = "outputLink", column = @Column(name = "OutputLink")),
			@AttributeOverride(name = "parallelcount", column = @Column(name = "parallelcount")) })
	public TblWorkOrderBookingDetailsBeforedelete17062018Id getId() {
		return this.id;
	}

	public void setId(TblWorkOrderBookingDetailsBeforedelete17062018Id id) {
		this.id = id;
	}

}
