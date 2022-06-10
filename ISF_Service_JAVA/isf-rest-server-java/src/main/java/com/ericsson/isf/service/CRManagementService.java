/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.CRManagementDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.mapper.AuditManagementMapper;
import com.ericsson.isf.model.AllocatedResourceModel;
import com.ericsson.isf.model.AuditDataModel;
import com.ericsson.isf.model.CRManagementModel;
import com.ericsson.isf.model.CRManagementResultModel;
import com.ericsson.isf.model.ChangeManagement;
import com.ericsson.isf.model.ChangeRequestPositionNewModel;
import com.ericsson.isf.model.DemandForecasSaveDetailsModel;
import com.ericsson.isf.model.DemandRequestModel;
import com.ericsson.isf.model.ProposedResourcesModel;
import com.ericsson.isf.model.RaiseCRMannagmentModel;
import com.ericsson.isf.model.WorkEffortModel;
import com.ericsson.isf.service.audit.AuditManagementService;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AppUtil;

import io.swagger.models.properties.StringProperty.Format;

/**
 *
 * @author esanpup
 */
@Service    
public class CRManagementService {
    @Autowired
    private CRManagementDAO changeManagementDAO;
    
    @Autowired
    private OutlookAndEmailService emailService;
    
    @Autowired
    private ActivityMasterDAO activityMasterDAO;
    
    @Autowired /*Bind to bean/pojo  */
    private ActivityMasterService activityMasterService;

    @Autowired
    private DemandForecastService demandForecastService;
    
    
    @Autowired
    private AuditManagementService auditManagmentService;
    

    public List<CRManagementResultModel> getCRDetails() {
        return changeManagementDAO.getCRDetails();
    }
    
    public CRManagementResultModel getCRDetailsByCRID(int cRID) {
        return changeManagementDAO.getCRDetailsByCRID(cRID);
    }
    private static final String CR_STATUS_ACCEPT="ACCEPTED";
    private static final String CR_STATUS_REJECT="REJECTED";
    @Transactional("transactionManager")
    public boolean acceptCR(List<CRManagementModel> crList) {
    	
    	AuditDataModel auditDataModel= new AuditDataModel();
    	
    	
    	
        for(CRManagementModel cr:crList) {
        	cr.setcRStatus(CR_STATUS_ACCEPT);
            this.changeManagementDAO.changeCrStatus(cr);
            
            String positionStatus=changeManagementDAO.getPositionStatus(cr.getcRID());
            
            CRManagementResultModel cRModel = this.getCRDetailsByCRID(cr.getcRID());
            List<WorkEffortModel> existingWeList;
            boolean isDiscardAllWe=false;
            if(cRModel.getWorkEffortIdExisting()==null){
            	existingWeList = activityMasterDAO.getActiveWorkEffortsByRpid(cRModel.getResourcePositionID());
            	isDiscardAllWe=true;
            }else{
            	existingWeList=new ArrayList<WorkEffortModel>();
            	existingWeList.add(this.changeManagementDAO.getWorkEffortByID(cRModel.getWorkEffortIdExisting()));
            }
            
         // Setting data in AuditDataModel
			auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
			auditDataModel.setCreatedBy(cr.getCurrentSignumID());
			auditDataModel.setAuditPageId(cr.getcRID()); 
			auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_CHANGE_REQUEST); 
			auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
			auditDataModel.setOldValue(positionStatus); 
			auditDataModel.setNewValue(positionStatus); 
			auditDataModel.setType(AppConstants.AUDIT);
			auditDataModel.setSourceId(1);
			
            if(cRModel.getActionType().equalsIgnoreCase("PM")) {
    			auditDataModel.setActorType(AppConstants.FULFILLMENT_MANAGER);
    			auditDataModel.setCommentCategory(AppConstants.RESOURCE_ALLOCATED_DATE_EXTEND_CR_APPROVAL_DATE_BY_FM);
            }else {
    			auditDataModel.setActorType(AppConstants.PROJECT_MANAGER);
    			auditDataModel.setCommentCategory(AppConstants.RESOURCE_CHANGE_CR_APPROVAL_DATE_BY_PM);
            }
            auditManagmentService.addToAudit(auditDataModel);
            
            // End of code
            
            for(WorkEffortModel wEffortModel:existingWeList){
            	if(wEffortModel.getWorkEffortID()!=cRModel.getWorkEffortIdProposed()){
	            	this.changeManagementDAO.disableBookedResourceByWEID(wEffortModel.getWorkEffortID());
	    	        this.changeManagementDAO.updateWorkEffortStatusByWeId(false, wEffortModel.getWorkEffortID()+"");
	    	        changeManagementDAO.updateWorkEffortPositionStatusByWeId(AppConstants.DELETE_STATUS, wEffortModel.getWorkEffortID()+""); 
            	}

            }
            this.changeManagementDAO.updateWorkEffortStatusByWeId(true, cRModel.getWorkEffortIdProposed()+"");    
	            
	            if(!isDiscardAllWe){
	            	WorkEffortModel wEffortModelProposed = this.changeManagementDAO.getWorkEffortByID(cRModel.getWorkEffortIdProposed());
		            Map<String,Object> map  = this.activityMasterDAO.getDetails(wEffortModelProposed.getResourcePositionID());
		            Date date1 = wEffortModelProposed.getStartDate();
					Date date2 = wEffortModelProposed.getEndDate();
		            
		            double totalHrs = AppUtil.getNoOfWeekdayHoursBetweenDates(date1,date2);
		            double days =(int) (totalHrs / AppUtil.WORKING_HOURS_IN_A_DAY);
		            
		            Calendar start = Calendar.getInstance();
		            start.setTime(cr.getProposedStartDate());
		            float hoursPerFte = (float) (( totalHrs * wEffortModelProposed.getFte_Percent() ) / 100);
		            float percentageHour = (float) (( AppUtil.WORKING_HOURS_IN_A_DAY * wEffortModelProposed.getFte_Percent() ) /100);
		            
		            this.changeManagementDAO.updateWorkEffortDetails(wEffortModelProposed.getStartDate(), wEffortModelProposed.getEndDate()
		            		, wEffortModelProposed.getWorkEffortID(), cr.getCurrentSignumID(),(int) (totalHrs / AppUtil.WORKING_HOURS_IN_A_DAY), hoursPerFte);
		            
		            for (int i = 0; i < days; i++){
		                Date targetDate = start.getTime();
		                int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
		                if (dayOfWeek != Calendar.SUNDAY && dayOfWeek != Calendar.SATURDAY) {
		                	if (wEffortModelProposed.getSignum() != null){
		                		boolean a =  this.changeManagementDAO.insertBookedResource(wEffortModelProposed.getWorkEffortID(), wEffortModelProposed.getResourcePositionID(), wEffortModelProposed.getSignum(), 
		                    		targetDate, Integer.parseInt(map.get("ProjectID")+""), percentageHour);
		                	}
		                }else {
		                	i=i-1;
		                }
		                start.add(Calendar.DATE, 1);
		            }
            }
	            this.activityMasterService.updatePoistionStatusByWfStatus(cRModel.getResourcePositionID());
        }
        
		emailService.sendMail(AppConstants.NOTIFICATION_TYPE_ACCEPT_CR,emailService.enrichMailforCM(crList,AppConstants.NOTIFICATION_TYPE_ACCEPT_CR));

        return true;
    }
    
    
    @Transactional("transactionManager")
    public boolean rejectCR(List<CRManagementModel> crList) {
    	
		
        AuditDataModel auditDataModel=new AuditDataModel();
    	
        for(CRManagementModel cr:crList) {
        	cr.setcRStatus(CR_STATUS_REJECT);
            this.changeManagementDAO.changeCrStatus(cr);
            
            String positionStatus=changeManagementDAO.getPositionStatus(cr.getcRID());

            ChangeRequestPositionNewModel cRDetails = this.changeManagementDAO.getCRDetailsByID(cr.getcRID());
            WorkEffortModel wEffortModelProposed = this.changeManagementDAO.getWorkEffortDetailsByID(cRDetails.getWorkEffortIdProposed());
            WorkEffortModel wEffortModelExisting = this.changeManagementDAO.getWorkEffortDetailsByID(cRDetails.getWorkEffortIdExisting());
            AllocatedResourceModel allocatedResource =  new AllocatedResourceModel();
            
            Calendar existingEDate = Calendar.getInstance();
			Calendar proposedEDate = Calendar.getInstance();
			Calendar proposedSDate = Calendar.getInstance();
			Calendar existingSDate = Calendar.getInstance();
			
			existingEDate.setTime(wEffortModelExisting.getEndDate());
			proposedEDate.setTime(wEffortModelProposed.getEndDate());
			proposedSDate.setTime(wEffortModelProposed.getStartDate());
			existingSDate.setTime(wEffortModelExisting.getStartDate());
            
            if (existingEDate.before(proposedEDate)){
            	existingEDate.add(Calendar.DATE, 1);
            	allocatedResource.setStartDate(existingEDate.getTime());
    			allocatedResource.setEndDate(proposedEDate.getTime());
            }else if (proposedSDate.before(existingSDate)){
            	existingSDate.add(Calendar.DATE, -1);
            	allocatedResource.setStartDate(proposedSDate.getTime());
    			allocatedResource.setEndDate(existingSDate.getTime());
            }
            
            if(allocatedResource.getStartDate()!=null &&allocatedResource.getEndDate()!=null) {
			double noOfHours = AppUtil.getNoOfWeekdayHoursBetweenDates(	allocatedResource.getStartDate(), allocatedResource.getEndDate());
            
			float hoursPerFte = (float) (( noOfHours * wEffortModelProposed.getFte_Percent() ) / 100);
			
			allocatedResource.setDuration((int) (noOfHours / AppUtil.WORKING_HOURS_IN_A_DAY));
			allocatedResource.setHours((int) hoursPerFte);
            }
			allocatedResource.setPositionStatus( AppConstants.PROPOSAL_PENDING_STATUS);
			allocatedResource.setWeid(wEffortModelProposed.getWorkEffortID());
			
			this.activityMasterDAO.updateWorkEffort(allocatedResource,0);
			
			//Setting data in AuditDataModel
			auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
			auditDataModel.setCreatedBy(cr.getCurrentSignumID());
			auditDataModel.setAuditPageId(cr.getcRID());
			auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_CHANGE_REQUEST);
			auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
			auditDataModel.setOldValue(positionStatus);
			auditDataModel.setNewValue(positionStatus);
			auditDataModel.setType(AppConstants.AUDIT);
			auditDataModel.setSourceId(1);
			
			if(cRDetails.getActionType().equalsIgnoreCase("pm")) {
				auditDataModel.setActorType(AppConstants.FULFILLMENT_MANAGER);
				auditDataModel.setCommentCategory(AppConstants.RESOURCE_ALLOCATED_DATE_EXTEND_CR_REJECTION_DATE_BY_FM);
			}
			else {
				    auditDataModel.setActorType(AppConstants.PROJECT_MANAGER);
    				auditDataModel.setCommentCategory(AppConstants.RESOURCE_CHANGE_CR_REJECTION_DATE_BY_PM);
				 }
            auditManagmentService.addToAudit(auditDataModel);
            
        }
        
        emailService.sendMail(AppConstants.NOTIFICATION_TYPE_REJECT_CR,emailService.enrichMailforCM(crList,AppConstants.NOTIFICATION_TYPE_REJECT_CR));

        return true;
    }
    

    public Map<String, Object> getRRIDFlag(int rRID){
        return changeManagementDAO.getRRIDFlag(rRID);
    }
    
    @Transactional("transactionManager")
    public boolean deleteRpID(int rPID, String signum){
    	
    	AuditDataModel auditDataModel=new AuditDataModel();
    	 
       //UPDATE Remote or Onsite count
    	List<Map<String, Object>> rPData = changeManagementDAO.getResourcePositionDataByRpID(rPID);
    	if(rPData.size()>0) {
    		int rrID=(int) rPData.get(0).get("ResourceRequestID");
    		String remoteOnsite = (String) rPData.get(0).get("Remote_Onsite");
    		changeManagementDAO.updateResourceRequestCount(rrID,remoteOnsite);
    	}
         changeManagementDAO.deleteRpID(rPID, signum);
         
			// Setting data in AuditDataModel
			auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
			auditDataModel.setCreatedBy(signum);
			auditDataModel.setAuditPageId(rPID);
			auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_RESOURCE_POSITION);
			auditDataModel.setActorType(AppConstants.PROJECT_MANAGER);
			auditDataModel.setCommentCategory(AppConstants.PROPOSAL_PENDING_RESOURCE_DELETION_BY_PM);
			auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
			auditDataModel.setOldValue(AppConstants.PROPOSAL_PENDING);
			auditDataModel.setNewValue(AppConstants.PROPOSAL_PENDING);
			auditDataModel.setType(AppConstants.AUDIT);
			auditDataModel.setSourceId(1);
			auditManagmentService.addToAudit(auditDataModel);
         
         //update summary count 
         Integer projectId=changeManagementDAO.getProjectIdbyRpId(rPID);
         DemandForecasSaveDetailsModel saverequest= new DemandForecasSaveDetailsModel();;
         
         saverequest.setProjectid(projectId);
         saverequest.setOperation("");
         saverequest.setRole("Project Manager");
         saverequest.setSignum(signum);
         
         demandForecastService.updateSummaryCountForSPM(saverequest);
         
         
         List <Map<String, Integer>> wef=changeManagementDAO.getWorkEffortID(rPID, 1);
         for ( Map<String, Integer> data : wef){
        	 int weId=data.get("WorkEffortID");
        	 changeManagementDAO.updateWorkEffortStatusByWeId(false, weId+"");
        	 changeManagementDAO.updateWorkEffortPositionStatusByWeId(AppConstants.DELETE_STATUS, weId+"");
         }
         return true;
    }
    
    @Transactional("transactionManager")
    public boolean updateCrStatus(int crID,String signum){
    	
    	AuditDataModel auditDataModel=new AuditDataModel();
    	Boolean rRIDFlag;
    	
    	String positionStatus = changeManagementDAO.getPositionStatus(crID);
    	
        rRIDFlag= changeManagementDAO.updateCrStatus(crID,signum);
        
            if(Boolean.TRUE.equals(rRIDFlag)) {
            	
            // Setting data in AuditDataModel
     		auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
     		auditDataModel.setCreatedBy(signum);
     		auditDataModel.setAuditPageId(crID);
     		auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_CHANGE_REQUEST);
     		auditDataModel.setActorType(AppConstants.PROJECT_MANAGER);
     		auditDataModel.setCommentCategory(AppConstants.CR_WITHDRAWAL_DATE_BY_PM);
     		auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
     		auditDataModel.setOldValue(positionStatus);
     		auditDataModel.setNewValue(positionStatus);
     		auditDataModel.setType(AppConstants.AUDIT);
     		auditDataModel.setSourceId(1);
            auditManagmentService.addToAudit(auditDataModel);
            }
             
        return rRIDFlag;   
    }
  
  /* 
      //TODO code cleanup
    @Transactional("transactionManager")
    public boolean updateWorkEffortStatus(int rPID){
        return changeManagementDAO.updateWorkEffortStatus(rPID);
    }*/
    
	public Map<Integer, Boolean> raiseChangeManagment(
			ChangeManagement changeManagement)
			throws ParseException {
		
		AuditDataModel auditDataModel=new AuditDataModel();
		
		Map<Integer, Boolean> statusMap = new HashMap<Integer, Boolean>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String allocatedSignum = "" ;
		for (RaiseCRMannagmentModel cRObject : changeManagement.getRaiseCRMannagmentModel()) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = formatter.parse(cRObject.getStartDate());
			Date endDate = formatter.parse(cRObject.getEndDate());
			
			cRObject.setStartDate(formatter1.format(startDate));
			cRObject.setEndDate(formatter1.format(endDate));
			boolean isPostPreClosure = false;
			 List<WorkEffortModel> existingWeList; 
			 existingWeList = activityMasterDAO.getActiveWorkEffortsByRpid(cRObject.getRpID());
			 allocatedSignum =existingWeList.get(0).getSignum();
			int count = changeManagementDAO.checkOpenStatusCount(cRObject.getRpID());
			if (count == 0) {
			//	List<WorkEffortModel> workEffortRows  = this.activityMasterDAO.getWorkEffortsByRpidInteger(cRObject.getRpID());
			//	getAffectedWorkEffortID(workEffortRows,startDate,endDate);
				List<WorkEffortModel> rowsSdate  = this.activityMasterDAO.getWeidWithMinSdate(cRObject.getRpID(),1);
				List<WorkEffortModel> rowsEdate  = this.activityMasterDAO.getWeidWithMaxEdate(cRObject.getRpID());
				float fte = this.changeManagementDAO.getFtePercentage(cRObject.getRpID());
				Map<String, String> remoteOnsite = this.changeManagementDAO.getRemoteOnsite(cRObject.getRpID());
				
				WorkEffortModel newWorkEffort = new WorkEffortModel();
				newWorkEffort.setResourcePositionID(cRObject.getRpID());
				
				newWorkEffort.setWorkEffortStatus(remoteOnsite.get("Remote_Onsite"));
				newWorkEffort.setCreatedBy(cRObject.getLoggedInSignum());
				newWorkEffort.setLastModifiedBy(cRObject.getLoggedInSignum());
				
				newWorkEffort.setAllocatedBy(cRObject.getLoggedInSignum());
				Integer ExistingWfID = null ;
				double noOfHours = 0 ;
				String status = null;
				if (!rowsSdate.get(0).getStartDate().equals(startDate) && !rowsEdate.get(0).getEndDate().equals(endDate)){
					newWorkEffort.setIsActive(false);
					newWorkEffort.setStartDate(startDate);
					newWorkEffort.setEndDate(endDate);
					newWorkEffort.setPositionStatus("Proposal Pending");
					ExistingWfID=rowsSdate.get(0).getWorkEffortID();
				//	newWorkEffort.setSignum(allocatedSignum);
					noOfHours = AppUtil.getNoOfWeekdayHoursBetweenDates(startDate, endDate);
					status = "OPEN";
					cRObject.setComments("Both Start and End Date got changed!!");

				}else if (startDate.before(rowsSdate.get(0).getStartDate())){
					newWorkEffort.setIsActive(false);
					newWorkEffort.setStartDate(startDate);
					newWorkEffort.setEndDate(rowsSdate.get(0).getEndDate());
					newWorkEffort.setSignum(rowsSdate.get(0).getSignum());
					ExistingWfID = rowsSdate.get(0).getWorkEffortID();
					newWorkEffort.setPositionStatus(rowsSdate.get(0).getPositionStatus());
					noOfHours = AppUtil.getNoOfWeekdayHoursBetweenDates(startDate, rowsSdate.get(0).getEndDate());
					status = "OPEN";
					
					// Setting data in AuditDataModel 
					auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
					auditDataModel.setCreatedBy(cRObject.getLoggedInSignum());
					auditDataModel.setAuditPageId(cRObject.getRpID());
					auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_WORK_EFFORT);
					auditDataModel.setActorType(AppConstants.PROJECT_MANAGER);
					auditDataModel.setCommentCategory(AppConstants.RESOURCE_ALLOCATED_DATE_EXTEND_CR_DATE_BY_PM);
					auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
					auditDataModel.setOldValue(AppConstants.RESOURCE_ALLOCATED);
					auditDataModel.setNewValue(AppConstants.RESOURCE_ALLOCATED); 
					auditDataModel.setType(AppConstants.AUDIT);
					auditDataModel.setSourceId(1);
					
				}else if (endDate.after(rowsEdate.get(0).getEndDate())){
// case2: ResourceAllocatedDateExtendCRDateByPM					
					newWorkEffort.setIsActive(false);
					newWorkEffort.setStartDate(rowsEdate.get(0).getStartDate());
					newWorkEffort.setEndDate(endDate);
					newWorkEffort.setSignum(rowsEdate.get(0).getSignum());
					ExistingWfID = rowsEdate.get(0).getWorkEffortID();
					newWorkEffort.setPositionStatus(rowsEdate.get(0).getPositionStatus());
					noOfHours = AppUtil.getNoOfWeekdayHoursBetweenDates(rowsEdate.get(0).getStartDate(), endDate);
					status = "OPEN";
					
					// Setting data in AuditDataModel
					auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
					auditDataModel.setCreatedBy(cRObject.getLoggedInSignum());
					auditDataModel.setAuditPageId(cRObject.getRpID());
					auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_WORK_EFFORT);
					auditDataModel.setActorType(AppConstants.PROJECT_MANAGER);
					auditDataModel.setCommentCategory(AppConstants.RESOURCE_ALLOCATED_DATE_EXTEND_CR_DATE_BY_PM);
					auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
					auditDataModel.setOldValue(AppConstants.RESOURCE_ALLOCATED);
					auditDataModel.setNewValue(AppConstants.RESOURCE_ALLOCATED); 
					auditDataModel.setType(AppConstants.AUDIT);
					auditDataModel.setSourceId(1);
					
				}else if (startDate.after(rowsSdate.get(0).getStartDate())){
					isPostPreClosure = true;
					int startIndex =  getWeIDPostponedPosition(rowsSdate, startDate, endDate);
					int i = 0;
					for (WorkEffortModel row : rowsSdate){
						if (i <= startIndex){
							this.changeManagementDAO.updateWorkEffortStatusByWeId(false, +row.getWorkEffortID()+""); 
							changeManagementDAO.updateWorkEffortPositionStatusByWeId("Post-Closure", row.getWorkEffortID()+""); 
						}
						i++;
					}
					newWorkEffort.setIsActive(true);
					newWorkEffort.setStartDate(startDate);
					newWorkEffort.setEndDate(rowsSdate.get(startIndex).getEndDate());
					newWorkEffort.setSignum(rowsSdate.get(startIndex).getSignum());
					ExistingWfID = rowsSdate.get(startIndex).getWorkEffortID();
					newWorkEffort.setPositionStatus(rowsSdate.get(startIndex).getPositionStatus());
					noOfHours = AppUtil.getNoOfWeekdayHoursBetweenDates(startDate, rowsSdate.get(startIndex).getEndDate());
					status = "CLOSED";
					
					// Setting data in AuditDataModel 
					auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
					auditDataModel.setCreatedBy(cRObject.getLoggedInSignum());
					auditDataModel.setAuditPageId(cRObject.getRpID());
					auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_WORK_EFFORT);
					auditDataModel.setActorType(AppConstants.PROJECT_MANAGER);
					auditDataModel.setCommentCategory(AppConstants.RESOURCE_ALLOCATED_DATE_PREPONDED_CR_DATE_BY_PM);
					auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
					auditDataModel.setOldValue(AppConstants.RESOURCE_ALLOCATED);
					auditDataModel.setNewValue(AppConstants.RESOURCE_ALLOCATED); 
					auditDataModel.setType(AppConstants.AUDIT);
					auditDataModel.setSourceId(1);
					
				}else if (endDate.before(rowsEdate.get(0).getEndDate())){					
// case1: ResourceAllocatedDatePreponedCRDateByPM
					isPostPreClosure = true;
					int endIndex = getWeIDPreClosed(rowsEdate, startDate, endDate);
					int i = 0;
					for (WorkEffortModel row : rowsEdate){
						if (i <= endIndex){
							this.changeManagementDAO.updateWorkEffortStatusByWeId(false, +row.getWorkEffortID()+"");
							changeManagementDAO.updateWorkEffortPositionStatusByWeId("Pre-Closure", row.getWorkEffortID()+""); 
						}
						i++;
					}
					
					newWorkEffort.setIsActive(true);
					newWorkEffort.setStartDate(rowsEdate.get(endIndex).getStartDate());
					newWorkEffort.setEndDate(endDate);
					newWorkEffort.setSignum(rowsEdate.get(endIndex).getSignum());
					ExistingWfID = rowsEdate.get(endIndex).getWorkEffortID();
					newWorkEffort.setPositionStatus(rowsEdate.get(endIndex).getPositionStatus());
					noOfHours = AppUtil.getNoOfWeekdayHoursBetweenDates(rowsEdate.get(endIndex).getStartDate(), endDate);
					status = "CLOSED";
					
					
					// Setting data in AuditDataModel
					auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
					auditDataModel.setCreatedBy(cRObject.getLoggedInSignum());
					auditDataModel.setAuditPageId(cRObject.getRpID());
					auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_WORK_EFFORT);
					auditDataModel.setActorType(AppConstants.PROJECT_MANAGER);
					auditDataModel.setCommentCategory(AppConstants.RESOURCE_ALLOCATED_DATE_PREPONDED_CR_DATE_BY_PM);
					auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
					auditDataModel.setOldValue(AppConstants.RESOURCE_ALLOCATED);
					auditDataModel.setNewValue(AppConstants.RESOURCE_ALLOCATED); // xx Make it clear
					auditDataModel.setType(AppConstants.AUDIT);
					auditDataModel.setSourceId(1);
					
				}else if(rowsSdate.get(0).getStartDate().equals(startDate) && rowsEdate.get(0).getEndDate().equals(endDate) 
						&& cRObject.getActionType().equals("fm")){
					
// Case3: ResourceAllocatedResourceChangeCRDateByFM					
					ExistingWfID=rowsSdate.get(0).getWorkEffortID();
//					this.changeManagementDAO.updateWorkEffortStatusByWeId(false, ExistingWfID.toString());
					
					newWorkEffort.setIsActive(false);
					newWorkEffort.setStartDate(rowsSdate.get(0).getStartDate());
					newWorkEffort.setEndDate(rowsEdate.get(0).getEndDate());
					newWorkEffort.setSignum(cRObject.getSignum());
					newWorkEffort.setPositionStatus("Resource Allocated");
					noOfHours = AppUtil.getNoOfWeekdayHoursBetweenDates(rowsSdate.get(0).getStartDate(), rowsEdate.get(0).getEndDate());
					status = "OPEN";
					
					// Setting data in AuditDataModel
					auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
					auditDataModel.setCreatedBy(cRObject.getLoggedInSignum());
					auditDataModel.setAuditPageId(cRObject.getRpID());
					auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_WORK_EFFORT);
					auditDataModel.setActorType(AppConstants.FULFILLMENT_MANAGER);
					auditDataModel.setCommentCategory(AppConstants.RESOURCE_ALLOCATED_RESOURCE_CHANGE_CR_DATE_BY_FM);
					auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
					auditDataModel.setOldValue(AppConstants.RESOURCE_ALLOCATED);
					auditDataModel.setNewValue(AppConstants.RESOURCE_ALLOCATED); 
					auditDataModel.setType(AppConstants.AUDIT);
					auditDataModel.setSourceId(1);
					
				}else if (cRObject.getActionType().equals("fm")){
					newWorkEffort.setIsActive(false);
					newWorkEffort.setStartDate(startDate);
					newWorkEffort.setEndDate(endDate);
					newWorkEffort.setPositionStatus("Resource Allocated");
					ExistingWfID = cRObject.getWeid();
					newWorkEffort.setSignum(cRObject.getSignum());
					noOfHours = AppUtil.getNoOfWeekdayHoursBetweenDates(startDate, endDate);
					status = "OPEN";
					
				}// changes for signum change CR request
				
				
				
				float hoursPerFte = (float) (( noOfHours * fte ) / 100);
				newWorkEffort.setDuration((int) (noOfHours / AppUtil.WORKING_HOURS_IN_A_DAY)); // days
				newWorkEffort.setFte_Percent(fte);
				newWorkEffort.setHours((int) hoursPerFte);
				this.changeManagementDAO.insertInWorkEffort(newWorkEffort);
				
				Map<String, Integer> data = this.changeManagementDAO.getLastInsertedID(cRObject);
				statusMap.put(cRObject.getRpID(), changeManagementDAO.raiseChangeManagment(cRObject, startDate, endDate,data.get("WorkEffortID"),ExistingWfID, status,changeManagement.getReason()));

				this.activityMasterService.updatePoistionStatusByWfStatus(rowsSdate.get(0).getResourcePositionID());
				
				
				// Calling AuditDetails Inserttion 
				
				if(StringUtils.isNotEmpty(auditDataModel.getCommentCategory())) {
					auditDataModel.setAuditPageId(data.get("WorkEffortID"));
					auditDataModel.setOldValue(newWorkEffort.getPositionStatus());
					auditDataModel.setNewValue(newWorkEffort.getPositionStatus());
					auditManagmentService.addToAudit(auditDataModel);
				}
				
				
				
				// booked hours entry
				if (isPostPreClosure) {

					double days = AppUtil.getTotalDaysBetweenDates(
							newWorkEffort.getStartDate(),
							newWorkEffort.getEndDate());
					float percentageHour = (float) ((AppUtil.WORKING_HOURS_IN_A_DAY * newWorkEffort
							.getFte_Percent()) / 100);

					Calendar start = Calendar.getInstance();
					start.setTime(newWorkEffort.getStartDate());
					Map<String, Object> map = this.activityMasterDAO
							.getDetails(cRObject.getRpID());

					for (int i = 0; i < days; i++) {
						Date targetDate = start.getTime();
						int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
						if (dayOfWeek != Calendar.SUNDAY
								&& dayOfWeek != Calendar.SATURDAY) {
							if (newWorkEffort.getSignum() != null) {
								boolean a = this.changeManagementDAO
										.insertBookedResource(
												data.get("WorkEffortID"),
												newWorkEffort
														.getResourcePositionID(),
												newWorkEffort.getSignum(),
												targetDate,
												Integer.parseInt(map
														.get("ProjectID") + ""),
												percentageHour);
							}else {
								i=i-1;
							}
						}
						start.add(Calendar.DATE, 1);
					}
				}
				isPostPreClosure = false;
			
			} else {
				statusMap.put(cRObject.getRpID(), false);
			}
			
		}
		changeManagement.getRaiseCRMannagmentModel().get(0).setAllocatedSignum(allocatedSignum);
		if(changeManagement.getRaiseCRMannagmentModel().get(0).getActionType().equalsIgnoreCase("fm")) {
		emailService.sendMail(AppConstants.NOTIFICATION_TYPE_RAISE_CR,emailService.enrichMailforCM(changeManagement.getRaiseCRMannagmentModel(),AppConstants.NOTIFICATION_TYPE_RAISE_CR));
		}
		else {
			

			emailService.sendMail(AppConstants.NOTIFICATION_TYPE_RAISE_CR_PM,emailService.enrichMailforCM(changeManagement.getRaiseCRMannagmentModel(),AppConstants.NOTIFICATION_TYPE_RAISE_CR_PM));
		}
		
		return statusMap;
	}

	public int getWeIDPostponedPosition(List<WorkEffortModel> rowsSdate, Date startDate, Date endDate){
		int i = 0;
		for (WorkEffortModel row : rowsSdate){
			if ((row.getStartDate().before(startDate) || row.getStartDate().equals(startDate)) && (row.getEndDate().after(startDate) || row.getEndDate().equals(startDate))){	
				break;
				
			}
			i++;
		}
		return i;
	}
	
	public int getWeIDPreClosed(List<WorkEffortModel> rowsSdate, Date startDate, Date endDate){
		int i = 0;
		for (WorkEffortModel row : rowsSdate){
			if ((endDate.before(row.getEndDate()) || endDate.equals(row.getEndDate())) && (endDate.after(row.getStartDate()) || endDate.equals(row.getStartDate()))){
				break;
			}
			i++;
		}
		return i;
	}
	
        @Transactional("transactionManager")
	public void checkUpdatePreClosure(RaiseCRMannagmentModel cRObject, Date startDate, Date endDate, Integer proposed, Integer existing) {
		List<Map<String, Date>> startData = this.changeManagementDAO.getCrfromStartDate(cRObject);
		if (startData.size() == 1) {
			List<Map<String, Object>> endData = this.changeManagementDAO
					.getEndDatefromWorkEffort(cRObject);
			if (endData.size() > 1) {
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				cal1.setTime((Date) endData.get(0).get("EndDate"));
				cal2.setTime((Date) endData.get(1).get("EndDate"));
				
				
				if (cal1.before(cal2)) {
					Map<String, Integer> data = this.changeManagementDAO.getLastInsertedID(cRObject);
					
					double totalHrs = AppUtil.getNoOfWeekdayHoursBetweenDates(startDate,endDate);
					
					this.changeManagementDAO.updateWorkEffortID(proposed);
					this.changeManagementDAO.updateWorkEffortID(existing);
					this.changeManagementDAO.updateCrStatusPreClosure(cRObject.getRpID());
					this.changeManagementDAO.updateWorkEffortHours(data.get("WorkEffortID"), totalHrs);
				}
			}
		}
	}
    
   public List<ProposedResourcesModel> getPositionsAndProposedResources(String status,String signum, String projectID, String marketArea){
        List<ProposedResourcesModel> parDetails=new ArrayList<>();
        parDetails=this.changeManagementDAO.getPositionsAndProposedResources(status,signum,projectID,marketArea);
        return parDetails;
    }
    
  /* //TODO code cleanup 
   *  public List<ProposedResourcesModel> getApprovedResourceList(String signum){
        List<ProposedResourcesModel> listDetails = new ArrayList<>();
        listDetails = this.changeManagementDAO.getApprovedResourceList(signum);
        return listDetails;
    }*/
    
    public void updateRPDates(int rPID, String startDate, String endDate, int hours, String signum, String reason){
    	
    	AuditDataModel auditDataModel=new AuditDataModel();
    	
        this.changeManagementDAO.updateRPDates(rPID,startDate,endDate,hours,signum,reason);
        
		// Setting data in AuditDataModel
		auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
		auditDataModel.setCreatedBy(signum);
		auditDataModel.setAuditPageId(rPID);
		auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_RESOURCE_POSITION);
		auditDataModel.setActorType(AppConstants.PROJECT_MANAGER);
		auditDataModel.setCommentCategory(AppConstants.PROPOSAL_PENDING_DATE_CHANGE_CR_DATE_BY_PM);
		auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
		auditDataModel.setOldValue(AppConstants.PROPOSAL_PENDING);
		auditDataModel.setNewValue(AppConstants.PROPOSAL_PENDING);
		auditDataModel.setType(AppConstants.AUDIT);
		auditDataModel.setSourceId(1);
		auditManagmentService.addToAudit(auditDataModel);
    }

    public List<CRManagementResultModel> getCRDetails(String viewType, String signumID,String marketArea) {
        String view="";
       List<CRManagementResultModel> crList = null;
        if(viewType.equalsIgnoreCase("PM")){
            view="fm";
           crList= this.changeManagementDAO.getCRDetailsPm(view,signumID,marketArea);
        }
        else if(viewType.equalsIgnoreCase("FM")){
            view="pm";
           crList= this.changeManagementDAO.getCRDetails(view,signumID,marketArea);
        }
        return crList;
    }
    public List<Map<String,Object>> getReason() {
        return this.changeManagementDAO.getReason();
    }
  
}
