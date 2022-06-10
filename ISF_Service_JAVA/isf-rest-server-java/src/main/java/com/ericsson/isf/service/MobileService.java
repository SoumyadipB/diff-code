package com.ericsson.isf.service;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.MobileDAO;
import com.ericsson.isf.exception.ApplicationException;
 

import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.ServiceRequestModel;
import com.ericsson.isf.model.WebNotificationModel;
import com.ericsson.isf.model.WorkOrderDetailsModel;
import com.ericsson.isf.security.aes.AesUtil;
import com.ericsson.isf.service.audit.AuditManagementService;
import com.ericsson.isf.model.AuditDataModel;
import com.ericsson.isf.model.CreateSrResponse;
import com.ericsson.isf.model.CreateWoResponse;
import com.ericsson.isf.model.CreateWorkOrderModel;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.ProjectScopeModel;
import com.ericsson.isf.model.FieldUtilityResourceMappingModel;
import com.ericsson.isf.model.MobileNotificationModel;
import com.ericsson.isf.model.NodeNameValidationModel;
import com.ericsson.isf.model.ProjectModel;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.BeanUtils;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.ericsson.isf.util.IsfCustomIdInsert;

@Service
public class MobileService {
	
	@Autowired
	private MobileDAO mobileDAO;
	
	@Autowired
	private ValidationUtilityService validationUtilityService;
	
	@Autowired
	private WorkOrderPlanService workOrderPlanService;
	
	@Autowired
	AuditManagementService auditManagementService;
	

    @Autowired
    private OutlookAndEmailService emailService;
    
    @Autowired
    ActivityMasterDAO activityMasterDAO;
    
    @Autowired
   	private IsfCustomIdInsert isfCustomIdInsert;
    
    @Autowired
	private AesUtil aesUtil;
    
    @Autowired
	private ApplicationConfigurations configurations;
    
    @Autowired
    private AppService appService;
	
    
	private static final Logger LOG = LoggerFactory.getLogger(MobileService.class);
	
	
	private static final String NO_DATA_FOUND = "No Data Found!";
	private static final String INVALID_INPUT_PROJECTID = "Invalid input... ProjectID cannot be 0 or null !!!";
	private static final String INVALID_INPUT_SRID = "Invalid input... SRID cannot be 0 or null !!!";
	
	private static final String SP="DeliveryExecution/WorkorderAndTask";

	
	public Response<CreateSrResponse> createServiceRequest(CreateWorkOrderModel createWorkOrderModel, String role) { 
		Response<CreateSrResponse> response = new Response<>();
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.DEFAULT_DATE_FORMAT);
			SimpleDateFormat timeFormat = new SimpleDateFormat(AppConstants.TWENTY_FOUR_HOURS_TIME_FORMAT);
			
			BeanUtils.trimAllStrings(createWorkOrderModel);
			String errorMsg = workOrderPlanService.doBasicValidationAndPrepareCreateWorkOrderPlanModel(createWorkOrderModel);
			if (StringUtils.isNotBlank(errorMsg)) {
				throw new ApplicationException(200,errorMsg);
			}
			
			createWorkOrderModel.setDoVolume(1);
			createWorkOrderModel.setPriority(AppConstants.LIVE);
			
			Date date = DateUtils.addHours(new Date(),1);
			createWorkOrderModel.setStartDate(dateFormat.format(date));
			createWorkOrderModel.setStartTime(Time.valueOf(timeFormat.format(date)));
			
			CreateWoResponse createWoResponse= new CreateWoResponse();
			workOrderPlanService.createWorkOrderAndExecutionPlan(createWorkOrderModel, createWoResponse);
			
			ServiceRequestModel serviceRequest=getServiceRequestModel(createWorkOrderModel,createWoResponse.getWorkOrderID().get(0).getDoID(), role);
			mobileDAO.createServiceRequest(serviceRequest);
			int srIdWithInstanceId=isfCustomIdInsert.generateCustomId(serviceRequest.getSrid());
			
			CreateSrResponse createSrResponse= new CreateSrResponse();
			createSrResponse.setWorkOrderDetails(createWoResponse);
			createSrResponse.setServiceRequestID(srIdWithInstanceId);
			response.setResponseData(createSrResponse);
			
		} catch (ApplicationException ae) {
			LOG.error(ae.getErrorMessage());
			response.addFormError(ae.getErrorMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(String.format(AppConstants.GLOBAL_EXCEPTION, e.getMessage()));
		}
		
		return response;
		
	}


	private ServiceRequestModel getServiceRequestModel(CreateWorkOrderModel createWorkOrderModel, int doID, String role) {
		ServiceRequestModel serviceRequestModel	=new ServiceRequestModel()
													.deliverableID(createWorkOrderModel.getScopeID())
													.projectDetails(new ProjectModel().projectID(createWorkOrderModel.getProjectID()))
													.comment(createWorkOrderModel.getComment())
													.srPriority(createWorkOrderModel.getPriority())
													.doid(doID)
													.woName(createWorkOrderModel.getwOName())
													.startTime(createWorkOrderModel.getPlannedStartDate())
													.srSource(createWorkOrderModel.getExternalSourceName())
													.createdBy(createWorkOrderModel.getCreatedBy())
													.srCreatedByProfile(role);
		
		
		if(createWorkOrderModel.getWorkOrderInputFileModel()!=null && CollectionUtils.isNotEmpty(createWorkOrderModel.getWorkOrderInputFileModel().getFile())
				&& StringUtils.isNotEmpty(createWorkOrderModel.getWorkOrderInputFileModel().getFile().get(0).getInputUrl())) {
			serviceRequestModel.setInputURL(createWorkOrderModel.getWorkOrderInputFileModel().getFile().get(0).getInputUrl());
		}else {
			serviceRequestModel.setInputURL(null);
		}
	    if(CollectionUtils.isNotEmpty(createWorkOrderModel.getListOfNode()) && StringUtils.isNotEmpty(createWorkOrderModel.getListOfNode().get(0).getNodeNames())) {
	    	serviceRequestModel.setNodeName(createWorkOrderModel.getListOfNode().get(0).getNodeNames());
	    }else {
	    	serviceRequestModel.setNodeName(null);
	    }
	    if(CollectionUtils.isNotEmpty(createWorkOrderModel.getListOfNode()) && StringUtils.isNotEmpty(createWorkOrderModel.getListOfNode().get(0).getNodeNames())) {
	    	serviceRequestModel.setNodeType(createWorkOrderModel.getListOfNode().get(0).getNodeType());
	    }else {
	    	serviceRequestModel.setNodeType(null);
	    }
	    
		return serviceRequestModel;
	}

	public ResponseEntity<Response<List<NodeNameValidationModel>>> getNodeNamesByProject(int projectID, String type,
			String term) {
		Response<List<NodeNameValidationModel>> result = new  Response<>();
		try {
			
			LOG.info("getNodeNamesByProject:Start");
			validationUtilityService.validateIntForZero(projectID, AppConstants.PROJECT_ID_2);

			int ccGroupID = appService.getCountryCustomerGroupIDByProjectID(projectID);

			List<NodeNameValidationModel> nodeNameList = this.mobileDAO.getNodeNamesByCountryCustomerGroupID(ccGroupID, type, term);
			
			if (nodeNameList.isEmpty()) {
				result.addFormMessage(NO_DATA_FOUND); 
				result.setResponseData(nodeNameList);
			} else {
				result.setResponseData(nodeNameList);
			}
			LOG.info("getNodeNamesByProject:End");
			}
			catch(ApplicationException exe) {
				result.addFormError(exe.getMessage());
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			catch(Exception ex) {
				result.addFormError(ex.getMessage());
				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	public Response<List<ProjectScopeModel>> getFUDeliverablesByProject(int projectID, String term) {
		Response<List<ProjectScopeModel>> response= new Response<>();
		try {
			response.setResponseData(mobileDAO.getFUDeliverablesByProject(projectID,term));
		}
		catch(Exception ex) {
			LOG.error(ex.getMessage());
			response.addFormError(ex.getMessage());
		}
		return response;
	}
	
	public ResponseEntity<Response<List<NodeNameValidationModel>>> getNodeTypeByProjectID(Integer projectID) {
		Response<List<NodeNameValidationModel>> result = new Response<>();
		try {
			LOG.info("getNodeTypeByProjectID:Start");

			if (projectID == null || projectID == 0) {
				throw new ApplicationException(500, INVALID_INPUT_PROJECTID);

			} else {
				int ccGroupID = appService.getCountryCustomerGroupIDByProjectID(projectID);
				List<NodeNameValidationModel> nodeTypes = this.mobileDAO.getNodeTypeByCountryCustomerGroupID(ccGroupID);

				if (nodeTypes.isEmpty()) {
					result.addFormMessage(NO_DATA_FOUND);
					result.setResponseData(nodeTypes);
				} else {
					result.setResponseData(nodeTypes);
				}

			}
			LOG.info("getNodeTypeByProjectID:End");
		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	

	public ResponseEntity<Response<List<FieldUtilityResourceMappingModel>>> getFUProjectsBySignum(String signum, String role) {

		Response<List<FieldUtilityResourceMappingModel>> result = new Response<>();
		try {
			LOG.info("getFUProjectsBySignum : Start");
			List<FieldUtilityResourceMappingModel> fieldUtilityProjectsList = this.mobileDAO.getFUProjectsBySignum(signum, role);

			if (fieldUtilityProjectsList.isEmpty()) {

				result.addFormMessage(NO_DATA_FOUND);
				result.setResponseData(fieldUtilityProjectsList);

			} else {
				result.setResponseData(fieldUtilityProjectsList);
			}
		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	public Response<List<ServiceRequestModel>> getServiceRequestsBySignum(String signum,
			String status, String range, int offset, int length) {
		Response<List<ServiceRequestModel>> response = new Response<>();
		try {
			validateGetServiceRequestsBySignum(offset,length);
			
			LocalDateTime rangeStartDateTime;
			LocalDateTime rangeEndDateTime=LocalDateTime.now();
			
			switch(range.toUpperCase()){
				case AppConstants.RANGE_TODAY:
					rangeStartDateTime =rangeEndDateTime.toLocalDate().atStartOfDay();
					break;
				case AppConstants.RANGE_THIS_WEEK:
					rangeStartDateTime =rangeEndDateTime.minusWeeks(1);
					break;
				case AppConstants.RANGE_THIS_MONTH:
					rangeStartDateTime =rangeEndDateTime.minusMonths(1);
					break;
				case "ALL":
					rangeStartDateTime=null;
					break;
				default:
					throw new ApplicationException(200, "Please provide date range in the following: All, TODAY, THIS WEEK OR THIS MONTH");
			}
			
			response.setResponseData(mobileDAO.getServiceRequestsBySignum(signum, status, rangeStartDateTime, rangeEndDateTime, offset, length));
		} catch (ApplicationException ae) {
			response.addFormError(ae.getMessage());
		}
		catch (Exception ex) {
			LOG.error(ex.getMessage());
			response.addFormError(ex.getMessage());
		}
	
		return response;
	}

	private void validateGetServiceRequestsBySignum(int offset, int length) {
		validationUtilityService.validateIntForZero(length, "length");
		if(offset <0) {
			throw new ApplicationException(200,"Please provide positive offset");
		}
	}
	
	
	public ResponseEntity<Response<Void>> addServiceRequestComment(AuditDataModel auditDataModel) {

		Response<Void> response = new Response<>();

		try {
			if (auditDataModel.getAuditPageId() == null || auditDataModel.getAuditPageId() == 0) {
				throw new ApplicationException(500, INVALID_INPUT_SRID);

			}
			
			int srID= auditDataModel.getAuditPageId();
			
			List<WorkOrderDetailsModel> woDetails = null;

			if (StringUtils.isBlank(auditDataModel.getType())) {
				auditDataModel.setType(AppConstants.AUDIT_TYPE_USER_COMMENT);
			}

			if (StringUtils.equalsIgnoreCase(auditDataModel.getCommentCategory(), "SERVICE_REQUEST")) {
			
				 woDetails = this.mobileDAO.getWODetailsBySRID(srID);
				
			}

			if(CollectionUtils.isNotEmpty(woDetails)) {
			for (int i = 0; i < woDetails.size() ; i++) {

				auditDataModel.setAuditPageId(woDetails.get(i).getwOID());
				auditDataModel.setRefferer("Mobile");
				auditManagementService.insertAuditData(auditDataModel);
				
				if(woDetails.get(i).getSignumID()!= null) {
					saveWebNotification(auditDataModel, srID, woDetails, i);  // saveWebNotification 


				if (auditDataModel.getNotification() != null && auditDataModel.getNotification() > 0) {

					EmployeeModel eDetailsforSignum = activityMasterDAO.getEmployeeBySignum(auditDataModel.getCreatedBy());

				Map<String, Object> placeHolder = auditManagementService
							.enrichCommentForNotification(auditDataModel, eDetailsforSignum);
				placeHolder.put(AppConstants.PAGELINK, SP);
				emailService.sendMail(AppConstants.NOTIFICATION_ADD_COMMENT, placeHolder);
			}
				}

			}
			}
			response.addFormMessage("Comment Added Successfully");

		} catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			response.addFormError(ex.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);

	}


	private void saveWebNotification(AuditDataModel auditDataModel, int srID, List<WorkOrderDetailsModel> woDetails,
			int i) {
		if(StringUtils.equalsIgnoreCase(woDetails.get(i).getStatus(),AppConstants.ASSIGNED) || 
				StringUtils.equalsIgnoreCase(woDetails.get(i).getStatus(), AppConstants.INPROGRESS) || 
				StringUtils.equalsIgnoreCase(woDetails.get(i).getStatus(), AppConstants.ONHOLD) || 
				StringUtils.equalsIgnoreCase(woDetails.get(i).getStatus(), AppConstants.REOPENED)) {
			
			WebNotificationModel webNotificationModel = new WebNotificationModel();
			webNotificationModel.setAuditComments(auditDataModel.getMessage());
			webNotificationModel.setCreatedBy(auditDataModel.getCreatedBy());
			webNotificationModel.setModule("SR");
			webNotificationModel.setNotificationSource("ISF Mobile");
			webNotificationModel.setReferenceId(srID);
			webNotificationModel.setToSignum(woDetails.get(i).getSignumID());
			webNotificationModel.setWoid(auditDataModel.getAuditPageId());
			
			mobileDAO.saveWebNotification(webNotificationModel);
			
		}
	}

    
	public Response<List<AuditDataModel>> getAuditDataForServiceRequest(int srid, Optional<Integer> woid, Integer start, Integer length,
			String searchString) {
		Response<List<AuditDataModel>> response = new Response<>();
		
		try {
				searchString=(searchString==null)? "" :searchString.toUpperCase();
				Map<String,EmployeeModel> fetchedUserDetails=new HashMap<>();
				List<AuditDataModel> filteredComments=new LinkedList<>();
				int count=0;
				
				String workOrderIds;
				List<AuditDataModel> allcomments=new LinkedList<>();
				
	    		if(woid.isPresent()) {
	    			workOrderIds=new StringBuilder(AppConstants.OPENING_BRACES).append(woid.get()).append(AppConstants.CLOSING_BRACES).toString();
	    			if(StringUtils.isBlank(searchString)) {
	    				allcomments=mobileDAO.getAuditDataForServiceRequest(workOrderIds, start, length,false);
	    			}
	    			else {
	    				allcomments=mobileDAO.getAuditDataForServiceRequest(workOrderIds, null, null, false);
	    			}
	    		}
	    		else {
	    			List<Integer> workOrders=mobileDAO.getWoidBySrid(srid);
	    			if(!workOrders.isEmpty()) {
	    				workOrderIds=workOrders.stream().map(String::valueOf)
								  .collect(Collectors.joining(AppConstants.CSV_CHAR_COMMA, AppConstants.OPENING_BRACES, AppConstants.CLOSING_BRACES));
						if(StringUtils.isBlank(searchString)) {
							allcomments=mobileDAO.getAuditDataForServiceRequest(workOrderIds, start, length,true);
						}
						else {
							allcomments=mobileDAO.getAuditDataForServiceRequest(workOrderIds, null, null, true);
						}
	    			}
			    	
	    		}
	    		
	    		response.setResponseData(allcomments);
	    		for(AuditDataModel a:allcomments){
		    		auditManagementService.formatMessage(a);
					EmployeeModel eDetails = fetchedUserDetails.get(a.getCreatedBy().toUpperCase());
					if(eDetails==null){
						eDetails=activityMasterDAO.getEmployeeBySignum(a.getCreatedBy());
						if(eDetails!=null){
							fetchedUserDetails.put(eDetails.getSignum().toUpperCase(), eDetails);
							a.setCreatedBy(new StringBuilder(eDetails.getEmployeeName())
									.append(AppConstants.OPENING_BRACES_WITH_SPACE)
									.append(eDetails.getSignum().toLowerCase())
									.append(AppConstants.CLOSING_BRACES)
									.toString());
						}
					}
					else {
						a.setCreatedBy(new StringBuilder(eDetails.getEmployeeName())
								.append(AppConstants.OPENING_BRACES_WITH_SPACE)
								.append(eDetails.getSignum().toLowerCase())
								.append(AppConstants.CLOSING_BRACES)
								.toString());
					}
					
					if(StringUtils.isBlank(searchString)) {
						response.setResponseData(allcomments);
					}
					else {
						searchString=searchString.replaceAll(AppConstants.BACKWARD_DOUBLE_SLASH_SPLUS,"");
						
						if((a.getMessage()!=null && a.getMessage().replaceAll(AppConstants.BACKWARD_DOUBLE_SLASH_SPLUS,"").toUpperCase().contains(searchString))
								||  (a.getContext()!=null && a.getContext().replaceAll(AppConstants.BACKWARD_DOUBLE_SLASH_SPLUS,"").toUpperCase().contains(searchString))
								|| (a.getCreatedBy()!=null  &&a.getCreatedBy().replaceAll(AppConstants.BACKWARD_DOUBLE_SLASH_SPLUS,"").toUpperCase().contains(searchString))){
							if(start==null || count>=start) {
								filteredComments.add(a);
								if(length!=null && filteredComments.size()==length) {
									break;
								}
							}
							count++;
						}
						
						response.setResponseData(filteredComments);
					}
	    		}
		} catch (ApplicationException ae) {
			LOG.error(ae.getMessage());
			response.addFormError(ae.getMessage());
		}
		catch (Exception ex) {
			LOG.error(ex.getMessage());
			response.addFormError(ex.getMessage());
		}
		
		return response;
	}


	public String getEncrypted(String text) {
		return aesUtil.encryptBase64(text);
	}


	public String getDecrypted(String encryptedText) {
		return new String(aesUtil.decryptBase64(encryptedText));
	}


	public Response<List<ProjectScopeModel>> getAllFUDeliverablesByProjectWithoutAnyTerm(int projectID) {
		Response<List<ProjectScopeModel>> response= new Response<>();
		try {
			response.setResponseData(mobileDAO.getAllFUDeliverablesByProjectWithoutAnyTerm(projectID));
		}
		catch(Exception ex) {
			LOG.error(ex.getMessage());
			response.addFormError(ex.getMessage());
		}
		return response;
	}


	public void saveMobileNotification(AuditDataModel auditDataModel) {
		ServiceRequestModel serviceRequest=mobileDAO.getServiceRequestByWoid(auditDataModel.getAuditPageId());
		if(serviceRequest !=null) {
			MobileNotificationModel mobileNotificationModel = new MobileNotificationModel();
			mobileNotificationModel.setAuditComments(auditDataModel.getMessage());
			mobileNotificationModel.setCreatedBy(auditDataModel.getCreatedBy());
			mobileNotificationModel.setModule("SR");
			mobileNotificationModel.setReferenceId(serviceRequest.getSrid());
			mobileNotificationModel.setToSignum(serviceRequest.getCreatedBy());
			mobileNotificationModel.setWoid(auditDataModel.getAuditPageId());
			mobileDAO.saveMobileNotification(mobileNotificationModel);
			
			auditDataModel.setSrid(serviceRequest.getSrid());
			auditDataModel.setSrCreator(serviceRequest.getCreatedBy());
		}
	}
	
	public ResponseEntity<Response<ServiceRequestModel>> getSRDetailsBySRID(int srID) {
		Response <ServiceRequestModel> result = new Response<>();
		try {
			LOG.info("getSRDetailsBySRID:Start");

			validationUtilityService.validateIntForZero(srID, "SRID");

				ServiceRequestModel srDetails = this.mobileDAO.getSRDetailsBySRID(srID);

				if (srDetails==null) {
					result.addFormMessage(NO_DATA_FOUND);
					result.setResponseData(srDetails);
				} else {
					result.setResponseData(srDetails);
					}
				
			LOG.info("getSRDetailsBySRID:End");
		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	public Response<List<MobileNotificationModel>> getMobileNotifications(String signum, int start, int length) {
		Response<List<MobileNotificationModel>> response = new Response<>();
		
		try {
			response.setResponseData(mobileDAO.getMobileNotifications(signum, start, length));
		}
		catch (ApplicationException exe) {
			LOG.error(exe.getMessage());
			response.addFormError(exe.getMessage());
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			response.addFormError(ex.getMessage());
		}
		return response;
	}


	public ResponseEntity<Response<List<WorkOrderDetailsModel>>> getWODetailsBySRID(int srID) {
		Response<List<WorkOrderDetailsModel>> result = new  Response<>();
		try {
			
			LOG.info("getWODetailsBySRID:Start");

			validationUtilityService.validateIntForZero(srID, AppConstants.SRID);
			List<WorkOrderDetailsModel> woDetails = this.mobileDAO.getWODetailsBySRID(srID);
			
			if (woDetails.isEmpty()) {
				result.addFormMessage(NO_DATA_FOUND); 
				result.setResponseData(woDetails);
			} else {
				result.setResponseData(woDetails);
			}
			LOG.info("getWODetailsBySRID:End");
			}
			catch(ApplicationException exe) {
				result.addFormError(exe.getMessage());
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			catch(Exception ex) {
				result.addFormError(ex.getMessage());
				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
