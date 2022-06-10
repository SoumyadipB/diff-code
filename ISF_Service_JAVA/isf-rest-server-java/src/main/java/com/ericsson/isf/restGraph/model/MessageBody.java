package com.ericsson.isf.restGraph.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author eakinhm
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageBody {
	private String contentType;
	private String content;
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	
}
