package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

public class InProgressTaskModel {
	private int bookingID;
	private String type;
	private String status;
	private int flowChartDefID;
	private String flowChartStepID;
	private String stepName;
	private String executionType;
	private int rpaID;
	private String stepType;
	private int taskID;
	private Date startDate;
	private Date endDate;
	private Integer hours;
	private int woID;
	private int woPlanID;
	private String task;
	private String description;
	private int projectID;
	private String projectName;
	private int versionNumber;
	private String outputUpload;
	private int doID;
	private Date plannedEndDate;
	private String woName;
	private String flowcharttype;
	private Boolean workOrderAutoSenseEnabled;
	private boolean workFlowAutoSenseEnabled;
	private Boolean isStepAutoSenseEnabled;
	private String workFlow;
	private boolean startRule;
	private boolean stopRule;
	private int serverTime;
	private double totalEffort;
	private List<String> nodeNames;
	private String botType;
	private List<InProgressNextStepModal> nextSteps;
	private ProficiencyTypeModal proficiencyType;
	private int subActivityID;
	
	public int getBookingID() {
		return bookingID;
	}
	public void setBookingID(int bookingID) {
		this.bookingID = bookingID;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getFlowChartDefID() {
		return flowChartDefID;
	}
	public void setFlowChartDefID(int flowChartDefID) {
		this.flowChartDefID = flowChartDefID;
	}
	public String getFlowChartStepID() {
		return flowChartStepID;
	}
	public void setFlowChartStepID(String flowChartStepID) {
		this.flowChartStepID = flowChartStepID;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getExecutionType() {
		return executionType;
	}
	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}
	public int getRpaID() {
		return rpaID;
	}
	public void setRpaID(int rpaID) {
		this.rpaID = rpaID;
	}
	public String getStepType() {
		return stepType;
	}
	public void setStepType(String stepType) {
		this.stepType = stepType;
	}
	public int getTaskID() {
		return taskID;
	}
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
	public Integer getHours() {
		return hours;
	}
	public void setHours(Integer hours) {
		this.hours = hours;
	}
	public int getWoID() {
		return woID;
	}
	public void setWoID(int woID) {
		this.woID = woID;
	}
	public int getWoPlanID() {
		return woPlanID;
	}
	public void setWoPlanID(int woPlanID) {
		this.woPlanID = woPlanID;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getOutputUpload() {
		return outputUpload;
	}
	public void setOutputUpload(String outputUpload) {
		this.outputUpload = outputUpload;
	}
	public int getDoID() {
		return doID;
	}
	public void setDoID(int doID) {
		this.doID = doID;
	}
	public String getWoName() {
		return woName;
	}
	public void setWoName(String woName) {
		this.woName = woName;
	}
	public String getFlowcharttype() {
		return flowcharttype;
	}
	public void setFlowcharttype(String flowcharttype) {
		this.flowcharttype = flowcharttype;
	}
	public boolean getWorkFlowAutoSenseEnabled() {
		return workFlowAutoSenseEnabled;
	}
	public void setWorkFlowAutoSenseEnabled(boolean workFlowAutoSenseEnabled) {
		this.workFlowAutoSenseEnabled = workFlowAutoSenseEnabled;
	}
	public String getWorkFlow() {
		return workFlow;
	}
	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}
	public boolean isStartRule() {
		return startRule;
	}
	public void setStartRule(boolean startRule) {
		this.startRule = startRule;
	}
	public boolean isStopRule() {
		return stopRule;
	}
	public void setStopRule(boolean stopRule) {
		this.stopRule = stopRule;
	}
	public int getServerTime() {
		return serverTime;
	}
	public void setServerTime(int serverTime) {
		this.serverTime = serverTime;
	}
	public double getTotalEffort() {
		return totalEffort;
	}
	public void setTotalEffort(double totalEffort) {
		this.totalEffort = totalEffort;
	}
	public List<String> getNodeNames() {
		return nodeNames;
	}
	public void setNodeNames(List<String> nodeNames) {
		this.nodeNames = nodeNames;
	}
	public String getBotType() {
		return botType;
	}
	public void setBotType(String botType) {
		this.botType = botType;
	}
	public List<InProgressNextStepModal> getNextSteps() {
		return nextSteps;
	}
	public void setNextSteps(List<InProgressNextStepModal> nextSteps) {
		this.nextSteps = nextSteps;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getPlannedEndDate() {
		return plannedEndDate;
	}
	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}
	public Boolean getWorkOrderAutoSenseEnabled() {
		return workOrderAutoSenseEnabled;
	}
	public void setWorkOrderAutoSenseEnabled(Boolean workOrderAutoSenseEnabled) {
		this.workOrderAutoSenseEnabled = workOrderAutoSenseEnabled;
	}
	public Boolean getIsStepAutoSenseEnabled() {
		return isStepAutoSenseEnabled;
	}
	public void setIsStepAutoSenseEnabled(Boolean isStepAutoSenseEnabled) {
		this.isStepAutoSenseEnabled = isStepAutoSenseEnabled;
	}
	public ProficiencyTypeModal getProficiencyType() {
		return proficiencyType;
	}
	public void setProficiencyType(ProficiencyTypeModal proficiencyType) {
		this.proficiencyType = proficiencyType;
	}
	public int getSubActivityID() {
		return subActivityID;
	}
	public void setSubActivityID(int subActivityID) {
		this.subActivityID = subActivityID;
	}

}
