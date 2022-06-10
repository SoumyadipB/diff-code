/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.ericsson.isf.model.BookingDetailsModel;
import com.ericsson.isf.model.CreateWorkOrderModel;
import com.ericsson.isf.model.CreateWorkOrderModel2;
import com.ericsson.isf.model.CreateWorkOrderNetworkElementModel;
import com.ericsson.isf.model.DOIDModel;
import com.ericsson.isf.model.DOWOModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DeleteWOListModel;
import com.ericsson.isf.model.EmployeeBasicDetails;
import com.ericsson.isf.model.ExecutionPlanDetail;
import com.ericsson.isf.model.ExecutionPlanFlow;
import com.ericsson.isf.model.ExecutionPlanModel;
import com.ericsson.isf.model.FcStepDetails;
import com.ericsson.isf.model.FinalRecordsForWOCreationModel;
import com.ericsson.isf.model.InProgressNextStepModal;
import com.ericsson.isf.model.InProgressTaskModel;
import com.ericsson.isf.model.MailModel;
import com.ericsson.isf.model.NextStepModel;
import com.ericsson.isf.model.NextSteps;
import com.ericsson.isf.model.ProjectModel;
import com.ericsson.isf.model.ProjectNodeTypeModel;
import com.ericsson.isf.model.ServerTimeModel;
import com.ericsson.isf.model.WOInputFileModel;
import com.ericsson.isf.model.WOOutputFileModel;
import com.ericsson.isf.model.WOOutputFileResponseModel;
import com.ericsson.isf.model.WorkOrderCompleteDetailsModel;
import com.ericsson.isf.model.WorkOrderInputFileModel;
import com.ericsson.isf.model.WorkOrderModel;
import com.ericsson.isf.model.WorkOrderOutputFileModel;
import com.ericsson.isf.model.WorkOrderPlanModel;
import com.ericsson.isf.model.WorkOrderPlanModel2;
import com.ericsson.isf.model.WorkOrderPlanNodesModel;
import com.ericsson.isf.model.WorkOrderViewProjectModel;

/**
 *
 * @author eguphee
 */
public interface WorkOrderMapper {

	public void deleteWorkOrderPlan(@Param("wOrderPlanModel") WorkOrderPlanModel wOPlanObject);

	public List<WorkOrderPlanModel> getWorkOrderPlanDetails(@Param("woPlanID") int woPlanID,
			@Param("projectId") int projectId, @Param("signumID") String signumID);

	public WorkOrderPlanModel getPlanDetailsById(@Param("woPlanID") int woPlanID);

	public List<WorkOrderViewProjectModel> getWorkOrderViewDetails(@Param("projectID") String projectID,
			@Param("scope") String scope, @Param("activity") String activity, @Param("WOID") String WOID,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("assignedTo") String assignedTo, @Param("signum_LoggedIn") String signum_LoggedIn,
			@Param("isWeekEndIncluded") boolean isWeekEndIncluded,
			@Param("isOddHoursIncluded") boolean isOddHoursIncluded, @Param("status") String status,
			@Param("nodeName") String nodeName, @Param("marketArea") String marketArea,
			@Param("assignedBy") String assignedBy);

	public List<Map<String, Object>> getNEIDByProjectID(@Param("projectID") int projectID, @Param("term") String term,
			RowBounds rowBounds);

	public List<WorkOrderModel> checkNotStartedStatusOfWorkOrderPlan(@Param("wOPlanId") int wOPlanId);

	public void updateWorkOrderAndWorkOrderNodes(@Param("wOrderPlanModel") WorkOrderPlanModel wOPlanObject);

	public void updateWorkOrderPlanAndWOPlanNodes(@Param("wOrderPlanModel") WorkOrderPlanModel wOPlanObject);

	public int getEstdHrs(@Param("projectID") int projectID, @Param("subActId") int subActId);

	public List<WorkOrderPlanModel> searchWorkOrderPlanDetails(@Param("projectID") int projectID,
			@Param("domainID") int domainID, @Param("serviceAreaID") int serviceAreaID,
			@Param("technologyID") int technologyID, @Param("activityName") String activityName,
			@Param("subActivityID") int subActivityID);

	public List<WorkOrderPlanModel2> getWorkOrderPlans(@Param("projectID") int projectID,
			@Param("dataTableReq") DataTableRequest dataTableReq,
			@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("woStatus") String woStatus);

	public Integer getWorkOrderPlansCount(@Param("projectID") int projectID,
			@Param("dataTableReq") DataTableRequest dataTableReq, @Param("startDate") String startDate,
			@Param("endDate") String endDate, @Param("woStatus") String woStatus);

	public void deleteWorkOrder(@Param("wOID") int wOID, @Param("signumID") String signumID);

	public void deleteWONodes(@Param("wOID") int wOID);

	public Boolean checkNotStartedStatusOfWorkOrder(@Param("wOID") int wOID);

	public void updateWorkOrder(@Param("workOrderModel") WorkOrderModel workOrderModel);

	public Boolean updateWorkOrderParentId(@Param("parentWorkOrderID") int parentWorkOrderID, @Param("woid") int woid);

	public Boolean updatePreviousWOID(@Param("workOrderId") int workOrderId);

	public Boolean closeDeferedWorkOrder(@Param("workOrderId") int workOrderId);

	public Boolean setInprogressDeferedWorkOrder(@Param("workOrderId") int workOrderId);

	public Boolean setAssignedDeferedWorkOrder(@Param("workOrderId") int workOrderId);

	public List<String> getDeferedWorkOrderDetails(@Param("workOrderId") int workOrderId);

	public List<WorkOrderModel> getWorkOrderDetails(@Param("woID") int wOID);

	public WorkOrderModel getWorkOrderDetailsById(@Param("woID") int wOID);

	public void insertNodeDetails(@Param("woid") int woid, @Param("nodeType") String nodeType,
			@Param("nodeName") String nodeName);

	public void deleteNodeforWO(@Param("woid") int woid);

	public void transferWorkOrder(@Param("wOID") int wOID, @Param("senderID") String senderID,
			@Param("receiverID") String receiverID);

	public void massUpdateWorkOrder(@Param("wOID") int wOID, @Param("senderID") String senderID,
			@Param("receiverID") String receiverID, @Param("plannedEndDate") Date plannedEndDate,
			@Param("plannedStartDate") Date plannedStartDate

	);

	public void sendSuccessMail(@Param("woIDs") String woIDs, @Param("senderID") String senderID,
			@Param("receiverID") String receiverID);

	// Removing WO Duplicate functionality from 9.0
	/*
	 * public void createDuplicateWorkOrder(@Param("wOID") String wOID,
	 * 
	 * @Param("signum") String signum,
	 * 
	 * @Param("projectID") String projectID,
	 * 
	 * @Param("comment") String comment);
	 */
	public List<String> getPriority();

	public List<String> getNodeType(@Param("countryCustomerGroupID") Integer countryCustomerGroupID);

	public List<String> getMarketArea(@Param("countryCustomerGroupID") int countryCustomerGroupID, @Param("nodeType") String nodeType,
			@Param("type") String type);

	public List<String> getVendor(@Param("countryCustomerGroupID") int countryCustomerGroupID, @Param("marketArea") String marketArea,
			@Param("nodeType") String nodeType);

	public List<ProjectNodeTypeModel> getNodeNames(@Param("projectID") int projectID,
			@Param("elementType") String elementType, @Param("type") String type);

	public List<ProjectNodeTypeModel> getNodeNamesFilter(@Param("projectID") int projectID,
			@Param("elementType") String elementType, @Param("type") String type, @Param("vendor") String vendor,
			@Param("market") String market, @Param("tech") String tech, @Param("domain") String domain,
			@Param("subDomain") String subDomain);

	public List<String> getNodeNamesFilterValidate(@Param("projectID") int projectID,
			@Param("elementType") String elementType, @Param("type") String type, @Param("vendor") String vendor,
			@Param("market") String market, @Param("tech") String tech, @Param("domain") String domain,
			@Param("subDomain") String subDomain, @Param("nodeNames") String nodeNames);

	public List<ProjectNodeTypeModel> getNodeNamesForSite(@Param("projectID") int projectID,
			@Param("type") String type);

	public List<ProjectNodeTypeModel> getNodeNamesForSiteFilter(@Param("projectID") int projectID,
			@Param("type") String type, @Param("vendor") String vendor, @Param("market") String market,
			@Param("tech") String tech, @Param("domain") String domain, @Param("subDomain") String subDomain);

	public List<String> getNodeNamesForSiteFilterValidate(@Param("projectID") int projectID, @Param("type") String type,
			@Param("vendor") String vendor, @Param("market") String market, @Param("tech") String tech,
			@Param("domain") String domain, @Param("subDomain") String subDomain, @Param("nodeNames") String nodeNames);

	public List<ProjectNodeTypeModel> getNodeNamesForSector(@Param("projectID") int projectID,
			@Param("type") String type);

	public List<ProjectNodeTypeModel> getNodeNamesForSectorFilter(@Param("projectID") int projectID,
			@Param("type") String type, @Param("vendor") String vendor, @Param("market") String market,
			@Param("tech") String tech, @Param("domain") String domain, @Param("subDomain") String subDomain);

	public List<String> getNodeNamesForSectorFilterValidate(@Param("projectID") int projectID,
			@Param("type") String type, @Param("vendor") String vendor, @Param("market") String market,
			@Param("tech") String tech, @Param("domain") String domain, @Param("subDomain") String subDomain,
			@Param("nodeNames") String nodeNames);

	public Boolean checkWOStatus(@Param("wOID") Integer t);

	public void updateTransferWOLOG(@Param("wOID") Integer t, @Param("receiverID") String receiverID,
			@Param("senderID") String senderID);

	public void addWorkOrderPlan(@Param("wOrderPlanModel") CreateWorkOrderModel wOPlanObject,
			@Param("signumID") String signum);

	public void addWorkOrderPlanNodes(@Param("createWorkOrderModel") CreateWorkOrderModel wOPlanObject,
			@Param("nodeType") String nodeType, @Param("nodeName") String nodeName);

	public void createWorkOrder(@Param("createWorkOrderModel") CreateWorkOrderModel2 woPlanModel);

	public boolean checkIFWOExists(@Param("woid") int woid);

	public List<ProjectModel> getProjectBySignum(@Param("signum") String signum,
			@Param("marketArea") String marketArea, @Param("role") String role);

	public MailModel getWoMailNotificationDetails(@Param("woID") int woID);

	public List<String> getEmailIDs(@Param("lstSignumID") List<String> lstSignumID);

	public Map<String, String> getCreatorEmailID(@Param("createdBy") String createdBy);

	public void SendMailNotification(@Param("senderSignumID") String senderSignumID, @Param("body") String body,
			@Param("receiverSignumID") String receiverSignumID);

	public Map<String, Integer> getWorkFlowVersion(@Param("projectID") int projectID,
			@Param("subActivityID") int subActivityID);

	public int getWorkFlowVersionWhileUpload(@Param("projectID") int projectID,
			@Param("subActivityID") int subActivityID);

	public List<Integer> getWorkFlowVersionList(@Param("projectID") int projectID,
			@Param("subActivityID") int subActivityID);

	public void updateWOWFVersion(@Param("woID") int woID, @Param("wfVersion") int wfVersion,
			@Param("workFlowDefID") int workFlowDefID);

	public List<ProjectNodeTypeModel> getClusterNames(@Param("projectID") int projectID, @Param("type") String type);

	public List<ProjectNodeTypeModel> getClusterNamesFilter(@Param("projectID") int projectID,
			@Param("type") String type, @Param("vendor") String vendor, @Param("market") String market,
			@Param("tech") String tech, @Param("domain") String domain, @Param("subDomain") String subDomain);

	public List<ProjectNodeTypeModel> getClusterNamesByFilter(@Param("projectID") int projectID,
			@Param("type") String type, @Param("vendor") String vendor, @Param("market") String market,
			@Param("tech") String tech, @Param("domain") String domain, @Param("subDomain") String subDomain,
			@Param("term") String term, RowBounds rowBounds);

	public List<String> getClusterNamesFilterValidate(@Param("projectID") int projectID, @Param("type") String type,
			@Param("vendor") String vendor, @Param("market") String market, @Param("tech") String tech,
			@Param("domain") String domain, @Param("subDomain") String subDomain, @Param("nodeNames") String nodeNames);

	public int getEstdHrsForScope(@Param("projectID") int projectID, @Param("subActivityID") int subActivityID,
			@Param("scopeID") int scopeID);

	public int getLatestWorkFlowVersion(@Param("projectID") int projectID, @Param("subActivityID") int subActivityID);

	public List<Map<String, Object>> getDomainDetailsByWOPlanID(@Param("woPlanId") int woPlanId);

	public List<Map<String, Object>> getPlanDataByWOPlanID(@Param("woPlanId") int woPlanId);

	public List<Map<String, Object>> getNodeTypeByWOPlanID(@Param("woPlanId") int woPlanId);

	public List<Map<String, Object>> getNodeNamesByWOPlanID(@Param("woPlanId") int woPlanId);

	public List<Map<String, Object>> getAssignedUsersByWOPlanID(@Param("woPlanId") int woPlanId);

	public void updateWorkOrderPlanStatus(@Param("workOrderPlanID") int workOrderPlanID);

	public void updateWorkOrderStatusNew(@Param("workOrderPlanID") int workOrderPlanID);

	public void updateWorkOrderStatus(@Param("woID") int woID, @Param("signumID") String updatedBY,
			@Param("status") String status);

	public List<Map<String, Object>> getCheckBoxData(@Param("woPlanId") int woPlanId);

	public List<Map<String, Object>> getSourcesForMapping();

	public List<Map<String, Object>> getSourcesForPlan(@Param("projectId") int projectId);

	public void uploadFileForWOCreation(@Param("projectID") Integer projectID, @Param("createdBy") String createdBy,
			@Param("fileName") String fileName);

	public int saveExecutionPlan(@Param("executionPlanModel") ExecutionPlanModel executionPlanModel);

	public int saveExecutionPlanDetails(@Param("executionPlanModel") ExecutionPlanModel executionPlanModel);

	public void setInactiveExistingExecPlan(@Param("projectId") Integer projectId, @Param("planName") String planName);

	public List<ExecutionPlanModel> getExecutionPlansByProjectId(@Param("projectId") int projectId);

	public List<ExecutionPlanDetail> getExecutionPlanDetailsByExecutionPlanId(
			@Param("executionPlanId") int executionPlanId);

	public ExecutionPlanDetail getExecutionPlanDetailsByExecutionPlanDetailId(
			@Param("executionPlanDetailId") long executionPlanDetailId);

	public ExecutionPlanDetail getExecutionPlanDetailsByTaskNumber(@Param("taskNumber") long taskNumber);

	public void updateExecutionPlanDetails(@Param("executionPlanModel") ExecutionPlanModel executionPlanModel);

	public void deleteExecutionPlanDetails(@Param("executionPlanModel") ExecutionPlanModel executionPlanModel);

	public List<ExecutionPlanModel> getActiveExecutionPlans(
			@Param("executionPlanModel") ExecutionPlanModel executionPlanModel);

	public List<ExecutionPlanModel> searchExecutionPlans(@Param("subactivityId") Integer subactivityId,
			@Param("workFlowVersionNo") Integer workFlowVersionNo);

	public ExecutionPlanModel getActiveExecutionPlanByid(@Param("executionPlanId") long executionPlanDetailId);

	public void updateExecutionPlanStatus(@Param("executionPlanId") int executionPlanId,
			@Param("isActive") boolean isActive, @Param("userName") String userName);

	public List<WorkOrderPlanModel> checkProjectEditByProjID(@Param("projectID") int projectID);

	public int updateExecutionPlan(@Param("executionPlanModel") ExecutionPlanModel executionPlanModel);

	public int saveExecutionPlanFlow(@Param("executionPlanFlow") ExecutionPlanFlow executionPlanFlow);

	public ExecutionPlanFlow getExecutionPlanFlowByGroupId(@Param("execPlanGroupId") long execPlanGroupId,
			@Param("executionPlanDetailId") long executionPlanDetailId);

	public void markExecutionPlanFlowComplete(@Param("execPlanGroupId") long execPlanGroupId,
			@Param("executionPlanDetailId") long executionPlanDetailId);

	public void markExecutionPlanFlowStepComplete(@Param("execPlanGroupId") long execPlanGroupId,
			@Param("executionPlanDetailId") long executionPlanDetailId,
			@Param("executionPlanFlowId") long executionPlanFlowId);

	public ExecutionPlanFlow getExecutionFlowByWoid(@Param("woid") int woid);

	public List<String> isPlanInExecution(@Param("executionPlanId") long executionPlanId);

	public ExecutionPlanModel getExecutionPlandDetilsbyWoPlanId(@Param("planid") String planid);

	public void updateExecutionPlanFlowWoId(@Param("woId") int woid, @Param("newWoId") int newWoId,
			@Param("newPlanId") int newPlanId);

	public Integer getWOIdbyWoplanID(@Param("woPlanId") int woPlanId);

	public List<LinkedHashMap<String, Object>> getInprogressTask(@Param("signum") String signum);

	public Double getWoTotalTime(@Param("woid") String woid);

	public List<ServerTimeModel> getServerTimeByTaskID(@Param("listTaskID") String listTaskID,
			@Param("listStepID") String listStepID, @Param("listWOID") String listWOID);

	public List<LinkedHashMap<String, Object>> getBookingDetails(@Param("WoID") int WoID,
			@Param("SignumID") String SignumID);

	public int isPlanEditable(@Param("woPlanId") int woPlanId);

	public List<Map<String, Object>> getPlannedDateForWoPlanID(@Param("woPlanId") int woPlanID);

	public List<Map<String, Object>> getBotIDByWFSignum(@Param("signum") String signum);

	public List<Map<String, Object>> getWoIDByProjectID(@Param("projectID") String projectID,
			@Param("subactivityID") String subactivityID,
			@Param("subActivityFlowChartDefID") String subActivityFlowChartDefID);

	public String getWoStartDate(@Param("getwOID") int getwOID);

	public void updateWoStartDateByCurrentDate(@Param("getwOID") int getwOID);

	public void updateWoStartDates(@Param("getwOID") int getwOID, @Param("startDate") String startDate);

	public List<Map<String, Object>> getWorkOrderDetailsByName(@Param("woName") String woName,
			@Param("signum") String signum);

	public List<Map<String, Object>> getAdditionalInfoOfPlan(@Param("WOPlanID") int woPlanId);

	public List<NextSteps> getNextStepData(@Param("stepID") String stepID, @Param("defID") Integer defID);
	public List<NextStepModel> getNextStepDataModel(@Param("stepID") String stepID, @Param("defID") Integer defID);

	public List<String> getWorkOrderNodes(@Param("woID") int woId);

	public int getWorkFlowDefID(@Param("projectID") int projectID, @Param("subActivityID") int subActivityID,
			@Param("workFlowVersionNo") int workFlowVersionNo, @Param("wfid") String wfid);

	public int getFlowChartDefID(@Param("woID") int woID, @Param("workFlowVersionNo") int workFlowVersionNo);

	public List<String> getWoMailNotificationDetailsForDAC(@Param("woID") int woID);

	public List<ProjectNodeTypeModel> getNodeNamesBySiteFilter(@Param("projectID") int projectID,
			@Param("vendor") String vendor, @Param("market") String market, @Param("tech") String tech,
			@Param("domain") String domain, @Param("subDomain") String subDomain, @Param("term") String term,
			RowBounds rowBounds);

	public List<ProjectNodeTypeModel> getNodeNamesBySectorFilter(@Param("projectID") int projectID,
			@Param("vendor") String vendor, @Param("market") String market, @Param("tech") String tech,
			@Param("domain") String domain, @Param("subDomain") String subDomain, @Param("term") String term,
			RowBounds rowBounds);

	public List<ProjectNodeTypeModel> getNodeNamesByFilter(@Param("projectID") int projectID,
			@Param("elementType") String elementType, @Param("type") String type, @Param("vendor") String vendor,
			@Param("market") String market, @Param("tech") String tech, @Param("domain") String domain,
			@Param("subDomain") String subDomain, @Param("term") String term, RowBounds rowBounds);

	public List<Map<String, Object>> getLatestVersionOfWfBySubactivityID(@Param("projectID") String projectID,
			@Param("subactivityID") String subactivityID,
			@Param("subActivityFlowChartDefID") String subActivityFlowChartDefID,
			@Param("workFlowName") String workFlowName, @Param("wfid") int wfid);

	public Boolean checkNotStartedStatusOfWorkOrder1(@Param("woid") List<Integer> woid);

	public void deleteWONodes1(@Param("deleteWOListModel") DeleteWOListModel deleteWOListModel);

	public void deleteWorkOrder1(@Param("deleteWOListModel") DeleteWOListModel deleteWOListModel);

	public EmployeeBasicDetails getEmpInfo(@Param("lastModifiedBy") String lastModifiedBy);

	public List<Integer> getWOPlanList(@Param("woid") List<Integer> woid);

	public Boolean checkPlanStatus(@Param("woplan") int woplan);

	public void inactiveWOPlanList(@Param("woplanlist") List<Integer> woplanlist);
	
	public void inactiveWOPlan(@Param("woplan") int woplan);

	public int getWOPlanByWOID(@Param("wOID") int wOID);

	public void updateWoFcDefIdForAssignedWo(@Param("flowchartDefId") int flowchartDefId,
			@Param("newVersion") int newVersion, @Param("projectId") int projectId,
			@Param("subActivityId") int subActivityId, @Param("workFlowName") String workFlowName,
			@Param("workFlowId") int workFlowId);

	public List<FcStepDetails> getStepBookingDetailsByWoidV1(@Param("woId") Integer woId);
	/* Replica getStepBookingDetailsByWoid
	 * */
	public List<Map<String, Object>> getStepBookingDetailsByWoid(@Param("woId") Integer woId);

	public boolean addDeliverablePlanMapping(@Param("executionPlanId") Integer executionPlanId,
			@Param("scopeId") Integer scopeId, @Param("currentUser") String currentUser);

	public Integer getExecutionPlanIdByScopeId(@Param("scopeId") Integer scopeId);

	public void insertOutputFileWO(
			@Param("workOrderOutputFileModel") WorkOrderOutputFileModel workOrderOutputFileModel);

	public void insertInputFileWO(@Param("inputFile") WorkOrderInputFileModel inputFile);

	public List<WOOutputFileResponseModel> getWOOutputFile(@Param("woid") int woid);

	public List<WOInputFileModel> getWOInputFile(@Param("woid") int woid);

	public int getCountOfUnassignedWOByWOPLAN(@Param("woplanid") int woplanid);

	public void deleteDeliverablePlanMapping(@Param("scopeId") Integer scopeId);

	public boolean checkIfStepInStartedState(@Param("woid") int woid);

	public void deleteInputFile(@Param("id") int id, @Param("signumID") String signumID);

	public void deleteOutputFile(@Param("id") int id, @Param("signumID") String signumID);

	public void editOutputFile(@Param("workOrderOutputFileModel") WorkOrderOutputFileModel workOrderOutputFileModel);

	public void deleteOutputFile1(@Param("workOrderOutputFileModel") WorkOrderOutputFileModel workOrderOutputFileModel);

	public void deleteInputFile1(@Param("workOrderInputFileModel") WorkOrderInputFileModel workOrderInputFileModel);

	public List<String> checkNameIsPresentOrNot(@Param("file") List<WOOutputFileModel> file, @Param("woid") int woid);

	public void updateExecutionPlanName(@Param("newPlanName") String newPlanName,
			@Param("oldPlanName") String oldPlanName, @Param("projectId") int projectId);

	public List<String> getPriorityByName(@Param("priority") String priority);

	public boolean isExternalSourceExists(@Param("source") String source);

	public boolean isValidNodeNameAndNodeType(@Param("nodeName") String nodeName, @Param("nodeType") String nodeType,
			@Param("projectID") int projectID);

	public void createWorkOrder2(@Param("createWorkOrderModel") CreateWorkOrderModel2 createWorkOrderModel);

	public void createNodesForSingleWorkOrder(@Param("wOPlanID") int wOPlanID, @Param("nodeNames") String[] nodeNames);

	public void editWOPriority(@Param("woid") int woid, @Param("priority") String priority,
			@Param("signumID") String signumID);

	public List<Map<String, Object>> getWorkOrdersByProjectID(RowBounds rowBounds, @Param("projectID") String projectID,
			@Param("woName") String woName, @Param("projectScopeID") String projectScopeID,
			@Param("assignedBy") String assignedBy, @Param("assignedTo") String assignedTo,
			@Param("nodeName") String nodeName, @Param("marketArea") String marketArea, @Param("role") String role,
			@Param("signum") String signum);

	public List<Map<String, Object>> getWFListForDeliverablePlan(@Param("executionPlanId") Integer executionPlanId);

	public List<FinalRecordsForWOCreationModel> getWorkOrderModelForBulkCreation(@Param("source") String source);

	public void deleteProcessedDataFromWOCreationTable(@Param("woHistoryID") int woHistoryID);

	public void createDOID(@Param("doID") DOIDModel doID);

	public boolean saveEditedWorkOrderDetails(
			@Param("oldWorkOrderDetails") WorkOrderCompleteDetailsModel oldWorkOrderDetails,
			@Param("plannedStartDate") Date plannedStartDate,
			@Param("plannedEndDate")Date plannedEndDate);

	public Integer getDeliverableIdByNameAndProjectID(@Param("deliverableName") String deliverableName,
			@Param("projectID") int projectID);

	public void markExecutionPlanFlowComplete(@Param("flowDetails") ExecutionPlanFlow flowDetails);

	public void markWoHistoryIDCompleted(@Param("woHistoryID") int woHistoryID,@Param("modifiedBy") String modifiedBy);

	public void insertWOCreationTable(@Param("woPlanObject") CreateWorkOrderModel woPlanObject,
			@Param("createWorkOrderModel2List") List<CreateWorkOrderModel2> createWorkOrderModel2List);

	public List<Integer> getWorkOrdersByWoplanid(@Param("woplnid") int woplnid);

	public void createNodesForWorkOrder(@Param("createWorkOrderModel") CreateWorkOrderModel2 createWorkOrderModel2,
			@Param("nodeNames") String[] nodeNames);

	public ExecutionPlanFlow getExecutionPlanFlowByDOID(@Param("doID") int doID,@Param("executionPlanDetailId") int executionPlanDetailId);

	public void insertBulkWoErrorTable(@Param("woPlanObject") CreateWorkOrderModel woPlanObject, @Param("errorDetails") String errorDetails, @Param("ERROR_CATEGORY") String ERROR_CATEGORY);

	public List<WorkOrderPlanNodesModel> getNodesByWOId(@Param("workOrderId")int workOrderId);

	public void deleteWorkOrderFromExecutionPlanFlow(@Param("parentWorkOrderID") int parentWorkOrderID);

	public void updateWoPlanStartEndDateAndTime(@Param("woPlanID") int woPlanID);

	public List<DOWOModel> getWorkOrdersByDoid(@Param("doid") int doid);

	public List<ProjectNodeTypeModel> getNodeNamesForDeliverable(@Param("projectID")int projectID, @Param("elementType") String elementType, @Param("type") String type,
			@Param("vendor") String vendor, @Param("market") String market, @Param("techCommaSeparated") String techCommaSeparated, @Param("domainCommaSeparated") String domainCommaSeparated, @Param("term") String term);

	public List<Map<String, Object>> getActivity(@Param("subActivityID") int subActivityID);

	public List<String> getNodeValidateForDeliverable(@Param("projectID")int projectID,@Param("elementType") String elementType,@Param("type") String type,@Param("vendor") String vendor,
			@Param("market") String market, @Param("techCommaSeparated") String techCommaSeparated, @Param("domainCommaSeparated") String domainCommaSeparated, @Param("nodeNamesCommaSeparated")String nodeNamesCommaSeparated);

	public List<String> getValidateJsonForExternalSource(@Param("externalSourceName") String externalSourceName);

	public List<Map<String, Object>> getExecutionPlanDetailsByProjectIDSubactivityID(@Param("projectID") Integer projectID,
			@Param("subActivityID") Integer subActivityID, @Param("subActivityFlowChartDefID") Integer subActivityFlowChartDefID);
	
	public List<WorkOrderOutputFileModel> getWOOutputFileDetails(@Param("woid") int woid);

	public List<WorkOrderOutputFileModel> getWOOutputFileDetailsByWoIDAndWoOutputFileModel(
			@Param("woOutputFileModel") WOOutputFileModel woOutputFileModel, @Param("woid") int woid);
	public List<LinkedHashMap<String, Object>> getAllBookingDetails(@Param("WoID") int WoID,
			@Param("SignumID") String SignumID, @Param("flag") boolean flag, @Param("stepId") String stepId);

	public int checkOutputFileCount(@Param("woOutputFileModel") WorkOrderOutputFileModel woOutputFileModel);

	public String validateStepIdAndExecutionType(@Param("flowChartStepId") String flowChartStepId, @Param("flowChartDefID") int flowChartDefID);

	public void updateDefIdForAssignWo(@Param("oldDefID")int oldDefID);

	public void updateWoAutoSense(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID,
			 @Param("flowcharDefId") int flowcharDefId);

	public List<InProgressTaskModel> getInprogressTaskBySignum(@Param("signum") String signum);

	public InProgressNextStepModal getNextStepInfoWithProficiency(@Param("flowChartStepID") String flowChartStepID,@Param("flowChartDefID") int flowChartDefID, @Param("proficiencyID") int proficiencyID);


	public List<String> getValidateJsonForApi(@Param("apiName") String apiName,@Param("externalSourceName") String externalSourceName);

	public boolean validateFailureReason(@Param("reason") String reason, @Param("category") String category);

	public List<String> getStepWOIDBookings(@Param("wOID") int wOID,@Param("stepid") String stepid);

	public String getExecutionType(@Param("stepid")String stepid, @Param("taskID") int taskID,@Param("flowChartDefID") int flowChartDefID);

	public boolean validateStepIDforStepType(@Param("flowChartStepId")String flowChartStepId,@Param("flowChartDefId") int flowChartDefId);

	public int getTaskIdONStepIdAndFlowChartdefId(@Param("flowChartStepId") String flowChartStepId, @Param("flowChartDefID") int flowChartDefID);

	public String getPreviousStepStepId(@Param("flowChartStepId") String flowChartStepId, @Param("flowChartDefID") int flowChartDefID);

	public boolean checkPreviousStepCompleted(@Param("previousStepStepId")String previousStepStepId,@Param("flowChartDefID") int flowChartDefID,@Param("woId") int woId,@Param("executionType") String executionType);

	public boolean checkDecisionStepComplitionStatus(@Param("previousStepStepId")String previousStepStepId,@Param("flowChartDefID") int flowChartDefID,@Param("woId") int woId);

	public boolean checkStepWithDecisionValue(@Param("previousStepStepId")String previousStepStepId,@Param("flowChartDefID") int flowChartDefID,@Param("woId") int woId, 
			@Param("decisionValue") String decisionValue);

	public BookingDetailsModel getBookingDetailsOnWoidTaskIdDefIdStepId(@Param("woId") int woId,@Param("taskID") int taskID,@Param("flowchartdefid") int flowchartdefid,
			@Param("stepID") String stepID);

	public String getLatestConpletedEnabledStep(@Param("woID") int woID);

	public boolean checkIfNextStepDisabled(@Param("flowChartStepID") String flowChartStepID, @Param("flowChartDefID") int flowChartDefID, @Param("workOrderProficiencyLevel") int workOrderProficiencyLevel);

	public InProgressNextStepModal getNextStepInfoForSignumId(@Param("flowChartStepID") String flowChartStepID,@Param("flowChartDefID") int flowChartDefID);

	public String checkWorkOrderForSrRequest(@Param("listofWoid")  List<Integer> listofWoid);

	public Integer checkWorkOrderLinkedWithSRID( @Param("wOID") int wOID);
	
	public boolean validateWoid(@Param("wOID")int wOID);
	
	public boolean validateWOStatusForWOName(@Param("wOID")int wOID);
	
	public boolean validateWOStatusForStartDate(@Param("wOID")int wOID);
	
	public boolean validateSource(@Param("externalSourceName")String externalSourceName);
	
	public boolean validateSignumForPMDR(@Param("signumID")String signumID,@Param("projectID")int projectID);
	
	public boolean validateExternalSourceForErisite(@Param("externalSourceName")String externalSourceName);
	
	public boolean validateExternalSourceForErisiteWO(@Param("createdBy")String createdBy);

	public String getCreatedByOfWOID(@Param("wOID") int wOID);
	
	public void addNetworkWorkOrderPlan(@Param("wOrderPlanModel") CreateWorkOrderNetworkElementModel wOPlanObject,
			@Param("signumID") String signum);

	public void createWorkOrderNodes(@Param("createWorkOrderModel") CreateWorkOrderModel2 createModel, @Param("listOfNode") List<WorkOrderPlanNodesModel> listOfNode);

	public List<ProjectModel> getProjectBySignumForProjectQueue(@Param("signum") String signum);

    public boolean validateLastModifiedByForPMDRNE(@Param("logedInSignum") String logedInSignum, @Param("projectId") int projectId, @Param("woid") int woid);

	public boolean validateLastModifiedForPMDRBookedResource(@Param("lastModifiedBy") String lastModifiedBy, @Param("projectID") int projectID);

	public boolean checkNotStartedStatusOfWorkOrderV1(@Param("wOID") int wOID);
	
	}