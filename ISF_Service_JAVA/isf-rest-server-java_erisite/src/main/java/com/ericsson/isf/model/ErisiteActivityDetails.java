package com.ericsson.isf.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErisiteActivityDetails {
   
	private ErisiteActivityCoreDetails erisiteActivityCoreDetails;
	private ErisiteParentReferences erisiteParentReferences;
	private ErisiteSchedule erisiteSchedule;
	private ErisiteMileage erisiteMileage;
	private List <ErisiteAssignment> erisiteAssignment;
	private List <ErisiteDocumentContainer> erisiteDocumentContainer;
	private List <ErisiteChecklist> erisiteChecklist;
	private List <ErisiteReworkOrInterrupt> erisiteReworkOrInterrupt;
	
	@JsonProperty("info:Assignment")
	public List<ErisiteAssignment> getErisiteAssignment() {
		return erisiteAssignment;
	}
	@JsonProperty("info:Assignment")
	public void setErisiteAssignment(List<ErisiteAssignment> erisiteAssignment) {
		this.erisiteAssignment = erisiteAssignment;
	}
	@JsonProperty("info:ActivityCoreDetails")
	public ErisiteActivityCoreDetails getErisiteActivityCoreDetails() {
		return erisiteActivityCoreDetails;
	}
	@JsonProperty("info:ActivityCoreDetails")
	public void setErisiteFaultInfo(ErisiteActivityCoreDetails erisiteActivityCoreDetails) {
		this.erisiteActivityCoreDetails = erisiteActivityCoreDetails;
	}
	@JsonProperty("info:ParentReferences")
	public ErisiteParentReferences getErisiteParentReferences() {
		return erisiteParentReferences;
	}
	@JsonProperty("info:ParentReferences")
	public void setErisiteParentReferences(
			ErisiteParentReferences erisiteParentReferences) {
		this.erisiteParentReferences = erisiteParentReferences;
	}
	@JsonProperty("info:Schedule")
	public ErisiteSchedule getErisiteSchedule() {
		return erisiteSchedule;
	}
	@JsonProperty("info:Schedule")
	public void setErisiteSchedule(ErisiteSchedule erisiteSchedule) {
		this.erisiteSchedule = erisiteSchedule;
	}
	@JsonProperty("info:Mileage")
	public ErisiteMileage getErisiteMileage() {
		return erisiteMileage;
	}
	@JsonProperty("info:Mileage")
	public void setErisiteMileage(ErisiteMileage erisiteMileage) {
		this.erisiteMileage = erisiteMileage;
	
	}
	@JsonProperty("info:DocumentContainer")
	public List<ErisiteDocumentContainer> getErisiteDocumentContainer() {
		return erisiteDocumentContainer;
	}
	@JsonProperty("info:DocumentContainer")
	public void setErisiteDocumentContainer(
			List<ErisiteDocumentContainer> erisiteDocumentContainer) {
		this.erisiteDocumentContainer = erisiteDocumentContainer;
	}
	@JsonProperty("info:Checklist")
	public List<ErisiteChecklist> getErisiteChecklist() {
		return erisiteChecklist;
	}
	@JsonProperty("info:Checklist")
	public void setErisiteChecklist(List<ErisiteChecklist> erisiteChecklist) {
		this.erisiteChecklist = erisiteChecklist;
	}
	@JsonProperty("info:ReworkOrInterrupt")
	public List<ErisiteReworkOrInterrupt> getErisiteReworkOrInterrupt() {
		return erisiteReworkOrInterrupt;
	}
	@JsonProperty("info:ReworkOrInterrupt")
	public void setErisiteReworkOrInterrupt(
			List<ErisiteReworkOrInterrupt> erisiteReworkOrInterrupt) {
		this.erisiteReworkOrInterrupt = erisiteReworkOrInterrupt;
	}
}
