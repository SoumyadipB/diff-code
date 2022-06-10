package com.ericsson.isf.security.aes;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.controller.MobileController;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Order(2)
@RestControllerAdvice
public class ResponseEncryption implements ResponseBodyAdvice<Object>{
	
	@Autowired
    private ApplicationConfigurations configurations;
	
	@Autowired
	private AesUtil aesUtil;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return configurations.getBooleanProperty(ConfigurationFilesConstants.AUDITING_ENABLED, true) && returnType.hasMethodAnnotation(Encrypt.class);
	}

	
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Object resp=aesUtil.encryptBase64(mapper.writeValueAsString(body));
			return resp;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return body;
		}
	}
	
	

}
