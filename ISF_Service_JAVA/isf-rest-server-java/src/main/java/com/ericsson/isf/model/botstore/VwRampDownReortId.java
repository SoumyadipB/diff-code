package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * VwRampDownReortId generated by hbm2java
 */
@Embeddable
public class VwRampDownReortId implements java.io.Serializable {

	private int resourcePositionId;
	private Integer projectId;
	private Serializable projectName;
	private Date projectStartDate;
	private Date projectEndDate;
	private String domain;
	private String subDomain;
	private String technology;
	private String serviceArea;
	private String subServiceArea;
	private String positionStatus;
	private String signum;
	private String employeeName;
	private int personnelNumber;
	private int workEffortId;
	private String managerSignum;
	private Date positionStartDate;
	private Date positionEndDate;
	private String rampDownWeek;
	private Integer rampDownYear;
	private Serializable certificateName;
	private Serializable region;
	private Serializable marketArea;
	private Serializable marketAreaRegion;
	private Serializable countryName;
	private String orgUnit;

	public VwRampDownReortId() {
	}

	public VwRampDownReortId(int resourcePositionId, String employeeName, int personnelNumber, int workEffortId,
			Date positionStartDate, Date positionEndDate, Serializable certificateName) {
		this.resourcePositionId = resourcePositionId;
		this.employeeName = employeeName;
		this.personnelNumber = personnelNumber;
		this.workEffortId = workEffortId;
		this.positionStartDate = positionStartDate;
		this.positionEndDate = positionEndDate;
		this.certificateName = certificateName;
	}

	public VwRampDownReortId(int resourcePositionId, Integer projectId, Serializable projectName, Date projectStartDate,
			Date projectEndDate, String domain, String subDomain, String technology, String serviceArea,
			String subServiceArea, String positionStatus, String signum, String employeeName, int personnelNumber,
			int workEffortId, String managerSignum, Date positionStartDate, Date positionEndDate, String rampDownWeek,
			Integer rampDownYear, Serializable certificateName, Serializable region, Serializable marketArea,
			Serializable marketAreaRegion, Serializable countryName, String orgUnit) {
		this.resourcePositionId = resourcePositionId;
		this.projectId = projectId;
		this.projectName = projectName;
		this.projectStartDate = projectStartDate;
		this.projectEndDate = projectEndDate;
		this.domain = domain;
		this.subDomain = subDomain;
		this.technology = technology;
		this.serviceArea = serviceArea;
		this.subServiceArea = subServiceArea;
		this.positionStatus = positionStatus;
		this.signum = signum;
		this.employeeName = employeeName;
		this.personnelNumber = personnelNumber;
		this.workEffortId = workEffortId;
		this.managerSignum = managerSignum;
		this.positionStartDate = positionStartDate;
		this.positionEndDate = positionEndDate;
		this.rampDownWeek = rampDownWeek;
		this.rampDownYear = rampDownYear;
		this.certificateName = certificateName;
		this.region = region;
		this.marketArea = marketArea;
		this.marketAreaRegion = marketAreaRegion;
		this.countryName = countryName;
		this.orgUnit = orgUnit;
	}

	@Column(name = "ResourcePositionID", nullable = false)
	public int getResourcePositionId() {
		return this.resourcePositionId;
	}

	public void setResourcePositionId(int resourcePositionId) {
		this.resourcePositionId = resourcePositionId;
	}

	@Column(name = "ProjectID")
	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Column(name = "ProjectName")
	public Serializable getProjectName() {
		return this.projectName;
	}

	public void setProjectName(Serializable projectName) {
		this.projectName = projectName;
	}

	@Column(name = "ProjectStartDate", length = 23)
	public Date getProjectStartDate() {
		return this.projectStartDate;
	}

	public void setProjectStartDate(Date projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	@Column(name = "ProjectEndDate", length = 23)
	public Date getProjectEndDate() {
		return this.projectEndDate;
	}

	public void setProjectEndDate(Date projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	@Column(name = "Domain", length = 128)
	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "SubDomain", length = 128)
	public String getSubDomain() {
		return this.subDomain;
	}

	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}

	@Column(name = "Technology", length = 128)
	public String getTechnology() {
		return this.technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	@Column(name = "ServiceArea", length = 128)
	public String getServiceArea() {
		return this.serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	@Column(name = "SubServiceArea", length = 128)
	public String getSubServiceArea() {
		return this.subServiceArea;
	}

	public void setSubServiceArea(String subServiceArea) {
		this.subServiceArea = subServiceArea;
	}

	@Column(name = "PositionStatus", length = 100)
	public String getPositionStatus() {
		return this.positionStatus;
	}

	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}

	@Column(name = "Signum", length = 8)
	public String getSignum() {
		return this.signum;
	}

	public void setSignum(String signum) {
		this.signum = signum;
	}

	@Column(name = "EmployeeName", nullable = false, length = 500)
	public String getEmployeeName() {
		return this.employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	@Column(name = "PersonnelNumber", nullable = false)
	public int getPersonnelNumber() {
		return this.personnelNumber;
	}

	public void setPersonnelNumber(int personnelNumber) {
		this.personnelNumber = personnelNumber;
	}

	@Column(name = "WorkEffortID", nullable = false)
	public int getWorkEffortId() {
		return this.workEffortId;
	}

	public void setWorkEffortId(int workEffortId) {
		this.workEffortId = workEffortId;
	}

	@Column(name = "ManagerSignum", length = 8)
	public String getManagerSignum() {
		return this.managerSignum;
	}

	public void setManagerSignum(String managerSignum) {
		this.managerSignum = managerSignum;
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

	@Column(name = "RampDownWeek", length = 9)
	public String getRampDownWeek() {
		return this.rampDownWeek;
	}

	public void setRampDownWeek(String rampDownWeek) {
		this.rampDownWeek = rampDownWeek;
	}

	@Column(name = "RampDownYear")
	public Integer getRampDownYear() {
		return this.rampDownYear;
	}

	public void setRampDownYear(Integer rampDownYear) {
		this.rampDownYear = rampDownYear;
	}

	@Column(name = "CertificateName", nullable = false)
	public Serializable getCertificateName() {
		return this.certificateName;
	}

	public void setCertificateName(Serializable certificateName) {
		this.certificateName = certificateName;
	}

	@Column(name = "Region")
	public Serializable getRegion() {
		return this.region;
	}

	public void setRegion(Serializable region) {
		this.region = region;
	}

	@Column(name = "MarketArea")
	public Serializable getMarketArea() {
		return this.marketArea;
	}

	public void setMarketArea(Serializable marketArea) {
		this.marketArea = marketArea;
	}

	@Column(name = "MarketAreaRegion")
	public Serializable getMarketAreaRegion() {
		return this.marketAreaRegion;
	}

	public void setMarketAreaRegion(Serializable marketAreaRegion) {
		this.marketAreaRegion = marketAreaRegion;
	}

	@Column(name = "CountryName")
	public Serializable getCountryName() {
		return this.countryName;
	}

	public void setCountryName(Serializable countryName) {
		this.countryName = countryName;
	}

	@Column(name = "ORG_UNIT", length = 128)
	public String getOrgUnit() {
		return this.orgUnit;
	}

	public void setOrgUnit(String orgUnit) {
		this.orgUnit = orgUnit;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VwRampDownReortId))
			return false;
		VwRampDownReortId castOther = (VwRampDownReortId) other;

		return (this.getResourcePositionId() == castOther.getResourcePositionId())
				&& ((this.getProjectId() == castOther.getProjectId()) || (this.getProjectId() != null
						&& castOther.getProjectId() != null && this.getProjectId().equals(castOther.getProjectId())))
				&& ((this.getProjectName() == castOther.getProjectName())
						|| (this.getProjectName() != null && castOther.getProjectName() != null
								&& this.getProjectName().equals(castOther.getProjectName())))
				&& ((this.getProjectStartDate() == castOther.getProjectStartDate())
						|| (this.getProjectStartDate() != null && castOther.getProjectStartDate() != null
								&& this.getProjectStartDate().equals(castOther.getProjectStartDate())))
				&& ((this.getProjectEndDate() == castOther.getProjectEndDate())
						|| (this.getProjectEndDate() != null && castOther.getProjectEndDate() != null
								&& this.getProjectEndDate().equals(castOther.getProjectEndDate())))
				&& ((this.getDomain() == castOther.getDomain()) || (this.getDomain() != null
						&& castOther.getDomain() != null && this.getDomain().equals(castOther.getDomain())))
				&& ((this.getSubDomain() == castOther.getSubDomain()) || (this.getSubDomain() != null
						&& castOther.getSubDomain() != null && this.getSubDomain().equals(castOther.getSubDomain())))
				&& ((this.getTechnology() == castOther.getTechnology()) || (this.getTechnology() != null
						&& castOther.getTechnology() != null && this.getTechnology().equals(castOther.getTechnology())))
				&& ((this.getServiceArea() == castOther.getServiceArea())
						|| (this.getServiceArea() != null && castOther.getServiceArea() != null
								&& this.getServiceArea().equals(castOther.getServiceArea())))
				&& ((this.getSubServiceArea() == castOther.getSubServiceArea())
						|| (this.getSubServiceArea() != null && castOther.getSubServiceArea() != null
								&& this.getSubServiceArea().equals(castOther.getSubServiceArea())))
				&& ((this.getPositionStatus() == castOther.getPositionStatus())
						|| (this.getPositionStatus() != null && castOther.getPositionStatus() != null
								&& this.getPositionStatus().equals(castOther.getPositionStatus())))
				&& ((this.getSignum() == castOther.getSignum()) || (this.getSignum() != null
						&& castOther.getSignum() != null && this.getSignum().equals(castOther.getSignum())))
				&& ((this.getEmployeeName() == castOther.getEmployeeName())
						|| (this.getEmployeeName() != null && castOther.getEmployeeName() != null
								&& this.getEmployeeName().equals(castOther.getEmployeeName())))
				&& (this.getPersonnelNumber() == castOther.getPersonnelNumber())
				&& (this.getWorkEffortId() == castOther.getWorkEffortId())
				&& ((this.getManagerSignum() == castOther.getManagerSignum())
						|| (this.getManagerSignum() != null && castOther.getManagerSignum() != null
								&& this.getManagerSignum().equals(castOther.getManagerSignum())))
				&& ((this.getPositionStartDate() == castOther.getPositionStartDate())
						|| (this.getPositionStartDate() != null && castOther.getPositionStartDate() != null
								&& this.getPositionStartDate().equals(castOther.getPositionStartDate())))
				&& ((this.getPositionEndDate() == castOther.getPositionEndDate())
						|| (this.getPositionEndDate() != null && castOther.getPositionEndDate() != null
								&& this.getPositionEndDate().equals(castOther.getPositionEndDate())))
				&& ((this.getRampDownWeek() == castOther.getRampDownWeek())
						|| (this.getRampDownWeek() != null && castOther.getRampDownWeek() != null
								&& this.getRampDownWeek().equals(castOther.getRampDownWeek())))
				&& ((this.getRampDownYear() == castOther.getRampDownYear())
						|| (this.getRampDownYear() != null && castOther.getRampDownYear() != null
								&& this.getRampDownYear().equals(castOther.getRampDownYear())))
				&& ((this.getCertificateName() == castOther.getCertificateName())
						|| (this.getCertificateName() != null && castOther.getCertificateName() != null
								&& this.getCertificateName().equals(castOther.getCertificateName())))
				&& ((this.getRegion() == castOther.getRegion()) || (this.getRegion() != null
						&& castOther.getRegion() != null && this.getRegion().equals(castOther.getRegion())))
				&& ((this.getMarketArea() == castOther.getMarketArea()) || (this.getMarketArea() != null
						&& castOther.getMarketArea() != null && this.getMarketArea().equals(castOther.getMarketArea())))
				&& ((this.getMarketAreaRegion() == castOther.getMarketAreaRegion())
						|| (this.getMarketAreaRegion() != null && castOther.getMarketAreaRegion() != null
								&& this.getMarketAreaRegion().equals(castOther.getMarketAreaRegion())))
				&& ((this.getCountryName() == castOther.getCountryName())
						|| (this.getCountryName() != null && castOther.getCountryName() != null
								&& this.getCountryName().equals(castOther.getCountryName())))
				&& ((this.getOrgUnit() == castOther.getOrgUnit()) || (this.getOrgUnit() != null
						&& castOther.getOrgUnit() != null && this.getOrgUnit().equals(castOther.getOrgUnit())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getResourcePositionId();
		result = 37 * result + (getProjectId() == null ? 0 : this.getProjectId().hashCode());
		result = 37 * result + (getProjectName() == null ? 0 : this.getProjectName().hashCode());
		result = 37 * result + (getProjectStartDate() == null ? 0 : this.getProjectStartDate().hashCode());
		result = 37 * result + (getProjectEndDate() == null ? 0 : this.getProjectEndDate().hashCode());
		result = 37 * result + (getDomain() == null ? 0 : this.getDomain().hashCode());
		result = 37 * result + (getSubDomain() == null ? 0 : this.getSubDomain().hashCode());
		result = 37 * result + (getTechnology() == null ? 0 : this.getTechnology().hashCode());
		result = 37 * result + (getServiceArea() == null ? 0 : this.getServiceArea().hashCode());
		result = 37 * result + (getSubServiceArea() == null ? 0 : this.getSubServiceArea().hashCode());
		result = 37 * result + (getPositionStatus() == null ? 0 : this.getPositionStatus().hashCode());
		result = 37 * result + (getSignum() == null ? 0 : this.getSignum().hashCode());
		result = 37 * result + (getEmployeeName() == null ? 0 : this.getEmployeeName().hashCode());
		result = 37 * result + this.getPersonnelNumber();
		result = 37 * result + this.getWorkEffortId();
		result = 37 * result + (getManagerSignum() == null ? 0 : this.getManagerSignum().hashCode());
		result = 37 * result + (getPositionStartDate() == null ? 0 : this.getPositionStartDate().hashCode());
		result = 37 * result + (getPositionEndDate() == null ? 0 : this.getPositionEndDate().hashCode());
		result = 37 * result + (getRampDownWeek() == null ? 0 : this.getRampDownWeek().hashCode());
		result = 37 * result + (getRampDownYear() == null ? 0 : this.getRampDownYear().hashCode());
		result = 37 * result + (getCertificateName() == null ? 0 : this.getCertificateName().hashCode());
		result = 37 * result + (getRegion() == null ? 0 : this.getRegion().hashCode());
		result = 37 * result + (getMarketArea() == null ? 0 : this.getMarketArea().hashCode());
		result = 37 * result + (getMarketAreaRegion() == null ? 0 : this.getMarketAreaRegion().hashCode());
		result = 37 * result + (getCountryName() == null ? 0 : this.getCountryName().hashCode());
		result = 37 * result + (getOrgUnit() == null ? 0 : this.getOrgUnit().hashCode());
		return result;
	}

}