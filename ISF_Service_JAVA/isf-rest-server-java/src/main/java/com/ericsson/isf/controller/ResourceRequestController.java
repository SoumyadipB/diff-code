/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.ericsson.isf.dao.DemandManagementDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.ericsson.isf.model.CompetenceSubModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DownloadTemplateModel;

import com.ericsson.isf.model.EngineerDetailsModel;
import com.ericsson.isf.model.ResigReqModel;
import com.ericsson.isf.model.ResoucePositionFmModel;
import com.ericsson.isf.model.ResourceCalandarModel;
import com.ericsson.isf.model.ResourceModel;
import com.ericsson.isf.model.ResourcePositionWorkEffortModel;
import com.ericsson.isf.model.ResourceRequestModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.SearchResourceByFilterModel;
import com.ericsson.isf.service.ResourceRequestService;

import net.sf.json.JSONArray;

/**
 *
 * @author eshalku
 */
@RestController
@RequestMapping("/resourceManagement")
public class ResourceRequestController {

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

	@Autowired /* Bind to bean/pojo */
	private ResourceRequestService resourceRequestService;

	@Autowired
	private DemandManagementDAO demandManagementDAO;

	@RequestMapping(value = "/SearchResourcesByFilter", method = RequestMethod.POST)
	public List<HashMap<String, Object>> SearchResourcesByFilter(
			@RequestBody SearchResourceByFilterModel searchResourceByFilterModel) {
		List<HashMap<String, Object>> resourcelist = this.resourceRequestService
				.searchResourcesByFilter(searchResourceByFilterModel);

		if (!resourcelist.isEmpty()) {
			LOG.info(" SUCCESS");
			return resourcelist;
		} else {
			throw new ApplicationException(500, "No  data Exists !! ");
		}
	}

	@RequestMapping(value = "/SearchResourcesByFilters", method = RequestMethod.POST)
	public List<HashMap<String, Object>> SearchResourcesByFilters(
			@RequestBody SearchResourceByFilterModel searchResourceByFilterModel) {
		List<HashMap<String, Object>> resourcelist = this.resourceRequestService
				.searchResourcesByFilters(searchResourceByFilterModel);

		if (!resourcelist.isEmpty()) {
			LOG.info(" SUCCESS");
			return resourcelist;
		} else {
			throw new ApplicationException(500, "No  data Exists !! ");
		}
	}

	@RequestMapping(value = "/GetResourceRequestsByFilter", method = RequestMethod.GET)
	public List<LinkedHashMap<String, Object>> getResourceRequestsByFilter(
			@RequestParam(value = "ProjectID", required = false) Integer ProjectID,
			@RequestParam(value = "DomainID", required = false) Integer DomainID,
			@RequestParam(value = "SubDomainID", required = false) Integer SubDomainID,
			@RequestParam(value = "SubServiceAreaID", required = false) Integer SubServiceAreaID,
			@RequestParam(value = "TechnologyID", required = false) Integer TechnologyID,
			@RequestParam(value = "PositionStatus", required = false) String PositionStatus,
			@RequestParam(value = "AllocatedResource", required = false) String AllocatedResource,
			@RequestParam(value = "spoc", required = false) String spoc,
			@RequestHeader("MarketArea") String marketArea) {
		List<LinkedHashMap<String, Object>> resourcelist = this.resourceRequestService.getResourceRequestsByFilter(
				ProjectID, DomainID, SubDomainID, SubServiceAreaID, TechnologyID, PositionStatus, AllocatedResource,
				spoc, marketArea);

		return resourcelist;
	}

	@RequestMapping(value = "/getPositionsAndAllocatedResources", method = RequestMethod.GET)
	public List<ResoucePositionFmModel> getPositionsAndAllocatedResources(
			@RequestParam(value = "RRID", required = false) Integer rrID,
			@RequestParam(value = "positionStatus", required = false) String positionStatus,
			@RequestParam(value = "spoc", required = false) String spoc,
			@RequestParam(value = "projectId", required = false) Integer projectID,
			@RequestHeader("MarketArea") String marketArea) {
		List<ResoucePositionFmModel> parDetail = this.resourceRequestService
				.getPositionsAndAllocatedResources(rrID, positionStatus, spoc, marketArea, projectID);
		return parDetail;
	}

	@RequestMapping(value = "/getAllPositions", method = RequestMethod.GET)
	public List<ResoucePositionFmModel> getAllPositions(
			@RequestParam(value = "RRID", required = false) Integer rrID,
			@RequestParam(value = "positionStatus", required = false) String positionStatus,
			@RequestParam(value = "spoc", required = false) String spoc,
			@RequestParam(value = "projectId", required = false) Integer projectID,
			@RequestHeader("MarketArea") String marketArea) {
		return this.resourceRequestService.getAllPositions(rrID, positionStatus, spoc, marketArea, projectID);
	}

	@RequestMapping(value = "/getAllPositionsCount", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllPositionsCount(@RequestParam(value = "RRID", required = false) Integer rrID,
			@RequestParam(value = "projectId", required = false) Integer projectID) {
		return this.resourceRequestService.getAllPositionsCount(projectID);
	}

	private static final String VENDOR_COL_NAME = "vendor";

	@RequestMapping(value = "/getDemandRequestDetail/{RRID}", method = RequestMethod.GET)
	public List<ResourceRequestModel> getDemandRequestDetail(@PathVariable("RRID") int rrID) {

		List<ResourceRequestModel> rrDetails = this.resourceRequestService.getDemandRequestDetail(rrID);
		// getResourceRequestWEffort
		List<ResourcePositionWorkEffortModel> rpefList = this.resourceRequestService
				.getResourceRequestWEffortDetails(rrID);

		List<CompetenceSubModel> competenceList = this.resourceRequestService.getCompetenceSubDetails(rrID);
		List<HashMap<String, Object>> certificateList = this.resourceRequestService.getCertificateSubDetails(rrID);
		int i = 1;

		if (competenceList.isEmpty()) {
			CompetenceSubModel obj = new CompetenceSubModel();
			competenceList.add(obj);
		}
		if (certificateList.isEmpty()) {
			HashMap<String, Object> dummyCerti = new HashMap<String, Object>();
			dummyCerti.put("CertificateType", null);
			dummyCerti.put("CertificateID", null);
			dummyCerti.put("CertificateName", null);
			certificateList.add(dummyCerti);
		}

		for (ResourcePositionWorkEffortModel rpef : rpefList) {
			rpef.setRpefId(i);
			i++;
			List<HashMap<String, Object>> resourcePositionList = this.resourceRequestService
					.getResourcePositionSubList(rpef, rrID);
			// List<HashMap<String,Object>>
			// cmpLevelList=this.resourceRequestService.getCompetenceLevel(cmp.getCompetenceId());
			rpef.setResourcePositionList(resourcePositionList);
			// cmp.setCompetenceLevelList(cmpLevelList);
		}

		for (ResourceRequestModel rrModel : rrDetails) {
			rrModel.setRpefList(rpefList);
			rrModel.setCompetenceList(competenceList);
			rrModel.setCertificateList(certificateList);
			rrModel.setVendors(demandManagementDAO.getVendorNamesByRrid(rrID));

		}
		return rrDetails;
	}

	/*
	 * //TODO Code Cleanup
	 * 
	 * @RequestMapping(value = "/SearchDemandRequests", method = RequestMethod.GET)
	 * public List<HashMap<String,Object>>
	 * SearchDemandRequests(@RequestParam(value="ProjectScopeID",required=false)
	 * Integer ProjectScopeID,@RequestParam (value="DomainID" ,required=false)
	 * Integer DomainID,@RequestParam (value="SubServiceAreaID",required=false)
	 * Integer SubServiceAreaID,@RequestParam (value="TechnologyID",required=false)
	 * Integer TechnologyID,@RequestParam (value="PositionStatus",required=false)
	 * String PositionStatus,@RequestParam
	 * (value="AllocatedResource",required=false) String AllocatedResource) {
	 * 
	 * List<HashMap<String, Object>> resourcelist =
	 * this.resourceRequestService.SearchDemandRequests( ProjectScopeID,DomainID
	 * ,SubServiceAreaID,TechnologyID);
	 * 
	 * if(!resourcelist.isEmpty()){ LOG.info(" SUCCESS"); return resourcelist;
	 * }else{ throw new ApplicationException(500, "No  data Exists !! "); } }
	 */

	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/CreateResourceReplacement", method =
	 * RequestMethod.POST) public boolean CreateResourceReplacement(@RequestBody
	 * ChangeRequestPositionModel changeRequestPositionModel) { return
	 * this.resourceRequestService.CreateResourceReplacement(
	 * changeRequestPositionModel );
	 * 
	 * }
	 */

	@RequestMapping(value = "/ModifyDemandRequest", method = RequestMethod.POST)
	public boolean ModifyDemandRequest(@RequestBody ResourceModel resourceModel) {
		return this.resourceRequestService.ModifyDemandRequest(resourceModel);

	}

	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/changePositionStatus", method = RequestMethod.POST)
	 * public boolean changePositionStatus(@RequestBody ResourcePositionModel
	 * resourcePositionModel) {
	 * 
	 * return
	 * this.resourceRequestService.changePositionStatus(resourcePositionModel); }
	 */

	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/searchDemandPositions", method = RequestMethod.GET)
	 * public List<ResourcePositionModel>
	 * searchDemandPositions(@RequestParam(value="domainID",required=false) Integer
	 * domainID,@RequestParam (value="serviceAreaID",required=false) Integer
	 * serviceAreaID,@RequestParam (value="technologyID",required=false) Integer
	 * technologyID,@RequestParam (value="positionStatus",required=false) String
	 * positionStatus,@RequestParam (value="resourceSignum",required=false) String
	 * resourceSignum) { List<ResourcePositionModel> rplist =
	 * this.resourceRequestService.searchDemandPositions(domainID,serviceAreaID,
	 * technologyID,positionStatus,resourceSignum);
	 * 
	 * if(!rplist.isEmpty()){ LOG.info(" SUCCESS"); return rplist; }else{ throw new
	 * ApplicationException(500, "No  data Exists !! "); } }
	 */

	/**
	 * @author ekmbuma
	 * 
	 * @param signumID
	 * @param projectID
	 * @param startDate
	 * @param endDate
	 * @param term
	 * @return Map<String, Object>
	 * @throws ParseException
	 */
	@RequestMapping(value = "/getWOResourceLevelDetails/{signumID}/{projectID}/{startDate}/{endDate}", method = RequestMethod.POST)
	public Map<String, Object> getWOResourceLevelDetails(HttpServletRequest request,
			@PathVariable("signumID") String signumID, @PathVariable("projectID") Integer projectID,
			@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate,
			@RequestParam(value = "term", required = false) String term) throws ParseException {

		DataTableRequest req = new DataTableRequest(request);
		LOG.info("getWOResourceLevelDetails : SUCCESS");
		return resourceRequestService.getWOResourceLevelDetails(signumID, projectID, startDate, endDate, term, req);
	}

	@RequestMapping(value = "/getResourceWOLevelDetails/{signumID}", method = RequestMethod.GET)
	public JSONArray getResourceWOLevelDetails(@PathVariable("signumID") String signumID) {
		return this.resourceRequestService.getResourceWOLevelDetails(signumID);
	}

	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/searchChangeRequests", method = RequestMethod.GET)
	 * public List<HashMap<String,Object>>
	 * searchChangeRequests(@RequestParam(value="projectID",required=false) Integer
	 * projectID,@RequestParam (value="changeRequestType",required=false) String
	 * changeRequestType,@RequestParam (value="positionStatus",required=false)
	 * String positionStatus,@RequestParam (value="resourceSignum",required=false)
	 * String resourceSignum) { List<HashMap<String,Object>> crlist =
	 * this.resourceRequestService.searchChangeRequests(projectID,changeRequestType,
	 * positionStatus,resourceSignum);
	 * 
	 * if(!crlist.isEmpty()){ LOG.info(" SUCCESS"); return crlist; }else{ throw new
	 * ApplicationException(500, "No  data Exists !! "); } }
	 */

	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/modifyDemandPosition", method = RequestMethod.POST)
	 * public boolean modifyDemandPosition(@RequestBody ModifyDemandModel
	 * modifyDemandModel) { return
	 * this.resourceRequestService.modifyDemandPosition(modifyDemandModel);
	 * 
	 * }
	 */

	@RequestMapping(value = "/getBookedResourceBySignum", method = RequestMethod.GET)
	public Map<String, Object> getBookedResourceBySignum(@RequestParam(value = "signum") String signum,
			@RequestParam(value = "weid") Integer weid) {
		return this.resourceRequestService.getBookedResourceBySignum(signum, weid);
	}

	@RequestMapping(value = "/updateResignedResource", method = RequestMethod.POST)
	public boolean updateResignedResource(@RequestBody ResigReqModel resModel) {
		this.resourceRequestService.updateResignedResource(resModel);
		return true;
	}

	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/getAllCertifications", method = RequestMethod.GET)
	 * public List<Map<String, Object>>
	 * getAllCertifications(@RequestParam(value="issuer",required=false) String
	 * issuer) { return this.resourceRequestService.getAllCertifications(issuer); }
	 */
	@RequestMapping(value = "/getResourceCalander", method = RequestMethod.GET)
	public ResponseEntity<Response<List<ResourceCalandarModel>>> getResourceCalander(
			@RequestParam(value = "signum", required = false) String signum,
			@RequestParam(value = "startdate", required = false) String startdate,
			@RequestParam(value = "enddate", required = false) String enddate) {
		return this.resourceRequestService.getResourceCalander(signum, startdate, enddate);
	}

	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/getJobRoles", method = RequestMethod.GET) public
	 * List<Map<String, Object>> getJobRoles() { return
	 * this.resourceRequestService.getJobRoles(); }
	 */
	@RequestMapping(value = "/getJobStages", method = RequestMethod.GET)
	public List<Map<String, Object>> getJobStages() {
		return this.resourceRequestService.getJobStages();
	}
	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/getOnsiteLocations", method = RequestMethod.GET)
	 * public List<Map<String, Object>> getOnsiteLocations() { return
	 * this.resourceRequestService.getOnsiteLocations(); }
	 */

	@RequestMapping(value = "/getBacklogWorkOrders", method = RequestMethod.GET)
	public List<Map<String, Object>> getBacklogWorkOrders(
			@RequestParam(value = "signum", required = true) String signum) {
		return this.resourceRequestService.getBacklogWorkOrders(signum);
	}
	/*
	 * //TODO code cleanup
	 * 
	 * @RequestMapping(value = "/getBacklogWorkOrdersForProject", method =
	 * RequestMethod.GET) public List<Map<String, Object>>
	 * getBacklogWorkOrdersForProject(@RequestParam(value="projectID",required=true)
	 * Integer projectID ) { return
	 * this.resourceRequestService.getBacklogWorkOrdersForProject(projectID); }
	 */

	@RequestMapping(value = "/getSubServiceAreaPCode", method = RequestMethod.GET)
	public Map<String, Object> getSubServiceAreaPCode(@RequestParam(value = "serviceAreaID") int serviceAreaID) {
		return this.resourceRequestService.getSubServiceAreaPCode(serviceAreaID);
	}

	@RequestMapping(value = "/getJobRoles", method = RequestMethod.GET)
	public List<Map<String, Object>> getJobRoles() {
		return this.resourceRequestService.getJobRoles();
	}

	@RequestMapping(value = "/getAllCertifications", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllCertifications(
			@RequestParam(value = "Issuer", required = false) String Issuer) {
		return this.resourceRequestService.getAllCertifications(Issuer);
	}

	@RequestMapping(value = "/getUniqueIssuer", method = RequestMethod.GET)
	public List<Map<String, Object>> getUniqueIssuer() {
		return this.resourceRequestService.getUniqueIssuer();
	}

	@RequestMapping(value = "/getOnsiteLocations", method = RequestMethod.GET)
	public List<Map<String, Object>> getOnsiteLocations() {
		return this.resourceRequestService.getOnsiteLocations();
	}

	@RequestMapping(value = "/getPositionStatus", method = RequestMethod.GET)
	public List<Map<String, Object>> getPositionStatus() {
		return this.resourceRequestService.getPositionStatus();
	}

	@RequestMapping(value = "/getFilteredCompetences", method = RequestMethod.GET)
	public List<Map<String, Object>> getFilteredCompetences(
			@RequestParam(value = "competenceString") String competenceString) {
		return this.resourceRequestService.getFilteredCompetences(competenceString);
	}

	/**
	 * @author EKMRAVU
	 * 
	 * @param projectID
	 * @param startDate
	 * @param endDate
	 * @param term
	 * @return List<Map<String, Object>>
	 * @throws ParseException
	 */
	@RequestMapping(value = "/getSignumsFilteredForEngEngagement", method = RequestMethod.GET)
	public List<Map<String, Object>> getSignumsFilteredForEngEngagement(
			@RequestParam(value = "projectID", required = false) Integer projectID,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "term", required = false) String term) throws ParseException {
		return resourceRequestService.getSignumsFilteredForEngEngagement(projectID, startDate, endDate, term);
	}

	/**
	 * @author ekmbuma
	 * 
	 * @param signumID
	 * @param projectID
	 * @param startDate
	 * @param endDate
	 * @param term
	 * @return DownloadTemplateModel
	 * @throws ParseException
	 */
	@RequestMapping(value = "/downlaodEngEngagementDataInExcel/{signumID}/{projectID}/{startDate}/{endDate}", method = RequestMethod.GET)
	public DownloadTemplateModel downlaodEngEngagementDataInExcel(@PathVariable("signumID") String signumID,
			@PathVariable("projectID") Integer projectID, @PathVariable("startDate") String startDate,
			@PathVariable("endDate") String endDate, @RequestParam(value = "term", required = false) String term)
			throws ParseException {

		return resourceRequestService.downlaodEngEngagementDataInExcel(signumID, projectID, startDate, endDate, term);
	}

	/**
	 * @author ekmbuma
	 * 
	 * @param signumID
	 * @param projectID
	 * @param startDate
	 * @param endDate
	 * @param term
	 * @return Map<String, Object>
	 * @throws ParseException
	 */
	@RequestMapping(value = "/getEEDashboardData/{signumID}/{projectID}/{startDate}/{endDate}", method = RequestMethod.POST)
	public Map<String, Object> getEEDashboardData(HttpServletRequest request, @PathVariable("signumID") String signumID,
			@PathVariable("projectID") Integer projectID, @PathVariable("startDate") String startDate,
			@PathVariable("endDate") String endDate, @RequestParam(value = "term", required = false) String term)
			throws ParseException {

		DataTableRequest req = new DataTableRequest(request);
		LOG.info("getEEDashboardData : SUCCESS");
		return resourceRequestService.getEEDashboardData(signumID, projectID, startDate, endDate, term, req);
	}
	
	/**
	 * @author ekmravu
	 * @param signumID
	 * @param projectID
	 * @param startDate
	 * @param endDate
	 * @return Map<String, Object>
	 * @throws ParseException
	 */
	@RequestMapping(value = "/getEngineerDetails", method = RequestMethod.POST)
	public ResponseEntity<Response<EngineerDetailsModel>> getEngineerDetailsNew(
			@RequestParam(value = "signumID") String signumID,
			@RequestParam(value = "projectID", required = false) Integer projectID,
			@RequestParam(value = "selectedDate") String selectedDate

	) throws ParseException {
		LOG.info("getEngineerDetails : SUCCESS");
		return resourceRequestService.getEngineerDetails(signumID, projectID, selectedDate);
	}

	@RequestMapping(value = "/downlaodEEDashboardData/{signumID}/{projectID}/{startDate}/{endDate}", method = RequestMethod.GET)
	public DownloadTemplateModel downlaodEEDashboardData(@PathVariable("signumID") String signumID,
			@PathVariable("projectID") Integer projectID, @PathVariable("startDate") String startDate,
			@PathVariable("endDate") String endDate, @RequestParam(value = "term", required = false) String term)
			throws ParseException {

		return resourceRequestService.downlaodEEDashboardData(signumID, projectID, startDate, endDate, term);
	}
	
}
