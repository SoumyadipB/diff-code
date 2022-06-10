/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author eefhiio
 */
public class WOWorkFlowModel {
    int subActivityDefID;
    String flowChartJSON;
    int version;
    boolean uploadedJSON;
    
    String workFlowName;
    private List<WorkflowStepDetailModel> workFlowSteps;
    
    private boolean workFlowAutoSenseEnabled;
    private Boolean workOrderAutoSenseEnabled;
    
    private boolean multiview;
    private StepDetailsModel disabledStepDetails;
    private String status;
    private Date actualStartDate;
   

	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isWorkFlowAutoSenseEnabled() {
		return workFlowAutoSenseEnabled;
	}

	public void setWorkFlowAutoSenseEnabled(boolean workFlowAutoSenseEnabled) {
		this.workFlowAutoSenseEnabled = workFlowAutoSenseEnabled;
	}

	public Boolean getWorkOrderAutoSenseEnabled() {
		return workOrderAutoSenseEnabled;
	}

	public void setWorkOrderAutoSenseEnabled(Boolean workOrderAutoSenseEnabled) {
		this.workOrderAutoSenseEnabled = workOrderAutoSenseEnabled;
	}

	public List<WorkflowStepDetailModel> getWorkFlowSteps() {
		return workFlowSteps;
	}

	public void setWorkFlowSteps(List<WorkflowStepDetailModel> workFlowSteps) {
		this.workFlowSteps = workFlowSteps;
	}

	public String getWorkFlowName() {
		return workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	public boolean isUploadedJSON() {
		return uploadedJSON;
	}

	public void setUploadedJSON(boolean uploadedJSON) {
		this.uploadedJSON = uploadedJSON;
	}

	public int getSubActivityDefID() {
        return subActivityDefID;
    }

    public void setSubActivityDefID(int subActivityDefID) {
        this.subActivityDefID = subActivityDefID;
    }

    public String getFlowChartJSON() {
        return flowChartJSON;
    }

    public void setFlowChartJSON(String flowChartJSON) {
        this.flowChartJSON = flowChartJSON;
    }
    

    public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "WOWorkFlowModel [subActivityDefID=" + subActivityDefID + ", flowChartJSON=" + flowChartJSON
				+ ", version=" + version + ", uploadedJSON=" + uploadedJSON + ", workFlowName=" + workFlowName
				+ ", workFlowSteps=" + workFlowSteps + ", workFlowAutoSenseEnabled=" + workFlowAutoSenseEnabled
				+ ", workOrderAutoSenseEnabled=" + workOrderAutoSenseEnabled + ", Multiview=" + multiview
				+ "]";
	}

	public StepDetailsModel getDisabledStepDetails() {
		return disabledStepDetails;
	}

	public void setDisabledStepDetails(StepDetailsModel disabledStepDetails) {
		this.disabledStepDetails = disabledStepDetails;
	}

	public boolean isMultiview() {
		return multiview;
	}

	public void setMultiview(boolean multiview) {
		this.multiview = multiview;
	}

}
