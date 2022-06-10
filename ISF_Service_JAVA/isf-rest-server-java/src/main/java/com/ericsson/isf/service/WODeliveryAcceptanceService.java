/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.WODeliveryAcceptanceDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.ericsson.isf.model.ClosedWODetailsModel;
import com.ericsson.isf.model.EmployeeBasicDetails;
import com.ericsson.isf.model.MailModel;
import com.ericsson.isf.model.ReasonFromWOModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.WORelatedDetails;
import com.ericsson.isf.model.WorkOrderAcceptanceModel;
import com.ericsson.isf.model.WorkOrderModel;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.ericsson.isf.util.MailUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author esanpup
 */
@Service
public class WODeliveryAcceptanceService {
    
    @Autowired
    private WODeliveryAcceptanceDAO woDeliveryAcceptanceDAO;
    
    @Autowired
    private WorkOrderPlanService woPlanService;

    @Autowired
	private WOExecutionService woExecutionService;
    
    @Autowired
    MailUtil mailUtil;
    
    @Autowired
	private ValidationUtilityService validationUtilityService;
    
    @Autowired
	private ApplicationConfigurations configurations;
    
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);
    private static final String WO_REOPENED_MSG_SRID = "WO cannot be reopened as it is linked to SRID";
    
    public List<String> getAcceptanceRatings() {
        return this.woDeliveryAcceptanceDAO.getAcceptanceRatings();
        
    }

    @Transactional("transactionManager")
    public void acceptWorkOrder(WorkOrderAcceptanceModel woAcceptanceModel) {
        if(!woAcceptanceModel.getLstWoID().isEmpty()){
            
           woAcceptanceModel.getLstWoID().stream().forEach(woID -> {
            String drEmail="";
            String dACEmail="";
            this.woDeliveryAcceptanceDAO.acceptWorkOrder(woID, woAcceptanceModel.getRating(), woAcceptanceModel.getComment(),
                                                        woAcceptanceModel.getAcceptedOrRejectedBy(),woAcceptanceModel.getLastModifiedBy());
            if(configurations.getBooleanProperty(ConfigurationFilesConstants.WORKORDER_MAIL_ENABLED, false)) {
            	 MailModel model= woPlanService.getWoMailNotificationDetails(woID);
                 List<String> dac=woPlanService.getWoMailNotificationDetailsForDAC(woID);
                 EmployeeBasicDetails empDet= woPlanService.getEmpInfo(woAcceptanceModel.getLastModifiedBy());
                  if(model !=null){
                          String mailBody= mailUtil.generateMailBodyForWorkOrders(model.getEmployeeSignum(), model.getEmployeeName(),
                          		empDet.getSignum(),empDet.getEmployeeName() ,"ACCEPTED", woID,"NA",this.woExecutionService.getNodesByWoId(woID));
                      for(String email:model.getdR_EmailID()){
                          drEmail= email+";"+drEmail;
                      }
                      if(dac != null)
                      {	  
                        for(String email:dac){
                            dACEmail= email+";"+dACEmail;	
                      }
                      }
                  woPlanService.sendMailNotification(model.getEmployeeEmailID(), mailBody,empDet.getEmployeeEmailId()+";"+model.getProjectCreatorEmailID()+";"+drEmail+";"+dACEmail);
                 }
           
            }
        });
        }else{
            throw new ApplicationException(500, "Invalid input... WorkOrder List cannot be empty !!! ");
        }
        
    }
	
    @Transactional("transactionManager")
    public void sendWorkOrderForAcceptance(WorkOrderAcceptanceModel woAcceptanceModel){
        if(!woAcceptanceModel.getLstWoID().isEmpty()){
            woAcceptanceModel.getLstWoID().stream().forEach(woID -> {
                this.woDeliveryAcceptanceDAO.sendWorkOrderForAcceptance(woID, woAcceptanceModel.getAcceptedOrRejectedBy(), woAcceptanceModel.getComment(),
                                                                        woAcceptanceModel.getLastModifiedBy());
            });
        }else{
            throw new ApplicationException(500, "Invalid input... WorkOrder List cannot be empty !!! ");
        }
       
    }
    
    @Transactional("transactionManager")
	public ResponseEntity<Response<Void>> rejectWorkOrder(WorkOrderAcceptanceModel woAcceptanceModel) {
		Response<Void> rejectWorkOrderResponse = new Response<>();
		try {
			if (!woAcceptanceModel.getLstWoID().isEmpty()) {
				List<Integer> listofWoid = new ArrayList<>();
				woAcceptanceModel.getLstWoID().stream().forEach(woID -> {
					int srId = 0;
					String drEmail = "";
					String dACEmail = "";
					srId = this.woDeliveryAcceptanceDAO.rejectWorkOrder(woID, woAcceptanceModel.getAcceptance(),
							woAcceptanceModel.getReason(), woAcceptanceModel.getComment(),
							woAcceptanceModel.getAcceptedOrRejectedBy(), woAcceptanceModel.getLastModifiedBy());
					if(srId!=0) {
						listofWoid.add(srId);
					}
					if(configurations.getBooleanProperty(ConfigurationFilesConstants.WORKORDER_MAIL_ENABLED, false)) {
						MailModel model = woPlanService.getWoMailNotificationDetails(woID);
						List<String> dac = woPlanService.getWoMailNotificationDetailsForDAC(woID);
						EmployeeBasicDetails empDet = woPlanService.getEmpInfo(woAcceptanceModel.getLastModifiedBy());
						if (model != null) {
							String mailBody = mailUtil.generateMailBodyForWorkOrders(model.getEmployeeSignum(),
									model.getEmployeeName(), empDet.getSignum(), empDet.getEmployeeName(), "REJECTED", woID,
									"NA", this.woExecutionService.getNodesByWoId(woID));
							for (String email : model.getdR_EmailID()) {
								drEmail = email + ";" + drEmail;
							}
							if (dac != null) {
								for (String email : dac) {
									dACEmail = email + ";" + dACEmail;
								}
							}
							woPlanService.sendMailNotification(model.getEmployeeEmailID(), mailBody,
									empDet.getEmployeeEmailId() + ";" + model.getProjectCreatorEmailID() + ";" + drEmail
											+ ";" + dACEmail);
						}
					}
				});
				if (CollectionUtils.isEmpty(listofWoid)) {
					rejectWorkOrderResponse.addFormMessage("Successfully rejected");
				} else {
					rejectWorkOrderResponse
							.addFormMessage("Successfully rejected. " + listofWoid.size() + " " + WO_REOPENED_MSG_SRID);
				}
				

			} else {
				throw new ApplicationException(500, "Invalid input... WorkOrder list cannot be empty");
			}
		} catch (ApplicationException e) {
			rejectWorkOrderResponse.addFormError(e.getMessage());
			return new ResponseEntity<>(rejectWorkOrderResponse, HttpStatus.OK);
		}
		return new ResponseEntity<>(rejectWorkOrderResponse, HttpStatus.OK);

	}

    /**
     * 
     * @param projectID
     * @param projectName
     * @param signumID
     * @param role
     * @return List<ClosedWODetailsModel>
     */
    public List<ClosedWODetailsModel> getclosedWODetails(int projectID, String projectName,String signumID, String role) {
        
    	return this.woDeliveryAcceptanceDAO.getclosedWODetails(projectID, projectName,signumID,role);
    }
   
    public List<ReasonFromWOModel> getReasonFromWO(int wOID) {
        List<ReasonFromWOModel> wODetails = this.woDeliveryAcceptanceDAO.getReasonFromWO(wOID);
        return wODetails;
    }

    public WORelatedDetails getStatusReasons(int woID) {
        String wOStatus= this.woDeliveryAcceptanceDAO.getWOStatus(woID);
//        String deliveryAcceptance= this.woDeliveryAcceptanceDAO.getDelieveryAcceptance(woID);
        WORelatedDetails reasons = new WORelatedDetails();
        if(wOStatus.equalsIgnoreCase("CLOSED") || wOStatus.equalsIgnoreCase("REJECTED") ){
            reasons = this.woDeliveryAcceptanceDAO.getStatusReasons(woID);
            if(reasons == null){
                reasons = new WORelatedDetails();
            }
            reasons.setIsParent(false);
            if(reasons.getDeliveryComment()==null || reasons.getDeliveryComment().equalsIgnoreCase("null") || reasons.getDeliveryComment().length()==0){
                reasons.setDeliveryComment(" ");
            }
            else if(reasons.getUserComments()==null || reasons.getUserComments().equalsIgnoreCase("NULL") || reasons.getUserComments().length()==0){
                reasons.setUserComments(" ");
            }
            else if(reasons.getDeliveryReason()==null || reasons.getDeliveryReason().equalsIgnoreCase("NULL") || reasons.getDeliveryReason().length()==0){
                reasons.setDeliveryReason(" ");
            }
            else if(reasons.getUserDeliveryStatus()==null || reasons.getUserDeliveryStatus().equalsIgnoreCase("NULL") || reasons.getUserDeliveryStatus().length()==0){
                reasons.setUserDeliveryStatus(" ");
            }
            else if(reasons.getDeliveryStatus()==null || reasons.getDeliveryStatus().equalsIgnoreCase("NULL") || reasons.getDeliveryStatus().length()==0){
                reasons.setDeliveryStatus(" ");
            }
            
//            if(deliveryAcceptance != null){
//                reasons = this.woDeliveryAcceptanceDAO.getStatusReasons(woID);
//            }
//            else{
//                throw new ApplicationException(500,"WorkOrder is neither ACCEPTED NOR REJECTED...!!!");
//            }
        }
        else if(wOStatus.equalsIgnoreCase("REOPENED")){
            int pWOID= this.woDeliveryAcceptanceDAO.getWOForParentWOID(woID);
            reasons= this.woDeliveryAcceptanceDAO.getStatusReasons(pWOID);
            reasons.setIsParent(true);
            if(reasons.getDeliveryComment()==null || reasons.getDeliveryComment().equalsIgnoreCase("NULL") || reasons.getDeliveryComment().length()==0 ){
                reasons.setDeliveryComment(" ");
            }
            else if(reasons.getUserComments()==null || reasons.getUserComments().equalsIgnoreCase("NULL") || reasons.getUserComments().length()==0 ){
                reasons.setUserComments(" ");
            }
            else if(reasons.getDeliveryReason()==null || reasons.getDeliveryReason().equalsIgnoreCase("NULL") || reasons.getDeliveryReason().length()==0){
                reasons.setDeliveryReason(" ");
            }
            else if(reasons.getUserDeliveryStatus()==null || reasons.getUserDeliveryStatus().equalsIgnoreCase("NULL") || reasons.getUserDeliveryStatus().length()==0){
                reasons.setUserDeliveryStatus(" ");
            }
            else if(reasons.getDeliveryStatus()==null || reasons.getDeliveryStatus().equalsIgnoreCase("NULL") || reasons.getDeliveryStatus().length()==0){
                reasons.setDeliveryStatus(" ");
            }
        }else {
        	List<WorkOrderModel> woDetails = woPlanService.getWorkOrderDetails(woID);
            reasons.setIsParent(false);
            
        	reasons.setWoStatus(woDetails.get(0).getStatus());
            reasons.setUserDeliveryStatus("NA");
        	reasons.setUserReason("NA");
        	reasons.setUserComments("NA");

        	reasons.setDeliveryStatus("NA");
        	reasons.setDeliveryReason("NA");
        	reasons.setDeliveryComment("NA");
        	
        }
        return reasons;
    }

    /**
     * 
     * @param projectID
     * @param projectName
     * @param signumID
     * @param role
     * @return List<ClosedWODetailsModel>
     */
	public List<ClosedWODetailsModel> getacceptedWODetails(int projectID, String projectName, String signumID,
			String role) {
		
			return this.woDeliveryAcceptanceDAO.getacceptedWODetails(projectID, projectName,signumID,role);
	}

//    public List<String> getStandardFailureReasons(String failureStatus) {
//        return woDeliveryAcceptanceDAO.getStandardFailureReasons(failureStatus);
//    }

    
     
    
}
