package com.ericsson.isf.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.model.SignalR;
import com.ericsson.isf.model.SignalrModel;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Component
public class SignalRService {
	@Autowired
	private ApplicationConfigurations configurations;
	
	static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SignalRService.class);

	public SignalR sendAndReceiveMessageToSignalR(SignalrModel signalrModel)
			throws InterruptedException, ExecutionException, NoSuchAlgorithmException, KeyManagementException {

		SignalR signalR = new SignalR();

		// Create a new console logger
		Logger logger = new Logger() {

			@Override
			public void log(String message, LogLevel level) {
				System.out.println(message);
			}
		};
////////////////////////////////
//for SSH
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}

		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
//// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		// Connect to the server
		LOG.info("hub url is: "  + signalrModel.getHubUrl());
		HubConnection conn = new HubConnection(signalrModel.getHubUrl(), "", true, logger);

		System.out.println("connection Value1 : " + conn.getConnectionId());
		LOG.info("connection Value1 : " + conn.getConnectionId());

		// Create the hub proxy
		HubProxy proxy = conn.createHubProxy(signalrModel.getHubName());
		
		SignalRFuture<Object> futureObject = null;
		if(signalrModel.getMethodName().equals("updateFloatingWindow")) {
			conn.connected(()->invokeOnHubUpdateFloatingWindow(conn,proxy,futureObject,signalrModel,signalR));		
			conn.start().get();
		}
		else {
			conn.connected(()->invokeOnHub(conn,proxy,futureObject,signalrModel,signalR));		
			conn.start().get();
		}
		
		if(signalrModel.getMethodName().equals("updateFloatingWindow")) {
			conn.connected(()->invokeOnHubUpdateFloatingWindow(conn,proxy,futureObject,signalrModel,signalR));
		}
		else {
			conn.connected(()->invokeOnHub(conn,proxy,futureObject,signalrModel,signalR));
		}
		conn.start().get();
		LOG.info("connection Value3 : " + conn.getConnectionId());
		//conn.stop();
		conn.disconnect();
		LOG.info("connection id after disconnect : " + conn.getConnectionId());
		LOG.info("connection status after disconnect : " + conn.getState());
		return signalR;

	}

	private void invokeOnHub(HubConnection conn, HubProxy proxy, SignalRFuture<Object> futureObject, SignalrModel signalrModel, SignalR signalR) {
		if (conn.getConnectionId() != null) {
			LOG.info("connection Value2 : " + conn.getConnectionId());
			if (signalrModel.getExecutionType().equalsIgnoreCase("signalR")) {
				System.out.println("============================connection Id is: =========================== "+conn.getConnectionId());
				try {
					LOG.info("Inside signalrModel.getExecutionType() if condition");
					ObjectMapper Object = new ObjectMapper();
					String jsonStr = Object.writeValueAsString(signalrModel.getPayload());
					futureObject=proxy.invoke(Object.class, signalrModel.getMethodName(), jsonStr);
					LOG.info("getting message from future object:=======" + futureObject.get());
					signalR.setMessage("Signalr called Successfully.");
				} catch (Exception e) {
					LOG.info("connection Value4 : " + conn.getConnectionId());
					LOG.info("exception = " + e.getMessage());
					signalR.setMessage("Unable to connect Signalr.");
				}
			}

		} 
	}
	
	private void invokeOnHubUpdateFloatingWindow(HubConnection conn, HubProxy proxy, SignalRFuture<Object> futureObject, SignalrModel signalrModel, SignalR signalR) {
		if (conn.getConnectionId() != null) {
			LOG.info("connection Value2 : " + conn.getConnectionId());
			if (signalrModel.getExecutionType().equalsIgnoreCase("signalR")) {
				System.out.println("============================connection Id is: =========================== "+conn.getConnectionId());
				try {
					LOG.info("Inside signalrModel.getExecutionType() if condition");
					futureObject=proxy.invoke(Object.class, signalrModel.getMethodName(), signalrModel.getPayload().toString(), StringUtils.EMPTY);
					LOG.info("getting message from future object:=======" + futureObject.get());
					signalR.setMessage("Signalr called Successfully.");
				} catch (Exception e) {
					LOG.info("connection Value4 : " + conn.getConnectionId());
					LOG.info("exception = " + e.getMessage());
					signalR.setMessage("Unable to connect Signalr.");
				}
			}

		} 
	}

	public String testSignalR() {
		String msg="SignalR is UP";
		return msg;
	}

}
