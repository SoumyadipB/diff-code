
package com.ericsson.isf.controller.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ericsson.isf.model.Response;

@ControllerAdvice
public class IsfExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger LOGGER = Logger.getLogger(IsfExceptionHandler.class);
	private static final String SIGNUM_FIELDNAME = "Signum";
	private static final String LOGGER_USER_TEXT = "User: ";

	@ExceptionHandler(value = ConstraintViolationException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public @ResponseBody Response<Void> handleConstraintViolationException(ConstraintViolationException ex) {
		LOGGER.error("Constraint Violation Exception Occurred : " + ex.getMessage(), ex);
		LOGGER.error("--------------------------------------------------");
		Response<Void> apiResponse = new Response<Void>();
		apiResponse.addFormError(ex.getMessage());
		return apiResponse;
	}

//	@ExceptionHandler(value = { Exception.class })
//	protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
//		LOGGER.error(LOGGER_USER_TEXT + request.getHeader(SIGNUM_FIELDNAME), ex);
//		return handleExceptionInternal(ex, ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
//	}


	
	// >> Remove Global Exception in order to merge External API code changes
	/*  
	 * @ExceptionHandler(value = { Exception.class }) protected
	 * ResponseEntity<Response> handleConflict(Exception ex, WebRequest request) {
	 * LOGGER.error(LOGGER_USER_TEXT + request.getHeader(SIGNUM_FIELDNAME), ex);
	 * Response<Map<String, Object>> apiResponse = new Response<Map<String,
	 * Object>>(); apiResponse.addErrorsDetail("500", "INTERNAL_SERVER_ERROR");
	 * apiResponse.addFormError(ex.toString()); return new
	 * ResponseEntity<Response>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR); }
	 * 
	 * @ExceptionHandler(IOException.class)
	 * 
	 * @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) public
	 * ResponseEntity<Response> exceptionHandler(IOException e, HttpServletRequest
	 * request) { if
	 * (StringUtils.containsIgnoreCase(ExceptionUtils.getRootCauseMessage(e),
	 * "Broken pipe")) { return null; } else { Response<Map<String, Object>>
	 * apiResponse = new Response<Map<String, Object>>();
	 * apiResponse.addErrorsDetail("500", "BROKEN_PIPE_EXCEPTION");
	 * apiResponse.addFormError(e.getMessage()); return new
	 * ResponseEntity<Response>(apiResponse, HttpStatus.SERVICE_UNAVAILABLE); } }
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
			Response<Map<String, Object>> errorResponse = new Response<Map<String, Object>>();
//			errorResponse.addFormError(ex.getMessage());
			errorResponse.addFormError(alterExceptionMessage(ex.getMessage()));
			errorResponse.addErrorsDetail("400", "BAD_REQUEST");
		
		    return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
//	@ExceptionHandler(value = { Exception.class })
//	protected ResponseEntity<Response> handleConflict(Exception ex, WebRequest request) {
//		LOGGER.error(LOGGER_USER_TEXT + request.getHeader(SIGNUM_FIELDNAME), ex);
//		Response<Map<String, Object>> apiResponse = new Response<Map<String, Object>>();
//		apiResponse.addErrorsDetail("500", "INTERNAL_SERVER_ERROR");
//		apiResponse.addFormError(ex.toString());
//		return new ResponseEntity<Response>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
//	@ExceptionHandler(IOException.class)
//	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)  
//	public ResponseEntity<Response> exceptionHandler(IOException e, HttpServletRequest request) {
//	    if (StringUtils.containsIgnoreCase(ExceptionUtils.getRootCauseMessage(e), "Broken pipe")) {   
//	        return null;          
//	    } else {
//	    	Response<Map<String, Object>> apiResponse = new Response<Map<String, Object>>();
//			apiResponse.addErrorsDetail("500", "BROKEN_PIPE_EXCEPTION");
//			apiResponse.addFormError(e.getMessage());
//			return new ResponseEntity<Response>(apiResponse, HttpStatus.SERVICE_UNAVAILABLE);  
//	    }
//	}
//	@Override
//	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//			HttpHeaders headers, HttpStatus status, WebRequest request) {
////			Response<Map<String, Object>> errorResponse = new Response<Map<String, Object>>();
////			errorResponse.addFormError(ex.getMessage());
////			errorResponse.addErrorsDetail("400", "BAD_REQUEST");
//			Response<Map<String, Object>> errorResponse = new Response<Map<String, Object>>();
//			errorResponse.addErrorsDetail("400", "BAD_REQUEST");
//			errorResponse.addFormError(ex.getMessage());
//		
//		    return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
//	}
    public static String alterExceptionMessage(String errorMessage) {
        String error="";
        if(errorMessage==null || errorMessage.isEmpty()) {
        error=errorMessage;
        }
        else {
        String[] sarr=errorMessage.split("default message");
        error=sarr[sarr.length-1].replace("[", StringUtils.EMPTY).replace("]", StringUtils.EMPTY);
        }
        return error;
        }

	

}
