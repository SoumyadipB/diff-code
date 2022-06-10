package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblWorkOrderFlowchartStepLinkDetailsId generated by hbm2java
 */
@Embeddable
public class TblWorkOrderFlowchartStepLinkDetailsId implements java.io.Serializable {

	private int stepLinkId;
	private Integer subActivityFlowChartDefId;
	private String flowchartSourceId;
	private String flowchartTargetId;

	public TblWorkOrderFlowchartStepLinkDetailsId() {
	}

	public TblWorkOrderFlowchartStepLinkDetailsId(int stepLinkId) {
		this.stepLinkId = stepLinkId;
	}

	public TblWorkOrderFlowchartStepLinkDetailsId(int stepLinkId, Integer subActivityFlowChartDefId,
			String flowchartSourceId, String flowchartTargetId) {
		this.stepLinkId = stepLinkId;
		this.subActivityFlowChartDefId = subActivityFlowChartDefId;
		this.flowchartSourceId = flowchartSourceId;
		this.flowchartTargetId = flowchartTargetId;
	}

	@Column(name = "StepLinkID", nullable = false)
	public int getStepLinkId() {
		return this.stepLinkId;
	}

	public void setStepLinkId(int stepLinkId) {
		this.stepLinkId = stepLinkId;
	}

	@Column(name = "SubActivityFlowChartDefID")
	public Integer getSubActivityFlowChartDefId() {
		return this.subActivityFlowChartDefId;
	}

	public void setSubActivityFlowChartDefId(Integer subActivityFlowChartDefId) {
		this.subActivityFlowChartDefId = subActivityFlowChartDefId;
	}

	@Column(name = "FlowchartSourceID", length = 512)
	public String getFlowchartSourceId() {
		return this.flowchartSourceId;
	}

	public void setFlowchartSourceId(String flowchartSourceId) {
		this.flowchartSourceId = flowchartSourceId;
	}

	@Column(name = "FlowchartTargetID", length = 512)
	public String getFlowchartTargetId() {
		return this.flowchartTargetId;
	}

	public void setFlowchartTargetId(String flowchartTargetId) {
		this.flowchartTargetId = flowchartTargetId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblWorkOrderFlowchartStepLinkDetailsId))
			return false;
		TblWorkOrderFlowchartStepLinkDetailsId castOther = (TblWorkOrderFlowchartStepLinkDetailsId) other;

		return (this.getStepLinkId() == castOther.getStepLinkId())
				&& ((this.getSubActivityFlowChartDefId() == castOther.getSubActivityFlowChartDefId())
						|| (this.getSubActivityFlowChartDefId() != null
								&& castOther.getSubActivityFlowChartDefId() != null
								&& this.getSubActivityFlowChartDefId()
										.equals(castOther.getSubActivityFlowChartDefId())))
				&& ((this.getFlowchartSourceId() == castOther.getFlowchartSourceId())
						|| (this.getFlowchartSourceId() != null && castOther.getFlowchartSourceId() != null
								&& this.getFlowchartSourceId().equals(castOther.getFlowchartSourceId())))
				&& ((this.getFlowchartTargetId() == castOther.getFlowchartTargetId())
						|| (this.getFlowchartTargetId() != null && castOther.getFlowchartTargetId() != null
								&& this.getFlowchartTargetId().equals(castOther.getFlowchartTargetId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getStepLinkId();
		result = 37 * result
				+ (getSubActivityFlowChartDefId() == null ? 0 : this.getSubActivityFlowChartDefId().hashCode());
		result = 37 * result + (getFlowchartSourceId() == null ? 0 : this.getFlowchartSourceId().hashCode());
		result = 37 * result + (getFlowchartTargetId() == null ? 0 : this.getFlowchartTargetId().hashCode());
		return result;
	}

}