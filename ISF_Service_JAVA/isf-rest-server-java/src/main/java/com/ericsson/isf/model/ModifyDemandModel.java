package com.ericsson.isf.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ModifyDemandModel {
	
		private int resourcePositionId;
		private String positionStatus;//:"Inactive",
		boolean isActivie;//:"true",
		private String CRStatus;//:"FILL",
		private int domainId;
		private int serviceAreaId;
		private int technologyId;
		private String  resourceSignum;
		
		@JsonFormat(pattern="yyyy-MM-dd")
		private Date startDate;//2017-12-10 00:00:00.000"
		
		@JsonFormat(pattern="yyyy-MM-dd")
		private Date endDate;
		private String actionType;
		public int getResourcePositionId() {
			return resourcePositionId;
		}
		public void setResourcePositionId(int resourcePositionId) {
			this.resourcePositionId = resourcePositionId;
		}
		public String getPositionStatus() {
			return positionStatus;
		}
		public void setPositionStatus(String positionStatus) {
			this.positionStatus = positionStatus;
		}
		public boolean isActivie() {
			return isActivie;
		}
		public void setActivie(boolean isActivie) {
			this.isActivie = isActivie;
		}
		public String getCRStatus() {
			return CRStatus;
		}
		public void setCRStatus(String cRStatus) {
			CRStatus = cRStatus;
		}
		public int getDomainId() {
			return domainId;
		}
		public void setDomainId(int domainId) {
			this.domainId = domainId;
		}
		public int getServiceAreaId() {
			return serviceAreaId;
		}
		public void setServiceAreaId(int serviceAreaId) {
			this.serviceAreaId = serviceAreaId;
		}
		public int getTechnologyId() {
			return technologyId;
		}
		public void setTechnologyId(int technologyId) {
			this.technologyId = technologyId;
		}
		public String getResourceSignum() {
			return resourceSignum;
		}
		public void setResourceSignum(String resourceSignum) {
			this.resourceSignum = resourceSignum;
		}
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
		public String getActionType() {
			return actionType;
		}
		public void setActionType(String actionType) {
			this.actionType = actionType;
		}
		
}
