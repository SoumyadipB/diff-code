package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CompleteresourcedetailsId generated by hbm2java
 */
@Embeddable
public class CompleteresourcedetailsId implements java.io.Serializable {

	private int resourceRequestId;
	private Serializable resourceType;
	private Integer jobRoleId;
	private Integer jobStageId;
	private Integer projectId;
	private Integer projectScopeDetailId;
	private int onsiteCount;
	private int remoteCount;
	private String positionStatus;
	private Date inActivatedOn;
	private String crstatus;
	private int workEffortId;
	private Date wkeffCreatedOn;
	private int resourcePositionId;
	private Date positionStartDate;
	private Date positionEndDate;
	private Integer duration;
	private Double ftePercent;
	private Serializable remoteOnsite;
	private Double hours;
	private String blockedResource;
	private String workEffortStatus;
	private Serializable jobRoleName;
	private Serializable jobStageName;

	public CompleteresourcedetailsId() {
	}

	public CompleteresourcedetailsId(int resourceRequestId, int onsiteCount, int remoteCount, int workEffortId,
			Date wkeffCreatedOn, int resourcePositionId, Date positionStartDate, Date positionEndDate) {
		this.resourceRequestId = resourceRequestId;
		this.onsiteCount = onsiteCount;
		this.remoteCount = remoteCount;
		this.workEffortId = workEffortId;
		this.wkeffCreatedOn = wkeffCreatedOn;
		this.resourcePositionId = resourcePositionId;
		this.positionStartDate = positionStartDate;
		this.positionEndDate = positionEndDate;
	}

	public CompleteresourcedetailsId(int resourceRequestId, Serializable resourceType, Integer jobRoleId,
			Integer jobStageId, Integer projectId, Integer projectScopeDetailId, int onsiteCount, int remoteCount,
			String positionStatus, Date inActivatedOn, String crstatus, int workEffortId, Date wkeffCreatedOn,
			int resourcePositionId, Date positionStartDate, Date positionEndDate, Integer duration, Double ftePercent,
			Serializable remoteOnsite, Double hours, String blockedResource, String workEffortStatus,
			Serializable jobRoleName, Serializable jobStageName) {
		this.resourceRequestId = resourceRequestId;
		this.resourceType = resourceType;
		this.jobRoleId = jobRoleId;
		this.jobStageId = jobStageId;
		this.projectId = projectId;
		this.projectScopeDetailId = projectScopeDetailId;
		this.onsiteCount = onsiteCount;
		this.remoteCount = remoteCount;
		this.positionStatus = positionStatus;
		this.inActivatedOn = inActivatedOn;
		this.crstatus = crstatus;
		this.workEffortId = workEffortId;
		this.wkeffCreatedOn = wkeffCreatedOn;
		this.resourcePositionId = resourcePositionId;
		this.positionStartDate = positionStartDate;
		this.positionEndDate = positionEndDate;
		this.duration = duration;
		this.ftePercent = ftePercent;
		this.remoteOnsite = remoteOnsite;
		this.hours = hours;
		this.blockedResource = blockedResource;
		this.workEffortStatus = workEffortStatus;
		this.jobRoleName = jobRoleName;
		this.jobStageName = jobStageName;
	}

	@Column(name = "ResourceRequestID", nullable = false)
	public int getResourceRequestId() {
		return this.resourceRequestId;
	}

	public void setResourceRequestId(int resourceRequestId) {
		this.resourceRequestId = resourceRequestId;
	}

	@Column(name = "ResourceType")
	public Serializable getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(Serializable resourceType) {
		this.resourceType = resourceType;
	}

	@Column(name = "JobRoleID")
	public Integer getJobRoleId() {
		return this.jobRoleId;
	}

	public void setJobRoleId(Integer jobRoleId) {
		this.jobRoleId = jobRoleId;
	}

	@Column(name = "JobStageID")
	public Integer getJobStageId() {
		return this.jobStageId;
	}

	public void setJobStageId(Integer jobStageId) {
		this.jobStageId = jobStageId;
	}

	@Column(name = "ProjectID")
	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Column(name = "ProjectScopeDetailID")
	public Integer getProjectScopeDetailId() {
		return this.projectScopeDetailId;
	}

	public void setProjectScopeDetailId(Integer projectScopeDetailId) {
		this.projectScopeDetailId = projectScopeDetailId;
	}

	@Column(name = "OnsiteCount", nullable = false)
	public int getOnsiteCount() {
		return this.onsiteCount;
	}

	public void setOnsiteCount(int onsiteCount) {
		this.onsiteCount = onsiteCount;
	}

	@Column(name = "RemoteCount", nullable = false)
	public int getRemoteCount() {
		return this.remoteCount;
	}

	public void setRemoteCount(int remoteCount) {
		this.remoteCount = remoteCount;
	}

	@Column(name = "PositionStatus", length = 100)
	public String getPositionStatus() {
		return this.positionStatus;
	}

	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}

	@Column(name = "InActivatedOn", length = 10)
	public Date getInActivatedOn() {
		return this.inActivatedOn;
	}

	public void setInActivatedOn(Date inActivatedOn) {
		this.inActivatedOn = inActivatedOn;
	}

	@Column(name = "CRStatus", length = 100)
	public String getCrstatus() {
		return this.crstatus;
	}

	public void setCrstatus(String crstatus) {
		this.crstatus = crstatus;
	}

	@Column(name = "WorkEffortID", nullable = false)
	public int getWorkEffortId() {
		return this.workEffortId;
	}

	public void setWorkEffortId(int workEffortId) {
		this.workEffortId = workEffortId;
	}

	@Column(name = "WKEffCreatedON", nullable = false, length = 23)
	public Date getWkeffCreatedOn() {
		return this.wkeffCreatedOn;
	}

	public void setWkeffCreatedOn(Date wkeffCreatedOn) {
		this.wkeffCreatedOn = wkeffCreatedOn;
	}

	@Column(name = "ResourcePositionID", nullable = false)
	public int getResourcePositionId() {
		return this.resourcePositionId;
	}

	public void setResourcePositionId(int resourcePositionId) {
		this.resourcePositionId = resourcePositionId;
	}

	@Column(name = "PositionStartDate", nullable = false, length = 10)
	public Date getPositionStartDate() {
		return this.positionStartDate;
	}

	public void setPositionStartDate(Date positionStartDate) {
		this.positionStartDate = positionStartDate;
	}

	@Column(name = "PositionEndDate", nullable = false, length = 10)
	public Date getPositionEndDate() {
		return this.positionEndDate;
	}

	public void setPositionEndDate(Date positionEndDate) {
		this.positionEndDate = positionEndDate;
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

	@Column(name = "remote_onsite")
	public Serializable getRemoteOnsite() {
		return this.remoteOnsite;
	}

	public void setRemoteOnsite(Serializable remoteOnsite) {
		this.remoteOnsite = remoteOnsite;
	}

	@Column(name = "Hours", precision = 53, scale = 0)
	public Double getHours() {
		return this.hours;
	}

	public void setHours(Double hours) {
		this.hours = hours;
	}

	@Column(name = "BlockedResource", length = 8)
	public String getBlockedResource() {
		return this.blockedResource;
	}

	public void setBlockedResource(String blockedResource) {
		this.blockedResource = blockedResource;
	}

	@Column(name = "WorkEffortStatus", length = 100)
	public String getWorkEffortStatus() {
		return this.workEffortStatus;
	}

	public void setWorkEffortStatus(String workEffortStatus) {
		this.workEffortStatus = workEffortStatus;
	}

	@Column(name = "JobRoleName")
	public Serializable getJobRoleName() {
		return this.jobRoleName;
	}

	public void setJobRoleName(Serializable jobRoleName) {
		this.jobRoleName = jobRoleName;
	}

	@Column(name = "JobStageName")
	public Serializable getJobStageName() {
		return this.jobStageName;
	}

	public void setJobStageName(Serializable jobStageName) {
		this.jobStageName = jobStageName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CompleteresourcedetailsId))
			return false;
		CompleteresourcedetailsId castOther = (CompleteresourcedetailsId) other;

		return (this.getResourceRequestId() == castOther.getResourceRequestId())
				&& ((this.getResourceType() == castOther.getResourceType())
						|| (this.getResourceType() != null && castOther.getResourceType() != null
								&& this.getResourceType().equals(castOther.getResourceType())))
				&& ((this.getJobRoleId() == castOther.getJobRoleId()) || (this.getJobRoleId() != null
						&& castOther.getJobRoleId() != null && this.getJobRoleId().equals(castOther.getJobRoleId())))
				&& ((this.getJobStageId() == castOther.getJobStageId()) || (this.getJobStageId() != null
						&& castOther.getJobStageId() != null && this.getJobStageId().equals(castOther.getJobStageId())))
				&& ((this.getProjectId() == castOther.getProjectId()) || (this.getProjectId() != null
						&& castOther.getProjectId() != null && this.getProjectId().equals(castOther.getProjectId())))
				&& ((this.getProjectScopeDetailId() == castOther.getProjectScopeDetailId())
						|| (this.getProjectScopeDetailId() != null && castOther.getProjectScopeDetailId() != null
								&& this.getProjectScopeDetailId().equals(castOther.getProjectScopeDetailId())))
				&& (this.getOnsiteCount() == castOther.getOnsiteCount())
				&& (this.getRemoteCount() == castOther.getRemoteCount())
				&& ((this.getPositionStatus() == castOther.getPositionStatus())
						|| (this.getPositionStatus() != null && castOther.getPositionStatus() != null
								&& this.getPositionStatus().equals(castOther.getPositionStatus())))
				&& ((this.getInActivatedOn() == castOther.getInActivatedOn())
						|| (this.getInActivatedOn() != null && castOther.getInActivatedOn() != null
								&& this.getInActivatedOn().equals(castOther.getInActivatedOn())))
				&& ((this.getCrstatus() == castOther.getCrstatus()) || (this.getCrstatus() != null
						&& castOther.getCrstatus() != null && this.getCrstatus().equals(castOther.getCrstatus())))
				&& (this.getWorkEffortId() == castOther.getWorkEffortId())
				&& ((this.getWkeffCreatedOn() == castOther.getWkeffCreatedOn())
						|| (this.getWkeffCreatedOn() != null && castOther.getWkeffCreatedOn() != null
								&& this.getWkeffCreatedOn().equals(castOther.getWkeffCreatedOn())))
				&& (this.getResourcePositionId() == castOther.getResourcePositionId())
				&& ((this.getPositionStartDate() == castOther.getPositionStartDate())
						|| (this.getPositionStartDate() != null && castOther.getPositionStartDate() != null
								&& this.getPositionStartDate().equals(castOther.getPositionStartDate())))
				&& ((this.getPositionEndDate() == castOther.getPositionEndDate())
						|| (this.getPositionEndDate() != null && castOther.getPositionEndDate() != null
								&& this.getPositionEndDate().equals(castOther.getPositionEndDate())))
				&& ((this.getDuration() == castOther.getDuration()) || (this.getDuration() != null
						&& castOther.getDuration() != null && this.getDuration().equals(castOther.getDuration())))
				&& ((this.getFtePercent() == castOther.getFtePercent()) || (this.getFtePercent() != null
						&& castOther.getFtePercent() != null && this.getFtePercent().equals(castOther.getFtePercent())))
				&& ((this.getRemoteOnsite() == castOther.getRemoteOnsite())
						|| (this.getRemoteOnsite() != null && castOther.getRemoteOnsite() != null
								&& this.getRemoteOnsite().equals(castOther.getRemoteOnsite())))
				&& ((this.getHours() == castOther.getHours()) || (this.getHours() != null
						&& castOther.getHours() != null && this.getHours().equals(castOther.getHours())))
				&& ((this.getBlockedResource() == castOther.getBlockedResource())
						|| (this.getBlockedResource() != null && castOther.getBlockedResource() != null
								&& this.getBlockedResource().equals(castOther.getBlockedResource())))
				&& ((this.getWorkEffortStatus() == castOther.getWorkEffortStatus())
						|| (this.getWorkEffortStatus() != null && castOther.getWorkEffortStatus() != null
								&& this.getWorkEffortStatus().equals(castOther.getWorkEffortStatus())))
				&& ((this.getJobRoleName() == castOther.getJobRoleName())
						|| (this.getJobRoleName() != null && castOther.getJobRoleName() != null
								&& this.getJobRoleName().equals(castOther.getJobRoleName())))
				&& ((this.getJobStageName() == castOther.getJobStageName())
						|| (this.getJobStageName() != null && castOther.getJobStageName() != null
								&& this.getJobStageName().equals(castOther.getJobStageName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getResourceRequestId();
		result = 37 * result + (getResourceType() == null ? 0 : this.getResourceType().hashCode());
		result = 37 * result + (getJobRoleId() == null ? 0 : this.getJobRoleId().hashCode());
		result = 37 * result + (getJobStageId() == null ? 0 : this.getJobStageId().hashCode());
		result = 37 * result + (getProjectId() == null ? 0 : this.getProjectId().hashCode());
		result = 37 * result + (getProjectScopeDetailId() == null ? 0 : this.getProjectScopeDetailId().hashCode());
		result = 37 * result + this.getOnsiteCount();
		result = 37 * result + this.getRemoteCount();
		result = 37 * result + (getPositionStatus() == null ? 0 : this.getPositionStatus().hashCode());
		result = 37 * result + (getInActivatedOn() == null ? 0 : this.getInActivatedOn().hashCode());
		result = 37 * result + (getCrstatus() == null ? 0 : this.getCrstatus().hashCode());
		result = 37 * result + this.getWorkEffortId();
		result = 37 * result + (getWkeffCreatedOn() == null ? 0 : this.getWkeffCreatedOn().hashCode());
		result = 37 * result + this.getResourcePositionId();
		result = 37 * result + (getPositionStartDate() == null ? 0 : this.getPositionStartDate().hashCode());
		result = 37 * result + (getPositionEndDate() == null ? 0 : this.getPositionEndDate().hashCode());
		result = 37 * result + (getDuration() == null ? 0 : this.getDuration().hashCode());
		result = 37 * result + (getFtePercent() == null ? 0 : this.getFtePercent().hashCode());
		result = 37 * result + (getRemoteOnsite() == null ? 0 : this.getRemoteOnsite().hashCode());
		result = 37 * result + (getHours() == null ? 0 : this.getHours().hashCode());
		result = 37 * result + (getBlockedResource() == null ? 0 : this.getBlockedResource().hashCode());
		result = 37 * result + (getWorkEffortStatus() == null ? 0 : this.getWorkEffortStatus().hashCode());
		result = 37 * result + (getJobRoleName() == null ? 0 : this.getJobRoleName().hashCode());
		result = 37 * result + (getJobStageName() == null ? 0 : this.getJobStageName().hashCode());
		return result;
	}

}
