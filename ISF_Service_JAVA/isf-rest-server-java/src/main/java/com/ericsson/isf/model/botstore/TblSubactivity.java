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
 * TblSubactivity generated by hbm2java
 */
@Entity
@Table(name = "TBL_SUBACTIVITY", schema = "refData")
public class TblSubactivity implements java.io.Serializable {

	private int subActivityId;
	private TblDomain tblDomain;
	private TblServicearea tblServicearea;
	private TblTechnology tblTechnology;
	private String activity;
	private String subActivity;
	private Integer avgEstdEffort;
	private Boolean active;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Set<TblTask> tblTasks = new HashSet<TblTask>(0);
	private Set<TblWorkFlowDefinition> tblWorkFlowDefinitions = new HashSet<TblWorkFlowDefinition>(0);
	private Set<TblWorkFlowUserProficiency> tblWorkFlowUserProficiencies = new HashSet<TblWorkFlowUserProficiency>(0);

	public TblSubactivity() {
	}

	public TblSubactivity(int subActivityId) {
		this.subActivityId = subActivityId;
	}

	public TblSubactivity(int subActivityId, TblDomain tblDomain, TblServicearea tblServicearea,
			TblTechnology tblTechnology, String activity, String subActivity, Integer avgEstdEffort, Boolean active,
			String createdBy, Date createdDate, String lastModifiedBy, Date lastModifiedDate, Set<TblTask> tblTasks,
			Set<TblWorkFlowDefinition> tblWorkFlowDefinitions,
			Set<TblWorkFlowUserProficiency> tblWorkFlowUserProficiencies) {
		this.subActivityId = subActivityId;
		this.tblDomain = tblDomain;
		this.tblServicearea = tblServicearea;
		this.tblTechnology = tblTechnology;
		this.activity = activity;
		this.subActivity = subActivity;
		this.avgEstdEffort = avgEstdEffort;
		this.active = active;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.tblTasks = tblTasks;
		this.tblWorkFlowDefinitions = tblWorkFlowDefinitions;
		this.tblWorkFlowUserProficiencies = tblWorkFlowUserProficiencies;
	}

	@Id

	@Column(name = "SubActivityID", unique = true, nullable = false)
	public int getSubActivityId() {
		return this.subActivityId;
	}

	public void setSubActivityId(int subActivityId) {
		this.subActivityId = subActivityId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DomainID")
	public TblDomain getTblDomain() {
		return this.tblDomain;
	}

	public void setTblDomain(TblDomain tblDomain) {
		this.tblDomain = tblDomain;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ServiceAreaID")
	public TblServicearea getTblServicearea() {
		return this.tblServicearea;
	}

	public void setTblServicearea(TblServicearea tblServicearea) {
		this.tblServicearea = tblServicearea;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TechnologyID")
	public TblTechnology getTblTechnology() {
		return this.tblTechnology;
	}

	public void setTblTechnology(TblTechnology tblTechnology) {
		this.tblTechnology = tblTechnology;
	}

	@Column(name = "Activity", length = 512)
	public String getActivity() {
		return this.activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	@Column(name = "SubActivity", length = 512)
	public String getSubActivity() {
		return this.subActivity;
	}

	public void setSubActivity(String subActivity) {
		this.subActivity = subActivity;
	}

	@Column(name = "AvgEstdEffort")
	public Integer getAvgEstdEffort() {
		return this.avgEstdEffort;
	}

	public void setAvgEstdEffort(Integer avgEstdEffort) {
		this.avgEstdEffort = avgEstdEffort;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblSubactivity")
	public Set<TblTask> getTblTasks() {
		return this.tblTasks;
	}

	public void setTblTasks(Set<TblTask> tblTasks) {
		this.tblTasks = tblTasks;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblSubactivity")
	public Set<TblWorkFlowDefinition> getTblWorkFlowDefinitions() {
		return this.tblWorkFlowDefinitions;
	}

	public void setTblWorkFlowDefinitions(Set<TblWorkFlowDefinition> tblWorkFlowDefinitions) {
		this.tblWorkFlowDefinitions = tblWorkFlowDefinitions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblSubactivity")
	public Set<TblWorkFlowUserProficiency> getTblWorkFlowUserProficiencies() {
		return this.tblWorkFlowUserProficiencies;
	}

	public void setTblWorkFlowUserProficiencies(Set<TblWorkFlowUserProficiency> tblWorkFlowUserProficiencies) {
		this.tblWorkFlowUserProficiencies = tblWorkFlowUserProficiencies;
	}

}