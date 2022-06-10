package com.ericsson.isf.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.DbJobsMapper;
import com.ericsson.isf.model.DbJobsModel;
import com.ericsson.isf.model.ServiceStartStopModel;

@Repository
public class DbJobsDao {

	@Qualifier("sqlSession")
	/* Create session from SQLSessionFactory */
	@Autowired
	private SqlSessionTemplate sqlSession;

	public List<DbJobsModel> startJob(String procedureName) {
		DbJobsMapper dbJobsMapper = sqlSession.getMapper(DbJobsMapper.class);
		return dbJobsMapper.startJob(procedureName);
	}

	public List<DbJobsModel> stopJob(String procedureName) {
		DbJobsMapper dbJobsMapper = sqlSession.getMapper(DbJobsMapper.class);
		return dbJobsMapper.stopJob(procedureName);
	}
}
