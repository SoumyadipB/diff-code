package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ProjectSubactivityDetailsId generated by hbm2java
 */
@Embeddable
public class ProjectSubactivityDetailsId implements java.io.Serializable {

	private int projectId;
	private Serializable projectName;
	private Integer subActivityId;
	private String subActivity;

	public ProjectSubactivityDetailsId() {
	}

	public ProjectSubactivityDetailsId(int projectId) {
		this.projectId = projectId;
	}

	public ProjectSubactivityDetailsId(int projectId, Serializable projectName, Integer subActivityId,
			String subActivity) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.subActivityId = subActivityId;
		this.subActivity = subActivity;
	}

	@Column(name = "ProjectID", nullable = false)
	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	@Column(name = "ProjectName")
	public Serializable getProjectName() {
		return this.projectName;
	}

	public void setProjectName(Serializable projectName) {
		this.projectName = projectName;
	}

	@Column(name = "SubActivityID")
	public Integer getSubActivityId() {
		return this.subActivityId;
	}

	public void setSubActivityId(Integer subActivityId) {
		this.subActivityId = subActivityId;
	}

	@Column(name = "SubActivity", length = 1412)
	public String getSubActivity() {
		return this.subActivity;
	}

	public void setSubActivity(String subActivity) {
		this.subActivity = subActivity;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ProjectSubactivityDetailsId))
			return false;
		ProjectSubactivityDetailsId castOther = (ProjectSubactivityDetailsId) other;

		return (this.getProjectId() == castOther.getProjectId())
				&& ((this.getProjectName() == castOther.getProjectName())
						|| (this.getProjectName() != null && castOther.getProjectName() != null
								&& this.getProjectName().equals(castOther.getProjectName())))
				&& ((this.getSubActivityId() == castOther.getSubActivityId())
						|| (this.getSubActivityId() != null && castOther.getSubActivityId() != null
								&& this.getSubActivityId().equals(castOther.getSubActivityId())))
				&& ((this.getSubActivity() == castOther.getSubActivity())
						|| (this.getSubActivity() != null && castOther.getSubActivity() != null
								&& this.getSubActivity().equals(castOther.getSubActivity())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getProjectId();
		result = 37 * result + (getProjectName() == null ? 0 : this.getProjectName().hashCode());
		result = 37 * result + (getSubActivityId() == null ? 0 : this.getSubActivityId().hashCode());
		result = 37 * result + (getSubActivity() == null ? 0 : this.getSubActivity().hashCode());
		return result;
	}

}