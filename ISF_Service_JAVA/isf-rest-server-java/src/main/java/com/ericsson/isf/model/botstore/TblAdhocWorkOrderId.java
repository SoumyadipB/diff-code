package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblAdhocWorkOrderId generated by hbm2java
 */
@Embeddable
public class TblAdhocWorkOrderId implements java.io.Serializable {

	private int adhocWoid;
	private String woName;
	private Integer projectId;
	private Integer domainId;
	private Integer serviceAreaId;
	private Integer technologyId;
	private Integer subActivityId;
	private Date startDate;
	private Double avgEstdEffort;
	private String market;
	private String vendor;
	private String priority;
	private String createdBy;
	private Date createdDate;
	private Boolean active;
	private Date startTime;
	private String assignedTo;
	private Double sla;
	private Integer scopeId;

	public TblAdhocWorkOrderId() {
	}

	public TblAdhocWorkOrderId(int adhocWoid) {
		this.adhocWoid = adhocWoid;
	}

	public TblAdhocWorkOrderId(int adhocWoid, String woName, Integer projectId, Integer domainId, Integer serviceAreaId,
			Integer technologyId, Integer subActivityId, Date startDate, Double avgEstdEffort, String market,
			String vendor, String priority, String createdBy, Date createdDate, Boolean active, Date startTime,
			String assignedTo, Double sla, Integer scopeId) {
		this.adhocWoid = adhocWoid;
		this.woName = woName;
		this.projectId = projectId;
		this.domainId = domainId;
		this.serviceAreaId = serviceAreaId;
		this.technologyId = technologyId;
		this.subActivityId = subActivityId;
		this.startDate = startDate;
		this.avgEstdEffort = avgEstdEffort;
		this.market = market;
		this.vendor = vendor;
		this.priority = priority;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.active = active;
		this.startTime = startTime;
		this.assignedTo = assignedTo;
		this.sla = sla;
		this.scopeId = scopeId;
	}

	@Column(name = "AdhocWOID", nullable = false)
	public int getAdhocWoid() {
		return this.adhocWoid;
	}

	public void setAdhocWoid(int adhocWoid) {
		this.adhocWoid = adhocWoid;
	}

	@Column(name = "WoName", length = 1024)
	public String getWoName() {
		return this.woName;
	}

	public void setWoName(String woName) {
		this.woName = woName;
	}

	@Column(name = "ProjectID")
	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Column(name = "DomainID")
	public Integer getDomainId() {
		return this.domainId;
	}

	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}

	@Column(name = "ServiceAreaID")
	public Integer getServiceAreaId() {
		return this.serviceAreaId;
	}

	public void setServiceAreaId(Integer serviceAreaId) {
		this.serviceAreaId = serviceAreaId;
	}

	@Column(name = "TechnologyID")
	public Integer getTechnologyId() {
		return this.technologyId;
	}

	public void setTechnologyId(Integer technologyId) {
		this.technologyId = technologyId;
	}

	@Column(name = "SubActivityID")
	public Integer getSubActivityId() {
		return this.subActivityId;
	}

	public void setSubActivityId(Integer subActivityId) {
		this.subActivityId = subActivityId;
	}

	@Column(name = "StartDate", length = 10)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "AvgEstdEffort", precision = 53, scale = 0)
	public Double getAvgEstdEffort() {
		return this.avgEstdEffort;
	}

	public void setAvgEstdEffort(Double avgEstdEffort) {
		this.avgEstdEffort = avgEstdEffort;
	}

	@Column(name = "Market", length = 1024)
	public String getMarket() {
		return this.market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	@Column(name = "Vendor", length = 512)
	public String getVendor() {
		return this.vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	@Column(name = "Priority", length = 1024)
	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	@Column(name = "CreatedBy", length = 1024)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CreatedDate", length = 23)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "Active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "StartTime", length = 16)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "AssignedTO")
	public String getAssignedTo() {
		return this.assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	@Column(name = "SLA", precision = 53, scale = 0)
	public Double getSla() {
		return this.sla;
	}

	public void setSla(Double sla) {
		this.sla = sla;
	}

	@Column(name = "ScopeID")
	public Integer getScopeId() {
		return this.scopeId;
	}

	public void setScopeId(Integer scopeId) {
		this.scopeId = scopeId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblAdhocWorkOrderId))
			return false;
		TblAdhocWorkOrderId castOther = (TblAdhocWorkOrderId) other;

		return (this.getAdhocWoid() == castOther.getAdhocWoid())
				&& ((this.getWoName() == castOther.getWoName()) || (this.getWoName() != null
						&& castOther.getWoName() != null && this.getWoName().equals(castOther.getWoName())))
				&& ((this.getProjectId() == castOther.getProjectId()) || (this.getProjectId() != null
						&& castOther.getProjectId() != null && this.getProjectId().equals(castOther.getProjectId())))
				&& ((this.getDomainId() == castOther.getDomainId()) || (this.getDomainId() != null
						&& castOther.getDomainId() != null && this.getDomainId().equals(castOther.getDomainId())))
				&& ((this.getServiceAreaId() == castOther.getServiceAreaId())
						|| (this.getServiceAreaId() != null && castOther.getServiceAreaId() != null
								&& this.getServiceAreaId().equals(castOther.getServiceAreaId())))
				&& ((this.getTechnologyId() == castOther.getTechnologyId())
						|| (this.getTechnologyId() != null && castOther.getTechnologyId() != null
								&& this.getTechnologyId().equals(castOther.getTechnologyId())))
				&& ((this.getSubActivityId() == castOther.getSubActivityId())
						|| (this.getSubActivityId() != null && castOther.getSubActivityId() != null
								&& this.getSubActivityId().equals(castOther.getSubActivityId())))
				&& ((this.getStartDate() == castOther.getStartDate()) || (this.getStartDate() != null
						&& castOther.getStartDate() != null && this.getStartDate().equals(castOther.getStartDate())))
				&& ((this.getAvgEstdEffort() == castOther.getAvgEstdEffort())
						|| (this.getAvgEstdEffort() != null && castOther.getAvgEstdEffort() != null
								&& this.getAvgEstdEffort().equals(castOther.getAvgEstdEffort())))
				&& ((this.getMarket() == castOther.getMarket()) || (this.getMarket() != null
						&& castOther.getMarket() != null && this.getMarket().equals(castOther.getMarket())))
				&& ((this.getVendor() == castOther.getVendor()) || (this.getVendor() != null
						&& castOther.getVendor() != null && this.getVendor().equals(castOther.getVendor())))
				&& ((this.getPriority() == castOther.getPriority()) || (this.getPriority() != null
						&& castOther.getPriority() != null && this.getPriority().equals(castOther.getPriority())))
				&& ((this.getCreatedBy() == castOther.getCreatedBy()) || (this.getCreatedBy() != null
						&& castOther.getCreatedBy() != null && this.getCreatedBy().equals(castOther.getCreatedBy())))
				&& ((this.getCreatedDate() == castOther.getCreatedDate())
						|| (this.getCreatedDate() != null && castOther.getCreatedDate() != null
								&& this.getCreatedDate().equals(castOther.getCreatedDate())))
				&& ((this.getActive() == castOther.getActive()) || (this.getActive() != null
						&& castOther.getActive() != null && this.getActive().equals(castOther.getActive())))
				&& ((this.getStartTime() == castOther.getStartTime()) || (this.getStartTime() != null
						&& castOther.getStartTime() != null && this.getStartTime().equals(castOther.getStartTime())))
				&& ((this.getAssignedTo() == castOther.getAssignedTo()) || (this.getAssignedTo() != null
						&& castOther.getAssignedTo() != null && this.getAssignedTo().equals(castOther.getAssignedTo())))
				&& ((this.getSla() == castOther.getSla()) || (this.getSla() != null && castOther.getSla() != null
						&& this.getSla().equals(castOther.getSla())))
				&& ((this.getScopeId() == castOther.getScopeId()) || (this.getScopeId() != null
						&& castOther.getScopeId() != null && this.getScopeId().equals(castOther.getScopeId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getAdhocWoid();
		result = 37 * result + (getWoName() == null ? 0 : this.getWoName().hashCode());
		result = 37 * result + (getProjectId() == null ? 0 : this.getProjectId().hashCode());
		result = 37 * result + (getDomainId() == null ? 0 : this.getDomainId().hashCode());
		result = 37 * result + (getServiceAreaId() == null ? 0 : this.getServiceAreaId().hashCode());
		result = 37 * result + (getTechnologyId() == null ? 0 : this.getTechnologyId().hashCode());
		result = 37 * result + (getSubActivityId() == null ? 0 : this.getSubActivityId().hashCode());
		result = 37 * result + (getStartDate() == null ? 0 : this.getStartDate().hashCode());
		result = 37 * result + (getAvgEstdEffort() == null ? 0 : this.getAvgEstdEffort().hashCode());
		result = 37 * result + (getMarket() == null ? 0 : this.getMarket().hashCode());
		result = 37 * result + (getVendor() == null ? 0 : this.getVendor().hashCode());
		result = 37 * result + (getPriority() == null ? 0 : this.getPriority().hashCode());
		result = 37 * result + (getCreatedBy() == null ? 0 : this.getCreatedBy().hashCode());
		result = 37 * result + (getCreatedDate() == null ? 0 : this.getCreatedDate().hashCode());
		result = 37 * result + (getActive() == null ? 0 : this.getActive().hashCode());
		result = 37 * result + (getStartTime() == null ? 0 : this.getStartTime().hashCode());
		result = 37 * result + (getAssignedTo() == null ? 0 : this.getAssignedTo().hashCode());
		result = 37 * result + (getSla() == null ? 0 : this.getSla().hashCode());
		result = 37 * result + (getScopeId() == null ? 0 : this.getScopeId().hashCode());
		return result;
	}

}
