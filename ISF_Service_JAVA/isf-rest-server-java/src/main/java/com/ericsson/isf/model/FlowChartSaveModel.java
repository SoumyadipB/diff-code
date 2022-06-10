/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

/**
 *
 * @author ekarath
 */
public class FlowChartSaveModel {
    
    private int projectID;
    private int subActivityID;
    private int flowChartDefID;
    private String signumID;
    private int versionNumber;
    private String wfName;
    private boolean update;
    private String saveMode;
    private int qualificationCount;
    private boolean neMandatory;
    private boolean experiencedFlow;
    private String wfOwner;
    private String flowchartJSON;
    private boolean active;
    private String wfType;
    private float slaHours;
    private String ftrValue;
    private String botapprovalPage;
    private String botForStepID;
    private boolean futureWorkOrdersUpdate;
    private int workFlowID;
    private String wfEditReason;
    private boolean enableField;
    private List<KpiModel> lstKpiModel;
    private boolean resetProficiency;
    private Integer loeMeasurementCriterionID;

	public boolean isResetProficiency() {
		return resetProficiency;
	}

	public void setResetProficiency(boolean resetProficiency) {
		this.resetProficiency = resetProficiency;
	}

	public List<KpiModel> getLstKpiModel() {
		return lstKpiModel;
	}

	public void setLstKpiModel(List<KpiModel> lstKpiModel) {
		this.lstKpiModel = lstKpiModel;
	}

	public boolean isEnableField() {
		return enableField;
	}

	public void setEnableField(boolean enableField) {
		this.enableField = enableField;
	}


    private List<WFStepInstructionModel> lstWFStepInstructionModel;
    
    public List<WFStepInstructionModel> getLstWFStepInstructionModel() {
		return lstWFStepInstructionModel;
	}

	public void setLstWFStepInstructionModel(List<WFStepInstructionModel> lstWFStepInstructionModel) {
		this.lstWFStepInstructionModel = lstWFStepInstructionModel;
	}

	public boolean isFutureWorkOrdersUpdate() {
		return futureWorkOrdersUpdate;
	}

	public void setFutureWorkOrdersUpdate(boolean futureWorkOrdersUpdate) {
		this.futureWorkOrdersUpdate = futureWorkOrdersUpdate;
	}

	public String getBotForStepID() {
		return botForStepID;
	}

	public void setBotForStepID(String botForStepID) {
		this.botForStepID = botForStepID;
	}

	public String getBotapprovalPage() {
		return botapprovalPage;
	}

	public void setBotapprovalPage(String botapprovalPage) {
		this.botapprovalPage = botapprovalPage;
	}

	private int botId;

    
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

    public int getFlowChartDefID() {
        return flowChartDefID;
    }

    public void setFlowChartDefID(int flowChartDefID) {
        this.flowChartDefID = flowChartDefID;
    }

    public String getSignumID() {
        return signumID;
    }

    public void setSignumID(String signumID) {
        this.signumID = signumID;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getWfName() {
        return wfName;
    }

    public void setWfName(String wfName) {
        this.wfName = wfName;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getSaveMode() {
        return saveMode;
    }

    public void setSaveMode(String saveMode) {
        this.saveMode = saveMode;
    }

    public int getQualificationCount() {
        return qualificationCount;
    }

    public void setQualificationCount(int qualificationCount) {
        this.qualificationCount = qualificationCount;
    }

    public boolean isNeMandatory() {
        return neMandatory;
    }

    public void setNeMandatory(boolean neMandatory) {
        this.neMandatory = neMandatory;
    }

   

    public boolean isExperiencedFlow() {
		return experiencedFlow;
	}

	public void setExperiencedFlow(boolean experiencedFlow) {
		this.experiencedFlow = experiencedFlow;
	}

	public String getWfOwner() {
        return wfOwner;
    }

    public void setWfOwner(String wfOwner) {
        this.wfOwner = wfOwner;
    }

    public String getFlowchartJSON() {
        return flowchartJSON;
    }

    public void setFlowchartJSON(String flowchartJSON) {
        this.flowchartJSON = flowchartJSON;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getWfType() {
        return wfType;
    }

    public void setWfType(String wfType) {
        this.wfType = wfType;
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
    
	public int getBotId() {
		return botId;
	}

	public void setBotId(int botId) {
		this.botId = botId;
	}

	@Override
	public String toString() {
		return "FlowChartSaveModel [projectID=" + projectID + ",enableField=" + enableField + ", subActivityID=" + subActivityID + ", flowChartDefID="
				+ flowChartDefID + ", signumID=" + signumID + ", versionNumber=" + versionNumber + ", wfName=" + wfName
				+ ", update=" + update + ", saveMode=" + saveMode + ", qualificationCount=" + qualificationCount
				+ ", neMandatory=" + neMandatory + ", experiencedFlow=" + experiencedFlow + ", wfOwner=" + wfOwner
				+ ", flowchartJSON=" + flowchartJSON + ", active=" + active + ", wfType=" + wfType + ", slaHours="
				+ slaHours + ", ftrValue=" + ftrValue + ", botapprovalPage=" + botapprovalPage + ", botForStepID="
				+ botForStepID + ", isFutureWorkOrdersUpdate=" + futureWorkOrdersUpdate + ", botId=" + botId + "]";
	}

	public int getWorkFlowID() {
		return workFlowID;
	}

	public void setWorkFlowID(int workFlowID) {
		this.workFlowID = workFlowID;
	}

	public String getWfEditReason() {
		return wfEditReason;
	}

	public void setWfEditReason(String wfEditReason) {
		this.wfEditReason = wfEditReason;
	}

	public Integer getLoeMeasurementCriterionID() {
		return loeMeasurementCriterionID;
	}

	public void setLoeMeasurementCriterionID(Integer loeMeasurementCriterionID) {
		this.loeMeasurementCriterionID = loeMeasurementCriterionID;
	}

}
