package com.ericsson.isf.model;

import java.util.List;

import org.springframework.http.HttpMethod;

/**
 * This class is model class for all event publisher APIs.
 * 
 * @author eakinhm
 *
 */
public class EventPublisherRequestModel {

	private String url;
	private String payLoad;
	private Long sourceID;
	private String externalSourceName;
	private List<HeaderModel> headerModels;
	private HttpMethod methodType;
	private String category;



	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPayLoad() {
		return payLoad;
	}

	public void setPayLoad(String payLoad) {
		this.payLoad = payLoad;
	}

	public Long getSourceID() {
		return sourceID;
	}

	public void setSourceID(Long sourceID) {
		this.sourceID = sourceID;
	}

	public String getExternalSourceName() {
		return externalSourceName;
	}

	public void setExternalSourceName(String externalSourceName) {
		this.externalSourceName = externalSourceName;
	}

	public List<HeaderModel> getHeaderModels() {
		return headerModels;
	}

	public void setHeaderModel(List<HeaderModel> headerModels) {
		this.headerModels = headerModels;
	}

	public HttpMethod getMethodType() {
		return methodType;
	}

	public void setMethodType(HttpMethod methodType) {
		this.methodType = methodType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setHeaderModels(List<HeaderModel> headerModels) {
		this.headerModels = headerModels;
	}

	
	

	@Override
	public String toString() {
		return "EventPublisherRequestModel [url=" + url + ", payLoad=" + payLoad + ", sourceID=" + sourceID
				+ ", externalSourceName=" + externalSourceName + ", headerModels=" + headerModels + ", methodType="
				+ methodType + ", category=" + category + "]";
	}

	public EventPublisherRequestModel() {

	}

	public EventPublisherRequestModel(String url, String payLoad, Long sourceID, String externalSourceName,
			List<HeaderModel> headerModels, HttpMethod methodType, String category) {
		this.url = url;
		this.payLoad = payLoad;
		this.sourceID = sourceID;
		this.externalSourceName = externalSourceName;
		this.headerModels = headerModels;
		this.methodType = methodType;
		this.category = category;
		
	}

}
