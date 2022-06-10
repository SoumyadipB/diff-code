package com.ericsson.isf.model;

import com.ericsson.isf.model.botstore.TblRpaBotstaging;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class GetBotStageDetailsForTestingDTO {
	@JsonUnwrapped
	private TblRpaBotstaging tblRpaBotstaging;
	
	private String languageBaseVersion;

	public TblRpaBotstaging getTblRpaBotstaging() {
		return tblRpaBotstaging;
	}

	public void setTblRpaBotstaging(TblRpaBotstaging tblRpaBotstaging) {
		this.tblRpaBotstaging = tblRpaBotstaging;
	}

	public String getLanguageBaseVersion() {
		return languageBaseVersion;
	}

	public void setLanguageBaseVersion(String languageBaseVersion) {
		this.languageBaseVersion = languageBaseVersion;
	}
}
