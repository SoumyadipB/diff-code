package com.ericsson.isf.model;

public class WorkInstructionModel {
	private int wIID;
	private String workInstructionName;
	private int domainID;
	private int vendorID;
	private int technologyID;
	private String sWrelease;
	private String kPIName;
	private int revNumber;
	private String flowchartOwner;
	private String description;
	private boolean active;
	private String createdBy;
	private String createdON;
	private String modifiedBy;
	private String modifiedON;
	private String hyperlink;
	private String domain;
	private String vendor;
	private String technology;
	private String fileName;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public byte[] getDataFile() {
		return dataFile;
	}
	public void setDataFile(byte[] dataFile) {
		this.dataFile = dataFile;
	}
	private String fileType;
	private byte[] dataFile;
	public int getwIID() {
		return wIID;
	}
	public void setwIID(int wIID) {
		this.wIID = wIID;
	}
	public String getWorkInstructionName() {
		return workInstructionName;
	}
	public void setWorkInstructionName(String workInstructionName) {
		this.workInstructionName = workInstructionName;
	}
	public int getDomainID() {
		return domainID;
	}
	public void setDomainID(int domainID) {
		this.domainID = domainID;
	}
	public int getVendorID() {
		return vendorID;
	}
	public void setVendorID(int vendorID) {
		this.vendorID = vendorID;
	}
	public int getTechnologyID() {
		return technologyID;
	}
	public void setTechnologyID(int technologyID) {
		this.technologyID = technologyID;
	}
	public String getsWrelease() {
		return sWrelease;
	}
	public void setsWrelease(String sWrelease) {
		this.sWrelease = sWrelease;
	}
	public String getkPIName() {
		return kPIName;
	}
	public void setkPIName(String kPIName) {
		this.kPIName = kPIName;
	}
	public int getRevNumber() {
		return revNumber;
	}
	public void setRevNumber(int revNumber) {
		this.revNumber = revNumber;
	}
	public String getFlowchartOwner() {
		return flowchartOwner;
	}
	public void setFlowchartOwner(String flowchartOwner) {
		this.flowchartOwner = flowchartOwner;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedON() {
		return createdON;
	}
	public void setCreatedON(String createdON) {
		this.createdON = createdON;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedON() {
		return modifiedON;
	}
	public void setModifiedON(String modifiedON) {
		this.modifiedON = modifiedON;
	}
	public String getHyperlink() {
		return hyperlink;
	}
	public void setHyperlink(String hyperlink) {
		this.hyperlink = hyperlink;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	
	
	

}
