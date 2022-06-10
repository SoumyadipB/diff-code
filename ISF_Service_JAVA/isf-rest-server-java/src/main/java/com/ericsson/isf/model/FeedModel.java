package com.ericsson.isf.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class FeedModel {
	
	@NotBlank(message = "Market Area can't be left Null/Blank")
	@NotEmpty(message = "Market Area can't be left Null/Blank")
	private String marketArea;
	@NotNull(message="Columns can't be null")
	private List<String> columns;
	@NotNull(message="OrderBy can't be null")
	private String orderBy;
	
	@NotNull(message="filters can't be null")
	private List<Project> filters;
	
	@NotBlank(message = "Start Date can't be left Null/Blank")
	@NotEmpty(message = "Start Date can't be Null/Blank")
	private String eventStartDate;
	
	@NotBlank(message = "End Date can't be left Null/Blank")
	@NotEmpty(message = "End Date can't be left Null/Blank")
	private String eventEndDate;
	
	public String getMarketArea() {
		return marketArea;
	}
	public void setMarketArea(String marketArea) {
		this.marketArea = marketArea;
	}
	public List<String> getColumns() {
		return columns;
	}
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public List<Project> getFilters() {
		return filters;
	}
	public void setFilters(List<Project> filters) {
		this.filters = filters;
	}
	public String getEventStartDate() {
		return eventStartDate;
	}
	public void setEventStartDate(String eventStartDate) {
		this.eventStartDate = eventStartDate;
	}
	public String getEventEndDate() {
		return eventEndDate;
	}
	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
	
}
