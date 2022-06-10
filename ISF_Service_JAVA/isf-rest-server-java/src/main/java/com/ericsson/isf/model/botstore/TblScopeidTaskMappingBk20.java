package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblScopeidTaskMappingBk20 generated by hbm2java
 */
@Entity
@Table(name = "TBL_SCOPEID_TASK_MAPPING_bk_2_0", schema = "transactionalData")
public class TblScopeidTaskMappingBk20 implements java.io.Serializable {

	private TblScopeidTaskMappingBk20Id id;

	public TblScopeidTaskMappingBk20() {
	}

	public TblScopeidTaskMappingBk20(TblScopeidTaskMappingBk20Id id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "projectId", column = @Column(name = "ProjectID")),
			@AttributeOverride(name = "scopeId", column = @Column(name = "ScopeID")),
			@AttributeOverride(name = "subActivityId", column = @Column(name = "SubActivityID")),
			@AttributeOverride(name = "taskId", column = @Column(name = "TaskID")),
			@AttributeOverride(name = "task", column = @Column(name = "Task", length = 1024)),
			@AttributeOverride(name = "executionType", column = @Column(name = "ExecutionType", length = 1024)),
			@AttributeOverride(name = "avgEstdEffort", column = @Column(name = "AvgEstdEffort", precision = 53, scale = 0)),
			@AttributeOverride(name = "tool", column = @Column(name = "Tool", length = 256)),
			@AttributeOverride(name = "scopeTaskMappingId", column = @Column(name = "ScopeTaskMappingID", nullable = false)),
			@AttributeOverride(name = "toolId", column = @Column(name = "ToolID")),
			@AttributeOverride(name = "rpaId", column = @Column(name = "RpaID")),
			@AttributeOverride(name = "rpaName", column = @Column(name = "RpaName", length = 256)),
			@AttributeOverride(name = "masterTask", column = @Column(name = "MasterTask", length = 1024)),
			@AttributeOverride(name = "versionNumber", column = @Column(name = "VersionNumber")) })
	public TblScopeidTaskMappingBk20Id getId() {
		return this.id;
	}

	public void setId(TblScopeidTaskMappingBk20Id id) {
		this.id = id;
	}

}
