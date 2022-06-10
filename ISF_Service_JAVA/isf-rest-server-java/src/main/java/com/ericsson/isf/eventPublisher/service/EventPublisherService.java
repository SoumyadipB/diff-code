package com.ericsson.isf.eventPublisher.service;

import org.springframework.http.ResponseEntity;

import com.ericsson.isf.model.EventPublisherRequestModel;
import com.ericsson.isf.model.Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * This is a Retrofit event publisher service class which contains list of APIs.
 * 
 * @author eakinhm
 *
 */
public interface EventPublisherService {

	@POST("eventPublisher/sendRequestToExternalSources")
	Call<Response<ResponseEntity<String>>> sendRequestToExternalSources(@Body EventPublisherRequestModel eventPublisherRequestModel);
	
	@POST("eventPublisher/callSiganlRClient")
	Call<Response<String>> callSiganlRClient(@Body EventPublisherRequestModel eventPublisherRequestModel);

}
