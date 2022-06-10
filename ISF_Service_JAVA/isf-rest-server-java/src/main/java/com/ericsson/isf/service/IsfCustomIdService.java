package com.ericsson.isf.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.isf.dao.IsfCustomIdDao;
import com.ericsson.isf.model.InstanceModel;

@Service
public class IsfCustomIdService {

	@Autowired
	private IsfCustomIdDao isfCustomIdDao;
	
	public InstanceModel getInstance(String databaseIp) {

		return isfCustomIdDao.getInstance(databaseIp);
		
	}

}
