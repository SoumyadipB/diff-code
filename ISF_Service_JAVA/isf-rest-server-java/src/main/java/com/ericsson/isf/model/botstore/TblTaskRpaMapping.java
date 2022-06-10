package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblTaskRpaMapping generated by hbm2java
 */
@Entity
@Table(name = "TBL_TASK_RPA_MAPPING", schema = "transactionalData")
public class TblTaskRpaMapping implements java.io.Serializable {

	private TblTaskRpaMappingId id;

	public TblTaskRpaMapping() {
	}

	public TblTaskRpaMapping(TblTaskRpaMappingId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "taskRpaId", column = @Column(name = "TaskRpaID", nullable = false)),
			@AttributeOverride(name = "taskId", column = @Column(name = "TaskID")),
			@AttributeOverride(name = "rpaid", column = @Column(name = "RPAID")),
			@AttributeOverride(name = "active", column = @Column(name = "Active")) })
	public TblTaskRpaMappingId getId() {
		return this.id;
	}

	public void setId(TblTaskRpaMappingId id) {
		this.id = id;
	}

}
