package com.ericsson.isf.util;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.model.InstanceModel;
import com.ericsson.isf.service.IsfCustomIdService;

/**
 * This class used to Create Id with concatenation of instanceId and systemId.
 * 
 * @author ekarmuj
 *
 */
@Component
public class IsfCustomIdInsert {
	@Autowired
	private IsfCustomIdService isfCustomIdService;

	@Autowired
	private ApplicationConfigurations configurations;

	public int generateCustomId(int systemId) {

		InstanceModel instance = getInstanceModel();
		return  Integer.parseInt(instance.getInstanceId()+StringUtils.EMPTY+systemId);

	}
	public Long generateCustomId(long systemId) {

		InstanceModel instance = getInstanceModel();
		return  Long.parseLong(instance.getInstanceId()+StringUtils.EMPTY+systemId);

	}
	
	public InstanceModel getInstanceModel() {

		String databaseIp = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNIP);
		InstanceModel instance = isfCustomIdService.getInstance(databaseIp);
		return instance;
	}
}
