package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * VwMissingToolIdId generated by hbm2java
 */
@Embeddable
public class VwMissingToolIdId implements java.io.Serializable {

	private Integer woid;
	private Double hours;
	private String type;
	private String status;
	private Integer woplanId;
	private Date woActualStartDate;
	private Date woActualEndDate;
	private Date woPlannedStartDate;
	private Date woPlannedEndDate;
	private String woSignum;
	private String woStatus;
	private Integer workFlowVersion;
	private Integer projectId;
	private Integer subActivityId;
	private String workFlowName;
	private String signumid;
	private Integer taskId;
	private String task;
	private Integer toolId;
	private String executionType;
	private Double avgEstdEffort;
	private Integer botId;
	private int bookingId;
	private Date startDate;
	private Date endDate;
	private Integer parentBookingDetailsId;
	private String reason;
	private String outputLink;
	private Integer stepId;
	private String stepName;

	public VwMissingToolIdId() {
	}

	public VwMissingToolIdId(int bookingId) {
		this.bookingId = bookingId;
	}

	public VwMissingToolIdId(Integer woid, Double hours, String type, String status, Integer woplanId,
			Date woActualStartDate, Date woActualEndDate, Date woPlannedStartDate, Date woPlannedEndDate,
			String woSignum, String woStatus, Integer workFlowVersion, Integer projectId, Integer subActivityId,
			String workFlowName, String signumid, Integer taskId, String task, Integer toolId, String executionType,
			Double avgEstdEffort, Integer botId, int bookingId, Date startDate, Date endDate,
			Integer parentBookingDetailsId, String reason, String outputLink, Integer stepId, String stepName) {
		this.woid = woid;
		this.hours = hours;
		this.type = type;
		this.status = status;
		this.woplanId = woplanId;
		this.woActualStartDate = woActualStartDate;
		this.woActualEndDate = woActualEndDate;
		this.woPlannedStartDate = woPlannedStartDate;
		this.woPlannedEndDate = woPlannedEndDate;
		this.woSignum = woSignum;
		this.woStatus = woStatus;
		this.workFlowVersion = workFlowVersion;
		this.projectId = projectId;
		this.subActivityId = subActivityId;
		this.workFlowName = workFlowName;
		this.signumid = signumid;
		this.taskId = taskId;
		this.task = task;
		this.toolId = toolId;
		this.executionType = executionType;
		this.avgEstdEffort = avgEstdEffort;
		this.botId = botId;
		this.bookingId = bookingId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.parentBookingDetailsId = parentBookingDetailsId;
		this.reason = reason;
		this.outputLink = outputLink;
		this.stepId = stepId;
		this.stepName = stepName;
	}

	@Column(name = "WOID")
	public Integer getWoid() {
		return this.woid;
	}

	public void setWoid(Integer woid) {
		this.woid = woid;
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

	@Column(name = "WOPlanID")
	public Integer getWoplanId() {
		return this.woplanId;
	}

	public void setWoplanId(Integer woplanId) {
		this.woplanId = woplanId;
	}

	@Column(name = "WO Actual start Date", length = 23)
	public Date getWoActualStartDate() {
		return this.woActualStartDate;
	}

	public void setWoActualStartDate(Date woActualStartDate) {
		this.woActualStartDate = woActualStartDate;
	}

	@Column(name = "WO Actual End Date", length = 23)
	public Date getWoActualEndDate() {
		return this.woActualEndDate;
	}

	public void setWoActualEndDate(Date woActualEndDate) {
		this.woActualEndDate = woActualEndDate;
	}

	@Column(name = "WO Planned Start Date", length = 23)
	public Date getWoPlannedStartDate() {
		return this.woPlannedStartDate;
	}

	public void setWoPlannedStartDate(Date woPlannedStartDate) {
		this.woPlannedStartDate = woPlannedStartDate;
	}

	@Column(name = "WO Planned End Date", length = 23)
	public Date getWoPlannedEndDate() {
		return this.woPlannedEndDate;
	}

	public void setWoPlannedEndDate(Date woPlannedEndDate) {
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

	@Column(name = "SubActivityID")
	public Integer getSubActivityId() {
		return this.subActivityId;
	}

	public void setSubActivityId(Integer subActivityId) {
		this.subActivityId = subActivityId;
	}

	@Column(name = "WorkFlowName")
	public String getWorkFlowName() {
		return this.workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	@Column(name = "SIGNUMID", length = 512)
	public String getSignumid() {
		return this.signumid;
	}

	public void setSignumid(String signumid) {
		this.signumid = signumid;
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

	@Column(name = "ToolID")
	public Integer getToolId() {
		return this.toolId;
	}

	public void setToolId(Integer toolId) {
		this.toolId = toolId;
	}

	@Column(name = "ExecutionType", length = 1024)
	public String getExecutionType() {
		return this.executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	@Column(name = "AvgEstdEffort", precision = 53, scale = 0)
	public Double getAvgEstdEffort() {
		return this.avgEstdEffort;
	}

	public void setAvgEstdEffort(Double avgEstdEffort) {
		this.avgEstdEffort = avgEstdEffort;
	}

	@Column(name = "BOT Id")
	public Integer getBotId() {
		return this.botId;
	}

	public void setBotId(Integer botId) {
		this.botId = botId;
	}

	@Column(name = "BookingID", nullable = false)
	public int getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	@Column(name = "StartDate", length = 23)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "EndDate", length = 23)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "ParentBookingDetailsID")
	public Integer getParentBookingDetailsId() {
		return this.parentBookingDetailsId;
	}

	public void setParentBookingDetailsId(Integer parentBookingDetailsId) {
		this.parentBookingDetailsId = parentBookingDetailsId;
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

	@Column(name = "StepID")
	public Integer getStepId() {
		return this.stepId;
	}

	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}

	@Column(name = "StepName", length = 1024)
	public String getStepName() {
		return this.stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VwMissingToolIdId))
			return false;
		VwMissingToolIdId castOther = (VwMissingToolIdId) other;

		return ((this.getWoid() == castOther.getWoid()) || (this.getWoid() != null && castOther.getWoid() != null
				&& this.getWoid().equals(castOther.getWoid())))
				&& ((this.getHours() == castOther.getHours()) || (this.getHours() != null
						&& castOther.getHours() != null && this.getHours().equals(castOther.getHours())))
				&& ((this.getType() == castOther.getType()) || (this.getType() != null && castOther.getType() != null
						&& this.getType().equals(castOther.getType())))
				&& ((this.getStatus() == castOther.getStatus()) || (this.getStatus() != null
						&& castOther.getStatus() != null && this.getStatus().equals(castOther.getStatus())))
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
				&& ((this.getWorkFlowVersion() == castOther.getWorkFlowVersion())
						|| (this.getWorkFlowVersion() != null && castOther.getWorkFlowVersion() != null
								&& this.getWorkFlowVersion().equals(castOther.getWorkFlowVersion())))
				&& ((this.getProjectId() == castOther.getProjectId()) || (this.getProjectId() != null
						&& castOther.getProjectId() != null && this.getProjectId().equals(castOther.getProjectId())))
				&& ((this.getSubActivityId() == castOther.getSubActivityId())
						|| (this.getSubActivityId() != null && castOther.getSubActivityId() != null
								&& this.getSubActivityId().equals(castOther.getSubActivityId())))
				&& ((this.getWorkFlowName() == castOther.getWorkFlowName())
						|| (this.getWorkFlowName() != null && castOther.getWorkFlowName() != null
								&& this.getWorkFlowName().equals(castOther.getWorkFlowName())))
				&& ((this.getSignumid() == castOther.getSignumid()) || (this.getSignumid() != null
						&& castOther.getSignumid() != null && this.getSignumid().equals(castOther.getSignumid())))
				&& ((this.getTaskId() == castOther.getTaskId()) || (this.getTaskId() != null
						&& castOther.getTaskId() != null && this.getTaskId().equals(castOther.getTaskId())))
				&& ((this.getTask() == castOther.getTask()) || (this.getTask() != null && castOther.getTask() != null
						&& this.getTask().equals(castOther.getTask())))
				&& ((this.getToolId() == castOther.getToolId()) || (this.getToolId() != null
						&& castOther.getToolId() != null && this.getToolId().equals(castOther.getToolId())))
				&& ((this.getExecutionType() == castOther.getExecutionType())
						|| (this.getExecutionType() != null && castOther.getExecutionType() != null
								&& this.getExecutionType().equals(castOther.getExecutionType())))
				&& ((this.getAvgEstdEffort() == castOther.getAvgEstdEffort())
						|| (this.getAvgEstdEffort() != null && castOther.getAvgEstdEffort() != null
								&& this.getAvgEstdEffort().equals(castOther.getAvgEstdEffort())))
				&& ((this.getBotId() == castOther.getBotId()) || (this.getBotId() != null
						&& castOther.getBotId() != null && this.getBotId().equals(castOther.getBotId())))
				&& (this.getBookingId() == castOther.getBookingId())
				&& ((this.getStartDate() == castOther.getStartDate()) || (this.getStartDate() != null
						&& castOther.getStartDate() != null && this.getStartDate().equals(castOther.getStartDate())))
				&& ((this.getEndDate() == castOther.getEndDate()) || (this.getEndDate() != null
						&& castOther.getEndDate() != null && this.getEndDate().equals(castOther.getEndDate())))
				&& ((this.getParentBookingDetailsId() == castOther.getParentBookingDetailsId())
						|| (this.getParentBookingDetailsId() != null && castOther.getParentBookingDetailsId() != null
								&& this.getParentBookingDetailsId().equals(castOther.getParentBookingDetailsId())))
				&& ((this.getReason() == castOther.getReason()) || (this.getReason() != null
						&& castOther.getReason() != null && this.getReason().equals(castOther.getReason())))
				&& ((this.getOutputLink() == castOther.getOutputLink()) || (this.getOutputLink() != null
						&& castOther.getOutputLink() != null && this.getOutputLink().equals(castOther.getOutputLink())))
				&& ((this.getStepId() == castOther.getStepId()) || (this.getStepId() != null
						&& castOther.getStepId() != null && this.getStepId().equals(castOther.getStepId())))
				&& ((this.getStepName() == castOther.getStepName()) || (this.getStepName() != null
						&& castOther.getStepName() != null && this.getStepName().equals(castOther.getStepName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getWoid() == null ? 0 : this.getWoid().hashCode());
		result = 37 * result + (getHours() == null ? 0 : this.getHours().hashCode());
		result = 37 * result + (getType() == null ? 0 : this.getType().hashCode());
		result = 37 * result + (getStatus() == null ? 0 : this.getStatus().hashCode());
		result = 37 * result + (getWoplanId() == null ? 0 : this.getWoplanId().hashCode());
		result = 37 * result + (getWoActualStartDate() == null ? 0 : this.getWoActualStartDate().hashCode());
		result = 37 * result + (getWoActualEndDate() == null ? 0 : this.getWoActualEndDate().hashCode());
		result = 37 * result + (getWoPlannedStartDate() == null ? 0 : this.getWoPlannedStartDate().hashCode());
		result = 37 * result + (getWoPlannedEndDate() == null ? 0 : this.getWoPlannedEndDate().hashCode());
		result = 37 * result + (getWoSignum() == null ? 0 : this.getWoSignum().hashCode());
		result = 37 * result + (getWoStatus() == null ? 0 : this.getWoStatus().hashCode());
		result = 37 * result + (getWorkFlowVersion() == null ? 0 : this.getWorkFlowVersion().hashCode());
		result = 37 * result + (getProjectId() == null ? 0 : this.getProjectId().hashCode());
		result = 37 * result + (getSubActivityId() == null ? 0 : this.getSubActivityId().hashCode());
		result = 37 * result + (getWorkFlowName() == null ? 0 : this.getWorkFlowName().hashCode());
		result = 37 * result + (getSignumid() == null ? 0 : this.getSignumid().hashCode());
		result = 37 * result + (getTaskId() == null ? 0 : this.getTaskId().hashCode());
		result = 37 * result + (getTask() == null ? 0 : this.getTask().hashCode());
		result = 37 * result + (getToolId() == null ? 0 : this.getToolId().hashCode());
		result = 37 * result + (getExecutionType() == null ? 0 : this.getExecutionType().hashCode());
		result = 37 * result + (getAvgEstdEffort() == null ? 0 : this.getAvgEstdEffort().hashCode());
		result = 37 * result + (getBotId() == null ? 0 : this.getBotId().hashCode());
		result = 37 * result + this.getBookingId();
		result = 37 * result + (getStartDate() == null ? 0 : this.getStartDate().hashCode());
		result = 37 * result + (getEndDate() == null ? 0 : this.getEndDate().hashCode());
		result = 37 * result + (getParentBookingDetailsId() == null ? 0 : this.getParentBookingDetailsId().hashCode());
		result = 37 * result + (getReason() == null ? 0 : this.getReason().hashCode());
		result = 37 * result + (getOutputLink() == null ? 0 : this.getOutputLink().hashCode());
		result = 37 * result + (getStepId() == null ? 0 : this.getStepId().hashCode());
		result = 37 * result + (getStepName() == null ? 0 : this.getStepName().hashCode());
		return result;
	}

}
