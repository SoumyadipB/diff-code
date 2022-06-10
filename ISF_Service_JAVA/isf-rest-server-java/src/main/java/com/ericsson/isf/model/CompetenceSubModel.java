package com.ericsson.isf.model;

import java.util.HashMap;
import java.util.List;

public class CompetenceSubModel {

	private int competenceId;
	private String competenceName;
	private String competenceType;
	
	private int resourceRequestCompetenceId;
	private String  competenceLevel;
	
	
	
	//1	RAN	LEVEL 5
	//List<HashMap<String,Object>> competenceLevelList;

	public int getResourceRequestCompetenceId() {
		return resourceRequestCompetenceId;
	}

	public void setResourceRequestCompetenceId(int resourceRequestCompetenceId) {
		this.resourceRequestCompetenceId = resourceRequestCompetenceId;
	}

	public String getCompetenceLevel() {
		return competenceLevel;
	}

	public void setCompetenceLevel(String competenceLevel) {
		this.competenceLevel = competenceLevel;
	}

	public int getCompetenceId() {
		return competenceId;
	}

	public void setCompetenceId(int competenceId) {
		this.competenceId = competenceId;
	}

	public String getCompetenceName() {
		return competenceName;
	}

	public void setCompetenceName(String competenceName) {
		this.competenceName = competenceName;
	}

	public String getCompetenceType() {
		return competenceType;
	}

	public void setCompetenceType(String competenceType) {
		this.competenceType = competenceType;
	}

/*	public List<HashMap<String, Object>> getCompetenceLevelList() {
		return competenceLevelList;
	}

	public void setCompetenceLevelList(List<HashMap<String, Object>> competenceLevelList) {
		this.competenceLevelList = competenceLevelList;
	}
	*/
	
	
	
}
