/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.ProjectManagementMapper;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.ProjectScopeModel;
import com.ericsson.isf.model.ScopeDomainProject;

/**
 *
 * @author eabhmoj
 */
@Repository
public class ProjectScopeDao {
     @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;
     
     public void saveScope(ProjectScopeModel projectScopeModel) {
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        projectManagementMapper.saveScope(projectScopeModel);
    }
     
       public void DeleteScope(int scopeId,String lastModifiedBy) {
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        projectManagementMapper.DeleteScope(scopeId,lastModifiedBy);
    }

    public Boolean checkIfProjectScopeExists(int projectID, String scopeName, String requestType, Date startDate, Date endDate, int projectScopeId) {
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.checkIfProjectScopeExists(projectID,scopeName,requestType,startDate,endDate,projectScopeId);
    }
    
    public List<Integer> checkParamNotExist(ProjectScopeModel projectScopeModel) {
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.checkParamNotExist(projectScopeModel);
  	}
    
    public Boolean checkScopeName(int projectID, String scopeName, int projectScopeId) {
        ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.checkScopeName(projectID,scopeName,projectScopeId);
    }
    
    public List<ProjectScopeModel> scopeByProject(Integer projectId, List<String> deliverableStatusList,DataTableRequest dataTableRequest){
    	
    	String status = "";
    	List<String> statusList = new ArrayList<String>();
    	if(deliverableStatusList.size() != 0) {
	    	for (String a  : deliverableStatusList) {
	    		a = a.replaceAll( "'", "''");
	    		statusList.add(a);
	    	}
	    	status=String.join("','",statusList );
	    	status = "'"+status+"'"; 
    	}
    	ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.scopeByProject(projectId,status,dataTableRequest);
    }
    
	public List<ProjectScopeModel> scopeByProjectV1(Integer projectId, List<String> deliverableStatusList){
	    	
	    	String status = "";
	    	List<String> statusList = new ArrayList<String>();
	    	if(deliverableStatusList.size() != 0) {
		    	for (String a  : deliverableStatusList) {
		    		a = a.replaceAll( "'", "''");
		    		statusList.add(a);
		    	}
		    	status=String.join("','",statusList );
		    	status = "'"+status+"'"; 
	    	}
	    	ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
	        return projectManagementMapper.scopeByProjectV1(projectId,status);
	    }
    
    public List<Map<String,Object>> getActivtiySubActivityByScope(Integer projectScopeId){
    	ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.getActivtiySubActivityByScope(projectScopeId);
    }
    
    public List<ProjectScopeModel> getProjectScopeByScopeId(Integer projectScopeId){
    	ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.getProjectScopeByScopeId(projectScopeId);
    }

	public Boolean checkifProjectScopeErisite(String sourceId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.checkifProjectScopeErisite(sourceId);
	}

	public int getNumberOfWorkOrders(String activityName) {
	
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getNumberOfWorkOrders(activityName);
	}

	public List<Integer> getexecutionId(List<Integer> listOfScopeIds) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getexecutionId(listOfScopeIds);
	}

	public List<ProjectScopeModel> activeScopeByProject(Integer projectId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.activeScopeByProject(projectId);
	}

	public List<ScopeDomainProject> getScopeDomainByProject(Integer projectId, int projectScopeID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getScopeDomainByProject(projectId, projectScopeID);
	}

	public Boolean checkIfProjectScopeExistsExceptName(int projectID, int projectScopeId) {
		 ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
	        return projectManagementMapper.checkIfProjectScopeExistsExceptName(projectID,projectScopeId);
	}

	public ProjectScopeModel isfProjectIdIfExists(String externalReference, Integer externalProjectId,
			String externalWorkplanTemplate) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.isfProjectIdIfExists(externalReference,externalProjectId,externalWorkplanTemplate);
	}

	public ProjectScopeModel chechIfScopeAlreadyMapped(ProjectScopeModel projectScopeModel, String status) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
        return projectManagementMapper.chechIfScopeAlreadyMapped(projectScopeModel, status);
	}
}
