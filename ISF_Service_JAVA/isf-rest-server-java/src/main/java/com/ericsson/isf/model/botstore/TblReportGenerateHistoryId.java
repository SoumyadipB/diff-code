package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblReportGenerateHistoryId generated by hbm2java
 */
@Embeddable
public class TblReportGenerateHistoryId implements java.io.Serializable {

	private int generatedReportId;
	private String signumId;
	private String moduleName;
	private String subModuleName;
	private String reportName;
	private String groupBy;
	private Date generatedDate;

	public TblReportGenerateHistoryId() {
	}

	public TblReportGenerateHistoryId(int generatedReportId) {
		this.generatedReportId = generatedReportId;
	}

	public TblReportGenerateHistoryId(int generatedReportId, String signumId, String moduleName, String subModuleName,
			String reportName, String groupBy, Date generatedDate) {
		this.generatedReportId = generatedReportId;
		this.signumId = signumId;
		this.moduleName = moduleName;
		this.subModuleName = subModuleName;
		this.reportName = reportName;
		this.groupBy = groupBy;
		this.generatedDate = generatedDate;
	}

	@Column(name = "GeneratedReportID", nullable = false)
	public int getGeneratedReportId() {
		return this.generatedReportId;
	}

	public void setGeneratedReportId(int generatedReportId) {
		this.generatedReportId = generatedReportId;
	}

	@Column(name = "SignumID", length = 256)
	public String getSignumId() {
		return this.signumId;
	}

	public void setSignumId(String signumId) {
		this.signumId = signumId;
	}

	@Column(name = "ModuleName")
	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Column(name = "SubModuleName")
	public String getSubModuleName() {
		return this.subModuleName;
	}

	public void setSubModuleName(String subModuleName) {
		this.subModuleName = subModuleName;
	}

	@Column(name = "ReportName")
	public String getReportName() {
		return this.reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Column(name = "GroupBy")
	public String getGroupBy() {
		return this.groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	@Column(name = "GeneratedDate", length = 23)
	public Date getGeneratedDate() {
		return this.generatedDate;
	}

	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblReportGenerateHistoryId))
			return false;
		TblReportGenerateHistoryId castOther = (TblReportGenerateHistoryId) other;

		return (this.getGeneratedReportId() == castOther.getGeneratedReportId())
				&& ((this.getSignumId() == castOther.getSignumId()) || (this.getSignumId() != null
						&& castOther.getSignumId() != null && this.getSignumId().equals(castOther.getSignumId())))
				&& ((this.getModuleName() == castOther.getModuleName()) || (this.getModuleName() != null
						&& castOther.getModuleName() != null && this.getModuleName().equals(castOther.getModuleName())))
				&& ((this.getSubModuleName() == castOther.getSubModuleName())
						|| (this.getSubModuleName() != null && castOther.getSubModuleName() != null
								&& this.getSubModuleName().equals(castOther.getSubModuleName())))
				&& ((this.getReportName() == castOther.getReportName()) || (this.getReportName() != null
						&& castOther.getReportName() != null && this.getReportName().equals(castOther.getReportName())))
				&& ((this.getGroupBy() == castOther.getGroupBy()) || (this.getGroupBy() != null
						&& castOther.getGroupBy() != null && this.getGroupBy().equals(castOther.getGroupBy())))
				&& ((this.getGeneratedDate() == castOther.getGeneratedDate())
						|| (this.getGeneratedDate() != null && castOther.getGeneratedDate() != null
								&& this.getGeneratedDate().equals(castOther.getGeneratedDate())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getGeneratedReportId();
		result = 37 * result + (getSignumId() == null ? 0 : this.getSignumId().hashCode());
		result = 37 * result + (getModuleName() == null ? 0 : this.getModuleName().hashCode());
		result = 37 * result + (getSubModuleName() == null ? 0 : this.getSubModuleName().hashCode());
		result = 37 * result + (getReportName() == null ? 0 : this.getReportName().hashCode());
		result = 37 * result + (getGroupBy() == null ? 0 : this.getGroupBy().hashCode());
		result = 37 * result + (getGeneratedDate() == null ? 0 : this.getGeneratedDate().hashCode());
		return result;
	}

}
