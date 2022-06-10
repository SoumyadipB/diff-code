package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TblWorkFlowSteps generated by hbm2java
 */
@Entity
@Table(name = "TBL_WORK_FLOW_STEPS", schema = "transactionalData")
public class TblWorkFlowSteps implements java.io.Serializable {

	private int workFlowStepId;
	private Integer taskId;
	private Integer rpaId;
	private Integer workFlowId;
	private Serializable taskName;
	private Serializable stepName;
	private Serializable executionType;
	private Integer avgEstdEffort;
	private Serializable stepType;
	private Integer sizeWidth;
	private Integer sizeHeight;
	private Integer positionX;
	private Integer positionY;
	private Integer angle;
	private Integer clientStepId;
	private Integer z;
	private Serializable createdBy;
	private Date createdOn;
	private Serializable action;

	public TblWorkFlowSteps() {
	}

	public TblWorkFlowSteps(int workFlowStepId) {
		this.workFlowStepId = workFlowStepId;
	}

	public TblWorkFlowSteps(int workFlowStepId, Integer taskId, Integer rpaId, Integer workFlowId,
			Serializable taskName, Serializable stepName, Serializable executionType, Integer avgEstdEffort,
			Serializable stepType, Integer sizeWidth, Integer sizeHeight, Integer positionX, Integer positionY,
			Integer angle, Integer clientStepId, Integer z, Serializable createdBy, Date createdOn,
			Serializable action) {
		this.workFlowStepId = workFlowStepId;
		this.taskId = taskId;
		this.rpaId = rpaId;
		this.workFlowId = workFlowId;
		this.taskName = taskName;
		this.stepName = stepName;
		this.executionType = executionType;
		this.avgEstdEffort = avgEstdEffort;
		this.stepType = stepType;
		this.sizeWidth = sizeWidth;
		this.sizeHeight = sizeHeight;
		this.positionX = positionX;
		this.positionY = positionY;
		this.angle = angle;
		this.clientStepId = clientStepId;
		this.z = z;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.action = action;
	}

	@Id

	@Column(name = "WorkFlowStepID", unique = true, nullable = false)
	public int getWorkFlowStepId() {
		return this.workFlowStepId;
	}

	public void setWorkFlowStepId(int workFlowStepId) {
		this.workFlowStepId = workFlowStepId;
	}

	@Column(name = "TaskID")
	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	@Column(name = "RpaID")
	public Integer getRpaId() {
		return this.rpaId;
	}

	public void setRpaId(Integer rpaId) {
		this.rpaId = rpaId;
	}

	@Column(name = "WorkFlowID")
	public Integer getWorkFlowId() {
		return this.workFlowId;
	}

	public void setWorkFlowId(Integer workFlowId) {
		this.workFlowId = workFlowId;
	}

	@Column(name = "TaskName")
	public Serializable getTaskName() {
		return this.taskName;
	}

	public void setTaskName(Serializable taskName) {
		this.taskName = taskName;
	}

	@Column(name = "StepName")
	public Serializable getStepName() {
		return this.stepName;
	}

	public void setStepName(Serializable stepName) {
		this.stepName = stepName;
	}

	@Column(name = "ExecutionType")
	public Serializable getExecutionType() {
		return this.executionType;
	}

	public void setExecutionType(Serializable executionType) {
		this.executionType = executionType;
	}

	@Column(name = "AvgEstdEffort")
	public Integer getAvgEstdEffort() {
		return this.avgEstdEffort;
	}

	public void setAvgEstdEffort(Integer avgEstdEffort) {
		this.avgEstdEffort = avgEstdEffort;
	}

	@Column(name = "StepType")
	public Serializable getStepType() {
		return this.stepType;
	}

	public void setStepType(Serializable stepType) {
		this.stepType = stepType;
	}

	@Column(name = "Size_Width")
	public Integer getSizeWidth() {
		return this.sizeWidth;
	}

	public void setSizeWidth(Integer sizeWidth) {
		this.sizeWidth = sizeWidth;
	}

	@Column(name = "Size_Height")
	public Integer getSizeHeight() {
		return this.sizeHeight;
	}

	public void setSizeHeight(Integer sizeHeight) {
		this.sizeHeight = sizeHeight;
	}

	@Column(name = "Position_X")
	public Integer getPositionX() {
		return this.positionX;
	}

	public void setPositionX(Integer positionX) {
		this.positionX = positionX;
	}

	@Column(name = "Position_Y")
	public Integer getPositionY() {
		return this.positionY;
	}

	public void setPositionY(Integer positionY) {
		this.positionY = positionY;
	}

	@Column(name = "Angle")
	public Integer getAngle() {
		return this.angle;
	}

	public void setAngle(Integer angle) {
		this.angle = angle;
	}

	@Column(name = "ClientStepID")
	public Integer getClientStepId() {
		return this.clientStepId;
	}

	public void setClientStepId(Integer clientStepId) {
		this.clientStepId = clientStepId;
	}

	@Column(name = "Z")
	public Integer getZ() {
		return this.z;
	}

	public void setZ(Integer z) {
		this.z = z;
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

	@Column(name = "action")
	public Serializable getAction() {
		return this.action;
	}

	public void setAction(Serializable action) {
		this.action = action;
	}

}