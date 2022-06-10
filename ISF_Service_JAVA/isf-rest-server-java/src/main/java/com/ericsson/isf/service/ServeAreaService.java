/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import com.ericsson.isf.dao.ServeAreaDao;
import com.ericsson.isf.model.ServeAreaModel;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author eabhmoj
 */
@Service
public class ServeAreaService {

	@Autowired
	ServeAreaDao serveAreaDao;

	/**
	 * 
	 * @param projectID
	 * @return List<ServeAreaModel>
	 */
	public List<ServeAreaModel> getServeArea(Integer projectID) {

		return serveAreaDao.getServeArea(projectID);
	}

	/**
	 * 
	 * @param projectID
	 * @return List<HashMap<String, Object>>
	 */
	public List<HashMap<String, Object>> getServiceAreaDetailsByProject(Integer projectID) {

		return serveAreaDao.getServiceAreaDetailsByProject(projectID);
	}

	/**
	 * 
	 * @param countryID
	 * @param marketAreaID
	 * @return List<HashMap<String, Object>>
	 */
	public List<HashMap<String, Object>> getCustomerDetailsByMA(Integer countryID, Integer marketAreaID) {

		return serveAreaDao.getCustomerDetailsByMA(countryID, marketAreaID);
	}
}
