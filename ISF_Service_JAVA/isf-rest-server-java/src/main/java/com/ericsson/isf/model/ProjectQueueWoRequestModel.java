package com.ericsson.isf.model;

import java.util.List;

public class ProjectQueueWoRequestModel {		
	
			 private String signum;
			 private String startDate;
			 private String endDate;
			 private String status;
			 private String priority;
			 private String doStatus;
			 private List<String> projectIdList;
			 
			public String getSignum() {
				return signum;
			}
			public void setSignum(String signum) {
				this.signum = signum;
			}
			public String getStartDate() {
				return startDate;
			}
			public void setStartDate(String startDate) {
				this.startDate = startDate;
			}
			public String getEndDate() {
				return endDate;
			}
			public void setEndDate(String endDate) {
				this.endDate = endDate;
			}
			public String getStatus() {
				return status;
			}
			public void setStatus(String status) {
				this.status = status;
			}
			public String getPriority() {
				return priority;
			}
			public void setPriority(String priority) {
				this.priority = priority;
			}
			public String getDoStatus() {
				return doStatus;
			}
			public void setDoStatus(String doStatus) {
				this.doStatus = doStatus;
			}
			public List<String> getProjectIdList() {
				return projectIdList;
			}
			public void setProjectIdList(List<String> projectIdList) {
				this.projectIdList = projectIdList;
			}
			 
			 
			 
}
