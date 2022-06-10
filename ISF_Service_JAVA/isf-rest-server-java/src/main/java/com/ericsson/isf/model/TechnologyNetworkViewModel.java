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
public class TechnologyNetworkViewModel {
    
    private int technologyID;
    private String technology;

    public TechnologyNetworkViewModel() {}

    public TechnologyNetworkViewModel(int technologyID, String technology) {
		this.technologyID = technologyID;
		this.technology = technology;
	}

	public int getTechnologyID() {
        return technologyID;
    }

    public void setTechnologyID(int technologyID) {
        this.technologyID = technologyID;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

        @Override
    public String toString() {
        return "TechnologyModel{" + "technologyID=" + technologyID + ", technology=" + technology  + '}';
    }
    
    
}
