package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonModel {


	@JsonProperty("totalRecords") 
	public int totalRecords;
	
	@JsonProperty("currentPage") 
	public int currentPage;

	@JsonProperty("totalPages") 
	public int totalPages;

	@JsonProperty("pageSize") 
	public int pageSize;

//	@JsonProperty("data") 
//	WorkPlanFullModel[] workPlanFullModel;

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

//	public WorkPlanFullModel[] getWorkPlanFullModel() {
//		return workPlanFullModel;
//	}
//
//	public void setWorkPlanFullModel(WorkPlanFullModel[] workPlanFullModel) {
//		this.workPlanFullModel = workPlanFullModel;
//	}


}
