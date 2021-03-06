package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblSiteExecutionSummaryVolumeJobDoneId generated by hbm2java
 */
@Embeddable
public class TblSiteExecutionSummaryVolumeJobDoneId implements java.io.Serializable {

	private Integer projectScopeDetailId;
	private int subActivityId;
	private Serializable marketArea;
	private int projectId;
	private Serializable projectName;
	private String subactivity;
	private String resourceName;
	private int workOrderId;
	private String workOrderStatus;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Date actualStartDate;
	private Date actualEndDate;
	private Date date;
	private double avgEstdEffort;
	private Double hours;
	private Serializable siteName;

	public TblSiteExecutionSummaryVolumeJobDoneId() {
	}

	public TblSiteExecutionSummaryVolumeJobDoneId(int subActivityId, int projectId, int workOrderId,
			double avgEstdEffort) {
		this.subActivityId = subActivityId;
		this.projectId = projectId;
		this.workOrderId = workOrderId;
		this.avgEstdEffort = avgEstdEffort;
	}

	public TblSiteExecutionSummaryVolumeJobDoneId(Integer projectScopeDetailId, int subActivityId,
			Serializable marketArea, int projectId, Serializable projectName, String subactivity, String resourceName,
			int workOrderId, String workOrderStatus, Date plannedStartDate, Date plannedEndDate, Date actualStartDate,
			Date actualEndDate, Date date, double avgEstdEffort, Double hours, Serializable siteName) {
		this.projectScopeDetailId = projectScopeDetailId;
		this.subActivityId = subActivityId;
		this.marketArea = marketArea;
		this.projectId = projectId;
		this.projectName = projectName;
		this.subactivity = subactivity;
		this.resourceName = resourceName;
		this.workOrderId = workOrderId;
		this.workOrderStatus = workOrderStatus;
		this.plannedStartDate = plannedStartDate;
		this.plannedEndDate = plannedEndDate;
		this.actualStartDate = actualStartDate;
		this.actualEndDate = actualEndDate;
		this.date = date;
		this.avgEstdEffort = avgEstdEffort;
		this.hours = hours;
		this.siteName = siteName;
	}

	@Column(name = "ProjectScopeDetailID")
	public Integer getProjectScopeDetailId() {
		return this.projectScopeDetailId;
	}

	public void setProjectScopeDetailId(Integer projectScopeDetailId) {
		this.projectScopeDetailId = projectScopeDetailId;
	}

	@Column(name = "SubActivityID", nullable = false)
	public int getSubActivityId() {
		return this.subActivityId;
	}

	public void setSubActivityId(int subActivityId) {
		this.subActivityId = subActivityId;
	}

	@Column(name = "Market Area")
	public Serializable getMarketArea() {
		return this.marketArea;
	}

	public void setMarketArea(Serializable marketArea) {
		this.marketArea = marketArea;
	}

	@Column(name = "ProjectID", nullable = false)
	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	@Column(name = "ProjectName")
	public Serializable getProjectName() {
		return this.projectName;
	}

	public void setProjectName(Serializable projectName) {
		this.projectName = projectName;
	}

	@Column(name = "Subactivity", length = 512)
	public String getSubactivity() {
		return this.subactivity;
	}

	public void setSubactivity(String subactivity) {
		this.subactivity = subactivity;
	}

	@Column(name = "Resource Name", length = 1525)
	public String getResourceName() {
		return this.resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	@Column(name = "Work Order Id", nullable = false)
	public int getWorkOrderId() {
		return this.workOrderId;
	}

	public void setWorkOrderId(int workOrderId) {
		this.workOrderId = workOrderId;
	}

	@Column(name = "Work Order Status", length = 512)
	public String getWorkOrderStatus() {
		return this.workOrderStatus;
	}

	public void setWorkOrderStatus(String workOrderStatus) {
		this.workOrderStatus = workOrderStatus;
	}

	@Column(name = "Planned Start Date", length = 23)
	public Date getPlannedStartDate() {
		return this.plannedStartDate;
	}

	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	@Column(name = "Planned End Date", length = 23)
	public Date getPlannedEndDate() {
		return this.plannedEndDate;
	}

	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}

	@Column(name = "Actual Start Date", length = 23)
	public Date getActualStartDate() {
		return this.actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	@Column(name = "Actual End Date", length = 23)
	public Date getActualEndDate() {
		return this.actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	@Column(name = "Date", length = 10)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "AvgEstdEffort", nullable = false, precision = 53, scale = 0)
	public double getAvgEstdEffort() {
		return this.avgEstdEffort;
	}

	public void setAvgEstdEffort(double avgEstdEffort) {
		this.avgEstdEffort = avgEstdEffort;
	}

	@Column(name = "Hours", precision = 53, scale = 0)
	public Double getHours() {
		return this.hours;
	}

	public void setHours(Double hours) {
		this.hours = hours;
	}

	@Column(name = "SiteName")
	public Serializable getSiteName() {
		return this.siteName;
	}

	public void setSiteName(Serializable siteName) {
		this.siteName = siteName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblSiteExecutionSummaryVolumeJobDoneId))
			return false;
		TblSiteExecutionSummaryVolumeJobDoneId castOther = (TblSiteExecutionSummaryVolumeJobDoneId) other;

		return ((this.getProjectScopeDetailId() == castOther.getProjectScopeDetailId())
				|| (this.getProjectScopeDetailId() != null && castOther.getProjectScopeDetailId() != null
						&& this.getProjectScopeDetailId().equals(castOther.getProjectScopeDetailId())))
				&& (this.getSubActivityId() == castOther.getSubActivityId())
				&& ((this.getMarketArea() == castOther.getMarketArea()) || (this.getMarketArea() != null
						&& castOther.getMarketArea() != null && this.getMarketArea().equals(castOther.getMarketArea())))
				&& (this.getProjectId() == castOther.getProjectId())
				&& ((this.getProjectName() == castOther.getProjectName())
						|| (this.getProjectName() != null && castOther.getProjectName() != null
								&& this.getProjectName().equals(castOther.getProjectName())))
				&& ((this.getSubactivity() == castOther.getSubactivity())
						|| (this.getSubactivity() != null && castOther.getSubactivity() != null
								&& this.getSubactivity().equals(castOther.getSubactivity())))
				&& ((this.getResourceName() == castOther.getResourceName())
						|| (this.getResourceName() != null && castOther.getResourceName() != null
								&& this.getResourceName().equals(castOther.getResourceName())))
				&& (this.getWorkOrderId() == castOther.getWorkOrderId())
				&& ((this.getWorkOrderStatus() == castOther.getWorkOrderStatus())
						|| (this.getWorkOrderStatus() != null && castOther.getWorkOrderStatus() != null
								&& this.getWorkOrderStatus().equals(castOther.getWorkOrderStatus())))
				&& ((this.getPlannedStartDate() == castOther.getPlannedStartDate())
						|| (this.getPlannedStartDate() != null && castOther.getPlannedStartDate() != null
								&& this.getPlannedStartDate().equals(castOther.getPlannedStartDate())))
				&& ((this.getPlannedEndDate() == castOther.getPlannedEndDate())
						|| (this.getPlannedEndDate() != null && castOther.getPlannedEndDate() != null
								&& this.getPlannedEndDate().equals(castOther.getPlannedEndDate())))
				&& ((this.getActualStartDate() == castOther.getActualStartDate())
						|| (this.getActualStartDate() != null && castOther.getActualStartDate() != null
								&& this.getActualStartDate().equals(castOther.getActualStartDate())))
				&& ((this.getActualEndDate() == castOther.getActualEndDate())
						|| (this.getActualEndDate() != null && castOther.getActualEndDate() != null
								&& this.getActualEndDate().equals(castOther.getActualEndDate())))
				&& ((this.getDate() == castOther.getDate()) || (this.getDate() != null && castOther.getDate() != null
						&& this.getDate().equals(castOther.getDate())))
				&& (this.getAvgEstdEffort() == castOther.getAvgEstdEffort())
				&& ((this.getHours() == castOther.getHours()) || (this.getHours() != null
						&& castOther.getHours() != null && this.getHours().equals(castOther.getHours())))
				&& ((this.getSiteName() == castOther.getSiteName()) || (this.getSiteName() != null
						&& castOther.getSiteName() != null && this.getSiteName().equals(castOther.getSiteName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getProjectScopeDetailId() == null ? 0 : this.getProjectScopeDetailId().hashCode());
		result = 37 * result + this.getSubActivityId();
		result = 37 * result + (getMarketArea() == null ? 0 : this.getMarketArea().hashCode());
		result = 37 * result + this.getProjectId();
		result = 37 * result + (getProjectName() == null ? 0 : this.getProjectName().hashCode());
		result = 37 * result + (getSubactivity() == null ? 0 : this.getSubactivity().hashCode());
		result = 37 * result + (getResourceName() == null ? 0 : this.getResourceName().hashCode());
		result = 37 * result + this.getWorkOrderId();
		result = 37 * result + (getWorkOrderStatus() == null ? 0 : this.getWorkOrderStatus().hashCode());
		result = 37 * result + (getPlannedStartDate() == null ? 0 : this.getPlannedStartDate().hashCode());
		result = 37 * result + (getPlannedEndDate() == null ? 0 : this.getPlannedEndDate().hashCode());
		result = 37 * result + (getActualStartDate() == null ? 0 : this.getActualStartDate().hashCode());
		result = 37 * result + (getActualEndDate() == null ? 0 : this.getActualEndDate().hashCode());
		result = 37 * result + (getDate() == null ? 0 : this.getDate().hashCode());
		result = 37 * result + (int) this.getAvgEstdEffort();
		result = 37 * result + (getHours() == null ? 0 : this.getHours().hashCode());
		result = 37 * result + (getSiteName() == null ? 0 : this.getSiteName().hashCode());
		return result;
	}

}
