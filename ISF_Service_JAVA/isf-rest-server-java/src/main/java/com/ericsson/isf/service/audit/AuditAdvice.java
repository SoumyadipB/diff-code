package com.ericsson.isf.service.audit;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Order(1)
@ControllerAdvice
public class AuditAdvice implements ResponseBodyAdvice<Object> {
	
	@Autowired
	AuditManager auditManager;

	@Autowired
    private ApplicationConfigurations configurations;
	
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AuditAdvice.class);
	
    @Override
    public boolean supports(MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType) {
    	
    	List<Annotation> annotations = Arrays.asList(returnType.getMethodAnnotations());
        boolean isAuditedApi = annotations.stream().anyMatch(annotation -> annotation.annotationType().equals(AuditEnabled.class));
        return configurations.getBooleanProperty(ConfigurationFilesConstants.AUDITING_ENABLED, true) && isAuditedApi;
    }


    
	@Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {
    	
    	try{
    		//long startTime=System.currentTimeMillis();
	    	String[] token=request.getURI().getPath().trim().split("/+");
	    	HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
	    	
	    	AuditDataProcessorRequestModel auditRequest=new AuditDataProcessorRequestModel();
	    	if("POST".equalsIgnoreCase(servletRequest.getMethod())){
	    		ContentCachingRequestWrapper requestWrapper = null;
				try {
					requestWrapper = WebUtils.getNativeRequest(servletRequest,ContentCachingRequestWrapper.class );
				
					if(!StringUtils.contains(requestWrapper.getContentType(), "multipart/form-data")) 
					{
					String requestBody = new String(requestWrapper.getContentAsByteArray());
					auditRequest.setRequestBody(requestBody);
					}
		    		
				} catch (Exception e) {
					LOG.error(e.getMessage());
					e.printStackTrace();
				}
	    		
	    		auditRequest.setParamTypes(returnType.getMethod().getParameterTypes());
	    	}
	    		
	    	Map<String,Object> requestParams=new HashMap<>(servletRequest.getParameterMap());
	    	for(int i=4;i<token.length;i++){
	    		requestParams.put("pathParam"+(i-3), token[i]);
	    	}
	    	
	    	Map<String,Object> requestHeader=new HashMap<>();
	    	requestHeader.put(AppConstants.ROLE, servletRequest.getHeader(AppConstants.ROLE));
	    	
	    	auditRequest.setApiEndpoint(token[3]);
	    	auditRequest.setController(token[2]);
	    	auditRequest.setResponse(body);
	    	auditRequest.setRequestParams(requestParams);
	    	auditRequest.setRequestHeaders(requestHeader);
	    	auditManager.audit(auditRequest);
	    	//System.out.println("Time taken in audit advice:"+(System.currentTimeMillis()-startTime));
	    	
	        
    	}catch(Exception e){
    		System.out.println("Error processing audit advice : "+e.getMessage());
    	}
    	
    	return body;
    }
    
    

}