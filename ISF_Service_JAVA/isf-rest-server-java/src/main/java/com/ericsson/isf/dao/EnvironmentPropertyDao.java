package com.ericsson.isf.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.EnvironmentPropertyMapper;
import com.ericsson.isf.model.EnvironmentPropertyModel;

/**
 *
 * @author eakinhm
 */

@Repository
public class EnvironmentPropertyDao {

	@Qualifier("sqlSession")
	/* Create session from SQLSessionFactory */
	@Autowired
	private SqlSessionTemplate sqlSession;

	public List<EnvironmentPropertyModel> getEnvironmentPropertyModelByKey(final String key) {
		EnvironmentPropertyMapper environmentPropertyMapper = sqlSession.getMapper(EnvironmentPropertyMapper.class);
		return environmentPropertyMapper.getEnvironmentPropertyModelByKey(key);
	}

}
