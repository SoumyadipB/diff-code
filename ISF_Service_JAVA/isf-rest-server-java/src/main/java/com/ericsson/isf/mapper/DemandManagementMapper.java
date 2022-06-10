/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.model.DemandForecastDetailModel;
import com.ericsson.isf.model.DemandRequestModel;


/**
 *
 * @author edhhklu
 */

public interface DemandManagementMapper {
	public Integer saveDemandResourceRequest(@Param("demandRequestModel")DemandRequestModel  saveDemandRequestModel);
	public void updateDemandResourceRequest(@Param("demandRequestModel")DemandRequestModel  saveDemandRequestModel);
	public void saveVendorsForDemandRequest(@Param("demandRequestModel")DemandRequestModel  saveDemandRequestModel);
	public void deleteVendorsForDemandRequest(@Param("demandRequestModel")DemandRequestModel  saveDemandRequestModel);
	public String[] getVendorsByRrid(@Param("resourceRequestId")int  resourceRequestId);
	public String[] getVendorNamesByRrid(@Param("resourceRequestId")int  resourceRequestId);
	public List<DemandRequestModel> getResourceRequests(@Param("projectId")int  projectId,@Param("projectScopeDetailId")int  projectScopeDetailId);
	
}
