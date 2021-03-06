package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblSiteExecutionSummaryVolumeJobDone generated by hbm2java
 */
@Entity
@Table(name = "tbl_Site_execution_summary_Volume_JobDone", schema = "transactionalData")
public class TblSiteExecutionSummaryVolumeJobDone implements java.io.Serializable {

	private TblSiteExecutionSummaryVolumeJobDoneId id;

	public TblSiteExecutionSummaryVolumeJobDone() {
	}

	public TblSiteExecutionSummaryVolumeJobDone(TblSiteExecutionSummaryVolumeJobDoneId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "projectScopeDetailId", column = @Column(name = "ProjectScopeDetailID")),
			@AttributeOverride(name = "subActivityId", column = @Column(name = "SubActivityID", nullable = false)),
			@AttributeOverride(name = "marketArea", column = @Column(name = "Market Area")),
			@AttributeOverride(name = "projectId", column = @Column(name = "ProjectID", nullable = false)),
			@AttributeOverride(name = "projectName", column = @Column(name = "ProjectName")),
			@AttributeOverride(name = "subactivity", column = @Column(name = "Subactivity", length = 512)),
			@AttributeOverride(name = "resourceName", column = @Column(name = "Resource Name", length = 1525)),
			@AttributeOverride(name = "workOrderId", column = @Column(name = "Work Order Id", nullable = false)),
			@AttributeOverride(name = "workOrderStatus", column = @Column(name = "Work Order Status", length = 512)),
			@AttributeOverride(name = "plannedStartDate", column = @Column(name = "Planned Start Date", length = 23)),
			@AttributeOverride(name = "plannedEndDate", column = @Column(name = "Planned End Date", length = 23)),
			@AttributeOverride(name = "actualStartDate", column = @Column(name = "Actual Start Date", length = 23)),
			@AttributeOverride(name = "actualEndDate", column = @Column(name = "Actual End Date", length = 23)),
			@AttributeOverride(name = "date", column = @Column(name = "Date", length = 10)),
			@AttributeOverride(name = "avgEstdEffort", column = @Column(name = "AvgEstdEffort", nullable = false, precision = 53, scale = 0)),
			@AttributeOverride(name = "hours", column = @Column(name = "Hours", precision = 53, scale = 0)),
			@AttributeOverride(name = "siteName", column = @Column(name = "SiteName")) })
	public TblSiteExecutionSummaryVolumeJobDoneId getId() {
		return this.id;
	}

	public void setId(TblSiteExecutionSummaryVolumeJobDoneId id) {
		this.id = id;
	}

}
