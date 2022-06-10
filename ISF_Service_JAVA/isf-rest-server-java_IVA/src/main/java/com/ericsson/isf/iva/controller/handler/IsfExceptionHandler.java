package com.ericsson.isf.iva.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.ericsson.isf.iva.exception.ApplicationException;
import com.ericsson.isf.iva.model.IsfResponseModel;

@ControllerAdvice
public class IsfExceptionHandler{

	@ExceptionHandler(ServletRequestBindingException.class)
    public final ResponseEntity<Object> handleHeaderException(Exception ex, WebRequest request) 
    {
		IsfResponseModel<Void> response=new IsfResponseModel<>();
		response.addFormError(ex.getLocalizedMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(ApplicationException.class)
	public final ResponseEntity<Object> handleApplicationException(ApplicationException ex, WebRequest request) {
		IsfResponseModel<Void> response=new IsfResponseModel<>();
		response.addFormError(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.resolve(ex.getErrorCode()));
	}
	
	/*
	 * @ResponseBody
	 * 
	 * @ExceptionHandler(Exception.class)
	 * 
	 * @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) public final
	 * ResponseEntity<Object> generationExceptionHandler(Exception e){ return new
	 * ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); }
	 */
	
	

}
