/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.ReportManagementMapper;
import com.ericsson.isf.model.DhashboardConfModel;
import com.ericsson.isf.model.ExportedReportHistoryModel;
import com.ericsson.isf.model.FilterModel;
import com.ericsson.isf.model.ReportModel;
import com.ericsson.isf.service.JdbcConnectionFactory;

/**
 *
 * @author edhhklu
 */
@Repository
public class ReportManagementDAO {

	@Qualifier("sqlSession")
	/* Create session from SQLSessionFactory */
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Autowired
	private JdbcConnectionFactory jdbcConnectionFactory;

	public List<ReportModel> getReportList() {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);

		List<ReportModel> result = reportManagementMapper.getReportList();

		return result;
	}

	public boolean addReport(ReportModel reportModel) {

		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);

		Boolean check = reportManagementMapper.checkIFModuleSubModuleExists(reportModel);
		if (check == false) {
			@SuppressWarnings("unused")
			int addResult = reportManagementMapper.addModuleSubModule(reportModel);

		}
		int rows = reportManagementMapper.addReport(reportModel);
		if (rows == 0)
			return false;
		else
			return true;
	}

	public boolean deleteReport(ReportModel reportModel) {

		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		int rows = reportManagementMapper.deleteReport(reportModel);
		if (rows == 0)
			return false;
		else
			return true;
	}

	public boolean updateReport(ReportModel reportModel) {

		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		int rows = reportManagementMapper.updateReport(reportModel);
		if (rows == 0)
			return false;
		else
			return true;
	}

	public List<LinkedHashMap<String, Object>> searchReport(String reportName, String subModule, String module) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);

		List<LinkedHashMap<String, Object>> result = reportManagementMapper.searchReport(reportName, subModule, module);

		return result;
	}

	public ReportModel getReportDetail(ReportModel reportRequest) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.getReportDetail(reportRequest);
	}

	public List<Map<String, Object>> getDeliveryDashboardData(String signum, String marketArea, String role) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.getDeliveryDashboardData(signum, marketArea, role);
	}

	public List<Map<String, Object>> getAllModuleSubmodule(String activeFlag) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		List<Map<String, Object>> result = reportManagementMapper.getAllModuleSubmodule(activeFlag);
		return result;
	}

	public List<Map<String, Object>> getReportByModule(String role) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		List<Map<String, Object>> result = reportManagementMapper.getReportByModule(role);
		return result;
	}

	public List<ExportedReportHistoryModel> getDumpData(String marketArea) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.getDumpData(marketArea);
	}

	public List<Map<String, Object>> generateReport(ReportModel reportRequest) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.generateReport(reportRequest);
	}

	public List<Map<String, Object>> generateDetailedReport(ReportModel reportRequest) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.generateDetailedReport(reportRequest);
	}

	public List<Map<String, Object>> getFilterData(String tablename, String columns) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.getFilterData(tablename, columns);
	}

	public List<Map<String, Object>> validateDetailedQuery(ReportModel reportRequest) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.validateDetailedQuery(reportRequest);
	}

	public List<String> getReportGroupBy(String detailQuery) throws SQLException {
		Connection conn = jdbcConnectionFactory.getConnection();

		Statement st = conn.createStatement();
		st = conn.createStatement();
		ResultSet rs = st.executeQuery(detailQuery);
		ResultSetMetaData rsMetaData = rs.getMetaData();

		int numberOfColumns = rsMetaData.getColumnCount();

		List<String> colNames = new ArrayList<>();
		for (int i = 1; i <= numberOfColumns; i++) {

			// get the column's name.

			colNames.add(rsMetaData.getColumnName(i));
		}
		return colNames;
	}

	public List<String> getChartFilters(String chartQuery) throws SQLException {
		Connection conn = jdbcConnectionFactory.getConnection();

		Statement st = conn.createStatement();
		st = conn.createStatement();
		ResultSet rs = st.executeQuery(chartQuery);
		ResultSetMetaData rsMetaData = rs.getMetaData();

		int numberOfColumns = rsMetaData.getColumnCount();

		List<String> colNames = new ArrayList<>();
		for (int i = 1; i <= numberOfColumns; i++) {

			colNames.add(rsMetaData.getColumnName(i));
		}
		return colNames;
	}

	public List<Map<String, Object>> getReportFilters() {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.getReportFilters();
	}

	public List<Map<String, Object>> generateChartReport(ReportModel reportRequest) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.generateChartReport(reportRequest);
	}

	public List<FilterModel> getReportFilters(String filterGroup) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.getFilters(filterGroup);
	}

	public List<FilterModel> getReportDate() {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.getReportDate();
	}

	public DhashboardConfModel getDhashboardReportId(String dhashboardId, String sequence) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.getDhashboardDetailsById(dhashboardId, sequence);
	}

	public ReportModel getReportDetailsById(String reportId) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.getReportDetailsById(reportId);
	}

	public void insertReportHistory(ReportModel reportRequest) {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		reportManagementMapper.insertReportHistory(reportRequest);
	}

	public void getTableauReport() {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		reportManagementMapper.getTableauReport();

	}

	public int getEFiciencyIndexDump_V() {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.getEFiciencyIndexDump_V();
	}

	public List<Map<String, Object>> getAllTableauReport() {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.getAllTableauReport();
	}

	public List<Map<String, Object>> getAllTableauDataLive() {
		ReportManagementMapper reportManagementMapper = sqlSession.getMapper(ReportManagementMapper.class);
		return reportManagementMapper.getAllTableauDataLive();
	}

}
