package com.ericsson.isf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.model.ErisiteDataModel;
import com.ericsson.isf.model.EventPublisherRequestModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.service.ExternalInterfaceManagmentService;

@RestController
@RequestMapping("/externalInterface")
@Validated
public class ExternalInterfaceManagmentController {

	@Autowired
	private ExternalInterfaceManagmentService eriSiteManagmentService;

	/**
	 * @author ekarmuj Api name: externalInterface/checkIsfHealth
	 * @return healthcheck cloud api response
	 */

	@GetMapping("/checkIsfHealth")
	public String checkIsfHealth() {
		return this.eriSiteManagmentService.cloudHealthcheckResponse();
	}

	/**
	 * @author ekarmuj Api name : externalInterface/generateWOrkOrder
	 * @param erisiteDataModel
	 * @param headers
	 * @return boolean
	 */

	@PostMapping("/generateWOrkOrder")
	public boolean generateWOrkOrder(@RequestBody ErisiteDataModel erisiteDataModel,
			@RequestHeader HttpHeaders headers) {
		return this.eriSiteManagmentService.generateWOrkOrder(erisiteDataModel, headers);
	}

	@PostMapping(value = "/callErisiteFromSignalR")
	public Response<ResponseEntity<String>> callErisiteFromSignalR(
			@RequestBody EventPublisherRequestModel eventPublisherModel) {
		return eriSiteManagmentService.callErisiteFromSignalR(eventPublisherModel);
	}

}
