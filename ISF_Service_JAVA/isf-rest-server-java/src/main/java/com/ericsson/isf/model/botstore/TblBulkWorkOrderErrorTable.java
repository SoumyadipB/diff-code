package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblBulkWorkOrderErrorTable generated by hbm2java
 */
@Entity
@Table(name = "TBL_BULK_WORK_ORDER_ERROR_TABLE", schema = "transactionalData")
public class TblBulkWorkOrderErrorTable implements java.io.Serializable {

	private TblBulkWorkOrderErrorTableId id;

	public TblBulkWorkOrderErrorTable() {
	}

	public TblBulkWorkOrderErrorTable(TblBulkWorkOrderErrorTableId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "wocreationId", column = @Column(name = "WOCreationID")),
			@AttributeOverride(name = "projectId", column = @Column(name = "ProjectID")),
			@AttributeOverride(name = "subserviceArea", column = @Column(name = "SubserviceArea", length = 1024)),
			@AttributeOverride(name = "domain", column = @Column(name = "Domain", length = 1024)),
			@AttributeOverride(name = "subDomain", column = @Column(name = "SubDomain", length = 1024)),
			@AttributeOverride(name = "technology", column = @Column(name = "Technology", length = 128)),
			@AttributeOverride(name = "woid", column = @Column(name = "WOID")),
			@AttributeOverride(name = "scopeName", column = @Column(name = "ScopeName", length = 1024)),
			@AttributeOverride(name = "subActivityId", column = @Column(name = "SubActivityID")),
			@AttributeOverride(name = "subActivityName", column = @Column(name = "SubActivityName", length = 1024)),
			@AttributeOverride(name = "priority", column = @Column(name = "Priority", length = 1024)),
			@AttributeOverride(name = "slahours", column = @Column(name = "SLAHours", length = 20)),
			@AttributeOverride(name = "assignTo", column = @Column(name = "AssignTo", length = 1024)),
			@AttributeOverride(name = "startDate", column = @Column(name = "StartDate", length = 10)),
			@AttributeOverride(name = "startTime", column = @Column(name = "StartTime", length = 16)),
			@AttributeOverride(name = "workOrderName", column = @Column(name = "WorkOrderName", length = 1024)),
			@AttributeOverride(name = "workFlowName", column = @Column(name = "WorkFlowName", length = 1024)),
			@AttributeOverride(name = "nodeType", column = @Column(name = "NodeType", length = 1024)),
			@AttributeOverride(name = "nodeNames", column = @Column(name = "NodeNames", length = 1024)),
			@AttributeOverride(name = "createdBy", column = @Column(name = "CreatedBy", length = 1024)),
			@AttributeOverride(name = "createdDate", column = @Column(name = "CreatedDate", length = 23)),
			@AttributeOverride(name = "lastModifiedBy", column = @Column(name = "LastModifiedBy", length = 1024)),
			@AttributeOverride(name = "lastModifiedDate", column = @Column(name = "LastModifiedDate", length = 23)),
			@AttributeOverride(name = "externalSourceId", column = @Column(name = "ExternalSourceID")),
			@AttributeOverride(name = "externalSourceName", column = @Column(name = "ExternalSourceName", length = 1024)),
			@AttributeOverride(name = "uploadedBy", column = @Column(name = "UploadedBy", length = 1024)),
			@AttributeOverride(name = "executionPlanId", column = @Column(name = "ExecutionPlanID")),
			@AttributeOverride(name = "errorCategory", column = @Column(name = "ErrorCategory", length = 1024)),
			@AttributeOverride(name = "errorDetails", column = @Column(name = "ErrorDetails")) })
	public TblBulkWorkOrderErrorTableId getId() {
		return this.id;
	}

	public void setId(TblBulkWorkOrderErrorTableId id) {
		this.id = id;
	}

}