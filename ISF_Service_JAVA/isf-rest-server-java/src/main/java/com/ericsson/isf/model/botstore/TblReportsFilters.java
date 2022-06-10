package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TblReportsFilters generated by hbm2java
 */
@Entity
@Table(name = "TBL_Reports_Filters", schema = "refData")
public class TblReportsFilters implements java.io.Serializable {

	private String filterGroup;
	private int filterId;
	private String filterDescription;
	private String filterconfig;

	public TblReportsFilters() {
	}

	public TblReportsFilters(String filterGroup, int filterId, String filterDescription) {
		this.filterGroup = filterGroup;
		this.filterId = filterId;
		this.filterDescription = filterDescription;
	}

	public TblReportsFilters(String filterGroup, int filterId, String filterDescription, String filterconfig) {
		this.filterGroup = filterGroup;
		this.filterId = filterId;
		this.filterDescription = filterDescription;
		this.filterconfig = filterconfig;
	}

	@Id

	@Column(name = "filterGroup", unique = true, nullable = false, length = 300)
	public String getFilterGroup() {
		return this.filterGroup;
	}

	public void setFilterGroup(String filterGroup) {
		this.filterGroup = filterGroup;
	}

	@Column(name = "filterId", nullable = false)
	public int getFilterId() {
		return this.filterId;
	}

	public void setFilterId(int filterId) {
		this.filterId = filterId;
	}

	@Column(name = "filterDescription", nullable = false, length = 4000)
	public String getFilterDescription() {
		return this.filterDescription;
	}

	public void setFilterDescription(String filterDescription) {
		this.filterDescription = filterDescription;
	}

	@Column(name = "filterconfig", length = 500)
	public String getFilterconfig() {
		return this.filterconfig;
	}

	public void setFilterconfig(String filterconfig) {
		this.filterconfig = filterconfig;
	}

}