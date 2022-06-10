package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblCustomerComplainTrackerJobdoneId generated by hbm2java
 */
@Embeddable
public class TblCustomerComplainTrackerJobdoneId implements java.io.Serializable {

	private Integer woNumber;
	private String ticketNumber;
	private String complaintType;
	private Integer projectId;
	private Serializable projectName;
	private Integer subActivityId;
	private String subActivity;
	private Integer technologyId;
	private String technology;
	private String executionStatus;
	private String engineer;
	private Integer woplanId;
	private String woCreatedDate;
	private int bookingId;
	private String task;
	private String executionType;
	private Date woPlannedStartDate;
	private Date woPlannedEndDate;
	private Date woActualStartDate;
	private Date woActualEndDate;
	private String taskStartDate;
	private String taskCloseDate;
	private String date;
	private Double hours;
	private String type;
	private String status;
	private Integer slaStatus;
	private Integer reasonForMissedSla;
	private String remarks;

	public TblCustomerComplainTrackerJobdoneId() {
	}

	public TblCustomerComplainTrackerJobdoneId(int bookingId, String executionType) {
		this.bookingId = bookingId;
		this.executionType = executionType;
	}

	public TblCustomerComplainTrackerJobdoneId(Integer woNumber, String ticketNumber, String complaintType,
			Integer projectId, Serializable projectName, Integer subActivityId, String subActivity,
			Integer technologyId, String technology, String executionStatus, String engineer, Integer woplanId,
			String woCreatedDate, int bookingId, String task, String executionType, Date woPlannedStartDate,
			Date woPlannedEndDate, Date woActualStartDate, Date woActualEndDate, String taskStartDate,
			String taskCloseDate, String date, Double hours, String type, String status, Integer slaStatus,
			Integer reasonForMissedSla, String remarks) {
		this.woNumber = woNumber;
		this.ticketNumber = ticketNumber;
		this.complaintType = complaintType;
		this.projectId = projectId;
		this.projectName = projectName;
		this.subActivityId = subActivityId;
		this.subActivity = subActivity;
		this.technologyId = technologyId;
		this.technology = technology;
		this.executionStatus = executionStatus;
		this.engineer = engineer;
		this.woplanId = woplanId;
		this.woCreatedDate = woCreatedDate;
		this.bookingId = bookingId;
		this.task = task;
		this.executionType = executionType;
		this.woPlannedStartDate = woPlannedStartDate;
		this.woPlannedEndDate = woPlannedEndDate;
		this.woActualStartDate = woActualStartDate;
		this.woActualEndDate = woActualEndDate;
		this.taskStartDate = taskStartDate;
		this.taskCloseDate = taskCloseDate;
		this.date = date;
		this.hours = hours;
		this.type = type;
		this.status = status;
		this.slaStatus = slaStatus;
		this.reasonForMissedSla = reasonForMissedSla;
		this.remarks = remarks;
	}

	@Column(name = "WO Number")
	public Integer getWoNumber() {
		return this.woNumber;
	}

	public void setWoNumber(Integer woNumber) {
		this.woNumber = woNumber;
	}

	@Column(name = "Ticket Number")
	public String getTicketNumber() {
		return this.ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	@Column(name = "Complaint Type", length = 512)
	public String getComplaintType() {
		return this.complaintType;
	}

	public void setComplaintType(String complaintType) {
		this.complaintType = complaintType;
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

	@Column(name = "SubActivityID")
	public Integer getSubActivityId() {
		return this.subActivityId;
	}

	public void setSubActivityId(Integer subActivityId) {
		this.subActivityId = subActivityId;
	}

	@Column(name = "SubActivity", length = 512)
	public String getSubActivity() {
		return this.subActivity;
	}

	public void setSubActivity(String subActivity) {
		this.subActivity = subActivity;
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

	@Column(name = "Execution Status", length = 512)
	public String getExecutionStatus() {
		return this.executionStatus;
	}

	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}

	@Column(name = "Engineer", length = 1013)
	public String getEngineer() {
		return this.engineer;
	}

	public void setEngineer(String engineer) {
		this.engineer = engineer;
	}

	@Column(name = "WOPlanID")
	public Integer getWoplanId() {
		return this.woplanId;
	}

	public void setWoplanId(Integer woplanId) {
		this.woplanId = woplanId;
	}

	@Column(name = "WO Created Date", length = 50)
	public String getWoCreatedDate() {
		return this.woCreatedDate;
	}

	public void setWoCreatedDate(String woCreatedDate) {
		this.woCreatedDate = woCreatedDate;
	}

	@Column(name = "BookingID", nullable = false)
	public int getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	@Column(name = "Task", length = 1024)
	public String getTask() {
		return this.task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	@Column(name = "ExecutionType", nullable = false, length = 11)
	public String getExecutionType() {
		return this.executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	@Column(name = "WO PlannedStartDate", length = 23)
	public Date getWoPlannedStartDate() {
		return this.woPlannedStartDate;
	}

	public void setWoPlannedStartDate(Date woPlannedStartDate) {
		this.woPlannedStartDate = woPlannedStartDate;
	}

	@Column(name = "WO PlannedEndDate", length = 23)
	public Date getWoPlannedEndDate() {
		return this.woPlannedEndDate;
	}

	public void setWoPlannedEndDate(Date woPlannedEndDate) {
		this.woPlannedEndDate = woPlannedEndDate;
	}

	@Column(name = "WO ActualStartDate", length = 23)
	public Date getWoActualStartDate() {
		return this.woActualStartDate;
	}

	public void setWoActualStartDate(Date woActualStartDate) {
		this.woActualStartDate = woActualStartDate;
	}

	@Column(name = "WO ActualEndDate", length = 23)
	public Date getWoActualEndDate() {
		return this.woActualEndDate;
	}

	public void setWoActualEndDate(Date woActualEndDate) {
		this.woActualEndDate = woActualEndDate;
	}

	@Column(name = "Task Start Date", length = 50)
	public String getTaskStartDate() {
		return this.taskStartDate;
	}

	public void setTaskStartDate(String taskStartDate) {
		this.taskStartDate = taskStartDate;
	}

	@Column(name = "Task Close Date", length = 50)
	public String getTaskCloseDate() {
		return this.taskCloseDate;
	}

	public void setTaskCloseDate(String taskCloseDate) {
		this.taskCloseDate = taskCloseDate;
	}

	@Column(name = "Date", length = 50)
	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
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

	@Column(name = "SLA Status")
	public Integer getSlaStatus() {
		return this.slaStatus;
	}

	public void setSlaStatus(Integer slaStatus) {
		this.slaStatus = slaStatus;
	}

	@Column(name = "Reason for Missed SLA")
	public Integer getReasonForMissedSla() {
		return this.reasonForMissedSla;
	}

	public void setReasonForMissedSla(Integer reasonForMissedSla) {
		this.reasonForMissedSla = reasonForMissedSla;
	}

	@Column(name = "Remarks", length = 5000)
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblCustomerComplainTrackerJobdoneId))
			return false;
		TblCustomerComplainTrackerJobdoneId castOther = (TblCustomerComplainTrackerJobdoneId) other;

		return ((this.getWoNumber() == castOther.getWoNumber()) || (this.getWoNumber() != null
				&& castOther.getWoNumber() != null && this.getWoNumber().equals(castOther.getWoNumber())))
				&& ((this.getTicketNumber() == castOther.getTicketNumber())
						|| (this.getTicketNumber() != null && castOther.getTicketNumber() != null
								&& this.getTicketNumber().equals(castOther.getTicketNumber())))
				&& ((this.getComplaintType() == castOther.getComplaintType())
						|| (this.getComplaintType() != null && castOther.getComplaintType() != null
								&& this.getComplaintType().equals(castOther.getComplaintType())))
				&& ((this.getProjectId() == castOther.getProjectId()) || (this.getProjectId() != null
						&& castOther.getProjectId() != null && this.getProjectId().equals(castOther.getProjectId())))
				&& ((this.getProjectName() == castOther.getProjectName())
						|| (this.getProjectName() != null && castOther.getProjectName() != null
								&& this.getProjectName().equals(castOther.getProjectName())))
				&& ((this.getSubActivityId() == castOther.getSubActivityId())
						|| (this.getSubActivityId() != null && castOther.getSubActivityId() != null
								&& this.getSubActivityId().equals(castOther.getSubActivityId())))
				&& ((this.getSubActivity() == castOther.getSubActivity())
						|| (this.getSubActivity() != null && castOther.getSubActivity() != null
								&& this.getSubActivity().equals(castOther.getSubActivity())))
				&& ((this.getTechnologyId() == castOther.getTechnologyId())
						|| (this.getTechnologyId() != null && castOther.getTechnologyId() != null
								&& this.getTechnologyId().equals(castOther.getTechnologyId())))
				&& ((this.getTechnology() == castOther.getTechnology()) || (this.getTechnology() != null
						&& castOther.getTechnology() != null && this.getTechnology().equals(castOther.getTechnology())))
				&& ((this.getExecutionStatus() == castOther.getExecutionStatus())
						|| (this.getExecutionStatus() != null && castOther.getExecutionStatus() != null
								&& this.getExecutionStatus().equals(castOther.getExecutionStatus())))
				&& ((this.getEngineer() == castOther.getEngineer()) || (this.getEngineer() != null
						&& castOther.getEngineer() != null && this.getEngineer().equals(castOther.getEngineer())))
				&& ((this.getWoplanId() == castOther.getWoplanId()) || (this.getWoplanId() != null
						&& castOther.getWoplanId() != null && this.getWoplanId().equals(castOther.getWoplanId())))
				&& ((this.getWoCreatedDate() == castOther.getWoCreatedDate())
						|| (this.getWoCreatedDate() != null && castOther.getWoCreatedDate() != null
								&& this.getWoCreatedDate().equals(castOther.getWoCreatedDate())))
				&& (this.getBookingId() == castOther.getBookingId())
				&& ((this.getTask() == castOther.getTask()) || (this.getTask() != null && castOther.getTask() != null
						&& this.getTask().equals(castOther.getTask())))
				&& ((this.getExecutionType() == castOther.getExecutionType())
						|| (this.getExecutionType() != null && castOther.getExecutionType() != null
								&& this.getExecutionType().equals(castOther.getExecutionType())))
				&& ((this.getWoPlannedStartDate() == castOther.getWoPlannedStartDate())
						|| (this.getWoPlannedStartDate() != null && castOther.getWoPlannedStartDate() != null
								&& this.getWoPlannedStartDate().equals(castOther.getWoPlannedStartDate())))
				&& ((this.getWoPlannedEndDate() == castOther.getWoPlannedEndDate())
						|| (this.getWoPlannedEndDate() != null && castOther.getWoPlannedEndDate() != null
								&& this.getWoPlannedEndDate().equals(castOther.getWoPlannedEndDate())))
				&& ((this.getWoActualStartDate() == castOther.getWoActualStartDate())
						|| (this.getWoActualStartDate() != null && castOther.getWoActualStartDate() != null
								&& this.getWoActualStartDate().equals(castOther.getWoActualStartDate())))
				&& ((this.getWoActualEndDate() == castOther.getWoActualEndDate())
						|| (this.getWoActualEndDate() != null && castOther.getWoActualEndDate() != null
								&& this.getWoActualEndDate().equals(castOther.getWoActualEndDate())))
				&& ((this.getTaskStartDate() == castOther.getTaskStartDate())
						|| (this.getTaskStartDate() != null && castOther.getTaskStartDate() != null
								&& this.getTaskStartDate().equals(castOther.getTaskStartDate())))
				&& ((this.getTaskCloseDate() == castOther.getTaskCloseDate())
						|| (this.getTaskCloseDate() != null && castOther.getTaskCloseDate() != null
								&& this.getTaskCloseDate().equals(castOther.getTaskCloseDate())))
				&& ((this.getDate() == castOther.getDate()) || (this.getDate() != null && castOther.getDate() != null
						&& this.getDate().equals(castOther.getDate())))
				&& ((this.getHours() == castOther.getHours()) || (this.getHours() != null
						&& castOther.getHours() != null && this.getHours().equals(castOther.getHours())))
				&& ((this.getType() == castOther.getType()) || (this.getType() != null && castOther.getType() != null
						&& this.getType().equals(castOther.getType())))
				&& ((this.getStatus() == castOther.getStatus()) || (this.getStatus() != null
						&& castOther.getStatus() != null && this.getStatus().equals(castOther.getStatus())))
				&& ((this.getSlaStatus() == castOther.getSlaStatus()) || (this.getSlaStatus() != null
						&& castOther.getSlaStatus() != null && this.getSlaStatus().equals(castOther.getSlaStatus())))
				&& ((this.getReasonForMissedSla() == castOther.getReasonForMissedSla())
						|| (this.getReasonForMissedSla() != null && castOther.getReasonForMissedSla() != null
								&& this.getReasonForMissedSla().equals(castOther.getReasonForMissedSla())))
				&& ((this.getRemarks() == castOther.getRemarks()) || (this.getRemarks() != null
						&& castOther.getRemarks() != null && this.getRemarks().equals(castOther.getRemarks())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getWoNumber() == null ? 0 : this.getWoNumber().hashCode());
		result = 37 * result + (getTicketNumber() == null ? 0 : this.getTicketNumber().hashCode());
		result = 37 * result + (getComplaintType() == null ? 0 : this.getComplaintType().hashCode());
		result = 37 * result + (getProjectId() == null ? 0 : this.getProjectId().hashCode());
		result = 37 * result + (getProjectName() == null ? 0 : this.getProjectName().hashCode());
		result = 37 * result + (getSubActivityId() == null ? 0 : this.getSubActivityId().hashCode());
		result = 37 * result + (getSubActivity() == null ? 0 : this.getSubActivity().hashCode());
		result = 37 * result + (getTechnologyId() == null ? 0 : this.getTechnologyId().hashCode());
		result = 37 * result + (getTechnology() == null ? 0 : this.getTechnology().hashCode());
		result = 37 * result + (getExecutionStatus() == null ? 0 : this.getExecutionStatus().hashCode());
		result = 37 * result + (getEngineer() == null ? 0 : this.getEngineer().hashCode());
		result = 37 * result + (getWoplanId() == null ? 0 : this.getWoplanId().hashCode());
		result = 37 * result + (getWoCreatedDate() == null ? 0 : this.getWoCreatedDate().hashCode());
		result = 37 * result + this.getBookingId();
		result = 37 * result + (getTask() == null ? 0 : this.getTask().hashCode());
		result = 37 * result + (getExecutionType() == null ? 0 : this.getExecutionType().hashCode());
		result = 37 * result + (getWoPlannedStartDate() == null ? 0 : this.getWoPlannedStartDate().hashCode());
		result = 37 * result + (getWoPlannedEndDate() == null ? 0 : this.getWoPlannedEndDate().hashCode());
		result = 37 * result + (getWoActualStartDate() == null ? 0 : this.getWoActualStartDate().hashCode());
		result = 37 * result + (getWoActualEndDate() == null ? 0 : this.getWoActualEndDate().hashCode());
		result = 37 * result + (getTaskStartDate() == null ? 0 : this.getTaskStartDate().hashCode());
		result = 37 * result + (getTaskCloseDate() == null ? 0 : this.getTaskCloseDate().hashCode());
		result = 37 * result + (getDate() == null ? 0 : this.getDate().hashCode());
		result = 37 * result + (getHours() == null ? 0 : this.getHours().hashCode());
		result = 37 * result + (getType() == null ? 0 : this.getType().hashCode());
		result = 37 * result + (getStatus() == null ? 0 : this.getStatus().hashCode());
		result = 37 * result + (getSlaStatus() == null ? 0 : this.getSlaStatus().hashCode());
		result = 37 * result + (getReasonForMissedSla() == null ? 0 : this.getReasonForMissedSla().hashCode());
		result = 37 * result + (getRemarks() == null ? 0 : this.getRemarks().hashCode());
		return result;
	}

}
