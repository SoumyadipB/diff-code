package com.ericsson.isf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.model.EventPublisherRequestModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.service.EventPublisherService;

@RestController
@RequestMapping("/eventPublisher")
public class EventPublisherController {

	@Autowired
	EventPublisherService eventPublisherService;

	@RequestMapping(value = "/sendRequestToExternalSources", method = RequestMethod.POST, produces = "application/json")
	public Response<ResponseEntity<String>> sendRequestToExternalSources(
			@RequestBody EventPublisherRequestModel eventPublisherModel){
		return eventPublisherService.sendRequestToExternalSources(eventPublisherModel);
	}

	@RequestMapping(value = "/callSiganlRClient", method = RequestMethod.POST, produces = "application/json")
	public Response<String> callSiganlRClient(@RequestBody EventPublisherRequestModel eventPublisherModel){

		return eventPublisherService.callSiganlRClient(eventPublisherModel);
	}

}
