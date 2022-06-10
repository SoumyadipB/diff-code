/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
package com.ericsson.isf.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.AccessManagementDAO;
import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.CRManagementDAO;
import com.ericsson.isf.dao.ProjectDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.helper.FileCSVHelper;
import com.ericsson.isf.model.ActivityMasterModel;
import com.ericsson.isf.model.AdhocBookingProjectModel;
import com.ericsson.isf.model.AllocatedResourceModel;
import com.ericsson.isf.model.AuditDataModel;
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
import com.ericsson.isf.model.NodeNamesByFilterModel;
import com.ericsson.isf.model.NotificationLogModel;
import com.ericsson.isf.model.ProjectApprovalModel;
import com.ericsson.isf.model.ProjectFilterModel;
import com.ericsson.isf.model.ResourceRequestCertificationModel;
import com.ericsson.isf.model.ResourceRequestCompetenceModel;
import com.ericsson.isf.model.ResourceRequestWorkEffortsModel;
import com.ericsson.isf.model.ResourceStatusModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.ScopeDetailsModel;
import com.ericsson.isf.model.ServiceAreaModel;
import com.ericsson.isf.model.ShiftTimmingModel;
import com.ericsson.isf.model.ShiftTimmingModel2;
import com.ericsson.isf.model.SpocModel;
import com.ericsson.isf.model.StandardActivityModel;
import com.ericsson.isf.model.TaskModel;
import com.ericsson.isf.model.TaskToolModel;
import com.ericsson.isf.model.TechnologyModel;
import com.ericsson.isf.model.TimeZoneModel;
import com.ericsson.isf.model.ToolsModel;
import com.ericsson.isf.model.UserFeedbackModel;
import com.ericsson.isf.model.VendorModel;
import com.ericsson.isf.model.WFStepInstructionModel;
import com.ericsson.isf.model.WorkEffortModel;
import com.ericsson.isf.model.WorkFlowFeedbackActivityModel;
import com.ericsson.isf.model.WorkFlowFeedbackModel;
import com.ericsson.isf.model.WorkFlowFilterModel;
import com.ericsson.isf.model.WorkInstructionModel;
import com.ericsson.isf.model.botstore.TblAdhocBooking;
import com.ericsson.isf.service.audit.AuditManagementService;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AppUtil;
import com.ericsson.isf.util.ApplicationMessages;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.ericsson.isf.util.DateTimeUtil;
import com.ericsson.isf.util.FileUtil;
import com.ericsson.isf.util.IsfCustomIdInsert;

/**
 *
 * @author ekarath
 */
@Service
public class ActivityMasterService {

	@Autowired
	private ActivityMasterDAO activityMasterDAO;

	@Autowired
	private CRManagementDAO changeManagementDAO;

	@Autowired
	private ProjectDAO projectDao;

	@Autowired
	private OutlookAndEmailService emailService;

	@Autowired
	private FlowChartService flowChartService;

	@Autowired
	private ToolsMasterService toolsMasterService;

	@Autowired
	private DemandForecastService demandForecastService;

	@Autowired
	private IsfCustomIdInsert isfCustomIdInsert;

	@Autowired
	private AccessManagementDAO accessManagementDAO;
	
	@Autowired
	private AuditManagementService auditManagmentService;


	@Autowired
	private AppService appService;
	
	@Autowired
	private ApplicationConfigurations configurations;
	

	
	@Autowired
	private ValidationUtilityService validationUtilityService;

	private static final Logger LOG = LoggerFactory.getLogger(ActivityMasterService.class);

	private static final String VALUE_CANNOT_BE_0 = "Invalid input.... %s cannot be 0 !!!";
	private static final String ACT_TYPE = "actType";
	private static final String ID = "id";
	private static final String DEPLOYED = "Deployed";
	private static final String RESOURCE_ALLOCATED = "Resource Allocated";
	private static final String PAGE_LINK2 = "pageLink2";
	private static final String PAGE_LINK1 = "pageLink1";
	private static final String NO_DETAILS_EXISTS = "No details exists";
	private static final String PM = "Project/Search";
	private static final String FM = "Resource/Request";
	private static String POSITION_STATUS_PROPOSALPENDING = "Proposal Pending";
	private static String POSITION_STATUS_PROPOSED = "Proposed";
	private static String POSITION_STATUS_REJECTED_A = "capacityNotAvailable";
	private static String POSITION_STATUS_REJECTED_B = "competenceNotAvailable";
	private static final String APPROVED = "Approved";
	private static final String REJECTED = "Rejected";
	private static final String VENDOR_ID = "VendorID";
	private static final String TASK_ID = "TaskID";
	private static final String TECHNOLOGY_ID = "TechnologyID";
	private static final String SERVICE_AREA_ID = "ServiceAreaID";
	private static final String DOMAIN_ID = "DomainID";
	private static final String SUB_ACTIVITY_ID = "SubActivityID";
	private static final String ALREADY_EXISTS = "%s already Exists !!";
	private static final String DO_NOT_EXISTS = " do not Exists !! ";
	private static final String CANNOT_BE_NULL = "Invalid input... %s cannot be Null !!!";
	private static final String RECORDS_TOTAL = "recordsTotal";

	private static final String RECORDS_FILTERED = "recordsFiltered";
	private static final String NETWORK_ENGINEER ="Network Engineer";
	private static final String DELIVERY_RESPONSIBLE ="Delivery Responsible";
	private static final String PROJECT_MANAGER ="Project Manager";
	private static final String OPERATIONAL_MANAGER ="Operational Manager";
	private static final String NE_DELETED = "Network Element(s) successfully deleted!";
	private static final String DELETE_MAXLIMIT = "You have already reached Max limit (500) of rows allowed for deletion in one go. ";
	private static final String	NE_NOT_NULL = "Invalid Input! Network Element ID cannot be 0 or Signum cannot be NULL.";
			
	private static final String DomainTechIDNotExist= "DomainId or TechID Does not Exist";


	/**
	 * 
	 * @param projectScopeID
	 * @return List<Map<String, Object>>
	 */
	@Transactional("transactionManager")
	public Response<List<Map<String, Object>>> getSubScopebyScopeID(String projectScopeID) {
		Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
		List<Map<String, Object>> subScopeList = activityMasterDAO.getSubScopebyScopeID(projectScopeID);
		if (CollectionUtils.isEmpty(subScopeList)) {
			response.addFormError(NO_DETAILS_EXISTS);
		} else {
			response.setResponseData(subScopeList);
		}
		return response;
	}

	/**
	 * 
	 * @param projectScopeDetailID
	 * @return List<Map<String, Object>>
	 */
	@Transactional("transactionManager")
	public Response<List<Map<String, Object>>> getActivitiesBySubScopeId(int projectScopeDetailID) {
		Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
		List<Map<String, Object>> activities = activityMasterDAO.getActivitiesBySubScopeId(projectScopeDetailID);
		if (CollectionUtils.isEmpty(activities)) {
			response.addFormError(NO_DETAILS_EXISTS);
		} else {
			response.setResponseData(activities);
		}
		return response;
	}

	/**
	 * 
	 * @param projectScopeID
	 * @return List<Map<String, Object>>
	 */
	@Transactional("transactionManager")
	public Response<List<Map<String, Object>>> getViewScopeDetails(String projectScopeID) {
		Response<List<Map<String, Object>>> apiResponse=new Response<List<Map<String,Object>>>();
		try {
			List<Map<String, Object>> subScopeList = activityMasterDAO.getSubScopebyScopeID(projectScopeID);
			apiResponse.setResponseData(subScopeList);

			for (Map<String, Object> row : subScopeList) {
				int projectScopeDetailID = (int) row.get("ProjectScopeDetailID");
				List<Map<String, Object>> checkIfDetailsExists1 = this.activityMasterDAO
						.getActivitiesBySubScopeId(projectScopeDetailID);
				if (!checkIfDetailsExists1.isEmpty()) {
					row.put("ViewScopeDetails", checkIfDetailsExists1);
				}

			}
			if (CollectionUtils.isEmpty(subScopeList)) {
				throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, NO_DETAILS_EXISTS);
			} else {
				return apiResponse;
			}

		}catch (Exception e) {
			apiResponse.addFormError(e.getMessage());
			LOG.error(e.getMessage());
		}

		return apiResponse;
	}

	/**
	 * 
	 * @param serviceAreaModel
	 */
	@Transactional("transactionManager")
	/* Closes connection automatically */
	public void saveServiceAreaDetails(ServiceAreaModel serviceAreaModel) {

		if (StringUtils.isBlank(serviceAreaModel.getServiceArea())
				|| StringUtils.isBlank(serviceAreaModel.getSubServiceArea())) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(CANNOT_BE_NULL, "Service Area or SubService Area"));
		} else {
			boolean isServiceAreaExists = activityMasterDAO.isServiceAreaExists(serviceAreaModel.getServiceArea(),
					serviceAreaModel.getSubServiceArea());
			if (isServiceAreaExists) {
				throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
						String.format(ALREADY_EXISTS, "Service Area name"));
			} else {
				activityMasterDAO.saveServiceAreaDetails(serviceAreaModel);
			}
		}
	}

	/**
	 * 
	 * @param signum
	 * @param projectID
	 * @param servAreaID
	 * @return List<ServiceAreaModel>
	 */
	@Transactional("transactionManager")
	public List<ServiceAreaModel> getServiceAreaDetailsBySignum(String signum, Integer projectID, Integer servAreaID) {

		List<ServiceAreaModel> serviceAreaModels = activityMasterDAO.getServiceAreaDetailsBySignum(signum, projectID,
				servAreaID);
		if (CollectionUtils.isEmpty(serviceAreaModels)) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, NO_DETAILS_EXISTS);
		} else {
			return serviceAreaModels;
		}
	}

	/**
	 * 
	 * @param serviceAreaModel
	 */
	@Transactional("transactionManager")
	public void updateServiceAreaDetails(ServiceAreaModel serviceAreaModel) {

		if (StringUtils.isBlank(serviceAreaModel.getServiceArea())
				|| StringUtils.isBlank(serviceAreaModel.getSubServiceArea())
				|| StringUtils.isBlank(serviceAreaModel.getLastModifiedBy())) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(CANNOT_BE_NULL, "Service Area or SubService Area or LastModifiedBy"));
		} else {
			activityMasterDAO.updateServiceAreaDetails(serviceAreaModel);
		}

	}

	/**
	 * 
	 * @param projectID
	 * @param servAreaID
	 * @return List<ServiceAreaModel>
	 */
	public List<ServiceAreaModel> getServiceAreaDetails(Integer projectID, Integer servAreaID) {

		List<ServiceAreaModel> serviceAreaDetails = activityMasterDAO.getServiceAreaDetails(projectID, servAreaID);
		if (CollectionUtils.isEmpty(serviceAreaDetails)) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, NO_DETAILS_EXISTS);
		} else {
			return serviceAreaDetails;
		}
	}

	/**
	 * 
	 * @param technologyModel
	 */
	@Transactional("transactionManager")
	public void saveTechnologyDetails(TechnologyModel technologyModel) {

		boolean istechnologyExists = activityMasterDAO.isTechnologyExists(technologyModel.getTechnology());
		if (istechnologyExists) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(ALREADY_EXISTS, "Technology"));
		} else {
			activityMasterDAO.saveTechnologyDetails(technologyModel);
		}
	}

	/**
	 * 
	 * @param technologyModel
	 */
	@Transactional("transactionManager")
	public void updateTechnologyDetails(TechnologyModel technologyModel) {

		activityMasterDAO.updateTechnologyDetails(technologyModel);
	}

	/**
	 * 
	 * @param domainId
	 * @param projectID
	 * @return List<TechnologyModel>
	 */
	public List<TechnologyModel> getTechnologyDetails(Integer domainId, Integer projectID) {

		List<TechnologyModel> technologyModel = activityMasterDAO.getTechnologyDetails(domainId, projectID);
		if (CollectionUtils.isEmpty(technologyModel)) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, NO_DETAILS_EXISTS);
		} else {
			return technologyModel;
		}
	}

	/**
	 * 
	 * @param standardActivityModel
	 */
	@Transactional("transactionManager")
	public void saveActivityAndSubActivityDetails(StandardActivityModel standardActivityModel) {

		boolean isDomainTechExist = activityMasterDAO.isDomainTechExist(standardActivityModel);
		boolean isSubActivityExists = activityMasterDAO.isSubActivtyExists(standardActivityModel);
		boolean isSubActivityExistsInActive = activityMasterDAO.isSubActivityExistsInActive(standardActivityModel);

		boolean isDomainTechIDExist = activityMasterDAO.isDomainTechIDExist(standardActivityModel);
		boolean isDomainTechIDExistInActive = activityMasterDAO.isDomainTechIDExistInActive(standardActivityModel);

		if (isDomainTechExist) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, String.format(DomainTechIDNotExist));
		}

		else {
			if (isSubActivityExists) {
				throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
						String.format(ALREADY_EXISTS, "Activity & SubActivity"));
			}

			// Case with Inactive 0 then it's value will be converted to Active 1
			else if (isSubActivityExistsInActive) {
				activityMasterDAO.updateInActiveValue(standardActivityModel);
			}

			else {
				if (isDomainTechIDExist) {
					activityMasterDAO.saveActivityAndSubActivityDetails(standardActivityModel);
				} else {
					if (isDomainTechIDExistInActive) {
						activityMasterDAO.updatDtIdActive(standardActivityModel);
						activityMasterDAO.saveActivityAndSubActivityDetails(standardActivityModel);
					} else {
						activityMasterDAO.insertDomainTechID(standardActivityModel);
						activityMasterDAO.saveActivityAndSubActivityDetails(standardActivityModel);
					}
				}

			}
		}

	}

	/**
	 * 
	 * @param standardActivityModel
	 */
	@Transactional("transactionManager")
	public void updateActivityAndSubActivityDetails(StandardActivityModel standardActivityModel) {

		activityMasterDAO.updateActivityAndSubActivityDetails(standardActivityModel);
	}

	/**
	 * 
	 * @param domainID
	 * @param serviceAreaID
	 * @param technologyID
	 * @return List<StandardActivityModel>
	 */
	public List<StandardActivityModel> getActivityAndSubActivityDetails(int domainID, int serviceAreaID,
			int technologyID) {

		if (domainID == 0 && serviceAreaID == 0 && technologyID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, "DomainID or ServiceAreaID or TechnologyID"));
		} else {
			return activityMasterDAO.getActivityAndSubActivityDetails(domainID, serviceAreaID, technologyID);
		}
	}

	/**
	 * 
	 * @param domainModel
	 */
	@Transactional("transactionManager")
	public void saveDomainDetails(DomainModel domainModel) {

		boolean isDomainExist = activityMasterDAO.isDomainExits(domainModel.getDomain(), domainModel.getSubDomain());

		if (isDomainExist) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(ALREADY_EXISTS, "Domain"));
		} else {
			activityMasterDAO.saveDomainDetails(domainModel);
		}

	}

	/**
	 * 
	 * @param projectID
	 * @param serviceAreaID
	 * @return List<DomainModel>
	 */
	public List<DomainModel> getDomainDetails(Integer projectID, Integer serviceAreaID) {

		List<DomainModel> domainModels = activityMasterDAO.getDomainDetails(projectID, serviceAreaID);
		if (CollectionUtils.isEmpty(domainModels)) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, NO_DETAILS_EXISTS);
		} else {
			return domainModels;
		}

	}

	/**
	 * 
	 * @param signum
	 * @param projectID
	 * @param serviceAreaID
	 * @return List<DomainModel>
	 */
	public List<DomainModel> getDomainDetailsBySignum(String signum, Integer projectID, Integer serviceAreaID) {

		List<DomainModel> domainModels = activityMasterDAO.getDomainDetailsBySignum(signum, projectID, serviceAreaID);
		if (CollectionUtils.isEmpty(domainModels)) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, NO_DETAILS_EXISTS);
		} else {
			return domainModels;
		}
	}

	/**
	 * 
	 * @param signum
	 * @return List<HashMap<String, Object>>
	 */
	public List<HashMap<String, Object>> getAllProjectsBySignum(String signum) {

		List<HashMap<String, Object>> projects = activityMasterDAO.getAllProjectsBySignum(signum);
		if (CollectionUtils.isEmpty(projects)) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, NO_DETAILS_EXISTS);
		} else {
			return projects;
		}
	}

	/**
	 * 
	 * @param serviceAreaID
	 * @return List<DomainModel>
	 */
	public Response<List<DomainModel>> getAllDomainDetailsByService(Integer serviceAreaID) {

		Response<List<DomainModel>> response = new Response<List<DomainModel>>();
		List<DomainModel> domainModels = activityMasterDAO.getAllDomainDetailsByService(serviceAreaID);
		if (CollectionUtils.isEmpty(domainModels)) {
			response.addFormError(NO_DETAILS_EXISTS);
		} else {
			response.setResponseData(domainModels);
		}
		return response;
	}

	/**
	 * 
	 * @param domainModel
	 */
	@Transactional("transactionManager")
	public void updateDomainDetails(DomainModel domainModel) {

		activityMasterDAO.updateDomainDetails(domainModel);
	}

	/**
	 * 
	 * @param taskModel
	 */
	@Transactional("transactionManager")
	public void saveTaskDetails(TaskModel taskModel) {

		boolean isTaskExists = activityMasterDAO.isTaskExists(taskModel.getTask(), taskModel.getSubActivityID());
		if (isTaskExists) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(ALREADY_EXISTS, "Task for the SubActivity"));
		} else {
			activityMasterDAO.saveTaskDetails(taskModel);
			int taskIdAndInstanceId = isfCustomIdInsert.generateCustomId(taskModel.getTaskID());
			taskModel.setTaskID(taskIdAndInstanceId);

			if (taskModel.getTaskID() != 0) {
				taskModel.getTools().stream().map((tool) -> {
					TaskToolModel tskToolsModel = new TaskToolModel();
					tskToolsModel.setTaskID(taskModel.getTaskID());
					tskToolsModel.setToolID(tool.getToolID());
					return tskToolsModel;
				}).map((tskToolsModel) -> {
					tskToolsModel.setCreatedBy(taskModel.getCreatedBy());
					return tskToolsModel;
				}).forEachOrdered((tskToolsModel) -> {
					mapTaskAndTool(tskToolsModel);
				});
				syncProjectConfTable(taskModel);
			}
		}
	}

	/**
	 * 
	 * @param subActivityID
	 * @param signumID
	 * @return
	 */
	public List<TaskModel> getTaskDetails(int subActivityID, String signumID) {

		if (subActivityID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, SUB_ACTIVITY_ID));
		} else {
			return activityMasterDAO.getTaskDetails(subActivityID, signumID);
		}
	}

	/**
	 * 
	 * @param projectID
	 * @param domain
	 * @param subDomain
	 * @param technologyID
	 * @param elementType
	 * @return List<String>
	 */
	public Response<List<String>> getNodeTypeByFilter(int projectID, String domain, String subDomain,
			String technologyID, String elementType) {
		Response<List<String>> response = new Response<List<String>>();
		try {
			response.setResponseData(activityMasterDAO.getNodeTypeByFilter(
					getNodeNamesByFilterModel(projectID, domain, subDomain, technologyID, elementType)));
		} catch (Exception e) {
			response.addFormError(e.getMessage());
		}
		return response;
	}

	/**
	 * 
	 * @param projectID
	 * @param domain
	 * @param subDomain
	 * @param technologyID
	 * @param elementType
	 * @return NodeNamesByFilterModel
	 */
	private NodeNamesByFilterModel getNodeNamesByFilterModel(int projectID, String domain, String subDomain,
			String technologyID, String elementType) {

		NodeNamesByFilterModel nodeNamesByFilterModel = new NodeNamesByFilterModel();
		nodeNamesByFilterModel.setProjectID(projectID);
		nodeNamesByFilterModel.setDomain(domain);
		nodeNamesByFilterModel.setSubDomain(subDomain);
		nodeNamesByFilterModel.setTech(technologyID);
		nodeNamesByFilterModel.setElementType(elementType);
		return nodeNamesByFilterModel;
	}

	/**
	 * 
	 * @param taskModel
	 */
	@Transactional("transactionManager")
	public void updateTaskDetails(TaskModel taskModel) {

		activityMasterDAO.updateTaskDetails(taskModel);
		TaskToolModel taskToolModel = new TaskToolModel();
		taskToolModel.setTaskID(taskModel.getTaskID());
		taskToolModel.setLastModifiedBy(taskModel.getLastModifiedBy());
		taskModel.getTools().forEach((toolModel) -> {
			taskToolModel.setToolID(toolModel.getToolID());
		});
		activityMasterDAO.updateTaskToolDetails(taskToolModel);
		syncProjectConfTable(taskModel);
	}

	/**
	 * 
	 * @return List<TaskModel>
	 */
	public List<TaskModel> getTaskToolDetails() {

		List<TaskModel> taskModels = activityMasterDAO.getTaskToolDetails();
		if (CollectionUtils.isEmpty(taskModels)) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, NO_DETAILS_EXISTS);
		} else {
			return taskModels;
		}
	}

	/**
	 * 
	 * @param taskToolModel
	 */
	@Transactional("transactionManager")
	public void mapTaskAndTool(TaskToolModel taskToolModel) {
		activityMasterDAO.mapTaskAndTool(taskToolModel);
	}

	/**
	 * 
	 * @param taskToolModel
	 */
	@Transactional("transactionManager")
	public void updateTaskToolMapping(TaskToolModel taskToolModel) {
		activityMasterDAO.updateTaskToolMapping(taskToolModel);
	}

	/**
	 * 
	 * @param domainID
	 * @param signumID
	 */
	@Transactional("transactionManager")
	public void deleteDomainDetails(int domainID, String signumID) {

		if (domainID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, DOMAIN_ID));
		} else {
			activityMasterDAO.deleteDomainDetails(domainID, signumID);
		}

	}

	/**
	 * 
	 * @param serviceAreaID
	 * @param signumID
	 */
	@Transactional("transactionManager")
	public void deleteServiceArea(int serviceAreaID, String signumID) {

		if (serviceAreaID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, SERVICE_AREA_ID));
		} else {
			activityMasterDAO.deleteServiceArea(serviceAreaID, signumID);
		}
	}

	/**
	 * 
	 * @param technologyID
	 * @param signumID
	 */
	@Transactional("transactionManager")
	public void deleteTechnology(int technologyID, String signumID) {

		if (technologyID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, TECHNOLOGY_ID));
		} else {
			activityMasterDAO.deleteTechnology(technologyID, signumID);
		}
	}

	/**
	 * 
	 * @param subActivityID
	 * @param signumID
	 */
	@Transactional("transactionManager")
	public void deleteActivityAndSubActivity(int subActivityID, String signumID) {

		if (subActivityID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, SUB_ACTIVITY_ID));
		} else {
			activityMasterDAO.deleteActivityAndSubActivity(subActivityID, signumID);
		}
	}

	/**
	 * 
	 * @param taskID
	 * @param signumID
	 */
	@Transactional("transactionManager")
	public void deleteTaskDetails(int taskID, String signumID) {

		if (taskID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, TASK_ID));
		} else {
			activityMasterDAO.deleteTaskDetails(taskID, signumID);
		}

	}

	/**
	 * 
	 * @param taskToolID
	 * @param signumID
	 */
	@Transactional("transactionManager")
	public void deleteTaskToolMapping(int taskToolID, String signumID) {

		if (taskToolID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, "TaskToolID"));
		} else {
			activityMasterDAO.deleteTaskToolMapping(taskToolID, signumID);
		}
	}

	/**
	 * 
	 * @param domainID
	 * @param signumID
	 * @return
	 */
	public List<DomainModel> getDomainDetailsByID(int domainID, String signumID) {

		if (domainID == 0) {

			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, DOMAIN_ID));
		} else {
			return activityMasterDAO.getDomainDetailsByID(domainID, signumID);
		}
	}

	/**
	 * 
	 * @param serviceAreaID
	 * @param signumID
	 * @return
	 */
	public List<ServiceAreaModel> getServiceAreaDetailsByID(int serviceAreaID, String signumID) {

		if (serviceAreaID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, SERVICE_AREA_ID));
		} else {
			return activityMasterDAO.getServiceAreaDetailsByID(serviceAreaID, signumID);
		}
	}

	/**
	 * 
	 * @param technologyID
	 * @param ServiceAreaID
	 * @return List<TechnologyModel>
	 */
	public List<TechnologyModel> getTechnologyDetailsByDomain(Integer technologyID, Integer ServiceAreaID) {

		return activityMasterDAO.getTechnologyDetailsByDomain(technologyID, ServiceAreaID);
	}

	/**
	 * 
	 * @param technologyID
	 * @param signumID
	 * @return List<TechnologyModel>
	 */
	public List<TechnologyModel> getTechnologyDetailsByID(int technologyID, String signumID) {

		if (technologyID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, TECHNOLOGY_ID));
		} else {
			return activityMasterDAO.getTechnologyDetailsByID(technologyID, signumID);
		}
	}

	/**
	 * 
	 * @param subActivityID
	 * @param signumID
	 * @return List<StandardActivityModel>
	 */
	public List<StandardActivityModel> getActivityAndSubActivityDetailsByID(int subActivityID, String signumID) {

		if (subActivityID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, SUB_ACTIVITY_ID));
		} else {
			return activityMasterDAO.getActivityAndSubActivityDetailsByID(subActivityID, signumID);
		}
	}

	/**
	 * 
	 * @param technologyID
	 * @param serviceAreaID
	 * @return List<ActivityMasterModel>
	 */
	public List<ActivityMasterModel> getAutoComDetails(int technologyID, int serviceAreaID) {

		if (technologyID == 0 && serviceAreaID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, "TechnologyID or ServiceAreaID"));
		} else {
			return activityMasterDAO.getAutoComDetails(technologyID, serviceAreaID);
		}
	}

	/**
	 * 
	 * @param taskID
	 * @param signumID
	 * @return List<TaskModel>
	 */
	public List<TaskModel> getTaskToolDetailsByID(int taskID, String signumID) {

		if (taskID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, TASK_ID));
		} else {
			return activityMasterDAO.getTaskToolDetailsByID(taskID, signumID);
		}
	}

	/**
	 * 
	 * @param taskID
	 * @param task
	 * @return List<TaskModel>
	 */
	public List<TaskModel> getTaskDetailsByID(int taskID, String task) {

		if (taskID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, TASK_ID));
		} else {
			return activityMasterDAO.getTaskDetailsByID(taskID, task);
		}
	}

	/**
	 * 
	 * @param signum
	 * @return EmployeeModel
	 */
	@Transactional("transactionManager")
	public EmployeeModel getEmployeeBySignum(String signum) {

		boolean isEmployeeExists = activityMasterDAO.isEmployeeExists(signum);
		if (isEmployeeExists) {
			return activityMasterDAO.getEmployeeBySignum(signum);
		} else {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					"Employee with signum " + signum + DO_NOT_EXISTS);
		}
	}

	/**
	 * 
	 * @param signum
	 * @return EmployeeModel
	 */
	@Transactional("transactionManager")
	public EmployeeModel getAspDetailsBySignum(String signum) {
		return activityMasterDAO.getAspDetailsBySignum(signum);
	}

	/**
	 * This method get all sub-employees detail of manager.
	 * 
	 * @param managerSignum
	 * @return Response<List<EmployeeModel>>
	 */
	public Response<List<EmployeeModel>> getEmployeesByManager(String managerSignum) {

		Response<List<EmployeeModel>> apiResponse = new Response<List<EmployeeModel>>();

		try {

			boolean isEmployeeExists = activityMasterDAO.isEmployeeByManagerExists(managerSignum);
			if (isEmployeeExists) {
				apiResponse.setResponseData(activityMasterDAO.getEmployeesByManager(managerSignum));
			} else {
				throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
						"Employees with manager signum " + managerSignum + DO_NOT_EXISTS);
			}

		} catch (Exception ex) {

			apiResponse.addFormError(ex.getMessage());
		}

		return apiResponse;

	}

	/**
	 * 
	 * @return List<HashMap<String, String>>
	 */
	public List<HashMap<String, String>> getAllLineManagers() {

		List<HashMap<String, String>> managerList = activityMasterDAO.getAllLineManagers();
		if (CollectionUtils.isNotEmpty(managerList)) {
			return managerList;
		} else {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "No Line manager data Exists !! ");
		}
	}

	/**
	 * 
	 * @return List<EmployeeBasicDetails>
	 */
	public List<EmployeeBasicDetails> getEmployeeDetails() {
		return activityMasterDAO.getEmployeeDetails();
	}

	/**
	 * 
	 * @param term
	 * @return List<EmployeeBasicDetails>
	 */
	public List<EmployeeBasicDetails> getEmployeesByFilter(String term) {
		return activityMasterDAO.getEmployeesByFilter(term);
	}
	public List<EmployeeBasicDetails> getEmployeesByFilter1(String term) {
		return activityMasterDAO.getEmployeesByFilter1(term);
	}

	/**
	 * 
	 * @param projectID
	 * @return List<EmployeeModel>
	 */
	public List<EmployeeModel> getEmployeeByProject(int projectID) {
		return activityMasterDAO.getEmployeeByProject(projectID);
	}

	/**
	 * 
	 * @param projectId
	 * @return List<ProjectApprovalModel>
	 */
	public List<ProjectApprovalModel> getProjectApprovalDetailsById(int projectId) {

		if (projectId == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, "ProjectID"));
		} else {
			boolean isExists = activityMasterDAO.isProjectApprovalByProjectIdExists(projectId);
			if (isExists) {
				return activityMasterDAO.getProjectApprovalDetailsById(projectId);
			} else {
				throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
						"Project Approvals  with project ID " + projectId + DO_NOT_EXISTS);
			}
		}

	}

	/**
	 * 
	 * @param approverSignum
	 * @param marketArea
	 * @return List<ProjectApprovalModel>
	 */
	public List<ProjectApprovalModel> getProjectApprovalsByApprover(String approverSignum, String marketArea) {

		boolean isExists = activityMasterDAO.isProjectApprovalByApproverExists(approverSignum);
		if (isExists) {
			return activityMasterDAO.getProjectApprovalsByApprover(approverSignum, marketArea);
		} else {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					"Project Approvals  with approver signum " + approverSignum + DO_NOT_EXISTS);
		}
	}

	/**
	 * 
	 * @param projectApprovalModel
	 * @param signumID 
	 * @return boolean
	 */
	@Transactional("transactionManager")
	public boolean updateProjectApprovalDetails(ProjectApprovalModel projectApprovalModel, String Signum) {

		boolean response = activityMasterDAO.updateProjectApprovalDetails(projectApprovalModel,Signum);
		projectDao.changeProjectStaus(projectApprovalModel.isApprovedOrRejected() ? APPROVED : REJECTED,
				projectApprovalModel.getApproverSignum(), projectApprovalModel.getProjectId());

		if (response && projectApprovalModel.isApprovedOrRejected()) {
			activityMasterDAO.activateOpportunity(projectApprovalModel.getProjectId());
		}
		response = activityMasterDAO.updateProjectByApproval(projectApprovalModel.getProjectId());
		return response;

	}

	/**
	 * 
	 * @param resourceRequestCompetenceModel
	 * @return boolean
	 */
	@Transactional("transactionManager")
	public boolean updateResourceRequestCompetence(ResourceRequestCompetenceModel resourceRequestCompetenceModel) {

		return activityMasterDAO.updateResourceRequestCompetence(resourceRequestCompetenceModel);
	}

	/**
	 * 
	 * @param resourceRequestCertificationModel
	 * @return boolean
	 */
	@Transactional("transactionManager")
	public boolean updateResourceRequestCertification(
			ResourceRequestCertificationModel resourceRequestCertificationModel) {

		return activityMasterDAO.updateResourceRequestCertification(resourceRequestCertificationModel);
	}

	/**
	 * 
	 * @param resourceRequestCertificationModel
	 * @return boolean
	 */
	@Transactional("transactionManager")
	public boolean addCertificationByRRID(ResourceRequestCertificationModel resourceRequestCertificationModel) {
		boolean flag=false;
		boolean isCertificateExists = activityMasterDAO
				.isCertificateExists(resourceRequestCertificationModel.getCertificateId());
		boolean isResourceRequestExists = activityMasterDAO
				.isResourceRequestExists(resourceRequestCertificationModel.getResourceRequestId());
		if (isCertificateExists && isResourceRequestExists) {
			flag=activityMasterDAO.addCertificationByRRID(resourceRequestCertificationModel);
		}
		return flag;	
	}

	/**
	 * 
	 * @param resourceRequestCompetenceModel
	 * @return boolean
	 */
	@Transactional("transactionManager")
	public boolean addCompetenceByRRID(ResourceRequestCompetenceModel resourceRequestCompetenceModel) {

		boolean isCompetenceExists = activityMasterDAO
				.isCompetenceExists(resourceRequestCompetenceModel.getCompetenceId());
		boolean isResourceRequestExists = activityMasterDAO
				.isResourceRequestExists(resourceRequestCompetenceModel.getResourceRequestId());
		if (isCompetenceExists && isResourceRequestExists)
			return activityMasterDAO.addCompetenceByRRID(resourceRequestCompetenceModel);
		else
			return false;
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
	public List<NetworkElementModel> searchNetworkElementDetails(int projectID, String domainID, String technologyID,
			String vendorID, String marketType, String elementType) {

		return activityMasterDAO.searchNetworkElementDetails(
				getNetworkElementModel(projectID, domainID, technologyID, vendorID, marketType, elementType));
	}

	/**
	 * 
	 * @param projectID
	 * @param domainID
	 * @param technologyID
	 * @param vendorID
	 * @param marketType
	 * @param elementType
	 * @return NetworkElementModel
	 */
	private NetworkElementModel getNetworkElementModel(int projectID, String domainID, String technologyID,
			String vendorID, String marketType, String elementType) {

		NetworkElementModel networkElementModel = new NetworkElementModel();
		networkElementModel.setProjectID(projectID);
		networkElementModel.setRequestedTechnologyID(technologyID);
		networkElementModel.setRequestedDomainID(domainID);
		networkElementModel.setRequestedVendorID(vendorID);
		networkElementModel.setMarket(marketType);
		networkElementModel.setElementType(elementType);
		return networkElementModel;
	}

	/**
	 * 
	 * @param projectID
	 * @param dataTableReq
	 * @return Map<String, Object>
	 */
	public Response<NetworkElements> getNetworkElementDetails(int projectID, DataTableRequest dataTableReq) {
		Response<NetworkElements> response=new Response<>();
		NetworkElements networkElements=new NetworkElements();
		networkElements.setData(activityMasterDAO.getNetworkElementDetails(projectID, dataTableReq));
		networkElements.setDraw(dataTableReq.getDraw());
		int total = activityMasterDAO.getNetworkElementDetailsCount(projectID, dataTableReq);
		networkElements.setRecordsTotal(total);
		networkElements.setRecordsFiltered(total);
		response.setResponseData(networkElements);
		return response;
	}

	/**
	 * 
	 * @param networkElementID
	 * @param signumID
	 */
	@Transactional("transactionManager")
	public void deleteNetworkElementDetails(int networkElementID, String signumID) {

		if (networkElementID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, "NetworkElementID"));
		} else {
			activityMasterDAO.deleteNetworkElementDetails(networkElementID, signumID);
		}
	}

	
	/**
	 * 
	 * @param vendorID
	 * @param signumID
	 */
	@Transactional("transactionManager")
	public void deleteVendorDetails(int vendorID, String signumID) {

		if (vendorID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, VENDOR_ID));
		} else {
			activityMasterDAO.deleteVendorDetails(vendorID, signumID);
		}
	}

	/**
	 * 
	 * @param flowChartStepDetailsModel
	 */
	@Transactional("transactionManager")
	public void saveProjectDefinedTasks(FlowChartStepDetailsModel flowChartStepDetailsModel) {

		activityMasterDAO.saveProjectDefinedTasks(flowChartStepDetailsModel);
	}

	/**
	 * 
	 * @return List<VendorModel>
	 */
	public List<VendorModel> getVendorDetails() {

		return activityMasterDAO.getVendorDetails();
	}

	/**
	 * 
	 * @param vendorID
	 * @param signumID
	 * @return List<VendorModel>
	 */
	public List<VendorModel> getVendorDetailsByID(int vendorID, String signumID) {

		if (vendorID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, VENDOR_ID));
		} else {
			return activityMasterDAO.getVendorDetailsByID(vendorID, signumID);
		}
	}

	/**
	 * 
	 * @param vendorModel
	 */
	@Transactional("transactionManager")
	public void updateVendorDetails(VendorModel vendorModel) {

		activityMasterDAO.updateVendorDetails(vendorModel);

	}

	/**
	 * 
	 * @param vendorModel
	 */
	@Transactional("transactionManager")
	public void saveVendorDetails(VendorModel vendorModel) {

		boolean isVendorExists = activityMasterDAO.isVendorExists(vendorModel.getVendor());
		if (isVendorExists) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(ALREADY_EXISTS, "Vendor"));
		} else {
			activityMasterDAO.saveVendorDetails(vendorModel);
		}

	}

	/**
	 * 
	 * @param domainID
	 * @param technologyID
	 * @param serviceAreaID
	 * @param activityName
	 * @return List<ActivityMasterModel>
	 */
	public List<ActivityMasterModel> getSubActivityForActivity(int domainID, int technologyID, int serviceAreaID,
			String activityName) {
		return activityMasterDAO.getSubActivityForActivity(domainID, technologyID, serviceAreaID, activityName);
	}

	/**
	 * 
	 * @param allocatedResourceModel
	 * @param projectId
	 * @return boolean
	 * @throws ParseException
	 */
	@Transactional("transactionManager")
	public boolean updateProposedResources(List<AllocatedResourceModel> allocatedResourceModel, Integer projectId) {
		boolean weFlag = false;
		boolean rpFlag = false;
		
		AuditDataModel auditDataModel=new AuditDataModel();

		boolean rejectFlag = false;

		for (AllocatedResourceModel allocatedResource : allocatedResourceModel) {
			WorkEffortModel wEffortModel = this.changeManagementDAO
					.getWorkEffortDetailsByID(allocatedResource.getWeid());
			String position = this.activityMasterDAO.getPositionStatusByID(allocatedResource.getWeid());
			if (allocatedResource.getPositionStatus().equalsIgnoreCase(POSITION_STATUS_REJECTED_A)) {
				rejectFlag = true;
				if (!wEffortModel.getPositionStatus().equalsIgnoreCase(DEPLOYED)
						|| !wEffortModel.getPositionStatus().equalsIgnoreCase(RESOURCE_ALLOCATED)) {
					if (!position.equals(DEPLOYED) && !position.equals(RESOURCE_ALLOCATED)) {
						rpFlag = this.activityMasterDAO.updateResourcePositionToRejected(allocatedResource.getWeid(),
								allocatedResource.getLoggedInUser(), REJECTED + "_NO_CAPACITY");
					}
					weFlag = this.activityMasterDAO.updateWorkEffort(allocatedResource, 1);
					this.changeManagementDAO.updateWorkEffortStatusByWeId(false, allocatedResource.getWeid() + "");
					
					// Setting data in AuditDataModel
					auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
					auditDataModel.setCreatedBy(allocatedResource.getLoggedInUser());
					auditDataModel.setAuditPageId(allocatedResource.getWeid());
					auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_WORK_EFFORT);
					auditDataModel.setActorType(AppConstants.FULFILLMENT_MANAGER);
					auditDataModel.setCommentCategory(AppConstants.RESOURCE_REJECTION_DATE_BY_FM);
					auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
					auditDataModel.setOldValue(AppConstants.PROPOSAL_PENDING);
					auditDataModel.setNewValue(AppConstants.REJECTED_NO_CAPACITY);
					auditDataModel.setType(AppConstants.AUDIT);
					auditDataModel.setSourceId(1);
					auditManagmentService.addToAudit(auditDataModel);
					
				}
			} else if (allocatedResource.getPositionStatus().equalsIgnoreCase(POSITION_STATUS_REJECTED_B)) {
				rejectFlag = true;
				if (!wEffortModel.getPositionStatus().equalsIgnoreCase(DEPLOYED)
						|| !wEffortModel.getPositionStatus().equalsIgnoreCase(RESOURCE_ALLOCATED)) {
					if (!position.equals(DEPLOYED) && !position.equals(RESOURCE_ALLOCATED)) {
						rpFlag = this.activityMasterDAO.updateResourcePositionToRejected(allocatedResource.getWeid(),
								allocatedResource.getLoggedInUser(), REJECTED + "_NO_COMPETENCE");
					}
					weFlag = this.activityMasterDAO.updateWorkEffort(allocatedResource, 1);
					this.changeManagementDAO.updateWorkEffortStatusByWeId(false, allocatedResource.getWeid() + "");
					
					// Setting data in AuditDataModel
					auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
					auditDataModel.setCreatedBy(allocatedResource.getLoggedInUser());
					auditDataModel.setAuditPageId(allocatedResource.getWeid());
					auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_WORK_EFFORT);
					auditDataModel.setActorType(AppConstants.FULFILLMENT_MANAGER);
					auditDataModel.setCommentCategory(AppConstants.RESOURCE_REJECTION_DATE_BY_FM);
					auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
					auditDataModel.setOldValue(AppConstants.PROPOSAL_PENDING);
					auditDataModel.setNewValue(AppConstants.REJECTED_NO_COMPETENCE);
					auditDataModel.setType(AppConstants.AUDIT);
					auditDataModel.setSourceId(1);
					auditManagmentService.addToAudit(auditDataModel);
				}
			} else {
				WorkEffortModel wEffortModelExisting = this.changeManagementDAO
						.getWorkEffortDetailsByID(allocatedResource.getWeid());
				this.activityMasterDAO.insertProposedWorkEfforts(wEffortModelExisting.getWorkEffortID(),
						wEffortModelExisting.getHours(), wEffortModelExisting.getDuration(),
						wEffortModelExisting.getStartDate(), wEffortModelExisting.getEndDate());

				long duration = AppUtil.getNoOfWeekdayDaysBetweenDates(allocatedResource.getStartDate(),
						allocatedResource.getEndDate());
				allocatedResource.setDuration(duration);
				allocatedResource.setHours(
						(duration * (AppUtil.WORKING_HOURS_IN_A_DAY * allocatedResource.getFtePercentage()) / 100));
				LOG.info("StartDate: {}", allocatedResource.getStartDate());
				LOG.info("EndDate: {}", allocatedResource.getEndDate());
				weFlag = this.activityMasterDAO.updateWorkEffort(allocatedResource, 1);
				this.changeManagementDAO.updateWorkEffortStatusByWeId(false, allocatedResource.getWeid() + "");

				rpFlag = this.activityMasterDAO.updateResourcePositionToProposed(allocatedResource.getWeid(),
						allocatedResource.getLoggedInUser(), POSITION_STATUS_PROPOSED);

				Map<String, Object> map = this.activityMasterDAO.getProjectDetailsByWeid(allocatedResource.getWeid());

				Date tmpDate1 = allocatedResource.getStartDate();
				Calendar c = Calendar.getInstance();
				double percentageHour = (AppUtil.WORKING_HOURS_IN_A_DAY * allocatedResource.getFtePercentage()) / 100;
				map.put("hrsPerDay", percentageHour);
				for (int i = 0; i < duration; i++) {
					c.setTime(tmpDate1);

					int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
					if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {

						i = i - 1;
					} else {
						map.put("currentDate", c.getTime());
						this.activityMasterDAO.insertBookedResource(map);
					}
					c.add(Calendar.DATE, 1); // number of days to add
					tmpDate1 = c.getTime();
				}

				updatePoistionStatusByWfStatus(wEffortModel.getResourcePositionID());
				
				// Setting data in AuditDataModel
				auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
				auditDataModel.setCreatedBy(allocatedResource.getLoggedInUser());
				auditDataModel.setAuditPageId(allocatedResource.getWeid());
				auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_WORK_EFFORT);
				auditDataModel.setActorType(AppConstants.FULFILLMENT_MANAGER);
				auditDataModel.setCommentCategory(AppConstants.RESOURCE_PROPOSAL_DATE_BY_FM);
				auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
				auditDataModel.setOldValue(AppConstants.PROPOSAL_PENDING);
				auditDataModel.setNewValue(AppConstants.PROPOSED);
				auditDataModel.setType(AppConstants.AUDIT);
				auditDataModel.setSourceId(1);
				auditManagmentService.addToAudit(auditDataModel);
			}

			this.activityMasterDAO.appendToPositionComments(
					"(" + allocatedResource.getLoggedInUser() + ", Weid: " + allocatedResource.getWeid() + ") : "
							+ allocatedResource.getCommentsByFM(),
							wEffortModel.getResourcePositionID(), allocatedResource.getLoggedInUser());
		}

		demandForecastService.updateFMSummary(allocatedResourceModel, projectId);
		if (!rejectFlag)
			emailService.sendMail(AppConstants.NOTIFICATION_TYPE_PROPOSE,
					emailService.enrichMailforCM(allocatedResourceModel, AppConstants.NOTIFICATION_TYPE_PROPOSE));

		return weFlag && rpFlag;
	}

	/**
	 * 
	 * @param allocatedResourceModel
	 * @return boolean
	 * @throws ParseException
	 */
	@Transactional("transactionManager")
	public boolean cancelProposedResources(List<AllocatedResourceModel> allocatedResourceModel) throws ParseException {
		
		AuditDataModel auditDataModel=new AuditDataModel();

		for (AllocatedResourceModel allocatedResource : allocatedResourceModel) {

			int weid = allocatedResource.getWeid();
			WorkEffortModel wEffortModel = changeManagementDAO.getWorkEffortDetailsByID(weid);
			activityMasterDAO.updateWorkEffortPositionStatusById(weid, POSITION_STATUS_PROPOSALPENDING);
			changeManagementDAO.updateWorkEffortSignum(weid, allocatedResource.getLoggedInUser(), null);
			changeManagementDAO.updateWorkEffortStatusByWeId(true, String.valueOf(weid));
			activityMasterDAO.deleteBookedHoursByWeID(allocatedResource);
			updatePoistionStatusByWfStatus(wEffortModel.getResourcePositionID());
			
			// Setting data in AuditDataModel
			auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
			auditDataModel.setCreatedBy(allocatedResource.getLoggedInUser());
			auditDataModel.setAuditPageId(allocatedResource.getWeid());
			auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_WORK_EFFORT);
			auditDataModel.setActorType(AppConstants.FULFILLMENT_MANAGER);
			auditDataModel.setCommentCategory(AppConstants.PROPOSED_RESOURCE_CANCEL_DATE_BY_FM);
			auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
			auditDataModel.setOldValue(AppConstants.PROPOSED);
			auditDataModel.setNewValue(AppConstants.PROPOSAL_PENDING);
			auditDataModel.setType(AppConstants.AUDIT);
			auditDataModel.setSourceId(1);
			auditManagmentService.addToAudit(auditDataModel);

		}
		return true;
	}

	/**
	 * 
	 * @param allocatedResourceModel
	 * @param projectId
	 * @return boolean
	 * @throws ParseException
	 */
	public boolean updateAllocatedResources(List<AllocatedResourceModel> allocatedResourceModel, Integer projectId)
	{
		boolean rpFlag = false;
		boolean brFlag = false;
		boolean flag = false;
		boolean isReject = false;
		
		AuditDataModel auditDataModel=new AuditDataModel();

		for (AllocatedResourceModel allocatedResource : allocatedResourceModel) {

			rpFlag = this.activityMasterDAO.updateResourcePosition(allocatedResource);
			// .getWeDetails().getCreatedBy()
			if (allocatedResource.getAllocatedStatus().equals(POSITION_STATUS_PROPOSALPENDING)) {
				isReject = true;
				// required resource name in notification
				this.changeManagementDAO.getWorkEffortDetailsByID(allocatedResource.getWeid());
				String signum = this.changeManagementDAO.getWorkEffortDetailsByID(allocatedResource.getWeid())
						.getSignum();

				Map<String, Object> wfDetails = this.changeManagementDAO.getProposedWorkEfforts(allocatedResource.getWeid());

				this.activityMasterDAO.updateWorkEffortByWeIDByProposedWorkEffort(wfDetails);
				rpFlag = this.activityMasterDAO.updateWorkEffortByWeIDReject(allocatedResource);
				this.activityMasterDAO.deleteBookedHoursByWeID(allocatedResource);

				allocatedResource.setSignum(signum);
				
				// Setting data in AuditDataModel
				auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
				auditDataModel.setCreatedBy(allocatedResource.getLoggedInUser());
				auditDataModel.setAuditPageId(allocatedResource.getRpID());
				auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_RESOURCE_POSITION);
				auditDataModel.setActorType(AppConstants.PROJECT_MANAGER);
				auditDataModel.setCommentCategory(AppConstants.RESOURCE_PROPOSAL_REJECTION_DATE_BY_PM);
				auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
				auditDataModel.setOldValue(AppConstants.PROPOSED);
				auditDataModel.setNewValue(AppConstants.PROPOSAL_PENDING);
				auditDataModel.setType(AppConstants.AUDIT);
				auditDataModel.setSourceId(1);
				auditManagmentService.addToAudit(auditDataModel);
				
			} else if (allocatedResource.getAllocatedStatus().equals(RESOURCE_ALLOCATED)) {
				allocatedResource.setPositionStatus(allocatedResource.getAllocatedStatus());
				WorkEffortModel wEffortModel = this.changeManagementDAO
						.getWorkEffortDetailsByID(allocatedResource.getWeid());

				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				cal1.setTime(wEffortModel.getEndDate());
				cal2.setTime(allocatedResource.getPositionEndDate());

				cal2.set(Calendar.HOUR_OF_DAY, 0);
				cal2.set(Calendar.MINUTE, 0);
				cal2.set(Calendar.SECOND, 0);
				cal2.set(Calendar.MILLISECOND, 0);

				cal1.getTime();
				cal2.getTime();

				if (cal1.before(cal2)) {
					Calendar newCal = cal1;
					newCal.add(Calendar.DATE, 1);

					WorkEffortModel newWorkEffort = new WorkEffortModel();
					newWorkEffort.setPositionStatus(POSITION_STATUS_PROPOSALPENDING);
					newWorkEffort.setResourcePositionID(allocatedResource.getRpID());
					newWorkEffort.setStartDate(newCal.getTime());
					newWorkEffort.setEndDate(allocatedResource.getPositionEndDate());
					newWorkEffort.setIsActive(true);
					double noOfHours = AppUtil.getNoOfWeekdayHoursBetweenDates(cal1.getTime(),
							allocatedResource.getPositionEndDate());

					float hoursPerFte = (float) ((noOfHours * wEffortModel.getFte_Percent()) / 100);
					newWorkEffort.setDuration((int) (noOfHours / AppUtil.WORKING_HOURS_IN_A_DAY)); // days
					newWorkEffort.setFte_Percent(wEffortModel.getFte_Percent());
					newWorkEffort.setHours((int) hoursPerFte);
					newWorkEffort.setWorkEffortStatus(wEffortModel.getWorkEffortStatus());
					newWorkEffort.setCreatedBy(allocatedResource.getLoggedInUser());
					newWorkEffort.setLastModifiedBy(allocatedResource.getLoggedInUser());
					newWorkEffort.setAllocatedBy(allocatedResource.getLoggedInUser());
					this.changeManagementDAO.insertInWorkEffort(newWorkEffort);
				}

				List<WorkEffortModel> workEffortRows = this.activityMasterDAO
						.getWeidWithMinSdateAll(allocatedResource.getRpID());

				Calendar cal3 = Calendar.getInstance();
				Calendar cal4 = Calendar.getInstance();
				cal3.setTime(workEffortRows.get(0).getStartDate());
				cal4.setTime(allocatedResource.getPositionStartDate());
				cal4.set(Calendar.HOUR_OF_DAY, 0);
				cal4.set(Calendar.MINUTE, 0);
				cal4.set(Calendar.SECOND, 0);
				cal4.set(Calendar.MILLISECOND, 0);

				if (cal4.before(cal3)) {
					ResourceRequestWorkEffortsModel resourceRequestWorkEffort = new ResourceRequestWorkEffortsModel();
					double noOfHours = AppUtil.getNoOfWeekdayHoursBetweenDates(workEffortRows.get(0).getStartDate(),
							allocatedResource.getPositionEndDate());

					float hoursPerFte = (float) ((noOfHours * wEffortModel.getFte_Percent()) / 100);
					resourceRequestWorkEffort.setDuration((int) (noOfHours / AppUtil.WORKING_HOURS_IN_A_DAY)); // days
					resourceRequestWorkEffort.setHours((int) hoursPerFte);
					resourceRequestWorkEffort.setStartDate(workEffortRows.get(0).getStartDate());
					resourceRequestWorkEffort.setResourcePositionID(allocatedResource.getRpID());
				} 

				Calendar cal6 = Calendar.getInstance();
				cal6.setTime(allocatedResource.getStartDate());
				cal6.set(Calendar.HOUR_OF_DAY, 0);
				cal6.set(Calendar.MINUTE, 0);
				cal6.set(Calendar.SECOND, 0);
				cal6.set(Calendar.MILLISECOND, 0);

				if (cal4.equals(cal3) && cal1.equals(cal2) && cal3.equals(cal6)) {
					int existingWfID = workEffortRows.get(0).getWorkEffortID();
					this.changeManagementDAO.updateWorkEffortStatusByWeId(false, String.valueOf(existingWfID));
				}

				this.activityMasterDAO.updateWorkEffortStatus(allocatedResource.getWeid());
				updatePoistionStatusByWfStatus(wEffortModel.getResourcePositionID());
				
				// Setting data in AuditDataModel
				auditDataModel.setMessage(AppConstants.DEMAND_EVENTS);
				auditDataModel.setCreatedBy(allocatedResource.getLoggedInUser());
				auditDataModel.setAuditPageId(allocatedResource.getRpID());
				auditDataModel.setAuditGroupCategory(AppConstants.DEMAND_EVENT_RESOURCE_POSITION);
				auditDataModel.setActorType(AppConstants.PROJECT_MANAGER);
				auditDataModel.setCommentCategory(AppConstants.RESOURCE_PROPOSAL_ACCEPTANCE_DATE_BY_PM);
				auditDataModel.setFieldName(AppConstants.POSITION_STATUS);
				auditDataModel.setOldValue(AppConstants.PROPOSED);
				auditDataModel.setNewValue(AppConstants.RESOURCE_ALLOCATED);
				auditDataModel.setType(AppConstants.AUDIT);
				auditDataModel.setSourceId(1);
				auditManagmentService.addToAudit(auditDataModel);
				
			}
			if (rpFlag && brFlag) {
				flag = true;
			}
			this.activityMasterDAO.deleteWorkEffortFromProposed(allocatedResource.getWeid());
		}
		demandForecastService.updateFMSummary(allocatedResourceModel, projectId);
		Map<String, Object> placeHolder;
		if (isReject) {
			placeHolder = emailService.enrichMailforCM(allocatedResourceModel,
					AppConstants.NOTIFICATION_TYPE_REJECT_PROPOSE);
			placeHolder.put(PAGE_LINK1, PM);
			placeHolder.put(PAGE_LINK2, FM);
			emailService.sendMail(AppConstants.NOTIFICATION_TYPE_REJECT_PROPOSE, placeHolder);
		} else {
			placeHolder = emailService.enrichMailforCM(allocatedResourceModel,
					AppConstants.NOTIFICATION_TYPE_ACCPET_PROPOSE);
			placeHolder.put(PAGE_LINK1, PM);
			placeHolder.put(PAGE_LINK2, FM);
			emailService.sendMail(AppConstants.NOTIFICATION_TYPE_ACCPET_PROPOSE, placeHolder);
		}
		return flag;
	}

	/**
	 * 
	 * @param rpID
	 */
	public void updatePoistionStatusByWfStatus(int rpID) {

		List<WorkEffortModel> workEffortRows = activityMasterDAO.getWorkEffortsDetailsByRpID(rpID);
		Map<String, Integer> statusMap = getStatusMap();
		HashMap<Integer, String> reversedStatusMap = new HashMap<>();

		for (Entry<String, Integer> tmpString : statusMap.entrySet()) {
			reversedStatusMap.put(statusMap.get(tmpString.getKey()), tmpString.getKey());
		}

		int tmpNum = 0;
		int i = 0;
		for (WorkEffortModel row : workEffortRows) {
			if (statusMap.keySet().contains(row.getPositionStatus().trim())) {
				if (i == 0) {
					tmpNum = statusMap.get(row.getPositionStatus());
					i++;
				} else if (statusMap.get(row.getPositionStatus()) <= tmpNum) {
					tmpNum = statusMap.get(row.getPositionStatus());
				}
			}
		}
		String maxPositionStatus = null;
		if (reversedStatusMap.get(tmpNum) != null) {
			maxPositionStatus = reversedStatusMap.get(tmpNum);
		}
		activityMasterDAO.updateRpPositionStatus(maxPositionStatus, rpID);
	}

	/**
	 * 
	 * @return Map<String, Integer>
	 */
	public Map<String, Integer> getStatusMap() {

		Map<String, Integer> statusMap = new HashMap<String, Integer>();
		statusMap.put("Closed", 1);
		statusMap.put(DEPLOYED, 2);
		statusMap.put(RESOURCE_ALLOCATED, 3);
		statusMap.put(POSITION_STATUS_PROPOSED, 4);
		statusMap.put(POSITION_STATUS_PROPOSALPENDING, 5);
		statusMap.put("Pre-Closure", 6);
		statusMap.put("Post-Closure", 7);
		return statusMap;
	}

	/**
	 * 
	 * @param projectID
	 * @param serviceAreaID
	 * @param domainID
	 * @param technologyID
	 * @param isWfAvailable
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getActivitySubActivityByProject_V2(int projectID, int serviceAreaID, int domainID,
			int technologyID, boolean isWfAvailable) {

		if (isWfAvailable) {
			return activityMasterDAO.getActivitySubActivityByProject_V2(projectID, serviceAreaID, domainID,
					technologyID);
		} else {
			return activityMasterDAO.getActivitySubActivityByProject_V3(projectID, serviceAreaID, domainID,
					technologyID);
		}

	}

	/**
	 * 
	 * @param projectID
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getActivitySubActivityByProjectId(Integer projectID) {

		return activityMasterDAO.getActivitySubActivityByProjectId(projectID);
	}

	/**
	 * 
	 * @param signum
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getTechnologyDetailsBySignum(String signum) {

		return activityMasterDAO.getTechnologyDetailsBySignum(signum);
	}

	/**
	 * 
	 * @param signum
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getEmployeeDetailsBySignum(String signum) {

		return activityMasterDAO.getEmployeeDetailsBySignum(signum);
	}

	/**
	 * 
	 * @param projectId
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getActivityByProjectId(int projectId) {

		return activityMasterDAO.getActivityByProjectId(projectId);
	}

	/**
	 * 
	 * @param projectID
	 * @param subActivityID
	 * @return List<TaskModel>
	 */
	public List<TaskModel> viewTaskDetails(int projectID, int subActivityID) {

		if (subActivityID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, SUB_ACTIVITY_ID));
		} else {
			return activityMasterDAO.viewTaskDetails(projectID, subActivityID);
		}
	}

	/**
	 * 
	 * @param spocRequest
	 * @return boolean
	 */
	public boolean addDomainSpoc(SpocModel spocRequest) {

		SpocModel domspoc = activityMasterDAO.getDomainSpoc(spocRequest);
		if (domspoc != null) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, String.format(ALREADY_EXISTS, "Spoc"));
		}

		return activityMasterDAO.addDomainSpoc(spocRequest);
	}

	/**
	 * 
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getAllDomainSpoc() {

		return activityMasterDAO.getAllDomainSpoc();
	}

	/**
	 * 
	 * @param taskModel
	 */
	private void syncProjectConfTable(TaskModel taskModel) {

		List<Integer> projectIDs = activityMasterDAO.getProjectIDForSubActivity(taskModel.getSubActivityID());
		for (int projectId : projectIDs) {
			boolean isScopeTaskExists = activityMasterDAO.isTaskExistsInProjConfTable(taskModel.getTaskID(),
					taskModel.getSubActivityID(), projectId);
			if (isScopeTaskExists) {
				activityMasterDAO.updateProjConfTable(projectId, taskModel);
			} else {
				List<ToolsModel> tools = toolsMasterService.getToolByTaskID(taskModel.getTaskID());
				if (CollectionUtils.isNotEmpty(tools)) {
					tools.forEach(tool -> {
						flowChartService.prepareTaskScopeModel(projectId, 0, taskModel.getSubActivityID(), taskModel,
								tool, 0);
					});
				} else {
					flowChartService.prepareTaskScopeModel(projectId, 0, taskModel.getSubActivityID(), taskModel,
							new ToolsModel(), 0);
				}

			}
		}
	}

	/**
	 * 
	 * @param projectID
	 * @param signum
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getMaxTasksValueByProject(int projectID, String signum) {
		Map<String, Object> data = new HashMap<>();
		try {
			data = activityMasterDAO.getMaxTasksValueByProject(projectID);

			if (MapUtils.isEmpty(data)) {
				activityMasterDAO.insertMaxTasksValueByProject(projectID, signum);
				data = activityMasterDAO.getMaxTasksValueByProject(projectID);
			}
		} catch (Exception e) {
			if (data == null) {
				data = new HashMap<>();
			}
			data.put("Error : ", "Error while getting data : "+e.getMessage());
		}
		return data;
	}

	/**
	 * 
	 * @param projectID
	 * @param signum
	 * @param maxManual
	 * @param maxAutomaic
	 * @return Boolean
	 */
	@Transactional("transactionManager")
	public Map<String, Object> updateMaxTasksValueByProject(int projectID, String signum, int maxManual,
			int maxAutomaic) {
		Map<String, Object> data = new HashMap<String, Object>();
		int maxManualStepLimit = configurations.getIntegerProperty(ConfigurationFilesConstants.MAX_MANUAL_TASK_ALLOWED);
		int maxAutoStepLimit = configurations.getIntegerProperty(ConfigurationFilesConstants.MAX_AUTOMATIC_TASK_ALLOWED);
		if(maxManual > maxManualStepLimit &&  maxAutomaic>maxAutoStepLimit && maxManualStepLimit!=0 && maxAutoStepLimit!=0) {
			if(maxManual > maxManualStepLimit) {
				String error = "Manual & Automatic task limit is exceeded- only " + maxManualStepLimit+ " Manual and " + maxAutoStepLimit+ " automatic tasks are allowed";
				LOG.error(error);
				data.put(AppConstants.MSG, error);
				data.put("updateFlag", false);
				return data;
			}
		}
		if(maxManualStepLimit > 0) {
			if(maxManual > maxManualStepLimit) {
				String error = "Manual task limit exceeded - Only " + maxManualStepLimit+  " Manual tasks are allowed";
				LOG.error(error);
				data.put(AppConstants.MSG, error);
				data.put("updateFlag", false);
				return data;
			}
		}
		if(maxAutoStepLimit > 0) {
			if(maxAutomaic > maxAutoStepLimit) {
				String error = "Automatic task limit exceeded - Only " + maxAutoStepLimit + " Automatic  tasks are allowed";
				LOG.error(error);
				data.put(AppConstants.MSG, error);
				data.put("updateFlag", false);
				return data;
			}
		}
		
		boolean isUpdated = activityMasterDAO.updateMaxTasksValueByProject(projectID, signum, maxManual, maxAutomaic);

		data.put("updateFlag", isUpdated);
		if (isUpdated) {
			data.put(AppConstants.MSG, "Successfull!!");
		} else {
			data.put(AppConstants.MSG, "UnSuccessfull!!");
		}

		return data;
	}

	/**
	 * 
	 * @param projectID
	 * @param response
	 * @return DownloadTemplateModel
	 * @throws IOException
	 * @throws SQLException
	 */
	public DownloadTemplateModel downloadNetworkElement(int projectID, HttpServletResponse response)
			throws IOException {

		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = "NetworkElement" + "-" + (new SimpleDateFormat(AppConstants.DATE_FORMAT).format(new Date()))
				+ AppConstants.XLSX;

		byte[] fData = FileUtil.generateXlsFile(activityMasterDAO.downloadNetworkElement(projectID));

		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);

		return downloadTemplateModel;
	}

	/**
	 * 
	 * @param projectID
	 * @param response
	 * @param status
	 * @param endDate
	 * @param startDate
	 * @return DownloadTemplateModel
	 * @throws IOException
	 * @throws SQLException
	 */
	public DownloadTemplateModel downloadWoViewFile(int projectID, HttpServletResponse response, String startDate,
			String endDate, String status) throws IOException, SQLException {

		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = "WorkOrdersViewExport" + "-" + (new SimpleDateFormat(AppConstants.DATE_FORMAT).format(new Date()))
				+ AppConstants.XLSX;

		List<Map<String, Object>> workOrderPlan = activityMasterDAO.downloadWoViewFile(projectID, startDate, endDate,
				status);
		byte[] fData = null;
		if (CollectionUtils.isNotEmpty(workOrderPlan)) {
			fData = FileUtil.generateXlsFile(workOrderPlan);
		}

		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);

		return downloadTemplateModel;
	}

	/**
	 * 
	 * @param projectID
	 * @param response
	 * @param status
	 * @param endDate
	 * @param startDate
	 * @return DownloadTemplateModel
	 * @throws IOException
	 * @throws SQLException
	 */
	public DownloadTemplateModel downloadDoPlanViewFile(int projectID, HttpServletResponse response, String startDate,
			String endDate, String status) throws IOException, SQLException {

		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = "DOPlanViewExport" + "-" + (new SimpleDateFormat(AppConstants.DATE_FORMAT).format(new Date()))
				+ AppConstants.XLSX;

		List<Map<String, Object>> workOrderPlan = activityMasterDAO.downloadDoPlanViewFile(projectID, startDate,
				endDate, status);
		byte[] fData = null;
		if (CollectionUtils.isNotEmpty(workOrderPlan)) {
			fData = FileUtil.generateXlsFile(workOrderPlan);
		}

		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);

		return downloadTemplateModel;
	}


	/**
	 * 
	 * @return List<CustomerModel>
	 */
	public List<CustomerModel> getCustomerDetails() {

		return activityMasterDAO.getCustomerDetails();
	}

	/**
	 * 
	 * @param customerModel
	 * @return 
	 */
	@Transactional("transactionManager")
	public Response<Void> saveCustomerDetails(CustomerModel customerModel) {
		Response<Void> response=new Response<Void>();
		try {
			if (activityMasterDAO.getCustMod(customerModel) == null) {

				activityMasterDAO.saveCustomerDetails(customerModel);
				int customerIdAndInstanceId = isfCustomIdInsert.generateCustomId(customerModel.getCustomerID());
				customerModel.setCustomerID(customerIdAndInstanceId);
				activityMasterDAO.saveCustomerCountryMappimg(customerModel);
				response.addFormMessage("Customer saved successfully.");
			} else {
				response.addFormError(String.format(ALREADY_EXISTS, "Customer"));
			}
		} catch (Exception e) {
			response.addFormError(e.getMessage());
		}
		return response;
	}

	/**
	 * 
	 * @return List<WorkInstructionModel>
	 */
	public List<WorkInstructionModel> getWorkInstruction() {

		return activityMasterDAO.getWorkInstruction();
	}

	/**
	 * 
	 * @param workInstructionModelString
	 * @param workinstructionfile
	 * @throws IOException
	 */
	public void saveWorkInstruction(String workInstructionModelString, MultipartFile workinstructionfile)
			throws IOException {

		WorkInstructionModel workInstructionModel = AppUtil.convertJsonToClassObject(workInstructionModelString,
				WorkInstructionModel.class);

		if (workInstructionModel != null && workinstructionfile != null) {
			workInstructionModel.setFileName(workinstructionfile.getOriginalFilename());
			workInstructionModel.setFileType(FilenameUtils.getExtension(workinstructionfile.getOriginalFilename()));
			workInstructionModel.setDataFile(workinstructionfile.getBytes());
		}
		activityMasterDAO.saveWorkInstruction(workInstructionModel);

	}

	/**
	 * 
	 * @param wIID
	 * @param signumID
	 * @param active
	 */
	@Transactional("transactionManager")
	public void deleteWorkInstruction(int wIID, String signumID, boolean active) {

		if (wIID == 0) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(VALUE_CANNOT_BE_0, "WorkInstructionID"));
		} else {
			activityMasterDAO.deleteWorkInstruction(wIID, signumID, active);
		}

	}

	/**
	 * 
	 * @param workInstructionModel
	 * @param workinstructionfile
	 * @throws IOException
	 */
	public void editWorkInstruction(String workInstructionModelString, MultipartFile workinstructionfile)
			throws IOException {

		WorkInstructionModel workInstructionModel = AppUtil.convertJsonToClassObject(workInstructionModelString,
				WorkInstructionModel.class);
		activityMasterDAO.editPrevWorkInstruction(workInstructionModel.getwIID(), workInstructionModel.getModifiedBy());
		workInstructionModel.setFileName(workinstructionfile.getOriginalFilename());
		workInstructionModel.setFileType(FilenameUtils.getExtension(workinstructionfile.getOriginalFilename()));
		workInstructionModel.setDataFile(workinstructionfile.getBytes());
		workInstructionModel.setRevNumber(workInstructionModel.getRevNumber() + 1);

		activityMasterDAO.saveWorkInstruction(workInstructionModel);
	}

	/**
	 * 
	 * @param domainID
	 * @param vendorID
	 * @param technologyID
	 * @return List<WorkInstructionModel>
	 */
	public Response<List<WorkInstructionModel>> getActiveWorkInstruction(Integer domainID, Integer vendorID,
			Integer technologyID) {
		Response<List<WorkInstructionModel>> response = new Response<List<WorkInstructionModel>>();
		try {
			response.setResponseData(activityMasterDAO.getActiveWorkInstruction(domainID, vendorID, technologyID));
		} catch (Exception e) {
			response.addFormError(e.getMessage());
		}
		return response;
	}

	/**
	 * 
	 * @param signum
	 * @return Map<String, String>
	 */
	public ResponseEntity<Response<AdhocBookingProjectModel>> getAdhocBookingProject(String signum) {
		Response<AdhocBookingProjectModel> response=new Response<>();
		try {
			response.setResponseData(activityMasterDAO.getAdhocBookingForProject(signum));
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<AdhocBookingProjectModel>>(response, org.springframework.http.HttpStatus.OK);
		}
		catch(Exception ex) {
			response.addFormError(ex.getMessage());
			return new ResponseEntity<Response<AdhocBookingProjectModel>>(response, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return  new ResponseEntity<Response<AdhocBookingProjectModel>>(response, org.springframework.http.HttpStatus.OK);
	}

	// v1/getAdhocBookingProject
	public Map<String, String> getAdhocBookingProjectV1(String signum) {

		Map<String, String> dataMap = new HashMap<String, String>();
		Map<String, Object> booking = activityMasterDAO.getAdhocBookingForProjectV1(signum);

		if (booking != null) {

			long diffInMillies = Math.abs(new Date().getTime() - ((Date) booking.get("StartDate")).getTime());
			dataMap.put(ID, String.valueOf(booking.get(ID)));
			dataMap.put(ACT_TYPE, (String) booking.get(ACT_TYPE));
			dataMap.put("actualDuration", String.valueOf(diffInMillies / 1000));
		}

		return dataMap;

	}

	/**
	 * 
	 * @param leavePlanModel
	 * @return boolean
	 */
	@Transactional("transactionManager")
	public boolean saveLeavePlan(LeavePlanModel leavePlanModel) {

		boolean isSuccess = true;
		try {
			Set<String> existingDateRange = new HashSet<>();
			List<LeavePlanModel> leavePlanModels = activityMasterDAO.getLeavePlanBySignum(leavePlanModel.getSignum());
			for (LeavePlanModel tmpLeavePlanModel : leavePlanModels) {
				if (tmpLeavePlanModel.getIsActive() != 0) {
					existingDateRange.addAll(AppUtil.getDaysBetweenDates(tmpLeavePlanModel.getStartDate(),
							tmpLeavePlanModel.getEndDate()));
				}
			}
			if (CollectionUtils.isNotEmpty(existingDateRange) && (existingDateRange
					.contains(DateTimeUtil.convertDateToString(leavePlanModel.getStartDate(),
							AppConstants.DEFAULT_DATE_FORMAT))
					|| existingDateRange.contains(DateTimeUtil.convertDateToString(leavePlanModel.getEndDate(),
							AppConstants.DEFAULT_DATE_FORMAT)))) {
				isSuccess = false;
			}
			if (isSuccess) {
				activityMasterDAO.saveLeavePlan(leavePlanModel);
				int leavePlanIdAndInstanceId = isfCustomIdInsert.generateCustomId(leavePlanModel.getLeavePlanID());
				leavePlanModel.setLeavePlanID(leavePlanIdAndInstanceId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

	/**
	 * 
	 * @param signum
	 * @return List<LeavePlanModel>
	 */
	public List<LeavePlanModel> getLeavePlanBySignum(String signum) {

		return activityMasterDAO.getLeavePlanBySignum(signum);
	}

	/**
	 * 
	 * @param signum
	 * @param leavePlanID
	 */
	@Transactional("transactionManager")
	public void deleteLeavePlan(String signum, int leavePlanID) {

		activityMasterDAO.deleteLeavePlan(signum, leavePlanID);
	}

	/**
	 * 
	 * @param signum
	 * @param week
	 * @return List<ShiftTimmingModel2>
	 */
	public List<ShiftTimmingModel2> getShiftTimmingBySignum(String signum, String startDate) {

		List<ShiftTimmingModel2> shiftTimmingDetails = activityMasterDAO.getShiftTimmingBySignum(signum, startDate);
		setValuesForShiftTimingDetails(shiftTimmingDetails);
		return shiftTimmingDetails;
	}

	/**
	 * 
	 * @param shiftTimmingDetails
	 */
	private static final String ENDYEARDATE="2099-12-31";
	private void setValuesForShiftTimingDetails(List<ShiftTimmingModel2> shiftTimmingDetails) {

		for (ShiftTimmingModel2 shift : shiftTimmingDetails) {

			DateTime shiftEndDate;
			if (shift.getEndDate() == null) {
				shiftEndDate = new DateTime(ENDYEARDATE);
				shift.setEndDate(shiftEndDate.toString(AppConstants.DATEFORMAT));
				shift.setShiftISTEndDate(shiftEndDate.toString(AppConstants.DATEFORMAT));

			} else {

				shiftEndDate = new DateTime(shift.getEndDate());
			}

			Weeks weeks = Weeks.weeksBetween(new DateTime(shift.getStartDate()), shiftEndDate);
			
			int startYear=Integer.parseInt(shift.getStartDate().substring(0, 4));
			int endYear=Integer.parseInt(shift.getEndDate().substring(0, 4));
			
			if(shift.getEndDate().equals(ENDYEARDATE)) {
				shift.setEndWeek("-");	
			}else {
				// if User pick Date differ by year
				if(endYear > startYear && !shift.getEndDate().equals(ENDYEARDATE)) {									
					int totalWeek=(Integer.parseInt(shift.getStartWeek()) + weeks.getWeeks());
					int newYearWeek=totalWeek-52;				
					shift.setEndWeek(endYear+AppConstants.FORWARD_SLASH+newYearWeek);
				}else {
					shift.setEndWeek(endYear+AppConstants.FORWARD_SLASH+(Integer.parseInt(shift.getStartWeek()) + weeks.getWeeks()));	
				}
				
				
			}
			shift.setStartWeek(startYear+AppConstants.FORWARD_SLASH+shift.getStartWeek());
			
			
		}
	}

	/**
	 * 
	 * @return List<TimeZoneModel>
	 */
	public List<TimeZoneModel> getTimeZones() {

		return activityMasterDAO.getTimeZones();
	}

	/**
	 * 
	 * @param shiftTimmingModel
	 * @return boolean
	 */
	public boolean saveshiftTimming(ShiftTimmingModel shiftTimmingModel) {

		boolean isSuccess = true;
		try {
			for (String signum : shiftTimmingModel.getSignum()) {

				List<ShiftTimmingModel2> shiftTimmingModel2List = activityMasterDAO
						.getShiftTimmingBySignumAndDate(signum, shiftTimmingModel.getStartDate());
				if (CollectionUtils.isNotEmpty(shiftTimmingModel2List)) {
					shiftTimmingModel.setEndDate(shiftTimmingModel2List.get(0).getEndDate());
					shiftTimmingModel.setShiftISTEndDate(shiftTimmingModel2List.get(0).getShiftISTEndDate());
					activityMasterDAO.deleteShiftTimmingBySignum(signum, shiftTimmingModel.getStartDate());
				} else {
					String shiftEndDate = null;
					updateNearestPreviousShift(signum, shiftTimmingModel.getStartDate(),
							shiftTimmingModel.getStartDate());
					String nextShiftStartDate = activityMasterDAO.getNearestNextShiftStartDate(signum,
							shiftTimmingModel.getStartDate());
					if (StringUtils.isNotBlank(nextShiftStartDate)) {
						shiftEndDate = new DateTime(nextShiftStartDate).minusDays(1).toString();
					}

					shiftTimmingModel.setEndDate(shiftEndDate);
					shiftTimmingModel.setShiftISTEndDate(shiftEndDate);
				}
				activityMasterDAO.saveshiftTimming(signum, shiftTimmingModel);

			}
		} catch (Exception e) {
			isSuccess = false;
			e.printStackTrace();
		}
		return isSuccess;
	}

	/**
	 * 
	 * @param projectScopeDetailID
	 * @return List<ScopeDetailsModel>
	 */
	public List<ScopeDetailsModel> getScopeDetailsById(String projectScopeDetailID) {

		return activityMasterDAO.getScopeDetailsById(projectScopeDetailID);
	}

	/**
	 * 
	 * @param signum
	 * @param startDate
	 * @param endDate
	 * @param timeZone
	 * @return List<ShiftTimmingModel2>
	 */
	@Transactional("transactionManager")
	public List<ShiftTimmingModel2> getShiftTimmingByDate(String signum, String startDate, String endDate,
			String timeZone) {

		List<ShiftTimmingModel2> shiftTimmingDetails = activityMasterDAO.getShiftTimmingByDate(signum, startDate,
				endDate, timeZone);
		setValuesForShiftTimingDetails(shiftTimmingDetails);
		return shiftTimmingDetails;
	}

	/**
	 * 
	 * @param signum
	 * @param shiftId
	 * @param week
	 * @return boolean
	 */
	@Transactional("transactionManager")
	public boolean deleteShiftTimmingbyID(String signum, int shiftId, String startDate) {

		updateNearestPreviousShift(signum, startDate,
				activityMasterDAO.getNearestNextShiftStartDate(signum, startDate));
		return activityMasterDAO.deleteShiftTimmingByID(signum, shiftId);
	}

	/**
	 * 
	 * @param customerID
	 * @param signum
	 */
	public void deleteCustomerDetails(int customerID, String signum) {

		activityMasterDAO.deleteCustomerDetails(customerID, signum);
	}

	/**
	 * 
	 * @param signum
	 * @param week
	 * @param endDate
	 */
	public void updateNearestPreviousShift(String signum, String currWeekStartDate, String nextWeekStartDate) {

		if (nextWeekStartDate != null) {
			nextWeekStartDate = new DateTime(nextWeekStartDate).minusDays(1).toString();
		}

		ShiftTimmingModel2 shiftTimingModel2 = activityMasterDAO.getNearestPreviousShift(signum, currWeekStartDate);
		if (shiftTimingModel2 != null) {
			activityMasterDAO.deleteShiftTimmingByID(signum, shiftTimingModel2.getShiftID());
			shiftTimingModel2.setEndDate(nextWeekStartDate);
			shiftTimingModel2.setShiftISTEndDate(nextWeekStartDate);
			ShiftTimmingModel shiftTimingModel = shiftTimingModel2.toShiftTimmingModel();
			activityMasterDAO.saveshiftTimming(signum, shiftTimingModel);
		}
	}

	/**
	 * 
	 * @param customerModel
	 */
	public void editCustomerName(CustomerModel customerModel) {

		CustomerModel custmod = activityMasterDAO.getCustMod(customerModel);
		if (custmod != null) {
			throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
					String.format(ALREADY_EXISTS, "Customer"));
		} else {
			activityMasterDAO.editCustomerName(customerModel);
		}

	}

	/**
	 * 
	 * @param wIID
	 * @return DownloadTemplateModel
	 */
	public DownloadTemplateModel downloadWorkInstructionFile(String wIID) {

		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();
		try {

			WorkInstructionModel workInstructionModel = activityMasterDAO.downloadWorkInstructionFile(wIID);
			if (workInstructionModel != null) {
				downloadTemplateModel.setpFileContent(workInstructionModel.getDataFile());
				downloadTemplateModel.setpFileName(workInstructionModel.getFileName());
			}
		} catch (Exception e) {
			LOG.info("Download Request: FAIL");
			e.printStackTrace();
		}
		return downloadTemplateModel;
	}

	/**
	 * 
	 * @return List<UserFeedbackModel>
	 */
	public List<UserFeedbackModel> getUserFeedBack() {

		return activityMasterDAO.getUserFeedBack();
	}

	/**
	 * 
	 * @return List<HashMap<String, Object>>
	 */
	public List<HashMap<String, Object>> getEssLeave() {
		return activityMasterDAO.getEssLeave();
	}

	/**
	 * 
	 * @param projectID
	 * @param deliverableId
	 * @param elementType
	 * @return List<String>
	 */
	public List<String> getNodeTypeByDeliverableId(int projectID, int deliverableId, String elementType) {

		return activityMasterDAO.getNodeTypeByDeliverableId(projectID, deliverableId, elementType);
	}

	public Response<List<CountryModel>> getCountries() {
		Response<List<CountryModel>> response = new Response<>();
		List<CountryModel> countriesList = activityMasterDAO.getCountries();
		if (CollectionUtils.isEmpty(countriesList)) {
			response.addFormMessage("There is no country");
			return response;
		} else {
			response.setResponseData(countriesList);
		}
		return response;
	}

	public ScopeDetailsModel getScopeByScopeId(int projectScopeID) {
		return this.activityMasterDAO.getScopeByScopeId(projectScopeID);
	}

	public String changeEmployeeStatus(ResourceStatusModel resourceStatusModel) {
		try {
			resourceStatusModel.setResourceStatusName(resourceStatusModel.getResourceStatusName().toString());
			resourceStatusModel.setSignum(resourceStatusModel.getSignum().toString());
			resourceStatusModel
			.setResignedOrTransferredDate(resourceStatusModel.getResignedOrTransferredDate().toString());
			resourceStatusModel.setReleaseDate(resourceStatusModel.getReleaseDate().toString());
			resourceStatusModel.setReason(resourceStatusModel.getReason().toString());
			resourceStatusModel.setCreatedBy(resourceStatusModel.getCreatedBy().toString());

			this.activityMasterDAO.changeEmployeeStatus(resourceStatusModel);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Successfull";
	}

	public Response<List<String>> getNodes(int projectID, String elementType) {
		Response<List<String>> response = new Response<>();
		try {
			int countryCustomerGroupID = appService.getCountryCustomerGroupIDByProjectID(projectID);
			response.setResponseData(activityMasterDAO.getNodes(countryCustomerGroupID, elementType));
		} catch (Exception e) {
			response.addFormError(e.getMessage());
		}
		return response;
	}

	public List<ScopeDetailsModel> getScopeDetailsByScopeId(String projectScopeId) {
		return activityMasterDAO.getScopeDetailsByScopeId(projectScopeId);
	}

	@Transactional("transactionManager")
	public Response<Void> saveGlobalUrl(GlobalUrlModel globalUrlModel, String signumID, String role) {
		Response<Void> apiResponse = new Response<>();
		try {
			if (StringUtils.isEmpty(signumID) || StringUtils.isEmpty(role)) {
				throw new ApplicationException(200, ApplicationMessages.INVALID_HEADER);
			} 
			globalUrlModel.setSignum(signumID);
			globalUrlModel.setRoleID(accessManagementDAO.getRoleIDByRoleName(role));

			validationUtilityService.validateLength(globalUrlModel.getUrlLink(), 500, AppConstants.URL_LINK);
			validationUtilityService.validateLength(globalUrlModel.getUrlName(), 50, AppConstants.URL_NAME);

			if (StringUtils.isEmpty(globalUrlModel.getUrlName())
					&& StringUtils.isEmpty(globalUrlModel.getUrlLink())) {			
				throw new ApplicationException(200, ApplicationMessages.EMPTY_ENTITY);
			}
			boolean isUrlExistInGlobalUrl = isUrlExistInGlobalUrl(globalUrlModel.getUrlLink());
			boolean isNameExistInGlobal = isNameExistInGlobalUrl(globalUrlModel.getUrlName());
			if (isNameExistInGlobal) {
				apiResponse.addFormError(ApplicationMessages.ALREADY_PRESENT_GLOBAL_NAME);
			} else if (isUrlExistInGlobalUrl) {
				apiResponse.addFormError(ApplicationMessages.ALREADY_PRESENT_GLOBAL_ENTITY);
			}
			if ((AppUtil.validateUrl(globalUrlModel.getUrlLink())
					|| AppUtil.validateSharedFolderUrl(globalUrlModel.getUrlLink())) && !isUrlExistInGlobalUrl
					&& !isNameExistInGlobal) {

				activityMasterDAO.saveGlobalUrl(globalUrlModel);
				apiResponse.addFormMessage(AppConstants.SUCCESS);


			} else if (!isUrlExistInGlobalUrl && !isNameExistInGlobal) {
				apiResponse.addFormError(globalUrlModel.getUrlLink() + " " + ApplicationMessages.INVALID_INPUT_URL);
			}
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			apiResponse.addFormError(e.getMessage());
			return apiResponse;

		}
		catch (Exception e) {
			apiResponse.addFormError(e.getMessage());
		}
		return apiResponse;
	}

	@Transactional("transactionManager")
	public Response<Void> updateGlobalUrl(GlobalUrlModel globalUrlModel, String signumID, String role) {
		Response<Void> apiResponse = new Response<>();
		try {
			boolean isUrlExistInGlobalUrl = false;
			boolean isNameExistedInGlobalUrl = false;
			if (StringUtils.isEmpty(signumID) || StringUtils.isEmpty(role)) {
				throw new ApplicationException(200, ApplicationMessages.INVALID_HEADER);
			} 
			globalUrlModel.setSignum(signumID);
			globalUrlModel.setRoleID(accessManagementDAO.getRoleIDByRoleName(role));

			validationUtilityService.validateLength(globalUrlModel.getUrlLink(), 500, AppConstants.URL_LINK);
			validationUtilityService.validateLength(globalUrlModel.getUrlName(), 50, AppConstants.URL_NAME);

			switch (globalUrlModel.getActionType()) {
			case AppConstants.UPDATE:

				isUrlExistInGlobalUrl = isUrlExistInGlobalUrl(globalUrlModel.getUrlLink());
				isNameExistedInGlobalUrl = isNameExistInGlobalUrl(globalUrlModel.getUrlName());
				if (isNameExistedInGlobalUrl && isUrlExistInGlobalUrl) {
					apiResponse.addFormError(ApplicationMessages.URLNAME_URLLINK_BOTH_PRESENT);
					return apiResponse;
				}					
				if ((AppUtil.validateUrl(globalUrlModel.getUrlLink()) || AppUtil.validateSharedFolderUrl(globalUrlModel.getUrlLink()))) {						
					activityMasterDAO.updateGlobalUrl(globalUrlModel);
					apiResponse.addFormMessage(AppConstants.SUCCESS);

				} else if (!isUrlExistInGlobalUrl && !isNameExistedInGlobalUrl) {
					apiResponse.addFormError(globalUrlModel.getUrlLink() + ApplicationMessages.INVALID_INPUT_URL);
				}
				break;
			case AppConstants.DELETE:
				activityMasterDAO.updateGlobalUrl(globalUrlModel);
				apiResponse.addFormMessage(AppConstants.SUCCESS);
				break;
			default:
				apiResponse.addFormError("Not an Action Type");				
			}
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			apiResponse.addFormError(e.getMessage());
			return apiResponse;

		}
		catch (Exception e) {
			LOG.error(AppConstants.EXCEPTION,e.getMessage());
			apiResponse.addFormError(e.getMessage());
		}
		return apiResponse;
	}

	public List<GlobalUrlModel> getAllGlobalUrl() {
		return activityMasterDAO.getAllGlobalUrl();
	}

	public Response<Void> saveLocalUrl(LocalUrlModel localUrlModel, String signumID, String role) {
		Response<Void> apiResponse = new Response<>();
		try {
			localUrlModel.setSignum(signumID);
			localUrlModel.setRoleID(accessManagementDAO.getRoleIDByRoleName(role));

			validationUtilityService.validateLength(localUrlModel.getLocalUrlLink(), 500, AppConstants.URL_LINK);
			validationUtilityService.validateLength(localUrlModel.getLocalUrlName(), 50, AppConstants.URL_NAME);

			if (StringUtils.isEmpty(localUrlModel.getLocalUrlName())
					&& StringUtils.isEmpty(localUrlModel.getLocalUrlLink())) {				
				throw new ApplicationException(200, ApplicationMessages.EMPTY_ENTITY);
			} 

			boolean isDataExisted = isGlobalUrlExist(localUrlModel.getLocalUrlLink(), localUrlModel.getLocalUrlName());

			boolean isLocalDataExisted = isLocalUrlExist(localUrlModel);
			if (isDataExisted) {
				apiResponse.addFormError(ApplicationMessages.ALREADY_PRESENT_ENTITY);
			} else if (isLocalDataExisted) {
				apiResponse.addFormError(ApplicationMessages.ALREADY_PRESENT_LOCAL_ENTITY);
			} else if (isNameExistInLocalUrl(localUrlModel)) {
				apiResponse.addFormError(ApplicationMessages.ALREADY_PRESENT_LOCAL_NAME);
			} else {
				if (AppUtil.validateTransactionalUrl(localUrlModel.getLocalUrlLink())) {


					activityMasterDAO.saveLocalUrl(localUrlModel);
					apiResponse.addFormMessage(AppConstants.SUCCESS);


				} else {
					apiResponse.addFormError(
							localUrlModel.getLocalUrlLink() + " " + ApplicationMessages.INVALID_INPUT_URL);
				}
			}
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			apiResponse.addFormError(e.getMessage());
			return apiResponse;

		}
		catch (Exception e) {
			apiResponse.addFormError(e.getMessage());
		}

		return apiResponse;
	}

	private boolean isLocalUrlExist(LocalUrlModel localUrlModel) {
		boolean result = false;
		List<LocalUrlModel> listOfLocalUrl = getAllLocalUrl(localUrlModel.getProjectID()).getResponseData();
		for (int i = 0; i < listOfLocalUrl.size(); i++) {
			if (listOfLocalUrl.get(i).getLocalUrlLink().equalsIgnoreCase(localUrlModel.getLocalUrlLink())) {
				result = true;
				break;
			}
		}
		return result;
	}

	private boolean isNameExistInLocalUrl(LocalUrlModel localUrlModel) {
		boolean result = false;
		List<LocalUrlModel> listOfLocalUrl = getAllLocalUrl(localUrlModel.getProjectID()).getResponseData();
		for (int i = 0; i < listOfLocalUrl.size(); i++) {
			if (listOfLocalUrl.get(i).getLocalUrlName().equalsIgnoreCase(localUrlModel.getLocalUrlName())) {
				result = true;
				break;
			}
		}
		return result;
	}

	private boolean isGlobalUrlExist(String globalUrl, String globalUrlName) {
		boolean result = false;
		List<GlobalUrlModel> listOfGlobalUrl = getAllGlobalUrl();
		for (int i = 0; i < listOfGlobalUrl.size(); i++) {
			if (listOfGlobalUrl.get(i).getUrlLink().equalsIgnoreCase(globalUrl)
					|| listOfGlobalUrl.get(i).getUrlName().equalsIgnoreCase(globalUrlName)) {
				result = true;
				;
				break;
			}
		}
		return result;
	}

	private boolean isUrlExistInGlobalUrl(String globalUrlLink) {
		boolean result = false;
		List<GlobalUrlModel> listOfGlobalUrl = getAllGlobalUrl();
		for (int i = 0; i < listOfGlobalUrl.size(); i++) {
			if (listOfGlobalUrl.get(i).getUrlLink().equalsIgnoreCase(globalUrlLink)) {
				result = true;
				break;
			}
		}
		return result;
	}

	private boolean isNameExistInGlobalUrl(String globalUrlName) {
		boolean result = false;
		List<GlobalUrlModel> listOfGlobalUrl = getAllGlobalUrl();
		for (int i = 0; i < listOfGlobalUrl.size(); i++) {
			if (listOfGlobalUrl.get(i).getUrlName().equalsIgnoreCase(globalUrlName)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public Response<Void> updateLocalUrl(LocalUrlModel localUrlModel, String signumID, String role) {
		Response<Void> apiResponse = new Response<>();
		try {
			boolean isLocalDataExisted = false;
			boolean isNameExist = false;
			if (StringUtils.isEmpty(signumID) || StringUtils.isEmpty(role)) {
				apiResponse.addFormError(ApplicationMessages.INVALID_HEADER);
			} else {
				localUrlModel.setSignum(signumID);
				localUrlModel.setRoleID(accessManagementDAO.getRoleIDByRoleName(role));
			}
			if(StringUtils.isNotBlank(localUrlModel.getLocalUrlLink()) && StringUtils.isNotBlank(localUrlModel.getLocalUrlName())) {
				validationUtilityService.validateLength(localUrlModel.getLocalUrlLink(), 500, "UrlLink");
				validationUtilityService.validateLength(localUrlModel.getLocalUrlName(), 50, "UrlName");
			}
			if (localUrlModel.getLocalUrlId() != 0 && StringUtils.isNotEmpty(localUrlModel.getLocalUrlName())
					&& StringUtils.isNotEmpty(localUrlModel.getLocalUrlLink())
					&& (localUrlModel.getActionType().equalsIgnoreCase(AppConstants.TOGGLE)
							|| localUrlModel.getActionType().equalsIgnoreCase(AppConstants.UPDATE))) {
				boolean isDataExisted = isGlobalUrlExist(localUrlModel.getLocalUrlLink(), localUrlModel.getLocalUrlName());
				if (localUrlModel.getActionType().equalsIgnoreCase(AppConstants.UPDATE)) {
					isLocalDataExisted = isLocalUrlExist(localUrlModel);
					isNameExist = isNameExistInLocalUrl(localUrlModel);
				}
				if (isDataExisted) {
					apiResponse.addFormError(ApplicationMessages.ALREADY_PRESENT_ENTITY);
					return apiResponse;
				} else if (isNameExist && isLocalDataExisted) {
					apiResponse.addFormError(ApplicationMessages.ALREADY_PRESENT_LOCAL_NAME);
					return apiResponse;
				}

				if (AppUtil.validateTransactionalUrl(localUrlModel.getLocalUrlLink()) && !isDataExisted
						&& ((!isLocalDataExisted && isNameExist) || (isLocalDataExisted && !isNameExist)
								|| (!isLocalDataExisted && !isNameExist))) {

					activityMasterDAO.updateLocalUrl(localUrlModel);
					apiResponse.addFormMessage(AppConstants.SUCCESS);

					return apiResponse;
				} else {
					apiResponse.addFormError(ApplicationMessages.INVALID_INPUT_URL);
					return apiResponse;
				}
			} else {
				apiResponse.addFormError(ApplicationMessages.INVALID_ENTITY_UPDATE);
				return apiResponse;
			}
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			apiResponse.addFormError(ApplicationMessages.URL_AND_NAME_LENGTH_ERROR);
			return apiResponse;

		}
		catch (Exception e) {
			apiResponse.addFormError(e.getMessage());
		}
		return apiResponse;
	}

	public Response<List<LocalUrlModel>> getAllLocalUrl(int projectID) {
		Response<List<LocalUrlModel>> response = new Response<>();
		try {
			List<LocalUrlModel> data = activityMasterDAO.getAllLocalUrl(projectID);
			response.setResponseData(data);
		} catch (Exception e) {
			response.addFormError(e.getMessage());
		}
		return response;
	}

	public List<LocalUrlModel> getAllActiveLocalUrl(int projectID) {
		return activityMasterDAO.getAllActiveLocalUrl(projectID);
	}

	public Map<String, Object> isValidTransactionalData(String url, int projectID) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isNotBlank(url)) {
			String parentUrl = getParentUrl(url);
			boolean isDataExistedInGlobalTable = false;
			boolean isLocalDataExisted = false;
			if (StringUtils.isNotBlank(parentUrl)) {
				map = AppUtil.validateTransactionalUrlData(parentUrl);
				if ((boolean) map.get(AppConstants.RESULT)) {
					isLocalDataExisted = getAllActiveLocalUrl(projectID).stream()
							.anyMatch(item -> getParentUrl(item.getLocalUrlLink()).equalsIgnoreCase(parentUrl));

					if (!isLocalDataExisted) {
						isDataExistedInGlobalTable = getAllGlobalUrl().stream()
								.anyMatch(item -> getParentUrl(item.getUrlLink()).equalsIgnoreCase(parentUrl));
					}
					if (isLocalDataExisted) {
						map.put(AppConstants.RESULT, Boolean.TRUE);
						map.put(AppConstants.MESSAGE, url + " is already existed in local url.");
					} else if (isDataExistedInGlobalTable) {
						map.put(AppConstants.RESULT, Boolean.TRUE);
						map.put(AppConstants.MESSAGE, url + " is already existed in global url.");
					} else {
						map.put(AppConstants.RESULT, Boolean.FALSE);
						map.put(AppConstants.MESSAGE, url + " is not configured with project url or global url");
					}

				}

			} else {
				map.put(AppConstants.RESULT, Boolean.FALSE);
				map.put(AppConstants.MESSAGE, url + " should start with https or ftp or shared url");
			}
		} else {
			map.put(AppConstants.RESULT, Boolean.FALSE);
			map.put(AppConstants.MESSAGE, url + " should start with https or ftp or or shared url");
		}

		return map;
	}

	private String getParentUrl(String url) {

		if (url.startsWith("https") || url.startsWith("ftp")) {
			return AppUtil.substringNthOccurrence(url, '/', 3);
		} else if (url.startsWith("\\")) {
			return AppUtil.substringNthOccurrence(url, '\\', 3);
		}
		return "";
	}

	public boolean isSignumExist(String signum) {
		return activityMasterDAO.isSignumExist(signum);
	}

	public Response<Void> saveInstantFeedback(InstantFeedbackModel instantFeedbackModel) {
		Response<Void> result = new Response<>();
		try {
			List<InstantFeedbackModel> listofInstantFeedback = activityMasterDAO.getInstantFeedback();
			boolean isExist = listofInstantFeedback.stream().anyMatch(action -> StringUtils
					.equalsIgnoreCase(action.getFeedbackText(), instantFeedbackModel.getFeedbackText())
					&& StringUtils.equalsIgnoreCase(action.getFeedbackType(), instantFeedbackModel.getFeedbackType()));
			if (isExist) {
				throw new ApplicationException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.value(),
						" \'" + instantFeedbackModel.getFeedbackText() + "\' already exist for type \'"
								+ instantFeedbackModel.getFeedbackType() + "\'");
			}
			activityMasterDAO.saveInstantFeedback(instantFeedbackModel);
			result.addFormMessage(AppConstants.SUCCESS);
		} catch (Exception e) {
			result.addFormError(e.getMessage());
			LOG.info(e.getMessage());
		}
		return result;
	}

	public Response<Void> updateInstantFeedback(InstantFeedbackModel instantFeedbackModel) {
		Response<Void> result = new Response<>();
		try {
			List<InstantFeedbackModel> listofInstantFeedback = activityMasterDAO
					.getInstantFeedbackUpdate(instantFeedbackModel.getInstantFeedbackID());
			boolean isExist = listofInstantFeedback.stream().anyMatch(action -> StringUtils
					.equalsIgnoreCase(action.getFeedbackText(), instantFeedbackModel.getFeedbackText())
					&& StringUtils.equalsIgnoreCase(action.getFeedbackType(),instantFeedbackModel.getFeedbackType()));
			if (isExist) {
				throw new ApplicationException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.value(),
						" \'" + instantFeedbackModel.getFeedbackText() + "\' already exist for type \'"
								+ instantFeedbackModel.getFeedbackType() + "\'");
			}
			activityMasterDAO.updateInstantFeedback(instantFeedbackModel);
			result.addFormMessage(AppConstants.SUCCESS);
		} catch (Exception e) {
			result.addFormError(e.getMessage());
			LOG.info(e.getMessage());
		}
		return result;
	}

	public Response<Void> saveWFFeedbackComments(WorkFlowFeedbackModel workFlowFeedbackModel) {
		Response<Void> response = new Response<>();
		if ((workFlowFeedbackModel == null
				|| StringUtils.isEmpty(workFlowFeedbackModel.getWorkFlowFeedbackActivityModel().getFeedbackComment()))
				&& StringUtils.isEmpty(workFlowFeedbackModel.getStepID())) {
			response.addFormError("Please enter some comments!!");
		} else if (StringUtils.isEmpty(workFlowFeedbackModel.getWorkFlowFeedbackActivityModel().getFeedbackComment())
				&& StringUtils.equalsIgnoreCase("Others", workFlowFeedbackModel.getInstantFeedback()))

		{
			response.addFormError("Comment is mandatory, when InstantFeedback = Others");
		}

		else {
			try {
				if(StringUtils.isEmpty(workFlowFeedbackModel.getFeedbackType())) {
					if (StringUtils.isNotEmpty(workFlowFeedbackModel.getStepID())
							&& StringUtils.isNotEmpty(workFlowFeedbackModel.getStepName())) {
						if (StringUtils.isEmpty(workFlowFeedbackModel.getFeedbackOn())
								&& StringUtils.isEmpty(workFlowFeedbackModel.getFeedbackType())) {				
							workFlowFeedbackModel.setFeedbackOn(AppConstants.FEEDBACK_ON_STEP);
							workFlowFeedbackModel.setFeedbackType(AppConstants.FEEDBACK_TYPE_STEP);
						}
					} else {
						workFlowFeedbackModel.setFeedbackOn(AppConstants.FEEDBACK_ON_WF);
						workFlowFeedbackModel.setFeedbackType(AppConstants.FEEDBACK_TYPE_WF);
					}
				}
				activityMasterDAO.addFeedbackDetail(workFlowFeedbackModel);

				int feedbackDetailIdAndInstanceId = isfCustomIdInsert
						.generateCustomId(workFlowFeedbackModel.getFeedbackDetailID());

				WorkFlowFeedbackActivityModel workFlowFeedbackActivityModel = workFlowFeedbackModel
						.getWorkFlowFeedbackActivityModel();

				workFlowFeedbackActivityModel.setFeedbackDetailID(feedbackDetailIdAndInstanceId);
				if (workFlowFeedbackModel.getFeedbackType()
						.equalsIgnoreCase(AppConstants.FEEDBACK_TYPE_STEP_SAD_COUNT)) {
					workFlowFeedbackActivityModel.setFeedbackStatus("NULL");
				} else {
					workFlowFeedbackActivityModel.setFeedbackStatus("NEW");
				}
				workFlowFeedbackActivityModel.setSignum(workFlowFeedbackModel.getSignum());
				workFlowFeedbackActivityModel.setUserRole(workFlowFeedbackModel.getUserRole());
				activityMasterDAO.addFeedbackActivity(workFlowFeedbackActivityModel);

				response.addFormMessage(AppConstants.SUCCESS);

			} catch (Exception e) {

				response.addFormError(ApplicationMessages.FEEDBACK_SAVE_ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		return response;
	}

	public List<WorkFlowFeedbackActivityModel> getFeedbackHistory(WorkFlowFeedbackModel workFlowFeedbackModel) {

		List<WorkFlowFeedbackActivityModel> workFlowFeedbackActivityModel = activityMasterDAO
				.getFeedbackHistory(workFlowFeedbackModel);
		if(!CollectionUtils.sizeIsEmpty(workFlowFeedbackActivityModel)) {
			String feedbackComment;
			String instantFeedbackText;
			for(WorkFlowFeedbackActivityModel wfFeedbackActivityModel : workFlowFeedbackActivityModel) {
				feedbackComment	=	wfFeedbackActivityModel.getFeedbackComment();
				instantFeedbackText	=	wfFeedbackActivityModel.getInstantFeedbackText();
				if (StringUtils.isNotEmpty(instantFeedbackText) && StringUtils.isNotEmpty(feedbackComment)) {
					wfFeedbackActivityModel
					.setFeedbackComment(instantFeedbackText.trim() + " (" + feedbackComment.trim() + ")");
				} else if (StringUtils.isNotEmpty(instantFeedbackText) && StringUtils.isEmpty(feedbackComment)) {
					wfFeedbackActivityModel.setFeedbackComment(instantFeedbackText.trim());
				}
			}
		}

		return workFlowFeedbackActivityModel;
	}

	public Response<Void> deleteFeedbackComment( String signum, WorkFlowFeedbackModel workFlowFeedbackModel) {

		Response<Void> response = new Response<Void>();
		try {
			WorkFlowFeedbackActivityModel workFlowFeedbackActivityModel = activityMasterDAO
					.getStatusandSignum(workFlowFeedbackModel);
			if (workFlowFeedbackActivityModel != null && workFlowFeedbackModel.getFeedbackDetailID() != 0
					&& StringUtils.equalsIgnoreCase(workFlowFeedbackActivityModel.getSignum(), signum)
					&& StringUtils.equalsIgnoreCase(workFlowFeedbackActivityModel.getFeedbackStatus(), "NEW")) {
				activityMasterDAO.deleteFeedbackDetailComment(workFlowFeedbackModel);
				activityMasterDAO.deleteFeedbackActivityComment(workFlowFeedbackModel);
				response.addFormMessage("Feedback Deleted!");

			} else {
				response.addFormMessage("Delete Failed");
			}

		} catch (Exception e) {
			response.addFormError(e.getMessage());
		}
		return response;
	}

	public List<InstantFeedbackModel> getInstantFeedback() {
		return activityMasterDAO.getInstantFeedback();
	}

	public List<InstantFeedbackModel> getInstantFeedbackForDropDown() {
		return activityMasterDAO.getInstantFeedbackForDropDown();
	}

	public List<ProjectFilterModel> getProjectList(String signum,String role, String marketArea) {
		return activityMasterDAO.getProjectList(signum,role,marketArea);
	}

	public List<WorkFlowFilterModel> getWorkFlowList(String signum, ProjectFilterModel projectFilterModel,
			String role) {
		return activityMasterDAO.getWorkFlowList(signum,projectFilterModel.getProjectID(),role);

	}

	public Map<String, Object> getAllFeedback(String signum, FeedbackNEPMModel feedbackNePmModel,
			DataTableRequest dataTableRequest, String role) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.DATEFORMAT);
		Date endDate = dateFormat.parse(feedbackNePmModel.getEndDate());
		Calendar c = Calendar.getInstance(); 
		c.setTime(endDate); 
		c.add(Calendar.DATE, 1);
		endDate = c.getTime();
		feedbackNePmModel.setEndDate(DateTimeUtil.convertDateToString(endDate,AppConstants.DATEFORMAT));
		Map<String, Object> map = new HashMap<>();
		List<FeedbackNEPMModel> listOfFeedbackData = activityMasterDAO.getAllFeedback(signum, feedbackNePmModel,
				dataTableRequest, role);
		map.put("data", listOfFeedbackData);
		if (!listOfFeedbackData.isEmpty()) {
			map.put(RECORDS_TOTAL, listOfFeedbackData.get(0).getTotalCount());
			map.put(RECORDS_FILTERED, listOfFeedbackData.get(0).getTotalCount());
		} else {
			map.put(RECORDS_TOTAL, 0);
			map.put(RECORDS_FILTERED, 0);
		}

		return map;

	}

	@Transactional("transactionManager")
	public Response<Void> saveStepSadCount(WorkFlowFeedbackModel workFlowFeedbackModel, String role) {

		Response<Void> response = new Response<Void>();
		try {
			if(StringUtils.equalsIgnoreCase("Default User", role)) {
				role=NETWORK_ENGINEER;
			}
			WorkFlowFeedbackActivityModel WfFeedbackActivityModel = activityMasterDAO.getFeedbackActivityIDBySignum(
					workFlowFeedbackModel.getProjectID(), workFlowFeedbackModel.getSignum(),
					workFlowFeedbackModel.getStepID(), AppConstants.FEEDBACK_ON_STEP,
					AppConstants.FEEDBACK_TYPE_STEP_SAD_COUNT, workFlowFeedbackModel.getWorkOrderID());

			WorkFlowFeedbackModel workFlowFeedbackModelPrev = activityMasterDAO.getFeedbackDetail(
					workFlowFeedbackModel.getProjectID(), workFlowFeedbackModel.getStepID(),
					AppConstants.FEEDBACK_ON_STEP, AppConstants.FEEDBACK_TYPE_STEP_SAD_COUNT,
					workFlowFeedbackModel.getSignum());

			if (WfFeedbackActivityModel != null
					&& WfFeedbackActivityModel.getSignum().equalsIgnoreCase(workFlowFeedbackModel.getSignum())) {
				this.activityMasterDAO.updateSadCount(WfFeedbackActivityModel.getFeedbackActivityID(),
						workFlowFeedbackModel.getWorkFlowFeedbackActivityModel().getSadCount(),role);
				response.addFormMessage(AppConstants.SUCCESS);
			}else if(workFlowFeedbackModelPrev!= null) {
				WorkFlowFeedbackActivityModel workFlowFeedbackActivityModel = workFlowFeedbackModel
						.getWorkFlowFeedbackActivityModel();
				workFlowFeedbackActivityModel.setFeedbackStatus("NULL");
				workFlowFeedbackActivityModel.setFeedbackDetailID(workFlowFeedbackModelPrev.getFeedbackDetailID());
				workFlowFeedbackActivityModel.setSignum(workFlowFeedbackModel.getSignum());
				workFlowFeedbackActivityModel.setUserRole(role);
				activityMasterDAO.addFeedbackActivity(workFlowFeedbackActivityModel);

				response.addFormMessage(AppConstants.SUCCESS);
			} else {
				workFlowFeedbackModel.setFeedbackOn(AppConstants.FEEDBACK_ON_STEP);
				workFlowFeedbackModel.setFeedbackType(AppConstants.FEEDBACK_TYPE_STEP_SAD_COUNT);

				workFlowFeedbackModel.setUserRole(role);
				response = this.saveWFFeedbackComments(workFlowFeedbackModel);
			}
		} catch (Exception e) {
			response.addFormError(e.getMessage());
		}
		return response;
	}

	@Transactional("transactionManager")
	public Response<Void> updateFeedbackStatus(String role, FeedbackStatusUpdateModel feedbackStatusUpdateModel,
			String signum) {
		Response<Void> response = new Response<>();
		try {
			if (feedbackStatusUpdateModel.getFeedbackDetailID() == 0
					|| StringUtils.isEmpty(feedbackStatusUpdateModel.getFeedbackStatusNew())) {
				response.addFormError("Invalid status, please enter valid status !!");
			} 

			WorkFlowFeedbackActivityModel WfFeedbackActivityModel = activityMasterDAO.getLatestFeedbackStatus(feedbackStatusUpdateModel.getFeedbackDetailID());
			Boolean checkStatus = StringUtils.equalsIgnoreCase(WfFeedbackActivityModel.getFeedbackStatus(), feedbackStatusUpdateModel.getFeedbackStatus());
			if(!checkStatus) {
				response.addFormError("The Action "+WfFeedbackActivityModel.getFeedbackStatus()+" is Already taken by "+ WfFeedbackActivityModel.getUserRole()+".Please Refresh the page.");

			}

			else {
				if(feedbackStatusUpdateModel.getFeedbackStatusNew().equalsIgnoreCase(AppConstants.ACCEPTED)) {
					String previousStatus = feedbackStatusUpdateModel.getFeedbackStatusNew();
					feedbackStatusUpdateModel.setFeedbackStatus(previousStatus);
					feedbackStatusUpdateModel.setFeedbackStatusNew("Closed");
				}
				if(StringUtils.equalsIgnoreCase(NETWORK_ENGINEER, role) || StringUtils.equalsIgnoreCase(DELIVERY_RESPONSIBLE, role) ) {
					String pmComments=	activityMasterDAO.getReplyONComment(feedbackStatusUpdateModel.getFeedbackActivityID());
					feedbackStatusUpdateModel.setPmComments(pmComments);

				}
				else if(StringUtils.equalsIgnoreCase(PROJECT_MANAGER, role) || StringUtils.equalsIgnoreCase(OPERATIONAL_MANAGER, role) ) {
					String neComments=	activityMasterDAO.getNEComments(feedbackStatusUpdateModel.getFeedbackActivityID());
					feedbackStatusUpdateModel.setNeComments(neComments);
				}

				this.activityMasterDAO.addFeedbackStatusForPM(role,feedbackStatusUpdateModel,signum);
				WorkFlowFeedbackModel workFlowFeedbackModel = this.activityMasterDAO.getFeedbackByDetailID(
						feedbackStatusUpdateModel.getFeedbackDetailID(),
						feedbackStatusUpdateModel.getFeedbackActivityID());

				if(StringUtils.isNotEmpty(feedbackStatusUpdateModel.getFeedbackStatusNew())){

					if(StringUtils.isEmpty(workFlowFeedbackModel.getStepID()) || 
							StringUtils.isEmpty(workFlowFeedbackModel.getStepName())) {
						workFlowFeedbackModel.setStepID(AppConstants.NA);
						workFlowFeedbackModel.setStepName(AppConstants.NA);
					}
					if(StringUtils.isEmpty(workFlowFeedbackModel.getInstantFeedback())) {
						workFlowFeedbackModel.setInstantFeedback(AppConstants.NA);
					}
					if(StringUtils.isEmpty(feedbackStatusUpdateModel.getStatusUpdateComment())) {
						feedbackStatusUpdateModel.setStatusUpdateComment(AppConstants.NA);
					}
					String name = activityMasterDAO.getNameandSignum(signum);
					if(StringUtils.isNotEmpty(name)) {
						workFlowFeedbackModel.setSignumAndName(name);
					}

					workFlowFeedbackModel.getWorkFlowFeedbackActivityModel().setUserRole(role);

					Map<String, Object> placeHolder = emailService.enrichMailforFeedbackStatus(workFlowFeedbackModel,
							feedbackStatusUpdateModel);

					emailService.sendMail(AppConstants.NOTIFICATION_TYPE_FEEDBACK_STATUS, placeHolder);


				}
				response.addFormMessage("Feedback updated Successfully");
			}
		} catch (Exception e) {
			response.addFormError(e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	public List<EmployeeBasicDetails> getASPMsByFilter(String term) {
		return activityMasterDAO.getASPMsByFilter(term);
	}

	public DownloadTemplateModel downloadFeedbackFile(String signum, FeedbackNEPMModel feedbackNePmModel, String role,
			HttpServletResponse response) throws IOException {

		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = "FeedbackExport" + "-" + (new SimpleDateFormat(AppConstants.DATE_FORMAT).format(new Date()))
				+ AppConstants.XLSX;

		List<Map<String, Object>> PMFeedback = activityMasterDAO.downloadFeedbackFile(signum,feedbackNePmModel,role);

		byte[] fData = null;
		if (CollectionUtils.isNotEmpty(PMFeedback)) {
			fData = FileUtil.generateXlsFile(PMFeedback);

		}

		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);

		return downloadTemplateModel;

	}

	public List<EmployeeBasicDetails> testGetEmployeesByFilter(String term) {
		return activityMasterDAO.testGetEmployeesByFilter(term);
	}

	public Response<Void> saveSignalRNotificationLog(NotificationLogModel notificationLogModel) {
		Response<Void> response = new Response<>();
		try {
			LOG.info("Start execution of saveSignalRNotificationLog  Api.");
			validateNotificationLogModel(notificationLogModel);
			activityMasterDAO.saveSignalRNotificationLog(notificationLogModel);
			response.addFormMessage("Notification Added Successfully!");
			LOG.info("Execution of saveSignalRNotificationLog Api Successful.");
		} catch (ApplicationException e) {
			response.addFormError(e.getMessage());
		} catch (Exception e) {
			throw new ApplicationException(500, e.getMessage());
		}
		return response;
	}
	private static final String[] EXCEL_MIME_TYPES = {
	"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" };

	public Response<Void> updateSignalRNotificationLog(Integer notificationId) {
		Response<Void> response = new Response<>();
		try {
			LOG.info("Start execution of updateSignalRNotificationLog  Api.");
			validationUtilityService.validateIntegerForNullOrZero(notificationId, AppConstants.NOTIFICATION_ID);
			activityMasterDAO.updateSignalRNotificationLog(notificationId);
			response.addFormMessage("Notification updated Successfully!");
			LOG.info("Execution of updateSignalRNotificationLog Api Successful.");
		} catch (ApplicationException e) {
			response.addFormError(e.getMessage());
		} catch (Exception e) {
			throw new ApplicationException(500, e.getMessage());
		}
		return response;
	}

	private void validateNotificationLogModel(NotificationLogModel notificationLogModel) {
		validationUtilityService.validateStringForBlank(notificationLogModel.getSignum(), AppConstants.SIGNUM);
		validationUtilityService.validateStringForBlank(notificationLogModel.getNotificationPurpose(),
				AppConstants.NOTIFICATION_PURPOSE);
		validationUtilityService.validateStringForBlank(notificationLogModel.getNotificationFrom(),
				AppConstants.NOTIFICATION_FROM);
		validationUtilityService.validateStringForBlank(notificationLogModel.getNotificationTo(),
				AppConstants.NOTIFICATION_TO);
	}


	public Response<List<LogLevelModel>> getLogLevelByUser(String userSignum) {
		Response<List<LogLevelModel>> response = new Response<>();
		try {
			LOG.info("Start execution of getLogLevelByUser  Api.");
			validationUtilityService.validateStringForBlank(userSignum, AppConstants.SIGNUM);
			response.setResponseData(activityMasterDAO.getLogLevelByUser(userSignum));
			LOG.info("Execution of getLogLevelByUser Api Successful.");
		} catch (ApplicationException e) {
			response.addFormError(e.getMessage());
		} catch (Exception e) {
			throw new ApplicationException(500, e.getMessage());
		}
		return response;
	}


	/**
	 * @author ekmbuma
	 * 
	 * @param uploadedBy
	 * @param file
	 * @return Response<String>
	 * @throws Exception
	 */
	public Response<String> uploadFileForESSLeaveData(String uploadedBy, MultipartFile file){
		Response<String> apiResponse = new Response<>();

		try {
			if (Arrays.asList(EXCEL_MIME_TYPES).contains(file.getContentType())) {

				String fileName = AppUtil.getFileNameWithTimestamp(file.getName());
				flowChartService.uploadFile(file, AppConstants.MSS_LEAVE_UPLOAD_TABLE_NAME, fileName);

				String rootPath = appService.getConfigUploadFilePath() + AppConstants.MSS_LEAVE_UPLOAD_TABLE_NAME + AppConstants.FORWARD_SLASH;
				String filePath = rootPath + fileName;
				String csvFilePath = rootPath + AppUtil.getFileNameWithTimestamp("MSSLeaveTempCSV");

				// convert uploaded file into csv
				AppUtil.convertMSSLeaveExcelToCSV(filePath, csvFilePath);
				LOG.info("csv path is --->" +csvFilePath);

				// validation for column
				Map<String, String> validationResult = FileCSVHelper.validateBulkMssLeaveFile(csvFilePath);
				if (MapUtils.isNotEmpty(validationResult)) {
					for (Entry<String, String> entrySet : validationResult.entrySet()) {
						apiResponse.addFormError(validationResult.get(entrySet.getKey()));
					}
					return apiResponse;
				}

				// Bulk upload method.
				Map<String, Object> data = appService.CsvBulkUploadForLeave(csvFilePath, AppConstants.MSS_LEAVE_UPLOAD_TABLE_NAME, fileName, AppConstants.CSV_CHAR_DOUBLE_SEMICOLON);
				if((boolean)data.get("isUploadSuccess")) {
					LOG.info("CsvBulkUploadForLeave --->isUploadSuccess");
					Map<String, String> uploadResults = this.activityMasterDAO.uploadFileForESSLeaveData(uploadedBy,fileName);

					apiResponse.addFormMessage((String)data.get("msg"));
					apiResponse.addFormMessage(uploadResults.toString());
					apiResponse.setValidationFailed(false);
					LOG.info("File uploaded successfully !! - FileName/TableName : " + fileName);
				}else {
					apiResponse.addFormError((String)data.get("error"));
					apiResponse.setValidationFailed(true);
				}
			} else {
				apiResponse.addFormError("Invalid File Type!!,Please upload only xslx file");
			}
		} catch (Exception e) {
			apiResponse.addFormError(e.getMessage());
			apiResponse.setValidationFailed(true);
		}
		return apiResponse;
	}

	public Response<String> uploadFileForESSLeaveDataV2(String uploadedBy, MultipartFile file){
		Response<String> apiResponse = new Response<String>();
		String fileName = AppUtil.getFileNameWithTimestamp(file.getName());
		String rootPath = appService.getConfigUploadFilePath() + AppConstants.MSS_LEAVE_UPLOAD_TABLE_NAME + AppConstants.FORWARD_SLASH;
		String filePath = rootPath + fileName;
		try {
			flowChartService.uploadFile(file, AppConstants.MSS_LEAVE_UPLOAD_TABLE_NAME, fileName);


			// validation for column
			Map<String, String> validationResult = FileCSVHelper.validateBulkMssLeaveFile(filePath);
			if (MapUtils.isNotEmpty(validationResult)) {
				for (Entry<String, String> entrySet : validationResult.entrySet()) {
					apiResponse.addFormError(validationResult.get(entrySet.getKey()));
				}
				return apiResponse;
			}

			// Bulk upload method.
			Map<String, Object> data = appService.CsvBulkUploadForLeave(filePath, AppConstants.MSS_LEAVE_UPLOAD_TABLE_NAME, fileName, AppConstants.CSV_CHAR_DOUBLE_SEMICOLON);
			if((boolean)data.get("isUploadSuccess")) {
				LOG.info("CsvBulkUploadForLeave --->isUploadSuccess");
				Map<String, String> uploadResults = this.activityMasterDAO.uploadFileForESSLeaveData(uploadedBy,fileName);

				apiResponse.addFormMessage((String)data.get("msg"));
				apiResponse.addFormMessage(uploadResults.toString());
				apiResponse.setValidationFailed(false);
				LOG.info("File uploaded successfully !! - FileName/TableName : " + fileName);
			}else {
				apiResponse.addFormError((String)data.get("error"));
				apiResponse.setValidationFailed(true);
			}
		} catch (Exception e) {
			apiResponse.addFormError(e.getMessage());
			apiResponse.setValidationFailed(true);
		}finally {
			try {
				appService.dropTable(fileName);
				Files.delete(Paths.get(filePath));
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		return apiResponse;
	}
	public List<EmployeeBasicDetails> getEmployeesOrManager(String term, String managerSignum) {
		return activityMasterDAO.getEmployeesOrManager(term,managerSignum);
	}

	public List<HashMap<String, String>> getLineManagersBySearch(String term) {
		return activityMasterDAO.getLineManagersBySearch(term);
	}

	public Map<String, Object> getInstructionURLList(List<WFStepInstructionModel> instructionModel) {

		Map<String, Object> map = new LinkedHashMap<>();
		List<WFStepInstructionModel> workflowStepInstruction  = new ArrayList<>();
		List<WFStepInstructionModel> instructionList=null;

		try {
			if (CollectionUtils.isNotEmpty(instructionModel)) {
				for(WFStepInstructionModel wfStepInstructionModel : instructionModel ) {
					if(wfStepInstructionModel.getFlowChartDefID()!=0) {
						instructionList = this.activityMasterDAO.getInstructionURLList(wfStepInstructionModel); 
						workflowStepInstruction.addAll(instructionList);

					}
				}

				map.put("data",workflowStepInstruction);


			}
		}
		catch (Exception e) {
			throw new ApplicationException(500, e.getMessage());
		}
		return map;

	}

	public TblAdhocBooking getAdhocBookingForSignum(String signumID, String status) {
		return activityMasterDAO.getAdhocBookingForSignum(signumID, status);
	}


	/**
	 * 
	 * @param projectID
	 * @param response
	 * @param status
	 * @return DownloadTemplateModel
	 * @throws IOException
	 * @throws SQLException
	 */
	public DownloadTemplateModel downloadErrorDictionaryFile(int sourceID, HttpServletResponse response) throws IOException, SQLException {

		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = "ErrorDictionaryViewExport" + "-" + (new SimpleDateFormat(AppConstants.DATE_FORMAT).format(new Date()))
				+ AppConstants.XLSX;

		
		 /* boolean checkSource=rpaDAO.isSourceExists(sourceID);
          if(!checkSource) {
  			throw new ApplicationException(200, "Please provide valid Source!");
          }
          else {*/
        	  List<Map<String, Object>> errorDictionaryData = activityMasterDAO.downloadErrorDictionaryFile(sourceID);
      		byte[] fData = null;
      		if (CollectionUtils.isNotEmpty(errorDictionaryData)) {
      			fData = FileUtil.generateXlsFile(errorDictionaryData);
      		}

      		downloadTemplateModel.setpFileContent(fData);
      		downloadTemplateModel.setpFileName(fName);
      //    }
		return downloadTemplateModel;
	}
	
	public Response<Void> deleteNEList(DeleteNEListModel deleteNEListModel) {
		Response<Void> response=new Response<>();
		try {
			if(deleteNEListModel.getNetworkElementID().isEmpty() || (deleteNEListModel.getSignumID().equalsIgnoreCase("Null") || deleteNEListModel.getSignumID().isEmpty())) {
				throw new ApplicationException(500, NE_NOT_NULL );
			}
			else {
				String[] NEList = deleteNEListModel.getNetworkElementID().split("[,]", 0);
				int maxLimit = configurations.getIntegerProperty(ConfigurationFilesConstants.NETWORK_ELEMENT_DELETE_COUNT);
				if(NEList.length > maxLimit) {
					response.addFormError(DELETE_MAXLIMIT);
				}
				else {
					activityMasterDAO.deleteNEList(deleteNEListModel.getNetworkElementID(),deleteNEListModel.getSignumID());
					response.addFormMessage(NE_DELETED);
					LOG.info("TBL_NETWORK_ELEMENT : SUCCESS");
				}
			}
				
		} catch (Exception e) {
			response.addFormError(e.getMessage());
		}
		return response;
	}
		
}


