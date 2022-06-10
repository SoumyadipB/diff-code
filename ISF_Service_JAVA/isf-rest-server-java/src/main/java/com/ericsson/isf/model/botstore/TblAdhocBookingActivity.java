package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TblAdhocBookingActivity generated by hbm2java
 */
@Entity
@Table(name = "TBL_ADHOC_BOOKING_ACTIVITY", schema = "refData")
public class TblAdhocBookingActivity implements java.io.Serializable {

	private int adhocBookingActivityId;
	private String type;
	private String activity;
	private String description;
	private String status;
	private Set<TblAdhocBooking> tblAdhocBooking= new HashSet<TblAdhocBooking>(0);

	public TblAdhocBookingActivity() {
	}

	public TblAdhocBookingActivity(int adhocBookingActivityId, 
			String type, String activity, String description, 
			String status, String createdBy, Date createdOn, int isActive, Set<TblAdhocBooking> tblAdhocBooking) {
		this.adhocBookingActivityId = adhocBookingActivityId;
		this.type = type;
		this.activity = activity;
		this.description = description;
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "ADHOCBOOKINGACTIVITYID", unique = true, nullable = false)
	public int getAdhocBookingActivityId() {
		return this.adhocBookingActivityId;
	}

	public void setAdhocBookingActivityId(int adhocBookingActivityId) {
		this.adhocBookingActivityId = adhocBookingActivityId;
	}

	@Column(name = "Activity", nullable = false, length = 255)
	public String getActivity() {
		return this.activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	@Column(name = "Type", nullable = false, length = 50)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "Description", nullable = true, length = 255)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "Status", nullable = true, length = 255)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblAdhocBookingActivity")
	public Set<TblAdhocBooking> getTblAdhocBooking() {
		return this.tblAdhocBooking;
	}

	public void setTblAdhocBooking(Set<TblAdhocBooking> tblAdhocBooking) {
		this.tblAdhocBooking = tblAdhocBooking;
	}

}