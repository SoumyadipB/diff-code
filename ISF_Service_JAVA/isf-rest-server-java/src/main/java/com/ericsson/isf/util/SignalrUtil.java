package com.ericsson.isf.util;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.model.SignalrModel;
import com.ericsson.isf.service.ValidationUtilityService;

@Component
public class SignalrUtil {
	
	@Autowired
	private ApplicationConfigurations configurations;
	
	@Autowired
	private ValidationUtilityService validationUtilityService;

	private final static Logger LOG = LoggerFactory.getLogger(SignalrModel.class);
	
	public Supplier<SignalrModel> returnDefaultSignalrConfiguration=()-> {
			SignalrModel signalRModel= new SignalrModel();
			signalRModel.setHubName(configurations.getStringProperty(ConfigurationFilesConstants.HUB_NAME));
			//signalRModel.setHubUrl(configurations.getStringProperty(ConfigurationFilesConstants.HUB_URL));
			//signalRModel.setHubUrl(validationUtilityService.getCurrentEnvironment("BaseUrl"));
			signalRModel.setHubUrl(System.getenv("CONFIG_BASE_URL"));
			signalRModel.setExecutionType(AppConstants.SIGNALR_EXECUTION_TYPE);
			signalRModel.setPayload(null);
			return signalRModel;
		};
		
	public void callSignalrApplicationToCallSignalRHub(SignalrModel signalrModel) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						connectSignalrApplication(signalrModel,
								configurations.getStringProperty(ConfigurationFilesConstants.SIGNALR_APPLICATION_URL)+ AppConstants.CALL_SIGNALR_API);
					} catch (Exception e) {
						LOG.debug(String.format("Exception thrown  %s", e.getMessage()));
					}

				}
			}).start();

	}
	 
	 public static void connectSignalrApplication(SignalrModel signalRModel,String url) throws org.apache.http.ParseException, IOException {
		    
			RestTemplate restTemplate = new RestTemplate();
			String res = StringUtils.EMPTY;
			LOG.info("url called============:" + url);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<SignalrModel> request = new HttpEntity<>(signalRModel, headers);
			
			try {
				LOG.info("\nSignalR Request : " + request.getBody());
				res = restTemplate.postForObject(url, request, String.class);
				System.out.println("SignalR Response : " + res);
			} catch (RestClientException ex) {
				LOG.debug(String.format("Exception thrown  %s", ex.getMessage()));
				res = ex.getMessage();
			}
		}
}
