package com.ericsson.isf.model.botstore;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TBL_RPA_BOTFileNew", schema = "transactionalData")
public class TblRpaBotFileNew 
{

	private int srno;
	private int rpaRequestId;
	private String fileName;
	private String fileType;
	private byte[] dataFile;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	private int isActive;
	
	public TblRpaBotFileNew() {
	}
	public TblRpaBotFileNew(int srno, int rpaRequestId,String fileName,String fileType,byte[] dataFile, String createdBy,
			Date createdOn,String modifiedBy,Date modifiedOn,int isActive) {
		this.srno = srno;
		this.rpaRequestId=rpaRequestId;
		this.fileName=fileName;
		this.fileType=fileType;
		this.dataFile=dataFile;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.modifiedBy=modifiedBy;
		this.modifiedOn=modifiedOn;
		this.isActive = isActive;
			}

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "SRNO", unique = true, nullable = false)
	public int getSrno() {
		return this.srno;
	}

	public void setSrno(int srno) {
		this.srno = srno;
	}
	
	@Column(name = "rpaRequestID",nullable = false)
	public int getRpaRequestId() {
		return rpaRequestId;
	}
	public void setRpaRequestId(int rpaRequestId) {
		this.rpaRequestId = rpaRequestId;
	}
	
	@Column(name = "FileName", nullable = false)
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name = "FileType", nullable = false)
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	@Column(name = "DataFile", nullable = false)
	public byte[] getDataFile() {
		return dataFile;
	}
	public void setDataFile(byte[] dataFile) {
		this.dataFile = dataFile;
	}
	
	@Column(name = "CreatedBY", nullable = false, length = 10)
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedOn", nullable = false, length = 23)
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	@Column(name = "ModifiedBy", length = 10)
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ModifiedOn", length = 23)
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	@Column(name = "isActive", nullable = false)
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
	
	
}
