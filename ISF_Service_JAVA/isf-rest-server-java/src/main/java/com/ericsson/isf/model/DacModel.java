package com.ericsson.isf.model;

import java.util.List;

public class DacModel {

	private int projectID;
	private List<String> dac_Signum;
	private List<String> dac_EmailID;
	private List<String> dac_Name;
	
	public List<String> getDac_EmailID() {
		return dac_EmailID;
	}
	public void setDac_EmailID(List<String> dac_EmailID) {
		this.dac_EmailID = dac_EmailID;
	}
	public List<String> getDac_Name() {
		return dac_Name;
	}
	public void setDac_Name(List<String> dac_Name) {
		this.dac_Name = dac_Name;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public List<String> getDac_Signum() {
		return dac_Signum;
	}
	public void setDac_Signum(List<String> dac_Signum) {
		this.dac_Signum = dac_Signum;
	}
}
