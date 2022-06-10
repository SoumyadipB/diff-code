

package com.ericsson.isf.controller.handler;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class IsfExceptionHandler extends ResponseEntityExceptionHandler {
	 private static final Logger LOGGER = Logger.getLogger(IsfExceptionHandler.class);
	 private static final String SIGNUM_FIELDNAME="Signum";
	 private static final String LOGGER_USER_TEXT="User: ";
	 
	 @ExceptionHandler(value = { Exception.class })
	 protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request){
		 LOGGER.error(LOGGER_USER_TEXT+request.getHeader(SIGNUM_FIELDNAME),ex); 
		 return handleExceptionInternal(ex,ex, 
					          new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	 }

}
