package com.ericsson.isf.mqttclient.config;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Component
@Configuration("mqtt")
public class MqttConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(MqttConfig.class);
	private final MqttPushClient mqttPushClient;
	
	private final ApplicationConfigurations configurations;

    @Bean
    public MqttPushClient getMqttPushClient() {
    	System.out.println("client name is:==============="+configurations.getStringProperty("mqtt.client.clientId"));
    	mqttPushClient.connect(configurations.getStringProperty("mqtt.client.url"),
				//configurations.getStringProperty("mqtt.client.clientId"),
    		    saltedString(),
				configurations.getStringProperty("mqtt.client.username"),
				configurations.getStringProperty("mqtt.client.password"),
				configurations.getIntegerProperty("mqtt.client.timeout"),
				configurations.getIntegerProperty("mqtt.client.keepalive"));
        return mqttPushClient;
    }
    
    private String saltedString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        logger.info("client Id is-------  "+saltStr);
        return saltStr;

    }
}
