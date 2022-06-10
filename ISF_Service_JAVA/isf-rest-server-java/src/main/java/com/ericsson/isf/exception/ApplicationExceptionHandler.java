package com.ericsson.isf.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ericsson.isf.model.ErrorDetail;

@ControllerAdvice
public class ApplicationExceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

	@ExceptionHandler(value = ApplicationException.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ErrorDetail handleApplicationException(ApplicationException ex) {
		LOG.error("Application Exception Occurred, Error Code: " + ex.getErrorCode() + ", Error Message: " + ex.getMessage(), ex);
		return new ErrorDetail(ex.getErrorCode(), ex.getErrorMessage(), "Application Exception", ex.getStackTrace());
	}

	@ExceptionHandler(value = Throwable.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ErrorDetail handleGlobalException(Throwable ex) {
		LOG.error("Global Exception Occurred : " + ex.getMessage(), ex);
		LOG.error("--------------------------------------------------");
		return new ErrorDetail(500, "An error has occurred while performing operation. Please retry or contact technical team", "Global Exception", ex.getStackTrace());
	}
}
