package com.ericsson.isf.model;

import java.util.List;

public class DescisionStepDetailsModel {
	
	private String decisionStepName ;
	private String setDecisionStepId;
	List<ChildStepDetailsModel> childStepDetailsModelList;
	
	public String getSetDecisionStepId() {
		return setDecisionStepId;
	}
	public void setSetDecisionStepId(String setDecisionStepId) {
		this.setDecisionStepId = setDecisionStepId;
	}
	public List<ChildStepDetailsModel> getChildStepDetailsModelList() {
		return childStepDetailsModelList;
	}
	public void setChildStepDetailsModelList(List<ChildStepDetailsModel> childStepDetailsModelList) {
		this.childStepDetailsModelList = childStepDetailsModelList;
	}
	public String getDecisionStepName() {
		return decisionStepName;
	}
	public void setDecisionStepName(String decisionStepName) {
		this.decisionStepName = decisionStepName;
	}
}
