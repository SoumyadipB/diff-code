/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.model.DhashboardConfModel;
import com.ericsson.isf.model.ExportedReportHistoryModel;
import com.ericsson.isf.model.FilterModel;
import com.ericsson.isf.model.ReportModel;

/**
 *
 * @author edhhklu
 */
public interface ReportManagementMapper {

	public List<ReportModel> getReportList();

	public int addReport(@Param("reportModel") ReportModel reportModel);

	public int addModuleSubModule(@Param("reportModel") ReportModel reportModel);

	public Boolean checkIFModuleSubModuleExists(@Param("reportModel") ReportModel reportModel);

	public int deleteReport(@Param("reportModel") ReportModel reportModel);

	public int updateReport(@Param("reportModel") ReportModel reportModel);

	public List<LinkedHashMap<String, Object>> searchReport(@Param("reportName") String reportName,
			@Param("subModule") String subModule, @Param("module") String module);

	
	public ReportModel getReportDetail(@Param("reportRequest") ReportModel reportRequest);

	public List<Map<String, Object>> getDeliveryDashboardData(@Param("signum") String signum,
			@Param("marketArea") String marketArea, @Param("role") String role);

	public List<Map<String, Object>> getAllModuleSubmodule(@Param("activeFlag") String activeFlag);

	public List<Map<String, Object>> generateReport(@Param("request") ReportModel reportRequest);

	public List<Map<String, Object>> validateDetailedQuery(@Param("request") ReportModel reportRequest);

	public List<HashMap<String, Object>> getCompetenceLevel(@Param("RRID") int rrID);

	public List<Map<String, Object>> getReportFilters();

	public List<Map<String, Object>> generateChartReport(@Param("request") ReportModel reportRequest);

	public int getModuleIdByName(@Param("moduleName") String moduleName, @Param("subModuleName") String subModuleName);

	public List<Map<String, Object>> getFilterData(@Param("tablename") String tablename, @Param("cols") String cols);

	public List<FilterModel> getFilters(@Param("filterGroup") String filterGroup);

	public DhashboardConfModel getDhashboardDetailsById(@Param("dashboardId") String dashboardId,
			@Param("sequence") String sequence);

	public ReportModel getReportDetailsById(@Param("reportId") String reportId);

	public List<Map<String, Object>> generateDetailedReport(@Param("request") ReportModel reportRequest);

	public List<FilterModel> getReportDate();

	public void insertReportHistory(@Param("reportRequest") ReportModel reportRequest);

	public List<Map<String, Object>> getReportByModule(@Param("role") String role);

	public List<ExportedReportHistoryModel> getDumpData(@Param("marketArea") String marketArea);

	public void getTableauReport();

	public int getEFiciencyIndexDump_V();

	public List<Map<String, Object>> getAllTableauReport();

	public List<Map<String, Object>> getAllTableauDataLive();


}
