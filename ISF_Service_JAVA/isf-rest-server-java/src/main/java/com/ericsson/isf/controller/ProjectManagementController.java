/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ericsson.isf.model.ExternalSourceModel;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.ericsson.isf.model.ActivityModel;
import com.ericsson.isf.model.ActivityScopeModel;
import com.ericsson.isf.model.AdhocTypeModel;
import com.ericsson.isf.model.CountryModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DeliveryAcceptanceModel;
import com.ericsson.isf.model.DeliveryResponsibleModel;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.ExternalAppReferenceModel;
import com.ericsson.isf.model.ProjectAllDetailsModel;
import com.ericsson.isf.model.ProjectComponentModel;
import com.ericsson.isf.model.ProjectCreationModel;
import com.ericsson.isf.model.ProjectDeliverableUnitModel;
import com.ericsson.isf.model.ProjectDetailsModel;
import com.ericsson.isf.model.ProjectDocumentModel;
import com.ericsson.isf.model.ProjectFilterModel;
import com.ericsson.isf.model.ProjectScopeDetailModel;
import com.ericsson.isf.model.ProjectScopeModel;
import com.ericsson.isf.model.ProjectSpecificToolModel;
import com.ericsson.isf.model.ProjectsModel;
import com.ericsson.isf.model.ProjectsTableModel;
import com.ericsson.isf.model.RPMModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.WorkFlowFilterModel;
import com.ericsson.isf.model.WorkflowProficiencyModel;
import com.ericsson.isf.service.AccessManagementService;
import com.ericsson.isf.service.ActivityScopeService;
import com.ericsson.isf.service.OpportunityService;
import com.ericsson.isf.service.ProjectScopeDetailService;
import com.ericsson.isf.service.ProjectScopeService;
import com.ericsson.isf.service.ProjectService;

/**
 *
 * @author eabhmoj
 */
@RestController
@RequestMapping("/projectManagement")
public class ProjectManagementController {

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

	@Autowired
	OpportunityService opportunityService;

	@Autowired
	ProjectScopeService projectScopeService;

	@Autowired
	ProjectScopeDetailService projectScopeDetailService;

	@Autowired
	ActivityScopeService activityScopeService;

	@Autowired
	ProjectService projectService;

	@Autowired /* Bind to bean/pojo */
	AccessManagementService accessManagementService;

	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/getOpportunityDetail", method = RequestMethod.GET)
	 * public List<OpportunityModel> getOpportunityDetails() {
	 * List<OpportunityModel> oppDetails=
	 * this.opportunityService.getOpportunityDetails(); return oppDetails; }
	 */

	/**
	 * @ModifiedBy ekmbuma
	 * @param projectScopeModel
	 * @return Response<String>
	 */
	@RequestMapping(value = "/SaveScope", method = RequestMethod.POST)
	public Response<String> SaveScope(@RequestBody ProjectScopeModel projectScopeModel) {
		LOG.info("TBL_PROJECTSCOPE: SUCCESS");
		return this.projectScopeService.saveScope(projectScopeModel);
	}

	@RequestMapping(value = "/SaveActivityScope", method = RequestMethod.POST)
	public int SaveActivityScope(@RequestBody ActivityScopeModel activityScopeModel) {
		return this.activityScopeService.saveActivityScope(activityScopeModel);
		// LOG.info("TBL_PROJECTSCOPE: SUCCESS");
		// return new ResponseEntity<String>("Successfully Saved Activity Scope",
		// HttpStatus.OK);
	}

	@RequestMapping(value = "/SaveProjectScopeDetail", method = RequestMethod.POST)
	public ResponseEntity<String> SaveProjectScopeDetail(@RequestBody ProjectScopeDetailModel projectScopeDetailModel) {
		this.projectScopeDetailService.saveProjectScopeDetail(projectScopeDetailModel);
		LOG.info("TBL_PROJECTSCOPE: SUCCESS");
		return new ResponseEntity<String>("Successfully Saved Project Scope Details", HttpStatus.OK);
	}

	@RequestMapping(value = "/DeleteScope/{scopeID}/{lastModifiedBy}", method = RequestMethod.POST)
	public ResponseEntity<String> DeleteScope(@PathVariable("scopeID") int scopeID,
			@PathVariable("lastModifiedBy") String lastModifiedBy) {
		if (scopeID != 0) {
			this.projectScopeService.DeleteScope(scopeID, lastModifiedBy);
			LOG.info("TBL_PROJECTSCOPE: SUCCESS");
			return new ResponseEntity<String>("Successfully Deleted Scope", HttpStatus.OK);
		} else {
			throw new ApplicationException(500, "Invalid input... ScopeID cannot be 0 !!!");
		}
	}

	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value =
	 * "/DeleteActivityScope/{activityScopeID}/{lastModifiedBy}", method =
	 * RequestMethod.POST) public ResponseEntity<String>
	 * DeleteActivityScope(@PathVariable ("activityScopeID") int
	 * activityScopeID,@PathVariable ("lastModifiedBy") String lastModifiedBy) {
	 * if(activityScopeID !=0){
	 * this.activityScopeService.DeleteActivityScope(activityScopeID,lastModifiedBy)
	 * ; LOG.info("TBL_PROJECTSCOPE: SUCCESS"); return new
	 * ResponseEntity<String>("Successfully Deleted Activity Scope", HttpStatus.OK);
	 * } else{ throw new ApplicationException(500,
	 * "Invalid input... ActivityScopeID cannot be 0 !!!"); } }
	 */

	@RequestMapping(value = "/DeleteProjectScopeDetails/{ProjectScopeDetailsID}/{lastModifiedBy}", method = RequestMethod.POST)
	public ResponseEntity<String> DeleteProjectScopeDetails(
			@PathVariable("ProjectScopeDetailsID") int ProjectScopeDetailsID,
			@PathVariable("lastModifiedBy") String lastModifiedBy) {
		if (ProjectScopeDetailsID != 0) {
			this.projectScopeDetailService.DeleteProjectScopeDetail(ProjectScopeDetailsID, lastModifiedBy);
			LOG.info("TBL_PROJECTSCOPE: SUCCESS");
			return new ResponseEntity<String>("Successfully Deleted Project Scope Details", HttpStatus.OK);
		} else {
			throw new ApplicationException(500, "Invalid input... ProjectScopeDetailsID cannot be 0 !!!");
		}
	}

	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/getStatus", method = RequestMethod.GET) public
	 * List<ProjectsModel> getStatus() { return this.projectService.getStatus(); }
	 */

	@RequestMapping(value = "/getProjectByFilters", method = RequestMethod.GET)
	public List<ProjectsModel> getProjectByFilters(
			@RequestParam(value = "projectID", required = false) Integer projectID,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "countryID", required = false) Integer countryID,
			@RequestParam(value = "customerID", required = false) Integer customerID,
			@RequestParam(value = "marketAreaID", required = false) Integer marketAreaID,
			@RequestParam(value = "serviceAreaID", required = false) Integer serviceAreaID,
			@RequestHeader(value = "Role", required = false) String role, @RequestHeader("Signum") String signum) {
		ProjectFilterModel projectFilterModel = new ProjectFilterModel();
		projectFilterModel.setCountryID(countryID);
		projectFilterModel.setCustomerID(customerID);
		projectFilterModel.setMarketAreaID(marketAreaID);
		projectFilterModel.setProjectID(projectID);
		projectFilterModel.setStatus(status);
		projectFilterModel.setServiceAreaID(serviceAreaID);
		projectFilterModel.setRole(role);
		projectFilterModel.setSignum(signum);
		return this.projectService.getProjectByFilters(projectFilterModel);
	}

	@RequestMapping(value = "/getDashboardProjects", method = RequestMethod.GET)
	public List<ProjectsModel> getProjectByFilters(@RequestHeader("MarketArea") String marketArea,
			@RequestHeader("Role") String role, @RequestHeader("Signum") String signum) {
		return this.projectService.getDashboardProject(marketArea, role, signum);
	}

	@RequestMapping(value = "/addDeliveryResponsible", method = RequestMethod.POST)
	public ResponseEntity<Response<Void>> addDeliveryResponsible(@RequestBody DeliveryResponsibleModel deliveryResponsibleModel) {
		
		LOG.info("Add Delivery Responsible:Success");
		return this.projectService.addDeliveryResponsible(deliveryResponsibleModel);
	}

	@RequestMapping(value = "/getDeliveryResponsibleByProject/{projectID}", method = RequestMethod.GET)
	public List<DeliveryResponsibleModel> getDeliveryResponsibleByProject(@PathVariable int projectID) {
		LOG.info("Delivery Responsible:Success");
		return this.projectService.getDeliveryResponsibleByProject(projectID);
	}

	@RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
	public void deleteProject(@RequestBody DeliveryResponsibleModel deliveryResponsibleModel) {
		this.projectService.deleteProject(deliveryResponsibleModel);
		LOG.info("Add Delivery Responsible:Success");
	}

	@RequestMapping(value = "/getProjectAcceptance/{signum}", method = RequestMethod.GET)
	public Response<List<HashMap<String, Object>>> getProjectAcceptance(@PathVariable String signum,
			@RequestHeader(value = "MarketArea",required = false) String marketArea,
			@RequestHeader(value = "Role", required = false) String role) {
		LOG.info("get Project Acceptance:Success");
		return this.projectService.getProjectAcceptance(signum, marketArea, role);

	}
	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/getCountries", method = RequestMethod.GET) public
	 * List<Map<String,Object>> getCountries(){ return
	 * this.projectService.getCountries(); }
	 */

	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/getCountriesbyMarketAreaID", method =
	 * RequestMethod.GET) public List<Map<String,Object>>
	 * getCountriesbyMarketAreaID(@RequestParam(value="marketAreaID",required=false)
	 * String MarketAreaID ){ return
	 * this.projectService.getCountriesbyMarketAreaID(MarketAreaID); }
	 */

	// TODO code cleanup
	@RequestMapping(value = "/getCustomers", method = RequestMethod.GET)
	public List<Map<String, Object>> getCustomers(
			@RequestParam(value = "countryID", required = false) String countryID) {
		return this.projectService.getCustomers(countryID);
	}

	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/getAllProjects", method = RequestMethod.GET) public
	 * List<Map<String,Object>>
	 * getAllProjects(@RequestParam(value="marketAreaID",required=false) String
	 * marketAreaID ,@RequestParam(value="countryID",required=false) String
	 * countryID ,@RequestParam(value="signum",required=false) String signum
	 * ,@RequestParam(value="status",required=false) String status ){ return
	 * this.projectService.getAllProjects(marketAreaID ,countryID ,signum ,status);
	 * }
	 */

	@RequestMapping(value = "/getAllScopeDetailsByProject", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllProjects(
			@RequestParam(value = "projectId", required = true) String projectId) {
		return this.projectScopeDetailService.getAllScopeDetailsByProject(projectId);
	}

	// TODO code cleanup
	@RequestMapping(value = "/getDemandOwningCompanies", method = RequestMethod.GET)
	public List<Map<String, Object>> getDemandOwningCompanies() {
		return this.projectScopeDetailService.getDemandOwningCompanies();
	}

	// TODO code cleanup
	@RequestMapping(value = "/getMarketAreas", method = RequestMethod.GET)
	public List<Map<String, Object>> getMarketAreas() {
		return this.projectScopeDetailService.getMarketAreas();
	}

	// TODO code cleanup

	@RequestMapping(value = "/getOpportunities", method = RequestMethod.GET)
	public List<Map<String, Object>> getOpportunities(@RequestHeader("MarketArea") String marketArea) {
		return this.projectScopeDetailService.getOpportunities(marketArea);
	}

	// TODO code cleanup
	@RequestMapping(value = "/getOpportunityDetailsById", method = RequestMethod.GET)
	public Map<String, Object> getOpportunityDetails(
			@RequestParam(value = "opportunityID", required = true) String opportunityID) {
		return this.projectScopeDetailService.getOpportunityDetailsById(opportunityID);
	}

	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/getDesktopVersion", method = RequestMethod.GET)
	 * public String getDesktopVersion(@RequestParam(value="type",required=true)
	 * String type,@RequestParam(value="Signum",required=true) String signum ){
	 * return this.projectScopeDetailService.getDesktopVersion(type,signum); }
	 */
	@RequestMapping(value = "/deleteProjectComponents", method = RequestMethod.POST)
	public Map<String, Object> deleteProjectComponents(@RequestBody ProjectComponentModel projectComponentModel) {
		return this.projectService.deleteProjectComponents(projectComponentModel);
	}

	@RequestMapping(value = "/editProjectComponents", method = RequestMethod.POST)
	public Map<String, Object> editProjectComponents(@RequestBody ProjectComponentModel projectComponentModel) {
		return this.projectService.editProjectComponents(projectComponentModel);
	}

	@RequestMapping(value = "/updateEditProjectScope", method = RequestMethod.POST)
	public Response<Map<String, Object>> updateEditProjectScope(@RequestBody ProjectScopeModel projectScopeModel) {
		return this.projectService.updateEditProjectScope(projectScopeModel);
	}

	@RequestMapping(value = "/updateEditProjectScopeDetail", method = RequestMethod.POST)
	public Map<String, Object> updateEditProjectScopeDetail(
			@RequestBody ProjectScopeDetailModel projectScopeDetailModel) {
		return this.projectService.updateEditProjectScopeDetail(projectScopeDetailModel);
	}

	@RequestMapping(value = "/updateProject", method = RequestMethod.POST)
	public void updateProject(@RequestBody ProjectsTableModel projectsTableModel) {
		LOG.info("Method called");
		// projectsTableModel.setStartDate(new Date());
		// projectsTableModel.setEndDate(new Date());
		this.projectService.updateProject(projectsTableModel);

	}

	/**
	 * Api name:projectManagement/addExternalApplicationReference
	 * purpose: Api used to add external application under project configuration tab.
	 * @author ekarmuj
	 * @param externalRefModel
	 * @return ResponseEntity<Response<Void>>
	 */
	@RequestMapping(value = "/addExternalApplicationReference", method = RequestMethod.POST)
	public ResponseEntity<Response<Void>> addExternalApplicationReference(@RequestBody List<ExternalAppReferenceModel> externalRefModel) {

		return this.projectService.addExternalApplicationReference(externalRefModel);

	}

	@RequestMapping(value = "/getExternalApplicationReferencesByProjectId", method = RequestMethod.GET)
	public List<ExternalAppReferenceModel> getExternalApplicationReferencesByProjectId(
			@RequestParam(value = "projectId", required = false) Integer projectId) {
		return this.projectService.getExternalApplicationReferencesByProjectId(projectId);
	}

	@RequestMapping(value = "/updateStatusOfExternalReference", method = RequestMethod.GET)
	public void updateStatusOfExternalReference(@RequestParam(value = "isActive", required = true) boolean isActive,
			@RequestParam(value = "referenceId", required = true) Integer referenceId) {
		this.projectService.updateStatusOfExternalReference(isActive, referenceId);
	}

	@RequestMapping(value = "/getExternalProjects", method = RequestMethod.GET)
	public List<Map<String, Object>> getExternalProjects() {
		return this.projectService.getExternalProjects();
	}

	@RequestMapping(value = "/getRequestType", method = RequestMethod.GET)
	public List<Map<String, Object>> getRequestType() {
		return this.projectService.getRequestType();
	}

	@RequestMapping(value = "/getTools", method = RequestMethod.GET)
	public List<Map<String, Object>> getTools() {
		return this.projectService.getTools();
	}

	@RequestMapping(value = "/getToolLicenseType", method = RequestMethod.GET)
	public List<Map<String, Object>> getToolLicenseType() {
		return this.projectService.getToolLicenseType();
	}

	@RequestMapping(value = "/getToolLicenseOwner", method = RequestMethod.GET)
	public List<Map<String, Object>> getToolLicenseOwner() {
		return this.projectService.getToolLicenseOwner();
	}

	@RequestMapping(value = "/getAccessMethod", method = RequestMethod.GET)
	public List<Map<String, Object>> getAccessMethod() {
		return this.projectService.getAccessMethod();
	}

	@RequestMapping(value = "/saveProjectSpecificTools", method = RequestMethod.POST)
	public boolean saveProjectSpecificTools(@RequestBody ProjectSpecificToolModel projectToolModel) {
		return this.projectService.saveProjectSpecificTools(projectToolModel);
	}
	@RequestMapping(value = "/getProjectSpecificTools", method = RequestMethod.GET)
	public Response<List<Map<String, Object>>> getProjectSpecificTools(
			@RequestParam(value = "projectID", required = true) Integer projectID,
			@RequestParam(value = "isOnlyActiveRequired", required = false) Integer isOnlyActiveRequired) {
		return this.projectService.getProjectSpecificTools(projectID, isOnlyActiveRequired);
	}

	@RequestMapping(value = "/updateProjectSpecificTools", method = RequestMethod.POST)
	public void updateProjectSpecificTools(@RequestBody ProjectSpecificToolModel projectToolModel) {
		this.projectService.updateProjectSpecificTools(projectToolModel);
	}

	@RequestMapping(value = "/disableEnableTools", method = RequestMethod.POST)
	public void disableEnableTools(@RequestBody ProjectSpecificToolModel projectToolModel) {
		this.projectService.disableEnableTools(projectToolModel);
	}

	@RequestMapping(value = "/saveDeliveryAcceptance", method = RequestMethod.POST)
	public boolean saveDeliveryAcceptance(@RequestBody DeliveryAcceptanceModel deliveryAcceptanceModel) {
		return this.projectService.saveDeliveryAcceptance(deliveryAcceptanceModel);
	}

	@RequestMapping(value = "/disableDeliveryAcceptance", method = RequestMethod.POST)
	public void disableDeliveryAcceptance(
			@RequestParam(value = "deliveryAcceptanceID", required = true) Integer deliveryAcceptanceID,
			@RequestParam(value = "signumID", required = true) String signumID) {
		this.projectService.disableDeliveryAcceptance(deliveryAcceptanceID, signumID);
	}

	@RequestMapping(value = "/getDeliveryAcceptance/{projectID}", method = RequestMethod.GET)
	public List<Map<String, Object>> getDeliveryAcceptance(@PathVariable int projectID) {
		return this.projectService.getDeliveryAcceptance(projectID);
	}

	@RequestMapping(value = "/saveRPM", method = RequestMethod.POST)
	public boolean saveRPM(@RequestBody RPMModel rmpModel) {
		return this.projectService.saveRPM(rmpModel);
	}

	@RequestMapping(value = "/disableRPM", method = RequestMethod.POST)
	public void disableRPM(@RequestParam(value = "rpmID", required = true) Integer rpmID,
			@RequestParam(value = "signumID", required = true) String signumID) {
		this.projectService.disableRPM(rpmID, signumID);
	}

	@RequestMapping(value = "/getRPM/{projectID}", method = RequestMethod.GET)
	public List<Map<String, Object>> getRPM(@PathVariable int projectID) {
		return this.projectService.getRPM(projectID);
	}
	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/getDeliverableUnit", method = RequestMethod.GET)
	 * public List<Map<String,Object>> getDeliverableUnit(){ return
	 * this.projectService.getDeliverableUnit(); }
	 */

	@RequestMapping(value = "/closeProject/{projectID}", method = RequestMethod.GET)
	public Map<String, String> closeProject(@PathVariable int projectID, @RequestHeader("Signum") String signum) {
		return this.projectService.closeProject(projectID, signum);
	}

	@RequestMapping(value = "/saveDeliverableUnit", method = RequestMethod.POST)
	public boolean saveDeliverableUnit(@RequestBody ProjectDeliverableUnitModel projectDeliverableUnitModel) {
		return this.projectService.saveDeliverableUnit(projectDeliverableUnitModel);
	}

	@RequestMapping(value = "/getAllDeliverableUnit", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllDeliverableUnit(@RequestParam(value="term", required= false) String term) {
		return this.projectService.getAllDeliverableUnit(term);
	}

	@RequestMapping(value = "/deleteEditProjDelUnit", method = RequestMethod.POST)
	public ResponseEntity<String> deleteEditProjDelUnit(
			@RequestBody ProjectDeliverableUnitModel projectDeliverableUnitModel) {
		if (projectDeliverableUnitModel.getDeliverableUnitID() != 0) {
			this.projectService.deleteProjDelUnit(projectDeliverableUnitModel);
			return new ResponseEntity<String>("Successfully Deleted project delivery unit", HttpStatus.OK);
		} else {
			throw new ApplicationException(500, "Invalid input... DeliverableUnitID cannot be 0 !!!");
		}
	}

	/**
	 *
	 * Purpose - This API is used to get data on deliverable page.
	 * <p>
	 * Method Type - POST.
	 * <p>
	 *
	 */
	@PostMapping(value = "ScopeByProject")
	public Map<String, Object> scopeByProject(@RequestParam(value = "projectId", required = true) Integer projectId,
			@RequestParam(value = "deliverableStatus", required = false) String deliverableStatus
			, HttpServletRequest request) {
		
		DataTableRequest dataTableRequest = new DataTableRequest(request);
   	    LOG.info("ScopeByProject :SUCCESS");
		return this.projectScopeService.scopeByProject(projectId, deliverableStatus,dataTableRequest);
	}

	
	@RequestMapping(value = "/v1/ScopeByProject", method = RequestMethod.GET)
	public List<ProjectScopeModel> scopeByProject(@RequestParam(value = "projectId", required = true) Integer projectId,
			@RequestParam(value = "deliverableStatus", required = false) String deliverableStatus) {
		return this.projectScopeService.scopeByProject(projectId, deliverableStatus);
	}
	
	@RequestMapping(value = "/EditDeliverableUnit", method = RequestMethod.POST)
	public ResponseEntity<String> EditDeliverableUnit(
			@RequestBody ProjectDeliverableUnitModel projectDeliverableUnitModel) {
		if (projectDeliverableUnitModel.getDeliverableUnitID() != 0) {
			boolean response = this.projectService.EditDeliverableUnit(projectDeliverableUnitModel);
			if (!response)
				return new ResponseEntity<String>("Successfully Updated project delivery unit", HttpStatus.OK);
			else
				return new ResponseEntity<String>("Unit Name Already Exists", HttpStatus.OK);
		} else {
			throw new ApplicationException(500, "Invalid input... DeliverableUnitID cannot be 0 !!!");
		}
	}

	@RequestMapping(value = "/checkDTRCASubActivityId", method = RequestMethod.GET)
	public boolean checkDTRCASubActivityId(
			@RequestParam(value = "subActivityId", required = true) Integer subActivityId) {
		return this.activityScopeService.checkDTRCASubActivityId(subActivityId);

	}

	@RequestMapping(value = "/addExternalSource", method = RequestMethod.POST)
	public Response addExternalSource(@RequestBody ExternalSourceModel externalSource) {
		Response res = this.projectService.addExternalSource(externalSource);
//    	projectService.sendEmailOnKeyGeneration(externalSource);

		return res;
	}

	/**
	 * @param marketAreaID
	 * @return List<CountryModel>
	 */
	@RequestMapping(value = "/getCountrybyMarketAreaID", method = RequestMethod.GET)
	public List<CountryModel> getCountrybyMarketAreaID(@RequestParam(value = "marketAreaID") Integer marketAreaID) {
		return this.projectService.getCountrybyMarketAreaID(marketAreaID);
	}

	/**
	 * @param signum
	 * @param role
	 * @param marketArea
	 * @return List<ProjectDetailsModel>
	 */
	@RequestMapping(value = "/getProjectAndScopeDetailBySignum", method = RequestMethod.GET)
	public List<ProjectDetailsModel> getProjectAndScopeDetailBySignum(@RequestParam(value = "signum") String signum,
			@RequestParam(value = "role") String role, @RequestHeader(value = "marketArea") String marketArea) {
		return this.projectService.getProjectAndScopeDetailBySignum(signum, role, marketArea);
	}

	/**
	 * @param ProjectID
	 * @return ProjectAllDetailsModel
	 */
	@RequestMapping(value = "/getProjectDetails", method = RequestMethod.GET)
	public ProjectAllDetailsModel getProjectDetails(@RequestParam(value = "ProjectID") int ProjectID) {
		ProjectAllDetailsModel projectDetails = this.projectService.getProjectDetails(ProjectID);
		List<ProjectDocumentModel> projectDocuments = this.projectService.getProjectDocuments(ProjectID);

		projectDetails.setProjectDocuments(projectDocuments);

		return projectDetails;
	}

	/**
	 * @param MarketAreaID
	 * @param CountryID
	 * @param signum
	 * @param Status
	 * @param marketArea
	 * @param role
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping(value = "/getAllProjects", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllProjects(
			@RequestHeader(value = "MarketAreaID", required = false) String MarketAreaID,
			@RequestHeader(value = "CountryID", required = false) String CountryID,
			@RequestHeader(value = "signum", required = false) String signum,
			@RequestHeader(value = "Status", required = false) String Status,
			@RequestHeader(value = "marketArea", required = false) String marketArea,
			@RequestHeader(value = "role", required = false) String role) {

		return this.projectService.getAllProjects(MarketAreaID, CountryID, signum, Status, marketArea, role);
	}

	/**
	 * @param projectCreationModel
	 * @return Response<String>
	 */
	@RequestMapping(value = "/createProject", method = RequestMethod.POST)
	public Response<String> createProject(@RequestBody ProjectCreationModel projectCreationModel) {
		int projectID = this.projectService.createProject(projectCreationModel);
		Response<String> response = new Response<>();
		response.addFormMessage("Project is successfully created!");

		response.setResponseData(String.valueOf(projectID));
		return response;
	}
	
	/**
	 * 
	 * To show deliverables for details are added and have mapping done.
	 * 
	 * @param projectId
	 * @return List<ProjectScopeModel>
	 */
	@RequestMapping(value = "activeScopeByProject", method = RequestMethod.GET)
	public List<ProjectScopeModel> activeScopeByProject(@RequestParam(value = "projectId", required = true) Integer projectId) {

		return this.projectScopeService.activeScopeByProject(projectId);
	}
	
	/**
	 * Purpose :- to validate Activity to update scope detail
	 * 
	 * @author ekmbuma
	 * @param activityModelList
	 * @return Response<Boolean>
	 */
	@RequestMapping(value = "validateScopeDetailForActivitySubActivity", method = RequestMethod.POST)
	public Response<Boolean> validateScopeDetailForActivitySubActivity(@RequestBody List<ActivityModel> activityModelList) {
		
		return this.projectService.validateScopeDetailForActivitySubActivity(activityModelList);
	}
	
	/**
	 * Purpose:  This API is used to fetch WFID_WFName by ProjectID
	 * @author emntiuk
	 * @param projectID
	 * @param term
	 * @return
	 */
	@RequestMapping(value = "/getWorkFlowsByProjectID", method = RequestMethod.POST)
	public ResponseEntity<Response<List<WorkFlowFilterModel>>> getWorkFlowsByProjectID(@RequestParam(value="projectID", required=true) int projectID, @RequestParam("term") String term) {
		LOG.info("getWorkFlowsByProjectID :Success");
		return projectService.getWorkFlowsByProjectID(projectID,term);
	}

	/**
	 * Api Name: projectManagement/getEFWorkflowForSignumWFID
	 * Purpose: This Api used to get workflow proficiency details.
	 * @author ekarmuj
	 * @param workFlowProficiencyModelList
	 * @param projectID
	 * @return Map<String, Object>
	 */
	
	@RequestMapping(value = "/getEFWorkflowForSignumWFID", method = RequestMethod.POST)
	public Map<String, Object>  getEFWorkflowForSignumWFID(@RequestParam(value = "listOfSignum", required = true) List<String> listOfSignum,
			@RequestParam(value="projectID", required=true) int projectID,
			@RequestParam(value = "listOfWfid", required = true) List<Integer> listOfWfid,
			@RequestParam(value = "proficiencyStatus",required = true) String proficiencyStatus,
			HttpServletRequest request){
		return projectService.getEFWorkflowForSignumWFID(listOfSignum,listOfWfid,projectID,new DataTableRequest(request),proficiencyStatus);
		
	}
	
	/**
	 * Api Name: projectManagement/updateProficiency
	 * Purpose: This Api used to update the proficiency level
	 * @author ekarmuj
	 * @param workFlowProficiencyModelList
	 * @return ResponseEntity<Response<Void>>
	 */
	
	@RequestMapping(value = "/updateProficiency", method = RequestMethod.POST)
	public ResponseEntity<Response<Void>> updateProficiency(@RequestBody List<WorkflowProficiencyModel> workFlowProficiencyModelList,@RequestParam(value="projectID", required=true) int projectID){
		return projectService.updateProficiency(workFlowProficiencyModelList,projectID);
	}
	
	/**
	 * API Name:projectManagement/getScopeType
	 * @author elkpain
	 * @purpose gets Scope Type for all deliverables,if scope type flag is true.
	 * @param deliverableUnit
	 * @return ResponseEntity<Response<List<Map<String, Object>>>>
	 * @throws Exception 
	 */
	@GetMapping(value = "/getScopeType")
	public ResponseEntity<Response<List<Map<String, Object>>>> getScopeType(@RequestParam(value="deliverableUnit", required=true) int deliverableUnit) {
	
  	    LOG.info("getScopeType:SUCCESS");
  	    return projectService.getScopeType(deliverableUnit);

	}
	
	/**
	 * API Name:projectManagement/getMethodForDU
	 * @author elkpain
	 * @purpose gets Method for particular deliverables,whose method flag is true.
	 * @param deliverableUnit
	 * @return ResponseEntity<Response<List<Map<String, Object>>>>
	 * @throws Exception 
	 */
	@GetMapping(value = "/getMethodForDU")
	public ResponseEntity<Response<List<Map<String, Object>>>> getMethodForDU(@RequestParam(value="deliverableUnit", required=true) int deliverableUnit) {
	
  	    LOG.info("getMethodForDU:SUCCESS");
  	    return projectService.getMethodForDU(deliverableUnit);

	}
	
	/**
	 * API Name:projectManagement/getOperatorCount
	 * @author elkpain
	 * @purpose gets Operator Count for particular deliverables,whose operator count flag is true.
	 * @param deliverableUnit
	 * @return ResponseEntity<Response<List<Map<String, Object>>>>
	 * @throws Exception 
	 */
	@GetMapping(value = "/getOperatorCount")
	public ResponseEntity<Response<List<Map<String, Object>>>> getOperatorCount(@RequestParam(value="deliverableUnit", required=true) int deliverableUnit) {
	
  	    LOG.info("getOperatorCount:SUCCESS");
  	    return projectService.getOperatorCount(deliverableUnit);

	}
	
	/**
	 * API Name:projectManagement/getProjectFinancials
	 * @author elkpain
	 * @purpose gets Project Financials for particular deliverables,whose project financials flag is true.
	 * @param deliverableUnit
	 * @return ResponseEntity<Response<List<Map<String, Object>>>>
	 * @throws Exception 
	 */
	@GetMapping(value = "/getProjectFinancials")
	public ResponseEntity<Response<List<Map<String, Object>>>> getProjectFinancials(@RequestParam(value="deliverableUnit", required=true) int deliverableUnit) {
	
  	    LOG.info("getProjectFinancials:SUCCESS");
  	    return projectService.getProjectFinancials(deliverableUnit);

	}
	
	/**
	 * API Name:projectManagement/downloadDeliverableData
	 * @author elkpain
	 * @purpose downloads deliverable page data.
	 * @return DownloadTemplateModel
	 * @throws IOException 
	 */
	@RequestMapping(value = "/downloadDeliverableData", method = RequestMethod.GET)
	public DownloadTemplateModel downloadWorkHistoryData(
	            @RequestParam(value = "projectId", required = true) Integer projectId,
	            @RequestParam(value = "deliverableStatus", required = false) String deliverableStatus) throws IOException {
		
		LOG.info("downloadDeliverableData : Success");
		return this.projectService.downloadDeliverableData(projectId, deliverableStatus);
	}
	

	/**
	 * @param signum
	 * @param role
	 * @param marketArea
	 * @param activity
	 * @return List<ProjectDetailsModel>
	 */
	@RequestMapping(value = "/getAdhocTypes", method = RequestMethod.GET)
	public List<AdhocTypeModel> getAdhocTypes(@RequestParam(value = "signum") String signum,
			@RequestParam(value = "role") String role, @RequestHeader(value = "marketArea") String marketArea,
			@RequestParam(value = "activity") String activity) {
		return this.projectService.getAdhocTypes(signum, role, marketArea,activity);
	}
	
	
}
