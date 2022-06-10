package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblProjectNodetypeDbId generated by hbm2java
 */
@Embeddable
public class TblProjectNodetypeDbId implements java.io.Serializable {

	private int projectNodeTypeId;
	private Integer projectId;
	private String nodeType;

	public TblProjectNodetypeDbId() {
	}

	public TblProjectNodetypeDbId(int projectNodeTypeId) {
		this.projectNodeTypeId = projectNodeTypeId;
	}

	public TblProjectNodetypeDbId(int projectNodeTypeId, Integer projectId, String nodeType) {
		this.projectNodeTypeId = projectNodeTypeId;
		this.projectId = projectId;
		this.nodeType = nodeType;
	}

	@Column(name = "ProjectNodeTypeID", nullable = false)
	public int getProjectNodeTypeId() {
		return this.projectNodeTypeId;
	}

	public void setProjectNodeTypeId(int projectNodeTypeId) {
		this.projectNodeTypeId = projectNodeTypeId;
	}

	@Column(name = "ProjectID")
	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Column(name = "NodeType", length = 1024)
	public String getNodeType() {
		return this.nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblProjectNodetypeDbId))
			return false;
		TblProjectNodetypeDbId castOther = (TblProjectNodetypeDbId) other;

		return (this.getProjectNodeTypeId() == castOther.getProjectNodeTypeId())
				&& ((this.getProjectId() == castOther.getProjectId()) || (this.getProjectId() != null
						&& castOther.getProjectId() != null && this.getProjectId().equals(castOther.getProjectId())))
				&& ((this.getNodeType() == castOther.getNodeType()) || (this.getNodeType() != null
						&& castOther.getNodeType() != null && this.getNodeType().equals(castOther.getNodeType())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getProjectNodeTypeId();
		result = 37 * result + (getProjectId() == null ? 0 : this.getProjectId().hashCode());
		result = 37 * result + (getNodeType() == null ? 0 : this.getNodeType().hashCode());
		return result;
	}

}