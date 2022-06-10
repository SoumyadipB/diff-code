/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;


import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.ericsson.isf.model.ChangeRequestPositionModel;
import com.ericsson.isf.model.CompetenceSubModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.LeavePlanModel;
import com.ericsson.isf.model.ModifyDemandModel;
import com.ericsson.isf.model.ResoucePositionFmModel;
import com.ericsson.isf.model.ResourceCalandarModel;
import com.ericsson.isf.model.ResourceEngagementModel;
import com.ericsson.isf.model.ResourceModel;
import com.ericsson.isf.model.ResourcePositionModel;
import com.ericsson.isf.model.ResourcePositionWorkEffortModel;
import com.ericsson.isf.model.ResourceRequestModel;
import com.ericsson.isf.model.SearchResourceByFilterModel;


/**
 *
 * @author eshalku
 */
public interface ResourceRequestMapper {
    
    public List<HashMap<String, Object>> searchResourcesByFilter(@Param("searchResourceByFilterModel") SearchResourceByFilterModel searchResourceByFilterModel);
    public List<HashMap<String, Object>> searchResourcesByFilters(@Param("searchResourceByFilterModel") SearchResourceByFilterModel searchResourceByFilterModel);

    public List<HashMap<String, Object>> searchResourcesByEmployeeManager(@Param("searchResourceByFilterModel") SearchResourceByFilterModel searchResourceByFilterModel);
                                                                                               
    public List<LinkedHashMap<String, Object>> getResourceRequestsByFilter(@Param("ProjectID") Integer ProjectID,@Param("DomainID") Integer DomainID,@Param("SubDomainID") Integer SubDomainID,@Param("SubServiceAreaID") Integer SubServiceAreaID,@Param("TechnologyID") Integer TechnologyID,@Param("PositionStatus") String PositionStatus,@Param("AllocatedResource") String AllocatedResource,@Param("spoc")  String spoc,@Param("marketArea") String marketArea);
    	
    public List<ResoucePositionFmModel> getPositionsAndAllocatedResources(@Param("RRID") Integer rrID,@Param("positionStatus") String positionStatus,@Param("spoc") String spoc,
    														@Param("marketArea") String marketArea,@Param("ProjectID")  Integer projectID);
    
    public List<ResoucePositionFmModel> getAllPositions(@Param("RRID") Integer rrID,@Param("positionStatus") String positionStatus,@Param("spoc") String spoc,
			@Param("marketArea") String marketArea,@Param("ProjectID")  Integer projectID);
    
    
    public List<ResourceRequestModel> getDemandRequestDetail(@Param("RRID") int rrID);
    public List<ResourcePositionWorkEffortModel> getResourceRequestWEffort(@Param("RRID") int rrID);
    public List<ResourcePositionWorkEffortModel> getResourceRequestWEffortDetails(@Param("RRID") int rrID);
    
    /* //TODO code cleanup
     * public int changePositionStatus(@Param("resourcePositionModel") ResourcePositionModel resourcePositionModel);*/

    /* //TODO code cleanup
     * public List<ResourcePositionModel> searchDemandPositions(@Param("domainID") Integer domainID,@Param("serviceAreaID") Integer SubServiceAreaID,@Param("technologyID") Integer technologyID,@Param("positionStatus") String positionStatus,@Param("resourceSignum") String resourceSignum);*/
   
   /* //TODO Code Cleanup
    *  public List<HashMap<String, Object>> SearchDemandRequests(@Param("ProjectScopeID") Integer ProjectScopeID,@Param("DomainID") Integer DomainID,@Param("SubServiceAreaID") Integer SubServiceAreaID,@Param("TechnologyID") Integer TechnologyID);*/
   /* //TODO code cleanup
    *  public int CreateResourceReplacement(@Param("changeRequestPositionModel") ChangeRequestPositionModel changeRequestPositionModel);*/
    public boolean checkIfResourceExists(@Param("changeRequestPositionModel") ChangeRequestPositionModel changeRequestPositionModel);
    
    public int modifyWorkEffort(@Param("resourceModel") ResourceModel resourceModel);
    //public int modifyDemandRequest(@Param("resourceModel") ResourceModel resourceModel);
    
   /* //TODO code cleanup
    *  public List<HashMap<String, Object>> searchChangeRequests(@Param("projectID") Integer domainID,@Param("changeRequestType") String changeRequestType,@Param("positionStatus") String positionStatus,@Param("resourceSignum") String resourceSignum);*/
    
    public List<CompetenceSubModel> getCompetenceSubDetails(@Param("RRID") Integer rrID);
    
    public List<HashMap<String, Object>> getCompetenceLevel(@Param("competenceId") Integer competenceId);
    public List<HashMap<String, Object>> getResourcePositionSubList(@Param("rpefModel")ResourcePositionWorkEffortModel rpef,@Param("RRID") int rrID);
    public List<HashMap<String,Object>> getCertificateSubDetails(@Param("RRID") Integer rrID);


	public int modifyDemandRequest(@Param("resourceRequestID") int resourceRequestID, @Param("jobRoleID") int jobRoleID, @Param("jobStageID") int jobStageID, @Param("remoteCount")int remoteCount, @Param("onsiteCount") int onsiteCount,
			@Param("requestType") String requestType,@Param("resourceType") String resourceType);
    
	/*//TODO code cleanup
	 * public int modifyDemandPositionRP(@Param("inputMap") ModifyDemandModel inputMap);*/
	/* //TODO code cleanup
	 * public int modifyDemandPositionPSD(@Param("inputMap") ModifyDemandModel inputMap);*/
	/*//TODO code cleanup
	 * public int modifyDemandPositionWE(@Param("inputMap") ModifyDemandModel inputMap);*/
	/* //TODO code cleanup
	 * public int modifyDemandPositionCRP(@Param("inputMap") ModifyDemandModel inputMap);*/
	public List<Map<String, Object>> getBookedResourceBySignum(@Param("signum") String signum,@Param("startDate") Date startDate,@Param("endDate") Date endtDate);
	/* //TODO code cleanup
	 * public List<Map<String, Object>> getAllCertifications(@Param("issuer") String issuer);*/
	/*  //TODO code cleanup
	 * public List<Map<String, Object>> getJobRoles();*/
	public List<Map<String, Object>> getJobStages();
	/* //TODO code cleanup
	 * public List<Map<String, Object>> getOnsiteLocations();*/
	public List<Map<String, Object>> getBacklogWorkOrders(@Param("signum") String signum);
	public Map<String, Object> getDomainDetailsByPorjectScopeId(@Param("projectScopeDetailID") String projectScopeDetailID );

	public List<ResourceCalandarModel> getResourceCalander(@Param("signum") String signum,
			@Param("startdate") String startdate, @Param("enddate") String enddate);
	public List<ResourceEngagementModel> getWOResourceLevelDetails(@Param("signumID") String signumID, @Param("signumForProject") List<String> signumForProject ,@Param("projectID") Integer projectID,@Param("startDate") String startDate,
			@Param("endDate") String endDate);
	public List<Map<String, Object>> getAvgLoeForWoID(@Param("signumID") String signumID,@Param("projectID") Integer projectID,@Param("startDate") String startDate,
			@Param("endDate") String endDate);
	public int getFlowchartdefID(@Param("woID") int woID);
	public List<Map<String, Object>> getBacklogWorkOrdersForProject(@Param("projectID") Integer projectID);
	
	public List<Map<String, Object>> getAllPositionsCount(@Param("projectID") Integer projectID);
	public Map<String, Object> getSubServiceAreaPCode(@Param("serviceAreaID") Integer serviceAreaID);
	public List<Map<String, Object>> getJobRoles();
	public List<Map<String, Object>> getAllCertifications(@Param("Issuer")String Issuer);
	public List<Map<String, Object>> getUniqueIssuer();
	public List<Map<String, Object>> getOnsiteLocations();
	public List<Map<String, Object>> getPositionStatus();
	public List<Map<String, Object>> getFilteredCompetences(@Param("competenceString")String competenceString);
	public List<Map<String,Object>> getSignumsWorkingInProject(@Param("signumID")String signumID,@Param("projectID") Integer projectID, @Param("startDate")String startDate, @Param("endDate")String endDate,@Param("dataTableReq") DataTableRequest dataTableReq,@Param("term")String term);
	public List<Map<String, Object>> getBacklogWorkOrdersForProjectWithSignum(@Param("projectID") Integer projectID, @Param("signumForProject") List<String> signumForProject);
	public List<Map<String, Object>> getSignumsFilteredForEngEngagement(@Param("projectID") Integer projectID, @Param("startDate")String startDate, @Param("endDate")String endDate, @Param("term")String term,  RowBounds rowBounds);
	public List<Map<String,Object>> getAllSignumForProject(@Param("term") String term, @Param("projectID") Integer projectID, @Param("startDate") String startDate, @Param("endDate") String endDate);
	public List<ResourceEngagementModel> getWOResourceLevelDetailsX(@Param("signumID") String signumID,@Param("projectID") Integer projectID,@Param("startDate") String startDate,
			@Param("endDate") String endDate);

	public List<Map<String, Object>> getSignumsWorkingInProjectSecond(@Param("signumID")String signumID,@Param("projectID") Integer projectID, @Param("startDate")String startDate,
			@Param("endDate")String endDate);
	public List<Map<String, Object>> getEmployeesInProject(@Param("projectID") Integer projectID, @Param("startDate")String startDate,
			@Param("endDate")String endDate,@Param("dataTableReq") DataTableRequest dataTableReq,@Param("term")String term);
	public List<Map<String, Object>> getBacklogWOCounts(@Param("projectID") Integer projectID, @Param("signums") List<String> signums);
	public List<Map<String, Object>> getClosedWOCounts(@Param("projectID") Integer projectID, @Param("signum") String signum, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	public List<Map<String, Object>> getManualHours(@Param("projectID") Integer projectID, @Param("signumID") String signumID, @Param("date") String date);
	public List<Map<String, Object>> getAutomaticHours(@Param("projectID") Integer projectID, @Param("signumID") String signumID, @Param("date") String date);
	
	public List<Map<String, Object>> getProjectAdhocHours(@Param("projectID") Integer projectID, @Param("signum") String signum, @Param("date") String date);
	public List<Map<String, Object>> getInternalAdhocHours(@Param("projectID") Integer projectID, @Param("signum") String signum, @Param("date") String date);
	
	public List<Map<String, Object>> getPlannedAssignedWOCount(@Param("projectID") Integer projectID, @Param("signum") String signum, @Param("date") String date);
	public List<Map<String, Object>> getInProgressWOcount(@Param("projectID") Integer projectID, @Param("signum") String signum, @Param("date") String date);
	
	public List<LeavePlanModel> getLeaveHours(@Param("signum") String signum, @Param("date") String date);
	
	
}
