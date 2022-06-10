package com.ericsson.isf.restGraph.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.model.Response;
import com.ericsson.isf.restGraph.model.TokenRequest;
import com.ericsson.isf.restGraph.service.RestGraphMailService;

/**
 * 
 * @author eakinhm
 *
 */

@RestController
@RequestMapping("/restGraphMailController")
public class RestGraphMailController {

	private static final Logger LOG = LoggerFactory.getLogger(RestGraphMailController.class);

	@Autowired
	private RestGraphMailService restGraphMailService;

	@RequestMapping(value = "/generateRefreshToken", method = RequestMethod.POST)
	public Response<Void> generateRefreshToken(@RequestBody TokenRequest tokenRequest) {
		LOG.info("Generating Token!");
		return restGraphMailService.generateRefreshToken(tokenRequest);
	}
	
	@RequestMapping(value = "/readMailbox", method = RequestMethod.POST)
	public void readMailbox() {
		LOG.info("Generating Token!");
		restGraphMailService.readMailbox();
	}

}
