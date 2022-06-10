package com.ericsson.isf.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EmployeeBasicDetails {
	
	//private int employeeId;
	//EmployeeName	Signum	PersonnelNumber	Gender	PositionId	CostCenter	Unit	ManagerSignum	IsLineManager	EmployeeGroup	Status	CompanyName	FSO	CountryName	ParentUnit	JobName	JobStage	Domain	SubDomain	FunctionalArea	ServiceArea	IndustryVertical	EmployeeEmailID	ContactNumber	JobRoleFamily	City	HRLocation	OfficeBuilding	Floor	CreatedBy	CreatedOn	LastModifiedBy	LastModifiedOn

	
	private String signum;
	private String employeeName;
	private String employeeEmailId;
	private String managerSignum;
	private String managerName;
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeEmailId() {
		return employeeEmailId;
	}
	public void setEmployeeEmailId(String employeeEmailId) {
		this.employeeEmailId = employeeEmailId;
	}
	public String getManagerSignum() {
		return managerSignum;
	}
	public void setManagerSignum(String managerSignum) {
		this.managerSignum = managerSignum;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	
	

}
