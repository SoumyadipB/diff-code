/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import com.ericsson.isf.mapper.ProjectManagementMapper;
import com.ericsson.isf.model.OpportunityModel;

import java.util.List;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author eabhmoj
 */
@Repository
public class OpportunityDao {
    
    
    @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;

  /* //TODO code cleanup 
      public List<OpportunityModel> getOpportunityDetails() {
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.getOpportunityDetails();
    }*/
}
