package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TblChangeRequestPosition generated by hbm2java
 */
@Entity
@Table(name = "TBL_ChangeRequestPosition", schema = "transactionalData")
public class TblChangeRequestPosition implements java.io.Serializable {

	private int crid;
	private Integer resourcePositionId;
	private Integer resourceRequestId;
	private String createdBy;
	private Date createdOn;
	private String status;
	private String comments;
	private String actionType;
	private String positionStatus;
	private Integer workEffortIdExisting;
	private Integer workEffortIdProposed;
	private String actionTakenBy;
	private Date actionTakenon;

	public TblChangeRequestPosition() {
	}

	public TblChangeRequestPosition(int crid, String createdBy, Date createdOn) {
		this.crid = crid;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	public TblChangeRequestPosition(int crid, Integer resourcePositionId, Integer resourceRequestId, String createdBy,
			Date createdOn, String status, String comments, String actionType, String positionStatus,
			Integer workEffortIdExisting, Integer workEffortIdProposed, String actionTakenBy, Date actionTakenon) {
		this.crid = crid;
		this.resourcePositionId = resourcePositionId;
		this.resourceRequestId = resourceRequestId;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.status = status;
		this.comments = comments;
		this.actionType = actionType;
		this.positionStatus = positionStatus;
		this.workEffortIdExisting = workEffortIdExisting;
		this.workEffortIdProposed = workEffortIdProposed;
		this.actionTakenBy = actionTakenBy;
		this.actionTakenon = actionTakenon;
	}

	@Id

	@Column(name = "CRID", unique = true, nullable = false)
	public int getCrid() {
		return this.crid;
	}

	public void setCrid(int crid) {
		this.crid = crid;
	}

	@Column(name = "ResourcePositionID")
	public Integer getResourcePositionId() {
		return this.resourcePositionId;
	}

	public void setResourcePositionId(Integer resourcePositionId) {
		this.resourcePositionId = resourcePositionId;
	}

	@Column(name = "ResourceRequestID")
	public Integer getResourceRequestId() {
		return this.resourceRequestId;
	}

	public void setResourceRequestId(Integer resourceRequestId) {
		this.resourceRequestId = resourceRequestId;
	}

	@Column(name = "CreatedBy", nullable = false, length = 8)
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

	@Column(name = "Status", length = 50)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "Comments")
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name = "ActionType", length = 50)
	public String getActionType() {
		return this.actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Column(name = "PositionStatus", length = 50)
	public String getPositionStatus() {
		return this.positionStatus;
	}

	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}

	@Column(name = "WorkEffortIdExisting")
	public Integer getWorkEffortIdExisting() {
		return this.workEffortIdExisting;
	}

	public void setWorkEffortIdExisting(Integer workEffortIdExisting) {
		this.workEffortIdExisting = workEffortIdExisting;
	}

	@Column(name = "WorkEffortIdProposed")
	public Integer getWorkEffortIdProposed() {
		return this.workEffortIdProposed;
	}

	public void setWorkEffortIdProposed(Integer workEffortIdProposed) {
		this.workEffortIdProposed = workEffortIdProposed;
	}

	@Column(name = "ActionTakenBy", length = 8)
	public String getActionTakenBy() {
		return this.actionTakenBy;
	}

	public void setActionTakenBy(String actionTakenBy) {
		this.actionTakenBy = actionTakenBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ActionTakenon", length = 23)
	public Date getActionTakenon() {
		return this.actionTakenon;
	}

	public void setActionTakenon(Date actionTakenon) {
		this.actionTakenon = actionTakenon;
	}

}
