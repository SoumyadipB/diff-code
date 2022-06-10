package com.ericsson.isf.model;

import com.ericsson.isf.model.botstore.TblRpaRequest;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class RPARequestDetailsDTO {
	@JsonUnwrapped
	private TblRpaRequest tblRpaRequest;
	private String languageBaseVersion;
	
	public RPARequestDetailsDTO() {
		this.tblRpaRequest=new TblRpaRequest();
	}
	
	public TblRpaRequest getTblRpaRequest() {
		return tblRpaRequest;
	}
	public void setTblRpaRequest(TblRpaRequest tblRpaRequest) {
		this.tblRpaRequest = tblRpaRequest;
	}
	public String getLanguageBaseVersion() {
		return languageBaseVersion;
	}
	public void setLanguageBaseVersion(String languageBaseVersion) {
		this.languageBaseVersion = languageBaseVersion;
	}
}
