package com.ericsson.isf.service;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ericsson.isf.config.ConfigurationFilesConstants;
import com.ericsson.isf.model.EmailModel;
@Service
public class SMTPMail {
	@Value("${mail.smtp.host}")
    private String SMTP_MAIL_URL;
	
	@Value("${mail.user}")
    private String SMTP_MAIL_USER;
	
	@Value("${mail.password}")
    private String SMTP_MAIL_PASSWORD;
	
	@Value("${mail.smtp.starttls.enable}")
    private String SMTP_MAIL_STARTTLS;
	
	@Value("${mail.smtp.port}")
    private String SMTP_MAIL_PORT;
	
	@Value("${mail.smtp.auth}")
    private String SMTP_MAIL_AUTH;

	
	public void sendSmtpMailThread(EmailModel emailDetails, boolean update)
			throws MessagingException, IOException, InterruptedException {
		
		Properties properties = System.getProperties();
		properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_URL,
				SMTP_MAIL_URL);
	    properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_USER,
	    		SMTP_MAIL_USER);                   
	    properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_PASSWORD,
	    		SMTP_MAIL_PASSWORD);
	    properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_STARTTLS,
	    		SMTP_MAIL_STARTTLS);
		properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_PORT,
				SMTP_MAIL_PORT);
		// enable authentication 
        properties.put(ConfigurationFilesConstants.SMTP_MAIL_AUTH,
        		SMTP_MAIL_AUTH); 
        properties.put("mail.smtp.ssl.protocols","TLSv1.2"); 
          
        // SSL Factory 
        //properties.put("mail.smtp.socketFactory.class", 
        //        "javax.net.ssl.SSLSocketFactory");
		//Session session = Session.getInstance(properties);
		Session session = Session.getInstance(properties, 
		          new javax.mail.Authenticator() { 
		             
		            //override the getPasswordAuthentication method 
		            protected javax.mail.PasswordAuthentication  
		                           getPasswordAuthentication() { 
		                                         
		                return new javax.mail.PasswordAuthentication(SMTP_MAIL_USER,  
		                		SMTP_MAIL_PASSWORD); 
		            } 
		          });
		
		MimeMessage message = new MimeMessage(session);

		// from
		if (StringUtils.isNotBlank(emailDetails.getSenderSignum())) {
			message.setFrom(new InternetAddress(emailDetails.getSenderSignum().replaceAll("\\s", "")));
		} else {
			emailDetails.setSenderSignum(ConfigurationFilesConstants.MAIL_SENDER_ID);
			message.setFrom(new InternetAddress(emailDetails.getSenderSignum().replaceAll("\\s", "")));
		}

		// copy-recepients
		if (StringUtils.isNotBlank(emailDetails.getCc())) {
			String[] recipientList1 = emailDetails.getCc().split(";");
			List<String> recipientList2 = new ArrayList<String>(Arrays.asList(recipientList1));
			recipientList2.removeAll(Arrays.asList("", null));
			String[] recipientList = recipientList2.toArray(new String[recipientList2.size()]);

			InternetAddress[] recipientAddressCopy = new InternetAddress[recipientList.length];
			int counter = 0;
			for (String recipientcopy : recipientList) {
				recipientAddressCopy[counter] = new InternetAddress(recipientcopy.trim());
				counter++;
			}
			message.setRecipients(Message.RecipientType.CC, (recipientAddressCopy));
		}

		message.setContent(emailDetails.getBody(), "text/html");
		// to , recipients
		if (StringUtils.isNotBlank(emailDetails.getTo())) {
			String[] toList1 = emailDetails.getTo().split(";");
			List<String> toList2 = new ArrayList<String>(Arrays.asList(toList1));
			toList2.removeAll(Arrays.asList("", null));

			String[] toList = toList2.toArray(new String[toList2.size()]);

			InternetAddress[] recipientAddress = new InternetAddress[toList.length];
			int counter = 0;
			for (String recipient : toList) {
				if (StringUtils.isNotBlank(recipient)) {
					// String recepient1 = recipient.replaceAll("[^ . ^
					// @,a-zA-Z0-9]","").replaceAll("\\s.*", "");
					recipientAddress[counter] = new InternetAddress(recipient.trim());
					counter++;
				}

			}

			message.setRecipients(Message.RecipientType.TO, (recipientAddress));
			if (emailDetails.getSubject() != null) {
				message.setSubject(emailDetails.getSubject());
			}

			Transport.send(message);
			// System.out.println("Mail Sent !!!");

			

		} else {
			System.out.println("Mail Not sent");
		}
	}
	

	

}
