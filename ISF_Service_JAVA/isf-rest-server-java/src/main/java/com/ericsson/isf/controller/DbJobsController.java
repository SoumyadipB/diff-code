package com.ericsson.isf.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.model.DbJobsModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.ServiceStartStopModel;
import com.ericsson.isf.service.AppService;
import com.ericsson.isf.service.DbJobsService;
import com.ericsson.isf.util.AppConstants;


@RestController
@RequestMapping("/dbJobs")
@Validated
public class DbJobsController {

	@Autowired
	private DbJobsService dbJobsService;

	@Autowired
	private AppService appService;

	@Autowired
	private ApplicationConfigurations configurations;
	
	/**
	 * Function Name: startJob Description: This Method used To Start Job.
	 * 
	 * @param serviceStartStopModel
	 * @author ekarmuj
	 * @throws IOException
	 * @throws SQLException 
	 */
   
	@RequestMapping(value = "/startJob", method = RequestMethod.POST)
	public Response<DbJobsModel> startJob(@RequestBody @Valid ServiceStartStopModel serviceStartStopModel,
			BindingResult bindingResult) throws IOException, SQLException {
		if (serviceStartStopModel.getQueryTimeout() == 0) {
			serviceStartStopModel.setQueryTimeout(Integer.parseInt(AppConstants.DEFAULT_TIME_OUT));
		}
		serviceStartStopModel.setQueryTimeout(serviceStartStopModel.getQueryTimeout());
		return appService.jdbcConnection(serviceStartStopModel);
		
	}

	/**
	 * Function Name: stopJob Description: This Method used To Stop Job.
	 * 
	 * @param serviceStartStopModel
	 * @author ekarmuj
	 * @throws SQLException 
	 * @throws IOException 
	 */
	
	@RequestMapping(value = "/stopJob", method = RequestMethod.POST)
	public Response<DbJobsModel> stopJob(@RequestBody @Valid ServiceStartStopModel serviceStartStopModel,
			BindingResult bindingResult) throws IOException, SQLException {

		if (serviceStartStopModel.getQueryTimeout() == 0) {
			serviceStartStopModel.setQueryTimeout(Integer.parseInt(AppConstants.DEFAULT_TIME_OUT));
		}
		serviceStartStopModel.setQueryTimeout(serviceStartStopModel.getQueryTimeout());
		return appService.jdbcConnectionStopJob(serviceStartStopModel);
	}

}
