package com.ericsson.isf.model;

public class KPIValueModel {
     
     private int kpiID;
     private String kpiName;
     private String kpiDataType;
     private Integer kpiDefaultValue;
     private Integer kpiMinValue;
     private Integer kpiMaxValue;
     private String kpiDescription;
     private boolean mandatory;
     private boolean active;
     private int kPIValueID;
     private Integer kpiValue;
     private String flag;
     
	public int getKpiID() {
		return kpiID;
	}
	public void setKpiID(int kpiID) {
		this.kpiID = kpiID;
	}
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	public String getKpiDataType() {
		return kpiDataType;
	}
	public void setKpiDataType(String kpiDataType) {
		this.kpiDataType = kpiDataType;
	}
	public String getKpiDescription() {
		return kpiDescription;
	}
	public void setKpiDescription(String kpiDescription) {
		this.kpiDescription = kpiDescription;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getkPIValueID() {
		return kPIValueID;
	}
	public void setkPIValueID(int kPIValueID) {
		this.kPIValueID = kPIValueID;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Integer getKpiDefaultValue() {
		return kpiDefaultValue;
	}
	public void setKpiDefaultValue(Integer kpiDefaultValue) {
		this.kpiDefaultValue = kpiDefaultValue;
	}
	public Integer getKpiMinValue() {
		return kpiMinValue;
	}
	public void setKpiMinValue(Integer kpiMinValue) {
		this.kpiMinValue = kpiMinValue;
	}
	public Integer getKpiMaxValue() {
		return kpiMaxValue;
	}
	public void setKpiMaxValue(Integer kpiMaxValue) {
		this.kpiMaxValue = kpiMaxValue;
	}
	public Integer getKpiValue() {
		return kpiValue;
	}
	public void setKpiValue(Integer kpiValue) {
		this.kpiValue = kpiValue;
	}
     
}