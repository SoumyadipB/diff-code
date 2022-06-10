package com.ericsson.isf.security.aes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.controller.MobileController;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.nimbusds.jose.util.IOUtils;

@ControllerAdvice
public class RequestDecryption extends RequestBodyAdviceAdapter {
	
	@Autowired
    private ApplicationConfigurations configurations;
	
	@Autowired
	private AesUtil aesUtil;

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		 return configurations.getBooleanProperty(ConfigurationFilesConstants.AUDITING_ENABLED, true) && methodParameter.hasParameterAnnotation(Decrypt.class);
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

		try {
			byte[] decrypt =aesUtil.decryptBase64(IOUtils.readInputStreamToString(inputMessage.getBody()));
			final ByteArrayInputStream bais = new ByteArrayInputStream(decrypt);
			return new HttpInputMessage() {
				@Override
				public InputStream getBody() throws IOException {
					return bais;
				}

				@Override
				public HttpHeaders getHeaders() {
					return inputMessage.getHeaders();
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
			return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
		}
	}	
}
