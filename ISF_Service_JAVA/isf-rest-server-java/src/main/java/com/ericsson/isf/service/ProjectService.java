package com.ericsson.isf.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
//import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.ExternalInterfaceManagmentDAO;
import com.ericsson.isf.dao.FlowChartDAO;
import com.ericsson.isf.dao.ProjectDAO;
import com.ericsson.isf.dao.ProjectScopeDao;
import com.ericsson.isf.dao.ProjectScopeDetailDao;
import com.ericsson.isf.dao.WorkOrderPlanDao;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.exception.ApplicationExceptionHandler;

import com.ericsson.isf.model.ActivityModel;
import com.ericsson.isf.model.AdhocTypeModel;
import com.ericsson.isf.model.CountryModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DeliveryAcceptanceModel;
import com.ericsson.isf.model.DeliveryResponsibleModel;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.ExternalAppReferenceModel;
import com.ericsson.isf.model.ExternalSourceModel;
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
import com.ericsson.isf.model.ScopeDetailModel;
import com.ericsson.isf.model.WorkFlowFilterModel;
import com.ericsson.isf.model.WorkflowProficiencyModel;
import com.ericsson.isf.model.botstore.TblProjects;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.ericsson.isf.util.FileUtil;
import com.ericsson.isf.util.IsfCustomIdInsert;

@Service
public class ProjectService {

	private static final String SOURCE_ADDED_SUCCESSFULLY = "Source Added Successfully.";
	private static final String SIGNUM_AND_WORKFLOWID_BOTH_SHOULD_NOT_BE_BLANK = "Signum and workflowid both should not be blank!";
	private static final String NULL_ZERO_IS_NOT_VALID_WORKFLOW_ID = "Null/Zero is not valid WorkflowId!";
	private static final String NULL_IS_NOT_VALID_SIGNUM = "Null is not valid Signum!";
	private static final String PROFICIENCY_STATUS = "PROFICIENCYSTATUS";
	private static final String PROFICIENCY_UPDATED_SUCCESSFULLY = "Proficiency Updated Successfully!";
	private static final String RECORDS_TOTAL = "recordsTotal";
    private static final String RECORDS_FILTERED = "recordsFiltered";
    private static final String DELIVERABLE_UNIT="deliverableUnit";
    
	@Autowired
	private ProjectDAO projectDao;

	@Autowired
	private WorkOrderPlanDao workOrderPlanDao;

	@Autowired
	private ProjectScopeDao projectScopeDao;

	@Autowired
	private ActivityMasterDAO activityMasterDAO;

	@Autowired
	private OutlookAndEmailService emailService;
	
    @Autowired
    ProjectScopeDetailDao projectScopeDetailDao;
    
    @Autowired
	private ValidationUtilityService validationUtilityService;
    
    @Autowired
    private IsfCustomIdInsert isfCustomIdInsert;
    
    @Autowired
    private FlowChartDAO flowChartDao;
    
    @Autowired
	private ExternalInterfaceManagmentDAO erisiteManagmentDAO;
    @Autowired
	private ApplicationConfigurations configurations;
    
    @Autowired
	private AdhocActivityService adhocActivityService;
    
    private static final String DELIVERY_RESPONSIBLE_ADDED = "Delivery Responsible Added Successfully!";

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

	private static final String MODIFIED_BY = "ModifiedBy";

	private static final String MSG_EMPTY_LIST = "Workflow Proficiency Model Can Not be Empty.";
	
	private static final String NO_DATA_FOUND = "No Data Found!";

	/*
	 * //TODO code cleanup public List<ProjectsModel> getStatus() {
	 * List<ProjectsModel> status = new ArrayList<>(); status =
	 * this.projectDao.getStatus(); return status; }
	 */

	public List<ProjectsModel> getProjectByFilters(ProjectFilterModel projectFilterModel) {
		List<ProjectsModel> projects = new ArrayList<>();
		projects = this.projectDao.getProjectByFilters(projectFilterModel);
		return projects;
	}

	public List<ProjectsModel> getDashboardProject(String marketArea, String role, String signum) {
		return this.projectDao.getDashboardProject(marketArea, role, signum);
	}

	@Transactional("transactionManager")
	public ResponseEntity<Response<Void>> addDeliveryResponsible(DeliveryResponsibleModel deliveryResponsibleModel) {
		Response<Void> response = new Response<>();
		int maxLimit = configurations.getIntegerProperty(ConfigurationFilesConstants.MAX_DR_ALLOWED);
		if(maxLimit>0 ) {
			boolean ifLimit = projectDao.checkDrCount(deliveryResponsibleModel.getProjectID(),maxLimit);
		
		if(ifLimit) {
			String error = "Maximum limit exceeded- Maximum " + maxLimit+ " DR Allowed";
			LOG.error(error);
			response.addFormError(error);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);			
		}
		}
		
		try {
		
		boolean ifExists = this.projectDao.checkIFSignumExists(deliveryResponsibleModel.getDeliveryResponsible(),
				deliveryResponsibleModel.getProjectID());
		if (!ifExists) {
			String signumAndName = deliveryResponsibleModel.getDeliveryResponsible();
			String[] signum = signumAndName.split("\\(");
			deliveryResponsibleModel.setSignumID(signum[0]);

			this.projectDao.addDeliveryResponsible(deliveryResponsibleModel);
			response.addFormMessage(DELIVERY_RESPONSIBLE_ADDED);
			
		} else {
			throw new ApplicationException(500, "Signum already exists for selected Project!!!");
		       }
		}	
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch(Exception ex) {
			response.addFormError(ex.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	public List<DeliveryResponsibleModel> getDeliveryResponsibleByProject(int projectID) {
		Boolean checkProject = this.projectDao.checkProject(projectID);
		
		List<DeliveryResponsibleModel> deliveryResponsibleModel = new ArrayList<DeliveryResponsibleModel>();
		if (checkProject) {
			deliveryResponsibleModel = this.projectDao.getDeliveryResponsibleByProject(projectID);
		}
		
		return deliveryResponsibleModel;
	}

	@Transactional("transactionManager")
	public boolean deleteProject(DeliveryResponsibleModel deliveryResponsibleModel) {

		return this.projectDao.deleteProject(deliveryResponsibleModel);
	}

	public Response<List<HashMap<String, Object>>> getProjectAcceptance(String signum, String marketArea, String role) {
		
		Response<List<HashMap<String, Object>>> apiResponse=new Response<List<HashMap<String, Object>>>();
		try {
			List<HashMap<String, Object>> obj = this.projectDao.getProjectAcceptance(signum, marketArea, role);
			if (obj != null && !obj.isEmpty()) {
				apiResponse.setResponseData(obj);
				return apiResponse;
				// return this.projectDao.getProjectAcceptance(signum);
			} else {
				throw new ApplicationException(500, "No Data available for selected signum!!!");
			}	
		}catch (Exception e) {
			apiResponse.addFormError(e.getMessage());
			LOG.error(e.getMessage());
		}
		return apiResponse;

	}

	/*
	 * //TODO code cleanup public List<Map<String,Object>> getCountries() { return
	 * this.projectDao.getCountries(); }
	 */

	/*
	 * //TODO code cleanup public List<Map<String,Object>>
	 * getCountriesbyMarketAreaID(String marketAreaID) { return
	 * this.projectDao.getCountriesbyMarketAreaID(marketAreaID); }
	 */
	// TODO code cleanup
	public List<Map<String, Object>> getCustomers(String countryId) {
		return this.projectDao.getCustomers(countryId);
	}
	/*
	 * //TODO code cleanup public List<Map<String,Object>> getAllProjects(String
	 * marketAreaID, String countryID, String signum, String status) { return
	 * this.projectDao.getAllProjects(marketAreaID ,countryID ,signum ,status); }
	 */

	public Map<String, Object> deleteProjectComponents(ProjectComponentModel projectComponentModel) {
		Map<String, Object> finalData = new HashMap<String, Object>();
		boolean isDeleted = false;
		String msg = "";
		if (projectComponentModel.getType().equalsIgnoreCase("PROJECT")) {

			List<Map<String, Object>> data = this.projectDao.getBookingDetalsByScope(projectComponentModel);
			List<Map<String, Object>> demandData = this.projectDao.getDemandDetailsByScope(projectComponentModel);
			if (data.isEmpty() && demandData.isEmpty()) {

				this.projectDao.updateWorkEffort(projectComponentModel);
				this.projectDao.updateResourcePosition(projectComponentModel);
				this.projectDao.updateResourceRequest(projectComponentModel);
				this.projectDao.deleteBookedResources(projectComponentModel);
				this.projectDao.deleteNetworkElement(projectComponentModel);

				this.projectDao.updateWorkOrder(projectComponentModel);
//						this.projectDao.updateAdhocWorkOrder(projectComponentModel);
				this.projectDao.updateWorkOrderPlan(projectComponentModel);
				this.projectDao.updateFlowChartStepDetails(projectComponentModel);
				this.projectDao.updateWorkFlow(projectComponentModel);

				this.projectDao.updateActivity(projectComponentModel);
				this.projectDao.updateProjectScopeDetail(projectComponentModel);
				this.projectDao.updateProjectScope(projectComponentModel);
				this.projectDao.updateProjectByProjectID(projectComponentModel);
				this.projectDao.updateDeliveryResonsible(projectComponentModel);

				isDeleted = true;
				msg = "Project Flow got deleted";
			} else {
				isDeleted = false;
				msg = "WO is already executed for Project ID : " + projectComponentModel.getProjectID()
						+ "/ Demand for the deliverable is in Deployed or Closed State ";
			}
		} else if (projectComponentModel.getType().equalsIgnoreCase("REJECT_PROJECT")) {

			this.projectDao.updateWorkEffort(projectComponentModel);
			this.projectDao.updateResourcePosition(projectComponentModel);
			this.projectDao.updateResourceRequest(projectComponentModel);
			this.projectDao.deleteBookedResources(projectComponentModel);
			this.projectDao.deleteNetworkElement(projectComponentModel);

			this.projectDao.updateWorkOrder(projectComponentModel);
//						this.projectDao.updateAdhocWorkOrder(projectComponentModel);
			this.projectDao.updateWorkOrderPlan(projectComponentModel);
			this.projectDao.updateFlowChartStepDetails(projectComponentModel);
			this.projectDao.updateWorkFlow(projectComponentModel);

			this.projectDao.updateActivity(projectComponentModel);
			this.projectDao.updateProjectScopeDetail(projectComponentModel);
			this.projectDao.updateProjectScope(projectComponentModel);
			this.projectDao.updateProjectByProjectID(projectComponentModel);
			this.projectDao.updateDeliveryResonsible(projectComponentModel);

			isDeleted = true;
			msg = "Project Flow got Rejected";
		} else if (projectComponentModel.getType().equalsIgnoreCase("SCOPE")) {
			
			ProjectScopeModel projectScopeDetails	=	projectScopeDao.getProjectScopeByScopeId(
					projectComponentModel.getScopeID()).get(0);

			List<String> positionStatus = this.projectDao.getPositionStatus(projectComponentModel.getScopeID());
			List<Map<String, Object>> data = this.projectDao.getBookingDetalsByScope(projectComponentModel);
			List<Map<String, Object>> demandData = this.projectDao.getDemandDetailsByScope(projectComponentModel);
			if (data.isEmpty() && demandData.isEmpty() && positionStatus.isEmpty()) {

				this.projectDao.updateWorkEffort(projectComponentModel);
				this.projectDao.updateResourcePosition(projectComponentModel);
				this.projectDao.updateResourceRequest(projectComponentModel);
				this.projectDao.deleteBookedResources(projectComponentModel);

				if(StringUtils.isNotEmpty(projectScopeDetails.getSource()) && projectScopeDetails.getExternalProjectId()!=null) {
					if( this.projectScopeDao.checkifProjectScopeErisite(projectScopeDetails.getSource())) {
						this.projectDao.updateWorkOrderErisite(projectComponentModel);
						
						List<Integer> wPlanList=	this.projectDao.getWorkPlanIds(projectScopeDetails.getProjectID(),projectScopeDetails.getExternalReference(),
								projectScopeDetails.getExternalProjectId(),projectScopeDetails.getExternalWorkplanTemplate(),projectScopeDetails.getSource());
			    		if(!wPlanList.isEmpty()) {
			    			String wPlanIds=wPlanList.stream().map(String::valueOf)
			  					  .collect(Collectors.joining(AppConstants.CSV_CHAR_COMMA, AppConstants.OPENING_BRACES, AppConstants.CLOSING_BRACES));
			    			this.projectDao.updateIsfProjectIdExternal(0,projectScopeDetails.getExternalReference(),
			    					projectScopeDetails.getExternalProjectId(),projectScopeDetails.getExternalWorkplanTemplate(),projectScopeDetails.getSource(), wPlanIds);
			    		
			    		}
						
						woHistorycheck(projectScopeDetails);
					}
				}else {
					this.projectDao.updateWorkOrder(projectComponentModel);
				}
//						this.projectDao.updateAdhocWorkOrder(projectComponentModel);
				this.projectDao.updateWorkOrderPlan(projectComponentModel);
//						this.projectDao.updateWorkFlow(projectComponentModel);
				this.projectDao.updateActivity(projectComponentModel);
				this.projectDao.updateProjectScopeDetail(projectComponentModel);
				this.projectDao.updateProjectScope(projectComponentModel);
				workOrderPlanDao.updateExecutionPlanStatus(projectComponentModel.getExecutionPlanId(), false, projectComponentModel.getLoggedInUser());

				isDeleted = true;
				msg = "Project Flow got deleted";
			} else if (!positionStatus.isEmpty() && data.isEmpty()) {
				isDeleted = false;
				msg = "Deliverable[" + projectComponentModel.getScopeID()
						+ "] cannot be deleted as its positions are already processed.";
			} else {
				isDeleted = false;
				msg = "WO is already executed for Deliverable ID : " + projectComponentModel.getScopeID()
						+ "/ Demand for the deliverable is in Deployed or Closed State ";
			}
		} else if (projectComponentModel.getType().equalsIgnoreCase("SCOPEDETAIL")) {

			List<Map<String, Object>> data = this.projectDao.getBookingDetalsByScopeDetailID(projectComponentModel);
			List<Map<String, Object>> demandData = this.projectDao.getDemandDetailsByScope(projectComponentModel);
			if (data.isEmpty() && demandData.isEmpty()) {

				this.projectDao.updateWorkEffort(projectComponentModel);
				this.projectDao.updateResourcePosition(projectComponentModel);
				this.projectDao.updateResourceRequest(projectComponentModel);
				this.projectDao.deleteBookedResources(projectComponentModel);

				this.projectDao.updateWorkOrder(projectComponentModel);
//						this.projectDao.updateAdhocWorkOrder(projectComponentModel);
				this.projectDao.updateWorkOrderPlan(projectComponentModel);
//						this.projectDao.updateWorkFlow(projectComponentModel);
				this.projectDao.updateActivity(projectComponentModel);
				this.projectDao.updateProjectScopeDetail(projectComponentModel);

				isDeleted = true;
				msg = "Project Flow got deleted";
			} else {
				isDeleted = false;
				msg = "WO is already executed for DELIVERABLE DETAIL ID : " + projectComponentModel.getScopeDetailID();
			}
		} else if (projectComponentModel.getType().equalsIgnoreCase("ACTIVITY")) {

			List<Map<String, Object>> data = this.projectDao.getBookingDetalsByActivityID(projectComponentModel);
			if (data.isEmpty()) {
				List<Map<String, Object>> woCount = this.projectDao.getWoCountByActivityID(projectComponentModel);
				List<Map<String, Object>> wfCount = this.projectDao.getWfCountByActivityID(projectComponentModel);

				isDeleted = true;
				msg = "WO/WF are mapped to this Activity";
				finalData.put("count_woid", woCount.size());
				finalData.put("count_flowChart", wfCount.size());

			} else {
				isDeleted = false;
				msg = "WO is already executed for Activity ID : " + projectComponentModel.getActivityID();
			}
		} else if (projectComponentModel.getType().equalsIgnoreCase("DELETE_ACTIVITY")) {
			this.projectDao.disableWorkOrder(projectComponentModel);
//					this.projectDao.updateAdhocWorkOrderByActivityID(projectComponentModel);
			this.projectDao.updateWorkOrderPlanByActivityID(projectComponentModel);
			this.projectDao.disableWorkFlow(projectComponentModel);
			this.projectDao.disableAcitvity(projectComponentModel);

			isDeleted = true;
			msg = "Project Flow got deleted";
		} else if (projectComponentModel.getType().equalsIgnoreCase("WORKFLOW")) {

			StringBuilder message = new StringBuilder();
			List<Map<String, Object>> executionPlanDetails = workOrderPlanDao
					.getExecutionPlanDetailsByProjectIDSubactivityID(projectComponentModel.getProjectID(),
							projectComponentModel.getSubActivityID(),projectComponentModel.getSubActivityFlowChartDefID());
			if (CollectionUtils.isEmpty(executionPlanDetails)) {

				List<Map<String, Object>> data = this.projectDao.getBookingDetalsFlowChartDefID(projectComponentModel);
				if (CollectionUtils.isNotEmpty(data)) {
					
					message.append("WO already Executed for the selected Work Flow (WO COUNT) : ").append(data.size());
				}
				isDeleted = true;
				finalData.put("count_woid", data.size());
			} else {
				String planName="";
				if(executionPlanDetails.size()>0) {
					for(int i=0;i<executionPlanDetails.size();i++) {
						planName=StringUtils.isEmpty(planName)?planName.concat(executionPlanDetails.get(i).get("planName").toString()):planName+","+planName.concat(executionPlanDetails.get(i).get("planName").toString());
					}
				}
				isDeleted = false;
				message.append("Work flow Attached with deliverable Plan ! Please detached it from delivarable plan ( "+ planName+" )");
			}
			msg = new String(message);
		} else if (projectComponentModel.getType().equalsIgnoreCase("DELETE_WORKFLOW")) {

			this.projectDao.updateWorkOrderByFlowChartDefID(projectComponentModel);
			this.projectDao.updateWoPlanIDWithNonExecutedWO(projectComponentModel);
//					this.projectDao.updateAdhocWoIDWithNonExecutedWO(projectComponentModel);
			this.projectDao.updateWorkFlow(projectComponentModel);
			Integer subActivityDefID = checkAndgetExpertWorkFlow(projectComponentModel.getSubActivityFlowChartDefID(),
					projectComponentModel.getProjectID(), projectComponentModel.getSubActivityID());
			if (subActivityDefID != 0) {
				projectComponentModel.setSubActivityFlowChartDefID(subActivityDefID);
				this.projectDao.updateWorkFlow(projectComponentModel);
			}

			isDeleted = true;
			msg = "Project Flow got deleted";
		} else if (projectComponentModel.getType().equalsIgnoreCase("WOPLAN")) {

			List<Map<String, Object>> data = this.projectDao.getBookingDetalsWoPlanID(projectComponentModel);
			List<Map<String, Object>> allData = this.projectDao.getAllBookingDetalsWoPlanID(projectComponentModel);
			if (data.size() == allData.size()) {
				isDeleted = false;
				msg = "No Work Order is in Assigned,Deferred and Reopened state. Wo Plan Can not be deleted";
			} else {
				isDeleted = true;
				msg = "Work Orders can be deleted.";
			}
			finalData.put("count_woid", data.size());
			finalData.put("all_count_woid", allData.size());
		} else if (projectComponentModel.getType().equalsIgnoreCase("DELETE_WOPLAN")) {
//			List<Map<String, Object>> data = this.projectDao.getBookingDetalsWoPlanID(projectComponentModel);
			List<Map<String, Object>> allData = this.projectDao.getAllBookingDetalsWoPlanID(projectComponentModel);
			this.projectDao.updateWorkOrderByWoPlanID(projectComponentModel);

			
			List<Integer> wPlanId	=	this.projectDao.getListWoPlanID(projectComponentModel);
			
			projectComponentModel.getLstWoPlanID().removeAll(wPlanId);
			if (CollectionUtils.isNotEmpty(projectComponentModel.getLstWoPlanID())) {
				msg = "Wo Plan Got Deleted : " + projectComponentModel.getLstWoPlanID();
				this.projectDao.updateWorkOrderPlan(projectComponentModel);
			}

			isDeleted = true;
			msg += "Total WO deleted : " + allData.size();
		}
		finalData.put("isDeleted", isDeleted);
		finalData.put("msg", msg);
		return finalData;

	}

	/**
	 * @param projectScopeDetails 
	 * 
	 */
	private boolean woHistorycheck(ProjectScopeModel projectScopeDetails) {
		try {

			List<Integer> wPlanList=	this.projectDao.getWorkPlanIds(projectScopeDetails.getProjectID(),projectScopeDetails.getExternalReference(),
					projectScopeDetails.getExternalProjectId(),projectScopeDetails.getParentWorkplanTemplateName(),projectScopeDetails.getSource());
			if(CollectionUtils.isNotEmpty(wPlanList) && wPlanList.size()>0) {

				String wPlanIds=wPlanList.stream().map(String::valueOf)
						.collect(Collectors.joining(AppConstants.CSV_CHAR_COMMA, AppConstants.OPENING_BRACES, AppConstants.CLOSING_BRACES));

				List<Map<String, Object>> woCreationDataList = this.erisiteManagmentDAO.checkIfExistsInCreation(
						projectScopeDetails.getExternalReference(),
						wPlanIds,
						projectScopeDetails.getExternalProjectId(),
						projectScopeDetails.getParentWorkplanTemplateName(),
						Integer.parseInt(projectScopeDetails.getSource()),
						projectScopeDetails.getProjectID()
						);


				for(Map<String, Object> woCreationData :woCreationDataList) {
					if (woCreationData != null && woCreationData.get("WOCreationID") != null) {
						String historyStatus = null;
						try {
							historyStatus = this.erisiteManagmentDAO
									.checkIfWOExists(Long.parseLong(woCreationData.get("WOCreationID").toString()));
						} catch (Exception e) {
							e.printStackTrace();
							return true;
						}
						if ("INPROGRESS".equals(historyStatus.toUpperCase())) {
							try {
								this.erisiteManagmentDAO.updateExecutionPlanBulkWoCreation(Long.parseLong(woCreationData.get("WOCreationID").toString()));
							} catch (Exception e) {
								e.printStackTrace();
							}
						} 
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return true;
		}

		return true;
	}

	public Integer checkAndgetExpertWorkFlow(Integer subActivityFlowChartDefID, int projectID, int subActivityID) {
		String woName = this.getWorkFlowName(subActivityFlowChartDefID);
		return projectDao.getExpertWFDefID(woName + "_EXPERT", projectID, subActivityID);
	}

	public String getWorkFlowName(Integer subActivityFlowChartDefID) {
		return projectDao.getWorkFlowName(subActivityFlowChartDefID);
	}

	public Map<String, Object> editProjectComponents(ProjectComponentModel projectComponentModel) {
		Map<String, Object> finalData = new HashMap<String, Object>();
		if (projectComponentModel.getType().equalsIgnoreCase("SCOPE")) {
			List<Map<String, Object>> data = this.projectDao.getScopeByScopeID(projectComponentModel);
			List<Map<String, Object>> bookingData = this.projectDao.getBookingDetalsByScope(projectComponentModel);
			List<Map<String, Object>> woData = this.projectDao.getWoDetailsByScopeID(projectComponentModel);
			List<Map<String, Object>> resourceRequestData = this.projectDao
					.getResourceRequestDataByProjecDetails(projectComponentModel);

			boolean flag = false;
			List<String> editableItems = new ArrayList<String>();
			if (!bookingData.isEmpty()) {
				flag = true;
			}
			if (!woData.isEmpty()) {
				flag = true;
			}
			if (!resourceRequestData.isEmpty()) {
				flag = true;
			}
			if (data.get(0).get("source") == null) {
				editableItems.add("select_ext_resource");
				editableItems.add("reference_name");
			} else {
				if ((Integer) data.get(0).get("source") == 1) {
					editableItems.add("select_ext_resource");
					editableItems.add("reference_name");
				}
			}
			editableItems.add("end_date1");
			editableItems.add("scope_name");
			editableItems.add("select_req_type");
			editableItems.add("select_deliverable_unit");
			editableItems.add("select_asp_vendor");
			editableItems.add("delivery_status");
			
			editableItems.add("selectScopeType");
			editableItems.add("selectMethod");
			editableItems.add("selectProjectFinancial");
			editableItems.add("selectOperatorCount");

			if (!flag) {
				editableItems.add("start_date1");
			}

			finalData.put("data", data);
			finalData.put("editableItems", editableItems);
			finalData.put("isEdited", true);

			return finalData;
		} else if (projectComponentModel.getType().equalsIgnoreCase("SCOPEDETAIL")) {

			boolean flag = false;
			boolean isEdited = false;

			List<Map<String, Object>> data = this.projectDao.getBookingDetalsByScopeDetailID(projectComponentModel);
			List<Map<String, Object>> activityData = this.projectDao.getActivityByScopeDetailID(projectComponentModel);
			
			List<Map<String, Object>> workFlowData = this.projectDao.getWorkFlowByScopeDetailID(projectComponentModel);
			List<Map<String, Object>> workOrderData = this.projectDao
					.getWorkOrderByScopeDetailID(projectComponentModel);
			List<String> editableItems = new ArrayList<String>();
			
			List<Map<String, Object>> scopeDetailData = this.projectDao.getscopeDetailData(projectComponentModel);
			
			List<Map<String, Object>> activitySubActivityList = new ArrayList<Map<String,Object>>();
			
			if (data.isEmpty() && workFlowData.isEmpty() && workOrderData.isEmpty()) {
				if(!activityData.isEmpty()) {
					Map<String, Object> activitySubActivityDetail = new HashMap<String, Object>();
					for(Map<String, Object> actData : activityData) {
						activitySubActivityDetail = this.projectDao.getActivitySubActivityNameByScopeDetailID(actData.get("Activity").toString(),actData.get("SubActivity").toString());
						
						activitySubActivityList.add(activitySubActivityDetail);						
					}
					
					if(activitySubActivityList.size() > 0) {
						editableItems.add("select_domainScope");
						editableItems.add("select_serviceScope");
						editableItems.add("select_technologyScope");
						
						finalData.put("data", scopeDetailData);
						finalData.put("editableItems", editableItems);
						finalData.put("activitySubActivityList", activitySubActivityList);
						isEdited = true;
					}else {
						isEdited = false;
						finalData.put("msg","Existing sub-activity doesn't fall into other domain");						
					}
				} else {
					editableItems.add("select_domainScope");
					editableItems.add("select_serviceScope");
					editableItems.add("select_technologyScope");

					finalData.put("data", scopeDetailData);
					finalData.put("editableItems", editableItems);
					finalData.put("activitySubActivityList", activitySubActivityList);
					isEdited = true;					
				}
			} else {
				isEdited = false;
				finalData.put("msg",
						"Either of the Components are mapped to Delievrable Detail ID. WO Executed/WO Created/WF Created/Activity Created");
			}
			finalData.put("isEdited", isEdited);
			return finalData;

		}
		return finalData;
	}

	public Response<Map<String, Object>> updateEditProjectScope(ProjectScopeModel projectScopeModel) {

		Response<Map<String, Object>> response	=	new Response<Map<String, Object>>();
		Map<String, Object> finalData = new HashMap<String, Object>();
		
		try {

		boolean isUpdated = false;
		String msg = "";
		
		//Check if scope already exists
		Boolean checkProjectScopeExceptName = this.projectScopeDao.checkIfProjectScopeExistsExceptName(projectScopeModel.getProjectID()
				,projectScopeModel.getProjectScopeID());
		Boolean checkScopeName = this.projectScopeDao.checkScopeName(projectScopeModel.getProjectID(),
				projectScopeModel.getScopeName(),projectScopeModel.getProjectScopeID());

		if (checkProjectScopeExceptName) {
			if(checkScopeName) {
				throw new ApplicationException(200, "Deliverable name already exists for the selected project !!!");
			}
			
		
		ProjectScopeModel oldProjectScopeModel = projectScopeDao
				.getProjectScopeByScopeId(projectScopeModel.getProjectScopeID()).get(0);
		if(StringUtils.isNotEmpty(oldProjectScopeModel.getSource()) && oldProjectScopeModel.getExternalProjectId()!=null &&
				!StringUtils.equalsIgnoreCase(oldProjectScopeModel.getDeliverableStatus(), projectScopeModel.getDeliverableStatus())) {
			if( this.projectScopeDao.checkifProjectScopeErisite(oldProjectScopeModel.getSource())) {
				if(StringUtils.equalsIgnoreCase(projectScopeModel.getDeliverableStatus(),"Inactive")) {
					ProjectComponentModel projectComponentModel	=	new ProjectComponentModel();
					projectComponentModel.setLoggedInUser(projectScopeModel.getLoggedInUser());
					projectComponentModel.setProjectID(projectScopeModel.getProjectID());
					projectComponentModel.setLoggedInUser(projectScopeModel.getLoggedInUser());
					projectComponentModel.setProjectID(projectScopeModel.getProjectID());
					projectComponentModel.setScopeID(projectScopeModel.getProjectScopeID());
					//projectComponentModel.setScopeDetailID(projectScopeModel.);
					
					this.projectDao.updateWorkOrderErisitePlanned(projectComponentModel);

					List<Integer> wPlanList=	this.projectDao.getWorkPlanIds(oldProjectScopeModel.getProjectID(),oldProjectScopeModel.getExternalReference(),
							oldProjectScopeModel.getExternalProjectId(),oldProjectScopeModel.getParentWorkplanTemplateName(),oldProjectScopeModel.getSource());
		    		if(!wPlanList.isEmpty()) {
		    			String wPlanIds=wPlanList.stream().map(String::valueOf)
		  					  .collect(Collectors.joining(AppConstants.CSV_CHAR_COMMA, AppConstants.OPENING_BRACES, AppConstants.CLOSING_BRACES));
		    			this.projectDao.updateIsfProjectIdExternal(oldProjectScopeModel.getProjectID(),oldProjectScopeModel.getExternalReference(),
		    					oldProjectScopeModel.getExternalProjectId(),oldProjectScopeModel.getParentWorkplanTemplateName(),oldProjectScopeModel.getSource(), wPlanIds);
		    		}
					

					woHistorycheck(oldProjectScopeModel);
				}else if(StringUtils.equalsIgnoreCase(projectScopeModel.getDeliverableStatus(),"Active") ||
						StringUtils.equalsIgnoreCase(projectScopeModel.getDeliverableStatus(),"New")) {

					ProjectScopeModel oldProjectScopeModelErisite	=	this.projectScopeDao.chechIfScopeAlreadyMapped(oldProjectScopeModel,
							projectScopeModel.getDeliverableStatus());
					//						int chechIfScopeAlreadyMapped=this.projectScopeDao.chechIfScopeAlreadyMapped(oldProjectScopeModel);
					if(oldProjectScopeModelErisite!= null) {
						ProjectAllDetailsModel projectDetails = this.projectDao.getProjectDetails(oldProjectScopeModelErisite.getProjectID());

						EmployeeModel employee = activityMasterDAO.getEmployeeBySignum(oldProjectScopeModelErisite.getCreatedBy());
						response.addFormError("External Project " +oldProjectScopeModelErisite.getExternalProjectId()+", "+oldProjectScopeModelErisite.getParentWorkplanTemplateName()
						+" and "+oldProjectScopeModelErisite.getExternalReference()+" are already mapped with project: "+ oldProjectScopeModelErisite.getProjectID() + " - "+ projectDetails.getProjectName() + 
						" and owner of the Deliverable is "+ employee.getEmployeeName());
						return response;
					}
				}
			}
		}
		
		this.projectDao.updateEditProjectScope(projectScopeModel);
		this.workOrderPlanDao.updateExecutionPlanName(projectScopeModel.getScopeName(),
				oldProjectScopeModel.getScopeName(), projectScopeModel.getProjectID());
		isUpdated = true;
		msg = "Deliverable Successfully Updated";
		
		finalData.put("isUpdated", isUpdated);
		finalData.put("msg", msg);
		response.setResponseData(finalData);
		}
		
		
		}
		 catch (ApplicationException appexe) {
			 response.addFormMessage(appexe.getMessage());
			} catch (Exception ex) {
				response.addFormError(ex.getMessage());
			}
		
		
		return response;
	}

	public Map<String, Object> updateEditProjectScopeDetail(ProjectScopeDetailModel projectScopeDetailModel) {
		// TODO Auto-generated method stub
		Map<String, Object> finalData = new HashMap<String, Object>();

        Boolean dataExists= this.projectScopeDetailDao.checkIfDataExists(projectScopeDetailModel.getProjectScopeID(),projectScopeDetailModel.getServiceAreaID(),projectScopeDetailModel.getDomainID(),projectScopeDetailModel.getTechnologyID());

        if(dataExists) {
    		finalData.put("isUpdated", false);
    		finalData.put("msg", "Data already exists for selected combination");
        }else {
			finalData.put("isUpdated", this.projectDao.updateEditProjectScopeDetail(projectScopeDetailModel));
			finalData.put("msg", "Deliverable Successfully Updated");
        }
		return finalData;
	}

	public void updateProject(ProjectsTableModel projectsTableModel) {

		 this.projectDao.inactiveDoc(
		 Integer.parseInt(projectsTableModel.getProjectID()));

		List<ProjectDocumentModel> projDocDetails = projectsTableModel.getProjectDocument();
		if (projDocDetails != null) {
			for (ProjectDocumentModel projDocMod : projDocDetails) {
				this.projectDao.insertDocDetails(projDocMod, Integer.parseInt(projectsTableModel.getProjectID()));
			}
		}

		Calendar cal = Calendar.getInstance();
		Calendar cal1 = Calendar.getInstance();
		if (projectsTableModel.getEndDate() != null) {
			cal.setTime(projectsTableModel.getEndDate());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			projectsTableModel.setEndDate(cal.getTime());
		}
		if (projectsTableModel.getStartDate() != null) {
			cal1.setTime(projectsTableModel.getStartDate());
			cal1.set(Calendar.HOUR_OF_DAY, 0);
			cal1.set(Calendar.MINUTE, 0);
			cal1.set(Calendar.SECOND, 0);
			cal1.set(Calendar.MILLISECOND, 0);
			projectsTableModel.setStartDate(cal1.getTime());
		}
		this.projectDao.updateProject(projectsTableModel);
		this.projectDao.updateProjectPM(projectsTableModel);
		if (projectsTableModel.getOperationalManager() != null) {
			this.projectDao.updateProjectApprover(projectsTableModel);
		}
	}

	public ResponseEntity<Response<Void>> addExternalApplicationReference(
			List<ExternalAppReferenceModel> externalRefModel) {
		Response<Void> response = new Response<>();
		try {
			LOG.info("Start Execution of addExternalApplicationReference Api");
			for (ExternalAppReferenceModel externalRef : externalRefModel) {
				ExternalAppReferenceModel extProjMapping = projectDao.checkIfExternalProjectExistForSameIsfproject(
						externalRef.getExternalProjectId(), externalRef.getSourceId(),externalRef.getProjectId());

				if (extProjMapping != null) {
					response = new Response<>();
					response.addFormError("External project " + externalRef.getExternalProjectId()
							+ " already mapped to ISF project " + externalRef.getProjectId() + ".");
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
				projectDao.addExternalApplicationReference(externalRef);
			}
			LOG.info("Stop Execution of addExternalApplicationReference Api");
			response.addFormMessage(SOURCE_ADDED_SUCCESSFULLY);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response.addFormMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public List<ExternalAppReferenceModel> getExternalApplicationReferencesByProjectId(int projectId) {
		return projectDao.getExternalApplicationReferencesByProjectId(projectId);
	}

	public void updateStatusOfExternalReference(boolean isActive, int referenceId) {
		projectDao.updateStatusOfExternalReference(isActive, referenceId);
	}

	public List<Map<String, Object>> getExternalProjects() {
		return projectDao.getExternalProjects();
	}

	public List<Map<String, Object>> getRequestType() {
		return this.projectDao.getRequestType();
	}

	public List<Map<String, Object>> getTools() {
		return this.projectDao.getTools();
	}

	public List<Map<String, Object>> getToolLicenseType() {
		return this.projectDao.getToolLicenseType();
	}

	public List<Map<String, Object>> getToolLicenseOwner() {
		return this.projectDao.getToolLicenseOwner();
	}

	public List<Map<String, Object>> getAccessMethod() {
		return this.projectDao.getAccessMethod();
	}

	public boolean saveProjectSpecificTools(ProjectSpecificToolModel projectToolModel) {
		boolean resoponse = this.projectDao.checkIFToolExists(projectToolModel);
		if (!resoponse) {
			this.projectDao.saveProjectSpecificTools(projectToolModel);
			resoponse = true;
		} else
			resoponse = false;
		return resoponse;
	}

	public Response<List<Map<String, Object>>> getProjectSpecificTools(Integer projectID,
			Integer isOnlyActiveRequired) {
		Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
		try {
			response.setResponseData(projectDao.getProjectSpecificTools(projectID, isOnlyActiveRequired));
		} catch (Exception e) {
			response.addFormError(e.getMessage());
		}
		return response;
	}

	public void updateProjectSpecificTools(ProjectSpecificToolModel projectToolModel) {
		this.projectDao.updateProjectSpecificTools(projectToolModel);

	}

	public void disableEnableTools(ProjectSpecificToolModel projectToolModel) {
		this.projectDao.disableEnableTools(projectToolModel);
	}

	public boolean saveDeliveryAcceptance(DeliveryAcceptanceModel deliveryAcceptanceModel) {

		boolean response = this.projectDao.checkIFUserExists(deliveryAcceptanceModel.getProjectID(),
				deliveryAcceptanceModel.getSignumID());
		if (!response) {
			this.projectDao.saveDeliveryAcceptance(deliveryAcceptanceModel);

		} else {
			boolean res = this.projectDao.checkIFUserIsActive(deliveryAcceptanceModel.getProjectID(),
					deliveryAcceptanceModel.getSignumID());
			if (!res) {
				this.projectDao.updateDeliveryAcceptance(deliveryAcceptanceModel);
				res = false;
			}
			response = res;
		}
		return response;
	}

	public void disableDeliveryAcceptance(Integer deliveryAcceptanceID, String signumID) {
		this.projectDao.disableDeliveryAcceptance(deliveryAcceptanceID, signumID);
	}

	public List<Map<String, Object>> getDeliveryAcceptance(int projectID) {
		return this.projectDao.getDeliveryAcceptance(projectID);
	}

	public boolean saveRPM(RPMModel rmpModel) {
		boolean response = this.projectDao.checkIFUserExistsRPM(rmpModel.getProjectID(), rmpModel.getRpmSignumID());
		if (!response) {
			this.projectDao.saveRPM(rmpModel);
		}
		return response;
	}

	public void disableRPM(Integer rpmID, String signumID) {
		this.projectDao.disableRPM(rpmID, signumID);
	}

	public List<Map<String, Object>> getRPM(int projectID) {
		return this.projectDao.getRPM(projectID);
	}

	/*
	 * //TODO code cleanup public List<Map<String, Object>> getDeliverableUnit() {
	 * // TODO Auto-generated method stub return
	 * this.projectDao.getDeliverableUnit(); }
	 */

	@Transactional("txManager")
	public Map<String, String> closeProject(int projectID, String signum) {
		int woCount = 0;

		Map<String, String> respMap = new HashMap<String, String>();

		LOG.info("Closing project for proj id-->:" + projectID);

		List<Integer> WOs = projectDao.getActiveWorkOrders(projectID);
		if (WOs != null && WOs.size() > 0 && WOs.get(0) != null && WOs.get(0) != null) {
			woCount = WOs.get(0);
		}

		LOG.info("Total Open Work Orders (Inprogress, onhold, assigned, reopened) for the project id is-->:" + woCount);
		if (woCount > 0) {
			respMap.put("IsApiSuccess", "false");
			respMap.put("msg", "There are total- " + woCount
					+ " Open Work Orders for this project, please close them first to close the project.");
		} else {
			List<String> resPosStaus = projectDao.getResPosStaus(projectID);
			LOG.info("Total PositionStatus of resources are-->" + resPosStaus.size());
			// Logic1:: if above query return PositionStatus= 'Cancelled' or 'Position
			// Completion' then we can close the proj else not-->
			// Logic1:: if PositionStatus not in 'Cancelled' or 'Position Completion' and
			// isActive=0 then we can close the proj else not-->
			if (resPosStaus != null) {
				boolean canCloseProject = false;
				if (resPosStaus.size() == 0)
					canCloseProject = true;
				else if (resPosStaus.size() == 1 && (resPosStaus.get(0).equalsIgnoreCase("Cancelled")
						|| resPosStaus.get(0).equalsIgnoreCase("Position Completion")))
					canCloseProject = true;
				else if (resPosStaus.size() == 2
						&& (resPosStaus.get(0).equalsIgnoreCase("Cancelled")
								&& resPosStaus.get(1).equalsIgnoreCase("Position Completion"))
						&& (resPosStaus.get(1).equalsIgnoreCase("Cancelled")
								&& resPosStaus.get(0).equalsIgnoreCase("Position Completion")))
					canCloseProject = true;
				else if (resPosStaus.size() > 2)
					canCloseProject = false;

				List<Integer> resPosStausActive = projectDao.getResPosStausActive(projectID);
				LOG.info("Total resPosStausActive of resources are-->" + resPosStausActive.size());
				if (resPosStausActive != null && resPosStausActive.size() > 0) {
					LOG.info("Total count of resPosStausActive of resources are-->" + resPosStausActive.get(0));
					if (resPosStausActive.get(0) > 0)
						canCloseProject = false;
					else
						canCloseProject = true;
				}

				if (canCloseProject) {
					int udpateCnt = projectDao.updateProjectStatus(projectID, "Closed", signum);

					LOG.info("Total rows updated for the project id is-->:" + udpateCnt);
					if (udpateCnt > 0) {
						respMap.put("IsApiSuccess", "true");
						respMap.put("msg", "Project updated successfully.");
					} else {
						respMap.put("IsApiSuccess", "false");
						respMap.put("msg", "Project cannot be updated now.");
					}
				} else {
					respMap.put("IsApiSuccess", "false");
					respMap.put("msg",
							"The project cannot be closed now as some positions are still open, please close positions first to close the project.");
				}
			}
		}

		// System.out.println("woCount---->"+woCount);

		return respMap;
	}

	@Transactional("transactionManager")
	public boolean saveDeliverableUnit(ProjectDeliverableUnitModel projectDeliverableUnitModel) {

		LOG.info("projectDeliverableUnitModel.getDeliverableUnitName()-->"
				+ projectDeliverableUnitModel.getDeliverableUnitName());
		boolean resoponse = this.projectDao.checkIFUnitExists(projectDeliverableUnitModel);
		// System.out.println("resoponse-->"+resoponse);
		if (!resoponse) {
			this.projectDao.saveProjectDelUnit(projectDeliverableUnitModel);
		}
		return resoponse;
	}

	public List<Map<String, Object>> getAllDeliverableUnit(String term) {
		// TODO Auto-generated method stub
		return this.projectDao.getAllDeliverableUnit(term);
	}

	@Transactional("transactionManager")
	public void deleteProjDelUnit(ProjectDeliverableUnitModel projectDeliverableUnitModel) {
		LOG.info("projectDeliverableUnitModel.getDeliverableUnitID()-->"
				+ projectDeliverableUnitModel.getDeliverableUnitID());
		this.projectDao.deleteProjDelUnit(projectDeliverableUnitModel);
	}

	@Transactional("transactionManager")
	public boolean checkIfProjectActive(Integer ProjectID) {

		boolean resoponse = this.projectDao.checkIfProjectActive(ProjectID);
		// System.out.println("resoponse-->"+resoponse);
		return resoponse;
	}

	@Transactional("transactionManager")
	public boolean EditDeliverableUnit(ProjectDeliverableUnitModel projectDeliverableUnitModel) {
		boolean response;
		if (projectDeliverableUnitModel.getFlag())
			response = false;
		else
			response = this.projectDao.checkIFUnitExists(projectDeliverableUnitModel);
		// System.out.println("resoponse-->"+resoponse);
		if (!response) {
			LOG.info("projectDeliverableUnitModel.getDeliverableUnitID()-->"
					+ projectDeliverableUnitModel.getDeliverableUnitID());
			this.projectDao.EditDeliverableUnit(projectDeliverableUnitModel);
		}
		return response;

	}

	public List<ProjectsTableModel> getProjectByProjectID(int projectID) {
		return projectDao.getProjectByProjectID(projectID);

	}

	public Response addExternalSource(ExternalSourceModel externalSource) {
		Response response = null;

		boolean checkIfSourceExists = projectDao.checkIfSourceExists(externalSource);
		if (!checkIfSourceExists) {
			projectDao.addExternalSource(externalSource);
			projectDao.addInternalSource(externalSource);
			this.sendEmailOnKeyGeneration(externalSource);

		} else {
			response = new Response();
			response.addFormError("External source " + externalSource.getSourceName() + " already exists in ISF");
			return response;
		}

		return response;
	}

	public void sendEmailOnKeyGeneration(ExternalSourceModel externalSource) {
		String createdBy = externalSource.getCreatedby();

		EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(createdBy);

		Map<String, Object> placeHolder = enrichTokenNotifcation(externalSource, eDetails);
		System.out.println("eDetails : " + eDetails);
		emailService.sendMail(AppConstants.EXTERNAL_REFERENCE_TOKEN_CREATION, placeHolder);
	}

	public Map<String, Object> enrichTokenNotifcation(ExternalSourceModel externalSource,
			EmployeeModel currentUserDetails) {

		EmployeeModel createrDetails = activityMasterDAO.getEmployeeBySignum(externalSource.getCreatedby());
		EmployeeModel referenceSponserDetails = activityMasterDAO
				.getEmployeeBySignum(externalSource.getReferenceSponser());
		EmployeeModel managerDetails = activityMasterDAO.getEmployeeBySignum(createrDetails.getManagerSignum());
		EmployeeModel referenceOwnerDetails = activityMasterDAO.getEmployeeBySignum(externalSource.getReferenceOwner());

		String referenceSponser = referenceSponserDetails.getEmployeeName() + " ("
				+ referenceSponserDetails.getSignum().toUpperCase() + ")";
		String createdBy = createrDetails.getEmployeeName() + " (" + createrDetails.getSignum().toUpperCase() + ")";
		String referenceOwner = referenceOwnerDetails.getEmployeeName() + " ("
				+ referenceOwnerDetails.getSignum().toUpperCase() + ")";
		String IFSTeamEmail = "ISFSupportTeam@ericsson.onmicrosoft.com";

		// String createdBy =createrDetails.getSignum();

		System.out.println("created by : " + createdBy);

		Map<String, Object> data = new HashMap<>();
		data.put(AppConstants.CURRENT_USER, currentUserDetails.getSignum());
		data.put("externalSourceName", externalSource.getSourceName().toUpperCase());
		data.put("referenceSponser", referenceSponser);
		data.put("createdBy", createdBy);
		data.put("referenceOwner", referenceOwner);

		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO,
				referenceOwnerDetails.getEmployeeEmailId() + ';' + referenceSponserDetails.getEmployeeEmailId() + ';'
						+ managerDetails.getEmployeeEmailId() + ';' + IFSTeamEmail);
		// data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO,
		// externalSource.getReferenceSponser());
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, createrDetails.getEmployeeEmailId());

		return data;
	}

	/**
	 * @param marketAreaID
	 * @return List<CountryModel>
	 */
	public List<CountryModel> getCountrybyMarketAreaID(Integer marketAreaID) {
		return this.projectDao.getCountrybyMarketAreaID(marketAreaID);
	}

	/**
	 * @param signum
	 * @param role
	 * @param marketArea
	 * @return List<ProjectDetailsModel>
	 */
	public List<ProjectDetailsModel> getProjectAndScopeDetailBySignum(String signum, String role, String marketArea) {
		List<ProjectDetailsModel> listOfProject = this.projectDao.getProjectAndScopeDetailBySignum(signum, role,
				marketArea);
		List<ProjectDetailsModel> finalListOfProject = new ArrayList<ProjectDetailsModel>();
		
		for(ProjectDetailsModel projectDetails : listOfProject) {
			List<ScopeDetailModel> scopeDetailList = projectDetails.getScopeDetails();
			List<ScopeDetailModel> scopeDetailNewList = new ArrayList<ScopeDetailModel>();
			for(ScopeDetailModel scopeDetailModel : scopeDetailList) {

				if (scopeDetailModel.getScopeActive()){
					if(scopeDetailModel.isHaveExecutionPlan()) {
						scopeDetailNewList.add(scopeDetailModel);
					}
				}
				projectDetails.setScopeDetails(scopeDetailNewList);
			}
			finalListOfProject.add(projectDetails);
		}
		return finalListOfProject;
	}

	/**
	 * @param ProjectID
	 * @return ProjectAllDetailsModel
	 */
	public ProjectAllDetailsModel getProjectDetails(int ProjectID) {
		return this.projectDao.getProjectDetails(ProjectID);
	}

	/**
	 * @param ProjectID
	 * @return List<ProjectDocumentModel>
	 */
	public List<ProjectDocumentModel> getProjectDocuments(int ProjectID) {
		return this.projectDao.getProjectDocuments(ProjectID);
	}

	/**
	 * @param marketAreaID
	 * @param countryID
	 * @param signum
	 * @param status
	 * @param marketArea
	 * @param role
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getAllProjects(String marketAreaID, String countryID, String signum, String status,
			String marketArea, String role) {
		return this.projectDao.getAllProjects(marketAreaID, countryID, signum, status, marketArea, role);
	}

	/**
	 * @param projectCreationModel
	 * @return int
	 */
	public int createProject(ProjectCreationModel projectCreationModel) {

		int projectID = this.projectDao.createProject(projectCreationModel);

		if(projectCreationModel.getProjectDocuments().size() != 0) {
			List<ProjectDocumentModel> projectDocumentList = projectCreationModel.getProjectDocuments();
			
			for(ProjectDocumentModel documents : projectDocumentList) {
				this.addProjectDocuments(Integer.toString(projectID), documents, projectCreationModel.getProjectCreator());
			}			
		}
		return projectID;			
	}
	
	public boolean addProjectDocuments(String projectId, ProjectDocumentModel projectDocument, String signum) {
		return this.projectDao.addProjectDocuments(projectId, projectDocument, signum);
	}
	
	public boolean haveExecutionPlan(int projectID, int scopeID) {
		return this.projectDao.haveExecutionPlan(projectID,scopeID);
	}
	
	public List<DeliveryResponsibleModel> getDeliveryResponsibleByProjectIDandSignum(int projectID, String signumID) {
		return projectDao.getDeliveryResponsibleByProjectIDandSignum(projectID,signumID);
	}
	
	public String getManagerByProjectId(String projectId) {
		return projectDao.getManagerByProjectId(projectId);
	}

	public Response<Boolean> validateScopeDetailForActivitySubActivity(List<ActivityModel> activityModelList) {
		Response<Boolean> res = new Response<>();
		boolean isValid = false;
		
		for(ActivityModel activityModel : activityModelList) {
			isValid = projectDao.validateScopeDetailForActivitySubActivity(activityModel);
			
			// if any combination of scope detail doesn't comply with Activity, don't allow them to update it
			if(!isValid) {
				res.addFormError("Scope detail is not valid for update");
				res.setResponseData(isValid);
				return res;				
			}
		}
		if(isValid) {
			res.addFormMessage("Scope detail is valid for update");
			res.setResponseData(isValid);
			
			return res;
		}else {
			res.addFormError("Scope detail is not valid for update");
			res.setResponseData(isValid);
			return res;
		}
	}
	
	
	public ResponseEntity<Response<List<WorkFlowFilterModel>>> getWorkFlowsByProjectID(int projectID, String term) {
		
		Response<List<WorkFlowFilterModel>> result = new  Response();
		try {
			LOG.info("getWorkFlowsByProjectID :Start");
			validationUtilityService.validateIntForZero(projectID, AppConstants.PROJECT_ID_2);
			List<WorkFlowFilterModel> workFlowList = this.projectDao.getWorkFlowsByProjectID(projectID, term);
			
			if (workFlowList.isEmpty()) {
				result.addFormMessage(NO_DATA_FOUND); 
				result.setResponseData(workFlowList);
			} else {
				result.setResponseData(workFlowList);
			}
			}
			catch(ApplicationException exe) {
				result.addFormError(exe.getMessage());
				return new ResponseEntity<Response<List<WorkFlowFilterModel>>>(result, HttpStatus.OK);
			}
			catch(Exception ex) {
				result.addFormError(ex.getMessage());
				return new ResponseEntity<Response<List<WorkFlowFilterModel>>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<Response<List<WorkFlowFilterModel>>>(result, HttpStatus.OK);
	}

	

	private void validateWorkFlowProficiencyModel(List<String> listOfSignum, List<Integer> listOfWfid, int projectID,
			String proficiencyStatus) {

		validationUtilityService.validateIntForZero(projectID, AppConstants.PROJECT_ID_2);
		validationUtilityService.validateStringForBlank(proficiencyStatus, PROFICIENCY_STATUS);
		listOfSignum = Optional.ofNullable(listOfSignum).map(ArrayList::new).filter(list -> !list.contains(null))
				.orElseThrow(() -> new ApplicationException(200, NULL_IS_NOT_VALID_SIGNUM));
		listOfWfid = Optional.ofNullable(listOfWfid).map(ArrayList::new)
				.filter(list -> !(list.contains(null) || list.contains(0)))
				.orElseThrow(() -> new ApplicationException(200, NULL_ZERO_IS_NOT_VALID_WORKFLOW_ID));
		if (CollectionUtils.isEmpty(listOfSignum) && CollectionUtils.isEmpty(listOfWfid)) {
			throw new ApplicationException(200, SIGNUM_AND_WORKFLOWID_BOTH_SHOULD_NOT_BE_BLANK);
		}
	}

	@Transactional("transactionManager")
	public ResponseEntity<Response<Void>> updateProficiency(List<WorkflowProficiencyModel> workFlowProficiencyModelList,
			int projectID) {
		Response<Void> result = new Response<>();
		try {
			validateWorkFlowProficiencyModelForUpdateProficiency(workFlowProficiencyModelList, projectID);
			
			int count = 0;
			for (WorkflowProficiencyModel workFlowProficiencyModel : workFlowProficiencyModelList) {
				
				this.projectDao.insertUserProficiencyOnUpdate(workFlowProficiencyModel, projectID);
				if (count == 0) {
					this.projectDao.updatePreviousAction(workFlowProficiencyModelList);
				}
				int wfUserProficenctID = isfCustomIdInsert
						.generateCustomId(workFlowProficiencyModel.getWfUserProficenctID());
				workFlowProficiencyModel.setWfUserProficenctID(wfUserProficenctID);
				
				projectDao.addProficiencyAction(workFlowProficiencyModel, projectID);
				count++;
				Map<String, Object> placeHolder = emailService.enrichMailforUpdateProficiency(workFlowProficiencyModel, true);
				emailService.sendMail(AppConstants.NOTIFICATION_UPDATE_PROFICIENCY, placeHolder);
			}
			result.addFormMessage(PROFICIENCY_UPDATED_SUCCESSFULLY);

		}

		catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			throw ex;
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	private void validateWorkFlowProficiencyModelForUpdateProficiency(
			List<WorkflowProficiencyModel> workFlowProficiencyModelList, int projectID) {
		validationUtilityService.validateIntForZero(projectID, AppConstants.PROJECT_ID_2);
		if(CollectionUtils.isNotEmpty(workFlowProficiencyModelList)) {
			for(WorkflowProficiencyModel workFlowProficiency:workFlowProficiencyModelList) {
				validationUtilityService.validateIntForZero(workFlowProficiency.getWorkFlowId(), AppConstants.WORKFLOW_ID);
				validationUtilityService.validateStringForBlank(workFlowProficiency.getSignum(), AppConstants.SIGNUM);
				validationUtilityService.validateStringForBlank(workFlowProficiency.getModifiedBy(), MODIFIED_BY);
				validationUtilityService.validateIntForZero(workFlowProficiency.getSubActivityId(), AppConstants.SUBACTIVITY_ID);
				validationUtilityService.validateStringForBlank(workFlowProficiency.getTriggeredBy(), AppConstants.TRIGGER_BY);
				validationUtilityService.validateStringForBlank(workFlowProficiency.getWorkFlowName(), AppConstants.WORKFLOW_NAME);
			}
			validateLatestStatusforUserProficiency(workFlowProficiencyModelList,  projectID);
		}else {
			throw new ApplicationException(200, MSG_EMPTY_LIST);
		}
		
	}

	private void validateLatestStatusforUserProficiency(List<WorkflowProficiencyModel> workFlowProficiencyModelList,
			int projectID) {
		String proficiency= StringUtils.EMPTY;;
		String userSignumandName = StringUtils.EMPTY;
		String modifiedBySignumandName =  StringUtils.EMPTY;
		if(CollectionUtils.isNotEmpty(workFlowProficiencyModelList)) {
			for(WorkflowProficiencyModel workFlowProficiency:workFlowProficiencyModelList) {
				WorkflowProficiencyModel latestworkflowProficiencyofUser = flowChartDao.getLatestProficiencyofUser(
						workFlowProficiency,workFlowProficiency.getSignum() );
				Boolean checkStatus = StringUtils.equalsIgnoreCase(workFlowProficiency.getProficiencyLevel(), latestworkflowProficiencyofUser.getProficiencyLevel());
				if(!checkStatus) {
					if(StringUtils.equalsIgnoreCase(workFlowProficiency.getProficiencyLevel(), AppConstants.ASSESSED)) {
						proficiency= "Upgrade";
					}
					else if (StringUtils.equalsIgnoreCase(workFlowProficiency.getProficiencyLevel(), AppConstants.EXPERIENCED)) {
						proficiency= "Downgrade";
					}
					if(StringUtils.isNotEmpty(workFlowProficiency.getSignum())) {
					 userSignumandName = activityMasterDAO.getNameandSignum(workFlowProficiency.getSignum());
					}
					if(StringUtils.isNotEmpty(latestworkflowProficiencyofUser.getModifiedBy())) {
						modifiedBySignumandName = activityMasterDAO.getNameandSignum(latestworkflowProficiencyofUser.getModifiedBy());
					}
				
					throw new ApplicationException(200, "The action to "+proficiency+ " for user "+ userSignumandName +" is already taken by "+ modifiedBySignumandName+ ". Please refresh the data.")	;
				}
				
			}
		
	}
}

	public Map<String, Object> getEFWorkflowForSignumWFID(List<String> listOfSignum,
			List<Integer> listOfWfid, int projectID, DataTableRequest dataTableRequest, String proficiencyStatus) {
		Map<String, Object> map = new HashMap<>();
		try {
			validateWorkFlowProficiencyModel(listOfSignum,listOfWfid, projectID,proficiencyStatus);
//			listOfSignum.removeIf(Objects::isNull);
//			listOfWfid.removeIf(Objects::isNull);
//			listOfWfid.removeIf(x->x==0);
			List<WorkflowProficiencyModel> listofWorkflowProficiency = this.projectDao
					.getEFWorkflowForSignumWFID(listOfSignum, listOfWfid, projectID,dataTableRequest,StringUtils.trim(proficiencyStatus.toLowerCase()));
			map.put("data", listofWorkflowProficiency);
			if (!listofWorkflowProficiency.isEmpty()) {
				map.put(RECORDS_TOTAL, listofWorkflowProficiency.get(0).getRecordsTotal());
				map.put(RECORDS_FILTERED, listofWorkflowProficiency.get(0).getRecordsTotal());
			} else {
				map.put(RECORDS_TOTAL, 0);
				map.put(RECORDS_FILTERED, 0);
			}
		} catch (ApplicationException appexe) {
			map.put("errormsg", appexe.getMessage());
		} catch (Exception ex) {
			map.put("errormsg", ex.getMessage());
		}
		return map;

	}
	
	
	@Transactional("transactionManager")
	public ResponseEntity<Response<List<Map<String, Object>>>> getScopeType(int deliverableUnit) {
		
		Response<List<Map<String, Object>>> response = new Response<>();
 
	    try {
			LOG.info("getScopeType:Start");
			
			validationUtilityService.validateIntForZero(deliverableUnit, DELIVERABLE_UNIT);
			
			List<Map<String, Object>> listOfScopeType=new ArrayList<>();
			
			boolean isScopeTypeRequired=this.projectDao.isScopeTypeRequired(deliverableUnit);
			if(Boolean.TRUE.equals(isScopeTypeRequired)) {
				listOfScopeType= this.projectDao.getScopeType();
			}
			
			if (listOfScopeType.isEmpty()) {
				response.addFormMessage("is_ScopeType_Required = false");
			} else {
				response.addFormMessage("is_ScopeType_Required = true");
				response.setResponseData(listOfScopeType);
			}

			LOG.info("getScopeType:End");
			
		}
		catch(ApplicationException exe) {
			response.addFormMessage(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	
	
	@Transactional("transactionManager")
	public ResponseEntity<Response<List<Map<String, Object>>>> getMethodForDU(int deliverableUnit) {
		
		Response<List<Map<String, Object>>> response = new Response<>();
 
	    try {
			LOG.info("getMethodForDU:Start");
			
			validationUtilityService.validateIntForZero(deliverableUnit, DELIVERABLE_UNIT);
			
			List<Map<String, Object>> listOfMethodForDU=new ArrayList<>();
			
			boolean isMethodRequired=this.projectDao.isMethodRequired(deliverableUnit);
			if(Boolean.TRUE.equals(isMethodRequired)) {
				listOfMethodForDU= this.projectDao.getMethodForDU();
			}
			
			if (listOfMethodForDU.isEmpty()) {
				response.addFormMessage("is_Method_Required = false");

			} else {
				response.addFormMessage("is_Method_Required = true");
				response.setResponseData(listOfMethodForDU);
			}

			LOG.info("getMethodForDU:End");
			
		}
		catch(ApplicationException exe) {
			response.addFormMessage(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	@Transactional("transactionManager")
	public ResponseEntity<Response<List<Map<String, Object>>>> getOperatorCount(int deliverableUnit) {
		
		Response<List<Map<String, Object>>> response = new Response<>();
 
	    try {
			LOG.info("getOperatorCount:Start");
			
			validationUtilityService.validateIntForZero(deliverableUnit, DELIVERABLE_UNIT);
			
			List<Map<String, Object>> listOfOperatorCount=new ArrayList<>();
		
			boolean isOperatorCountRequired=this.projectDao.isOperatorCountRequired(deliverableUnit);
			if(Boolean.TRUE.equals(isOperatorCountRequired)) {
				listOfOperatorCount= this.projectDao.getOperatorCount();
			}
						
			if (listOfOperatorCount.isEmpty()) {
				response.addFormMessage("is_OperatorCount_Required = false");

			} else {
				response.addFormMessage("is_OperatorCount_Required = true");
				response.setResponseData(listOfOperatorCount);
			}

			LOG.info("getOperatorCount:End");
			
		}
		catch(ApplicationException exe) {
			response.addFormMessage(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	@Transactional("transactionManager")
	public ResponseEntity<Response<List<Map<String, Object>>>> getProjectFinancials(int deliverableUnit) {
		
		Response<List<Map<String, Object>>> response = new Response<>();
 
	    try {
			LOG.info("getProjectFinancials:Start");
			
			validationUtilityService.validateIntForZero(deliverableUnit, DELIVERABLE_UNIT);
			
			List<Map<String, Object>> listOfProjectFinancials=new ArrayList<>();
		
			boolean isProjectFinancialsRequired=this.projectDao.isProjectFinancialsRequired(deliverableUnit);
			if(Boolean.TRUE.equals(isProjectFinancialsRequired)) {
				listOfProjectFinancials= this.projectDao.getProjectFinancials();
			}
						
			if (listOfProjectFinancials.isEmpty()) {
				response.addFormMessage("is_ProjectFinancials_Required = false");

			} else {
				response.addFormMessage("is_ProjectFinancials_Required = true");
				response.setResponseData(listOfProjectFinancials);
			}

			LOG.info("getProjectFinancials:End");
			
		}
		catch(ApplicationException exe) {
			response.addFormMessage(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public DownloadTemplateModel downloadDeliverableData(Integer projectId, String deliverableStatus) throws IOException {
		
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = "ISF-Project View" + "-" + (new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()))
				+ ".xlsx";
        
		
		 List<String> deliverableStatusList = new ArrayList<String>();
    	 if(StringUtils.isNotBlank(deliverableStatus)) {
        	 deliverableStatusList = Arrays.asList(deliverableStatus.trim().split("\\s*,\\s*"));
    	 }
    	 
		List<Map<String, Object>> deliverableData = this.projectDao.downloadDeliverableData(projectId,deliverableStatusList);
		
		byte[] fData = null;
		if (CollectionUtils.isNotEmpty(deliverableData)) {
			fData = FileUtil.generateXlsFile(deliverableData);
			
		}
		
		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);
		

		return downloadTemplateModel;
	}
	
	public List<AdhocTypeModel> getAdhocTypes(String signum, String role, String marketArea, String activity) {
		
		if (StringUtils.equalsIgnoreCase(role, "Default User")) {

			// getting project data for ne
			List<TblProjects> finalListOfProject = adhocActivityService.getProjectsOfUser(signum);

			List<AdhocTypeModel> finalListOfProjectAndInternal = new ArrayList<>();

			List<AdhocTypeModel> listOfIDAndTypeOfActivity = this.projectDao.getIDAndTypeOfActivity(activity);
			
			for (AdhocTypeModel adhocTypeModel : listOfIDAndTypeOfActivity) {

				if (StringUtils.equalsIgnoreCase(adhocTypeModel.getType(), "project")) {
					for (TblProjects projectDetails : finalListOfProject) {
						AdhocTypeModel adhocTypeDetails = new AdhocTypeModel();
						adhocTypeDetails.setType(projectDetails.getProjectId() + "-" + projectDetails.getProjectName());
						adhocTypeDetails.setId(adhocTypeModel.getId());
						finalListOfProjectAndInternal.add(adhocTypeDetails);
					}
				}

				if (StringUtils.equalsIgnoreCase(adhocTypeModel.getType(), "internal")) {
					AdhocTypeModel adhocTypeDetails = new AdhocTypeModel();
					adhocTypeDetails.setType(adhocTypeModel.getType());
					adhocTypeDetails.setId(adhocTypeModel.getId());
					finalListOfProjectAndInternal.add(adhocTypeDetails);
				}
			}
			return finalListOfProjectAndInternal;
		}

		else {

			// getting project data for others
			List<ProjectDetailsModel> finalListOfProject = getProjectAndScopeDetailBySignum(signum, role, marketArea);

			List<AdhocTypeModel> finalListOfProjectAndInternal = new ArrayList<>();

			List<AdhocTypeModel> listOfIDAndTypeOfActivity = this.projectDao.getIDAndTypeOfActivity(activity);
			for (AdhocTypeModel adhocTypeModel : listOfIDAndTypeOfActivity) {
				if (StringUtils.equalsIgnoreCase(adhocTypeModel.getType(), "project")) {
					for (ProjectDetailsModel projectDetails : finalListOfProject) {
						AdhocTypeModel adhocTypeDetails = new AdhocTypeModel();
						adhocTypeDetails.setType(projectDetails.getProjectID() + "-" + projectDetails.getProjectName());
						adhocTypeDetails.setId(adhocTypeModel.getId());
						finalListOfProjectAndInternal.add(adhocTypeDetails);
					}
				}

				if (StringUtils.equalsIgnoreCase(adhocTypeModel.getType(), "internal")) {
					AdhocTypeModel adhocTypeDetails = new AdhocTypeModel();
					adhocTypeDetails.setType(adhocTypeModel.getType());
					adhocTypeDetails.setId(adhocTypeModel.getId());
					finalListOfProjectAndInternal.add(adhocTypeDetails);
				}
			}
			return finalListOfProjectAndInternal;
		}
	}
	
	
}
