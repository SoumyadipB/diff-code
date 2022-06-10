package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblVwIsfPositiondetailsJobDone generated by hbm2java
 */
@Entity
@Table(name = "TBL_vw_ISF_Positiondetails_JobDone", schema = "refData")
public class TblVwIsfPositiondetailsJobDone implements java.io.Serializable {

	private TblVwIsfPositiondetailsJobDoneId id;

	public TblVwIsfPositiondetailsJobDone() {
	}

	public TblVwIsfPositiondetailsJobDone(TblVwIsfPositiondetailsJobDoneId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "marketAreaId", column = @Column(name = "MarketAreaID", nullable = false, length = 15)),
			@AttributeOverride(name = "marketAreaName", column = @Column(name = "MarketAreaName", nullable = false, length = 20)),
			@AttributeOverride(name = "serviceArea", column = @Column(name = "ServiceArea", length = 50)),
			@AttributeOverride(name = "projectId", column = @Column(name = "ProjectID", length = 20)),
			@AttributeOverride(name = "projectName", column = @Column(name = "ProjectName", length = 200)),
			@AttributeOverride(name = "projectType", column = @Column(name = "ProjectType", length = 20)),
			@AttributeOverride(name = "customerId", column = @Column(name = "CustomerID", length = 15)),
			@AttributeOverride(name = "customerName", column = @Column(name = "CustomerName", length = 100)),
			@AttributeOverride(name = "cpm", column = @Column(name = "CPM", length = 8)),
			@AttributeOverride(name = "projectCreator", column = @Column(name = "ProjectCreator", length = 14)),
			@AttributeOverride(name = "projectStartDate", column = @Column(name = "ProjectStartDate", length = 25)),
			@AttributeOverride(name = "projectEndDate", column = @Column(name = "ProjectEndDate", length = 25)),
			@AttributeOverride(name = "projectCreatedOn", column = @Column(name = "ProjectCreatedON", length = 25)),
			@AttributeOverride(name = "projectStatus", column = @Column(name = "ProjectStatus", length = 20)),
			@AttributeOverride(name = "operationalManager", column = @Column(name = "OperationalManager", length = 20)),
			@AttributeOverride(name = "projectScopeDetailId", column = @Column(name = "ProjectScopeDetailID", length = 20)),
			@AttributeOverride(name = "domainId", column = @Column(name = "DomainID", length = 20)),
			@AttributeOverride(name = "domain", column = @Column(name = "Domain", length = 200)),
			@AttributeOverride(name = "subDomain", column = @Column(name = "SubDomain", length = 200)),
			@AttributeOverride(name = "technologyId", column = @Column(name = "TechnologyID", length = 20)),
			@AttributeOverride(name = "technology", column = @Column(name = "Technology", length = 200)),
			@AttributeOverride(name = "resourceRequestId", column = @Column(name = "ResourceRequestID", length = 20)),
			@AttributeOverride(name = "resourceType", column = @Column(name = "ResourceType", length = 200)),
			@AttributeOverride(name = "resourcePositionId", column = @Column(name = "ResourcePositionID", length = 20)),
			@AttributeOverride(name = "positionStatus", column = @Column(name = "PositionStatus", length = 50)),
			@AttributeOverride(name = "remoteOnsite", column = @Column(name = "Remote_Onsite", length = 15)),
			@AttributeOverride(name = "resourcPositionIsActive", column = @Column(name = "ResourcPositionIsActive", length = 25)),
			@AttributeOverride(name = "workEffortId", column = @Column(name = "WorkEffortID", length = 20)),
			@AttributeOverride(name = "workEffortStartDate", column = @Column(name = "WorkEffortStartDate", length = 25)),
			@AttributeOverride(name = "workEffortEndDate", column = @Column(name = "WorkEffortEndDate", length = 25)),
			@AttributeOverride(name = "workEffortDuration", column = @Column(name = "WorkEffortDuration", length = 20)),
			@AttributeOverride(name = "ftePercent", column = @Column(name = "FTE_Percent", length = 20)),
			@AttributeOverride(name = "hours", column = @Column(name = "Hours", length = 10)),
			@AttributeOverride(name = "signum", column = @Column(name = "Signum", length = 10)),
			@AttributeOverride(name = "workEffortStatus", column = @Column(name = "WorkEffortStatus", length = 20)),
			@AttributeOverride(name = "workEffortIsActive", column = @Column(name = "WorkEffortIsActive", length = 20)),
			@AttributeOverride(name = "crid", column = @Column(name = "CRID", length = 20)),
			@AttributeOverride(name = "workEffortPositionStatus", column = @Column(name = "WorkEffortPositionStatus", length = 30)),
			@AttributeOverride(name = "workEffortCreatedBy", column = @Column(name = "WorkEffortCreatedBy", length = 20)),
			@AttributeOverride(name = "workEffortCreatedOn", column = @Column(name = "WorkEffortCreatedOn", length = 30)),
			@AttributeOverride(name = "uploadedOn", column = @Column(name = "UploadedON", nullable = false, length = 30)) })
	public TblVwIsfPositiondetailsJobDoneId getId() {
		return this.id;
	}

	public void setId(TblVwIsfPositiondetailsJobDoneId id) {
		this.id = id;
	}

}