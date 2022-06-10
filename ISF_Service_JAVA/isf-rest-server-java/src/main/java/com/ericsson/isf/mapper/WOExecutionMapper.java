/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.model.AdhocWorkOrderCreationModel;
import com.ericsson.isf.model.AdhocWorkOrderModel;
import com.ericsson.isf.model.BookingDetailsModel;
import com.ericsson.isf.model.BulkWOCreationDetailsModel;
import com.ericsson.isf.model.ChildStepDetailsModel;
import com.ericsson.isf.model.CurrentWorkOrderModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DeliverableSuccessReasonModel;
import com.ericsson.isf.model.DescisionStepDetailsModel;
import com.ericsson.isf.model.EventPublisherURLMappingModel;
import com.ericsson.isf.model.ExternalExecutionDetailsModel;
import com.ericsson.isf.model.FcStepDetails;
import com.ericsson.isf.model.FlowChartDefModel;
import com.ericsson.isf.model.FlowChartReverseTraversalModel;
import com.ericsson.isf.model.InProgressWorkOrderModel;
import com.ericsson.isf.model.LastStepDetailsModel;
import com.ericsson.isf.model.NextStepModel;
import com.ericsson.isf.model.ProficiencyTypeModal;
import com.ericsson.isf.model.ProjectQueueWorkOrderBasicDetailsModel;
import com.ericsson.isf.model.RpaModel;
import com.ericsson.isf.model.SaveClosureDetailsForWOModel;
import com.ericsson.isf.model.SaveWfUserProfResponseModel;
import com.ericsson.isf.model.SearchPlannedWOProjectModel;
import com.ericsson.isf.model.SharePointDetailModel;
import com.ericsson.isf.model.SourceSystemDetailsModel;
import com.ericsson.isf.model.StepDetailsModel;
import com.ericsson.isf.model.StepTaskModel;
import com.ericsson.isf.model.UserLoginModel;
import com.ericsson.isf.model.UserWorkFlowProficencyModel;
import com.ericsson.isf.model.WOWorkFlowModel;
import com.ericsson.isf.model.WoCompleteDetailsModel;
import com.ericsson.isf.model.WorkFlowBookingDetailsModel;
import com.ericsson.isf.model.WorkFlowDetailsModel;
import com.ericsson.isf.model.WorkFlowStepBookingDetailsModel;
import com.ericsson.isf.model.WorkFlowStepDataModel;
import com.ericsson.isf.model.WorkFlowStepDetailsModel;
import com.ericsson.isf.model.WorkFlowStepLinksDetailModel;
import com.ericsson.isf.model.WorkFlowUserProficiencyModel;
import com.ericsson.isf.model.WorkOrderBasicDetailsModel;
import com.ericsson.isf.model.WorkOrderColumnMappingModel;
import com.ericsson.isf.model.WorkOrderCompleteDetailsModel;
import com.ericsson.isf.model.WorkOrderDetailsModel;
import com.ericsson.isf.model.WorkOrderDetailsSearchModel;
import com.ericsson.isf.model.WorkOrderFailureReasonModel;
import com.ericsson.isf.model.WorkOrderModel;
import com.ericsson.isf.model.WorkOrderNodesModel;
import com.ericsson.isf.model.WorkOrderNodes;
import com.ericsson.isf.model.WorkOrderProgressModel;
import com.ericsson.isf.model.WorkOrderViewDetailsByWOIDModel;
import com.ericsson.isf.model.WorkflowStepDetailModel;
import com.ericsson.isf.model.botstore.ServerBotModel;

/**
 *
 * @author esanpup
 */
public interface WOExecutionMapper {

	public List<WorkOrderDetailsSearchModel> searchPlannedWorkOrders(@Param("signum") String signum, @Param("woStatus") String woStatus,@Param("startDate") String startDate,@Param("endDate") String endDate ,@Param("dataTableRequest") DataTableRequest dataTableRequest);

    public List<AdhocWorkOrderModel> getAdhocWorkOrderDetails(@Param("signumID") String signumID);

    public List<AdhocWorkOrderModel> getAdhocWorkOrderDetailsByID(@Param("adhocWOID") int adhocWOID, @Param("signumID") String signumID);

    public void saveFeedback(@Param("signumID") String signumID,
            @Param("feedback") String feedback);

    public Boolean checkIFWOIDExists(@Param("woID") int woID);

    public void updateWorkOrderStatus(@Param("woID") int woID,
            @Param("signumID") String signumID,
            @Param("status") String status,
            @Param("comment") String comment);

    public void startWorkorder(@Param("woId") int woID,@Param("signum") String signum);



    public void updateStatusWO(@Param("wOID") int wOID);

    public void startWorkOrderTask(@Param("wOID") int wOID, @Param("taskID") int taskID, @Param("signum") String signum);

    public void stopWorkOrderTask(@Param("wOID") String wOID, @Param("taskID") String taskID, @Param("signum") String signum, @Param("bookingID") int bookingID);

    public List<InProgressWorkOrderModel> getInProgressWorkOrderDetails(@Param("signumID") String signumID);
    public List<WorkflowStepDetailModel> getWorkflowStepDetails(@Param("subActivityFlowChartDefID") int subActivityFlowChartDefID);
    public List<WorkflowStepDetailModel> getWorkflowStepDetailsForComments(@Param("subActivityFlowChartDefID") int subActivityFlowChartDefID);


    public WorkflowStepDetailModel getStepDetailsByFcDefId(@Param("stepID") String stepID,@Param("fcDefId") String  fcDefId);
    public WorkflowStepDetailModel getStepDetailsByStepIdAndFcDefId(@Param("stepID") String stepID,@Param("fcDefId") String  fcDefId);


//    public void startWorkOrder(@Param("wOID") String wOID, @Param("taskID") String taskID, @Param("signum") String signum, @Param("status") String status);
//
//    public void stopWorkOrder(@Param("wOID") String wOID, @Param("taskID") String taskID, @Param("signum") String signum, @Param("status") String status, @Param("bookingID") String bookingID);

    public List<WorkOrderCompleteDetailsModel> getCompleteWorkOrderDetails(@Param("wOID") int wOID, @Param("columnNames") String columnNames);

    public Boolean checkWorkOrder(@Param("wOID") int wOID);

    public void updateWorkOrderUserLevel(@Param("workOrderModel") WorkOrderModel workOrderModel);

    public void createAdhocWorkOrder(@Param("adhocWOModel") AdhocWorkOrderCreationModel adhocWOObject, @Param("endDate") Date endDate);

    public int getEstdHrs(@Param("subActivityID")int subActivityID);

    public void saveClosureDetails_WO(@Param("saveClosureDetailsObject") SaveClosureDetailsForWOModel saveClosureDetailsObject);

    public void saveClosureDetails_InsertDA(@Param("saveClosureDetailsObject") SaveClosureDetailsForWOModel saveClosureDetailsObject);

    public WOWorkFlowModel getWOWorkFlow(@Param("wOID") int wOID);

    public List<WorkFlowBookingDetailsModel> getBookingHours(@Param("wOID") int wOID);

    public void updateBookingDetailsStatus(@Param("wOID")int wOID,
                                           @Param("signumID") String signumID,
                                           @Param("taskID") int taskID,
                                           @Param("bookingID") int bookingID,
                                           @Param("status") String status);



    public List<CurrentWorkOrderModel> getCurrentWorkOrderDetails(@Param("signumID") String signumID, @Param("isApproved") String isApproved);

    public List<Map<String,Object>> checkParallelWorkOrderDetails(@Param("signumID") String signumID, @Param("isApproved") String isApproved);



    public void getCurrentWorkOrderDetails(int woid, String nodeType, String nodeNames);

    public void deleteExistingNodes(@Param("woID") int woid
                                   );
     public void deleteExistingNodesFromPlanNodes(@Param("wOPlanID") int wOPlanID);

    public void insertWorkOrderNode(@Param("woID")int woid,
                                    @Param("nodeType")String nodeType,
                                    @Param("nodeNames")String nodeNames,
                                    @Param("createdBy") String createdBy);

      public void insertWorkOrderNodeInPlanNodes(@Param("wOPlanID")int wOPlanID,
                                    @Param("nodeType")String nodeType,
                                    @Param("nodeNames")String nodeNames,
                                    @Param("createdBy") String createdBy);

    public int getWoPlanID(@Param("woID") int woID);

    public List<WorkOrderViewDetailsByWOIDModel> getWorkOrderViewDetailsByWOID(@Param("WOID") int WOID);

    public void updateStatusAndHours(@Param("wOID") int wOID,
                                     @Param("taskID") int taskID,
                                     @Param("bookingID") int bookingID,
                                     @Param("status") String status,
                                     @Param("reason") String reason,
                                     @Param("signumID") String signumID,
                                     @Param("stepid") String stepid,@Param("sourceID") Integer sourceID, 
                                     @Param("defID") Integer defID, 
                                     @Param("executionType") String executionType);

    public void startWO(@Param("wOID") int wOID,
                                     @Param("signumID") String signumID);

    public void startWO_bookingDetails(@Param("wOID") int wOID,
                                       @Param("taskID") int taskID,
                                       @Param("signumID") String signumID);

    public String startWO_getBookingID(@Param("wOID") int wOID,
                                       @Param("taskID") int taskID,
                                       @Param("signumID") String signumID);
    public String getMaxBookingID(@Param("wOID") int wOID,
                                       @Param("taskID") int taskID,
                                       @Param("signumID") String signumID);
    public Integer getMaxBookingIdByWoid(@Param("wOID") int wOID,@Param("signumID") String signumID);


//    public String closeWO_wOID(@Param("wOID") int wOID,
//                                @Param("signumID") String signumID,
//                                @Param("url") String url);

    public boolean checkIFPreviousStepCompleted(@Param("woID") int woid,@Param("taskID") int taskID,@Param("flowChartDefID") int subActivityDefID ,@Param("flowChartStepID") String flowChartStepID);

    public Boolean checkIFPreviousStepCompletedV2(@Param("woID") int woid,@Param("flowChartStepID") String flowChartStepID,@Param("flowChartDefID") int subActivityDefID);

    public LastStepDetailsModel checkIFLastStep(@Param("woID") int woid,@Param("flowChartDefID") int subactivityDefID);

    public List<WorkOrderFailureReasonModel> getWOFailureReasons(@Param("category") String category);

    public Boolean checkForType(@Param("wOID") int wOID,
                                @Param("taskID") int taskID,
                                @Param("bookingID") int bookingID);

    public void closeWO_bookingID(@Param("wOID") int wOID,
                                  @Param("taskID") int taskID,
                                  @Param("bookingID")int bookingID,
                                  @Param("status") String status,
                                  @Param("signumID") String signumID,
                                  @Param("url") String url);

    public int getAdhocWorkOrderForRPA(@Param("rpaID") String rpaID);

    public String getProjectCreatorSignum(@Param("woid") int woid);

    public void updateRpaFailureStatus(@Param("wOID") int wOID,@Param("taskID") int taskID, @Param("bookingID")int bookingID,
                                       @Param("status") String status, @Param("serverBotModel") ServerBotModel serverBotModel);

    public void addStepDetailsForFlowChart(@Param("stepDetailsModel") StepDetailsModel stepDetailsModel);
    //v1/addStepDetailsForFlowChart
    public void addStepDetailsForFlowChartV1(@Param("wOID") int wOID,@Param("taskID") int taskID,
            @Param("bookingID")int bookingID,@Param("stepID") String stepID,
            @Param("flowChartDefID") int flowChartDefID,@Param("status") String status,
            @Param("signumID") String signumID,@Param("decisionValue") String decisionValue,
            @Param("exeType") String exeType);

    public List<WorkFlowStepBookingDetailsModel> getWorkFlowStepBookingDetails(@Param("wOID") int wOID);

    public WorkFlowBookingDetailsModel getBookingDetails(@Param("woID") int wOID, @Param("taskID") int taskID, @Param("signumID") String signum);
    public List<BookingDetailsModel> getStartedBookingsBySignum( @Param("signum") String signum);

    public int getWoWfVersionNo(@Param("woID")int wOID);

    public WOWorkFlowModel getWOWorkFlowWithWFNo(@Param("wOID")int wOID,@Param("wfVersionNo") int wfVersionNo);

    public void updateWOBooking(@Param("wOID") int wOID,
                                       @Param("taskID") int taskID,
                                       @Param("bookingStatus") String bookingStatus,
                                       @Param("signumID") String signumID);

    public String getEmployeeEmail(@Param("signumID") String signumID);


    public Map<String, String> getWfVersionDetails(@Param("wOID")String wOID);

    public int getWorkOrderDetails(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID,@Param("flowchartDefID") int flowchartDefID,
                                   @Param("wfVersion") int wfVersion,@Param("signumID") String eSignumID);

    public void updateWorkFlowVersion(@Param("woID")int woID,
                                      @Param("version")int updatedVersion,
                                      @Param("signumID") String eSignumID,
                                      @Param("subActivityFlowChartDefID")int subActivityFlowChartDefID);

    public Map<String, String> getWorkOrderData(@Param("woID") String wOID);

    public void updateRPABotStatus(@Param("woID") int wOID,@Param("taskID") int taskID,@Param("bookingID") int bookingID,
                                   @Param("signumID") String signumID,@Param("status") String status);

    public String getFlowChartStepID(@Param("woID") int wOID,@Param("taskID") int taskID,@Param("flowchartDefID") int flowchartDefID,
                                  @Param("bookingID") int bookingID,@Param("signumID") String signumID);

    public String getBookingHoursForStep(@Param("stepID") String stepID,@Param("woID") int woID);

    public List<FlowChartDefModel> getFlowChartBySubActFCDefID(@Param("subActFCDefID")  Integer subActFCDefID);

    public List<WorkFlowStepDetailsModel> getBookingStatusOfPreviousStep(@Param("stepDetailsModel") StepDetailsModel stepDetailsModel);
    //v1
    public List<WorkFlowStepDetailsModel> getBookingStatusOfPreviousStepV1(@Param("wOID") int wOID,@Param("woFCID") int woFCID,@Param("previousStepID") int previousStepID,
            @Param("flowChartDefID") int flowChartDefID,@Param("bookingID") int bookingID,
            @Param("signumID") String signumID);

    public Map<String,Integer> getPreviousStepID(@Param("stepID") int stepID,@Param("flowChartDefID") int flowChartDefID,@Param("wOID") int wOID);

    public int getBookingIDOfPreviousStep(@Param("previousStepID") int previousStepID,@Param("wOID") int wOID,
                                          @Param("woFCID") int woFCID,@Param("flowChartDefID")  int flowChartDefID,@Param("signumID")  String signumID);

    public void updateStepBookingStatus(@Param("wOID") int wOID,@Param("stepID") String stepID,
                                        @Param("taskID") int taskID,@Param("flowChartDefID") int flowChartDefID,
                                        @Param("bookingID") int stepBookingID,@Param("signumID") String signumID,
                                        @Param("bookingStatus") String bookingStatus);

    public void checkAndUpdateStatusOfSameTask(@Param("wOID") int wOID,@Param("taskID")  int taskID,
                                               @Param("stepID") int stepID,@Param("flowChartDefID")  int flowChartDefID,
                                               @Param("signumID") String signumID);

    public int getSubActivityIDForWOID(@Param("wOID") int wOID);

    public int getWFLatestVersion(@Param("defID") int defID);

	public List<RpaModel> getParallelBotDetails(@Param("taskid") int taskid,@Param("subActivityFlowChartDefID") int subActivityFlowChartDefID,@Param("stepID") String stepID);
//    public List<RpaModel> getRpaIDByWOID(@Param("map") Map<String, Object> map);
	public String getRpaDetails(@Param("rpaId") int rpaId);

	public int getProjectIDByTaskiD(@Param("taskid") int taskid);

    public int getLatestBookingID(@Param("wOID") int woID, @Param("signumID") String signumID);

    public void updateBookingStatus(@Param("woID") int woID,
                                    @Param("bookingID") int bookingID,
                                    @Param("signumID") String signumID,
                                    @Param("status") String status,
                                    @Param("comment") String comment);

    public void updateWOFlowChartStepDetails(@Param("woID") int woID,
                                            @Param("taskID")int taskID,
                                            @Param("bookingID") int bookingID,
                                            @Param("flowChartStepID") String flowChartStepID,
                                            @Param("flowChartDefID") int flowChartDefID,
                                            @Param("status") String status,
                                            @Param("reason") String reason,
                                            @Param("signumID") String signumID);

    public Boolean checkBookingStatus(@Param("woID")int woID,@Param("bookingID") int bookingID);

    public void updateBookingStatusInFlowChart(@Param("woID") int woID,
                                               @Param("bookingID") int bookingID,
                                               @Param("signumID") String signumID,
                                               @Param("status") String status);

    public void saveUserWFProficency(@Param("userWFProficency") UserWorkFlowProficencyModel userWorkFlowProficency,@Param("lgSignum") String lgSignum);

    public void updateUserWFProficency(@Param("userWFProficency") UserWorkFlowProficencyModel userWorkFlowProficency,@Param("lgSignum") String lgSignum);

    public List<Map<String, String>> getUserWFProficency(@Param("signumID") String signumID,
                                                         @Param("projectID") int projectID,
                                                         @Param("subActivityID") int subActivityID);

    public void updateWorkOrderWFVersion(@Param("woID")int woID,@Param("wfVersionNo") int wfVersionNo,@Param("signumID") String signumID);

    public LinkedList<WorkOrderProgressModel> getWorkOrders(@Param("signumID")String signumID,
                                                      @Param("status") String status,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate);

    public boolean checkIfParentBooked(@Param("woID") int WOID,@Param("subActivityDefID") int subActivityDefID,@Param("parentID") String parentID);

    public List<Map<String,Object>> getWoProgressData(@Param("woID") String woID);

    public List<WorkOrderNodes> getWONodeDetails(@Param("woID") String woID);

    public Boolean checkWorkOrderBookingStatus(@Param("wOID") int woid);

    public WorkFlowBookingDetailsModel stopWOMaxBookingID(@Param("woID") int woID,@Param("taskID") int taskID);
    public WorkFlowBookingDetailsModel stopWOMaxBookingIDNew(@Param("woID") int woID,@Param("taskID") int taskID,@Param("stepid") String stepid);



    public BookingDetailsModel getBookingsByWoIdAndFcStepId(@Param("wOID") int wOID,@Param("flowChartStepId") String flowChartStepId);

//    public int updateBookingsStatus(@Param("bookingids") String bookingids,@Param("status") String status,@Param("type") String type);
    public int updateBookingsStatus(@Param("bookingids") String bookingids,@Param("status") String status,@Param("type") String type,
    		@Param("reason") String reason);
    public int updateBookingsStatusByIds(@Param("bookingids") String bookingids,@Param("status") String status, @Param("signumID") String signumID);

    public boolean insertBooking(@Param("list") List<BookingDetailsModel> currentBookingList);
    public boolean addBooking(@Param("booking") BookingDetailsModel booking);



    public boolean insertFcStepDetails(@Param("stepList") List<FcStepDetails> stepList);

    public int updateFcStepStatusByBookingIds(@Param("bookingids") String bookingids,@Param("status") String status);
    public List<FcStepDetails> selectFcStepDetailsByBookingIds(@Param("bookingids") String bookingids);

    public String getDecisionValueForStep(@Param("stepID") Integer stepID,@Param("woID") int woID);
    public BookingDetailsModel getBookingDetailsByWoid(@Param("woID") int wOID, @Param("taskID") int taskID,@Param("stepId") String stepId);

    public Integer getFlowChartStepByStepId(@Param("woId") int woId, @Param("flowChartStepId") String flowChartStepId);
    public StepDetailsModel getStepDetailsByStepId(@Param("woId") int woId, @Param("flowChartStepId") String flowChartStepId);


    public Integer getTotalBookingHoursForStep(@Param("woid") int woid, @Param("stepId") int stepId);

    public WorkFlowStepDataModel getWFStepData(@Param("stepID") String stepID,@Param("defID") int defID, @Param("executionType") String executionType);

    public List<Map<String, Object>> getWorkFlowStepDecisionValue(@Param("wOID") int wOID);

	public List<Map<String, Object>> checkIFLastStepNew(@Param("wOID")  int wOID, @Param("subactivityDefID")  int subactivityDefID);

	public WorkFlowDetailsModel getWorkFlowNameForWoID(@Param("wOID") int wOID);

    public WOWorkFlowModel getExpertWorkFlow(@Param("wOID") int wOID,@Param("wfName") String wfName,@Param("expertDefId") int expertDefId);

    public Map<String,Object> getExistingWFDefID(@Param("wOID") int wOID);

    public void updateWODefID(@Param("wOID") int wOID,@Param("flowChartDefID") int flowChartDefID);

	public  Map<String, Object> getWOCreationDeatilsByWOName(@Param("getwOName") String getwOName);

	public Map<String, Object> getWoDateDataByWoId(@Param("getwOID") int getwOID);

	public String getProjectIdByWoid(@Param("wOID")int wOID);

	public String getNodesByWoId(@Param("wOID") int wOID);

	public List<HashMap<String, Object>> getEfficiencyDevlieryIndexForUser(@Param("WOID") int woID);

	public void populateEfficiencyData(@Param("startDate") LocalDate startDate,
	@Param("endDate") LocalDate endDate,
	@Param("woID") int woID);

	public void populateDeliveryData(@Param("startDate") LocalDate startDate,
	@Param("endDate") LocalDate endDate,
	@Param("woID") int woID);

	public HashMap<String, Object> getWODetails(@Param("getwOID") int getwOID);

	public List<HashMap<String, Object>> getEfficiencyDevlieryPerformance(@Param("projectID") int projectID,@Param("subActivity") String subActivity,
			@Param("flowChartDefID")int flowChartDefID,@Param("signumID") String signumID);

	public List<WorkOrderBasicDetailsModel> getUnassignedWorkOrders(
			@Param("signum") String signum, @Param("startDate") String startDate,@Param("endDate") String endDate,@Param("priority") String priority,@Param("status") String status,@Param("doStatus") String doStatus);

	public List<ProjectQueueWorkOrderBasicDetailsModel> getProjectQueueWorkOrders(
			@Param("signum") String signum, @Param("startDate") String startDate,@Param("endDate") String endDate,@Param("priority") String priority,@Param("status") String status,@Param("doStatus") String doStatus,@Param("projectIdList") String projectIdList, @Param("dataTableRequest") DataTableRequest dataTableRequest,@Param("checkSearch") boolean checkSearch);
	
	public WorkOrderModel getWOData(@Param("getwOID") int getwOID);

	public boolean updateWoWithAsignedSignum(@Param("woData") WorkOrderModel woData);

	public List<WorkOrderBasicDetailsModel> getAssignedWorkOrders(@Param("signum") String signum, @Param("woStatus") String woStatus,@Param("startDate") String startDate,@Param("endDate") String endDate);

	public boolean updateWoSignumToNull(@Param("woData") WorkOrderModel woData);

	public boolean insertWOTransferHistory(@Param("woData")  WorkOrderModel woData);

	public List<Map<String, Object>> checkTaskStatus(@Param("woData") WorkOrderModel woData);

	public SourceSystemDetailsModel getSourceSystemDetails(@Param("sourceSystemId") String sourceSystemId);

	public List<String> getIntegratedSource(@Param("signum") String signum);

	public List<WorkFlowStepLinksDetailModel> getFlowChartStepLinkDetailsByWoId(@Param("woid") Integer woid);

	public boolean updateReferenceIDByBookingId(@Param("bookingId")int bookingId, @Param("referenceId")int referenceId, @Param("signumID")String signumID);

	public boolean saveExternalExecDetails(@Param("isfRefId")int isfRefId, @Param("externalRefId")int externalRefId);
	public ExternalExecutionDetailsModel getExternalExecDetailsbyRefId(@Param("isfRefId")int isfRefId);
	public BookingDetailsModel getBookingDetailsByBookingId(@Param("bookingId")int bookingId);

	public List<String> getMarketByWoid(@Param("woid") Integer woid);

	public Map<String, Object> validateUserLoginHistory(@Param("userLogin") UserLoginModel userLogin);

	public HashMap<String, Object> getWODetailsByWoid(@Param("woID") int woID);

	public int checkIfAllTaskCompleted(@Param("woID") int woID);

    public List<BookingDetailsModel> getAlreadyStartedBookingsByWoID(@Param("woID") int woID);

	public HashMap<String, Object> isInstalledDesktopVersionUpdated(@Param("signumID") String signumID);

	public WorkFlowBookingDetailsModel maxQueuedBookingID(@Param("woID") Integer woID,@Param("taskID") Integer taskID);

	public String getFlowChartStepIDByBooking(@Param("woID") Integer woID,@Param("taskID")Integer taskID,@Param("bookingID") int bookingID,@Param("signumID") String signumID);

	public int updateOnlyBookingsStatusByIds(@Param("bookingids") String bookingids,@Param("status") String status,@Param("signumID") String signumID);

	public int updateReasonByBookingIds(@Param("bookingid") String bookingid,@Param("status")  String status);

	public int countInvalidStepsForClouser(@Param("saveClosureDetailsForWOModel") SaveClosureDetailsForWOModel saveClosureDetailsForWOModel);

	public List<BookingDetailsModel> getStartedBookingsBySignum1(@Param("signumID") String signumID);

	public String getWorkOrderPriority(@Param("woID") int woID);

	public void updateWorkOrderPriority(@Param("woID")  int woID,@Param("priority") String priority);

	public void saveAutomaticStepClosureDetails(@Param("serverBotModel") ServerBotModel serverBotModel, @Param("hours") String hours,@Param("bookingID") int bookingID);

	public void updateOnlyBookingsReasonByIds(@Param("bookingids") String bookingids,@Param("reason") String reason,@Param("signumID") String signumID);

	public String getStepNameByBookingID(@Param("bookingID") int bookingID);

	public int getLatestBookingIDNotCompleted(@Param("woID") int woID);

	public boolean isWoAssignedToSignum(@Param("woId") Integer woId, @Param("signumID") String signumID);

	public Integer getExpertDefID(@Param("wfName") String wfName,@Param("wfid") String wfid);

	public Boolean checkWorkOrderBookingStatusReason(@Param("wOID") int wOID);

	public Integer getMaxBookingId(@Param("woID")  int woID);

	public List<DeliverableSuccessReasonModel> getWorkOrderSuccessReasons(@Param("wOID") int workOrderID);

	public void addDeliverableSuccessReason(@Param("signum")String signum,
			@Param("deliverableSuccessReasonModel")DeliverableSuccessReasonModel deliverableSuccessReasonModel);

	public void updateDeliverableSuccessReason(@Param("signum")String signum,
			@Param("deliverableSuccessReasonModel")DeliverableSuccessReasonModel deliverableSuccessReasonModel);

	public boolean validateSuccessReason(@Param("deliverableSuccessReasonModel")DeliverableSuccessReasonModel deliverableSuccessReasonModel);

	public List<DeliverableSuccessReasonModel> getAllSuccessReasons(@Param("deliverableUnitId")int deliverableUnitId);

	public List<Map<String, Object>> getWONodeDetailsByWOID(@Param("woIDValue") String woIDValue);

	public void saveReasonStatusActive(@Param("signum")String signum,@Param("deliverableSuccessReasonModel") DeliverableSuccessReasonModel deliverableSuccessReasonModel);

	public void saveReasonStatus(@Param("signum")String signum,@Param("deliverableSuccessReasonModel") DeliverableSuccessReasonModel deliverableSuccessReasonModel);

	public String getExecutionType(@Param("wOID") int wOID,@Param("stepid") String stepid,@Param("taskID") int taskID,@Param("bookingId") int bookingId);

	public EventPublisherURLMappingModel getEventPublisherURLMappingModel(@Param("sourceName") String sourceName,
			@Param("category") String category);

	public String getSadCountForStep(@Param("stepID") String stepID,@Param("woID")  int woID,@Param("signum") String signum, @Param("defID") int defID);

	public String getWOAssignedTo(@Param("wOID") int  wOID);

	public String getFlowChartType(@Param("wOID") Integer wOID);

	public boolean checkStartedTaskInBooking(@Param("wOID") int wOID);

	public BookingDetailsModel getBookingDetailsandHours(@Param("bookedID")int bookedID, @Param("stepID") String stepID, @Param("wOID")int wOID);

	public Integer getAutoSenseSourceIdOnSourceName(@Param("sourceName") String sourceName);

	public StepDetailsModel getDecsionDetailsByChildStep(@Param("flowChartDefID") int flowChartDefID,@Param("flowChartStepId") String flowChartStepId);

	public List<String> getMarkets(@Param("workOrderNodesModel") WorkOrderNodesModel workOrderNodesModel);
	public List<String> getMarkets(@Param("workOrderColumnMappingModel") WorkOrderColumnMappingModel workOrderColumnMappingModel);

	public LinkedList<WorkOrderProgressModel> getWorkOrderDetailsBySignum(@Param("signumID") String signumID,@Param("status") String status);

	public String getJsonValue(@Param("flowChartDefID") int flowChartDefID);

	public Integer addNewBooking(@Param("list") List<BookingDetailsModel> startedBookingsOfUser, @Param("wOID")int wOID,
			@Param("parallelCount") int parallelCount);

	public void checkAndUpdateFlowChartDefID( @Param("wOID")int wOID,  @Param("flowChartDefID")int flowChartDefID);

	public void markWorkOrderAsStarted( @Param("wOID") int woID, @Param("signum") String signum);

	public BookingDetailsModel getBookingDetailsByWorkOrderID(@Param("woID") int woID);

	public UserWorkFlowProficencyModel getWFUserProficiency(@Param("signumID") String signumID, @Param("subactivityID") int subactivityID,
			@Param("wfID") int wfID,  @Param("projectID") int projectID, @Param("defID") int defID);

	public SaveWfUserProfResponseModel saveWFUserProficiency( @Param("signumID") String signumID, @Param("workFlowUserProficiencyModel") WorkFlowUserProficiencyModel workFlowUserProficiencyModel,
			@Param("subActivityId") int subActivityId, @Param("wfID") int wfID, @Param("woID") int woID);
	public boolean checkIFProficiencyIDExists(@Param("proficiencyID") int proficiencyID);
	public void updateStatusandProficiencyId(@Param("wOID") int wOID,@Param("signumID")  String signumID,@Param("proficiencyID")  int proficiencyID,@Param("updateActualStartDate")  boolean updateActualStartDate);

	public StepDetailsModel getDisabledStepDetails(@Param("subActivityDefID") int subActivityDefID, @Param("wOID") int wOID);

	public List<ChildStepDetailsModel> getDescisionStepDetails(@Param("stepID") String stepID, 
			@Param("defID") Integer defID,@Param("woID")  Integer woID);
	
	public List<WorkflowStepDetailModel> getAllStepsByWOID(@Param("woID") int woID);

	public List<NextStepModel> getAllNextStepID(@Param("stepID") String stepID, 
			@Param("defID") Integer defID);

	public int getKpiValue(@Param("subactivityID") int subactivityID, @Param("wfID") int wfID, @Param("projectID") int projectID, @Param("qualifyCount") String qualifyCount);

	public ProficiencyTypeModal getProficiencyDetailsByName(@Param("proficiencyName") String proficiencyName);

	public String getWfNameByWfId(@Param("subactivityID")int subactivityID, @Param("wfID")int wfID,  @Param("projectID")int projectID,@Param("defID") int defID);

	public String getProficiencyNameWoID(@Param("wOID") int wOID);

	public boolean checkIFPreviousStepCompletedQualifiedWo(@Param("wOID") int wOID,@Param("defID") int subActivityDefID,@Param("stepID") String flowChartStepID);

	public int getSubActivityDefIDForWOID(@Param("woID") int woID);

	public StepTaskModel getCurrentStepDetails(@Param("stepID") String stepID,@Param("defID") Integer defID);

	public String getLastStepID(@Param("subactivityDefID") int subactivityDefID);

	public Integer getProficiencyId(@Param("wOID") String wOID,@Param("signum") String signum);

	@MapKey("stepID")
	public Map<String,FlowChartReverseTraversalModel> getAllWorkOrderStepsForTraversal(@Param("wOID") int wOID,@Param("subactivityDefID") int subactivityDefID);

    public void updateBookingStatusOnStart(@Param("woID") int woID,
            @Param("bookingID") int bookingID,
            @Param("signumID") String signumID,
            @Param("status") String status,
            @Param("comment") String comment);
	
	public WorkFlowBookingDetailsModel maxDisabledOnHoldBookingID(@Param("woID") Integer woID,@Param("taskID") Integer taskID);

	public boolean checkIFManualDisabledStep(@Param("defID") int subActivityDefID,@Param("stepID") String flowChartStepID);

	public boolean checkIFPreviousStepCompletedDisabled(@Param("woID") int wOID, @Param("defID") int subActivityDefID,
			@Param("stepID") String flowChartStepID);

	public String getExecutionTypeNew(@Param("defID") int defID,@Param("stepid") String stepid);

	public List<Map<String, Object>> downloadWorkHistoryData(@Param("signum") String signum, @Param("startDate") String startDate,@Param("endDate") String endDate,@Param("woStatus") String woStatus);
	
	public List<WorkOrderNodesModel> getListOfNode(@Param("wOID") int wOID);

	public boolean isVersionMatched(@Param("signum")String signum);
	
    public String getLatestVersionNumber();


	public boolean isInstalledLibraryVersionUpdated(@Param("signumID") String signumID);
	public void insertWorkOrderNetworkElement(@Param("tablename") String tablename, @Param("wOID") int wOID, @Param("signum") String signum);

	public void deleteExistingWorkOrderNECount(@Param("wOID") int wOID);

	public void insertWorkOrderNECount(@Param("wOID") int wOID, @Param("count") int count, @Param("neTextName") String neTextName, @Param("signum") String signum);

	public List<WoCompleteDetailsModel> getCompleteWoDetails(@Param("wOID") int wOID,@Param("columnNames") String columnNames);

	
	public List<String> getMarketsByWorkOrders(@Param("workOrderColumnMappingModel") WorkOrderColumnMappingModel workOrderColumnMappingModel, @Param("countryCustomerGroupId") int countryCustomerGroupId);

	public void updatetWorkOrderNECount(@Param("wOID") int wOID, @Param("count") int count, @Param("neTextName") String neTextName, @Param("signum") String signum);

	
	public String getAssignedToSignumByWOID(@Param("wOID") int wOID);
	
	public boolean validateWOStatus(@Param("wOID")int wOID);
	
	public List<Map<String, Object>> downloadProjectQueueWorkOrders(@Param("signum") String signum,
			@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("priority") String priority,
			@Param("status") String status, @Param("doStatus") String doStatus,
			@Param("projectIdList") String projectIdList,@Param("length") int length);
	
	public String getSharepointUrl();
	
	public int getMarketAreaIDByProjectID(@Param("projectID") int projectID);
	
	public SharePointDetailModel getDetailsByMarketID(@Param("marketAreaID") int marketID);
	
	public boolean validateSharePointDetailWithMarketArea(@Param("marketAreaID")int marketAreaID);
	
	public List<SharePointDetailModel> getSharepointConfigurationDetails();

	public String getOutPutUploadByStepID(@Param("stepID") String stepID, @Param("flowChartDefID") int flowChartDefID);
	
}
