/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author eguphee
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class WorkOrderNodesModel {
    
    private int wNID;
    private int wOID;
    private String nodeType;
    private String  nodeNames;
    private String createdBy;
    private String createdDate;
    private String market;
    private int doid;

   
	public int getwNID() {
        return wNID;
    }

    public void setwNID(int wNID) {
        this.wNID = wNID;
    }

    public int getwOID() {
        return wOID;
    }

    public void setwOID(int wOID) {
        this.wOID = wOID;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeNames() {
        return nodeNames;
    }

    public void setNodeNames(String nodeNames) {
        this.nodeNames = nodeNames;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public int getDoid() {
		return doid;
	}

	public void setDoid(int doid) {
		this.doid = doid;
	}
   
}
