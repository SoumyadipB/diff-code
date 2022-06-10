/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
package com.ericsson.isf.mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.ericsson.isf.model.ActivityMasterModel;
import com.ericsson.isf.model.AdhocBookingProjectModel;
import com.ericsson.isf.model.AllocatedResourceModel;
import com.ericsson.isf.model.CountryModel;
import com.ericsson.isf.model.CustomerModel;
import com.ericsson.isf.model.DacModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DeleteNEListModel;
import com.ericsson.isf.model.DesktopInformationModel;
import com.ericsson.isf.model.DomainModel;
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
import com.ericsson.isf.model.MailModel;

import com.ericsson.isf.model.NetworkElementModel;
import com.ericsson.isf.model.NodeNamesByFilterModel;
import com.ericsson.isf.model.NotificationLogModel;
import com.ericsson.isf.model.ProjectApprovalModel;
import com.ericsson.isf.model.ProjectFilterModel;
import com.ericsson.isf.model.ResourceRequestCertificationModel;
import com.ericsson.isf.model.ResourceRequestCompetenceModel;
import com.ericsson.isf.model.ResourceStatusModel;
import com.ericsson.isf.model.ScopeDetailsModel;
import com.ericsson.isf.model.ServeAreaModel;
import com.ericsson.isf.model.ServiceAreaModel;
import com.ericsson.isf.model.ShiftTimmingModel;
import com.ericsson.isf.model.ShiftTimmingModel2;
import com.ericsson.isf.model.Signum;
import com.ericsson.isf.model.SpocModel;
import com.ericsson.isf.model.StandardActivityModel;
import com.ericsson.isf.model.TaskModel;
import com.ericsson.isf.model.TaskToolModel;
import com.ericsson.isf.model.TechnologyModel;
import com.ericsson.isf.model.TimeZoneModel;
import com.ericsson.isf.model.UserFeedbackModel;
import com.ericsson.isf.model.VendorModel;
import com.ericsson.isf.model.WFStepInstructionModel;
import com.ericsson.isf.model.WorkEffortModel;
import com.ericsson.isf.model.WorkFlowFeedbackActivityModel;
import com.ericsson.isf.model.WorkFlowFeedbackModel;
import com.ericsson.isf.model.WorkFlowFilterModel;
import com.ericsson.isf.model.WorkInstructionModel;
import com.ericsson.isf.model.botstore.TblAdhocBooking;

/**
 *
 * @author ekarath
 *
 */
public interface ActivityMasterMapper {

	public List<Map<String, Object>> getSubScopebyScopeID(@Param("projectScopeID") String projectScopeID);

	public List<Map<String, Object>> getActivitiesBySubScopeId(@Param("projectScopeDetailID") int projectScopeDetailID);

	public void saveDomainDetails(@Param("domainModel") DomainModel domainModel);

	public List<DomainModel> getDomainDetails(@Param("ProjectID") Integer ProjectID,
			@Param("ServiceAreaID") Integer ServiceAreaID);

	public List<DomainModel> getDomainDetailsBySignum(@Param("signum") String signum, @Param("ProjectID") Integer ProjectID,
			@Param("ServiceAreaID") Integer ServiceAreaID);

	public List<HashMap<String, Object>> getAllProjectsBySignum(@Param("signum") String signum);

	public List<DomainModel> getAllDomainDetailsByService(@Param("ServiceAreaID") Integer ServiceAreaID);

	public void updateDomainDetails(@Param("domainModel") DomainModel domainModel);

	public boolean updateWorkEffort(@Param("updateWorkEffort") AllocatedResourceModel allocatedResource,
			@Param("active") int active);

	public boolean updateWorkEffortStatus(@Param("weid") int weid);

	public List<WorkEffortModel> getActiveWorkEffortsByRpid(@Param("rpID") int rpID);

	public List<WorkEffortModel> getWeidWithMinSdate(@Param("rpID") int rpID, @Param("isactive") int activeFlag);

	public List<WorkEffortModel> getWeidWithMinSdateAll(@Param("rpID") int rpID);

	public List<WorkEffortModel> getWorkEffortsDetailsByRpID(@Param("rpID") int rpID);

	public List<WorkEffortModel> getWeidWithMaxEdate(@Param("rpID") int rpID);

	public List<ServiceAreaModel> getServiceAreaDetailsBySignum(@Param("signum") String signum,
			@Param("ProjectID") Integer ProjectID, @Param("ServAreaID") Integer ServAreaID);

	public boolean updateResourcePosition(@Param("updateWorkEffort") AllocatedResourceModel allocatedResource);

	public boolean updateRpPositionStatus(@Param("status") String status, @Param("rpID") int rpID);

	public boolean updateWorkEffortByWeIDReject(@Param("updateWorkEffort") AllocatedResourceModel allocatedResource);

	public boolean deleteBookedHoursByWeID(@Param("updateWorkEffort") AllocatedResourceModel allocatedResource);

	public boolean updateResourcePositionToProposed(@Param("weid") int weid, @Param("loggedInUser") String loggedInUser,
			@Param("status") String status);

	public Map<String, Object> getProjectDetailsByWeid(@Param("weid") int weid);

	public void saveServiceAreaDetails(@Param("serviceAreaModel") ServiceAreaModel serviceAreaModel);

	public void updateServiceAreaDetails(@Param("serviceAreaModel") ServiceAreaModel serviceAreaModel);

	public List<ServiceAreaModel> getServiceAreaDetails(@Param("ProjectID") Integer ProjectID,
			@Param("ServAreaID") Integer ServAreaID);

	public void saveTechnologyDetails(@Param("technologyModel") TechnologyModel technologyModel);

	public void updateTechnologyDetails(@Param("technologyModel") TechnologyModel technologyModel);

	public List<TechnologyModel> getTechnologyDetails(@Param("domainID") Integer domainID,
			@Param("projectID") Integer projectID);

	public List<TechnologyModel> getTechnologyDetailsByDomain(@Param("domainID") Integer domainID,
			@Param("ServiceAreaID") Integer ServiceAreaID);

	public void saveActivityAndSubActivityDetails(
			@Param("standardActivityModel") StandardActivityModel standardActivityModel);

	public void updateActivityAndSubActivityDetails(
			@Param("standardActivityModel") StandardActivityModel standardActivityModel);

	public List<StandardActivityModel> getActivityAndSubActivityDetails(@Param("domainID") int domainID,
			@Param("serviceAreaID") int serviceAreaID, @Param("technologyID") int technologyID);

	public void saveTaskDetails(@Param("taskModel") TaskModel taskModel);

	public List<TaskModel> getTaskDetails(@Param("subActivityID") int subActivityID,
			@Param("signumID") String signumID);

	public void updateTaskDetails(@Param("taskModel") TaskModel taskModel);

	public void updateTaskToolDetails(@Param("taskToolModel") TaskToolModel taskToolModel);

	public List<TaskModel> getTaskToolDetails();

	public void mapTaskAndTool(@Param("taskToolModel") TaskToolModel taskToolModel);

	public void updateTaskToolMapping(@Param("taskToolModel") TaskToolModel taskToolModel);

	public void deleteDomainDetails(@Param("domainID") int domainID, @Param("signumID") String signumID);

	public void deleteServiceArea(@Param("serviceAreaID") int serviceAreaID, @Param("signumID") String signumID);

	public void deleteTechnology(@Param("technologyID") int technologyID, @Param("signumID") String signumID);

	public void deleteActivityAndSubActivity(@Param("subActivityID") int subActivityID,
			@Param("signumID") String signumID);

	public void deleteTaskDetails(@Param("taskID") int taskID, @Param("signumID") String signumID);

	public void deleteTaskToolMapping(@Param("taskToolID") int taskToolID, @Param("signumID") String signumID);

	public List<DomainModel> getDomainDetailsByID(@Param("domainID") int domainID, @Param("signumID") String signumID);

	public List<ServiceAreaModel> getServiceAreaDetailsByID(@Param("serviceAreaID") int serviceAreaID,
			@Param("signumID") String signumID);

	public List<TechnologyModel> getTechnologyDetailsByID(@Param("technologyID") int technologyID,
			@Param("signumID") String signumID);

	public List<StandardActivityModel> getActivityAndSubActivityDetailsByID(@Param("subActivityID") int technologyID,
			@Param("signumID") String signumID);

	public List<ActivityMasterModel> getAutoComDetails(@Param("technologyID") int technologyID,
			@Param("serviceAreaID") int serviceAreaID);

	public List<TaskModel> getTaskToolDetailsByID(@Param("taskID") int taskID, @Param("signumID") String signumID);

	public List<TaskModel> getTaskDetailsByID(@Param("taskID") int taskID, @Param("signumID") String signumID);

	public boolean isDomainExits(@Param("domain") String domain, @Param("subDomain") String subDomain);

	public boolean isTaskExists(@Param("task") String task, @Param("subActivityID") int subActivityID);

	public boolean isServiceAreaExists(@Param("serviceArea") String serviceArea,
			@Param("subServiceArea") String subServiceArea);

	public boolean isTechnologyExists(@Param("technology") String technology);

	public boolean isSubActivtyExists(@Param("standardActivityModel") StandardActivityModel standardActivityModel);

	public boolean isEmployeeExists(@Param("signumID") String signumID);

	public boolean isEmployeeByManagerExists(@Param("managerSignum") String managerSignum);

	public boolean isProjectApprovalByProjectIdExists(@Param("projectID") int id);

	public boolean isCertificateExists(@Param("certificateId") int certificateId);

	public boolean isCompetenceExists(@Param("competenceId") int competenceId);

	public boolean isResourceRequestExists(@Param("resourceRequestId") int resourceRequestId);

	public boolean isProjectApprovalByApproverExists(@Param("approverSignum") String approverSignum);

	public EmployeeModel getEmployeeBySignum(@Param("signumID") String signum);

	public EmployeeModel getAspDetailsBySignum(@Param("signum") String signum);

	public EmployeeModel getEmployeeByEmail(@Param("email") String email);

	public List<EmployeeBasicDetails> getEmployeeDetails();

	public List<EmployeeModel> getEmployeeByProject(@Param("projectID") int projectID);

	public List<EmployeeModel> getEmployeesByManager(@Param("managerSignum") String managerSignum);

	public List<HashMap<String, String>> getAllLineManagers();

	public List<ProjectApprovalModel> getProjectApprovalDetailsById(@Param("projectID") int id);

	public List<ProjectApprovalModel> getProjectApprovalsByApprover(@Param("approverSignum") String approverSignum,
			@Param("marketArea") String marketArea);

	public int updateProjectApprovalDetails(@Param("projectApprovalModel") ProjectApprovalModel projectApprovalModel,@Param("Signum") String Signum);

	public int activateOpportunity(@Param("projectId") int projectId);

	public int updateProjectByApproval(@Param("projectID") int projectId);

	public int updateResourceRequestCertification(
			@Param("resourceRequestCertificationModel") ResourceRequestCertificationModel resourceRequestCertificationModel);

	public int updateResourceRequestCompetence(
			@Param("resourceRequestCompetenceModel") ResourceRequestCompetenceModel resourceRequestCompetenceModel);

	public int addCertificationByRRID(
			@Param("resourceRequestCertificationModel") ResourceRequestCertificationModel resourceRequestCertificationModel);

	public int addCompetenceByRRID(
			@Param("resourceRequestCompetenceModel") ResourceRequestCompetenceModel resourceRequestCompetenceModel);

	public List<NetworkElementModel> searchNetworkElementDetails(
			@Param("networkElementModel") NetworkElementModel networkElementModel);

	public List<NetworkElementModel> getNetworkElementDetails(@Param("projectID") int projectID,
			@Param("dataTableReq") DataTableRequest dataTableReq,RowBounds rowBounds); 

	public int getNetworkElementDetailsCount(@Param("projectID") int projectID,
			@Param("dataTableReq") DataTableRequest dataTableReq);

	public void deleteNetworkElementDetails(@Param("networkElementID") int networkElementID,
			@Param("signumID") String signumID);

	public void deleteVendorDetails(@Param("vendorID") int vendorID, @Param("signumID") String signumID);

	public void saveProjectDefinedTasks(
			@Param("flowChartStepDetailsModel") FlowChartStepDetailsModel flowChartStepDetailsModel);

	public List<VendorModel> getVendorDetails();

	public List<VendorModel> getVendorDetailsByID(@Param("vendorID") int vendorID, @Param("signumID") String signumID);

	public void updateVendorDetails(@Param("vendorModel") VendorModel vendorModel);

	public void saveVendorDetails(@Param("vendorModel") VendorModel vendorModel);

	public boolean isVendorExists(@Param("vendor") String vendor);

	public List<TaskModel> getTaskRelatedDetails(@Param("taskID") String taskID,
			@Param("subActivityID") String subActivityID, @Param("projectID") String projectID);

	public List<ActivityMasterModel> getSubActivityForActivity(@Param("domainID") int domainID,
			@Param("technologyID") int technologyID, @Param("serviceAreaID") int serviceAreaID,
			@Param("activityName") String activityName);

	public List<Map<String, Object>> getActivitySubActivityByProjectId(@Param("ProjectID") Integer projectID);

	public Map<String, Object> getDetails(@Param("rpID") int rpID);

	public List<ServeAreaModel> getServeArea(@Param("projectID") Integer projectID);

	public List<HashMap<String, Object>> getServiceAreaDetailsByProject(@Param("projectID") Integer projectID);

	public List<HashMap<String, Object>> getCustomerDetailsByMA(@Param("countryID") Integer countryID,
			@Param("marketAreaID") Integer marketAreaID);

	public boolean insertBookedResource(@Param("map") Map<String, Object> map);

	public List<Map<String, Object>> getActivitySubActivityByProject_V3(@Param("domainID") int domainID,
			@Param("technologyID") int technologyID, @Param("serviceAreaID") int serviceAreaID,
			@Param("ProjectID") int projectID);

	public List<Map<String, Object>> getActivitySubActivityByProject_V2(@Param("domainID") int domainID,
			@Param("technologyID") int technologyID, @Param("serviceAreaID") int serviceAreaID,
			@Param("ProjectID") int projectID);

	public List<Map<String, Object>> getTechnologyDetailsBySignum(@Param("signum") String signum);

	public List<Map<String, Object>> getEmployeeDetailsBySignum(@Param("signum") String signum);

	public List<Map<String, Object>> getActivityByProjectId(@Param("ProjectID") int projectID);

	public List<TaskModel> viewTaskDetails(@Param("projectID") int projectID,
			@Param("subActivityID") int subActivityID);

	public int addDomainSpoc(@Param("spocRequest") SpocModel spocRequest);

	public SpocModel getDomainSpoc(@Param("spocRequest") SpocModel spocRequest);

	public List<Map<String, Object>> getAllDomainSpoc();

	public void insertProposedWorkEfforts(@Param("workEffortID") int workEffortID, @Param("hours") float hours,
			@Param("duration") int duration, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	public void updateWorkEffortByWeIDByProposedWorkEffort(@Param("wfDetails") Map<String, Object> wfDetails);

	public void deleteWorkEffortFromProposed(@Param("weid") int weid);

	public TaskModel getTaskDetailsFromMaster(@Param("subActivityID") int subactivityID, @Param("taskID") int taskID);

	public void updateWorkEffortPositionStatusById(@Param("workEffortID") int workEffortID,
			@Param("positionStatus") String positionStatus);

	public List<Integer> getProjectIDForSubActivity(@Param("subActivityID") int subActivityID);

	public boolean isTaskExistsInProjConfTable(@Param("taskID") int taskID, @Param("subActivityID") int subActivityID,
			@Param("projectID") int projectID);

	public void updateProjConfTable(@Param("projectID") int projectID, @Param("taskModel") TaskModel taskModel);

	public Map<String, Object> getMaxTasksValueByProject(@Param("projectID") int projectID);

	public void insertMaxTasksValueByProject(@Param("projectID") int projectID, @Param("signum") String signum);

	public boolean updateMaxTasksValueByProject(@Param("projectID") int projectID, @Param("signum") String signum,
			@Param("maxManual") int maxManual, @Param("maxAutomaic") int maxAutomaic);

	public List<Map<String, Object>> downloadNetworkElement(@Param("projectID") int projectID);

	public Map<String, Object> getTaskDetailForSID(@Param("subActivityID") int subActivityID,
			@Param("task") String taskOrParentTaskMapped);

	public Map<String, String> getToolDetailForWF(@Param("toolName") String toolName);

	public List<CustomerModel> getCustomerDetails();

	public void saveCustomerDetails(@Param("customerModel") CustomerModel customerModel);

	public void saveCustomerCountryMappimg(@Param("customerModel") CustomerModel customerModel);

	public CustomerModel getCustMod(@Param("customerModel") CustomerModel customerModel);

	public List<EmployeeBasicDetails> getEmployeesByFilter(@Param("term") String term, RowBounds rowBounds);
	public List<EmployeeBasicDetails> getEmployeesByFilter1(@Param("term") String term, RowBounds rowBounds);

	public int getProjectIdByRpID(@Param("rpID") int rpID);

	public List<WorkInstructionModel> getWorkInstruction();

	public void saveWorkInstruction(@Param("workInstructionModel") WorkInstructionModel workInstructionModel1);

	public void deleteWorkInstruction(@Param("wIID") int wIID, @Param("signumID") String signumID,
			@Param("active") boolean active);

	public void editPrevWorkInstruction(@Param("wIID") int wIID, @Param("modifiedBy") String modifiedBy);

	public List<WorkInstructionModel> getActiveWorkInstruction(@Param("domainID") Integer domainID,
			@Param("vendorID") Integer vendorID, @Param("technologyID") Integer technologyID);

	public AdhocBookingProjectModel getAdhocBookingForProject(@Param("signum") String signum);
	//v1/getAdhocBookingForProject
	public Map<String, Object> getAdhocBookingForProjectV1(@Param("signum") String signum);

	public void saveLeavePlan(@Param("leavePlanModel") LeavePlanModel leavePlanModel);

	public List<LeavePlanModel> getLeavePlanBySignum(@Param("signum") String signum);

	public void deleteLeavePlan(@Param("signum") String signum, @Param("leavePlanID") int leavePlanID);

	public List<ShiftTimmingModel2> getShiftTimmingBySignumAndDate(@Param("signum") String signum, @Param("startDate") String startDate);

	public List<TimeZoneModel> getTimeZones();

	public void deleteShiftTimmingBySignum(@Param("signum") String signum, @Param("startDate") String startDate);

	public void saveshiftTimming(@Param("signum") String signum,
			@Param("shiftTimmingModel") ShiftTimmingModel shiftTimmingModel);

	public boolean updateResourcePositionToRejected(@Param("weid") int weid, @Param("loggedInUser") String loggedInUser,
			@Param("status") String status);

	public List<String> getNodeTypeByFilter(
			@Param("nodeNamesByFilterModel") NodeNamesByFilterModel nodeNamesByFilterModel);

	public List<ScopeDetailsModel> getScopeDetailsById(@Param("projectScopeDetailID") String projectScopeDetailID);

	public void appendToPositionComments(@Param("comments") String comments, @Param("positionId") int positionId, @Param("signum") String signum);

	public List<ShiftTimmingModel2> getShiftTimmingByDate(@Param("signum") String signum,
			@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("timeZone") String timeZone);

	public boolean deleteShiftTimmingByID(@Param("signum") String signum, @Param("shiftId") int shiftId);

	public void deleteCustomerDetails(@Param("customerID") int customerID, @Param("signum") String signum);

	public void editCustomerName(@Param("customerModel") CustomerModel customerModel);

	public WorkInstructionModel downloadWorkInstructionFile(@Param("wIID") String wIID);

	public List<UserFeedbackModel> getUserFeedBack();

	public List<HashMap<String, Object>> getEssLeave();

	public List<String> getNodeTypeByDeliverableId(@Param("projectID") int projectID,
			@Param("deliverableId") int deliverableId, @Param("elementType") String elementType);

	public String getPositionStatusByID(@Param("weid") int weid);

	public List<String> getNodes(@Param("countryCustomerGroupID") int countryCustomerGroupID, @Param("elementType") String elementType);

	public String getNearestNextShiftStartDate(@Param("signum") String signum, @Param("prevShiftStartDate") String prevShiftStartDate);

	public ShiftTimmingModel2 getNearestPreviousShift(@Param("signum") String signum, @Param("endDate") String endDate);

    public List<CountryModel> getCountries();

	public ScopeDetailsModel getScopeByScopeId(@Param("projectScopeID")int projectScopeID);

	public String changeEmployeeStatus(@Param("resourceStatusModel")ResourceStatusModel resourceStatusModel);

	public List<ScopeDetailsModel> getScopeDetailsByScopeId(@Param("projectScopeId") String projectScopeId);

	public List<ShiftTimmingModel2> getShiftTimmingBySignum(@Param("signum") String signum, @Param("startDate") String startDate);

	public void saveGlobalUrl(@Param("globalUrlModel") GlobalUrlModel globalUrlModel);

	public void updateGlobalUrl(@Param("globalUrlModel") GlobalUrlModel globalUrlModel);

	public List<GlobalUrlModel> getAllGlobalUrl();
	public List<Map<String, Object>> downloadWoViewFile( @Param("projectID") int projectID,  @Param("startDate") String startDate, 
			 @Param("endDate") String endDate, @Param("woStatus")  String status);
	
	public List<Map<String, Object>> downloadDoPlanViewFile( @Param("projectID") int projectID,  @Param("startDate") String startDate, 
			 @Param("endDate") String endDate, @Param("woStatus")  String status);

	public void saveLocalUrl(@Param("localUrlModel") LocalUrlModel localUrlModel);

	public void updateLocalUrl(@Param("localUrlModel") LocalUrlModel localUrlModel);
	public List<LocalUrlModel> getAllLocalUrl( @Param("projectID") int projectID);

	public List<LocalUrlModel> getAllActiveLocalUrl( @Param("projectID") int projectId);

	public boolean isSignumExist(@Param("signum") String signum);

	public void saveInstantFeedback(@Param("instantFeedbackModel") InstantFeedbackModel instantFeedbackModel);

	public void updateInstantFeedback(@Param("instantFeedbackModel") InstantFeedbackModel instantFeedbackModel);
	
	
	public List<WorkFlowFeedbackActivityModel> getFeedbackHistory(@Param("workFlowFeedbackModel") WorkFlowFeedbackModel workFlowFeedbackModel);

   public void deleteFeedbackDetailComment(@Param("workFlowFeedbackModel") WorkFlowFeedbackModel workFlowFeedbackModel);
	
	
	public void deleteFeedbackActivityComment(@Param("workFlowFeedbackModel") WorkFlowFeedbackModel workFlowFeedbackModel);
	
	public void addFeedbackDetail(@Param("workFlowFeedbackModel") WorkFlowFeedbackModel workFlowFeedbackModel);

	public void addFeedbackActivity(@Param("workFlowFeedbackActivityModel") WorkFlowFeedbackActivityModel workFlowFeedbackActivityModel);

	
	public List<InstantFeedbackModel> getInstantFeedback();

	public List<InstantFeedbackModel> getInstantFeedbackForDropDown();
	
	public List<ProjectFilterModel> getProjectList(@Param("signum") String signum,@Param("role") String role,@Param("marketArea") String marketArea);

	public List<InstantFeedbackModel> getInstantFeedbackUpdate(@Param("instantFeedbackId") int instantFeedbackId);

	public List<WorkFlowFilterModel> getWorkFlowList(@Param("signum") String signum,@Param("projectId") Integer projectId,@Param("role") String role);
	
	

	public List<FeedbackNEPMModel> getAllFeedback(@Param("signum")String signum,
			@Param("feedbackNePmModel") FeedbackNEPMModel feedbackNePmModel, @Param("dataTableReq") DataTableRequest dataTableReq,@Param("role") String role);

	

	public WorkFlowFeedbackActivityModel getFeedbackActivityIDBySignum(@Param("signum") String signum, @Param("projectID") int projectID,
			@Param("stepID") String stepID, @Param("feedbackOnStep") String feedbackOnStep,
			@Param("feedbackTypeWf") String feedbackTypeWf,@Param("woID") int woID);

	public void updateSadCount(@Param("feedbackActivityID") int feedbackActivityID, @Param("sadCount") int sadCount,
			@Param("role") String role);

	public void updateFeedbackStatus(@Param("feedbackStatusUpdateModel") FeedbackStatusUpdateModel feedbackStatusUpdateModel);

	public WorkFlowFeedbackModel getFeedbackByDetailID(@Param("feedbackDetailID")  int feedbackDetailID,
			@Param("feedbackActivityID") int feedbackActivityID);

	public MailModel getFeedbackMailNotificationDetails(@Param("projectID") int projectID);

	public void addFeedbackStatusForPM(@Param("role") String role,@Param("feedbackStatusUpdateModel") FeedbackStatusUpdateModel feedbackStatusUpdateModel, @Param("signum") String signum);

	public WorkFlowFeedbackModel getFeedbackDetail(@Param("projectID")  int projectID,@Param("stepID")  String stepID, 
			@Param("feedbackOnStep") String feedbackOnStep, @Param("feedbackTypeStep") String feedbackTypeStep,@Param("signum") String signum);

	public int getTemplateId(@Param("notificationTypeFeedbackStatus") String notificationTypeFeedbackStatus);

	public List<EmployeeBasicDetails> getASPMsByFilter(@Param("term") String term, RowBounds rowBounds);

	public WorkFlowFeedbackActivityModel getStatusandSignum(@Param("workFlowFeedbackModel") WorkFlowFeedbackModel workFlowFeedbackModel);
	
	
	public List<Map<String, Object>> downloadFeedbackFile(@Param("signum") String signum, @Param("feedbackNePmModel") FeedbackNEPMModel feedbackNePmModel, @Param("role")  String role);

	public List<EmployeeBasicDetails> testGetEmployeesByFilter(@Param("term") String term, RowBounds rowBounds);
	
	public List<WFStepInstructionModel> getInstructionURLList(@Param("wFStepInstructionModel") WFStepInstructionModel wFStepInstructionModel);

	public String getNameandSignum(@Param("signum") String signum);

	public WorkFlowFeedbackActivityModel getLatestFeedbackStatus(@Param("feedbackDetailID")  int feedbackDetailID);

	public LinkedHashMap<String, String> uploadFileForESSLeaveData(@Param("uploadedBy") String uploadedBy,@Param("fileName") String fileName);

	public List<EmployeeBasicDetails> getEmployeesOrManager(@Param("term") String term, @Param("managerSignum") String managerSignum,RowBounds rowBounds);

	public List<HashMap<String, String>> getLineManagersBySearch(@Param("term") String string, RowBounds rowBounds);

	public String getReplyONComment(@Param("feedbackActivityID") int feedbackActivityID);

	public String getNEComments(@Param("feedbackActivityID") int feedbackActivityID);
	

	public void saveSignalRNotificationLog(@Param("notificationLogModel") NotificationLogModel notificationLogModel);

	public void updateSignalRNotificationLog(@Param("notificationId") Integer notificationId);

	public List<LogLevelModel> getLogLevelByUser(@Param("userSignum") String userSignum);

	public String getWfOwnerByWFIDandWFName(@Param("projectId") int projectId,  @Param("subActivityId") int subActivityId, @Param("workFlowId") int workFlowId, @Param("workFlowName") String workFlowName);

	public DacModel getDacDetails(@Param("projectID") int  projectID);

	public String getSubActivityByID(@Param("subActivityID") int subActivityID);

	public List<Signum> getAllSignumForWoid(@Param("woId")int woId);

	public TblAdhocBooking getAdhocBookingForSignum(@Param("signumID") String signumID,@Param("status")  String status);

	public List<Map<String, Object>> downloadErrorDictionaryFile(@Param("sourceID") int sourceID);

	public DesktopInformationModel getDesktopInformation(@Param("signumId")String signumId);

	public void deleteNEList(@Param("networkElementID")String networkElementID,  @Param("signumID")String signumID);
	//Changes
	public boolean isSubActivityExistsInActive(@Param("standardActivityModel") StandardActivityModel standardActivityModel);
	
	public boolean isDomainTechIDExist(@Param("standardActivityModel") StandardActivityModel standardActivityModel);

	public boolean isDomainTechIDExistInActive(@Param("standardActivityModel") StandardActivityModel standardActivityModel);


	public void updatDtIdActive(@Param("standardActivityModel") StandardActivityModel standardActivityModel);

	public void insertDomainTechID(@Param("standardActivityModel") StandardActivityModel standardActivityModel);

	public boolean isDomainTechExist(@Param("standardActivityModel") StandardActivityModel standardActivityModel);

	public void updateInActiveValue(@Param("standardActivityModel") StandardActivityModel standardActivityModel);

	



}