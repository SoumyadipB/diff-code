package com.ericsson.isf.model;

public class ExportedReportHistoryModel {
	private int ExportedReportHistory;
    public int getExportedReportHistory() {
		return ExportedReportHistory;
	}
	public void setExportedReportHistory(int exportedReportHistory) {
		ExportedReportHistory = exportedReportHistory;
	}
	public String getReportName() {
		return ReportName;
	}
	public void setReportName(String reportName) {
		ReportName = reportName;
	}
	public String getReportFile() {
		return reportFile;
	}
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}
	public String getFileUplodedOn() {
		return FileUplodedOn;
	}
	public void setFileUplodedOn(String fileUplodedOn) {
		FileUplodedOn = fileUplodedOn;
	}
	public String getModifiedOn() {
		return ModifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		ModifiedOn = modifiedOn;
	}
	private String ReportName ;
    private String reportFile  ;
    private String FileUplodedOn ;
    private String ModifiedOn ;
    private String description;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
