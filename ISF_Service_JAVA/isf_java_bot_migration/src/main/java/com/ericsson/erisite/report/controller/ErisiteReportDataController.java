package com.ericsson.erisite.report.controller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.erisite.report.model.Response;
import com.ericsson.erisite.report.service.ErisiteReportDataService;

@RestController
public class ErisiteReportDataController {

	@Autowired
	private ErisiteReportDataService erisiteReportDataService;
	
	@RequestMapping(value = "/downloadJsonForReport", method = RequestMethod.POST)
	public Response<Void> downloadJsonForReport() throws URISyntaxException {

		return erisiteReportDataService.downloadJsonForReport();
	}
	
}
