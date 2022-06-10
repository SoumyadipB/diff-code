/*
 * To change this license header, choose License Headers in Project  Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service.audit;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.AccessManagementDAO;
import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.AuditManagementDAO;
import com.ericsson.isf.dao.WOExecutionDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.AuditDataModel;
import com.ericsson.isf.model.AuditGroupModel;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.UserImageURIModel;
import com.ericsson.isf.model.WorkOrderViewDetailsByWOIDModel;
import com.ericsson.isf.service.AccessManagementService;
import com.ericsson.isf.service.MobileService;
import com.ericsson.isf.service.OutlookAndEmailService;
import com.ericsson.isf.service.WOExecutionService;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AuditMessageEnum;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.ericsson.isf.util.FileUtil;
import com.ericsson.isf.util.IsfCustomIdInsert;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 *
 * @author edhhklu
 */
@Service
public class AuditManagementService {
	
    @Autowired
    private AuditManagementDAO auditManagementDAO;
    
    
    @Autowired
    private OutlookAndEmailService emailService;
    
    @Autowired
    private MobileService mobileService;
    
    
   @Autowired
   private WOExecutionService woExecutionService;
   
   @Autowired
   private WOExecutionDAO woExecutionDAO;
   
   @Autowired
   ActivityMasterDAO activityMasterDAO;
   
   @Autowired
   AccessManagementService accessManagementService;
   
   @Autowired
   AccessManagementDAO accessManagementDAO;
   
   @Autowired
   private IsfCustomIdInsert isfCustomIdInsert;
   
   @Autowired
   private ApplicationConfigurations configurations;
   
   private static final String SP="DeliveryExecution/WorkorderAndTask";
   
   private static final Logger LOG = LoggerFactory.getLogger(AuditManagementService.class);
   
	
	public AuditDataModel addComment(AuditDataModel auditDataModel) {

		if (StringUtils.isBlank(auditDataModel.getType())) {

			auditDataModel.setType(AppConstants.AUDIT_TYPE_USER_COMMENT);
		}
		insertAuditData(auditDataModel);
		
		EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(auditDataModel.getCreatedBy());
		if (eDetails != null) {
			auditDataModel.setCreatedBy((new StringBuilder()).append(eDetails.getEmployeeName())
				.append(AppConstants.OPENING_BRACES_WITH_SPACE).append(eDetails.getSignum().toLowerCase())
					.append(AppConstants.CLOSING_BRACES).toString());
		}
		
		if (auditDataModel.getNotification() != null && auditDataModel.getNotification() > 0) {
			Map<String, Object> placeHolder = enrichCommentForNotification(auditDataModel, eDetails);
			placeHolder.put(AppConstants.PAGELINK, SP);
			emailService.sendMail(AppConstants.NOTIFICATION_ADD_COMMENT, placeHolder);
		}
		
		formatMessage(auditDataModel);
		return auditDataModel;
	}
	
	public ResponseEntity<Response<AuditDataModel>> addComments(AuditDataModel auditDataModel) {
        
		Response<AuditDataModel> response=new Response<>();

		try {
		response.setResponseData(addComment(auditDataModel));
		
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch(Exception ex) {
			response.addFormError(ex.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    
    public Map<String,Object> enrichCommentForNotification(AuditDataModel auditDataModel, EmployeeModel currentUserRetails){
    	Map<String,Object> data=new HashMap<>();
    		int woid=auditDataModel.getAuditPageId();
    		String projectId = woExecutionDAO.getProjectIdByWoid(woid);
    		data.put(AppConstants.PROJECT_ID, projectId);
    		data.put(AppConstants.REQUEST, auditDataModel);
    		data.put(AppConstants.WOID, woid);
    		data.put(AppConstants.CONTEXT, auditDataModel.getContext().startsWith(AppConstants.WORK_ORDER)?auditDataModel.getContext():AppConstants.STEP_NAME_WITH_COLON+auditDataModel.getContext());
    		data.put(AppConstants.CURRENT_USER, currentUserRetails.getSignum());
    		if(!AppConstants.ACTOT_TYPE_SP.equalsIgnoreCase(auditDataModel.getActorType())){
    			WorkOrderViewDetailsByWOIDModel woDetails=woExecutionService.getWorkOrderViewDetailsByWOID(woid).get(0);
    			EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(woDetails.getSignumID());
    			data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, eDetails.getEmployeeEmailId());
    		}
		return data;
    }
    
    @Transactional("transactionManager")
    public AuditDataModel addToAudit(AuditDataModel auditDataModel){
		auditDataModel.setType(AppConstants.AUDIT_TYPE_AUDIT);
		return insertAuditData(auditDataModel);
	}

	public AuditDataModel insertAuditData(AuditDataModel auditDataModel) {
		AuditGroupModel auditGroup = auditManagementDAO.getAuditGroupByPageId(
				StringUtils.defaultString(String.valueOf(auditDataModel.getAuditPageId())),
				auditDataModel.getAuditGroupCategory());

		if (auditGroup == null) {
			auditGroup = new AuditGroupModel();
			auditGroup.setAuditPageId(auditDataModel.getAuditPageId());
			auditGroup.setAuditGroupCategory(auditDataModel.getAuditGroupCategory());
			auditManagementDAO.insertAuditGroup(auditGroup);
			long auditGroupIdAndInstanceId = isfCustomIdInsert.generateCustomId(auditGroup.getAuditGroupId());
			auditGroup.setAuditGroupId(auditGroupIdAndInstanceId);
		}
		auditDataModel.setAuditgroupid(auditGroup.getAuditGroupId());
		auditManagementDAO.insertAuditData(auditDataModel);
		
		if(auditDataModel.getAuditGroupCategory().equalsIgnoreCase("WORK_ORDER") 
				&& auditDataModel.getType().equalsIgnoreCase("USER_COMMENT")
				&& !StringUtils.equalsIgnoreCase(auditDataModel.getRefferer(), "Mobile")) {
			mobileService.saveMobileNotification(auditDataModel);
		}
		return auditDataModel;
	}
    
   private boolean validateAuditDataModel(AuditDataModel a, String searchString) {
	   boolean flag=false;
		if((a.getMessage()!=null && a.getMessage().replaceAll("\\s+","").toUpperCase().contains(searchString)) ||  (a.getContext()!=null && a.getContext().replaceAll("\\s+","").toUpperCase().contains(searchString))|| (a.getCreatedBy()!=null  &&a.getCreatedBy().replaceAll("\\s+","").toUpperCase().contains(searchString))){
			flag= true;
		}	
		return flag;
   }  
	public Map<String,Object> getAuditDataByPageId(String pageId, Integer start, Integer length, String searchString){
		Map<String, Object> response = new HashMap<>();
		
		if(searchString==null){
			searchString="";
		}
		searchString=searchString.toUpperCase();
		HashMap<String, Object> data= auditManagementDAO.getAuditDataByPageId(pageId,start, length, searchString);
		
		
		
		if(data.containsKey(AppConstants.ERROR_FLAG)) {
			
			response.put("ErrorData", data);
			response.put(AppConstants.ERROR_FLAG, true);
			return response;
		}
		@SuppressWarnings("unchecked")
		List<AuditDataModel> allcomments = (List<AuditDataModel>) data.get("data");
		List<AuditDataModel> filteredComments=new ArrayList<>();
		int i=0;
		Map<String,EmployeeModel> fetchedUserDetails=new HashMap<>();
		Map<String,String> userImageData=new HashMap<>();
		searchString=searchString.replaceAll("\\s+","");
		
		for(AuditDataModel a:allcomments){
			formatMessage(a);
			a.setCommentid("C" + (i++));
			EmployeeModel eDetails = fetchedUserDetails.get(a.getCreatedBy().toUpperCase());
			if(eDetails==null){
				eDetails=activityMasterDAO.getEmployeeBySignum(a.getCreatedBy());
				if(eDetails!=null){
					fetchedUserDetails.put(eDetails.getSignum().toUpperCase(), eDetails);
				}
			}
			if(eDetails!=null){
				a.setCreatedBy(eDetails.getEmployeeName()+" ("+eDetails.getSignum().toLowerCase()+")");
//				if(!userImageData.containsKey(a.getCreatedBy())){
//					///For Image Using LDAP
//					try {
//						getImageForAuditLDAP(userImageData, a, eDetails);
//					} catch (Exception e) {
//						LOG.info("ERROR in allcomments: {}", e.getMessage());
//					}
//				}
			}
			if(validateAuditDataModel(a, searchString)) {
				filteredComments.add(a);
			}
		}
		
		response.put(AppConstants.ERROR_FLAG, false);
		response.put("Error", null);
		response.put(AppConstants.COMMENT_DATA, filteredComments);
		response.put("userProfileData", userImageData);
		response.put("toEmailDefaults", "tomail@test.com");
		response.put("ccEmailDefaults", "ccemail@test.com");
		
		return response;
	}
	
	private void getImageForAuditLDAP(Map<String, String> userImageData, AuditDataModel a, EmployeeModel eDetails) {
		UserImageURIModel imgData = this.accessManagementDAO.getUserImageURIFromDB(eDetails.getSignum());
		if(imgData!=null) {
		String imageData=imgData.getUserImage();
		userImageData.put(a.getCreatedBy(), (imageData==null)?"":(imageData));
		}
		else {
			userImageData.put(a.getCreatedBy(),"");
		}
	}
	
	
	public void formatMessage(AuditDataModel auditModel) {

		String msg;
		if (AppConstants.AUDIT_TYPE_AUDIT.equalsIgnoreCase(auditModel.getType())) {
			if ((auditModel.getMessage() == null) || (AuditMessageEnum.valueOf(auditModel.getMessage()) == null)) {
				msg = AuditMessageEnum.DEFAULT.getAuditMessage();
			} else {
				msg = AuditMessageEnum.valueOf(auditModel.getMessage()).getAuditMessage();
			}
		} else {
			msg = auditModel.getMessage();
		}
		if (msg != null) {
			msg = msg.replace("{filedName}", StringUtils.defaultString(auditModel.getFieldName()))
					.replace("{oldValue}", StringUtils.defaultString(auditModel.getOldValue()))
					.replace("{newValue}", StringUtils.defaultString(auditModel.getNewValue()));
			StringBuilder context = new StringBuilder();

			if (StringUtils.isNotBlank(auditModel.getContext())) {
				context.append(AppConstants.OPENING_BRACES).append(auditModel.getContext())
						.append(AppConstants.CLOSING_BRACES_WITH_COLON_SPACE);
			}
			msg = (new StringBuilder()).append(context.toString())
					.append(((auditModel.getAdditionalInfo() != null)
							? auditModel.getAdditionalInfo() + AppConstants.PIPE_WITH_SPACE
							: StringUtils.EMPTY))
					.append(msg).toString();
		} else {
			msg = (new StringBuilder()).append(auditModel.getContext()).append(
					((auditModel.getAdditionalInfo() != null) ? auditModel.getAdditionalInfo() : StringUtils.EMPTY))
					.toString();
		}
		auditModel.setMessage(msg);

	}


	public DownloadTemplateModel downloadAuditData(String pageId, Integer start, Integer length, String searchString) throws IOException {
		DownloadTemplateModel downloadTemplateModel=new DownloadTemplateModel();
		Map<String,Object> data=getAuditDataByPageIdforExcelDownload(pageId,start, length, searchString);
		List<Map<String,Object>> filteredComments= (List<Map<String, Object>>) data.get(AppConstants.COMMENT_DATA);
		LOG.info("filtered comment: {}",filteredComments);
		String auditPageId = pageId.substring(pageId.lastIndexOf('_')+1);
		String fName="Comments_WORK_ORDER_"+auditPageId+".xlsx";
		byte[] fData = FileUtil.generateXlsFile(filteredComments);
		downloadTemplateModel.setpFileContent(fData);
   		downloadTemplateModel.setpFileName(fName);
		return downloadTemplateModel;
		
	}


	@SuppressWarnings("unchecked")
	private Map<String, Object> getAuditDataByPageIdforExcelDownload(String pageId, Integer start, Integer length,
			String searchString) {
		searchString=searchString.toUpperCase();
		HashMap<String, Object> data	= auditManagementDAO.getAuditDataByPageId(pageId,start, length, searchString);
		
		List<AuditDataModel> allcomments =(List<AuditDataModel>) data.get("data");
		List<Map<String,Object>> filteredComments=new ArrayList<>();
		int i=0;
		
		Map<String,EmployeeModel> fetchedUserDetails=new HashMap<>();
		Map<String,String> userImageData=new HashMap<>();
		searchString=searchString.replaceAll("\\s+","");
		
		for(AuditDataModel a:allcomments){
			formatMessage(a);
			a.setCommentid("C" + (i++));
			EmployeeModel eDetails = fetchedUserDetails.get(a.getCreatedBy().toUpperCase());
			if(eDetails==null){
				eDetails=activityMasterDAO.getEmployeeBySignum(a.getCreatedBy());
					fetchedUserDetails.put(eDetails.getSignum().toUpperCase(), eDetails);
			}
			if(eDetails!=null){
				a.setCreatedBy(eDetails.getEmployeeName()+" ("+eDetails.getSignum().toLowerCase()+")");
				if(!userImageData.containsKey(a.getCreatedBy())){
					///For Image Using LDAP
					try {
						getImageForAuditLDAP(userImageData, a, eDetails);
					} catch (Exception e) {
						LOG.info("ERROR in getAuditDataByPageIdforExcelDownload: {}", e.getMessage());
					}
				}
			}
			if(validateAuditDataModel(a, searchString)){
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> map = 
						mapper.convertValue(a, new TypeReference<Map<String, Object>>() {});
				map.remove("parent");
				map.remove("modified");
				map.remove("fileURL");
				map.remove("auditgroupid");
				map.remove("fieldName");
				map.remove("oldValue");
				map.remove("newValue");
				map.remove("id");
				Date created = new Date((long) map.get("created"));
				SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				map.put("created", simpleformat.format(created));
				filteredComments.add(map);
			}
		}
		
		Map<String,Object> response=new HashMap<>();
		response.put(AppConstants.COMMENT_DATA, filteredComments);
		
		
		return response;

	}

}
