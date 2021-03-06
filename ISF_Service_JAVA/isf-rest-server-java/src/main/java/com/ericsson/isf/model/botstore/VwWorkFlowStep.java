package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * VwWorkFlowStep generated by hbm2java
 */
@Entity
@Table(name = "vw_WorkFlowStep", schema = "dbo")
public class VwWorkFlowStep implements java.io.Serializable {

	private VwWorkFlowStepId id;

	public VwWorkFlowStep() {
	}

	public VwWorkFlowStep(VwWorkFlowStepId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "marketAreaName", column = @Column(name = "MarketAreaName", length = 20)),
			@AttributeOverride(name = "countryName", column = @Column(name = "CountryName", length = 250)),
			@AttributeOverride(name = "customerName", column = @Column(name = "CustomerName", length = 200)),
			@AttributeOverride(name = "countryId", column = @Column(name = "CountryID", length = 10)),
			@AttributeOverride(name = "serviceArea", column = @Column(name = "ServiceArea", length = 128)),
			@AttributeOverride(name = "projectName", column = @Column(name = "ProjectName", length = 250)),
			@AttributeOverride(name = "projectId", column = @Column(name = "ProjectID", length = 10)),
			@AttributeOverride(name = "subActivityId", column = @Column(name = "SubActivityID", length = 13)),
			@AttributeOverride(name = "domain", column = @Column(name = "Domain", length = 128)),
			@AttributeOverride(name = "subDomain", column = @Column(name = "SubDomain", length = 128)),
			@AttributeOverride(name = "subServiceArea", column = @Column(name = "SubServiceArea", length = 128)),
			@AttributeOverride(name = "technology", column = @Column(name = "Technology", length = 128)),
			@AttributeOverride(name = "activity", column = @Column(name = "Activity", length = 512)),
			@AttributeOverride(name = "subActivity", column = @Column(name = "SubActivity", length = 512)),
			@AttributeOverride(name = "subActivityFlowChartDefId", column = @Column(name = "SubActivityFlowChartDefID", length = 25)),
			@AttributeOverride(name = "taskId", column = @Column(name = "TaskID", length = 10)),
			@AttributeOverride(name = "task", column = @Column(name = "Task", length = 500)),
			@AttributeOverride(name = "fcstepId", column = @Column(name = "FCStepID", length = 10)),
			@AttributeOverride(name = "executionType", column = @Column(name = "ExecutionType", length = 20)),
			@AttributeOverride(name = "toolId", column = @Column(name = "ToolID", length = 10)),
			@AttributeOverride(name = "tool", column = @Column(name = "Tool", length = 128)),
			@AttributeOverride(name = "versionNumber", column = @Column(name = "VersionNumber", length = 13)),
			@AttributeOverride(name = "workFlowName", column = @Column(name = "WorkFlowName")),
			@AttributeOverride(name = "stepName", column = @Column(name = "StepName", length = 1024)),
			@AttributeOverride(name = "avgEstdEffort", column = @Column(name = "AvgEstdEffort", length = 13)) })
	public VwWorkFlowStepId getId() {
		return this.id;
	}

	public void setId(VwWorkFlowStepId id) {
		this.id = id;
	}

}
