package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ProjectWiseJsonId generated by hbm2java
 */
@Embeddable
public class ProjectWiseJsonId implements java.io.Serializable {

	private Integer projectId;
	private Integer subActivityId;
	private Serializable flowChartJson;
	private Integer versionNumber;
	private String workFlowName;

	public ProjectWiseJsonId() {
	}

	public ProjectWiseJsonId(Integer projectId, Integer subActivityId, Serializable flowChartJson,
			Integer versionNumber, String workFlowName) {
		this.projectId = projectId;
		this.subActivityId = subActivityId;
		this.flowChartJson = flowChartJson;
		this.versionNumber = versionNumber;
		this.workFlowName = workFlowName;
	}

	@Column(name = "ProjectID")
	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Column(name = "SubActivityID")
	public Integer getSubActivityId() {
		return this.subActivityId;
	}

	public void setSubActivityId(Integer subActivityId) {
		this.subActivityId = subActivityId;
	}

	@Column(name = "FlowChartJSON")
	public Serializable getFlowChartJson() {
		return this.flowChartJson;
	}

	public void setFlowChartJson(Serializable flowChartJson) {
		this.flowChartJson = flowChartJson;
	}

	@Column(name = "VersionNumber")
	public Integer getVersionNumber() {
		return this.versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	@Column(name = "WorkFlowName")
	public String getWorkFlowName() {
		return this.workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ProjectWiseJsonId))
			return false;
		ProjectWiseJsonId castOther = (ProjectWiseJsonId) other;

		return ((this.getProjectId() == castOther.getProjectId()) || (this.getProjectId() != null
				&& castOther.getProjectId() != null && this.getProjectId().equals(castOther.getProjectId())))
				&& ((this.getSubActivityId() == castOther.getSubActivityId())
						|| (this.getSubActivityId() != null && castOther.getSubActivityId() != null
								&& this.getSubActivityId().equals(castOther.getSubActivityId())))
				&& ((this.getFlowChartJson() == castOther.getFlowChartJson())
						|| (this.getFlowChartJson() != null && castOther.getFlowChartJson() != null
								&& this.getFlowChartJson().equals(castOther.getFlowChartJson())))
				&& ((this.getVersionNumber() == castOther.getVersionNumber())
						|| (this.getVersionNumber() != null && castOther.getVersionNumber() != null
								&& this.getVersionNumber().equals(castOther.getVersionNumber())))
				&& ((this.getWorkFlowName() == castOther.getWorkFlowName())
						|| (this.getWorkFlowName() != null && castOther.getWorkFlowName() != null
								&& this.getWorkFlowName().equals(castOther.getWorkFlowName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getProjectId() == null ? 0 : this.getProjectId().hashCode());
		result = 37 * result + (getSubActivityId() == null ? 0 : this.getSubActivityId().hashCode());
		result = 37 * result + (getFlowChartJson() == null ? 0 : this.getFlowChartJson().hashCode());
		result = 37 * result + (getVersionNumber() == null ? 0 : this.getVersionNumber().hashCode());
		result = 37 * result + (getWorkFlowName() == null ? 0 : this.getWorkFlowName().hashCode());
		return result;
	}

}