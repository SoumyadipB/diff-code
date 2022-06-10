
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.FlowChartDAO;
import com.ericsson.isf.dao.ProjectDAO;
import com.ericsson.isf.dao.RpaDAO;
import com.ericsson.isf.dao.ToolsMasterDAO;
import com.ericsson.isf.dao.WorkOrderPlanDao;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.ericsson.isf.flowchart.util.FlowChartStepTemplate;
import com.ericsson.isf.helper.FileCSVHelper;
import com.ericsson.isf.model.BotSavingModel;
import com.ericsson.isf.model.CustomStepJSONModel;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.ErrorBean;
import com.ericsson.isf.model.FlowChartDefModel;
import com.ericsson.isf.model.FlowChartDependencyModel;
import com.ericsson.isf.model.FlowChartJsonModel;
import com.ericsson.isf.model.FlowChartPopulateDataModel;
import com.ericsson.isf.model.FlowChartSaveModel;
import com.ericsson.isf.model.FlowChartStepDetailsModel;
import com.ericsson.isf.model.FlowChartStepModel;
import com.ericsson.isf.model.KPIValueModel;
import com.ericsson.isf.model.KpiModel;
import com.ericsson.isf.model.LoeMeasurementCriterionModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.ScopeAndMasterTaskModel;
import com.ericsson.isf.model.ScopeTaskMappingModel;
import com.ericsson.isf.model.TaskAndScopeMappingModel;
import com.ericsson.isf.model.TaskModel;
import com.ericsson.isf.model.ToolsModel;
import com.ericsson.isf.model.WFStepInstructionModel;
import com.ericsson.isf.model.WorkFlowApprovalModel;
import com.ericsson.isf.model.WorkFlowAvailabilitySearchModel;
import com.ericsson.isf.model.WorkFlowDefinitionModel;
import com.ericsson.isf.model.WorkFlowLinksModel;
import com.ericsson.isf.model.WorkFlowLinksVerticesModel;
import com.ericsson.isf.model.WorkFlowStepAttrModel;
import com.ericsson.isf.model.WorkFlowStepsModel;
import com.ericsson.isf.model.WorkflowProficiencyModel;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AppUtil;
import com.ericsson.isf.util.ApplicationMessages;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.ericsson.isf.util.IsfCustomIdInsert;
import com.ericsson.isf.util.MailUtil;
import com.ericsson.isf.service.ActivityMasterService;;

/**
 *
 * @author ekarath
 */
@Service
public class FlowChartService {

	private static final String PROFICIENCY_RESET_SUCCESSFULLY = "Proficiency reset Successfully!";
	private static final String SIGNUM_ID = "SignumID";
    private static final String NO_DATA_FOUND="No Data Found!";
    private static final String KPI_VALUES_SAVED="KPI Values Of WF saved Successfully!";
    private static final String DUPLICATE_WORKFLOW_NAME = "Please enter other WorkFlow name and try again!!";

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);
	@Autowired
	private ApplicationConfigurations configurations;
    
    @Autowired
    private FlowChartDAO flowChartDao;
    @Autowired
    private ActivityMasterDAO activityMasterDAO;
    @Autowired
    private ToolsMasterService toolsMasterService;
    
     @Autowired
    private WorkOrderPlanDao workOrderPlanDao;
     
     @Autowired
    private ToolsMasterDAO toolsMasterDAO;
     
     @Autowired
     private OutlookAndEmailService emailService;
     
     @Autowired
     private MailUtil mailUtil;
     
     @Autowired
     private AppService appService;
     
     
     @Autowired
     private IsfCustomIdInsert isfCustomIdInsert;
     
     @Autowired
 	private RpaDAO rpaDAO;
     
     @Autowired
     private ActivityMasterService activityMasterService;
     
    @Autowired
 	private ValidationUtilityService validationUtilityService;
    
    @Autowired
	private ProjectDAO projectDao;
     
     private static final String MG ="Project/Search";
     
    public HashMap<String,Object> uploadFileForFlowChart(MultipartFile file, int projectID, int subActivityID, String createdBy,String workFlowName) throws IOException, SQLException {

        String fileName = file.getName();
        fileName = AppUtil.getFileNameWithTimestamp(fileName);
        String relativeFilePath = AppUtil.getRelativeFilePathForFlowChart(projectID, subActivityID);
        LOG.info("File Upload Start");
        this.uploadFile(file, relativeFilePath, fileName);
        String filePath = appService.getConfigUploadFilePath() + relativeFilePath + "/" + fileName;
        String csvFilePath = appService.getConfigUploadFilePath() + relativeFilePath + "/" + AppUtil.getFileNameWithTimestamp("flowchart") ;
        AppUtil.convertExcelToCSV(filePath, csvFilePath);
        appService.CsvBulkUploadNewGenWorkFlow(csvFilePath, "FlowChartCreationTmp", fileName);

        HashMap<String,Object> flowChartDefModel = null;
        try {
            flowChartDefModel = uploadExcel(fileName, projectID, subActivityID, createdBy,workFlowName);
        } catch (Exception e) {
            flowChartDefModel.put("Error",e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
           appService.dropTable(fileName);
        }
        return flowChartDefModel;
    }	

    @Transactional("transactionManager") 
    public HashMap<String,Object> uploadExcel(String fileName, int projectID, int subActivityID, String signumID,String workFlowName) {
        FlowChartDefModel flowChartDefModel = new FlowChartDefModel();
        HashMap<String,Object> data = new HashMap<>();
        HashMap<String,Object> response = new HashMap<>();
        ErrorBean err = new ErrorBean();
        List<ErrorBean> errorBean = new ArrayList<>();
        try {
            errorBean = this.flowChartDao.uploadExcel(fileName, projectID, subActivityID, signumID,workFlowName);
            if (errorBean.isEmpty()) {
                List<FlowChartStepModel> flowChartStepModel = this.flowChartDao.getFlowChartStepDetails(projectID, subActivityID);
                List<FlowChartStepModel> stepJSON = generateJSON(projectID,subActivityID, flowChartStepModel);
                List<FlowChartDependencyModel> dependencyModel = this.flowChartDao.getDependencyStep(subActivityID);
                List<FlowChartDependencyModel> linkJSON = generateLink(dependencyModel);
                flowChartDefModel = createFinalJSON(projectID, subActivityID,stepJSON,linkJSON);
                data.put("ErrorFlag",false);
                data.put("Success",flowChartDefModel);
                response.put("SuccessData",data);
            } else {
            	data.put("ErrorFlag",true);
                data.put("Error", errorBean);
                response.put("ErrorData",data);
            }
        } catch (Exception ex) {
            deleteFailedRecord(projectID, subActivityID,0);
            err.setErrorDescription("Error while saving the workFlow");
            err.setDetails(ex.getMessage());
            err.setHowToFix("Please check the file inputs and try again!!");
            errorBean.add(err);
            data.put("ErrorFlag",true);
            data.put("Error", errorBean);
            response.put("ErrorData",data);
            LOG.info("Error while saving the workFlow-> File Upload:" + ex.getMessage());
            ex.printStackTrace();
            return response;
        }
        return response;
    }
     private List<FlowChartStepModel> generateJSON(int projectID,int subActivityID, List<FlowChartStepModel> flowChartStepModel) {
        int x = 100;
        int y = 50;
        String json = "";
        String newJSON;
        List<FlowChartStepModel> stepJSON = new ArrayList<>();
        try {
            for (FlowChartStepModel flowModel : flowChartStepModel) {
                if (flowModel.getGraphicalRepresentation().equalsIgnoreCase("start")) {
                    json = FlowChartStepTemplate.START_STEP_JSON;
                    newJSON = updateJSON(projectID,subActivityID,json, flowModel,x, y, new HashMap<>(),new HashMap<>());
                    flowModel.setStepJSON(newJSON);
                } else if (flowModel.getGraphicalRepresentation().equalsIgnoreCase("operation")) {
                    Map<String, Object> taskModel = this.activityMasterDAO.getTaskDetailForSID(subActivityID, flowModel.getTaskOrParentTaskMapped().trim());
                    Map<String,String> toolData = this.activityMasterDAO.getToolDetailForWF(flowModel.getToolName().trim());
                    if (!taskModel.isEmpty() && taskModel.size()>0) {
                        if ((flowModel.getExecutionType().equalsIgnoreCase("Automated")) || (flowModel.getExecutionType().equalsIgnoreCase("Automatic"))) {
                            json = FlowChartStepTemplate.AUTOMATED_JSON_WITH_TASK;
                        } else {
                            json = FlowChartStepTemplate.OPERATION_STEP_JSON_WITH_TASK;
                        }
                        newJSON = updateJSON(projectID,subActivityID,json,flowModel,x, y,taskModel,toolData);
                        flowModel.setStepJSON(newJSON);
                    }
                } else if ((flowModel.getGraphicalRepresentation().equalsIgnoreCase("decision")) || (flowModel.getGraphicalRepresentation().equalsIgnoreCase("condition"))) {
                    json = FlowChartStepTemplate.DECISION_STEP_JSON;
                    newJSON = updateJSON(projectID,subActivityID,json, flowModel, x, y, new HashMap<>(),new HashMap<>());
                    flowModel.setStepJSON(newJSON);
                }else if(flowModel.getGraphicalRepresentation().equalsIgnoreCase("end") || flowModel.getGraphicalRepresentation().equalsIgnoreCase("stop")) {
                	 json = FlowChartStepTemplate.STOP_STEP_JSON;
                     newJSON = updateJSON(projectID,subActivityID,json, flowModel, x, y, new HashMap<>(),new HashMap<>());
                     flowModel.setStepJSON(newJSON);
                }
                x = x + 50;
                y = y + 50;
                stepJSON.add(flowModel);
            }
        } catch (Exception ex) {
            deleteFailedRecord(projectID,subActivityID,0);
            throw new ApplicationException(500, "Error While Generating JSON ..Eg:Tool/Task Not Mapped,Please validate the file" + ex.getMessage()+"\n");
        }          
        return stepJSON;
    }
    
    private String updateJSON(int projectID,int subActivityID,String json,FlowChartStepModel flowModel ,int x, int y, Map<String, Object> taskModel,Map<String,String> toolData) {
        try {
            String[] step = splitStepNameByWord(flowModel.getStepName());
            String newStep = "";
            String newTool = "";
            for (int i = 0; i < step.length; i++) {
                newStep += step[i] + "\\\\n";
            }
            if (!taskModel.isEmpty() && taskModel.size()>0) {
                String taskID = String.valueOf(taskModel.get("TaskID"));
                String taskName = String.valueOf(taskModel.get("Task"));
                String toolID = String.valueOf(toolData.get("ToolID"));
                String toolName = String.valueOf(toolData.get("ToolName"));
                String[] tools = splitStepNameByWord(toolName);
                for (String tool1 : tools) {
                    newTool += tool1 + "\\\\n";
                }
                json = json.replaceAll("%STEP_NAME%", newStep + "\\\\nTool Name:" + newTool);
                json = json.replaceAll("%STEP_VALUE%", flowModel.getStepName());
                json = json.replaceAll("%POSITION_X%", String.valueOf(x));
                json = json.replaceAll("%POSITION_Y%", String.valueOf(y));
                json = json.replaceAll("%STEP_ID%", String.valueOf(flowModel.getSubActivityFlowChartStepID()));
                json = json.replaceAll("%TASK_ID%", String.valueOf(taskID));
                json = json.replaceAll("%TASK_NAME%", taskName);
                json = json.replaceAll("%EXECUTION_TYPE%", flowModel.getExecutionType());
                json = json.replaceAll("%TOOL_ID%", String.valueOf(toolID));
                json = json.replaceAll("%TOOL_NAME%", String.valueOf(toolName));
                json = json.replaceAll("%RPATOOL_ID%", String.valueOf(0));
                json = json.replaceAll("%ACTION%", String.valueOf(taskID)+"@"+taskName);
                json = json.replaceAll("%TOOL_DATA%", String.valueOf(toolID)+"@"+String.valueOf(toolName));
                json = json.replaceAll("%RPA_DATA%", String.valueOf(0)+"@"+String.valueOf(0));
                json = json.replaceAll("%RESP%", flowModel.getResponsible());
                if(StringUtils.isNotEmpty(flowModel.getOutputUpload())) {
                	String outputUpload=flowModel.getOutputUpload().toUpperCase();
                	flowModel.setOutputUpload(outputUpload);
                }
                json = json.replaceAll("%OUTPUT_UPLOAD%", flowModel.getOutputUpload());
                
                if(StringUtils.isNotEmpty(flowModel.getCascadeInput())) {
                	String cascadeInput=titleCaseConversion(flowModel.getCascadeInput());
                	flowModel.setCascadeInput(cascadeInput);
                }
  
                json = json.replaceAll("%CASCADE_INPUT%", flowModel.getCascadeInput());
            } else {
                json = json.replaceAll("%STEP_NAME%", newStep);
                json = json.replaceAll("%POSITION_X%", String.valueOf(x));
                json = json.replaceAll("%POSITION_Y%", String.valueOf(y));
                json = json.replaceAll("%STEP_ID%", String.valueOf(flowModel.getSubActivityFlowChartStepID()));
            }
        } catch (Exception ex) {
            deleteFailedRecord(projectID,subActivityID,0);
            LOG.info("Error While Creating WorkFlow JSON:Tool/Task Not Mapped" + ex.getMessage());
            throw new ApplicationException(500, "Error While Creating WorkFlow JSON:Tool/Task Not Mapped :" + flowModel.getStepName());
        }
        return json;
    }
    

    private static String titleCaseConversion(String cascadeInput) {
    	 if (StringUtils.isBlank(cascadeInput)) {
             return "";
         }
  
         if (StringUtils.length(cascadeInput) == 1) {
             return cascadeInput.toUpperCase();
         }
  
         StringBuffer resultPlaceHolder = new StringBuffer(cascadeInput.length());
  
         Stream.of(cascadeInput.split(" ")).forEach(stringPart -> {
             char[] charArray = stringPart.toLowerCase().toCharArray();
             charArray[0] = Character.toUpperCase(charArray[0]);
             resultPlaceHolder.append(new String(charArray)).append(" ");
         });
  
         return StringUtils.trim(resultPlaceHolder.toString());
     }

	private List<FlowChartDependencyModel> generateLink(List<FlowChartDependencyModel> dependencyModel) {
         String jsonLink = "";
         List<FlowChartDependencyModel> linkModel = new ArrayList<>();
         for(FlowChartDependencyModel dependency : dependencyModel) {
             if ("".equals(dependency.getLinkText()) || dependency.getLinkText() == null) {
                 jsonLink = FlowChartStepTemplate.APP_LINK_JSON;
                 jsonLink = jsonLink.replaceAll("%SRC_STEP_ID%", "" + dependency.getSrcFlowChartStepID());
                 jsonLink = jsonLink.replaceAll("%TAR_STEP_ID%", "" + dependency.getDestFlowChartStepID());
                 jsonLink = jsonLink.replaceAll("%LINK_ID%", "l_" + dependency.getSubActivityFlowChartDepID());
             } else {
                 jsonLink = FlowChartStepTemplate.APP_LINK_JSON_WITH_LABLE;
                 jsonLink = jsonLink.replaceAll("%SRC_STEP_ID%", "" + dependency.getSrcFlowChartStepID());
                 jsonLink = jsonLink.replaceAll("%TAR_STEP_ID%", "" + dependency.getDestFlowChartStepID());
                 jsonLink = jsonLink.replaceAll("%LINK_ID%", "l_" + dependency.getSubActivityFlowChartDepID());
                 jsonLink = jsonLink.replaceAll("%LINK_TEXT%", dependency.getLinkText());
             }
             dependency.setLinkJson(jsonLink);
             linkModel.add(dependency);
        }
        return linkModel;
    }
    
    public FlowChartDefModel createFinalJSON(int projectID,int subActivityID ,List<FlowChartStepModel> stepJSON,List<FlowChartDependencyModel> linkJSON) {
        
        FlowChartDefModel flowChartDefModel = new FlowChartDefModel();
        String finalJSON = prepareFinalJSON(stepJSON, linkJSON);
        flowChartDefModel.setFlowChartJSON(finalJSON);
        flowChartDefModel.setSubActivityID(subActivityID);
        flowChartDefModel.setProjectID(projectID);
        
        return flowChartDefModel;
    }
    private String prepareFinalJSON(List<FlowChartStepModel> lstFlowChartStepModel, List<FlowChartDependencyModel> lstFlowChartDepModel) {
        String jsonValue ="{\n" +
                          "    \"cells\": [ \n";
        jsonValue = lstFlowChartStepModel.stream().map((stepModel) -> stepModel.getStepJSON() + ",").reduce(jsonValue, String::concat);
        jsonValue = lstFlowChartDepModel.stream().map((stepModel) -> stepModel.getLinkJson() + ",").reduce(jsonValue, String::concat);
        jsonValue = jsonValue.substring(0, jsonValue.length() - 1);
        jsonValue +="  ]\n" +
                    "}";
        return jsonValue;
    }

    @Transactional("transactionManager")
	public Map<String, Object> saveJSONFromUI(FlowChartSaveModel flowChartSaveModel) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> defData = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		List<ErrorBean> lstErr = new ArrayList<>();
		ErrorBean errBean = new ErrorBean();
		FlowChartDefModel flowModel = new FlowChartDefModel();
		String type = "";
		boolean isUpdate = false;
		try {
			Integer curWFID = 0;
		    int newVersion = 1;
			

				if (flowChartSaveModel.getWfOwner() == null || flowChartSaveModel.getWfOwner().equalsIgnoreCase("Null")
						|| flowChartSaveModel.getWfOwner().equalsIgnoreCase(" ")) {
					flowChartSaveModel.setWfOwner(flowChartSaveModel.getSignumID());
				}
				if (flowChartSaveModel.getFtrValue() == null
						|| flowChartSaveModel.getFtrValue().equalsIgnoreCase("NULL")) {
					flowChartSaveModel.setFtrValue(AppConstants.NA);
				}
			
				if(StringUtils.equalsIgnoreCase(AppConstants.ASSESSED,flowChartSaveModel.getWfType() ) || StringUtils.equalsIgnoreCase(AppConstants.EXPERIENCED,flowChartSaveModel.getWfType() ) ) {
					type = AppConstants.PROJECTDEFINED;
				}
				
				int oldDefID = flowChartSaveModel.getFlowChartDefID();
				int oldVersionNumber = flowChartSaveModel.getVersionNumber();
				//Copy LOE value from parent WF
				if(flowChartSaveModel.getLoeMeasurementCriterionID() == null) {
					Integer parentLoeMeasurementCriterionID= flowChartDao.getParentLoeMeasurementCriterionID(oldDefID);
					flowChartSaveModel.setLoeMeasurementCriterionID(parentLoeMeasurementCriterionID);
				}
				
				if(StringUtils.equalsIgnoreCase(AppConstants.CREATE_NEW,flowChartSaveModel.getSaveMode() )) {
					if (flowChartDao.getWorkflow(flowChartSaveModel.getProjectID(),
							flowChartSaveModel.getWfName(), flowChartSaveModel.getSubActivityID())) {
						errBean.setErrorDescription(AppConstants.SAME_WORKFLOWNAME);
						errBean.setDetails("Name " + flowChartSaveModel.getWfName() + " already exists");
						errBean.setHowToFix(DUPLICATE_WORKFLOW_NAME);
						lstErr.add(errBean);
						data.put(AppConstants.ERROR_FLAG, true);
						data.put(AppConstants.ERROR, lstErr);
						response.put(AppConstants.ERROR_DATA, data);
						return response;
					}
				}
				if(StringUtils.equalsIgnoreCase(AppConstants.UPDATE_EXISTING,flowChartSaveModel.getSaveMode() )) {
					boolean wfdata = this.flowChartDao.getWorkflowForWFID(
							flowChartSaveModel.getProjectID(), flowChartSaveModel.getWfName(),
							flowChartSaveModel.getSubActivityID(), flowChartSaveModel.getWorkFlowID());
					if (flowChartDao.getWorkflow(flowChartSaveModel.getProjectID(),
							flowChartSaveModel.getWfName(), flowChartSaveModel.getSubActivityID()) && !wfdata) {
						errBean.setErrorDescription(AppConstants.SAME_WORKFLOWNAME);
						errBean.setDetails("Name " + flowChartSaveModel.getWfName() + " already exists");
						errBean.setHowToFix(DUPLICATE_WORKFLOW_NAME);
						lstErr.add(errBean);
						data.put(AppConstants.ERROR_FLAG, true);
						data.put(AppConstants.ERROR, lstErr);
						response.put(AppConstants.ERROR_DATA, data);
						return response;
					}
				}
				
				//validate the WF and Instruction URL
				Map<String, Object> validURLResponse = validateWFInstructionURL(flowChartSaveModel);
				if (validURLResponse.containsKey(AppConstants.ERROR_MSG)) {
					response.put(AppConstants.ERROR_MSG, validURLResponse.get(AppConstants.ERROR_MSG));
					return response;
				}

			if (!flowChartSaveModel.isUpdate()) {
					newVersion = flowChartDao.getWFVersioNo(flowChartSaveModel.getWorkFlowID())+1;
				
					
					if (flowChartSaveModel.getBotapprovalPage() != null
							&& "BOTAPPROVALVIEW".equals(flowChartSaveModel.getBotapprovalPage())) {
						if (!parseJsonToGetRequiredParam(flowChartSaveModel)) {
							response.put(AppConstants.ERROR_FLAG, false);
							response.put(AppConstants.ERROR_MSG, "Mandatory to use BOT ID : " + flowChartSaveModel.getBotId()
									+ "in the current Work Flow.");
							return response;
						}
						
					}
					flowModel.setWorkFlowName(flowChartSaveModel.getWfName());
					flowModel.setVersionNumber(newVersion);
					flowModel.setWFID(flowChartSaveModel.getWorkFlowID());
					flowModel.setParentFlowChartDefID(flowChartSaveModel.getFlowChartDefID());
					flowModel.setWfEditReason(flowChartSaveModel.getWfEditReason());
					
				
					isUpdate = true;
					this.flowChartDao.updateActiveStatus(flowChartSaveModel.getProjectID(),
							flowChartSaveModel.getSubActivityID(), flowChartSaveModel.getFlowChartDefID(),
							flowChartSaveModel.getSignumID());
					
					updateWFVersion(flowChartSaveModel.getProjectID(), flowChartSaveModel.getSubActivityID(),
							flowChartSaveModel.getFlowChartDefID(), flowChartSaveModel.getVersionNumber(),
							newVersion,flowChartSaveModel.getWfName());
			
				} else {
					
					
					if (flowChartSaveModel.getFlowChartDefID() != 0) {
						this.flowChartDao.updateActiveStatus(flowChartSaveModel.getProjectID(),
								flowChartSaveModel.getSubActivityID(), flowChartSaveModel.getFlowChartDefID(),
								flowChartSaveModel.getSignumID());
					
					}
					
					
					if(StringUtils.equalsIgnoreCase(AppConstants.ASSESSED,flowChartSaveModel.getWfType() ) || StringUtils.equalsIgnoreCase(AppConstants.EXPERIENCED,flowChartSaveModel.getWfType() ) ) {
							curWFID =this.flowChartDao.getMaxWFID() + 1;
					}
					
					flowModel.setWFID(curWFID);
					
					flowModel.setVersionNumber(newVersion);
					flowModel.setParentFlowChartDefID(0);
					newVersion++;
				}
				
				flowModel.setExperiencedFlow(flowChartSaveModel.isExperiencedFlow());
				flowModel.setProjectID(flowChartSaveModel.getProjectID());
				flowModel.setSubActivityID(flowChartSaveModel.getSubActivityID());
				flowModel.setFlowChartJSON(flowChartSaveModel.getFlowchartJSON());
				flowModel.setCreatedBy(flowChartSaveModel.getSignumID());
				flowModel.setWorkFlowName(flowChartSaveModel.getWfName());
				flowModel.setNeNeeded(flowChartSaveModel.isNeMandatory());

				flowModel.setWfOwner(flowChartSaveModel.getWfOwner());
				flowModel.setType(type);
				flowModel.setSlaHours(flowChartSaveModel.getSlaHours());
				flowModel.setFtrValue(flowChartSaveModel.getFtrValue());
				isUpdate = false;
				
				
				Map<String, Object> parseJsonResponse = parseJsonAndValidateCascade(
						flowChartSaveModel.getFlowchartJSON(), flowChartSaveModel.getFlowChartDefID(), flowModel,
						isUpdate, oldDefID, flowChartSaveModel.getVersionNumber());
				if (parseJsonResponse.containsKey("ErrorData")) {
					response.put("ErrorData", parseJsonResponse.get("ErrorData"));
					return response;
				}
				flowModel.setEnableField(flowChartSaveModel.isEnableField());
				flowModel.setLoeMeasurementCriterionID(flowChartSaveModel.getLoeMeasurementCriterionID());
				this.flowChartDao.insertJSONFromUI(flowModel);
				int subActivityFlowchartDefIdAndInstanceId=isfCustomIdInsert.generateCustomId(flowModel.getSubActivityFlowChartDefID());
                flowModel.setSubActivityFlowChartDefID(subActivityFlowchartDefIdAndInstanceId);
				flowChartSaveModel.setFlowChartDefID(flowModel.getSubActivityFlowChartDefID());
				flowChartSaveModel.setFlowChartDefID(subActivityFlowchartDefIdAndInstanceId);
				flowChartSaveModel.setWorkFlowID(flowModel.getWFID());
				flowChartSaveModel.setVersionNumber(flowModel.getVersionNumber());
		

				parseJsonAndUpdateStepsForWFDefinition(
						flowChartSaveModel.getFlowchartJSON(), flowChartSaveModel.getFlowChartDefID(), flowModel,
						isUpdate, oldDefID, oldVersionNumber,flowModel.getSubActivityFlowChartDefID(),
						flowModel.getProjectID()+AppConstants.UNDERSCORE+flowModel.getSubActivityID()+AppConstants.UNDERSCORE+flowModel.getCreatedBy(),
						flowModel.getCreatedBy());
				
				
				
				if(StringUtils.equalsIgnoreCase(AppConstants.CREATE_NEW,flowChartSaveModel.getSaveMode() )) {
				 addWFInstruction(flowChartSaveModel);
				}
				else if(StringUtils.equalsIgnoreCase(AppConstants.UPDATE_EXISTING,flowChartSaveModel.getSaveMode() )) {
		
				addWFStepInstructionOnNewDEfID(flowChartSaveModel);
				}
			
				List<KpiModel> kpiModel =flowChartSaveModel.getLstKpiModel();
				
				saveKPIValuesOfWF(kpiModel,flowChartSaveModel,oldDefID);
				
	
				
				if(StringUtils.equalsIgnoreCase(AppConstants.ASSESSED,flowChartSaveModel.getWfType() ) || StringUtils.equalsIgnoreCase(AppConstants.EXPERIENCED,flowChartSaveModel.getWfType() ) ) {
				
					defData.put("FlowChartJSON", flowChartSaveModel.getFlowchartJSON());
					defData.put("OldDefID", oldDefID);
					defData.put("OldStepId", flowChartSaveModel.getBotForStepID());
					defData.put("FlowChartDefID", flowChartSaveModel.getFlowChartDefID());
					defData.put("Version", flowModel.getVersionNumber());
				
					if (flowChartSaveModel.isFutureWorkOrdersUpdate()) {
							workOrderPlanDao.updateWoFcDefIdForAssignedWo(flowChartSaveModel.getFlowChartDefID(),
									flowModel.getVersionNumber(), flowChartSaveModel.getProjectID(),
									flowChartSaveModel.getSubActivityID(), flowChartSaveModel.getWfName(),flowChartSaveModel.getWorkFlowID());
							
							if((StringUtils.isNoneBlank(flowChartSaveModel.getBotapprovalPage()) && StringUtils.equalsIgnoreCase(flowChartSaveModel.getBotapprovalPage(), AppConstants.BOT_APPROVAL_PAGE))
									|| (StringUtils.equalsIgnoreCase(flowChartSaveModel.getSaveMode(),AppConstants.UPDATE_EXISTING) && 
									!StringUtils.equalsIgnoreCase(flowChartSaveModel.getBotapprovalPage(), AppConstants.BOT_APPROVAL_PAGE))) {
								workOrderPlanDao.updateWoAutoSense( flowChartSaveModel.getProjectID(),
										flowChartSaveModel.getSubActivityID(),flowChartSaveModel.getFlowChartDefID());
								//workOrderPlanDao.updateDefIdForAssignWo(oldDefID);	
							}
					}
				} 
			
				response.put(AppConstants.SUCCESS, defData);
			
			callSendMail(flowChartSaveModel);	
		
		}catch (Exception ex) {
			LOG.info("Error in saveJSONfromUI:" + ex.getMessage());
			throw new ApplicationException(500, ex.getMessage());
			
		}
		return response;
	}

	
 
	private Response<Void> addWFStepInstructionOnNewDEfID(FlowChartSaveModel flowChartSaveModel) {
		Response<Void> response = new Response<Void>();
		try {
		
			List<WFStepInstructionModel> wFInstruction =flowChartSaveModel.getLstWFStepInstructionModel();
			if (CollectionUtils.isNotEmpty(flowChartSaveModel.getLstWFStepInstructionModel())) {
		    for(WFStepInstructionModel wfStepInstruction:wFInstruction)
			{
				if(StringUtils.isNotEmpty(wfStepInstruction.getStepID())) {
				Integer fcStepDetailsID= this.flowChartDao.getFcStepDeatilsID(wfStepInstruction.getStepID(),flowChartSaveModel.getFlowChartDefID());
				if(fcStepDetailsID!=0) {
				wfStepInstruction.setFcStepDetailsID(fcStepDetailsID);
				}
				}
				wfStepInstruction.setCreatedBy(flowChartSaveModel.getWfOwner());
				wfStepInstruction.setModifiedBy(flowChartSaveModel.getWfOwner());
				if(!wfStepInstruction.isDeleted()) {
					wfStepInstruction.setFlowChartDefID(flowChartSaveModel.getFlowChartDefID());
					if(StringUtils.isNotEmpty(wfStepInstruction.getStepID()) && wfStepInstruction.getFcStepDetailsID()!=0
							|| StringUtils.isEmpty(wfStepInstruction.getStepID()) && wfStepInstruction.getFcStepDetailsID()==0 ) {
				
					addInstructionURL(wfStepInstruction);
					}
				}
			}
		}
			}
		
		catch(Exception e) {
			response.addFormMessage(e.getMessage());
			throw e;
		}
		return response;
	}

    
    private void callSendMail(FlowChartSaveModel flowChartSaveModel) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					sendMail(flowChartSaveModel);
				} catch (Exception e) {
					LOG.debug(String.format("Exception thrown  %s", e.getMessage()));
				}

			}
		}).start();

	}
  private Map<String, Object> validateWFInstructionURL(FlowChartSaveModel flowChartSaveModel) {
    	
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<>();
		HashMap<String,Object> data = new HashMap<>();
		if (CollectionUtils.isNotEmpty(flowChartSaveModel.getLstWFStepInstructionModel())) {
		for (int i = 0; i < flowChartSaveModel.getLstWFStepInstructionModel().size(); i++) {
			
			map = activityMasterService.isValidTransactionalData(
					flowChartSaveModel.getLstWFStepInstructionModel().get(i).getUrlLink(), flowChartSaveModel.getProjectID());
			if (!(boolean) map.get(AppConstants.RESULT)) {
				data.put("Given URL is not present in Global URL list or not locally configured with ProjectID :",flowChartSaveModel.getLstWFStepInstructionModel().get(i).getUrlLink() );
				response.put("ErrorMsg", data);
				break;
			}
		}
		}
		return response;
		
    }
    


	private Response<Void> addWFInstruction(FlowChartSaveModel flowChartSaveModel) {
		
		Response<Void> response = new Response<Void>();
		
		try {
			
			List<WFStepInstructionModel> wFInstruction =flowChartSaveModel.getLstWFStepInstructionModel();
			if (CollectionUtils.isNotEmpty(flowChartSaveModel.getLstWFStepInstructionModel())) {
			
			for(WFStepInstructionModel wfStepInstruction:wFInstruction)
			{
				if(StringUtils.isNotEmpty(wfStepInstruction.getStepID())  && wfStepInstruction.getFlowChartDefID()==0) {
				int fcStepDetailsID= this.flowChartDao.getFcStepDeatilsID(wfStepInstruction.getStepID(),flowChartSaveModel.getFlowChartDefID());
				if(fcStepDetailsID!=0) {
				wfStepInstruction.setFcStepDetailsID(fcStepDetailsID);
				}
				}
				
				wfStepInstruction.setCreatedBy(flowChartSaveModel.getWfOwner());
				wfStepInstruction.setModifiedBy(flowChartSaveModel.getWfOwner());
				if(wfStepInstruction.getFlowChartDefID()==0) {
					wfStepInstruction.setFlowChartDefID(flowChartSaveModel.getFlowChartDefID());
					if(StringUtils.isNotEmpty(wfStepInstruction.getStepID()) && wfStepInstruction.getFcStepDetailsID()!=0
							|| StringUtils.isEmpty(wfStepInstruction.getStepID()) && wfStepInstruction.getFcStepDetailsID()==0 ) {
					addInstructionURL(wfStepInstruction);
					}
				}
			
			}
		}
	}
		catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
			
		return response;
}
	
	

	private Response<Void> addInstructionURL(WFStepInstructionModel wfStepInstructionModel) {
		Response<Void> response = new Response<Void>();
		try {
		if (StringUtils.isEmpty(wfStepInstructionModel.getInstructionType())){
			if (StringUtils.isNotEmpty(wfStepInstructionModel.getStepID())){
				wfStepInstructionModel.setInstructionType(AppConstants.INSTRUCTION_ON_STEP);
				}
			else {
				wfStepInstructionModel.setInstructionType(AppConstants.INSTRUCTION_ON_WF);
				}
			}
		      flowChartDao.addInstructionURL(wfStepInstructionModel);
			}
		
		catch(Exception e) {
		e.printStackTrace();
	    throw e;
		}
		
		return response;
	
	}

	private boolean parseJsonToGetRequiredParam(FlowChartSaveModel flowChartSaveModel) throws Exception {

		JSONObject jObject = new JSONObject(flowChartSaveModel.getFlowchartJSON());
		JSONArray arr = jObject.getJSONArray("cells");
		boolean isRpaFound = false;
		for (int i = 0; i < arr.length(); i++) {
			String rpaID = null;
			if (arr.getJSONObject(i).has("attrs") && arr.getJSONObject(i).getJSONObject("attrs").has("task")) {
					if (String.valueOf(arr.getJSONObject(i)
							.getJSONObject("attrs").getJSONObject("tool")
							.get("RPAID")) == null
							|| String
									.valueOf(
											arr.getJSONObject(i)
													.getJSONObject("attrs")
													.getJSONObject("tool")
													.get("RPAID")).equals("")
							|| String
									.valueOf(
											arr.getJSONObject(i)
													.getJSONObject("attrs")
													.getJSONObject("tool")
													.get("RPAID")).equals(
											"%RPATOOL_ID%")) {
						rpaID = "0";
					} else {
						rpaID = String.valueOf(arr.getJSONObject(i)
								.getJSONObject("attrs").getJSONObject("tool")
								.get("RPAID"));
					}
					if (Integer.parseInt(rpaID.trim()) ==  flowChartSaveModel.getBotId()){
						isRpaFound = true;
						break;
					}
			}
		}
		return isRpaFound;
	}
   
	private void sendMail(FlowChartSaveModel saveModel) {
		Map<String, Object> placeHolder = new HashMap<>();
		//for (FlowChartSaveModel model : saveModel) {
			try {
				if (!saveModel.isUpdate()) {
					placeHolder = enrichMailforWorkFlow(saveModel, AppConstants.NOTIFICATION_UPDATE_WORKFLOW);
					placeHolder.put("pageLink",MG);
					emailService.sendMail(AppConstants.NOTIFICATION_UPDATE_WORKFLOW,placeHolder);
				} else {
					placeHolder = enrichMailforWorkFlow(saveModel, AppConstants.NOTIFICATION_CREATE_WORKFLOW);
					placeHolder.put("pageLink",MG);
					emailService.sendMail(AppConstants.NOTIFICATION_CREATE_WORKFLOW,placeHolder);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//break;
		//}
	}

	private Map<String, Object> enrichMailforWorkFlow(FlowChartSaveModel saveModel,String notificationUpdateWorkflow) {
		Map<String,Object> data=new HashMap<String, Object>();

    	data.put(AppConstants.CURRENT_USER,saveModel.getSignumID());
    	data.put(AppConstants.PROJECT_ID, saveModel.getProjectID());
    	data.put("projectID", saveModel.getProjectID());
    	data.put("wFName", saveModel.getWfName());
    	data.put("details", flowChartDao.getAdditionalInfoOfWorkFlow(saveModel.getSubActivityID()));
    	String toEmails="";
    	EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(saveModel.getSignumID());
    	toEmails=eDetails.getEmployeeEmailId();
    	if(!"".equals(toEmails)){
    		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, toEmails);
    	}
    	return data;
	}

     @Transactional("transactionManager")
	private Map<String, Object> parseJsonAndUpdateStepsForWFDefinition(String wFJson, int subActFCDefID, FlowChartDefModel flowModel,boolean fileBased, int oldDefID, int oldVersionNo,
			int subActivityFlowChartDefID, String projectIDSubactivityIDLoggedInSignum, String createdBy) throws Exception {
		Map<String, Object> response = new HashMap<>();
		try {
			FlowChartPopulateDataModel flowChartdataModel=new FlowChartPopulateDataModel();
        	List<ErrorBean> lstErr = new ArrayList<>();
        	ErrorBean errBean = new ErrorBean();
        	HashMap<String,Object> data = new HashMap<>();
        	
            JSONObject jObject = new JSONObject(wFJson);
            JSONArray arr = jObject.getJSONArray("cells");
            
            for (int i = 0; i < arr.length(); i++) {
                String stepId = null;
                String stepName = null;
                String taskId = null;
                String taskName = null;
                String executionType = null;
                String toolId = null;
                String reason = null;
                String sourceID = null;
                String targetID = null;
                String masterTask = null;
                String rpaID = null;
                String attrType = null;
                String wiid= null;
                String outputUpload = "YES";
                String cascadeInput = "NO";
                String viewMode=null;
                JSONObject types = (JSONObject) arr.get(i);
                 if("ericsson.Manual".equalsIgnoreCase((String) types.get("type")) ){
          	      viewMode = (String) types.get("viewMode");
          	      
                  }
                if("ericsson.Manual".equalsIgnoreCase((String) types.get("type")) 
                || "ericsson.Automatic".equalsIgnoreCase((String) types.get("type"))){
                	      attrType = "bodyText";
                
                	      
                }
               
                else if("ericsson.StartStep".equalsIgnoreCase((String) types.get("type")) 
                       || "ericsson.EndStep".equalsIgnoreCase((String) types.get("type"))) {
                		  attrType = "text";
                }else if("ericsson.Decision".equalsIgnoreCase((String) types.get("type"))){
                	      attrType = "label";
                }
                
                if (arr.getJSONObject(i).has("id")) {
                    stepId = arr.getJSONObject(i).getString("id");
                }
                if (arr.getJSONObject(i).has("attrs")) {
                    if (arr.getJSONObject(i).getJSONObject("attrs").has(attrType)) {
                        String step = arr.getJSONObject(i).getJSONObject("attrs").getJSONObject(attrType).getString("text");
                        String part[] = step.split("\n\n");
                        stepName = part[0];
                    }
                    if(types.has("outputUpload")) {
                    	outputUpload = types.getString("outputUpload");
                    }
                    // CASCADE
                    if(types.has("cascadeInput")) {
                    	cascadeInput = types.getString("cascadeInput");
                    	if(cascadeInput.isEmpty() || cascadeInput.equals(null)) {
                    		cascadeInput= "NO";
                    	}
                    }
                    if (arr.getJSONObject(i).getJSONObject("attrs").has("task")) {
                        taskId = arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").getString("taskID");
						if (arr.getJSONObject(i).getJSONObject("attrs").has("workInstruction") && arr.getJSONObject(i).getJSONObject("attrs")
								.getJSONObject("workInstruction").has("WIID") ) {
							wiid = String.valueOf(arr.getJSONObject(i).getJSONObject("attrs")
									.getJSONObject("workInstruction").get("WIID"));
						} 
                        taskName = arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").getString("taskName");
                        executionType = arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").getString("executionType");
                        toolId = String.valueOf(arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").get("toolID"));
                        if(arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").has("reason")) {
                        	reason = arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").getString("reason");	
                        }
                        
                        if(String.valueOf(arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("tool").get("RPAID"))== null
                        || String.valueOf(arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("tool").get("RPAID")).equals("")
                        || String.valueOf(arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("tool").get("RPAID")).equals("%RPATOOL_ID%")){
                            rpaID = "0";
                        }else{
                            rpaID = String.valueOf(arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("tool").get("RPAID"));
                        }
                        if (StringUtils.equalsIgnoreCase(reason, "%REASON%")) {
                            reason = "NULL";
                        }
                        // CASCADE
                        if(cascadeInput.equalsIgnoreCase("YES") && rpaID.trim().length()>0 ) {
                        	HashMap<String, Object> botData= rpaDAO.getRPAIsRunOnServer(Integer.parseInt(rpaID));
                        	if(null != botData) {
                        		if(!botData.get("isRunOnServer").equals(1)) {
                        			errBean.setErrorDescription("Cascade Step Mapping");
                        			errBean.setDetails("Cascade step can only be mapped to Server BOTS!!");
                        			errBean.setHowToFix("Update RpaId "+rpaID+" with a server BOT");
                        			lstErr.add(errBean);
                        		}
                        	}else {
                        		errBean.setErrorDescription("Cascade Step Mapping");
                        		errBean.setDetails("Cascade step can only be mapped to Server BOTS!!");
                        		errBean.setHowToFix("Update RpaId "+rpaID+" with a server BOT");
                        		lstErr.add(errBean);
                        	}
                        }
                        try {
                            masterTask = flowChartDao.getMasterTask(Integer.parseInt(taskId), flowModel.getSubActivityID());
                            if (toolId == null || "0".equals(toolId)) {
                                int taskID = Integer.parseInt(taskId);
                                int tool = toolsMasterDAO.getToolsDetails(taskID, flowModel.getSubActivityID());
                                toolId = String.valueOf(tool);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                        if(lstErr.size()>0) {
                        	data.put("ErrorFlag",true);
                            data.put("Error", lstErr);
                            response.put("ErrorData",data);
                            return response;
                        }

                        flowChartdataModel= createModelForpopulateFlowChartStepDetails(flowChartdataModel,subActFCDefID, stepId.trim(), stepName.trim(), taskId.trim(), taskName.trim(), executionType.trim(),
                                toolId.trim(), StringUtils.trim(reason), flowModel.getVersionNumber(), masterTask, (String) types.get("type"),rpaID,fileBased,wiid, oldDefID, oldVersionNo, outputUpload,cascadeInput,
                                projectIDSubactivityIDLoggedInSignum,subActivityFlowChartDefID,createdBy,flowModel.getType(),viewMode,flowModel.getWFID(), flowModel.isExperiencedFlow());
                        populateFlowChartStepDetails(flowChartdataModel);
                        
                    } else {
                        if (!"app.Link".equalsIgnoreCase((String) types.get("type")) ){
                        	flowChartdataModel= createModelForpopulateFlowChartStepDetails(flowChartdataModel,subActFCDefID, stepId.trim(), stepName.trim(), "0", "NULL", "Manual",
                                    "0","NULL", flowModel.getVersionNumber(), "NULL", (String) types.get("type"),"0",fileBased,"0", oldDefID, oldVersionNo, outputUpload,cascadeInput,
                                    projectIDSubactivityIDLoggedInSignum,subActivityFlowChartDefID,createdBy,flowModel.getType(),viewMode, flowModel.getWFID(), flowModel.isExperiencedFlow());
                            populateFlowChartStepDetails(flowChartdataModel);
                       }
                    }
                }
                if ("app.Link".equalsIgnoreCase((String) types.get("type"))) {
                    sourceID = arr.getJSONObject(i).getJSONObject("source").getString("id");
                    targetID = arr.getJSONObject(i).getJSONObject("target").getString("id");
                    boolean checkIFLinkExists = this.flowChartDao.checkIFLinkDataExists(subActFCDefID, sourceID, targetID);
                    if(!checkIFLinkExists){
                       this.flowChartDao.insertFlowchartStepLinkDetails(subActFCDefID, sourceID.trim(), targetID.trim());
                    }
                }
                
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return response;
    }
    
    private FlowChartPopulateDataModel createModelForpopulateFlowChartStepDetails(FlowChartPopulateDataModel flowChartdataModel,
			int subActFCDefID, String stepId, String stepName, String taskId, String taskName, String executionType, String toolId,
			String  reason, int versionNumber, String masterTask, String type, String rpaID, boolean fileBased,
			String wiid, int oldDefID, int oldVersionNo, String outputUpload, String cascadeInput, String projectIDSubactivityIDLoggedInSignum, int subActivityFlowChartDefID, String loggedInSignum, String expertType, String viewMode, int wFID, boolean experiencedView) {
    	flowChartdataModel.setFlowChartDefId(subActFCDefID);
    	flowChartdataModel.setStepId(stepId);
    	flowChartdataModel.setStepName(stepName);
    	flowChartdataModel.setTaskId(taskId);
    	flowChartdataModel.setTaskName(taskName);
    	flowChartdataModel.setExecutionType(executionType);
    	flowChartdataModel.setToolId(toolId);
    	flowChartdataModel.setReason(reason);
    	flowChartdataModel.setVersionNumber(versionNumber);
    	flowChartdataModel.setMasterTask(masterTask);
    	flowChartdataModel.setType(type);
    	flowChartdataModel.setRpaId(rpaID);
    	flowChartdataModel.setFileBased(fileBased);
    	flowChartdataModel.setWiId(wiid);
    	flowChartdataModel.setOldDefID(oldDefID);
    	flowChartdataModel.setOldVersionNo(oldVersionNo);
    	flowChartdataModel.setOutputUpload(outputUpload);
    	flowChartdataModel.setCascadeInput(cascadeInput);
    	flowChartdataModel.setProjectIDSubactivityIDLoggedInSignum(projectIDSubactivityIDLoggedInSignum);
    	flowChartdataModel.setSubActivityFlowchartStepDefId(subActivityFlowChartDefID);
    	flowChartdataModel.setLoggedInSignum(loggedInSignum);
    	flowChartdataModel.setExperiencedView(experiencedView);
    	
    	flowChartdataModel.setViewMode(viewMode);
    	flowChartdataModel.setwFID(wFID);
    	return flowChartdataModel;
		
	}

		private void populateFlowChartStepDetails(FlowChartPopulateDataModel flowChartdataModel) {
			LOG.info("=======================start of populateFlowChartStepDetails====================================================");
			LOG.info("flowChartDefId is ------- "+flowChartdataModel.getFlowChartDefId());
			LOG.info("subActivityFlowchartStepDefId is ------- "+flowChartdataModel.getSubActivityFlowchartStepDefId());
			LOG.info("=======================end of populateFlowChartStepDetails====================================================");
    	boolean checkIFDataExists = this.flowChartDao.checkIFStepDataExists(flowChartdataModel.getFlowChartDefId(), flowChartdataModel.getStepId(), 
    			flowChartdataModel.getStepName(),
                Integer.parseInt(flowChartdataModel.getTaskId()), flowChartdataModel.getVersionNumber());
    	int fcStepDetailsId=0;
        if (checkIFDataExists) {
                this.flowChartDao.updateFlowChartStepDetailsValue(flowChartdataModel.getFlowChartDefId(), flowChartdataModel.getStepId(), 
                		flowChartdataModel.getStepName(), flowChartdataModel.getTaskId(), flowChartdataModel.getTaskName(), flowChartdataModel.getExecutionType(),
                		flowChartdataModel.getToolId(), flowChartdataModel.getReason(), flowChartdataModel.getVersionNumber(), flowChartdataModel.getMasterTask(),
                		flowChartdataModel.getRpaId(),flowChartdataModel.getWiId(),flowChartdataModel.getOutputUpload(),flowChartdataModel.getCascadeInput());
        } else {
        	Map<String, Object> stepData = this.flowChartDao.getStepExistingData(flowChartdataModel.getOldDefID(), flowChartdataModel.getStepId(), flowChartdataModel.getOldVersionNo());
            this.flowChartDao.deleteFlowchartStepDetails(flowChartdataModel.getFlowChartDefId(), flowChartdataModel.getStepId(), Integer.parseInt(flowChartdataModel.getTaskId()), 
            		flowChartdataModel.getVersionNumber());
            this.flowChartDao.insertInFlowChartStepDetails1(flowChartdataModel,stepData);
            fcStepDetailsId=flowChartdataModel.getfCStepDetailsID();
            fcStepDetailsId=isfCustomIdInsert.generateCustomId(fcStepDetailsId);
        }
        if(fcStepDetailsId!=0 && !StringUtils.equalsIgnoreCase(flowChartdataModel.getType(), AppConstants.START_TYPE)
            && !StringUtils.equalsIgnoreCase(flowChartdataModel.getType(), AppConstants.END_TYPE)) {
        	flowChartdataModel.setfCStepDetailsID(fcStepDetailsId);
        	this.flowChartDao.insertWorkflowStepAutoSenseRule(flowChartdataModel);
        }
    
        if(StringUtils.equalsIgnoreCase(flowChartdataModel.getType(), AppConstants.END_TYPE)) {
    	   StringBuilder customStepId=new StringBuilder();
    	   customStepId.append(flowChartdataModel.getwFID()).append("-").
    	            append(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    	   String disabledExcutionType = "ManualDisabled";
    	   this.flowChartDao.insertDummyDataOnStepDetails(customStepId.toString(),AppConstants.ERICSSON_MANUAL_DISABLED,
    			   flowChartdataModel.getLoggedInSignum(),flowChartdataModel.getViewMode(),customStepId.append("-DisabledManual").toString(),
    			   flowChartdataModel.getSubActivityFlowchartStepDefId(),disabledExcutionType,flowChartdataModel.getVersionNumber() );
       }
    }
   
	public List<Map<String, Object>> viewFlowChartForSubActivity(int projectID, int subActivityID, int woID,
		int wfVersion, boolean experiencedMode, int wfid) {
		List<Map<String, Object>> wfJSONData = new ArrayList<>();
		List<Map<String, Object>> workFlow = new ArrayList<>();
		
		workFlow = this.getWFAndJSON(projectID, subActivityID, woID, wfVersion, experiencedMode,wfid);
		
		int subActivityFlowChartDefID=0;
		if (workFlow != null && !workFlow.isEmpty()) {
			for (Map<String, Object> wfData : workFlow) {
				Map<String, Object> data = new HashMap<>();
				String wfEditReason=this.flowChartDao.getWFEditReason(Integer.parseInt(wfData.get("SubActivityFlowChartDefID").toString()));
				wfData.put("WFEditReason", wfEditReason);
				subActivityFlowChartDefID=Integer.parseInt(wfData.get("SubActivityFlowChartDefID").toString());
				String json = String.valueOf(wfData.get("FlowChartJSON"));
				List<Map<String, Object>> stepData = this.flowChartDao.getStepDetails(Integer.parseInt(wfData.get("SubActivityFlowChartDefID").toString()));
				
				JSONObject obj = new JSONObject(json);
				JSONArray cells = (JSONArray) obj.get("cells");
				String attrType = null;
				for (int i = 0; i < cells.length(); i++) {
					JSONObject types = (JSONObject) cells.get(i);
					if (!"app.Link".equalsIgnoreCase((String) types.get("type"))
					&& !"ericsson.StartStep".equalsIgnoreCase((String) types.get("type"))
					&& !"ericsson.EndStep".equalsIgnoreCase((String) types.get("type"))) {

						if ("ericsson.Manual".equalsIgnoreCase((String) types.get("type"))
						|| "ericsson.Automatic".equalsIgnoreCase((String) types.get("type"))) {
							attrType = "bodyText";
						} else if ("ericsson.Decision".equalsIgnoreCase((String) types.get("type"))) {
							attrType = "label";
						}

						if (!stepData.isEmpty()) {
							for (Map<String, Object> stepModel : stepData) {
								    String stepDetails = (String) types.get("id");
	                                if (stepDetails.equals(String.valueOf(stepModel.get("FlowChartStepID")))) {
									String toolID = String.valueOf(stepModel.get("ToolID"));
									String toolName = String.valueOf(stepModel.get("ToolName"));
									String rpaID = String.valueOf(stepModel.get("RpaID"));
									String rpaName = String.valueOf(stepModel.get("BOTName"));
									String avgEstdEffort = String.valueOf(stepModel.get("AvgEstdEffort"));
									String outputUpload = String.valueOf(stepModel.get("OutputUpload"));

									JSONObject attrs = (JSONObject) types.get("attrs");
									if (attrs.has("task")) {
										JSONObject task = (JSONObject) attrs.get("task");
										Integer taskid = Integer.parseInt(task.get("taskID").toString());
										if (task.has("taskID")) {
											if (taskid == (int) stepModel.get("TaskID")) {
												task.remove("tool");
												task.remove("toolID");
												task.remove("toolName");
												task.remove("avgEstdEffort");
												task.put("toolID", toolID);
												task.put("toolName", toolName);
												task.put("avgEstdEffort", avgEstdEffort);
												types.put("outputUpload", outputUpload);
											}
										}
									}
									if (attrs.has("tool")) {
										JSONObject tool = (JSONObject) attrs.get("tool");
										tool.remove("RPAID");
										tool.put("RPAID", rpaID);
										tool.remove("RPAName");
										tool.put("RPAName", rpaName);
									}
									JSONObject text = (JSONObject) attrs.get(attrType);
									String textName = text.getString("text");
									text.remove(textName);
									try {
										int index = 0;
										if (textName.contains("Tool Name:")) {
											index = textName.indexOf("Tool Name:");
										} else {
											index = textName.indexOf("ToolName:");
										}
										textName = textName.substring(0, index);
										textName = textName + "Tool Name:" + toolName;
									} catch (Exception ex) {
										ex.printStackTrace();
									}
									text.remove("text");
									text.put("text", textName);
								}
							}
						}
					}
				}
				json = obj.toString();
				wfData.remove("FlowChartJSON");
				wfData.put("FlowChartJSON", json);
				wfData.put("WFID", wfid);
				
				Integer parentLoeMeasurementCriterionID= flowChartDao.getParentLoeMeasurementCriterionID(subActivityFlowChartDefID);
				wfData.put("loeMeasurementCriterionID", parentLoeMeasurementCriterionID);
				//wfData.put("WFEditReason", wfEditReason);
				data.put("Success", wfData);
				wfJSONData.add(data);
				
			}
		} else {
			Map<String, Object> data = new HashMap<>();
			String value = "{ \"cells\": [{ \"size\": { \"width\": 200, \"height\": 90 }, \"angle\": 0, \"z\": 1, \"position\": { \"x\": 225, \"y\": 230 }, \"type\": \"basic.Rect\", \"attrs\": { \"rect\": { \"rx\": 2, \"ry\": 2, \"width\": 50, \"stroke-dasharray\": \"0\", \"stroke-width\": 2, \"fill\": \"#dcd7d7\", \"stroke\": \"#31d0c6\", \"height\": 30 }, \"text\": { \"font-weight\": \"Bold\", \"font-size\": 11, \"font-family\": \"Roboto Condensed\", \"text\": \"No FlowChart Available\", \"stroke-width\": 0, \"fill\": \"#222138\" }, \".\": { \"data-tooltip-position-selector\": \".joint-stencil\", \"data-tooltip-position\": \"left\" } } }] }";
			data.put("Failed", value);
			wfJSONData.add(data);
		}
		return wfJSONData;
	}

    public List<Map<String, Object>> getWFAndJSON(int projectID, int subActivityID, int woID, int wfVersion, boolean experiencedMode, int wfid) {
        List<Map<String, Object>> wfData = new ArrayList<>();
        if (experiencedMode) {
            String wfName = this.flowChartDao.getWFName(projectID, subActivityID, wfVersion,wfid);
          
                wfData = this.flowChartDao.getFlowChartForExperiencedMode(projectID, subActivityID, wfName,wfVersion,wfid);
           
        } else {
            if (woID != 0) {
                int wfVersionNo = flowChartDao.getWoWfVersionNo(woID);
                wfData = getJsonData(projectID, subActivityID, woID, wfVersionNo,wfid);
            } else if(wfVersion!=0){
                wfData = getJsonData(projectID, subActivityID, woID, wfVersion,wfid);
            }else {
               wfData = this.flowChartDao.viewFlowChartForSubActivity(projectID, subActivityID, woID,wfid);
            }
        }
        return wfData;
    }
    
     private List<Map<String, Object>> getJsonData(int projectID, int subActivityID, int woID, int wfVersion, int wfid) {
        return this.flowChartDao.viewFlowChartForSubActivityWithVersion(projectID, subActivityID, woID, wfVersion,wfid);
    }
    

    public String getDetails() {
        return this.flowChartDao.getDetails();
    }
  
    public TaskAndScopeMappingModel getStepTaskDetails(Integer projectID, Integer scopeID, Integer subactivityID,Integer versionNo) {
        TaskAndScopeMappingModel taskAndScopeMappingModel = new TaskAndScopeMappingModel();
        List<ScopeTaskMappingModel> lstscopeTaskMappings = new ArrayList<>();
        List<ScopeTaskMappingModel> lstscopeTaskValue = new ArrayList<>();
        List<TaskModel> task = this.activityMasterDAO.getTaskDetails(subactivityID, "");
        taskAndScopeMappingModel.setLstMasterTaskModels(task);
        if (task != null && task.size() > 0) {
            lstscopeTaskMappings = this.flowChartDao.getScopeTaskMapping(projectID, scopeID, subactivityID, task.get(0).getTaskID());
            for(ScopeTaskMappingModel model :lstscopeTaskMappings){
                if(model.getVersionNumber()!= 0 && model.getVersionNumber() == versionNo){
                    lstscopeTaskValue.add(model);
                    taskAndScopeMappingModel.setLstScopeTaskMapping(lstscopeTaskValue);
                }else{
                    taskAndScopeMappingModel.setLstScopeTaskMapping(lstscopeTaskMappings);
                }
            }
        }
        return taskAndScopeMappingModel;
    }
    
     public void insertStepTaskDetails(Integer projectID, Integer scopeID, Integer subactivityID, int versionNo) {
        TaskAndScopeMappingModel taskAndScopeMappingModel = new TaskAndScopeMappingModel();
        List<ScopeTaskMappingModel> lstscopeTaskMappings = new ArrayList<>();
        List<TaskModel> task = this.activityMasterDAO.getTaskDetails(subactivityID, "");
        taskAndScopeMappingModel.setLstMasterTaskModels(task);
        if (task != null && task.size() > 0) {
            task.forEach(t -> {
                List<ScopeTaskMappingModel> scopeTaskMappings = this.flowChartDao.getScopeTaskMapping(projectID, scopeID, subactivityID, t.getTaskID());
                if (scopeTaskMappings.isEmpty()) {
                    List<ToolsModel> tools = toolsMasterService.getToolByTaskID(t.getTaskID());
                    if (tools != null && tools.size() > 0) {
                        tools.forEach(tool -> {
                            prepareTaskScopeModel(projectID,scopeID,subactivityID,t,tool,versionNo);
                        });
                    } else {
                         ToolsModel tool = new ToolsModel();
                         prepareTaskScopeModel(projectID,scopeID,subactivityID,t,tool,versionNo);
                     }
                } else {
                    for (ScopeTaskMappingModel model : scopeTaskMappings){
                        if (model.getVersionNumber()!= 0 || model.getVersionNumber() != versionNo){
                             List<ToolsModel> tool = toolsMasterService.getToolByTaskID(t.getTaskID());
                            if (tool != null && tool.size() > 0) {
                                 tool.forEach(tools -> {
                                 prepareTaskScopeModel(projectID,scopeID,subactivityID,t,tools,versionNo);
                                });
                            }else{
                                ToolsModel tool1 = new ToolsModel();
                                prepareTaskScopeModel(projectID,scopeID,subactivityID,t,tool1,versionNo);
                            }
                        }
                        break;
                    }
                }
            });
            lstscopeTaskMappings = this.flowChartDao.getScopeTaskMapping(projectID, scopeID, subactivityID, task.get(0).getTaskID());
        }
        taskAndScopeMappingModel.setLstScopeTaskMapping(lstscopeTaskMappings);

    }

    @Transactional("transactionManager")
    public Response<Void> uploadAndSaveFile(String domain, String technology, String vendor, MultipartFile file, String projectID, String type, String uploadedON, String uploadedBy) throws IOException, SQLException  {
    	Response<Void> response=new Response<>();
        String fileName = file.getName();
        fileName = AppUtil.getFileNameWithTimestamp(fileName);
        String relativeFilePath = AppUtil.getRelativeFilePath(domain, technology, vendor, projectID);
        this.uploadFile(file, relativeFilePath, fileName);
        String filePath = appService.getConfigUploadFilePath() + "/" + relativeFilePath + "/" + fileName;
        
        Map<String, String> validationResult = FileCSVHelper.validateNetworkElementsCsvFile(filePath);
        if(validationResult!=null && !validationResult.isEmpty()){
        	for(String key:validationResult.keySet()){
        		response.addFormError(validationResult.get(key));
        	}
        	return response;
        }
        boolean isBulkUploadSuccess = appService.CsvBulkUploadNewGen(filePath, AppConstants.NETWORK_ELEMENTS_TEMP_TABLE_NAME, fileName);
        
		try {
			if (isBulkUploadSuccess){
				if ("NEW".equalsIgnoreCase(type)){
					this.flowChartDao.insertNetworkelements(fileName, Integer.parseInt(domain),Integer.parseInt(technology),Integer.parseInt(vendor),Integer.parseInt(projectID),uploadedON,uploadedBy);
				}else if ("UPDATE".equalsIgnoreCase(type)){
					this.flowChartDao.insertNetworkelementsUpdate(fileName, Integer.parseInt(domain),Integer.parseInt(technology),Integer.parseInt(vendor),Integer.parseInt(projectID),uploadedON,uploadedBy);
				}
				
			}else{
				response.addFormError(ApplicationMessages.ERROR_BCP_FILE_UPLOAD);
				return response;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}finally{
			appService.dropTable(fileName);
		}
		response.addFormMessage(ApplicationMessages.SUCCESS_BCP_FILE_UPLOAD);
        return response;
    }
    
    public boolean downloadNetworkElement(String domain, String technology, String vendor, String projectID, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> result =  this.flowChartDao.downloadNetworkElement(domain, technology, vendor, projectID);
		if (result.size() == 0){
			return false;
		}
	        response.setContentType("text/plain");
			response.setHeader("Content-type","application/csv");
			response.setHeader("Content-Disposition","attachment;filename=" + "NetworkElementSampleReport.csv");
			PrintWriter out = response.getWriter();
			for (Map<String, Object> data : result){
				String head = "";
				for (String header : data.keySet()){
					head += header+",";
				}
				out.println(head);
				break;
			}
			
			for (Map<String, Object> data : result){
				String row = "";
				for (String header : data.keySet()){
					row += data.get(header)+",";
				}
				out.println(row);
			}
			
	        out.flush();  
	        out.close();  
	        return true;
    }

    public void uploadFile(MultipartFile file, String relativeFilePath, String fileName) {
        String directory = appService.getConfigUploadFilePath() + "/"  + relativeFilePath;
        File df = new File(directory);
        if (!df.exists()) {
            df.mkdirs();
        }

        if (file != null && !file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(appService.getConfigUploadFilePath() + "/" + relativeFilePath + "/" + fileName)));
                stream.write(bytes);
                stream.close();
            } catch (Exception e) {
            	
                throw new ApplicationException(500, "File upload failed with error - " + e.getMessage());
            }
        }
    }
    
    // Excel File API upload
    public void uploadExcelFile(MultipartFile file, String relativeFilePath, String fileName) 
	{
    	String directory = appService.getConfigUploadFilePath() + "/"  + relativeFilePath;
        File df = new File(directory);
        BufferedOutputStream stream =null;
        if (!df.exists()) {
            df.mkdirs();
        }
        if (file != null && !file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                 stream = new BufferedOutputStream(new FileOutputStream(new File(appService.getConfigUploadFilePath() + "/" + relativeFilePath + "/" + fileName)));
                stream.write(bytes);
                
            } catch (Exception e) {
            	
                throw new ApplicationException(500, "File upload failed with error - " + e.getMessage());
            }finally {
            	try {
            		stream.close();
            	}catch(Exception e){
            		
            	}
            }
            
        }

		
	}

    public String getFilePath(String fileType) {
        String filePath = "";
        if ("WFSampleTemplate".equalsIgnoreCase(fileType)) {
            filePath = appService.getConfigUploadFilePathForSampleTemplate();
        } else if ("NodeTemplate".equalsIgnoreCase(fileType)) {
            filePath = appService.getConfigUploadFilePathForNodeTemplate();
        }else if ("WOCreationTemplate".equalsIgnoreCase(fileType)) {
            filePath = appService.getConfigUploadFilePathForWOCreationTemplate();
        }
        return filePath;
    }
    
    @Transactional("transactionManager")
    public void updateProjectDefinedTask(FlowChartStepDetailsModel fcStepDetails) {
            this.flowChartDao.updateFlowChartStepDetailsTasks(fcStepDetails);
            int avgEffort = flowChartDao.getStepAvgEffortDetails(fcStepDetails);
            fcStepDetails.setAvgEstdEffort(avgEffort);
            this.flowChartDao.updateProjectDefinedTask(fcStepDetails);
    }
   
    public List<FlowChartJsonModel> getDetailsForImportExistingWF(Integer projectID,Integer subActivityID) {
         List<FlowChartJsonModel> flowChart = new ArrayList<>();
        flowChart = this.flowChartDao.getDetailsForImportExistingWF(projectID,subActivityID);
        for(FlowChartJsonModel model :flowChart){
             List<Map<String,Object>> data = this.flowChartDao.getDefIDAndVersionName(model.getProjectID(),model.getSubActivityID());
             model.setWfDetails(data);
        }
        return flowChart;
    }
	
    public List<WorkFlowAvailabilitySearchModel> searchWFAvailabilityforScope(String projectID,String domain,String subDomain,String serviceArea,String subServiceArea,String technology,String activity,String subActivity, String marketArea){
        return this.flowChartDao.searchWFAvailabilityforScope(projectID,domain,subDomain,serviceArea,subServiceArea,technology,activity,subActivity,marketArea);
    }
	
    @Transactional("transactionManager")
    public void addWorkFlowToProject(FlowChartDefModel flowChartDefModel){
        FlowChartDefModel flowChartDef = this.flowChartDao.checkFlowChartVersion(flowChartDefModel);
        int projectID = flowChartDefModel.getProjectID();
        int subActID = flowChartDefModel.getSubActivityID();
        if(projectID == 0 || subActID == 0){
            throw new ApplicationException(500,"ProjectID and SubActivityID should not be null/zero");
        }else{
            flowChartDefModel.setType("Project Defined");
            if(flowChartDef == null){
                flowChartDefModel.setVersionNumber(1);
                this.flowChartDao.addWorkFlowToProject(flowChartDefModel);
            }
            else{
                this.flowChartDao.inActiveWorkflowForProject(flowChartDef.getSubActivityFlowChartDefID());
                int ver=flowChartDef.getVersionNumber();
                ver=ver+1;
                flowChartDefModel.setVersionNumber(ver);
                this.flowChartDao.addWorkFlowToProject(flowChartDefModel);
            }
        }        
    }
    
    public String createJSONForStep(CustomStepJSONModel customModel) {
       String stepID = UUID.randomUUID().toString();
       return generateJSONForStep(stepID,customModel);
    }
    
    private String generateJSONForStep(String stepID,CustomStepJSONModel model) {
        int x = 100;
        int y = 50;
        String json=null;
        String jsonStep=null;
        String jsonStep1;
        
            if (model.getStepType().equalsIgnoreCase("Manual")) {  // Rectangle
               if (!"null".equalsIgnoreCase(model.getTaskName())) {
                    json = FlowChartStepTemplate.OPERATION_STEP_JSON_WITH_TASK; 
                }
            } else if (model.getStepType().equalsIgnoreCase("Automatic")) {
                if (!"null".equalsIgnoreCase(model.getTaskName())) {
                    json = FlowChartStepTemplate.AUTOMATED_JSON_WITH_TASK;
                } 
            } else if (model.getStepType().equalsIgnoreCase("Decision")) {
                    json = FlowChartStepTemplate.DECISION_STEP_JSON;
            }else {
                throw new ApplicationException(500, "Invalid input... Please enter the values as step/decision!!!");
            }
            jsonStep = "{\"cells\":[";
            jsonStep1 = updateJSONForStep(json, x, y,stepID,model);
            jsonStep = jsonStep.concat(jsonStep1);
            jsonStep = jsonStep.concat("]}");
        return jsonStep;
    }

  
    private String updateJSONForStep(String json,int x, int y, String stepID,CustomStepJSONModel model) {
    	 String[] step =  splitStepNameByWord(model.getStepName()); 
         String newStep ="";
         for(int i=0;i<step.length;i++){
              newStep += step[i]+"\\\\n";  
         }
        if(!"null".equalsIgnoreCase(model.getTaskName()) && model.getTaskName()!= null){
             String[] tools =  splitStepNameByWord(model.getToolName()); 
                String newTool ="";
                for (String tool1 : tools) {
                    newTool += tool1 + "\\\\n";
                }
            json = json.replaceAll("%STEP_NAME%", newStep +"\\\\nTool Name:"+newTool+"\\\\n\\\\n");
            json = json.replaceAll("%STEP_VALUE%", model.getStepName());
            json = json.replaceAll("%POSITION_X%", String.valueOf(x));
            json = json.replaceAll("%POSITION_Y%", String.valueOf(y));
            json = json.replaceAll("%STEP_ID%", stepID);
            json = json.replaceAll("%TASK_ID%", model.getTaskID());
            json = json.replaceAll("%TASK_NAME%", model.getTaskName());
            json = json.replaceAll("%EXECUTION_TYPE%", model.getStepType());
            json = json.replaceAll("%TOOL_ID%", model.getToolID());
            json = json.replaceAll("%TOOL_NAME%", model.getToolName());
            json = json.replaceAll("%REASON%", model.getReason());
            json = json.replaceAll("%ACTION%", model.getTaskID()+"@"+model.getTaskName());
            json = json.replaceAll("%TOOL_DATA%", model.getToolID()+"@"+model.getToolName());
            json = json.replaceAll("%RPA_DATA%", "0@0");
            json = json.replaceAll("%RESP%", model.getResponsible());
        }else{
            json = json.replaceAll("%STEP_NAME%", model.getStepName());
            json = json.replaceAll("%POSITION_X%", String.valueOf(x));
            json = json.replaceAll("%POSITION_Y%", String.valueOf(y));
            json = json.replaceAll("%STEP_ID%", String.valueOf(stepID));
        }
        return json;
    }

    public List<String> getElementType(int projectID) {
       return this.flowChartDao.getElementType(projectID);
    }
     
    public List<String> getMarketDetails(int projectID) {
       return this.flowChartDao.getMarketDetails(projectID);
    }
    
     public List<FlowChartDefModel> getVersionName(int projectID,int subActivityID) {
       return this.flowChartDao.getVersionName(projectID,subActivityID);
    }
      public List<FlowChartDefModel> getVersionNameCurProjId(int flowChartDefID) {
       return this.flowChartDao.getVersionNameCurProjId(flowChartDefID);
    }
      
    public List<FlowChartDefModel> getWorkFlowVersionData(int projectID,int subActivityID, int wfid) {
       return this.flowChartDao.getWorkFlowVersionData(projectID,subActivityID,wfid);
    }

    private String sendFileUsingftp(String filePath) throws IOException {
        String server = appService.getServerIP();
        int port = Integer.parseInt(appService.getConfigServerPort());
        String user = appService.getConfigUserName();
        String pass = appService.getConfigPassword();
        String finalFilepath ="";
        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            File firstLocalFile = new File(filePath);
            String firstRemoteFile = "FileUpload\\" + firstLocalFile.getName();
            finalFilepath= "D:\\ISFDBFileUpload\\FileUpload\\"+firstLocalFile.getName();
            InputStream inputStream = new FileInputStream(firstLocalFile);
            
           ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
        } catch (IOException ex) {
            
        } finally {
            if (ftpClient.isConnected()) {
                  ftpClient.logout();
                ftpClient.disconnect();
            }
        }
        return finalFilepath;
    }
    
    public String[] splitStepNameByWord(String stepName){
        String[] stepValue = new String[100];
        int spaceCount = 0;
        int lastIndex = 0;
        String[] stringSplitted = new String[100];
        int stringLength = 0;
        for (int i = 0; i < stepName.length(); i++) {
            if (stepName.charAt(i) == ' ') {  
                spaceCount++;
            }
            if (spaceCount == 4) {  
                stringSplitted[stringLength++] = stepName.substring(lastIndex, i);
                lastIndex = i;
                spaceCount = 0;

            }
        }
        stringSplitted[stringLength++] = stepName.substring(lastIndex, stepName.length()+1 - 1);
        for (int i = 0; i < stringSplitted.length; i++) {
            if (stringSplitted[i] != null && stringSplitted.length > 0) {
                stepValue[i] =  stringSplitted[i];
            }
        }
        String[] newStepName = Arrays.stream(stepValue).filter(Objects::nonNull).toArray(String[]::new);
          return newStepName;
    }

//    private String[] splitStepName(String stepName) {
//	String[] step =  stepName.split(String.format("(?<=\\G.{%1$d})", 30));
//        return step;
//    }

    public ScopeAndMasterTaskModel getTaskDetailsForJSONStep(Integer projectID, Integer subactivityID, Integer taskID,Integer versionNo,String stepID,int flowChartDefID) {
        ScopeAndMasterTaskModel scopeAndMasterTaskModel = new ScopeAndMasterTaskModel();
        ScopeTaskMappingModel scopeTaskMappings = new ScopeTaskMappingModel();
        TaskModel task = this.activityMasterDAO.getTaskDetailsFromMaster(subactivityID,taskID);
        scopeAndMasterTaskModel.setMasterTaskModels(task);
        scopeTaskMappings = this.flowChartDao.getTaskDetailsForJSONStep(projectID,subactivityID,taskID,versionNo,stepID,flowChartDefID);
        if(scopeTaskMappings!=null) {
        	scopeTaskMappings.setProjectID(projectID);
            scopeTaskMappings.setSubActivityID(subactivityID);
        }
            scopeAndMasterTaskModel.setScopeTaskMapping(scopeTaskMappings);	
        
        return scopeAndMasterTaskModel;
    }

    @Transactional("transactionManager")
    private void updateTaskExecutionType(List<ScopeTaskMappingModel> scopeTaskModel, List<FlowChartStepModel> flowModel,int versionNumber) {
        for(ScopeTaskMappingModel scModel : scopeTaskModel){
            for(FlowChartStepModel fcModel : flowModel){
                if(scModel.getMasterTask().equalsIgnoreCase(fcModel.getTaskOrParentTaskMapped())){
                    if(!scModel.getExecutionType().equalsIgnoreCase(fcModel.getExecutionType()) 
                       || scModel.getExecutionType().isEmpty()){
                       this.flowChartDao.updatePrjectConfigTaskDetails(scModel.getProjectID(),scModel.getSubActivityID(),scModel.getTaskID(),fcModel.getExecutionType(),versionNumber); 
                    }
                }
            }
        }
    }

    public List<WorkFlowApprovalModel> getWorkFlowForApproval(int projectID) {
        return this.flowChartDao.getWorkFlowForApproval(projectID);
    }

    @Transactional("transactionManager")
    public void rejectWorkFlow(int projectID, int subActivityID, int flowchartDefID,int wfVersion,String managerSignumID,String employeeSignumID,String reason) {
        try{
            String res = reason.replaceAll("@", "/");
            String wfName = this.flowChartDao.getWorfFlowName(flowchartDefID);
            this.flowChartDao.deactivateCustomVersion(projectID,subActivityID,flowchartDefID,managerSignumID,res);
            this.flowChartDao.deactivateCustomVersionOfWOStepDetails(flowchartDefID);
            sendMailToEmployee(managerSignumID,employeeSignumID,"Rejected",wfName);
            updateApprovalLog(flowchartDefID,"rejected",managerSignumID,employeeSignumID);
        }catch(Exception ex){
           LOG.error("Error while reject workflow:"+ ex.getMessage());
           ex.printStackTrace();
           throw new ApplicationException(500,"Error while rejecting WorkFlow");
       }
    }
    
    @Transactional("transactionManager")
    public Response<Void> approveWorkFlow(int projectID, int subActivityID, int flowchartDefID, String wfName, int wfVersion, String mSignumID, String eSignumID, boolean isCreateNew, String newWFName, int wfid, String wfEditReason, String flowChartJSON) {
    	Response<Void> responseData=new Response<Void>();
    	try {
    		
            FlowChartDefModel flowModel = new FlowChartDefModel();
            int parentWFDefID = this.flowChartDao.getParentWorkFlowDefID(flowchartDefID);
            int updatedVersion = 0;
            int subActivityFlowChartDefID = 0;
            boolean flag = false;
			if (isCreateNew) {
				if (this.flowChartDao.getWorkflow(projectID, newWFName, subActivityID)) {
					responseData.addFormError(
							"Name " + newWFName + " already exists.Please enter other WorkFlow name and try again!!");
					return responseData;
				}
                FlowChartDefModel flowDefModel = flowChartDao.getFlowChartJSON(projectID, subActivityID, wfVersion, eSignumID,wfid);
                
                Integer parentLoeMeasurementCriterionID= flowChartDao.getParentLoeMeasurementCriterionID(parentWFDefID);
                
                flowModel.setProjectID(projectID);
                flowModel.setSubActivityID(subActivityID);
                flowModel.setFlowChartJSON(flowDefModel.getFlowChartJSON());
                flowModel.setCreatedBy(mSignumID);
                flowModel.setVersionNumber(1);
                flowModel.setWFID(this.flowChartDao.getMaxWFID()+1);
                flowModel.setParentFlowChartDefID(0);
                flowModel.setWorkFlowName(newWFName);
                
                flowModel.setNeNeeded(flowDefModel.isNeNeeded());
                flowModel.setExperiencedFlow(flowDefModel.isExperiencedFlow());
                flowModel.setWfOwner(mSignumID);
                flowModel.setType("PROJECTDEFINED");
                flowModel.setSlaHours(flowDefModel.getSlaHours());
                flowModel.setFtrValue(flowDefModel.getFtrValue());
                flowModel.setLoeMeasurementCriterionID(parentLoeMeasurementCriterionID);
                
                this.flowChartDao.insertJSONFromUI(flowModel);
                int subActivityFlowchartDefIdAndInstanceId=isfCustomIdInsert.generateCustomId(flowModel.getSubActivityFlowChartDefID());
                flowModel.setSubActivityFlowChartDefID(subActivityFlowchartDefIdAndInstanceId);
                subActivityFlowChartDefID = flowModel.getSubActivityFlowChartDefID();
                this.flowChartDao.deactivateCustomVersion(projectID, subActivityID, flowchartDefID, mSignumID, "");
                this.flowChartDao.deactivateCustomVersionOfWOStepDetails(flowchartDefID);
                flowChartDao.updateWorkOrderForAutoSense(flowchartDefID);
                updatedVersion = flowModel.getVersionNumber();
                flowChartDao.insertDataToFlowChartStepDetails(subActivityFlowChartDefID, flowchartDefID,updatedVersion,wfVersion, mSignumID);
                StringBuilder customStepId=new StringBuilder();
         	   customStepId.append(flowModel.getWFID()).append("-").
         	            append(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
         	   String disabledExcutionType = "ManualDisabled";
         	   
         	   this.flowChartDao.insertDummyDataOnStepDetails(customStepId.toString(),AppConstants.ERICSSON_MANUAL_DISABLED,
         			  flowModel.getCreatedBy(),"Experienced",customStepId.append("-DisabledManual").toString(),
         			 subActivityFlowChartDefID,disabledExcutionType,updatedVersion );
            
                
                flowChartDao.insertDataToFlowChartLinkDetails(subActivityFlowChartDefID, flowchartDefID);
                flowChartDao.insertCustomWorkflowStepAutoSenseRule(parentWFDefID,subActivityFlowchartDefIdAndInstanceId);
                flag = true;
                if(flag) {
                    responseData.addFormMessage("New Work Flow Created Successfully");
                   }
            } else {
               // int newVersion = workOrderPlanDao.getLatestWorkFlowVersion(projectID, subActivityID);
                FlowChartDefModel parentDetails = flowChartDao.getFlowchartDetails(parentWFDefID);
               // newVersion = newVersion + 1;
                flowModel.setProjectID(projectID);
                flowModel.setSubActivityID(subActivityID);
                flowModel.setFlowChartJSON(flowChartJSON);
                flowModel.setCreatedBy(mSignumID);
               // flowModel.setVersionNumber(newVersion);
                
            	int verNO = flowChartDao.getWFVersioNo(wfid);
            	flowModel.setVersionNumber(verNO+1);
                flowModel.setWFID(wfid);
                flowModel.setParentFlowChartDefID(flowchartDefID);
                flowModel.setWfEditReason(wfEditReason);
                
                flowModel.setWorkFlowName(parentDetails.getWorkFlowName());
                flowModel.setLoeMeasurementCriterionID(parentDetails.getLoeMeasurementCriterionID());
                
                flowModel.setNeNeeded(parentDetails.isNeNeeded());
                flowModel.setExperiencedFlow(parentDetails.isExperiencedFlow());
                flowModel.setWfOwner(mSignumID);
                flowModel.setType("PROJECTDEFINED");
                flowModel.setSlaHours(parentDetails.getSlaHours());
                flowModel.setFtrValue(parentDetails.getFtrValue());
                this.flowChartDao.insertJSONFromUI(flowModel);
                int subActivityFlowchartDefIdAndInstanceId=isfCustomIdInsert.generateCustomId(flowModel.getSubActivityFlowChartDefID());
                flowModel.setSubActivityFlowChartDefID(subActivityFlowchartDefIdAndInstanceId);
                subActivityFlowChartDefID = flowModel.getSubActivityFlowChartDefID();
                this.flowChartDao.deactivateCustomVersion(projectID, subActivityID, flowchartDefID, mSignumID, "");
                this.flowChartDao.deactivateCustomVersionOfWOStepDetails(flowchartDefID);
                this.flowChartDao.deactivateParentVersion(projectID, subActivityID, parentWFDefID, mSignumID, "Updated With Existing Name");
                this.flowChartDao.deactivateCustomVersionOfWOStepDetails(parentWFDefID);
                updatedVersion = flowModel.getVersionNumber();
                flowChartDao.insertDataToFlowChartStepDetails(subActivityFlowChartDefID, flowchartDefID,updatedVersion,wfVersion, mSignumID);
                StringBuilder customStepId=new StringBuilder();
                customStepId.append(flowModel.getWFID()).append("-").
 	            append(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
 	            String disabledExcutionType = "ManualDisabled";
 	   
 	           this.flowChartDao.insertDummyDataOnStepDetails(customStepId.toString(),AppConstants.ERICSSON_MANUAL_DISABLED,
 			  flowModel.getCreatedBy(),"Experienced",customStepId.append("-DisabledManual").toString(),
 			 subActivityFlowChartDefID,disabledExcutionType,updatedVersion );
    
        
                flowChartDao.insertDataToFlowChartLinkDetails(subActivityFlowChartDefID, flowchartDefID);
                flowChartDao.updateWorkOrderForAutoSense(flowchartDefID);
                flowChartDao.insertCustomWorkflowStepAutoSenseRule(parentWFDefID,subActivityFlowChartDefID);
                // update version in tabel transactionalData.TBL_ExecutionPlan_details.
                updateWFVersion(projectID, subActivityID, parentWFDefID, parentDetails.getVersionNumber(), updatedVersion, parentDetails.getWorkFlowName());
                flag = true;
                if(flag) {
                responseData.addFormMessage("Work Flow Updated Successfully");
                }
            }
			
            updateApprovalLog(flowchartDefID, "approved", mSignumID, eSignumID);
            if (flag) {
            	callSendMailToEmployee(mSignumID, eSignumID, "Approved", wfName);
                }
        } catch (Exception ex) {
            LOG.error("Error while approving workflow:" + ex.getMessage());
            ex.printStackTrace();
            responseData.addFormError("Error while approving workflow");
            
        }
		return responseData;
    }
    private void callSendMailToEmployee(String mSignumID, String eSignumID, String status, String wfName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					sendMailToEmployee(mSignumID, eSignumID, status, wfName);
				} catch (Exception e) {
					LOG.debug(String.format("Exception thrown  %s", e.getMessage()));
				}

			}
		}).start();

	}
	private void sendMailToEmployee(String mSignumID, String eSignumID,String status,String wfName) throws IOException, MessagingException, InterruptedException {
        Map<String,String> mDetails= getEmployeeEmailDetails(mSignumID);
        Map<String,String> eDetails= getEmployeeEmailDetails(eSignumID);
        String managerEmail = "";
        String employeeEmail = "";
        if(mDetails.get("EmployeeEmailID") == null || "NULL".equals(mDetails.get("EmployeeEmailID"))){
            managerEmail ="ankith.kumar@ericsson.com";
        }else{
            managerEmail = mDetails.get("EmployeeEmailID");
        }
        if(eDetails.get("EmployeeEmailID") == null || "NULL".equals(eDetails.get("EmployeeEmailID"))){
            employeeEmail ="ankith.kumar@ericsson.com";
        } else{
            employeeEmail=eDetails.get("EmployeeEmailID");
        }
        String mailBody = mailUtil.generateMailBodyForWorkFlow(mSignumID, mDetails.get("EmployeeName"), eSignumID, eDetails.get("EmployeeName"),status,wfName);
 
        workOrderPlanDao.SendMailNotification(managerEmail,mailBody,employeeEmail);
    }
    
    private Map<String,String> getEmployeeEmailDetails(String signumID) {
        return this.flowChartDao.getEmployeeEmailDetails(signumID);
    }
    
    @Transactional("transactionManager")
    private void updateApprovalLog(int flowchartDefID, String status, String managerSignumID, String employeeSignumID) {
        this.flowChartDao.saveApprovalLogDetails(flowchartDefID,status,managerSignumID,employeeSignumID);
    }

    @Transactional("transactionManager")
    public void prepareTaskScopeModel(Integer projectID, Integer scopeID, Integer subactivityID, TaskModel t, ToolsModel tool, int versionNo) {
        if (tool.getToolID()!=0 && !"null".equalsIgnoreCase(tool.getTool())) {
                ScopeTaskMappingModel mapping = new ScopeTaskMappingModel();
                mapping.setProjectID(projectID);
                mapping.setScopeID(scopeID);
                mapping.setSubActivityID(subactivityID);
                mapping.setTaskID(t.getTaskID());
                mapping.setAvgEstdEffort(t.getAvgEstdEffort());
                mapping.setExecutionType(t.getExecutionType());
                mapping.setTask(t.getTask());
                mapping.setTool(tool.getTool());
                mapping.setToolID(tool.getToolID());
                mapping.setRpaID(t.getRpaID());
                mapping.setMasterTask(t.getTask());
                mapping.setVersionNumber(versionNo);
                this.flowChartDao.insertScopeTaskMapping(mapping); // insert version no
        } else {
            ScopeTaskMappingModel mapping = new ScopeTaskMappingModel();
            mapping.setProjectID(projectID);
            mapping.setScopeID(scopeID);
            mapping.setSubActivityID(subactivityID);
            mapping.setTaskID(t.getTaskID());
            mapping.setAvgEstdEffort(t.getAvgEstdEffort());
            mapping.setExecutionType(t.getExecutionType());
            mapping.setTask(t.getTask());
            mapping.setTool("null");
            mapping.setToolID(0);
            mapping.setRpaID(t.getRpaID());
            mapping.setMasterTask(t.getTask());
            mapping.setVersionNumber(versionNo);
            this.flowChartDao.insertScopeTaskMapping(mapping);// insert version no 
        }
    }

    public void saveJsonForWorkFlow(List<WorkFlowDefinitionModel> workFlowDefinitionModel, String signum) {

        if (workFlowDefinitionModel != null) {
            for (WorkFlowDefinitionModel model : workFlowDefinitionModel) {
                this.flowChartDao.saveWorkflowDefinition(model, signum);
                int workFlowIdAndInstanceId=isfCustomIdInsert.generateCustomId(model.getWorkFlowID());
                model.setWorkFlowID(workFlowIdAndInstanceId);
                int workFlowId = model.getWorkFlowID();
                
                //this.flowChartDao.saveWorkflowDefinition(model);
                List<WorkFlowStepsModel> workFlowSteps = model.getWorkFlowSteps();
                List<WorkFlowLinksModel> WorkFlowLinks = model.getWorkFlowLinks();
                if (workFlowSteps != null) {
                    for (WorkFlowStepsModel workFlow : workFlowSteps) {

                        this.flowChartDao.saveworkFlowSteps(workFlow, workFlowId, signum);
                        int workFlowStepIdAndInstanceId=isfCustomIdInsert.generateCustomId(workFlow.getWorkFlowStepID());
                        workFlow.setWorkFlowStepID(workFlowStepIdAndInstanceId);
                        int flowChartStepId = workFlow.getWorkFlowStepID();
                        List<WorkFlowStepAttrModel> workFlowStepAttr = workFlow.getWorkFlowStepAttr();
                        if (workFlowStepAttr != null) {
                            for (WorkFlowStepAttrModel stepAttr : workFlowStepAttr) {
                                this.flowChartDao.saveWorkFlowStepAttr(stepAttr, flowChartStepId, signum);
                            }
                        }
                    }
                }
                if (WorkFlowLinks != null) {
                    for (WorkFlowLinksModel workFlowLink : WorkFlowLinks) {
                        this.flowChartDao.saveWorkFlowLinks(workFlowLink, workFlowId, signum);
                        int workFloewLinkIdAndInstanceId=isfCustomIdInsert.generateCustomId(workFlowLink.getWorkFlow_LinkID());
                        workFlowLink.setWorkFlow_LinkID(workFloewLinkIdAndInstanceId);
                        List<WorkFlowLinksVerticesModel> vertices = workFlowLink.getVertices();
                        int workFlow_LinkID = workFlowLink.getWorkFlow_LinkID();
                        if (vertices != null) {
                            for (WorkFlowLinksVerticesModel vertice : vertices) {
                                this.flowChartDao.saveWorkFlowVertices(vertice, workFlow_LinkID, signum);
                            }
                        }
                    }
                }
            }
        }
    }
    
     public WorkFlowDefinitionModel getJsonDataForWorkFlow( int projectID,int subActID, int versionNO)
      { 
        return flowChartDao.getJsonDataForWorkFlow(projectID, subActID, versionNO);
      }

    private void deleteFailedRecord(int projectID, int subActivityID, int defID) {
        if (defID != 0) {
            this.flowChartDao.deleteEmptyData(defID);
        } else {
            int subaActivityDefID = this.flowChartDao.getLatestDefID(projectID, subActivityID);
            if (subaActivityDefID != 0) {
                this.flowChartDao.deleteStepData(subaActivityDefID);
                this.flowChartDao.deleteEmptyData(subaActivityDefID);
            }
        }
    }
    
     private void deleteInvalidRecord(int subActivityDefID, boolean jsonInsert) {
        if (subActivityDefID != 0 && jsonInsert) {
                this.flowChartDao.deleteStepData(subActivityDefID);
                this.flowChartDao.deleteFlowStepLinkData(subActivityDefID);
                this.flowChartDao.deleteFlowStepData(subActivityDefID);
                this.flowChartDao.deleteWFDefData(subActivityDefID);
            }
    }

    public List<Map<String,String>> getWFOwners(int projectID) {
        return flowChartDao.getWFOwners(projectID);
    }

    public void updateWFOwner(int flowChartDefID, String signumID) {
        flowChartDao.updateWFOwner(flowChartDefID,signumID);
    }

    public void downloadFlowChartData(int flowChartDefID, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
		response.setHeader("Content-type","application/csv");
		response.setHeader("Content-Disposition","attachment;filename=" + "FlowChartData.csv");
		List<Map<String, Object>> result = flowChartDao.getFlowChartData(flowChartDefID);
		writeWorkbookToResponse(response, generateXlsFile(result),"FlowChartData"+"-"+ (new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date())) +".xlsx");
    }
    
      private void writeWorkbookToResponse(HttpServletResponse response,Workbook wbook,String filename) throws IOException{
    	response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition","attachment;filename=" + filename);
		OutputStream out = response.getOutputStream();
		wbook.write(out);
		out.flush();
  		out.close();
    }
      
      private Workbook generateXlsFile(List<Map<String, Object>> result) throws IOException{
    	@SuppressWarnings("unchecked")
    	Workbook workbook=new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        Row row = sheet.createRow(0);
        int col=0;
        for(String key:result.get(0).keySet()){	
            Cell cell = row.createCell(col++);
            cell.setCellValue(key);	
        }
   
        for(int i=1;i<=result.size();i++){
        	col=0;
        	row = sheet.createRow(i);
	        for(String key:result.get(i-1).keySet()){
	            Cell cell = row.createCell(col++);
	            cell.setCellValue((result.get(i-1).get(key)==null)?(""):(result.get(i-1).get(key).toString()));
	        }
        }
        return workbook;
    }

	@SuppressWarnings({ "unused", "null" })
	public Map<String, Object> getAndSaveBotSavingSummary(BotSavingModel botSavingModel) 
	{
		Map<String, Object> data = new HashMap<String, Object>();

		List<Map<String, Object>> deletedSteps = flowChartDao.getDeletedSteps(botSavingModel.getOldDefID(),botSavingModel.getNewDefID()); 

		// Deleted
		for (Map<String, Object> dSteps : deletedSteps)
		{
		 
			if (dSteps.get("ExecutionType").toString().toUpperCase().equals("MANUAL")) {

				//Map<String, Object> isCalculationNeeded = flowChartDao.checkisCalculationNeeded((String) dSteps.get("StepID"), botSavingModel.getOldDefID()); // 2
				Map<String, Object> preSavingdata = flowChartDao.checkIfStepExperienced((String) dSteps.get("StepID"),botSavingModel.getOldDefID()); 
				
				int count = configurations.getIntegerProperty(ConfigurationFilesConstants.FLOWCHART_PRECONDITION_COUNT);
				int date =  configurations.getIntegerProperty(ConfigurationFilesConstants.FLOWCHART_PRECONDITION_CLOSED_DATE);
				
				// New Query
				boolean isPreCondtionSatisfied = flowChartDao.isPreCondtionSatisfied((String) dSteps.get("StepID"), botSavingModel.getOldDefID(), count, date);

				// PreCondition
				if (isPreCondtionSatisfied) {
					if (preSavingdata != null) {
						//Map<String, Object> sData = flowChartDao.getSavingForManualStep((String) dSteps.get("StepID"),botSavingModel.getOldDefID()); 
						String sData =  String.valueOf(dSteps.get("Savings"));
						if (sData != null) {
							//dSteps.put("Savings", sData.get("Savings"));
							dSteps.put("Savings", sData);
							dSteps.put("msg", "20 WO's were executed for this Step.");
							dSteps.put("flag", true);
							dSteps.put("isAll20WOExecuted", true);
						}
						else {
							dSteps.put("Savings", null);
							dSteps.put("msg", "");
							dSteps.put("flag", false);
						}
					}

					else {
						//Map<String, Object> sData = flowChartDao.getSavingForManualStep((String) dSteps.get("StepID"),botSavingModel.getOldDefID());
						String sData =  String.valueOf(dSteps.get("Savings"));
						//if (sData != null && (double) sData.get("Savings") != 0) {
						if (sData != null && (double) dSteps.get("Savings") != 0) {
							//dSteps.put("Savings", sData.get("Savings"));
							dSteps.put("Savings", sData);
							dSteps.put("msg","The minimum WOs were not found. Calculated with available data. Recheck parent mapping");
							dSteps.put("flag", true);
							dSteps.put("isAll20WOExecuted", false);
						} 
						else {
							dSteps.put("Savings", null);
							dSteps.put("msg", "");
							dSteps.put("flag", false);
						}
					}
					dSteps.put("isPreCondtionSatisfied", true);
				}

				// PreCondition False Scenario
				//if (!isPreCondtionSatisfied) {
				else 
				{
					//Map<String, Object> sData = flowChartDao.getSavingForManualStep((String) dSteps.get("StepID"),botSavingModel.getOldDefID()); 
					String sData = String.valueOf(dSteps.get("Savings"));
					if(sData != null) {
						dSteps.put("Savings", sData);
					}
					else {
						dSteps.put("Savings", null);
					}
					//dSteps.put("Savings", sData.get("Savings"));
					dSteps.put("msg", "No data in the Pre-Period.");
					dSteps.put("isPreCondtionSatisfied", false);
				}

			}

		
			else if (dSteps.get("ExecutionType").toString().toUpperCase().equals("AUTOMATIC")) {
				botSavingModel.setOldStepId((String) dSteps.get("StepID"));
				botSavingModel.setbOTID((int) dSteps.get("RpaID"));

				Map<String, Object> sData = flowChartDao.getSavingForAutomaticStep(botSavingModel.getOldDefID(), botSavingModel.getOldStepId()); 

				if (sData != null) {
					dSteps.put("Savings", sData.get("Savings"));
					dSteps.put("msg", "20 WO's were executed for this Step.");
					dSteps.put("flag", true);
				} else {
					dSteps.put("Savings", null);
					dSteps.put("msg", "");
					dSteps.put("flag", false);
				}
			}
		}

		// Added Steps
		List<Map<String, Object>> addedSteps = flowChartDao.getAddedSteps(botSavingModel.getOldDefID(),botSavingModel.getNewDefID()); 

		for (Map<String, Object> dSteps : addedSteps) {
			if (dSteps.get("ExecutionType").toString().toUpperCase().equals("MANUAL")) {
				Map<String, Object> isCalculationNeeded = flowChartDao
						.checkisCalculationNeeded((String) dSteps.get("StepID"), botSavingModel.getNewDefID()); 

				if (isCalculationNeeded != null && isCalculationNeeded.get("isCalculationNeeded") != "1") {
					Map<String, Object> preSavingdata = flowChartDao.checkIfStepExperienced((String) dSteps.get("StepID"), botSavingModel.getNewDefID()); 
					if (preSavingdata != null) {
						Map<String, Object> sData = flowChartDao.getSavingForManualStep((String) dSteps.get("StepID"),botSavingModel.getNewDefID()); 

						if (sData != null) {
							dSteps.put("Savings", sData.get("Savings"));
							dSteps.put("msg", "20 WO's were executed for this Step.");
							dSteps.put("flag", true);
						} else {
							dSteps.put("Savings", null);
							dSteps.put("msg", "");
							dSteps.put("flag", false);
						}
					}

					else {
						dSteps.put("Savings", "NA");
						dSteps.put("msg", "20 WO's were not executed for this Step.");
						dSteps.put("flag", false);
					}
				}

				else {
					dSteps.put("Savings", null);
					dSteps.put("msg", "");
					dSteps.put("flag", false);
				}
			}
		}

		data.put("deletedSteps", deletedSteps);
		data.put("addedSteps", addedSteps);

		return data;

	}

	public void deleteDefID(BotSavingModel botSavingModel) {

		int oldDefID = botSavingModel.getOldDefID();
		// in case of WF+		
		if(oldDefID==0){
			deleteDefIDByOldDefID(botSavingModel);
		}
		// in case of EWF+
		else{
		List<FlowChartDefModel> flowChartDefModel = flowChartDao.getVersionNameCurProjId(oldDefID);
	
		int projectID = flowChartDefModel.get(0).getProjectID();
		int subActivityID = flowChartDefModel.get(0).getSubActivityID();
		int subActivityFlowChartDefID = flowChartDefModel.get(0).getSubActivityFlowChartDefID();
		int lastUpdatedVersion = botSavingModel.getNewVersion(); // need to change this in version number
		int currentVersion = flowChartDefModel.get(0).getVersionNumber();
		String workFlowName = flowChartDefModel.get(0).getWorkFlowName();
		
			//reverts all WOs which are created for the new subactivityFlowchartDefID until user click cancel(Bot Saving) to old subactivityFlowchartDefID
			flowChartDao.revertOldDefIdForNewDefIdWO(oldDefID, botSavingModel.getNewDefID(),currentVersion);
		
		deleteDefIDByOldDefID(botSavingModel);
		flowChartDao.updateOldDefID(botSavingModel.getOldDefID());
		
		
		// to update version in table transactionalData.TBL_ExecutionPlan_details
		try {
			this.updateWFVersion(projectID,subActivityID,subActivityFlowChartDefID,lastUpdatedVersion,currentVersion,workFlowName);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
	}


	private void deleteDefIDByOldDefID(BotSavingModel botSavingModel) {
		flowChartDao.deleteFlowChartDefIDFromInstructionTable(botSavingModel.getNewDefID());
		flowChartDao.deleteFlowChartDefIDFromSaveKPItable(botSavingModel.getNewDefID());
		flowChartDao.updateOldDefIDFromSaveKPItable(botSavingModel.getOldDefID());
		flowChartDao.deleteDefIDFromAutoSenseRule(botSavingModel.getNewDefID());
		flowChartDao.deleteDefID(botSavingModel.getNewDefID());
		flowChartDao.deleteSubAcitvityDefID(botSavingModel.getNewDefID());
	
	}

	public void saveBotSavingDetailsAndHistory(List<BotSavingModel> botSavingModels) {

		for (BotSavingModel botSavingModel : botSavingModels) 
		{
            
			flowChartDao.deactiveBotStatusForOldReocrds(botSavingModel);

			if (botSavingModel.getIsSavingFlag().toUpperCase().equals("TRUE")) 
			{
				flowChartDao.updateStepDetailsSavings(botSavingModel);
			} 
			else 
			{
				flowChartDao.updateStepDetailsSavingsRemarks(botSavingModel);
			}

			//New Steps
			for (Map<String, String> nSteps : botSavingModel.getNewSteps())
			{
				if (nSteps.get("Saving") == null  || nSteps.get("Saving").toUpperCase().equals("NAN") || nSteps.get("Saving").toUpperCase().equals("NA") 
					|| nSteps.get("Saving").toUpperCase().equals("NULL")) 
				{
					nSteps.put("Saving", "0");
				}
				
				String StepName = (String) nSteps.get("StepName");
				StepName = StepName.replaceAll("\n", " ");
				StepName = StepName.replaceAll("\r", " ");
				
	        flowChartDao.saveBotSavingDetailsAndHistoryNew(
	        nSteps.get("StepID"), StepName, "POST", botSavingModel,nSteps.get("ExecType"), nSteps.get("Saving"));

			}
			
			//Old Steps
			for (Map<String, String> oSteps : botSavingModel.getOldSteps()) 
			{
				if (oSteps.get("Saving") == null || oSteps.get("Saving").toUpperCase().equals("NAN") || oSteps.get("Saving").toUpperCase().equals("NA")
						|| oSteps.get("Saving").toUpperCase().equals("NULL")  ) 
				{
					oSteps.put("Saving", "0");
				}
				
				String StepName = (String) oSteps.get("StepName");
				StepName = StepName.replaceAll("\n", " ");
				StepName = StepName.replaceAll("\r", " ");
				
	        flowChartDao.saveBotSavingDetailsAndHistoryOld(
			oSteps.get("StepID"), StepName, "PRE", botSavingModel,oSteps.get("ExecType"), oSteps.get("Saving"), oSteps.get("emeCalculationDefID"));
			}
		}
	}

	public ResponseEntity<Response<List<Map<String, Object>>>> getWFVersionsFromSubactivityID(BotSavingModel botSavingModel) {
		
		int pageLength  = configurations.getIntegerProperty(ConfigurationFilesConstants.FLOWCHART_PAGE_SIZE);
	
		Response<List<Map<String,Object>>> response = new Response<>();
		//return flowChartDao.getWFVersionsFromSubactivityID(botSavingModel , term);
		
		try {
			LOG.info("WF Version from sub activity id :Start");
			
			List<Map<String,Object>> WFVersionsFromSubactivityID = this.flowChartDao.getWFVersionsFromSubactivityID(botSavingModel  , pageLength);
			
				response.setResponseData(WFVersionsFromSubactivityID);
		        LOG.info("get Forward Reverse WF Transition:End");
		}	
		catch (ApplicationException e)
		{
			LOG.error(e.getMessage());
			response.addFormMessage(e.getMessage());
			return new ResponseEntity<Response<List<Map<String,Object>>>>(response, HttpStatus.OK);
		}
		
		catch(Exception ex) {
			LOG.error(ex.getMessage());
			response.addFormError(ex.getMessage());
			return new ResponseEntity<Response<List<Map<String,Object>>>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Response<List<Map<String,Object>>>>(response, HttpStatus.OK);
	}

	public List<Map<String, Object>> getFlowChartByDefID(int subActivityID, int wfVersion, int projectID, int wfid) {
		// TODO Auto-generated method stub
		return flowChartDao.getFlowChartByDefID(subActivityID, wfVersion, projectID,wfid);
	}

	public Map<String, Object> getAndSaveBotSaving(BotSavingModel botSavingModel) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		// New Query
		int count = configurations.getIntegerProperty(ConfigurationFilesConstants.FLOWCHART_PRECONDITION_COUNT);
		int date =  configurations.getIntegerProperty(ConfigurationFilesConstants.FLOWCHART_PRECONDITION_CLOSED_DATE);
		
		boolean isPreCondtionSatisfied = flowChartDao.isPreCondtionSatisfied(botSavingModel.getOldStepId(), botSavingModel.getOldDefID(), count, date);
		
		LOG.info("isPreCondtionSatisfied:Start" +isPreCondtionSatisfied );
		
		if (botSavingModel.getExecutionType().toUpperCase().equals("MANUAL")) 
		{
			LOG.info("Execution Type Manual:Start");
		
			if(isPreCondtionSatisfied) {
				Map<String, Object> preSavingdata = flowChartDao.checkIfStepExperienced(botSavingModel.getOldStepId(),botSavingModel.getOldDefID());
				
				LOG.info("pre saving data:Start" + preSavingdata);
				if (preSavingdata != null) 
				{				
					Map<String, Object> sData = flowChartDao.getSavingForManualStep(botSavingModel.getOldStepId(),botSavingModel.getOldDefID());
					
					if (sData != null) 
					{
						data.put("Savings", sData.get("Savings"));
						data.put("msg", "20 WO's were executed for this Step.");
						data.put("flag", true);
						data.put("isAll20WOExecuted", true );
					} 
					else {
						data.put("Savings", null);
						data.put("msg", "");
						data.put("flag", false);
					}
				} 
				
				else 
				{
					Map<String, Object> sData = flowChartDao.getSavingForManualStep(botSavingModel.getOldStepId(),botSavingModel.getOldDefID());
					
					if (sData != null) 
					{
						data.put("Savings", sData.get("Savings"));
						data.put("msg", "The minimum WOs were not found. Calculated with available data. Recheck parent mapping");
						data.put("flag", true);
						data.put("isAll20WOExecuted", false );
					} 
					else {
						data.put("Savings", null);
						data.put("msg", "");
						data.put("flag", false);
					}
				}
				data.put("isPreCondtionSatisfied", true);
				LOG.info("Manual Execution:End");
		}
			
			else 
			{
				LOG.info("Execution Type Manual Else Condition:Start");
				Map<String, Object> sData = flowChartDao.getSavingForManualStep(botSavingModel.getOldStepId(),botSavingModel.getOldDefID()); 
				
				if(sData != null) {
					data.put("Savings", sData.get("Savings"));
				}
				else {
					data.put("Savings", null);
				}
				//dSteps.put("Savings", sData.get("Savings"));
				data.put("msg", "No data in the Pre-Period.");
				data.put("isPreCondtionSatisfied", false);
				LOG.info("Execution Type Manual Else Condition:End");

				
			}
			LOG.info("isPreCondtionSatisfied:End");
		}
		
		 else if (botSavingModel.getExecutionType().toUpperCase().equals("AUTOMATIC")) 
		{
			Map<String, Object> sData = flowChartDao.getSavingForAutomaticStep(botSavingModel.getOldDefID(), botSavingModel.getOldStepId());
			if (sData != null) {
				data.put("Savings", sData.get("Saving Calculated"));
				data.put("msg", "20 WO's were executed for this Step.");
				data.put("flag", true);
			} else {
				data.put("Savings", null);
				data.put("msg", "");
				data.put("flag", false);
			}
		}
		 
		 
		return data;

	}

	public Map<String, Object> getSavingForAutomaticStepForOthers(BotSavingModel botSavingModel) {
		Map<String, Object> sData = flowChartDao.getSavingForAutomaticStepForOthers(botSavingModel);
		Map<String, Object> data = new HashMap<String, Object>();
		if (sData != null) {
			data.put("Savings", sData.get("savings"));
			data.put("msg", "successfull");
			data.put("flag", true);
		}else {
			data.put("Savings", null);
			data.put("msg", "Saving Not Found for other Projects");
			data.put("flag", false);
		}
		return data;
	}

	public List<Map<String, Object>> getDeployedBotList(String signumID) {
		List<Map<String, Object>> data = flowChartDao.getDeployedBotList(signumID);
		
		for (Map<String, Object> botData :  data){
			if ((Integer)botData.get("SubActivityFlowChartDefID") == 41655){
				System.out.println("jatin");
			}
			String pre = flowChartDao.getPreSumSaving((Integer)botData.get("RpaID"),(Integer)botData.get("SubActivityFlowChartDefID"));
		//	float preAutomatic = flowChartDao.getPreAutomaticSumSaving((Integer)botData.get("RpaID"),(Integer)botData.get("SubActivityFlowChartDefID"));
			String avgBookingHours = flowChartDao.getAvgBookingHours((Integer)botData.get("RpaID"),(Integer)botData.get("SubActivityFlowChartDefID"));
			botData.put("preManualHour" ,pre);
			botData.put("postManualHour" ,avgBookingHours);
		}
		
		return data;	
	}
	
	public List<Map<String,Object>> getWFBySubActivityId(Integer subActivityId,Integer projectId){
		return this.flowChartDao.getWFBySubActivityId(subActivityId, projectId);
	}

	public boolean updatePostPeriodDates(BotSavingModel botSavingModel) {
		return flowChartDao.updatePostPeriodDates(botSavingModel);
	}
	
	 public String updateWFVersion(int projectId, int subActivityId, int SubActflowChartDefId, int versionOld, int versionNew, String wfName) throws IOException, SQLException 
	 {
		 String status="";
	        try 
	        {
                 
	        	List<HashMap<String,Object>> getwfVersionDetails = flowChartDao.getWFVersionDetails(projectId, subActivityId, SubActflowChartDefId, versionOld);
	        	
	        	if(getwfVersionDetails!=null && getwfVersionDetails.size()>0)
	        	{
	        		JSONArray jarr = new JSONArray(getwfVersionDetails);
	        		for (int i=0; i<jarr.length(); i++)
	        		{
	        			JSONObject obj=jarr.getJSONObject(i);
	        			int exePlanId = obj.getInt("executionPlanDetailId");
	        			String taskJSON= obj.getString("taskJson");
	        			
	        			if(taskJSON.trim().length()>0)
		        		{
		        			JSONObject jsonObject = new JSONObject(taskJSON);
		        			
		        			jsonObject.put("workFlowVersionNo", versionNew);
		        			String workFlow = jsonObject.getString("workflow");
		        			String arrData[] = workFlow.split("/");
		        			if(arrData.length>2) {
		        			workFlow = arrData[0]+"/"+wfName+"/"+versionNew;
		        			jsonObject.put("workflow", workFlow);
		        			}
		        			else {
		        				workFlow = wfName+"/"+versionNew;
			        			jsonObject.put("workflow", workFlow);
		        			}
		        			//Update TaskJson in Table
		        			flowChartDao.updateWFVersionDetails(exePlanId, versionNew, jsonObject.toString());
		        		}
	        		}
	        	}
	        	
	        	status = "SUCCESS";
	        }
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	            throw e;
	        }
	        return status;
	    }
  

	public List<Map<String, Object>> getFlowChartEditReason() {
		return flowChartDao.getFlowChartEditReason();
	}

		private Map<String, Object> parseJsonAndValidateCascade(String wFJson, int subActFCDefID, FlowChartDefModel flowModel,boolean fileBased, int oldDefID, int oldVersionNo) throws Exception {
			Map<String, Object> response = new HashMap<>();
			try {
	        	
	        	List<ErrorBean> lstErr = new ArrayList<>();
	        	ErrorBean errBean = new ErrorBean();
	        	HashMap<String,Object> data = new HashMap<>();
	        	
	            JSONObject jObject = new JSONObject(wFJson);
	            JSONArray arr = jObject.getJSONArray("cells");
	            for (int i = 0; i < arr.length(); i++) {
	                String reason = null;
	                String rpaID = null;
	                String cascadeInput = "NO";
	                
	                JSONObject types = (JSONObject) arr.get(i);
	                if (arr.getJSONObject(i).has("attrs")) {
	                    // CASCADE
	                    if(types.has("cascadeInput")) {
	                    	cascadeInput = types.getString("cascadeInput");
	                    	if(cascadeInput.isEmpty() || cascadeInput.equals(null)) {
	                    		cascadeInput= "NO";
	                    	}
	                    }
	                    if (arr.getJSONObject(i).getJSONObject("attrs").has("task")) {
	                    	if(arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").has("reason")) {
	                        reason = arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").getString("reason");
	                    	}
	                        if(String.valueOf(arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("tool").get("RPAID"))== null
	                        || String.valueOf(arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("tool").get("RPAID")).equals("")
	                        || String.valueOf(arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("tool").get("RPAID")).equals("%RPATOOL_ID%")){
	                            rpaID = "0";
	                        }else{
	                            rpaID = String.valueOf(arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("tool").get("RPAID"));
	                        }
	                        if (StringUtils.equalsIgnoreCase(reason, "%REASON%")) {
	                            reason = "NULL";
	                        }
	                        // CASCADE
	                        if(cascadeInput.equalsIgnoreCase("YES") && rpaID.trim().length()>0 ) {
	                        	HashMap<String, Object> botData= rpaDAO.getRPAIsRunOnServer(Integer.parseInt(rpaID));
	                        	if(null != botData) {
	                        		if(!botData.get("isRunOnServer").equals(1)) {
	                        			errBean.setErrorDescription("Cascade Step Mapping");
	                        			errBean.setDetails("Cascade step can only be mapped to Server BOTS!!");
	                        			errBean.setHowToFix("Update RpaId "+rpaID+" with a server BOT");
	                        			lstErr.add(errBean);
	                        		}
	                        	}else {
	                        		errBean.setErrorDescription("Cascade Step Mapping");
	                        		errBean.setDetails("Cascade step can only be mapped to Server BOTS!!");
	                        		errBean.setHowToFix("Update RpaId "+rpaID+" with a server BOT");
	                        		lstErr.add(errBean);
	                        	}
	                        }
	                        if(lstErr.size()>0) {
	                        	data.put("ErrorFlag",true);
	                            data.put("Error", lstErr);
	                            response.put("ErrorData",data);
	                            return response;
	                        }
	                    
	                }
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            throw ex;
	        }
	        return response;
	    }

		public Response<Void> checkUserAuthenticationForWorkFlow(String signum, String marketArea,String role,int projectId) {
			Response<Void> response=new Response<>();
			
			if(!StringUtils.equalsIgnoreCase(role,"project manager") && !StringUtils.equalsIgnoreCase(role,"Operational Manager") 
					&& !StringUtils.equalsIgnoreCase(role,"Delivery Responsible") && !StringUtils.equalsIgnoreCase(role,"Default User") &&
					!role.contains("Executive User") && !role.contains("Application Admin")) {
				response.addFormError("Not Authorized To Access The URL!!! ");
			}
			else {
				try {
					boolean PM_DR_flag=false;
					boolean OM_flag=false;
					boolean NE_flag=false;
					boolean EU_flag=false;
					boolean ADMIN_flag=false;
							if(StringUtils.equalsIgnoreCase(role,"project manager") || StringUtils.equalsIgnoreCase(role,"Delivery Responsible")) {
								PM_DR_flag = flowChartDao.validateDRAndPM(projectId,signum);
							}
							if(StringUtils.equalsIgnoreCase(role,"Operational Manager")){
								OM_flag = flowChartDao.validateOM(marketArea,signum);
							}
							if(StringUtils.equalsIgnoreCase(role,"Default User")) {
								NE_flag = flowChartDao.validateNE(projectId,signum);
							}
							if(StringUtils.equalsIgnoreCase(role,"Executive User")) {
								EU_flag=flowChartDao.validateEU(marketArea,signum);
							}
							if(StringUtils.equalsIgnoreCase(role,"Application Admin")) {
								ADMIN_flag=flowChartDao.validateAdmin(marketArea,signum);
							}	
							
					if(PM_DR_flag || OM_flag || NE_flag || EU_flag || ADMIN_flag) {
						response.addFormMessage("Authorized to Access URL!!! ");
					}
					else {
						response.addFormError("Not Authorized To Access The URL!!! ");
					}
				}
				catch(Exception e) {
					response.addFormError(e.getMessage());
				}
			}
			return response;
		}
		
		public ResponseEntity<Response<List<Map<String,Object>>>> getForwardReverseWFTransition(){
			Response<List<Map<String,Object>>> response = new Response<>();
			
			try {
				LOG.info("get Forward Reverse WF Transition:Start");
				List<Map<String,Object>> forwardReverseWFTransition = this.flowChartDao.getForwardReverseWFTransition();
				
				if (forwardReverseWFTransition.isEmpty()) {
					response.addFormMessage(NO_DATA_FOUND);
					response.setResponseData(forwardReverseWFTransition);
				} else {
					response.setResponseData(forwardReverseWFTransition);
				}
			    LOG.info("get Forward Reverse WF Transition:End");
			}	
			catch (ApplicationException e) {
				LOG.error(e.getMessage());
				response.addFormMessage(e.getMessage());
				return new ResponseEntity<Response<List<Map<String,Object>>>>(response, HttpStatus.OK);
			}
			catch(Exception ex) {
				LOG.error(ex.getMessage());
				response.addFormError(ex.getMessage());
				return new ResponseEntity<Response<List<Map<String,Object>>>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			return new ResponseEntity<Response<List<Map<String,Object>>>>(response, HttpStatus.OK);
          
		}
		
		
		public ResponseEntity<Response<List<String>>> getListOfViewMode() {
			Response<List<String>> result = new  Response<>();
			
			try {
				LOG.info("getListOfViewMode : Start");	
				List<String> proficiencyNames = this.flowChartDao.getListOfViewMode();
					
				
				if (proficiencyNames.isEmpty()) {
					result.addFormMessage(NO_DATA_FOUND);
					result.setResponseData(proficiencyNames);
				} 
				else {
					result.setResponseData(proficiencyNames);
				}
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
		
		public ResponseEntity<Response<List<KPIValueModel>>> getListOfKPIs(Integer proficiencyLevelSource,String saveMode,Integer subactivityFlowChartDefID){
			Response<List<KPIValueModel>> response = new Response<>();
			
			try {
				LOG.info("get list of KPIs:Start");				
				 if(StringUtils.equalsIgnoreCase(AppConstants.CREATE_NEW,saveMode)) {
				List<KPIValueModel> listOfKPIsForWF = this.flowChartDao.getListOfKPIsForWF(proficiencyLevelSource);
								
				if (listOfKPIsForWF.isEmpty()) {
					response.addFormMessage(NO_DATA_FOUND);
					response.setResponseData(listOfKPIsForWF);
				} else {
					response.setResponseData(listOfKPIsForWF);
				}
			    }
			    else if(StringUtils.equalsIgnoreCase(AppConstants.UPDATE_EXISTING,saveMode)) {
			    	List<KPIValueModel> listOfKPIsForEWF = this.flowChartDao.getListOfKPIsForEWF(subactivityFlowChartDefID);
					
					if (listOfKPIsForEWF.isEmpty()) {
				    List<KPIValueModel> listOfAllKPIsForEWF = this.flowChartDao.getListOfAllKPIsForEWF();
						response.setResponseData(listOfAllKPIsForEWF);
					} else {
						response.setResponseData(listOfKPIsForEWF);
					}
			    }
			    LOG.info("get list of KPIs:End");
			}	
			catch (ApplicationException e) {
				LOG.error(e.getMessage());
				response.addFormError(e.getMessage());
				return new ResponseEntity<Response<List<KPIValueModel>>>(response, HttpStatus.OK);
			}
			catch(Exception ex) {
				LOG.error(ex.getMessage());
				response.addFormError(ex.getMessage());
				return new ResponseEntity<Response<List<KPIValueModel>>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			return new ResponseEntity<Response<List<KPIValueModel>>>(response, HttpStatus.OK);

		}
		
		@Transactional("transactionManager")
		public ResponseEntity<Response<Void>> saveKPIValuesOfWF(List<KpiModel> kpiModelList,FlowChartSaveModel flowChartSaveModel,int oldDefID) throws IOException {
		    Response<Void> response=new Response<Void>();
		  
		    try {
		    	 LOG.info("saveKPIValuesOfWF:Start");
		    	if (CollectionUtils.isNotEmpty(kpiModelList)) {
				    for(KpiModel KpiDataModelList:kpiModelList)
					{
				    	KpiDataModelList.setProjectID(flowChartSaveModel.getProjectID());
				    	KpiDataModelList.setSignum(flowChartSaveModel.getSignumID());
				    	KpiDataModelList.setSubActivityFlowChartDefID(flowChartSaveModel.getFlowChartDefID());
				    	KpiDataModelList.setSubActivityID(flowChartSaveModel.getSubActivityID());
				    	KpiDataModelList.setwFID(flowChartSaveModel.getWorkFlowID());
				    	
				    	
					}
		    	}
		    	if (StringUtils.equalsIgnoreCase(AppConstants.CREATE_NEW,flowChartSaveModel.getSaveMode())) {
  	
						this.flowChartDao.saveKPIValuesOfWF(kpiModelList);
						response.addFormMessage(KPI_VALUES_SAVED);
		    	 }
		    	else if (StringUtils.equalsIgnoreCase(AppConstants.UPDATE_EXISTING,flowChartSaveModel.getSaveMode())) {
		    		 
		    		 this.flowChartDao.updatePreviousKPIValuesOfWF(oldDefID,flowChartSaveModel.getSignumID());
		    		 this.flowChartDao.saveKPIValuesOfWF(kpiModelList);
		    		 response.addFormMessage(KPI_VALUES_SAVED);	
		    	 }
		    	 LOG.info("saveKPIValuesOfWF:End");
				
			}
			catch (ApplicationException e) {
				LOG.error(e.getMessage());
				response.addFormError(e.getMessage());
				throw e;
				
			}
			catch (Exception e) {
				LOG.error(e.getMessage());
				response.addFormError(e.getMessage());
				throw e;
				
			}	
			return new ResponseEntity<Response<Void>>(response, HttpStatus.OK);
		}

	@Transactional("transactionManager")
	public ResponseEntity<Response<Void>> resetProficiency(WorkflowProficiencyModel workFlowProficiencyModel) {
		Response<Void> result = new Response<>();

		try {
			LOG.info("Execution of resetProficiency Started.");
			validateResetProficiency(workFlowProficiencyModel);
			String loggedInSignum = workFlowProficiencyModel.getModifiedBy();
			
			List<Map<String, Object>> listOfAllSignumWfid = flowChartDao.getAllSignumForWfid(workFlowProficiencyModel);
			if(CollectionUtils.isEmpty(listOfAllSignumWfid)) {
				throw new ApplicationException(200, "No Signum Exist For WFID "+workFlowProficiencyModel.getWorkFlowId()+"("+workFlowProficiencyModel.getWorkFlowName()+")"+". So  proficiency cannot be reset!");
			}

            int count=0;
			for (int i = 0; i < listOfAllSignumWfid.size(); i++) {
				 if(listOfAllSignumWfid.get(i)!=null) {
					 WorkflowProficiencyModel latestworkflowProficiencyofUser = flowChartDao.getLatestProficiencyofUser(
								workFlowProficiencyModel, (String) listOfAllSignumWfid.get(i).get(SIGNUM_ID));
						if (latestworkflowProficiencyofUser == null) {
							latestworkflowProficiencyofUser = new WorkflowProficiencyModel();
							latestworkflowProficiencyofUser.setProficiencyLevel(AppConstants.ASSESSED);
						}
						this.flowChartDao.resetProficiency(workFlowProficiencyModel,
								(String) listOfAllSignumWfid.get(i).get(SIGNUM_ID), loggedInSignum);

						int wfUserProficenctID = isfCustomIdInsert
								.generateCustomId(workFlowProficiencyModel.getWfUserProficenctID());
						workFlowProficiencyModel.setWfUserProficenctID(wfUserProficenctID);
						workFlowProficiencyModel.setSignum((String) listOfAllSignumWfid.get(i).get(SIGNUM_ID));
						//workFlowProficiencyModel
						//		.setPreviousProficiencyLevel((String) listOfAllSignumWfid.get(i).get(PROFICIENCY_NAME));
						workFlowProficiencyModel.setProficiencyLevel(AppConstants.ASSESSED);
						
						projectDao.updatePreviousActionInReset(workFlowProficiencyModel);
						projectDao.addProficiencyAction(workFlowProficiencyModel, workFlowProficiencyModel.getProjectId());

						if (StringUtils.equalsIgnoreCase(latestworkflowProficiencyofUser.getProficiencyLevel(),
								AppConstants.EXPERIENCED)) {
							workFlowProficiencyModel.setPreviousProficiencyLevel(AppConstants.EXPERIENCED);
							workFlowProficiencyModel.setProficiencyLevel(AppConstants.ASSESSED);
							workFlowProficiencyModel.setComments(AppConstants.NA);
							Map<String, Object> placeHolder = emailService
									.enrichMailforUpdateProficiency(workFlowProficiencyModel, false);
							emailService.sendMail(AppConstants.NOTIFICATION_UPDATE_PROFICIENCY, placeHolder);
						}
						count++;
				 }
				
			}

			LOG.info("Execution of resetProficiency  Successful.");
			result.addFormMessage(PROFICIENCY_RESET_SUCCESSFULLY);
			return new ResponseEntity<>(result, HttpStatus.OK);	
			
		} catch (ApplicationException e) {
			result.addFormError(e.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			throw ex;

		}
	}

	private void validateResetProficiency(WorkflowProficiencyModel workFlowProficiencyModel) {
		validationUtilityService.validateIntForZero(workFlowProficiencyModel.getProjectId(), AppConstants.PROJECT_ID_2);
		validationUtilityService.validateIntForZero(workFlowProficiencyModel.getWorkFlowId(), AppConstants.WORKFLOW_ID);
		validationUtilityService.validateStringForBlank(workFlowProficiencyModel.getSignum(), AppConstants.SIGNUM);
		validationUtilityService.validateStringForBlank(workFlowProficiencyModel.getModifiedBy(), AppConstants.MODIFIED_BY);
		validationUtilityService.validateIntForZero(workFlowProficiencyModel.getSubActivityId(), AppConstants.SUBACTIVITY_ID);
		validationUtilityService.validateStringForBlank(workFlowProficiencyModel.getTriggeredBy(), AppConstants.TRIGGER_BY);
		validationUtilityService.validateStringForBlank(workFlowProficiencyModel.getWorkFlowName(), AppConstants.WORKFLOW_NAME);
	}

	public ResponseEntity<Response<List<LoeMeasurementCriterionModel>>> getLoeMeasurementCriterion() {
		Response<List<LoeMeasurementCriterionModel>> response = new Response<>();
		
		List<LoeMeasurementCriterionModel> resultList	=	 this.flowChartDao.getLoeMeasurementCriterion();
		
		if(CollectionUtils.isNotEmpty(resultList)) {
			response.setResponseData(resultList);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	public ResponseEntity<Response<List<Map<String, Object>>>> getOldStepBotSavingDetails(
			BotSavingModel botSavingModel , String term) {
		
		int pageLength  = configurations.getIntegerProperty(ConfigurationFilesConstants.FLOWCHART_PAGE_SIZE);
		
		Response<List<Map<String,Object>>> response = new Response<>();	
		try {
			LOG.info("get Old Step Bot Saving Details :Start");
			
			List<Map<String,Object>> oldStepBotSavingDetails = this.flowChartDao.getOldStepBotSavingDetails(botSavingModel , term, pageLength);
			
				response.setResponseData(oldStepBotSavingDetails);
		        LOG.info("get Old Step Bot Saving Details : End");
		}	
		catch (ApplicationException e)
		{
			LOG.error(e.getMessage());
			response.addFormMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		catch(Exception ex) {
			LOG.error(ex.getMessage());
			response.addFormError(ex.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}

