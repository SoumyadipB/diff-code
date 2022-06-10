/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

/**
 *
 * @author eefhiio
 */
public class DeliveryResponsibleModel {
    
    int deliveryResponsibleID;
    int projectID;
    String deliveryResponsible;
    boolean active;
    String createdBy;
    Date createdDate;
    String lastModifiedBy;
    Date lastModifiedDate;
    String lastModifiedOn;
    private String signumID;

    public String getSignumID() {
        return signumID;
    }

    public void setSignumID(String signumID) {
        this.signumID = signumID;
    }
    
    

    public int getDeliveryResponsibleID() {
        return deliveryResponsibleID;
    }

    public void setDeliveryResponsibleID(int deliveryResponsibleID) {
        this.deliveryResponsibleID = deliveryResponsibleID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getDeliveryResponsible() {
        return deliveryResponsible;
    }

    public void setDeliveryResponsible(String deliveryResponsible) {
        this.deliveryResponsible = deliveryResponsible;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    
    
    public String getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(String lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	@Override
    public String toString() {
        return "DeliveryResponsibleModel{" + "deliveryResponsibleID=" + deliveryResponsibleID + ", projectID=" + projectID + ", deliveryResponsible=" + deliveryResponsible + ", active=" + active + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedDate=" + lastModifiedDate + '}';
    }
    
    
}
