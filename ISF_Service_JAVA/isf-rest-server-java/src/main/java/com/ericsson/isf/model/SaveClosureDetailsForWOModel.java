/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author ekumvsu
 */
public class SaveClosureDetailsForWOModel {
 
	@NotNull
    Integer wOID;
    String wOName;
    String deliveryStatus;
    String reason;
    String description;
    String statusComment;
    String acceptedOrRejectedBy;
    String lastModifiedBy;
    Date lastModifiedDate;
    int subactivityDefID;
    boolean isCreateSubSequentWO;
    private String priority;
    private String signumID;
    @Size(min = 3, message = "Invalid source name lenght, Lenght can't be less than 3")
    @NotEmpty(message = "source name can't be empty")
    private String externalSourceName;

    public Integer getwOID() {
        return wOID;
    }

    public void setwOID(Integer wOID) {
        this.wOID = wOID;
    }

    public String getwOName() {
        return wOName;
    }

    public void setwOName(String wOName) {
        this.wOName = wOName;
    }

    public String getAcceptedOrRejectedBy() {
        return acceptedOrRejectedBy;
    }

    public void setAcceptedOrRejectedBy(String acceptedOrRejectedBy) {
        this.acceptedOrRejectedBy = acceptedOrRejectedBy;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusComment() {
        return statusComment;
    }

    public void setStatusComment(String statusComment) {
        this.statusComment = statusComment;
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

	public int getSubactivityDefID() {
		return subactivityDefID;
	}

	public void setSubactivityDefID(int subactivityDefID) {
		this.subactivityDefID = subactivityDefID;
	}

	@JsonProperty(value="isCreateSubSequentWO") 
	public boolean isCreateSubSequentWO() {
		return isCreateSubSequentWO;
	}

	public void setCreateSubSequentWO(boolean isCreateSubSequentWO) {
		this.isCreateSubSequentWO = isCreateSubSequentWO;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSignumID() {
		return signumID;
	}

	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}

	public String getExternalSourceName() {
		return externalSourceName;
	}

	public void setExternalSourceName(String externalSourceName) {
		this.externalSourceName = externalSourceName;
	}

}
