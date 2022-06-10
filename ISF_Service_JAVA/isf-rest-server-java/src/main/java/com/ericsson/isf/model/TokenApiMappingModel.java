package com.ericsson.isf.model;

import java.util.Date;

public class TokenApiMappingModel {
	private long apiTokenID;
	private long externalRefID;
	private long subscribedApiID;
	private String token;
	private boolean isActive;
	private Date createdOn;
	private Date expirationDate;
	private String ownerSignum;
	private Date lastModifiedOn;
	private String lastModifiedBy;
	
	
	public static class TokenApiBuilder {
		private long externalRefID;
		private long subscribedApiID;
		private String token;
		private boolean isActive;
		private Date createdOn;
		private Date expirationDate;
		private String ownerSignum;
		private Date lastModifiedOn;
		private String lastModifiedBy;
		
		public TokenApiBuilder() {
			
		}

		public TokenApiBuilder setExternalRefID(long externalRefID) {
			this.externalRefID = externalRefID;
			return this;
		}

		public TokenApiBuilder setSubscribedApiID(long subscribedApiID) {
			this.subscribedApiID = subscribedApiID;
			return this;
		}

		public TokenApiBuilder setToken(String token) {
			this.token = token;
			return this;
		}

		public TokenApiBuilder setActive(boolean isActive) {
			this.isActive = isActive;
			return this;
		}

		public TokenApiBuilder setCreatedOn(Date createdOn) {
			this.createdOn = createdOn;
			return this;
		}

		public TokenApiBuilder setExpirationDate(Date expirationDate) {
			this.expirationDate = expirationDate;
			return this;
		}

		public TokenApiBuilder setOwnerSignum(String ownerSignum) {
			this.ownerSignum = ownerSignum;
			return this;
		}

		public TokenApiBuilder setLastModifiedOn(Date lastModifiedOn) {
			this.lastModifiedOn = lastModifiedOn;
			return this;
		}

		public TokenApiBuilder setLastModifiedBy(String lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public TokenApiMappingModel build() {
			return new TokenApiMappingModel(externalRefID,subscribedApiID,token,isActive,
					createdOn,expirationDate,ownerSignum,lastModifiedOn,lastModifiedBy);
		}
		
	}
	
	public TokenApiMappingModel(long externalRefID, long subscribedApiID, String token, boolean isActive,
			Date createdOn, Date expirationDate, String ownerSignum, Date lastModifiedOn, String lastModifiedBy) {
		this.externalRefID = externalRefID;
		this.subscribedApiID = subscribedApiID;
		this.token = token;
		this.isActive = isActive;
		this.createdOn = createdOn;
		this.expirationDate = expirationDate;
		this.ownerSignum = ownerSignum;
		this.lastModifiedOn = lastModifiedOn;
		this.lastModifiedBy = lastModifiedBy;
	}

	public long getApiTokenID() {
		return apiTokenID;
	}
	public void setApiTokenID(long apiTokenID) {
		this.apiTokenID = apiTokenID;
	}
	public long getExternalRefID() {
		return externalRefID;
	}
	public void setExternalRefID(long externalRefID) {
		this.externalRefID = externalRefID;
	}
	public long getSubscribedApiID() {
		return subscribedApiID;
	}
	public void setSubscribedApiID(long subscribedApiID) {
		this.subscribedApiID = subscribedApiID;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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

	@Override
	public String toString() {
		return "TokenApiMappingModel [apiTokenID=" + apiTokenID + ", externalRefID=" + externalRefID
				+ ", subscribedApiID=" + subscribedApiID + ", token=" + token + ", isActive=" + isActive + ", createdOn="
				+ createdOn + ", expirationDate=" + expirationDate + ", ownerSignum=" + ownerSignum
				+ ", lastModifiedOn=" + lastModifiedOn + ", lastModifiedBy=" + lastModifiedBy + "]";
	}

	
}
