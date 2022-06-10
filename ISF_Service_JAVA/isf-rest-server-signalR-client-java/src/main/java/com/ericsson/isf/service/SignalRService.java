package com.ericsson.isf.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.ericsson.isf.model.SignalR;
import com.ericsson.isf.model.SignalrModel;

import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;

@Service
public class SignalRService {
	static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SignalRService.class);

	public SignalR sendAndReceiveMessageToSignalR(SignalrModel signalrModel)
			throws InterruptedException, ExecutionException {

		SignalR signalR = new SignalR();

		// Create a new console logger
		Logger logger = new Logger() {

			@Override
			public void log(String message, LogLevel level) {
				System.out.println(message);
			}
		};

		// Connect to the server

		HubConnection conn = new HubConnection(signalrModel.getHubUrl(), "", true, logger);

		// Create the hub proxy
		HubProxy proxy = conn.createHubProxy(signalrModel.getHubName());

		SignalRFuture<Object> futureObject = null;
		conn.start();
		TimeUnit.SECONDS.sleep(3);
		if (conn.getConnectionId() != null) {
			if (signalrModel.getExecutionType().equalsIgnoreCase("signalR")) {
				futureObject = proxy.invoke(Object.class, signalrModel.getMethodName(), signalrModel.getPayload());
				log.info("getting message from future object:=======" + futureObject.get());
			}

			conn.stop();
		}
		if (futureObject == null) {
			signalR.setMessage("can not connect to signalR");
			conn.stop();

		} else {
			signalR.setMessage("message received successfully");
		}

		return signalR;

	}

}
