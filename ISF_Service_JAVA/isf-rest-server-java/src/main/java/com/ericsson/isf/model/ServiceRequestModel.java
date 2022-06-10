package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ServiceRequestModel {
	private int srid;
	private int doid;
	private ProjectModel projectDetails;
	private int deliverableID;
	private String nodeType;
	private String nodeName;
	private String inputURL;
	private String comment;
	private String srPriority;
	private Date startTime;
	private String woName;
	private int doVolume=1;
	private boolean isActive=true;
	private String createdBy;
	private String srSource;
	private String deliverableName;
	private String srStatus;
	private List<WOOutputFileModel> outputURL;
	private String srCreatedByProfile;
	
	public String getDeliverableName() {
		return deliverableName;
	}

	public void setDeliverableName(String deliverableName) {
		this.deliverableName = deliverableName;
	}

	public ServiceRequestModel srid(int srid) {
		this.srid=srid;
		return this;
	}
	
	public ServiceRequestModel doid(int doid) {
		this.doid=doid;
		return this;
	}
	
	
	public ServiceRequestModel deliverableID(int deliverableID) {
		this.deliverableID=deliverableID;
		return this;
	}
	
	public ServiceRequestModel nodeType(String nodeType) {
		this.nodeType=nodeType;
		return this;
	}
	
	public ServiceRequestModel nodeName(String nodeName) {
		this.nodeName=nodeName;
		return this;
	}
	
	public ServiceRequestModel inputURL(String inputURL) {
		this.inputURL=inputURL;
		return this;
	}
	
	public ServiceRequestModel comment(String comment) {
		this.comment=comment;
		return this;
	}
	
	public ServiceRequestModel srPriority(String srPriority) {
		this.srPriority=srPriority;
		return this;
	}
	
	public ServiceRequestModel startTime(Date startTime) {
		this.setStartTime(startTime);
		return this;
	}
	
	public ServiceRequestModel woName(String woName) {
		this.woName=woName;
		return this;
	}
	
	public ServiceRequestModel createdBy(String createdBy) {
		this.createdBy=createdBy;
		return this;
	}
	
	public ServiceRequestModel projectDetails(ProjectModel projectDetails) {
		this.setProjectDetails(projectDetails);
		return this;
	}
	
	public ServiceRequestModel srSource(String srSource) {
		this.srSource=srSource;
		return this;
	}
	
	
	public ServiceRequestModel srCreatedByProfile(String srCreatedByProfile) {
		this.srCreatedByProfile=srCreatedByProfile;
		return this;
	}
	
	public String getWoName() {
		return woName;
	}
	
	public void setWoName(String woName) {
		this.woName = woName;
	}
	
	public int getSrid() {
		return srid;
	}
	public void setSrid(int srid) {
		this.srid = srid;
	}
	public int getDoid() {
		return doid;
	}
	public void setDoid(int doid) {
		this.doid = doid;
	}
	public int getDeliverableID() {
		return deliverableID;
	}
	public void setDeliverableID(int deliverableID) {
		this.deliverableID = deliverableID;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getInputURL() {
		return inputURL;
	}
	public void setInputURL(String inputURL) {
		this.inputURL = inputURL;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getSrPriority() {
		return srPriority;
	}
	public void setSrPriority(String srPriority) {
		this.srPriority = srPriority;
	}
	
	public int getDoVolume() {
		return doVolume;
	}
	public void setDoVolume(int doVolume) {
		this.doVolume = doVolume;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getSrSource() {
		return srSource;
	}

	public void setSrSource(String srSource) {
		this.srSource = srSource;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public List<WOOutputFileModel> getOutputURL() {
		return outputURL;
	}

	public void setOutputURL(List<WOOutputFileModel> outputURL) {
		this.outputURL = outputURL;
	}

	public ProjectModel getProjectDetails() {
		return projectDetails;
	}

	public void setProjectDetails(ProjectModel projectDetails) {
		this.projectDetails = projectDetails;
	}

	public String getSrStatus() {
		return srStatus;
	}

	public void setSrStatus(String srStatus) {
		this.srStatus = srStatus;
	}

	public String getSrCreatedByProfile() {
		return srCreatedByProfile;
	}

	public void setSrCreatedByProfile(String srCreatedByProfile) {
		this.srCreatedByProfile = srCreatedByProfile;
	}
}
