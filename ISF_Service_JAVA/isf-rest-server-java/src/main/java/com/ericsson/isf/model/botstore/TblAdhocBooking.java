package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

/**
 * TblAdhocBooking generated by hbm2java
 */
@Entity
@Table(name = "TBL_ADHOC_BOOKING", schema = "transactionalData")
public class TblAdhocBooking implements java.io.Serializable {

	private int adhocBookingId;
	private TblProjects tblProjects;
	private TblAdhocBookingActivity tblAdhocBookingActivity;
	private String signumID;
	private Date startDate;
	private Date plannedEndDate;
	private Date actualEndDate;
	private String plannedDuration;
	private String bookedDuration;
	private String status;
	private String pausedBooking;
	private String comment;
	private String createdBy;
	private Date createdOn=new Date();
	private String lastModifiedBy;
	private Date lastModifiedOn=new Date();
	private String outlookMeetingId;
	private boolean active;
	
	private String refferer;
	
	
	public TblAdhocBooking() {
	}

	public TblAdhocBooking(int adhocBookingId, TblAdhocBookingActivity tblAdhocBookingActivity, String signumID, String plannedDuration,String bookedDuration,
			String pausedBooking, String comment, String status) {
		this.adhocBookingId = adhocBookingId;
		this.tblAdhocBookingActivity = tblAdhocBookingActivity;
		this.signumID = signumID;
		this.plannedDuration = plannedDuration;
		this.bookedDuration= bookedDuration;
		this.pausedBooking = pausedBooking;
		this.comment = comment;
		this.status = status;
	}

	
	
	@Transient
	public String getRefferer() {
		return refferer;
	}

	public void setRefferer(String refferer) {
		this.refferer = refferer;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "ADHOCBOOKINGID", unique = true, nullable = false)
	public int getAdhocBookingId() {
		return this.adhocBookingId;
	}

	public void setAdhocBookingId(int adhocBookingId) {
		this.adhocBookingId = adhocBookingId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ProjectID", nullable = true)
	public TblProjects getTblProjects() {
		return this.tblProjects;
	}

	public void setTblProjects(TblProjects tblProjects) {
		this.tblProjects = tblProjects;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADHOCBOOKINGACTIVITYID", nullable = false)
	public TblAdhocBookingActivity getTblAdhocBookingActivity() {
		return this.tblAdhocBookingActivity;
	}

	public void setTblAdhocBookingActivity(TblAdhocBookingActivity tblAdhocBookingActivity) {
		this.tblAdhocBookingActivity = tblAdhocBookingActivity;
	}

	@Column(name = "SignumID", nullable = false, length = 128)
	public String getSignumID() {
		return this.signumID;
	}

	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}

	@Column(name = "PlannedDuration", nullable = true, length = 40)
	public String getPlannedDuration() {
		return this.plannedDuration;
	}

	public void setPlannedDuration(String plannedDuration) {
		this.plannedDuration = plannedDuration;
	}

	@Column(name = "BookedDuration", nullable = true, length = 40)
	public String getBookedDuration() {
		return this.bookedDuration;
	}

	public void setBookedDuration(String bookedDuration) {
		this.bookedDuration = bookedDuration;
	}

	@Column(name = "PausedBooking", nullable = true, length = 1000)
	public String getPausedBooking() {
		return this.pausedBooking;
	}

	public void setPausedBooking(String pausedBooking) {
		this.pausedBooking = pausedBooking;
	}

	@Column(name = "Comment", nullable = true, length = 1000)
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "Status", nullable = false, length = 20)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "CreatedBY", nullable = true, length = 128)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "StartDate", length = 23)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PlannedEndDate", length = 23)
	public Date getPlannedEndDate() {
		return this.plannedEndDate;
	}

	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ActualEndDate", length = 23)
	public Date getActualEndDate() {
		return this.actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	
	@Column(name = "LastModifiedBy", nullable = true, length = 128)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LastModifiedOn", nullable = true, length = 23)
	public Date getLastModifiedOn() {
		return this.lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedOn", nullable = true, length = 23)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	@Column(name = "outlookMeetingId", nullable = true, length = 300)
	public String getOutlookMeetingId() {
		return outlookMeetingId;
	}

	public void setOutlookMeetingId(String outlookMeetingId) {
		this.outlookMeetingId = outlookMeetingId;
	}
	
}