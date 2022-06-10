package com.ericsson.isf.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.ericsson.isf.dao.DbJobsDao;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.DbJobsModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.ServiceStartStopModel;

@Service
public class DbJobsService {

	@Autowired
	private DbJobsDao dbJobsDao;

	/**
	 * 
	 * @param serviceStartStopModel
	 * @param bindingResult
	 * @return Response<DbJobsModel>
	 */
	
	public Response<DbJobsModel> startJob(ServiceStartStopModel serviceStartStopModel, BindingResult bindingResult) {

		Response<DbJobsModel> apiResponse = new Response<DbJobsModel>();
		try {

			if (bindingResult.hasErrors()) {
				apiResponse.addFormError(bindingResult.getFieldError().getDefaultMessage());
			} else {

				List<DbJobsModel> model = dbJobsDao.startJob(serviceStartStopModel.getProcedureName());
				if(CollectionUtils.isNotEmpty(model)) {
					if(model.get(0).getErrorCode()==1) {
						apiResponse.setResponseData(model.get(0));
					}
					else {
						apiResponse.addFormError(model.get(0).toString());
					}
				}
			}

			return apiResponse;
		} catch (ApplicationException e) {
			apiResponse.addFormError(e.getMessage());
			return apiResponse;
		} catch (Exception e) {
			apiResponse.addFormError(e.getMessage());
			return apiResponse;
		}

	}

	/**
	 * 
	 * @param serviceStartStopModel
	 * @param bindingResult
	 * @return Response<DbJobsModel>
	 */
	
	public Response<DbJobsModel> stopJob(ServiceStartStopModel serviceStartStopModel, BindingResult bindingResult) {

		Response<DbJobsModel> apiResponse = new Response<DbJobsModel>();
		try {
			
			if (bindingResult.hasErrors()) {
				apiResponse.addFormError(bindingResult.getFieldError().getDefaultMessage());
			} else {
				
				List<DbJobsModel> model = dbJobsDao.stopJob(serviceStartStopModel.getProcedureName());
				if(CollectionUtils.isNotEmpty(model)) {
					if(model.get(0).getErrorCode()==1) {
						apiResponse.setResponseData(model.get(0));
					}
					else {
						apiResponse.addFormError(model.get(0).toString());
					}
				}
			}
			return apiResponse;
		} catch (ApplicationException e) {
			apiResponse.addFormError(e.getMessage());
			return apiResponse;
		} catch (Exception e) {
			apiResponse.addFormError(e.getMessage());
			return apiResponse;
		}

	}
}
