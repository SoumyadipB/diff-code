package com.ericsson.isf.model;

public class DownloadTemplateModel {
private byte[] pFileContent;
private String pFileName;

public byte[] getpFileContent() {
	return pFileContent;
}

public void setpFileContent(byte[] pFileContent) {
	this.pFileContent = pFileContent;
}

public String getpFileName() {
	return pFileName;
}

public void setpFileName(String pFileName) {
	this.pFileName = pFileName;
}
}
