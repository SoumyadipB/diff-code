package com.ericsson.isf.iva.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.isf.iva.dao.IvaManagementDAO;
import com.ericsson.isf.iva.exception.ApplicationException;
import com.ericsson.isf.iva.model.ServerBotModel;
import com.ericsson.isf.iva.model.WorkOrderModel;
import com.ericsson.isf.iva.util.AppConstants;

/**
 * 
 * This is a common utility service class which provides all validation method
 * which are mostly used by modules.
 * 
 * @author eakinhm
 *
 */
@Service
public class ValidationUtilityService {

	private static final String WOID = "WOID: ";
	private static final String WORK_ORDER_ID = "Work Order ID";
	private static final String WORK_ORDER_SIGNUM ="Work Order is not assigned to signum %s";
	private static final String COMMON_VALUE =" is not associated with ";
	private static final String VALIDATIONS="validations";
	private static final String WOID_NULL_OR_ZERO="validateWOIDForNullOrZero";
	private static final String WOID_ACTIVE="validateWOIDIsActive";
	private static final String WO_UNASSIGNED="validateIfWorkorderIsUnassigned";
	private static final String WOID_CLOSED_DEFERRED_REJECTED="validateWOIDForClosedDeferredAndRejectedStatus";
	private static final String WOID_ASSIGNED_SIGNUM = "validateWOIDForSignum";
	private static final String PROJECTID_NULL_OR_ZERO = "validateProjectIDForNullOrZero";
	private static final String EXECUTION_TYPE_NULL_BLANK = "validateExecutionTypeBlankNull";
	private static final String VERSION_NUMBER_NULL_OR_ZERO = "validateVersionNumberForNullOrZero";
	private static final String FLOWCHARTDEFID_NULL_OR_ZERO = "validateFlowChartDefIdForNullOrZero";
	private static final String FLOWCHARTDEFID_VALUE = "validateFlowChartDefIdValue";
	private static final String STEPID_NULL_BLANK = "validateStepIdBlankNull";
	private static final String TASK_ID_NULL_OR_ZERO = "validateTaskIdBlankNull";
	private static final String VALIDATE_DETAILS_VALUE = "validateDetailsValue";
	
	@Autowired
	private IvaManagementDAO ivaManagementDAO;
	
	public void validateSignumInModel(String signum, String headerSignum) {

		if(StringUtils.isEmpty(signum) || StringUtils.equalsAnyIgnoreCase(signum, "null")) {
			throw new ApplicationException(200, AppConstants.NULL_BLANK_SIGNUM);
		}
		else if(!signum.equalsIgnoreCase(headerSignum)) {
			throw new ApplicationException(200, AppConstants.SIGNUM_ERROR_MESSAGE);
		}
	}

	/**
	 * This method validate if given signum is valid, against given woid
	 * 
	 * @param signumID
	 * @param workOrderModel
	 */
	public void validateIfWorkOrderAssignedToSignum(String headerSignum, int workOrderID,String signum) {
		validateSignumInModel(signum, headerSignum);
		getWorkOrderModel(workOrderID);
		if (!ivaManagementDAO.isWorkOrderAssignedToSignum(headerSignum, workOrderID)) {

			throw new ApplicationException(200, String.format(WORK_ORDER_SIGNUM, headerSignum));
		}
	}
	public void validateIfWorkOrderAssignedToSignum(String headerSignum, int workOrderID) {
		getWorkOrderModel(workOrderID);
		if (!ivaManagementDAO.isWorkOrderAssignedToSignum(headerSignum, workOrderID)) {

			throw new ApplicationException(200, String.format(WORK_ORDER_SIGNUM, headerSignum));
		}
	}

	public void validateWOIDForNullOrZero(Integer woID) {

		WorkOrderModel workOrderModel = getWorkOrderModel(woID);

		validateWOIDForNullOrZero(woID, workOrderModel);
	}

	public void validateWOIDForNullOrZero(Integer woID, WorkOrderModel workOrderModel) {

		validateIntegerForNullOrZero(woID, WORK_ORDER_ID);

		validateWorkOrderModelForNull(workOrderModel);
	}

	public void validateIntegerForNullOrZero(Integer integerValue, String variableName) {

		if (integerValue == null || integerValue == 0) {

			throw new ApplicationException(200, String.format(AppConstants.PLEASE_PROVIDE_VALUE, variableName));
		}

	}

	private void validateWorkOrderModelForNull(WorkOrderModel workOrderModel) {

		if (workOrderModel == null) {

			throw new ApplicationException(200, String.format(AppConstants.PLEASE_PROVIDE_VALUE, WORK_ORDER_ID));
		}
	}

	public WorkOrderModel getWorkOrderModel(Integer woID) {

		WorkOrderModel workOrderModel = ivaManagementDAO.getWorkOrderDetailsById(woID);

		validateWorkOrderModelForNull(workOrderModel);

		return workOrderModel;
	}
	
	public void validateStringForBlank(String stringValue, String variableName) {

		if (StringUtils.isBlank(stringValue)) {

			throw new ApplicationException(200, String.format(AppConstants.PLEASE_PROVIDE_VALUE, variableName));
		}
	}
	
	public void validateWOIDIsActive(WorkOrderModel workOrderModel) {

		if (!workOrderModel.getActive()) {
			throw new ApplicationException(200, "Given woID is not active!");
		}
	}
	public void validateCheckParallelWorkOrderDetails(ServerBotModel serverBotModel,String apiName) {
		WorkOrderModel workOrderModel = ivaManagementDAO.getWorkOrderDetailsById(serverBotModel.getwOID());
		apiName=(apiName!= "")?apiName:"/ivaManagement/checkParallelWorkOrderDetails";

		List<String> json = ivaManagementDAO.getValidateJsonForApi(apiName);
		if (json.isEmpty()) {

			throw new ApplicationException(500, "Invalid source Name or validateJson found!");
		}

		JSONObject obj = new JSONObject(json.get(0));
		JSONArray array = obj.getJSONArray(VALIDATIONS);

		if (array == null || array.length() == 0) {
			throw new ApplicationException(500, "Given Source name has invalid validateJson!");
		}
		
		for(int i = 0; i < array.length(); i++) {
			switch (String.valueOf(array.get(i))) {
			
				case WOID_NULL_OR_ZERO:
					validateWOIDForNullOrZero(serverBotModel.getwOID(), workOrderModel);
					break;
				case WOID_ACTIVE:
					validateWOIDIsActive(workOrderModel);
					break;
				case WO_UNASSIGNED:
					validateIfWorkorderIsUnassigned(workOrderModel);
					break;
				
				case WOID_CLOSED_DEFERRED_REJECTED:
					validateWOIDForClosedDeferredAndRejectedStatus(workOrderModel);
					break;
				case WOID_ASSIGNED_SIGNUM:
					validateWoidAssignedToSignum(workOrderModel,serverBotModel);
					break;
				case PROJECTID_NULL_OR_ZERO:
					validateIntegerForNullOrZero(serverBotModel.getProjectId(),AppConstants.PROJECT_ID);
					validateProjectId(serverBotModel.getProjectId(),workOrderModel);
					break;
				case EXECUTION_TYPE_NULL_BLANK:
					validateStringForBlank(serverBotModel.getExecutionType(), AppConstants.EXECUTION_TYPE);
					break;
				case VERSION_NUMBER_NULL_OR_ZERO:
					validateIntegerForNullOrZero(serverBotModel.getVersionNO(),AppConstants.VERSION_NUMBER);
					break;
				case FLOWCHARTDEFID_NULL_OR_ZERO:
					validateIntegerForNullOrZero(serverBotModel.getSubActivityFlowChartDefID(),AppConstants.FLOWCHART_DEFID);
					break;
				case FLOWCHARTDEFID_VALUE:
					validateFlowChartDefId(serverBotModel,workOrderModel);
					break;
				case STEPID_NULL_BLANK:
				    validateStringForBlank(serverBotModel.getStepID(), AppConstants.STEPID);
				    break;
				case TASK_ID_NULL_OR_ZERO:
					validateIntegerForNullOrZero(serverBotModel.getTaskID(),AppConstants.TASK_ID);
					break;
				case VALIDATE_DETAILS_VALUE:
					validateDetailsValue(serverBotModel);
					break;
			}
			
		}
	}

	private void validateProjectId(int projectId, WorkOrderModel workOrderModel) {
		if(projectId!= workOrderModel.getProjectid()) {
			 throw new ApplicationException(200,AppConstants.PROJECT_ID+projectId+COMMON_VALUE+WOID+workOrderModel.getwOID()); 
		}
		
	}

	private void validateDetailsValue(ServerBotModel serverBotModel) {
		ServerBotModel serverBotResult=ivaManagementDAO.getStepIdAndExecutionType(serverBotModel.getSubActivityFlowChartDefID(),serverBotModel.getStepID());
		if(serverBotResult==null) {
			 throw new ApplicationException(200,AppConstants.STEPID+serverBotModel.getStepID()+COMMON_VALUE+AppConstants.FLOWCHART_DEFID+serverBotModel.getSubActivityFlowChartDefID()); 
		}
		else if(serverBotModel.getTaskID()!=serverBotResult.getTaskID()) {
			throw new ApplicationException(200,AppConstants.TASK_ID+serverBotModel.getTaskID()+COMMON_VALUE+ AppConstants.STEPID+serverBotModel.getStepID()); 
		}
		else if (!StringUtils.equalsAnyIgnoreCase(serverBotResult.getExecutionType(), serverBotModel.getExecutionType())) {
			 throw new ApplicationException(200,serverBotModel.getExecutionType()+COMMON_VALUE+AppConstants.STEPID+serverBotModel.getStepID()); 
		 }
		 else if(serverBotModel.getVersionNO()!=serverBotResult.getVersionNO()) {
			 throw new ApplicationException(200,AppConstants.VERSION_NUMBER+serverBotModel.getVersionNO()+COMMON_VALUE+ AppConstants.FLOWCHART_DEFID+serverBotModel.getSubActivityFlowChartDefID()); 
		 }
		
	}

	private void validateFlowChartDefId(ServerBotModel serverBotModel, WorkOrderModel workOrderModel) {
		if(workOrderModel.getFlowchartdefid()!=serverBotModel.getSubActivityFlowChartDefID()) {
			 throw new ApplicationException(200,"subActivityFlowChartDefID "+serverBotModel.getSubActivityFlowChartDefID()+" does not exist, please provide a valid subActivityFlowChartDefID"); 
		 }
		
	}

	private void validateWoidAssignedToSignum(WorkOrderModel workOrderModel, ServerBotModel serverBotModel) {
		 if(!StringUtils.equalsAnyIgnoreCase(workOrderModel.getSignumID(), serverBotModel.getSignumID()) ) {
				throw new ApplicationException(200,"WorkOrder "+workOrderModel.getwOID()+" not assigned to "+serverBotModel.getSignumID());
			}
		
	}

	private void validateWOIDForClosedDeferredAndRejectedStatus(WorkOrderModel workOrderModel) {
		if (StringUtils.equalsIgnoreCase(AppConstants.WO_STATUS_REJECTED, workOrderModel.getStatus())
				|| StringUtils.equalsIgnoreCase(AppConstants.WO_STATUS_DEFERRED, workOrderModel.getStatus())
				|| StringUtils.equalsIgnoreCase(AppConstants.CLOSED, workOrderModel.getStatus())) {

			throw new ApplicationException(200,
					"Work Order is either Rejected, Deferred or already Closed, Kindly provide the correct WOID");

		}
		else if( StringUtils.equalsIgnoreCase(AppConstants.STATUS_PLANNED, workOrderModel.getStatus())) {
			throw new ApplicationException(200,
					"Work Order is in Planned state, Kindly provide the correct WOID");
		}
	
	}

	private void validateIfWorkorderIsUnassigned(WorkOrderModel workOrderModel) {
		if (StringUtils.isBlank(workOrderModel.getSignumID())) {
            throw new ApplicationException(200, "Given Work Order is unassigned!");
		}
		
	}

	public void validateDateFormat(String date, String format, String variableName) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setLenient(false);
		try {
			formatter.parse(date);
		} catch (ParseException e) {
			throw new ApplicationException(200,(String.format(AppConstants.PLEASE_PROVIDE_VALUE,
					(new StringBuilder()).append(variableName).append("; Accepted date format is ").append(format).toString())));
		}
		
	}

	public void validateWorkOrderStatus(String statusList) {
		String[] statusArr=Pattern.compile(",").split(statusList);
		
		try {
			for(String status:statusArr) {
				AppConstants.VALID_WO_STATUS.valueOf(status.toUpperCase());
			}
		}
		catch (IllegalArgumentException e) {
	        throw new ApplicationException(200, String.format(AppConstants.PLEASE_PROVIDE_VALUE, "status"));
	    }
		
	}

	public void validateSignumForBookingId(int bookingId, String signumHeader) {
		
		String signum= ivaManagementDAO.getSignumForBookingId(bookingId);
		if(StringUtils.isEmpty(signum)) {
			throw new ApplicationException(200, "This booking id does not exists!");
		}
		else if(!StringUtils.equalsAnyIgnoreCase(signum, signumHeader)) {
			 throw new ApplicationException(200, "BookingId "+bookingId+ " not valid for signum "+signumHeader);
		}
	}
	
	public void validateSignumProficientforWO(int subActivityID, int wFID, String signum) {
		if(!ivaManagementDAO.validateSignumProficientforWO(subActivityID,wFID, signum)) {
			throw new ApplicationException(200, "Given Signum is not an expert user for the Work Order's Work Flow");
		}
	}
}
