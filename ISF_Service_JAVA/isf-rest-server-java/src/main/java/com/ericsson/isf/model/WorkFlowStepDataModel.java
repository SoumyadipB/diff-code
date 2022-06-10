package com.ericsson.isf.model;

public class WorkFlowStepDataModel {
	private String toolId;
	private String toolName;
	private String outputUpload;
	private int rpaID;
	private String rpaName;
	private String isRunOnServer;
	private boolean isInputRequired;
	private String botType;
	
	public String getBotType() {
		return botType;
	}
	public void setBotType(String botType) {
		this.botType = botType;
	}
	
	public String getToolId() {
		return toolId;
	}
	public void setToolId(String toolId) {
		this.toolId = toolId;
	}
	public String getToolName() {
		return toolName;
	}
	public void setToolName(String toolName) {
		this.toolName = toolName;
	}
	public String getOutputUpload() {
		return outputUpload;
	}
	public void setOutputUpload(String outputUpload) {
		this.outputUpload = outputUpload;
	}
	public String getRpaName() {
		return rpaName;
	}
	public void setRpaName(String rpaName) {
		this.rpaName = rpaName;
	}
	public boolean isInputRequired() {
		return isInputRequired;
	}
	public void setInputRequired(boolean isInputRequired) {
		this.isInputRequired = isInputRequired;
	}
	public String getIsRunOnServer() {
		return isRunOnServer;
	}
	public void setIsRunOnServer(String isRunOnServer) {
		this.isRunOnServer = isRunOnServer;
	}
	public int getRpaID() {
		return rpaID;
	}
	public void setRpaID(int rpaID) {
		this.rpaID = rpaID;
	}	

}
