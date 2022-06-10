package com.ericsson.isf.model;

import java.util.Date;

import com.ericsson.isf.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErisiteSchedule {
 
private String baselineEndDate;
	
	private String baselineStartDate;
	
	private String plannedStartDate;
	
	private String plannedEndDate;
	
	private Date forecastStartDate;
	
	private String forecastStartDateTmp;
	
	private Date forecastEndDate;
	
	private String forecastEndDateTmp;
	
	private String actualStartDate;
	
	private String actualEndDate;
	
	@JsonProperty("info:BaselineEndDate")
	
	public String getBaselineEndDate() {
		return baselineEndDate;
	}
	@JsonProperty("info:BaselineEndDate")
	public void setBaselineEndDate(String baselineEndDate) {
		this.baselineEndDate = baselineEndDate;
	}
	@JsonProperty("info:BaselineStartDate")
	
	public String getBaselineStartDate() {
		return baselineStartDate;
	}
	@JsonProperty("info:BaselineStartDate")
	public void setBaselineStartDate(String baselineStartDate) {
		this.baselineStartDate = baselineStartDate;
	}
	@JsonProperty("info:PlannedStartDate")
	
	public String getPlannedStartDate() {
		return plannedStartDate;
	}
	@JsonProperty("info:PlannedStartDate")
	public void setPlannedStartDate(String plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}
	@JsonProperty("info:PlannedEndDate")
	
	public String getPlannedEndDate() {
		return plannedEndDate;
	}
	@JsonProperty("info:PlannedEndDate")
	public void setPlannedEndDate(String plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone =AppConstants.TIMEZONE_IST)
	public Date getForecastStartDate() {
		return forecastStartDate;
	}
	
	public void setForecastStartDate(Date forecastStartDate) {
		this.forecastStartDate = forecastStartDate;
	}
	
	@JsonProperty("info:ForecastStartDate")
	public String getForecastStartDateTmp() {
		return forecastStartDateTmp;
	}
	@JsonProperty("info:ForecastStartDate")
	public void setForecastStartDateTmp(String forecastStartDateTmp) {
		this.forecastStartDateTmp = forecastStartDateTmp;
	}
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone =AppConstants.TIMEZONE_IST)
	public Date getForecastEndDate() {
		return forecastEndDate;
	}
	public void setForecastEndDate(Date forecastEndDate) {
		this.forecastEndDate = forecastEndDate;
	}
	
	@JsonProperty("info:ForecastEndDate")
	public String getForecastEndDateTmp() {
		return forecastEndDateTmp;
	}
	@JsonProperty("info:ForecastEndDate")
	public void setForecastEndDateTmp(String forecastEndDateTmp) {
		this.forecastEndDateTmp = forecastEndDateTmp;
	}
	
	
	@JsonProperty("info:ActualStartDate")
	
	public String getActualStartDate() {
		return actualStartDate;
	}
	@JsonProperty("info:ActualStartDate")
	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	@JsonProperty("info:ActualEndDate")
	
	public String getActualEndDate() {
		return actualEndDate;
	}
	@JsonProperty("info:ActualEndDate")
	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
}
