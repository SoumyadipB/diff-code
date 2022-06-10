package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblTaskRpaMappingId generated by hbm2java
 */
@Embeddable
public class TblTaskRpaMappingId implements java.io.Serializable {

	private int taskRpaId;
	private Integer taskId;
	private Integer rpaid;
	private Boolean active;

	public TblTaskRpaMappingId() {
	}

	public TblTaskRpaMappingId(int taskRpaId) {
		this.taskRpaId = taskRpaId;
	}

	public TblTaskRpaMappingId(int taskRpaId, Integer taskId, Integer rpaid, Boolean active) {
		this.taskRpaId = taskRpaId;
		this.taskId = taskId;
		this.rpaid = rpaid;
		this.active = active;
	}

	@Column(name = "TaskRpaID", nullable = false)
	public int getTaskRpaId() {
		return this.taskRpaId;
	}

	public void setTaskRpaId(int taskRpaId) {
		this.taskRpaId = taskRpaId;
	}

	@Column(name = "TaskID")
	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	@Column(name = "RPAID")
	public Integer getRpaid() {
		return this.rpaid;
	}

	public void setRpaid(Integer rpaid) {
		this.rpaid = rpaid;
	}

	@Column(name = "Active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblTaskRpaMappingId))
			return false;
		TblTaskRpaMappingId castOther = (TblTaskRpaMappingId) other;

		return (this.getTaskRpaId() == castOther.getTaskRpaId())
				&& ((this.getTaskId() == castOther.getTaskId()) || (this.getTaskId() != null
						&& castOther.getTaskId() != null && this.getTaskId().equals(castOther.getTaskId())))
				&& ((this.getRpaid() == castOther.getRpaid()) || (this.getRpaid() != null
						&& castOther.getRpaid() != null && this.getRpaid().equals(castOther.getRpaid())))
				&& ((this.getActive() == castOther.getActive()) || (this.getActive() != null
						&& castOther.getActive() != null && this.getActive().equals(castOther.getActive())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getTaskRpaId();
		result = 37 * result + (getTaskId() == null ? 0 : this.getTaskId().hashCode());
		result = 37 * result + (getRpaid() == null ? 0 : this.getRpaid().hashCode());
		result = 37 * result + (getActive() == null ? 0 : this.getActive().hashCode());
		return result;
	}

}
