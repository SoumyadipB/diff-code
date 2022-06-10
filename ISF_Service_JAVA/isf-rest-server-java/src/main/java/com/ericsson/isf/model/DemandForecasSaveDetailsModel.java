/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;
/**
 *
 * @author edhhklu
 */
public class DemandForecasSaveDetailsModel {
	
	private  List<DemandForecastDetailModel> positionData;
	private String role;
	private String  signum;
	private Integer projectid;
	//, 	operation =saveasdraft/sendtospm/sendbacktorpm
	private String operation;
	public List<DemandForecastDetailModel> getPositionData() {
		return positionData;
	}
	public void setPositionData(List<DemandForecastDetailModel> positionData) {
		this.positionData = positionData;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public Integer getProjectid() {
		return projectid;
	}
	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}	
	
	
		
}
