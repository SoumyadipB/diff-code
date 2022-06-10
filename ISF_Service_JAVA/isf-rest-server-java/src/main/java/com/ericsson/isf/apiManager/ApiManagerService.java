package com.ericsson.isf.apiManager;

import com.ericsson.isf.model.ApiManagerRequestModel;
import com.ericsson.isf.model.ApiManagerResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiManagerService {
	
	@PUT("users/{userId}")
	Call<ApiManagerResponseModel> users(@Path("userId") String userId, @Query("api-version") String apiVersion,
			@Query("notify") Boolean notify,@Body ApiManagerRequestModel apiManagerRequestModel);
}
