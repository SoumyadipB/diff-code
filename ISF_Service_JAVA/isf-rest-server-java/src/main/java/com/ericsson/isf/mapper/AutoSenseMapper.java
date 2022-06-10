package com.ericsson.isf.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.model.AutoSenseRuleFlowchartModel;
import com.ericsson.isf.model.AutoSenseRuleModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.MigrationAutoSenseRuleModel;
import com.ericsson.isf.model.RuleMigrationModel;
import com.ericsson.isf.model.RuleModel;
import com.ericsson.isf.model.StepAutoSenseRuleScanner;
import com.ericsson.isf.model.TaskDetailModel;
import com.ericsson.isf.model.TmpWorkflowStepAutoSenseRuleModel;
import com.ericsson.isf.model.WorkOrderColumnMappingModel;

public interface AutoSenseMapper {

	public List<AutoSenseRuleModel> getRulesForTaskID(@Param("taskId") int taskId,@Param("ruleType") String ruleType);

	public void saveAutoSenseRule(@Param("ruleModel") AutoSenseRuleModel ruleModel);

	public List<AutoSenseRuleModel> getAutosenseRuleName(@Param("ruleName")String ruleName);


	public List<AutoSenseRuleFlowchartModel> getAllRulesForFlowchart(@Param("flowchartDefID") Integer flowchartDefID, @Param("woID") Integer woID);
	 
	public List<AutoSenseRuleModel> getAllRulesInMasterData(@Param("dataTableRequset") DataTableRequest dataTableRequset);

	public List<Map<String,Object>> getAllQueuedTask(@Param("Signum") String woID);

	public Boolean checkAutoSenseRuleID(@Param("ruleId") int ruleId);

	public void deleteAutoSenseRule(@Param("ruleId") int ruleId, @Param("signum") String signum,  @Param("deleteValue") int deleteValue);

	public void updateWorkOrderAutoSenseStatus(@Param("woID") Integer woID,@Param("flowchartDefID") Integer flowchartDefID,@Param("autoSenseFlag") boolean autoSenseFlag,@Param("signum")String signum, @Param("versionNum") Integer versionNum);

	public void saveStepRuleInAutoSenseTmpTable(@Param("tmpWorkFlowStepAutoSenseRule") TmpWorkflowStepAutoSenseRuleModel tmpWorkFlowStepAutoSenseRule);

	public List<Map<String,Object>> getRuleDescriptionForStep(@Param("stepID")String stepID, @Param("flowChartDefID") String flowChartDefID);

	public void activeInactiveAutoSenseRule(@Param("autoSenseRuleModel") AutoSenseRuleModel autoSenseRuleModel,
			 @Param("signum") String signum);
	
	public void editAutoSenseRule(@Param("autoSenseRuleModel") AutoSenseRuleModel autoSenseRuleModel,
			 @Param("signum") String signum);

	public Boolean checkActiveAutoSenseRuleID(@Param("ruleId")int ruleId);
	
	public List<AutoSenseRuleModel> getRuleJsonByID(@Param("ruleID") int ruleID);

	public int getMaxDeletedValue(@Param("ruleModel") AutoSenseRuleModel ruleModel);
	
	public int deleteStepRuleFromAutoSenseTmpTable(@Param("tmpWorkFlowStepAutoSenseRule")TmpWorkflowStepAutoSenseRuleModel tmpWorkFlowStepAutoSenseRule);

	public void deleteSameRuleWithDeletedStatus(@Param("ruleModel") AutoSenseRuleModel ruleModel);

	@MapKey("taskActionName")
	public Map<String, Object> getRulesByStepIDNew(@Param("stepID") String stepID,@Param("flowchartDefID") int flowchartDefID);
	
	@MapKey("taskActionName")
	public Map<String, Object> getRulesByStepID(@Param("stepID") String stepID,@Param("flowchartDefID") int flowchartDefID);
	
	public void editStepRuleInAutoSenseTmpTable(@Param("tmpWorkFlowStepAutoSenseRule")  TmpWorkflowStepAutoSenseRuleModel tmpWorkFlowStepAutoSenseRule);

	public boolean checkDataExistInAutoSenseTmpTable(@Param("stepIDStepNameTaskId")String stepIDStepNameTaskId,@Param("projectIdSubIdWfOwner")String projectIdSubIdWfOwner,
			@Param("taskActionName")String taskActionName);

	public List<AutoSenseRuleModel> getRulesForStepFromTemp(@Param("tmpWorkFlowStepAutoSenseRule")TmpWorkflowStepAutoSenseRuleModel tmpWorkFlowStepAutoSenseRule);

	public List<TaskDetailModel> getTaskDetailsBySubactivityID( @Param("subActivityID") int subActivityID);

	public List<AutoSenseRuleModel> getRulesForTaskIDProjectID(@Param("projectId") int projectId, @Param("taskId") int taskId,@Param("ruleType") String ruleType);

	public void saveWfDataAutoSenseTempTable(@Param("subActivityFlowchartDefId")int[] subActivityFlowchartDefId,@Param("signum") String signum);

	public List<Map<String,Object>> getWOColumnMappingJsonForMarketAndNode(@Param("woID") Integer woID, @Param("reqColumn") String reqColumn);

	public Boolean isWorkFlowAutoSenseEnabled(@Param("flowchartDefId") int flowchartDefId);
	
	public void saveScannerSensedRule(@Param("stepAutoSenseRuleScanner")StepAutoSenseRuleScanner stepAutoSenseRuleScanner);

	public int deleteDataAutoSenseTempTable(@Param("projectIDSubactivityIDLoggedInSignum")String projectIDSubactivityIDLoggedInSignum);

	public WorkOrderColumnMappingModel getWOColumnMappingJsonFromWOD1(@Param("woID")Integer woID);

	public List<Map<String,Object>> getWOColumnMappingJsonFromWOD2(@Param("woID")Integer woID);

	public Integer getStepDetailsID(@Param("stepID")String stepID,@Param("flowchartdefid") int flowchartdefid);

	public List<Map<String, Object>> getAllNextStepData(@Param("stepID")String stepID, @Param("defID") Integer defID);

	public void updateMigrationAutoSenseRule(@Param("migrationAutoSenseRuleModel") MigrationAutoSenseRuleModel migrationAutoSenseRuleModel,
			 @Param("signum") String signum);
	
	public List<MigrationAutoSenseRuleModel> getAutosenseMigrationRuleName(@Param("ruleName")String ruleName, @Param("ruleMigrationID")int ruleMigrationID);
	
	public void deleteMigrationAutoSenseRule(@Param("migrationAutoSenseRuleModel") MigrationAutoSenseRuleModel migrationAutoSenseRuleModel);
	
	public void manualValidationMigrationAutoSenseRule(@Param("migrationAutoSenseRuleModel") MigrationAutoSenseRuleModel migrationAutoSenseRuleModel,
			 @Param("signum") String signum);

	public int deleteEditedStepRuleFromAutoSenseTmpTable(
			@Param("tmpWorkflowStepAutoSenseRuleModel")TmpWorkflowStepAutoSenseRuleModel tmpWorkflowStepAutoSenseRuleModel);

	public List<MigrationAutoSenseRuleModel> getAllRuleMigrationDetails(@Param("creatorSignum") String creatorSignum);

	
	public MigrationAutoSenseRuleModel getRuleJsonByMigrationID(@Param("ruleMigrationID") int ruleMigrationID);

	public List<AutoSenseRuleModel> getDuplicateAutosenseRuleName(@Param("ruleName")String ruleName, @Param("ruleId")int ruleId);

	public int insertMigrationData(@Param("ruleData") RuleMigrationModel ruleData);

	public void updateRuleName(@Param("ruleData") RuleMigrationModel ruleData);
	
	public List<MigrationAutoSenseRuleModel> getAutosenseMigrationRuleNames(@Param("ruleName")String ruleName);
	
	public void saveMigrationAutoSenseRule(@Param("migrationAutoSenseRuleModel") MigrationAutoSenseRuleModel migrationAutoSenseRuleModel,
			 @Param("signum") String signum);

	public boolean isTaskValid(@Param("taskId") Integer taskId);

	public HashMap<String, Object> getTaskData(@Param("taskId") Integer taskId);

	public void completeSensedRules(@Param("signum") String signum,@Param("overrideActionName") String overrideActionName,@Param("source") String source);

	public List<Map<String, Object>> downloadAutoSenseRuleExcel();
}
