package com.ericsson.isf.dao;

import com.ericsson.isf.mapper.ActivityMasterMapper;
import com.ericsson.isf.mapper.FlowChartMapper;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.TestMapper;
import com.ericsson.isf.model.FlowChartDefModel;
import com.ericsson.isf.model.FlowChartDependencyModel;
import com.ericsson.isf.model.FlowChartStepInformationModel;
import com.ericsson.isf.model.FlowChartStepModel;
import com.ericsson.isf.model.TestProject;

@Repository
public class TestDAO {
    
    @Qualifier("sqlSession") /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;

	public List<TestProject> getProjects() {
		TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
		return testMapper.getProjects();
	}

    public List<Integer> getSubActivityList(int projectID) {
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        return testMapper.getSubActivityList(projectID);
    }

    public List<FlowChartDefModel> getFlowChartJSONForSID(int projectID,Integer sid) {
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        return testMapper.getFlowChartJSONForSID(projectID,sid);
    }

    public List<FlowChartStepInformationModel> getFlowChartStepDetails(int subActivityFlowChartDefID, int versionNumber) {
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        return testMapper.getFlowChartStepDetails(subActivityFlowChartDefID,versionNumber);
    }

    public String getToolName(int toolID) {
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        return testMapper.getToolName(toolID);
    }

    public void updateFlowChartJSON(int projectID, int subActivityID, int subActivityFlowChartDefID, int versionNumber, String flowChartJSON) {
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        testMapper.updateFlowChartJSON(projectID,subActivityID,subActivityFlowChartDefID,versionNumber,flowChartJSON);
    }

    public void saveUpdateStatus(int projectID, int subActivityID, int subActivityFlowChartDefID, int versionNumber, String status) {
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        testMapper.saveUpdateStatus(projectID,subActivityID,subActivityFlowChartDefID,versionNumber,status);
    }

    public List<Integer> getProjectID() {
          TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
           return testMapper.getProjectID();
    }

    public Boolean checkIFDataExists(Integer projectID, Integer subActivityID, int subActivityFlowChartDefID, int versionNumber) {
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
           return testMapper.checkIFDataExists(projectID,subActivityID,subActivityFlowChartDefID,versionNumber);
    }

    public List<Integer> getFlowChartDefID() {
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
           return testMapper.getFlowChartDefID();
    }

    public String getFlowChartJSON(int defID) {
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        return testMapper.getFlowChartJSON(defID);
    }

    public List<Integer> getFlowChartDefIDForStep() {
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
           return testMapper.getFlowChartDefIDForStep();
    }

    public FlowChartDefModel getFlowChartJSONForStep(int defID) {
         TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        return testMapper.getFlowChartJSONForStep(defID);
    }

    public void updateFlowChartStepDetailsValue(int subActFCDefID, String stepID, String stepName, String taskID, String taskName,
                                                String toolID,int versionNumber, String stepType,String rpaID) {
         TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
         testMapper.updateFlowChartStepDetailsValue(subActFCDefID,stepID,stepName,taskID,taskName,toolID,versionNumber,stepType,rpaID); 
    }
    
    public void insertInFlowChartStepDetails(Integer subActFCDefID,String stepId, String stepName, String taskId, String taskName, String executionType, 
                                              String toolId,String result,int versionNO,String masterTask,String stepType,String rpaID){
       TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
       testMapper.insertInFlowChartStepDetails(subActFCDefID,stepId,stepName,taskId,taskName,executionType,
                                                    toolId,result,versionNO,masterTask,stepType,rpaID); 
    }

    public List<Integer> getFlowChartDefIDToUpdateJSON() {
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        return testMapper.getFlowChartDefIDToUpdateJSON();
    }

	public void uploadExcel(String fileName, int subActivityFlowChartDefID) {
		TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        testMapper.uploadExcel(fileName,subActivityFlowChartDefID);
		
	}

	public List<FlowChartStepModel> getFlowChartStepDetails(int subActivityFlowChartDefID) {
		TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        return testMapper.getFlowChartStepDetails(subActivityFlowChartDefID);
	}

	public List<FlowChartDependencyModel> getDependencyStep(int subActivityFlowChartDefID) {
		TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        return testMapper.getDependencyStep(subActivityFlowChartDefID);
	}

	public int getFlowChartSubActivityID(int flowChartDefID) {
		TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        return testMapper.getFlowChartSubActivityID(flowChartDefID);
	}

	public void insertStatus(String status, int flowChartDefID) {
		TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        testMapper.insertStatus(status,flowChartDefID);
		
	}

	public void updateFlowChartJsonDATA(int flowChartDefID, String jsonData) {
		TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        testMapper.updateFlowChartJsonDATA(flowChartDefID,jsonData);
		
	}

	public String viewWorkFlow(int flowChartDefID) {
		TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        return testMapper.viewWorkFlow(flowChartDefID);
	}

	public Map<String, Object> getWorkFlowInformation(int flowChartDefID) {
		TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        return testMapper.getWorkFlowInformation(flowChartDefID);
	}
	
	public Map<String,Object> getTaskDetailForSID(int subActivityID, int taskID) {
		TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        return testMapper.getTaskDetailForSID(subActivityID,taskID);
    }
	
	   public Map<String,String> getToolDetailForWF(int toolID) {
		   TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
	        return testMapper.getToolDetailForWF(toolID);
	    }

	public void deleteMigrationData(int flowChartDefID) {
		TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
         testMapper.deleteMigrationData(flowChartDefID);
		
	}
	
	public void deleteMigrationStepData(int flowChartDefID) {
		TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
         testMapper.deleteMigrationStepData(flowChartDefID);
		
	}

}
