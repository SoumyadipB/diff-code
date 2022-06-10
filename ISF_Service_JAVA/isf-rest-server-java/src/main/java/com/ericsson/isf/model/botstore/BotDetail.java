package com.ericsson.isf.model.botstore;

import java.net.URI;

public class BotDetail 
{
	private String botId;
	private String botName;
	private String botDownloadLocation;

	private String zipInputFilePath;
	private String zipFileName;

	private String woNo;
	private String signum;
	private String projectId;
	private String taskId;
	private String inpath;
	private String nodes;
	private String outpath;
	
	private String outputFileName;
	private boolean isSuccess;
	private String responseMsg;
	private String pFileName;
	private String pFileNameWithFullPath;
	byte[] botOutputFileByteArr= null;
	byte[] pFileContent=null;
	//new byte[77777]
	//new byte[999999999]
	private String downloadBasePath;
	private String isFixedPath;
	
	private String botPlateForm;
	private String isEcnConnected;
	private String botType;
//	byte[] pFileContent;
	
	private String baseVersion;
	
	private URI uri;

	
	public URI getUri() {
		return uri;
	}
	public void setUri(URI uri) {
		this.uri = uri;
	}
	public String getDownloadBasePath() {
		return downloadBasePath;
	}
	public String getIsFixedPath() {
		return isFixedPath;
	}
	public void setIsFixedPath(String isFixedPath) {
		this.isFixedPath = isFixedPath;
	}
	public void setDownloadBasePath(String downloadBasePath) {
		this.downloadBasePath = downloadBasePath;
	}
	public int getpErrorFlag() {
		
		return pErrorFlag;
	}
	public void setpErrorFlag(int pErrorFlag) {
		this.pErrorFlag = pErrorFlag;
	}
	public String getpErrorMsg() {
		return pErrorMsg;
	}
	public void setpErrorMsg(String pErrorMsg) {
		this.pErrorMsg = pErrorMsg;
	}
	int pErrorFlag;
	String pErrorMsg;
	
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public byte[] getBotOutputFileByteArr() {
		return botOutputFileByteArr;
	}
	public void setBotOutputFileByteArr(byte[] botOutputFileByteArr) {
		this.botOutputFileByteArr = botOutputFileByteArr;
	}
	public String getOutputFileName() {
		return outputFileName;
	}
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	public String getWoNo() {
		return woNo;
	}
	public void setWoNo(String woNo) {
		this.woNo = woNo;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getInpath() {
		return inpath;
	}
	public void setInpath(String inpath) {
		this.inpath = inpath;
	}
	public String getNodes() {
		return nodes;
	}
	public void setNodes(String nodes) {
		this.nodes = nodes;
	}
	public String getOutpath() {
		return outpath;
	}
	public String getpFileNameWithFullPath() {
		return pFileNameWithFullPath;
	}
	public void setpFileNameWithFullPath(String pFileNameWithFullPath) {
		this.pFileNameWithFullPath = pFileNameWithFullPath;
	}
	public String getpFileName() {
		return pFileName;
	}
	public void setpFileName(String pFileName) {
		this.pFileName = pFileName;
	}
	public void setOutpath(String outpath) {
		this.outpath = outpath;
	}

	public String getBotDownloadLocation() {
		return botDownloadLocation;
	}
	public void setBotDownloadLocation(String botDownloadLocation) {
		this.botDownloadLocation = botDownloadLocation;
	}
	public String getBotId() {
		return botId;
	}
	public void setBotId(String botId) {
		this.botId = botId;
	}
	public String getBotName() {
		return botName;
	}
	public void setBotName(String botName) {
		this.botName = botName;
	}
	public String getZipInputFilePath() {
		return zipInputFilePath;
	}
	public void setZipInputFilePath(String zipInputFilePath) {
		this.zipInputFilePath = zipInputFilePath;
	}
	public String getZipFileName() {
		return zipFileName;
	}
	public void setZipFileName(String zipFileName) {
		this.zipFileName = zipFileName;
	}
	public byte[] getpFileContent() {
		return pFileContent;
	}
	public void setpFileContent(byte[] pFileContent) {
		this.pFileContent = pFileContent;
	}
	public String getBotPlateForm() {
		return botPlateForm;
	}
	public void setBotPlateForm(String botPlateForm) {
		this.botPlateForm = botPlateForm;
	}
	public String getIsEcnConnected() {
		return isEcnConnected;
	}
	public void setIsEcnConnected(String isEcnConnected) {
		this.isEcnConnected = isEcnConnected;
	}
	public String getBotType() {
		return botType;
	}
	public void setBotType(String botType) {
		this.botType = botType;
	}
	public String getBaseVersion() {
		return baseVersion;
	}
	public void setBaseVersion(String baseVersion) {
		this.baseVersion = baseVersion;
	}
	
}
