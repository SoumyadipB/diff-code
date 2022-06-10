package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblCustomerComplainTrackerJobdone generated by hbm2java
 */
@Entity
@Table(name = "tbl_Customer_Complain_Tracker_Jobdone", schema = "transactionalData")
public class TblCustomerComplainTrackerJobdone implements java.io.Serializable {

	private TblCustomerComplainTrackerJobdoneId id;

	public TblCustomerComplainTrackerJobdone() {
	}

	public TblCustomerComplainTrackerJobdone(TblCustomerComplainTrackerJobdoneId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "woNumber", column = @Column(name = "WO Number")),
			@AttributeOverride(name = "ticketNumber", column = @Column(name = "Ticket Number")),
			@AttributeOverride(name = "complaintType", column = @Column(name = "Complaint Type", length = 512)),
			@AttributeOverride(name = "projectId", column = @Column(name = "ProjectID")),
			@AttributeOverride(name = "projectName", column = @Column(name = "ProjectName")),
			@AttributeOverride(name = "subActivityId", column = @Column(name = "SubActivityID")),
			@AttributeOverride(name = "subActivity", column = @Column(name = "SubActivity", length = 512)),
			@AttributeOverride(name = "technologyId", column = @Column(name = "TechnologyID")),
			@AttributeOverride(name = "technology", column = @Column(name = "Technology", length = 128)),
			@AttributeOverride(name = "executionStatus", column = @Column(name = "Execution Status", length = 512)),
			@AttributeOverride(name = "engineer", column = @Column(name = "Engineer", length = 1013)),
			@AttributeOverride(name = "woplanId", column = @Column(name = "WOPlanID")),
			@AttributeOverride(name = "woCreatedDate", column = @Column(name = "WO Created Date", length = 50)),
			@AttributeOverride(name = "bookingId", column = @Column(name = "BookingID", nullable = false)),
			@AttributeOverride(name = "task", column = @Column(name = "Task", length = 1024)),
			@AttributeOverride(name = "executionType", column = @Column(name = "ExecutionType", nullable = false, length = 11)),
			@AttributeOverride(name = "woPlannedStartDate", column = @Column(name = "WO PlannedStartDate", length = 23)),
			@AttributeOverride(name = "woPlannedEndDate", column = @Column(name = "WO PlannedEndDate", length = 23)),
			@AttributeOverride(name = "woActualStartDate", column = @Column(name = "WO ActualStartDate", length = 23)),
			@AttributeOverride(name = "woActualEndDate", column = @Column(name = "WO ActualEndDate", length = 23)),
			@AttributeOverride(name = "taskStartDate", column = @Column(name = "Task Start Date", length = 50)),
			@AttributeOverride(name = "taskCloseDate", column = @Column(name = "Task Close Date", length = 50)),
			@AttributeOverride(name = "date", column = @Column(name = "Date", length = 50)),
			@AttributeOverride(name = "hours", column = @Column(name = "Hours", precision = 53, scale = 0)),
			@AttributeOverride(name = "type", column = @Column(name = "Type", length = 512)),
			@AttributeOverride(name = "status", column = @Column(name = "Status", length = 512)),
			@AttributeOverride(name = "slaStatus", column = @Column(name = "SLA Status")),
			@AttributeOverride(name = "reasonForMissedSla", column = @Column(name = "Reason for Missed SLA")),
			@AttributeOverride(name = "remarks", column = @Column(name = "Remarks", length = 5000)) })
	public TblCustomerComplainTrackerJobdoneId getId() {
		return this.id;
	}

	public void setId(TblCustomerComplainTrackerJobdoneId id) {
		this.id = id;
	}

}
