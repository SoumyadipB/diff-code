package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblDashboardConfig generated by hbm2java
 */
@Entity
@Table(name = "TBL_DASHBOARD_CONFIG", schema = "refData")
public class TblDashboardConfig implements java.io.Serializable {

	private TblDashboardConfigId id;
	private String reportid;
	private String dashboardname;
	private String widgetName;

	public TblDashboardConfig() {
	}

	public TblDashboardConfig(TblDashboardConfigId id, String reportid, String dashboardname, String widgetName) {
		this.id = id;
		this.reportid = reportid;
		this.dashboardname = dashboardname;
		this.widgetName = widgetName;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "dashboardId", column = @Column(name = "dashboardId", nullable = false)),
			@AttributeOverride(name = "sequence", column = @Column(name = "sequence", nullable = false, length = 300)) })
	public TblDashboardConfigId getId() {
		return this.id;
	}

	public void setId(TblDashboardConfigId id) {
		this.id = id;
	}

	@Column(name = "Reportid", nullable = false, length = 300)
	public String getReportid() {
		return this.reportid;
	}

	public void setReportid(String reportid) {
		this.reportid = reportid;
	}

	@Column(name = "dashboardname", nullable = false, length = 300)
	public String getDashboardname() {
		return this.dashboardname;
	}

	public void setDashboardname(String dashboardname) {
		this.dashboardname = dashboardname;
	}

	@Column(name = "widgetName", nullable = false, length = 300)
	public String getWidgetName() {
		return this.widgetName;
	}

	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}

}
