package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblProcessComplianceJobDone generated by hbm2java
 */
@Entity
@Table(name = "TBL_Process_Compliance_JobDone", schema = "transactionalData")
public class TblProcessComplianceJobDone implements java.io.Serializable {

	private TblProcessComplianceJobDoneId id;

	public TblProcessComplianceJobDone() {
	}

	public TblProcessComplianceJobDone(TblProcessComplianceJobDoneId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "projectId", column = @Column(name = "ProjectID")),
			@AttributeOverride(name = "projectName", column = @Column(name = "ProjectName")),
			@AttributeOverride(name = "projectType", column = @Column(name = "ProjectType")),
			@AttributeOverride(name = "cpm", column = @Column(name = "CPM")),
			@AttributeOverride(name = "projectCreator", column = @Column(name = "ProjectCreator")),
			@AttributeOverride(name = "opportunityId", column = @Column(name = "OpportunityID")),
			@AttributeOverride(name = "status", column = @Column(name = "status")),
			@AttributeOverride(name = "operationalManager", column = @Column(name = "OperationalManager")),
			@AttributeOverride(name = "opportunityName", column = @Column(name = "OpportunityName")),
			@AttributeOverride(name = "opportunityCode", column = @Column(name = "OpportunityCode", length = 100)),
			@AttributeOverride(name = "companyId", column = @Column(name = "CompanyID")),
			@AttributeOverride(name = "companyName", column = @Column(name = "CompanyName")),
			@AttributeOverride(name = "countryId", column = @Column(name = "CountryID")),
			@AttributeOverride(name = "countryName", column = @Column(name = "CountryName")),
			@AttributeOverride(name = "marketAreaId", column = @Column(name = "MarketAreaID")),
			@AttributeOverride(name = "marketAreaName", column = @Column(name = "MarketAreaName")),
			@AttributeOverride(name = "customerId", column = @Column(name = "customerID")),
			@AttributeOverride(name = "customerName", column = @Column(name = "CustomerName")),
			@AttributeOverride(name = "scopeId", column = @Column(name = "ScopeID")),
			@AttributeOverride(name = "subActivityId", column = @Column(name = "SubActivityID")),
			@AttributeOverride(name = "subActivity", column = @Column(name = "SubActivity", length = 512)),
			@AttributeOverride(name = "avgEstdEffort", column = @Column(name = "AvgEstdEffort", precision = 53, scale = 0)),
			@AttributeOverride(name = "woname", column = @Column(name = "WOName")),
			@AttributeOverride(name = "priority", column = @Column(name = "Priority", length = 1024)),
			@AttributeOverride(name = "sla", column = @Column(name = "SLA", nullable = false, precision = 53, scale = 0)),
			@AttributeOverride(name = "woid", column = @Column(name = "WOID", nullable = false)),
			@AttributeOverride(name = "woplanId", column = @Column(name = "WOPlanID")),
			@AttributeOverride(name = "plannedStartDate", column = @Column(name = "PlannedStartDate", length = 23)),
			@AttributeOverride(name = "plannedEndDate", column = @Column(name = "PlannedEndDate", length = 23)),
			@AttributeOverride(name = "date", column = @Column(name = "Date", length = 23)),
			@AttributeOverride(name = "actualStartDate", column = @Column(name = "ActualStartDate", length = 23)),
			@AttributeOverride(name = "actualEndDate", column = @Column(name = "ActualEndDate", length = 23)),
			@AttributeOverride(name = "signumId", column = @Column(name = "SignumID", length = 1024)),
			@AttributeOverride(name = "employeeName", column = @Column(name = "EmployeeName", length = 500)),
			@AttributeOverride(name = "wostatus", column = @Column(name = "WOStatus", length = 512)),
			@AttributeOverride(name = "deliveryStatus", column = @Column(name = "DeliveryStatus", length = 1024)),
			@AttributeOverride(name = "failureReason", column = @Column(name = "FailureReason", nullable = false, length = 1024)),
			@AttributeOverride(name = "statusComment", column = @Column(name = "StatusComment", nullable = false, length = 5000)),
			@AttributeOverride(name = "parentWorkOrderId", column = @Column(name = "ParentWorkOrderID")),
			@AttributeOverride(name = "clockedHours", column = @Column(name = "ClockedHours", precision = 53, scale = 0)),
			@AttributeOverride(name = "taskId", column = @Column(name = "TaskID")),
			@AttributeOverride(name = "task", column = @Column(name = "Task", length = 1024)),
			@AttributeOverride(name = "nodeCount", column = @Column(name = "NodeCount")),
			@AttributeOverride(name = "taskPlannedLoe", column = @Column(name = "TaskPlannedLOE", precision = 53, scale = 0)),
			@AttributeOverride(name = "perSiteActualLoe", column = @Column(name = "PerSiteActualLOE", precision = 53, scale = 0)),
			@AttributeOverride(name = "isCompliant", column = @Column(name = "isCompliant", nullable = false)),
			@AttributeOverride(name = "nodeType", column = @Column(name = "NodeType", length = 512)),
			@AttributeOverride(name = "nodeNames", column = @Column(name = "NodeNames", length = 8000)) })
	public TblProcessComplianceJobDoneId getId() {
		return this.id;
	}

	public void setId(TblProcessComplianceJobDoneId id) {
		this.id = id;
	}

}
