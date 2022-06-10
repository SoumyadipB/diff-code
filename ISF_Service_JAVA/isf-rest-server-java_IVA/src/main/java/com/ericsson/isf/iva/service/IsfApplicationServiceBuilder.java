package com.ericsson.isf.iva.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ericsson.isf.iva.model.HeaderModel;
import com.ericsson.isf.iva.profiles.configuration.AppConfig;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Request.Builder;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.activity.InvalidActivityException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


@Component
public class IsfApplicationServiceBuilder {
	
	@Autowired
	private AppConfig appconfig;
	
	private Retrofit retrofit;
	
	@Bean
	public void createRetrofit() {
			
			String baseUrl=appconfig.getIsfUrl();
			// Create a request interceptor to add headers that belong on
			// every request
			Interceptor requestInterceptor = new Interceptor() {
				@Override
				public Response intercept(Interceptor.Chain chain) throws IOException {
					Request original = chain.request();
					Builder builder = original.newBuilder()
							.header("X-Auth-Token", "Internal")
							.header("Content-Type", "application/json")
							.header("Accept", "application/json")
							.method(original.method(), original.body());

					Request request = builder.build();
					return chain.proceed(request);
				}
			};
					
			
			OkHttpClient client =getUnsafeOkHttpClient().newBuilder()
					.addInterceptor(requestInterceptor)
					.readTimeout(1, TimeUnit.MINUTES)
					.connectTimeout(1, TimeUnit.MINUTES)
					.writeTimeout(1, TimeUnit.MINUTES).build();
			
			// Create and configure the Retrofit object
			retrofit = new Retrofit.Builder()
					.baseUrl(baseUrl)
					.client(client)
					.addConverterFactory(JacksonConverterFactory.create())
					.build();
	}
	
	public IsfApplicationService getIsfApplicationService() {
		if(retrofit==null) {
			createRetrofit();
		}
			// Generate the token service
		return retrofit.create(IsfApplicationService.class);
	}
	
	private OkHttpClient getUnsafeOkHttpClient() {
		  try {
		    // Create a trust manager that does not validate certificate chains
		    final TrustManager[] trustAllCerts = new TrustManager[] {
		        new X509TrustManager() {
		          @Override
		          public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
		          }

		          @Override
		          public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
		          }

		          @Override
		          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return new java.security.cert.X509Certificate[]{};
		          }
		        }
		    };

		    // Install the all-trusting trust manager
		    final SSLContext sslContext = SSLContext.getInstance("SSL");
		    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
		    // Create an ssl socket factory with our all-trusting manager
		    final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

		    OkHttpClient.Builder builder = new OkHttpClient.Builder();
		    builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
		    builder.hostnameVerifier(new HostnameVerifier() {
		      @Override
		      public boolean verify(String hostname, SSLSession session) {
		        return true;
		      }
		    });

		    OkHttpClient okHttpClient = builder.build();
		    return okHttpClient;
		  } catch (Exception e) {
		    throw new RuntimeException(e);
		  }
		}
		
	
}
