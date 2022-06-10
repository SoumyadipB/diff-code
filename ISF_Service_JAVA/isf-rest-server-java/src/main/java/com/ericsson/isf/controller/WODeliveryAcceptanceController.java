/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;


import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.ericsson.isf.model.ClosedWODetailsModel;
import com.ericsson.isf.model.ReasonFromWOModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.WORelatedDetails;
import com.ericsson.isf.model.WorkOrderAcceptanceModel;
import com.ericsson.isf.service.WODeliveryAcceptanceService;
import com.ericsson.isf.service.audit.AuditEnabled;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author esanpup
 */
@RestController
@RequestMapping("/woDeliveryAcceptance")
public class WODeliveryAcceptanceController {
    
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @Autowired /*Bind to bean/pojo  */
    private WODeliveryAcceptanceService woDeliveryAcceptanceService;
    
    @RequestMapping(value = "/getAcceptanceRatings", method = RequestMethod.GET)
    public List<String> getAcceptanceRatings() {
        LOG.info("TBL_ACCEPTANCE_RATING: Success");
        return this.woDeliveryAcceptanceService.getAcceptanceRatings();
        
    }
    
    @AuditEnabled
    @RequestMapping(value = "/acceptWorkOrder", method = RequestMethod.POST)
    public void acceptWorkOrder(@RequestBody WorkOrderAcceptanceModel woAcceptanceModel) {
        LOG.info("TBL_WORK_ORDER_DELIVERY_ACCEPTANCE: Success");
        this.woDeliveryAcceptanceService.acceptWorkOrder(woAcceptanceModel);
        
    }
	
    @RequestMapping(value = "/sendWorkOrderForAcceptance", method = RequestMethod.POST)
    public void sendWorkOrderForAcceptance(@RequestBody WorkOrderAcceptanceModel woAcceptanceModel){
        
        this.woDeliveryAcceptanceService.sendWorkOrderForAcceptance(woAcceptanceModel);
        LOG.info("TBL_WORK_ORDER_DELIVERY_ACCEPTANCE: Success");
    }
    
    @AuditEnabled
    @RequestMapping(value = "/rejectWorkOrder", method = RequestMethod.POST)
    public ResponseEntity<Response<Void>> rejectWorkOrder(@RequestBody WorkOrderAcceptanceModel woAcceptanceModel){
        return this.woDeliveryAcceptanceService.rejectWorkOrder(woAcceptanceModel);
        
    }
    
    /**
     * 
     * @param projectID
     * @param projectName
     * @param signumID
     * @param role
     * @return List<ClosedWODetailsModel>
     */
    @RequestMapping(value = "/getclosedWODetails/{projectID}/{projectName}/{signumID}", method = RequestMethod.GET)
    public List<ClosedWODetailsModel> getclosedWODetails(@PathVariable("projectID") int projectID,
                                           @PathVariable("projectName") String projectName,
                                           @PathVariable ("signumID") String signumID,
                                           @RequestHeader(value="Role",required=false) String role) {

    	LOG.info("getclosedWODetails: Success");
    	return this.woDeliveryAcceptanceService.getclosedWODetails(projectID, projectName,signumID,role);
    }
    
    @RequestMapping(value = "/getReasonFromWO/{wOID}", method = RequestMethod.GET)
    public List<ReasonFromWOModel> getReasonFromWO(@PathVariable("wOID") int wOID) {
        List<ReasonFromWOModel> wODetails = this.woDeliveryAcceptanceService.getReasonFromWO(wOID);
        LOG.info("getReasonFromWO: Success");
        
        return wODetails;
    }
    
    @RequestMapping(value = "/getStatusReasons/{woID}", method = RequestMethod.GET)
    public WORelatedDetails getStatusReasons(@PathVariable("woID") int woID) {
        return this.woDeliveryAcceptanceService.getStatusReasons(woID);
    }
    
//    @RequestMapping(value = "/getStandardFailureReasons/{failureStatus}", method = RequestMethod.GET)
//    public List<String> getStandardFailureReasons(@PathVariable("failureStatus") String failureStatus  ) {
//        return this.woDeliveryAcceptanceService.getStandardFailureReasons(failureStatus);
//    }
    
    /**
     * 
     * @param projectID
     * @param projectName
     * @param signumID
     * @param role
     * @return List<ClosedWODetailsModel>
     */
    @RequestMapping(value = "/getacceptedWODetails/{projectID}/{projectName}/{signumID}", method = RequestMethod.GET)
    public List<ClosedWODetailsModel> getacceptedWODetails(@PathVariable("projectID") int projectID,
                                           @PathVariable("projectName") String projectName,
                                           @PathVariable ("signumID") String signumID,
                                           @RequestHeader(value="Role",required=false) String role) {
    	
        LOG.info("getclosedWODetails: Success");
    	return this.woDeliveryAcceptanceService.getacceptedWODetails(projectID, projectName,signumID,role);
    }
}
