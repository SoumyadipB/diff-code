package com.ericsson.isf.iva.model;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class ServerBotModel implements java.io.Serializable {

	private String signumID;
	private int wOID=0;
	private int projectId=0;
	private int taskID=0;
	private String stepID;
	private int flowChartDefID=0;
	private String decisionValue;
	private String exeType;
	private int botId=0;
	private String inPath;
	private String nodes;
	private String botPlatform;
	private int subActivityFlowChartDefID=0;
	private String type;
	private String botType;
	private String isECNConnected;
	private String forTesting;
	private String bookingType;
	private String executionType;
	private String reason;
	private int bookingID=0;
	private String refferer;
	private String status;
	private String outputLink;
	private String qBookingId;
	private String outputUpload;
	private int prevStepTaskID;
	private String externalSourceName;
	private int versionNO;

	public int getPrevStepTaskID() {
		return prevStepTaskID;
	}


		
	public void setPrevStepTaskID(int prevStepTaskID) {
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

	public int getwOID() {
		return wOID;
	}

	public void setwOID(int wOID) {
		this.wOID = wOID;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public String getStepID() {
		return stepID;
	}

	public void setStepID(String stepID) {
		this.stepID = stepID;
	}

	public int getFlowChartDefID() {
		return flowChartDefID;
	}

	public void setFlowChartDefID(int flowChartDefID) {
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

	public int getBotId() {
		return botId;
	}

	public void setBotId(int botId) {
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

	public int getSubActivityFlowChartDefID() {
		return subActivityFlowChartDefID;
	}

	public void setSubActivityFlowChartDefID(int subActivityFlowChartDefID) {
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
	
    public int getVersionNO() {
		return versionNO;
	}

	public void setVersionNO(int versionNO) {
		this.versionNO = versionNO;
	}
	public ServerBotModel(int wOID, String signumID, int taskID, String executionType, String bookingType,
			int subActivityFlowChartDefID, String decisionValue, String botPlatform, String reason, String stepID,
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
		super();
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
				+ ", prevStepTaskID=" + prevStepTaskID + ", externalSourceName=" + externalSourceName + "]";
	}


}
