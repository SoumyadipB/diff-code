package com.ericsson.isf.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EmployeeModel {
	
	//private int employeeId;
	//EmployeeName	Signum	PersonnelNumber	Gender	PositionId	CostCenter	Unit	ManagerSignum	IsLineManager	EmployeeGroup	Status	CompanyName	FSO	CountryName	ParentUnit	JobName	JobStage	Domain	SubDomain	FunctionalArea	ServiceArea	IndustryVertical	EmployeeEmailID	ContactNumber	JobRoleFamily	City	HRLocation	OfficeBuilding	Floor	CreatedBy	CreatedOn	LastModifiedBy	LastModifiedOn

	
	private String signum;
	private String employeeName;
	private String employeeEmailId;
	private String personnelNumber;
	//private String employeeNumber;
	private String ContactNumber;
	private String gender;
	//private String careerStartDate;
	//private String dateOfJoining;
	private String status;
	private String managerSignum;
	private String managerName;
	//private String team;
	private String costCenter;
	private String functionalArea;
	private String serviceArea;
	private String grade;
	private String JobRoleFamily;
	private String JobRole;
	private String JobStage;
	private String city;
	private String hrLocation;
	private String officeBuilding;
	private String floor;
	//private String seatNumber;
	private String createdBy;
	private String createdOn;
	private String lastModifiedBy;
	private String lastModifiedOn;
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
	public String getPersonnelNumber() {
		return personnelNumber;
	}
	public void setPersonnelNumber(String personnelNumber) {
		this.personnelNumber = personnelNumber;
	}
	public String getContactNumber() {
		return ContactNumber;
	}
	public void setContactNumber(String contactNumber) {
		ContactNumber = contactNumber;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getCostCenter() {
		return costCenter;
	}
	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}
	public String getFunctionalArea() {
		return functionalArea;
	}
	public void setFunctionalArea(String functionalArea) {
		this.functionalArea = functionalArea;
	}
	public String getServiceArea() {
		return serviceArea;
	}
	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getJobRoleFamily() {
		return JobRoleFamily;
	}
	public void setJobRoleFamily(String jobRoleFamily) {
		JobRoleFamily = jobRoleFamily;
	}
	public String getJobRole() {
		return JobRole;
	}
	public void setJobRole(String jobRole) {
		JobRole = jobRole;
	}
	public String getJobStage() {
		return JobStage;
	}
	public void setJobStage(String jobStage) {
		JobStage = jobStage;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getHrLocation() {
		return hrLocation;
	}
	public void setHrLocation(String hrLocation) {
		this.hrLocation = hrLocation;
	}
	public String getOfficeBuilding() {
		return officeBuilding;
	}
	public void setOfficeBuilding(String officeBuilding) {
		this.officeBuilding = officeBuilding;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
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
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getLastModifiedOn() {
		return lastModifiedOn;
	}
	public void setLastModifiedOn(String lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}
	public int getPositionId() {
		return positionId;
	}
	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}
	public String getOrgUnit() {
		return OrgUnit;
	}
	public void setOrgUnit(String orgUnit) {
		OrgUnit = orgUnit;
	}
	public String getIsLineManager() {
		return isLineManager;
	}
	public void setIsLineManager(String isLineManager) {
		this.isLineManager = isLineManager;
	}
	public String getEmployeeGroup() {
		return employeeGroup;
	}
	public void setEmployeeGroup(String employeeGroup) {
		this.employeeGroup = employeeGroup;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getFso() {
		return fso;
	}
	public void setFso(String fso) {
		this.fso = fso;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getParentUnit() {
		return parentUnit;
	}
	public void setParentUnit(String parentUnit) {
		this.parentUnit = parentUnit;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getSubDomain() {
		return subDomain;
	}
	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}
	public String getIndustryVertical() {
		return industryVertical;
	}
	public void setIndustryVertical(String industryVertical) {
		this.industryVertical = industryVertical;
	}
	private int positionId;
	private String OrgUnit;
	private String isLineManager;
	private String employeeGroup;
	private String companyName;
	private String fso;
	private String country;
	private String parentUnit;
	private String jobName;
	private String domain;
	private String subDomain;
	private String industryVertical;
	

}
