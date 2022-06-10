/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import java.util.Date;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.DemandForecastMapper;

import com.ericsson.isf.model.DemandForecastDetailModel;
import com.ericsson.isf.model.DemandForecastFulfillmentModel;
import com.ericsson.isf.model.DemandForecastModel;
import com.ericsson.isf.model.ProjectScopeDetailMappingModel;
import com.ericsson.isf.model.ResourceRequestWorkEffortsModel;
import com.ericsson.isf.model.VendorTechModel;

/**
 *
 * @author edhhklu
 */
@Repository
public class DemandForecastDAO {
    
    @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;
    
    public void removeDupilcateDemandSummaryDraft(DemandForecastModel demandSummarySaveRequest){
    	DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        demandForecastMapper.removeDupilcateDemandSummaryDraft(demandSummarySaveRequest);
    }
    
    public void saveDemandSummaryDraft(DemandForecastModel  demandSummarySaveRequest) {
       DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
       demandForecastMapper.saveDemandSummaryDraft(demandSummarySaveRequest);
    }
    
    public void updateDemandSummaryDraft(DemandForecastModel  demandSummarySaveRequest) {
        DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        demandForecastMapper.updateDemandSummaryDraft(demandSummarySaveRequest);
     }
    
    public List<DemandForecastDetailModel> getDemandForecastDetails(String signum,Integer projectId,Date startDate,Integer pageSize,String role) {
        DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getDemandForecastDetails(signum,projectId,startDate,pageSize,role);
    }
    
    public List<DemandForecastDetailModel> getAllDemandForecastDetailsByRole(Integer projectId,String role) {
        DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getAllDemandForecastDetailsByRole(projectId,role.trim());
    }
    
    
    
    public List<Map<String,Object>> getToolDetails(Integer positionId){
        DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getToolDetails(positionId);
    }
    
 /*  //TODO Code cleanup
  *   public List<DemandForecastDetailModel> getDemandForecastDetailsForMigration(String signum,Integer projectId,Date startDate,Integer pageSize,String role) {
        DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getDemandForecastDetailsForMigration(signum,projectId,startDate,pageSize,role);
    }*/
    
    public List<DemandForecastFulfillmentModel> getDemandPositionDetailsByprojectId(Integer projectId) {
        DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getDemandPositionDetailsByprojectId(projectId);
    }
    
    
    public List<Map<String, Object>> getDemandSummary(String signum,Date startDate,Integer pageSize,String uniqueDates,String role,String marketArea) {
        DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getDemandSummary(signum,startDate,pageSize,uniqueDates,role,marketArea);
    }
    
    public String getUniqueDatesForForcast(String signum) {
        DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getUniqueDatesForForcast(signum);
    }

	public int saveDemandForecastDetails(DemandForecastDetailModel data, Integer projectid, String role, String signum,
			String operation) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
	    return demandForecastMapper.saveDemandForecastDetails(data, projectid, role, signum, operation);
	
	}

	public void sendToFM(Integer resourceRequestID) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        demandForecastMapper.sendToFM(resourceRequestID);
	}

	public List<DemandForecastFulfillmentModel> getDemandForecastDetailsByfilter(Integer projectID, String positionStatus, String marketArea) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
		return demandForecastMapper.getDemandForecastDetailsByfilter(projectID, positionStatus, marketArea);
	}

	public void updateDemandForecastDetails(DemandForecastDetailModel data, Integer projectid, String role,
			String signum, String operation) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
	     demandForecastMapper.updateDemandForecastDetails(data, projectid, role, signum, operation);
		
	}

	public void saveResourceRequestWorkEfforts(ResourceRequestWorkEffortsModel resourceRequestWorkEffort) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
	     demandForecastMapper.saveResourceRequestWorkEfforts(resourceRequestWorkEffort);
		
	}
	public void updateResourceRequestWorkEfforts(ResourceRequestWorkEffortsModel resourceRequestWorkEffort) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
	     demandForecastMapper.updateResourceRequestWorkEfforts(resourceRequestWorkEffort);
		
	}
	
	

	public void saveDemandVendorMapping(Integer positionId, String vendor) {
		// TODO Auto-generated method stub
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
	     demandForecastMapper.saveDemandVendorMapping(positionId, vendor);
	}
	
	public void deleteDemandVendorMapping(Integer positionId) {
		// TODO Auto-generated method stub
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
		demandForecastMapper.deleteDemandVendorMapping(positionId);

	}
	
	public void saveDemandToolMapping(Integer positionId, Integer toolId) {
		// TODO Auto-generated method stub
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
	     demandForecastMapper.saveDemandToolMapping(positionId, toolId);
	}
	
	public void deleteDemandToolMapping(Integer positionId) {
		// TODO Auto-generated method stub
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
		demandForecastMapper.deleteDemandToolMapping(positionId);

	}
	
	public void deleteOthersUsersDrafts(String signum,Integer projectId) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
		demandForecastMapper.deleteOthersUsersDrafts(signum,projectId);

	}
	
	public boolean isDraftAllowed(Integer projectId, String role) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
		return (demandForecastMapper.isDraftAllowed(projectId,role)==0);

	}
	
	
	public List<Integer> getProjectScopeDetail(DemandForecastDetailModel data) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
	     return demandForecastMapper.getProjectScopeDetail(data);
	
	}

	public DemandForecastDetailModel getDemandForecastDetailsByRRID(Integer rrID) {
		// TODO Auto-generated method stub
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getDemandForecastDetailsByRRID(rrID);
	}

	/*public String getVendorNameByID(String vendor) {
		// TODO Auto-generated method stub
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getVendorNameByID(vendor);
	}*/
	
	public List<Map<String, Object>> getDomainSubdomain(@Param("projectScopeID") String projectScopeID,@Param("serviceAreaID") String serviceAreaID){
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getDomainSubdomain(projectScopeID,serviceAreaID);
	}
	public List<Map<String, Object>> getTechnologies(@Param("projectScopeID") String projectScopeID,@Param("serviceAreaID") String serviceAreaID,@Param("domainID") String domainID){
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getTechnologies(projectScopeID,serviceAreaID,domainID);
	}

	public List<Map<String, Object>> getScopeDetails(String projectId){
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getScopeDetails(projectId);
	}
	public List<ProjectScopeDetailMappingModel> getAllScopeDetailsByProject(String projectId){
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getAllScopeDetailsByProject(projectId);
	}
	
	public List<Map<String, Object>> getDemandSummaryByRole(int projectId,String role){
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getDemandSummaryByRole(projectId,role);
	}

	public void deleteResourcePositionByRrID(Integer positionId, String signum) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        demandForecastMapper.deleteResourcePositionByRrID(positionId,signum);		
	}

	public void deleteWorkEffortsByRrID(Integer positionId, String signum) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        demandForecastMapper.deleteWorkEffortsByRrID(positionId,signum);		
		
	}

	public List<Map<String, Object>> getResourceRequestedBySubScope(int projectID, int projectScopeDetailID) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getResourceRequestedBySubScope(projectID,projectScopeDetailID);		
	}

	public int getDemandSummaryDraftDetails(DemandForecastModel demandSummarySaveRequest) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getDemandSummaryDraftDetails(demandSummarySaveRequest);
		
	}

	public DemandForecastDetailModel getDemandForecastDetailsByRrIdAndPId(Integer positionId, Integer projectId) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getDemandForecastDetailsByRrIdAndPId(positionId,projectId);
	}

	public List<VendorTechModel> getVendorTechCombination(int start, int length, String term) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        return demandForecastMapper.getVendorTechCombination(start, length, term);
	}

	public void saveDemandVendorTechCombination(List<VendorTechModel> vendorTechDataModel) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
        demandForecastMapper.saveDemandVendorTechCombination(vendorTechDataModel);
		
	}

	public boolean validateVendortech(String vendortechname) {
		DemandForecastMapper demandForecastMapper = sqlSession.getMapper(DemandForecastMapper.class);
		return demandForecastMapper.validateVendortech(vendortechname);
	}
    
}
