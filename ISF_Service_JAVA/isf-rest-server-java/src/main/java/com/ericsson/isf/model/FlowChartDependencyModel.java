/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author ekarath
 */
public class FlowChartDependencyModel {
    
   private int subActivityFlowChartDepID;
   private String srcFlowChartStepID;
   private String srcStepName;
   private String destFlowChartStepID;
   private String destStepName;
   private String linkJson;
   private String linkText;
   
   

    public String getSrcFlowChartStepID() {
	return srcFlowChartStepID;
}

public void setSrcFlowChartStepID(String srcFlowChartStepID) {
	this.srcFlowChartStepID = srcFlowChartStepID;
}

public String getDestFlowChartStepID() {
	return destFlowChartStepID;
}

public void setDestFlowChartStepID(String destFlowChartStepID) {
	this.destFlowChartStepID = destFlowChartStepID;
}

	public int getSubActivityFlowChartDepID() {
        return subActivityFlowChartDepID;
    }

    public void setSubActivityFlowChartDepID(int subActivityFlowChartDepID) {
        this.subActivityFlowChartDepID = subActivityFlowChartDepID;
    }
     

    public String getSrcStepName() {
        return srcStepName;
    }

    public void setSrcStepName(String srcStepName) {
        this.srcStepName = srcStepName;
    }


    public String getDestStepName() {
        return destStepName;
    }

    public void setDestStepName(String destStepName) {
        this.destStepName = destStepName;
    }

    public String getLinkJson() {
        return linkJson;
    }

    public void setLinkJson(String linkJson) {
        this.linkJson = linkJson;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }
   
    
    
}
