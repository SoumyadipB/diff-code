/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ericsson.isf.exception.ApplicationException;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.model.ActivityMasterModel;
import com.ericsson.isf.model.AdhocBookingProjectModel;
import com.ericsson.isf.model.AllocatedResourceModel;
import com.ericsson.isf.model.CountryModel;
import com.ericsson.isf.model.CustomerModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DeleteNEListModel;
import com.ericsson.isf.model.DomainModel;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.EmployeeBasicDetails;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.FeedbackNEPMModel;
import com.ericsson.isf.model.FeedbackStatusUpdateModel;
import com.ericsson.isf.model.FlowChartStepDetailsModel;
import com.ericsson.isf.model.GlobalUrlModel;
import com.ericsson.isf.model.InstantFeedbackModel;
import com.ericsson.isf.model.LeavePlanModel;
import com.ericsson.isf.model.LocalUrlModel;
import com.ericsson.isf.model.LogLevelModel;
import com.ericsson.isf.model.NetworkElementModel;
import com.ericsson.isf.model.NotificationLogModel;
import com.ericsson.isf.model.ProjectApprovalModel;
import com.ericsson.isf.model.ProjectFilterModel;
import com.ericsson.isf.model.ResourceRequestCertificationModel;
import com.ericsson.isf.model.ResourceRequestCompetenceModel;
import com.ericsson.isf.model.ResourceStatusModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.ScopeDetailsModel;
import com.ericsson.isf.model.ServeAreaModel;
import com.ericsson.isf.model.ServiceAreaModel;
import com.ericsson.isf.model.ShiftTimmingModel;
import com.ericsson.isf.model.ShiftTimmingModel2;
import com.ericsson.isf.model.SpocModel;
import com.ericsson.isf.model.StandardActivityModel;
import com.ericsson.isf.model.TaskModel;
import com.ericsson.isf.model.TaskToolModel;
import com.ericsson.isf.model.TechnologyModel;
import com.ericsson.isf.model.TimeZoneModel;
import com.ericsson.isf.model.UserFeedbackModel;
import com.ericsson.isf.model.VendorModel;
import com.ericsson.isf.model.WFStepInstructionModel;
import com.ericsson.isf.model.WorkFlowFeedbackActivityModel;
import com.ericsson.isf.model.WorkFlowFeedbackModel;
import com.ericsson.isf.model.WorkFlowFilterModel;
import com.ericsson.isf.model.WorkInstructionModel;
import com.ericsson.isf.service.ActivityMasterService;
import com.ericsson.isf.service.NetworkElements;
import com.ericsson.isf.service.ServeAreaService;
import com.ericsson.isf.util.AppConstants;

/**
 *
 * @author ekarath
 */

/**
 * @author ekarmuj
 *
 */
@RestController
@RequestMapping("/activityMaster")
public class ActivityMasterController {

	private static final Logger LOG = LoggerFactory.getLogger(ActivityMasterController.class);
	private static final String NETWORK_ENGINEER ="Network Engineer";
	private static final String DEFAULT_USER ="Default User";

	@Autowired /* Bind to bean/pojo */
	private ActivityMasterService activityMasterService;
	@Autowired /* Bind to bean/pojo */
	private ServeAreaService serveAreaService;

	/**
	 * 
	 * @param domainModel
	 */
	@RequestMapping(value = "/saveDomainDetails", method = RequestMethod.POST)
	public void saveDomainDetails(@RequestBody DomainModel domainModel) {

		activityMasterService.saveDomainDetails(domainModel);
		LOG.info("TBL_DOMAIN: Success");
	}

	/**
	 * 
	 * @param projectID
	 * @param signum
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "/getMaxTasksValueByProject/{ProjectID}/{signum}", method = RequestMethod.GET)
	public Map<String, Object> getMaxTasksValueByProject(@PathVariable("ProjectID") int projectID,
			@PathVariable("signum") String signum) {
		LOG.info("Success : getMaxTasksValueByProject");
		return activityMasterService.getMaxTasksValueByProject(projectID, signum);
	}

	/**
	 * 
	 * @param projectID
	 * @param signum
	 * @param maxManual
	 * @param maxAutomaic
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "/updateMaxTasksValueByProject/{ProjectID}/{signum}/{maxManual}/{maxAutomaic}", method = RequestMethod.POST)
	public Map<String, Object> updateMaxTasksValueByProject(@PathVariable("ProjectID") int projectID,
			@PathVariable("signum") String signum, @PathVariable("maxManual") int maxManual,
			@PathVariable("maxAutomaic") int maxAutomaic) {

		return activityMasterService.updateMaxTasksValueByProject(projectID, signum, maxManual, maxAutomaic);
	}

	/**
	 * 
	 * @param signum
	 * @param projectID
	 * @param servAreaID
	 * @return List<ServiceAreaModel>
	 */
	@RequestMapping(value = "/getServiceAreaDetailsBySignum", method = RequestMethod.GET)
	public List<ServiceAreaModel> getServiceAreaDetailsBySignum(
			@RequestParam(value = "signum", required = false) String signum,
			@RequestParam(value = "ProjectID", required = false) Integer projectID,
			@RequestParam(value = "ServAreaID", required = false) Integer servAreaID) {

		return activityMasterService.getServiceAreaDetailsBySignum(signum, projectID, servAreaID);
	}

	/**
	 * 
	 * @param projectID
	 * @param Domain
	 * @param subDomain
	 * @param technology
	 * @param elementType
	 * @return List<String>
	 */
	@RequestMapping(value = "/getNodeTypeByFilter/{ProjectID}/{Domain}/{SubDomain}/{Technology}/{ElementType}", method = RequestMethod.GET)
	public Response<List<String>> getNodeTypeByFilter(@PathVariable("ProjectID") int projectID,
			@PathVariable("Domain") String Domain, @PathVariable("SubDomain") String subDomain,
			@PathVariable("Technology") String technology, @PathVariable("ElementType") String elementType) {

		return activityMasterService.getNodeTypeByFilter(projectID, Domain, subDomain, technology, elementType);
	}

	/**
	 * 
	 * @param domainModel
	 */
	@RequestMapping(value = "/updateDomainDetails", method = RequestMethod.POST)
	public void updateDomainDetails(@RequestBody DomainModel domainModel) {

		activityMasterService.updateDomainDetails(domainModel);
		LOG.info("TBL_DOMAIN: Success");
	}

	/**
	 * 
	 * @param allocatedResourceModel
	 * @param projectId
	 * @return boolean
	 * @throws ParseException
	 */
	@RequestMapping(value = "/updateAllocatedResources", method = RequestMethod.POST)
	public boolean updateAllocatedResources(@RequestBody List<AllocatedResourceModel> allocatedResourceModel,
			@RequestParam(value = "projectId", required = false) Integer projectId) throws ParseException {

		return activityMasterService.updateAllocatedResources(allocatedResourceModel, projectId);
	}

	/**
	 * 
	 * @param allocatedResourceModel
	 * @param projectId
	 * @return boolean
	 * @throws ParseException
	 */
	@RequestMapping(value = "/updateProposedResources", method = RequestMethod.POST)
	public boolean updateProposedResources(@RequestBody List<AllocatedResourceModel> allocatedResourceModel,
			@RequestParam(value = "projectId", required = false) Integer projectId) throws ParseException {

		return activityMasterService.updateProposedResources(allocatedResourceModel, projectId);
	}

	/**
	 * 
	 * @param allocatedResourceModel
	 * @return boolean
	 * @throws ParseException
	 */
	@RequestMapping(value = "/cancelProposedResources", method = RequestMethod.POST)
	public boolean cancelProposedResources(@RequestBody List<AllocatedResourceModel> allocatedResourceModel)
			throws ParseException {

		return activityMasterService.cancelProposedResources(allocatedResourceModel);
	}

	/**
	 * 
	 * @param projectID
	 * @return List<ServeAreaModel>
	 */
	@RequestMapping(value = "/getServAreaDetails", method = RequestMethod.GET)
	public List<ServeAreaModel> getServeArea(@RequestParam(value = "projectID", required = false) Integer projectID) {

		LOG.info("TBL_SERVICEAREA: ServiceAreaList: SUCCESS");
		return serveAreaService.getServeArea(projectID);
	}

	/**
	 * 
	 * @param customerModel
	 */
	@RequestMapping(value = "/editCustomerName", method = RequestMethod.POST)
	public void editCustomerName(@RequestBody CustomerModel customerModel) {

		activityMasterService.editCustomerName(customerModel);
	}

	/**
	 * 
	 * @param projectID
	 * @return List<HashMap<String, Object>>
	 */
	@RequestMapping(value = "/getServiceAreaDetailsByProject", method = RequestMethod.GET)
	public List<HashMap<String, Object>> getServiceAreaDetailsByProject(
			@RequestParam(value = "projectID", required = false) Integer projectID) {

		LOG.info("TBL_SERVICEAREA: ServiceAreaList: SUCCESS");
		return serveAreaService.getServiceAreaDetailsByProject(projectID);
	}

	/**
	 * 
	 * @param countryID
	 * @param marketAreaID
	 * @return List<HashMap<String, Object>>
	 */
	@RequestMapping(value = "/getCustomerDetailsByMA", method = RequestMethod.GET)
	public List<HashMap<String, Object>> getCustomerDetailsByMA(
			@RequestParam(value = "countryID", required = false) Integer countryID,
			@RequestParam(value = "marketAreaID", required = false) Integer marketAreaID) {

		LOG.info(StringUtils.upperCase(AppConstants.SUCCESS));
		return serveAreaService.getCustomerDetailsByMA(countryID, marketAreaID);
	}

	/**
	 * 
	 * @param domainID
	 * @param signumID
	 */
	@RequestMapping(value = "/deleteDomainDetails/{domainID}/{signumID}", method = RequestMethod.GET)
	public void deleteDomainDetails(@PathVariable("domainID") int domainID, @PathVariable("signumID") String signumID) {

		activityMasterService.deleteDomainDetails(domainID, signumID);
		LOG.info("TBL_DOMAIN: Success " + domainID + AppConstants.CSV_CHAR_COMMA + signumID);
	}

	/**
	 * 
	 * @param projectID
	 * @param serviceAreaID
	 * @return List<DomainModel>
	 */
	@RequestMapping(value = "/getDomainDetails", method = RequestMethod.GET)
	public List<DomainModel> getDomainDetails(@RequestParam(value = "ProjectID", required = false) Integer projectID,
			@RequestParam(value = "ServiceAreaID", required = false) Integer serviceAreaID) {

		LOG.info(AppConstants.TBL_DOMAIN_LIST_SUCCESS);
		return activityMasterService.getDomainDetails(projectID, serviceAreaID);
	}

	/**
	 * 
	 * @param signum
	 * @param projectID
	 * @param serviceAreaID
	 * @return List<DomainModel>
	 */
	@RequestMapping(value = "/getDomainDetailsBySignum", method = RequestMethod.GET)
	public List<DomainModel> getDomainDetailsBySignum(@RequestParam(value = "signum", required = false) String signum,
			@RequestParam(value = "ProjectID", required = false) Integer projectID,
			@RequestParam(value = "ServiceAreaID", required = false) Integer serviceAreaID) {

		LOG.info(AppConstants.TBL_DOMAIN_LIST_SUCCESS);
		return activityMasterService.getDomainDetailsBySignum(signum, projectID, serviceAreaID);
	}

	/**
	 * 
	 * @param signum
	 * @return List<HashMap<String, Object>>
	 */
	@RequestMapping(value = "/getAllProjectsBySignum", method = RequestMethod.GET)
	public List<HashMap<String, Object>> getAllProjectsBySignum(
			@RequestParam(value = "signum", required = false) String signum) {

		LOG.info(AppConstants.TBL_DOMAIN_LIST_SUCCESS);
		return activityMasterService.getAllProjectsBySignum(signum);
	}

	/**
	 * 
	 * @param serviceAreaID
	 * @return List<DomainModel>
	 */
	@RequestMapping(value = "/getAllDomainDetailsByService", method = RequestMethod.GET)
	public Response<List<DomainModel>> getAllDomainDetailsByService(
			@RequestParam(value = "ServiceAreaID", required = false) Integer serviceAreaID) {

		LOG.info(AppConstants.TBL_DOMAIN_LIST_SUCCESS);
		return activityMasterService.getAllDomainDetailsByService(serviceAreaID);
	}

	/**
	 * 
	 * @param domainId
	 * @param serviceAreaID
	 * @return List<TechnologyModel>
	 */
	@RequestMapping(value = "/getTechnologyDetailsByDomain", method = RequestMethod.GET)
	public List<TechnologyModel> getTechnologyDetailsByDomain(
			@RequestParam(value = "domainId", required = false) Integer domainId,
			@RequestParam(value = "serviceAreaID", required = false) Integer serviceAreaID) {

		LOG.info(AppConstants.TBL_DOMAIN_LIST_SUCCESS);
		return activityMasterService.getTechnologyDetailsByDomain(domainId, serviceAreaID);
	}

	/**
	 * 
	 * @param domainID
	 * @param signumID
	 * @return List<DomainModel>
	 */
	@RequestMapping(value = "/getDomainDetailsByID/{domainID}/{signumID}", method = RequestMethod.GET)
	public List<DomainModel> getDomainDetailsByID(@PathVariable("domainID") int domainID,
			@PathVariable("signumID") String signumID) {

		LOG.info("TBL_DOMAIN : DomainID: " + domainID);
		return activityMasterService.getDomainDetailsByID(domainID, signumID);
	}

	/**
	 * 
	 * @param serviceAreaModel
	 */
	@RequestMapping(value = "/saveServiceAreaDetails", method = RequestMethod.POST)
	public void saveServiceAreaDetails(@RequestBody ServiceAreaModel serviceAreaModel) {

		activityMasterService.saveServiceAreaDetails(serviceAreaModel);
		LOG.info(AppConstants.TBL_SERVICEAREA_SUCCESS);
	}

	/**
	 * 
	 * @param serviceAreaModel
	 */
	@RequestMapping(value = "/updateServiceAreaDetails", method = RequestMethod.POST)
	public void updateServiceAreaDetails(@RequestBody ServiceAreaModel serviceAreaModel) {

		activityMasterService.updateServiceAreaDetails(serviceAreaModel);
		LOG.info(AppConstants.TBL_SERVICEAREA_SUCCESS);
	}

	/**
	 * 
	 * @param serviceAreaID
	 * @param signumID
	 */
	@RequestMapping(value = "/deleteServiceArea/{serviceAreaID}/{signumID}", method = RequestMethod.GET)
	public void deleteServiceArea(@PathVariable("serviceAreaID") int serviceAreaID,
			@PathVariable("signumID") String signumID) {

		LOG.info(AppConstants.TBL_SERVICEAREA_SUCCESS);
		activityMasterService.deleteServiceArea(serviceAreaID, signumID);
	}

	/**
	 * 
	 * @param projectID
	 * @param servAreaID
	 * @return List<ServiceAreaModel>
	 */
	@RequestMapping(value = "/getServiceAreaDetails", method = RequestMethod.GET)
	public List<ServiceAreaModel> getServiceAreaDetails(
			@RequestParam(value = "ProjectID", required = false) Integer projectID,
			@RequestParam(value = "ServAreaID", required = false) Integer servAreaID) {

		LOG.info("TBL_SERVICEAREA: Success: ");
		return activityMasterService.getServiceAreaDetails(projectID, servAreaID);
	}

	/**
	 * 
	 * @param serviceAreaID
	 * @param signumID
	 * @return List<ServiceAreaModel>
	 */
	@RequestMapping(value = "/getServiceAreaDetailsByID/{serviceAreaID}/{signumID}", method = RequestMethod.GET)
	public List<ServiceAreaModel> getServiceAreaDetailsByID(@PathVariable("serviceAreaID") int serviceAreaID,
			@PathVariable("signumID") String signumID) {

		LOG.info("TBL_SERVICEAREA: ServiceAreaList: Success: ");
		return activityMasterService.getServiceAreaDetailsByID(serviceAreaID, signumID);
	}

	/**
	 * 
	 * @param technologyModel
	 */
	@RequestMapping(value = "/saveTechnologyDetails", method = RequestMethod.POST)
	public void saveTechnologyDetails(@RequestBody TechnologyModel technologyModel) {

		LOG.info(AppConstants.TBL_TECHNOLOGY_SUCCESS);
		activityMasterService.saveTechnologyDetails(technologyModel);

	}

	/**
	 * 
	 * @param technologyModel
	 */
	@RequestMapping(value = "/updateTechnologyDetails", method = RequestMethod.POST)
	public void updateTechnologyDetails(@RequestBody TechnologyModel technologyModel) {

		activityMasterService.updateTechnologyDetails(technologyModel);
		LOG.info(AppConstants.TBL_TECHNOLOGY_SUCCESS);
	}

	/**
	 * 
	 * @param technologyID
	 * @param signumID
	 */
	@RequestMapping(value = "/deleteTechnology/{technologyID}/{signumID}", method = RequestMethod.GET)
	public void deleteTechnology(@PathVariable("technologyID") int technologyID,
			@PathVariable("signumID") String signumID) {

		LOG.info(AppConstants.TBL_TECHNOLOGY_SUCCESS);
		activityMasterService.deleteTechnology(technologyID, signumID);
	}

	/**
	 * 
	 * @param projectID
	 * @param domainID
	 * @return List<TechnologyModel>
	 */
	@RequestMapping(value = "/getTechnologyDetails", method = RequestMethod.GET)
	public List<TechnologyModel> getTechnologyDetails(
			@RequestParam(value = "projectID", required = false) Integer projectID,
			@RequestParam(value = "domainID", required = false) Integer domainID) {

		return activityMasterService.getTechnologyDetails(domainID, projectID);
	}

	/**
	 * 
	 * @param technologyID
	 * @param signumID
	 * @return List<TechnologyModel>
	 */
	@RequestMapping(value = "/getTechnologyDetailsByID/{technologyID}/{signumID}", method = RequestMethod.GET)
	public List<TechnologyModel> getTechnologyDetailsByID(@PathVariable("technologyID") int technologyID,
			@PathVariable("signumID") String signumID) {

		return activityMasterService.getTechnologyDetailsByID(technologyID, signumID);
	}

	/**
	 * 
	 * @param standardActivityModel
	 */
	@RequestMapping(value = "/saveActivityAndSubActivityDetails", method = RequestMethod.POST)
	public void saveActivityAndSubActivityDetails(@RequestBody StandardActivityModel standardActivityModel) {

		activityMasterService.saveActivityAndSubActivityDetails(standardActivityModel);
		LOG.info("TBL_SUBACTIVITY: SUCCESS");
	}

	/**
	 * 
	 * @param standardActivityModel
	 */
	@RequestMapping(value = "/updateActivityAndSubActivityDetails", method = RequestMethod.POST)
	public void updateActivityAndSubActivityDetails(@RequestBody StandardActivityModel standardActivityModel) {

		activityMasterService.updateActivityAndSubActivityDetails(standardActivityModel);
	}

	/**
	 * 
	 * @param subActivityID
	 * @param signumID
	 */
	@RequestMapping(value = "/deleteActivityAndSubActivity/{subActivityID}/{signumID}", method = RequestMethod.GET)
	public void deleteActivityAndSubActivity(@PathVariable("subActivityID") int subActivityID,
			@PathVariable("signumID") String signumID) {

		activityMasterService.deleteActivityAndSubActivity(subActivityID, signumID);
	}

	/**
	 * 
	 * @param domainID
	 * @param serviceAreaID
	 * @param technologyID
	 * @return List<StandardActivityModel>
	 */
	@RequestMapping(value = "/getActivityAndSubActivityDetails/{domainID}/{serviceAreaID}/{technologyID}", method = RequestMethod.GET)
	public List<StandardActivityModel> getActivityAndSubActivityDetails(@PathVariable("domainID") int domainID,
			@PathVariable("serviceAreaID") int serviceAreaID, @PathVariable("technologyID") int technologyID) {

		LOG.info("TBL_SUBACTIVITY: ActivityList: Success");
		return activityMasterService.getActivityAndSubActivityDetails(domainID, serviceAreaID, technologyID);
	}

	/**
	 * 
	 * @param subActivityID
	 * @param signumID
	 * @return List<StandardActivityModel>
	 */
	@RequestMapping(value = "/getActivityAndSubActivityDetailsByID/{subActivityID}/{signumID}", method = RequestMethod.GET)
	public List<StandardActivityModel> getActivityAndSubActivityDetailsByID(
			@PathVariable("subActivityID") int subActivityID, @PathVariable("signumID") String signumID) {

		LOG.info("TBL_SUBACTIVITY: ActivityList: Success");
		return activityMasterService.getActivityAndSubActivityDetailsByID(subActivityID, signumID);
	}

	/**
	 * 
	 * @param taskModel
	 */
	@RequestMapping(value = "/saveTaskDetails", method = RequestMethod.POST)
	public void saveTaskDetails(@RequestBody TaskModel taskModel) {

		activityMasterService.saveTaskDetails(taskModel);
		LOG.info("TBL_TASK: SUCCESS");
	}

	/**
	 * 
	 * @param subActivityID
	 * @param signumID
	 * @return List<TaskModel>
	 */
	@RequestMapping(value = "/getTaskDetails/{subActivityID}/{signumID}", method = RequestMethod.GET)
	public List<TaskModel> getTaskDetails(@PathVariable("subActivityID") int subActivityID,
			@PathVariable("signumID") String signumID) {

		return activityMasterService.getTaskDetails(subActivityID, signumID);
	}

	/**
	 * 
	 * @param projectID
	 * @param subActivityID
	 * @return List<TaskModel>
	 */
	@RequestMapping(value = "/viewTaskDetails/{projectID}/{subActivityID}", method = RequestMethod.GET)
	public List<TaskModel> viewTaskDetails(@PathVariable("projectID") int projectID,
			@PathVariable("subActivityID") int subActivityID) {

		return activityMasterService.viewTaskDetails(projectID, subActivityID);
	}

	/**
	 * 
	 * @param taskModel
	 */
	@RequestMapping(value = "/updateTaskDetails", method = RequestMethod.POST)
	public void updateTaskDetails(@RequestBody TaskModel taskModel) {

		activityMasterService.updateTaskDetails(taskModel);
		LOG.info("updating TBL_TASK and TBL_TASK_TOOL: SUCCESS");
	}

	/**
	 * 
	 * @param taskID
	 * @param signumID
	 */
	@RequestMapping(value = "/deleteTaskDetails/{taskID}/{signumID}", method = RequestMethod.GET)
	public void deleteTaskDetails(@PathVariable("taskID") int taskID, @PathVariable("signumID") String signumID) {

		activityMasterService.deleteTaskDetails(taskID, signumID);
	}

	/**
	 * 
	 * @param taskToolModel
	 */
	@RequestMapping(value = "/mapTaskAndTool", method = RequestMethod.POST)
	public void mapTaskAndTool(@RequestBody TaskToolModel taskToolModel) {

		activityMasterService.mapTaskAndTool(taskToolModel);
		LOG.info(StringUtils.upperCase(AppConstants.SUCCESS));
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getTaskToolDetails", method = RequestMethod.GET)
	public List<TaskModel> getTaskToolDetails() {

		return activityMasterService.getTaskToolDetails();
	}

	/**
	 * 
	 * @param taskToolModel
	 */
	@RequestMapping(value = "/updateTaskToolMapping", method = RequestMethod.POST)
	public void updateTaskToolMapping(@RequestBody TaskToolModel taskToolModel) {

		activityMasterService.updateTaskToolMapping(taskToolModel);
		LOG.info(StringUtils.upperCase(AppConstants.SUCCESS));
	}

	/**
	 * 
	 * @param taskToolID
	 * @param signumID
	 */
	@RequestMapping(value = "/deleteTaskToolMapping/{taskToolID}/{signumID}", method = RequestMethod.GET)
	public void deleteTaskToolMapping(@PathVariable("taskToolID") int taskToolID,
			@PathVariable("signumID") String signumID) {

		activityMasterService.deleteTaskToolMapping(taskToolID, signumID);
		LOG.info(StringUtils.upperCase(AppConstants.SUCCESS));
	}

	/**
	 * 
	 * @param technologyID
	 * @param serviceAreaID
	 * @return List<ActivityMasterModel>
	 */
	@RequestMapping(value = "/getAutoComDetails/{technologyID}/{serviceAreaID}", method = RequestMethod.GET)
	public List<ActivityMasterModel> getAutoComDetails(@PathVariable("technologyID") int technologyID,
			@PathVariable("serviceAreaID") int serviceAreaID) {

		LOG.info("AutoCompleteDetails: Success");
		return activityMasterService.getAutoComDetails(technologyID, serviceAreaID);
	}

	/**
	 * 
	 * @param taskID
	 * @param signumID
	 * @return List<TaskModel>
	 */
	@RequestMapping(value = "/getTaskToolDetailsByID/{taskID}/{signumID}", method = RequestMethod.GET)
	public List<TaskModel> getTaskToolDetailsByID(@PathVariable("taskID") int taskID,
			@PathVariable("signumID") String signumID) {

		return activityMasterService.getTaskToolDetailsByID(taskID, signumID);
	}

	/**
	 * 
	 * @param taskID
	 * @param signumID
	 * @return List<TaskModel>
	 */
	@RequestMapping(value = "/getTaskDetailsByID/{taskID}/{signumID}", method = RequestMethod.GET)
	public List<TaskModel> getTaskDetailsByID(@PathVariable("taskID") int taskID,
			@PathVariable("signumID") String signumID) {

		return activityMasterService.getTaskDetailsByID(taskID, signumID);
	}

	/**
	 * 
	 * @param signum
	 * @return EmployeeModel
	 */
	@RequestMapping(value = "/getEmployeeBySignum/{signum}", method = RequestMethod.GET)
	public EmployeeModel getEmployeeBySignum(@PathVariable("signum") String signum) {

		LOG.info("TBL_EMPLOYEES : Success");
		return activityMasterService.getEmployeeBySignum(signum);
	}

	/**
	 * 
	 * @param signum
	 * @return EmployeeModel
	 */
	@RequestMapping(value = "/getAspDetailsBySignum", method = RequestMethod.GET)
	public EmployeeModel getAspDetailsBySignum(@RequestParam(value = "signum") String signum) {

		return activityMasterService.getAspDetailsBySignum(signum);

	}

	/**
	 * Purpose - This method get all sub-employees detail of manager.
	 * 
	 * <p>
	 * Method Type - GET.
	 * <p>
	 * URL - "/activityMaster/getEmployeesByManager".
	 * 
	 * @param managerSignum
	 * @return Response<List<EmployeeModel>>
	 * 
	 */
	@RequestMapping(value = "/getEmployeesByManager/{managerSignum}", method = RequestMethod.GET)
	public Response<List<EmployeeModel>> getEmployeesByManager(@PathVariable("managerSignum") String managerSignum) {

		LOG.info("TBL_EMPLOYEES : EmployeesByManagerList: Success");
		return activityMasterService.getEmployeesByManager(managerSignum);
	}

	/**
	 * 
	 * @return List<EmployeeBasicDetails>
	 */
	@RequestMapping(value = "/getEmployeeDetails", method = RequestMethod.GET)
	public List<EmployeeBasicDetails> getEmployeeDetails() {

		LOG.info("TBL_EMPLOYEES : EmployeeList: SUCCESS");
		return activityMasterService.getEmployeeDetails();
	}

	/**
	 * 
	 * @param projectID
	 * @return List<EmployeeModel>
	 */
	@RequestMapping(value = "/getEmployeeByProject/{projectID}", method = RequestMethod.GET)
	public List<EmployeeModel> getEmployeeByProject(@PathVariable("projectID") int projectID) {

		LOG.info("TBL_EMPLOYEES:EmployeeList by Project: SUCCESS");
		return activityMasterService.getEmployeeByProject(projectID);
	}

	/**
	 * 
	 * @return List<HashMap<String, String>>
	 */
	@RequestMapping(value = "/getAllLineManagers", method = RequestMethod.GET)
	public List<HashMap<String, String>> getAllLineManagers() {

		LOG.info("TBL_EMPLOYEES : ManagerList: SUCCESS");
		return activityMasterService.getAllLineManagers();
	}

	/**
	 * 
	 * @param projectId
	 * @return List<ProjectApprovalModel>
	 */
	@RequestMapping(value = "/getProjectApprovalDetailsById/{projectId}", method = RequestMethod.GET)
	public List<ProjectApprovalModel> getProjectApprovalDetailsById(@PathVariable("projectId") int projectId) {

		LOG.info("TBL_PROJECTAPPROVALS: ProjectApprovalDetailsById: Success");
		return activityMasterService.getProjectApprovalDetailsById(projectId);
	}

	/**
	 * 
	 * @param approverSignum
	 * @param marketArea
	 * @return List<ProjectApprovalModel>
	 */
	@RequestMapping(value = "/getProjectApprovalsByApprover/{approverSignum}", method = RequestMethod.GET)
	public List<ProjectApprovalModel> getProjectApprovalsByApprover(
			@PathVariable("approverSignum") String approverSignum, @RequestHeader("MarketArea") String marketArea) {

		LOG.info("TBL_PROJECTAPPROVALS: ProjectApprovalsByApproverList: SUCCESS");
		return activityMasterService.getProjectApprovalsByApprover(approverSignum, marketArea);
	}

	/**
	 * 
	 * @param projectApprovalModel
	 * @return boolean
	 */
	@RequestMapping(value = "/updateProjectApprovalDetails", method = RequestMethod.POST)
	public boolean updateProjectApprovalDetails(@RequestBody ProjectApprovalModel projectApprovalModel,
			@RequestHeader("Signum") String Signum) {

		return activityMasterService.updateProjectApprovalDetails(projectApprovalModel, Signum);
	}

	/**
	 * 
	 * @param resourceRequestCertificationModel
	 * @return boolean
	 */
	@RequestMapping(value = "/updateResourceRequestCertification", method = RequestMethod.POST)
	public boolean updateResourceRequestCertification(
			@RequestBody ResourceRequestCertificationModel resourceRequestCertificationModel) {

		return activityMasterService.updateResourceRequestCertification(resourceRequestCertificationModel);
	}

	/**
	 * 
	 * @param resourceRequestCompetenceModel
	 * @return boolean
	 */
	@RequestMapping(value = "/updateResourceRequestCompetence", method = RequestMethod.POST)
	public boolean updateResourceRequestCompetence(
			@RequestBody ResourceRequestCompetenceModel resourceRequestCompetenceModel) {

		return activityMasterService.updateResourceRequestCompetence(resourceRequestCompetenceModel);
	}

	/**
	 * 
	 * @param resourceRequestCertificationModel
	 * @return boolean
	 */
	@RequestMapping(value = "/addCertificationByRRID", method = RequestMethod.POST)
	public boolean addCertificationByRRID(
			@RequestBody ResourceRequestCertificationModel resourceRequestCertificationModel) {

		return activityMasterService.addCertificationByRRID(resourceRequestCertificationModel);
	}

	/**
	 * 
	 * @param resourceRequestCompetenceModel
	 * @return boolean
	 */
	@RequestMapping(value = "/addCompetenceByRRID", method = RequestMethod.POST)
	public boolean addCompetenceByRRID(@RequestBody ResourceRequestCompetenceModel resourceRequestCompetenceModel) {

		return activityMasterService.addCompetenceByRRID(resourceRequestCompetenceModel);
	}

	/**
	 * 
	 * @param projectID
	 * @param domainID
	 * @param technologyID
	 * @param vendorID
	 * @param marketType
	 * @param elementType
	 * @return List<NetworkElementModel>
	 */
	@RequestMapping(value = "/searchNetworkElementDetails/{projectID}/{domainSubDomainID}/{technologyID}/{vendorID}/{marketType}/{elementType}", method = RequestMethod.GET)
	public List<NetworkElementModel> searchNetworkElementDetails(@PathVariable("projectID") int projectID,
			@PathVariable("domainSubDomainID") String domainID, @PathVariable("technologyID") String technologyID,
			@PathVariable("vendorID") String vendorID, @PathVariable("marketType") String marketType,
			@PathVariable("elementType") String elementType) {

		LOG.info("TBL_NETWORK_ELEMENT : searchNEDetails: SUCCESS");
		return activityMasterService.searchNetworkElementDetails(projectID, domainID, technologyID, vendorID,
				marketType, elementType);

	}

	/**
	 * 
	 * @param request
	 * @param projectID
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "/getNetworkElementDetails/{projectID}", method = RequestMethod.POST)
	public Response<NetworkElements> getNetworkElementDetails(HttpServletRequest request,
			@PathVariable("projectID") int projectID) {

		return activityMasterService.getNetworkElementDetails(projectID, new DataTableRequest(request));

	}

	/**
	 * 
	 * @param request
	 * @param projectID
	 * @param response
	 * @return DownloadTemplateModel
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/downloadNetworkElement/{projectID}", method = RequestMethod.GET)
	public DownloadTemplateModel downloadNetworkElement(HttpServletRequest request,
			@PathVariable("projectID") int projectID, HttpServletResponse response) throws IOException, SQLException {

		return activityMasterService.downloadNetworkElement(projectID, response);
	}
	
	
	/**
	 * 
	 * @param request
	 * @param projectID
	 * @param response
	 * @return DownloadTemplateModel
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/downloadWoViewFile", method = RequestMethod.GET)
	public DownloadTemplateModel downloadWoViewFile(
			@RequestParam(value = "projectID", required = true) int projectID,
			@RequestParam(value = "startDate", required = true) String startDate,
			@RequestParam(value = "endDate", required = true) String endDate,
			@RequestParam(value = "status", required = true) String status,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException {

		return activityMasterService.downloadWoViewFile(projectID, response, startDate, endDate, status);
	}

	/**
	 * 
	 * @param request
	 * @param projectID
	 * @param response
	 * @return DownloadTemplateModel
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/downloadDoPlanViewFile", method = RequestMethod.GET)
	public DownloadTemplateModel downloadDoPlanViewFile(
			@RequestParam(value = "projectID", required = true) int projectID,
			@RequestParam(value = "startDate", required = true) String startDate,
			@RequestParam(value = "endDate", required = true) String endDate,
			@RequestParam(value = "status", required = true) String status,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException {

		return activityMasterService.downloadDoPlanViewFile(projectID, response, startDate, endDate, status);
	}
	/**
	 * 
	 * @param networkElementID
	 * @param signumID
	 */
	@RequestMapping(value = "/deleteNetworkElementDetails/{networkElementID}/{signumID}", method = RequestMethod.GET)
	public void deleteNetworkElementDetails(@PathVariable("networkElementID") int networkElementID,
			@PathVariable("signumID") String signumID) {

		activityMasterService.deleteNetworkElementDetails(networkElementID, signumID);
		LOG.info("TBL_NETWORK_ELEMENT : SUCCESS" + networkElementID + "," + signumID);
	}

	
	/**
	 * 
	 * @param vendorID
	 * @param signumID
	 */
	@RequestMapping(value = "/deleteVendorDetails/{vendorID}/{signumID}", method = RequestMethod.GET)
	public void deleteVendorDetails(@PathVariable("vendorID") int vendorID, @PathVariable("signumID") String signumID) {

		activityMasterService.deleteVendorDetails(vendorID, signumID);
		LOG.info("TBL_VENDOR: SUCCESS " + vendorID + "," + signumID);
	}

	/**
	 * 
	 * @param flowchartStepDetailsModel
	 */
	@RequestMapping(value = "/saveProjectDefinedTasks", method = RequestMethod.POST)
	public void saveProjectDefinedTasks(@RequestBody FlowChartStepDetailsModel flowchartStepDetailsModel) {

		activityMasterService.saveProjectDefinedTasks(flowchartStepDetailsModel);
		LOG.info("TBL_SCOPEID_TASK_MAPPING: SUCCESS");
	}

	/**
	 * 
	 * @return List<VendorModel>
	 */
	@RequestMapping(value = "/getVendorDetails", method = RequestMethod.GET)
	public List<VendorModel> getVendorDetails() {

		LOG.info("TBL_VENDOR: VENDOR LIST: SUCCESS");
		return activityMasterService.getVendorDetails();
	}

	/**
	 * 
	 * @param vendorID
	 * @param signumID
	 * @return List<VendorModel>
	 */
	@RequestMapping(value = "/getVendorDetailsByID/{vendorID}/{signumID}", method = RequestMethod.GET)
	public List<VendorModel> getVendorDetailsByID(@PathVariable("vendorID") int vendorID,
			@PathVariable("signumID") String signumID) {

		LOG.info("TBL_VENDOR: VENDOR LIST By ID: " + vendorID + "," + signumID);
		return activityMasterService.getVendorDetailsByID(vendorID, signumID);
	}

	/**
	 * 
	 * @param vendorModel
	 */
	@RequestMapping(value = "/saveVendorDetails", method = RequestMethod.POST)
	public void saveVendorDetails(@RequestBody VendorModel vendorModel) {

		activityMasterService.saveVendorDetails(vendorModel);
		LOG.info("TBL_VENDOR: SUCCESS");
	}

	/**
	 * 
	 * @param vendorModel
	 */
	@RequestMapping(value = "/updateVendorDetails", method = RequestMethod.POST)
	public void updateVendorDetails(@RequestBody VendorModel vendorModel) {

		activityMasterService.updateVendorDetails(vendorModel);
		LOG.info("TBL_VENDOR: SUCCESS");
	}

	/**
	 * 
	 * @param domainID
	 * @param technologyID
	 * @param serviceAreaID
	 * @param activityName
	 * @return List<ActivityMasterModel>
	 */
	@RequestMapping(value = "/getSubActivityForActivity/{domainID}/{technologyID}/{serviceAreaID}/{activityName}", method = RequestMethod.GET)
	public List<ActivityMasterModel> getSubActivityForActivity(@PathVariable("domainID") int domainID,
			@PathVariable("technologyID") int technologyID, @PathVariable("serviceAreaID") int serviceAreaID,
			@PathVariable("activityName") String activityName) {

		LOG.info("TBL_SUBACTIVITY: SUCCESS");
		return activityMasterService.getSubActivityForActivity(domainID, technologyID, serviceAreaID, activityName);

	}

	/**
	 * 
	 * @param domainID
	 * @param serviceAreaID
	 * @param technologyID
	 * @param ProjectID
	 * @param isWfAvailable
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping(value = "/getActivitySubActivityByProject_V2/{DomainID}/{ServiceAreaID}/{TechnologyID}/{ProjectID}/{wfAvailable}", method = RequestMethod.GET)
	public List<Map<String, Object>> getActivitySubActivityByProject_V2(@PathVariable("DomainID") int domainID,
			@PathVariable("ServiceAreaID") int serviceAreaID, @PathVariable("TechnologyID") int technologyID,
			@PathVariable("ProjectID") int projectID, @PathVariable("wfAvailable") boolean isWfAvailable) {

		LOG.info(AppConstants.TBL_DOMAIN_LIST_SUCCESS);
		return activityMasterService.getActivitySubActivityByProject_V2(projectID, serviceAreaID, domainID,
				technologyID, isWfAvailable);
	}

	/**
	 * 
	 * @param ProjectID
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping(value = "/getActivitySubActivityByProjectId", method = RequestMethod.GET)
	public List<Map<String, Object>> getActivitySubActivityByProjectId(
			@RequestParam(value = "ProjectID", required = false) Integer projectID) {

		LOG.info(AppConstants.TBL_DOMAIN_LIST_SUCCESS);
		return activityMasterService.getActivitySubActivityByProjectId(projectID);
	}

	/**
	 * 
	 * @param signum
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping(value = "/getTechnologyDetailsBySignum", method = RequestMethod.GET)
	public List<Map<String, Object>> getTechnologyDetailsBySignum(@RequestParam(value = "signum") String signum) {

		LOG.info(AppConstants.TBL_DOMAIN_LIST_SUCCESS);
		return activityMasterService.getTechnologyDetailsBySignum(signum);
	}

	/**
	 * 
	 * @param signum
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping(value = "/getEmployeeDetailsBySignum", method = RequestMethod.GET)
	public List<Map<String, Object>> getEmployeeDetailsBySignum(@RequestParam(value = "signum") String signum) {

		LOG.info(AppConstants.TBL_DOMAIN_LIST_SUCCESS);
		return activityMasterService.getEmployeeDetailsBySignum(signum);
	}

	/**
	 * 
	 * @param projectScopeID
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping(value = "/getSubScopebyScopeID", method = RequestMethod.GET)
	public Response<List<Map<String, Object>>> getSubScopebyScopeID(
			@RequestParam(value = "projectScopeID") String projectScopeID) {

		LOG.info(AppConstants.TBL_DOMAIN_LIST_SUCCESS);
		return activityMasterService.getSubScopebyScopeID(projectScopeID);
	}

	/**
	 * 
	 * @param projectScopeDetailID
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping(value = "/getActivitiesBySubScopeId", method = RequestMethod.GET)
	public Response<List<Map<String, Object>>> getActivitiesBySubScopeId(
			@RequestParam(value = "projectScopeDetailID") int projectScopeDetailID) {

		LOG.info(AppConstants.TBL_DOMAIN_LIST_SUCCESS);
		return activityMasterService.getActivitiesBySubScopeId(projectScopeDetailID);
	}

	/**
	 * 
	 * @param projectScopeID
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping(value = "/getViewScopeDetails", method = RequestMethod.GET)
	public Response<List<Map<String, Object>>> getViewScopeDetails(
			@RequestParam(value = "projectScopeID", required=false) String projectScopeID) {
		Response<List<Map<String, Object>>> response = new Response<>();
		if(projectScopeID!=null) {
			LOG.info(AppConstants.TBL_DOMAIN_LIST_SUCCESS);
			response = activityMasterService.getViewScopeDetails(projectScopeID);
		}
		else {
			response.addFormError("Please provide projectScopeID");
		}
		return response;
	}

	/**
	 * 
	 * @param projectScopeDetailID
	 * @return List<ScopeDetailsModel>
	 */
	@RequestMapping(value = "/getScopeDetailsById", method = RequestMethod.GET)
	public List<ScopeDetailsModel> getScopeDetailsById(
			@RequestParam(value = "projectScopeDetailID") String projectScopeDetailID) {

		LOG.info(AppConstants.TBL_DOMAIN_LIST_SUCCESS);
		return activityMasterService.getScopeDetailsById(projectScopeDetailID);
	}

	/**
	 * 
	 * @param projectId
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping(value = "/getActivityByProjectId", method = RequestMethod.GET)
	public List<Map<String, Object>> getActivityByProjectId(@RequestParam(value = "projectId") int projectId) {

		return activityMasterService.getActivityByProjectId(projectId);
	}

	/**
	 * 
	 * @param spocRequest
	 * @return boolean
	 */
	@RequestMapping(value = "/addDomainSpoc", method = RequestMethod.POST)
	public boolean addDomainSpoc(@RequestBody SpocModel spocRequest) {

		return activityMasterService.addDomainSpoc(spocRequest);
	}

	/**
	 * 
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping(value = "/getAllDomainSpoc", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllDomainSpoc() {

		return activityMasterService.getAllDomainSpoc();
	}

	/**
	 * 
	 * @return List<CustomerModel>
	 */
	@RequestMapping(value = "/getCustomerDetails", method = RequestMethod.GET)
	public List<CustomerModel> getCustomerDetails() {

		LOG.info("TBL_CUSTOMERS: CUSTOMER LIST: SUCCESS");
		return activityMasterService.getCustomerDetails();
	}

	/**
	 * 
	 * @param customerID
	 * @param signum
	 */
	@RequestMapping(value = "/deleteCustomerDetails/{customerID}/{signum}", method = RequestMethod.POST)
	public void deleteCustomerDetails(@PathVariable("customerID") int customerID,
			@PathVariable("signum") String signum) {

		activityMasterService.deleteCustomerDetails(customerID, signum);

	}

	/**
	 * 
	 * @param customerModel
	 */
	@RequestMapping(value = "/saveCustomerDetails", method = RequestMethod.POST)
	public Response<Void> saveCustomerDetails(@RequestBody CustomerModel customerModel) {

		Response<Void> response=activityMasterService.saveCustomerDetails(customerModel);
		LOG.info("TBL_CUSTOMERS: SUCCESS");
		return response;
	}

	/**
	 * 
	 * @param term
	 * @return List<EmployeeBasicDetails>
	 */
	@RequestMapping(value = "/getEmployeesByFilter", method = RequestMethod.POST)
	public List<EmployeeBasicDetails> getEmployeesByFilter(@RequestParam("term") String term) {

		return activityMasterService.getEmployeesByFilter(term);
	}

	@RequestMapping(value = "/getASPMsByFilter", method = RequestMethod.POST)
	public List<EmployeeBasicDetails> getASPMsByFilter(@RequestParam("term") String term) {

		return activityMasterService.getASPMsByFilter(term);
	}
	/**
	 * 
	 * @return List<WorkInstructionModel>
	 */
	@RequestMapping(value = "/getWorkInstruction", method = RequestMethod.GET)
	public List<WorkInstructionModel> getWorkInstruction() {

		LOG.info(AppConstants.TBL_WF_WORK_INSTRUCTION_SUCCESS);
		return activityMasterService.getWorkInstruction();
	}

	/**
	 * 
	 * @param workInstructionModel
	 * @param workinstructionfile
	 * @throws IOException
	 */
	@RequestMapping(value = "/saveWorkInstruction", method = RequestMethod.POST, consumes = "multipart/form-data")
	public void saveWorkInstruction(@RequestParam("workInstructionModel") String workInstructionModelString,
			@RequestParam(value = "workinstructionfile", required = false) MultipartFile workinstructionfile)
			throws IOException {

		activityMasterService.saveWorkInstruction(workInstructionModelString, workinstructionfile);
		LOG.info(AppConstants.TBL_WF_WORK_INSTRUCTION_SUCCESS);
	}

	/**
	 * 
	 * @param wIID
	 * @param signumID
	 * @param active
	 */
	@RequestMapping(value = "/deleteWorkInstruction/{wIID}/{signumID}/{active}", method = RequestMethod.GET)
	public void deleteWorkInstruction(@PathVariable("wIID") int wIID, @PathVariable("signumID") String signumID,
			@PathVariable("active") boolean active) {

		activityMasterService.deleteWorkInstruction(wIID, signumID, active);
		LOG.info("TBL_WF_WORK_INSTRUCTION: Success");
	}

	/**
	 * 
	 * @param workInstructionModel
	 * @param workinstructionfile
	 * @throws IOException
	 */
	@RequestMapping(value = "/editWorkInstruction", method = RequestMethod.POST, consumes = "multipart/form-data")
	public void editWorkInstruction(@RequestParam("workInstructionModel") String workInstructionModelString,
			@RequestParam(value = "workinstructionfile", required = false) MultipartFile workinstructionfile)
			throws IOException {

		activityMasterService.editWorkInstruction(workInstructionModelString, workinstructionfile);
		LOG.info(AppConstants.TBL_WF_WORK_INSTRUCTION_SUCCESS);
	}

	/**
	 * 
	 * @param domainID
	 * @param vendorID
	 * @param technologyID
	 * @return List<WorkInstructionModel>
	 */
	@RequestMapping(value = "/getActiveWorkInstruction", method = RequestMethod.GET)
	public Response<List<WorkInstructionModel>> getActiveWorkInstruction(
			@RequestParam(value = "domainID", required = false) Integer domainID,
			@RequestParam(value = "vendorID", required = false) Integer vendorID,
			@RequestParam(value = "technologyID", required = false) Integer technologyID) {

		LOG.info("TBL_WF_WORK_INSTRUCTION:WORK LIST: SUCCESS ");
		return activityMasterService.getActiveWorkInstruction(domainID, vendorID, technologyID);
	}

	/**
	 * 
	 * @param signum
	 * @return Response<AdhocBookingProjectModel>
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/getAdhocBookingProject/{signum}", method = RequestMethod.GET)
	public ResponseEntity<Response<AdhocBookingProjectModel>> getAdhocBookingProject(@PathVariable("signum") String signum)
			throws IOException, SQLException {

		return activityMasterService.getAdhocBookingProject(signum);
	}
	//v1/getAdhocBookingProject

	@RequestMapping(value = "/v1/getAdhocBookingProject/{signum}", method = RequestMethod.GET)
	public Map<String, String> getAdhocBookingProjectV1(@PathVariable("signum") String signum)
			throws IOException, SQLException {

		return activityMasterService.getAdhocBookingProjectV1(signum);
	}

	/**
	 * 
	 * @param leavePlanModel
	 * @return boolean
	 */
	@RequestMapping(value = "/saveLeavePlan", method = RequestMethod.POST)
	public boolean saveLeavePlan(@RequestBody LeavePlanModel leavePlanModel) {

		LOG.info("TBL_Leave_Plan: SUCCESS");
		return activityMasterService.saveLeavePlan(leavePlanModel);
	}

	/**
	 * 
	 * @param signum
	 * @return List<LeavePlanModel>
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/getLeavePlanBySignum/{signum}", method = RequestMethod.GET)
	public List<LeavePlanModel> getLeavePlanBySignum(@PathVariable("signum") String signum)
			throws IOException, SQLException {

		return activityMasterService.getLeavePlanBySignum(signum);
	}

	/**
	 * 
	 * @param signum
	 * @param leavePlanID
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/deleteLeavePlan", method = RequestMethod.POST)
	public void deleteLeavePlan(@RequestParam(value = "signum") String signum,
			@RequestParam(value = "leavePlanID") int leavePlanID) throws IOException, SQLException {

		activityMasterService.deleteLeavePlan(signum, leavePlanID);
	}

	/**
	 * 
	 * @param signum
	 * @param week
	 * @return List<ShiftTimmingModel2>
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/getShiftTimmingBySignum/{signum}/{startDate}", method = RequestMethod.GET)
	public List<ShiftTimmingModel2> getShiftTimmingBySignum(@PathVariable("signum") String signum,
			@PathVariable("startDate") String startDate) throws IOException, SQLException {

		return activityMasterService.getShiftTimmingBySignum(signum, startDate);
	}

	/**
	 * 
	 * @return List<TimeZoneModel>
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/getTimeZones", method = RequestMethod.GET)
	public List<TimeZoneModel> getTimeZones() throws IOException, SQLException {

		return activityMasterService.getTimeZones();
	}

	/**
	 * 
	 * @param shiftTimmingModel
	 * @return boolean
	 */
	@RequestMapping(value = "/saveshiftTimming", method = RequestMethod.POST)
	public boolean saveshiftTimming(@RequestBody ShiftTimmingModel shiftTimmingModel) {

		return activityMasterService.saveshiftTimming(shiftTimmingModel);

	}

	/**
	 * 
	 * @param signum
	 * @param startDate
	 * @param endDate
	 * @param timeZone
	 * @return List<ShiftTimmingModel2>
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/getShiftTimmingByDate", method = RequestMethod.GET)
	public List<ShiftTimmingModel2> getShiftTimmingByDate(@RequestParam("signum") String signum,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam(value = "timeZone", required = false) String timeZone) throws IOException, SQLException {

		return activityMasterService.getShiftTimmingByDate(signum, startDate, endDate, timeZone);
	}

	/**
	 * 
	 * @param signum
	 * @param shiftId
	 * @param week
	 * @return boolean
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/deleteShiftTimmingbyID", method = RequestMethod.POST)
	public boolean deleteShiftTimmingbyID(@RequestParam(value = "signum", required = false) String signum,
			@RequestParam("shiftId") int shiftId, @RequestParam("startDate") String  startDate)
			throws IOException, SQLException {

		return activityMasterService.deleteShiftTimmingbyID(signum, shiftId, startDate);
	}

	/**
	 * 
	 * @param response
	 * @param wIID
	 * @return DownloadTemplateModel
	 * @throws IOException
	 */
	@RequestMapping(value = "/downloadWorkInstructionFile", method = RequestMethod.GET)
	private DownloadTemplateModel downloadWorkInstructionFile(HttpServletResponse response,
			@RequestParam("wIID") String wIID) throws IOException {

		return activityMasterService.downloadWorkInstructionFile(wIID);
	}

	/**
	 * 
	 * @return List<UserFeedbackModel>
	 */
	@RequestMapping(value = "/getUserFeedBack", method = RequestMethod.GET)
	public List<UserFeedbackModel> getUserFeedBack() {

		return activityMasterService.getUserFeedBack();
	}

	/**
	 * 
	 * @return List<HashMap<String, Object>>
	 */
	@RequestMapping(value = "/getEssLeave", method = RequestMethod.GET)
	public List<HashMap<String, Object>> getEssLeave() {

		return activityMasterService.getEssLeave();
	}

	/**
	 * 
	 * @param projectID
	 * @param deliverableId
	 * @param elementType
	 * @return List<String>
	 */
	@RequestMapping(value = "/getNodeTypeByDeliverableId/{ProjectID}/{DeliverableId}/{ElementType}", method = RequestMethod.GET)
	public List<String> getNodeTypeByDeliverableId(@PathVariable("ProjectID") int projectID,
			@PathVariable("DeliverableId") int deliverableId, @PathVariable("ElementType") String elementType) {

		return activityMasterService.getNodeTypeByDeliverableId(projectID, deliverableId, elementType);
	}

   @RequestMapping(value = "/getCountries", method = RequestMethod.GET)
   public Response<List<CountryModel>> getCountries() throws ParseException {
	   return this.activityMasterService.getCountries();
   }

   @RequestMapping(value = "/getScopeByScopeId", method = RequestMethod.GET)
   public ScopeDetailsModel getScopeByScopeId(@RequestParam("projectScopeID") int projectScopeID) {
	   return this.activityMasterService.getScopeByScopeId(projectScopeID);
   }
   
   @RequestMapping(value = "/changeEmployeeStatus", method = RequestMethod.POST)
   public String changeEmployeeStatus(@RequestBody ResourceStatusModel resourceStatusModel){
	   return this.activityMasterService.changeEmployeeStatus(resourceStatusModel);
   }
   
	@RequestMapping(value = "/getNodes/{ProjectID}/{ElementType}", method = RequestMethod.GET)
	public Response<List<String>> getNodes(@PathVariable("ProjectID") int ProjectID,
			@PathVariable("ElementType") String ElementType) {
		return activityMasterService.getNodes(ProjectID, ElementType);
	}

   	/**
   	 * 
   	 * @param projectScopeId
   	 * @return List<ScopeDetailsModel>
   	 */
	@RequestMapping(value = "/getScopeDetailsByScopeId", method = RequestMethod.GET)
	public List<ScopeDetailsModel> getScopeDetailsByScopeId(
			@RequestParam(value = "projectScopeId") String projectScopeId) {
	
		LOG.info(AppConstants.TBL_DOMAIN_LIST_SUCCESS);
		return activityMasterService.getScopeDetailsByScopeId(projectScopeId);
	}

	
	/**
     * @author ekarmuj
     * Api Url :/saveGlobalUrl
     * Purpose: This Api used to save global Url.
     * @param globalUrlModel
     * @return Response json
     * 
     */
    
    @RequestMapping(value = "/saveGlobalUrl", method = RequestMethod.POST)
    public Response<Void> saveGlobalUrl(@RequestHeader("Signum") String signumID, @RequestHeader("Role") String role,@RequestBody GlobalUrlModel globalUrlModel)
    {
    	return activityMasterService.saveGlobalUrl(globalUrlModel,signumID,role);
    }
    
    /**
     * @author ekarmuj
     * Api Url :/updateGlobalUrl
     * Purpose: This Api used to update global Url.
     * @param globalUrlModel
     * @return Response json
     * 
     */
    
    @RequestMapping(value = "/updateGlobalUrl", method = RequestMethod.POST)
    public Response<Void> updateGlobalUrl(@RequestHeader("Signum") String signumID, @RequestHeader("Role") String role,@RequestBody GlobalUrlModel globalUrlModel)
    {
    	return activityMasterService.updateGlobalUrl(globalUrlModel, signumID, role);
    }

    /**
     * @author ekarmuj
     * Api Url :/getAllGlobalUrl
     * Purpose: This Api used to get All global Url.
     * @return List of global url.
     * 
     */
    
    @RequestMapping(value= "/getAllGlobalUrl", method= RequestMethod.GET)
    public List<GlobalUrlModel> getAllGlobalUrl() {
        
        return activityMasterService.getAllGlobalUrl();
    }

    /**
     * @author ekarmuj
     * Api Url :/saveLocalUrl
     * Purpose: This Api used to save local Url.
     * @param localUrlModel
     * @return Response json
     * 
     */
    
    @RequestMapping(value = "/saveLocalUrl", method = RequestMethod.POST)
    public Response<Void> saveLocalUrl(@RequestHeader("Signum") String signumID, @RequestHeader("Role") String role,@RequestBody LocalUrlModel localUrlModel)
    {
    	return activityMasterService.saveLocalUrl(localUrlModel,signumID,role);
    }
    
    /**
     * @author ekarmuj
     * Api Url :/updateLocalUrl
     * Purpose: This Api used to update local Url.
     * @param LocalUrlModel
     * @return Response json
     * 
     */
    
    @RequestMapping(value = "/updateLocalUrl", method = RequestMethod.POST)
    public Response<Void> updateLocalUrl(@RequestHeader("Signum") String signumID, @RequestHeader("Role") String role,@RequestBody LocalUrlModel localUrlModel)
    {
    	return activityMasterService.updateLocalUrl(localUrlModel, signumID, role);
    }

    /**
     * @author ekarmuj
     * Api Url :/getAllLocalUrl
     * Purpose: This Api used to get All local Url.
     * @return List of local url.
     * 
     */
    
    @RequestMapping(value= "/getAllLocalUrl", method= RequestMethod.GET)
    public Response<List<LocalUrlModel>> getAllLocalUrl(@RequestParam("projectID") int projectID) {
        
        return activityMasterService.getAllLocalUrl(projectID);
    }
    
    /**
     * @author ekarmuj
     * Api Url :/addInstantFeedback
     * Purpose: This Api used to add Instant feedback.
     * @return Response<Void>.
     * 
     */
    
    @RequestMapping(value= "/addInstantFeedback", method= RequestMethod.POST)
    public Response<Void> addInstantFeedback(@RequestBody InstantFeedbackModel instantFeedbackModel) {
    return activityMasterService.saveInstantFeedback(instantFeedbackModel);
    }
    
    
    
    /**
     * @author ekarmuj
     * Api Url :/updateInstantFeedback
     * Purpose: This Api used to update Instant feedback.
     * @return Response<Void>.
     * 
     */
    
    @RequestMapping(value= "/updateInstantFeedback", method= RequestMethod.POST)
    public Response<Void> updateInstantFeedback(@RequestBody InstantFeedbackModel instantFeedbackModel) {  
        return activityMasterService.updateInstantFeedback(instantFeedbackModel);
    }
    
    
    /**
     * 
     * @param workFlowFeedbackModel
     * @return
     */
    @RequestMapping(value = "/saveWFFeedbackComments", method = RequestMethod.POST)
    public  Response<Void>  saveWFFeedbackComments(@RequestHeader("role") String role,
    		@RequestBody WorkFlowFeedbackModel workFlowFeedbackModel) {
      if(StringUtils.equalsIgnoreCase(DEFAULT_USER, role)) {
    	  role=NETWORK_ENGINEER;
    	}
    	workFlowFeedbackModel.setUserRole(role);
    	return this.activityMasterService.saveWFFeedbackComments(workFlowFeedbackModel);
    }
    

    
    /**
     * 
     * @param workFlowFeedbackModel
     * @return
     */
    
    @RequestMapping(value = "/getFeedbackHistory", method = RequestMethod.POST)
	public List<WorkFlowFeedbackActivityModel> getFeedbackHistory(
			@RequestBody WorkFlowFeedbackModel workFlowFeedbackModel) {

		return activityMasterService.getFeedbackHistory(workFlowFeedbackModel);
	}
    
    
    /**
     * 
     * @param workFlowFeedbackModel
     */
    @RequestMapping(value = "/deleteFeedbackComment", method = RequestMethod.POST)
    public Response<Void> deleteFeedbackComment(@RequestHeader("signum") String signum,
    		@RequestBody WorkFlowFeedbackModel workFlowFeedbackModel) {
    	
    	return activityMasterService.deleteFeedbackComment(signum,workFlowFeedbackModel);
      
    }
    
    
    /**
     * @author : EKMRAVU
     * @Param : /getInstantFeedback
     */
    @RequestMapping(value ="/getInstantFeedback", method=RequestMethod.GET)
	public List<InstantFeedbackModel>  getInstantFeedback()
    {
		return activityMasterService.getInstantFeedback();
	}
    
    /**
     * 
     * @return List<InstantFeedbackModel>
     */
    @RequestMapping(value ="/getInstantFeedbackForDropDown", method=RequestMethod.GET)
   	public List<InstantFeedbackModel>  getInstantFeedbackForDropDown()
    {
   		return activityMasterService.getInstantFeedbackForDropDown();
   	}
    
    
    
    /**
     * Purpose: used to get all project based on signum.
     * @param signum
     * @return list of project list.
     */
    
    @RequestMapping(value ="/getProjectList", method=RequestMethod.GET)
   	public List<ProjectFilterModel>  getProjectList(
   			                                   @RequestHeader("signum") String signum,
   			                                @RequestHeader("role") String role,
   			                             @RequestHeader("MarketArea") String marketArea)
    {
    	if(StringUtils.equalsIgnoreCase(DEFAULT_USER, role)) {
      	  role=NETWORK_ENGINEER;
      	}
   		return activityMasterService.getProjectList(signum,role,marketArea);
   	}
    
    /**
     * @param signum
     * @param projectFilterModel
     * @return list of work flow filter model.
     */
    
    @RequestMapping(value ="/getWorkFlowList", method=RequestMethod.POST)
   	public List<WorkFlowFilterModel>  getWorkFlowList(@RequestHeader("signum") String signum,
   			@RequestHeader("role") String role,
   			                                 @RequestBody ProjectFilterModel projectFilterModel)
    {
    	if(StringUtils.equalsIgnoreCase(DEFAULT_USER, role)) {
      	  role=NETWORK_ENGINEER;
      	}
   		return activityMasterService.getWorkFlowList(signum,projectFilterModel,role);
   	}
   
    /**
     * @param signum
     * @param feedbackNePmModel
     * @return list of feedback for NE and PM.
     * @throws ParseException 
     */
    
    @RequestMapping(value ="/getAllFeedback", method=RequestMethod.POST)
   	public Map<String, Object>  getAllFeedback(HttpServletRequest request, @RequestHeader("signum") String signum,
   			@RequestHeader("role") String role,
   			@RequestParam(value = "startDate", required = true) String startDate,
			@RequestParam(value = "projectID", required = true) Integer projectID,
			@RequestParam(value = "endDate", required = true) String endDate,
			@RequestParam(value = "feedbackStatus", required = true) String feedbackStatus,
			@RequestParam(value = "listOfWorkFlowId", required = true) List<Integer> listOfWorkFlowId,
			@RequestParam(value = "listOfFlowchartdefId", required = true) List<Integer> listOfFlowchartdefId ) throws ParseException
   		                                            // @RequestBody FeedbackNEPMModel feedbackNePmModel)
    {
      if(StringUtils.equalsIgnoreCase(DEFAULT_USER, role)) {
    	  role=NETWORK_ENGINEER;
    	}
    	
    	FeedbackNEPMModel feedbackNePmModel=new FeedbackNEPMModel();
    	feedbackNePmModel.setProjectID(projectID);
    	feedbackNePmModel.setEndDate(endDate);
    	feedbackNePmModel.setFeedbackStatus(feedbackStatus);
    	feedbackNePmModel.setStartDate(startDate);
    	feedbackNePmModel.setListOfWorkFlowId(listOfWorkFlowId);
    	feedbackNePmModel.setListOfFlowchartdefId(listOfFlowchartdefId);
    	DataTableRequest dataTableRequset=new DataTableRequest(request);
   		return activityMasterService.getAllFeedback(signum,feedbackNePmModel,dataTableRequset,role);
   	} 

    /**
     * 
     * @author : ETAPAWA
     * @param workFlowFeedbackModel
     * @return
     */
    @RequestMapping(value = "/saveStepSadCount", method = RequestMethod.POST)
    public  Response<Void>  saveStepSadCount(@RequestBody WorkFlowFeedbackModel workFlowFeedbackModel,
    		@RequestHeader("role") String role) {
    	if(StringUtils.equalsIgnoreCase(DEFAULT_USER, role)) {
      	  role=NETWORK_ENGINEER;
      	}
    	return this.activityMasterService.saveStepSadCount(workFlowFeedbackModel,role);
    }
    
    /**
     * @author : ETAPAWA
     * @param feedbackStatusUpdateModel
     * @return
     */
    @RequestMapping(value = "/updateFeedbackStatus", method = RequestMethod.POST)
    public  Response<Void>  updateFeedbackStatus(@RequestHeader("role") String role,
    		@RequestHeader("signum") String signum,
    		@RequestBody FeedbackStatusUpdateModel feedbackStatusUpdateModel) {
    	if(StringUtils.equalsIgnoreCase(DEFAULT_USER, role)) {
      	  role=NETWORK_ENGINEER;
      	}
    	return this.activityMasterService.updateFeedbackStatus(role,feedbackStatusUpdateModel,signum);
    }
    
    /**
     * 
     * @param projectID
     * @param signum
     * @param role
     * @param startDate
     * @param endDate
     * @param feedbackStatus
     * @param listOfWorkFlowId
     * @param listOfFlowchartdefId
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @RequestMapping(value = "/downloadFeedbackFile", method = RequestMethod.GET)
   	public DownloadTemplateModel downloadPMFeedbackFile(
   			@RequestParam(value = "projectID", required = true) int projectID,
      			@RequestParam(value = "startDate", required = true) String startDate,
      			@RequestParam(value = "endDate", required = true) String endDate,
      			@RequestParam(value = "feedbackStatus", required = true) String feedbackStatus,
      			@RequestParam(value = "listOfWorkFlowId", required = true) List<Integer> listOfWorkFlowId,
   			@RequestParam(value = "listOfFlowchartdefId", required = true) List<Integer> listOfFlowchartdefId,
   			@RequestParam(value="signum", required=true) String signum,
			@RequestParam(value="role", required=true) String role,
   			HttpServletRequest request,
   			HttpServletResponse response) throws IOException, SQLException {
       	if(StringUtils.equalsIgnoreCase(DEFAULT_USER, role)) {
         	  role=NETWORK_ENGINEER;
         	}
         	
         	FeedbackNEPMModel feedbackNePmModel=new FeedbackNEPMModel();
         	feedbackNePmModel.setProjectID(projectID);
         	feedbackNePmModel.setEndDate(endDate);
         	feedbackNePmModel.setFeedbackStatus(feedbackStatus);
         	feedbackNePmModel.setStartDate(startDate);
         	feedbackNePmModel.setListOfWorkFlowId(listOfWorkFlowId);
         	feedbackNePmModel.setListOfFlowchartdefId(listOfFlowchartdefId);
         	

   		return activityMasterService.downloadFeedbackFile(signum,feedbackNePmModel,role,response);
   	}
    
	/**
	 * 
	 * @param term
	 * @return List<EmployeeBasicDetails>
	 */
	@RequestMapping(value = "/testGetEmployeesByFilter", method = RequestMethod.POST)
	public List<EmployeeBasicDetails> testGetEmployeesByFilter(@RequestParam("term") String term) {

		return activityMasterService.testGetEmployeesByFilter(term);
	}
	
	/**
     * Purpose : This api used to save SignalR transaction Logs in Table.
     * @author : EKARMUJ
     * @param notificationLogModel
     * @return Response<Void>
     */
	
    @RequestMapping(value = "/saveSignalRNotificationLog", method = RequestMethod.POST)
    public  Response<Void>  saveSignalRNotificationLog(@RequestBody NotificationLogModel notificationLogModel) {
    	return this.activityMasterService.saveSignalRNotificationLog(notificationLogModel);
    }
    
    /**
     * Purpose : This api used to update notification received time in table.
     * @author : EKARMUJ
     * @param notificationId
     * @return Response<Void>
     */
    
    @RequestMapping(value = "/updateSignalRNotificationLog", method = RequestMethod.POST)
    public  Response<Void>  updateSignalRNotificationLog(@RequestParam(value = "notificationId", required = true) Integer notificationId) {
    	return this.activityMasterService.updateSignalRNotificationLog(notificationId);
    }
    
    /**
     * Purpose:This api used to getLogLevelByUser.
     * @author ekarmuj
     * @param userSignum
     * @return Response<List<LogLevelModel>>
     */
    
    @RequestMapping(value = "/getLogLevelByUser", method = RequestMethod.GET)
    public  Response<List<LogLevelModel>>  getLogLevelByUser(@RequestParam(value = "userSignum", required = true) String userSignum) {
    	return this.activityMasterService.getLogLevelByUser(userSignum);
    }

	/**
	 * 
	 * @param wFStepInstructionModel
	 * @return
	 */
	 @RequestMapping(value = "/getInstructionURLList", method = RequestMethod.POST)
	 public Map<String,Object> getInstructionURLList( @RequestBody List<WFStepInstructionModel> wFStepInstructionModel) {

	           return activityMasterService.getInstructionURLList(wFStepInstructionModel);
	    }

	
	/**
	 * @author ekmbuma
	 * 
	 * @param uploadedBy
	 * @param file
	 * @return Response<Void>
	 * @throws Exception
	 */
    @RequestMapping(value="/uploadFileForESSLeaveData",method=RequestMethod.POST, consumes = "multipart/form-data")
    public Response<String> uploadFileForESSLeaveData(@RequestParam("uploadedBy") String uploadedBy,
    		@RequestParam("file") MultipartFile file) throws Exception {

    	LOG.info("uploadFileForESSLeaveData: SUCCESS");
    	return activityMasterService.uploadFileForESSLeaveData(uploadedBy,file);
		
    }
    
    @RequestMapping(value="/v2/uploadFileForESSLeaveData",method=RequestMethod.POST, consumes = "multipart/form-data")
    public Response<String> uploadFileForESSLeaveDataV2(@RequestParam("uploadedBy") String uploadedBy,
    		@RequestParam("file") MultipartFile file) throws Exception {

    	LOG.info("uploadFileForESSLeaveData: SUCCESS");
    	return activityMasterService.uploadFileForESSLeaveDataV2(uploadedBy,file);
		
    }

    /**
     * @author ekmbuma
     * @page View Access
     * 
     * @param term
     * @param managerSignum
     * @return List<EmployeeBasicDetails>
     */
    @RequestMapping(value="/getEmployeesOrManager", method=RequestMethod.POST)
	public List<EmployeeBasicDetails> getEmployeesOrManager(@RequestParam(value="term", required=true) String term, @RequestParam(value="managerSignum", required=false) String managerSignum) {

		return activityMasterService.getEmployeesOrManager(term,managerSignum);
	}
    
    /**
     * @author ekmbuma
     * @page View Access
     * 
     * @param term
     * @return List<HashMap<String, String>>
     */
	@RequestMapping(value = "/getLineManagersBySearch", method = RequestMethod.POST)
	public List<HashMap<String, String>> getLineManagersBySearch(@RequestParam(value="term", required=true) String term) {

		return activityMasterService.getLineManagersBySearch(term);
	}

	/**
	 * 
	 * @param request
	 * @param projectID
	 * @param response
	 * @return DownloadTemplateModel
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/downloadErrorDictionaryFile", method = RequestMethod.GET)
	public DownloadTemplateModel downloadErrorDictionaryFile(
			@RequestParam(value="sourceID", required = true) int sourceID,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException {

		return activityMasterService.downloadErrorDictionaryFile(sourceID, response);
	}
	
	@RequestMapping(value = "/deleteNEList", method = RequestMethod.POST)
	public Response<Void> deleteNEList(@RequestBody DeleteNEListModel deleteNEListModel) {
		Response<Void> response=activityMasterService.deleteNEList(deleteNEListModel);	
		LOG.info("TBL_NETWORK_ELEMENT : SUCCESS");
		return response;
		}	
}
