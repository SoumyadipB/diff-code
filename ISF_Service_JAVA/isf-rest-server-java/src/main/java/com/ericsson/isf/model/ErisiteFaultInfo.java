package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErisiteFaultInfo {
	
	private String timeStamp;
	private String faultCode;
	private String faultMessage;
	private String detailedMessage;
	private String reportingUnit;
	private String logItemId;
	
	@JsonProperty("info:TimeStamp")
	public String getTimeStamp() {
		return timeStamp;
	}
	@JsonProperty("info:TimeStamp")
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	@JsonProperty("info:FaultCode")
	public String getFaultCode() {
		return faultCode;
	}
	@JsonProperty("info:FaultCode")
	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}
	@JsonProperty("info:FaultMessage")
	public String getFaultMessage() {
		return faultMessage;
	}
	@JsonProperty("info:FaultMessage")
	public void setFaultMessage(String faultMessage) {
		this.faultMessage = faultMessage;
	}
	@JsonProperty("info:DetailedMessage")
	public String getDetailedMessage() {
		return detailedMessage;
	}
	@JsonProperty("info:DetailedMessage")
	public void setDetailedMessage(String detailedMessage) {
		this.detailedMessage = detailedMessage;
	}
	@JsonProperty("info:ReportingUnit")
	public String getReportingUnit() {
		return reportingUnit;
	}
	@JsonProperty("info:ReportingUnit")
	public void setReportingUnit(String reportingUnit) {
		this.reportingUnit = reportingUnit;
	}
	@JsonProperty("info:LogItemId")
	public String getLogItemId() {
		return logItemId;
	}
	@JsonProperty("info:LogItemId")
	public void setLogItemId(String logItemId) {
		this.logItemId = logItemId;
	}
}
