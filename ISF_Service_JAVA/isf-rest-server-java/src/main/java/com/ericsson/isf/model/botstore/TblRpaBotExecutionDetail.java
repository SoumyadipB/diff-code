package com.ericsson.isf.model.botstore;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TBL_RPA_BOTExecutionDetail", schema = "transactionalData",catalog = "ISFPROD_BLOB")
public class TblRpaBotExecutionDetail 
{

	private int srno;
	private int botId;
	private String signum;
	private int woNo;
	private int projectId;
	private int taskId;
	private String nodes;
	private String botPlateform;
	private String isEcnConnected;
	private String inputFileName;
	private String outputFileName;
	private byte[] inputDataFile;
	private byte[] outputDataFile;
	private String createdBy;
	private Date createdOn;
	
	public TblRpaBotExecutionDetail() {
	}

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "SRNO", unique = true, nullable = false)
	public int getSrno() {
		return srno;
	}

	public void setSrno(int srno) {
		this.srno = srno;
	}

	@Column(name = "BOTID",nullable = false)
	public int getBotId() {
		return botId;
	}

	public void setBotId(int botId) {
		this.botId = botId;
	}

	@Column(name = "Signum", nullable = false)
	public String getSignum() {
		return signum;
	}

	public void setSignum(String signum) {
		this.signum = signum;
	}

	@Column(name = "WONo", nullable = false)
	public int getWoNo() {
		return woNo;
	}

	public void setWoNo(int woNo) {
		this.woNo = woNo;
	}

	@Column(name = "ProjectID", nullable = false)
	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	@Column(name = "TaskID", nullable = false)
	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	@Column(name = "Nodes", nullable = false)
	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	@Column(name = "BOTPlateform", nullable = false)
	public String getBotPlateform() {
		return botPlateform;
	}

	public void setBotPlateform(String botPlateform) {
		this.botPlateform = botPlateform;
	}

	@Column(name = "IsECNConnected", nullable = false)
	public String getIsEcnConnected() {
		return isEcnConnected;
	}

	public void setIsEcnConnected(String isEcnConnected) {
		this.isEcnConnected = isEcnConnected;
	}
	

	@Column(name = "InputFileName", nullable = true)
	public String getInputFileName() {
		return inputFileName;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	@Column(name = "OutputFileName", nullable = true)
	public String getOutputFileName() {
		return outputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	@Column(name = "InputDataFile", nullable = true)
	public byte[] getInputDataFile() {
		return inputDataFile;
	}

	public void setInputDataFile(byte[] inputDataFile) {
		this.inputDataFile = inputDataFile;
	}

	@Column(name = "OutputDataFile", nullable = true)
	public byte[] getOutputDataFile() {
		return outputDataFile;
	}

	public void setOutputDataFile(byte[] outputDataFile) {
		this.outputDataFile = outputDataFile;
	}

	@Column(name = "CreatedBY", nullable = false, length = 10)
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedON", nullable = false, length = 23)
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	
}
