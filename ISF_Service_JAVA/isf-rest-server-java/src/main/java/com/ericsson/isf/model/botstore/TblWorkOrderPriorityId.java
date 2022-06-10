package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblWorkOrderPriorityId generated by hbm2java
 */
@Embeddable
public class TblWorkOrderPriorityId implements java.io.Serializable {

	private int wopriority;
	private String priority;
	private String description;
	private Boolean active;

	public TblWorkOrderPriorityId() {
	}

	public TblWorkOrderPriorityId(int wopriority) {
		this.wopriority = wopriority;
	}

	public TblWorkOrderPriorityId(int wopriority, String priority, String description, Boolean active) {
		this.wopriority = wopriority;
		this.priority = priority;
		this.description = description;
		this.active = active;
	}

	@Column(name = "WOPriority", nullable = false)
	public int getWopriority() {
		return this.wopriority;
	}

	public void setWopriority(int wopriority) {
		this.wopriority = wopriority;
	}

	@Column(name = "Priority", length = 1024)
	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	@Column(name = "Description", length = 1204)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		if (!(other instanceof TblWorkOrderPriorityId))
			return false;
		TblWorkOrderPriorityId castOther = (TblWorkOrderPriorityId) other;

		return (this.getWopriority() == castOther.getWopriority())
				&& ((this.getPriority() == castOther.getPriority()) || (this.getPriority() != null
						&& castOther.getPriority() != null && this.getPriority().equals(castOther.getPriority())))
				&& ((this.getDescription() == castOther.getDescription())
						|| (this.getDescription() != null && castOther.getDescription() != null
								&& this.getDescription().equals(castOther.getDescription())))
				&& ((this.getActive() == castOther.getActive()) || (this.getActive() != null
						&& castOther.getActive() != null && this.getActive().equals(castOther.getActive())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getWopriority();
		result = 37 * result + (getPriority() == null ? 0 : this.getPriority().hashCode());
		result = 37 * result + (getDescription() == null ? 0 : this.getDescription().hashCode());
		result = 37 * result + (getActive() == null ? 0 : this.getActive().hashCode());
		return result;
	}

}
