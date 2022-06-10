package com.ericsson.serverbots.models;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.List;
import java.util.Map;

public class RpaApiResponse implements java.io.Serializable 
{

	private String apiName;

	private boolean isApiSuccess;
	private String responseMsg;
	private String data;
	private String responseCode;
	private Map<String, List<String>> responseMap;

	public RpaApiResponse() {
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public boolean isApiSuccess() {
		return isApiSuccess;
	}

	public void setApiSuccess(boolean isApiSuccess) {
		this.isApiSuccess = isApiSuccess;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public Map<String, List<String>> getResponseMap() {
		return responseMap;
	}

	public void setResponseMap(Map<String, List<String>> responseMap) {
		this.responseMap = responseMap;
	}


}
