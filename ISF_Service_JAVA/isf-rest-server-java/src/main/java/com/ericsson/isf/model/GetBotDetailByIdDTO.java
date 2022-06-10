package com.ericsson.isf.model;

import com.ericsson.isf.model.botstore.TblRpaDeployedBot;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class GetBotDetailByIdDTO {
	@JsonUnwrapped
	private TblRpaDeployedBot tblRpaDeployedBot;
	
	private String languageBaseVersion;

	public TblRpaDeployedBot getTblRpaDeployedBot() {
		return tblRpaDeployedBot;
	}

	public void setTblRpaDeployedBot(TblRpaDeployedBot tblRpaDeployedBot) {
		this.tblRpaDeployedBot = tblRpaDeployedBot;
	}

	public String getLanguageBaseVersion() {
		return languageBaseVersion;
	}

	public void setLanguageBaseVersion(String languageBaseVersion) {
		this.languageBaseVersion = languageBaseVersion;
	}

}
