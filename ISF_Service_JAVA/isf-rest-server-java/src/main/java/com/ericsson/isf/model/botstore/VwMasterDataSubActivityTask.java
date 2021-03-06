package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * VwMasterDataSubActivityTask generated by hbm2java
 */
@Entity
@Table(name = "vw_MasterData_SubActivity_Task", schema = "dbo")
public class VwMasterDataSubActivityTask implements java.io.Serializable {

	private VwMasterDataSubActivityTaskId id;

	public VwMasterDataSubActivityTask() {
	}

	public VwMasterDataSubActivityTask(VwMasterDataSubActivityTaskId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "subActivityId", column = @Column(name = "SubActivityID", nullable = false)),
			@AttributeOverride(name = "domainId", column = @Column(name = "DomainID")),
			@AttributeOverride(name = "domain", column = @Column(name = "Domain", length = 128)),
			@AttributeOverride(name = "subDomain", column = @Column(name = "SubDomain", length = 128)),
			@AttributeOverride(name = "serviceAreaId", column = @Column(name = "ServiceAreaID")),
			@AttributeOverride(name = "serviceArea", column = @Column(name = "ServiceArea", length = 128)),
			@AttributeOverride(name = "subServiceArea", column = @Column(name = "SubServiceArea", length = 128)),
			@AttributeOverride(name = "technologyId", column = @Column(name = "TechnologyID")),
			@AttributeOverride(name = "technology", column = @Column(name = "Technology", length = 128)),
			@AttributeOverride(name = "activity", column = @Column(name = "Activity", length = 512)),
			@AttributeOverride(name = "subActivity", column = @Column(name = "SubActivity", length = 512)),
			@AttributeOverride(name = "taskId", column = @Column(name = "TaskID")),
			@AttributeOverride(name = "task", column = @Column(name = "Task", length = 512)),
			@AttributeOverride(name = "executionType", column = @Column(name = "ExecutionType", length = 128)) })
	public VwMasterDataSubActivityTaskId getId() {
		return this.id;
	}

	public void setId(VwMasterDataSubActivityTaskId id) {
		this.id = id;
	}

}
