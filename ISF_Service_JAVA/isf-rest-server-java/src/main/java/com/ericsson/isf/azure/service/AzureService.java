package com.ericsson.isf.azure.service;

import com.ericsson.isf.model.EventPublisherRequestModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface AzureService {
	@POST("signalr/")
	Call<String> callSignalR(@Body EventPublisherRequestModel eventPublisherRequestModel);

}
