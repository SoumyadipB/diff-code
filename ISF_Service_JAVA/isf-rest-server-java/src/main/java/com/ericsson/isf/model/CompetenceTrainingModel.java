package com.ericsson.isf.model;

public class CompetenceTrainingModel {

	private int trainingID;
	private String trainingName;
	private String trainingType;
	private String externalTrainingID;
	private int deliveryCompetence;
	private int competenceUpgradeID;
	
	public int getTrainingID() {
		return trainingID;
	}
	public void setTrainingID(int trainingID) {
		this.trainingID = trainingID;
	}
	public String getTrainingName() {
		return trainingName;
	}
	public void setTrainingName(String trainingName) {
		this.trainingName = trainingName;
	}
	public String getTrainingType() {
		return trainingType;
	}
	public void setTrainingType(String trainingType) {
		this.trainingType = trainingType;
	}
	public String getExternalTrainingID() {
		return externalTrainingID;
	}
	public void setExternalTrainingID(String externalTrainingID) {
		this.externalTrainingID = externalTrainingID;
	}
	public int getDeliveryCompetence() {
		return deliveryCompetence;
	}
	public void setDeliveryCompetence(int deliveryCompetence) {
		this.deliveryCompetence = deliveryCompetence;
	}
	public int getCompetenceUpgradeID() {
		return competenceUpgradeID;
	}
	public void setCompetenceUpgradeID(int competenceUpgradeID) {
		this.competenceUpgradeID = competenceUpgradeID;
	}
	

}
