package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

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
 * TblTaskTool generated by hbm2java
 */
@Entity
@Table(name = "TBL_TASK_TOOL", schema = "refData")
public class TblTaskTool implements java.io.Serializable {

	private int taskToolId;
	private TblTask tblTask;
	private TblToolInventory tblToolInventory;
	private Boolean active;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;

	public TblTaskTool() {
	}

	public TblTaskTool(int taskToolId) {
		this.taskToolId = taskToolId;
	}

	public TblTaskTool(int taskToolId, TblTask tblTask, TblToolInventory tblToolInventory, Boolean active,
			String createdBy, Date createdDate, String lastModifiedBy, Date lastModifiedDate) {
		this.taskToolId = taskToolId;
		this.tblTask = tblTask;
		this.tblToolInventory = tblToolInventory;
		this.active = active;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

	@Id

	@Column(name = "TaskToolID", unique = true, nullable = false)
	public int getTaskToolId() {
		return this.taskToolId;
	}

	public void setTaskToolId(int taskToolId) {
		this.taskToolId = taskToolId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TaskID")
	public TblTask getTblTask() {
		return this.tblTask;
	}

	public void setTblTask(TblTask tblTask) {
		this.tblTask = tblTask;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ToolID")
	public TblToolInventory getTblToolInventory() {
		return this.tblToolInventory;
	}

	public void setTblToolInventory(TblToolInventory tblToolInventory) {
		this.tblToolInventory = tblToolInventory;
	}

	@Column(name = "Active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "CreatedBy", length = 128)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedDate", length = 23)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "LastModifiedBy", length = 128)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LastModifiedDate", length = 23)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}