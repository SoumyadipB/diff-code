package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblPrjojectNodewiseWoExecutionJobDoneTemp generated by hbm2java
 */
@Entity
@Table(name = "tbl_Prjoject_Nodewise_WO_Execution_JobDone_temp", schema = "transactionalData")
public class TblPrjojectNodewiseWoExecutionJobDoneTemp implements java.io.Serializable {

	private TblPrjojectNodewiseWoExecutionJobDoneTempId id;

	public TblPrjojectNodewiseWoExecutionJobDoneTemp() {
	}

	public TblPrjojectNodewiseWoExecutionJobDoneTemp(TblPrjojectNodewiseWoExecutionJobDoneTempId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "marketAreaId", column = @Column(name = "MarketAreaID")),
			@AttributeOverride(name = "marketAreaName", column = @Column(name = "MarketAreaName")),
			@AttributeOverride(name = "projectId", column = @Column(name = "ProjectID")),
			@AttributeOverride(name = "projectName", column = @Column(name = "ProjectName")),
			@AttributeOverride(name = "customerId", column = @Column(name = "CustomerID")),
			@AttributeOverride(name = "customerName", column = @Column(name = "CustomerName")),
			@AttributeOverride(name = "technologyId", column = @Column(name = "TechnologyID")),
			@AttributeOverride(name = "technology", column = @Column(name = "Technology", length = 128)),
			@AttributeOverride(name = "domainId", column = @Column(name = "DomainID")),
			@AttributeOverride(name = "domain", column = @Column(name = "Domain", length = 128)),
			@AttributeOverride(name = "subdomain", column = @Column(name = "subdomain", length = 128)),
			@AttributeOverride(name = "subActivityId", column = @Column(name = "SubActivityID")),
			@AttributeOverride(name = "subActivity", column = @Column(name = "SubActivity", length = 512)),
			@AttributeOverride(name = "activity", column = @Column(name = "Activity", length = 512)),
			@AttributeOverride(name = "woid", column = @Column(name = "WOID")),
			@AttributeOverride(name = "woplanId", column = @Column(name = "WOPlanID")),
			@AttributeOverride(name = "woActualStartDate", column = @Column(name = "WO Actual start Date", length = 23)),
			@AttributeOverride(name = "woActualEndDate", column = @Column(name = "WO Actual End Date", length = 23)),
			@AttributeOverride(name = "woPlannedStartDate", column = @Column(name = "WO Planned Start Date", length = 23)),
			@AttributeOverride(name = "woPlannedEndDate", column = @Column(name = "WO Planned End Date", length = 23)),
			@AttributeOverride(name = "woSignum", column = @Column(name = "WO Signum", length = 512)),
			@AttributeOverride(name = "woStatus", column = @Column(name = "WO Status", length = 512)),
			@AttributeOverride(name = "woname", column = @Column(name = "WOName")),
			@AttributeOverride(name = "sla", column = @Column(name = "sla", precision = 53, scale = 0)),
			@AttributeOverride(name = "nodeType", column = @Column(name = "NodeType", length = 512)),
			@AttributeOverride(name = "nodeNames", column = @Column(name = "NodeNames")),
			@AttributeOverride(name = "executionType", column = @Column(name = "Execution Type", length = 1024)),
			@AttributeOverride(name = "taskEstimatedEfforts", column = @Column(name = "Task Estimated Efforts", precision = 53, scale = 0)),
			@AttributeOverride(name = "woEstimatedEfforts", column = @Column(name = "WO Estimated Efforts", precision = 53, scale = 0)),
			@AttributeOverride(name = "taskId", column = @Column(name = "TaskID")),
			@AttributeOverride(name = "task", column = @Column(name = "Task", length = 512)),
			@AttributeOverride(name = "bookingId", column = @Column(name = "BookingID", nullable = false)),
			@AttributeOverride(name = "bookStartDate", column = @Column(name = "Book Start Date", length = 23)),
			@AttributeOverride(name = "bookEndDate", column = @Column(name = "Book End Date", length = 23)),
			@AttributeOverride(name = "hours", column = @Column(name = "Hours", precision = 53, scale = 0)),
			@AttributeOverride(name = "parentBookingDetailsId", column = @Column(name = "ParentBookingDetailsID")),
			@AttributeOverride(name = "type", column = @Column(name = "Type", length = 512)),
			@AttributeOverride(name = "status", column = @Column(name = "Status", length = 512)),
			@AttributeOverride(name = "signumId", column = @Column(name = "SignumID", length = 512)),
			@AttributeOverride(name = "reason", column = @Column(name = "Reason", length = 1024)),
			@AttributeOverride(name = "outputLink", column = @Column(name = "OutputLink")) })
	public TblPrjojectNodewiseWoExecutionJobDoneTempId getId() {
		return this.id;
	}

	public void setId(TblPrjojectNodewiseWoExecutionJobDoneTempId id) {
		this.id = id;
	}

}
