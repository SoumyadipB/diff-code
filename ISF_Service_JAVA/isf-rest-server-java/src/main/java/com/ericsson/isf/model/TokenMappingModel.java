package com.ericsson.isf.model;

import java.util.Date;

/**
 * This class contains all information about a token.
 *
 * @author eakinhm
 */
public class TokenMappingModel {

	private long tokenID;
	private String token;
	private String type;
	private int sourceID;
	private Date activationDate;
	private Date expirationDate;
	private int active;
	private String signumID;
	private Boolean employeeEmailID;
	
	/**
	 * TokenBuilder helps to create object of TokenMappingModel by using Builder design pattern.
	 * 
	 * @author eakinhm
	 *
	 */

	public static class TokenBuilder {

		private String token;
		private String type;
		private int sourceID;
		private Date activationDate;
		private Date expirationDate;
		private int active;
		private String signumID;
		private Boolean employeeEmailID;

		public TokenBuilder() {
		}

		public TokenBuilder setToken(String token) {
			this.token = token;
			return this;
		}
		
		public TokenBuilder setType(String type) {
			this.type = type;
			return this;
		}

		public TokenBuilder setSourceID(int sourceID) {
			this.sourceID = sourceID;
			return this;
		}

		public TokenBuilder setActivationDate(Date activationDate) {
			this.activationDate = activationDate;
			return this;
		}

		public TokenBuilder setExpirationDate(Date expirationDate) {
			this.expirationDate = expirationDate;
			return this;
		}

		public TokenBuilder setActive(int active) {
			this.active = active;
			return this;
		}

		public TokenBuilder setSignumID(String signumID) {
			this.signumID = signumID;
			return this;
		}

		public TokenBuilder setEmployeeEmailID(Boolean employeeEmailID) {
			this.employeeEmailID = employeeEmailID;
			return this;
		}

		public TokenMappingModel build() {
			return new TokenMappingModel(token, type, sourceID, activationDate, expirationDate, active, signumID,
					employeeEmailID);
		}
	}

	public TokenMappingModel(String token, String type, int sourceID, Date activationDate, Date expirationDate,
			int active, String signumID, Boolean employeeEmailID) {
		this.token = token;
		this.type = type;
		this.sourceID = sourceID;
		this.activationDate = activationDate;
		this.expirationDate = expirationDate;
		this.active = active;
		this.signumID = signumID;
		this.employeeEmailID = employeeEmailID;
	}

	public long getTokenID() {
		return tokenID;
	}

	public String getToken() {
		return token;
	}

	public String getType() {
		return type;
	}

	public int getSourceID() {
		return sourceID;
	}

	public Date getActivationDate() {
		return activationDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public int getActive() {
		return active;
	}

	public String getSignumID() {
		return signumID;
	}

	public Boolean isEmployeeEmailID() {
		return employeeEmailID;
	}
	public void setTokenID(long tokenID) {
		this.tokenID = tokenID;
	} 
	
	@Override
	public String toString() {
		return "TokenMappingModel [tokenID=" + tokenID + ", token=" + token + ", type=" + type + ", sourceID="
				+ sourceID + ", activationDate=" + activationDate + ", expirationDate=" + expirationDate + ", active="
				+ active + ", signumID=" + signumID + ", employeeEmailID=" + employeeEmailID + "]";
	}

}
