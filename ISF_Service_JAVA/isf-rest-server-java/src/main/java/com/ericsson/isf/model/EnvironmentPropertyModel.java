package com.ericsson.isf.model;

/**
*
* @author eakinhm
*/

public class EnvironmentPropertyModel {

	private int EnvironmentPropertiesId;
	private String key;
	private String value;
	private String signum;
	private String deployedEnv;
	
	public int getEnvironmentPropertiesId() {
		return EnvironmentPropertiesId;
	}
	public void setEnvironmentPropertiesId(final int environmentPropertiesId) {
		EnvironmentPropertiesId = environmentPropertiesId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(final String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(final String value) {
		this.value = value;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(final String signum) {
		this.signum = signum;
	}
	public String getDeployedEnv() {
		return deployedEnv;
	}
	public void setDeployedEnv(String deployedEnv) {
		this.deployedEnv = deployedEnv;
	}
	
}
