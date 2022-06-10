package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MerlinId generated by hbm2java
 */
@Embeddable
public class MerlinId implements java.io.Serializable {

	private Serializable employeeGroup;
	private Serializable resourceName;
	private Serializable signum;
	private Serializable posNo;
	private Serializable fso;
	private Serializable perNo;
	private Serializable jobName;
	private Serializable jobStage;
	private Serializable unit;
	private Serializable parentUnit;
	private Serializable domain;
	private Serializable subDomain;
	private Serializable countryName;
	private Serializable companyName;
	private Serializable functionalArea;
	private Serializable serviceArea;
	private Serializable industryVertical;
	private Serializable costCentre;
	private Serializable lineManager;
	private Serializable profileType;
	private Serializable cvStatus;
	private Serializable assessmentStatus;

	private Serializable certificationFlagYesNo;

	private Serializable etcpCertificateCount;
	private Serializable giapCertificateCount;
	private Serializable nonEricssonCertificateCount;
	private Serializable competenceName;
	private Serializable competenceLevel;
	private Serializable lastAssessedDate;
	private Serializable f30;
	private Serializable f31;

	public MerlinId() {
	}

	public MerlinId(Serializable employeeGroup, Serializable resourceName, Serializable signum, Serializable posNo, Serializable fso, Serializable perNo, Serializable jobName, Serializable jobStage, Serializable unit, Serializable parentUnit, Serializable domain, Serializable subDomain, Serializable countryName, Serializable companyName, Serializable functionalArea, Serializable serviceArea, Serializable industryVertical, Serializable costCentre, Serializable lineManager, Serializable profileType, Serializable cvStatus, Serializable assessmentStatus,

	Serializable certificationFlagYesNo, Serializable etcpCertificateCount, Serializable giapCertificateCount, Serializable nonEricssonCertificateCount, Serializable competenceName, Serializable competenceLevel, Serializable lastAssessedDate, Serializable f30, Serializable f31) {
       this.employeeGroup = employeeGroup;
       this.resourceName = resourceName;
       this.signum = signum;
       this.posNo = posNo;
       this.fso = fso;
       this.perNo = perNo;
       this.jobName = jobName;
       this.jobStage = jobStage;
       this.unit = unit;
       this.parentUnit = parentUnit;
       this.domain = domain;
       this.subDomain = subDomain;
       this.countryName = countryName;
       this.companyName = companyName;
       this.functionalArea = functionalArea;
       this.serviceArea = serviceArea;
       this.industryVertical = industryVertical;
       this.costCentre = costCentre;
       this.lineManager = lineManager;
       this.profileType = profileType;
       this.cvStatus = cvStatus;
       this.assessmentStatus = assessmentStatus;
       this.certificationFlagYesNo = certificationFlagYesNo;
       this.etcpCertificateCount = etcpCertificateCount;
       this.giapCertificateCount = giapCertificateCount;
       this.nonEricssonCertificateCount = nonEricssonCertificateCount;
       this.competenceName = competenceName;
       this.competenceLevel = competenceLevel;
       this.lastAssessedDate = lastAssessedDate;
       this.f30 = f30;
       this.f31 = f31;
    }

	@Column(name = "Employee Group")
	public Serializable getEmployeeGroup() {
		return this.employeeGroup;
	}

	public void setEmployeeGroup(Serializable employeeGroup) {
		this.employeeGroup = employeeGroup;
	}

	@Column(name = "Resource Name")
	public Serializable getResourceName() {
		return this.resourceName;
	}

	public void setResourceName(Serializable resourceName) {
		this.resourceName = resourceName;
	}

	@Column(name = "Signum")
	public Serializable getSignum() {
		return this.signum;
	}

	public void setSignum(Serializable signum) {
		this.signum = signum;
	}

	@Column(name = "PosNo")
	public Serializable getPosNo() {
		return this.posNo;
	}

	public void setPosNo(Serializable posNo) {
		this.posNo = posNo;
	}

	@Column(name = "FSO")
	public Serializable getFso() {
		return this.fso;
	}

	public void setFso(Serializable fso) {
		this.fso = fso;
	}

	@Column(name = "Per_No")
	public Serializable getPerNo() {
		return this.perNo;
	}

	public void setPerNo(Serializable perNo) {
		this.perNo = perNo;
	}

	@Column(name = "Job Name")
	public Serializable getJobName() {
		return this.jobName;
	}

	public void setJobName(Serializable jobName) {
		this.jobName = jobName;
	}

	@Column(name = "Job Stage")
	public Serializable getJobStage() {
		return this.jobStage;
	}

	public void setJobStage(Serializable jobStage) {
		this.jobStage = jobStage;
	}

	@Column(name = "Unit")
	public Serializable getUnit() {
		return this.unit;
	}

	public void setUnit(Serializable unit) {
		this.unit = unit;
	}

	@Column(name = "ParentUnit")
	public Serializable getParentUnit() {
		return this.parentUnit;
	}

	public void setParentUnit(Serializable parentUnit) {
		this.parentUnit = parentUnit;
	}

	@Column(name = "Domain")
	public Serializable getDomain() {
		return this.domain;
	}

	public void setDomain(Serializable domain) {
		this.domain = domain;
	}

	@Column(name = "Sub Domain")
	public Serializable getSubDomain() {
		return this.subDomain;
	}

	public void setSubDomain(Serializable subDomain) {
		this.subDomain = subDomain;
	}

	@Column(name = "Country Name")
	public Serializable getCountryName() {
		return this.countryName;
	}

	public void setCountryName(Serializable countryName) {
		this.countryName = countryName;
	}

	@Column(name = "Company Name")
	public Serializable getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(Serializable companyName) {
		this.companyName = companyName;
	}

	@Column(name = "Functional Area")
	public Serializable getFunctionalArea() {
		return this.functionalArea;
	}

	public void setFunctionalArea(Serializable functionalArea) {
		this.functionalArea = functionalArea;
	}

	@Column(name = "Service Area")
	public Serializable getServiceArea() {
		return this.serviceArea;
	}

	public void setServiceArea(Serializable serviceArea) {
		this.serviceArea = serviceArea;
	}

	@Column(name = "Industry Vertical")
	public Serializable getIndustryVertical() {
		return this.industryVertical;
	}

	public void setIndustryVertical(Serializable industryVertical) {
		this.industryVertical = industryVertical;
	}

	@Column(name = "Cost Centre")
	public Serializable getCostCentre() {
		return this.costCentre;
	}

	public void setCostCentre(Serializable costCentre) {
		this.costCentre = costCentre;
	}

	@Column(name = "Line Manager")
	public Serializable getLineManager() {
		return this.lineManager;
	}

	public void setLineManager(Serializable lineManager) {
		this.lineManager = lineManager;
	}

	@Column(name = "Profile Type")
	public Serializable getProfileType() {
		return this.profileType;
	}

	public void setProfileType(Serializable profileType) {
		this.profileType = profileType;
	}

	@Column(name = "CV Status")
	public Serializable getCvStatus() {
		return this.cvStatus;
	}

	public void setCvStatus(Serializable cvStatus) {
		this.cvStatus = cvStatus;
	}

	@Column(name = "Assessment Status")
	public Serializable getAssessmentStatus() {
		return this.assessmentStatus;
	}

	public void setAssessmentStatus(Serializable assessmentStatus) {
		this.assessmentStatus = assessmentStatus;
	}

	@Column(name="Certification Flag (Yes/No)")
    public Serializable getCertificationFlagYesNo() {
        return this.certificationFlagYesNo;
    }

	public void setCertificationFlagYesNo(

	Serializable certificationFlagYesNo) {
        this.certificationFlagYesNo = certificationFlagYesNo;
    }

	@Column(name = "ETCP Certificate Count")
	public Serializable getEtcpCertificateCount() {
		return this.etcpCertificateCount;
	}

	public void setEtcpCertificateCount(Serializable etcpCertificateCount) {
		this.etcpCertificateCount = etcpCertificateCount;
	}

	@Column(name = "GIAP Certificate Count")
	public Serializable getGiapCertificateCount() {
		return this.giapCertificateCount;
	}

	public void setGiapCertificateCount(Serializable giapCertificateCount) {
		this.giapCertificateCount = giapCertificateCount;
	}

	@Column(name = "Non Ericsson Certificate Count")
	public Serializable getNonEricssonCertificateCount() {
		return this.nonEricssonCertificateCount;
	}

	public void setNonEricssonCertificateCount(Serializable nonEricssonCertificateCount) {
		this.nonEricssonCertificateCount = nonEricssonCertificateCount;
	}

	@Column(name = "Competence Name")
	public Serializable getCompetenceName() {
		return this.competenceName;
	}

	public void setCompetenceName(Serializable competenceName) {
		this.competenceName = competenceName;
	}

	@Column(name = "Competence Level")
	public Serializable getCompetenceLevel() {
		return this.competenceLevel;
	}

	public void setCompetenceLevel(Serializable competenceLevel) {
		this.competenceLevel = competenceLevel;
	}

	@Column(name = "Last Assessed Date")
	public Serializable getLastAssessedDate() {
		return this.lastAssessedDate;
	}

	public void setLastAssessedDate(Serializable lastAssessedDate) {
		this.lastAssessedDate = lastAssessedDate;
	}

	@Column(name = "F30")
	public Serializable getF30() {
		return this.f30;
	}

	public void setF30(Serializable f30) {
		this.f30 = f30;
	}

	@Column(name = "F31")
	public Serializable getF31() {
		return this.f31;
	}

	public void setF31(Serializable f31) {
		this.f31 = f31;
	}

	public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof MerlinId) ) return false;
		 MerlinId castOther = ( MerlinId ) other; 
         
		 return ( (this.getEmployeeGroup()==castOther.getEmployeeGroup()) || ( this.getEmployeeGroup()!=null && castOther.getEmployeeGroup()!=null && this.getEmployeeGroup().equals(castOther.getEmployeeGroup()) ) )
 && ( (this.getResourceName()==castOther.getResourceName()) || ( this.getResourceName()!=null && castOther.getResourceName()!=null && this.getResourceName().equals(castOther.getResourceName()) ) )
 && ( (this.getSignum()==castOther.getSignum()) || ( this.getSignum()!=null && castOther.getSignum()!=null && this.getSignum().equals(castOther.getSignum()) ) )
 && ( (this.getPosNo()==castOther.getPosNo()) || ( this.getPosNo()!=null && castOther.getPosNo()!=null && this.getPosNo().equals(castOther.getPosNo()) ) )
 && ( (this.getFso()==castOther.getFso()) || ( this.getFso()!=null && castOther.getFso()!=null && this.getFso().equals(castOther.getFso()) ) )
 && ( (this.getPerNo()==castOther.getPerNo()) || ( this.getPerNo()!=null && castOther.getPerNo()!=null && this.getPerNo().equals(castOther.getPerNo()) ) )
 && ( (this.getJobName()==castOther.getJobName()) || ( this.getJobName()!=null && castOther.getJobName()!=null && this.getJobName().equals(castOther.getJobName()) ) )
 && ( (this.getJobStage()==castOther.getJobStage()) || ( this.getJobStage()!=null && castOther.getJobStage()!=null && this.getJobStage().equals(castOther.getJobStage()) ) )
 && ( (this.getUnit()==castOther.getUnit()) || ( this.getUnit()!=null && castOther.getUnit()!=null && this.getUnit().equals(castOther.getUnit()) ) )
 && ( (this.getParentUnit()==castOther.getParentUnit()) || ( this.getParentUnit()!=null && castOther.getParentUnit()!=null && this.getParentUnit().equals(castOther.getParentUnit()) ) )
 && ( (this.getDomain()==castOther.getDomain()) || ( this.getDomain()!=null && castOther.getDomain()!=null && this.getDomain().equals(castOther.getDomain()) ) )
 && ( (this.getSubDomain()==castOther.getSubDomain()) || ( this.getSubDomain()!=null && castOther.getSubDomain()!=null && this.getSubDomain().equals(castOther.getSubDomain()) ) )
 && ( (this.getCountryName()==castOther.getCountryName()) || ( this.getCountryName()!=null && castOther.getCountryName()!=null && this.getCountryName().equals(castOther.getCountryName()) ) )
 && ( (this.getCompanyName()==castOther.getCompanyName()) || ( this.getCompanyName()!=null && castOther.getCompanyName()!=null && this.getCompanyName().equals(castOther.getCompanyName()) ) )
 && ( (this.getFunctionalArea()==castOther.getFunctionalArea()) || ( this.getFunctionalArea()!=null && castOther.getFunctionalArea()!=null && this.getFunctionalArea().equals(castOther.getFunctionalArea()) ) )
 && ( (this.getServiceArea()==castOther.getServiceArea()) || ( this.getServiceArea()!=null && castOther.getServiceArea()!=null && this.getServiceArea().equals(castOther.getServiceArea()) ) )
 && ( (this.getIndustryVertical()==castOther.getIndustryVertical()) || ( this.getIndustryVertical()!=null && castOther.getIndustryVertical()!=null && this.getIndustryVertical().equals(castOther.getIndustryVertical()) ) )
 && ( (this.getCostCentre()==castOther.getCostCentre()) || ( this.getCostCentre()!=null && castOther.getCostCentre()!=null && this.getCostCentre().equals(castOther.getCostCentre()) ) )
 && ( (this.getLineManager()==castOther.getLineManager()) || ( this.getLineManager()!=null && castOther.getLineManager()!=null && this.getLineManager().equals(castOther.getLineManager()) ) )
 && ( (this.getProfileType()==castOther.getProfileType()) || ( this.getProfileType()!=null && castOther.getProfileType()!=null && this.getProfileType().equals(castOther.getProfileType()) ) )
 && ( (this.getCvStatus()==castOther.getCvStatus()) || ( this.getCvStatus()!=null && castOther.getCvStatus()!=null && this.getCvStatus().equals(castOther.getCvStatus()) ) )
 && ( (this.getAssessmentStatus()==castOther.getAssessmentStatus()) || ( this.getAssessmentStatus()!=null && castOther.getAssessmentStatus()!=null && this.getAssessmentStatus().equals(castOther.getAssessmentStatus()) ) )
 && ( (this.getCertificationFlagYesNo()==castOther.getCertificationFlagYesNo()) || ( this.getCertificationFlagYesNo()!=null && castOther.getCertificationFlagYesNo()!=null && this.getCertificationFlagYesNo().equals(castOther.getCertificationFlagYesNo()) ) )
 && ( (this.getEtcpCertificateCount()==castOther.getEtcpCertificateCount()) || ( this.getEtcpCertificateCount()!=null && castOther.getEtcpCertificateCount()!=null && this.getEtcpCertificateCount().equals(castOther.getEtcpCertificateCount()) ) )
 && ( (this.getGiapCertificateCount()==castOther.getGiapCertificateCount()) || ( this.getGiapCertificateCount()!=null && castOther.getGiapCertificateCount()!=null && this.getGiapCertificateCount().equals(castOther.getGiapCertificateCount()) ) )
 && ( (this.getNonEricssonCertificateCount()==castOther.getNonEricssonCertificateCount()) || ( this.getNonEricssonCertificateCount()!=null && castOther.getNonEricssonCertificateCount()!=null && this.getNonEricssonCertificateCount().equals(castOther.getNonEricssonCertificateCount()) ) )
 && ( (this.getCompetenceName()==castOther.getCompetenceName()) || ( this.getCompetenceName()!=null && castOther.getCompetenceName()!=null && this.getCompetenceName().equals(castOther.getCompetenceName()) ) )
 && ( (this.getCompetenceLevel()==castOther.getCompetenceLevel()) || ( this.getCompetenceLevel()!=null && castOther.getCompetenceLevel()!=null && this.getCompetenceLevel().equals(castOther.getCompetenceLevel()) ) )
 && ( (this.getLastAssessedDate()==castOther.getLastAssessedDate()) || ( this.getLastAssessedDate()!=null && castOther.getLastAssessedDate()!=null && this.getLastAssessedDate().equals(castOther.getLastAssessedDate()) ) )
 && ( (this.getF30()==castOther.getF30()) || ( this.getF30()!=null && castOther.getF30()!=null && this.getF30().equals(castOther.getF30()) ) )
 && ( (this.getF31()==castOther.getF31()) || ( this.getF31()!=null && castOther.getF31()!=null && this.getF31().equals(castOther.getF31()) ) );
   }

	public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getEmployeeGroup() == null ? 0 : this.getEmployeeGroup().hashCode() );
         result = 37 * result + ( getResourceName() == null ? 0 : this.getResourceName().hashCode() );
         result = 37 * result + ( getSignum() == null ? 0 : this.getSignum().hashCode() );
         result = 37 * result + ( getPosNo() == null ? 0 : this.getPosNo().hashCode() );
         result = 37 * result + ( getFso() == null ? 0 : this.getFso().hashCode() );
         result = 37 * result + ( getPerNo() == null ? 0 : this.getPerNo().hashCode() );
         result = 37 * result + ( getJobName() == null ? 0 : this.getJobName().hashCode() );
         result = 37 * result + ( getJobStage() == null ? 0 : this.getJobStage().hashCode() );
         result = 37 * result + ( getUnit() == null ? 0 : this.getUnit().hashCode() );
         result = 37 * result + ( getParentUnit() == null ? 0 : this.getParentUnit().hashCode() );
         result = 37 * result + ( getDomain() == null ? 0 : this.getDomain().hashCode() );
         result = 37 * result + ( getSubDomain() == null ? 0 : this.getSubDomain().hashCode() );
         result = 37 * result + ( getCountryName() == null ? 0 : this.getCountryName().hashCode() );
         result = 37 * result + ( getCompanyName() == null ? 0 : this.getCompanyName().hashCode() );
         result = 37 * result + ( getFunctionalArea() == null ? 0 : this.getFunctionalArea().hashCode() );
         result = 37 * result + ( getServiceArea() == null ? 0 : this.getServiceArea().hashCode() );
         result = 37 * result + ( getIndustryVertical() == null ? 0 : this.getIndustryVertical().hashCode() );
         result = 37 * result + ( getCostCentre() == null ? 0 : this.getCostCentre().hashCode() );
         result = 37 * result + ( getLineManager() == null ? 0 : this.getLineManager().hashCode() );
         result = 37 * result + ( getProfileType() == null ? 0 : this.getProfileType().hashCode() );
         result = 37 * result + ( getCvStatus() == null ? 0 : this.getCvStatus().hashCode() );
         result = 37 * result + ( getAssessmentStatus() == null ? 0 : this.getAssessmentStatus().hashCode() );
         result = 37 * result + ( getCertificationFlagYesNo() == null ? 0 : this.getCertificationFlagYesNo().hashCode() );
         result = 37 * result + ( getEtcpCertificateCount() == null ? 0 : this.getEtcpCertificateCount().hashCode() );
         result = 37 * result + ( getGiapCertificateCount() == null ? 0 : this.getGiapCertificateCount().hashCode() );
         result = 37 * result + ( getNonEricssonCertificateCount() == null ? 0 : this.getNonEricssonCertificateCount().hashCode() );
         result = 37 * result + ( getCompetenceName() == null ? 0 : this.getCompetenceName().hashCode() );
         result = 37 * result + ( getCompetenceLevel() == null ? 0 : this.getCompetenceLevel().hashCode() );
         result = 37 * result + ( getLastAssessedDate() == null ? 0 : this.getLastAssessedDate().hashCode() );
         result = 37 * result + ( getF30() == null ? 0 : this.getF30().hashCode() );
         result = 37 * result + ( getF31() == null ? 0 : this.getF31().hashCode() );
         return result;
   }

}