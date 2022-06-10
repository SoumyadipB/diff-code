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
public class ErrorBean {
    
    private String message;
    private String errorDescription;
    private String details;
    private String howToFix;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

	public String getHowToFix() {
		return howToFix;
	}

	public void setHowToFix(String howToFix) {
		this.howToFix = howToFix;
	}

	@Override
	public String toString() {
		return "ErrorBean [message=" + message + ", errorDescription=" + errorDescription + ", details=" + details
				+ ", howToFix=" + howToFix + "]";
	}

	  
    
}
