/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.ericsson.isf.model.ExportedReportHistoryModel;
import com.ericsson.isf.model.FilterModel;
import com.ericsson.isf.model.ReportModel;
import com.ericsson.isf.service.ReportManagementService;

import java.sql.SQLException;

/**
 *
 * @author edhhklu
 */

@RestController
@RequestMapping("/reportManagement")
public class ReportManagementController {

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

	@Autowired
	private ReportManagementService reportManagementService;

	/**
	 * 
	 * @return List<ReportModel>
	 */

	@RequestMapping(value = "/getReportList", method = RequestMethod.GET)
	public List<ReportModel> getReportList() {

		return this.reportManagementService.getReportList();

	}

	/**
	 * 
	 * @param reportName
	 * @param subModule
	 * @param module
	 * @return List<LinkedHashMap<String, Object>>
	 */

	@RequestMapping(value = "/searchReport", method = RequestMethod.GET)
	public List<LinkedHashMap<String, Object>> searchReport(
			@RequestParam(value = "reportName", required = false) String reportName,
			@RequestParam(value = "subModule", required = false) String subModule,
			@RequestParam(value = "module", required = false) String module) {

		LOG.info("REFDATA.TBL_REPORTS: Success");

		return this.reportManagementService.searchReport(reportName, subModule, module);

	}

	/**
	 * 
	 * @param reportName
	 * @param subModule
	 * @param module
	 * @return ReportModel
	 */

	@RequestMapping(value = "/getReportDetail", method = RequestMethod.GET)
	public ReportModel getReportDetail(@RequestParam(value = "reportName", required = false) String reportName,
			@RequestParam(value = "subModule", required = false) String subModule,
			@RequestParam(value = "module", required = false) String module) {

		LOG.info("REFDATA.TBL_REPORTS: Success");
		return this.reportManagementService.getReportDetail(reportName, subModule, module);

	}

	/**
	 * 
	 * @param reportModel
	 * @return
	 */

	@RequestMapping(value = "/deleteReport", method = RequestMethod.POST)
	public boolean deleteReport(@RequestBody ReportModel reportModel) {

		return this.reportManagementService.deleteReport(reportModel);
	}

	/**
	 * 
	 * @param reportModel
	 * @return
	 */

	@RequestMapping(value = "/addReport", method = RequestMethod.POST)
	public boolean addReport(@RequestBody ReportModel reportModel) {

		return this.reportManagementService.addReport(reportModel);
	}

	/**
	 * 
	 * @param reportModel
	 * @return
	 */

	@RequestMapping(value = "/updateReport", method = RequestMethod.POST)
	public boolean updateReport(@RequestBody ReportModel reportModel) {

		return this.reportManagementService.updateReport(reportModel);
	}

	/**
	 * 
	 * @param reportRequest
	 * @return Map<String, Object>
	 */

	@RequestMapping(value = "/generateReport", method = RequestMethod.POST)
	public Map<String, Object> generateReport(@RequestBody ReportModel reportRequest) {

		return this.reportManagementService.generateReport(reportRequest);

	}

	/**
	 * 
	 * @param reportRequest
	 * @return Map<String, List<Map<String, Object>>>
	 */

	@RequestMapping(value = "/previewReport", method = RequestMethod.POST)
	public Map<String, List<Map<String, Object>>> previewReport(@RequestBody ReportModel reportRequest) {

		return this.reportManagementService.previewReport(reportRequest);
	}

	/**
	 * 
	 * @param active
	 * @return Map<String, List<Map<String, Object>>>
	 */

	@RequestMapping(value = "/getReportInitData", method = RequestMethod.GET)
	public Map<String, List<Map<String, Object>>> getReportInitData(
			@RequestParam(value = "active", required = false) String active) {

		return this.reportManagementService.getReportInitData(active);
	}

	/**
	 * 
	 * @param activeFlag
	 * @param role
	 * @return List<Object>
	 */

	@RequestMapping(value = "/getReportByModule", method = RequestMethod.GET)
	public List<Object> getReportByModule(String activeFlag, @RequestHeader("Role") String role) {

		return this.reportManagementService.getReportByModule(activeFlag, role);
	}

	/**
	 * 
	 * @param marketArea
	 * @return List<ExportedReportHistoryModel>
	 */
	@RequestMapping(value = "/getDumpData", method = RequestMethod.GET)
	public List<ExportedReportHistoryModel> getDumpData(@RequestHeader("MarketArea") String marketArea) {

		return this.reportManagementService.getDumpData(marketArea);
	}

	/**
	 * 
	 * @param reportRequest
	 * @return List<Map<String, Object>>
	 */

	@RequestMapping(value = "/validateDetailedQuery", method = RequestMethod.POST)
	public List<Map<String, Object>> validateDetailedQuery(@RequestBody ReportModel reportRequest) {

		return this.reportManagementService.validateDetailedQuery(reportRequest);

	}

	/**
	 * 
	 * @param response
	 * @param reportRequest
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadReport", method = RequestMethod.GET)
	public void downloadReport(HttpServletResponse response, @ModelAttribute ReportModel reportRequest)
			throws Exception {

		this.reportManagementService.downloadReport(reportRequest, response);
	}

	/**
	 * 
	 * @param response
	 * @param reportmodel
	 * @throws Exception
	 */

	@RequestMapping(value = "/downloadChartReport", method = RequestMethod.GET)
	public void downloadChartReport(HttpServletResponse response, @ModelAttribute ReportModel reportmodel)
			throws Exception {

		this.reportManagementService.downloadChartReport(reportmodel, response);
	}

	/**
	 * 
	 * @param reportName
	 * @param subModule
	 * @param module
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "/getReportFilters", method = RequestMethod.GET)
	public Map<String, Object> getReportFilters(@RequestParam(value = "reportName", required = true) String reportName,
			@RequestParam(value = "subModuleName", required = true) String subModule,
			@RequestParam(value = "moduleName", required = true) String module) {

		return this.reportManagementService.getReportFilters(reportName, subModule, module);

	}

	/**
	 * 
	 * @param dashboardId
	 * @param sequence
	 * @param projectId
	 * @param fromDate
	 * @param toDate
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "/getDashboardData", method = RequestMethod.GET)
	public Map<String, Object> getDashboardData(
			@RequestParam(value = "dashboardId", required = true) String dashboardId,
			@RequestParam(value = "sequence", required = false) String sequence,
			@RequestParam(value = "projectId", required = false) String projectId,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate) {
		return this.reportManagementService.getDashboardData(dashboardId, sequence, projectId, fromDate, toDate);

	}

	/**
	 * 
	 * @param filterGroups
	 * @return List<FilterModel>
	 */
	@RequestMapping(value = "/getFilterGroups", method = RequestMethod.GET)
	public List<FilterModel> getFilterGroups(
			@RequestParam(value = "filterGroups", required = false) String filterGroups) {

		return this.reportManagementService.getFilterGroups(filterGroups);

	}

	/**
	 * 
	 * @param reportModel
	 * @return
	 * @throws SQLException
	 */

	@RequestMapping(value = "/getReportGroupBy", method = RequestMethod.POST)
	public List<String> getReportGroupBy(@RequestBody ReportModel reportModel) throws SQLException {

		return this.reportManagementService.getReportGroupBy(reportModel);
	}

	/**
	 * 
	 * @param reportModel
	 * @return Map<String, Object>
	 */

	@RequestMapping(value = "/getFilters", method = RequestMethod.POST)
	public Map<String, Object> getFilters(@RequestBody ReportModel reportModel) {

		return this.reportManagementService.getFilters(reportModel);
	}

	/**
	 * 
	 * @param reportRequest
	 * @return List<String>
	 * @throws SQLException
	 */
	@RequestMapping(value = "/getChartFilters", method = RequestMethod.POST)
	public List<String> getChartFilters(@RequestBody ReportModel reportRequest) throws SQLException {

		return this.reportManagementService.getChartFilters(reportRequest);
	}

	/**
	 * 
	 * @param reportRequest
	 * @return
	 */
	@RequestMapping(value = "/validateReportFilters", method = RequestMethod.POST)
	public boolean validateReportFilters(@RequestBody ReportModel reportRequest) {

		return this.reportManagementService.validateReportFilters(reportRequest);

	}

	/**
	 * 
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping(value = "/getAllTableauReport", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllTableauReport() {

		return this.reportManagementService.getAllTableauReport();

	}

	/**
	 * 
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping(value = "/getAllTableauDataLive", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllTableauDataLive() {

		return this.reportManagementService.getAllTableauDataLive();

	}

	/**
	 * 
	 * @param signum
	 * @param marketArea
	 * @param role
	 * @return List<Map<String,Object>>
	 */
	@RequestMapping(value = "/getDeliveryDashboardData", method = RequestMethod.GET)
	public List<Map<String, Object>> getDeliveryDashboardData(
			@RequestParam(value = "signum", required = false) String signum,
			@RequestParam(value = "marketArea", required = false) String marketArea,
			@RequestParam(value = "role", required = false) String role) {

		LOG.info("REFDATA.TBL_REPORTS: Success");
		return this.reportManagementService.getDeliveryDashboardData(signum, marketArea, role);

	}

}
