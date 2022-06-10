package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TblExecutionPlanFlow generated by hbm2java
 */
@Entity
@Table(name = "TBL_ExecutionPlan_Flow", schema = "transactionalData")
public class TblExecutionPlanFlow implements java.io.Serializable {

	private int executionPlanFlowId;
	private TblExecutionPlanDetails tblExecutionPlanDetails;
	private int woid;
	private Boolean isComplete;
	private Long planid;
	private Integer execPlanGroupId;

	public TblExecutionPlanFlow() {
	}

	public TblExecutionPlanFlow(int executionPlanFlowId, TblExecutionPlanDetails tblExecutionPlanDetails, int woid) {
		this.executionPlanFlowId = executionPlanFlowId;
		this.tblExecutionPlanDetails = tblExecutionPlanDetails;
		this.woid = woid;
	}

	public TblExecutionPlanFlow(int executionPlanFlowId, TblExecutionPlanDetails tblExecutionPlanDetails, int woid,
			Boolean isComplete, Long planid, Integer execPlanGroupId) {
		this.executionPlanFlowId = executionPlanFlowId;
		this.tblExecutionPlanDetails = tblExecutionPlanDetails;
		this.woid = woid;
		this.isComplete = isComplete;
		this.planid = planid;
		this.execPlanGroupId = execPlanGroupId;
	}

	@Id

	@Column(name = "executionPlanFlowId", unique = true, nullable = false)
	public int getExecutionPlanFlowId() {
		return this.executionPlanFlowId;
	}

	public void setExecutionPlanFlowId(int executionPlanFlowId) {
		this.executionPlanFlowId = executionPlanFlowId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "executionPlanDetailId", nullable = false)
	public TblExecutionPlanDetails getTblExecutionPlanDetails() {
		return this.tblExecutionPlanDetails;
	}

	public void setTblExecutionPlanDetails(TblExecutionPlanDetails tblExecutionPlanDetails) {
		this.tblExecutionPlanDetails = tblExecutionPlanDetails;
	}

	@Column(name = "woid", nullable = false)
	public int getWoid() {
		return this.woid;
	}

	public void setWoid(int woid) {
		this.woid = woid;
	}

	@Column(name = "isComplete")
	public Boolean getIsComplete() {
		return this.isComplete;
	}

	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}

	@Column(name = "planid")
	public Long getPlanid() {
		return this.planid;
	}

	public void setPlanid(Long planid) {
		this.planid = planid;
	}

	@Column(name = "execPlanGroupId")
	public Integer getExecPlanGroupId() {
		return this.execPlanGroupId;
	}

	public void setExecPlanGroupId(Integer execPlanGroupId) {
		this.execPlanGroupId = execPlanGroupId;
	}

}
