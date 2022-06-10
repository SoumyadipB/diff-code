package com.ericsson.isf.azure.service;

import java.io.IOException;
import java.util.UUID;
import org.apache.commons.collections.CollectionUtils;

import com.ericsson.isf.model.EventPublisherRequestModel;
import com.ericsson.isf.model.HeaderModel;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Request.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class AzureServiceBuilder {

	public static AzureService getAzureService(EventPublisherRequestModel eventPublisherModel, String url) {

		// Create a request interceptor to add headers that belong on
		// every request
		Interceptor requestInterceptor = new Interceptor() {
			@Override
			public Response intercept(Interceptor.Chain chain) throws IOException {
				Request original = chain.request();

				Builder builder = original.newBuilder().header("User-Agent", "ISF")
						.header("client-request-id", UUID.randomUUID().toString())
						.header("return-client-request-id", "true").header("Prefer", "outlook.body-content-type='text'")
						.header("Content-Type", "application/json").header("Accept", "application/json")
						.method(original.method(), original.body());

				if(CollectionUtils.isNotEmpty(eventPublisherModel.getHeaderModels())) {
					
					for (HeaderModel headerModel : eventPublisherModel.getHeaderModels()) {
						builder.header(headerModel.getHeaderKey(), headerModel.getHeaderValue());
					}
				}

				Request request = builder.build();
				return chain.proceed(request);
			}
		};

		// Create a logging interceptor to log request and responses
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(requestInterceptor)
				.addInterceptor(loggingInterceptor).build();

		// Create and configure the Retrofit object
		Retrofit retrofit = new Retrofit.Builder().baseUrl(url).client(client)
				.addConverterFactory(JacksonConverterFactory.create()).build();

		// Generate the EventPublisher Service
		return retrofit.create(AzureService.class);
	}
}
