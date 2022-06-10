/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ericsson.isf.dao.ReportManagementDAO;
import com.ericsson.isf.model.DhashboardConfModel;
import com.ericsson.isf.model.ExportedReportHistoryModel;
import com.ericsson.isf.model.FilterModel;
import com.ericsson.isf.model.JsonModel;
import com.ericsson.isf.model.ReportModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.WorkPlanFullModel;
import com.ericsson.isf.util.AppConstants;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;


/**
 *
 * @author edhhklu
 */
@Service
public class ReportManagementService {

	@Autowired
	private ReportManagementDAO reportManagementDAO;
	
	private static final Logger LOG = LoggerFactory.getLogger(ReportManagementService.class);


	private static final String MODULE_SUBMODULE_INFO = "MODULE_SUBMODULE";
	private static final char CONFIG_COL_SEP = '<';
	private static final String CONFIG_TABLENAME = "tablename";
	private static final String CONFIG_COLS = "columns";
	private static final String RESPONSE_COL_NAME = "colName";
	private static final String RESPONSE_COL_VAL = "colValue";
	private static final String RESPONSE_COL_LABEL = "label";
	private static final String RESPONSE_COL_TYPE = "type";
	private static final String RESPONSE_COL_DATA = "data";
	private static final String RESPONSE_COL_CONFIG = "config";
	private static final String RESPONSE_COL_DESC = "description";
	private static final String DRILL_DOWN = "drillDown";
	private static final String DRILL_DOWN_DATA = "drillDownData";
	private static final String FILTER_ATTRIBUTE = ":filterAttribute";
	private static final String DETAIL_QUERY = ":detailQuery";
	private static final String SELECT_GROUP_BY = ":selectGroupBy";
	private static final String NEXT_LEVEL_GROUP_BY = "nextLevelGroupBy";
	private static final String DISTINCT = "DISTINCT";
	private static final String SELECT = "SELECT";
	private static final String SELECT_TOP_10 = "SELECT TOP 10";
	private static final String AND = " and ";
	private static final String WHERE = " WHERE ";
	private static final String MODULE_NAME = "moduleName";
	private static final String SUBMODULE_NAME = "subModuleName";
	private static final String REPORT_NAME = "reportName";
	private static final String REPORT_DESCRIPTION = "reportDescription";
	private static final String Report_INFO_IMG = "reportInfoImg";
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String ATTACHMENT_FILENAME = "attachment;filename=";
	private static final String DETAILED_REPORT = "DetailedReport.csv";
	private static final String CHART_REPORT = "ChartReport.csv";
	private static final String AS = " AS ";
	private static final String ALPHABET_PATTERN = ".*[^a-z].*";
	private static final String Y_AXIS_LABEL = "yAxisLabel";
	private static final String X_AXIS_LABEL = "xAxisLabel";
	private static final String CHART_TYPE = "chartType";
	private static final String SUM = "SUM";
	private static final String MIN = "MIN";
	private static final String NULL = "NULL";
	private static final String COUNT = "COUNT";
	private static final String AVERAGE = "AVG";
	private static final String MAX = "MAX";
	private static final String CONVERT = "CONVERT";
	private static final String CASE = "CASE";
	private static final String ISNULL = "ISNULL";
	private static final String XLSX = ".xlsx";
	private static final String EQUALS_SIGN=" = ";

	/**
	 * 
	 * @return List<ReportModel>
	 */
	public List<ReportModel> getReportList() {

		return this.reportManagementDAO.getReportList();
	}

	/**
	 * 
	 * @param reportName
	 * @param subModule
	 * @param module
	 * @return List<LinkedHashMap<String, Object>>
	 */
	public List<LinkedHashMap<String, Object>> searchReport(String reportName, String subModule, String module) {

		return this.reportManagementDAO.searchReport(reportName, subModule, module);
	}

	/**
	 * 
	 * @param reportName
	 * @param subModule
	 * @param module
	 * @return
	 */
	public ReportModel getReportDetail(String reportName, String subModule, String module) {

		ReportModel reportRequest = new ReportModel();
		reportRequest.setReportName(reportName);
		reportRequest.setSubModuleName(subModule);
		reportRequest.setModuleName(module);
		return this.reportManagementDAO.getReportDetail(reportRequest);
	}

	/**
	 * 
	 * @param report
	 * @return Map<String, List<Map<String, String>>>
	 */
	public Map<String, List<Map<String, String>>> getReportFilterData(ReportModel report) {

		ReportModel reportmodel = this.reportManagementDAO.getReportDetail(report);
		List<Map<String, Object>> reportData = reportManagementDAO.validateDetailedQuery(reportmodel);
		List<Map<String, String>> resultData = new ArrayList<>();
		if (StringUtils.isNotEmpty(reportmodel.getFilterAttribute())) {
			String[] allfilters = { "SubActivity", "ScopeName" };
			for (Map<String, Object> reportMap : reportData) {
				Map<String, String> row = new HashMap<String, String>();
				for (String filter : allfilters) {
					row.put(filter, String.valueOf(reportMap.get(filter)));
				}
				resultData.add(row);

			}
		}

		Map<String, List<Map<String, String>>> initData = new HashMap<>();
		initData.put(MODULE_SUBMODULE_INFO, resultData);
		return initData;
	}

	/**
	 * 
	 * @param reportModel
	 * @return boolean
	 */

	@Transactional("transactionManager")
	public boolean deleteReport(ReportModel reportModel) {

		return this.reportManagementDAO.deleteReport(reportModel);
	}

	/**
	 * 
	 * @param reportModel
	 * @return boolean
	 */
	@Transactional("transactionManager")
	public boolean addReport(ReportModel reportModel) {

		return this.reportManagementDAO.addReport(reportModel);
	}

	/**
	 * 
	 * @param reportModel
	 * @return boolean
	 */

	@Transactional("transactionManager")
	public boolean updateReport(ReportModel reportModel) {

		return this.reportManagementDAO.updateReport(reportModel);
	}

	/**
	 * 
	 * @param activeFlag
	 * @return Map<String, List<Map<String, Object>>>
	 */
	public Map<String, List<Map<String, Object>>> getReportInitData(String activeFlag) {

		Map<String, List<Map<String, Object>>> initData = new HashMap<>();
		initData.put(MODULE_SUBMODULE_INFO, reportManagementDAO.getAllModuleSubmodule(activeFlag));
		return initData;
	}

	/**
	 * 
	 * @param activeFlag
	 * @param role
	 * @return List<Object>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Object> getReportByModule(String activeFlag, String role) {

		List<Map<String, Object>> respMod = reportManagementDAO.getReportByModule(role);
		List<Object> masterResultList = new ArrayList<>();
		Map<String, List<Map<String, String>>> masterResultMap = new HashMap<>();
		Map<String, String> resultMap = new HashMap<>();
		List<Map<String, String>> resultList = new ArrayList<>();
		for (Map<String, Object> map : respMod) {

			String key = StringUtils.EMPTY;
			if (map.containsKey("id")) {
				key = (String) map.get(MODULE_NAME);
			}

			if (masterResultMap.containsKey(key)) {

				resultMap = getResultMap(resultMap, map);
				resultList = (ArrayList) masterResultMap.get(key);
				resultList.add(resultMap);
			} else {

				resultMap = getResultMap(resultMap, map);
				resultList.add(resultMap);
			}
			masterResultMap.put(key, resultList);

		}
		masterResultList.add(masterResultMap);

		return masterResultList;
	}

	/**
	 * 
	 * @param resultMap
	 * @param map
	 * @return Map<String, String>
	 */

	private Map<String, String> getResultMap(Map<String, String> resultMap, Map<String, Object> map) {

		resultMap.put(SUBMODULE_NAME, (String) map.get(SUBMODULE_NAME));
		resultMap.put(REPORT_NAME, (String) map.get(REPORT_NAME));
		resultMap.put(REPORT_DESCRIPTION, (String) map.get(REPORT_DESCRIPTION));
		resultMap.put(Report_INFO_IMG, (String) map.get(Report_INFO_IMG));
		return resultMap;
	}

	/**
	 * 
	 * @param marketArea
	 * @return List<ExportedReportHistoryModel>
	 */
	public List<ExportedReportHistoryModel> getDumpData(String marketArea) {

		return this.reportManagementDAO.getDumpData(marketArea);

	}

	/**
	 * 
	 * @param reportRequest
	 * @return Map<String, Object>
	 */

	public Map<String, Object> generateReport(ReportModel reportRequest) {

		ReportModel reportdetails = reportManagementDAO.getReportDetail(reportRequest);
		String query = reportdetails.getChartQuery().replace(SELECT_GROUP_BY, reportRequest.getSelectGroupBy())
				.replaceAll(DETAIL_QUERY, reportdetails.getDetailQuery());
		if (StringUtils.isNotEmpty(reportRequest.getFilterAttribute())) {
			query = query.replace(FILTER_ATTRIBUTE, reportRequest.getFilterAttribute());

		}
		reportdetails.setChartQuery(query);
		reportdetails.setSelectGroupBy(reportRequest.getSelectGroupBy());
		List<Map<String, Object>> data = this.reportManagementDAO.generateChartReport(reportdetails);
		if (CollectionUtils.isNotEmpty(data)) {

			this.reportManagementDAO.insertReportHistory(reportRequest);
		}

		Map<String, Object> finalData = getfinalData(reportRequest, reportdetails, data);
		return finalData;
	}

	/**
	 * 
	 * @param reportRequest
	 * @param reportdetails
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getfinalData(ReportModel reportRequest, ReportModel reportdetails,
			List<Map<String, Object>> data) {

		Map<String, Object> finalData = new HashMap<String, Object>();
		finalData.put(RESPONSE_COL_DATA, data);

		// drill down code.
		if (StringUtils.isNotEmpty(reportdetails.getChartTypeConfig())) {

			Map<String, Object> chartTypeConfig = new Gson().fromJson(reportdetails.getChartTypeConfig(),
					new TypeToken<HashMap<String, Object>>() {
			}.getType());
			Map<String, Map<String, String>> drillDownConfig = ((Map<String, Map<String, String>>) chartTypeConfig
					.get(DRILL_DOWN));
			if (StringUtils.isNotEmpty(reportdetails.getChartTypeConfig()) && chartTypeConfig.containsKey(DRILL_DOWN)
					&& drillDownConfig.get(NEXT_LEVEL_GROUP_BY).containsKey(reportRequest.getSelectGroupBy())) {

				Map<Object, Object> drillDownData = addDrillDownData(reportRequest, data,
						chartTypeConfig.get(DRILL_DOWN), drillDownConfig);
				finalData.put(DRILL_DOWN_DATA, drillDownData);
				finalData.put(DRILL_DOWN, chartTypeConfig.get(DRILL_DOWN));
			}
		}
		return finalData;
	}

	/**
	 * 
	 * @param reportRequest
	 * @param data
	 * @param chartTypeConfig
	 * @param drillDownConfig
	 * @return Map<Object, Object>
	 */

	private Map<Object, Object> addDrillDownData(ReportModel reportRequest, List<Map<String, Object>> data,
			Object chartTypeConfig, Map<String, Map<String, String>> drillDownConfig) {

		Map<Object, Object> drillDownData = new HashMap<Object, Object>();
		for (Map<String, Object> innerMap : data) {

			ReportModel reportdetails = reportManagementDAO.getReportDetail(reportRequest);
			String query = reportdetails.getChartQuery();
			query = query
					.replace(SELECT_GROUP_BY,
							drillDownConfig.get(NEXT_LEVEL_GROUP_BY).get(reportRequest.getSelectGroupBy()))
					.replaceAll(DETAIL_QUERY, reportdetails.getDetailQuery());
			if (StringUtils.isNotEmpty(reportRequest.getFilterAttribute())) {
				query = query.replace(FILTER_ATTRIBUTE, reportRequest.getFilterAttribute() + FILTER_ATTRIBUTE)
						.replace(FILTER_ATTRIBUTE, AND + reportRequest.getSelectGroupBy() + EQUALS_SIGN
								+ innerMap.get(reportRequest.getSelectGroupBy()));
			} else {
				query = query.replace(FILTER_ATTRIBUTE, WHERE + reportRequest.getSelectGroupBy()
				+ EQUALS_SIGN + innerMap.get(reportRequest.getSelectGroupBy()));

			}
			reportdetails.setChartQuery(query);
			reportdetails.setSelectGroupBy(reportRequest.getSelectGroupBy());
			drillDownData.put(innerMap.get(reportRequest.getSelectGroupBy()),
					reportManagementDAO.generateChartReport(reportdetails));
		}
		return drillDownData;

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

	public Map<String, Object> getDashboardData(String dashboardId, String sequence, String projectId, String fromDate,
			String toDate) {

		DhashboardConfModel dashboardConf = reportManagementDAO.getDhashboardReportId(dashboardId, sequence);
		ReportModel reportdetails = reportManagementDAO
				.getReportDetailsById(String.valueOf(dashboardConf.getReportId()));
		String query = reportdetails.getChartQuery();
		Map<String, Object> response = new HashMap<>();
		Map<String, String> config = new HashMap<>();
		getconfig(dashboardConf, reportdetails, config);
		String groupBy = StringUtils.EMPTY;
		if (StringUtils.isNotEmpty(reportdetails.getSelectGroupBy())) {
			groupBy = reportdetails.getSelectGroupBy().split(AppConstants.SEMICOLON)[0];
		}
		query = query.replace(SELECT_GROUP_BY, groupBy).replaceAll(DETAIL_QUERY, reportdetails.getDetailQuery());
		String dhashboardFilter = StringUtils.EMPTY;
		if (projectId != null) {
			dhashboardFilter = "where ProjectID=" + projectId;
		}
		if (StringUtils.isNotEmpty(fromDate) && StringUtils.isNotEmpty(toDate)) {
			dhashboardFilter += " and [date] between '" + fromDate + "' and '" + toDate + AppConstants.SINGLE_QUOTE;
		}
		query = query.replace(FILTER_ATTRIBUTE, dhashboardFilter);

		reportdetails.setChartQuery(query);
		reportdetails.setSelectGroupBy(groupBy);
		List<Map<String, Object>> data = this.reportManagementDAO.generateChartReport(reportdetails);
		response.put(RESPONSE_COL_DATA, data);
		response.put("conf", config);
		return response;
	}

	/**
	 * 
	 * @param dashboardConf
	 * @param reportdetails
	 * @param config
	 */
	private void getconfig(DhashboardConfModel dashboardConf, ReportModel reportdetails, Map<String, String> config) {

		config.put(Y_AXIS_LABEL, reportdetails.getyAxisLabel());
		config.put(X_AXIS_LABEL, reportdetails.getxAxisLabel());
		config.put(SELECT_GROUP_BY, reportdetails.getSelectGroupBy());
		config.put(CHART_TYPE, reportdetails.getChartType());
		config.put(RESPONSE_COL_DESC, dashboardConf.getWidgetName());
	}

	/**
	 * 
	 * @param reportRequest
	 * @return Map<String, List<Map<String, Object>>>
	 */

	public Map<String, List<Map<String, Object>>> previewReport(ReportModel reportRequest) {

		Map<String, List<Map<String, Object>>> initData = new HashMap<>();
		String query = reportRequest.getChartQuery().replace(SELECT_GROUP_BY, reportRequest.getSelectGroupBy())
				.replaceAll(AppConstants.SEMICOLON, AppConstants.CSV_CHAR_COMMA)
				.replaceAll(DETAIL_QUERY, reportRequest.getDetailQuery()).replace(FILTER_ATTRIBUTE, StringUtils.EMPTY);
		if (query.toUpperCase().contains(DISTINCT)) {
			query = query.toUpperCase().replace(DISTINCT, StringUtils.EMPTY);

		}
		query = query.toUpperCase().replace(SELECT, SELECT_TOP_10);
		reportRequest.setChartQuery(query);

		for (String selectGroupBy : reportRequest.getSelectGroupBy().split(AppConstants.SEMICOLON)) {
			reportRequest.setSelectGroupBy(selectGroupBy);
			initData.put(selectGroupBy, reportManagementDAO.generateChartReport(reportRequest));
		}
		return initData;
	}

	/**
	 * 
	 * @param reportRequest
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> validateDetailedQuery(ReportModel reportRequest) {

		String query = reportRequest.getDetailQuery();
		if (query.toUpperCase().contains(DISTINCT)) {
			query = query.toUpperCase().replace(DISTINCT, StringUtils.EMPTY);
		}
		query = query.toUpperCase().replace(SELECT, SELECT_TOP_10);
		reportRequest.setDetailQuery(query);
		return this.reportManagementDAO.validateDetailedQuery(reportRequest);
	}

	/**
	 * 
	 * @param reportName
	 * @param subModule
	 * @param module
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getReportFilters(String reportName, String subModule, String module) {

		ReportModel reportDetails = getReportDetail(reportName, subModule, module);

		String filterList = AppConstants.SINGLE_QUOTE
				+ reportDetails.getFilterAttribute().replaceAll(AppConstants.SEMICOLON, "','")
				+ AppConstants.SINGLE_QUOTE;
		List<FilterModel> filterGroups = reportManagementDAO.getReportFilters(filterList);
		String[] configuredFilters = reportDetails.getFilterAttribute().split(AppConstants.SEMICOLON);
		Map<String, Object> response = new LinkedHashMap<>();
		for (String configFilter : configuredFilters) {
			FilterModel fgConfig = null;

			// done to maintain the order of response see as done in configuration
			for (FilterModel filter : filterGroups) {
				if (filter.getFilterGroup().equals(configFilter)) {
					fgConfig = filter;
					break;
				}
			}

			getFilterAttributes(response, fgConfig);
		}
		return response;
	}

	/**
	 * 
	 * @param response
	 * @param fgConfig
	 */
	private void getFilterAttributes(Map<String, Object> response, FilterModel fgConfig) {

		String[] config = fgConfig.getFilterconfig().split(AppConstants.SEMICOLON);
		String tablename = null;
		String columns = null;
		String[] tokens = null;
		for (String token : config) {
			tokens = token.split(AppConstants.COLON);
			switch (tokens[0]) {
			case CONFIG_TABLENAME:
				tablename = tokens[1];
				break;
			case CONFIG_COLS:
				columns = tokens[1];
				break;
			}
		}
		String[] cols = columns.split(AppConstants.CSV_CHAR_COMMA);
		List<String> allCols = new ArrayList<>();
		List<Map<String, String>> confData = new ArrayList<>();
		for (String colConfig : cols) {

			tokens = colConfig.substring(colConfig.indexOf(CONFIG_COL_SEP) + 1, colConfig.length() - 1)
					.split(AppConstants.CSV_CHAR_PIPE);

			if (!"date".equals(tokens[2])) {
				// no need to fetch data from database for date
				allCols.add(tokens[0]);
				allCols.add(tokens[1]);
			}
			Map<String, String> cData = new LinkedHashMap<>();
			cData.put(RESPONSE_COL_NAME, tokens[0]);
			cData.put(RESPONSE_COL_VAL, tokens[1]);
			cData.put(RESPONSE_COL_LABEL, colConfig.substring(0, colConfig.indexOf(CONFIG_COL_SEP)));
			cData.put(RESPONSE_COL_TYPE, tokens[2]);
			confData.add(cData);

		}
		List<Map<String, Object>> filterData = null;
		if ((CollectionUtils.isNotEmpty(allCols))) {
			filterData = this.reportManagementDAO.getFilterData(tablename,
					allCols.toString().substring(1, allCols.toString().length() - 1));
		}
		Map<String, Object> filterGroup = new LinkedHashMap<>();
		filterGroup.put(RESPONSE_COL_DATA, filterData);
		filterGroup.put(RESPONSE_COL_CONFIG, confData);
		filterGroup.put(RESPONSE_COL_DESC, fgConfig.getFilterDescription());
		response.put(fgConfig.getFilterGroup(), filterGroup);
	}

	/**
	 * 
	 * @param result
	 * @return
	 * @throws IOException
	 */
	private Workbook generateXlsFile(List<Map<String, Object>> result) throws IOException {

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		getHeader(result, sheet);
		return workbook;
	}

	/**
	 * 
	 * @param result
	 * @param sheet
	 */
	private void getHeader(List<Map<String, Object>> result, Sheet sheet) {
		// write header
		Row row = sheet.createRow(0);
		int col = 0;
		for (String key : result.get(0).keySet()) {
			Cell cell = row.createCell(col++);
			cell.setCellValue(key);
		}
		// write data
		for (int i = 1; i <= result.size(); i++) {
			col = 0;
			row = sheet.createRow(i);
			for (String key : result.get(i - 1).keySet()) {
				Cell cell = row.createCell(col++);
				cell.setCellValue((result.get(i - 1).get(key) == null) ? (StringUtils.EMPTY)
						: (result.get(i - 1).get(key).toString()));
			}
		}
	}

	/**
	 * 
	 * @param response
	 * @param wbook
	 * @param filename
	 * @throws IOException
	 */
	private void writeWorkbookToResponse(HttpServletResponse response, Workbook wbook, String filename)
			throws IOException {

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + filename);
		OutputStream out = response.getOutputStream();
		wbook.write(out);
		out.flush();
		out.close();
	}

	/**
	 * 
	 * @param reportRequest
	 * @param response
	 * @throws IOException
	 */
	public void downloadReport(ReportModel reportRequest, HttpServletResponse response) throws IOException {

		response.setContentType(AppConstants.TEXT_PLAIN);
		response.setHeader(AppConstants.CONTENT_TYPE, AppConstants.APPLICATION_JSON);
		response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + DETAILED_REPORT);
		ReportModel reportDetials = reportManagementDAO.getReportDetail(reportRequest);
		reportDetials.setFilterAttribute(reportRequest.getFilterAttribute());
		List<Map<String, Object>> result = this.reportManagementDAO.generateDetailedReport(reportDetials);
		writeWorkbookToResponse(response, generateXlsFile(result),
				reportDetials.getReportName() + AppConstants.CHAR_HYPHEN
				+ (new SimpleDateFormat(AppConstants.SIMPLE_DATE_FORMAT).format(new Date())) + XLSX);
	}

	/**
	 * 
	 * @param reportRequest
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void downloadChartReport(ReportModel reportRequest, HttpServletResponse response) throws IOException {

		response.setContentType(AppConstants.TEXT_PLAIN);
		response.setHeader(AppConstants.CONTENT_TYPE, AppConstants.APPLICATION_JSON);
		response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + CHART_REPORT);
		Map<String, Object> result = generateReport(reportRequest);
		writeWorkbookToResponse(response, generateXlsFile((List<Map<String, Object>>) result.get(RESPONSE_COL_DATA)),
				reportRequest.getReportName() + AppConstants.CHAR_HYPHEN
				+ (new SimpleDateFormat(AppConstants.SIMPLE_DATE_FORMAT).format(new Date())) + XLSX);

	}

	/**
	 * 
	 * @param filterGroups
	 * @return List<FilterModel>
	 */

	public List<FilterModel> getFilterGroups(String filterGroups) {

		return reportManagementDAO.getReportFilters(filterGroups);
	}

	/**
	 * 
	 * @param reportModel
	 * @return List<String>
	 * @throws SQLException
	 */

	public List<String> getReportGroupBy(ReportModel reportModel) throws SQLException {

		String detailQuery = reportModel.getDetailQuery().toUpperCase();
		if (detailQuery.contains(DISTINCT)) {
			detailQuery = detailQuery.replace(DISTINCT, StringUtils.EMPTY);
		}
		detailQuery = detailQuery.replace(SELECT, "SELECT TOP 2 *").replace("\"SELECT", SELECT).trim();
		return reportManagementDAO.getReportGroupBy(detailQuery);
	}

	/**
	 * 
	 * @param query
	 * @param flag
	 * @return List<String>
	 */

	private List<String> getGroupByValues(String query, boolean flag) {

		List<String> group = new ArrayList<>();
		for (String value : query.split(AppConstants.CSV_CHAR_COMMA)) {
			if (value.toUpperCase().contains(SELECT)) {
				value = value.replaceAll("\\(select", StringUtils.EMPTY)
						.substring(value.indexOf(StringUtils.SPACE) + 1).trim();
			}
			if (value.toUpperCase().contains("SELECT DISTINCT") || value.toUpperCase().contains(DISTINCT)) {
				value = value.substring(value.indexOf(StringUtils.SPACE) + 1).trim();
			}
			if (!flag) {
				String[] newValue;
				if (value.toUpperCase().contains(AS)) {
					newValue = value.toUpperCase().split(AS);
					value = newValue[1];
				} else if (value.toUpperCase().contains(" AS")) {
					newValue = value.toUpperCase().split(" AS");
					value = newValue[1];
				} else if (value.matches(ALPHABET_PATTERN)) {
					newValue = value.split(AppConstants.EQUAL);
					value = newValue[0];
				} else if (value.matches(ALPHABET_PATTERN)) {
					newValue = value.split("= ");
					value = newValue[0];
				} else if (value.matches(ALPHABET_PATTERN)) {
					newValue = value.split(EQUALS_SIGN);
					value = newValue[0];
				}
				value = value.trim();
				group.add(value);
			} else {
				group.add(value);
			}
		}
		if (flag) {
			List<String> finalResult = new ArrayList<>();
			List<String> result = group.stream()
					.filter(val -> !val.toUpperCase().contains(SUM) && !val.toUpperCase().contains(MIN)
							&& !val.contains("--") && !val.contains("/*") && !val.contains("))")
							&& !val.toUpperCase().contains(NULL) && !val.toUpperCase().contains("YEAR(")
							&& !val.toUpperCase().contains("MONTH(") && !val.toUpperCase().contains("POSITIONENDDATE)")
							&& !val.toUpperCase().contains(MAX) && !val.toUpperCase().contains(AVERAGE)
							&& !val.toUpperCase().contains(COUNT) && !val.toUpperCase().contains(CONVERT)
							&& !val.toUpperCase().contains("FROM") && !val.toUpperCase().contains(CASE)
							&& !val.toUpperCase().contains(AppConstants.ASTERISK) && !val.toUpperCase().contains(ISNULL)
							&& !val.toUpperCase().contains("DATEPART") && !val.toUpperCase().contains("THEN"))
					.collect(Collectors.toList());
			for (String value : result) {
				String[] newValue;
				if (value.toUpperCase().contains(AS)) {
					newValue = value.toUpperCase().split(AS);
					value = newValue[1];
				} else if (value.matches(ALPHABET_PATTERN)) {
					newValue = value.split(AppConstants.EQUAL);
					value = newValue[0];
				}
				value = value.trim();
				finalResult.add(value);
			}
			return finalResult;
		} else {
			return group;
		}
	}

	/**
	 * 
	 * @param reportModel
	 * @return Map<String, Object>
	 */

	public Map<String, Object> getFilters(ReportModel reportModel) {

		List<String> filterList = new ArrayList<>();
		String detailQuery = reportModel.getDetailQuery();
		if (detailQuery != null) {
			String[] query = detailQuery.toUpperCase().split("FROM");
			if (!query[0].toUpperCase().contains("SELECT * ")) {
				query[0] = query[0].trim();
				filterList = getGroupByValues(query[0], true);
			} else {
				query[1] = query[1].trim();
				filterList = getGroupByValues(query[1], true);
			}
		}
		String filterValue = StringUtils.EMPTY;
		for (String value : filterList) {
			filterValue = filterValue + AppConstants.CSV_CHAR_COMMA + value;
		}
		filterValue = AppConstants.SINGLE_QUOTE + filterValue.replaceAll(AppConstants.CSV_CHAR_COMMA, "','") + "' ";
		filterValue = filterValue.substring(3, filterValue.length() - 1).replaceAll(AppConstants.SINGLE_QUOTE,
				StringUtils.EMPTY);

		String[] configuredFilters = filterValue.split(AppConstants.CSV_CHAR_COMMA);
		Map<String, Object> response = new LinkedHashMap<>();
		for (String configFilter : configuredFilters) {
			FilterModel fgConfig = null;
			List<FilterModel> filterGroups = reportManagementDAO.getReportDate();
			for (FilterModel filter : filterGroups) {
				if (StringUtils.isNotEmpty(filter.getFilterconfig())) {
					if (filter.getFilterconfig().toUpperCase().contains(configFilter)) {
						fgConfig = filter;
						break;
					}
				}
			}
			if (fgConfig != null) {
				getFilterAttributes(response, fgConfig);
			}
		}
		return response;
	}

	/**
	 * 
	 * @param reportRequest
	 * @return List<String>
	 * @throws SQLException
	 */

	public List<String> getChartFilters(ReportModel reportRequest) throws SQLException {

		String chartQuery = reportRequest.getChartQuery();
		if (chartQuery != null) {
			if (chartQuery.contains(DETAIL_QUERY)) {
				chartQuery = chartQuery.replace(DETAIL_QUERY, reportRequest.getDetailQuery().trim());
			}
			if (chartQuery.contains(SELECT_GROUP_BY)) {
				chartQuery = chartQuery.replace(SELECT_GROUP_BY,
						reportRequest.getSelectGroupBy().replace(AppConstants.SEMICOLON, AppConstants.CSV_CHAR_COMMA));
			}
			if (chartQuery.contains(FILTER_ATTRIBUTE)) {
				chartQuery = chartQuery.replace(FILTER_ATTRIBUTE, StringUtils.EMPTY);
			}
		}
		if (chartQuery.toUpperCase().contains(DISTINCT)) {
			chartQuery = chartQuery.toUpperCase().replace(DISTINCT, StringUtils.EMPTY);
		}
		chartQuery = chartQuery.toUpperCase().replace(SELECT, "SELECT TOP 2").trim();
		return reportManagementDAO.getChartFilters(chartQuery);

	}

	/**
	 * 
	 * @param reportRequest
	 * @return boolean
	 */

	public boolean validateReportFilters(ReportModel reportRequest) {

		return true;
	}

	/**
	 * 
	 * @return boolean
	 */

	public boolean getTableauReport() {

		boolean spResult = false;

		this.reportManagementDAO.getTableauReport();

		if (this.reportManagementDAO.getEFiciencyIndexDump_V() > 0) {

			spResult = true;
		}

		return spResult;
	}

	/**
	 * 
	 * @param signum
	 * @param marketArea
	 * @param role
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getDeliveryDashboardData(String signum, String marketArea, String role) {

		return this.reportManagementDAO.getDeliveryDashboardData(signum, marketArea, role);
	}

	/**
	 * 
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getAllTableauReport() {

		return this.reportManagementDAO.getAllTableauReport();
	}

	/**
	 * 
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getAllTableauDataLive() {

		return this.reportManagementDAO.getAllTableauDataLive();
	}

	public Response<Void> downloadJsonForReport()  {
		Response<Void> apiResponse= new Response<>();
		try {
			ResponseEntity<Object> response = null;

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();

			Base64.Encoder encoder = Base64.getEncoder();  

			String username="RxwUIfFWwCWufFhJjULywLbpfCka";
			String password="07lJbzfafWYps9npMWLbVvXxMbUa";

			String userpass = username + ':' + password;
			String encoded_u = encoder.encodeToString(userpass.getBytes());
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.setAccept(Collections.singletonList(MediaType.ALL));
			headers.add("Authorization", "Basic "+encoded_u);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("grant_type","client_credentials");

			String url = "https://emea-api.erisite.ericsson.net/token";

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
			response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
//			System.out.println(response.getBody().toString());
			JSONObject obj = new JSONObject(response.getBody().toString().replaceAll("=",":"));
			String bearerToken	=	obj.get("access_token").toString();			

			downloadWorkPlanFullStructure(bearerToken);


			//				response.getBody().toString().substring(beginIndex, endIndex)

			//			JSONParser parser = new JSONParser();
			//			Object obj = parser.parse(file.getBytes());
			//			String originalFileName=file.getOriginalFilename();
			//			JsonParser parse = new JsonParser();
			//			JsonObject object = (JsonObject) parse.parse(obj.toString());
			//			JsonArray data=object.getAsJsonArray("data");
			//			if(originalFileName.contains("sites_fullstructure")) {
			//				for(JsonElement ob:data) {
			//					ObjectMapper mapper= new ObjectMapper();
			//					SiteDataModel siteData= mapper.readValue(ob.toString(), SiteDataModel.class);
			//
			//					this.reportManagementDAO.insertSiteData(siteData);
			//
			//					//				System.out.println(siteData.getSiteStatus());
			//				}
			//			}
			//			if(originalFileName.contains("milestone_fullstructure")) {
			//				for(JsonElement ob:data) {
			//					ObjectMapper mapper= new ObjectMapper();
			//					SiteDataModel siteData= mapper.readValue(ob.toString(), SiteDataModel.class);
			//
			//					this.reportManagementDAO.insertSiteData(siteData);
			//
			//					//				System.out.println(siteData.getSiteStatus());
			//				}
			//			}


		} catch (Exception e) {
			e.printStackTrace();
			apiResponse.addFormError(e.getMessage());
			LOG.error(e.getMessage());
		}
		return apiResponse;
	}

	private void downloadWorkPlanFullStructure(String bearerToken) throws JsonParseException, JsonMappingException, IOException {

		ResponseEntity<String> response = null;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		String url = "https://emea-api.erisite.ericsson.net/ErisiteEMEA/1.0.0/api/workplans/fullstructure";
		headers.add("Authorization", "Bearer "+bearerToken);
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		//project ID's- 1004050
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("ProjectID", "1004050");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, String.class);
		System.out.println(response.getBody().toString());
		
		JSONObject myObject = new JSONObject(response.getBody());
		
//		JsonParser parse = new JsonParser();
//		JsonObject object = (JsonObject) parse.parse(response.getBody().toString().trim());
//		JsonArray data=object.getAsJsonArray("data");

		JSONArray data=myObject.getJSONArray("data");
		
		for(Object ob:data) {
			ObjectMapper mapper= new ObjectMapper();
			WorkPlanFullModel workPlanData= mapper.readValue(ob.toString(), WorkPlanFullModel.class);

//			this.reportManagementDAO.insertSiteData(siteData);

			System.out.println(workPlanData.getWorkPlanName());
		}
	}

}
