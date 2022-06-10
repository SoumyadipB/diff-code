/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

/**
 *
 * @author esanpup
 */
public class VendorNetworkViewModel {
    
    private int vendorID;
    private String vendor;

    public VendorNetworkViewModel(int vendorID, String vendor) {
		this.vendorID = vendorID;
		this.vendor = vendor;
	}
    
    public VendorNetworkViewModel() {
	}

	public int getVendorID() {
        return vendorID;
    }

    public void setVendorID(int vendorID) {
        this.vendorID = vendorID;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    
}
