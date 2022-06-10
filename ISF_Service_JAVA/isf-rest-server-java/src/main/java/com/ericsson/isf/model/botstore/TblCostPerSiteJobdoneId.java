package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblCostPerSiteJobdoneId generated by hbm2java
 */
@Embeddable
public class TblCostPerSiteJobdoneId implements java.io.Serializable {

	private Integer projectScopeDetailId;
	private Serializable marketArea;
	private int projectId;
	private Serializable projectName;
	private int subActivityId;
	private String subActivity;
	private String resourceName;
	private int workOrderId;
	private String workOrderStatus;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Date actualStartDate;
	private Date actualEndDate;
	private Date date;
	private double avgEstdEffort;
	private Integer week;
	private double hours;
	private Integer mhr;
	private Double avgEstCost;
	private Double woCost;
	private Serializable siteName;

	public TblCostPerSiteJobdoneId() {
	}

	public TblCostPerSiteJobdoneId(int projectId, int subActivityId, int workOrderId, double avgEstdEffort,
			double hours) {
		this.projectId = projectId;
		this.subActivityId = subActivityId;
		this.workOrderId = workOrderId;
		this.avgEstdEffort = avgEstdEffort;
		this.hours = hours;
	}

	public TblCostPerSiteJobdoneId(Integer projectScopeDetailId, Serializable marketArea, int projectId,
			Serializable projectName, int subActivityId, String subActivity, String resourceName, int workOrderId,
			String workOrderStatus, Date plannedStartDate, Date plannedEndDate, Date actualStartDate,
			Date actualEndDate, Date date, double avgEstdEffort, Integer week, double hours, Integer mhr,
			Double avgEstCost, Double woCost, Serializable siteName) {
		this.projectScopeDetailId = projectScopeDetailId;
		this.marketArea = marketArea;
		this.projectId = projectId;
		this.projectName = projectName;
		this.subActivityId = subActivityId;
		this.subActivity = subActivity;
		this.resourceName = resourceName;
		this.workOrderId = workOrderId;
		this.workOrderStatus = workOrderStatus;
		this.plannedStartDate = plannedStartDate;
		this.plannedEndDate = plannedEndDate;
		this.actualStartDate = actualStartDate;
		this.actualEndDate = actualEndDate;
		this.date = date;
		this.avgEstdEffort = avgEstdEffort;
		this.week = week;
		this.hours = hours;
		this.mhr = mhr;
		this.avgEstCost = avgEstCost;
		this.woCost = woCost;
		this.siteName = siteName;
	}

	@Column(name = "ProjectScopeDetailID")
	public Integer getProjectScopeDetailId() {
		return this.projectScopeDetailId;
	}

	public void setProjectScopeDetailId(Integer projectScopeDetailId) {
		this.projectScopeDetailId = projectScopeDetailId;
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

	@Column(name = "SubActivityID", nullable = false)
	public int getSubActivityId() {
		return this.subActivityId;
	}

	public void setSubActivityId(int subActivityId) {
		this.subActivityId = subActivityId;
	}

	@Column(name = "SubActivity", length = 512)
	public String getSubActivity() {
		return this.subActivity;
	}

	public void setSubActivity(String subActivity) {
		this.subActivity = subActivity;
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

	@Column(name = "Week")
	public Integer getWeek() {
		return this.week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	@Column(name = "Hours", nullable = false, precision = 53, scale = 0)
	public double getHours() {
		return this.hours;
	}

	public void setHours(double hours) {
		this.hours = hours;
	}

	@Column(name = "mhr")
	public Integer getMhr() {
		return this.mhr;
	}

	public void setMhr(Integer mhr) {
		this.mhr = mhr;
	}

	@Column(name = "avgEstCost", precision = 53, scale = 0)
	public Double getAvgEstCost() {
		return this.avgEstCost;
	}

	public void setAvgEstCost(Double avgEstCost) {
		this.avgEstCost = avgEstCost;
	}

	@Column(name = "WO Cost", precision = 53, scale = 0)
	public Double getWoCost() {
		return this.woCost;
	}

	public void setWoCost(Double woCost) {
		this.woCost = woCost;
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
		if (!(other instanceof TblCostPerSiteJobdoneId))
			return false;
		TblCostPerSiteJobdoneId castOther = (TblCostPerSiteJobdoneId) other;

		return ((this.getProjectScopeDetailId() == castOther.getProjectScopeDetailId())
				|| (this.getProjectScopeDetailId() != null && castOther.getProjectScopeDetailId() != null
						&& this.getProjectScopeDetailId().equals(castOther.getProjectScopeDetailId())))
				&& ((this.getMarketArea() == castOther.getMarketArea()) || (this.getMarketArea() != null
						&& castOther.getMarketArea() != null && this.getMarketArea().equals(castOther.getMarketArea())))
				&& (this.getProjectId() == castOther.getProjectId())
				&& ((this.getProjectName() == castOther.getProjectName())
						|| (this.getProjectName() != null && castOther.getProjectName() != null
								&& this.getProjectName().equals(castOther.getProjectName())))
				&& (this.getSubActivityId() == castOther.getSubActivityId())
				&& ((this.getSubActivity() == castOther.getSubActivity())
						|| (this.getSubActivity() != null && castOther.getSubActivity() != null
								&& this.getSubActivity().equals(castOther.getSubActivity())))
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
				&& ((this.getWeek() == castOther.getWeek()) || (this.getWeek() != null && castOther.getWeek() != null
						&& this.getWeek().equals(castOther.getWeek())))
				&& (this.getHours() == castOther.getHours())
				&& ((this.getMhr() == castOther.getMhr()) || (this.getMhr() != null && castOther.getMhr() != null
						&& this.getMhr().equals(castOther.getMhr())))
				&& ((this.getAvgEstCost() == castOther.getAvgEstCost()) || (this.getAvgEstCost() != null
						&& castOther.getAvgEstCost() != null && this.getAvgEstCost().equals(castOther.getAvgEstCost())))
				&& ((this.getWoCost() == castOther.getWoCost()) || (this.getWoCost() != null
						&& castOther.getWoCost() != null && this.getWoCost().equals(castOther.getWoCost())))
				&& ((this.getSiteName() == castOther.getSiteName()) || (this.getSiteName() != null
						&& castOther.getSiteName() != null && this.getSiteName().equals(castOther.getSiteName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getProjectScopeDetailId() == null ? 0 : this.getProjectScopeDetailId().hashCode());
		result = 37 * result + (getMarketArea() == null ? 0 : this.getMarketArea().hashCode());
		result = 37 * result + this.getProjectId();
		result = 37 * result + (getProjectName() == null ? 0 : this.getProjectName().hashCode());
		result = 37 * result + this.getSubActivityId();
		result = 37 * result + (getSubActivity() == null ? 0 : this.getSubActivity().hashCode());
		result = 37 * result + (getResourceName() == null ? 0 : this.getResourceName().hashCode());
		result = 37 * result + this.getWorkOrderId();
		result = 37 * result + (getWorkOrderStatus() == null ? 0 : this.getWorkOrderStatus().hashCode());
		result = 37 * result + (getPlannedStartDate() == null ? 0 : this.getPlannedStartDate().hashCode());
		result = 37 * result + (getPlannedEndDate() == null ? 0 : this.getPlannedEndDate().hashCode());
		result = 37 * result + (getActualStartDate() == null ? 0 : this.getActualStartDate().hashCode());
		result = 37 * result + (getActualEndDate() == null ? 0 : this.getActualEndDate().hashCode());
		result = 37 * result + (getDate() == null ? 0 : this.getDate().hashCode());
		result = 37 * result + (int) this.getAvgEstdEffort();
		result = 37 * result + (getWeek() == null ? 0 : this.getWeek().hashCode());
		result = 37 * result + (int) this.getHours();
		result = 37 * result + (getMhr() == null ? 0 : this.getMhr().hashCode());
		result = 37 * result + (getAvgEstCost() == null ? 0 : this.getAvgEstCost().hashCode());
		result = 37 * result + (getWoCost() == null ? 0 : this.getWoCost().hashCode());
		result = 37 * result + (getSiteName() == null ? 0 : this.getSiteName().hashCode());
		return result;
	}

}
