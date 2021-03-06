package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TblRoles generated by hbm2java
 */
@Entity
@Table(name = "TBL_Roles", schema = "refData")
public class TblRoles implements java.io.Serializable {

	private int roleId;
	private Serializable roleName;
	private Serializable createdBy;
	private Date createdOn;
	private Serializable lastModifiedBy;
	private Date lastModifiedOn;
	private Set<TblUserRoleMappings> tblUserRoleMappingses = new HashSet<TblUserRoleMappings>(0);

	public TblRoles() {
	}

	public TblRoles(int roleId, Date createdOn, Date lastModifiedOn) {
		this.roleId = roleId;
		this.createdOn = createdOn;
		this.lastModifiedOn = lastModifiedOn;
	}

	public TblRoles(int roleId, Serializable roleName, Serializable createdBy, Date createdOn,
			Serializable lastModifiedBy, Date lastModifiedOn, Set<TblUserRoleMappings> tblUserRoleMappingses) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedOn = lastModifiedOn;
		this.tblUserRoleMappingses = tblUserRoleMappingses;
	}

	@Id

	@Column(name = "RoleID", unique = true, nullable = false)
	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	@Column(name = "RoleName")
	public Serializable getRoleName() {
		return this.roleName;
	}

	public void setRoleName(Serializable roleName) {
		this.roleName = roleName;
	}

	@Column(name = "CreatedBy")
	public Serializable getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Serializable createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedOn", nullable = false, length = 23)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "LastModifiedBy")
	public Serializable getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(Serializable lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LastModifiedOn", nullable = false, length = 23)
	public Date getLastModifiedOn() {
		return this.lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblRoles")
	public Set<TblUserRoleMappings> getTblUserRoleMappingses() {
		return this.tblUserRoleMappingses;
	}

	public void setTblUserRoleMappingses(Set<TblUserRoleMappings> tblUserRoleMappingses) {
		this.tblUserRoleMappingses = tblUserRoleMappingses;
	}

}
