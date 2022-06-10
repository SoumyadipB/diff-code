package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * VwProjectAndScopeDetails generated by hbm2java
 */
@Entity
@Table(name = "vw_Project_and_ScopeDetails", schema = "refData")
public class VwProjectAndScopeDetails implements java.io.Serializable {

	private VwProjectAndScopeDetailsId id;

	public VwProjectAndScopeDetails() {
	}

	public VwProjectAndScopeDetails(VwProjectAndScopeDetailsId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "flow", column = @Column(name = "Flow", nullable = false, length = 11)),
			@AttributeOverride(name = "projectId", column = @Column(name = "ProjectID", nullable = false)),
			@AttributeOverride(name = "projectName", column = @Column(name = "ProjectName")),
			@AttributeOverride(name = "projectStartDate", column = @Column(name = "ProjectStartDate", nullable = false, length = 23)),
			@AttributeOverride(name = "projectEndDate", column = @Column(name = "ProjectEndDate", length = 23)),
			@AttributeOverride(name = "servAreaId", column = @Column(name = "ServAreaID")),
			@AttributeOverride(name = "status", column = @Column(name = "Status")),
			@AttributeOverride(name = "projectCreator", column = @Column(name = "ProjectCreator")),
			@AttributeOverride(name = "serviceAreaId", column = @Column(name = "ServiceAreaID")),
			@AttributeOverride(name = "serviceArea", column = @Column(name = "ServiceArea", length = 128)),
			@AttributeOverride(name = "pcode", column = @Column(name = "PCode")),
			@AttributeOverride(name = "opportunityId", column = @Column(name = "OpportunityID")),
			@AttributeOverride(name = "opportunityCode", column = @Column(name = "OpportunityCode", length = 100)),
			@AttributeOverride(name = "opportunityName", column = @Column(name = "OpportunityName")),
			@AttributeOverride(name = "countryId", column = @Column(name = "CountryID")),
			@AttributeOverride(name = "countryName", column = @Column(name = "CountryName")),
			@AttributeOverride(name = "customerId", column = @Column(name = "CustomerID")),
			@AttributeOverride(name = "customerName", column = @Column(name = "CustomerName")),
			@AttributeOverride(name = "marketAreaId", column = @Column(name = "MarketAreaID")),
			@AttributeOverride(name = "marketAreaName", column = @Column(name = "MarketAreaName")),
			@AttributeOverride(name = "scopeStartDate", column = @Column(name = "ScopeStartDate", length = 23)),
			@AttributeOverride(name = "scopeEndDate", column = @Column(name = "ScopeEndDate", length = 23)),
			@AttributeOverride(name = "scopeStatus", column = @Column(name = "ScopeStatus")),
			@AttributeOverride(name = "domainId", column = @Column(name = "DomainID")),
			@AttributeOverride(name = "domainSubDomain", column = @Column(name = "Domain_SubDomain", length = 259)),
			@AttributeOverride(name = "technologyId", column = @Column(name = "TechnologyID")),
			@AttributeOverride(name = "technology", column = @Column(name = "Technology", length = 128)),
			@AttributeOverride(name = "scopeDetailStatus", column = @Column(name = "ScopeDetailStatus")),
			@AttributeOverride(name = "subActivityId", column = @Column(name = "SubActivityID")),
			@AttributeOverride(name = "activity", column = @Column(name = "Activity", length = 512)),
			@AttributeOverride(name = "subActivity", column = @Column(name = "SubActivity", length = 512)),
			@AttributeOverride(name = "activityScopeStatus", column = @Column(name = "ActivityScopeStatus")),
			@AttributeOverride(name = "domainSubDomainTechnologySubactivity", column = @Column(name = "DomainSubDomainTechnologySubactivity", length = 899)) })
	public VwProjectAndScopeDetailsId getId() {
		return this.id;
	}

	public void setId(VwProjectAndScopeDetailsId id) {
		this.id = id;
	}

}