package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblWorkOrderId generated by hbm2java
 */
@Embeddable
public class TblWorkOrderId implements java.io.Serializable {

	private int woid;
	private Integer woplanId;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Date actualStartDate;
	private Date actualEndDate;
	private String signumId;
	private String status;
	private Date closedOn;
	private Boolean active;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String priority;
	private String market;
	private Integer vendor;
	private Integer adhocWoid;
	private String deliveryStatus;
	private String failureReason;
	private String description;
	private String statusComment;
	private Integer parentWorkOrderId;
	private Integer workFlowVersion;
	private Integer projectId;

	public TblWorkOrderId() {
	}

	public TblWorkOrderId(int woid) {
		this.woid = woid;
	}

	public TblWorkOrderId(int woid, Integer woplanId, Date plannedStartDate, Date plannedEndDate, Date actualStartDate,
			Date actualEndDate, String signumId, String status, Date closedOn, Boolean active, String createdBy,
			Date createdDate, String lastModifiedBy, Date lastModifiedDate, String priority, String market,
			Integer vendor, Integer adhocWoid, String deliveryStatus, String failureReason, String description,
			String statusComment, Integer parentWorkOrderId, Integer workFlowVersion, Integer projectId) {
		this.woid = woid;
		this.woplanId = woplanId;
		this.plannedStartDate = plannedStartDate;
		this.plannedEndDate = plannedEndDate;
		this.actualStartDate = actualStartDate;
		this.actualEndDate = actualEndDate;
		this.signumId = signumId;
		this.status = status;
		this.closedOn = closedOn;
		this.active = active;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.priority = priority;
		this.market = market;
		this.vendor = vendor;
		this.adhocWoid = adhocWoid;
		this.deliveryStatus = deliveryStatus;
		this.failureReason = failureReason;
		this.description = description;
		this.statusComment = statusComment;
		this.parentWorkOrderId = parentWorkOrderId;
		this.workFlowVersion = workFlowVersion;
		this.projectId = projectId;
	}

	@Column(name = "WOID", nullable = false)
	public int getWoid() {
		return this.woid;
	}

	public void setWoid(int woid) {
		this.woid = woid;
	}

	@Column(name = "WOPlanID")
	public Integer getWoplanId() {
		return this.woplanId;
	}

	public void setWoplanId(Integer woplanId) {
		this.woplanId = woplanId;
	}

	@Column(name = "PlannedStartDate", length = 23)
	public Date getPlannedStartDate() {
		return this.plannedStartDate;
	}

	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	@Column(name = "PlannedEndDate", length = 23)
	public Date getPlannedEndDate() {
		return this.plannedEndDate;
	}

	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}

	@Column(name = "ActualStartDate", length = 23)
	public Date getActualStartDate() {
		return this.actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	@Column(name = "ActualEndDate", length = 23)
	public Date getActualEndDate() {
		return this.actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	@Column(name = "SignumID", length = 1024)
	public String getSignumId() {
		return this.signumId;
	}

	public void setSignumId(String signumId) {
		this.signumId = signumId;
	}

	@Column(name = "Status", length = 512)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "ClosedOn", length = 23)
	public Date getClosedOn() {
		return this.closedOn;
	}

	public void setClosedOn(Date closedOn) {
		this.closedOn = closedOn;
	}

	@Column(name = "Active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	@Column(name = "LastModifiedBy", length = 1024)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Column(name = "LastModifiedDate", length = 23)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "Priority", length = 30)
	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	@Column(name = "Market", length = 1024)
	public String getMarket() {
		return this.market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	@Column(name = "Vendor")
	public Integer getVendor() {
		return this.vendor;
	}

	public void setVendor(Integer vendor) {
		this.vendor = vendor;
	}

	@Column(name = "AdhocWOID")
	public Integer getAdhocWoid() {
		return this.adhocWoid;
	}

	public void setAdhocWoid(Integer adhocWoid) {
		this.adhocWoid = adhocWoid;
	}

	@Column(name = "DeliveryStatus", length = 1024)
	public String getDeliveryStatus() {
		return this.deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	@Column(name = "FailureReason", length = 1024)
	public String getFailureReason() {
		return this.failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	@Column(name = "Description", length = 1024)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "StatusComment", length = 5000)
	public String getStatusComment() {
		return this.statusComment;
	}

	public void setStatusComment(String statusComment) {
		this.statusComment = statusComment;
	}

	@Column(name = "ParentWorkOrderID")
	public Integer getParentWorkOrderId() {
		return this.parentWorkOrderId;
	}

	public void setParentWorkOrderId(Integer parentWorkOrderId) {
		this.parentWorkOrderId = parentWorkOrderId;
	}

	@Column(name = "WorkFlowVersion")
	public Integer getWorkFlowVersion() {
		return this.workFlowVersion;
	}

	public void setWorkFlowVersion(Integer workFlowVersion) {
		this.workFlowVersion = workFlowVersion;
	}

	@Column(name = "ProjectID")
	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblWorkOrderId))
			return false;
		TblWorkOrderId castOther = (TblWorkOrderId) other;

		return (this.getWoid() == castOther.getWoid())
				&& ((this.getWoplanId() == castOther.getWoplanId()) || (this.getWoplanId() != null
						&& castOther.getWoplanId() != null && this.getWoplanId().equals(castOther.getWoplanId())))
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
				&& ((this.getSignumId() == castOther.getSignumId()) || (this.getSignumId() != null
						&& castOther.getSignumId() != null && this.getSignumId().equals(castOther.getSignumId())))
				&& ((this.getStatus() == castOther.getStatus()) || (this.getStatus() != null
						&& castOther.getStatus() != null && this.getStatus().equals(castOther.getStatus())))
				&& ((this.getClosedOn() == castOther.getClosedOn()) || (this.getClosedOn() != null
						&& castOther.getClosedOn() != null && this.getClosedOn().equals(castOther.getClosedOn())))
				&& ((this.getActive() == castOther.getActive()) || (this.getActive() != null
						&& castOther.getActive() != null && this.getActive().equals(castOther.getActive())))
				&& ((this.getCreatedBy() == castOther.getCreatedBy()) || (this.getCreatedBy() != null
						&& castOther.getCreatedBy() != null && this.getCreatedBy().equals(castOther.getCreatedBy())))
				&& ((this.getCreatedDate() == castOther.getCreatedDate())
						|| (this.getCreatedDate() != null && castOther.getCreatedDate() != null
								&& this.getCreatedDate().equals(castOther.getCreatedDate())))
				&& ((this.getLastModifiedBy() == castOther.getLastModifiedBy())
						|| (this.getLastModifiedBy() != null && castOther.getLastModifiedBy() != null
								&& this.getLastModifiedBy().equals(castOther.getLastModifiedBy())))
				&& ((this.getLastModifiedDate() == castOther.getLastModifiedDate())
						|| (this.getLastModifiedDate() != null && castOther.getLastModifiedDate() != null
								&& this.getLastModifiedDate().equals(castOther.getLastModifiedDate())))
				&& ((this.getPriority() == castOther.getPriority()) || (this.getPriority() != null
						&& castOther.getPriority() != null && this.getPriority().equals(castOther.getPriority())))
				&& ((this.getMarket() == castOther.getMarket()) || (this.getMarket() != null
						&& castOther.getMarket() != null && this.getMarket().equals(castOther.getMarket())))
				&& ((this.getVendor() == castOther.getVendor()) || (this.getVendor() != null
						&& castOther.getVendor() != null && this.getVendor().equals(castOther.getVendor())))
				&& ((this.getAdhocWoid() == castOther.getAdhocWoid()) || (this.getAdhocWoid() != null
						&& castOther.getAdhocWoid() != null && this.getAdhocWoid().equals(castOther.getAdhocWoid())))
				&& ((this.getDeliveryStatus() == castOther.getDeliveryStatus())
						|| (this.getDeliveryStatus() != null && castOther.getDeliveryStatus() != null
								&& this.getDeliveryStatus().equals(castOther.getDeliveryStatus())))
				&& ((this.getFailureReason() == castOther.getFailureReason())
						|| (this.getFailureReason() != null && castOther.getFailureReason() != null
								&& this.getFailureReason().equals(castOther.getFailureReason())))
				&& ((this.getDescription() == castOther.getDescription())
						|| (this.getDescription() != null && castOther.getDescription() != null
								&& this.getDescription().equals(castOther.getDescription())))
				&& ((this.getStatusComment() == castOther.getStatusComment())
						|| (this.getStatusComment() != null && castOther.getStatusComment() != null
								&& this.getStatusComment().equals(castOther.getStatusComment())))
				&& ((this.getParentWorkOrderId() == castOther.getParentWorkOrderId())
						|| (this.getParentWorkOrderId() != null && castOther.getParentWorkOrderId() != null
								&& this.getParentWorkOrderId().equals(castOther.getParentWorkOrderId())))
				&& ((this.getWorkFlowVersion() == castOther.getWorkFlowVersion())
						|| (this.getWorkFlowVersion() != null && castOther.getWorkFlowVersion() != null
								&& this.getWorkFlowVersion().equals(castOther.getWorkFlowVersion())))
				&& ((this.getProjectId() == castOther.getProjectId()) || (this.getProjectId() != null
						&& castOther.getProjectId() != null && this.getProjectId().equals(castOther.getProjectId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getWoid();
		result = 37 * result + (getWoplanId() == null ? 0 : this.getWoplanId().hashCode());
		result = 37 * result + (getPlannedStartDate() == null ? 0 : this.getPlannedStartDate().hashCode());
		result = 37 * result + (getPlannedEndDate() == null ? 0 : this.getPlannedEndDate().hashCode());
		result = 37 * result + (getActualStartDate() == null ? 0 : this.getActualStartDate().hashCode());
		result = 37 * result + (getActualEndDate() == null ? 0 : this.getActualEndDate().hashCode());
		result = 37 * result + (getSignumId() == null ? 0 : this.getSignumId().hashCode());
		result = 37 * result + (getStatus() == null ? 0 : this.getStatus().hashCode());
		result = 37 * result + (getClosedOn() == null ? 0 : this.getClosedOn().hashCode());
		result = 37 * result + (getActive() == null ? 0 : this.getActive().hashCode());
		result = 37 * result + (getCreatedBy() == null ? 0 : this.getCreatedBy().hashCode());
		result = 37 * result + (getCreatedDate() == null ? 0 : this.getCreatedDate().hashCode());
		result = 37 * result + (getLastModifiedBy() == null ? 0 : this.getLastModifiedBy().hashCode());
		result = 37 * result + (getLastModifiedDate() == null ? 0 : this.getLastModifiedDate().hashCode());
		result = 37 * result + (getPriority() == null ? 0 : this.getPriority().hashCode());
		result = 37 * result + (getMarket() == null ? 0 : this.getMarket().hashCode());
		result = 37 * result + (getVendor() == null ? 0 : this.getVendor().hashCode());
		result = 37 * result + (getAdhocWoid() == null ? 0 : this.getAdhocWoid().hashCode());
		result = 37 * result + (getDeliveryStatus() == null ? 0 : this.getDeliveryStatus().hashCode());
		result = 37 * result + (getFailureReason() == null ? 0 : this.getFailureReason().hashCode());
		result = 37 * result + (getDescription() == null ? 0 : this.getDescription().hashCode());
		result = 37 * result + (getStatusComment() == null ? 0 : this.getStatusComment().hashCode());
		result = 37 * result + (getParentWorkOrderId() == null ? 0 : this.getParentWorkOrderId().hashCode());
		result = 37 * result + (getWorkFlowVersion() == null ? 0 : this.getWorkFlowVersion().hashCode());
		result = 37 * result + (getProjectId() == null ? 0 : this.getProjectId().hashCode());
		return result;
	}

}
