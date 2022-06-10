package com.ericsson.isf.model;

import org.apache.commons.lang3.StringUtils;

import com.ericsson.isf.util.AppConstants;

public class WoAutoTaskInfoModel {
	private String Signum;
	private int WoId;
	private int BookingId;
	private String StartDate;
	private String EndDate;
	private String FlowChartStepId;
	private String Hour;
	private String Status;
	private String Reason;
	private String OutputLink;
	private boolean IconsOnNextStep;
	private int FlowChartDefID;
	private String ExecutionType;
	private String flowChartType;
	private String refferer;
	private String actionPerfomed;
	private String taskId;
	
	@Override
	public String toString() {
		return "WoAutoTaskInfoModel [Signum=" + Signum + ", WoId=" + WoId + ", BookingId=" + BookingId + ", StartDate="
				+ StartDate + ", EndDate=" + EndDate + ", FlowChartStepId=" + FlowChartStepId + ", Hour=" + Hour
				+ ", Status=" + Status + ", Reason=" + Reason + ", OutputLink=" + OutputLink + ", IconsOnNextStep="
				+ IconsOnNextStep + ", FlowChartDefID=" + FlowChartDefID + ", ExecutionType=" + ExecutionType
				+ ", flowChartType=" + flowChartType + ", refferer=" + refferer + ", actionPerfomed=" + actionPerfomed
				+ ", taskId=" + taskId + ", overrideAction=" + overrideAction + ", startRule=" + startRule
				+ ", stopRule=" + stopRule + ", isInputRequired=" + isInputRequired + ", botType=" + botType
				+ ", isRunOnServer=" + isRunOnServer + ", rpaId=" + rpaId + "]";
	}
	private String overrideAction=StringUtils.EMPTY;
	private Boolean startRule;
	private Boolean stopRule;
	private boolean isInputRequired;
	private String botType;
	private boolean isRunOnServer;
	private int rpaId;
	
	public boolean isInputRequired() {
		return isInputRequired;
	}
	public void setIsInputRequired(boolean isInputRequired) {
		this.isInputRequired = isInputRequired;
	}
	public String getBotType() {
		return botType;
	}
	public void setBotType(String botType) {
		this.botType = botType;
	}
	public int getRpaId() {
		return rpaId;
	}
	public void setRpaId(int rpaId) {
		this.rpaId = rpaId;
	}
	public boolean isRunOnServer() {
		return isRunOnServer;
	}
	public void setIsRunOnServer(boolean isRunOnServer) {
		this.isRunOnServer = isRunOnServer;
	}
	
	public String getRefferer() {
		return refferer;
	}
	public void setRefferer(String refferer) {
		this.refferer = refferer;
	}
	public String getActionPerfomed() {
		return actionPerfomed;
	}
	public void setActionPerfomed(String actionPerfomed) {
		this.actionPerfomed = actionPerfomed;
	}
	public String getSignum() {
		return Signum;
	}
	public String getFlowChartType() {
		return flowChartType;
	}
	public void setFlowChartType(String flowChartType) {
		this.flowChartType = flowChartType;
	}
	public void setSignum(String signum) {
		Signum = signum;
	}
	public int getWoId() {
		return WoId;
	}
	public void setWoId(int woId) {
		WoId = woId;
	}
	public int getBookingId() {
		return BookingId;
	}
	public void setBookingId(int bookingId) {
		BookingId = bookingId;
	}
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public String getEndDate() {
		return EndDate;
	}
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}
	public String getFlowChartStepId() {
		return FlowChartStepId;
	}
	public void setFlowChartStepId(String flowChartStepId) {
		FlowChartStepId = flowChartStepId;
	}
	public String getHour() {
		return Hour;
	}
	public void setHour(String hour) {
		Hour = hour;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getReason() {
		return Reason;
	}
	public void setReason(String reason) {
		Reason = reason;
	}
	public String getOutputLink() {
		return OutputLink;
	}
	public void setOutputLink(String outputLink) {
		OutputLink = outputLink;
	}
	public boolean isIconsOnNextStep() {
		return IconsOnNextStep;
	}
	public void setIconsOnNextStep(boolean iconsOnNextStep) {
		IconsOnNextStep = iconsOnNextStep;
	}
	public int getFlowChartDefID() {
		return FlowChartDefID;
	}
	public void setFlowChartDefID(int flowChartDefID) {
		FlowChartDefID = flowChartDefID;
	}
	public String getExecutionType() {
		return ExecutionType;
	}
	public void setExecutionType(String executionType) {
		ExecutionType = executionType;
	}
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public WoAutoTaskInfoModel() {
		
	}
	
	public WoAutoTaskInfoModel(String signum, int woId, int bookingId,
			String flowChartStepId,Integer taskId,  String hour, String status, String reason,int flowChartDefID,
			String executionType, String flowChartType, String refferer,
			String actionPerfomed) {
		this.Signum = signum;
		this.WoId = woId;
		this.BookingId = bookingId;
		this.FlowChartStepId = flowChartStepId;
		this.Status = status;
		this.Reason = reason;
		if(StringUtils.isNotEmpty(reason) && reason.equalsIgnoreCase(AppConstants.SUCCESS)) {
			this.setIconsOnNextStep(true);
		}
		this.FlowChartDefID = flowChartDefID;
		this.ExecutionType = executionType;
		this.flowChartType = flowChartType;
		this.refferer = refferer;
		this.actionPerfomed = actionPerfomed;
		this.taskId=taskId.toString();
		this.Hour=hour;
	}
	
	public WoAutoTaskInfoModel(String signum, int woId,
			String flowChartStepId,int flowChartDefID, String refferer, String taskId) {
		this.Signum = signum;
		this.WoId = woId;
		this.FlowChartStepId = flowChartStepId;
		this.FlowChartDefID = flowChartDefID;
		this.refferer = refferer;
		this.taskId=taskId;
	}
	public Boolean getStartRule() {
		return startRule;
	}
	public void setStartRule(Boolean startRule) {
		this.startRule = startRule;
	}
	public Boolean getStopRule() {
		return stopRule;
	}
	public void setStopRule(Boolean stopRule) {
		this.stopRule = stopRule;
	}
	public String getOverrideAction() {
		return overrideAction;
	}
	public void setOverrideAction(String overrideAction) {
		this.overrideAction = overrideAction;
	}
	
}
