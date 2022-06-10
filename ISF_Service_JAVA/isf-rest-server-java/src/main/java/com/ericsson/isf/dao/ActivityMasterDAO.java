/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
package com.ericsson.isf.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.ActivityMasterMapper;
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
 */
@Repository
public class ActivityMasterDAO {

	@Qualifier("sqlSession")
	/* Create session from SQLSessionFactory */
	@Autowired
	private SqlSessionTemplate sqlSession;

	public List<Map<String, Object>> getSubScopebyScopeID(String projectScopeID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getSubScopebyScopeID(projectScopeID);
	}

	public List<Map<String, Object>> getActivitiesBySubScopeId(int projectScopeDetailID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getActivitiesBySubScopeId(projectScopeDetailID);
	}

	public void saveDomainDetails(DomainModel domainModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveDomainDetails(domainModel);
	}

	public List<ServiceAreaModel> getServiceAreaDetailsBySignum(String signum, Integer ProjectID, Integer ServAreaID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getServiceAreaDetailsBySignum(signum, ProjectID, ServAreaID);
	}

	public List<DomainModel> getDomainDetails(Integer ProjectID, Integer ServiceAreaID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getDomainDetails(ProjectID, ServiceAreaID);
	}

	public List<DomainModel> getDomainDetailsBySignum(String signum, Integer ProjectID, Integer ServiceAreaID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getDomainDetailsBySignum(signum, ProjectID, ServiceAreaID);
	}

	public List<HashMap<String, Object>> getAllProjectsBySignum(String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getAllProjectsBySignum(signum);
	}

	public List<DomainModel> getAllDomainDetailsByService(Integer ServiceAreaID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getAllDomainDetailsByService(ServiceAreaID);
	}

	public void updateDomainDetails(DomainModel domainModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateDomainDetails(domainModel);
	}

	public boolean updateWorkEffort(AllocatedResourceModel allocatedResource, int active) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.updateWorkEffort(allocatedResource, active);
	}

	public boolean updateWorkEffortStatus(int weid) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.updateWorkEffortStatus(weid);
	}

	public List<WorkEffortModel> getActiveWorkEffortsByRpid(int rpID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getActiveWorkEffortsByRpid(rpID);
	}

	public List<WorkEffortModel> getWeidWithMinSdate(int rpID, int isactive) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getWeidWithMinSdate(rpID, isactive);
	}

	public List<WorkEffortModel> getWeidWithMinSdateAll(int rpID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getWeidWithMinSdateAll(rpID);
	}

	public List<WorkEffortModel> getWorkEffortsDetailsByRpID(int rpID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getWorkEffortsDetailsByRpID(rpID);
	}

	public List<WorkEffortModel> getWeidWithMaxEdate(int rpID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getWeidWithMaxEdate(rpID);
	}

	public boolean updateResourcePosition(AllocatedResourceModel allocatedResource) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.updateResourcePosition(allocatedResource);
	}

	public boolean updateRpPositionStatus(String status, int rpID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.updateRpPositionStatus(status, rpID);
	}

	public boolean updateWorkEffortByWeIDReject(AllocatedResourceModel allocatedResource) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.updateWorkEffortByWeIDReject(allocatedResource);
	}

	public void updateWorkEffortByWeIDByProposedWorkEffort(Map<String, Object> wfDetails) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateWorkEffortByWeIDByProposedWorkEffort(wfDetails);
	}

	public void updateWorkEffortPositionStatusById(int workEffortId, String positionStatus) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateWorkEffortPositionStatusById(workEffortId, positionStatus);
	}

	public boolean deleteBookedHoursByWeID(AllocatedResourceModel allocatedResource) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.deleteBookedHoursByWeID(allocatedResource);
	}

	public boolean updateResourcePositionToProposed(int weid, String loggedInUser, String status ) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.updateResourcePositionToProposed(weid, loggedInUser,status);
	}

	public Map<String, Object> getProjectDetailsByWeid(int weid) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getProjectDetailsByWeid(weid);
	}

	public void saveServiceAreaDetails(ServiceAreaModel serviceAreaModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveServiceAreaDetails(serviceAreaModel);
	}

	public void updateServiceAreaDetails(ServiceAreaModel serviceAreaModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateServiceAreaDetails(serviceAreaModel);
	}

	public List<ServiceAreaModel> getServiceAreaDetails(Integer ProjectID, Integer ServAreaID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getServiceAreaDetails(ProjectID, ServAreaID);
	}

	public void saveTechnologyDetails(TechnologyModel technologyModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveTechnologyDetails(technologyModel);
	}

	public void updateTechnologyDetails(TechnologyModel technologyModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateTechnologyDetails(technologyModel);
	}

	public List<TechnologyModel> getTechnologyDetails(Integer domainId, Integer projectID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getTechnologyDetails(domainId, projectID);
	}

	public void saveActivityAndSubActivityDetails(StandardActivityModel standardActivityModel) {

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveActivityAndSubActivityDetails(standardActivityModel);
	}

	public void updateActivityAndSubActivityDetails(StandardActivityModel standardActivityModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateActivityAndSubActivityDetails(standardActivityModel);
	}

	public List<StandardActivityModel> getActivityAndSubActivityDetails(int domainID, int serviceAreaID,
			int technologyID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getActivityAndSubActivityDetails(domainID, serviceAreaID, technologyID);
	}

	public void saveTaskDetails(TaskModel taskModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveTaskDetails(taskModel);
	}

	public List<TaskModel> getTaskDetails(int subActivityID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getTaskDetails(subActivityID, signumID);
	}

	public void updateTaskDetails(TaskModel taskModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateTaskDetails(taskModel);
	}

	public void updateTaskToolDetails(TaskToolModel taskToolModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateTaskToolDetails(taskToolModel);
	}

	public List<TaskModel> getTaskToolDetails() {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getTaskToolDetails();
	}

	public void mapTaskAndTool(TaskToolModel taskToolModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.mapTaskAndTool(taskToolModel);
	}

	public void updateTaskToolMapping(TaskToolModel taskToolModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateTaskToolMapping(taskToolModel);
	}

	public void deleteDomainDetails(int domainID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.deleteDomainDetails(domainID, signumID);
	}

	public void deleteServiceArea(int serviceAreaID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.deleteServiceArea(serviceAreaID, signumID);
	}

	public void deleteTechnology(int technologyID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.deleteTechnology(technologyID, signumID);
	}

	public void deleteActivityAndSubActivity(int subActivityID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.deleteActivityAndSubActivity(subActivityID, signumID);
	}

	public void deleteTaskDetails(int taskID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.deleteTaskDetails(taskID, signumID);
	}

	public void deleteTaskToolMapping(int taskToolID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.deleteTaskToolMapping(taskToolID, signumID);
	}

	public List<DomainModel> getDomainDetailsByID(int domainID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getDomainDetailsByID(domainID, signumID);
	}

	public List<ServiceAreaModel> getServiceAreaDetailsByID(int serviceAreaID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getServiceAreaDetailsByID(serviceAreaID, signumID);
	}

	public List<TechnologyModel> getTechnologyDetailsByID(int technologyID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getTechnologyDetailsByID(technologyID, signumID);
	}

	public List<TechnologyModel> getTechnologyDetailsByDomain(Integer technologyID, Integer signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getTechnologyDetailsByDomain(technologyID, signumID);
	}

	public List<StandardActivityModel> getActivityAndSubActivityDetailsByID(int subActivityID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getActivityAndSubActivityDetailsByID(subActivityID, signumID);
	}

	public List<ActivityMasterModel> getAutoComDetails(int technologyID, int signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getAutoComDetails(technologyID, signumID);
	}

	public List<TaskModel> getTaskToolDetailsByID(int taskID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getTaskToolDetailsByID(taskID, signumID);
	}

	public List<TaskModel> getTaskDetailsByID(int taskID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getTaskDetailsByID(taskID, signumID);
	}

	public boolean isDomainExits(String domain, String subDomain) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isDomainExits(domain, subDomain);
	}

	public boolean isTaskExists(String task, int subActivityID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isTaskExists(task, subActivityID);
	}

	public boolean isServiceAreaExists(String serviceArea, String subServiceArea) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isServiceAreaExists(serviceArea, subServiceArea);
	}

	public boolean isTechnologyExists(String technology) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isTechnologyExists(technology);
	}

	public boolean isSubActivtyExists(StandardActivityModel standardActivityModel) {

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isSubActivtyExists(standardActivityModel);
	}

	public EmployeeModel getEmployeeBySignum(String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getEmployeeBySignum(signum);
	}
	public EmployeeModel getEmployeeByEmail(String email) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getEmployeeByEmail(email);
	}

	public boolean isEmployeeExists(String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isEmployeeExists(signumID);
	}

	public List<EmployeeBasicDetails> getEmployeeDetails() {

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getEmployeeDetails();
	}

	public List<EmployeeModel> getEmployeeByProject(int projectID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getEmployeeByProject(projectID);
	}

	public boolean isEmployeeByManagerExists(String managerSignum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isEmployeeByManagerExists(managerSignum);
	}

	public List<EmployeeModel> getEmployeesByManager(String managerSignum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getEmployeesByManager(managerSignum);
	}

	public List<HashMap<String, String>> getAllLineManagers() {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getAllLineManagers();
	}

	public boolean isProjectApprovalByProjectIdExists(int projectId) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isProjectApprovalByProjectIdExists(projectId);
	}

	public List<ProjectApprovalModel> getProjectApprovalDetailsById(int projectId) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getProjectApprovalDetailsById(projectId);
	}

	public boolean isProjectApprovalByApproverExists(String approverSignum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isProjectApprovalByApproverExists(approverSignum);
	}

	public List<ProjectApprovalModel> getProjectApprovalsByApprover(String approverSignum, String marketArea) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getProjectApprovalsByApprover(approverSignum, marketArea);
	}

	public boolean updateProjectApprovalDetails(ProjectApprovalModel projectApprovalModel, String Signum) {

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);

		int rows = activityMasterMapper.updateProjectApprovalDetails(projectApprovalModel,Signum);

		if (rows == 0)
			return false;
		else
			return true;

	}

	public boolean updateProjectByApproval(int projectId) {

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);

		int rows = activityMasterMapper.updateProjectByApproval(projectId);
		if (rows == 0)
			return false;
		else
			return true;

	}

	public boolean activateOpportunity(int projectId) {

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);

		int rows = activityMasterMapper.activateOpportunity(projectId);
		if (rows == 0) {
			return false;
		} else {
			return true;
		}

	}

	public boolean updateResourceRequestCertification(
			ResourceRequestCertificationModel resourceRequestCertificationModel) {

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		int rows = activityMasterMapper.updateResourceRequestCertification(resourceRequestCertificationModel);
		if (rows == 0) {
			return false;
		} else {
			return true;
		}

	}

	public boolean updateResourceRequestCompetence(ResourceRequestCompetenceModel resourceRequestCompetenceModel) {

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		int rows = activityMasterMapper.updateResourceRequestCompetence(resourceRequestCompetenceModel);
		if (rows == 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isCertificateExists(int certificateId) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isCertificateExists(certificateId);
	}

	public boolean addCertificationByRRID(ResourceRequestCertificationModel resourceRequestCertificationModel) {

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		int rows = activityMasterMapper.addCertificationByRRID(resourceRequestCertificationModel);
		if (rows == 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isResourceRequestExists(int resourceRequestId) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isResourceRequestExists(resourceRequestId);
	}

	public boolean isCompetenceExists(int competenceId) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isCompetenceExists(competenceId);
	}

	public boolean addCompetenceByRRID(ResourceRequestCompetenceModel resourceRequestCompetenceModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		int rows = activityMasterMapper.addCompetenceByRRID(resourceRequestCompetenceModel);
		if (rows == 0) {
			return false;
		} else {
			return true;
		}
	}

	public List<NetworkElementModel> searchNetworkElementDetails(NetworkElementModel networkElementModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.searchNetworkElementDetails(networkElementModel);
	}

	public List<NetworkElementModel> getNetworkElementDetails(int projectID, DataTableRequest dataTableReq) {
		int pageSize = dataTableReq.getLength();
		int pageNo = dataTableReq.getStart() / pageSize;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getNetworkElementDetails(projectID, dataTableReq, rowBounds );
	}

	public List<EmployeeBasicDetails> getEmployeesByFilter(String term) {
		int pageSize = 50;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getEmployeesByFilter('%' + term + '%',rowBounds);
	}
	public List<EmployeeBasicDetails> getEmployeesByFilter1(String term) {
		int pageSize = 50;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getEmployeesByFilter1('%' + term + '%',rowBounds);
	}

	public int getNetworkElementDetailsCount(int projectID, DataTableRequest dataTableReq) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getNetworkElementDetailsCount(projectID, dataTableReq);
	}

	public void deleteNetworkElementDetails(int networkElementID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.deleteNetworkElementDetails(networkElementID, signumID);
	}

	
	public void deleteVendorDetails(int vendorID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.deleteVendorDetails(vendorID, signumID);
	}

	public List<VendorModel> getVendorDetailsByID(int vendorID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getVendorDetailsByID(vendorID, signumID);
	}

	public void saveProjectDefinedTasks(FlowChartStepDetailsModel flowChartStepDetailsModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveProjectDefinedTasks(flowChartStepDetailsModel);
	}

	public List<VendorModel> getVendorDetails() {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getVendorDetails();
	}

	public void saveVendorDetails(VendorModel vendorModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveVendorDetails(vendorModel);
	}

	public void updateVendorDetails(VendorModel vendorModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateVendorDetails(vendorModel);
	}

	public boolean isVendorExists(String vendor) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isVendorExists(vendor);
	}

	public List<ActivityMasterModel> getSubActivityForActivity(int domainID, int technologyID, int serviceAreaID,
			String activityName) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getSubActivityForActivity(domainID, technologyID, serviceAreaID, activityName);
	}

	/**
	 * 
	 * @param projectID
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getActivitySubActivityByProjectId(Integer projectID) {

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getActivitySubActivityByProjectId(projectID);
	}

	public Map<String, Object> getDetails(int rrID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getDetails(rrID);
	}

	public boolean insertBookedResource(Map<String, Object> map) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.insertBookedResource(map);
	}

	public void insertProposedWorkEfforts(int workEffortID, float hours, int duration, Date startDate, Date endDate) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.insertProposedWorkEfforts(workEffortID, hours, duration, startDate, endDate);

	}

	public List<Map<String, Object>> getTechnologyDetailsBySignum(String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getTechnologyDetailsBySignum(signum);
	}

	public List<Map<String, Object>> getEmployeeDetailsBySignum(String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getEmployeeDetailsBySignum(signum);
	}

	public List<Map<String, Object>> getActivitySubActivityByProject_V2(int projectID, int serviceAreaID, int domainID,
			int technologyID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getActivitySubActivityByProject_V2(domainID, technologyID, serviceAreaID,
				projectID);
	}

	public List<Map<String, Object>> getActivitySubActivityByProject_V3(int ProjectID, int ServiceAreaID, int DomainID,
			int TechnologyID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getActivitySubActivityByProject_V3(DomainID, TechnologyID, ServiceAreaID,
				ProjectID);

	}

	public List<Map<String, Object>> getActivityByProjectId(int ProjectID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getActivityByProjectId(ProjectID);

	}

	public List<TaskModel> viewTaskDetails(int projectID, int subActivityID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.viewTaskDetails(projectID, subActivityID);
	}

	public boolean addDomainSpoc(SpocModel spocRequest) {

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.addDomainSpoc(spocRequest) > 0;

	}

	public SpocModel getDomainSpoc(SpocModel spocRequest) {

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getDomainSpoc(spocRequest);

	}

	public List<Map<String, Object>> getAllDomainSpoc() {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getAllDomainSpoc();
	}

	public void deleteWorkEffortFromProposed(int weid) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.deleteWorkEffortFromProposed(weid);

	}

	public TaskModel getTaskDetailsFromMaster(int subactivityID, int taskID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getTaskDetailsFromMaster(subactivityID, taskID);
	}

	public List<Integer> getProjectIDForSubActivity(int subActivityID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getProjectIDForSubActivity(subActivityID);
	}

	public boolean isTaskExistsInProjConfTable(int taskID, int subActivityID, int projectID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isTaskExistsInProjConfTable(taskID, subActivityID, projectID);
	}

	public void updateProjConfTable(int projectID, TaskModel taskModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateProjConfTable(projectID, taskModel);
	}

	public Map<String, Object> getMaxTasksValueByProject(int projectID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getMaxTasksValueByProject(projectID);
	}

	public void insertMaxTasksValueByProject(int projectID, String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.insertMaxTasksValueByProject(projectID, signum);

	}

	public boolean updateMaxTasksValueByProject(int projectID, String signum, int maxManual, int maxAutomaic) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.updateMaxTasksValueByProject(projectID, signum, maxManual, maxAutomaic);
	}

	public List<Map<String, Object>> downloadNetworkElement(int projectID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.downloadNetworkElement(projectID);
	}

	public Map<String, Object> getTaskDetailForSID(int subActivityID, String taskOrParentTaskMapped) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getTaskDetailForSID(subActivityID, taskOrParentTaskMapped);
	}

	public Map<String, String> getToolDetailForWF(String toolName) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getToolDetailForWF(toolName);
	}

	public List<CustomerModel> getCustomerDetails() {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getCustomerDetails();
	}

	public void saveCustomerDetails(CustomerModel customerModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveCustomerDetails(customerModel);
	}

	public void saveCustomerCountryMappimg(CustomerModel customerModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveCustomerCountryMappimg(customerModel);
	}

	public CustomerModel getCustMod(CustomerModel customerModel) {

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getCustMod(customerModel);

	}

	public int getProjectIdByRpID(int rpID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getProjectIdByRpID(rpID);
	}

	public List<WorkInstructionModel> getWorkInstruction() {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getWorkInstruction();
	}

	public void saveWorkInstruction(WorkInstructionModel workInstructionModel1) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveWorkInstruction(workInstructionModel1);
	}

	public void deleteWorkInstruction(int wIID, String signumID, boolean active) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.deleteWorkInstruction(wIID, signumID, active);

	}

	public void editPrevWorkInstruction(int wIID, String modifiedBy) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.editPrevWorkInstruction(wIID, modifiedBy);

	}

	public List<WorkInstructionModel> getActiveWorkInstruction(Integer domainID, Integer vendorID, Integer technologyID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getActiveWorkInstruction(domainID, vendorID, technologyID);
	}

	public AdhocBookingProjectModel getAdhocBookingForProject(String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getAdhocBookingForProject(signum);

	}
	//v1
	public Map<String, Object> getAdhocBookingForProjectV1(String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getAdhocBookingForProjectV1(signum);

	}

	public void saveLeavePlan(LeavePlanModel leavePlanModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveLeavePlan(leavePlanModel);
	}

	public List<LeavePlanModel> getLeavePlanBySignum(String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getLeavePlanBySignum(signum);
	}

	public void deleteLeavePlan(String signum, int leavePlanID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.deleteLeavePlan(signum, leavePlanID);
	}

	public List<ShiftTimmingModel2> getShiftTimmingBySignumAndDate(String signum, String startDate) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getShiftTimmingBySignumAndDate(signum, startDate);
	}

	public List<TimeZoneModel> getTimeZones() {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getTimeZones();
	}

	public void deleteShiftTimmingBySignum(String signum, String startDate) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.deleteShiftTimmingBySignum(signum, startDate);
	}

	public void saveshiftTimming(String signum, ShiftTimmingModel shiftTimmingModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveshiftTimming(signum, shiftTimmingModel);

	}

	public boolean updateResourcePositionToRejected(int weid, String loggedInUser, String status) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.updateResourcePositionToRejected(weid, loggedInUser,status);
	}

	public List<String> getNodeTypeByFilter(NodeNamesByFilterModel nodeNamesByFilterModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getNodeTypeByFilter(nodeNamesByFilterModel);
	}

	public List<ScopeDetailsModel> getScopeDetailsById(String projectScopeDetailID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getScopeDetailsById(projectScopeDetailID);
	}

	public EmployeeModel getAspDetailsBySignum(String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getAspDetailsBySignum(signum);
	}

	public void appendToPositionComments(String comment, int positionId, String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.appendToPositionComments(comment, positionId,signum);
	}

	public List<ShiftTimmingModel2> getShiftTimmingByDate(String signum, String startDate, String endDate,
			String timeZone) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getShiftTimmingByDate(signum, startDate, endDate, timeZone);
	}

	public boolean deleteShiftTimmingByID(String signum, int shiftId) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.deleteShiftTimmingByID(signum, shiftId);
	}

	public void deleteCustomerDetails(int customerID, String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.deleteCustomerDetails(customerID, signum);
	}

	public void editCustomerName(CustomerModel customerModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.editCustomerName(customerModel);

	}

	public WorkInstructionModel downloadWorkInstructionFile(String wIID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.downloadWorkInstructionFile(wIID);
	}

	public List<UserFeedbackModel> getUserFeedBack() {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getUserFeedBack();
	}

	public List<HashMap<String, Object>> getEssLeave() {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getEssLeave();
	}

	public List<String> getNodeTypeByDeliverableId(int projectID, int deliverableId, String elementType) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getNodeTypeByDeliverableId(projectID, deliverableId, elementType);
	}

	public String getPositionStatusByID(int weid) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getPositionStatusByID(weid);
	}

	public String getNearestNextShiftStartDate(String signum, String prevShiftStartDate) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getNearestNextShiftStartDate(signum, prevShiftStartDate);
	}

	public ShiftTimmingModel2 getNearestPreviousShift(String signum, String endDate) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getNearestPreviousShift(signum, endDate);
	}

	public List<CountryModel> getCountries() {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getCountries();
	}

	public ScopeDetailsModel getScopeByScopeId(int projectScopeID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getScopeByScopeId(projectScopeID);
	}

	public String changeEmployeeStatus(ResourceStatusModel resourceStatusModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.changeEmployeeStatus(resourceStatusModel);
	}
	public List<String> getNodes(int countryCustomerGroupID, String elementType) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getNodes(countryCustomerGroupID,elementType);
	}

	public List<ScopeDetailsModel> getScopeDetailsByScopeId(String projectScopeId) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getScopeDetailsByScopeId(projectScopeId);

	}

	public List<ShiftTimmingModel2> getShiftTimmingBySignum(String signum, String startDate) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getShiftTimmingBySignum(signum, startDate);
	}
	public void saveGlobalUrl(GlobalUrlModel globalUrlModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveGlobalUrl(globalUrlModel);
	}

	public List<Map<String, Object>> downloadWoViewFile(int projectID, String startDate, String endDate, String status) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.downloadWoViewFile(projectID,startDate, endDate, status);
	}

	public List<Map<String, Object>> downloadDoPlanViewFile(int projectID, String startDate, String endDate, String status) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.downloadDoPlanViewFile(projectID,startDate, endDate, status);
	}

	public void updateGlobalUrl(GlobalUrlModel globalUrlModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateGlobalUrl(globalUrlModel);

	}

	public List<GlobalUrlModel> getAllGlobalUrl() {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getAllGlobalUrl();
	}

	public void saveLocalUrl(LocalUrlModel localUrlModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveLocalUrl(localUrlModel);

	}

	public void updateLocalUrl(LocalUrlModel localUrlModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateLocalUrl(localUrlModel);

	}

	public List<LocalUrlModel> getAllLocalUrl(int projectID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getAllLocalUrl(projectID);
	}

	public List<LocalUrlModel> getAllActiveLocalUrl(int projectId) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getAllActiveLocalUrl(projectId);
	}

	public boolean isSignumExist(String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isSignumExist(signum);
	}


	public List<WorkFlowFeedbackActivityModel> getFeedbackHistory(WorkFlowFeedbackModel workFlowFeedbackModel) {

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getFeedbackHistory(workFlowFeedbackModel);
	}

	public void saveInstantFeedback(InstantFeedbackModel instantFeedbackModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveInstantFeedback(instantFeedbackModel);

	}

	public void updateInstantFeedback(InstantFeedbackModel instantFeedbackModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateInstantFeedback(instantFeedbackModel);

	}

	public void deleteFeedbackDetailComment(WorkFlowFeedbackModel workFlowFeedbackModel) {
		
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
	    activityMasterMapper.deleteFeedbackDetailComment(workFlowFeedbackModel);
	}

	public void deleteFeedbackActivityComment(WorkFlowFeedbackModel workFlowFeedbackModel) {
		
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
	    activityMasterMapper.deleteFeedbackActivityComment(workFlowFeedbackModel);
	}

	public void addFeedbackDetail(WorkFlowFeedbackModel workFlowFeedbackModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.addFeedbackDetail(workFlowFeedbackModel);
	}
	
	public List<InstantFeedbackModel> getInstantFeedback() {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getInstantFeedback();
		
	}

	public void addFeedbackActivity(WorkFlowFeedbackActivityModel workFlowFeedbackActivityModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.addFeedbackActivity(workFlowFeedbackActivityModel);
	}

	public List<InstantFeedbackModel> getInstantFeedbackForDropDown() {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getInstantFeedbackForDropDown();
	}
	
	public List<ProjectFilterModel> getProjectList(String signum, String role, String marketArea) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getProjectList(signum,role,marketArea);
	}

	public List<InstantFeedbackModel> getInstantFeedbackUpdate(int instantFeedbackId) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getInstantFeedbackUpdate(instantFeedbackId);
	}

	public List<WorkFlowFilterModel> getWorkFlowList(String signum, Integer projectId, String role) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getWorkFlowList(signum,projectId,role);
	}

	public List<FeedbackNEPMModel> getAllFeedback(String signum, FeedbackNEPMModel feedbackNePmModel, DataTableRequest dataTableRequest, String role) {
		
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getAllFeedback(signum,feedbackNePmModel,dataTableRequest,role);
	}

	public WorkFlowFeedbackActivityModel getFeedbackActivityIDBySignum(int projectID, String signum, String stepID, String feedbackOnStep,
			String feedbackTypeWf, int woID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getFeedbackActivityIDBySignum(signum,	projectID, stepID, feedbackOnStep, feedbackTypeWf, woID);
	}

	public void updateSadCount(int feedbackActivityID, int sadCount, String role) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateSadCount(feedbackActivityID, sadCount, role);
	}

	public void updateFeedbackStatus(FeedbackStatusUpdateModel feedbackStatusUpdateModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateFeedbackStatus(feedbackStatusUpdateModel);
	}

	public WorkFlowFeedbackModel getFeedbackByDetailID(int feedbackDetailID, int feedbackActivityID ) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getFeedbackByDetailID(feedbackDetailID,feedbackActivityID);
	}

	public MailModel getFeedbackMailNotificationDetails(int projectID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getFeedbackMailNotificationDetails(projectID);
	}

	public void addFeedbackStatusForPM(String role,FeedbackStatusUpdateModel feedbackStatusUpdateModel, String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.addFeedbackStatusForPM(role,feedbackStatusUpdateModel,signum);
	}

	public WorkFlowFeedbackModel getFeedbackDetail(int projectID, String stepID, String feedbackOnStep,
			String feedbackTypeStep, String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getFeedbackDetail(projectID, stepID, feedbackOnStep, feedbackTypeStep,signum);
	}

	public int getTemplateId(String notificationTypeFeedbackStatus) {
	       
        ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
        return activityMasterMapper.getTemplateId(notificationTypeFeedbackStatus);
    }

	public List<EmployeeBasicDetails> getASPMsByFilter(String term) {
		
		int pageSize = 50;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
	     return activityMasterMapper.getASPMsByFilter('%' + term + '%', rowBounds);
	}

	public WorkFlowFeedbackActivityModel getStatusandSignum(WorkFlowFeedbackModel workFlowFeedbackModel) {
		 ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
	        return activityMasterMapper.getStatusandSignum(workFlowFeedbackModel);
	}
	
	public List<Map<String, Object>> downloadFeedbackFile(String signum,FeedbackNEPMModel feedbackNePmModel,  String role) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.downloadFeedbackFile(signum,feedbackNePmModel,role);
	
		
		
	}

	public List<EmployeeBasicDetails> testGetEmployeesByFilter(String term) {
		int pageSize = 50;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.testGetEmployeesByFilter('%' + term + '%', rowBounds);
	}

	public void saveSignalRNotificationLog(NotificationLogModel notificationLogModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.saveSignalRNotificationLog(notificationLogModel);
		
	}

	public void updateSignalRNotificationLog(Integer notificationId) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.updateSignalRNotificationLog(notificationId);
	}
	
	public List<LogLevelModel> getLogLevelByUser(String userSignum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getLogLevelByUser(userSignum);
		
	}


	public LinkedHashMap<String, String> uploadFileForESSLeaveData(String uploadedBy,String fileName) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.uploadFileForESSLeaveData(uploadedBy,fileName);	
	}

	public List<EmployeeBasicDetails> getEmployeesOrManager(String term, String managerSignum) {
		int pageSize = 50;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getEmployeesOrManager('%' + term + '%',managerSignum,rowBounds);
	}

	public List<HashMap<String, String>> getLineManagersBySearch(String term) {
		int pageSize = 50;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);

		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getLineManagersBySearch('%' + term + '%',rowBounds);
	}

	
	public List<WFStepInstructionModel> getInstructionURLList(WFStepInstructionModel wFStepInstructionModel) {

        ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
        return activityMasterMapper.getInstructionURLList(wFStepInstructionModel);
    }

	public String getNameandSignum(String signum) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
        return activityMasterMapper.getNameandSignum(signum);
		
	}

	public WorkFlowFeedbackActivityModel getLatestFeedbackStatus(int feedbackDetailID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
        return activityMasterMapper.getLatestFeedbackStatus(feedbackDetailID);
		
	}

	public String getReplyONComment(int feedbackActivityID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
        return activityMasterMapper.getReplyONComment(feedbackActivityID);
		
	}

	public String getNEComments(int feedbackActivityID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
        return activityMasterMapper.getNEComments(feedbackActivityID);
	}

	public String getWfOwnerByWFIDandWFName(int projectId, int subActivityId, int workFlowId, String workFlowName) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
        return activityMasterMapper.getWfOwnerByWFIDandWFName(projectId, subActivityId, workFlowId, workFlowName);
	}

	public DacModel getDacDetails(int projectID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getDacDetails(projectID);
	}

	public String getSubActivityByID(int subActivityID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getSubActivityByID(subActivityID);
	}

	public List<Signum> getAllSignumForWoid(int woId) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getAllSignumForWoid(woId);
	}

	public TblAdhocBooking getAdhocBookingForSignum(String signumID, String status) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getAdhocBookingForSignum(signumID, status);
	}
	
	public List<Map<String, Object>> downloadErrorDictionaryFile(int sourceID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.downloadErrorDictionaryFile(sourceID);
	}

	public DesktopInformationModel getDesktopInformation(String signumId) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.getDesktopInformation(signumId);
	}
	
	public void deleteNEList(String networkElementID, String signumID) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		activityMasterMapper.deleteNEList(networkElementID, signumID);
}
	//Changes 
	
	public boolean isSubActivityExistsInActive(StandardActivityModel standardActivityModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isSubActivityExistsInActive(standardActivityModel);
	} 
	
	public boolean isDomainTechExist(StandardActivityModel standardActivityModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isDomainTechExist(standardActivityModel);
	} 
	
	public boolean isDomainTechIDExist(StandardActivityModel standardActivityModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isDomainTechIDExist(standardActivityModel);
	}
	
	public boolean isDomainTechIDExistInActive(StandardActivityModel standardActivityModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		return activityMasterMapper.isDomainTechIDExistInActive(standardActivityModel);
	}
	
	public void updatDtIdActive(StandardActivityModel standardActivityModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		 activityMasterMapper.updatDtIdActive(standardActivityModel);
	}
	
	public void insertDomainTechID(StandardActivityModel standardActivityModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		 activityMasterMapper.insertDomainTechID(standardActivityModel);
	}

	public void updateInActiveValue(StandardActivityModel standardActivityModel) {
		ActivityMasterMapper activityMasterMapper = sqlSession.getMapper(ActivityMasterMapper.class);
		 activityMasterMapper.updateInActiveValue(standardActivityModel);
	}
}
