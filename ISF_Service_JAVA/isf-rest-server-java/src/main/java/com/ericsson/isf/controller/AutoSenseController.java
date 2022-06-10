
package com.ericsson.isf.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.model.AutoSenseRuleFlowchartModel;
import com.ericsson.isf.model.AutoSenseRuleModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.MigrationAutoSenseRuleModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.RuleMigrationModel;
import com.ericsson.isf.model.StepAutoSenseRuleScanner;
import com.ericsson.isf.model.TaskDetailModel;
import com.ericsson.isf.model.TmpWorkflowStepAutoSenseRuleModel;
import com.ericsson.isf.service.AutoSenseService;

import net.minidev.json.parser.ParseException;

@RestController
@RequestMapping("/autoSense")
public class AutoSenseController {

	private static final Logger LOG = LoggerFactory.getLogger(AutoSenseController.class);

	@Autowired
	private AutoSenseService autoSenseService;

	/**
	 * @author ekarmuj 
	 * Api name:getRulesForTaskIDProjectID 
	 * Purpose:This Api used to get rules for given TaskId and ProjectId.
	 * @param projectId
	 * @param taskId
	 * @return list of auto sense rule model.
	 */

	@RequestMapping(value = "/getRulesForTaskIDProjectID", method = RequestMethod.GET)
	public Response<List<AutoSenseRuleModel>> getRulesForTaskIDProjectID(@RequestParam("projectId") int projectId,
			@RequestParam("taskId") int taskId, @RequestParam("ruleType") String ruleType) {
		return autoSenseService.getRulesForTaskIDProjectID(projectId, taskId, ruleType);
	}

	/**
	 * @author ekarmuj 
	 * Api getRulesForTaskID 
	 * Purpose:This Api used to get rules for given TaskId.
	 * @param taskId
	 * @return list of auto sense rule model.
	 */

	@RequestMapping(value = "/getRulesForTaskID", method = RequestMethod.GET)
	public Response<List<AutoSenseRuleModel>> getRulesForTaskID(@RequestParam("taskId") int taskId,
			@RequestParam("ruleType") String ruleType) {
		return autoSenseService.getRulesForTaskID(taskId, ruleType);
	}

	/**
	 * @author emntiuk
	 * @purpose This API is used for save the Auto sense rule in Master table.
	 * @param ruleModel
	 * @param signum
	 * @return Response<Void>
	 */
	@RequestMapping(value = "/saveAutoSenseRule", method = RequestMethod.POST)
	public Response<Integer> saveAutoSenseRule(@RequestBody AutoSenseRuleModel ruleModel,
			@RequestHeader("signum") String signum) {
		
		return autoSenseService.saveAutoSenseRule(ruleModel, signum);
	}

	/**
	 * @author ekmbuma
	 * @purpose Fetch All Rules for a flowchart
	 * @param flowchartDefID
	 * @param woID
	 * @return Response<List<AutoSenseRuleFlowchartModel>>
	 */
	@RequestMapping(value = "/getAllRulesForFlowchart", method = RequestMethod.GET)
	public List<AutoSenseRuleFlowchartModel> getAllRulesForFlowchart(
			@RequestParam(value = "flowchartDefID", required = true) Integer flowchartDefID,
			@RequestParam(value = "woID", required = true) Integer woID) {

		LOG.info("getAllRulesForFlowchart [ {} ] : Success", woID);
		return autoSenseService.getAllRulesForFlowchart(flowchartDefID, woID);
	}

	/**
	 * @author elkpain
	 * @purpose This api gets the list of all rules from master data
	 * @return Map<String, Object>
	 */

	@RequestMapping(value = "/getAllRulesInMasterData", method = RequestMethod.POST)
	public Map<String, Object> getAllRulesInMasterData(HttpServletRequest request) {
		LOG.info("getAllRulesInMasterData : Success");
		DataTableRequest dataTableRequset=new DataTableRequest(request);
		return this.autoSenseService.getAllRulesInMasterData(dataTableRequset);
	}

	/**
	 * @purpose To save autoSense value(0,1) for work order depending upon yes/no
	 *          click on UI
	 * @author epnwsia
	 * @param woID
	 * @param flowchartDefID
	 * @param autoSenseFlag
	 * @return
	 */
	@RequestMapping(value = "/updateWorkOrderAutoSenseStatus", method = RequestMethod.POST)
	public Response<Void> updateWorkOrderAutoSenseStatus(@RequestParam(value = "woID") Integer woID,
			@RequestParam(value = "flowchartDefID") Integer flowchartDefID,
			@RequestParam(value = "autoSenseFlag") boolean autoSenseFlag, @RequestHeader("signum") String signum) {
		return autoSenseService.updateWorkOrderAutoSenseStatus(woID, flowchartDefID, autoSenseFlag, signum);
	}

	/**
	 * @author ekmbuma
	 * @purpose To fetch all queued task
	 * @param Floating Window
	 * @param woID
	 * @return List<Map<String,Object>>
	 */
	@RequestMapping(value = "/getAllQueuedTask", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllQueuedTask(@RequestHeader("Signum") String Signum) {

		LOG.info("getAllQueuedTask [ {} ] : Success", Signum);
		return autoSenseService.getAllQueuedTask(Signum);
	}

	/**
	 * @author emntiuk
	 * @purpose This API is used for delete the rules from master table.
	 * @param ruleModel
	 * @param signum
	 * @return Response<Void>
	 */
	@RequestMapping(value = "/deleteAutoSenseRule", method = RequestMethod.POST)
	public Response<Void> deleteAutoSenseRule(@RequestBody AutoSenseRuleModel ruleModel,
			@RequestHeader("signum") String signum) {
		return autoSenseService.deleteAutoSenseRule(ruleModel, signum);
	}

	/**
	 * @author
	 * @param stepID
	 * @return
	 */
	@RequestMapping(value = "/getRuleDescriptionForStep", method = RequestMethod.GET)
	public Response<List<Map<String, Object>>> getRuleDescriptionForStep(@RequestParam("stepID") String stepID,
			@RequestParam("flowChartDefID") String flowChartDefID) {
		return autoSenseService.getRuleDescriptionForStep(stepID, flowChartDefID);
	}

	/**
	 * @author elkpain
	 * @purpose This API is used for making active rule inactive and vice versa
	 * @param autoSenseRuleModel
	 * @param signum
	 * @return Response<Void>
	 */
	@RequestMapping(value = "/activeInactiveAutoSenseRule", method = RequestMethod.POST)
	public Response<Void> activeInactiveAutoSenseRule(@RequestBody AutoSenseRuleModel autoSenseRuleModel,
			@RequestHeader("signum") String signum) {
		LOG.info("activeInactiveAutoSenseRule : Success");
		return autoSenseService.activeInactiveAutoSenseRule(autoSenseRuleModel, signum);
	}

	/**
	 * @author ekmbuma
	 *
	 * @param woID
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "/getWOColumnMappingJson", method = RequestMethod.POST)
	public Map<String, Object> getWOColumnMappingJson(@RequestParam("woID") Integer woID,
			@RequestBody List<String> reqColumnMap) {

		LOG.info("getWOColumnMappingJson [" + woID + "] : Success");
		return autoSenseService.getWOColumnMappingJson(woID, reqColumnMap);
	}

	/**
	 * @author emntiuk
	 * @purpose This API is used for fetch the ruleJson of given ruleID
	 * @param ruleID
	 * @return Response<String>
	 */
	@RequestMapping(value = "/getRuleJsonByID", method = RequestMethod.GET)
	public Response<List<AutoSenseRuleModel>> getRuleJsonByID(
			@RequestParam(value = "ruleID", required = true) int ruleID) {
		LOG.info("getRuleJsonByID : Success");
		return autoSenseService.getRuleJsonByID(ruleID);
	}

	/**
	 * @author elkpain
	 * @purpose This API is used for editing the rule
	 * @param autoSenseRuleModel
	 * @param signum
	 * @return Response<Void>
	 */
	@RequestMapping(value = "/editAutoSenseRule", method = RequestMethod.POST)
	public Response<Void> editAutoSenseRule(@RequestBody AutoSenseRuleModel autoSenseRuleModel,
			@RequestHeader("signum") String signum) {
		LOG.info("editAutoSenseRule : Success");
		return autoSenseService.editAutoSenseRule(autoSenseRuleModel, signum);
	}

	/**
	 * @author ekarmuj 
	 * Api name: saveStepRuleInAutoSenseTmpTable 
	 * Purpose: Used to save step rule in auto sense temp table.
	 * @param tmpWorkFlowStepAutoSenseRule
	 * @return Response<Void>
	 */

	@RequestMapping(value = "/saveStepRuleInAutoSenseTmpTable", method = RequestMethod.POST)
	public Response<Void> saveStepRuleInAutoSenseTmpTable(
			@RequestBody TmpWorkflowStepAutoSenseRuleModel tmpWorkFlowStepAutoSenseRule,
			@RequestHeader("signum") String signum) {
		tmpWorkFlowStepAutoSenseRule.setCreatedBy(signum);
		return autoSenseService.saveStepRuleInAutoSenseTmpTable(tmpWorkFlowStepAutoSenseRule);
	}

	/**
	 * @author ekarmuj 
	 * Api name: deleteStepRuleFromAutoSenseTmpTable 
	 * Purpose: Used to delete step rule in auto sense temporary table.
	 * @param tmpWorkFlowStepAutoSenseRule
	 * @return Response<Void>
	 */

	@RequestMapping(value = "/deleteStepRuleFromAutoSenseTmpTable", method = RequestMethod.POST)
	public Response<Void> deleteStepRuleFromAutoSenseTmpTable(
			@RequestBody List<TmpWorkflowStepAutoSenseRuleModel> tmpWorkFlowStepAutoSenseRule) {
		return autoSenseService.deleteStepRuleFromAutoSenseTmpTable(tmpWorkFlowStepAutoSenseRule);
	}

	/**
	 * @author ekarmuj 
	 * Api name: editStepRuleInAutoSenseTmpTable 
	 * Purpose: Used to delete step rule in auto sense temp table.
	 * @param tmpWorkFlowStepAutoSenseRule
	 * @return Response<Void>
	 */

	@RequestMapping(value = "/editStepRuleInAutoSenseTmpTable", method = RequestMethod.POST)
	public Response<Void> editStepRuleInAutoSenseTmpTable(
			@RequestBody List<TmpWorkflowStepAutoSenseRuleModel> tmpWorkFlowStepAutoSenseRule,
			@RequestHeader("signum") String signum) {
		return autoSenseService.editStepRuleInAutoSenseTmpTable(tmpWorkFlowStepAutoSenseRule, signum);
	}

	/**
	 * @author ekarmuj
	 * Api getRulesForStepFromTemp
	 * Purpose:This Api used to get rules for given StepIdTAskId and ProjectIdSubActivityIdLoggedInSignum.
	 * @param projectId
	 * @param taskId
	 * @return list of auto sense rule model.
	 */

	@RequestMapping(value = "/getRulesForStepFromTemp", method = RequestMethod.POST)
	public Response<List<AutoSenseRuleModel>> getRulesForStepFromTemp(
			@RequestBody TmpWorkflowStepAutoSenseRuleModel tmpWorkFlowStepAutoSenseRule) {
		return autoSenseService.getRulesForStepFromTemp(tmpWorkFlowStepAutoSenseRule);
	}

	/**
	 * @author emntiuk
	 * 
	 * @param subActivityID
	 * @return Response<List<TaskDetailModel>>
	 */
	@RequestMapping(value = "/getTaskDetailsBySubactivityID", method = RequestMethod.GET)
	public Response<List<TaskDetailModel>> getTaskDetailsBySubactivityID(
			@RequestParam("subActivityID") int subActivityID) {
		LOG.info("getTaskDetailsBySubactivityID : Success");
		return autoSenseService.getTaskDetailsBySubactivityID(subActivityID);
	}

	/**
	 * @author ekarmuj 
	 * Api Name: saveWfDataAutoSenseTempTable 
	 * Purpose: Api used to save data in Temp table form Auto sense main Table(tbl_workflowstepautosenserule).
	 * @param subActivityFlowchartDefId
	 * @return Response<Void>
	 */

	@RequestMapping(value = "/saveWfDataAutoSenseTempTable", method = RequestMethod.POST)
	public Response<Void> saveWfDataAutoSenseTempTable(
			@RequestParam("subActivityFlowchartDefId") int[] subActivityFlowchartDefId,@RequestHeader("signum") String signum) {
		return autoSenseService.saveWfDataAutoSenseTempTable(subActivityFlowchartDefId,signum);
	}

	/**
	 * @author ekarmuj 
	 * Api Name: deleteDataAutoSenseTempTable 
	 * Purpose: Api used to delete data from temporary Table based on projectIDSubactivityIDWfOwner.
	 * @param projectIDSubactivityIDWfOwner
	 * @return Response<Void>
	 */

	@RequestMapping(value = "/deleteDataAutoSenseTempTable", method = RequestMethod.POST)
	public Response<Void> deleteDataAutoSenseTempTable(
			@RequestParam("projectIDSubactivityIDLoggedInSignum") String projectIDSubactivityIDLoggedInSignum) {
		return autoSenseService.deleteDataAutoSenseTempTable(projectIDSubactivityIDLoggedInSignum);
	}

	/**
	  * API used By Scanner to insert into transactionalData.TBL_StepAutoSenseRuleScanner
	  * for every rule sensing start and completed
	  * @param stepAutoSenseRuleScanner
	  * @return
	  */
	 @RequestMapping(value = "/saveScannerSensedRule", method = RequestMethod.POST)
		public Response<Void> saveScannerSensedRule(@RequestBody StepAutoSenseRuleScanner stepAutoSenseRuleScanner) {
		 	LOG.info("saveScannerSensedRule : BEGIN");
	        return autoSenseService.saveScannerSensedRule(stepAutoSenseRuleScanner);
	    }
	 
	 /**
	  * @author ekmbuma
	  * @purpose fetch all next step data
	  * 
	  * @param stepID
	  * @param defID
	  * @return List<Map<String, Object>>
	  */
		@RequestMapping(value = "/getAllNextStepData/{stepID}/{defID}", method = RequestMethod.GET)
		public Response<List<Map<String, Object>>> getAllNextStepData(@PathVariable("stepID") String stepID,
				@PathVariable("defID") Integer defID) {
			
	        LOG.info("getAllNextStepData : Success");
			return autoSenseService.getAllNextStepData(stepID, defID);
		}
	 
	
	@RequestMapping(value = "/migrateRuleJSON", method = RequestMethod.POST ,consumes = "multipart/form-data")
	public Response<RuleMigrationModel> migrateRuleJSON(
			@RequestParam("file") MultipartFile file,
			@RequestHeader("Signum") String signum) throws ParseException, IOException {
		return autoSenseService.migrateRuleJSON(file,signum);
	}

	/**
	 * @author elkpain
	 * @purpose This API is used for editing the Rules in Rule Migration
	 * @param migrationAutoSenseRuleModel
	 * @param signum
	 * @return Response<Void>
	 */
	@RequestMapping(value = "/updateMigrationAutoSenseRule", method = RequestMethod.POST)
	public Response<Void> updateMigrationAutoSenseRule(
			@RequestBody MigrationAutoSenseRuleModel migrationAutoSenseRuleModel,
			@RequestHeader("signum") String signum) {
		LOG.info("updateMigrationAutoSenseRule : Success");
		return autoSenseService.updateMigrationAutoSenseRule(migrationAutoSenseRuleModel, signum);
	}

	/**
	 * @author elkpain
	 * @purpose This API is used to hard delete the rule from rule migration
	 * @param migrationAutoSenseRuleModel
	 * @return Response<Void>
	 */
	@RequestMapping(value = "/deleteMigrationAutoSenseRule", method = RequestMethod.POST)
	public Response<Void> deleteMigrationAutoSenseRule(
			@RequestBody MigrationAutoSenseRuleModel migrationAutoSenseRuleModel) {
		LOG.info("deleteMigrationAutoSenseRule : Success");
		return autoSenseService.deleteMigrationAutoSenseRule(migrationAutoSenseRuleModel);
	}

	/**
	 * @author elkpain
	 * @purpose This API is used for Manual Validation of Rule
	 * @param migrationAutoSenseRuleModel
	 * @param signum
	 * @return Response<Void>
	 */
	@RequestMapping(value = "/manualValidationMigrationAutoSenseRule", method = RequestMethod.POST)
    public  Response<Void>  manualValidationMigrationAutoSenseRule(@RequestBody MigrationAutoSenseRuleModel migrationAutoSenseRuleModel,
    		@RequestHeader("signum") String signum) {
		LOG.info("manualValidationMigrationAutoSenseRule : Success");
    	return autoSenseService.manualValidationMigrationAutoSenseRule(migrationAutoSenseRuleModel,signum);
    }
	
	/**
	 * @author emntiuk
	 * @purpose This API is used for get ALL data from Rule Migration Table according to creatorSignum
	 * @return
	 */
	@RequestMapping(value = "/getAllRuleMigrationDetails", method = RequestMethod.GET)
	public Response<List<MigrationAutoSenseRuleModel>> getAllRuleMigrationDetails(@RequestParam("creatorSignum") String creatorSignum) {
		LOG.info("getAllRuleMigrationDetails : Success");
		return this.autoSenseService.getAllRuleMigrationDetails(creatorSignum);
	}
	
	
	
	/**
	 * 
	 * @param ruleMigrationID
	 * @return
	 */
	@RequestMapping(value = "/getRuleJsonByMigrationID", method = RequestMethod.GET)
	public Response<MigrationAutoSenseRuleModel> getRuleJsonByMigrationID(
			@RequestParam(value = "ruleMigrationID", required = true) int ruleMigrationID) {
		LOG.info("getRuleJsonByMigrationID : Success");
		return autoSenseService.getRuleJsonByMigrationID(ruleMigrationID);
	}	
		
		/**
		 * @author elkpain
		 * @purpose This API is used for save the Auto sense rule in TBL_rule_migration.
		 * @param migrationAutoSenseRuleModel
		 * @param signum
		 * @return Response<Void>
		 */
		@RequestMapping(value = "/saveMigrationAutoSenseRule", method = RequestMethod.POST)
		public Response<Integer> saveMigrationAutoSenseRule(@RequestBody MigrationAutoSenseRuleModel migrationAutoSenseRuleModel,
				@RequestHeader("signum") String signum) {
			LOG.info("saveMigrationAutoSenseRule : Success");
			return autoSenseService.saveMigrationAutoSenseRule(migrationAutoSenseRuleModel,signum);
		}  
	

	/**
	 * @purpose complete all the started sensing 
	 * @author EHRMSNG
	 * @param signum
	 * @param overrideActionName
	 * @param source
	 * @return
	 */
	@RequestMapping(value = "/completeSensedRules", method = RequestMethod.POST)
	public Response<Void> completeSensedRules(@RequestHeader("signum") String signum ,
			@RequestParam(value = "overrideActionName", required = true) String overrideActionName,
			@RequestParam(value = "source", required = true) String source) {
		LOG.info("completeSensedRules : BEGIN");
		return autoSenseService.completeSensedRules(signum,overrideActionName,source);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	 @RequestMapping(value = "/downloadAutoSenseRuleExcel", method = RequestMethod.GET)
	   	public DownloadTemplateModel downloadAutoSenseRuleExcel() throws IOException {
	   				LOG.info("downloadAutoSenseRuleExcel : Success");
	   				return this.autoSenseService.downloadAutoSenseRuleExcel();
	   			}
	
}
