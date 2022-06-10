/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.CRManagementMapper;
import com.ericsson.isf.mapper.DemandManagementMapper;
import com.ericsson.isf.model.DemandForecastDetailModel;
import com.ericsson.isf.model.DemandRequestModel;

/**
 *
 * @author esanpup
 */
@Repository
public class DemandManagementDAO {

	@Qualifier("sqlSession")
	/* Create session from SQLSessionFactory */
	@Autowired
	private SqlSessionTemplate sqlSession;

	public Integer saveDemandResourceRequest(DemandRequestModel saveDemandRequestModel) {
		DemandManagementMapper demandManagementMapper = sqlSession.getMapper(DemandManagementMapper.class);
		return demandManagementMapper.saveDemandResourceRequest(saveDemandRequestModel);
	}

	public void updateDemandResourceRequest(DemandRequestModel saveDemandRequestModel) {
		DemandManagementMapper demandManagementMapper = sqlSession.getMapper(DemandManagementMapper.class);
		demandManagementMapper.updateDemandResourceRequest(saveDemandRequestModel);
	}

	public void saveVendorsForDemandRequest(DemandRequestModel saveDemandRequestModel) {
		DemandManagementMapper demandManagementMapper = sqlSession.getMapper(DemandManagementMapper.class);
		demandManagementMapper.saveVendorsForDemandRequest(saveDemandRequestModel);
	}

	public void deleteVendorsForDemandRequest(DemandRequestModel saveDemandRequestModel) {
		DemandManagementMapper demandManagementMapper = sqlSession.getMapper(DemandManagementMapper.class);
		demandManagementMapper.deleteVendorsForDemandRequest(saveDemandRequestModel);
	}

	public List<DemandRequestModel> getResourceRequests(int projectId, int projectScopeDetailId) {
		DemandManagementMapper demandManagementMapper = sqlSession.getMapper(DemandManagementMapper.class);
		return demandManagementMapper.getResourceRequests(projectId, projectScopeDetailId);
	}

	public String[] getVendorsByRrid(int resourceRequestId) {
		DemandManagementMapper demandManagementMapper = sqlSession.getMapper(DemandManagementMapper.class);
		return demandManagementMapper.getVendorsByRrid(resourceRequestId);
	}

	public String[] getVendorNamesByRrid(int resourceRequestId) {
		DemandManagementMapper demandManagementMapper = sqlSession.getMapper(DemandManagementMapper.class);
		return demandManagementMapper.getVendorNamesByRrid(resourceRequestId);
	}

}
