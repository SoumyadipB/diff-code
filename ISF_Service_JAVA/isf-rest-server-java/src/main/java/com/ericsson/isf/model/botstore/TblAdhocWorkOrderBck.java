package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblAdhocWorkOrderBck generated by hbm2java
 */
@Entity
@Table(name = "TBL_ADHOC_WORK_ORDER_Bck", schema = "transactionalData")
public class TblAdhocWorkOrderBck implements java.io.Serializable {

	private TblAdhocWorkOrderBckId id;

	public TblAdhocWorkOrderBck() {
	}

	public TblAdhocWorkOrderBck(TblAdhocWorkOrderBckId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "adhocWoid", column = @Column(name = "AdhocWOID", nullable = false)),
			@AttributeOverride(name = "woName", column = @Column(name = "WoName", length = 1024)),
			@AttributeOverride(name = "projectId", column = @Column(name = "ProjectID")),
			@AttributeOverride(name = "domainId", column = @Column(name = "DomainID")),
			@AttributeOverride(name = "serviceAreaId", column = @Column(name = "ServiceAreaID")),
			@AttributeOverride(name = "technologyId", column = @Column(name = "TechnologyID")),
			@AttributeOverride(name = "subActivityId", column = @Column(name = "SubActivityID")),
			@AttributeOverride(name = "startDate", column = @Column(name = "StartDate", length = 10)),
			@AttributeOverride(name = "avgEstdEffort", column = @Column(name = "AvgEstdEffort", precision = 53, scale = 0)),
			@AttributeOverride(name = "market", column = @Column(name = "Market", length = 1024)),
			@AttributeOverride(name = "vendor", column = @Column(name = "Vendor", length = 512)),
			@AttributeOverride(name = "priority", column = @Column(name = "Priority", length = 1024)),
			@AttributeOverride(name = "createdBy", column = @Column(name = "CreatedBy", length = 1024)),
			@AttributeOverride(name = "createdDate", column = @Column(name = "CreatedDate", length = 23)),
			@AttributeOverride(name = "active", column = @Column(name = "Active")),
			@AttributeOverride(name = "startTime", column = @Column(name = "StartTime", length = 16)),
			@AttributeOverride(name = "assignedTo", column = @Column(name = "AssignedTO")),
			@AttributeOverride(name = "sla", column = @Column(name = "SLA", precision = 53, scale = 0)),
			@AttributeOverride(name = "scopeId", column = @Column(name = "ScopeID")) })
	public TblAdhocWorkOrderBckId getId() {
		return this.id;
	}

	public void setId(TblAdhocWorkOrderBckId id) {
		this.id = id;
	}

}
