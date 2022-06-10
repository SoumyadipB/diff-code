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
public class DomainNetworkViewModel {
	
    private int domainID;
    private String domain;
    
    
    public DomainNetworkViewModel() {}
    
    public DomainNetworkViewModel(int domainID, String domain) {
		this.domainID = domainID;
		this.domain = domain;
	}
    
	public int getDomainID() {
        return domainID;
    }

    public void setDomainID(int domainID) {
        this.domainID = domainID;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }


    @Override
    public String toString() {
        return "DomainModel{" + "domainID=" + domainID + ", domain=" + domain + '}';
    }
    
    
     
    
}
