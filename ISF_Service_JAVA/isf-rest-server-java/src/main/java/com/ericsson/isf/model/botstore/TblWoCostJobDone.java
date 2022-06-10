package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblWoCostJobDone generated by hbm2java
 */
@Entity
@Table(name = "tbl_WO_Cost_JobDone", schema = "transactionalData")
public class TblWoCostJobDone implements java.io.Serializable {

	private TblWoCostJobDoneId id;

	public TblWoCostJobDone() {
	}

	public TblWoCostJobDone(TblWoCostJobDoneId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "projectId", column = @Column(name = "ProjectID")),
			@AttributeOverride(name = "projectName", column = @Column(name = "ProjectName")),
			@AttributeOverride(name = "woid", column = @Column(name = "WOID")),
			@AttributeOverride(name = "subActivityId", column = @Column(name = "SubActivityID")),
			@AttributeOverride(name = "engineer", column = @Column(name = "Engineer", length = 1024)),
			@AttributeOverride(name = "date", column = @Column(name = "Date", length = 50)),
			@AttributeOverride(name = "subActivity", column = @Column(name = "SubActivity", length = 512)),
			@AttributeOverride(name = "woStatus", column = @Column(name = "WO Status", length = 512)),
			@AttributeOverride(name = "totalManualHours", column = @Column(name = "Total Manual Hours", precision = 53, scale = 0)),
			@AttributeOverride(name = "manualCost", column = @Column(name = "Manual Cost", precision = 53, scale = 0)),
			@AttributeOverride(name = "manualCostSek", column = @Column(name = "Manual Cost SEK", precision = 10)) })
	public TblWoCostJobDoneId getId() {
		return this.id;
	}

	public void setId(TblWoCostJobDoneId id) {
		this.id = id;
	}

}