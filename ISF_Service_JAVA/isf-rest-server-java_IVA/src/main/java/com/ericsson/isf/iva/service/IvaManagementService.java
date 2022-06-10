package com.ericsson.isf.iva.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ericsson.isf.iva.dao.IvaManagementDAO;
import com.ericsson.isf.iva.exception.ApplicationException;
import com.ericsson.isf.iva.model.BookingDetailsModel;
import com.ericsson.isf.iva.model.HeaderModel;
import com.ericsson.isf.iva.model.IsfResponseModel;
import com.ericsson.isf.iva.model.ServerBotModel;
import com.ericsson.isf.iva.model.SignalrModel;
import com.ericsson.isf.iva.model.SignalrResponse;
import com.ericsson.isf.iva.model.StepDetailsModel;
import com.ericsson.isf.iva.model.Task;
import com.ericsson.isf.iva.model.WorkFlowModel;
import com.ericsson.isf.iva.model.WorkOrderModel;
import com.ericsson.isf.iva.profiles.configuration.AppConfig;
import com.ericsson.isf.iva.util.AppConstants;
import com.ericsson.isf.iva.util.AppConstants.VALID_WO_STATUS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.util.IOUtils;

import retrofit2.Response;


/**
 * 
 * @author ehrmsng
 *
 */
@Service
public class IvaManagementService {
	private static final Logger LOGGER = LoggerFactory.getLogger(IvaManagementService.class);

	
	
	@Autowired
	private IvaManagementDAO ivaManagementDAO;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private AppConfig appconfig;
	
	@Autowired
	private ValidationUtilityService validationUtilityService;
	
	@Autowired
	private IsfApplicationServiceBuilder isfApplicationServiceBuilder;
	
	private static String CONNECTION_ERROR="connection could not be setup: %s";
	private static String RESPONSE_ERROR="Error: %s";
	private static String API_NAME="apiName";
	private static final String STATUS = "status";
	private static final String STEPID = "stepId";
	private static final String REASON = "reason";
	
	public IsfResponseModel<Map<String, Object>> startTask(String serverBotModel, String signum){
		
		ServerBotModel serverBot;
			try {
				serverBot = objectMapper.readValue(StringEscapeUtils.unescapeHtml(StringUtils.trim(serverBotModel)),ServerBotModel.class);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
				throw new ApplicationException(400, e1.getMessage());
			}
		
		serverBot.setSignumID(signum);
		
		Response<IsfResponseModel<Map<String, Object>>> response;
		IsfApplicationService isfApplicationService;

		
		try {
			isfApplicationService=isfApplicationServiceBuilder.getIsfApplicationService();
			response=isfApplicationService.startTask(serverBot).execute();
			
			if(response.isSuccessful()) {
				LOGGER.info("Sucess startTask:");
				if(!response.body().isValidationFailed()) {
					SignalrModel signalRModel = returnSignalrConfiguration(signum,AppConstants.UPDATE_FLOATING_WINDOW);
					callSignalrApplicationToCallSignalRHub(signalRModel);
				}
				return response.body();
			}
			
			InputStream errorStream=response.errorBody().byteStream();
			String errorResponse=IOUtils.readInputStreamToString(errorStream);
			LOGGER.info( String.format(RESPONSE_ERROR, errorResponse));
			return objectMapper.readValue(errorResponse, new TypeReference<IsfResponseModel<Map<String, Object>>>() {});
		} catch (IOException e) {
			LOGGER.info( String.format(CONNECTION_ERROR, e.getMessage()));
			throw new ApplicationException(500,String.format(CONNECTION_ERROR, e.getMessage()));
		}
	}
	
	
	public String checkIvaAop(String signum) {
        LOGGER.info("check for AOP Called : ===== {}" , signum);
        return "UserAccessAspect is called";
    }
	
	public SignalrModel returnSignalrConfiguration(String payload, String methodName) {
		SignalrModel signalRModel = new SignalrModel();
		signalRModel.setHubName(appconfig.getSignalrHubName());
		signalRModel.setHubUrl(appconfig.getSignalrHubUrl());
		signalRModel.setMethodName(methodName);
		signalRModel.setExecutionType(AppConstants.SIGNALR_EXECUTION_TYPE);
		signalRModel.setPayload(payload);
		return signalRModel;
	}
	
	
	public void callSignalrApplicationToCallSignalRHub(SignalrModel signalRModel) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					connectSignalrApplication(signalRModel);
				} catch (Exception e) {
					LOGGER.debug(String.format("Exception thrown  %s", e.getMessage()));
				}

			}
		}).start();

	}
	
	public void connectSignalrApplication(SignalrModel signalRModel) {
		System.out.println(Thread.currentThread().getName());
		RestTemplate restTemplate = new RestTemplate();
		String url = appconfig.getSignalrAppUrl()
				+ AppConstants.CALL_SIGNALR_API;
		LOGGER.info("url called============:" + url);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SignalrModel> request = new HttpEntity<>(signalRModel, headers);
		try {
			restTemplate.exchange(url, HttpMethod.POST, request, SignalrResponse.class);
		} catch (RestClientException ex) {
			LOGGER.debug(String.format("Exception thrown  %s", ex.getMessage()));
		}
	}
	
	public IsfResponseModel<Map<String, Object>> stopTask(ServerBotModel serverBotModel, String signum){
		
		serverBotModel.setSignumID(signum);
		
		Response<IsfResponseModel<Map<String, Object>>> response;
		IsfApplicationService isfApplicationService;
		
		try {
			isfApplicationService=isfApplicationServiceBuilder.getIsfApplicationService();
			response=isfApplicationService.stopTask(serverBotModel).execute();
			
			if(response.isSuccessful()) {
				LOGGER.info("Sucess stopTask:");
				if(!response.body().isValidationFailed()) {
					SignalrModel signalRModel = returnSignalrConfiguration(signum,AppConstants.UPDATE_FLOATING_WINDOW);
					callSignalrApplicationToCallSignalRHub(signalRModel);
				}
				return response.body();
			}
			
			
			InputStream errorStream=response.errorBody().byteStream();
			String errorResponse=IOUtils.readInputStreamToString(errorStream);
			LOGGER.info( String.format(RESPONSE_ERROR, errorResponse));
			return objectMapper.readValue(errorResponse, new TypeReference<IsfResponseModel<Map<String, Object>>>() {});
		} catch (IOException e) {
			LOGGER.info( String.format(CONNECTION_ERROR, e.getMessage()));
			throw new ApplicationException(500,String.format(CONNECTION_ERROR, e.getMessage()));
		}
	}

	public IsfResponseModel<Map<String, Object>> updateBookingDetailsStatus(ServerBotModel serverBotModel,
			String signum) {
		validateUpdateBookingDetailsStatus(serverBotModel,signum);

		Response<IsfResponseModel<Map<String, Object>>> response;
		IsfApplicationService isfApplicationService;

		try {
			isfApplicationService = isfApplicationServiceBuilder.getIsfApplicationService();
			response = isfApplicationService.updateBookingDetailsStatus(serverBotModel.getwOID(),
					serverBotModel.getSignumID(), serverBotModel.getTaskID(), serverBotModel.getBookingID(),
					serverBotModel.getStatus(), serverBotModel.getReason(), serverBotModel.getStepID(),
					serverBotModel.getFlowChartDefID(), serverBotModel.getRefferer()).execute();

			if (response.isSuccessful()) {
				LOGGER.info("Sucess updateBookingDetailsStatus:");
				if(!response.body().isValidationFailed()) {
					SignalrModel signalRModel = returnSignalrConfiguration(signum,AppConstants.UPDATE_FLOATING_WINDOW);
					callSignalrApplicationToCallSignalRHub(signalRModel);
				}
				return response.body();
			}

			InputStream errorStream = response.errorBody().byteStream();
			String errorResponse = IOUtils.readInputStreamToString(errorStream);
			LOGGER.info(String.format(RESPONSE_ERROR, errorResponse));
			return objectMapper.readValue(errorResponse, new TypeReference<IsfResponseModel<Map<String, Object>>>() {
			});
		} catch (IOException e) {
			LOGGER.info(String.format(CONNECTION_ERROR, e.getMessage()));
			throw new ApplicationException(500, String.format(CONNECTION_ERROR, e.getMessage()));
		}
	}


	private void validateUpdateBookingDetailsStatus(ServerBotModel serverBotModel, String signum) {
		validationUtilityService.validateSignumInModel(serverBotModel.getSignumID(), signum);
		if(!(StringUtils.equalsIgnoreCase(serverBotModel.getStatus(), "ONHOLD") 
				||StringUtils.equalsIgnoreCase(serverBotModel.getStatus(), "SKIPPED")) ) {
			throw new ApplicationException(200,String.format(AppConstants.PROVIDE_VALID, STATUS));
		}
		else if(StringUtils.isEmpty(serverBotModel.getStepID())){
			throw new ApplicationException(200,String.format(AppConstants.PROVIDE_VALID, STEPID));
		}
		else if(StringUtils.isEmpty(serverBotModel.getReason())) {
			throw new ApplicationException(200,String.format(AppConstants.PROVIDE_VALID, REASON));
		}
	}

	
	public IsfResponseModel<Void> addStepDetailsForFlowChart(StepDetailsModel stepDetailsModel, String signum) {
		validationUtilityService.validateIfWorkOrderAssignedToSignum(signum, stepDetailsModel.getWoId(),
				stepDetailsModel.getSignumId());
		
		Response<IsfResponseModel<Void>> response;
		IsfApplicationService isfApplicationService;
		
		try {
			isfApplicationService=isfApplicationServiceBuilder.getIsfApplicationService();
			response=isfApplicationService.addStepDetailsForFlowChart(stepDetailsModel).execute();
			
			if(response.isSuccessful()) {
				LOGGER.info("Sucess addStepDetailsForFlowChart:");
				return response.body();
			}
			
			InputStream errorStream=response.errorBody().byteStream();
			String errorResponse=IOUtils.readInputStreamToString(errorStream);
			LOGGER.info( String.format(RESPONSE_ERROR, errorResponse));
			return objectMapper.readValue(errorResponse, new TypeReference<IsfResponseModel<Void>>() {});
		} catch (IOException e) {
			LOGGER.info( String.format(CONNECTION_ERROR, e.getMessage()));
			throw new ApplicationException(500,String.format(CONNECTION_ERROR, e.getMessage()));
		}
	}
	
	public IsfResponseModel<List<WorkFlowModel>> getWOWorkFlow(int wOID, boolean isQualified, String signum) {

		IsfResponseModel<List<WorkFlowModel>> response = new IsfResponseModel<>();
			
		WorkOrderModel workOrderModel = validationUtilityService.getWorkOrderModel(wOID);
			
		validateForGetWOWorkFlow(wOID, workOrderModel, signum);
			
		List<WorkFlowModel> workFlowModels = getWorkFlow(wOID, isQualified, workOrderModel);
			
		response.setResponseData(workFlowModels);
		
		return response;
	
	}


	private List<WorkFlowModel> getWorkFlow(int wOID, boolean isQualified, WorkOrderModel workOrderModel){
		
		List<WorkFlowModel> workFlowModels ;
		
		if(StringUtils.equalsIgnoreCase(workOrderModel.getStatus(), AppConstants.VALID_WO_STATUS.ASSIGNED.toString())) {
			if (isQualified) {
				Map<String, Object> wfData = getWorkFlowNameForWoID(wOID);
				
				workFlowModels = getWorkFlowJSONData(wOID, wfData);
				if (workFlowModels==null) {
					throw new ApplicationException(200,
							"No QualifiedWorkFlow avialable for given WorkOrder!! Please switch to the Novice WorkFlow");
				}
				 
				validationUtilityService.validateSignumProficientforWO(workOrderModel.getSubActivityID(),(Integer)wfData.get("WFID") , workOrderModel.getSignumID());
			}
			else {
				workFlowModels = getWOWorkFlow(wOID, workOrderModel);
			}
		}
		else {
			Map<String, Object> wfData = getWorkFlowNameForWoID(wOID);
			if (StringUtils.equalsIgnoreCase((String) wfData.get("Type"), "PROJECTDEFINED")
						&& isQualified) {
				throw new ApplicationException(200,
							"WOID status is not in Assigned state , Expert type Work Flow can not be opened for given WOID ");
			}
			else if(StringUtils.equalsIgnoreCase((String) wfData.get("Type"), "PROJECTDEFINED_EXPERT")
						&& !isQualified) {
				throw new ApplicationException(200,
							"WOID status is not in Assigned state , Novice type Work Flow can not be opened for given WOID ");
			}
			workFlowModels = getWOWorkFlow(wOID, workOrderModel);
		}

		
		if (workFlowModels==null) {
			throw new ApplicationException(200,
					"No QualifiedWorkFlow avialable for given WorkOrder!! Please switch to the Novice WorkFlow");
		}
		 
		
		return workFlowModels;

	}
	
	private List<WorkFlowModel> getWOWorkFlow(int wOID, WorkOrderModel workOrderModel) {

		List<WorkFlowModel> wo= ivaManagementDAO.getWOWorkFlowWithWFNoAndWOID(wOID,workOrderModel.getWfVersion());
		return wo;
    }
	

	private List<WorkFlowModel> getWorkFlowJSONData(int wOID, Map<String, Object> wfData) {
		
		List<WorkFlowModel> workFlow =null;
		String wfName;
		
		Integer expertDefId = (Integer) wfData.get("flowchartdefid");
		if (StringUtils.equalsIgnoreCase((String) wfData.get("Type"), "PROJECTDEFINED")) {
			wfName = wfData.get("WorkFlowName") + "_" + "Expert";
			expertDefId = getExpertDefID(wfName, wfData.get("WFID").toString());
		} else {
			wfName = (String) wfData.get("WorkFlowName");
		}
		if (expertDefId != null) {
			workFlow = getExpertWorkFlow(wOID, wfName, expertDefId);
		}
		return workFlow;
	}


	private List<WorkFlowModel> getExpertWorkFlow(int wOID, String wfName, Integer expertDefId) {
		return ivaManagementDAO.getExpertWorkFlow(wOID, wfName, expertDefId);
	}


	private Integer getExpertDefID(String wfName, String wfID) {
		return ivaManagementDAO.getExpertDefID(wfName, wfID);
	}


	private Map<String, Object> getWorkFlowNameForWoID(int wOID) {
		return ivaManagementDAO.getWorkFlowNameForWoID(wOID);
	}


	private void validateForGetWOWorkFlow(int wOID, WorkOrderModel workOrderModel, String signum) {
		
		validationUtilityService.validateWOIDForNullOrZero(wOID, workOrderModel);
		validationUtilityService.validateIfWorkOrderAssignedToSignum(signum, wOID);
		validationUtilityService.validateWOIDIsActive(workOrderModel);	
	}


	public IsfResponseModel<Map<String, Object>> getWorkOrders(WorkOrderModel workOrderModel, String signum) {
		
		Response<Map<String, Object>> response;
		IsfApplicationService isfApplicationService;
		IsfResponseModel<Map<String, Object>> isfResponseModel= new IsfResponseModel<>();
		
		validateGetWorkOrders(workOrderModel, signum);
		if(StringUtils.isBlank(workOrderModel.getPlannedStartDate()) ^ StringUtils.isBlank(workOrderModel.getPlannedEndDate())) {
			isfResponseModel.addFormWarning( "Both PlannedstartDate and plannedcloesdOn are required for date filter : "
					+ "If given only one of date results are not date filtered");
		}
		
		try {
			isfApplicationService=isfApplicationServiceBuilder.getIsfApplicationService();
			response=isfApplicationService.getWorkOrders(workOrderModel.getSignumID(),workOrderModel.getStatus(),
					workOrderModel.getPlannedStartDate(),workOrderModel.getPlannedEndDate()).execute();
			
			if(response.isSuccessful()) {				
				LOGGER.info("Sucess getWorkOrders:");
				isfResponseModel.setResponseData(response.body());
				return isfResponseModel;
				
			}
			InputStream errorStream=response.errorBody().byteStream();
			String errorResponse=IOUtils.readInputStreamToString(errorStream);
			LOGGER.info( String.format(RESPONSE_ERROR, errorResponse));
			return objectMapper.readValue(errorResponse, new TypeReference<IsfResponseModel<Map<String, Object>>>() {});
		} catch (IOException e) {
			LOGGER.info( String.format(CONNECTION_ERROR, e.getMessage()));
			throw new ApplicationException(500,String.format(CONNECTION_ERROR, e.getMessage()));
		}
	}

	public void isHeaderSignumExist(int getwOID, String signum) {
		boolean isHeaderSignumExist=ivaManagementDAO.isHeaderSignumExist(getwOID, signum);
		if(!isHeaderSignumExist) {
			throw new ApplicationException(HttpServletResponse.SC_UNAUTHORIZED,"provided workorder id is worng for");
			}
	}


	private void validateGetWorkOrders(WorkOrderModel workOrderModel, String signum) {
		validationUtilityService.validateSignumInModel(workOrderModel.getSignumID(), signum);
		validationUtilityService.validateStringForBlank(workOrderModel.getStatus(), "Status");
		validationUtilityService.validateWorkOrderStatus(workOrderModel.getStatus());
		if(!StringUtils.isBlank(workOrderModel.getPlannedStartDate())) {
			validationUtilityService.validateDateFormat(workOrderModel.getPlannedStartDate(),"yyyy-MM-dd", "plannedstartDate");
		}
		if(!StringUtils.isBlank(workOrderModel.getPlannedEndDate())) {
			validationUtilityService.validateDateFormat(workOrderModel.getPlannedEndDate(),"yyyy-MM-dd", "plannedclosedOn");
		}
		
	}
	
	public IsfResponseModel<Map<String, Object>> checkParallelWorkOrderDetails(ServerBotModel serverBotModel, String signum) {

		validateCheckParallelWorkOrderDetails(serverBotModel,signum);

		Response<Map<String,Object>> response;
		IsfResponseModel<Map<String, Object>> isfResponse=new IsfResponseModel<>();
		IsfApplicationService isfApplicationService;

		try {
			isfApplicationService = isfApplicationServiceBuilder.getIsfApplicationService();
			response = isfApplicationService.checkParallelWorkOrderDetails(serverBotModel.getSignumID(),
					true, serverBotModel.getExecutionType(), serverBotModel.getwOID(),
					serverBotModel.getTaskID(), serverBotModel.getProjectId(), serverBotModel.getVersionNO(),
					serverBotModel.getSubActivityFlowChartDefID(), serverBotModel.getStepID()).execute();
			
			if (response.isSuccessful()) {
				LOGGER.info("Sucess checkParallelWorkOrderDetails:");
				isfResponse.setResponseData(response.body());
				
			}

		} catch (Exception e) {
			LOGGER.info("Exception thrown in checkParallelWorkOrderDetails is "+ e.getMessage());
			isfResponse.addFormError(e.getMessage());
			
		}
		return isfResponse;
	
	}


	private void validateCheckParallelWorkOrderDetails(ServerBotModel serverBotModel, String signum) {
		validationUtilityService.validateSignumInModel(serverBotModel.getSignumID(), signum);
		validationUtilityService.validateCheckParallelWorkOrderDetails(serverBotModel,"");
		
	}


	public IsfResponseModel<BookingDetailsModel> getBookingDetailsByBookingId(int bookingId, String signum) {
		Response<IsfResponseModel<BookingDetailsModel>> response;
		IsfResponseModel<BookingDetailsModel> isfResponse=new IsfResponseModel<>();
		IsfApplicationService isfApplicationService;
        validateSignumForBookingId(bookingId,signum);
		List<HeaderModel> headers = new LinkedList<>();
		try {
			isfApplicationService = isfApplicationServiceBuilder.getIsfApplicationService();
			response = isfApplicationService.getBookingDetailsByBookingId(bookingId).execute();
			
			if (response.isSuccessful()) {
				LOGGER.info("Sucess getBookingDetailsByBookingId:");
				if(response.body().getResponseData()!=null) {
					isfResponse.setResponseData(response.body().getResponseData());
				}else {
					isfResponse.setFormErrors(response.body().getFormErrors());
				}	
			}

		} catch (Exception e) {
			LOGGER.info("Exception thrown in getBookingDetailsByBookingId is "+ e.getMessage());
			isfResponse.addFormError(e.getMessage());
			
		}
		return isfResponse;	
	}
	
	private void validateSignumForBookingId(int bookingId, String signum) {
		
		validationUtilityService.validateSignumForBookingId(bookingId, signum);
	}


	public IsfResponseModel<List<LinkedHashMap<String, Object>>> getInprogressTask(String signum) {
		
		Response<IsfResponseModel<List<LinkedHashMap<String, Object>>>> response;
		IsfApplicationService isfApplicationService;
		IsfResponseModel<List<LinkedHashMap<String, Object>>> isfResponseModel= new IsfResponseModel<>();
		try {
			isfApplicationService=isfApplicationServiceBuilder.getIsfApplicationService();
			response=isfApplicationService.getInprogressTask(signum).execute();
			
			if(response.isSuccessful()) {				
				LOGGER.info("Sucess getWorkOrders:");
				return response.body();	
			}
			
			InputStream errorStream=response.errorBody().byteStream();
			String errorResponse=IOUtils.readInputStreamToString(errorStream);
			LOGGER.info( String.format(RESPONSE_ERROR, errorResponse));
			return  objectMapper.readValue(errorResponse, new TypeReference<IsfResponseModel<List<LinkedHashMap<String, Object>>>>() {});
			
		} catch (IOException e) {
			LOGGER.info( String.format(CONNECTION_ERROR, e.getMessage()));
			throw new ApplicationException(500,String.format(CONNECTION_ERROR, e.getMessage()));
		}
	} 
	
}
