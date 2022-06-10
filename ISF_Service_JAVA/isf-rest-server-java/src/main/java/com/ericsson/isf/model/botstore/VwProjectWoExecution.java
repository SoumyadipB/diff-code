package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * VwProjectWoExecution generated by hbm2java
 */
@Entity
@Table(name = "vw_Project_WO_Execution", schema = "dbo")
public class VwProjectWoExecution implements java.io.Serializable {

	private VwProjectWoExecutionId id;

	public VwProjectWoExecution() {
	}

	public VwProjectWoExecution(VwProjectWoExecutionId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "projectId", column = @Column(name = "ProjectID")),
			@AttributeOverride(name = "subActivityId", column = @Column(name = "SubActivityID")),
			@AttributeOverride(name = "subActivity", column = @Column(name = "SubActivity", length = 512)),
			@AttributeOverride(name = "woid", column = @Column(name = "WOID")),
			@AttributeOverride(name = "bookingId", column = @Column(name = "BookingID", nullable = false)),
			@AttributeOverride(name = "taskId", column = @Column(name = "TaskID")),
			@AttributeOverride(name = "task", column = @Column(name = "Task", length = 512)),
			@AttributeOverride(name = "startDate", column = @Column(name = "StartDate", length = 23)),
			@AttributeOverride(name = "endDate", column = @Column(name = "EndDate", length = 23)),
			@AttributeOverride(name = "hours", column = @Column(name = "Hours", precision = 53, scale = 0)),
			@AttributeOverride(name = "parentBookingDetailsId", column = @Column(name = "ParentBookingDetailsID")),
			@AttributeOverride(name = "type", column = @Column(name = "Type", length = 512)),
			@AttributeOverride(name = "status", column = @Column(name = "Status", length = 512)),
			@AttributeOverride(name = "signumId", column = @Column(name = "SignumID", length = 512)),
			@AttributeOverride(name = "reason", column = @Column(name = "Reason", length = 1024)),
			@AttributeOverride(name = "outputLink", column = @Column(name = "OutputLink")) })
	public VwProjectWoExecutionId getId() {
		return this.id;
	}

	public void setId(VwProjectWoExecutionId id) {
		this.id = id;
	}

}
