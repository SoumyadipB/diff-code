package com.ericsson.isf.model;

public class BulkUploadModel {
	private String fileTable;
	private String signum;
	private Integer recordsInserted;
	
	public Integer getRecordsInserted() {
		return recordsInserted;
	}
	public void setRecordsInserted(Integer recordsInserted) {
		this.recordsInserted = recordsInserted;
	}
	public String getFileName() {
		return fileTable;
	}
	public void setFileName(String fileTable) {
		this.fileTable = fileTable;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	

}
