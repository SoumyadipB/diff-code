package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TblProjectscopedetail generated by hbm2java
 */
@Entity
@Table(name = "TBL_PROJECTSCOPEDETAIL", schema = "transactionalData")
public class TblProjectscopedetail implements java.io.Serializable {

	private int projectScopeDetailId;
	private TblProjectscope tblProjectscope;
	private int domainId;
	private int serviceAreaId;
	private int technologyId;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Boolean active;
	private Set<TblActivityscope> tblActivityscopes = new HashSet<TblActivityscope>(0);
	private Set<TblResourceRequests> tblResourceRequestses = new HashSet<TblResourceRequests>(0);

	public TblProjectscopedetail() {
	}

	public TblProjectscopedetail(int projectScopeDetailId, int domainId, int serviceAreaId, int technologyId) {
		this.projectScopeDetailId = projectScopeDetailId;
		this.domainId = domainId;
		this.serviceAreaId = serviceAreaId;
		this.technologyId = technologyId;
	}

	public TblProjectscopedetail(int projectScopeDetailId, TblProjectscope tblProjectscope, int domainId,
			int serviceAreaId, int technologyId, String createdBy, Date createdDate, String lastModifiedBy,
			Date lastModifiedDate, Boolean active, Set<TblActivityscope> tblActivityscopes,
			Set<TblResourceRequests> tblResourceRequestses) {
		this.projectScopeDetailId = projectScopeDetailId;
		this.tblProjectscope = tblProjectscope;
		this.domainId = domainId;
		this.serviceAreaId = serviceAreaId;
		this.technologyId = technologyId;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.active = active;
		this.tblActivityscopes = tblActivityscopes;
		this.tblResourceRequestses = tblResourceRequestses;
	}

	@Id

	@Column(name = "ProjectScopeDetailID", unique = true, nullable = false)
	public int getProjectScopeDetailId() {
		return this.projectScopeDetailId;
	}

	public void setProjectScopeDetailId(int projectScopeDetailId) {
		this.projectScopeDetailId = projectScopeDetailId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ProjectScopeID")
	public TblProjectscope getTblProjectscope() {
		return this.tblProjectscope;
	}

	public void setTblProjectscope(TblProjectscope tblProjectscope) {
		this.tblProjectscope = tblProjectscope;
	}

	@Column(name = "DomainID", nullable = false)
	public int getDomainId() {
		return this.domainId;
	}

	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}

	@Column(name = "ServiceAreaID", nullable = false)
	public int getServiceAreaId() {
		return this.serviceAreaId;
	}

	public void setServiceAreaId(int serviceAreaId) {
		this.serviceAreaId = serviceAreaId;
	}

	@Column(name = "TechnologyID", nullable = false)
	public int getTechnologyId() {
		return this.technologyId;
	}

	public void setTechnologyId(int technologyId) {
		this.technologyId = technologyId;
	}

	@Column(name = "CreatedBy", length = 256)
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

	@Column(name = "LastModifiedBy", length = 256)
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

	@Column(name = "Active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblProjectscopedetail")
	public Set<TblActivityscope> getTblActivityscopes() {
		return this.tblActivityscopes;
	}

	public void setTblActivityscopes(Set<TblActivityscope> tblActivityscopes) {
		this.tblActivityscopes = tblActivityscopes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblProjectscopedetail")
	public Set<TblResourceRequests> getTblResourceRequestses() {
		return this.tblResourceRequestses;
	}

	public void setTblResourceRequestses(Set<TblResourceRequests> tblResourceRequestses) {
		this.tblResourceRequestses = tblResourceRequestses;
	}

}
