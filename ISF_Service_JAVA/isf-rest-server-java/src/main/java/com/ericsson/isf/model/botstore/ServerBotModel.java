package com.ericsson.isf.model.botstore;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class ServerBotModel implements java.io.Serializable {

	private String signumID;
	
	@NotNull
	private Integer wOID;
	private Integer projectId;
	private Integer taskID;
	private String stepID;
	private Integer flowChartDefID;
	private String decisionValue;
	private String exeType;
	private Integer botId;
	private String inPath;
	private String nodes;
	private String botPlatform;
	private Integer subActivityFlowChartDefID;
	private String type;
	private String botType;
	private String isECNConnected;
	private String forTesting;
	private String bookingType;
	private String executionType;
	private String reason;
	private int bookingID;
	private String refferer;
	private String status;
	public String getServerBotInputUrl() {
		return serverBotInputUrl;
	}

	public void setServerBotInputUrl(String serverBotInputUrl) {
		this.serverBotInputUrl = serverBotInputUrl;
	}

	private String outputLink;
	private String qBookingId;
	private String outputUpload;
	private Integer prevStepTaskID;
	private Integer sourceId;
	@Size(min = 3, message = "Invalid source name lenght, Lenght can't be less than 3")
	@NotEmpty(message = "source name can't be left blank ")
	private String externalSourceName;
	private int proficiencyID;
	private String serverBotInputUrl;
	private String serverBotOutputUrl;



	public String getServerBotOutputUrl() {
		return serverBotOutputUrl;
	}

	public void setServerBotOutputUrl(String serverBotOutputUrl) {
		this.serverBotOutputUrl = serverBotOutputUrl;
	}

	public Integer getPrevStepTaskID() {
		return prevStepTaskID;
	}

	public void setPrevStepTaskID(Integer prevStepTaskID) {
		this.prevStepTaskID = prevStepTaskID;
	}

	public String getRefferer() {
		return refferer;
	}

	public void setRefferer(String refferer) {
		this.refferer = refferer;
	}

	public int getBookingID() {
		return bookingID;
	}

	public void setBookingID(int bookingID) {
		this.bookingID = bookingID;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getExecutionType() {
		return executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	public String getSignumID() {
		return signumID;
	}

	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}

	public Integer getwOID() {
		return wOID;
	}

	public void setwOID(Integer wOID) {
		this.wOID = wOID;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getTaskID() {
		return taskID;
	}

	public void setTaskID(Integer taskID) {
		this.taskID = taskID;
	}

	public String getStepID() {
		return stepID;
	}

	public void setStepID(String stepID) {
		this.stepID = stepID;
	}

	public Integer getFlowChartDefID() {
		return flowChartDefID;
	}

	public void setFlowChartDefID(Integer flowChartDefID) {
		this.flowChartDefID = flowChartDefID;
	}

	public String getDecisionValue() {
		return decisionValue;
	}

	public void setDecisionValue(String decisionValue) {
		this.decisionValue = decisionValue;
	}

	public String getExeType() {
		return exeType;
	}

	public void setExeType(String exeType) {
		this.exeType = exeType;
	}

	public Integer getBotId() {
		return botId;
	}

	public void setBotId(Integer botId) {
		this.botId = botId;
	}

	public String getInPath() {
		return inPath;
	}

	public void setInPath(String inPath) {
		this.inPath = inPath;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public String getBotPlatform() {
		return botPlatform;
	}

	public void setBotPlatform(String botPlatform) {
		this.botPlatform = botPlatform;
	}

	public Integer getSubActivityFlowChartDefID() {
		return subActivityFlowChartDefID;
	}

	public void setSubActivityFlowChartDefID(Integer subActivityFlowChartDefID) {
		this.subActivityFlowChartDefID = subActivityFlowChartDefID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBotType() {
		return botType;
	}

	public void setBotType(String botType) {
		this.botType = botType;
	}

	public String getIsECNConnected() {
		return isECNConnected;
	}

	public void setIsECNConnected(String isECNConnected) {
		this.isECNConnected = isECNConnected;
	}

	public String getForTesting() {
		return forTesting;
	}

	public void setForTesting(String forTesting) {
		this.forTesting = forTesting;
	}

	public String getBookingType() {
		return bookingType;
	}

	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOutputLink() {
		return outputLink;
	}

	public void setOutputLink(String outputLink) {
		this.outputLink = outputLink;
	}

	public String getqBookingId() {
		return qBookingId;
	}

	public void setqBookingId(String qBookingId) {
		this.qBookingId = qBookingId;
	}

	public String getOutputUpload() {
		return outputUpload;
	}

	public void setOutputUpload(String outputUpload) {
		this.outputUpload = outputUpload;
	}
	
	public String getExternalSourceName() {
		return externalSourceName;
	}

	public void setExternalSourceName(String externalSourceName) {
		this.externalSourceName = externalSourceName;
	}
	
	
	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public ServerBotModel(Integer wOID, String signumID, Integer taskID, String executionType, String bookingType,
			Integer subActivityFlowChartDefID, String decisionValue, String botPlatform, String reason, String stepID,
			String refferer, String status) {

		this.signumID = signumID;
		this.wOID = wOID;
		this.taskID = taskID;
		this.decisionValue = decisionValue;
		this.botPlatform = botPlatform;
		this.subActivityFlowChartDefID = subActivityFlowChartDefID;
		this.bookingType = bookingType;
		this.executionType = executionType;
		this.reason = reason;
		this.stepID = stepID;
		this.refferer = refferer;
		this.status = status;
	}

	public ServerBotModel() {
	}
	
	

	public ServerBotModel(ServerBotModel copyServerBotModel) {

		this.signumID = copyServerBotModel.getSignumID();
		this.wOID = copyServerBotModel.getwOID();
		this.projectId = copyServerBotModel.getProjectId();
		this.taskID = copyServerBotModel.getTaskID();
		this.stepID = copyServerBotModel.getStepID();
		this.flowChartDefID = copyServerBotModel.getFlowChartDefID();
		this.decisionValue = copyServerBotModel.getDecisionValue();
		this.exeType = copyServerBotModel.getExeType();
		this.botId = copyServerBotModel.getBotId();
		this.inPath = copyServerBotModel.getInPath();
		this.nodes = copyServerBotModel.getNodes();
		this.botPlatform = copyServerBotModel.getBotPlatform();
		this.subActivityFlowChartDefID = copyServerBotModel.getSubActivityFlowChartDefID();
		this.type = copyServerBotModel.getType();
		this.botType = copyServerBotModel.getBotType();
		this.isECNConnected = copyServerBotModel.getIsECNConnected();
		this.forTesting = copyServerBotModel.getForTesting();
		this.bookingType = copyServerBotModel.getBookingType();
		this.executionType = copyServerBotModel.getExecutionType();
		this.reason = copyServerBotModel.getReason();
		this.bookingID = copyServerBotModel.getBookingID();
		this.refferer = copyServerBotModel.getRefferer();
		this.status = copyServerBotModel.getStatus();
		this.outputLink = copyServerBotModel.getOutputLink();
		this.qBookingId = copyServerBotModel.getqBookingId();
		this.outputUpload = copyServerBotModel.getOutputUpload();
		this.prevStepTaskID = copyServerBotModel.getPrevStepTaskID();
		this.externalSourceName = copyServerBotModel.getExternalSourceName();
		this.sourceId=copyServerBotModel.getSourceId();
	}

	@Override
	public String toString() {
		return "ServerBotModel [signumID=" + signumID + ", wOID=" + wOID + ", projectId=" + projectId + ", taskID="
				+ taskID + ", stepID=" + stepID + ", flowChartDefID=" + flowChartDefID + ", decisionValue="
				+ decisionValue + ", exeType=" + exeType + ", botId=" + botId + ", inPath=" + inPath + ", nodes="
				+ nodes + ", botPlatform=" + botPlatform + ", subActivityFlowChartDefID=" + subActivityFlowChartDefID
				+ ", type=" + type + ", botType=" + botType + ", isECNConnected=" + isECNConnected + ", forTesting="
				+ forTesting + ", bookingType=" + bookingType + ", executionType=" + executionType + ", reason="
				+ reason + ", bookingID=" + bookingID + ", refferer=" + refferer + ", status=" + status
				+ ", outputLink=" + outputLink + ", qBookingId=" + qBookingId + ", outputUpload=" + outputUpload
				+ ", prevStepTaskID=" + prevStepTaskID + ",sourceId=" + sourceId + ", externalSourceName=" + externalSourceName + ", serverBotInputUrl=" + serverBotInputUrl +"]";
	}

	public int getProficiencyID() {
		return proficiencyID;
	}

	public void setProficiencyID(int proficiencyID) {
		this.proficiencyID = proficiencyID;
	}


}
