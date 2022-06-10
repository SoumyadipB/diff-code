package com.ericsson.isf.service.audit;

import java.util.Base64;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.isf.dao.WOExecutionDAO;
import com.ericsson.isf.dao.WorkOrderPlanDao;
import com.ericsson.isf.model.AssignWOModel;
import com.ericsson.isf.model.AuditDataModel;
import com.ericsson.isf.model.CreateSrResponse;
import com.ericsson.isf.model.CreateWoResponse;
import com.ericsson.isf.model.CreateWorkOrderModel;
import com.ericsson.isf.model.CreateWorkOrderModel2;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.SaveClosureDetailsForWOModel;
import com.ericsson.isf.model.TransferWorkOrderModel;
import com.ericsson.isf.model.WorkOrderAcceptanceModel;
import com.ericsson.isf.model.WorkOrderCompleteDetailsModel;
import com.ericsson.isf.model.WorkOrderModel;
import com.ericsson.isf.model.WorkflowStepDetailModel;
import com.ericsson.isf.model.botstore.ServerBotModel;
import com.ericsson.isf.security.aes.AesUtil;
import com.ericsson.isf.service.WOExecutionService;
import com.ericsson.isf.service.WorkOrderPlanService;
import com.ericsson.isf.util.AppConstants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class AuditDataProcessor {

	private static final String ASSIGNEE = "Assignee";

	private static final String ASSIGNED_WORK_ORDER_TO = "Assigned workOrder to ";

	@Autowired
	private AuditManagementService auditManagementService;
	
	@Autowired
	private WOExecutionService woExecutionService;
	
	@Autowired
	WOExecutionDAO wOExecutionDAO;
	
	@Autowired
	WorkOrderPlanDao workOrderPlanDao;
	
	@Autowired
	private AesUtil aesUtil;
	
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(WorkOrderPlanService.class);
	
	public void woExecution_startWorkOrderTask(AuditDataProcessorRequestModel auditRequest){
		AuditDataModel auditDataModel=new AuditDataModel();
		auditDataModel.setAuditPageId((Integer)getPathParamValueByIndex(auditRequest,1));
		auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
		String stepId=(String) getPathParamValueByIndex(auditRequest,3);
		String fcDefId=(String) getPathParamValueByIndex(auditRequest,4);
		WorkflowStepDetailModel stepDetails=woExecutionService.getStepDetailsByStepIdAndFcDefId(stepId,fcDefId);
		auditDataModel.setContext(stepDetails.getStepName());
		auditDataModel.setCreatedBy(getPathParamValueByIndex(auditRequest,5).toString());
		auditDataModel.setNewValue("STARTED");
		auditDataModel.setFieldName("Status");
		auditDataModel.setActorType(AppConstants.ACTOT_TYPE_SP);
		auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
		auditDataModel.setCommentCategory("WO_START_TASK");
		auditManagementService.addToAudit(auditDataModel);
	}
	
	public void woExecution_startTask(AuditDataProcessorRequestModel auditRequest){

		Map map = (Map) auditRequest.getRequestParams();
		String[] value = (String[]) map.get("serverBotModel");
		String req = value[0];
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(req);
		ServerBotModel serverBotModel = gson.fromJson(object, ServerBotModel.class);
		
		addStartTaskToAudit(serverBotModel);
		
	}
	
	public void addStartTaskToAudit(ServerBotModel serverBotModel) {

		AuditDataModel auditDataModel = new AuditDataModel();
		auditDataModel.setAuditPageId(serverBotModel.getwOID());
		auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
		String stepId = serverBotModel.getStepID();
		String fcDefId = Integer.toString(serverBotModel.getSubActivityFlowChartDefID());
		WorkflowStepDetailModel stepDetails = woExecutionService.getStepDetailsByStepIdAndFcDefId(stepId, fcDefId);
		if (StringUtils.isNotEmpty(serverBotModel.getRefferer())) {
			if (StringUtils.equalsIgnoreCase(serverBotModel.getRefferer(), AppConstants.UI)) {
				serverBotModel.setRefferer(AppConstants.WEB);
			}
			Integer sourceId = getAutoSenseSourceIdOnSourceName(StringUtils.trim(serverBotModel.getRefferer()));
			serverBotModel.setSourceId(sourceId);
		} else if(StringUtils.isNotEmpty(serverBotModel.getExternalSourceName())){
			if (StringUtils.equalsIgnoreCase(serverBotModel.getExternalSourceName(), AppConstants.UI)) {
				serverBotModel.setRefferer(AppConstants.WEB);
			}
			Integer sourceId = getAutoSenseSourceIdOnSourceName(StringUtils.trim(serverBotModel.getExternalSourceName()));
			serverBotModel.setSourceId(sourceId);
		}
		if(stepDetails.getExecutionType().equalsIgnoreCase(AppConstants.MANUAL_DISABLED)) {
			auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE + serverBotModel.getwOID());

		}else {
			auditDataModel.setContext(stepDetails.getStepName());			
		}
		auditDataModel.setCreatedBy(serverBotModel.getSignumID());
		auditDataModel.setNewValue(AppConstants.STARTED);
		auditDataModel.setFieldName(AppConstants.STATUS);
		auditDataModel.setActorType(AppConstants.ACTOT_TYPE_SP);
		auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
		auditDataModel.setCommentCategory("WO_START_TASK");
		auditDataModel.setSourceId(serverBotModel.getSourceId());
		auditManagementService.addToAudit(auditDataModel);
	}
	
	
	public void woExecution_stopWorkOrderTask(AuditDataProcessorRequestModel auditRequest) {
		ServerBotModel serverBotModel = (ServerBotModel) auditRequest.getRequestBody();

		addStopTaskToAudit(serverBotModel);

	}
	
	public void addStopTaskToAudit(ServerBotModel serverBotModel) {

		try {
			AuditDataModel auditDataModel = new AuditDataModel();
			auditDataModel.setAuditPageId(serverBotModel.getwOID());
			auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
			String stepId = serverBotModel.getStepID();
			String fcDefId = Integer.toString(serverBotModel.getSubActivityFlowChartDefID());
			WorkflowStepDetailModel stepDetails = null;
			String stepName = null;

			if (stepId.equals("0") && fcDefId.equals("0")) {
				stepName = woExecutionService.getStepNameByBookingID(serverBotModel.getBookingID());
			} else {
				stepDetails = woExecutionService.getStepDetailsByStepIdAndFcDefId(stepId, fcDefId);
				stepName = stepDetails.getStepName();
			}
			if (StringUtils.isNotEmpty(serverBotModel.getRefferer())) {
				if (StringUtils.equalsIgnoreCase(serverBotModel.getRefferer(), AppConstants.UI)) {
					serverBotModel.setRefferer(AppConstants.WEB);
				}
				Integer sourceId = getAutoSenseSourceIdOnSourceName(StringUtils.trim(serverBotModel.getRefferer()));
				serverBotModel.setSourceId(sourceId);
			} else if(StringUtils.isNotEmpty(serverBotModel.getExternalSourceName())){
				if (StringUtils.equalsIgnoreCase(serverBotModel.getExternalSourceName(), AppConstants.UI)) {
					serverBotModel.setRefferer(AppConstants.WEB);
				}
				Integer sourceId = getAutoSenseSourceIdOnSourceName(StringUtils.trim(serverBotModel.getExternalSourceName()));
				serverBotModel.setSourceId(sourceId);
			}
			if(StringUtils.equalsIgnoreCase(serverBotModel.getExecutionType(), AppConstants.MANUAL_DISABLED)) {
				auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE + serverBotModel.getwOID());

			}else {
				auditDataModel.setContext(stepName);			
			}
			auditDataModel.setCreatedBy(serverBotModel.getSignumID());
			auditDataModel.setNewValue(AppConstants.STOPPED);
			auditDataModel.setFieldName(AppConstants.STATUS);
			auditDataModel.setActorType(AppConstants.ACTOT_TYPE_SP);
			auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
			auditDataModel.setCommentCategory("WO_STOP_TASK");
			auditDataModel.setSourceId(serverBotModel.getSourceId());
			auditManagementService.addToAudit(auditDataModel);
		}
		catch(Exception e) {
			LOG.error("addStopTaskToAudit"+e.getLocalizedMessage());
		}
		
	}
	
	
	public void woExecution_updateBookingDetailsStatus(AuditDataProcessorRequestModel auditRequest){
		AuditDataModel auditDataModel=new AuditDataModel();
		auditDataModel.setAuditPageId(Integer.parseInt(getPathParamValueByIndex(auditRequest,1).toString()));
		auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
		String stepId=(String) getPathParamValueByIndex(auditRequest,7);
		Map<String,Object> fcDetails=wOExecutionDAO.getExistingWFDefID(Integer.parseInt(getPathParamValueByIndex(auditRequest,1).toString()));
		String flowChartDefID=fcDetails.get("FlowChartDefID").toString();
		WorkflowStepDetailModel stepDetails=woExecutionService.getStepDetailsByStepIdAndFcDefId(stepId,flowChartDefID);
		if(StringUtils.equalsIgnoreCase(stepDetails.getExecutionType(), AppConstants.MANUAL_DISABLED)) {
			auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE + Integer.parseInt(getPathParamValueByIndex(auditRequest,1).toString()));

		}else {
			auditDataModel.setContext(stepDetails.getStepName());
		}
		auditDataModel.setAdditionalInfo(getPathParamValueByIndex(auditRequest,6).toString());
		auditDataModel.setCreatedBy(getPathParamValueByIndex(auditRequest,2).toString());
		auditDataModel.setNewValue(getPathParamValueByIndex(auditRequest,5).toString());
		auditDataModel.setFieldName("Status");
		auditDataModel.setActorType(AppConstants.ACTOT_TYPE_SP);
		auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
		auditDataModel.setCommentCategory("WO_STEP_ONHOLD_SKIPPED");
		auditManagementService.addToAudit(auditDataModel);
	}
	
	public void externalInterface_updateBookingDetailsStatus(AuditDataProcessorRequestModel auditRequest){
		AuditDataModel auditDataModel=new AuditDataModel();
		auditDataModel.setAuditPageId(Integer.parseInt(getPathParamValueByIndex(auditRequest,1).toString()));
		auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
		String stepId=(String) getPathParamValueByIndex(auditRequest,7);
		Map<String,Object> fcDetails=wOExecutionDAO.getExistingWFDefID(Integer.parseInt(getPathParamValueByIndex(auditRequest,1).toString()));
		String flowChartDefID=fcDetails.get("FlowChartDefID").toString();
		WorkflowStepDetailModel stepDetails=woExecutionService.getStepDetailsByStepIdAndFcDefId(stepId,flowChartDefID);
		auditDataModel.setContext(stepDetails.getStepName());
		auditDataModel.setAdditionalInfo(getPathParamValueByIndex(auditRequest,6).toString());
		auditDataModel.setCreatedBy(getPathParamValueByIndex(auditRequest,2).toString());
		auditDataModel.setNewValue(getPathParamValueByIndex(auditRequest,5).toString());
		auditDataModel.setFieldName("Status");
		auditDataModel.setActorType(AppConstants.ACTOT_TYPE_SP);
		auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
		auditDataModel.setCommentCategory("WO_STEP_ONHOLD_SKIPPED");
		auditManagementService.addToAudit(auditDataModel);
	}
	
	public void woExecution_saveClosureDetailsForWO(AuditDataProcessorRequestModel auditRequest){

		SaveClosureDetailsForWOModel saveClosureDetailsForWOModel=(SaveClosureDetailsForWOModel) auditRequest.getRequestBody();
		
		addSaveClosureWoToAudit(saveClosureDetailsForWOModel);
		
	}
	
	public void addSaveClosureWoToAudit(SaveClosureDetailsForWOModel saveClosureDetailsForWOModel) {

		AuditDataModel auditDataModel = new AuditDataModel();
		auditDataModel.setAuditPageId(saveClosureDetailsForWOModel.getwOID());
		auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
		auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER + AppConstants.UNDERSCORE
				+ saveClosureDetailsForWOModel.getwOID());
		auditDataModel.setCreatedBy(saveClosureDetailsForWOModel.getLastModifiedBy());
		auditDataModel.setNewValue("closed with " + saveClosureDetailsForWOModel.getDeliveryStatus());
		auditDataModel.setFieldName("Delivery Status");
		auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);

		String additionalComment = (StringUtils.isNotBlank(saveClosureDetailsForWOModel.getReason()))
				? "Reason:" + saveClosureDetailsForWOModel.getReason()
				: StringUtils.EMPTY;
		if (StringUtils.isNotBlank(saveClosureDetailsForWOModel.getStatusComment())) {

			if (StringUtils.isNotBlank(additionalComment)) {
				additionalComment += " | ";
			}

			additionalComment += saveClosureDetailsForWOModel.getStatusComment();
		}
			
		auditDataModel.setAdditionalInfo(additionalComment);
		auditDataModel.setActorType(AppConstants.ACTOT_TYPE_SP);
		auditDataModel.setCommentCategory("WO_SAVE_CLOSURE");
		auditManagementService.addToAudit(auditDataModel);
	}
	
	
	public void woDeliveryAcceptance_acceptWorkOrder(AuditDataProcessorRequestModel auditRequest){
		WorkOrderAcceptanceModel request=(WorkOrderAcceptanceModel) auditRequest.getRequestBody();
		
		for(int woId:request.getLstWoID()){
			AuditDataModel auditDataModel=new AuditDataModel();
			auditDataModel.setAuditPageId(woId);
			auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
			auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE + woId);
			auditDataModel.setCreatedBy(request.getLastModifiedBy());
			auditDataModel.setNewValue("Accepted");
			auditDataModel.setFieldName("Delivery Status");
			auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
			auditDataModel.setActorType(AppConstants.ACTOT_TYPE_DR);
			auditDataModel.setAdditionalInfo(request.getComment() +" | Rating: " +request.getRating());
			auditDataModel.setCommentCategory("WO_ACCEPTED");
			auditManagementService.addToAudit(auditDataModel);
		}
	}
	
	public void woDeliveryAcceptance_rejectWorkOrder(AuditDataProcessorRequestModel auditRequest){
		WorkOrderAcceptanceModel request=(WorkOrderAcceptanceModel) auditRequest.getRequestBody();
		
		for(int woId:request.getLstWoID()){
			AuditDataModel auditDataModel=new AuditDataModel();
			auditDataModel.setAuditPageId(woId);
			auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
			auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE + woId);
			auditDataModel.setCreatedBy(request.getLastModifiedBy());
			auditDataModel.setNewValue("Rejected");
			auditDataModel.setFieldName("Delivery Status");
			auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
			auditDataModel.setActorType(AppConstants.ACTOT_TYPE_DR);
			auditDataModel.setAdditionalInfo(request.getComment() +" | Reason: " +request.getReason());
			auditDataModel.setCommentCategory("WO_REJECTED");
			auditManagementService.addToAudit(auditDataModel);
		}
	}
	
	
	
	
	public void woExecution_updateWorkOrderStatus(AuditDataProcessorRequestModel auditRequest) {
		SaveClosureDetailsForWOModel request=(SaveClosureDetailsForWOModel) auditRequest.getRequestBody();
		AuditDataModel auditDataModel=new AuditDataModel();
		String woId=Integer.toString(request.getwOID());
		auditDataModel.setAuditPageId(Integer.valueOf(woId));
		auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
		auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE +woId);
		auditDataModel.setCreatedBy(request.getSignumID());
		auditDataModel.setNewValue(request.getDeliveryStatus());
		auditDataModel.setFieldName("Status");
		auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
		auditDataModel.setAdditionalInfo(request.getStatusComment());
		auditDataModel.setActorType(AppConstants.ACTOT_TYPE_SP);
		auditDataModel.setCommentCategory("WO_STATUS_UPDATE");
		auditManagementService.addToAudit(auditDataModel);
		
		if(request.getPriority()!=null) {
			String oldPriority =request.getPriority();
			String newPriority =this.wOExecutionDAO.getWorkOrderPriority(request.getwOID());
	        
			if(request.getDeliveryStatus().equalsIgnoreCase(AppConstants.ONHOLD) && 
				oldPriority.equalsIgnoreCase(AppConstants.LIVE) && newPriority.equalsIgnoreCase(AppConstants.NORMAL) ) {
				auditDataModel.setAuditPageId(Integer.valueOf(woId));
				auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
				auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE +woId);
				auditDataModel.setCreatedBy(request.getSignumID());
				auditDataModel.setOldValue(oldPriority);
				auditDataModel.setNewValue("Normal");
				auditDataModel.setFieldName(AppConstants.PRIORITY);
				auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
				auditDataModel.setAdditionalInfo(request.getStatusComment());
				auditDataModel.setActorType(AppConstants.ACTOT_TYPE_SP);
				auditDataModel.setCommentCategory(AppConstants.WO_PRIORITY_EDIT);
				auditManagementService.addToAudit(auditDataModel);
			}
		}
	}
	

	public void woManagement_createWorkOrderPlan(AuditDataProcessorRequestModel auditRequest){
		CreateWorkOrderModel req= (CreateWorkOrderModel) auditRequest.getRequestBody();
		CreateWoResponse response= (CreateWoResponse) auditRequest.getResponse();
		for(CreateWorkOrderModel2 r:response.getWorkOrderID()){
//		for(CreateWorkOrderModel2 r:response.getResponseData()){
			
			LOG.info("WOID for Audit data is : "+r.getWoId());
			
			AuditDataModel auditDataModel=new AuditDataModel();
			auditDataModel.setAuditPageId(r.getWoId());
			auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
			auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE+r.getWoId());
			auditDataModel.setCreatedBy(r.getCreateBy());
			auditDataModel.setActorType(AppConstants.ACTOT_TYPE_PM);
			auditDataModel.setMessage("".equals(req.getComment())?null:req.getComment());
			auditDataModel.setAdditionalInfo(ASSIGNED_WORK_ORDER_TO+r.getSignumID());
			auditDataModel.setCommentCategory(AppConstants.WO_CREATE_PLAN);
			auditDataModel.setRefferer("Mobile");
			auditManagementService.addComment(auditDataModel);
		}
	}
	
	public void woManagement_transferWorkOrder(AuditDataProcessorRequestModel auditRequest){
		TransferWorkOrderModel request= (TransferWorkOrderModel) auditRequest.getRequestBody();
		for(int woid:request.getWoID()){
			AuditDataModel auditDataModel=new AuditDataModel();
			
			if (!workOrderPlanDao.checkIfStepInStartedState(woid)) {
				WorkOrderModel wodetails = workOrderPlanDao.getWorkOrderDetailsById(woid);	
				if (!AppConstants.CLOSED.equals(wodetails.getStatus())) {
					StringBuilder info = new StringBuilder();
					info.append("The Step Name from where next Engineer to Execute the WF : ");
					auditDataModel.setAuditPageId(woid);
					auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE + woid);
					auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
					auditDataModel.setCreatedBy(request.getSenderID());
					auditDataModel.setNewValue(request.getReceiverID());
					auditDataModel.setFieldName(ASSIGNEE);
					info.append(request.getStepName())
						.append(", Comments : ")
						.append(request.getUserComments());
					auditDataModel.setAdditionalInfo(info.toString());
					auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
					auditDataModel.setActorType(AppConstants.ACTOT_TYPE_SP);
					auditDataModel.setCommentCategory(AppConstants.WO_TRANSFER);
					auditManagementService.addToAudit(auditDataModel);								
				}
			}
		}
	}

	private Object getPathParamValueByIndex(AuditDataProcessorRequestModel auditRequest,int index){
		
		try{
			Map<String,Object> requestMap=(Map<String, Object>) auditRequest.getRequestParams();
			if(requestMap.containsKey("pathParam"+index)) {
				return requestMap.get("pathParam"+index);
			}
			return null;
		}catch(Exception e){
			System.out.println("ERROR while getting request param" + e.getMessage());
            e.printStackTrace();
		}
		return null;
	}
	
	public void rpaController_createWorkOrderPlan(AuditDataProcessorRequestModel auditRequest){
		CreateWorkOrderModel req= (CreateWorkOrderModel) auditRequest.getRequestBody();
		CreateWoResponse response= (CreateWoResponse) auditRequest.getResponse();
		for(CreateWorkOrderModel2 r:response.getWorkOrderID()){
//		for(CreateWorkOrderModel2 r:response.getResponseData()){
			AuditDataModel auditDataModel=new AuditDataModel();
			auditDataModel.setAuditPageId(r.getWoId());
			auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
			auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE+r.getWoId());
			auditDataModel.setCreatedBy(r.getCreateBy());
			auditDataModel.setActorType(AppConstants.ACTOT_TYPE_PM);
			auditDataModel.setMessage("".equals(req.getComment())?null:req.getComment());
			auditDataModel.setAdditionalInfo(ASSIGNED_WORK_ORDER_TO+r.getSignumID());
			auditDataModel.setCommentCategory(AppConstants.WO_CREATE_PLAN);
			auditManagementService.addComment(auditDataModel);
		}
	}
	
	public void woManagement_editWOPriority(AuditDataProcessorRequestModel auditRequest){
		AuditDataModel auditDataModel=new AuditDataModel();
		String woId=getPathParamValueByIndex(auditRequest,1).toString();
		auditDataModel.setAuditPageId(Integer.valueOf(woId));
		auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
		Map param = (Map)auditRequest.getRequestParams();
		String[] comment =(String[])param.get("comment");
		auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE +woId);
		auditDataModel.setCreatedBy(getPathParamValueByIndex(auditRequest,3).toString());
		auditDataModel.setNewValue(getPathParamValueByIndex(auditRequest,2).toString());
		auditDataModel.setFieldName(AppConstants.PRIORITY);
		auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
		auditDataModel.setAdditionalInfo(comment[0]);
		String actorType=getPathParamValueByIndex(auditRequest,4).toString();
		if(actorType.equalsIgnoreCase(AppConstants.ACTOT_TYPE_PM)){
		auditDataModel.setActorType(AppConstants.ACTOT_TYPE_PM);
		}
		else if(actorType.equalsIgnoreCase(AppConstants.ACTOT_TYPE_DR)){
			auditDataModel.setActorType(AppConstants.ACTOT_TYPE_DR);
		}
		auditDataModel.setCommentCategory(AppConstants.WO_PRIORITY_EDIT);
		auditManagementService.addToAudit(auditDataModel);
	}
	
	public void woExecution_assignWo(AuditDataProcessorRequestModel auditRequest){
		AssignWOModel request= (AssignWOModel) auditRequest.getRequestBody();
		for(WorkOrderModel woModel:request.getWorkOrderModel()){
			AuditDataModel auditDataModel=new AuditDataModel();
			auditDataModel.setAuditPageId(woModel.getwOID());
			auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE + woModel.getwOID());
			auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
			auditDataModel.setCreatedBy(woModel.getSenderSignum());
			auditDataModel.setNewValue(woModel.getSignumID());
			auditDataModel.setFieldName(ASSIGNEE);
			auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
			auditDataModel.setActorType(AppConstants.ACTOT_TYPE_SP);
			if(woModel.getUiStatus()==null) {
				auditDataModel.setCommentCategory("ASSIGN_WORK_ORDER");
				auditDataModel.setAdditionalInfo(ASSIGNED_WORK_ORDER_TO+woModel.getSignumID());
			}
			else if(woModel.getUiStatus().equalsIgnoreCase("TRANSFER")) {
			auditDataModel.setCommentCategory(AppConstants.WO_TRANSFER);
			auditDataModel.setAdditionalInfo("Transferred workOrder to "+woModel.getSignumID());
			}
			auditManagementService.addToAudit(auditDataModel);
		}
	}
	
	public void woManagement_massUpdateWorkOrder(AuditDataProcessorRequestModel auditRequest) {
		TransferWorkOrderModel request= (TransferWorkOrderModel) auditRequest.getRequestBody();
		for(int woid:request.getWoID()){
			AuditDataModel auditDataModel=new AuditDataModel();
			auditDataModel.setAuditPageId(woid);
			auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE + woid);
			auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
			auditDataModel.setCreatedBy(request.getSenderID());
			auditDataModel.setNewValue(request.getReceiverID());
			auditDataModel.setFieldName(ASSIGNEE);
			auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
			auditDataModel.setActorType(AppConstants.ACTOT_TYPE_SP);
			auditDataModel.setCommentCategory(AppConstants.WO_TRANSFER);
			auditManagementService.addToAudit(auditDataModel);
		}
		
			
	}
	
	public void externalInterface_createWorkOrderPlan(AuditDataProcessorRequestModel auditRequest){
		CreateWorkOrderModel req= (CreateWorkOrderModel) auditRequest.getRequestBody();
		CreateWoResponse response= (CreateWoResponse) auditRequest.getResponse();
		for(CreateWorkOrderModel2 r:response.getWorkOrderID()){
//		for(CreateWorkOrderModel2 r:response.getResponseData()){
			AuditDataModel auditDataModel=new AuditDataModel();
			auditDataModel.setAuditPageId(r.getWoId());
			auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
			auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE+r.getWoId());
			auditDataModel.setCreatedBy(r.getCreateBy());
			auditDataModel.setActorType(AppConstants.ACTOT_TYPE_PM);
			auditDataModel.setMessage("".equals(req.getComment())?null:req.getComment());
			auditDataModel.setAdditionalInfo(ASSIGNED_WORK_ORDER_TO+r.getSignumID());
			auditDataModel.setCommentCategory(AppConstants.WO_CREATE_PLAN);
			auditManagementService.addComment(auditDataModel);
		}
	}
	
	public void mobileController_createServiceRequest(AuditDataProcessorRequestModel auditRequest){
		try {
			String json =(String)auditRequest.getRequestBody();
			json=new String(aesUtil.decrypt( Base64.getDecoder().decode(json.getBytes())));
			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(json);
			CreateWorkOrderModel req= gson.fromJson(object, CreateWorkOrderModel.class);;
			
			Response<CreateSrResponse> response= (Response<CreateSrResponse>) auditRequest.getResponse();
			CreateSrResponse createSrResponse= response.getResponseData();
			for(CreateWorkOrderModel2 createWorkOrderModel2: response.getResponseData().getWorkOrderDetails().getWorkOrderID()) {
				AuditDataModel auditDataModel=new AuditDataModel();
				auditDataModel.setAuditPageId(createWorkOrderModel2.getWoId());
				auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
				auditDataModel.setContext(AppConstants.FIED_SERVICE_REQUEST+AppConstants.UNDERSCORE+createSrResponse.getServiceRequestID());
				auditDataModel.setCreatedBy(req.getCreatedBy());
				auditDataModel.setMessage("".equals(req.getComment())?null:req.getComment());
				auditDataModel.setAdditionalInfo(ASSIGNED_WORK_ORDER_TO+createWorkOrderModel2.getSignumID());
				auditDataModel.setCommentCategory(AppConstants.SR_COMMENT_CATEGORY);
				auditDataModel.setSourceId(getAutoSenseSourceIdOnSourceName(req.getExternalSourceName()));
				auditDataModel.setActorType((String)auditRequest.getRequestHeaders().get(AppConstants.ROLE));
				auditDataModel.setRefferer("Mobile");
				auditManagementService.addComment(auditDataModel);
			}
		}
		catch(Exception e) {
			LOG.error(e.getMessage());
		}
		
	}
	
	private Integer getAutoSenseSourceIdOnSourceName(String sourceName) {
		return wOExecutionDAO.getAutoSenseSourceIdOnSourceName(sourceName);
	}
	
	public void woManagement_updateWorkOrderDetails(AuditDataProcessorRequestModel auditRequest) {
		WorkOrderCompleteDetailsModel request=(WorkOrderCompleteDetailsModel) auditRequest.getRequestBody();
		AuditDataModel auditDataModel=new AuditDataModel();
		String woId=Integer.toString(request.getwOID());
		auditDataModel.setAuditPageId(Integer.valueOf(woId));
		auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_WORK_ORDER);
		auditDataModel.setContext(AppConstants.AUDIT_PREFIX_WORK_ORDER+AppConstants.UNDERSCORE +woId);
		auditDataModel.setCreatedBy(request.getLastModifiedBy());
		auditDataModel.setNewValue(request.getwOName()+AppConstants.UNDERSCORE+request.getStartDate());
		auditDataModel.setOldValue(request.getOldWoName()+AppConstants.UNDERSCORE+request.getOldStartDate());
		auditDataModel.setFieldName("WoName_StartDate");
		auditDataModel.setMessage(AppConstants.MSG_FIELDNAME_NEWVALUE);
//		auditDataModel.setAdditionalInfo(request.getStatusComment());
		auditDataModel.setActorType(AppConstants.ACTOT_TYPE_SP);
		auditDataModel.setCommentCategory("WO_UPDATE_External_WO");
		auditManagementService.addToAudit(auditDataModel);
	}
	
}
