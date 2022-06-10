package com.ericsson.isf.iva.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author edhhklu
 * Base response class
 * Use this class to pass JWT encrypted response To IVA
 */
public class IsfResponseModel<T> {
	
	private Integer formErrorCount=0;
	private Integer formMessageCount=0;
	private Integer formWarningCount=0;
	private boolean isValidationFailed;
	private Map<Integer,String> formErrors;
	private Map<Integer,String> formMessages;
	private Map<Integer,String> formWarnings;
	private T responseData;
	private Map<String,Object> errorsDetail;
	
	@JsonProperty(value="isValidationFailed")
	public boolean isValidationFailed() {
		return isValidationFailed;
	}

	public Integer getFormErrorCount() {
		return formErrorCount;
	}

	public void setFormErrorCount(Integer formErrorCount) {
		this.formErrorCount = formErrorCount;
	}

	public Integer getFormMessageCount() {
		return formMessageCount;
	}

	public void setFormMessageCount(Integer formMessageCount) {
		this.formMessageCount = formMessageCount;
	}

	public Integer getFormWarningCount() {
		return formWarningCount;
	}

	public void setFormWarningCount(Integer formWarningCount) {
		this.formWarningCount = formWarningCount;
	}

	public Map<Integer, String> getFormMessages() {
		return formMessages;
	}

	public void setFormMessages(Map<Integer, String> formMessages) {
		this.formMessages = formMessages;
	}

	public Map<Integer, String> getFormWarnings() {
		return formWarnings;
	}

	public void setFormWarnings(Map<Integer, String> formWarnings) {
		this.formWarnings = formWarnings;
	}

	public void setValidationFailed(boolean isValidationFailed) {
		this.isValidationFailed = isValidationFailed;
	}

	public Map<Integer, String> getFormErrors() {
		return formErrors;
	}

	public void setFormErrors(Map<Integer, String> formErrors) {
		this.formErrors = formErrors;
	}

	public T getResponseData() {
		return responseData;
	}

	public void setResponseData(T responseData) {
		this.responseData = responseData;
	}

	public void addFormError(String errormessage){
		if(!isValidationFailed){
			isValidationFailed=true;
		}
		formErrors.put((formErrorCount++), errormessage);
	}
	
	public void addFormMessage(String message){
		formMessages.put((formMessageCount++), message);
	}
	
	public void addFormWarning(String message){
		formWarnings.put((formWarningCount++), message);
	}
	
	public IsfResponseModel() {
		formErrors=new HashMap<>();
		formMessages=new HashMap<>();
		formWarnings=new HashMap<>();
		errorsDetail = new HashMap<>();
	}
    

	public IsfResponseModel(T responseData) {
		super();
		this.responseData = responseData;
	}

	public Map<String, Object> getErrorsDetail() {
		return errorsDetail;
	}

	public void setErrorsDetail(Map<String, Object> errorsDetail) {
		this.errorsDetail = errorsDetail;
	}

	public void addErrorsDetail(String key, Object errorDescription){
		errorsDetail.put(key, errorDescription);
	}
}

