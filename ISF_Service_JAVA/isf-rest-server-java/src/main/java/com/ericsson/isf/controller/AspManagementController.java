package com.ericsson.isf.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.model.AspAcceptRejectModel;
import com.ericsson.isf.model.AspExplorerModel;
import com.ericsson.isf.model.AspLoginModel;
import com.ericsson.isf.model.AspVendorModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.service.AspManagementService;


/**
*
* @author esudbhu
*/

@RestController
@RequestMapping("/aspManagement")

public class AspManagementController {
    
	    @Autowired /*Bind to bean/pojo  */
	    private AspManagementService aspService;
	    
	    @RequestMapping(value = "/getVendorDetailsByID", method = RequestMethod.GET)
	    public AspVendorModel getVendorDetailsByID(@RequestParam(value="vendorCode",required=true) String vendorCode) {
	    	return  this.aspService.getVendorDetailsByID(vendorCode);
	    }
	    
	    @RequestMapping(value = "/getAspExplorerForManager", method = RequestMethod.GET)
	    public List<AspExplorerModel> getAspExplorerForManager(@RequestParam(value="managerSignum",required=true) String managerSignum) {
	    	return aspService.getAspExplorerForManager(managerSignum);
	    }
	    
	    @RequestMapping(value = "/updateAspProfileAccess" , method = RequestMethod.POST)
	    public Response<Void> updateAspProfileAccess(@RequestBody AspAcceptRejectModel input) {
	    	return this.aspService.updateAspProfileAccess(input);
	    }
	    @RequestMapping(value = "/getAllAspVendors" , method = RequestMethod.GET)
	    public List<AspVendorModel> getAllAspVendors(){
	    	return aspService.getAllVendors();
	    }
	    
	    @RequestMapping(value = "/getAllActiveAspVendors" , method = RequestMethod.GET)
	    public List<AspVendorModel> getAllActiveAspVendorDetails(){
	    	return aspService.getAllActiveAspVendors();
	    }
	    
	    @RequestMapping(value = "/getAspDetailsBySignum" , method = RequestMethod.GET)
	    public AspLoginModel getAspDetailsBySignum(@RequestParam(value="signum",required=true) String signum){
	    	return aspService.getAspDetailsBySignum(signum);
	    }

	    @RequestMapping(value = "/getAspByVendor" , method = RequestMethod.GET)
	    public List<Map<String,Object>> getAspByVendor(@RequestParam(value="vendorCode",required=true) String vendorCode){
	    	return aspService.getAspByVendor(vendorCode);
	    }
	    
	    @RequestMapping(value = "/getAspByScope" , method = RequestMethod.GET)
	    public List<Map<String,Object>> getAspByScope(@RequestParam(value="projectScopeId",required=true) String projectScopedId){
	    	return aspService.getAspByScope(projectScopedId);
	    }
	    
	    @RequestMapping(value = "/getAspByWoid" , method = RequestMethod.GET)
	    public List<Map<String,Object>> getAspByWoid(@RequestParam(value="woId",required=true) String woId){
	    	return aspService.getAspByWoid(woId);
	    }
	    
	    @RequestMapping(value = "/getAllAspVendorDetails" , method = RequestMethod.GET)
	    public List<AspVendorModel> getAllAspVendorDetails(){
	    	return aspService.getAllAspVendorDetails();
	    }
	    
	  
	    
	    @RequestMapping(value = "/insertAspVendorDetails" , method = RequestMethod.POST)
	    public Response<Void> insertAspVendorDetails(@RequestBody AspVendorModel input) {
	    	return this.aspService.insertAspVendorDetails(input);
	    }
	    
	    @RequestMapping(value = "/updateAspVendorDetails" , method = RequestMethod.POST)
	    public Response<Void> updateAspVendorDetails(@RequestBody AspVendorModel input) {
	    	return this.aspService.updateAspVendorDetails(input);
	    }
	    
	    @RequestMapping(value = "/enableDisableAspVendorDetails" , method = RequestMethod.POST)
	    public Response<Void> enableDisableAspVendorDetails(@RequestParam(value="vendorCode") String vendorCode,@RequestParam(value="signum") String signum) {
	    	return this.aspService.enableDisableAspVendorDetails(vendorCode,signum);
	    }
	    
	    @RequestMapping(value = "/insertAspVendorDetailsFromFile" , method = RequestMethod.POST)
	    public Response<Void> insertAspVendorDetailsFromFile(@RequestParam("file") MultipartFile file) {
	    	return this.aspService.insertAspVendorDetailsFromFile(file);
	    }
}
