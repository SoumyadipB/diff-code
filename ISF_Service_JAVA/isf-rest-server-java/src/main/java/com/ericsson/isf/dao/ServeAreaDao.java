/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import com.ericsson.isf.mapper.ActivityMasterMapper;
import com.ericsson.isf.model.ServeAreaModel;

import java.util.HashMap;
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
public class ServeAreaDao {
    @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;
      public List<ServeAreaModel> getServeArea(Integer projectID) {
      ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
      return activityMasterMapper.getServeArea(projectID);
    }
    
      public List<HashMap<String,Object>> getServiceAreaDetailsByProject(Integer projectID) {
          ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
          return activityMasterMapper.getServiceAreaDetailsByProject(projectID);
        }

	public List<HashMap<String, Object>> getCustomerDetailsByMA(Integer countryID, Integer marketAreaID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
        return activityMasterMapper.getCustomerDetailsByMA(countryID,marketAreaID);
	}
        
    
}
