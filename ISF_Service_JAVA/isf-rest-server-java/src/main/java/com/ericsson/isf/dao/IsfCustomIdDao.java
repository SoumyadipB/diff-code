package com.ericsson.isf.dao;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.IsfCustomIdMapper;
import com.ericsson.isf.model.InstanceModel;

@Repository
public class IsfCustomIdDao {

	@Qualifier("sqlSession")
	/* Create session from SQLSessionFactory */
	@Autowired
	private SqlSessionTemplate sqlSession;

	public InstanceModel getInstance(String databaseIp) {
		InstanceModel instanceModel = new InstanceModel();
		try {
			IsfCustomIdMapper isfCustomIdMapper = sqlSession.getMapper(IsfCustomIdMapper.class);
			instanceModel = isfCustomIdMapper.getInstance(databaseIp);

			if (instanceModel == null || StringUtils.isEmpty(instanceModel.getIpAddress())) {
				instanceModel=returnModel();
			}
		} catch (PersistenceException e) {
			instanceModel=returnModel();
		}
		return instanceModel;
	}

	private InstanceModel returnModel() {
		InstanceModel instanceModel = new InstanceModel();
		instanceModel.setInstanceId(1);
		instanceModel.setLocation("Noida");
		return instanceModel;
	}

}
