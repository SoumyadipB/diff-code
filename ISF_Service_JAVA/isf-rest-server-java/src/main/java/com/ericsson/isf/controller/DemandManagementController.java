/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.model.DemandRequestModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.service.DemandManagementService;
import com.ericsson.isf.util.AppConstants;

/**
 *
 * @author edhhklu
 */
@RestController
@RequestMapping("/demandManagement")
public class DemandManagementController {

	private static final Logger LOG = LoggerFactory.getLogger(DemandManagementController.class);

	@Autowired
	private DemandManagementService demandManagementService;

	@RequestMapping(value = "/saveDemandResourceRequest", method = RequestMethod.POST)
	public Response<Integer> saveDemandResourceRequest(@RequestBody DemandRequestModel saveDemandRequestModel) {
		LOG.info(AppConstants.SAVING_DEMAND_REQUEST);
		return demandManagementService.saveDemandResourceRequest(saveDemandRequestModel);

	}

	@RequestMapping(value = "/updateDemandResourceRequest", method = RequestMethod.POST)
	public Response<Integer> updateDemandResourceRequest(@RequestBody DemandRequestModel demandRequestModel) {
		LOG.info(AppConstants.SAVING_DEMAND_REQUEST);
		return demandManagementService.updateDemandResourceRequest(demandRequestModel);
	}

	@RequestMapping(value = "/getResourceRequests", method = RequestMethod.GET)
	public List<DemandRequestModel> getResourceRequests(
			@RequestParam(value = "projectId", required = true) int projectId,
			@RequestParam(value = "projectScopeDetailId", required = false) int projectScopeDetailId) {
		return demandManagementService.getResourceRequests(projectId, projectScopeDetailId);

	}
}
