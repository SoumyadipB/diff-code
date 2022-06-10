/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

/**
 *
 * @author esanpup
 */
public class MailModel {
    
    private int projectID;
    private String cpmSignum;
    private String cpmName;
    private String cpmEmailID;
    private String employeeSignum;
    private String employeeName;
    private String employeeEmailID;
    private String projectCreatorSignum;
    private String projectCreatorName;
    private String projectCreatorEmailID;
    private List<String> dR_Signum;
    private List<String> dR_EmailID;
    private List<String> dR_Name;
    private String createdBy_Signum;
    private String created_EmployeeEmailID;	
    private String created_EmployeeName;
    private String omSignum;
    private String omEmailID;
    
   

	public String getOmEmailID() {
		return omEmailID;
	}

	public void setOmEmailID(String omEmailID) {
		this.omEmailID = omEmailID;
	}

	public String getOmSignum() {
		return omSignum;
	}

	public void setOmSignum(String omSignum) {
		this.omSignum = omSignum;
	}

	public String getCreatedBy_Signum() {
		return createdBy_Signum;
	}

	public void setCreatedBy_Signum(String createdBy_Signum) {
		this.createdBy_Signum = createdBy_Signum;
	}

	public String getCreated_EmployeeEmailID() {
		return created_EmployeeEmailID;
	}

	public void setCreated_EmployeeEmailID(String created_EmployeeEmailID) {
		this.created_EmployeeEmailID = created_EmployeeEmailID;
	}

	public String getCreated_EmployeeName() {
		return created_EmployeeName;
	}

	public void setCreated_EmployeeName(String created_EmployeeName) {
		this.created_EmployeeName = created_EmployeeName;
	}

	public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getCpmSignum() {
        return cpmSignum;
    }

    public void setCpmSignum(String cpmSignum) {
        this.cpmSignum = cpmSignum;
    }

    public String getEmployeeSignum() {
        return employeeSignum;
    }

    public void setEmployeeSignum(String employeeSignum) {
        this.employeeSignum = employeeSignum;
    }

    public String getCpmName() {
        return cpmName;
    }

    public void setCpmName(String cpmName) {
        this.cpmName = cpmName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getCpmEmailID() {
        return cpmEmailID;
    }

    public void setCpmEmailID(String cpmEmailID) {
        this.cpmEmailID = cpmEmailID;
    }

    public String getEmployeeEmailID() {
        return employeeEmailID;
    }

    public void setEmployeeEmailID(String employeeEmailID) {
        this.employeeEmailID = employeeEmailID;
    }

    public List<String> getdR_Signum() {
        return dR_Signum;
    }

    public void setdR_Signum(List<String> dR_Signum) {
        this.dR_Signum = dR_Signum;
    }

    public List<String> getdR_EmailID() {
        return dR_EmailID;
    }

    public void setdR_EmailID(List<String> dR_EmailID) {
        this.dR_EmailID = dR_EmailID;
    }

    public String getProjectCreatorSignum() {
        return projectCreatorSignum;
    }

    public void setProjectCreatorSignum(String projectCreatorSignum) {
        this.projectCreatorSignum = projectCreatorSignum;
    }

    public String getProjectCreatorName() {
        return projectCreatorName;
    }

    public void setProjectCreatorName(String projectCreatorName) {
        this.projectCreatorName = projectCreatorName;
    }

    public String getProjectCreatorEmailID() {
        return projectCreatorEmailID;
    }

    public void setProjectCreatorEmailID(String projectCreatorEmailID) {
        this.projectCreatorEmailID = projectCreatorEmailID;
    }

    public List<String> getdR_Name() {
        return dR_Name;
    }

    public void setdR_Name(List<String> dR_Name) {
        this.dR_Name = dR_Name;
    }
}
