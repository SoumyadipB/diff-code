package com.ericsson.isf.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.hibernate.SessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.ProjectManagementMapper;
import com.ericsson.isf.model.ActivityModel;
import com.ericsson.isf.model.AdhocTypeModel;
import com.ericsson.isf.model.CountryModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DeliveryAcceptanceModel;
import com.ericsson.isf.model.DeliveryResponsibleModel;
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
import com.ericsson.isf.model.WorkFlowFilterModel;
import com.ericsson.isf.model.WorkflowProficiencyModel;

@Repository
public class ProjectDAO {

	@Qualifier("sqlSession")
	/* Create session from SQLSessionFactory */
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Autowired
	SessionFactory sessionFactory;

	public List<ProjectsModel> getProjectByFilters(ProjectFilterModel projectFilterModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getProjectByFilters(projectFilterModel);
	}

	public List<ProjectsModel> getDashboardProject(String marketArea, String role, String signum) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getDashboardProject(marketArea, role, signum);
	}

	public void addDeliveryResponsible(DeliveryResponsibleModel deliveryResponsibleModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.addDeliveryResponsible(deliveryResponsibleModel);
	}

	public List<DeliveryResponsibleModel> getDeliveryResponsibleByProject(int projectID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getDeliveryResponsibleByProject(projectID);
	}

	public Boolean checkProject(int projectID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.checkProject(projectID);
	}

	public Boolean checkIFSignumExists(String deliveryResponsible, int projectID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.checkIFSignumExists(deliveryResponsible, projectID);
	}

	public Boolean changeProjectStaus(String status, String lastModifiedBy, int projectId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.changeProjectStatus(status, lastModifiedBy, projectId);
	}

	public boolean deleteProject(DeliveryResponsibleModel deliveryResponsibleModel) {

		ProjectManagementMapper projectMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		int rows = projectMapper.deleteProject(deliveryResponsibleModel);
		if (rows == 0)
			return false;
		else
			return true;
	}

	public List<HashMap<String, Object>> getProjectAcceptance(String signum, String marketArea, String role) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getProjectAcceptance(signum, marketArea, role);
	}

	public String getManagerByProjectId(String projectId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getManagerByProjectId(projectId);
	}

	public List<String> getSpocByProjectId(String projectId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getSpocByProjectId(projectId);
	}

	public List<Map<String, Object>> getCustomers(String countryId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getCustomers(countryId);
	}

	public List<Map<String, Object>> getBookingDetalsByScope(ProjectComponentModel projectComponentModel) {
		
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getBookingDetalsByScope(projectComponentModel);
	}

	public List<String> getPositionStatus(Integer scopeId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getPositionStatus(scopeId);
	}

	public boolean updateProjectScope(ProjectComponentModel projectComponentModel) {
		
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.updateProjectScope(projectComponentModel);
	}

	public boolean updateProjectScopeDetail(ProjectComponentModel projectComponentModel) {
		
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.updateProjectScopeDetail(projectComponentModel);
	}

	public boolean updateActivity(ProjectComponentModel projectComponentModel) {
		
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.updateActivity(projectComponentModel);
	}

	public boolean updateSubActivity(ProjectComponentModel projectComponentModel) {
		
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.updateSubActivity(projectComponentModel);
	}

	public boolean updateWorkFlow(ProjectComponentModel projectComponentModel) {
		
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.updateWorkFlow(projectComponentModel);
	}

	public boolean updateWorkOrder(ProjectComponentModel projectComponentModel) {
		
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.updateWorkOrder(projectComponentModel);
	}

	public List<Map<String, Object>> getBookingDetalsByScopeDetailID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getBookingDetalsByScopeDetailID(projectComponentModel);

	}

	public boolean updateAdhocWorkOrder(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.updateAdhocWorkOrder(projectComponentModel);

	}

	public List<Map<String, Object>> getBookingDetalsByActivityID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getBookingDetalsByActivityID(projectComponentModel);
	}

	public List<Map<String, Object>> getWoCountByActivityID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getWoCountByActivityID(projectComponentModel);
	}

	public List<Map<String, Object>> getWfCountByActivityID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getWfCountByActivityID(projectComponentModel);
	}

	public boolean disableAcitvity(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.disableAcitvity(projectComponentModel);
	}

	public boolean disableWorkFlow(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.disableWorkFlow(projectComponentModel);
	}

	public boolean disableWorkOrder(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.disableWorkOrder(projectComponentModel);
	}

	public List<Map<String, Object>> getScopeByScopeID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getScopeByScopeID(projectComponentModel);
	}

	public List<Map<String, Object>> getWoDetailsByScopeID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getWoDetailsByScopeID(projectComponentModel);
	}

	public List<Map<String, Object>> getActivityByScopeDetailID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getActivityByScopeDetailID(projectComponentModel);
	}

	public List<Map<String, Object>> getWorkFlowByScopeDetailID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getWorkFlowByScopeDetailID(projectComponentModel);
	}

	public List<Map<String, Object>> getWorkOrderByScopeDetailID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getWorkOrderByScopeDetailID(projectComponentModel);
	}

	public List<Map<String, Object>> getscopeDetailData(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getscopeDetailData(projectComponentModel);
	}

	public List<ProjectScopeModel> checkExistingScopeName(String scopeName, int projectID, int projectScopeID) {
		
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.checkExistingScopeName(scopeName, projectID, projectScopeID);
	}

	public void updateEditProjectScope(ProjectScopeModel projectScopeModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateEditProjectScope(projectScopeModel);
	}

	public boolean updateEditProjectScopeDetail(ProjectScopeDetailModel projectScopeDetailModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.updateEditProjectScopeDetail(projectScopeDetailModel);

	}

	public void updateProject(ProjectsTableModel projectsTableModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateProject(projectsTableModel);

	}

	public void inactiveDoc(int projId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.inactiveDoc(projId);

	}

	public void insertDocDetails(ProjectDocumentModel projDocMod, int projId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.insertDocDetails(projDocMod, projId);
	}

	public boolean updateWorkOrderPlan(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.updateWorkOrderPlan(projectComponentModel);
	}

	public void updateWorkOrderPlanByActivityID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateWorkOrderPlanByActivityID(projectComponentModel);

	}

	public void updateAdhocWorkOrderByActivityID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateAdhocWorkOrderByActivityID(projectComponentModel);

	}

	public List<Map<String, Object>> getDemandDetailsByScope(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getDemandDetailsByScope(projectComponentModel);
	}

	public void updateResourceRequest(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateResourceRequest(projectComponentModel);
	}

	public void updateResourcePosition(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateResourcePosition(projectComponentModel);
	}

	public void updateWorkEffort(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateWorkEffort(projectComponentModel);
	}

	public void deleteBookedResources(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.deleteBookedResources(projectComponentModel);
	}

	public List<Map<String, Object>> getBookingDetalsFlowChartDefID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getBookingDetalsFlowChartDefID(projectComponentModel);
	}

	public void updateWoPlanIDWithNonExecutedWO(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateWoPlanIDWithNonExecutedWO(projectComponentModel);
	}

	public void updateAdhocWoIDWithNonExecutedWO(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateAdhocWoIDWithNonExecutedWO(projectComponentModel);
	}

	public void updateWorkOrderByFlowChartDefID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateWorkOrderByFlowChartDefID(projectComponentModel);

	}

	public void updateProjectByProjectID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateProjectByProjectID(projectComponentModel);

	}

	public void updateDeliveryResonsible(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateDeliveryResonsible(projectComponentModel);
	}

	public List<Map<String, Object>> getBookingDetalsWoPlanID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getBookingDetalsWoPlanID(projectComponentModel);
	}

	public void updateWorkOrderByWoPlanID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateWorkOrderByWoPlanID(projectComponentModel);

	}

	public List<Map<String, Object>> getAllBookingDetalsWoPlanID(ProjectComponentModel projectComponentModel) {
		
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getAllBookingDetalsWoPlanID(projectComponentModel);
	}

	public void addExternalApplicationReference(ExternalAppReferenceModel externalRefModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.addExternalApplicationReference(externalRefModel);
	}

	public List<ExternalAppReferenceModel> getExternalApplicationReferencesByProjectId(int projectId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getExternalApplicationReferencesByProjectId(projectId);
	}

	public ExternalAppReferenceModel getExternalApplicationReferencesByExternalProjectId(int externalProjectId,
			Integer sourceId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getExternalApplicationReferencesByExternalProjectId(externalProjectId, sourceId);
	}

	public List<Map<String, Object>> getExternalProjects() {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getExternalProjects();
	}

	public void updateStatusOfExternalReference(boolean isActive, int referenceId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateStatusOfExternalReference(isActive, referenceId);
	}

	public void updateFlowChartStepDetails(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateFlowChartStepDetails(projectComponentModel);
	}

	public void deleteNetworkElement(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.deleteNetworkElement(projectComponentModel);

	}

	public void updateProjectExternalProject(ExternalAppReferenceModel externalRef) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateProjectExternalProject(externalRef);
	}

	public void updateProjectBlkWOCreation(ExternalAppReferenceModel externalRef) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateProjectBlkWOCreation(externalRef);

	}

	public List<Map<String, Object>> getRequestType() {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getRequestType();
	}

	public void updateProjectPM(ProjectsTableModel projectsTableModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateProjectPM(projectsTableModel);

	}

	public String getWorkFlowName(Integer subActivityFlowChartDefID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getWorkFlowName(subActivityFlowChartDefID);
	}

	public Integer getExpertWFDefID(String woName, int projectID, int subActivityID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getExpertWFDefID(woName, projectID, subActivityID);
	}

	public List<Map<String, Object>> getTools() {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getTools();
	}

	public List<Map<String, Object>> getToolLicenseType() {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getToolLicenseType();
	}

	public List<Map<String, Object>> getToolLicenseOwner() {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getToolLicenseOwner();
	}

	public List<Map<String, Object>> getAccessMethod() {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getAccessMethod();
	}

	public void saveProjectSpecificTools(ProjectSpecificToolModel projectToolModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.saveProjectSpecificTools(projectToolModel);
	}

	public List<Map<String, Object>> getProjectSpecificTools(Integer projectID, Integer isOnlyActiveRequired) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getProjectSpecificTools(projectID, isOnlyActiveRequired);
	}

	public void updateProjectSpecificTools(ProjectSpecificToolModel projectToolModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateProjectSpecificTools(projectToolModel);

	}

	public void disableEnableTools(ProjectSpecificToolModel projectToolModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.disableEnableTools(projectToolModel);
	}

	public void saveDeliveryAcceptance(DeliveryAcceptanceModel deliveryAcceptanceModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.saveDeliveryAcceptance(deliveryAcceptanceModel);
	}

	public void disableDeliveryAcceptance(Integer deliveryAcceptanceID, String signumID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.disableDeliveryAcceptance(deliveryAcceptanceID, signumID);
	}

	public List<Map<String, Object>> getDeliveryAcceptance(int projectID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getDeliveryAcceptance(projectID);
	}

	public boolean checkIFUserExists(int projectID, String signumID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.checkIFUserExists(projectID, signumID);

	}

	public boolean checkIFUserExistsRPM(int projectID, String signumID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.checkIFUserExistsRPM(projectID, signumID);

	}

	public void saveRPM(RPMModel rmpModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.saveRPM(rmpModel);
	}

	public void disableRPM(Integer rpmID, String signumID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.disableRPM(rpmID, signumID);
	}

	public List<Map<String, Object>> getRPM(int projectID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getRPM(projectID);
	}

	public void updateDeliveryAcceptance(DeliveryAcceptanceModel deliveryAcceptanceModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateDeliveryAcceptance(deliveryAcceptanceModel);

	}

	public boolean checkIFToolExists(ProjectSpecificToolModel projectToolModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.checkIFToolExists(projectToolModel);
	}

	public boolean checkIFUserIsActive(int projectID, String signumID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.checkIFUserIsActive(projectID, signumID);

	}

	public List<String> getRPMByProjectId(String projectId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getRPMByProjectId(projectId);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getActiveWorkOrders(int projectID) {
		return sessionFactory.getCurrentSession()
				.createSQLQuery("select distinct count(WOID) from transactionalData.TBL_WORK_ORDER "
						+ "where Active=1 and status in ('Inprogress', 'onhold', 'assigned', 'reopened') and PROJECTID="
						+ projectID)
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getResPosStaus(int projectID) {
		return sessionFactory.getCurrentSession()
				.createSQLQuery("select distinct rp.PositionStatus from transactionalData.TBL_ResourceRequests rr "
						+ "join transactionalData.TBL_ResourcePosition rp on rr.ResourceRequestID=rp.ResourceRequestID "
						+ "join transactionalData.TBL_WorkEffort we on rp.ResourcePositionID=we.ResourcePositionID "
						+ "where rp.IsActivie=1 and we.IsActive=1 and rr.active=1 and ProjectID=" + projectID)
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getResPosStausActive(int projectID) {
		return sessionFactory.getCurrentSession()
				.createSQLQuery("select distinct count(*) from transactionalData.TBL_ResourceRequests rr "
						+ "join transactionalData.TBL_ResourcePosition rp on rr.ResourceRequestID=rp.ResourceRequestID "
						+ "join transactionalData.TBL_WorkEffort we on rp.ResourcePositionID=we.ResourcePositionID "
						+ "where rp.PositionStatus not in ('Cancelled', 'Position Completion') and rp.IsActivie=1 and ProjectID="
						+ projectID)
				.list();
	}

	@SuppressWarnings("unchecked")
	public int updateProjectStatus(int projId, String status, String signum) {
		return sessionFactory.getCurrentSession()
				.createSQLQuery("update transactionalData.TBL_PROJECTS " + "set status='" + status + "', "
						+ "LastModifiedBy='" + signum + "', " + "LastModifiedOn= :now " + "where ProjectID=" + projId)
				.setTimestamp("now", new Date()).executeUpdate();
	}

	public boolean checkIFUnitExists(ProjectDeliverableUnitModel projectDeliverableUnitModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.checkIFUnitExists(projectDeliverableUnitModel);
	}

	public void saveProjectDelUnit(ProjectDeliverableUnitModel projectDeliverableUnitModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.saveProjectDelUnit(projectDeliverableUnitModel);
	}

	public List<Map<String, Object>> getAllDeliverableUnit(String term) {
		
		int pageSize = 200;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getAllDeliverableUnit(term, rowBounds);
	}

	public void deleteProjDelUnit(ProjectDeliverableUnitModel projectDeliverableUnitModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.deleteProjDelUnit(projectDeliverableUnitModel);
	}

	public boolean checkIfProjectActive(Integer projectID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.checkIfProjectActive(projectID);
	}

	public void EditDeliverableUnit(ProjectDeliverableUnitModel projectDeliverableUnitModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.EditDeliverableUnit(projectDeliverableUnitModel);
	}

	public void updateProjectApprover(ProjectsTableModel projectsTableModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateProjectApprover(projectsTableModel);
	}

	public List<ProjectsTableModel> getProjectByProjectID(int projectID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getProjectByProjectID(projectID);
	}

	public List<Map<String, Object>> getResourceRequestDataByProjecDetails(
			ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getResourceRequestDataByProjecDetails(projectComponentModel);
	}

	public boolean checkIfSourceExists(ExternalSourceModel externalSource) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.checkIfSourceExists(externalSource);
	}

	public void addExternalSource(ExternalSourceModel externalSource) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.addExternalSource(externalSource);
	}

	public List<CountryModel> getCountrybyMarketAreaID(Integer marketAreaID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getCountrybyMarketAreaID(marketAreaID);
	}

	public List<ProjectDetailsModel> getProjectAndScopeDetailBySignum(String signum, String role, String marketArea) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getProjectAndScopeDetailBySignum(signum, role, marketArea);
	}

	public ProjectAllDetailsModel getProjectDetails(int ProjectID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getProjectDetails(ProjectID);
	}

	public List<ProjectDocumentModel> getProjectDocuments(int ProjectID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getProjectDocuments(ProjectID);
	}

	public List<Map<String, Object>> getAllProjects(String marketAreaID, String countryID, String signum, String status,
			String marketArea, String role) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getAllProjects(marketAreaID, countryID, signum, status, marketArea, role);
	}

	public int createProject(ProjectCreationModel projectCreationModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.createProject(projectCreationModel);
	}

	public boolean addProjectDocuments(String projectId, ProjectDocumentModel projectDocument, String signum) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.addProjectDocuments(projectId,projectDocument,signum);
	}

	public boolean haveExecutionPlan(int projectID, int scopeID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.haveExecutionPlan(projectID,scopeID);
	}
	
	public List<DeliveryResponsibleModel> getDeliveryResponsibleByProjectIDandSignum(int projectID, String signumID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getDeliveryResponsibleByProjectIDandSignum(projectID,signumID);
	}

	public Map<String, Object> getActivitySubActivityNameByScopeDetailID(
			String activity, String subActivity) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getActivitySubActivityNameByScopeDetailID(activity,subActivity);
	}

	public boolean validateScopeDetailForActivitySubActivity(ActivityModel activityModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.validateScopeDetailForActivitySubActivity(activityModel);
	}

	public void addInternalSource(ExternalSourceModel externalSource) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.addInternalSource(externalSource);
		
	}

	public List<WorkFlowFilterModel> getWorkFlowsByProjectID(int projectID, String term) {
		int pageSize = 50;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getWorkFlowsByProjectID(projectID, '%' + term + '%',rowBounds);	
		}

	public List<WorkflowProficiencyModel> getEFWorkflowForSignumWFID(
			List<String>  listofsignumIDs , List<Integer>  listofWorkFlowIDs, int projectID, DataTableRequest dataTableRequest, String proficiencyStatus) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getEFWorkflowForSignumWFID(listofsignumIDs, listofWorkFlowIDs,projectID,dataTableRequest,proficiencyStatus);
	}

	public void insertUserProficiencyOnUpdate(
			WorkflowProficiencyModel workFlowProficiencyModel, int projectID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 projectManagementMapper.insertUserProficiencyOnUpdate(workFlowProficiencyModel,projectID);
	}

	public void addProficiencyAction(WorkflowProficiencyModel workFlowProficiencyModel, int projectID) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 projectManagementMapper.addProficiencyAction(workFlowProficiencyModel,projectID);
		
	}

	public void updatePreviousAction(List<WorkflowProficiencyModel> workFlowProficiencyModelList) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 projectManagementMapper.updatePreviousAction(workFlowProficiencyModelList);
		
	}

	public void updatePreviousActionInReset(WorkflowProficiencyModel workFlowProficiencyModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 projectManagementMapper.updatePreviousActionInReset(workFlowProficiencyModel);
		
	}

	public ExternalAppReferenceModel checkIfExternalProjectExistForSameIsfproject(Integer externalProjectId,
			Integer sourceId, Integer projectId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.checkIfExternalProjectExistForSameIsfproject(externalProjectId,sourceId,projectId);
	}
	public List<Integer> getListWoPlanID(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getListWoPlanID(projectComponentModel);
	}

	public void updateIsfProjectIdUploadedBy(int projectID, String externalReference, Integer externalProjectId,
			String externalWorkplanTemplate, String sourceId, String uploadedBy) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 projectManagementMapper.updateIsfProjectIdUploadedBy( projectID,  externalReference,  externalProjectId,
				 externalWorkplanTemplate,  sourceId,uploadedBy);
		
	}
	public boolean checkActiveProject(int projectId) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.checkActiveProject(projectId);
	}

	public void updateIsfProjectIdExternal(int projectID, String externalReference, Integer externalProjectId,
			String externalWorkplanTemplate, String sourceId, String wPlanIds) {
		 ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 projectManagementMapper.updateIsfProjectIdExternal( projectID,  externalReference,  externalProjectId,
				 externalWorkplanTemplate,  sourceId, wPlanIds);
		
	}

	public List<Integer> getWorkPlanIds(int projectID, String externalReference, Integer externalProjectId,
			String externalWorkplanTemplate, String source) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.getWorkPlanIds(projectID,externalReference,externalProjectId , externalWorkplanTemplate, source);
	}

	public boolean updateWorkOrderErisite(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.updateWorkOrderErisite(projectComponentModel);
	}

	public void updateProjectTblExternalProject(Integer externalProjectId, String source, int isfProjectId, String externalWorkplanTemplate) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		projectManagementMapper.updateProjectTblExternalProject(externalProjectId,source, isfProjectId,externalWorkplanTemplate);
	}

	public boolean updateWorkOrderErisitePlanned(ProjectComponentModel projectComponentModel) {
		ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		return projectManagementMapper.updateWorkOrderErisitePlanned(projectComponentModel);
		
	}

	public void updateExecPlanId(int projectID, String externalReference, Integer externalProjectId,
			String externalWorkplanTemplate, String source, String wPlanIds) {
		 ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 projectManagementMapper.updateExecPlanId( projectID,  externalReference,  externalProjectId,
				 externalWorkplanTemplate,  source, wPlanIds);
		
	}
	
	 public List<Map<String, Object>> getScopeType() {
		 ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 return projectManagementMapper.getScopeType();
	 }
	 
	 public boolean isScopeTypeRequired(int deliverableUnit) {
		 ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 return projectManagementMapper.isScopeTypeRequired(deliverableUnit);		
	 }
	 
	 public List<Map<String, Object>> getMethodForDU() {
		 ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 return projectManagementMapper.getMethodForDU();
	 }

	 public boolean isMethodRequired(int deliverableUnit) {
		 ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 return projectManagementMapper.isMethodRequired(deliverableUnit);		
	 }
	 
	 public List<Map<String, Object>> getOperatorCount() {
		 ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 return projectManagementMapper.getOperatorCount();
	 }

	 public boolean isOperatorCountRequired(int deliverableUnit) {
		 ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 return projectManagementMapper.isOperatorCountRequired(deliverableUnit);		
	 }
	 
	 public List<Map<String, Object>> getProjectFinancials() {
		 ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 return projectManagementMapper.getProjectFinancials();
	 }

	 public boolean isProjectFinancialsRequired(int deliverableUnit) {
		 ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 return projectManagementMapper.isProjectFinancialsRequired(deliverableUnit);		
	 }
	 
	 public List<Map<String, Object>> downloadDeliverableData(Integer projectId, List<String> deliverableStatusList) {
		 
		 String status = "";
	    	List<String> statusList = new ArrayList<String>();
	    	if(deliverableStatusList.size() != 0) {
		    	for (String a  : deliverableStatusList) {
		    		a = a.replaceAll( "'", "''");
		    		statusList.add(a);
		    	}
		    	status=String.join("','",statusList );
		    	status = "'"+status+"'"; 
	    	}

		 
	     ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
	     return projectManagementMapper.downloadDeliverableData(projectId,status);
	     
	}
	 
	 public List<AdhocTypeModel> getIDAndTypeOfActivity(String activity) {
		 ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 return projectManagementMapper.getIDAndTypeOfActivity(activity);
	 }

	public boolean checkDrCount(int projectID, int maxLimit) {
		 ProjectManagementMapper projectManagementMapper = sqlSession.getMapper(ProjectManagementMapper.class);
		 return projectManagementMapper.checkDrCount(projectID, maxLimit);
	}

}

