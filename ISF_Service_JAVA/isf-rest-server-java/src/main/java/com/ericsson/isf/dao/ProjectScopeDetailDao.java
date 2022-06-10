/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import com.ericsson.isf.mapper.ProjectManagementMapper;
import com.ericsson.isf.model.ProjectScopeDetailModel;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author eabhmoj
 */
@Repository
public class ProjectScopeDetailDao {
    @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;
      public void saveProjectScopeDetail( ProjectScopeDetailModel projectScopeDetailModel){
           ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
           projectManagementMapper.saveProjectScopeDetail(projectScopeDetailModel);
      }
    public void DeleteProjectScopeDetail( int projectScopeDetailID,String lastModifiedBy){
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        projectManagementMapper.DeleteProjectScopeDetail(projectScopeDetailID,lastModifiedBy);
    }
    
    
    public void deleteProjectScopeDetailByProjectScopeId( int projectScopeId,String lastModifiedBy){
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        projectManagementMapper.deleteProjectScopeDetailByProjectScopeId(projectScopeId,lastModifiedBy);
    }
    
    public List<ProjectScopeDetailModel> getProjectScopeDetailsByScopeId( int projectScopeId){
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.getProjectScopeDetailsByScopeId(projectScopeId);
    }

    public Boolean checkIfDataExists(int projectScopeID,int serviceAreaID,int domainID, int technologyID) {
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.checkIfDataExists(projectScopeID,serviceAreaID,domainID,technologyID);
    }
    
    public List<Map<String, Object>> getAllScopeDetailsByProject(String projectId) {
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.getAllScopeDetailsByProject(projectId);
    }
    //TODO code cleanup
      public List<Map<String, Object>> getDemandOwningCompanies() {
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.getDemandOwningCompanies();
    }
    //TODO code cleanup
      public List<Map<String, Object>> getMarketAreas() {
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.getMarketAreas();
    }
     //TODO code cleanup
      public List<Map<String, Object>> getOpportunities(String marketArea) {
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.getOpportunities(marketArea);
      }
    //TODO code cleanup
     public Map<String, Object> getOpportunityDetailsById(String opportunityID) {
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.getOpportunityDetailsById(opportunityID);
    }
    
 /* //TODO code cleanup
  *    public String getDesktopVersion(String type) {
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.getDesktopVersion(type);
    }*/
   
}
