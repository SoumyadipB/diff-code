package com.ericsson.isf.iva.model;

import java.util.Date;

public class JwtUser {
	
    private Date expiration;
    private Date issuedAt;
    private String token;
    private String userName;
    private String password;
    private String type;
    private String decodedUserName;
    private String decodedPassword;
    private String externalSourceName;
    private String ownerSignum;

	public String getOwnerSignum() {
		return ownerSignum;
	}

	public void setOwnerSignum(String ownerSignum) {
		this.ownerSignum = ownerSignum;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public Date getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(Date issuedAt) {
		this.issuedAt = issuedAt;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDecodedUserName() {
		return decodedUserName;
	}

	public void setDecodedUserName(String decodedUserName) {
		this.decodedUserName = decodedUserName;
	}

	public String getDecodedPassword() {
		return decodedPassword;
	}

	public void setDecodedPassword(String decodedPassword) {
		this.decodedPassword = decodedPassword;
	}
	
	public String getExternalSourceName() {
		return externalSourceName;
	}

	public void setExternalSourceName(String externalSourceName) {
		this.externalSourceName = externalSourceName;
	}
}