package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblWorkOrderNodesId generated by hbm2java
 */
@Embeddable
public class TblWorkOrderNodesId implements java.io.Serializable {

	private Integer woid;
	private String nodeType;
	private String nodeNames;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private int wnid;

	public TblWorkOrderNodesId() {
	}

	public TblWorkOrderNodesId(int wnid) {
		this.wnid = wnid;
	}

	public TblWorkOrderNodesId(Integer woid, String nodeType, String nodeNames, String createdBy, Date createdDate,
			String lastModifiedBy, Date lastModifiedDate, int wnid) {
		this.woid = woid;
		this.nodeType = nodeType;
		this.nodeNames = nodeNames;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.wnid = wnid;
	}

	@Column(name = "WOID")
	public Integer getWoid() {
		return this.woid;
	}

	public void setWoid(Integer woid) {
		this.woid = woid;
	}

	@Column(name = "NodeType", length = 512)
	public String getNodeType() {
		return this.nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	@Column(name = "NodeNames", length = 500)
	public String getNodeNames() {
		return this.nodeNames;
	}

	public void setNodeNames(String nodeNames) {
		this.nodeNames = nodeNames;
	}

	@Column(name = "CreatedBy", length = 1024)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CreatedDate", length = 23)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "LastModifiedBy", length = 1024)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Column(name = "LastModifiedDate", length = 23)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "WNID", nullable = false)
	public int getWnid() {
		return this.wnid;
	}

	public void setWnid(int wnid) {
		this.wnid = wnid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblWorkOrderNodesId))
			return false;
		TblWorkOrderNodesId castOther = (TblWorkOrderNodesId) other;

		return ((this.getWoid() == castOther.getWoid()) || (this.getWoid() != null && castOther.getWoid() != null
				&& this.getWoid().equals(castOther.getWoid())))
				&& ((this.getNodeType() == castOther.getNodeType()) || (this.getNodeType() != null
						&& castOther.getNodeType() != null && this.getNodeType().equals(castOther.getNodeType())))
				&& ((this.getNodeNames() == castOther.getNodeNames()) || (this.getNodeNames() != null
						&& castOther.getNodeNames() != null && this.getNodeNames().equals(castOther.getNodeNames())))
				&& ((this.getCreatedBy() == castOther.getCreatedBy()) || (this.getCreatedBy() != null
						&& castOther.getCreatedBy() != null && this.getCreatedBy().equals(castOther.getCreatedBy())))
				&& ((this.getCreatedDate() == castOther.getCreatedDate())
						|| (this.getCreatedDate() != null && castOther.getCreatedDate() != null
								&& this.getCreatedDate().equals(castOther.getCreatedDate())))
				&& ((this.getLastModifiedBy() == castOther.getLastModifiedBy())
						|| (this.getLastModifiedBy() != null && castOther.getLastModifiedBy() != null
								&& this.getLastModifiedBy().equals(castOther.getLastModifiedBy())))
				&& ((this.getLastModifiedDate() == castOther.getLastModifiedDate())
						|| (this.getLastModifiedDate() != null && castOther.getLastModifiedDate() != null
								&& this.getLastModifiedDate().equals(castOther.getLastModifiedDate())))
				&& (this.getWnid() == castOther.getWnid());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getWoid() == null ? 0 : this.getWoid().hashCode());
		result = 37 * result + (getNodeType() == null ? 0 : this.getNodeType().hashCode());
		result = 37 * result + (getNodeNames() == null ? 0 : this.getNodeNames().hashCode());
		result = 37 * result + (getCreatedBy() == null ? 0 : this.getCreatedBy().hashCode());
		result = 37 * result + (getCreatedDate() == null ? 0 : this.getCreatedDate().hashCode());
		result = 37 * result + (getLastModifiedBy() == null ? 0 : this.getLastModifiedBy().hashCode());
		result = 37 * result + (getLastModifiedDate() == null ? 0 : this.getLastModifiedDate().hashCode());
		result = 37 * result + this.getWnid();
		return result;
	}

}