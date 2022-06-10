/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import com.ericsson.isf.dao.ActivityScopeDao;
import com.ericsson.isf.dao.ProjectScopeDetailDao;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.ericsson.isf.model.ProjectScopeDetailModel;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author eabhmoj
 */
@Service
public class ProjectScopeDetailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProjectScopeDetailService.class);
	
    @Autowired
    ProjectScopeDetailDao projectScopeDetailDao;

    @Autowired
    ActivityScopeDao activityScopeDao;
    
    @Transactional("transactionManager")
    public void saveProjectScopeDetail( ProjectScopeDetailModel projectScopeDetailModel){
         
        Boolean dataExists= this.projectScopeDetailDao.checkIfDataExists(projectScopeDetailModel.getProjectScopeID(),projectScopeDetailModel.getServiceAreaID(),projectScopeDetailModel.getDomainID(),projectScopeDetailModel.getTechnologyID());
        if(dataExists){
            throw new ApplicationException(500, "Data already exists for selected combination...!!! ");
        }
        else{
            this.projectScopeDetailDao.saveProjectScopeDetail(projectScopeDetailModel);
        }
    }
    @Transactional("transactionManager")
    public void DeleteProjectScopeDetail( int projectScopeDetailID,String lastModifiedBy){
        this.projectScopeDetailDao.DeleteProjectScopeDetail(projectScopeDetailID,lastModifiedBy);
        this.activityScopeDao.deleteActivityScopeByProjectScopeDetailId(projectScopeDetailID,lastModifiedBy);
    }
    @Transactional("transactionManager")
    public void deleteProjectScopeDetailByScopeId( int projectScopeId,String lastModifiedBy){
    	
    	List<ProjectScopeDetailModel> projectList = projectScopeDetailDao.getProjectScopeDetailsByScopeId(projectScopeId);
    	for(ProjectScopeDetailModel p:projectList){
    		projectScopeDetailDao.deleteProjectScopeDetailByProjectScopeId( p.getProjectScopeID(),lastModifiedBy);
    		activityScopeDao.deleteActivityScopeByProjectScopeDetailId(p.getProjectScopeDetailID(), lastModifiedBy);
    	}
    }
    public List<Map<String, Object>> getAllScopeDetailsByProject( String projectId){
    	return projectScopeDetailDao.getAllScopeDetailsByProject(projectId);
    }
    
    //TODO code cleanup
      public List<Map<String, Object>> getDemandOwningCompanies(){
    	return projectScopeDetailDao.getDemandOwningCompanies();
    }
    //TODO code cleanup
      public List<Map<String, Object>> getMarketAreas(){
    	return projectScopeDetailDao.getMarketAreas();
    }
    //TODO code cleanup
      public List<Map<String, Object>> getOpportunities(String marketArea){
    	return projectScopeDetailDao.getOpportunities(marketArea);
      }
     //TODO code cleanup
      public Map<String, Object> getOpportunityDetailsById(String opportunityID){
    	return projectScopeDetailDao.getOpportunityDetailsById(opportunityID);
    }
   /* //TODO code cleanup
    *  public String getDesktopVersion(String opportunityID, String signum){
    	LOG.warn("Desktop version for:"+signum);
    	return projectScopeDetailDao.getDesktopVersion(opportunityID);
    }*/
    
}
