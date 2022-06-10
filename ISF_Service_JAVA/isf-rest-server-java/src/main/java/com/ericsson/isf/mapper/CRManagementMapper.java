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
import org.springframework.http.HttpHeaders;

import com.ericsson.isf.model.CRManagementModel;
import com.ericsson.isf.model.CRManagementResultModel;
import com.ericsson.isf.model.ChangeRequestPositionNewModel;
import com.ericsson.isf.model.ProposedResourcesModel;
import com.ericsson.isf.model.RaiseCRMannagmentModel;
import com.ericsson.isf.model.WorkEffortModel;

/**
 *
 * @author esanpup
 */

public interface CRManagementMapper {

    public List<CRManagementResultModel> getCRDetails();
    
    public ChangeRequestPositionNewModel getCRDetailsByID(@Param("crID") int crID);

    public CRManagementResultModel getCRDetailsByCRID(@Param("cRID") int cRID);

    public void acceptCRByID(@Param("CRID") int CRID, @Param("signumID") String signumID, 
                             @Param("comment") String comment);
    public void changeCrStatus(@Param("cr") CRManagementModel cr);
    public Map<String, Object> getRRIDFlag(@Param("rRID") int rRID);
    public boolean deleteRpID(@Param("rPID") int rPID, @Param("signum") String signum);
    
    public boolean updateCrStatus(@Param("crID") int crID,@Param("signum") String signum);
    
   /*
      //TODO code cleanup
      public boolean updateWorkEffortStatus(@Param("rPID") int rPID);
   */
    
    public boolean raiseChangeManagment(@Param("cRObject") RaiseCRMannagmentModel cRObject, @Param("startDate") Date startDate, @Param("endDate") Date endDate,@Param("proposed") Integer proposed,@Param("existing") Integer existing,@Param("status") String status, @Param("reason") String reason);
    
    public List<Map<String, Date>> getCrfromStartDate(@Param("cRObject") RaiseCRMannagmentModel cRObject);

    public Map<String,Integer> getWorkEffortDetailsFromRpid(@Param("rPID") int rPID);
    
    public List<Map<String, Object>> getEndDatefromWorkEffort(@Param("cRObject") RaiseCRMannagmentModel cRObject);
    
    public Map<String, Integer> getLastInsertedID(@Param("cRObject") RaiseCRMannagmentModel cRObject);
    
    public List<Map<String,Object>> checkRpID(@Param("rpID") int rpID);
    
    public int checkOpenStatusCount(@Param("rpID") int rpID);
    
    public List<ProposedResourcesModel> getPositionsAndProposedResources(@Param("status") String status,
                                                                          @Param("signum") String signum,@Param("projectID")  String projectID,@Param("marketArea") String marketArea);
    
    public Map<String, Object>  getResourceRequestDetailsById(@Param("rrId")  String rrId);

    
    public float getFtePercentage(@Param("rPID") int rPID);
    
    public Map<String, String> getRemoteOnsite(@Param("rPID") int rPID);
   
    /* //TODO code cleanup 
     * public List<ProposedResourcesModel> getApprovedResourceList(@Param("signum") String signum);*/

    public void rejectCRByID(@Param("cRID") int cRID,
                             @Param("signumID") String signumID, 
                             @Param("comment") String comment);

    public String getExistingComments(@Param("cRID") int cRID);
    
    public List<Map<String, Integer>> getWorkEffortID(@Param("rPID") int rPID, @Param("status") int status);
    public WorkEffortModel getWorkEffortByID(@Param("weId") int rPID);
    public List<WorkEffortModel> getWorkEffortsByRpId(@Param("rpId") int rPID);
    public WorkEffortModel getWorkEffortDetailsByID(@Param("weId") int rPID);
    
    public String getAllocatedSignum(@Param("rPID") int rPID);

    public String checkSignumInWEffort(@Param("cRID") int cRID);

    public void updateWorkEffortDetails(@Param("stDate") Date stDate,
                                        @Param("edDate") Date edDate, 
                                        @Param("WorkEffortID") Integer WorkEffortID,
                                        @Param("signumID") String signumID, @Param("days") int days,@Param("totalHrs") double totalHrs);
    
    public void updateCrStatusPreClosure(@Param("id") Integer id);
    
    public void updateWorkEffortHours(@Param("id") Integer id, @Param("totalHrs") double totalHrs);
    
    public void updateWorkEffortID(@Param("id") Integer id);

    public WorkEffortModel getWorkEffortByCRID(@Param("cRID") int cRID);

    public boolean insertBookedResource(@Param("workEffortID") int workEffortID,
                                     @Param("resourcePositionID") int resourcePositionID, 
                                     @Param("signum") String signum, 
                                     @Param("targetDate") Date targetDate, 
                                     @Param("projectID") int projectID, 
                                     @Param("hours") float hours);

    public void disableBookedResourceByWEID(@Param("workEffortID") int workEffortID);

    public void updateWorkEffortDates(@Param("startDa") Date startDa, 
                                      @Param("endDa") Date endDa, 
                                      @Param("CRID") Integer CRID);
    public long insertInWorkEffort(@Param("we") WorkEffortModel we);
 	
    public boolean updateWorkEffortStatusByWeId(@Param("isActive") boolean isActive,@Param("WorkEffortID") String weId);
    
    public boolean updateWorkEffortPositionStatusByWeId(@Param("status") String status,@Param("WorkEffortID") String weId);
    
    public void updateRPDates(@Param("rPID") int rPID, @Param("startDate") String startDate,
                                @Param("endDate") String endDate, @Param("hours") int hours, @Param("signum") String signum,@Param("reason") String reason);

    public List<CRManagementResultModel> getCRDetails(@Param("view") String view,
                                                      @Param("signumID") String signumID,@Param("marketArea") String marketArea);
    
    public List<CRManagementResultModel> getCRDetailsPm(@Param("view") String view,
            @Param("signumID") String signumID,@Param("marketArea") String marketArea);
    
    
    public void updateWorkEffortSignum(
            @Param("WorkEffortID") Integer WorkEffortID,
            @Param("signumID") String currentUser,
            @Param("signum") String signum);

    public List<WorkEffortModel> getFutureWorkEffortsBySignum(@Param("signum") String signum);
    
    public Integer getProjectIdbyWorkeffort(@Param("weId") String weId);
    public Integer getProjectIdbyRpId(@Param("rpId") String rpid);
    
    
    public boolean changeBookedResourceStatusByWEIDAndDates(@Param("workEffortID") int workEffortID,@Param("startDate") Date startDate,@Param("endtDate") Date endtDate,
    		@Param("status") int status);

	public Map<String, Object> getProposedWorkEfforts(@Param("workEffortID") int workEffortID);
	public List<Map<String,Object>> getReason();

	public Integer getProjectIdbyRpId(@Param("rpId") int rPID);

	public List<Map<String, Object>> getResourcePositionDataByRpID(@Param("rpId") int rPID);

	public void updateResourceRequestCount(@Param("rrID") int rrID,@Param("remoteOnsite") String remoteOnsite);
	
	public String getPositionStatus(@Param("crID") int crID);

    
}
