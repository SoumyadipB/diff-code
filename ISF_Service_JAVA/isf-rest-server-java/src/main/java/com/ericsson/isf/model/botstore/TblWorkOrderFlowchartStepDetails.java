package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblWorkOrderFlowchartStepDetails generated by hbm2java
 */
@Entity
@Table(name = "TBL_WORK_ORDER_FLOWCHART_STEP_DETAILS", schema = "transactionalData")
public class TblWorkOrderFlowchartStepDetails implements java.io.Serializable {

	private TblWorkOrderFlowchartStepDetailsId id;

	public TblWorkOrderFlowchartStepDetails() {
	}

	public TblWorkOrderFlowchartStepDetails(TblWorkOrderFlowchartStepDetailsId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "wofcstepDetailsId", column = @Column(name = "WOFCStepDetailsID", nullable = false)),
			@AttributeOverride(name = "woid", column = @Column(name = "WOID")),
			@AttributeOverride(name = "flowChartDefId", column = @Column(name = "FlowChartDefID")),
			@AttributeOverride(name = "flowChartStepId", column = @Column(name = "FlowChartStepID")),
			@AttributeOverride(name = "taskId", column = @Column(name = "TaskID")),
			@AttributeOverride(name = "executionType", column = @Column(name = "ExecutionType", length = 1024)),
			@AttributeOverride(name = "bookingId", column = @Column(name = "BookingID")),
			@AttributeOverride(name = "status", column = @Column(name = "Status", length = 1024)),
			@AttributeOverride(name = "decisionValue", column = @Column(name = "DecisionValue", length = 1024)),
			@AttributeOverride(name = "signumId", column = @Column(name = "SignumID", length = 1024)),
			@AttributeOverride(name = "bookedOn", column = @Column(name = "BookedOn", length = 23)) })
	public TblWorkOrderFlowchartStepDetailsId getId() {
		return this.id;
	}

	public void setId(TblWorkOrderFlowchartStepDetailsId id) {
		this.id = id;
	}

}
