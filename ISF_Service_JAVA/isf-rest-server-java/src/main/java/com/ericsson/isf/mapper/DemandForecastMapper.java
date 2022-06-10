/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;

import java.util.Date;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

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

public interface DemandForecastMapper {
	public void saveDemandSummaryDraft(@Param("demandSummarySaveRequest") DemandForecastModel demandSummarySaveRequest);
	public void updateDemandSummaryDraft(@Param("demandSummarySaveRequest") DemandForecastModel demandSummarySaveRequest);
	
	public List<Map<String,Object>> getDemandSummary(@Param("signum") String signum
													,@Param("startDate") Date startDate
													,@Param("pageSize") Integer pageSize
													,@Param("uniqueDates") String uniqueDates
													,@Param("role") String role
													,@Param("marketArea") String marketArea
													
													);
	
	public List<Map<String,Object>> getDemandSummaryByRole(@Param("projectId") int projectId
			,@Param("role") String role
			);
	
	
	
	List<DemandForecastDetailModel> getDemandForecastDetails(@Param("signum") String signum,@Param("projectId") Integer projectId,@Param("startDate") Date startDate
			,@Param("pageSize") Integer pageSize,@Param("role")  String role);
	
	
	List<DemandForecastDetailModel>getAllDemandForecastDetailsByRole(@Param("projectId") Integer projectId,@Param("role")  String role);
	
	public List<Map<String,Object>> getToolDetails(@Param("positionId") Integer positionId);
	/* //TODO Code cleanup
	 * List<DemandForecastDetailModel> getDemandForecastDetailsForMigration(@Param("signum") String signum,@Param("projectId") Integer projectId,@Param("startDate") Date startDate
			,@Param("pageSize") Integer pageSize,@Param("role")  String role);*/
	
	 public List<DemandForecastFulfillmentModel> getDemandPositionDetailsByprojectId(@Param("projectID")Integer projectId);
	
	public String getUniqueDatesForForcast(@Param("signum") String signum);
	public int saveDemandForecastDetails(@Param("demandForecastDetails") DemandForecastDetailModel data,@Param("projectId")  Integer projectid, 
			@Param("role") String role,@Param("signum")  String signum,
			@Param("operation") String operation);
	
	public List<Integer> getProjectScopeDetail(@Param("demandForecastDetails") DemandForecastDetailModel demandForecastDetails);
	
	public void removeDupilcateDemandSummaryDraft(@Param("demandSummarySaveRequest") DemandForecastModel demandSummarySaveRequest);
	
	
	public void sendToFM(@Param("resourceRequestID") Integer resourceRequestID);

	public List<DemandForecastFulfillmentModel> getDemandForecastDetailsByfilter(@Param("projectID") Integer projectID,@Param("positionStatus")  String positionStatus,
			@Param("marketArea")  String marketArea);
	public List<DemandForecastFulfillmentModel> getDemandForecastDetailsByfilterForMigration(@Param("projectID") Integer projectID,@Param("positionStatus")  String positionStatus,
			@Param("marketArea")  String marketArea);
	
	public void updateDemandForecastDetails(@Param("demandForecastDetails") DemandForecastDetailModel data,@Param("projectId")  Integer projectid, 
			@Param("role") String role,@Param("signum")  String signum,
			@Param("operation") String operation);
	
	public void saveResourceRequestWorkEfforts(@Param("resourceRequestWorkEffort")ResourceRequestWorkEffortsModel resourceRequestWorkEffort );
	public void updateResourceRequestWorkEfforts(@Param("resourceRequestWorkEffort")ResourceRequestWorkEffortsModel resourceRequestWorkEffort );
	
	public void saveDemandVendorMapping(@Param("positionId") Integer positionId,@Param("vendor")  String vendor);
	public void deleteDemandVendorMapping(@Param("positionId") Integer positionId);
	
	public void saveDemandToolMapping(@Param("positionId") Integer positionId,@Param("toolId")  Integer toolId);
	public void deleteDemandToolMapping(@Param("positionId") Integer positionId);
	
	public void deleteOthersUsersDrafts(@Param("signum") String signum,@Param("projectId") Integer projectId);
	public int isDraftAllowed(@Param("projectId") Integer projectId,@Param("role")String role);
	public DemandForecastDetailModel getDemandForecastDetailsByRRID(@Param("rrid") Integer rrID);
	
	public List<Map<String, Object>> getDomainSubdomain(@Param("projectScopeID") String projectScopeID,@Param("serviceAreaID") String serviceAreaID);
	public List<Map<String, Object>> getTechnologies(@Param("projectScopeID") String projectScopeID,@Param("serviceAreaID") String serviceAreaID,@Param("domainID") String domainID);
	
	public List<Map<String, Object>> getScopeDetails(@Param("projectId") String projectId);
	public List<ProjectScopeDetailMappingModel> getAllScopeDetailsByProject(@Param("projectId") String projectId);
	public void deleteResourcePositionByRrID(@Param("positionId") Integer positionId, @Param("signum") String signum);
	public void deleteWorkEffortsByRrID(@Param("positionId") Integer positionId, @Param("signum") String signum);
	public List<Map<String, Object>> getResourceRequestedBySubScope(@Param("projectID")int projectID,@Param("projectScopeDetailID")int projectScopeDetailID);
	public int getDemandSummaryDraftDetails(@Param("demandSummarySaveRequest") DemandForecastModel demandSummarySaveRequest);
	public DemandForecastDetailModel getDemandForecastDetailsByRrIdAndPId(@Param("positionId")Integer positionId,
			@Param("projectId") Integer projectId);
	public List<VendorTechModel> getVendorTechCombination(@Param("start") int start,  @Param("length") int length, @Param("term")  String term);
	public void saveDemandVendorTechCombination( @Param("vendorTechDataModel") List<VendorTechModel> vendorTechDataModel);
	public boolean validateVendortech( @Param("vendortechname") String vendortechname);

}
