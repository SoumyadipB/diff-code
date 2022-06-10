package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblFtrStatusJobdone generated by hbm2java
 */
@Entity
@Table(name = "tbl_FTR_STATUS_Jobdone", schema = "transactionalData")
public class TblFtrStatusJobdone implements java.io.Serializable {

	private TblFtrStatusJobdoneId id;

	public TblFtrStatusJobdone() {
	}

	public TblFtrStatusJobdone(TblFtrStatusJobdoneId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "woNumber", column = @Column(name = "WO Number")),
			@AttributeOverride(name = "ticketNumber", column = @Column(name = "Ticket Number")),
			@AttributeOverride(name = "complaintType", column = @Column(name = "Complaint Type", length = 512)),
			@AttributeOverride(name = "projectId", column = @Column(name = "ProjectID", nullable = false)),
			@AttributeOverride(name = "projectName", column = @Column(name = "ProjectName")),
			@AttributeOverride(name = "opportunityId", column = @Column(name = "OpportunityID")),
			@AttributeOverride(name = "domainId", column = @Column(name = "DomainID")),
			@AttributeOverride(name = "subActivityId", column = @Column(name = "SubActivityID", nullable = false)),
			@AttributeOverride(name = "subactivity", column = @Column(name = "subactivity", length = 512)),
			@AttributeOverride(name = "technology", column = @Column(name = "Technology", length = 128)),
			@AttributeOverride(name = "executionStatus", column = @Column(name = "Execution Status", length = 512)),
			@AttributeOverride(name = "engineer", column = @Column(name = "Engineer", length = 1013)),
			@AttributeOverride(name = "woplanId", column = @Column(name = "WOPlanID")),
			@AttributeOverride(name = "woCreatedDate", column = @Column(name = "WO Created Date", length = 50)),
			@AttributeOverride(name = "bookingId", column = @Column(name = "BookingID", nullable = false)),
			@AttributeOverride(name = "task", column = @Column(name = "Task", length = 512)),
			@AttributeOverride(name = "executionType", column = @Column(name = "ExecutionType", nullable = false, length = 11)),
			@AttributeOverride(name = "wotaskStartDate", column = @Column(name = "WOTASK Start Date", length = 50)),
			@AttributeOverride(name = "hours", column = @Column(name = "Hours", length = 50)),
			@AttributeOverride(name = "type", column = @Column(name = "Type", length = 512)),
			@AttributeOverride(name = "status", column = @Column(name = "Status", length = 512)),
			@AttributeOverride(name = "slaStatus", column = @Column(name = "SLA Status")),
			@AttributeOverride(name = "reasonForMissedSla", column = @Column(name = "Reason for Missed SLA")),
			@AttributeOverride(name = "remarks", column = @Column(name = "Remarks")),
			@AttributeOverride(name = "avgEstdEffort", column = @Column(name = "AvgEstdEffort")),
			@AttributeOverride(name = "siteName", column = @Column(name = "SiteName")),
			@AttributeOverride(name = "woPlannedStartDateTime", column = @Column(name = "WO PLanned Start DateTime", length = 50)),
			@AttributeOverride(name = "woActualStartDateTime", column = @Column(name = "WO Actual Start DateTime", length = 50)),
			@AttributeOverride(name = "woActualEndDateTime", column = @Column(name = "WO Actual End DateTime", length = 50)),
			@AttributeOverride(name = "woPlannedStartDate", column = @Column(name = "WO PLanned Start Date", length = 10)),
			@AttributeOverride(name = "woPlannedEndDate", column = @Column(name = "WO PLanned End Date", length = 10)),
			@AttributeOverride(name = "woActualStartDate", column = @Column(name = "WO Actual Start Date", length = 10)),
			@AttributeOverride(name = "woActualEndDate", column = @Column(name = "WO Actual End Date", length = 10)),
			@AttributeOverride(name = "woPlannedStartTime", column = @Column(name = "WO PLanned Start Time", length = 16)),
			@AttributeOverride(name = "woActualStartTime", column = @Column(name = "WO Actual Start Time", length = 16)),
			@AttributeOverride(name = "woActualEndTime", column = @Column(name = "WO Actual End Time", length = 16)),
			@AttributeOverride(name = "manualEffortCost", column = @Column(name = "Manual Effort COST", precision = 53, scale = 0)),
			@AttributeOverride(name = "estimatedCost", column = @Column(name = "estimated Cost")),
			@AttributeOverride(name = "date", column = @Column(name = "Date", length = 23)),
			@AttributeOverride(name = "week", column = @Column(name = "WEEK")),
			@AttributeOverride(name = "marketAreaName", column = @Column(name = "MarketAreaName")),
			@AttributeOverride(name = "customerName", column = @Column(name = "CustomerName")),
			@AttributeOverride(name = "domain", column = @Column(name = "Domain", length = 128)) })
	public TblFtrStatusJobdoneId getId() {
		return this.id;
	}

	public void setId(TblFtrStatusJobdoneId id) {
		this.id = id;
	}

}
