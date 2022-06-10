/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.apache.commons.lang3.StringUtils;
//import org.apache.logging.log4j.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.model.EmailModel;
import com.ericsson.isf.service.SMTPMail;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@RestController
@RequestMapping("/consumerManagment")
public class ConsumerManagmentController {
	
	
	@Value("${sendgrid.key}")
    private String SENDGRID_KEY ;
	
	@Autowired 
    private SMTPMail smtpMail;

	private static final String MAIL_SENDER_ID = "isfadmin@ericsson.com";
	private static final Logger LOGGER=LoggerFactory.getLogger(ProducerManagmentController.class);

	/*
	 * @KafkaListener(topics = "Kafka_Example", groupId = "group_id") public void
	 * consume(String message) { System.out.println("Consumed message: " + message);
	 * }
	 */

	@KafkaListener(topics = "${kafka.email.topic}", groupId = "group_json", containerFactory = "userKafkaListenerFactory")
	public void consumeJson(EmailModel emailModel) throws IOException, MessagingException, InterruptedException {
		//sendGridMailUtilityThread(emailModel);
		smtpMail.sendSmtpMailThread(emailModel,false);
	}
		
	public void sendGridMailUtilityThread(EmailModel emailDetails) throws IOException {
		
		Email from = new Email();
		Email subject = new Email();
		Content content = new Content();
		Personalization personalization = new Personalization();
		Mail mail = new Mail();
		if (emailDetails.getStatus() == null) {
			emailDetails.setStatus("SENT");
		}
		// from
		if (StringUtils.isNotBlank(emailDetails.getSenderSignum())) {
			from.setEmail(emailDetails.getSenderSignum());
		} else {
			emailDetails.setSenderSignum(MAIL_SENDER_ID);
			from.setEmail(emailDetails.getSenderSignum());
		}

		mail.setFrom(from);
		if (emailDetails.getReceiverSignum() == null || emailDetails.getReceiverSignum().isEmpty()) {
			emailDetails.setReceiverSignum(emailDetails.getCc());
		}
		// to
		List<String> toMail = Arrays.asList(emailDetails.getReceiverSignum().split(";")).stream().distinct()
				.collect(Collectors.toList());
		if (emailDetails.getReceiverSignum() != null && !emailDetails.getReceiverSignum().isEmpty()) {
			for (String toEmail : toMail) {
				Email to = new Email();
				if (toEmail != null && !toEmail.isEmpty()) {
					to.setEmail(toEmail);
					personalization.addTo(to);
				}
			}
		}
		// cc
		List<String> ccMail = new ArrayList<String>();
		if (emailDetails.getCc() != null) {
			ccMail = Arrays.asList(emailDetails.getCc().split(";")).stream().distinct().collect(Collectors.toList());
		}
		ccMail.removeAll(toMail);
		if (emailDetails.getCc() != null) {
			for (String ccEmail : ccMail) {
				Email cc = new Email();
				if (ccEmail != null && !ccEmail.isEmpty()) {
					cc.setEmail(ccEmail);
					personalization.addCc(cc);
				}

			}
		}
		// subject
		personalization.setSubject(emailDetails.getSubject());
		// personalization.setSubject("jatin");
		// body
		content.setType("text/html");
		content.setValue(emailDetails.getBody());
		// content.setValue("body");
		mail.addPersonalization(personalization);
		mail.addContent(content);

		SendGrid sg = new SendGrid(SENDGRID_KEY);
		Request request = new Request();
		boolean returnFlag = true;
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			returnFlag = true;
			
		} catch (IOException ex) {
			returnFlag = false;
			throw ex;
		}
		
	}

}



