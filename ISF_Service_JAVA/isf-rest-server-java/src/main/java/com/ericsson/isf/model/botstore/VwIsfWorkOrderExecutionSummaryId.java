package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * VwIsfWorkOrderExecutionSummaryId generated by hbm2java
 */
@Embeddable
public class VwIsfWorkOrderExecutionSummaryId implements java.io.Serializable {

	private Integer marketAreaId;
	private Serializable marketAreaName;
	private Integer projectId;
	private Serializable projectName;
	private Integer customerId;
	private Serializable customerName;
	private Integer technologyId;
	private String technology;
	private Integer domainId;
	private String domain;
	private String subdomain;
	private Integer subActivityId;
	private String activity;
	private String subActivity;
	private Integer woid;
	private Integer woplanId;
	private String woActualStartDate;
	private String woActualEndDate;
	private String woPlannedStartDate;
	private String woPlannedEndDate;
	private String woSignum;
	private String woStatus;
	private String executionType;
	private Integer botId;
	private Integer taskId;
	private String task;
	private int bookingId;
	private Integer parentBookingDetailsId;
	private String bookStartDate;
	private String bookEndDate;
	private Double hours;
	private String type;
	private String status;
	private String signumId;
	private Integer workFlowVersion;
	private String reason;
	private String outputLink;

	public VwIsfWorkOrderExecutionSummaryId() {
	}

	public VwIsfWorkOrderExecutionSummaryId(int bookingId) {
		this.bookingId = bookingId;
	}

	public VwIsfWorkOrderExecutionSummaryId(Integer marketAreaId, Serializable marketAreaName, Integer projectId,
			Serializable projectName, Integer customerId, Serializable customerName, Integer technologyId,
			String technology, Integer domainId, String domain, String subdomain, Integer subActivityId,
			String activity, String subActivity, Integer woid, Integer woplanId, String woActualStartDate,
			String woActualEndDate, String woPlannedStartDate, String woPlannedEndDate, String woSignum,
			String woStatus, String executionType, Integer botId, Integer taskId, String task, int bookingId,
			Integer parentBookingDetailsId, String bookStartDate, String bookEndDate, Double hours, String type,
			String status, String signumId, Integer workFlowVersion, String reason, String outputLink) {
		this.marketAreaId = marketAreaId;
		this.marketAreaName = marketAreaName;
		this.projectId = projectId;
		this.projectName = projectName;
		this.customerId = customerId;
		this.customerName = customerName;
		this.technologyId = technologyId;
		this.technology = technology;
		this.domainId = domainId;
		this.domain = domain;
		this.subdomain = subdomain;
		this.subActivityId = subActivityId;
		this.activity = activity;
		this.subActivity = subActivity;
		this.woid = woid;
		this.woplanId = woplanId;
		this.woActualStartDate = woActualStartDate;
		this.woActualEndDate = woActualEndDate;
		this.woPlannedStartDate = woPlannedStartDate;
		this.woPlannedEndDate = woPlannedEndDate;
		this.woSignum = woSignum;
		this.woStatus = woStatus;
		this.executionType = executionType;
		this.botId = botId;
		this.taskId = taskId;
		this.task = task;
		this.bookingId = bookingId;
		this.parentBookingDetailsId = parentBookingDetailsId;
		this.bookStartDate = bookStartDate;
		this.bookEndDate = bookEndDate;
		this.hours = hours;
		this.type = type;
		this.status = status;
		this.signumId = signumId;
		this.workFlowVersion = workFlowVersion;
		this.reason = reason;
		this.outputLink = outputLink;
	}

	@Column(name = "MarketAreaID")
	public Integer getMarketAreaId() {
		return this.marketAreaId;
	}

	public void setMarketAreaId(Integer marketAreaId) {
		this.marketAreaId = marketAreaId;
	}

	@Column(name = "MarketAreaName")
	public Serializable getMarketAreaName() {
		return this.marketAreaName;
	}

	public void setMarketAreaName(Serializable marketAreaName) {
		this.marketAreaName = marketAreaName;
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

	@Column(name = "CustomerID")
	public Integer getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@Column(name = "CustomerName")
	public Serializable getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(Serializable customerName) {
		this.customerName = customerName;
	}

	@Column(name = "TechnologyID")
	public Integer getTechnologyId() {
		return this.technologyId;
	}

	public void setTechnologyId(Integer technologyId) {
		this.technologyId = technologyId;
	}

	@Column(name = "Technology", length = 128)
	public String getTechnology() {
		return this.technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	@Column(name = "DomainID")
	public Integer getDomainId() {
		return this.domainId;
	}

	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}

	@Column(name = "Domain", length = 128)
	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "subdomain", length = 128)
	public String getSubdomain() {
		return this.subdomain;
	}

	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}

	@Column(name = "SubActivityID")
	public Integer getSubActivityId() {
		return this.subActivityId;
	}

	public void setSubActivityId(Integer subActivityId) {
		this.subActivityId = subActivityId;
	}

	@Column(name = "Activity", length = 512)
	public String getActivity() {
		return this.activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	@Column(name = "SubActivity", length = 512)
	public String getSubActivity() {
		return this.subActivity;
	}

	public void setSubActivity(String subActivity) {
		this.subActivity = subActivity;
	}

	@Column(name = "WOID")
	public Integer getWoid() {
		return this.woid;
	}

	public void setWoid(Integer woid) {
		this.woid = woid;
	}

	@Column(name = "WOPlanID")
	public Integer getWoplanId() {
		return this.woplanId;
	}

	public void setWoplanId(Integer woplanId) {
		this.woplanId = woplanId;
	}

	@Column(name = "WO Actual start Date", length = 120)
	public String getWoActualStartDate() {
		return this.woActualStartDate;
	}

	public void setWoActualStartDate(String woActualStartDate) {
		this.woActualStartDate = woActualStartDate;
	}

	@Column(name = "WO Actual End Date", length = 120)
	public String getWoActualEndDate() {
		return this.woActualEndDate;
	}

	public void setWoActualEndDate(String woActualEndDate) {
		this.woActualEndDate = woActualEndDate;
	}

	@Column(name = "WO Planned Start Date", length = 120)
	public String getWoPlannedStartDate() {
		return this.woPlannedStartDate;
	}

	public void setWoPlannedStartDate(String woPlannedStartDate) {
		this.woPlannedStartDate = woPlannedStartDate;
	}

	@Column(name = "WO Planned End Date", length = 120)
	public String getWoPlannedEndDate() {
		return this.woPlannedEndDate;
	}

	public void setWoPlannedEndDate(String woPlannedEndDate) {
		this.woPlannedEndDate = woPlannedEndDate;
	}

	@Column(name = "WO Signum", length = 1024)
	public String getWoSignum() {
		return this.woSignum;
	}

	public void setWoSignum(String woSignum) {
		this.woSignum = woSignum;
	}

	@Column(name = "WO Status", length = 512)
	public String getWoStatus() {
		return this.woStatus;
	}

	public void setWoStatus(String woStatus) {
		this.woStatus = woStatus;
	}

	@Column(name = "Execution Type", length = 1024)
	public String getExecutionType() {
		return this.executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	@Column(name = "BOT Id")
	public Integer getBotId() {
		return this.botId;
	}

	public void setBotId(Integer botId) {
		this.botId = botId;
	}

	@Column(name = "TaskID")
	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	@Column(name = "Task", length = 1024)
	public String getTask() {
		return this.task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	@Column(name = "BookingID", nullable = false)
	public int getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	@Column(name = "ParentBookingDetailsID")
	public Integer getParentBookingDetailsId() {
		return this.parentBookingDetailsId;
	}

	public void setParentBookingDetailsId(Integer parentBookingDetailsId) {
		this.parentBookingDetailsId = parentBookingDetailsId;
	}

	@Column(name = "Book Start Date", length = 120)
	public String getBookStartDate() {
		return this.bookStartDate;
	}

	public void setBookStartDate(String bookStartDate) {
		this.bookStartDate = bookStartDate;
	}

	@Column(name = "Book End Date", length = 120)
	public String getBookEndDate() {
		return this.bookEndDate;
	}

	public void setBookEndDate(String bookEndDate) {
		this.bookEndDate = bookEndDate;
	}

	@Column(name = "Hours", precision = 53, scale = 0)
	public Double getHours() {
		return this.hours;
	}

	public void setHours(Double hours) {
		this.hours = hours;
	}

	@Column(name = "Type", length = 512)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "Status", length = 512)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "SignumID", length = 512)
	public String getSignumId() {
		return this.signumId;
	}

	public void setSignumId(String signumId) {
		this.signumId = signumId;
	}

	@Column(name = "WorkFlowVersion")
	public Integer getWorkFlowVersion() {
		return this.workFlowVersion;
	}

	public void setWorkFlowVersion(Integer workFlowVersion) {
		this.workFlowVersion = workFlowVersion;
	}

	@Column(name = "Reason", length = 1024)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "OutputLink")
	public String getOutputLink() {
		return this.outputLink;
	}

	public void setOutputLink(String outputLink) {
		this.outputLink = outputLink;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VwIsfWorkOrderExecutionSummaryId))
			return false;
		VwIsfWorkOrderExecutionSummaryId castOther = (VwIsfWorkOrderExecutionSummaryId) other;

		return ((this.getMarketAreaId() == castOther.getMarketAreaId()) || (this.getMarketAreaId() != null
				&& castOther.getMarketAreaId() != null && this.getMarketAreaId().equals(castOther.getMarketAreaId())))
				&& ((this.getMarketAreaName() == castOther.getMarketAreaName())
						|| (this.getMarketAreaName() != null && castOther.getMarketAreaName() != null
								&& this.getMarketAreaName().equals(castOther.getMarketAreaName())))
				&& ((this.getProjectId() == castOther.getProjectId()) || (this.getProjectId() != null
						&& castOther.getProjectId() != null && this.getProjectId().equals(castOther.getProjectId())))
				&& ((this.getProjectName() == castOther.getProjectName())
						|| (this.getProjectName() != null && castOther.getProjectName() != null
								&& this.getProjectName().equals(castOther.getProjectName())))
				&& ((this.getCustomerId() == castOther.getCustomerId()) || (this.getCustomerId() != null
						&& castOther.getCustomerId() != null && this.getCustomerId().equals(castOther.getCustomerId())))
				&& ((this.getCustomerName() == castOther.getCustomerName())
						|| (this.getCustomerName() != null && castOther.getCustomerName() != null
								&& this.getCustomerName().equals(castOther.getCustomerName())))
				&& ((this.getTechnologyId() == castOther.getTechnologyId())
						|| (this.getTechnologyId() != null && castOther.getTechnologyId() != null
								&& this.getTechnologyId().equals(castOther.getTechnologyId())))
				&& ((this.getTechnology() == castOther.getTechnology()) || (this.getTechnology() != null
						&& castOther.getTechnology() != null && this.getTechnology().equals(castOther.getTechnology())))
				&& ((this.getDomainId() == castOther.getDomainId()) || (this.getDomainId() != null
						&& castOther.getDomainId() != null && this.getDomainId().equals(castOther.getDomainId())))
				&& ((this.getDomain() == castOther.getDomain()) || (this.getDomain() != null
						&& castOther.getDomain() != null && this.getDomain().equals(castOther.getDomain())))
				&& ((this.getSubdomain() == castOther.getSubdomain()) || (this.getSubdomain() != null
						&& castOther.getSubdomain() != null && this.getSubdomain().equals(castOther.getSubdomain())))
				&& ((this.getSubActivityId() == castOther.getSubActivityId())
						|| (this.getSubActivityId() != null && castOther.getSubActivityId() != null
								&& this.getSubActivityId().equals(castOther.getSubActivityId())))
				&& ((this.getActivity() == castOther.getActivity()) || (this.getActivity() != null
						&& castOther.getActivity() != null && this.getActivity().equals(castOther.getActivity())))
				&& ((this.getSubActivity() == castOther.getSubActivity())
						|| (this.getSubActivity() != null && castOther.getSubActivity() != null
								&& this.getSubActivity().equals(castOther.getSubActivity())))
				&& ((this.getWoid() == castOther.getWoid()) || (this.getWoid() != null && castOther.getWoid() != null
						&& this.getWoid().equals(castOther.getWoid())))
				&& ((this.getWoplanId() == castOther.getWoplanId()) || (this.getWoplanId() != null
						&& castOther.getWoplanId() != null && this.getWoplanId().equals(castOther.getWoplanId())))
				&& ((this.getWoActualStartDate() == castOther.getWoActualStartDate())
						|| (this.getWoActualStartDate() != null && castOther.getWoActualStartDate() != null
								&& this.getWoActualStartDate().equals(castOther.getWoActualStartDate())))
				&& ((this.getWoActualEndDate() == castOther.getWoActualEndDate())
						|| (this.getWoActualEndDate() != null && castOther.getWoActualEndDate() != null
								&& this.getWoActualEndDate().equals(castOther.getWoActualEndDate())))
				&& ((this.getWoPlannedStartDate() == castOther.getWoPlannedStartDate())
						|| (this.getWoPlannedStartDate() != null && castOther.getWoPlannedStartDate() != null
								&& this.getWoPlannedStartDate().equals(castOther.getWoPlannedStartDate())))
				&& ((this.getWoPlannedEndDate() == castOther.getWoPlannedEndDate())
						|| (this.getWoPlannedEndDate() != null && castOther.getWoPlannedEndDate() != null
								&& this.getWoPlannedEndDate().equals(castOther.getWoPlannedEndDate())))
				&& ((this.getWoSignum() == castOther.getWoSignum()) || (this.getWoSignum() != null
						&& castOther.getWoSignum() != null && this.getWoSignum().equals(castOther.getWoSignum())))
				&& ((this.getWoStatus() == castOther.getWoStatus()) || (this.getWoStatus() != null
						&& castOther.getWoStatus() != null && this.getWoStatus().equals(castOther.getWoStatus())))
				&& ((this.getExecutionType() == castOther.getExecutionType())
						|| (this.getExecutionType() != null && castOther.getExecutionType() != null
								&& this.getExecutionType().equals(castOther.getExecutionType())))
				&& ((this.getBotId() == castOther.getBotId()) || (this.getBotId() != null
						&& castOther.getBotId() != null && this.getBotId().equals(castOther.getBotId())))
				&& ((this.getTaskId() == castOther.getTaskId()) || (this.getTaskId() != null
						&& castOther.getTaskId() != null && this.getTaskId().equals(castOther.getTaskId())))
				&& ((this.getTask() == castOther.getTask()) || (this.getTask() != null && castOther.getTask() != null
						&& this.getTask().equals(castOther.getTask())))
				&& (this.getBookingId() == castOther.getBookingId())
				&& ((this.getParentBookingDetailsId() == castOther.getParentBookingDetailsId())
						|| (this.getParentBookingDetailsId() != null && castOther.getParentBookingDetailsId() != null
								&& this.getParentBookingDetailsId().equals(castOther.getParentBookingDetailsId())))
				&& ((this.getBookStartDate() == castOther.getBookStartDate())
						|| (this.getBookStartDate() != null && castOther.getBookStartDate() != null
								&& this.getBookStartDate().equals(castOther.getBookStartDate())))
				&& ((this.getBookEndDate() == castOther.getBookEndDate())
						|| (this.getBookEndDate() != null && castOther.getBookEndDate() != null
								&& this.getBookEndDate().equals(castOther.getBookEndDate())))
				&& ((this.getHours() == castOther.getHours()) || (this.getHours() != null
						&& castOther.getHours() != null && this.getHours().equals(castOther.getHours())))
				&& ((this.getType() == castOther.getType()) || (this.getType() != null && castOther.getType() != null
						&& this.getType().equals(castOther.getType())))
				&& ((this.getStatus() == castOther.getStatus()) || (this.getStatus() != null
						&& castOther.getStatus() != null && this.getStatus().equals(castOther.getStatus())))
				&& ((this.getSignumId() == castOther.getSignumId()) || (this.getSignumId() != null
						&& castOther.getSignumId() != null && this.getSignumId().equals(castOther.getSignumId())))
				&& ((this.getWorkFlowVersion() == castOther.getWorkFlowVersion())
						|| (this.getWorkFlowVersion() != null && castOther.getWorkFlowVersion() != null
								&& this.getWorkFlowVersion().equals(castOther.getWorkFlowVersion())))
				&& ((this.getReason() == castOther.getReason()) || (this.getReason() != null
						&& castOther.getReason() != null && this.getReason().equals(castOther.getReason())))
				&& ((this.getOutputLink() == castOther.getOutputLink())
						|| (this.getOutputLink() != null && castOther.getOutputLink() != null
								&& this.getOutputLink().equals(castOther.getOutputLink())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getMarketAreaId() == null ? 0 : this.getMarketAreaId().hashCode());
		result = 37 * result + (getMarketAreaName() == null ? 0 : this.getMarketAreaName().hashCode());
		result = 37 * result + (getProjectId() == null ? 0 : this.getProjectId().hashCode());
		result = 37 * result + (getProjectName() == null ? 0 : this.getProjectName().hashCode());
		result = 37 * result + (getCustomerId() == null ? 0 : this.getCustomerId().hashCode());
		result = 37 * result + (getCustomerName() == null ? 0 : this.getCustomerName().hashCode());
		result = 37 * result + (getTechnologyId() == null ? 0 : this.getTechnologyId().hashCode());
		result = 37 * result + (getTechnology() == null ? 0 : this.getTechnology().hashCode());
		result = 37 * result + (getDomainId() == null ? 0 : this.getDomainId().hashCode());
		result = 37 * result + (getDomain() == null ? 0 : this.getDomain().hashCode());
		result = 37 * result + (getSubdomain() == null ? 0 : this.getSubdomain().hashCode());
		result = 37 * result + (getSubActivityId() == null ? 0 : this.getSubActivityId().hashCode());
		result = 37 * result + (getActivity() == null ? 0 : this.getActivity().hashCode());
		result = 37 * result + (getSubActivity() == null ? 0 : this.getSubActivity().hashCode());
		result = 37 * result + (getWoid() == null ? 0 : this.getWoid().hashCode());
		result = 37 * result + (getWoplanId() == null ? 0 : this.getWoplanId().hashCode());
		result = 37 * result + (getWoActualStartDate() == null ? 0 : this.getWoActualStartDate().hashCode());
		result = 37 * result + (getWoActualEndDate() == null ? 0 : this.getWoActualEndDate().hashCode());
		result = 37 * result + (getWoPlannedStartDate() == null ? 0 : this.getWoPlannedStartDate().hashCode());
		result = 37 * result + (getWoPlannedEndDate() == null ? 0 : this.getWoPlannedEndDate().hashCode());
		result = 37 * result + (getWoSignum() == null ? 0 : this.getWoSignum().hashCode());
		result = 37 * result + (getWoStatus() == null ? 0 : this.getWoStatus().hashCode());
		result = 37 * result + (getExecutionType() == null ? 0 : this.getExecutionType().hashCode());
		result = 37 * result + (getBotId() == null ? 0 : this.getBotId().hashCode());
		result = 37 * result + (getTaskId() == null ? 0 : this.getTaskId().hashCode());
		result = 37 * result + (getTask() == null ? 0 : this.getTask().hashCode());
		result = 37 * result + this.getBookingId();
		result = 37 * result + (getParentBookingDetailsId() == null ? 0 : this.getParentBookingDetailsId().hashCode());
		result = 37 * result + (getBookStartDate() == null ? 0 : this.getBookStartDate().hashCode());
		result = 37 * result + (getBookEndDate() == null ? 0 : this.getBookEndDate().hashCode());
		result = 37 * result + (getHours() == null ? 0 : this.getHours().hashCode());
		result = 37 * result + (getType() == null ? 0 : this.getType().hashCode());
		result = 37 * result + (getStatus() == null ? 0 : this.getStatus().hashCode());
		result = 37 * result + (getSignumId() == null ? 0 : this.getSignumId().hashCode());
		result = 37 * result + (getWorkFlowVersion() == null ? 0 : this.getWorkFlowVersion().hashCode());
		result = 37 * result + (getReason() == null ? 0 : this.getReason().hashCode());
		result = 37 * result + (getOutputLink() == null ? 0 : this.getOutputLink().hashCode());
		return result;
	}

}