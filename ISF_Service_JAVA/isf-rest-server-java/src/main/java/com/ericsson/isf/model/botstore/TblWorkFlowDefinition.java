package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TblWorkFlowDefinition generated by hbm2java
 */
@Entity
@Table(name = "TBL_WORK_FLOW_DEFINITION", schema = "transactionalData")
public class TblWorkFlowDefinition implements java.io.Serializable {

	private int workFlowId;
	private TblSubactivity tblSubactivity;
	private Serializable workFlowName;
	private Integer projectId;
	private Serializable flowChartType;
	private Integer versionNumber;
	private Serializable createdBy;
	private Date createdOn;

	public TblWorkFlowDefinition() {
	}

	public TblWorkFlowDefinition(int workFlowId, TblSubactivity tblSubactivity) {
		this.workFlowId = workFlowId;
		this.tblSubactivity = tblSubactivity;
	}

	public TblWorkFlowDefinition(int workFlowId, TblSubactivity tblSubactivity, Serializable workFlowName,
			Integer projectId, Serializable flowChartType, Integer versionNumber, Serializable createdBy,
			Date createdOn) {
		this.workFlowId = workFlowId;
		this.tblSubactivity = tblSubactivity;
		this.workFlowName = workFlowName;
		this.projectId = projectId;
		this.flowChartType = flowChartType;
		this.versionNumber = versionNumber;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	@Id

	@Column(name = "WorkFlowID", unique = true, nullable = false)
	public int getWorkFlowId() {
		return this.workFlowId;
	}

	public void setWorkFlowId(int workFlowId) {
		this.workFlowId = workFlowId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SubActivityID", nullable = false)
	public TblSubactivity getTblSubactivity() {
		return this.tblSubactivity;
	}

	public void setTblSubactivity(TblSubactivity tblSubactivity) {
		this.tblSubactivity = tblSubactivity;
	}

	@Column(name = "WorkFlowName")
	public Serializable getWorkFlowName() {
		return this.workFlowName;
	}

	public void setWorkFlowName(Serializable workFlowName) {
		this.workFlowName = workFlowName;
	}

	@Column(name = "ProjectID")
	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Column(name = "FlowChartType")
	public Serializable getFlowChartType() {
		return this.flowChartType;
	}

	public void setFlowChartType(Serializable flowChartType) {
		this.flowChartType = flowChartType;
	}

	@Column(name = "VersionNumber")
	public Integer getVersionNumber() {
		return this.versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	@Column(name = "CreatedBy")
	public Serializable getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Serializable createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedOn", length = 23)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
