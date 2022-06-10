package com.ericsson.isf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.isf.dao.EnvironmentPropertyDao;
import com.ericsson.isf.model.EnvironmentPropertyModel;

@Service
public class EnvironmentPropertyService {

	@Autowired
	private EnvironmentPropertyDao environmentPropertyDao;

	/**
	 * 
	 * @param key
	 * @return List<EnvironmentPropertyModel>
	 */
	
	public List<EnvironmentPropertyModel> getEnvironmentPropertyModelByKey(final String key) {

		return environmentPropertyDao.getEnvironmentPropertyModelByKey(key);
	}

}
