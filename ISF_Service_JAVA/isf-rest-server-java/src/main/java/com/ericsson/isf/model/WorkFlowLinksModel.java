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
 * @author eabhmoj
 */
public class WorkFlowLinksModel {
    private int workFlow_LinkID;
private int workFlowID;
private String linkClientID;
private String router;
private String connector;
private int sourceStepClientID;
private int targetStepClientID;
private int z;
private String type;
private String label_Text;
private String createdBy;
private Date createdOn; 
List <WorkFlowLinksVerticesModel> vertices;

    public List<WorkFlowLinksVerticesModel> getVertices() {
        return vertices;
    }

    public void setVertices(List<WorkFlowLinksVerticesModel> vertices) {
        this.vertices = vertices;
    }
    public int getWorkFlow_LinkID() {
        return workFlow_LinkID;
    }

    public void setWorkFlow_LinkID(int workFlow_LinkID) {
        this.workFlow_LinkID = workFlow_LinkID;
    }

    public int getWorkFlowID() {
        return workFlowID;
    }

    public void setWorkFlowID(int workFlowID) {
        this.workFlowID = workFlowID;
    }

    public String getLinkClientID() {
        return linkClientID;
    }

    public void setLinkClientID(String linkClientID) {
        this.linkClientID = linkClientID;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

    public int getSourceStepClientID() {
        return sourceStepClientID;
    }

    public void setSourceStepClientID(int sourceStepClientID) {
        this.sourceStepClientID = sourceStepClientID;
    }

    public int getTargetStepClientID() {
        return targetStepClientID;
    }

    public void setTargetStepClientID(int targetStepClientID) {
        this.targetStepClientID = targetStepClientID;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel_Text() {
        return label_Text;
    }

    public void setLabel_Text(String label_Text) {
        this.label_Text = label_Text;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

}
