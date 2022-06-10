/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.ericsson.isf.model.ActivityModel;
import com.ericsson.isf.model.ActivityScopeModel;
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
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.ScopeDomainProject;
import com.ericsson.isf.model.WorkFlowFilterModel;
import com.ericsson.isf.model.WorkflowProficiencyModel;

/**
 *
 * @author ekarath
 */
public interface ProjectManagementMapper {

	/*
	 * //TODO code cleanup public List<OpportunityModel> getOpportunityDetails();
	 */
	public int saveActivityScope(@Param("activityScopeModel") ActivityScopeModel activityScopeModel);

	/*
	 * //TODO code cleanup public void
	 * DeleteActivityScope(@Param("activityScopeId")int
	 * activityScopeId,@Param("lastModifiedBy") String lastModifiedBy);
	 */
	public void saveScope(@Param("projectScopeModel") ProjectScopeModel projectScopeModel);

	public void DeleteScope(@Param("scopeId") int scopeId, @Param("lastModifiedBy") String lastModifiedBy);

	public void saveProjectScopeDetail(
			@Param("projectScopeDetailModel") ProjectScopeDetailModel projectScopeDetailModel);

	public void DeleteProjectScopeDetail(@Param("projectScopeDetailID") int projectScopeDetailID,
			@Param("lastModifiedBy") String lastModifiedBy);

	public void deleteProjectScopeDetailByProjectScopeId(@Param("projectScopeId") int projectScopeId,
			@Param("lastModifiedBy") String lastModifiedBy);

	public List<ProjectScopeDetailModel> getProjectScopeDetailsByScopeId(@Param("projectScopeId") int projectScopeId);

	/*
	 * //TODO code cleanup public List<ProjectsModel> getStatus();
	 */
	public List<ProjectsModel> getProjectByFilters(@Param("projectFilterModel") ProjectFilterModel projectFilterModel);

	public void addDeliveryResponsible(@Param("deliveryResponsible") DeliveryResponsibleModel deliveryResponsibleModel);

	public List<DeliveryResponsibleModel> getDeliveryResponsibleByProject(@Param("projectID") int projectID);

	public Boolean checkProject(@Param("projectID") int projectID);

	public Boolean checkIFSignumExists(@Param("deliveryResponsible") String deliveryResponsible,
			@Param("projectID") int projectID);

	public int deleteProject(@Param("DeliveryResponsibleModel") DeliveryResponsibleModel DeliveryResponsibleModel);

	public Boolean changeProjectStatus(@Param("status") String status, @Param("lastModifiedBy") String lastModifiedBy,
			@Param("projectId") Integer projectId);

	public List<HashMap<String, Object>> getProjectAcceptance(@Param("signum") String signum,
			@Param("marketArea") String marketArea, @Param("role") String role);

	public void deleteActivityScopeByProjectScopeDetailId(@Param("projectScopeDetailID") int projectScopeDetailID,
			@Param("lastModifiedBy") String lastModifiedBy);

	public List<ProjectsModel> getDashboardProject(@Param("marketArea") String marketArea, @Param("role") String role,
			@Param("signum") String signum);

	public String getManagerByProjectId(@Param("projectId") String projectId);

	public List<String> getSpocByProjectId(@Param("projectId") String projectId);

	public Boolean checkIfDataExists(@Param("projectScopeID") int projectScopeID,
			@Param("serviceAreaID") int serviceAreaID, @Param("domainID") int domainID,
			@Param("technologyID") int technologyID);

	public Boolean checkIfProjectScopeExists(@Param("projectID") int projectID, @Param("scopeName") String scopeName,
			@Param("requestType") String requestType, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate,@Param("projectScopeId")  int projectScopeId);

	public List<String> getAllMakretAreas();
	/*
	 * //TODO code cleanup public List<Map<String,Object>> getCountries();
	 */
	/*
	 * //TODO code cleanup public List<Map<String,Object>>
	 * getCountriesbyMarketAreaID(@Param("marketAreaID") String marketAreaID);
	 */

	public List<Map<String, Object>> getCustomers(@Param("countryID") String countryID);
	/*
	 * //TODO code cleanup public List<Map<String,Object>>
	 * getAllProjects(@Param("marketAreaID") String marketAreaID
	 * ,@Param("countryID") String countryID ,@Param("signum") String signum
	 * ,@Param("status") String status);
	 */

	public List<Map<String, Object>> getAllScopeDetailsByProject(@Param("projectId") String projectId);

	// TODO code cleanup
	public List<Map<String, Object>> getDemandOwningCompanies();

	// TODO code cleanup
	public List<Map<String, Object>> getMarketAreas();

	// TODO code cleanup
	public List<Map<String, Object>> getOpportunities(@Param("marketArea") String marketArea);

	// TODO code cleanup
	Map<String, Object> getOpportunityDetailsById(@Param("opportunityID") String opportunityID);

	/*
	 * //TODO code cleanup String getDesktopVersion(@Param("type") String type);
	 */
	public List<Map<String, Object>> getBookingDetalsByScope(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<String> getPositionStatus(@Param("scopeId") Integer scopeId);

	public boolean updateProjectScope(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public boolean updateProjectScopeDetail(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public boolean updateActivity(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public boolean updateSubActivity(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public boolean updateWorkFlow(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public boolean updateWorkOrder(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getBookingDetalsByScopeDetailID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public boolean updateAdhocWorkOrder(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getBookingDetalsByActivityID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getWoCountByActivityID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getWfCountByActivityID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public boolean disableAcitvity(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public boolean disableWorkFlow(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public boolean disableWorkOrder(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getScopeByScopeID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getWoDetailsByScopeID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getWorkOrderByScopeDetailID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getWorkFlowByScopeDetailID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getActivityByScopeDetailID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getscopeDetailData(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<ProjectScopeModel> checkExistingScopeName(@Param("scopeName") String scopeName,
			@Param("projectID") int projectID, @Param("projectScopeID") int projectScopeID);

	public void updateEditProjectScope(@Param("projectScopeModel") ProjectScopeModel projectScopeModel);

	public boolean updateEditProjectScopeDetail(
			@Param("projectScopeDetailModel") ProjectScopeDetailModel projectScopeDetailModel);

	public void updateProject(@Param("projectsTableModel") ProjectsTableModel projectsTableModel);

	public void inactiveDoc(@Param("projId") int projId);

	public void insertDocDetails(@Param("projDocMod") ProjectDocumentModel projDocMod, @Param("projId") int projId);

	public boolean updateWorkOrderPlan(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void updateWorkOrderPlanByActivityID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void updateAdhocWorkOrderByActivityID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getDemandDetailsByScope(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void updateResourceRequest(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void updateResourcePosition(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void updateWorkEffort(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void deleteBookedResources(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getBookingDetalsFlowChartDefID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void updateWoPlanIDWithNonExecutedWO(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void updateAdhocWoIDWithNonExecutedWO(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void updateWorkOrderByFlowChartDefID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void updateProjectByProjectID(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void updateDeliveryResonsible(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getBookingDetalsWoPlanID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void updateWorkOrderByWoPlanID(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getAllBookingDetalsWoPlanID(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void addExternalApplicationReference(
			@Param("externalAppReferenceModel") ExternalAppReferenceModel externalAppReferenceModel);

	public List<ExternalAppReferenceModel> getExternalApplicationReferencesByProjectId(
			@Param("projectId") int projectId);

	public ExternalAppReferenceModel getExternalApplicationReferencesByExternalProjectId(
			@Param("externalProjectId") int externalProjectId, @Param("sourceId") Integer sourceId);

	public void updateStatusOfExternalReference(@Param("isActive") boolean isActive,
			@Param("referenceId") int referenceId);

	public void updateFlowChartStepDetails(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void deleteNetworkElement(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Map<String, Object>> getExternalProjects();

	public void updateProjectExternalProject(
			@Param("externalAppReferenceModel") ExternalAppReferenceModel externalAppReferenceModel);

	public void updateProjectBlkWOCreation(
			@Param("externalAppReferenceModel") ExternalAppReferenceModel externalAppReferenceModel);

	public List<Map<String, Object>> getRequestType();

	public void updateProjectPM(@Param("projectsTableModel") ProjectsTableModel projectsTableModel);

	public String getWorkFlowName(@Param("subActivityFlowChartDefID") Integer subActivityFlowChartDefID);

	public Integer getExpertWFDefID(@Param("woName") String woName, @Param("projectID") int projectID,
			@Param("subActivityID") int subActivityID);

	public List<Map<String, Object>> getTools();

	public List<Map<String, Object>> getToolLicenseType();

	public List<Map<String, Object>> getToolLicenseOwner();

	public List<Map<String, Object>> getAccessMethod();

	public void saveProjectSpecificTools(@Param("projectToolModel") ProjectSpecificToolModel projectToolModel);

	public List<Map<String, Object>> getProjectSpecificTools(@Param("projectID") Integer projectID,
			@Param("isOnlyActiveRequired") Integer isOnlyActiveRequired);

	public void updateProjectSpecificTools(@Param("projectToolModel") ProjectSpecificToolModel projectToolModel);

	public void disableEnableTools(@Param("projectToolModel") ProjectSpecificToolModel projectToolModel);

	public void saveDeliveryAcceptance(
			@Param("deliveryAcceptanceModel") DeliveryAcceptanceModel deliveryAcceptanceModel);

	public void disableDeliveryAcceptance(@Param("deliveryAcceptanceID") Integer deliveryAcceptanceID,
			@Param("signumID") String signumID);

	public List<Map<String, Object>> getDeliveryAcceptance(@Param("projectID") int projectID);

	public boolean checkIFUserExists(@Param("projectID") int projectID, @Param("signumID") String signumID);

	public boolean checkIFUserExistsRPM(@Param("projectID") int projectID, @Param("signumID") String signumID);

	public void saveRPM(@Param("rmpModel") RPMModel rmpModel);

	public void disableRPM(@Param("rpmID") Integer rpmID, @Param("signumID") String signumID);

	public void updateDeliveryAcceptance(
			@Param("deliveryAcceptanceModel") DeliveryAcceptanceModel deliveryAcceptanceModel);

	public List<Map<String, Object>> getRPM(@Param("projectID") int projectID);

	public boolean checkIFToolExists(@Param("projectToolModel") ProjectSpecificToolModel projectToolModel);

	public boolean checkIFUserIsActive(@Param("projectID") int projectID, @Param("signumID") String signumID);

	public List<String> getRPMByProjectId(@Param("projectID") String projectId);
	/*
	 * //TODO code cleanup public List<Map<String, Object>> getDeliverableUnit();
	 */

	public boolean checkIFUnitExists(
			@Param("projectDeliverableUnitModel") ProjectDeliverableUnitModel projectDeliverableUnitModel);

	public void saveProjectDelUnit(
			@Param("projectDeliverableUnitModel") ProjectDeliverableUnitModel projectDeliverableUnitModel);

	public List<Map<String, Object>> getAllDeliverableUnit(@Param("term") String term, RowBounds rowBounds);

	public void deleteProjDelUnit(
			@Param("projectDeliverableUnitModel") ProjectDeliverableUnitModel projectDeliverableUnitModel);

	public boolean checkIfProjectActive(@Param("projectID") Integer projectID);

	public List<ProjectScopeModel> scopeByProject(@Param("projectId") Integer projectID,
			@Param("status") String status ,@Param("dataTableRequest") DataTableRequest dataTableRequest);

	public void EditDeliverableUnit(
			@Param("projectDeliverableUnitModel") ProjectDeliverableUnitModel projectDeliverableUnitModel);

	public List<Map<String, Object>> getActivtiySubActivityByScope(@Param("projectScopeId") Integer projectScopeId);

	public boolean checkDTRCASubActivityId(@Param("subActivityId") Integer subActivityId);

	public Boolean checkScopeName(@Param("projectId") Integer projectId, @Param("scopeName") String scopeName,@Param("projectScopeId") int projectScopeId);

	public List<ProjectScopeModel> getProjectScopeByScopeId(@Param("projectScopeId") Integer projectScopeId);

	public void updateProjectApprover(@Param("projectsTableModel") ProjectsTableModel projectsTableModel);

	public List<ProjectsTableModel> getProjectByProjectID(@Param("projectID") int projectID);

	public List<Map<String, Object>> getResourceRequestDataByProjecDetails(
			@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public Boolean checkifProjectScopeErisite(@Param("sourceId") String sourceId);

	public List<Integer> checkParamNotExist(@Param("projectScopeModel") ProjectScopeModel projectScopeModel);

	public int getNumberOfWorkOrders(@Param("activityName") String activityName);

	public List<Integer> getexecutionId(@Param("scopeId") List<Integer> checkParamNotExist);

	public boolean checkIfSourceExists(@Param("externalSource") ExternalSourceModel externalSource);

	public void addExternalSource(@Param("externalSource") ExternalSourceModel externalSource);

	public List<CountryModel> getCountrybyMarketAreaID(@Param("marketAreaID") Integer marketAreaID);

	public List<ProjectDetailsModel> getProjectAndScopeDetailBySignum(@Param("signum") String signum,
			@Param("role") String role, @Param("marketArea") String marketArea);

	public ProjectAllDetailsModel getProjectDetails(@Param("ProjectID") int ProjectID);

	public List<ProjectDocumentModel> getProjectDocuments(@Param("ProjectID") int ProjectID);

	public List<Map<String, Object>> getAllProjects(@Param("marketAreaID") String marketAreaID,
			@Param("countryID") String countryID, @Param("signum") String signum, @Param("status") String status,
			@Param("marketArea") String marketArea, @Param("role") String role);

	public int createProject(@Param("projectCreationModel") ProjectCreationModel projectCreationModel);

	public boolean addProjectDocuments(@Param("projectId")String projectId, @Param("projectDocument") ProjectDocumentModel projectDocument, @Param("signum") String signum);

	public boolean haveExecutionPlan(@Param("projectID")int projectID, @Param("scopeID") int scopeID);

	public List<ProjectScopeModel> activeScopeByProject(@Param("projectId")Integer projectId);
	
	public List<ScopeDomainProject> getScopeDomainByProject(@Param("projectId")Integer projectId,
			@Param("projectScopeID")Integer projectScopeID);
	
	public List<DeliveryResponsibleModel> getDeliveryResponsibleByProjectIDandSignum(@Param("projectID") int projectID,
			@Param("signumID") String signumID);

	public Map<String, Object> getActivitySubActivityNameByScopeDetailID(
			@Param("activity") String activity, @Param("subActivity") String subActivity);

	public boolean validateScopeDetailForActivitySubActivity(@Param("activityModel") ActivityModel activityModel);

	public Boolean checkIfProjectScopeExistsExceptName(@Param("projectID") int projectID,
			@Param("projectScopeId")  int projectScopeId);

	public void addInternalSource(@Param("externalSource") ExternalSourceModel externalSource);

	public List<WorkFlowFilterModel> getWorkFlowsByProjectID(@Param("projectID") int projectID, @Param("term") String term, RowBounds rowBounds);

	public List<WorkflowProficiencyModel> getEFWorkflowForSignumWFID(@Param("listofsignumIDs") List<String>  listofsignumIDs, @Param("listofWorkFlowIDs") List<Integer>  listofWorkFlowIDs  , @Param("projectID") int projectID,@Param("dataTableRequest") DataTableRequest dataTableRequest,@Param("proficiencyStatus") String proficiencyStatus);

	public void insertUserProficiencyOnUpdate(@Param("workflowProficiencyModel")  WorkflowProficiencyModel workFlowProficiencyModel,@Param("projectID") int projectID);

	public void addProficiencyAction(@Param("workFlowProficiencyModel") WorkflowProficiencyModel workFlowProficiencyModel, @Param("projectID") int projectID);

	public void updatePreviousAction(@Param("workFlowProficiencyModelList") List<WorkflowProficiencyModel> workFlowProficiencyModelList);

	public void updatePreviousActionInReset(@Param("workFlowProficiencyModel")WorkflowProficiencyModel workFlowProficiencyModel);

	public ProjectScopeModel isfProjectIdIfExists(@Param("externalReference") String externalReference, @Param("externalProjectId") Integer externalProjectId,
			 @Param("externalWorkplanTemplate") String externalWorkplanTemplate);

	public ExternalAppReferenceModel checkIfExternalProjectExistForSameIsfproject(@Param("externalProjectId")Integer externalProjectId,
			@Param("sourceId")Integer sourceId,@Param("projectId") Integer projectId);

	public void updateIsfProjectIdUploadedBy(@Param("projectID") int projectID,@Param("externalReference") String externalReference, @Param("externalProjectId") Integer externalProjectId,
			@Param("externalWorkplanTemplate") String externalWorkplanTemplate,@Param("sourceId") String sourceId,@Param("uploadedBy") String uploadedBy);

	public void updateIsfProjectIdExternal(@Param("projectID") int projectID,@Param("externalReference") String externalReference, @Param("externalProjectId") Integer externalProjectId,
			@Param("externalWorkplanTemplate") String externalWorkplanTemplate,@Param("sourceId") String sourceId,@Param("wPlanIds") String wPlanIds);

	public List<Integer> getListWoPlanID(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public List<Integer> getWorkPlanIds(@Param("projectID") int projectID,@Param("externalReference") String externalReference, @Param("externalProjectId") Integer externalProjectId,
			@Param("externalWorkplanTemplate") String externalWorkplanTemplate,@Param("sourceId") String sourceId);

	public boolean updateWorkOrderErisite(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public void updateProjectTblExternalProject(@Param("externalProjectId") Integer externalProjectId, @Param("source") String source,
			@Param("isfProjectId") int isfProjectId,@Param("externalWorkplanTemplate") String externalWorkplanTemplate);

	public boolean updateWorkOrderErisitePlanned(@Param("projectComponentModel") ProjectComponentModel projectComponentModel);

	public ProjectScopeModel chechIfScopeAlreadyMapped(@Param("projectScopeModel") ProjectScopeModel projectScopeModel,@Param("status") String status);

	public void updateExecPlanId(@Param("projectID")int projectID, @Param("externalReference")String externalReference, @Param("externalProjectId")Integer externalProjectId,
			@Param("externalWorkplanTemplate")String externalWorkplanTemplate,@Param("source") String source, @Param("wPlanIds")String wPlanIds);

	public boolean checkActiveProject(@Param("projectID") int projectID);
	
	public List<Map<String, Object>> getScopeType();
	
	public boolean isScopeTypeRequired(@Param("deliverableUnit")int deliverableUnit);
	
	public List<Map<String, Object>> getMethodForDU();
	
	public boolean isMethodRequired(@Param("deliverableUnit")int deliverableUnit);
	
    public List<Map<String, Object>> getOperatorCount();
	
	public boolean isOperatorCountRequired(@Param("deliverableUnit")int deliverableUnit);
		
    public List<Map<String, Object>> getProjectFinancials();
	
	public boolean isProjectFinancialsRequired(@Param("deliverableUnit")int deliverableUnit);
	
	public List<Map<String, Object>> downloadDeliverableData(@Param("projectId") Integer projectID,@Param("status") String status);

	public List<ProjectScopeModel> scopeByProjectV1(@Param("projectId") Integer projectID,
			@Param("status") String status);
	
	public List<AdhocTypeModel> getIDAndTypeOfActivity(@Param("activity") String activity);

	public boolean checkDrCount(@Param("projectID")int projectID, @Param("maxLimit")int maxLimit);
	
}
