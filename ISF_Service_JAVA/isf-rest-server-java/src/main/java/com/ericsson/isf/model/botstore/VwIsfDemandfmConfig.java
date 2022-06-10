package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * VwIsfDemandfmConfig generated by hbm2java
 */
@Entity
@Table(name = "VW_ISF_DEMANDFM_CONFIG", schema = "dbo")
public class VwIsfDemandfmConfig implements java.io.Serializable {

	private VwIsfDemandfmConfigId id;

	public VwIsfDemandfmConfig() {
	}

	public VwIsfDemandfmConfig(VwIsfDemandfmConfigId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "marketAreaName", column = @Column(name = "MarketAreaName")),
			@AttributeOverride(name = "description", column = @Column(name = "Description")),
			@AttributeOverride(name = "serviceArea", column = @Column(name = "ServiceArea", length = 128)),
			@AttributeOverride(name = "domain", column = @Column(name = "Domain", length = 128)),
			@AttributeOverride(name = "subDomain", column = @Column(name = "SubDomain", length = 128)),
			@AttributeOverride(name = "domainCcm", column = @Column(name = "DomainCCM", length = 128)),
			@AttributeOverride(name = "subDomainCcm", column = @Column(name = "SubDomainCCM", length = 128)),
			@AttributeOverride(name = "fulfilmentSpoc", column = @Column(name = "Fulfilment_Spoc", length = 250)),
			@AttributeOverride(name = "access", column = @Column(name = "Access")),
			@AttributeOverride(name = "employeeName", column = @Column(name = "EmployeeName", length = 500)),
			@AttributeOverride(name = "contactNumber", column = @Column(name = "ContactNumber", length = 13)),
			@AttributeOverride(name = "officeBuilding", column = @Column(name = "OfficeBuilding", length = 100)),
			@AttributeOverride(name = "hrlocation", column = @Column(name = "HRLocation", length = 100)),
			@AttributeOverride(name = "countryName", column = @Column(name = "CountryName", length = 100)),
			@AttributeOverride(name = "city", column = @Column(name = "City", length = 100)),
			@AttributeOverride(name = "companyName", column = @Column(name = "CompanyName", length = 150)),
			@AttributeOverride(name = "jobName", column = @Column(name = "JobName", length = 50)),
			@AttributeOverride(name = "jobRoleFamily", column = @Column(name = "JobRoleFamily", length = 50)),
			@AttributeOverride(name = "jobStage", column = @Column(name = "JobStage", length = 50)),
			@AttributeOverride(name = "functionalArea", column = @Column(name = "FunctionalArea", length = 100)),
			@AttributeOverride(name = "industryVertical", column = @Column(name = "IndustryVertical", length = 100)),
			@AttributeOverride(name = "empserviceArea", column = @Column(name = "EMPServiceArea", length = 100)),
			@AttributeOverride(name = "empDomain", column = @Column(name = "EmpDomain", length = 100)),
			@AttributeOverride(name = "empSubDomain", column = @Column(name = "EmpSubDomain", length = 100)),
			@AttributeOverride(name = "employeeEmailId", column = @Column(name = "EmployeeEmailID", length = 100)),
			@AttributeOverride(name = "isLineManager", column = @Column(name = "IsLineManager", length = 10)),
			@AttributeOverride(name = "managerSignum", column = @Column(name = "ManagerSignum", length = 8)),
			@AttributeOverride(name = "parentUnit", column = @Column(name = "ParentUnit", length = 150)),
			@AttributeOverride(name = "unit", column = @Column(name = "Unit", length = 100)),
			@AttributeOverride(name = "personnelNumber", column = @Column(name = "PersonnelNumber")) })
	public VwIsfDemandfmConfigId getId() {
		return this.id;
	}

	public void setId(VwIsfDemandfmConfigId id) {
		this.id = id;
	}

}
