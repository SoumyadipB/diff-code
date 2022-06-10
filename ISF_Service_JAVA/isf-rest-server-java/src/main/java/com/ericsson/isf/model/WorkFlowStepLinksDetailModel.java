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
 * @author edhhklu
 */
public class WorkFlowStepLinksDetailModel {
	private String sourceStepid;	
	private String targetStepId;
	private String sourceStepName;
	private String sourceStepType;
	
	public String getSourceStepid() {
		return sourceStepid;
	}
	public void setSourceStepid(String sourceStepid) {
		this.sourceStepid = sourceStepid;
	}
	public String getTargetStepId() {
		return targetStepId;
	}
	public void setTargetStepId(String targetStepId) {
		this.targetStepId = targetStepId;
	}
	public String getSourceStepName() {
		return sourceStepName;
	}
	public void setSourceStepName(String sourceStepName) {
		this.sourceStepName = sourceStepName;
	}
	public String getSourceStepType() {
		return sourceStepType;
	}
	public void setSourceStepType(String sourceStepType) {
		this.sourceStepType = sourceStepType;
	}
	
}
