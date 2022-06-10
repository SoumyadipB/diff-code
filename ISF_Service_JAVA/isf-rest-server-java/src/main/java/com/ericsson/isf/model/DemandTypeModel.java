package com.ericsson.isf.model;

public class DemandTypeModel {
	String demandType;
	String lastModifyBy;
	boolean inactivateDemandType;
	String demandTypeDescription;
	int demandTypeId;
	String createdBy;
	String createdOn;
	
	public String getLastModifyOn() {
		return lastModifyOn;
	}

	public void setLastModifyOn(String lastModifyOn) {
		this.lastModifyOn = lastModifyOn;
	}

	String lastModifyOn;

	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public int getDemandTypeId() {
		return demandTypeId;
	}

	public void setDemandTypeId(int demandTypeId) {
		this.demandTypeId = demandTypeId;
	}

	public String getDemandType() {
		return demandType;
	}

	public void setDemandType(String demandType) {
		this.demandType = demandType;
	}

	
    public String getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(String lastModifiyBy) {
		this.lastModifyBy = lastModifiyBy;
	}

	public boolean getInactivateDemandType() {
		return inactivateDemandType;
	}

	public void setInactivateDemandType(boolean inactivateDemandType) {
		this.inactivateDemandType = inactivateDemandType;
	}
		
     public String getDemandTypeDescription() {
		return demandTypeDescription;
	}

	public void setDemandTypeDescription(String demandTypeDescription) {
		this.demandTypeDescription = demandTypeDescription;
	}
	
	

}
