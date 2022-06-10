/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.restGraph.dao;

import java.util.Map;

import org.hibernate.SessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.restGraph.mapper.RestGraphMailMapper;
import com.ericsson.isf.restGraph.model.TokenResponse;


/**
 *
 * @author eakinhm
 */
@Repository
public class RestGraphMailDAO {
    
    @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;

	@Autowired
	SessionFactory sessionFactory;
    
	public boolean saveRefreshToken(TokenResponse tokenResponse) {
		RestGraphMailMapper restGraphMailMapper = sqlSession.getMapper(RestGraphMailMapper.class);
        return restGraphMailMapper.saveRefreshToken(tokenResponse);
		
	}

	public Map<String, Object> getActiveRefreshToken() {
		RestGraphMailMapper restGraphMailMapper = sqlSession.getMapper(RestGraphMailMapper.class);
        return restGraphMailMapper.getActiveRefreshToken();
	}

	public void inactivateRefreshTokenByID(int id) {
		RestGraphMailMapper restGraphMailMapper = sqlSession.getMapper(RestGraphMailMapper.class);
        restGraphMailMapper.inactivateRefreshTokenByID(id);
	}

	
}
