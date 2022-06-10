package com.ericsson.isf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErisiteNroActivityEvent {

	String changeCode;
	String hasFault;
	private ErisiteFaultInfo erisiteFaultInfo;
	private ErisiteActivityDetails erisiteActivityDetails;
	
	@JsonProperty("info:changeCode")
	public String getChangeCode() {
		return changeCode;
	}
	@JsonProperty("info:changeCode")
	public void setChangeCode(String changeCode) {
		this.changeCode = changeCode;
	}
	@JsonProperty("info:hasFault")
	public String getHasFault() {
		return hasFault;
	}
	@JsonProperty("info:hasFault")
	public void setHasFault(String hasFault) {
		this.hasFault = hasFault;
	}
	@JsonProperty("info:FaultInfo")
	public ErisiteFaultInfo getErisiteFaultInfo() {
		return erisiteFaultInfo;
	}
	@JsonProperty("info:FaultInfo")
	public void setErisiteFaultInfo(ErisiteFaultInfo erisiteFaultInfo) {
		this.erisiteFaultInfo = erisiteFaultInfo;
	}
	@JsonProperty("info:ActivityDetails")
	public ErisiteActivityDetails getErisiteActivityDetails() {
		return erisiteActivityDetails;
	}
	@JsonProperty("info:ActivityDetails")
	public void setErisiteActivityDetails(
			ErisiteActivityDetails erisiteActivityDetails) {
		this.erisiteActivityDetails = erisiteActivityDetails;
	}

}
