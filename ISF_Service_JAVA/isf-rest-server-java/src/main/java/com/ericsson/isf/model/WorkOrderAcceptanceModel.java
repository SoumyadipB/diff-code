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
 * @author esanpup
 */
public class WorkOrderAcceptanceModel {
    
    private int deliveryAcceptanceID;
    private List<Integer> lstWoID;
    private String woName;
    private String rating;
    private String acceptance;
    private String deliveryStatus;
    private String reason;
    private String comment;
    private String acceptedOrRejectedBy;
    private Date acceptedOrRejectDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;


    public int getDeliveryAcceptanceID() {
        return deliveryAcceptanceID;
    }

    public void setDeliveryAcceptanceID(int deliveryAcceptanceID) {
        this.deliveryAcceptanceID = deliveryAcceptanceID;
    }

    public List<Integer> getLstWoID() {
        return lstWoID;
    }

    public void setLstWoID(List<Integer> lstWoID) {
        this.lstWoID = lstWoID;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    
    
    public String getWoName() {
        return woName;
    }

    public void setWoName(String woName) {
        this.woName = woName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(String acceptance) {
        this.acceptance = acceptance;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAcceptedOrRejectedBy() {
        return acceptedOrRejectedBy;
    }

    public void setAcceptedOrRejectedBy(String acceptedOrRejectedBy) {
        this.acceptedOrRejectedBy = acceptedOrRejectedBy;
    }

    public Date getAcceptedOrRejectDate() {
        return acceptedOrRejectDate;
    }

    public void setAcceptedOrRejectDate(Date acceptedOrRejectDate) {
        this.acceptedOrRejectDate = acceptedOrRejectDate;
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
    
    
}
