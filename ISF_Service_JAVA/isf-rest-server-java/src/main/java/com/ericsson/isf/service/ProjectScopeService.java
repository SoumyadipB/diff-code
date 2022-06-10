/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.ExternalInterfaceManagmentDAO;
import com.ericsson.isf.dao.ProjectDAO;
import com.ericsson.isf.dao.ProjectScopeDao;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.ProjectAllDetailsModel;
import com.ericsson.isf.model.ProjectScopeModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.ScopeDomainProject;
import com.ericsson.isf.util.AppConstants;

/**
 *
 * @author eabhmoj
 */
@Service
public class ProjectScopeService {
	
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(WOExecutionService.class);
		
     @Autowired
    private ProjectScopeDao projectScopeDao;
     
     @Autowired
     private ProjectDAO projectDao;
     @Autowired
     private ActivityMasterDAO activityMasterDAO;
     
     @Autowired
     private ProjectScopeDetailService projectScopeDetailService;	

 	@Autowired
 	private ExternalInterfaceManagmentDAO erisiteManagmentDAO;
     
     /**
      * 
      * @param projectScopeModel
      * @return Response<String>
      */
    @Transactional("transactionManager")	
	public Response<String> saveScope(ProjectScopeModel projectScopeModel) {
    	  
    	Response<String> response = new Response<String>();
    	
    	ProjectScopeModel oldProjectScopeModel=this.projectScopeDao.isfProjectIdIfExists(projectScopeModel.getExternalReference(),
    			projectScopeModel.getExternalProjectId(),projectScopeModel.getExternalWorkplanTemplate());
    	if(oldProjectScopeModel!=null) {
    	if(oldProjectScopeModel.getProjectID()!=0) {
    		
    		ProjectAllDetailsModel projectDetails = this.projectDao.getProjectDetails(oldProjectScopeModel.getProjectID());
    	
    		EmployeeModel employee = activityMasterDAO.getEmployeeBySignum(oldProjectScopeModel.getCreatedBy());
    		response.addFormWarning("External Project " +projectScopeModel.getExternalProjectId()+", "+projectScopeModel.getExternalWorkplanTemplate()
    		+" and "+projectScopeModel.getExternalReference()+" are already mapped with project: "+ projectDetails.getProjectID() + " - "+ projectDetails.getProjectName() + 
    		" and owner of the Deliverable is "+ employee.getEmployeeName());
			response.setValidationFailed(true);
			return response;
    	}
    	}
    	
    	if(StringUtils.isNotEmpty(projectScopeModel.getExternalReference()) &&
    			projectScopeModel.getExternalProjectId()!=null &&
    			projectScopeModel.getExternalProjectId()!=0 && StringUtils.isNotEmpty(projectScopeModel.getExternalWorkplanTemplate())) {
    		
    		String uploadedBy = this.erisiteManagmentDAO.getExistingAssignToByProject(projectScopeModel.getProjectID(), Integer.parseInt(projectScopeModel.getSource()),
    				projectScopeModel.getExternalProjectId());
    		
    		this.projectDao.updateIsfProjectIdUploadedBy(projectScopeModel.getProjectID(),projectScopeModel.getExternalReference(),
    				projectScopeModel.getExternalProjectId(),projectScopeModel.getExternalWorkplanTemplate(),projectScopeModel.getSource(),
    				uploadedBy);

    		List<Integer> wPlanList=	this.projectDao.getWorkPlanIds(projectScopeModel.getProjectID(),projectScopeModel.getExternalReference(),
    				projectScopeModel.getExternalProjectId(),projectScopeModel.getExternalWorkplanTemplate(),projectScopeModel.getSource());
    		if(!wPlanList.isEmpty()) {
    			String wPlanIds=wPlanList.stream().map(String::valueOf)
  					  .collect(Collectors.joining(AppConstants.CSV_CHAR_COMMA, AppConstants.OPENING_BRACES, AppConstants.CLOSING_BRACES));
    			
    			this.projectDao.updateIsfProjectIdExternal(projectScopeModel.getProjectID(),projectScopeModel.getExternalReference(),
      				projectScopeModel.getExternalProjectId(),projectScopeModel.getExternalWorkplanTemplate(),projectScopeModel.getSource(), wPlanIds);
    		}
  
    	}
    	
		Boolean checkProjectScope = this.projectScopeDao.checkIfProjectScopeExists(projectScopeModel.getProjectID(),
				projectScopeModel.getScopeName(), projectScopeModel.getRequestType(), projectScopeModel.getStartDate(),
				projectScopeModel.getEndDate(),projectScopeModel.getProjectScopeID());
		Boolean checkScopeName = this.projectScopeDao.checkScopeName(projectScopeModel.getProjectID(),
				projectScopeModel.getScopeName(),projectScopeModel.getProjectScopeID());
		

		if (checkProjectScope) {
			response.addFormWarning("Deliverable already exists for the selected combination !!!");
			response.setValidationFailed(true);
			return response;
		} else if (checkScopeName) {
			response.addFormWarning("Deliverable name already exists for the selected project !!!");
			response.setValidationFailed(true);
			return response;
		} else {
			this.projectScopeDao.saveScope(projectScopeModel);
			response.addFormMessage("Successfully Saved Deliverable");
			response.setValidationFailed(false);
			return response;
		}
	}
    
     @Transactional("transactionManager")
    public void DeleteScope(int scopeId,String lastModifiedBy){
        
        projectScopeDao.DeleteScope(scopeId,lastModifiedBy);
        projectScopeDetailService.deleteProjectScopeDetailByScopeId(scopeId, lastModifiedBy);
         
     }
    
     public Map<String, Object> scopeByProject(Integer projectId, String deliverableStatus
    		 ,DataTableRequest dataTableRequest){
    	 
    	Map<String, Object> response = new HashMap<>();
   		try {
   			LOG.info("scopeByProject:Start");

    	 List<String> deliverableStatusList = new ArrayList<String>();
    	 if(StringUtils.isNotBlank(deliverableStatus)) {
        	 deliverableStatusList = Arrays.asList(deliverableStatus.trim().split("\\s*,\\s*"));
    	 }
    	 List<ProjectScopeModel> scopeByProjectList= projectScopeDao.scopeByProject(projectId,deliverableStatusList,dataTableRequest);
    	 
    	 
    	response.put(AppConstants.DATA_IN_RESPONSE, scopeByProjectList);
 		response.put(AppConstants.DRAW,dataTableRequest.getDraw());

 		if(!scopeByProjectList.isEmpty()) {
 			response.put(AppConstants.RECORD_TOTAL, scopeByProjectList.get(0).getTotalCounts());
 			response.put(AppConstants.RECORD_FILTERED, scopeByProjectList.get(0).getTotalCounts());			
 		} else {
 			response.put(AppConstants.RECORD_TOTAL, 0);
 			response.put(AppConstants.RECORD_FILTERED, 0);			
 		}
    	 
    	   LOG.info("scopeByProject:End");
	    } catch (ApplicationException appexe) {
		  response.put("errormsg", appexe.getMessage());
	    } catch (Exception ex) {
		  response.put("errormsg", ex.getMessage());
	    }
	   return response;

     }
     
     public List<ProjectScopeModel> scopeByProject(Integer projectId, String deliverableStatus){

    	 List<String> deliverableStatusList = new ArrayList<String>();
    	 if(StringUtils.isNotBlank(deliverableStatus)) {
        	 deliverableStatusList = Arrays.asList(deliverableStatus.trim().split("\\s*,\\s*"));
    	 }
    	 return projectScopeDao.scopeByProjectV1(projectId,deliverableStatusList);
     }
     
     

     /**
      * 
      * @param projectId
      * @return List<ProjectScopeModel>
      */
	public List<ProjectScopeModel> activeScopeByProject(Integer projectId) {
		List<ProjectScopeModel> projectScopeModel= projectScopeDao.activeScopeByProject(projectId);
		
//		if(CollectionUtils.isNotEmpty(projectScopeModel)) {
//			for(ProjectScopeModel p:projectScopeModel) {
//				
//				List<ScopeDomainProject> scopeDomainProject = projectScopeDao.getScopeDomainByProject(projectId, p.getProjectScopeID());
//				p.setDomainTechList(scopeDomainProject);
//			}
//		}
		return projectScopeModel;
	}
}
