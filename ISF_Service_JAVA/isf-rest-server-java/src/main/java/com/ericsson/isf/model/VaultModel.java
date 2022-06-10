package com.ericsson.isf.model;

public class VaultModel {

	private int vaultID;
	private String vaultName;
	private String secretName;
	private String secretType;
	private boolean isActive;
	private String seceretValue;

	public int getVaultID() {
		return vaultID;
	}

	public void setVaultID(int vaultID) {
		this.vaultID = vaultID;
	}

	public String getVaultName() {
		return vaultName;
	}

	public void setVaultName(String vaultName) {
		this.vaultName = vaultName;
	}

	public String getSecretName() {
		return secretName;
	}

	public void setSecretName(String secretName) {
		this.secretName = secretName;
	}

	public String getSecretType() {
		return secretType;
	}

	public void setSecretType(String secretType) {
		this.secretType = secretType;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getSeceretValue() {
		return seceretValue;
	}

	public void setSeceretValue(String seceretValue) {
		this.seceretValue = seceretValue;
	}

}
