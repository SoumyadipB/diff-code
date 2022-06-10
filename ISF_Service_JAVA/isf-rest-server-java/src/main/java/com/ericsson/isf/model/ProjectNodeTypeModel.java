/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

/**
 *
 * @author esanpup
 */
public class ProjectNodeTypeModel {
    
    private String nodeType;
    private List<String> lstNodeName;
    private int projectID;
    private String sector;

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public List<String> getLstNodeName() {
        return lstNodeName;
    }

    public void setLstNodeName(List<String> lstNodeName) {
        this.lstNodeName = lstNodeName;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
    
    
}
