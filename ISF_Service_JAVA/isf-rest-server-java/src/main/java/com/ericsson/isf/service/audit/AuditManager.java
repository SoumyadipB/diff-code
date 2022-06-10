/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service.audit;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 *
 * @author edhhklu
 */
@Service
public class AuditManager {
	
	@Autowired
    private ApplicationConfigurations configurations;
	
	
	 private static final Logger LOG = LoggerFactory.getLogger(AuditManager.class);
	
	
	
	private BlockingQueue<AuditDataProcessorRequestModel> auditDataQueue = new LinkedBlockingQueue<AuditDataProcessorRequestModel>();
	
	
	 @Autowired
	 private TaskExecutor taskExecutor;
	 
	 
	 @Autowired
	 private ApplicationContext applicationContext;
	
	
	@PostConstruct
	public void initializeAuditor(){
		if( configurations.getBooleanProperty(ConfigurationFilesConstants.AUDITING_ENABLED, true)){
			new Thread(new Runnable() {
				@Override
				public void run() {
					startAuditor();
				}
			}).start();
		}
	}
	
	@Bean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(configurations.getIntegerProperty(ConfigurationFilesConstants.AUDITING_THREADCOUNT));
        executor.setMaxPoolSize(configurations.getIntegerProperty(ConfigurationFilesConstants.AUDITING_THREADCOUNT));
        executor.setThreadNamePrefix("Audit_Processor_");
        executor.initialize();
        return executor;
    }
	
	public void startAuditor(){
		AuditDataProcessorRequestModel data=null;
		try {
			do{
				data =auditDataQueue.take();
				AuditProcessorThread myThread = applicationContext.getBean(AuditProcessorThread.class);
				myThread.setAuditData(data);
			    taskExecutor.execute(myThread);
			}while(data!=null);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
    
    public void audit(AuditDataProcessorRequestModel auditData){
    	if(configurations.getBooleanProperty(ConfigurationFilesConstants.AUDITING_ENABLED, true)){
    		auditDataQueue.add(auditData);
    	}
    }
    
    @Service
    @Scope("prototype")
    private class AuditProcessorThread implements Runnable {
    	AuditDataProcessorRequestModel auditData;
    	
    	@Autowired
    	private AuditDataProcessor auditDataProcessor;

		public void setAuditData(AuditDataProcessorRequestModel auditData) {
			this.auditData = auditData;
		}

		@Override
		public void run() {
			String processorMethodName=auditData.getController()+"_"+auditData.getApiEndpoint();
			String requestBody = null;
		    try {
		    	 Method method = auditDataProcessor.getClass().getMethod(processorMethodName,AuditDataProcessorRequestModel.class);
		    	 
		    	 if(auditData.getRequestBody()!=null && auditData.getParamTypes()!=null && auditData.getParamTypes().length>0){
		    		 	Class<?> modelType = auditData.getParamTypes()[0];
		    		 	Gson gson;
			    		//Gson gson = new Gson();
			    		if(processorMethodName.equals("woManagement_massUpdateWorkOrder")) {
			    			 gson= new GsonBuilder().setDateFormat("yyyy-MM-dd").disableHtmlEscaping().create();
			    		}
			    		else {
			    			gson = new GsonBuilder().disableHtmlEscaping().create();
			    		}
			    		
			    		requestBody = StringEscapeUtils.unescapeHtml(auditData.getRequestBody().toString());
			    		
			    		//Gson gson = new Gson();
			    	//	System.out.println("BODY :::::::::::::::::::" + requestBody);
			    	//	System.out.println("ModelType :::::::::::::::::::" + modelType);
			    	//	requestBody = URLDecoder.decode(requestBody, "UTF-8");
			    		
			    		// % issue in auditing while wo transfer ref. #1935299
			    		if(processorMethodName.equals("mobileController_createServiceRequest")) {
			    			auditData.setRequestBody(requestBody);
			    		}
			    		else {
			    			requestBody = replacer(requestBody);
				    		Object modelData = new Object();
				    		modelData =gson.fromJson(requestBody,modelType);
				    		auditData.setRequestBody(modelData);
			    		}
		    	 }
		    	 method.invoke(auditDataProcessor,auditData);
		    	 //LOG.debug("Audit Call success:"+processorMethodName);
			} catch (Exception e) {
				//LOG.error("Error in audit processing Manager:"+e.getMessage());
				//LOG.error(requestBody);
				//e.printStackTrace();
			}
		}

	}
    
    public static String replacer(String data) {

        //String data = outBuffer.toString();
        try {
            StringBuffer tempBuffer = new StringBuffer();
            int incrementor = 0;
            int dataLength = data.length();
            while (incrementor < dataLength) {
                char charecterAt = data.charAt(incrementor);
                if (charecterAt == '%') {
                    tempBuffer.append("<percentage>");
                }
                else if(charecterAt == '+') {
                	tempBuffer.append("<plus>");
                }
                else {
                    tempBuffer.append(charecterAt);	
                }
                incrementor++;
            }
            data = tempBuffer.toString();
            data = URLDecoder.decode(data, "utf-8");
            data = data.replaceAll("<percentage>", "%").replace("<plus>", "+");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
