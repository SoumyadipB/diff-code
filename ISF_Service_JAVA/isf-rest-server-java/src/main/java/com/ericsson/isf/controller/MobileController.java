package com.ericsson.isf.controller;


import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.model.FieldUtilityResourceMappingModel;
import com.ericsson.isf.model.MobileNotificationModel;
import com.ericsson.isf.model.NodeNameValidationModel;
import com.ericsson.isf.model.AuditDataModel;
import com.ericsson.isf.model.CreateSrResponse;
import com.ericsson.isf.model.CreateWorkOrderModel;
import com.ericsson.isf.model.ProjectScopeModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.ServiceRequestModel;
import com.ericsson.isf.model.WorkOrderDetailsModel;
import com.ericsson.isf.security.aes.Decrypt;
import com.ericsson.isf.security.aes.Encrypt;
import com.ericsson.isf.service.MobileService;
import com.ericsson.isf.service.audit.AuditEnabled;



@RestController
@RequestMapping("/mobileController")
public class MobileController {
	//1
	private static final Logger LOG = LoggerFactory.getLogger(MobileController.class);
	@Autowired
	MobileService  mobileService;
	
	
	@Encrypt
	@AuditEnabled
	@RequestMapping(value = "/createServiceRequest", method = RequestMethod.POST)
	public Response<CreateSrResponse> createServiceRequest(@Decrypt @RequestBody CreateWorkOrderModel createWorkOrderModel,@RequestHeader("role")String role){
		return mobileService.createServiceRequest(createWorkOrderModel, role);
	}
	
	@RequestMapping(value = "/getFUDeliverablesByProject/{projectID}", method = RequestMethod.GET)
	public Response<List<ProjectScopeModel>> getFUDeliverablesByProject(@Decrypt @PathVariable("projectID") int projectID,@RequestParam("term") String term){
		return mobileService.getFUDeliverablesByProject(projectID,term);
	}
	
	/**
	 * @author emntiuk
	 * @purpose This API is used to get the Node Names on the basis of projectID and Node Type in Mobile APP.
	 * @param projectID
	 * @param type
	 * @param term
	 * @return ResponseEntity<Response<List<String>>>
	 */
	@RequestMapping(value = "/getNodeNamesByProject", method = RequestMethod.GET)
	public ResponseEntity<Response<List<NodeNameValidationModel>>> getNodeNamesByProject(
			@RequestParam(value="projectID", required=true) int projectID, 
			@RequestParam (value = "type", required= true) String type,
			@RequestParam("term") String term) {
		
		return mobileService.getNodeNamesByProject(projectID, type, term);
	}
	
	/**
	 * API Name: mobileController/getNodeTypeByProjectID
	 * @author elkpain
	 * @purpose This API gets Node Type on the basis of Project ID.
	 * @param projectID
	 * @return ResponseEntity<Response<List<String>>>
	 */
	@RequestMapping(value = "/getNodeTypeByProjectID", method = RequestMethod.GET)
	public  ResponseEntity<Response<List<NodeNameValidationModel>>> getNodeTypeByProjectID(@RequestParam(value="projectID",required=true) Integer projectID) {
		
		LOG.info("getNodeTypeByProjectID:Success");
		return this.mobileService.getNodeTypeByProjectID(projectID);
}
	
	
	/**
	 * 
	 * @author epnwsia
	 * @param  signum
	 * @param  role
	 * @return List<FieldUtilityResourceMappingModel>
	 * 
	 */
	@RequestMapping(value = "/getFUProjectsBySignum", method = RequestMethod.GET)
	public  ResponseEntity<Response<List<FieldUtilityResourceMappingModel>>> getFUProjectsBySignum(@RequestHeader("Signum") String signum, 
			@RequestHeader(value = "Role", required = false) String role)
	{
		LOG.info("FieldUtilityResourceMappingModel LIST: SUCCESS ");
		return mobileService.getFUProjectsBySignum(signum, role);
	}
	
	
	@RequestMapping(value = "/getServiceRequestsBySignum", method = RequestMethod.GET)
	public  Response<List<ServiceRequestModel>> getServiceRequestsBySignum(@RequestHeader("Signum") String signum,
			@RequestParam("status") String status,
			@RequestParam("range") String  range, @RequestParam("offset") int  offset, @RequestParam("length") int  length){
		LOG.info("getServiceRequestsBySignum : SUCCESS ");
		return mobileService.getServiceRequestsBySignum(signum, status, range, offset, length);
	}
	
	@Encrypt
	@RequestMapping(value = "/addServiceRequestComment", method = RequestMethod.POST)
	public ResponseEntity<Response<Void>> addServiceRequestComment(@Decrypt @RequestBody AuditDataModel commentModel) {
		LOG.info("Add Comment:Success");
		return mobileService.addServiceRequestComment(commentModel);
	}
	
	@RequestMapping(value = "/getAuditDataForServiceRequest", method = RequestMethod.GET)
	public Response<List<AuditDataModel>> getAuditDataForServiceRequest(@RequestParam("srid") int srid,
			@RequestParam(value="woid") Optional<Integer> woid,
			@RequestParam(value="start",required=false) Integer start,@RequestParam(value="length",required=false) Integer length,
			@RequestParam(value="searchString",required=false) String searchString ){
		return mobileService.getAuditDataForServiceRequest(srid,woid, start, length, searchString);
	}
	
	
	@AuditEnabled
	@RequestMapping(value = "/createServiceRequestSecured", method = RequestMethod.POST)
	public Response<CreateSrResponse> createServiceRequestSecured(@Decrypt @RequestBody CreateWorkOrderModel createWorkOrderModel,@RequestHeader("role")String role){
		return mobileService.createServiceRequest(createWorkOrderModel, role);
	}
	
	@RequestMapping(value = "/getEncrypted", method = RequestMethod.GET)
	public String getEncrypted(@RequestBody String text){
		return mobileService.getEncrypted(text);
	}
	
	@RequestMapping(value = "/getDecrypted", method = RequestMethod.GET)
	public String getDecrypted(@RequestBody String encryptedText){
		return mobileService.getDecrypted(encryptedText);
	}
	
	/**
	 * purpose: This API is used for only testing purpose to check how much time deliverable API take without any term.
	 * @param projectID
	 * @return
	 */
	@RequestMapping(value = "/getAllFUDeliverablesByProjectWithoutAnyTerm/{projectID}", method = RequestMethod.GET)
	public Response<List<ProjectScopeModel>> getAllFUDeliverablesByProjectWithoutAnyTerm(@PathVariable("projectID") int projectID){
		return mobileService.getAllFUDeliverablesByProjectWithoutAnyTerm(projectID);
	}
	
	/**
	 * API Name mobileController/getSRDetailsBySRID
	 * purpose: This API is used to Fetch SR Details by SRID.
	 * @param srID
	 * @return
	 */
	@Encrypt
	@RequestMapping(value = "/getSRDetailsBySRID", method = RequestMethod.GET)
	public ResponseEntity<Response<ServiceRequestModel>> getSRDetailsBySRID(
			@RequestParam(value="srID", required=true) int srID ){
				return mobileService.getSRDetailsBySRID(srID);
	}
	
	
	@Encrypt
	@RequestMapping(value = "/getMobileNotifications", method = RequestMethod.GET)
	public Response<List<MobileNotificationModel>> getMobileNotifications(@RequestHeader("Signum") String signum,
			 @RequestParam("start") int  start, @RequestParam("length") int  length){
		return mobileService.getMobileNotifications(signum, start, length);
}
	
	/**
	 * API Name mobileController/getWODetailsBySRID
	 * purpose: This API is used to Fetch WO Details by SRID.
	 * @param srID
	 * @return
	 */
	@Encrypt
	@RequestMapping(value = "/getWODetailsBySRID", method = RequestMethod.GET)
	public ResponseEntity<Response<List<WorkOrderDetailsModel>>> getWODetailsBySRID(
			@RequestParam(value="srID", required=true) int srID ){
				return mobileService.getWODetailsBySRID(srID);
	}
	
}
