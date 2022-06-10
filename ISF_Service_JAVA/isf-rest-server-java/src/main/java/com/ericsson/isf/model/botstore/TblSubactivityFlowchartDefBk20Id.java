package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblSubactivityFlowchartDefBk20Id generated by hbm2java
 */
@Embeddable
public class TblSubactivityFlowchartDefBk20Id implements java.io.Serializable {

	private int subActivityFlowChartDefId;
	private Integer projectId;
	private Integer subActivityId;
	private Serializable flowChartJson;
	private Integer sourceProjectId;
	private String createdBy;
	private Date createdDate;
	private Boolean active;
	private Integer versionNumber;
	private String type;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String workFlowName;

	public TblSubactivityFlowchartDefBk20Id() {
	}

	public TblSubactivityFlowchartDefBk20Id(int subActivityFlowChartDefId) {
		this.subActivityFlowChartDefId = subActivityFlowChartDefId;
	}

	public TblSubactivityFlowchartDefBk20Id(int subActivityFlowChartDefId, Integer projectId, Integer subActivityId,
			Serializable flowChartJson, Integer sourceProjectId, String createdBy, Date createdDate, Boolean active,
			Integer versionNumber, String type, String lastModifiedBy, Date lastModifiedDate, String workFlowName) {
		this.subActivityFlowChartDefId = subActivityFlowChartDefId;
		this.projectId = projectId;
		this.subActivityId = subActivityId;
		this.flowChartJson = flowChartJson;
		this.sourceProjectId = sourceProjectId;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.active = active;
		this.versionNumber = versionNumber;
		this.type = type;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.workFlowName = workFlowName;
	}

	@Column(name = "SubActivityFlowChartDefID", nullable = false)
	public int getSubActivityFlowChartDefId() {
		return this.subActivityFlowChartDefId;
	}

	public void setSubActivityFlowChartDefId(int subActivityFlowChartDefId) {
		this.subActivityFlowChartDefId = subActivityFlowChartDefId;
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

	@Column(name = "SourceProjectID")
	public Integer getSourceProjectId() {
		return this.sourceProjectId;
	}

	public void setSourceProjectId(Integer sourceProjectId) {
		this.sourceProjectId = sourceProjectId;
	}

	@Column(name = "CreatedBy", length = 1024)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CreatedDate", length = 23)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "Active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "VersionNumber")
	public Integer getVersionNumber() {
		return this.versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	@Column(name = "Type", length = 512)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "LastModifiedBy", length = 256)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Column(name = "LastModifiedDate", length = 23)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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
		if (!(other instanceof TblSubactivityFlowchartDefBk20Id))
			return false;
		TblSubactivityFlowchartDefBk20Id castOther = (TblSubactivityFlowchartDefBk20Id) other;

		return (this.getSubActivityFlowChartDefId() == castOther.getSubActivityFlowChartDefId())
				&& ((this.getProjectId() == castOther.getProjectId()) || (this.getProjectId() != null
						&& castOther.getProjectId() != null && this.getProjectId().equals(castOther.getProjectId())))
				&& ((this.getSubActivityId() == castOther.getSubActivityId())
						|| (this.getSubActivityId() != null && castOther.getSubActivityId() != null
								&& this.getSubActivityId().equals(castOther.getSubActivityId())))
				&& ((this.getFlowChartJson() == castOther.getFlowChartJson())
						|| (this.getFlowChartJson() != null && castOther.getFlowChartJson() != null
								&& this.getFlowChartJson().equals(castOther.getFlowChartJson())))
				&& ((this.getSourceProjectId() == castOther.getSourceProjectId())
						|| (this.getSourceProjectId() != null && castOther.getSourceProjectId() != null
								&& this.getSourceProjectId().equals(castOther.getSourceProjectId())))
				&& ((this.getCreatedBy() == castOther.getCreatedBy()) || (this.getCreatedBy() != null
						&& castOther.getCreatedBy() != null && this.getCreatedBy().equals(castOther.getCreatedBy())))
				&& ((this.getCreatedDate() == castOther.getCreatedDate())
						|| (this.getCreatedDate() != null && castOther.getCreatedDate() != null
								&& this.getCreatedDate().equals(castOther.getCreatedDate())))
				&& ((this.getActive() == castOther.getActive()) || (this.getActive() != null
						&& castOther.getActive() != null && this.getActive().equals(castOther.getActive())))
				&& ((this.getVersionNumber() == castOther.getVersionNumber())
						|| (this.getVersionNumber() != null && castOther.getVersionNumber() != null
								&& this.getVersionNumber().equals(castOther.getVersionNumber())))
				&& ((this.getType() == castOther.getType()) || (this.getType() != null && castOther.getType() != null
						&& this.getType().equals(castOther.getType())))
				&& ((this.getLastModifiedBy() == castOther.getLastModifiedBy())
						|| (this.getLastModifiedBy() != null && castOther.getLastModifiedBy() != null
								&& this.getLastModifiedBy().equals(castOther.getLastModifiedBy())))
				&& ((this.getLastModifiedDate() == castOther.getLastModifiedDate())
						|| (this.getLastModifiedDate() != null && castOther.getLastModifiedDate() != null
								&& this.getLastModifiedDate().equals(castOther.getLastModifiedDate())))
				&& ((this.getWorkFlowName() == castOther.getWorkFlowName())
						|| (this.getWorkFlowName() != null && castOther.getWorkFlowName() != null
								&& this.getWorkFlowName().equals(castOther.getWorkFlowName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getSubActivityFlowChartDefId();
		result = 37 * result + (getProjectId() == null ? 0 : this.getProjectId().hashCode());
		result = 37 * result + (getSubActivityId() == null ? 0 : this.getSubActivityId().hashCode());
		result = 37 * result + (getFlowChartJson() == null ? 0 : this.getFlowChartJson().hashCode());
		result = 37 * result + (getSourceProjectId() == null ? 0 : this.getSourceProjectId().hashCode());
		result = 37 * result + (getCreatedBy() == null ? 0 : this.getCreatedBy().hashCode());
		result = 37 * result + (getCreatedDate() == null ? 0 : this.getCreatedDate().hashCode());
		result = 37 * result + (getActive() == null ? 0 : this.getActive().hashCode());
		result = 37 * result + (getVersionNumber() == null ? 0 : this.getVersionNumber().hashCode());
		result = 37 * result + (getType() == null ? 0 : this.getType().hashCode());
		result = 37 * result + (getLastModifiedBy() == null ? 0 : this.getLastModifiedBy().hashCode());
		result = 37 * result + (getLastModifiedDate() == null ? 0 : this.getLastModifiedDate().hashCode());
		result = 37 * result + (getWorkFlowName() == null ? 0 : this.getWorkFlowName().hashCode());
		return result;
	}

}