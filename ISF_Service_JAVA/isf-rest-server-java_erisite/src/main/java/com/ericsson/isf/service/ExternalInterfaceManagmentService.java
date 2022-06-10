package com.ericsson.isf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.ericsson.isf.model.ErisiteDataModel;
import com.ericsson.isf.model.EventPublisherRequestModel;
import com.ericsson.isf.model.HeaderModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.ConfigurationFilesConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Service
public class ExternalInterfaceManagmentService {

	private static final String CHECK_ISF_HEALTH = "checkIsfHealth";

	@Autowired
	private ApplicationConfigurations configurations;

	private static final Logger LOG = LoggerFactory.getLogger(ExternalInterfaceManagmentService.class);

	/**
	 * @param erisiteDataModel
	 * @param headers
	 * @return
	 */
	public boolean generateWOrkOrder(ErisiteDataModel erisiteDataModel, HttpHeaders headers) {
		LOG.info("in generateWOrkOrder method");
		new Thread(new Runnable() {
			@Override
			public void run() {

				if (configurations.getStringProperty(ConfigurationFilesConstants.CURRENT_ENV).equalsIgnoreCase("ECN")) {

					redirectToCloudForGenerateWorkOrder(erisiteDataModel, headers);

					LOG.info("Redirecting to Cloud URL :"
							+ configurations.getStringProperty(ConfigurationFilesConstants.CURRENT_ENV_URL));

				} else {
					LOG.info("NOT ECN !");
				}
			}

		}).start();

		LOG.info("generateWOrkOrder:SUCCESS");
		return true;
	}

	protected boolean redirectToCloudForGenerateWorkOrder(ErisiteDataModel erisiteDataModel, HttpHeaders headers) {

		LOG.info("in redirectToCloudForGenerateWorkOrder method");

		RestTemplate restTemplate = new RestTemplate();
		String url = configurations.getStringProperty(ConfigurationFilesConstants.CURRENT_ENV_URL)
				+ AppConstants.API_CONTROLLER + AppConstants.FORWARD_SLASH + "generateWOrkOrder";

		headers.set("Accept-Encoding", "identity");

		HttpEntity<ErisiteDataModel> request = new HttpEntity<>(erisiteDataModel, headers);

		ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.POST, request, Boolean.class);

		LOG.info("redirectToCloudForGenerateWorkOrder");

		return response.getBody();
	}

	public String cloudHealthcheckResponse() {
		LOG.info("config  url for cloud redirect is ++++++++++++++++++++++++"
				+ configurations.getStringProperty(ConfigurationFilesConstants.CURRENT_ENV_URL));
		RestTemplate restTemplate = new RestTemplate();
		String url = configurations.getStringProperty(ConfigurationFilesConstants.CURRENT_ENV_URL)
				+ AppConstants.API_CONTROLLER + AppConstants.FORWARD_SLASH + CHECK_ISF_HEALTH;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		return response.getBody();

	}

	public Response<ResponseEntity<String>> callErisiteFromSignalR(EventPublisherRequestModel eventPublisherModel) {
		Response<ResponseEntity<String>> finalResponse = new Response<ResponseEntity<String>>();
		ResponseEntity<String> response = null;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		if (eventPublisherModel.getHeaderModels()!=null) {
			for (HeaderModel header : eventPublisherModel.getHeaderModels()) {
				headers.set(header.getHeaderKey(), header.getHeaderValue());
			}
		}
		LOG.info(eventPublisherModel.toString());
		String url = eventPublisherModel.getUrl();

		try {
			HttpEntity<String> request = new HttpEntity<>(eventPublisherModel.getPayLoad(), headers);
			response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
			System.out.println(response);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finalResponse.setResponseData(response);
		// finalResponse.addFormError(e.getMessage());
		return finalResponse;
	}

}
