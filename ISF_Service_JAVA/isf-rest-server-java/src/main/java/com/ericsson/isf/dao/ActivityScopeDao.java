/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import com.ericsson.isf.mapper.ProjectManagementMapper;
import com.ericsson.isf.model.ActivityScopeModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author eabhmoj
 */
@Repository
public class ActivityScopeDao {
     @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;
     public int saveActivityScope(ActivityScopeModel activityScopeModel)
     {
         ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
         return projectManagementMapper.saveActivityScope(activityScopeModel);
     }
     
   /* //TODO code cleanup
      public void DeleteActivityScope(int activityScopeId,String lastModifiedBy)
     {
          ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
         projectManagementMapper.DeleteActivityScope(activityScopeId,lastModifiedBy);
     }*/
     
     public void deleteActivityScopeByProjectScopeDetailId(int scopedetailId,String lastModifiedBy)
     {
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        projectManagementMapper.deleteActivityScopeByProjectScopeDetailId(scopedetailId,lastModifiedBy);
     }
     
     public boolean checkDTRCASubActivityId(Integer subActivityId) {
    	 ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
    	 return projectManagementMapper.checkDTRCASubActivityId(subActivityId);
     }
}
