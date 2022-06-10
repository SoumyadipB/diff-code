package com.ericsson.isf.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.AutoSenseMapper;
import com.ericsson.isf.model.AutoSenseRuleFlowchartModel;
import com.ericsson.isf.model.AutoSenseRuleModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.MigrationAutoSenseRuleModel;
import com.ericsson.isf.model.RuleMigrationModel;
import com.ericsson.isf.model.StepAutoSenseRuleScanner;
import com.ericsson.isf.model.TaskDetailModel;
import com.ericsson.isf.model.TmpWorkflowStepAutoSenseRuleModel;
import com.ericsson.isf.model.WorkOrderColumnMappingModel;

@Repository
public class AutoSenseDao {

	@Qualifier("sqlSession")
	@Autowired
	private SqlSessionTemplate sqlSession;

	public List<AutoSenseRuleModel> getRulesForTaskID(int taskId, String ruleType) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getRulesForTaskID(taskId,ruleType);
	}

	public void saveAutoSenseRule(AutoSenseRuleModel ruleModel) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		autoSenseMapper.saveAutoSenseRule(ruleModel);

	}

	public List<AutoSenseRuleModel> getAutosenseRuleName(String ruleName) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getAutosenseRuleName( ruleName);

	}

	public List<AutoSenseRuleFlowchartModel> getAllRulesForFlowchart(Integer flowchartDefID, Integer woID) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getAllRulesForFlowchart(flowchartDefID, woID);
	}

	public List<AutoSenseRuleModel> getAllRulesInMasterData(DataTableRequest dataTableRequset) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getAllRulesInMasterData(dataTableRequset);
	}

	public List<Map<String, Object>> getAllQueuedTask(String Signum) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getAllQueuedTask(Signum);
	}

	public Boolean checkAutoSenseRuleID(int ruleId) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.checkAutoSenseRuleID(ruleId);
	}

	public void deleteAutoSenseRule(int ruleId, String signum, int deleteValue) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		autoSenseMapper.deleteAutoSenseRule(ruleId, signum, deleteValue);

	}

	public void updateWorkOrderAutoSenseStatus(Integer woID, Integer flowchartDefID, boolean autoSenseFlag,
			String signum, Integer versionNum) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		autoSenseMapper.updateWorkOrderAutoSenseStatus(woID, flowchartDefID, autoSenseFlag, signum,versionNum);
	}

	public void saveStepRuleInAutoSenseTmpTable(TmpWorkflowStepAutoSenseRuleModel tmpWorkFlowStepAutoSenseRule) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		autoSenseMapper.saveStepRuleInAutoSenseTmpTable(tmpWorkFlowStepAutoSenseRule);
	}

	public void activeInactiveAutoSenseRule(AutoSenseRuleModel autoSenseRuleModel, String signum) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		autoSenseMapper.activeInactiveAutoSenseRule(autoSenseRuleModel, signum);
	}

	public List<Map<String, Object>> getRuleDescriptionForStep(String stepID, String flowChartDefID) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getRuleDescriptionForStep(stepID,flowChartDefID);
	}

	public List<AutoSenseRuleModel> getRuleJsonByID(int ruleID) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getRuleJsonByID(ruleID);
	}

	public void editAutoSenseRule(AutoSenseRuleModel autoSenseRuleModel, String signum) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		autoSenseMapper.editAutoSenseRule(autoSenseRuleModel, signum);
	}

	public int getMaxDeletedValue(AutoSenseRuleModel ruleModel) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getMaxDeletedValue(ruleModel);
	}

	public int deleteStepRuleFromAutoSenseTmpTable(TmpWorkflowStepAutoSenseRuleModel tmpWorkFlowStepAutoSenseRule) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.deleteStepRuleFromAutoSenseTmpTable(tmpWorkFlowStepAutoSenseRule);
	}

	public Map<String,Object> getRulesByStepIDNew(String stepID, int flowchartDefID) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getRulesByStepIDNew(stepID, flowchartDefID);
	}
	public Map<String, Object> getRulesByStepID(String stepID, int flowchartDefID) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getRulesByStepID(stepID, flowchartDefID);
	}
	
	public boolean checkDataExistInAutoSenseTmpTable(String stepIDStepNameTaskId, String projectIdSubIdWfOwner,
			String taskActionName) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.checkDataExistInAutoSenseTmpTable(stepIDStepNameTaskId, projectIdSubIdWfOwner,
				taskActionName);
	}

	public List<AutoSenseRuleModel> getRulesForStepFromTemp(TmpWorkflowStepAutoSenseRuleModel tmpWorkFlowStepAutoSenseRule) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getRulesForStepFromTemp(tmpWorkFlowStepAutoSenseRule);
	}
	
	
	public List<TaskDetailModel> getTaskDetailsBySubactivityID(int subActivityID) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getTaskDetailsBySubactivityID(subActivityID);
	
	}

	public List<AutoSenseRuleModel> getRulesForTaskIDProjectID(int projectId, int taskId, String ruleType) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getRulesForTaskIDProjectID(projectId, taskId,ruleType);
	}

	public void saveWfDataAutoSenseTempTable(int[] subActivityFlowchartDefId, String signum) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		autoSenseMapper.saveWfDataAutoSenseTempTable(subActivityFlowchartDefId,signum);
		
	}

	public List<Map<String, Object>> getWOColumnMappingJsonForMarketAndNode(Integer woID, String reqColumn) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getWOColumnMappingJsonForMarketAndNode(woID,reqColumn);
	}

	public Boolean isWorkFlowAutoSenseEnabled(int flowchartDefId) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.isWorkFlowAutoSenseEnabled(flowchartDefId);
	}
	
	public int deleteDataAutoSenseTempTable(String projectIDSubactivityIDLoggedInSignum) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.deleteDataAutoSenseTempTable(projectIDSubactivityIDLoggedInSignum);
	}
	
	public void saveScannerSensedRule(StepAutoSenseRuleScanner stepAutoSenseRuleScanner) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		autoSenseMapper.saveScannerSensedRule(stepAutoSenseRuleScanner);
	}

	public WorkOrderColumnMappingModel getWOColumnMappingJsonFromWOD1(Integer woID) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getWOColumnMappingJsonFromWOD1(woID);
	}
	
	public List<Map<String,Object>> getWOColumnMappingJsonFromWOD2(Integer woID) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getWOColumnMappingJsonFromWOD2(woID);
	}

	public Integer getStepDetailsID(String stepID, int flowchartdefid) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getStepDetailsID( stepID, flowchartdefid);
		
	}

	public List<Map<String, Object>> getAllNextStepData(String stepID, Integer defID) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getAllNextStepData( stepID, defID);
	}
	
	public void updateMigrationAutoSenseRule(MigrationAutoSenseRuleModel migrationAutoSenseRuleModel, String signum) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		autoSenseMapper.updateMigrationAutoSenseRule(migrationAutoSenseRuleModel, signum);
	}
	
	public List<MigrationAutoSenseRuleModel> getAutosenseMigrationRuleName(String ruleName, int ruleMigrationID) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getAutosenseMigrationRuleName( ruleName,ruleMigrationID);

	}
	
	public void deleteMigrationAutoSenseRule(MigrationAutoSenseRuleModel migrationAutoSenseRuleModel) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		autoSenseMapper.deleteMigrationAutoSenseRule(migrationAutoSenseRuleModel);
	}
	
	public void manualValidationMigrationAutoSenseRule(MigrationAutoSenseRuleModel migrationAutoSenseRuleModel, String signum) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		autoSenseMapper.manualValidationMigrationAutoSenseRule(migrationAutoSenseRuleModel,signum);
	}

	public int deleteEditedStepRuleFromAutoSenseTmpTable(
			TmpWorkflowStepAutoSenseRuleModel tmpWorkflowStepAutoSenseRuleModel) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.deleteEditedStepRuleFromAutoSenseTmpTable(tmpWorkflowStepAutoSenseRuleModel);
	}

	public List<MigrationAutoSenseRuleModel> getAllRuleMigrationDetails(String creatorSignum) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
			return autoSenseMapper.getAllRuleMigrationDetails(creatorSignum);
		}

	
	public MigrationAutoSenseRuleModel getRuleJsonByMigrationID(int ruleMigrationID) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getRuleJsonByMigrationID(ruleMigrationID);
		
	}

	public List<AutoSenseRuleModel> getDuplicateAutosenseRuleName(String ruleName, int ruleId) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getDuplicateAutosenseRuleName( ruleName,ruleId);

	}

	public int insertMigrationData(RuleMigrationModel ruleData) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.insertMigrationData(ruleData);
	}

	public void updateRuleName(RuleMigrationModel ruleData) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		autoSenseMapper.updateRuleName(ruleData);		
	}
	
	public List<MigrationAutoSenseRuleModel> getAutosenseMigrationRuleNames(String ruleName) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getAutosenseMigrationRuleNames( ruleName);
	}
	
	public void saveMigrationAutoSenseRule(MigrationAutoSenseRuleModel migrationAutoSenseRuleModel, String signum) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		autoSenseMapper.saveMigrationAutoSenseRule(migrationAutoSenseRuleModel, signum);
	}

	public boolean isTaskValid(Integer taskId) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.isTaskValid(taskId);
	}

	public HashMap<String, Object> getTaskData(Integer taskId) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.getTaskData(taskId);	
		}

	public void completeSensedRules(String signum, String overrideActionName, String source) {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		autoSenseMapper.completeSensedRules(signum, overrideActionName, source);	
	}

	public List<Map<String, Object>> downloadAutoSenseRuleExcel() {
		AutoSenseMapper autoSenseMapper = sqlSession.getMapper(AutoSenseMapper.class);
		return autoSenseMapper.downloadAutoSenseRuleExcel();
	}
	
}
