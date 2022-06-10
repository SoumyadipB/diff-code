package com.ericsson.isf.apiManager;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import retrofit2.Retrofit;

@Component
public class ApiManagerServiceBuilder {
	
	 @Autowired 
	 private Retrofit retrofit;

	 public ApiManagerService getApiManagerService() {
		  // Generate the token service
		  return retrofit.create(ApiManagerService.class);
	 }

}
