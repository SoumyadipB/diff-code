package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

/**
*
* @author ekmbuma
*/
public class JwtApiUser {
	public int getExternalRefID() {
		return externalRefID;
	}
	public void setExternalRefID(int externalRefID) {
		this.externalRefID = externalRefID;
	}
	public int getSubscribedAPIID() {
		return subscribedAPIID;
	}
	public void setSubscribedAPIID(int subscribedAPIID) {
		this.subscribedAPIID = subscribedAPIID;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getOwnerSignum() {
		return ownerSignum;
	}
	public void setOwnerSignum(String ownerSignum) {
		this.ownerSignum = ownerSignum;
	}
	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}
	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public List<Integer> getRequestedAPI() {
		return requestedAPI;
	}
	public void setRequestedAPI(List<Integer> requestedAPI) {
		this.requestedAPI = requestedAPI;
	}
	public int getExpirationInYear() {
		return expirationInYear;
	}
	public void setExpirationInYear(int expirationInYear) {
		this.expirationInYear = expirationInYear;
	}
	public String getExternalSourceName() {
		return externalSourceName;
	}
	public void setExternalSourceName(String externalSourceName) {
		this.externalSourceName = externalSourceName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDecodedExternalSourceName() {
		return decodedExternalSourceName;
	}
	public void setDecodedExternalSourceName(String decodedExternalSourceName) {
		this.decodedExternalSourceName = decodedExternalSourceName;
	}
	public String getDecodedPassword() {
		return decodedPassword;
	}
	public void setDecodedPassword(String decodedPassword) {
		this.decodedPassword = decodedPassword;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private int externalRefID;
	private int subscribedAPIID;
	private String key;
	private boolean isActive;
	private Date createdOn;
	private Date expirationDate;
	private String ownerSignum;
	private Date lastModifiedOn;
	private String lastModifiedBy;
	private List<Integer> requestedAPI;
	private int expirationInYear;
	
    private String externalSourceName;
    private String password;
    private String decodedExternalSourceName;
    private String decodedPassword;
    private String message;
	
}
