/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.ericsson.isf.model.CRManagementModel;
import com.ericsson.isf.model.CRManagementResultModel;
import com.ericsson.isf.model.ChangeManagement;
import com.ericsson.isf.model.ProposedResourcesModel;
import com.ericsson.isf.service.CRManagementService;

/**
 *
 * @author esanpup
 */
@RestController
@RequestMapping("/cRManagement")
public class CRManagementController {
    
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);
    
    @Autowired
    private CRManagementService changeManagementService;
    
    
//    @RequestMapping(value = "/getCRDetails", method = RequestMethod.GET)
//    public List<CRManagementResultModel> getCRDetails() {
//       List<CRManagementResultModel> CRList= this.changeManagementService.getCRDetails();
//       LOG.info("TBL_Change_Request: SUCCESS");
//       return CRList;
//    }
    
    @RequestMapping(value = "/getCRDetails/{viewType}/{signumID}", method = RequestMethod.GET)
    public List<CRManagementResultModel> getCRDetails(@PathVariable ("viewType") String viewType,
                                                      @PathVariable ("signumID") String signumID,@RequestHeader("MarketArea") String marketArea) {
       List<CRManagementResultModel> CRList= this.changeManagementService.getCRDetails(viewType,signumID,marketArea);
       LOG.info("TBL_ChangeRequestPosition: SUCCESS");
       return CRList;
    }
 
 /*
   //TODO code cleanup
   @RequestMapping(value = "/getCRDetailsByCRID/{cRID}", method = RequestMethod.GET)
    public CRManagementResultModel getCRDetails(@PathVariable("cRID") int cRID) {
       LOG.info("TBL_Change_Request:SUCCESS ");
       return this.changeManagementService.getCRDetailsByCRID(cRID);
    }*/
    
    
    @RequestMapping(value = "/acceptCr", method = RequestMethod.POST)
    public boolean acceptCR(@RequestBody List <CRManagementModel>  crList) {
    	LOG.info("Accept CR success");
        return this.changeManagementService.acceptCR(crList);
        
    }
    @RequestMapping(value = "/rejectCr", method = RequestMethod.POST)
    public boolean rejectCR(@RequestBody List <CRManagementModel>  crList) {
    	LOG.info("Reject CR success");
        return this.changeManagementService.rejectCR(crList);
        
    }
    
    
    
    /*@RequestMapping(value = "/acceptCRByID/{signumID}/{startDate}/{endDate}/{comments}", method = RequestMethod.POST)
    public void acceptCRByID(@RequestBody CRManagementModel crModel, 
                            @PathVariable("signumID") String signumID,
                            @PathVariable("startDate") String startDate,
                            @PathVariable("endDate") String endDate,
                            @PathVariable("comments") String comments) {
        this.changeManagementService.acceptCRByID(crModel.getcRID(),signumID,startDate,endDate,comments);
        LOG.info("TBL_Change_Request:SUCCESS ");
    }
    
    @RequestMapping(value = "/rejectCRByID/{signumID}/{comments}", method = RequestMethod.POST)
    public void acceptCRByID(@RequestBody CRManagementModel crModel,
                             @PathVariable("signumID") String signumID,
                             @PathVariable("comments") String comments ) {
        
       this.changeManagementService.rejectCRByID(crModel.getcRID(),signumID,comments);
       LOG.info("TBL_Change_Request:SUCCESS "+ crModel.getcRID().get(0));
    }  */ 
    
    @RequestMapping(value = "/getRRIDFlag/{rRID}", method = RequestMethod.GET)
    public Map<String,Object> getRRIDFlag(@PathVariable("rRID") int rRID){
    	Map<String,Object> rRIDFlag = this.changeManagementService.getRRIDFlag(rRID);
        LOG.info("TBL_Change_Request: SUCCESS");
        return rRIDFlag;
    }
    
    @RequestMapping(value = "/deleteRpID/{rPID}", method = RequestMethod.GET)
    public boolean deleteRpID(@PathVariable("rPID") int rPID, @RequestParam(value="signum",required=false) String signum){
    	boolean rRIDFlag = this.changeManagementService.deleteRpID(rPID, signum);
        LOG.info("TBL_Change_Request: SUCCESS");
        return rRIDFlag;
    }
    
    @RequestMapping(value = "/updateCrStatus/{crID}", method = RequestMethod.GET)
    public boolean updateCrStatus(@PathVariable("crID") int crID,@RequestHeader("Signum") String signum){
        Boolean rRIDFlag = this.changeManagementService.updateCrStatus(crID,signum);
        LOG.info("TBL_Change_Request: SUCCESS");
        return rRIDFlag;
    }
 
/*
   //TODO code cleanup  
    @RequestMapping(value = "/updateWorkEffortStatus/{rPID}", method = RequestMethod.GET)
    public boolean updateWorkEffortStatus(@PathVariable("rPID") int rPID){
        Boolean rRIDFlag = this.changeManagementService.updateWorkEffortStatus(rPID);
        LOG.info("TBL_Change_Request: SUCCESS");
        return rRIDFlag;
    }*/
    
    @RequestMapping(value = "/raiseChangeManagment", method = RequestMethod.POST)
    public Map<Integer,Boolean> raiseChangeManagment(@RequestBody ChangeManagement changeManagement) throws ParseException{
        return changeManagementService.raiseChangeManagment(changeManagement);
    }
    
    @RequestMapping(value = "/getPositionsAndProposedResources/{status}/{signum}/{projectID}", method = RequestMethod.GET)
    public List<ProposedResourcesModel> getPositionsAndProposedResources(@PathVariable("status") String status,
                                                                                                     @PathVariable("signum") String signum,@PathVariable("projectID") String projectID,@RequestHeader("MarketArea") String marketArea){
		 return this.changeManagementService.getPositionsAndProposedResources(status,signum, projectID,marketArea);
	       
    }
    
 /*  //TODO code cleanup 
  @RequestMapping(value = "/getApprovedResourceList/{signum}", method = RequestMethod.GET)
    public List<ProposedResourcesModel> getApprovedResourceList(@PathVariable("signum") String signum){
        List<ProposedResourcesModel> listDetails = this.changeManagementService.getApprovedResourceList(signum);
        LOG.info("List of Approved Resources: Success");
        return listDetails;
    }*/
    
    @RequestMapping(value = "/updateRPDates/{rPID}/{startDate}/{endDate}/{hours}/{signum}/{reason}", method = RequestMethod.POST)
    public void updateRPDates(@PathVariable("rPID") int rPID, @PathVariable("startDate") String startDate,
        @PathVariable("hours") int hours,@PathVariable("endDate") String endDate, @PathVariable("signum") String signum,@PathVariable("reason") String reason){
        changeManagementService.updateRPDates(rPID,startDate,endDate,hours,signum,reason);
    }
    @RequestMapping(value = "/getReason", method = RequestMethod.GET)
    public List<Map<String,Object>> getReason(){
        return this.changeManagementService.getReason();
    }
}
   
