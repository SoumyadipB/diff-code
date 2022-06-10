package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblScopeidTaskMapping generated by hbm2java
 */
@Entity
@Table(name = "TBL_SCOPEID_TASK_MAPPING", schema = "transactionalData")
public class TblScopeidTaskMapping implements java.io.Serializable {

	private TblScopeidTaskMappingId id;

	public TblScopeidTaskMapping() {
	}

	public TblScopeidTaskMapping(TblScopeidTaskMappingId id) {
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
	public TblScopeidTaskMappingId getId() {
		return this.id;
	}

	public void setId(TblScopeidTaskMappingId id) {
		this.id = id;
	}

}
