package com.ericsson.isf.model;

public class KpiModel {
	 private int wFID;
     private int version;
     private int projectID;
     private int subActivityID;
     private int kpiID;
     private int subActivityFlowChartDefID;
     private String signum;
     private int kPIValueID;
     private Integer kPIValue;
     
     
	public int getwFID() {
		return wFID;
	}
	public void setwFID(int wFID) {
		this.wFID = wFID;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public int getSubActivityID() {
		return subActivityID;
	}
	public void setSubActivityID(int subActivityID) {
		this.subActivityID = subActivityID;
	}
	public int getKpiID() {
		return kpiID;
	}
	public void setKpiID(int kpiID) {
		this.kpiID = kpiID;
	}
	public int getSubActivityFlowChartDefID() {
		return subActivityFlowChartDefID;
	}
	public void setSubActivityFlowChartDefID(int subActivityFlowChartDefID) {
		this.subActivityFlowChartDefID = subActivityFlowChartDefID;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public int getkPIValueID() {
		return kPIValueID;
	}
	public void setkPIValueID(int kPIValueID) {
		this.kPIValueID = kPIValueID;
	}
	public Integer getkPIValue() {
		return kPIValue;
	}
	public void setkPIValue(Integer kPIValue) {
		this.kPIValue = kPIValue;
	}

   
}
