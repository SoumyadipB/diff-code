package com.ericsson.isf.config;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ericsson.isf.azure.keyVault.service.ClientSecretKeyValueProvider;
import com.ericsson.isf.model.AzureAppModel;
import com.ericsson.isf.model.VaultModel;
import com.ericsson.isf.service.AzureService;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.ConfigurationFilesConstants;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Request.Builder;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class ApiManagerConfigurations {
	@Autowired
	private ApplicationConfigurations configurations;
	
	@Autowired
	private AzureService azureService;
	
	@Autowired
	private ClientSecretKeyValueProvider clientSecretKeyValueProvider;

	private static final String APIM_AUTH_KEY = "AzApimAuthorizationKey";
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AzureConfigurations.class);
	@Bean
	public Retrofit retrofit() {
		
		String auth=getSecretAuthorizationKey();
		Interceptor requestInterceptor = new Interceptor() {
			@Override
			public Response intercept(Interceptor.Chain chain) throws IOException {
				Request original = chain.request();
				Builder builder = original.newBuilder().header(AppConstants.CONTENT_TYPE, AppConstants.APPLICATION_JSON)
						.header("Authorization",auth)
						.method(original.method(), original.body());

				Request request = builder.build();
				return chain.proceed(request);
			}
		};

		OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(requestInterceptor)
				.readTimeout(1, TimeUnit.MINUTES).connectTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES)
				.build();

		// Create and configure the Retrofit object
		return new Retrofit.Builder().baseUrl(getApiManagerUrl()).client(client)
				.addConverterFactory(JacksonConverterFactory.create()).build();
	}
	
	private String getApiManagerUrl() {
		return String.format(
				"https://%1$s.management.azure-api.net/subscriptions/%2$s/resourceGroups/%3$s/providers/Microsoft.ApiManagement/service/%1$s/",
				configurations.getStringProperty(ConfigurationFilesConstants.API_MANAGER_SERVICE),
				configurations.getStringProperty(ConfigurationFilesConstants.API_MANAGER_SUBSCRIPTION_ID),
				configurations.getStringProperty(ConfigurationFilesConstants.API_MANAGER_RESOURCE_GROUP));
	}
	
	private String getSecretAuthorizationKey() {
		try {

			String keyVaultName = configurations.getStringProperty(ConfigurationFilesConstants.AZURE_KEYVAULT_NAME);
			VaultModel vaultModel = azureService.getActiveSecretInfoByVaultNameAndSecretType(keyVaultName,
					APIM_AUTH_KEY);
			AzureAppModel azureAppModel = azureService.getInfoByAzureAppName(
					configurations.getStringProperty(ConfigurationFilesConstants.AZURE_APP_NAME));
			String secretValue = clientSecretKeyValueProvider.getSecretValue(azureAppModel, vaultModel);
			LOGGER.info("API Manager security key obtained");
			return secretValue;
		
		} catch (Exception e) {

			e.printStackTrace();
			LOGGER.error("error occured while getting apim Auth Key: " + e.getMessage());
			return null;
		}
	}

}
