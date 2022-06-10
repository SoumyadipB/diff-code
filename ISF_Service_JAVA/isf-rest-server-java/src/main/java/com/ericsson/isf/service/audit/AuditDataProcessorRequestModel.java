package com.ericsson.isf.service.audit;

import java.util.Map;

import org.springframework.core.MethodParameter;

public class AuditDataProcessorRequestModel {
	
		private String controller;
		private String apiEndpoint;
		private String apiPath;
	
		private String action;
    	private Map<String,Object> requestParams;
    	private Object response;
    	private Object requestBody;
    	private Class<?>[] paramTypes;
    	private MethodParameter mParam;
    	private Map<String,Object> requestHeaders;
    	
    	
		public MethodParameter getmParam() {
			return mParam;
		}
		public void setmParam(MethodParameter mParam) {
			this.mParam = mParam;
		}
		public Class<?>[] getParamTypes() {
			return paramTypes;
		}
		public void setParamTypes(Class<?>[] paramTypes) {
			this.paramTypes = paramTypes;
		}
		public Object getRequestBody() {
			return requestBody;
		}
		public void setRequestBody(Object requestBody) {
			this.requestBody = requestBody;
		}
		public String getAction() {
			return action;
		}
		public void setAction(String action) {
			this.action = action;
		}
		public String getController() {
			return controller;
		}
		public void setController(String controller) {
			this.controller = controller;
		}
		public String getApiEndpoint() {
			return apiEndpoint;
		}
		public void setApiEndpoint(String apiEndpoint) {
			this.apiEndpoint = apiEndpoint;
		}
		public String getApiPath() {
			return apiPath;
		}
		public void setApiPath(String apiPath) {
			this.apiPath = apiPath;
		}
		public Object getRequestParams() {
			return requestParams;
		}
		public void setRequestParams(Map<String,Object> request) {
			this.requestParams = request;
		}
		public Object getResponse() {
			return response;
		}
		public void setResponse(Object response) {
			this.response = response;
		}
		public Map<String,Object> getRequestHeaders() {
			return requestHeaders;
		}
		public void setRequestHeaders(Map<String,Object> requestHeaders) {
			this.requestHeaders = requestHeaders;
		}
		
}
