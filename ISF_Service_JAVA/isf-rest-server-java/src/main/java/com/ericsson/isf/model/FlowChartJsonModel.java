/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;
import java.util.Map;

/**
 *
 * @author eguphee
 */
public class FlowChartJsonModel {
    private int projectID;
    private int subActivityID;
    private String projectName;
    private String countryName;
    private String marketAreaName;
    private String customerName;
    private List<Map<String,Object>> wfDetails;


    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getMarketAreaName() {
        return marketAreaName;
    }

    public void setMarketAreaName(String marketAreaName) {
        this.marketAreaName = marketAreaName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<Map<String, Object>> getWfDetails() {
        return wfDetails;
    }

    public void setWfDetails(List<Map<String, Object>> wfDetails) {
        this.wfDetails = wfDetails;
    }

    
    
}
