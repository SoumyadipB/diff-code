package com.ericsson.isf.mqttclient.service;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ericsson.isf.mqttclient.config.MqttPushClient;
import com.ericsson.isf.mqttclient.dto.MqttPublishModel;
import com.ericsson.isf.mqttclient.util.AppConstants;

@Service
public class MqttManagementService {
	private static final Logger logger = LoggerFactory.getLogger(MqttManagementService.class);
	private static final String NEUPLOAD = "NEUPLOAD";

	@Autowired
	private MqttPushClient mqttPushClient;

	public String publishNotification(MqttPublishModel messagePublishModel)
			throws InterruptedException, MqttPersistenceException, MqttException {

		StringBuilder stringBuilder = prepareTopic(messagePublishModel);
		logger.info("================================ Topic name inside publishNotification is::::{}",
				stringBuilder.toString());
		mqttPushClient.publish(messagePublishModel.getQos(), messagePublishModel.isRetained(), stringBuilder.toString(),
				messagePublishModel.getMessage());

		return "MQ Telemetry Transport notification send successfully!";

	}

	private StringBuilder prepareTopic(MqttPublishModel messagePublishModel) {
		StringBuilder stringBuilder = new StringBuilder(StringUtils.EMPTY);
		stringBuilder.append(AppConstants.ISF);
		stringBuilder.append(AppConstants.SLASH);
		stringBuilder.append(NEUPLOAD);
		stringBuilder.append(AppConstants.SLASH);
		stringBuilder.append(returnActiveProfileName(messagePublishModel.getEnvName()));
		stringBuilder.append(AppConstants.SLASH);
		stringBuilder.append(StringUtils.lowerCase(messagePublishModel.getSignum()));
		return stringBuilder;
	}

	private String returnActiveProfileName(String profile) {

		profile = StringUtils.upperCase(StringUtils.trim(profile));
		String env;
		switch (profile) {

		case "DEV_MAJOR":
			env = "Dev_Major";
			break;

		case "DEV_MINOR":
			env = "Dev_Minor";
			break;

		case "DEV_MAJOR_S1":
			env = "Dev_Major_S1";
			break;

		case "DEV_MAJOR_S2":
			env = "Dev_Major_S2";
			break;

		case "SIT_MAJOR":
			env = "Sit_Major";
			break;

		case "SIT_MINOR":
			env = "Sit_Minor";
			break;

		case "SANDBOX1":
			env = "Sandbox1";
			break;

		case "SANDBOX2":
			env = "Sandbox2";
			break;

		case "PP":
			env = "Preprod";
			break;
		case "PT":
			env = "PerfTest";
			break;
		case "DR":
			env = "DryRun";
			break;
		case "PROD":
			env = "Prod";
			break;

		default:
			env = "Dev";
		}
		return env;
	}

}
