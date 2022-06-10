package com.ericsson.serverbots.components;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

	private static final Logger log = Logger.getLogger(EmailUtil.class);

	public static String EricssonSMTPServerIP="146.11.115.136";
	//public static String EricssonSMTPServerIP="10.64.103.30";
	//public static String smsPostFix="@sms.ericsson.com";
	//public static String mailFrom="SPMSAdmin@ericsson.com";
	public static String mailFrom="no_reply@ericsson.com";

	//	public static void main(String[] args) {
	//		
	//		EmailUtil emailUtil = new EmailUtil();
	//		ArrayList<String> mailList = new ArrayList<>();
	//		
	//		mailList.add("vishal.j.jain@ericsson.com");
	//		mailList.add("mashal.pannu@ericsson.com");
	//		
	//		//emailUtil.sendMail("vishal.j.jain@ericsson.com","Test Mail","Test Mail",false);
	//		
	//		emailUtil.sendMail(mailList,"Test Mail","Test Mail","C:\\temp\\file2.txt","file2.txt",false);
	//	}

	//Send mail to only one without attachment
	public void sendMail(String reciepentEmail,String subject,String message,boolean isMessageHTML) {
		EmailUtil client = new EmailUtil();

		try { 
			client.sendSingleTextMail(EmailUtil.EricssonSMTPServerIP,EmailUtil.mailFrom,reciepentEmail,subject,message,isMessageHTML); 
		} 
		catch(Exception e) { e.printStackTrace(); }
		finally { client=null; }
	}

	public void sendMail(ArrayList<String> reciepentEmail,String subject,String message,boolean isMessageHTML) {
		EmailUtil client = new EmailUtil();

		try  { 
			client.sendMultiTextEmail(EmailUtil.EricssonSMTPServerIP,EmailUtil.mailFrom,reciepentEmail,subject,message,isMessageHTML); 
		} 
		catch(Exception e) {
			e.printStackTrace(); }
		finally { client=null; }
	}

	public void sendMail(ArrayList<String> reciepentEmail,String subject,String message,String fileAttachment,String fileName,boolean isMessageHTML)
	{
		EmailUtil client = new EmailUtil();
		try  { 
			System.out.println("Mail Sending...");
			client.sendMultiTextEmailWithMultiAttachment(EmailUtil.EricssonSMTPServerIP,EmailUtil.mailFrom,reciepentEmail,subject,message,fileAttachment,fileName,isMessageHTML); 
		} 
		catch(Exception e) 
		{ 
			//log.warn(e);
			e.printStackTrace(); 
		}
		finally { client=null; }
	}



	private void sendMultiTextEmail(String mailServer,String from,ArrayList<String> to,String subject, String messageBody,boolean isMessageHTML) throws MessagingException, AddressException 
	{ 
		Properties props = System.getProperties(); // Setup mail server 
		props.put("mail.smtp.host", mailServer); 
		Session session = Session.getDefaultInstance(props, null); //Get a mail session 
		Message message = new MimeMessage(session);  //Define a new mail message 
		InternetAddress[] addressTo = new InternetAddress[to.size()];
		for (int j=0;j<to.size();j++)
		{
			addressTo[j]=new InternetAddress(to.get(j).toString());
		}
		message.setFrom(new InternetAddress(from)); 
		message.setRecipients(Message.RecipientType.TO, addressTo);
		message.setSubject(subject); 
		if(isMessageHTML)
		{
			message.setDataHandler(new DataHandler(new HTMLDataSource(messageBody)));
		}
		else
		{
			BodyPart messageBodyPart = new MimeBodyPart(); // Create a message part to represent the body text 
			messageBodyPart.setText(messageBody); 
			Multipart multipart = new MimeMultipart(); //use a MimeMultipart as we need to handle the file attachments 
			multipart.addBodyPart(messageBodyPart); //add the message body to the mime message 
			message.setContent(multipart); // Put all message parts in the message

		}	
		Transport.send(message); // Send the message 
	} 

	private void sendSingleTextMail(String mailServer,String from,String to,String subject, String messageBody,boolean isMessageHTML) throws MessagingException, AddressException
	{ 
		Properties props = System.getProperties(); // Setup mail server 
		props.put("mail.smtp.host", mailServer); 
		Session session = Session.getDefaultInstance(props, null); //Get a mail session 
		Message message = new MimeMessage(session);  //Define a new mail message 
		message.setFrom(new InternetAddress(from)); 
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); 
		message.setSubject(subject);
		if(isMessageHTML)
		{
			message.setDataHandler(new DataHandler(new HTMLDataSource(messageBody)));
		}
		else
		{
			BodyPart messageBodyPart = new MimeBodyPart(); // Create a message part to represent the body text 
			messageBodyPart.setText(messageBody); 
			Multipart multipart = new MimeMultipart(); //use a MimeMultipart as we need to handle the file attachments 
			multipart.addBodyPart(messageBodyPart); //add the message body to the mime message 
			message.setContent(multipart); // Put all message parts in the message
		}	
		Transport.send(message); // Send the message 
	}

	private void sendMultiTextEmailWithMultiAttachment(String mailServer, String from, ArrayList<String> to, String subject,
			String messageBody, String fileAttachment, String fileName,
			boolean isMessageHTML) throws MessagingException, AddressException {
		try {
			Properties props = System.getProperties(); // Setup mail server
			props.put("mail.smtp.host", mailServer);
			Session session = Session.getDefaultInstance(props, null); // Get a mail session
			Message message = new MimeMessage(session);  // Define a new mail message
			InternetAddress[] addressTo = new InternetAddress[to.size()];
			for (int j = 0; j < to.size(); j++) {
				addressTo[j] = new InternetAddress(to.get(j).toString());
			}
			message.setFrom(new InternetAddress(from, "Status Report"));
			message.setRecipients(Message.RecipientType.TO, addressTo);
			message.setSubject(subject);

			BodyPart messageBodyPart = new MimeBodyPart(); // Create a message part to represent the body text
			//messageBodyPart.setText(messageBody);
			if (isMessageHTML) {
				messageBodyPart.setDataHandler(new DataHandler(new HTMLDataSource(messageBody)));
			} else {
				messageBodyPart.setText(messageBody);
			}

			Multipart multipart = new MimeMultipart(); // use a MimeMultipart as we need to handle the file
			// attachments
			multipart.addBodyPart(messageBodyPart); // add the message body to the mime message

			// Part two is attachment
			String files[] =fileName.split(",");
			for(String filename:files)
			{
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(fileAttachment+filename);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(filename);
				multipart.addBodyPart(messageBodyPart);
			}

			message.setContent(multipart); // Put all message parts in the message


			Transport.send(message); // Send the message
		} catch (Exception e) {
			log.error(e);
		}
	}

	/*private void sendMultiTextEmailWithAttachment(String mailServer,String from,ArrayList<String> to,String subject, String messageBody,String fileAttachment,String fileName,boolean isMessageHTML) throws MessagingException, AddressException 
	{ 
		Properties props = System.getProperties(); // Setup mail server 
		props.put("mail.smtp.host", mailServer); 
		Session session = Session.getDefaultInstance(props, null); //Get a mail session 
		Message message = new MimeMessage(session);  //Define a new mail message 
		InternetAddress[] addressTo = new InternetAddress[to.size()];
		for (int j=0;j<to.size();j++)
		{
			addressTo[j]=new InternetAddress(to.get(j).toString());
		}
		message.setFrom(new InternetAddress(from)); 
		message.setRecipients(Message.RecipientType.TO, addressTo);
		message.setSubject(subject); 
		if(isMessageHTML)
		{
			message.setDataHandler(new DataHandler(new HTMLDataSource(messageBody)));
		}
		else
		{
			BodyPart messageBodyPart = new MimeBodyPart(); // Create a message part to represent the body text 
			messageBodyPart.setText(messageBody); 
			Multipart multipart = new MimeMultipart(); //use a MimeMultipart as we need to handle the file attachments 
			multipart.addBodyPart(messageBodyPart); //add the message body to the mime message 

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			DataSource source = 
					new FileDataSource(fileAttachment);
			messageBodyPart.setDataHandler(
					new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);



			message.setContent(multipart); // Put all message parts in the message

		}	
		Transport.send(message); // Send the message 
	}*/

	static class HTMLDataSource implements DataSource 
	{
		private String html;
		public HTMLDataSource(String htmlString) 
		{			html = htmlString;		}
		public InputStream getInputStream() throws IOException 
		{
			if (html == null) throw new IOException("Null HTML");
			return new ByteArrayInputStream(html.getBytes());
		}

		public OutputStream getOutputStream() throws IOException 
		{			throw new IOException("This DataHandler cannot write HTML");		}
		public String getContentType() 
		{			return "text/html";		}
		public String getName() 
		{		return "JAF text/html dataSource to send e-mail only";	}
	}

	/**
	 * Utility method to send simple HTML email
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendEmail(Session session, String toEmail, String subject, String body){
		try
		{
			MimeMessage msg = new MimeMessage(session);
			//set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("no_reply@ericsson.com", "NoReply-Viewnet"));

			msg.setReplyTo(InternetAddress.parse("no_reply@ericsson.com", false));

			msg.setSubject(subject, "UTF-8");

			msg.setText(body, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			System.out.println("Message is ready");
			Transport.send(msg);  

			System.out.println("EMail Sent Successfully!!");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}