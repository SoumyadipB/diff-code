/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author esanpup
 */
public class WORelatedDetails {
    
    private String woStatus;
    private String userComments;
    private String userDeliveryStatus;
    private String userReason;
    private String deliveryReason;
    private String deliveryComment;
    private float deliveryRatings;
    private String deliveryStatus;
    private Boolean isParent;
    

    public String getWoStatus() {
        return woStatus;
    }

    public void setWoStatus(String woStatus) {
        this.woStatus = woStatus;
    }
    
    public String getUserComments() {
        return userComments;
    }

    public void setUserComments(String userComments) {
        this.userComments = userComments;
    }

    public String getUserDeliveryStatus() {
        return userDeliveryStatus;
    }

    public void setUserDeliveryStatus(String userDeliveryStatus) {
        this.userDeliveryStatus = userDeliveryStatus;
    }

    public String getUserReason() {
        return userReason;
    }

    public void setUserReason(String userReason) {
        this.userReason = userReason;
    }

    public String getDeliveryReason() {
        return deliveryReason;
    }

    public void setDeliveryReason(String deliveryReason) {
        this.deliveryReason = deliveryReason;
    }

    public String getDeliveryComment() {
        return deliveryComment;
    }

    public void setDeliveryComment(String deliveryComment) {
        this.deliveryComment = deliveryComment;
    }

    public float getDeliveryRatings() {
        return deliveryRatings;
    }

    public void setDeliveryRatings(float deliveryRatings) {
        this.deliveryRatings = deliveryRatings;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }
   
    

    
    
    
}
