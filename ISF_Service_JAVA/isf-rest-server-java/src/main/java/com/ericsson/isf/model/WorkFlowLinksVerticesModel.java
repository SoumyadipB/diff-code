/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

/**
 *
 * @author eabhmoj
 */
public class WorkFlowLinksVerticesModel {
 private int   verticeID;
private int workFlow_LinkID;
private int x;
private int y;
private String createdBy;
private Date createdOn;

    public int getVerticeID() {
        return verticeID;
    }

    public void setVerticeID(int verticeID) {
        this.verticeID = verticeID;
    }

    public int getWorkFlow_LinkID() {
        return workFlow_LinkID;
    }

    public void setWorkFlow_LinkID(int workFlow_LinkID) {
        this.workFlow_LinkID = workFlow_LinkID;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
