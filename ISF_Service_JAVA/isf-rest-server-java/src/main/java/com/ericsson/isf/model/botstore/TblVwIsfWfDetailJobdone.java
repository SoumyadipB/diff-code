package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblVwIsfWfDetailJobdone generated by hbm2java
 */
@Entity
@Table(name = "TBL_vw_ISF_WF_Detail_Jobdone", schema = "refData")
public class TblVwIsfWfDetailJobdone implements java.io.Serializable {

	private TblVwIsfWfDetailJobdoneId id;

	public TblVwIsfWfDetailJobdone() {
	}

	public TblVwIsfWfDetailJobdone(TblVwIsfWfDetailJobdoneId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "marketAreaId", column = @Column(name = "MarketAreaID", nullable = false, length = 20)),
			@AttributeOverride(name = "marketAreaName", column = @Column(name = "MarketAreaName", nullable = false, length = 50)),
			@AttributeOverride(name = "projectId", column = @Column(name = "ProjectID", nullable = false, length = 20)),
			@AttributeOverride(name = "projectName", column = @Column(name = "ProjectName", length = 250)),
			@AttributeOverride(name = "customerId", column = @Column(name = "CustomerID", length = 20)),
			@AttributeOverride(name = "customerName", column = @Column(name = "CustomerName", length = 250)),
			@AttributeOverride(name = "technologyId", column = @Column(name = "TechnologyID", length = 20)),
			@AttributeOverride(name = "technology", column = @Column(name = "Technology", length = 250)),
			@AttributeOverride(name = "domainId", column = @Column(name = "DomainID", length = 20)),
			@AttributeOverride(name = "domain", column = @Column(name = "Domain", length = 250)),
			@AttributeOverride(name = "subdomain", column = @Column(name = "subdomain", length = 250)),
			@AttributeOverride(name = "subActivityId", column = @Column(name = "SubActivityID", length = 20)),
			@AttributeOverride(name = "subActivity", column = @Column(name = "SubActivity", length = 250)),
			@AttributeOverride(name = "subActivityFlowChartDefId", column = @Column(name = "SubActivityFlowChartDefID", length = 25)),
			@AttributeOverride(name = "fcstepDetailsId", column = @Column(name = "FCStepDetailsID", length = 25)),
			@AttributeOverride(name = "executionType", column = @Column(name = "ExecutionType", length = 25)),
			@AttributeOverride(name = "automatedWfs", column = @Column(name = "Automated WFs", length = 250)),
			@AttributeOverride(name = "uploadedOn", column = @Column(name = "UploadedON", length = 30)) })
	public TblVwIsfWfDetailJobdoneId getId() {
		return this.id;
	}

	public void setId(TblVwIsfWfDetailJobdoneId id) {
		this.id = id;
	}

}
