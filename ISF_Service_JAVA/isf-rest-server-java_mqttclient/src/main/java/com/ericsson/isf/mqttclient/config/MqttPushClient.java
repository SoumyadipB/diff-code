package com.ericsson.isf.mqttclient.config;

import java.io.IOException;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.ericsson.isf.mqttclient.exception.ApplicationException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MqttPushClient {
	private static final String TOPIC_NOT_EXIST = "topic not exist";

	private static final Logger logger = LoggerFactory.getLogger(MqttPushClient.class);

	private static MqttClient client;

	private static MqttClient getClient() {
		return client;
	}

	private static void setClient(MqttClient client) {
		MqttPushClient.client = client;
	}


	/**
	 * Client connection
	 *
	 * @param host      ip+port
	 * @param clientID  Client Id
	 * @param username  User name
	 * @param password  Password
	 * @param timeout   Timeout time
	 * @param keepalive Retention number
	 */
	public void connect(String host, String clientID, String username, String password, int timeout, int keepalive) {
		MqttClient mqttClient;
		try {
			mqttClient = new MqttClient(host, clientID, new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(false);
			options.setUserName(username);
			options.setPassword(password.toCharArray());
			options.setConnectionTimeout(timeout);
			options.setKeepAliveInterval(keepalive);
			options.setAutomaticReconnect(true);
			MqttPushClient.setClient(mqttClient);

			connectClient(mqttClient, options);
			logger.info("Current status of mqttClient on application Load++++++++++{}", client.isConnected());

		} catch (Exception e) {
			logger.error("Some exception occured++++++++++{}", e.getMessage());
		}
	}

	private void connectClient(MqttClient client, MqttConnectOptions options) {
		try {
			client.connect(options);
		} catch (Exception e) {
			logger.info("Some exception occured when establish connection with mqtt++++++++++{}", e.getMessage());
		}
	}

	/**
	 * Release
	 *
	 * @param qos         Connection mode
	 * @param retained    Whether to retain
	 * @param topic       theme
	 * @param pushMessage Message body
	 * @param envName
	 * @throws MqttException
	 * @throws InterruptedException
	 */
	public void publish(int qos, boolean retained, String topic, Object pushMessage) {
		logger.info("================================ Topic name is::::{}", topic);
		MqttMessage message = new MqttMessage();
		message.setQos(qos);
		message.setRetained(retained);
		message.setPayload(convertMessageInString(pushMessage).getBytes());
		MqttTopic mTopic = MqttPushClient.getClient().getTopic(topic);
		if (null == mTopic) {
			logger.error(TOPIC_NOT_EXIST);
			throw new ApplicationException(200, TOPIC_NOT_EXIST);

		}

		logger.info("================================ client status::::{}", client.isConnected());
		MqttDeliveryToken token;

		try {
			token = mTopic.publish(message);
			logger.info("!!!!!!!!!!!!!!!!!!!!!!!!Message  published successfully with MessageId is:!!!!!!!!!!!!!! "+token.getMessageId());
			logger.info("************************Message  published successfully*************");
		} catch (MqttException e) {
			logger.info("Message not published::::::{}", e.getMessage());
			logger.info("================================ client status in catch block::::{}", client.isConnected());
		}

	}

	private String convertMessageInString(Object pushMessage) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// Converting the Java object into a JSON string
			return objectMapper.writeValueAsString(pushMessage);
		} catch (IOException e) {
			logger.error(e.getMessage());
			return e.getMessage();
		}
	}

	/**
	 * Subscribe to a topic
	 *
	 * @param topic theme
	 * @param qos   Connection mode
	 */
	public void subscribe(String topic, int qos) {
		logger.info("Start subscribing to topics{}" , topic);
		try {
			MqttPushClient.getClient().subscribe(topic, qos);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

}