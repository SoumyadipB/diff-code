/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;
import java.util.Date;

/**
 *
 * @author etapawa
 */
public class LoeMeasurementCriterionModel {
    
    private int loeMeasurementCriterionID;
    private String loeMeasurementCriterion;
    private boolean active;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
    
    public int getLoeMeasurementCriterionID() {
		return loeMeasurementCriterionID;
	}
	public void setLoeMeasurementCriterionID(int loeMeasurementCriterionID) {
		this.loeMeasurementCriterionID = loeMeasurementCriterionID;
	}
	public String getLoeMeasurementCriterion() {
		return loeMeasurementCriterion;
	}
	public void setLoeMeasurementCriterion(String loeMeasurementCriterion) {
		this.loeMeasurementCriterion = loeMeasurementCriterion;
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
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
}
