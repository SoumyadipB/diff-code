package com.ericsson.isf.model;

import java.util.List;

public class ApiManagerErrorModel {
	
	private Error error;
	
	public class Error{
		private String code;
		private String message;
		private List<ApiManagerErrorDetails> details;
		
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public List<ApiManagerErrorDetails> getDetails() {
			return details;
		}
		public void setDetails(List<ApiManagerErrorDetails> details) {
			this.details = details;
		}
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
}
