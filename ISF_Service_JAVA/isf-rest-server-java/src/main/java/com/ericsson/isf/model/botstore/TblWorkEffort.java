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
 * TblWorkEffort generated by hbm2java
 */
@Entity
@Table(name = "TBL_WorkEffort", schema = "transactionalData")
public class TblWorkEffort implements java.io.Serializable {

	private int workEffortId;
	private Integer resourcePositionId;
	private Date startDate;
	private Date endDate;
	private Integer duration;
	private Double ftePercent;
	private Double hours;
	private String signum;
	private String workEffortStatus;
	private Boolean isActive;
	private Integer crid;
	private String createdBy;
	private Date createdOn;
	private Date lastModifiedOn;
	private String lastModifiedBy;
	private String allocatedBy;
	private String positionStatus;
	private Set<TblBookedResource> tblBookedResources = new HashSet<TblBookedResource>(0);

	public TblWorkEffort() {
	}

	public TblWorkEffort(int workEffortId, Date startDate, Date endDate, String createdBy, Date createdOn) {
		this.workEffortId = workEffortId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	public TblWorkEffort(int workEffortId, Integer resourcePositionId, Date startDate, Date endDate, Integer duration,
			Double ftePercent, Double hours, String signum, String workEffortStatus, Boolean isActive, Integer crid,
			String createdBy, Date createdOn, Date lastModifiedOn, String lastModifiedBy, String allocatedBy,
			String positionStatus, Set<TblBookedResource> tblBookedResources) {
		this.workEffortId = workEffortId;
		this.resourcePositionId = resourcePositionId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.duration = duration;
		this.ftePercent = ftePercent;
		this.hours = hours;
		this.signum = signum;
		this.workEffortStatus = workEffortStatus;
		this.isActive = isActive;
		this.crid = crid;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.lastModifiedOn = lastModifiedOn;
		this.lastModifiedBy = lastModifiedBy;
		this.allocatedBy = allocatedBy;
		this.positionStatus = positionStatus;
		this.tblBookedResources = tblBookedResources;
	}

	@Id

	@Column(name = "WorkEffortID", unique = true, nullable = false)
	public int getWorkEffortId() {
		return this.workEffortId;
	}

	public void setWorkEffortId(int workEffortId) {
		this.workEffortId = workEffortId;
	}

	@Column(name = "ResourcePositionID")
	public Integer getResourcePositionId() {
		return this.resourcePositionId;
	}

	public void setResourcePositionId(Integer resourcePositionId) {
		this.resourcePositionId = resourcePositionId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "StartDate", nullable = false, length = 10)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EndDate", nullable = false, length = 10)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "Duration")
	public Integer getDuration() {
		return this.duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@Column(name = "FTE_Percent", precision = 53, scale = 0)
	public Double getFtePercent() {
		return this.ftePercent;
	}

	public void setFtePercent(Double ftePercent) {
		this.ftePercent = ftePercent;
	}

	@Column(name = "Hours", precision = 53, scale = 0)
	public Double getHours() {
		return this.hours;
	}

	public void setHours(Double hours) {
		this.hours = hours;
	}

	@Column(name = "Signum", length = 8)
	public String getSignum() {
		return this.signum;
	}

	public void setSignum(String signum) {
		this.signum = signum;
	}

	@Column(name = "WorkEffortStatus", length = 100)
	public String getWorkEffortStatus() {
		return this.workEffortStatus;
	}

	public void setWorkEffortStatus(String workEffortStatus) {
		this.workEffortStatus = workEffortStatus;
	}

	@Column(name = "IsActive")
	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Column(name = "CRID")
	public Integer getCrid() {
		return this.crid;
	}

	public void setCrid(Integer crid) {
		this.crid = crid;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LastModifiedOn", length = 23)
	public Date getLastModifiedOn() {
		return this.lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	@Column(name = "LastModifiedBy", length = 8)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Column(name = "AllocatedBy", length = 8)
	public String getAllocatedBy() {
		return this.allocatedBy;
	}

	public void setAllocatedBy(String allocatedBy) {
		this.allocatedBy = allocatedBy;
	}

	@Column(name = "PositionStatus", length = 100)
	public String getPositionStatus() {
		return this.positionStatus;
	}

	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblWorkEffort")
	public Set<TblBookedResource> getTblBookedResources() {
		return this.tblBookedResources;
	}

	public void setTblBookedResources(Set<TblBookedResource> tblBookedResources) {
		this.tblBookedResources = tblBookedResources;
	}

}
