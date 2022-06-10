/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

/**
 *
 * @author ekarath
 */
public class FlowChartDefModel {
    
    private int subActivityFlowChartDefID;
    private Integer parentFlowChartDefID;
    private int projectID;
    private int subActivityID;
    private String flowChartJSON;
    private int sourceProjectID;
    private String createdBy;
    private Date createdDate;
    private Boolean active;
    private int versionNumber;
    private String type;
    private String workFlowName;
   
    private boolean neNeeded;
  
    private String wfOwner;
    private float slaHours;
    private String ftrValue;
    private Integer wFID;
    private String wfEditReason;
    private boolean enableField ;
    private boolean isAutoSenseEnable;
    private boolean experiencedFlow;
    private Integer loeMeasurementCriterionID;
    

	public boolean isExperiencedFlow() {
		return experiencedFlow;
	}

	public void setExperiencedFlow(boolean experiencedFlow) {
		this.experiencedFlow = experiencedFlow;
	}

	public String getWorkFlowName() {
        return workFlowName;
    }

    public void setWorkFlowName(String workFlowName) {
        this.workFlowName = workFlowName;
    }

    public int getSubActivityFlowChartDefID() {
        return subActivityFlowChartDefID;
    }

    public void setSubActivityFlowChartDefID(int subActivityFlowChartDefID) {
        this.subActivityFlowChartDefID = subActivityFlowChartDefID;
    }

    public int getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
    }

    public String getFlowChartJSON() {
        return flowChartJSON;
    }

    public void setFlowChartJSON(String flowChartJSON) {
        this.flowChartJSON = flowChartJSON;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getSourceProjectID() {
        return sourceProjectID;
    }

    public void setSourceProjectID(int sourceProjectID) {
        this.sourceProjectID = sourceProjectID;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

   

    public boolean isNeNeeded() {
        return neNeeded;
    }

    public void setNeNeeded(boolean neNeeded) {
        this.neNeeded = neNeeded;
    }

   

    public String getWfOwner() {
        return wfOwner;
    }

    public void setWfOwner(String wfOwner) {
        this.wfOwner = wfOwner;
    }

    public float getSlaHours() {
        return slaHours;
    }

    public void setSlaHours(float slaHours) {
        this.slaHours = slaHours;
    }

    public String getFtrValue() {
        return ftrValue;
    }

    public void setFtrValue(String ftrValue) {
        this.ftrValue = ftrValue;
    }

	public Integer getWFID() {
		return wFID;
	}

	public void setWFID(Integer wFID) {
		this.wFID = wFID;
	}

	public Integer getParentFlowChartDefID() {
		return parentFlowChartDefID;
	}

	public void setParentFlowChartDefID(Integer parentFlowChartDefID) {
		this.parentFlowChartDefID = parentFlowChartDefID;
	}

	public String getWfEditReason() {
		return wfEditReason;
	}

	public void setWfEditReason(String wfEditReason) {
		this.wfEditReason = wfEditReason;
	}
	public boolean isEnableField() {
		return enableField;
	}

	public void setEnableField(boolean enableField) {
		this.enableField = enableField;
	}

	@Override
	public String toString() {
		return "FlowChartDefModel [subActivityFlowChartDefID=" + subActivityFlowChartDefID + ", parentFlowChartDefID="
				+ parentFlowChartDefID + ", projectID=" + projectID + ", subActivityID=" + subActivityID
				+ ", flowChartJSON=" + flowChartJSON + ", sourceProjectID=" + sourceProjectID + ", createdBy="
				+ createdBy + ", createdDate=" + createdDate + ", active=" + active + ", versionNumber=" + versionNumber
				+ ", type=" + type + ", workFlowName=" + workFlowName + 
				 ", neNeeded=" + neNeeded + 
				 ", wfOwner=" + wfOwner + ", slaHours=" + slaHours + ", ftrValue=" + ftrValue + ", wFID=" + wFID
				+ ", wfEditReason=" + wfEditReason + ", enableField=" + enableField + "]";
	}

	public boolean isAutoSenseEnable() {
		return isAutoSenseEnable;
	}

	public void setAutoSenseEnable(boolean isAutoSenseEnable) {
		this.isAutoSenseEnable = isAutoSenseEnable;
	}

	public Integer getLoeMeasurementCriterionID() {
		return loeMeasurementCriterionID;
	}

	public void setLoeMeasurementCriterionID(Integer loeMeasurementCriterionID) {
		this.loeMeasurementCriterionID = loeMeasurementCriterionID;
	}
	
	
    
}
