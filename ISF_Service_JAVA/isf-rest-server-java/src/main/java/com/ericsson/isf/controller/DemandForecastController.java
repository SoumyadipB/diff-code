/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.model.DemandForecasSaveDetailsModel;
import com.ericsson.isf.model.DemandForecastDetailModel;
import com.ericsson.isf.model.DemandForecastFulfillmentModel;
import com.ericsson.isf.model.DemandForecastModel;

import com.ericsson.isf.model.ProjectScopeDetailMappingModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.VendorTechModel;
import com.ericsson.isf.service.DemandForecastService;

/**
 *
 * @author edhhklu
 */
@RestController
@RequestMapping("/demandForecast")
public class DemandForecastController {
    
    private static final Logger LOG = LoggerFactory.getLogger(DemandForecastController.class);
    
    @Autowired
    private DemandForecastService demandForecastService;
    
    
    
    @RequestMapping(value = "/getDemandSummary", method = RequestMethod.GET)
    public List<Map<String, Object>> getDemandSummary(@RequestParam(value="signum",required=false) String signum
    		,@RequestParam(value="startDate",required=false)  @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate
    		,@RequestParam(value="pageSize",required=false) Integer pageSize
    		,@RequestParam("role") String role
    		,@RequestParam("marketArea") String marketArea) {
    	return demandForecastService.getDemandSummary(signum, startDate, pageSize,role,marketArea);
    }
    
    
    @RequestMapping(value = "/isDraftAllowed", method = RequestMethod.GET)
    public boolean isDraftAllowed(
    		@RequestParam("projectId") Integer projectId
    		,@RequestParam("role") String role) {
    	return demandForecastService.isDraftAllowed(projectId,role);
    }
    
    @RequestMapping(value = "/getDemandForecastDetails", method = RequestMethod.GET)
    public List<DemandForecastDetailModel> getDemandForecastDetails(@RequestParam(value="signum",required=false) String signum
    		,@RequestParam(value="projectId",required=false) Integer projectId
    		,@RequestParam(value="startDate",required=false)  @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate
    		,@RequestParam(value="pageSize",required=false) Integer pageSize
    		,@RequestParam("role") String role) {
    	return demandForecastService.getDemandForecastDetails(signum,projectId, startDate, pageSize,role);
    			
        
    }
    
    @RequestMapping(value = "/saveDemandForecastDetails", method = RequestMethod.POST)
    public Response<Void> saveDemandForecastDetails(@RequestBody DemandForecasSaveDetailsModel saverequest) throws ParseException {
    	
    	return demandForecastService.saveDemandForecastDetails(saverequest); 			
        
    }
    @RequestMapping(value = "/getDemandForecastDetailsByfilter", method = RequestMethod.GET)
    public List<DemandForecastFulfillmentModel> getDemandForecastDetailsByfilter(
			@RequestParam(value = "projectID", required = false) Integer projectId,
			@RequestParam(value = "positionStatus", required = false) String positionStatus,
			@RequestHeader("MarketArea") String marketArea
			) {
    	return demandForecastService.getDemandForecastDetailsByfilter(projectId, positionStatus, marketArea); 			
    }
    
    @RequestMapping(value = "/saveDemandSummary", method = RequestMethod.POST)
    public Response<Void> saveDemandSummaryDraft(@RequestBody List<DemandForecastModel> demandSummarySaveRequest) {
    	return demandForecastService.saveDemandSummaryDraft(demandSummarySaveRequest,true);
    }
    
    @RequestMapping(value = "/getDomainSubdomain", method = RequestMethod.GET)
    public List<Map<String, Object>> getDomainSubdomain(@RequestParam(value="projectScopeID",required=false) String projectScopeID,@RequestParam(value="serviceAreaID",required=false)String serviceAreaID) {
    	return demandForecastService.getDomainSubdomain(projectScopeID,serviceAreaID);
    }
    
    @RequestMapping(value = "/getTechnologies", method = RequestMethod.GET)
    public List<Map<String, Object>> getTechnologies(@RequestParam(value="projectScopeID",required=false) String projectScopeID,@RequestParam(value="serviceAreaID",required=false)String serviceAreaID
    		,@RequestParam(value="domainID",required=false) String domainID) {
    	return demandForecastService.getTechnologies(projectScopeID,serviceAreaID,domainID);
    }
    
    @RequestMapping(value = "/getScopeDetails", method = RequestMethod.GET)
    public List<Map<String, Object>> getScopeDetails(@RequestParam(value="projectId",required=false) String projectId) {
    	return demandForecastService.getScopeDetails(projectId);
    }
    
    /* //TODO Code cleanup
     * @RequestMapping(value = "/migrateDemandforecastDate", method = RequestMethod.GET)
    public void migrateDemandforecastDate() {
    	demandForecastService.migrateDemandforecastDate();
    }*/
    @RequestMapping(value = "/getAllScopeDetailsByProject", method = RequestMethod.GET)
    public List<ProjectScopeDetailMappingModel> getAllScopeDetailsByProject(@RequestParam(value="projectId",required=true) String projectId  ){
        return this.demandForecastService.getAllScopeDetailsByProject(projectId);
    }
    
    @RequestMapping(value = "/getResourceRequestedBySubScope", method = RequestMethod.GET)
    public List<Map<String, Object>> getResourceRequestedBySubScope(@RequestParam(value="projectID", required=true) int projectID,@RequestParam(value="projectScopeDetailID", required=true) int projectScopeDetailID){
    	return this.demandForecastService.getResourceRequestedBySubScope(projectID,projectScopeDetailID);
    }
    
    
	/**
	 * @author emntiuk
	 * @apiNote: This API is used to Fetch Vendor Technology Combination for Demand
	 * @param start
	 * @param length
	 * @param term
	 * @return ResponseEntity<Response<List<VendorTechModel>>>
	 */
	@GetMapping(value = "/getVendorTechCombination")
	public ResponseEntity<Response<List<VendorTechModel>>> getVendorTechCombination(@RequestParam("start") int start,
			@RequestParam("length") int length, @RequestParam("term") String term) {
		LOG.info("getVendorTechCombination:Success");
		return demandForecastService.getVendorTechCombination(start, length, term);
	}
}
