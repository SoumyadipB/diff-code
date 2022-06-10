package com.ericsson.isf.mqttclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ericsson.isf.mqttclient.config.AmqpPublisher;
import com.ericsson.isf.mqttclient.dto.AmqpPublishModel;

@RestController
@RequestMapping(value = "/rabbitmq")
public class AmqpController {

	@Autowired
	AmqpPublisher rabbitMQSender;

	
	/**
	 * @author ekarmuj
	 * Api name: /rabbitmq/testAmqp
	 * Purpose: Used as a test Api
	 * @return Message
	 */
	
	@GetMapping("/testAmqp")
	public String testAmqp(@RequestParam("message") String message) {
		return message;
	}
	
	/**
	 * @author ekarmuj
	 * Api name: /rabbitmq/amqpPublisher
	 * Purpose: This Api used to publish message to AMQP(RabbitMq)
	 * @return Message
	 */
	
	@PostMapping(value = "/amqpPublisher")
	public String producer(@RequestBody AmqpPublishModel amqpPublishModel) {
		return "Message sent to the Advanced Message Queuing Protocol  Successfully with qbookingId=  "
				+rabbitMQSender.send(amqpPublishModel);
	}
	 
}
