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
public class TransferWorkOrderModel {
    	
    private List<Integer> woID;
    private String senderID;
    private String receiverID;
    private String logedInSignum;
    private Date startDate;
    private String stepName;
    private String userComments;
    
    public String getUserComments() {
		return userComments;
	}

	public void setUserComments(String userComments) {
		this.userComments = userComments;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public Date getStartDate() {
		return startDate;
	}

    public List<Integer> getWoID() {
        return woID;
    }

    public void setWoID(List<Integer> woID) {
        this.woID = woID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getLogedInSignum() {
        return logedInSignum;
    }

    public void setLogedInSignum(String logedInSignum) {
        this.logedInSignum = logedInSignum;
    }
    
    
    
    
}
