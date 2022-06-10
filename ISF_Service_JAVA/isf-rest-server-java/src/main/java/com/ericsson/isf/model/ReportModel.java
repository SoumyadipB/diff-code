/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author edhhklu
 */
public class ReportModel {
	private long reportId;
	private int modSubModuleId;
	private String reportName;
	private String moduleName;
	private String subModuleName;
	private String reportDescription;
	private String formula;
	private String technicalExplaination;
	private String remarks;
	private String detailQuery;
	private String selectGroupBy;
	private String aggColumn;
	private String filterAttribute;
	private String chartType;
	private String chartTypeConfig;
	private String axisType;
	private String xAxisLabel;
	private String yAxisLabel;
	private String columnChart;
	private String columnDetailQuery;
	private boolean active;
	private String createdBy;
	private String CreatedOn;
	private String modifiedBy;
	private String chartQuery;
	private String signumID;
	private String reportInfoImg;

	public String getReportInfoImg() {
		return reportInfoImg;
	}

	public void setReportInfoImg(String reportInfoImg) {
		this.reportInfoImg = reportInfoImg;
	}

	public int getModSubModuleId() {
		return modSubModuleId;
	}

	public void setModSubModuleId(int modSubModuleId) {
		this.modSubModuleId = modSubModuleId;
	}

	private String ModifiedOn;

	public String getSelectGroupBy() {
		return selectGroupBy;
	}

	public void setSelectGroupBy(String selectGroupBy) {
		this.selectGroupBy = selectGroupBy;
	}

	public String getAggColumn() {
		return aggColumn;
	}

	public void setAggColumn(String aggColumn) {
		this.aggColumn = aggColumn;
	}

	public long getReportId() {
		return reportId;
	}

	public void setReportId(long reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getSubModuleName() {
		return subModuleName;
	}

	public void setSubModuleName(String subModuleName) {
		this.subModuleName = subModuleName;
	}

	public String getReportDescription() {
		return reportDescription;
	}

	public void setReportDescription(String reportDescription) {
		this.reportDescription = reportDescription;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getTechnicalExplaination() {
		return technicalExplaination;
	}

	public void setTechnicalExplaination(String technicalExplaination) {
		this.technicalExplaination = technicalExplaination;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDetailQuery() {
		return detailQuery;
	}

	public void setDetailQuery(String detailQuery) {
		this.detailQuery = detailQuery;
	}

	public String getFilterAttribute() {
		return filterAttribute;
	}

	public void setFilterAttribute(String filterAttribute) {
		this.filterAttribute = filterAttribute;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getAxisType() {
		return axisType;
	}

	public void setAxisType(String axisType) {
		this.axisType = axisType;
	}

	public String getxAxisLabel() {
		return xAxisLabel;
	}

	public void setxAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
	}

	public String getyAxisLabel() {
		return yAxisLabel;
	}

	public void setyAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
	}

	public String getColumnChart() {
		return columnChart;
	}

	public void setColumnChart(String columnChart) {
		this.columnChart = columnChart;
	}

	public String getColumnDetailQuery() {
		return columnDetailQuery;
	}

	public void setColumnDetailQuery(String columnDetailQuery) {
		this.columnDetailQuery = columnDetailQuery;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedOn() {
		return CreatedOn;
	}

	public void setCreatedOn(String createdOn) {
		CreatedOn = createdOn;
	}

	public String getModifiedOn() {
		return ModifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		ModifiedOn = modifiedOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getChartQuery() {
		return chartQuery;
	}

	public void setChartQuery(String chartQuery) {
		this.chartQuery = chartQuery;
	}

	public String getSignumID() {
		return signumID;
	}

	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}

	public String getChartTypeConfig() {
		return chartTypeConfig;
	}

	public void setChartTypeConfig(String chartTypeConfig) {
		this.chartTypeConfig = chartTypeConfig;
	}

}
