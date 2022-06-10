/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.isf.dao.DemandManagementDAO;
import com.ericsson.isf.model.DemandRequestModel;
import com.ericsson.isf.model.Response;

/**
 *
 * @author edhhklu
 */
@Service
public class DemandManagementService {

	@Autowired
	private DemandManagementDAO demandManagementDao;
	
	@Autowired
	private DemandForecastService demandForecastService;

	/**
	 * 
	 * @param saveDemandRequestModel
	 * @return Response<Integer>
	 */
	@Transactional("transactionManager")
	public Response<Integer> saveDemandResourceRequest(DemandRequestModel saveDemandRequestModel) {
		Response<Integer> response = new Response<Integer>();
		Integer resourceRequestId = demandManagementDao.saveDemandResourceRequest(saveDemandRequestModel);

		response.setResponseData(resourceRequestId);
		if (ArrayUtils.isNotEmpty(saveDemandRequestModel.getVendor())) {
			saveDemandRequestModel.setResourceRequestId(resourceRequestId);
			demandManagementDao.saveVendorsForDemandRequest(saveDemandRequestModel);
		}
		return response;
	}

	/**
	 * 
	 * @param demandRequestModel
	 * @return Response<Integer>
	 */
	@Transactional("transactionManager")
	public Response<Integer> updateDemandResourceRequest(DemandRequestModel demandRequestModel) {

		Response<Integer> response = new Response<>();
		demandManagementDao.updateDemandResourceRequest(demandRequestModel);
		demandManagementDao.deleteVendorsForDemandRequest(demandRequestModel);

		demandForecastService.saveDemandVendorTechCombination(demandRequestModel.getVendorTechModel(), demandRequestModel.getResourceRequestId(),
				demandRequestModel.getCreatedBy());
		return response;
	}

	/**
	 * 
	 * @param projectId
	 * @param projectScopeDetailId
	 * @return List<DemandRequestModel>
	 */
	public List<DemandRequestModel> getResourceRequests(int projectId, int projectScopeDetailId) {
		List<DemandRequestModel> resourceRequestList = demandManagementDao.getResourceRequests(projectId, projectScopeDetailId);
		for (DemandRequestModel resourceRequest : resourceRequestList) {
			
			resourceRequest.setVendor(demandManagementDao.getVendorsByRrid(resourceRequest.getResourceRequestId()));
		}

		return resourceRequestList;
	}

}
