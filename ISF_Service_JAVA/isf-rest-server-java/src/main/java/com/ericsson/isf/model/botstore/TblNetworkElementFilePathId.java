package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblNetworkElementFilePathId generated by hbm2java
 */
@Embeddable
public class TblNetworkElementFilePathId implements java.io.Serializable {

	private int id;
	private String filePath;
	private String createdBy;
	private String createdDate;

	public TblNetworkElementFilePathId() {
	}

	public TblNetworkElementFilePathId(int id) {
		this.id = id;
	}

	public TblNetworkElementFilePathId(int id, String filePath, String createdBy, String createdDate) {
		this.id = id;
		this.filePath = filePath;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
	}

	@Column(name = "ID", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "FilePath")
	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "CreatedBy", length = 1024)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CreatedDate", length = 1024)
	public String getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblNetworkElementFilePathId))
			return false;
		TblNetworkElementFilePathId castOther = (TblNetworkElementFilePathId) other;

		return (this.getId() == castOther.getId())
				&& ((this.getFilePath() == castOther.getFilePath()) || (this.getFilePath() != null
						&& castOther.getFilePath() != null && this.getFilePath().equals(castOther.getFilePath())))
				&& ((this.getCreatedBy() == castOther.getCreatedBy()) || (this.getCreatedBy() != null
						&& castOther.getCreatedBy() != null && this.getCreatedBy().equals(castOther.getCreatedBy())))
				&& ((this.getCreatedDate() == castOther.getCreatedDate())
						|| (this.getCreatedDate() != null && castOther.getCreatedDate() != null
								&& this.getCreatedDate().equals(castOther.getCreatedDate())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getId();
		result = 37 * result + (getFilePath() == null ? 0 : this.getFilePath().hashCode());
		result = 37 * result + (getCreatedBy() == null ? 0 : this.getCreatedBy().hashCode());
		result = 37 * result + (getCreatedDate() == null ? 0 : this.getCreatedDate().hashCode());
		return result;
	}

}
