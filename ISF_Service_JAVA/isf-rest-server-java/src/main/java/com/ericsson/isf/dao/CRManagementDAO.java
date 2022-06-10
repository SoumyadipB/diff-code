/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.CRManagementMapper;
import com.ericsson.isf.model.CRManagementModel;
import com.ericsson.isf.model.CRManagementResultModel;
import com.ericsson.isf.model.ChangeRequestPositionNewModel;
import com.ericsson.isf.model.ProposedResourcesModel;
import com.ericsson.isf.model.RaiseCRMannagmentModel;
import com.ericsson.isf.model.WorkEffortModel;

/**
 *
 * @author esanpup
 */
@Repository
public class CRManagementDAO {

	@Qualifier("sqlSession")
	/*Create session from SQLSessionFactory */
	@Autowired
	private SqlSessionTemplate sqlSession;


	public List<CRManagementResultModel> getCRDetails() {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getCRDetails();
	}

	public ChangeRequestPositionNewModel getCRDetailsByID(int crID) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getCRDetailsByID(crID);
	}

	public CRManagementResultModel getCRDetailsByCRID(int cRID) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getCRDetailsByCRID(cRID);
	}

	public void acceptCRByID(int CRID, String signumID, String comment) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		changeManagementMapper.acceptCRByID(CRID,signumID,comment);
	}

	public void changeCrStatus(CRManagementModel cr) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		changeManagementMapper.changeCrStatus(cr);
	}

	public Map<String, Object> getRRIDFlag(int rRID){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getRRIDFlag(rRID);
	}

	public boolean deleteRpID(int rPID, String signum){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.deleteRpID(rPID,signum);
	}
	public boolean updateCrStatus(int crID,String signum){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.updateCrStatus(crID,signum);
	}

	public boolean raiseChangeManagment(RaiseCRMannagmentModel cRObject, Date startDate, Date endDate, Integer proposed, Integer existing, String status, String reason){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.raiseChangeManagment(cRObject, startDate, endDate, proposed, existing, status, reason);
	}

	public List<Map<String, Date>> getCrfromStartDate(RaiseCRMannagmentModel cRObject){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getCrfromStartDate(cRObject);
	}

	public Map<String,Integer> getWorkEffortDetailsFromRpid(int rPID){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getWorkEffortDetailsFromRpid(rPID);
	}

	public List<Map<String, Object>> getEndDatefromWorkEffort(RaiseCRMannagmentModel cRObject){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getEndDatefromWorkEffort(cRObject);
	}

	public Map<String, Integer> getLastInsertedID(RaiseCRMannagmentModel cRObject){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getLastInsertedID(cRObject);
	}

	public List<Map<String,Object>> checkRpID(int rpID){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.checkRpID(rpID);
	}

	public int checkOpenStatusCount(int rpID){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.checkOpenStatusCount(rpID);
	}


	public List<ProposedResourcesModel> getPositionsAndProposedResources(String status,String signum, String projectID, String marketArea){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getPositionsAndProposedResources(status,signum, projectID,marketArea);
	}

	public float getFtePercentage(int rPID){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getFtePercentage(rPID);
	}

	public Map<String, String> getRemoteOnsite(int rPID){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getRemoteOnsite(rPID);
	}

	public void rejectCRByID(int cRID, String signumID, String comment) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		changeManagementMapper.rejectCRByID(cRID,signumID,comment);
	}

	public String getExistingComments(int cRID) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getExistingComments(cRID);
	}

	public List<Map<String, Integer>> getWorkEffortID(int rPID, int status) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getWorkEffortID(rPID,status);
	}

	public String getAllocatedSignum(int rPID) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getAllocatedSignum(rPID);
	}

	public String checkSignumInWEffort(int cRID) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.checkSignumInWEffort(cRID);
	}

	public void updateWorkEffortDetails(Date stDate, Date edDate, Integer WorkEffortID, String signumID, int days, double totalHrs) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		changeManagementMapper.updateWorkEffortDetails(stDate,edDate,WorkEffortID,signumID, days, totalHrs);
	}

	public void updateCrStatusPreClosure(Integer integer) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		changeManagementMapper.updateCrStatusPreClosure(integer);
	}

	public void updateWorkEffortHours(Integer integer, double totalHrs) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		changeManagementMapper.updateWorkEffortHours(integer, totalHrs);
	}

	public void updateWorkEffortID(Integer integer) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		changeManagementMapper.updateWorkEffortID(integer);
	}


	public WorkEffortModel getWorkEffortByCRID(int cRID) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getWorkEffortByCRID(cRID);
	}

	public boolean insertBookedResource(int workEffortID, int resourcePositionID, String signum, Date targetDate, int projectID, float hours) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.insertBookedResource(workEffortID,resourcePositionID,signum,targetDate,projectID,hours);
	}

	public void disableBookedResourceByWEID(int workEffortID) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		changeManagementMapper.disableBookedResourceByWEID(workEffortID);
	}

	public void updateWorkEffortDates(Date startDa, Date endDa, Integer CRID) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		changeManagementMapper.updateWorkEffortDates(startDa,endDa,CRID);
	}

	public void insertInWorkEffort(WorkEffortModel we) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		changeManagementMapper.insertInWorkEffort(we);
	}

	public boolean updateWorkEffortStatusByWeId(boolean isActive, String weId) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.updateWorkEffortStatusByWeId(isActive, weId);
	}

	public boolean updateWorkEffortPositionStatusByWeId(String status, String weId) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.updateWorkEffortPositionStatusByWeId(status, weId);
	}

	public void updateRPDates(int rPID, String startDate, String endDate,int hours, String signum, String reason){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		changeManagementMapper.updateRPDates(rPID,startDate,endDate,hours,signum,reason);
	}

	public List<CRManagementResultModel> getCRDetails(String view, String signumID,String marketArea) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getCRDetails(view,signumID,marketArea);
	}

	public List<CRManagementResultModel> getCRDetailsPm(String view, String signumID,String marketArea) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getCRDetailsPm(view,signumID,marketArea);
	}
	public WorkEffortModel getWorkEffortByID(int weId) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getWorkEffortByID(weId);
	}

	public List<WorkEffortModel> getWorkEffortsByRpId(int weId) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getWorkEffortsByRpId(weId);
	}

	public WorkEffortModel getWorkEffortDetailsByID(int weId) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getWorkEffortDetailsByID(weId);
	}

	public void updateWorkEffortSignum(Integer workEffortID, String currentUser, String signum) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		changeManagementMapper.updateWorkEffortSignum(workEffortID,currentUser, signum);
	}
	public Integer getProjectIdbyWorkeffort(String workEffortID) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getProjectIdbyWorkeffort(workEffortID);
	}
	public List<WorkEffortModel> getFutureWorkEffortsBySignum(String signum) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getFutureWorkEffortsBySignum(signum);
	}

	public boolean changeBookedResourceStatusByWEIDAndDates(int workEffortID,Date startDate,Date endtDate,
			int status) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.changeBookedResourceStatusByWEIDAndDates(workEffortID,startDate,endtDate,status);
	}

	public Map<String, Object> getProposedWorkEfforts(int weid) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getProposedWorkEfforts(weid);
	}

	public Integer getProjectIdbyRpId(String rpid) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getProjectIdbyRpId(rpid);
	}


	public Map<String, Object>  getResourceRequestDetailsById( String rrId){

		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getResourceRequestDetailsById(rrId);

	}
	public List<Map<String,Object>> getReason(){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getReason();
	}

	public Integer getProjectIdbyRpId(int rPID) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getProjectIdbyRpId(rPID);

	}

	public List<Map<String, Object>> getResourcePositionDataByRpID(int rPID) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getResourcePositionDataByRpID(rPID);
	}

	public void updateResourceRequestCount(int rrID, String remoteOnsite) {
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		changeManagementMapper.updateResourceRequestCount(rrID, remoteOnsite);

	}
	
	public String getPositionStatus(int crID){
		CRManagementMapper changeManagementMapper = sqlSession.getMapper(CRManagementMapper.class);
		return changeManagementMapper.getPositionStatus(crID);
	}
}
