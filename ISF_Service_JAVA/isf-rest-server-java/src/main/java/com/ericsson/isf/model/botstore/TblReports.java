package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TblReports generated by hbm2java
 */
@Entity
@Table(name = "TBL_Reports", schema = "refData")
public class TblReports implements java.io.Serializable {

	private TblReportsId id;
	private TblReportsModuleSubModule tblReportsModuleSubModule;
	private int reportId;
	private String reportDescription;
	private String formula;
	private String technicalExplaination;
	private String remarks;
	private String detailQuery;
	private String chartQuery;
	private String selectGroupBy;
	private String filterAttribute;
	private String chartType;
	private String axisType;
	private String xaxisLabel;
	private String yaxisLabel;
	private String columnChart;
	private String columnDetailQuery;
	private int active;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	private String chartTypeConfig;
	private String reportInfoImg;

	public TblReports() {
	}

	public TblReports(TblReportsId id, TblReportsModuleSubModule tblReportsModuleSubModule, int reportId,
			String formula, String technicalExplaination, String chartQuery, String selectGroupBy, String chartType,
			String axisType, String xaxisLabel, int active, String createdBy, Date createdOn) {
		this.id = id;
		this.tblReportsModuleSubModule = tblReportsModuleSubModule;
		this.reportId = reportId;
		this.formula = formula;
		this.technicalExplaination = technicalExplaination;
		this.chartQuery = chartQuery;
		this.selectGroupBy = selectGroupBy;
		this.chartType = chartType;
		this.axisType = axisType;
		this.xaxisLabel = xaxisLabel;
		this.active = active;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	public TblReports(TblReportsId id, TblReportsModuleSubModule tblReportsModuleSubModule, int reportId,
			String reportDescription, String formula, String technicalExplaination, String remarks, String detailQuery,
			String chartQuery, String selectGroupBy, String filterAttribute, String chartType, String axisType,
			String xaxisLabel, String yaxisLabel, String columnChart, String columnDetailQuery, int active,
			String createdBy, Date createdOn, String modifiedBy, Date modifiedOn, String chartTypeConfig,
			String reportInfoImg) {
		this.id = id;
		this.tblReportsModuleSubModule = tblReportsModuleSubModule;
		this.reportId = reportId;
		this.reportDescription = reportDescription;
		this.formula = formula;
		this.technicalExplaination = technicalExplaination;
		this.remarks = remarks;
		this.detailQuery = detailQuery;
		this.chartQuery = chartQuery;
		this.selectGroupBy = selectGroupBy;
		this.filterAttribute = filterAttribute;
		this.chartType = chartType;
		this.axisType = axisType;
		this.xaxisLabel = xaxisLabel;
		this.yaxisLabel = yaxisLabel;
		this.columnChart = columnChart;
		this.columnDetailQuery = columnDetailQuery;
		this.active = active;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.modifiedBy = modifiedBy;
		this.modifiedOn = modifiedOn;
		this.chartTypeConfig = chartTypeConfig;
		this.reportInfoImg = reportInfoImg;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "reportName", column = @Column(name = "reportName", nullable = false, length = 300)),
			@AttributeOverride(name = "modSubModuleId", column = @Column(name = "modSubModuleId", nullable = false)) })
	public TblReportsId getId() {
		return this.id;
	}

	public void setId(TblReportsId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modSubModuleId", nullable = false, insertable = false, updatable = false)
	public TblReportsModuleSubModule getTblReportsModuleSubModule() {
		return this.tblReportsModuleSubModule;
	}

	public void setTblReportsModuleSubModule(TblReportsModuleSubModule tblReportsModuleSubModule) {
		this.tblReportsModuleSubModule = tblReportsModuleSubModule;
	}

	@Column(name = "reportId", nullable = false)
	public int getReportId() {
		return this.reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	@Column(name = "reportDescription", length = 2000)
	public String getReportDescription() {
		return this.reportDescription;
	}

	public void setReportDescription(String reportDescription) {
		this.reportDescription = reportDescription;
	}

	@Column(name = "formula", nullable = false, length = 8000)
	public String getFormula() {
		return this.formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	@Column(name = "technicalExplaination", nullable = false, length = 2000)
	public String getTechnicalExplaination() {
		return this.technicalExplaination;
	}

	public void setTechnicalExplaination(String technicalExplaination) {
		this.technicalExplaination = technicalExplaination;
	}

	@Column(name = "remarks", length = 2000)
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "detailQuery")
	public String getDetailQuery() {
		return this.detailQuery;
	}

	public void setDetailQuery(String detailQuery) {
		this.detailQuery = detailQuery;
	}

	@Column(name = "chartQuery", nullable = false, length = 8000)
	public String getChartQuery() {
		return this.chartQuery;
	}

	public void setChartQuery(String chartQuery) {
		this.chartQuery = chartQuery;
	}

	@Column(name = "selectGroupBy", nullable = false, length = 2000)
	public String getSelectGroupBy() {
		return this.selectGroupBy;
	}

	public void setSelectGroupBy(String selectGroupBy) {
		this.selectGroupBy = selectGroupBy;
	}

	@Column(name = "filterAttribute", length = 8000)
	public String getFilterAttribute() {
		return this.filterAttribute;
	}

	public void setFilterAttribute(String filterAttribute) {
		this.filterAttribute = filterAttribute;
	}

	@Column(name = "chartType", nullable = false, length = 400)
	public String getChartType() {
		return this.chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	@Column(name = "axisType", nullable = false, length = 100)
	public String getAxisType() {
		return this.axisType;
	}

	public void setAxisType(String axisType) {
		this.axisType = axisType;
	}

	@Column(name = "xAxisLabel", nullable = false, length = 200)
	public String getXaxisLabel() {
		return this.xaxisLabel;
	}

	public void setXaxisLabel(String xaxisLabel) {
		this.xaxisLabel = xaxisLabel;
	}

	@Column(name = "yAxisLabel", length = 500)
	public String getYaxisLabel() {
		return this.yaxisLabel;
	}

	public void setYaxisLabel(String yaxisLabel) {
		this.yaxisLabel = yaxisLabel;
	}

	@Column(name = "columnChart", length = 200)
	public String getColumnChart() {
		return this.columnChart;
	}

	public void setColumnChart(String columnChart) {
		this.columnChart = columnChart;
	}

	@Column(name = "columnDetailQuery", length = 8000)
	public String getColumnDetailQuery() {
		return this.columnDetailQuery;
	}

	public void setColumnDetailQuery(String columnDetailQuery) {
		this.columnDetailQuery = columnDetailQuery;
	}

	@Column(name = "active", nullable = false)
	public int getActive() {
		return this.active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	@Column(name = "createdBy", nullable = false, length = 500)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
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

	@Column(name = "ModifiedBy", length = 500)
	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ModifiedOn", length = 23)
	public Date getModifiedOn() {
		return this.modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	@Column(name = "chartTypeConfig", length = 500)
	public String getChartTypeConfig() {
		return this.chartTypeConfig;
	}

	public void setChartTypeConfig(String chartTypeConfig) {
		this.chartTypeConfig = chartTypeConfig;
	}

	@Column(name = "reportInfoImg", length = 100)
	public String getReportInfoImg() {
		return this.reportInfoImg;
	}

	public void setReportInfoImg(String reportInfoImg) {
		this.reportInfoImg = reportInfoImg;
	}

}
