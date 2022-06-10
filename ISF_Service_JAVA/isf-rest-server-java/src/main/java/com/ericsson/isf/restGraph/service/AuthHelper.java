package com.ericsson.isf.restGraph.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.web.util.UriComponentsBuilder;

import com.ericsson.isf.restGraph.model.AzureAppDetailsEnum;
import com.ericsson.isf.restGraph.model.TokenRequest;
import com.ericsson.isf.restGraph.model.TokenResponse;
import com.ericsson.isf.util.AppUtil;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * 
 * @author eakinhm
 *
 */
public class AuthHelper {
	private static final String authority = "https://login.microsoftonline.com";
	private static final String authorizeUrl = authority + "/common/oauth2/v2.0/authorize";

	private static String[] scopes = { "openid", "offline_access", "profile", "User.Read", "Mail.Read",
			"Calendars.Read", "Contacts.Read" };

	private static String clientID = null;
	private static String clientSecret = null;
	private static String redirectUrl = null;
	private static String tenantID = null;
	private static AzureAppDetailsEnum azureAppDetailsEnum;

	private static String getTenantID() {
		if (tenantID == null) {
			try {
				loadConfig();
			} catch (Exception e) {
				return null;
			}
		}
		return tenantID;
	}

	private static String getClientID() {
		if (clientID == null) {
			try {
				loadConfig();
			} catch (Exception e) {
				return null;
			}
		}
		return clientID;
	}

	private static String getClientSecret() {
		if (clientSecret == null) {
			try {
				loadConfig();
			} catch (Exception e) {
				return null;
			}
		}
		return clientSecret;
	}

	private static String getRedirectUrl() {
		if (redirectUrl == null) {
			try {
				loadConfig();
			} catch (Exception e) {
				return null;
			}
		}
		return redirectUrl;
	}

	private static String getScopes() {
		StringBuilder sb = new StringBuilder();
		for (String scope : scopes) {
			sb.append(scope + " ");
		}
		return sb.toString().trim();
	}

	private static void loadConfig() throws IOException {

		tenantID = azureAppDetailsEnum.getTenantID();
		clientID = azureAppDetailsEnum.getClientID();
		clientSecret = AppUtil.DecryptText(azureAppDetailsEnum.getClientSecret(), "tser");
		redirectUrl = azureAppDetailsEnum.getRedirectUrl();
	}

	public static String getLoginUrl(UUID state, UUID nonce) {

		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(authorizeUrl);
		urlBuilder.queryParam("client_id", getClientID());
		urlBuilder.queryParam("redirect_uri", getRedirectUrl());
		urlBuilder.queryParam("response_type", "code id_token");
		urlBuilder.queryParam("scope", getScopes());
		urlBuilder.queryParam("state", state);
		urlBuilder.queryParam("nonce", nonce);
		urlBuilder.queryParam("response_mode", "form_post");

		return urlBuilder.toUriString();
	}

	public static TokenResponse getTokenFromAuthCode(TokenRequest tokenRequest, AzureAppDetailsEnum azureApp) {
		// Create a logging interceptor to log request and responses
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

		// Create and configure the Retrofit object
		Retrofit retrofit = new Retrofit.Builder().baseUrl(authority).client(client)
				.addConverterFactory(JacksonConverterFactory.create()).build();

		// Generate the token service
		TokenService tokenService = retrofit.create(TokenService.class);
		azureAppDetailsEnum = azureApp;

		try {
			return tokenService.getAccessTokenFromAuthCode(getTenantID(), getClientID(), getClientSecret(),
					"authorization_code", tokenRequest.getCode(), getRedirectUrl()).execute().body();
		} catch (IOException e) {
			TokenResponse error = new TokenResponse();
			error.setError("IOException");
			error.setErrorDescription(e.getMessage());
			return error;
		}
	}

	public static TokenResponse getAccessTokenFromRefreshToken(String refreshToken, AzureAppDetailsEnum azureApp) {

		azureAppDetailsEnum = azureApp;

		// Expired, refresh the tokens
		// Create a logging interceptor to log request and responses
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

		// Create and configure the Retrofit object
		Retrofit retrofit = new Retrofit.Builder().baseUrl(authority).client(client)
				.addConverterFactory(JacksonConverterFactory.create()).build();

		// Generate the token service
		TokenService tokenService = retrofit.create(TokenService.class);

		try {
			return tokenService.getAccessTokenFromRefreshToken(getTenantID(), getClientID(), getClientSecret(), "refresh_token",
					refreshToken, getRedirectUrl()).execute().body();
		} catch (IOException e) {
			TokenResponse error = new TokenResponse();
			error.setError("IOException");
			error.setErrorDescription(e.getMessage());
			return error;
		}
	}
}
