/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author esudbhu
 */
public class BotConfig {
	private String type;
	private String referenceId;
	private String json;
	private String description;
	private int active;
	private String signum;
	private String botType;
	private int refBotId;
	private boolean isInputRequired;
	

	public boolean isInputRequired() {
		return isInputRequired;
	}

	public void setInputRequired(boolean isInputRequired) {
		this.isInputRequired = isInputRequired;
	}

	public String getSignum() {
		return signum;
	}

	public void setSignum(String signum) {
		this.signum = signum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getBotType() {
		return botType;
	}

	public void setBotType(String botType) {
		this.botType = botType;
	}

	public int getRefBotId() {
		return refBotId;
	}

	public void setRefBotId(int refBotId) {
		this.refBotId = refBotId;
	}


}
