package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblBacklogWithAgingId generated by hbm2java
 */
@Embeddable
public class TblBacklogWithAgingId implements java.io.Serializable {

	private Serializable marketAreaName;
	private Serializable customerName;
	private int projectId;
	private int subactivityid;
	private int woid;
	private Serializable projectName;
	private String subActivity;
	private String woname;
	private String assigned;
	private String woStatus;
	private Date derivedEndDate;
	private Date plannedStartDate;
	private Date date;
	private String dated;
	private Byte month;
	private Byte week;
	private Short year;

	public TblBacklogWithAgingId() {
	}

	public TblBacklogWithAgingId(int projectId, int subactivityid, int woid) {
		this.projectId = projectId;
		this.subactivityid = subactivityid;
		this.woid = woid;
	}

	public TblBacklogWithAgingId(Serializable marketAreaName, Serializable customerName, int projectId,
			int subactivityid, int woid, Serializable projectName, String subActivity, String woname, String assigned,
			String woStatus, Date derivedEndDate, Date plannedStartDate, Date date, String dated, Byte month, Byte week,
			Short year) {
		this.marketAreaName = marketAreaName;
		this.customerName = customerName;
		this.projectId = projectId;
		this.subactivityid = subactivityid;
		this.woid = woid;
		this.projectName = projectName;
		this.subActivity = subActivity;
		this.woname = woname;
		this.assigned = assigned;
		this.woStatus = woStatus;
		this.derivedEndDate = derivedEndDate;
		this.plannedStartDate = plannedStartDate;
		this.date = date;
		this.dated = dated;
		this.month = month;
		this.week = week;
		this.year = year;
	}

	@Column(name = "MarketAreaName")
	public Serializable getMarketAreaName() {
		return this.marketAreaName;
	}

	public void setMarketAreaName(Serializable marketAreaName) {
		this.marketAreaName = marketAreaName;
	}

	@Column(name = "CustomerName")
	public Serializable getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(Serializable customerName) {
		this.customerName = customerName;
	}

	@Column(name = "ProjectID", nullable = false)
	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	@Column(name = "Subactivityid", nullable = false)
	public int getSubactivityid() {
		return this.subactivityid;
	}

	public void setSubactivityid(int subactivityid) {
		this.subactivityid = subactivityid;
	}

	@Column(name = "WOID", nullable = false)
	public int getWoid() {
		return this.woid;
	}

	public void setWoid(int woid) {
		this.woid = woid;
	}

	@Column(name = "ProjectName")
	public Serializable getProjectName() {
		return this.projectName;
	}

	public void setProjectName(Serializable projectName) {
		this.projectName = projectName;
	}

	@Column(name = "SubActivity", length = 512)
	public String getSubActivity() {
		return this.subActivity;
	}

	public void setSubActivity(String subActivity) {
		this.subActivity = subActivity;
	}

	@Column(name = "WOName")
	public String getWoname() {
		return this.woname;
	}

	public void setWoname(String woname) {
		this.woname = woname;
	}

	@Column(name = "Assigned", length = 1525)
	public String getAssigned() {
		return this.assigned;
	}

	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}

	@Column(name = "WO Status", length = 512)
	public String getWoStatus() {
		return this.woStatus;
	}

	public void setWoStatus(String woStatus) {
		this.woStatus = woStatus;
	}

	@Column(name = "DerivedEndDate", length = 10)
	public Date getDerivedEndDate() {
		return this.derivedEndDate;
	}

	public void setDerivedEndDate(Date derivedEndDate) {
		this.derivedEndDate = derivedEndDate;
	}

	@Column(name = "PlannedStartDate", length = 10)
	public Date getPlannedStartDate() {
		return this.plannedStartDate;
	}

	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	@Column(name = "DATE", length = 10)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "Dated", length = 29)
	public String getDated() {
		return this.dated;
	}

	public void setDated(String dated) {
		this.dated = dated;
	}

	@Column(name = "Month")
	public Byte getMonth() {
		return this.month;
	}

	public void setMonth(Byte month) {
		this.month = month;
	}

	@Column(name = "Week")
	public Byte getWeek() {
		return this.week;
	}

	public void setWeek(Byte week) {
		this.week = week;
	}

	@Column(name = "YEAR")
	public Short getYear() {
		return this.year;
	}

	public void setYear(Short year) {
		this.year = year;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblBacklogWithAgingId))
			return false;
		TblBacklogWithAgingId castOther = (TblBacklogWithAgingId) other;

		return ((this.getMarketAreaName() == castOther.getMarketAreaName())
				|| (this.getMarketAreaName() != null && castOther.getMarketAreaName() != null
						&& this.getMarketAreaName().equals(castOther.getMarketAreaName())))
				&& ((this.getCustomerName() == castOther.getCustomerName())
						|| (this.getCustomerName() != null && castOther.getCustomerName() != null
								&& this.getCustomerName().equals(castOther.getCustomerName())))
				&& (this.getProjectId() == castOther.getProjectId())
				&& (this.getSubactivityid() == castOther.getSubactivityid()) && (this.getWoid() == castOther.getWoid())
				&& ((this.getProjectName() == castOther.getProjectName())
						|| (this.getProjectName() != null && castOther.getProjectName() != null
								&& this.getProjectName().equals(castOther.getProjectName())))
				&& ((this.getSubActivity() == castOther.getSubActivity())
						|| (this.getSubActivity() != null && castOther.getSubActivity() != null
								&& this.getSubActivity().equals(castOther.getSubActivity())))
				&& ((this.getWoname() == castOther.getWoname()) || (this.getWoname() != null
						&& castOther.getWoname() != null && this.getWoname().equals(castOther.getWoname())))
				&& ((this.getAssigned() == castOther.getAssigned()) || (this.getAssigned() != null
						&& castOther.getAssigned() != null && this.getAssigned().equals(castOther.getAssigned())))
				&& ((this.getWoStatus() == castOther.getWoStatus()) || (this.getWoStatus() != null
						&& castOther.getWoStatus() != null && this.getWoStatus().equals(castOther.getWoStatus())))
				&& ((this.getDerivedEndDate() == castOther.getDerivedEndDate())
						|| (this.getDerivedEndDate() != null && castOther.getDerivedEndDate() != null
								&& this.getDerivedEndDate().equals(castOther.getDerivedEndDate())))
				&& ((this.getPlannedStartDate() == castOther.getPlannedStartDate())
						|| (this.getPlannedStartDate() != null && castOther.getPlannedStartDate() != null
								&& this.getPlannedStartDate().equals(castOther.getPlannedStartDate())))
				&& ((this.getDate() == castOther.getDate()) || (this.getDate() != null && castOther.getDate() != null
						&& this.getDate().equals(castOther.getDate())))
				&& ((this.getDated() == castOther.getDated()) || (this.getDated() != null
						&& castOther.getDated() != null && this.getDated().equals(castOther.getDated())))
				&& ((this.getMonth() == castOther.getMonth()) || (this.getMonth() != null
						&& castOther.getMonth() != null && this.getMonth().equals(castOther.getMonth())))
				&& ((this.getWeek() == castOther.getWeek()) || (this.getWeek() != null && castOther.getWeek() != null
						&& this.getWeek().equals(castOther.getWeek())))
				&& ((this.getYear() == castOther.getYear()) || (this.getYear() != null && castOther.getYear() != null
						&& this.getYear().equals(castOther.getYear())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getMarketAreaName() == null ? 0 : this.getMarketAreaName().hashCode());
		result = 37 * result + (getCustomerName() == null ? 0 : this.getCustomerName().hashCode());
		result = 37 * result + this.getProjectId();
		result = 37 * result + this.getSubactivityid();
		result = 37 * result + this.getWoid();
		result = 37 * result + (getProjectName() == null ? 0 : this.getProjectName().hashCode());
		result = 37 * result + (getSubActivity() == null ? 0 : this.getSubActivity().hashCode());
		result = 37 * result + (getWoname() == null ? 0 : this.getWoname().hashCode());
		result = 37 * result + (getAssigned() == null ? 0 : this.getAssigned().hashCode());
		result = 37 * result + (getWoStatus() == null ? 0 : this.getWoStatus().hashCode());
		result = 37 * result + (getDerivedEndDate() == null ? 0 : this.getDerivedEndDate().hashCode());
		result = 37 * result + (getPlannedStartDate() == null ? 0 : this.getPlannedStartDate().hashCode());
		result = 37 * result + (getDate() == null ? 0 : this.getDate().hashCode());
		result = 37 * result + (getDated() == null ? 0 : this.getDated().hashCode());
		result = 37 * result + (getMonth() == null ? 0 : this.getMonth().hashCode());
		result = 37 * result + (getWeek() == null ? 0 : this.getWeek().hashCode());
		result = 37 * result + (getYear() == null ? 0 : this.getYear().hashCode());
		return result;
	}

}
