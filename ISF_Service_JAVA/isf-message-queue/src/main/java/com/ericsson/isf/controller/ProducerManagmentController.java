/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.model.EmailModel;


@RestController
@RequestMapping("/producerManagment")
public class ProducerManagmentController{
	
	@Value("${kafka.email.topic}")
    private String EMAIL_TOPIC ;
	
	@Autowired
	KafkaTemplate<String, EmailModel> kafkaTemplate ;
//	private static final String TOPIC = "first_test_topic" ;
	private static final Logger LOGGER=LoggerFactory.getLogger(ProducerManagmentController.class);
	
    @PostMapping(path = "/publishMessage", consumes = "application/json", produces = "application/json")
    public String kafkaProducer(@RequestBody EmailModel emailModel) {
    	
    	kafkaTemplate.send(EMAIL_TOPIC,emailModel);
        return "Published Successfully";
    }
    
    @GetMapping("/test/{message}")
    public String kafkaproducer(@PathVariable("message") final String message) {
    //	kafkaTemplate.send(TOPIC,message);
        return "Jatin";
    }
}

