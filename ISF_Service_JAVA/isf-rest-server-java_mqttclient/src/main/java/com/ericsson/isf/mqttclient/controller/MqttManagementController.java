package com.ericsson.isf.mqttclient.controller;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ericsson.isf.mqttclient.dto.MqttPublishModel;
import com.ericsson.isf.mqttclient.service.MqttManagementService;


@RestController
@RequestMapping("/mqttManagement")
public class MqttManagementController {
	private static final Logger LOG = LoggerFactory.getLogger(MqttManagementController.class);
	@Autowired
	private MqttManagementService mqttservice;

	/**
	 * @author ekarmuj
	 * Api name: mqttManagement/testmqtt
	 * Purpose: This Api used to test mqtt application is up or not.
	 * @return String
	 */
	
	@GetMapping("/testmqtt")
	public String testMqtt() {
		return "ISF MQ Telemetry Transport is Up Now!";
	}

	/**
	 * @author ekarmuj
	 * Api name: mqttManagement/publishNotification
	 * Purpose: This Api used to send notification on provided topic.
	 * @param messagePublishModel
	 * @return String
	 * @throws InterruptedException 
	 * @throws MqttException 
	 */
	
	@PostMapping("/publishNotification")
	public String publishMessage(@RequestBody MqttPublishModel messagePublishModel) throws MqttException, InterruptedException {
		LOG.info("env active is"+messagePublishModel.getEnvName());
		return mqttservice.publishNotification(messagePublishModel);
	}

	
}
