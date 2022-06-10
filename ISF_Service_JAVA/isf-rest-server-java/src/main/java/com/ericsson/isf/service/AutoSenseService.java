package com.ericsson.isf.service;


import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.dao.AccessManagementDAO;
import com.ericsson.isf.dao.AutoSenseDao;
import com.ericsson.isf.dao.FlowChartDAO;
import com.ericsson.isf.dao.WOExecutionDAO;
import com.ericsson.isf.dao.WorkOrderPlanDao;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.AutoSenseRuleFlowchartModel;
import com.ericsson.isf.model.AutoSenseRuleModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.FlowChartDefModel;
import com.ericsson.isf.model.MigrationAutoSenseRuleModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.RuleMigrationModel;
import com.ericsson.isf.model.SignalrModel;
import com.ericsson.isf.model.StepAutoSenseRuleScanner;
import com.ericsson.isf.model.TaskDetailModel;
import com.ericsson.isf.model.TmpWorkflowStepAutoSenseRuleModel;
import com.ericsson.isf.model.WorkOrderColumnMappingModel;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.ApplicationMessages;
import com.ericsson.isf.util.FileUtil;
import com.ericsson.isf.util.IsfCustomIdInsert;
import com.ericsson.isf.util.JsonUtils;
import com.ericsson.isf.util.RuleMigrationTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;



@Service
public class AutoSenseService {
	private static final String DO_ID = "doID";
	private static final String NODE_NAMES = "nodeNames";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String WORK_FLOW_NAME = "workFlowName";
	private static final String SIGNUM_ID = "signumID";
	private static final String MARKET_AREA = "marketArea";
	private static final String SUB_ACTIVITY_NAME = "subActivityName";
	private static final String START_DATE = "startDate";
	private static final String WO_NAME = "wOName";
	private static final String FLOWCHARTDEF_ID = "flowchartdefID";
	private static final String PROJECT_ID = "projectID";
	private static final String SAME_RULE_NAME = "Rule Name is already Present. Please try with different Rule Name.";
	private static final String MANDATORY_FIELDS_MISSING = "Mandatory fields are missing.Please provide valid value for all the fields.";
	private static final String RULE_DELETED = "Rule Deleted successfully";
	private static final String DELETE_ERROR = "The specified Rule doesn't exist.";
	private static final String MANDATORY_FIELDS_FOR_RULE_STATUS_UPDATE_MISSING = "Mandatory fields are missing.Please provide woID and flowchartdefID";
	private static final String WORK_ORDER_RULE_STATUS_UPDATED = "Your preference for auto-sense selection has been recorded successfully for this workorder!";
	public static final String AUTO_SENSE_RULE_UPDATE_ERROR_MESSAGE = "Auto Sense Rule Status Updation Unsuccessful.";
	private static final String RULE_ACTIVE_INACTIVE = "Rule status updated successfully";
	private static final String RULE_EDIT = "Rule edited successfully";
	private static final String RULE_EDIT_ERROR = "Rule updation unsuccessful";
	private static final String DELETE_ERROR_MESSAGE = "Delete Failed!";
	private static final String TASK_ERROR_MESSAGE = "No tasks in selected combination.";
	private static final String PROJECTID_SUBACTIVITYID_WFOWNER = "Project ID, Subactivity ID, Workflow Owner";
	private static final String WARNING_NO_AUTO_SENSE_WF="Work Flow is not Auto-Sense enabled, WorkOrder is loaded with no Auto-Sense";
	private static final String RULE_MANUAL_VALIDATION = "Rule validated successfully";
	private static final String STOP_RULE = "stopRule";
	private static final String START_RULE = "startRule";
	private static final String SUBACTIVITY_INVALID = "SubActivityID can't be zero.Please provide valid subActivityID.";
	private static final String INVALID_SIGNUM = "Signum is Invalid. Please provide valid Signum.";
	private static final String STEP_ID="StepID";
	private static final String RECORDS_TOTAL = "recordsTotal";
    private static final String RECORDS_FILTERED = "recordsFiltered";
	
	private static final Logger LOG = LoggerFactory.getLogger(AutoSenseService.class);
	@Autowired
	private FlowChartDAO flowChartDao;
	   
	@Autowired
	private AutoSenseDao autoSenseDao;
	
	@Autowired
	private AppService appService;

	@Autowired
	private ValidationUtilityService validationUtilityService;
	
	@Autowired
	private WorkOrderPlanDao workOrderPlanDao;
	
	@Autowired
	private JsonUtils jsonUtil;
	
	@Autowired
	private IsfCustomIdInsert isfCustomIdInsert;
	
	@Autowired
	private AccessManagementDAO accessManagementDAO;
	
	@Autowired
	private WOExecutionDAO wOExecutionDAO;
	
	@Autowired
	private ActivityMasterService activityMasterService;
	


	public Response<List<AutoSenseRuleModel>> getRulesForTaskIDProjectID(int projectId, int taskId,String ruleType) {
		Response<List<AutoSenseRuleModel>> response = new Response<>();
		try {
			LOG.info("Start execution of getRulesForTaskIDProjectID  Api.");
			validateRulesForTaskIDProjectID(projectId, taskId,ruleType);
			List<AutoSenseRuleModel> listOfRules = autoSenseDao.getRulesForTaskIDProjectID(projectId, taskId,StringUtils.upperCase(ruleType));
			if (CollectionUtils.isEmpty(listOfRules)) {
				response.addFormMessage(AppConstants.NO_DETAILS_EXISTS);
			} else {
				response.setResponseData(listOfRules);
			}
			LOG.info("Execution of getRulesForTaskIDProjectID Api Successful.");
		} catch (ApplicationException e) {
			response.addFormError(e.getMessage());
			LOG.info(e.getMessage());
		} catch (Exception e) {
			LOG.info(e.getMessage());
            throw new ApplicationException(500, e.getMessage());
		}

		return response;
	}

	public Response<List<AutoSenseRuleModel>> getRulesForTaskID(int taskId, String ruleType) {
		Response<List<AutoSenseRuleModel>> response = new Response<>();
		try {
			LOG.info("Start execution of getRulesForTaskID  Api.");
			validateRulesForTaskID(taskId,ruleType);
			List<AutoSenseRuleModel> listOfRules = autoSenseDao.getRulesForTaskID(taskId,StringUtils.upperCase(ruleType));
			if (CollectionUtils.isEmpty(listOfRules)) {
				response.addFormMessage(AppConstants.NO_DETAILS_EXISTS);
			} else {
				response.setResponseData(listOfRules);
			}
			LOG.info("Execution of getRulesForTaskID Api Successful.");
		} catch (ApplicationException e) {
			response.addFormError(e.getMessage());
			LOG.info(e.getMessage());
		} catch (Exception e) {
			LOG.info(e.getMessage());
			throw new ApplicationException(500, e.getMessage());
		}

		return response;
	}

	public Response<Integer> saveAutoSenseRule(AutoSenseRuleModel ruleModel, String signum) {
		Response<Integer> response = new Response<Integer>();
		Boolean checkEmpTbl = this.accessManagementDAO.isSignumExistsEmp(signum);
		if(!checkEmpTbl) {
			response.addFormError(INVALID_SIGNUM);
			return response;
		}
		
		if (ruleModel == null || StringUtils.isEmpty(ruleModel.getRuleName())
				|| StringUtils.isEmpty(ruleModel.getRuleDescription())
				|| StringUtils.isEmpty(ruleModel.getRuleJson()) || ruleModel.getDomainID()== 0 || ruleModel.getSubactivityID()==0 || ruleModel.getTechnologyID()==0 || ruleModel.getTaskID()==0 || ruleModel.getServiceAreaID() == 0 ) {

			response.addFormError(MANDATORY_FIELDS_MISSING);
		} else {
			List<AutoSenseRuleModel> ruleNames = this.autoSenseDao.getAutosenseRuleName(
					ruleModel.getRuleName());
			if (CollectionUtils.isNotEmpty(ruleNames)) {
				response.addFormError(SAME_RULE_NAME);
			} else {
				try {
					if (StringUtils.isNotEmpty(ruleModel.getRuleName())
							&& StringUtils.isNotEmpty(ruleModel.getRuleDescription())) {
						String parsedRuleJson=jsonUtil.getParsedRuleJsonFromRuleJson(ruleModel.getRuleJson(),false);
						String parsedRuleJsonWithDefaultDynamicValue=jsonUtil.getParsedRuleJsonFromRuleJson(ruleModel.getRuleJson(),true);
						String manualValidationJson=jsonUtil.getManualValidateJsonFromParsedRuleJson(parsedRuleJsonWithDefaultDynamicValue,signum);
						
						if(StringUtils.isBlank(parsedRuleJson) || StringUtils.isBlank(manualValidationJson)) {
							response.addFormError(AppConstants.GOT_AN_ERROR_WHILE_CONVERTING_RULE);
							return response;
					  
					} 
						ruleModel.setParsedRuleJson(parsedRuleJson);
					 	ruleModel.setJsonManualValidation(manualValidationJson);
						
						ruleModel.setCreatedBy(signum);
						ruleModel.setModifiedBy(signum);
						autoSenseDao.saveAutoSenseRule(ruleModel);
						int ruleID = isfCustomIdInsert.generateCustomId(ruleModel.getRuleId());
						response.setResponseData(ruleID);
						response.addFormMessage(AppConstants.AUTO_SENSE_RULE_ADDED_SUCCESSFULLY);
						LOG.info("Execution of saveAutoSenseRule API Successful.");
					}
				} catch (Exception e) {
					response.addFormError(ApplicationMessages.AUTO_SENSE_RULE_SAVE_ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		}
		return response;
	}

	public List<AutoSenseRuleFlowchartModel> getAllRulesForFlowchart(Integer flowchartDefID, Integer woID) {
		return autoSenseDao.getAllRulesForFlowchart(flowchartDefID, woID);
	}

	public  Map<String, Object> getAllRulesInMasterData(DataTableRequest dataTableRequset) {

		Map<String, Object> map = new HashMap<>();
		List<AutoSenseRuleModel> listOfRulesInMasterData = autoSenseDao.getAllRulesInMasterData(dataTableRequset);
		
		map.put("data", listOfRulesInMasterData);
		
		if (!listOfRulesInMasterData.isEmpty()) {
			map.put(RECORDS_TOTAL,listOfRulesInMasterData.get(0).getRecordsTotal() );
			map.put(RECORDS_FILTERED,listOfRulesInMasterData.get(0).getRecordsTotal());
		} else {
			map.put(RECORDS_TOTAL, 0);
			map.put(RECORDS_FILTERED, 0);
		}
		
		return map;

	}

	public List<Map<String, Object>> getAllQueuedTask(String signum) {

		return autoSenseDao.getAllQueuedTask(signum);
	}

	public Response<Void> deleteAutoSenseRule(AutoSenseRuleModel ruleModel, String signum) {
		Response<Void> response = new Response<Void>();
		try {
			Boolean checkRule = this.autoSenseDao.checkAutoSenseRuleID(ruleModel.getRuleId());
			int deleteValue = autoSenseDao.getMaxDeletedValue(ruleModel) + 1;

			if (checkRule) {
				autoSenseDao.deleteAutoSenseRule(ruleModel.getRuleId(), signum, deleteValue);

				response.addFormMessage(RULE_DELETED);

			} else {
				response.addFormMessage(DELETE_ERROR);
			}

		} catch (Exception e) {
			response.addFormError(DELETE_ERROR_MESSAGE);

		}
		return response;
	}


	public Response<Void> updateWorkOrderAutoSenseStatus(Integer woID, Integer flowchartDefID, boolean autoSenseFlag,
			String signum) {
		Response<Void> response = new Response<>();
		validateForBlank(woID,flowchartDefID,response);
		FlowChartDefModel flowChartDefModel=flowChartDao.getFlowchartDetails(flowchartDefID) ;
		if(autoSenseFlag && !flowChartDefModel.isAutoSenseEnable()) {
				response.addFormWarning(WARNING_NO_AUTO_SENSE_WF);
				autoSenseFlag=false;
		}
		
		try {
			autoSenseDao.updateWorkOrderAutoSenseStatus(woID, flowchartDefID, autoSenseFlag, signum,flowChartDefModel.getVersionNumber());
			response.addFormMessage(WORK_ORDER_RULE_STATUS_UPDATED);
			LOG.info("Execution of updateWorkOrderAutoSenseStatus Api Successful.");
		} catch (Exception e) {
			response.addFormError(AUTO_SENSE_RULE_UPDATE_ERROR_MESSAGE);
			e.printStackTrace();
		}
			
		return response;
	}

	private Response<Void> validateForBlank(Integer woID, Integer flowchartDefID, Response<Void> response) {
		if (woID == null || woID == 0 || flowchartDefID == null || flowchartDefID == 0) {
			response.addFormError(MANDATORY_FIELDS_FOR_RULE_STATUS_UPDATE_MISSING);
		}		
		return response;
	}

	public Response<Void> saveStepRuleInAutoSenseTmpTable(
			TmpWorkflowStepAutoSenseRuleModel tmpWorkFlowStepAutoSenseRule) {
		Response<Void> response = new Response<>();
		try {
			LOG.info("Start execution of saveStepRuleInAutoSenseTmpTable  Api.");
			validationUtilityService.validateSaveStepRuleInAutoSenseTmpTable(tmpWorkFlowStepAutoSenseRule);
			tmpWorkFlowStepAutoSenseRule
					.setTaskActionName(StringUtils.upperCase(tmpWorkFlowStepAutoSenseRule.getTaskActionName()));
			autoSenseDao.saveStepRuleInAutoSenseTmpTable(tmpWorkFlowStepAutoSenseRule);
			response.addFormMessage(AppConstants.AUTO_SENSE_RULE_ADDED_SUCCESSFULLY);
			LOG.info("Execution of saveStepRuleInAutoSenseTmpTable Api Successful.");
		} catch (ApplicationException e) {
			LOG.info(e.getMessage());
			response.addFormError(e.getMessage());
		} catch (Exception e1) {
			LOG.info(e1.getMessage());
			throw new ApplicationException(500, e1.getMessage());
		}

		return response;
	}

	public Response<Void> deleteStepRuleFromAutoSenseTmpTable(List<TmpWorkflowStepAutoSenseRuleModel> tmpWorkFlowStepAutoSenseRule) {
		Response<Void> response = new Response<>();
		try {
			LOG.info("Start execution of deleteStepRuleFromAutoSenseTmpTable  Api.");
			validationUtilityService.validateDeleteStepRuleInAutoSenseTmpTable(tmpWorkFlowStepAutoSenseRule);
			for(int i=0;i<tmpWorkFlowStepAutoSenseRule.size();i++) {
				autoSenseDao.deleteStepRuleFromAutoSenseTmpTable(tmpWorkFlowStepAutoSenseRule.get(i));	
			}
			response.addFormMessage(AppConstants.AUTO_SENSE_RULE_DELETED_SUCCESSFULLY);
			LOG.info("Execution of deleteStepRuleFromAutoSenseTmpTable Api Successful.");
		} catch (ApplicationException e) {
			response.addFormError(e.getMessage());
			LOG.info( e.getMessage());
		} catch (Exception e) {
			LOG.info(e.getMessage());
			throw new ApplicationException(500, e.getMessage());
		}

		return response;
	}

	public Response<List<Map<String, Object>>> getRuleDescriptionForStep(String stepID, String flowChartDefID) {
		Response<List<Map<String, Object>>> response = new Response<>();
		try {
			validateForBlankStepID(stepID);
			List<Map<String, Object>> listOfRuleDescriptions = autoSenseDao.getRuleDescriptionForStep(stepID,flowChartDefID);
			if (CollectionUtils.isEmpty(listOfRuleDescriptions)) {
				response.addFormMessage(AppConstants.NO_DETAILS_EXISTS);
			} else {
				response.setResponseData(listOfRuleDescriptions);
				LOG.info("Execution of getRuleDescriptionForStep Api Successful.");
			}
		} catch (Exception e) {
			response.addFormError(e.getMessage());
		}

		return response;
	}

	private void validateForBlankStepID(String stepID) {
		validationUtilityService.validateStringForBlank(stepID,STEP_ID);
	}

	public Response<Void> activeInactiveAutoSenseRule(AutoSenseRuleModel autoSenseRuleModel, String signum) {
		Response<Void> response = new Response<Void>();
		try {
			autoSenseDao.activeInactiveAutoSenseRule(autoSenseRuleModel, signum);
			response.addFormMessage(RULE_ACTIVE_INACTIVE);
			LOG.info("Execution of activeInactiveAutoSenseRule Api Successful.");
		} catch (Exception e) {
			response.addFormError(e.getMessage());
			LOG.info( e.getMessage());
		}
		return response;
	}

	public Map<String, Object> getWOColumnMappingJson(Integer woID, List<String> reqColumn) {
		
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			validationForWOColumnMapping(woID);
		
			List<String> temp1 = new ArrayList<String>();
			temp1.add(PROJECT_ID);
			temp1.add(FLOWCHARTDEF_ID);
			temp1.add(WO_NAME);
			temp1.add(START_DATE);
			temp1.add(SUB_ACTIVITY_NAME);
			temp1.add(SIGNUM_ID);
			temp1.add(WORK_FLOW_NAME);
			temp1.add(CUSTOMER_NAME);
			temp1.add(DO_ID);
			
			List<String> temp2 = new ArrayList<>();
			temp2.add(NODE_NAMES);
			temp2.add(MARKET_AREA);
			
			WorkOrderColumnMappingModel detail1 = new WorkOrderColumnMappingModel();
			List<Map<String,Object>> detail2 = new ArrayList<>();
	
			if(!Collections.disjoint(temp1, reqColumn)) {
				detail1 = autoSenseDao.getWOColumnMappingJsonFromWOD1(woID);
			}
	
			if(!Collections.disjoint(temp2, reqColumn)) {
				detail2 = autoSenseDao.getWOColumnMappingJsonFromWOD2(woID);
			}
			
			for(int i=0; i<reqColumn.size(); i++) {
	
				switch (String.valueOf(reqColumn.get(i))) {
					case PROJECT_ID:
						response.put(PROJECT_ID, detail1.getProjectID());
						break;
		
					case FLOWCHARTDEF_ID:
						response.put(FLOWCHARTDEF_ID, detail1.getFlowchartdefID());
						break;
		
					case WO_NAME:
						response.put(WO_NAME, detail1.getwOName());
						break;
		
					case START_DATE:
						response.put(START_DATE, detail1.getStartDate());
						break;
		
					case SUB_ACTIVITY_NAME:
						response.put(SUB_ACTIVITY_NAME, detail1.getSubActivityName());
						break;
		
					case MARKET_AREA:
						String marketArea = makeCommaSeparated(detail2,"Market");
						response.put(MARKET_AREA, marketArea);
						break;
		
					case SIGNUM_ID:
						response.put(SIGNUM_ID, detail1.getSignumID());
						break;
		
					case WORK_FLOW_NAME:
						response.put(WORK_FLOW_NAME, detail1.getWorkFlowName());
						break;
		
					case CUSTOMER_NAME: 
						response.put(CUSTOMER_NAME, detail1.getCustomerName());
						break;
	
					case NODE_NAMES:
						String nodeNames = makeCommaSeparated(detail2,"NodeNames");
						response.put(NODE_NAMES,nodeNames);
						break;
		
					case DO_ID:
						response.put(DO_ID, detail1.getDoID());
						break;
					default:LOG.info("default case");	
				}
			}
		} catch (Exception e) {
			LOG.info("Exception throws {}",e.getMessage());
		}

		return response;
	}

	private void validationForWOColumnMapping(Integer woID) {
		validationUtilityService.validateWOIDIsActive(woID);
	}

	private String makeCommaSeparated(List<Map<String, Object>> list, String key) {
		String items = StringUtils.EMPTY;
		
		for(Map<String,Object> node : list) { 
			items += node.get(key).toString() + AppConstants.CSV_CHAR_DOUBLE_SEMICOLON; 
		}
		if(StringUtils.isNotEmpty(items)) {
			items = items.substring(0,items.length() - 1); 
		}
		return items;
	}

	public Response<List<AutoSenseRuleModel>> getRuleJsonByID(int ruleID) {
		Response<List<AutoSenseRuleModel>> response = new Response<>();
	  List<AutoSenseRuleModel> ruleJson = this.autoSenseDao.getRuleJsonByID(ruleID);
		if (CollectionUtils.isEmpty(ruleJson)) {
			response.addFormError(AppConstants.NO_DETAILS_EXISTS);
			response.setValidationFailed(true);
		} else {
			response.setResponseData(ruleJson);
			response.setValidationFailed(false);
		}
		return response;

	}

	public Response<Void> editAutoSenseRule(AutoSenseRuleModel autoSenseRuleModel, String signum) {
		Response<Void> response = new Response<Void>();
		List<AutoSenseRuleModel> ruleNames = this.autoSenseDao.getDuplicateAutosenseRuleName(
				autoSenseRuleModel.getRuleName(),autoSenseRuleModel.getRuleId());
		if (CollectionUtils.isNotEmpty(ruleNames)) {
			response.addFormError(SAME_RULE_NAME);
		} else {
		try {
			String parsedRuleJson=jsonUtil.getParsedRuleJsonFromRuleJson(autoSenseRuleModel.getRuleJson(),false);
			String parsedRuleJsonWithDefaultDynamicValue=jsonUtil.getParsedRuleJsonFromRuleJson(autoSenseRuleModel.getRuleJson(),true);
			String manualValidationJson=jsonUtil.getManualValidateJsonFromParsedRuleJson(parsedRuleJsonWithDefaultDynamicValue,signum);
		
			if(StringUtils.isBlank(parsedRuleJson) || StringUtils.isBlank(manualValidationJson)) {
				response.addFormError(AppConstants.GOT_AN_ERROR_WHILE_CONVERTING_RULE);
				return response;
			}
			autoSenseRuleModel.setParsedRuleJson(parsedRuleJson); 
			autoSenseRuleModel.setJsonManualValidation(manualValidationJson);
			autoSenseDao.editAutoSenseRule(autoSenseRuleModel, signum);
			response.addFormMessage(RULE_EDIT);
			LOG.info("Execution of editAutoSenseRule API Successful.");
			
		} catch (Exception e) {
			response.addFormError(ApplicationMessages.AUTO_SENSE_RULE_EDIT_ERROR_MESSAGE);
			LOG.info(e.getMessage());
		}
		}
		return response;
	}

	public Map<String,Object> getRulesByStepIDNew(String stepID, int flowchartDefID) {
		return autoSenseDao.getRulesByStepIDNew(stepID, flowchartDefID);
	}
	public Map<String, Object> getRulesByStepID(String stepID, int flowchartDefID) {
		return autoSenseDao.getRulesByStepID(stepID, flowchartDefID);
	}

	@Transactional("transactionManager")
	public Response<Void> editStepRuleInAutoSenseTmpTable(
			List<TmpWorkflowStepAutoSenseRuleModel> tmpWorkFlowStepAutoSenseRule, String signum) {
		Response<Void> response = new Response<>();
		try {
			LOG.info("Start execution of editStepRuleInAutoSenseTmpTable  Api.");
			for (int i = 0; i < tmpWorkFlowStepAutoSenseRule.size(); i++) {
				if (tmpWorkFlowStepAutoSenseRule.get(i).getExisting()) {
					deleteEditedStepRuleFromAutoSenseTmpTable(tmpWorkFlowStepAutoSenseRule.get(i));
				} else {
					tmpWorkFlowStepAutoSenseRule.get(i).setCreatedBy(signum);
					saveStepRuleInAutoSenseTmpTable(tmpWorkFlowStepAutoSenseRule.get(i));
				}
			}
			response.addFormMessage(RULE_EDIT);
			LOG.info("Execution of editStepRuleInAutoSenseTmpTable Api Successful.");
		} catch (ApplicationException e) {
			response.addFormError(e.getMessage());
			LOG.info(e.getMessage());
		} catch (Exception e) {
			LOG.info( e.getMessage());
			throw new ApplicationException(500, e.getMessage());
		}
		return response;
	}

	private void deleteEditedStepRuleFromAutoSenseTmpTable(
			TmpWorkflowStepAutoSenseRuleModel tmpWorkflowStepAutoSenseRuleModel) {
            validationUtilityService.validateDeleteStepRuleInAutoSenseTmpTable(tmpWorkflowStepAutoSenseRuleModel);
			autoSenseDao.deleteEditedStepRuleFromAutoSenseTmpTable(tmpWorkflowStepAutoSenseRuleModel);
	}

	public Response<List<AutoSenseRuleModel>> getRulesForStepFromTemp(TmpWorkflowStepAutoSenseRuleModel tmpWorkFlowStepAutoSenseRule) {

		Response<List<AutoSenseRuleModel>> response = new Response<>();
		try {
			LOG.info("Start execution of getRulesForStepFromTemp  Api.");
			validationUtilityService.validateAutoSenseRulesForStepFromTemp(tmpWorkFlowStepAutoSenseRule);
			List<AutoSenseRuleModel> listOfRules = autoSenseDao.getRulesForStepFromTemp(tmpWorkFlowStepAutoSenseRule);
			if (CollectionUtils.isEmpty(listOfRules)) {
				response.addFormMessage(AppConstants.NO_DETAILS_EXISTS);
			} else {
				response.setResponseData(listOfRules);
			}
			LOG.info("Execution of getRulesForStepFromTemp Api Successful.");
		} catch (ApplicationException e) {
			LOG.info( e.getMessage());
			response.addFormError(e.getMessage());
		} catch (Exception e) {
			LOG.info(e.getMessage());
			throw new ApplicationException(500, e.getMessage());
		}

		return response;

	}
	public Response<List<TaskDetailModel>> getTaskDetailsBySubactivityID(int subActivityID) {
		Response<List<TaskDetailModel>> response =  new Response<>();
		if(subActivityID==0) {
			response.addFormError(SUBACTIVITY_INVALID);
			return response;
		}
		List<TaskDetailModel> TaskDetails = this.autoSenseDao.getTaskDetailsBySubactivityID(subActivityID);
		if(CollectionUtils.isEmpty(TaskDetails)) {
			response.addFormError(TASK_ERROR_MESSAGE);
			response.setValidationFailed(true);	
		}
		else {
			response.setResponseData(TaskDetails);
			response.setValidationFailed(false);
		}
		return response;
	}
	
	private void validateRulesForTaskIDProjectID(int projectId, int taskId, String ruleType) {
		validationUtilityService.validateIntegerForNullOrZero(projectId, AppConstants.PROJECT_ID_2);
		validationUtilityService.validateIntegerForNullOrZero(taskId, AppConstants.TASK_ID);
		validationUtilityService.validateStringForBlank(ruleType, AppConstants.RULE_TYPE);
	}

	private void validateRulesForTaskID(int taskId, String ruleType) {
		validationUtilityService.validateIntegerForNullOrZero(taskId, AppConstants.TASK_ID);
		validationUtilityService.validateStringForBlank(ruleType, AppConstants.RULE_TYPE);
	}

	public Response<Void> saveWfDataAutoSenseTempTable(int[] subActivityFlowchartDefId, String signum) {
		Response<Void> response = new Response<>();
			try {
				LOG.info("Start execution of saveWfDataAutoSenseTempTable  Api.");
				validateSaveWfDataAutoSenseTempTable(subActivityFlowchartDefId);
				autoSenseDao.saveWfDataAutoSenseTempTable(subActivityFlowchartDefId,signum);
				response.addFormMessage(AppConstants.AUTO_SENSE_RULE_ADDED_SUCCESSFULLY);
				LOG.info("Execution of saveWfDataAutoSenseTempTable Api Successful.");
			} catch (ApplicationException e) {
				LOG.info(e.getMessage());
				response.addFormError(e.getMessage());
			} catch (Exception e1) {
				LOG.info(e1.getMessage());
				throw new ApplicationException(500, e1.getMessage());
			}

			return response;
		}

	private void validateSaveWfDataAutoSenseTempTable(int[] subActivityFlowchartDefId) {
		for(int i=0;i<subActivityFlowchartDefId.length;i++) {
			validationUtilityService.validateIntegerForNullOrZero(subActivityFlowchartDefId[i], AppConstants.SUBACTIVITY_FLOWCHART_DEFID);	
		}
		
	}

	public Response<Void> deleteDataAutoSenseTempTable(String projectIDSubactivityIDLoggedInSignum) {
		Response<Void> response = new Response<>();
		try {
			LOG.info("Start execution of deleteDataAutoSenseTempTable  Api.");
			validateDeleteDataFromAutoSenseTempTable(projectIDSubactivityIDLoggedInSignum);
			if (autoSenseDao.deleteDataAutoSenseTempTable(projectIDSubactivityIDLoggedInSignum) > 0) {
				response.addFormMessage(AppConstants.AUTO_SENSE_RULE_DELETED_SUCCESSFULLY);
			} else {
				response.addFormMessage(AppConstants.NO_DETAILS_EXISTS);
			}
			LOG.info("Execution of deleteDataAutoSenseTempTable Api Successful.");
		} catch (ApplicationException e) {
			LOG.info(e.getMessage());
			response.addFormError(e.getMessage());
		} catch (Exception e) {
			LOG.info(e.getMessage());
			throw new ApplicationException(500, e.getMessage());
		}

		return response;
	}

	public Response<Void> saveScannerSensedRule(StepAutoSenseRuleScanner stepAutoSenseRuleScanner) {
		Response<Void> apiResponse= new Response<>();
		try {
			validateSaveScannerSensedRule(stepAutoSenseRuleScanner);
			autoSenseDao.saveScannerSensedRule(stepAutoSenseRuleScanner);
			SignalrModel signalrModel=appService.returnSignalrConfiguration(stepAutoSenseRuleScanner.getSignumId(),
					AppConstants.UPDATE_FLOATING_WINDOW);
			appService.callSignalrApplicationToCallSignalRHub(signalrModel);
			LOG.info("saveScannerSensedRule Success");
		} catch (Exception e) {

			apiResponse.addFormError(e.getMessage());
			LOG.error(e.getMessage());
		}
		return apiResponse;
	}
	
	private void validateSaveScannerSensedRule(StepAutoSenseRuleScanner stepAutoSenseRuleScanner) {
		validationUtilityService.validateIntForZero(stepAutoSenseRuleScanner.getwOID(),"Work Order ID");
		validationUtilityService.validateStringForBlank(stepAutoSenseRuleScanner.getSignumId(), SIGNUM_ID);
		validationUtilityService.validateStringForBlank(stepAutoSenseRuleScanner.getStepID(), "flowChartStepId");
		validationUtilityService.validateIntForZero(stepAutoSenseRuleScanner.getSubActivityFlowChartDefID(), FLOWCHARTDEF_ID);
		
		
		Integer fCStepDetailsID= autoSenseDao.getStepDetailsID(stepAutoSenseRuleScanner.getStepID(),stepAutoSenseRuleScanner.getSubActivityFlowChartDefID());
		if(fCStepDetailsID ==null) {
			throw new ApplicationException(500, 
					String.format("%s does not exists for woID %d",stepAutoSenseRuleScanner.getStepID(),stepAutoSenseRuleScanner.getwOID()));
		}
		stepAutoSenseRuleScanner.setfCStepDetailsID(fCStepDetailsID);
		
	}

	private void validateDeleteDataFromAutoSenseTempTable(String projectIDSubactivityIDWfOwner) {
		validationUtilityService.validateStringForBlank(projectIDSubactivityIDWfOwner,
				PROJECTID_SUBACTIVITYID_WFOWNER);
		
	}


	public Response<List<Map<String, Object>>> getAllNextStepData(String stepID, Integer defID) {
		
		Response<List<Map<String, Object>>> response = new Response<List<Map<String,Object>>>();
		List<Map<String, Object>> allSteps = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> stepList = new ArrayList<Map<String,Object>>();
		boolean isValid = false;
		
		try {
			allSteps = autoSenseDao.getAllNextStepData(stepID,defID);
			
			for(Map<String,Object> step : allSteps) {
				if("ericsson.Manual".equalsIgnoreCase((String) step.get(AppConstants.NEXT_STEP_TYPE)) 
						&& step.get("RuleStatus") != null) {
					
					Map<String, Object> nextStepData = new HashMap<>();

					Map<String,Object> rules = this.getRulesByStepID((String) step.get(AppConstants.NEXT_STEP_ID),defID);
					
					Map<String,Object> temp = new HashMap<>();
					temp.put(START_RULE, false);
					temp.put(STOP_RULE, false);
					
					if(rules!=null) {
						if(rules.containsKey(AppConstants.START)) {
							temp.put(START_RULE, true);
						}
						if(rules.containsKey(AppConstants.STOP)) {
							temp.put(STOP_RULE, true);
						}
					}
					nextStepData.put(AppConstants.NEXT_STEP_ID, step.get(AppConstants.NEXT_STEP_ID));
					nextStepData.put("NextStepName", step.get("NextStepName"));
					nextStepData.put("nextStepRpaId", String.valueOf(step.get("nextStepRpaId")));
					nextStepData.put(AppConstants.NEXT_STEP_TYPE, step.get(AppConstants.NEXT_STEP_TYPE));
					nextStepData.put("bookingID", String.valueOf(step.get("bookingID")));
					nextStepData.put("bookingStatus", step.get("bookingStatus"));
					nextStepData.put("NextTaskID", String.valueOf(step.get("NextTaskID")));
					nextStepData.put("NextExecutionType", step.get("NextExecutionType"));
					nextStepData.put("isRunOnServer", String.valueOf(step.get("isRunOnServer")));
					nextStepData.put("isStepAutoSenseEnabled", step.get("RuleStatus"));
					
					nextStepData.put(START_RULE, temp.get(START_RULE));
					nextStepData.put(STOP_RULE, temp.get(STOP_RULE));
					
					stepList.add(nextStepData);
					
					isValid = true;
				} else {
					isValid = false;
					break;
				}
			}

		} catch (Exception e) {
			response.addFormMessage("Some Exception occurs");
			response.setValidationFailed(true);			
		}
		
		if(isValid) {
			response.setResponseData(stepList);
			response.setValidationFailed(false);
		} else {
			response.setResponseData(new ArrayList<Map<String,Object>>());
			response.addFormMessage("All Steps are not Manual");;
			response.setValidationFailed(true);
		}
		return response;
	}

	@Transactional("transactionManager")
	public Response<RuleMigrationModel> migrateRuleJSON(MultipartFile file, String signum) throws JsonSyntaxException, IOException, ParseException  {
		Response<RuleMigrationModel> response=new Response<>();
		try {
			RuleMigrationModel ruleData=new RuleMigrationModel();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(file.getBytes());
			String originalFileName=file.getOriginalFilename();
			ruleData.setFileName(originalFileName);
			JsonParser parse = new JsonParser();
			JsonObject object = (JsonObject) parse.parse(obj.toString());
			ruleData.setOldJson(obj.toString());
			ruleData.setSignum(signum);
			validateAndCreateRuleData(originalFileName,ruleData,response);
			JsonObject graph=object.getAsJsonObject("graph");
			JsonObject nodes=graph.getAsJsonObject("nodes");
			JsonObject links=graph.getAsJsonObject("links");
			List<String> finalJson=new LinkedList<>();
			createLinkJSON(links, finalJson);
			createAppScanJSON(nodes,finalJson,ruleData.getRuleType());
			StringBuilder jsonValue=new StringBuilder("{\n" +
                    "    \"cells\":");
			jsonValue =jsonValue.append(finalJson.toString());
			jsonValue = jsonValue.append("}");
			String jsonResponse = new JSONTokener(jsonValue.toString()).nextValue().toString();
			LOG.info("New JSON {}", jsonResponse);
			ruleData.setNewJson(jsonResponse);
			String ruleName = "Rule_"+ RandomStringUtils.randomAlphanumeric(8);
			ruleData.setRuleName(ruleName);
			this.autoSenseDao.insertMigrationData(ruleData);
			int ruleID = isfCustomIdInsert.generateCustomId(ruleData.getRuleMigrationId());
			ruleData.setRuleMigrationId(ruleID);
			ruleName="Rule_"+ruleID;
			ruleData.setRuleName(ruleName);
			this.autoSenseDao.updateRuleName(ruleData);
			response.setResponseData(ruleData);
		}
		catch(Exception ex){
			response.addFormError(ex.getMessage());
		}
		return response;
	}

	private void validateAndCreateRuleData(String originalFileName, RuleMigrationModel ruleData, Response<RuleMigrationModel> response) {
		String[] fileNameArray = originalFileName.split("_");
		Integer taskId=Integer.parseInt(fileNameArray[1]);
		String ownerSignum=fileNameArray[3];
		if(StringUtils.containsIgnoreCase(ownerSignum, ".json")) {
			 String [] ownerSignumArray = ownerSignum.split("\\.");
			 ownerSignum = ownerSignumArray[0];
		}
		
		String ruleType=fileNameArray[2];
		boolean isTaskValid=this.autoSenseDao.isTaskValid(taskId);
		if(isTaskValid) {
			ruleData.setTaskId(taskId);
			HashMap<String,Object> taskData=this.autoSenseDao.getTaskData(taskId);
			if(taskData!=null) {
				ruleData.setDomainId(Integer.parseInt(taskData.get("DomainID").toString()));
				ruleData.setTechnologyId(Integer.parseInt(taskData.get("TechnologyID").toString()));
				ruleData.setSubactivityId(Integer.parseInt(taskData.get("SubActivityID").toString()));
				ruleData.setServiceAreaId(Integer.parseInt(taskData.get("ServiceAreaID").toString()));
			}
		}else {
			response.addFormMessage("Provided Task ID is not valid,Please select values from dropdown!!");
		}
		if (StringUtils.equalsIgnoreCase(ruleType, "start")) {
			ruleData.setRuleType("startTask");
		}
		else if(StringUtils.equalsIgnoreCase(ruleType, "stop")) {
			ruleData.setRuleType("stopTask");
		}
		Boolean checkEmpTbl = this.accessManagementDAO.isSignumExistsEmp(ownerSignum);
		if (checkEmpTbl) {
			ruleData.setOwnerSignum(ownerSignum);
		}

	}

	private void createAppScanJSON(JsonObject nodes, List<String> appscanJson, String ruleType) {
		for ( Map.Entry<String, JsonElement> map:nodes.entrySet()) {
			JsonElement nodesElement = map.getValue();
			JsonObject nodesObject=(JsonObject) nodesElement;
			String type=nodesObject.get("type").getAsString();

			if(StringUtils.equalsIgnoreCase(type, "app_scanner")) {
				String json=RuleMigrationTemplate.APP_SCAN_TEMPLATE;
				json=json.replace("%ID%",String.valueOf(nodesObject.get("id")));
				json=json.replace(AppConstants.PTRN_POSITION,String.valueOf(nodesObject.get(AppConstants.POSITION)));
				JsonObject propertiesObject=(JsonObject)nodesObject.get(AppConstants.PROPERTIES);
				JsonObject content=(JsonObject)propertiesObject.get(AppConstants.CONTENT);
				JsonArray validObject = content.getAsJsonArray("valid_apps");

				json=json.replace("%TRIGGER%",String.valueOf(content.get("ontriggerclose")));
				if(validObject.size()==0) {
					//changes for populate custom list for unknown apps list 
					json=json.replace(AppConstants.PTRN_APP,"-1");
					json=json.replace(AppConstants.PTRN_CUSTOMAPPS,StringUtils.EMPTY);
				} 
				else {
					List<String> appList =new LinkedList<String>();
					List<String> customList =new LinkedList<String>();
					for(int i =0; i<validObject.size(); i++) {
						if(getFieldValueDictionary(validObject.get(i).getAsString()).equalsIgnoreCase("-1")) {
							customList.add(validObject.get(i).getAsString());
							}
						else {
						appList.add(getFieldValueDictionary(validObject.get(i).getAsString()));
						}
						
					}
					if(appList.size()>0) {
					json=json.replace(AppConstants.PTRN_APP,appList.toString());
					}
					else {
						json=json.replace(AppConstants.PTRN_APP,"-1");
					}
					if(customList.size()>0) {
					json=json.replace(AppConstants.PTRN_CUSTOMAPPS,customList.toString()
							.substring(1, customList.toString().length()-1));
					}
					else {
						json=json.replace(AppConstants.PTRN_CUSTOMAPPS,StringUtils.EMPTY);
					}
					
				}
				appscanJson.add(json);
			}
			if(StringUtils.equalsIgnoreCase(type, "app_scanner_condition")) {
				String json=RuleMigrationTemplate.APP_MATCH_TEMPLATE;
				json=json.replace("%ID%",String.valueOf(nodesObject.get("id")));
				json=json.replace(AppConstants.PTRN_POSITION,String.valueOf(nodesObject.get(AppConstants.POSITION)));

				JsonObject propertiesObject=(JsonObject)nodesObject.get(AppConstants.PROPERTIES);
				JsonArray content = propertiesObject.getAsJsonArray(AppConstants.CONTENT);
				JsonObject values= (JsonObject) content.get(0);
				
				String sourceID	=	getSourceID(appscanJson,nodesObject.get("id").getAsString());
				
				List<String> appName= getAppName(sourceID,nodes,appscanJson);

				json=json.replace(AppConstants.PTRN_ATTRIBUTE,getMatchattrDictionary(values.get(AppConstants.ATTRIBUTE).getAsString()));
				json=json.replace(AppConstants.PTRN_OPERATOR,getOperatorDictionary(values.get(AppConstants.OPERATOR).getAsString()));
				json=json.replace(AppConstants.PTRN_MATCH,String.valueOf(values.get(AppConstants.MATCH)));
				
				
				String markupJson= getMarkupJson(values.get(AppConstants.ATTRIBUTE).getAsString(),"appscan",appName,"appType");
				json=json.replace("%ATTRIBUTE_CHILDREN%",markupJson);
				
				String operatorJson	=	updateOperatorJson(values.get(AppConstants.ATTRIBUTE).getAsString(),"appscan",appName,"appType");
				json=json.replace(AppConstants.PTRN_OPERATOR_CHILDREN,operatorJson);

				appscanJson.add(json);
			}
			if(StringUtils.equalsIgnoreCase(type, "notifications")) {
				String json=RuleMigrationTemplate.CONNECTOR_NOTIFICATIONS_TEMPLATE;
				json=json.replace("%ID%",String.valueOf(nodesObject.get("id")));
				json=json.replace(AppConstants.PTRN_POSITION,String.valueOf(nodesObject.get(AppConstants.POSITION)));
				if(StringUtils.isNotBlank(ruleType)) {
					json=json.replace("%ACTIONNAME%",ruleType);
				}
				else {
					json=json.replace("%ACTIONNAME%",StringUtils.EMPTY);
				}
				
				appscanJson.add(json);
			}
			if(StringUtils.equalsIgnoreCase(type, "email_scanner")) {
				String json=RuleMigrationTemplate.EMAIL_SCAN_TEMPLATE;
				json=json.replace("%ID%",String.valueOf(nodesObject.get("id")));
				json=json.replace(AppConstants.PTRN_POSITION,String.valueOf(nodesObject.get(AppConstants.POSITION)));
				JsonObject propertiesObject=(JsonObject)nodesObject.get(AppConstants.PROPERTIES);
				JsonObject content=(JsonObject)propertiesObject.get(AppConstants.CONTENT);
				json=json.replace("%TIME%",String.valueOf(content.get("time")));
				json=json.replace("%CHECKPAST%",String.valueOf(content.get("checkpast")));

				appscanJson.add(json);
			}
			if(StringUtils.equalsIgnoreCase(type, "email_scanner_condition")) {
				String json=RuleMigrationTemplate.EMAIL_MATCH_TEMPLATE;
				json=json.replace("%ID%",String.valueOf(nodesObject.get("id")));
				json=json.replace(AppConstants.PTRN_POSITION,String.valueOf(nodesObject.get(AppConstants.POSITION)));

				JsonObject propertiesObject=(JsonObject)nodesObject.get(AppConstants.PROPERTIES);
				JsonArray content = propertiesObject.getAsJsonArray(AppConstants.CONTENT);
				JsonObject values= (JsonObject) content.get(0);

				json=json.replace(AppConstants.PTRN_ATTRIBUTE,getMatchattrDictionary(values.get(AppConstants.ATTRIBUTE).getAsString()));
				String attribute=getMatchattrDictionary(values.get(AppConstants.ATTRIBUTE).getAsString());
				if(StringUtils.equalsIgnoreCase(attribute, "SENT_ATTACHMENT_COUNT")) {
				json=json.replace(AppConstants.PTRN_OPERATOR,"equals");
				}
				else {
					json=json.replace(AppConstants.PTRN_OPERATOR,getOperatorDictionary(values.get(AppConstants.OPERATOR).getAsString()));
				}
				json=json.replace(AppConstants.PTRN_MATCH,String.valueOf(values.get(AppConstants.MATCH)));
				
                String operatorJson	=	updateOperatorJsonForEmailMatch(values.get(AppConstants.ATTRIBUTE).getAsString(),"emailscan","LookForPastEmail");
				json=json.replace(AppConstants.PTRN_OPERATOR_CHILDREN,operatorJson);


				appscanJson.add(json);
			}
			if(StringUtils.equalsIgnoreCase(type, "email_conditional")) {
				String json=RuleMigrationTemplate.EMAIL_CONDITION_TEMPLATE;
				json=json.replace("%ID%",String.valueOf(nodesObject.get("id")));
				json=json.replace(AppConstants.PTRN_POSITION,String.valueOf(nodesObject.get(AppConstants.POSITION)));

				JsonObject propertiesObject=(JsonObject)nodesObject.get(AppConstants.PROPERTIES);
				JsonObject content=(JsonObject)propertiesObject.get(AppConstants.CONTENT);
				json=json.replace(AppConstants.PTRN_MODE,getFieldValueDictionary(content.get("mode").getAsString()));

				appscanJson.add(json);
			}
			if(StringUtils.equalsIgnoreCase(type, "conditional_rule")) {
				String json=RuleMigrationTemplate.CONNECTOR_CONDITION_TEMPLATE;
				json=json.replace("%ID%",String.valueOf(nodesObject.get("id")));
				json=json.replace(AppConstants.PTRN_POSITION,String.valueOf(nodesObject.get(AppConstants.POSITION)));

				JsonObject propertiesObject=(JsonObject)nodesObject.get(AppConstants.PROPERTIES);
				JsonObject content=(JsonObject)propertiesObject.get(AppConstants.CONTENT);
				json=json.replace(AppConstants.PTRN_MODE,getFieldValueDictionary(content.get("mode").getAsString()));

				appscanJson.add(json);
			}
			if(StringUtils.equalsIgnoreCase(type, "app_conditional")) {
				String json=RuleMigrationTemplate.APP_CONDITION_TEMPLATE;
				json=json.replace("%ID%",String.valueOf(nodesObject.get("id")));
				json=json.replace(AppConstants.PTRN_POSITION,String.valueOf(nodesObject.get(AppConstants.POSITION)));

				JsonObject propertiesObject=(JsonObject)nodesObject.get(AppConstants.PROPERTIES);
				JsonObject content=(JsonObject)propertiesObject.get(AppConstants.CONTENT);
				json=json.replace(AppConstants.PTRN_MODE,getFieldValueDictionary(content.get("mode").getAsString()));

				appscanJson.add(json);
			}
			if(StringUtils.equalsIgnoreCase(type, "fs_scanner")) {
				String json=RuleMigrationTemplate.FILE_SCAN_TEMPLATE;
				json=json.replace("%ID%",String.valueOf(nodesObject.get("id")));
				json=json.replace(AppConstants.PTRN_POSITION,String.valueOf(nodesObject.get(AppConstants.POSITION)));
				JsonObject propertiesObject=(JsonObject)nodesObject.get(AppConstants.PROPERTIES);
				JsonObject content=(JsonObject)propertiesObject.get(AppConstants.CONTENT);
				json=json.replace("%CUSTOMPATH%",String.valueOf(content.get("path")).replace("\\", "\\\\"));
				json=json.replace("%RECURSIVE%",String.valueOf(content.get("recursive")));
				json=json.replace(AppConstants.PTRN_APP,getFieldValueDictionary(content.get(AppConstants.REFERENCE).getAsString()));
				String field=getFieldValueDictionary(content.get(AppConstants.REFERENCE).getAsString());
				if(StringUtils.equalsIgnoreCase(field, "Absolute Path")) {
					json=json.replace("%DISABLED%",AppConstants.FALSE);
				}
				else {
					json=json.replace("%DISABLED%","true");
				}

				appscanJson.add(json);
			}
			if(StringUtils.equalsIgnoreCase(type, "fs_scanner_condition")) {
				String json=RuleMigrationTemplate.FILE_MATCH_TEMPLATE;
				json=json.replace("%ID%",String.valueOf(nodesObject.get("id")));
				json=json.replace(AppConstants.PTRN_POSITION,String.valueOf(nodesObject.get(AppConstants.POSITION)));

				JsonObject propertiesObject=(JsonObject)nodesObject.get(AppConstants.PROPERTIES);
				JsonArray content = propertiesObject.getAsJsonArray(AppConstants.CONTENT);
				JsonObject values= (JsonObject) content.get(0);

				json=json.replace(AppConstants.PTRN_ATTRIBUTE,getMatchattrDictionary(values.get(AppConstants.ATTRIBUTE).getAsString()));
				String attribute=getMatchattrDictionary(values.get(AppConstants.ATTRIBUTE).getAsString());
				if(StringUtils.equalsIgnoreCase(attribute, AppConstants.NESTED_LEVEL)|| StringUtils.equalsIgnoreCase(attribute, AppConstants.FILE_OPERATION)) {
					json=json.replace(AppConstants.PTRN_OPERATOR,"equals");
				}
				else {
				json=json.replace(AppConstants.PTRN_OPERATOR,getOperatorDictionary(values.get(AppConstants.OPERATOR).getAsString()));
				}
				json=json.replace(AppConstants.PTRN_MATCH,String.valueOf(values.get(AppConstants.MATCH)));
				
				if(StringUtils.equalsIgnoreCase(attribute, "FILE_NAME") || StringUtils.equalsIgnoreCase(attribute, "FOLDER_NAME")) {
				json=json.replace("%DYNAMIC%",AppConstants.FALSE);
				json=json.replace("%STATICANDDYNAMIC%",AppConstants.FALSE);
				json=json.replace("%DYNAMICANDSTATIC%",AppConstants.FALSE);
				}
				else {
					json=json.replace("%DYNAMIC%","true");
					json=json.replace("%STATICANDDYNAMIC%","true");
					json=json.replace("%DYNAMICANDSTATIC%","true");
				}
				String placeholder=getPlaceholder(attribute);
				json=json.replace("%PLACEHOLDER%",placeholder);
				String sourceID	=	getSourceID(appscanJson,nodesObject.get("id").getAsString());
				List<String> appName= getAppNameFsScan(sourceID,nodes,appscanJson);
				
				String markupJson= getMarkupJson(values.get(AppConstants.ATTRIBUTE).getAsString(),"filesystem",appName,"selectLoc");
				json=json.replace("%ATTRIBUTE_CHILDREN%",markupJson);
				
				String operatorJson	=	updateOperatorJson(values.get(AppConstants.ATTRIBUTE).getAsString(),"filesystem",appName,"selectLoc");
				json=json.replace(AppConstants.PTRN_OPERATOR_CHILDREN,operatorJson);


				appscanJson.add(json);
			}
			if(StringUtils.equalsIgnoreCase(type, AppConstants.FS_CONDITIONAL)) {
				String json=RuleMigrationTemplate.FILE_CONDITION_TEMPLATE;
				json=json.replace("%ID%",String.valueOf(nodesObject.get("id")));
				json=json.replace(AppConstants.PTRN_POSITION,String.valueOf(nodesObject.get(AppConstants.POSITION)));

				JsonObject propertiesObject=(JsonObject)nodesObject.get(AppConstants.PROPERTIES);
				JsonObject content=(JsonObject)propertiesObject.get(AppConstants.CONTENT);
				json=json.replace(AppConstants.PTRN_MODE,getFieldValueDictionary(content.get("mode").getAsString()));

				appscanJson.add(json);
			}
		}

	}

	private String getPlaceholder(String attribute) {
		String placeholder;
		if(StringUtils.equalsIgnoreCase(attribute, AppConstants.NESTED_LEVEL)) {
			placeholder="Static Text (number 0-3) allowed)";
		}
		else if(StringUtils.equalsIgnoreCase(attribute, AppConstants.FILE_OPERATION)) {
			placeholder="Static Text (createAndmodify/modify/delete) allowed)";
		}
		else if(StringUtils.equalsIgnoreCase(attribute, "FOLDER_OPERATION")) {
			placeholder="Static Text (createAndmodify/modify) allowed)";
		}
		else {
			placeholder="Static Text";
		}
		
		return placeholder;
	}

	private String getMarkupJson(String asString, String scanName, List<String> appName, String value) {
		
		String attributeChildren=RuleMigrationTemplate.ATTRIBUTE_CHILDREN;
		attributeChildren=attributeChildren.replace(AppConstants.PTRN_TEXT_CONTENT, "Select Attribute");
		
		Set<String> attributeList=new HashSet<>();
		List<String> operatorsList=new LinkedList<>();
		try {
			String json=RuleMigrationTemplate.STATIC_JSON;
			JsonParser parser = new JsonParser();
			JsonReader reader = new JsonReader(
					new StringReader(StringEscapeUtils.unescapeHtml(StringUtils.trim(json))));
			reader.setLenient(true);
			JsonObject staticJson = (JsonObject) parser.parse(reader);// response will be the json String

			JsonArray scanType = staticJson.getAsJsonArray(AppConstants.SCAN_TYPE);
			for(JsonElement appElement:scanType) {
				JsonObject nodesObject=(JsonObject) appElement;
				if(StringUtils.equalsIgnoreCase(nodesObject.get(AppConstants.VALUE).getAsString(), scanName)) {
					JsonArray appType = nodesObject.getAsJsonArray(value);
					for(JsonElement appTypeElements:appType) {
						JsonObject appTypeElement=(JsonObject) appTypeElements;
						if(appName.contains(appTypeElement.get(AppConstants.VALUE).getAsString())) {
							JsonArray attributesArray = appTypeElement.getAsJsonArray(AppConstants.ATTRIBUTES);
							for(JsonElement attributesElements:attributesArray) {
								JsonObject attributeElement=(JsonObject) attributesElements;
								String attribute=RuleMigrationTemplate.ATTRIBUTE;
								attribute=attribute.replace(AppConstants.PTRN_VALUE,attributeElement.get(AppConstants.VALUE).getAsString());
								attribute=attribute.replace(AppConstants.PTRN_ATTRS_NAME,attributeElement.get("attrsName").getAsString());
								attributeList.add(attribute);
								
								if(StringUtils.equalsIgnoreCase(attributeElement.get(AppConstants.VALUE).getAsString(),
										getMatchattrDictionary(asString))) {
									JsonArray operatorsArray = attributeElement.getAsJsonArray(AppConstants.OPERATORS);
									for(JsonElement operatorsElements:operatorsArray) {
										JsonObject operatorsElement=(JsonObject) operatorsElements;
										String operators=RuleMigrationTemplate.ATTRIBUTE;
										
										operators=operators.replace(AppConstants.PTRN_ATTRS_NAME,operatorsElement.get(AppConstants.OPNAME).getAsString());
										operators=operators.replace(AppConstants.PTRN_VALUE,operatorsElement.get(AppConstants.VALUE).getAsString());
										if(!operatorsList.contains(operators)) {
											operatorsList.add(operators);
										}
									}
								}
							}
						}
					}
				}
			}
			if(attributeList.size()>0) {
			attributeChildren=attributeChildren.replace(AppConstants.PTRN_ATTRIBUTE,","+
					attributeList.toString()
					.substring(1, attributeList.toString().length()-1)
					);
			}
			else {
				attributeChildren=attributeChildren.replace(AppConstants.PTRN_ATTRIBUTE,"");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return attributeChildren;
	}

	private String updateOperatorJsonForEmailMatch(String asString, String scanName, String value) {
		
		String operatorChildren=RuleMigrationTemplate.ATTRIBUTE_CHILDREN;
		operatorChildren=operatorChildren.replace(AppConstants.PTRN_TEXT_CONTENT, "Select Operator");
		List<String> operatorsList=new LinkedList<>();
		String json=RuleMigrationTemplate.STATIC_JSON;
		JsonParser parser = new JsonParser();
		JsonReader reader = new JsonReader(
				new StringReader(StringEscapeUtils.unescapeHtml(StringUtils.trim(json))));
		reader.setLenient(true);
		JsonObject staticJson = (JsonObject) parser.parse(reader);// response will be the json String

		JsonArray scanType = staticJson.getAsJsonArray(AppConstants.SCAN_TYPE);
		for(JsonElement appElement:scanType) {
			JsonObject nodesObject=(JsonObject) appElement;
			if(StringUtils.equalsIgnoreCase(nodesObject.get(AppConstants.VALUE).getAsString(), scanName)) {
				JsonArray appType = nodesObject.getAsJsonArray(value);
				for(JsonElement appTypeElements:appType) {
					JsonObject appTypeElement=(JsonObject) appTypeElements;
					JsonArray attributesArray = appTypeElement.getAsJsonArray(AppConstants.ATTRIBUTES);
						for(JsonElement attributesElements:attributesArray) {
							JsonObject attributeElement=(JsonObject) attributesElements;
							if(StringUtils.equalsIgnoreCase(attributeElement.get(AppConstants.VALUE).getAsString(),
									getMatchattrDictionary(asString))) {
								JsonArray operatorsArray = attributeElement.getAsJsonArray(AppConstants.OPERATORS);
								for(JsonElement operatorsElements:operatorsArray) {
									JsonObject operatorsElement=(JsonObject) operatorsElements;
									String operators=RuleMigrationTemplate.ATTRIBUTE;
									
									operators=operators.replace(AppConstants.PTRN_ATTRS_NAME,operatorsElement.get(AppConstants.OPNAME).getAsString());
									operators=operators.replace(AppConstants.PTRN_VALUE,operatorsElement.get(AppConstants.VALUE).getAsString());
									if(!operatorsList.contains(operators)) {
										operatorsList.add(operators);
									}
								}
							}
						}
				}
			}
		}
		if(operatorsList.size()>0) {
		operatorChildren=operatorChildren.replace(AppConstants.PTRN_ATTRIBUTE,","+
				operatorsList.toString()
				.substring(1, operatorsList.toString().length()-1)
				);
		}
		else {
			operatorChildren=operatorChildren.replace(AppConstants.PTRN_ATTRIBUTE,"");
		}
		return operatorChildren;
	}
	
private String updateOperatorJson(String asString, String scanName, List<String> appName, String value) {
		
		String operatorChildren=RuleMigrationTemplate.ATTRIBUTE_CHILDREN;
		operatorChildren=operatorChildren.replace(AppConstants.PTRN_TEXT_CONTENT, "Select Operator");
		List<String> operatorsList=new LinkedList<>();
		String json=RuleMigrationTemplate.STATIC_JSON;
		JsonParser parser = new JsonParser();
		JsonReader reader = new JsonReader(
				new StringReader(StringEscapeUtils.unescapeHtml(StringUtils.trim(json))));
		reader.setLenient(true);
		JsonObject staticJson = (JsonObject) parser.parse(reader);// response will be the json String

		JsonArray scanType = staticJson.getAsJsonArray(AppConstants.SCAN_TYPE);
		for(JsonElement appElement:scanType) {
			JsonObject nodesObject=(JsonObject) appElement;
			if(StringUtils.equalsIgnoreCase(nodesObject.get(AppConstants.VALUE).getAsString(), scanName)) {
				JsonArray appType = nodesObject.getAsJsonArray(value);
				for(JsonElement appTypeElements:appType) {
					JsonObject appTypeElement=(JsonObject) appTypeElements;
					if(appName.contains(appTypeElement.get(AppConstants.VALUE).getAsString())) {
						JsonArray attributesArray = appTypeElement.getAsJsonArray(AppConstants.ATTRIBUTES);
						for(JsonElement attributesElements:attributesArray) {
							JsonObject attributeElement=(JsonObject) attributesElements;
							if(StringUtils.equalsIgnoreCase(attributeElement.get(AppConstants.VALUE).getAsString(),
									getMatchattrDictionary(asString))) {
								JsonArray operatorsArray = attributeElement.getAsJsonArray(AppConstants.OPERATORS);
								for(JsonElement operatorsElements:operatorsArray) {
									JsonObject operatorsElement=(JsonObject) operatorsElements;
									String operators=RuleMigrationTemplate.ATTRIBUTE;
									
									operators=operators.replace(AppConstants.PTRN_ATTRS_NAME,operatorsElement.get(AppConstants.OPNAME).getAsString());
									operators=operators.replace(AppConstants.PTRN_VALUE,operatorsElement.get(AppConstants.VALUE).getAsString());
									if(!operatorsList.contains(operators)) {
										operatorsList.add(operators);
									}
								}
							}
						}
					}
				}
			}
		}
		if(operatorsList.size()>0) {
			operatorChildren=operatorChildren.replace(AppConstants.PTRN_ATTRIBUTE,","+
					operatorsList.toString()
					.substring(1, operatorsList.toString().length()-1)
					);
			}
			else {
				operatorChildren=operatorChildren.replace(AppConstants.PTRN_ATTRIBUTE,"");
			}
		
		return operatorChildren;
	}

	private List<String> getAppNameFsScan(String sourceID, JsonObject nodes, List<String> appscanJson) {
		List<String> appName = new ArrayList<String>();
		for ( Map.Entry<String, JsonElement> map:nodes.entrySet()) {
			JsonElement nodesElement = map.getValue();
			JsonObject nodesObject=(JsonObject) nodesElement;
			String id=nodesObject.get("id").getAsString();
			String type=nodesObject.get("type").getAsString();
			if(StringUtils.equalsIgnoreCase(id, sourceID)) {
				if(StringUtils.equalsIgnoreCase(type, AppConstants.FS_CONDITIONAL)) {
					sourceID=getSourceID(appscanJson,sourceID);
					return getAppNameFsScan(sourceID,nodes,appscanJson);
				}
				else {
				JsonObject propertiesObject=(JsonObject)nodesObject.get(AppConstants.PROPERTIES);
				JsonObject content=(JsonObject)propertiesObject.get(AppConstants.CONTENT);
				if(StringUtils.isEmpty(content.get(AppConstants.REFERENCE).getAsString())) {
					appName.add("-1");
				} 
				else {
					appName.add(getFieldValueDictionary(content.get(AppConstants.REFERENCE).getAsString()));
				}
				break;
			}
			}
		}
		return appName;
	}
	private List<String> getAppName(String sourceID, JsonObject nodes, List<String> appscanJson) {
		List<String> appName = new ArrayList<String>();
		for ( Map.Entry<String, JsonElement> map:nodes.entrySet()) {
			JsonElement nodesElement = map.getValue();
			JsonObject nodesObject=(JsonObject) nodesElement;
			String id=nodesObject.get("id").getAsString();
            String type=nodesObject.get("type").getAsString();
			if(StringUtils.equalsIgnoreCase(id, sourceID)) {
				if(StringUtils.equalsIgnoreCase(type, "email_conditional")||StringUtils.equalsIgnoreCase(type, "conditional_rule")||StringUtils.equalsIgnoreCase(type, "app_conditional")||StringUtils.equalsIgnoreCase(type, AppConstants.FS_CONDITIONAL)) {
					sourceID=getSourceID(appscanJson,sourceID);
					return getAppName(sourceID,nodes,appscanJson);
				}
				else {
				JsonObject propertiesObject=(JsonObject)nodesObject.get(AppConstants.PROPERTIES);
				JsonObject content=(JsonObject)propertiesObject.get(AppConstants.CONTENT);
				JsonArray validObject = content.getAsJsonArray("valid_apps");
				if(validObject.size()==0) {
					appName.add("-1");
				} 
				else {
					for(JsonElement appElement:validObject) {
					appName.add(getFieldValueDictionary(appElement.getAsString()));
					}
				}
				break;
				}
			}
		}
		return appName;
	}

	private String getSourceID(List<String> appscanJson, String targetID) {
		String sourceID=null;
		for ( String jsonString: appscanJson) {
			JsonObject convertedObject = new Gson().fromJson(jsonString, JsonObject.class);
			if(StringUtils.equalsIgnoreCase(convertedObject.get("type").getAsString(), "standard.Link")) {
				JsonObject targetObject =(JsonObject) convertedObject.get("target") ;
				if(StringUtils.equalsIgnoreCase(targetObject.get("id").getAsString(), targetID)){
					JsonObject sourceObject =(JsonObject) convertedObject.get("source");
					sourceID=sourceObject.get("id").getAsString();
					break;
				}
			}
		}
		return sourceID;
	}

	private void createLinkJSON(JsonObject links, List<String> linkjson) {
		for ( Map.Entry<String, JsonElement> map:links.entrySet()) {
			
			String json=RuleMigrationTemplate.LINK_TEMPLATE;
			JsonElement linkElement = map.getValue();
			JsonObject linksObject=(JsonObject) linkElement;
			
			JsonElement from=linksObject.get("from");
			JsonObject fromObject=(JsonObject) from;
			JsonElement fromPortId=fromObject.get("portId");
			JsonElement fromNodeId=fromObject.get("nodeId");
			JsonElement to=linksObject.get("to");
			JsonObject toObject=(JsonObject) to;
			JsonElement toPortId=toObject.get("portId");
			JsonElement toNodeId=toObject.get("nodeId");
			JsonElement linkId=linksObject.get("id");
			
			json=json.replace("%S_ID%",String.valueOf(fromNodeId));
			String sourcePortId=getPortIdDictionary(fromPortId.getAsString());
			json=json.replace("%S_PORT%",sourcePortId);
			json=json.replace("%T_ID%",String.valueOf(toNodeId));
			String targetPortId=getPortIdDictionary(toPortId.getAsString());
			json=json.replace("%T_PORT%",targetPortId);
			json=json.replace("%LINK_ID%",String.valueOf(linkId));
			
			linkjson.add(json);
		}
	}
	
	private String getOperatorDictionary(String fieldValue) {
		if(StringUtils.equalsIgnoreCase(fieldValue,"contains_i")) {
			fieldValue="containscaseinsensitive";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"contains")) {
			fieldValue="contains";
		}
		else {
			fieldValue= "-1";
		}
		return fieldValue;
	}
	
	private String getMatchattrDictionary(String fieldValue) {
		if(StringUtils.equalsIgnoreCase(fieldValue,"to")) {
			fieldValue="SENT_TO_EMAIL";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"inbox_subject")) {
			fieldValue="INBOX_SUBJECT";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"from")) {
			fieldValue="INBOX_FROM_EMAIL";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"sent_subject")) {
			fieldValue="SENT_SUBJECT";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"inbox_attachments")) {
			fieldValue="INBOX_ATTACHMENT";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"sent_attachments")) {
			fieldValue="SENT_ATTACHMENT";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"sent_body")) {
			fieldValue="SENT_BODY";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"sent_attachments_count")) {
			fieldValue="SENT_ATTACHMENT_COUNT";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"key")) {
			fieldValue="INPUT_BUFFER_KEYSTROKE";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"title")) {
			fieldValue="APPLICATION_TITLE";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"fileop")) {
			fieldValue=AppConstants.FILE_OPERATION;
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"filename")) {
			fieldValue="FILE_NAME";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"extension")) {
			fieldValue="FILE_EXTENSION";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"level")) {
			fieldValue=AppConstants.NESTED_LEVEL;
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"sheets")) {
			fieldValue="EXCEL_SHEETS";
		}
		
		else {
			fieldValue="-1";
		}
		return fieldValue;
	}
	
	private String getFieldValueDictionary(String fieldValue) {
		if(StringUtils.equalsIgnoreCase(fieldValue,"excel")) {
			fieldValue="Excel";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"winword")) {
			fieldValue="Word";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"chrome")) {
			fieldValue="Google Chrome";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"firefox")) {
			fieldValue="Mozilla Firefox";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"notepad")) {
			fieldValue="Notepad";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"iexplore")) {
			fieldValue="Internet Explorer";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"Desktop")) {
			fieldValue="Desktop";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"MyDownloads")) {
			fieldValue="Downloads";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"absolute")) {
			fieldValue="Absolute Path";
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"and")) {
			fieldValue=AppConstants.FALSE;
		}
		else if(StringUtils.equalsIgnoreCase(fieldValue,"or")) {
			fieldValue="true";
		}
		else {
			fieldValue="-1";
		}
		return fieldValue;
	}
	
	
	private String getPortIdDictionary(String portId) {
		if(StringUtils.equalsIgnoreCase(portId,"port1")) {
			portId="in";
		}
		else if(StringUtils.equalsIgnoreCase(portId,"port2")) {
			portId="out";
		}
		else if(StringUtils.equalsIgnoreCase(portId,"port3")) {
			portId="r";
		}
		else if(StringUtils.equalsIgnoreCase(portId,"port4")) {
			portId="and";
		}
		else if(StringUtils.equalsIgnoreCase(portId,"port5")) {
			portId="or";
		}
		return portId;
	}

	public Response<Void> updateMigrationAutoSenseRule(MigrationAutoSenseRuleModel migrationAutoSenseRuleModel, String signum) {
		Response<Void> response = new Response<Void>();
		if (migrationAutoSenseRuleModel == null || StringUtils.isEmpty(migrationAutoSenseRuleModel.getRuleName())
				|| StringUtils.isEmpty(migrationAutoSenseRuleModel.getRuleDescription())
				||StringUtils.isEmpty(migrationAutoSenseRuleModel.getNewRuleJson())
				|| migrationAutoSenseRuleModel.getDomainID()== 0 
				|| migrationAutoSenseRuleModel.getSubactivityID()==0 || migrationAutoSenseRuleModel.getTechnologyID()==0
				||migrationAutoSenseRuleModel.getTaskID()==0 || migrationAutoSenseRuleModel.getServiceAreaID() == 0 ) {

			response.addFormError(MANDATORY_FIELDS_MISSING);
		} 
		else {
			List<MigrationAutoSenseRuleModel> migrationRuleNames = this.autoSenseDao.getAutosenseMigrationRuleName(
					migrationAutoSenseRuleModel.getRuleName(),migrationAutoSenseRuleModel.getRuleMigrationID());
			if (CollectionUtils.isNotEmpty(migrationRuleNames)) {
				response.addFormError(SAME_RULE_NAME);
			} else {
				try {
					if (StringUtils.isNotEmpty(migrationAutoSenseRuleModel.getRuleName())
							&& StringUtils.isNotEmpty(migrationAutoSenseRuleModel.getRuleDescription())) {
						
						String ruleJsonForScanner =jsonUtil.getParsedRuleJsonFromRuleJson(migrationAutoSenseRuleModel.getNewRuleJson(),false);
						String parsedRuleJsonWithDefaultDynamicValue=jsonUtil.getParsedRuleJsonFromRuleJson(migrationAutoSenseRuleModel.getNewRuleJson(),true);
						String ruleJsonForValidation=jsonUtil.getManualValidateJsonFromParsedRuleJson(parsedRuleJsonWithDefaultDynamicValue,signum);
						
						if(StringUtils.isBlank(ruleJsonForScanner) || StringUtils.isBlank(ruleJsonForValidation)) {
							response.addFormError(AppConstants.GOT_AN_ERROR_WHILE_CONVERTING_RULE);
							return response;
					  
					} 
						migrationAutoSenseRuleModel.setRuleJsonForScanner(ruleJsonForScanner);
						migrationAutoSenseRuleModel.setRuleJsonForValidation(ruleJsonForValidation);
						
						autoSenseDao.updateMigrationAutoSenseRule(migrationAutoSenseRuleModel,signum);
						response.addFormMessage(RULE_EDIT);
						LOG.info("Execution of updateMigrationAutoSenseRule Api Successful.");
					}
				} catch (Exception e) {
					response.addFormError(RULE_EDIT_ERROR);
					e.printStackTrace();
					LOG.info( e.getMessage());
				}
			}
		}
		return response;
	  }	


	
	public Response<Void> deleteMigrationAutoSenseRule(MigrationAutoSenseRuleModel migrationAutoSenseRuleModel) {
		Response<Void> response = new Response<Void>();
		try {
			autoSenseDao.deleteMigrationAutoSenseRule(migrationAutoSenseRuleModel);
			response.addFormMessage(RULE_DELETED);
			LOG.info("Execution of deleteMigrationAutoSenseRule Api Successful.");
		} catch (Exception e) {
			response.addFormError(e.getMessage());
			LOG.info( e.getMessage());
		}
		return response;
	}
	
	public Response<Void> manualValidationMigrationAutoSenseRule(MigrationAutoSenseRuleModel migrationAutoSenseRuleModel, String signum) {
		Response<Void> response = new Response<Void>();
		try {
			autoSenseDao.manualValidationMigrationAutoSenseRule(migrationAutoSenseRuleModel,signum);
			response.addFormMessage(RULE_MANUAL_VALIDATION);
			LOG.info("Execution of manualValidationMigrationAutoSenseRule Api Successful.");
		} catch (Exception e) {
			response.addFormError(e.getMessage());
			LOG.info( e.getMessage());
		}
		return response;
	}
	
	public Response<List<MigrationAutoSenseRuleModel>> getAllRuleMigrationDetails(String creatorSignum) {
		Response<List<MigrationAutoSenseRuleModel>> response = new Response<>();
		List<MigrationAutoSenseRuleModel> listOfRulesMigration = autoSenseDao.getAllRuleMigrationDetails(creatorSignum);
		if (CollectionUtils.isEmpty(listOfRulesMigration)) {
			response.addFormError(AppConstants.NO_DETAILS_EXISTS);
		} else {
			response.setResponseData(listOfRulesMigration);
			LOG.info("Execution of getAllRuleMigrationDetails API Successful.");
			
		}
		return response;
	}

	

	public Response<MigrationAutoSenseRuleModel> getRuleJsonByMigrationID(int ruleMigrationID) {
		Response<MigrationAutoSenseRuleModel> response = new Response<>();
		MigrationAutoSenseRuleModel ruleJsonDetails = this.autoSenseDao.getRuleJsonByMigrationID(ruleMigrationID);
			if (ruleJsonDetails == null) {
				response.addFormError(AppConstants.NO_DETAILS_EXISTS);
				response.setValidationFailed(true);
			} else {
				response.setResponseData(ruleJsonDetails);
				response.setValidationFailed(false);
			}
			return response;
		
	}
	
	public Response<Integer> saveMigrationAutoSenseRule(MigrationAutoSenseRuleModel migrationAutoSenseRuleModel,String signum) {
		Response<Integer> response = new Response<Integer>();
		if (migrationAutoSenseRuleModel == null || StringUtils.isEmpty(migrationAutoSenseRuleModel.getRuleName())
				|| StringUtils.isEmpty(migrationAutoSenseRuleModel.getRuleDescription())
				||StringUtils.isEmpty(migrationAutoSenseRuleModel.getNewRuleJson())
				|| migrationAutoSenseRuleModel.getDomainID()== 0 
				|| migrationAutoSenseRuleModel.getSubactivityID()==0 || migrationAutoSenseRuleModel.getTechnologyID()==0
				||migrationAutoSenseRuleModel.getTaskID()==0 || migrationAutoSenseRuleModel.getServiceAreaID() == 0 ) {

			response.addFormError(MANDATORY_FIELDS_MISSING);
		} 
		 else {
			 List<MigrationAutoSenseRuleModel> migrationRuleNames = this.autoSenseDao.getAutosenseMigrationRuleNames(
				      migrationAutoSenseRuleModel.getRuleName());
				if (CollectionUtils.isNotEmpty(migrationRuleNames)) {
					response.addFormError(SAME_RULE_NAME);
				} 	 
			    else {
					 try {
							if (StringUtils.isNotEmpty(migrationAutoSenseRuleModel.getRuleName())
									&& StringUtils.isNotEmpty(migrationAutoSenseRuleModel.getRuleDescription())) {
					
								String ruleJsonForScanner =jsonUtil.getParsedRuleJsonFromRuleJson(migrationAutoSenseRuleModel.getNewRuleJson(),false);
								String parsedRuleJsonWithDefaultDynamicValue=jsonUtil.getParsedRuleJsonFromRuleJson(migrationAutoSenseRuleModel.getNewRuleJson(),true);
								String ruleJsonForValidation=jsonUtil.getManualValidateJsonFromParsedRuleJson(parsedRuleJsonWithDefaultDynamicValue,signum);
								
								if(StringUtils.isBlank(ruleJsonForScanner) || StringUtils.isBlank(ruleJsonForValidation)) {
									response.addFormError(AppConstants.GOT_AN_ERROR_WHILE_CONVERTING_RULE);
									return response;
							  
							} 
								migrationAutoSenseRuleModel.setRuleJsonForScanner(ruleJsonForScanner);
								migrationAutoSenseRuleModel.setRuleJsonForValidation(ruleJsonForValidation);
								
								autoSenseDao.saveMigrationAutoSenseRule(migrationAutoSenseRuleModel,signum);
								int migrationruleID = isfCustomIdInsert.generateCustomId(migrationAutoSenseRuleModel.getRuleMigrationID());
								response.setResponseData(migrationruleID);
								response.addFormMessage(AppConstants.AUTO_SENSE_RULE_ADDED_SUCCESSFULLY);
								LOG.info("Execution of saveMigrationAutoSenseRule API Successful.");
							}
						} catch (Exception e) {
							response.addFormError(ApplicationMessages.AUTO_SENSE_RULE_SAVE_ERROR_MESSAGE);
							e.printStackTrace();
						    LOG.info( e.getMessage());
						}
			           }
				}
				return response;
			  }

	
	public Response<Void> completeSensedRules(String signum, String overrideActionName, String source) {
		Response<Void> apiResponse= new Response<>();
		try {
		autoSenseDao.completeSensedRules(signum,overrideActionName, source);
		LOG.info("completeSensedRules : SUCCESS");
		}
		catch(Exception e) {
			apiResponse.addFormError(e.getMessage());
			LOG.error(e.getMessage());
		}
		return apiResponse;
	}

	public DownloadTemplateModel downloadAutoSenseRuleExcel() throws IOException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = "AutoSenseRuleExport" + "-" + (new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()))
				+ ".xlsx";

		List<Map<String, Object>> AutoSenseRule = autoSenseDao.downloadAutoSenseRuleExcel();
		
		byte[] fData = null;
		if (CollectionUtils.isNotEmpty(AutoSenseRule)) {
			fData = FileUtil.generateXlsFile(AutoSenseRule);
			
		}
		
		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);
		

		return downloadTemplateModel;
		
	}
		

		
			 
	
	
}
