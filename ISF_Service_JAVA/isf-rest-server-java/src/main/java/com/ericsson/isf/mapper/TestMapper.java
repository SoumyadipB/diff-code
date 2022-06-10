package com.ericsson.isf.mapper;

import com.ericsson.isf.model.FlowChartDefModel;
import com.ericsson.isf.model.FlowChartDependencyModel;
import com.ericsson.isf.model.FlowChartStepInformationModel;
import com.ericsson.isf.model.FlowChartStepModel;

import java.util.List;
import java.util.Map;

import com.ericsson.isf.model.TestProject;
import org.apache.ibatis.annotations.Param;

public interface TestMapper {
	public List<TestProject> getProjects();

        public List<Integer> getSubActivityList(@Param("projectID") int projectID );

        public List<FlowChartDefModel> getFlowChartJSONForSID(@Param("projectID") int projectID,@Param("subActivityID") Integer sid);

        public List<FlowChartStepInformationModel> getFlowChartStepDetails(@Param("subActivityFlowChartDefID") int subActivityFlowChartDefID,
                                                                           @Param("versionNumber") int versionNumber);

        public String getToolName(@Param("toolID") int toolID);

        public void updateFlowChartJSON(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID,
                                    @Param("subActivityFlowChartDefID") int subActivityFlowChartDefID,@Param("versionNumber") int versionNumber,
                                    @Param("flowChartJSON")String flowChartJSON);

        public void saveUpdateStatus(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID,
                                    @Param("subActivityFlowChartDefID") int subActivityFlowChartDefID,@Param("versionNumber") int versionNumber,
                                    @Param("status") String status);

    public List<Integer> getProjectID();

    public Boolean checkIFDataExists(@Param("projectID") int projectID,@Param("subActivityID") int subActivityID,
                                    @Param("subActivityFlowChartDefID") int subActivityFlowChartDefID,@Param("versionNumber") int versionNumber);

    public List<Integer> getFlowChartDefID();

    public String getFlowChartJSON(@Param("DefID") int defID);

    public List<Integer> getFlowChartDefIDForStep();

    public FlowChartDefModel getFlowChartJSONForStep(@Param("DefID") int defID);

    public void updateFlowChartStepDetailsValue(@Param("subActFCDefID")Integer subActFCDefID, @Param("stepID") String stepID, 
                                                @Param("stepName") String stepName,
                                                @Param("taskID") String taskID, @Param("taskName") String taskName,
                                                @Param("toolID") String toolID,
                                                @Param("versionNo") int versionNo,@Param("stepType") String stepType,
                                                @Param("rpaID") String rpaID);
    
    public void insertInFlowChartStepDetails(@Param("subActFCDefID")Integer subActFCDefID, @Param("stepId") String stepId, 
                                             @Param("stepName") String stepName, @Param("taskId") String taskId,
                                             @Param("taskName") String taskName, @Param("exeType") String executionType,
                                             @Param("toolId") String toolId,
                                             @Param("reason") String reason,@Param("versionNO") int versionNO,
                                             @Param("masterTask") String masterTask,@Param("stepType") String stepType,
                                             @Param("rpaID") String rpaID);

    public List<Integer> getFlowChartDefIDToUpdateJSON();

	public void uploadExcel(@Param ("FileTable") String fileName,@Param("subActivityFlowChartDefID") int subActivityFlowChartDefID);

	public List<FlowChartStepModel> getFlowChartStepDetails(@Param("subActivityFlowChartDefID") int subActivityFlowChartDefID);

	public List<FlowChartDependencyModel> getDependencyStep(@Param("subActivityFlowChartDefID") int subActivityFlowChartDefID);

	public int getFlowChartSubActivityID(@Param("flowChartDefID") int flowChartDefID);

	public void insertStatus(@Param("status") String status,@Param("flowChartDefID") int flowChartDefID);

	public void updateFlowChartJsonDATA(@Param("flowChartDefID") int flowChartDefID,@Param("jsonData") String jsonData);

	public String viewWorkFlow(@Param("flowChartDefID") int flowChartDefID);

	public Map<String, Object> getWorkFlowInformation(@Param("flowChartDefID") int flowChartDefID);
	
	public Map<String,String> getToolDetailForWF(@Param("toolID") int toolID);
	
	public Map<String,Object> getTaskDetailForSID(@Param("subActivityID") int subActivityID,@Param("taskID") int taskID);

	public void deleteMigrationData(@Param("flowChartDefID") int flowChartDefID);
	
	public void deleteMigrationStepData(@Param("flowChartDefID") int flowChartDefID);

}
