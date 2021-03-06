package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TblEmployeesCompetence generated by hbm2java
 */
@Entity
@Table(name = "TBL_EmployeesCompetence", schema = "refData")
public class TblEmployeesCompetence implements java.io.Serializable {

	private int id;
	private String signum;
	private int merlinExtId;
	private String competenceCategory;
	private Integer parentCompetenceExtId;
	private String competenceAssessedLevel;
	private Date uploadedOn;

	public TblEmployeesCompetence() {
	}

	public TblEmployeesCompetence(int id, String signum, int merlinExtId, Date uploadedOn) {
		this.id = id;
		this.signum = signum;
		this.merlinExtId = merlinExtId;
		this.uploadedOn = uploadedOn;
	}

	public TblEmployeesCompetence(int id, String signum, int merlinExtId, String competenceCategory,
			Integer parentCompetenceExtId, String competenceAssessedLevel, Date uploadedOn) {
		this.id = id;
		this.signum = signum;
		this.merlinExtId = merlinExtId;
		this.competenceCategory = competenceCategory;
		this.parentCompetenceExtId = parentCompetenceExtId;
		this.competenceAssessedLevel = competenceAssessedLevel;
		this.uploadedOn = uploadedOn;
	}

	@Id

	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "Signum", nullable = false, length = 250)
	public String getSignum() {
		return this.signum;
	}

	public void setSignum(String signum) {
		this.signum = signum;
	}

	@Column(name = "MerlinExtID", nullable = false)
	public int getMerlinExtId() {
		return this.merlinExtId;
	}

	public void setMerlinExtId(int merlinExtId) {
		this.merlinExtId = merlinExtId;
	}

	@Column(name = "CompetenceCategory", length = 250)
	public String getCompetenceCategory() {
		return this.competenceCategory;
	}

	public void setCompetenceCategory(String competenceCategory) {
		this.competenceCategory = competenceCategory;
	}

	@Column(name = "ParentCompetenceExtID")
	public Integer getParentCompetenceExtId() {
		return this.parentCompetenceExtId;
	}

	public void setParentCompetenceExtId(Integer parentCompetenceExtId) {
		this.parentCompetenceExtId = parentCompetenceExtId;
	}

	@Column(name = "CompetenceAssessedLevel", length = 2)
	public String getCompetenceAssessedLevel() {
		return this.competenceAssessedLevel;
	}

	public void setCompetenceAssessedLevel(String competenceAssessedLevel) {
		this.competenceAssessedLevel = competenceAssessedLevel;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "UploadedOn", nullable = false, length = 10)
	public Date getUploadedOn() {
		return this.uploadedOn;
	}

	public void setUploadedOn(Date uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

}
