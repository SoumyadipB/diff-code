package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblUserRoleId generated by hbm2java
 */
@Embeddable
public class TblUserRoleId implements java.io.Serializable {

	private int userRoleId;
	private String userName;
	private String signumId;
	private String role;
	private Boolean active;

	public TblUserRoleId() {
	}

	public TblUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
	}

	public TblUserRoleId(int userRoleId, String userName, String signumId, String role, Boolean active) {
		this.userRoleId = userRoleId;
		this.userName = userName;
		this.signumId = signumId;
		this.role = role;
		this.active = active;
	}

	@Column(name = "UserRoleID", nullable = false)
	public int getUserRoleId() {
		return this.userRoleId;
	}

	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
	}

	@Column(name = "UserName", length = 1024)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "SignumID", length = 128)
	public String getSignumId() {
		return this.signumId;
	}

	public void setSignumId(String signumId) {
		this.signumId = signumId;
	}

	@Column(name = "Role", length = 512)
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Column(name = "Active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblUserRoleId))
			return false;
		TblUserRoleId castOther = (TblUserRoleId) other;

		return (this.getUserRoleId() == castOther.getUserRoleId())
				&& ((this.getUserName() == castOther.getUserName()) || (this.getUserName() != null
						&& castOther.getUserName() != null && this.getUserName().equals(castOther.getUserName())))
				&& ((this.getSignumId() == castOther.getSignumId()) || (this.getSignumId() != null
						&& castOther.getSignumId() != null && this.getSignumId().equals(castOther.getSignumId())))
				&& ((this.getRole() == castOther.getRole()) || (this.getRole() != null && castOther.getRole() != null
						&& this.getRole().equals(castOther.getRole())))
				&& ((this.getActive() == castOther.getActive()) || (this.getActive() != null
						&& castOther.getActive() != null && this.getActive().equals(castOther.getActive())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getUserRoleId();
		result = 37 * result + (getUserName() == null ? 0 : this.getUserName().hashCode());
		result = 37 * result + (getSignumId() == null ? 0 : this.getSignumId().hashCode());
		result = 37 * result + (getRole() == null ? 0 : this.getRole().hashCode());
		result = 37 * result + (getActive() == null ? 0 : this.getActive().hashCode());
		return result;
	}

}
