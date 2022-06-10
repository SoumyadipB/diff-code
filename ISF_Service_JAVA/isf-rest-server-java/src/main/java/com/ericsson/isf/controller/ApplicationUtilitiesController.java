/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.model.EmailModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.SharePointModel;
import com.ericsson.isf.model.botstore.BotDetail;
import com.ericsson.isf.service.OutlookAndEmailService;
import com.ericsson.isf.util.ConfigurationFilesConstants;


/**
 *
 * @author edhhklu
 */

@RestController
@RequestMapping("/appUtil")
public class ApplicationUtilitiesController {
	
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationUtilitiesController.class);
    
    @Autowired 
    private OutlookAndEmailService emailService;
    
	@Autowired
    private static ApplicationConfigurations configurations;
    
    private static final String NOTIFICATION_NAME_FIELD="emailTemplateName";     
    
    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    public boolean updateAccessProfileStatus(@RequestBody Map<String,Object> data) {
    	LOG.info("Mail API called");
    	String notificationName = data.get(NOTIFICATION_NAME_FIELD).toString();
    	emailService.sendMail(notificationName,emailService.enrichMailforCM( data,notificationName));
        return true;
    }
    
    
	public static Connection getConnection() throws ClassNotFoundException, SQLException, IOException {

		// get the property value
		String databaseDriver = configurations.getStringProperty(ConfigurationFilesConstants.JDBC_DATASOURCECLASSNAME);
		String databaseUsername = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNUSRNM);
		String databasePassword = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNPWD);
		String databaseIp = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNIP);
		String databaseName = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME);
		String databaseUrl = "jdbc:sqlserver://" + databaseIp + ":1433;databaseName=" + databaseName;
		Class.forName(databaseDriver);
		Connection connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
		return connection;
	}
    
    
    @RequestMapping(value = "/botDownload", method = RequestMethod.POST)
    public BotDetail botDownload(@RequestBody BotDetail botDetail) {
    	

    	BotDetail file= emailService.botDownload(botDetail);
        LOG.info("botDownload Request: SUCCESS");
        
        return file;
    }
    
    @RequestMapping(value = "/sendGridMailUtility", method = RequestMethod.POST)
    public void sendGridMailUtility(@RequestBody EmailModel emailDetails) throws IOException {
    	emailService.sendGridMailUtility(emailDetails, true);   	
    }

    @RequestMapping(value = "/botUpload", method = RequestMethod.POST)
    public void botUpload(@RequestBody BotDetail botDetail) throws Exception {
    	

    	emailService.botUpload(botDetail);
        LOG.info("bot Uploaded Request: SUCCESS");
    }
    
    public static class FileModel {
    	String name;
    	String contents;
    	
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getContents() {
			return contents;
		}
		public void setContents(String contents) {
			this.contents = contents;
		}
    }
    @RequestMapping(value = "/creatFileFromContents", method = RequestMethod.POST)
    public ResponseEntity<Resource> creatFileFromContents(@RequestBody FileModel fileModel) {
    		byte[] contents = fileModel.getContents().getBytes();
    		InputStream inputStream = new ByteArrayInputStream(contents);
    	    
        	InputStreamResource resource = new InputStreamResource(inputStream);
        	        	
        	return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileModel.getName() + "\"")
                    .contentLength(contents.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
    }
    
    @RequestMapping(value = "/sendSmtpMail", method = RequestMethod.POST)
	public void sendSmtpMail(@RequestBody EmailModel emailModel) throws MessagingException, IOException, InterruptedException{		
	 emailService.sendSmtpMail(emailModel,true);	
	}
	
    /**
     * 
     * 
     * @return com.ericsson.isf.model.Response<List<EmailModel>>
     * @throws MessagingException
     * @throws IOException
     * @throws InterruptedException
     */
	@RequestMapping(value = "/getListOfUnsentMails", method = RequestMethod.GET)
	 public com.ericsson.isf.model.Response<List<EmailModel>> getEmailModeletListOfUnsentMails() throws MessagingException, IOException, InterruptedException{
		 
		 return emailService.getListOfUnsentMails();
	}
	
	@RequestMapping(value = "/uploadSharePointDetails", method = RequestMethod.POST)
    public Response<Void> uploadSharePointDetails (@RequestBody SharePointModel sharePointModel) throws IOException {
    	return emailService.uploadSharePointDetails(sharePointModel); 	
    }
	
	/**
	 * API Name: appUtil/validateSharePointAccess
	 * 
	 * @author emntiuk
	 * @Purpose: This API is used to check if we able to make Access on SharePoint
	 *           using create and delete a file on given baseURL or not.
	 * @param sharePointModel
	 * @return Response<Void>
	 */
	@PostMapping(value = "/validateSharePointAccess")
	public Response<Void> validateSharePointAccess(@RequestBody SharePointModel sharePointModel) {
		return emailService.validateSharePointAccess(sharePointModel);
	}
	
	/**
	 * 
	 * @param sharePointModel
	 * @return
	 */
	@PostMapping(value = "/saveSharePointAccessDetail")
	public ResponseEntity<Response<Void>> saveSharePointAccessDetail(@RequestBody SharePointModel sharePointModel) {
		return emailService.saveSharePointAccessDetail(sharePointModel);
	}
	
	
	/**
	 * API Name: appUtil/sendEmail
	 * 
	 * @author elkpain
	 * @Purpose: This API is used to send email.
	 * @param EmailModel
	 * @return Response<Void>
	 */
	@PostMapping(value = "/sendEmail")
	public ResponseEntity<Response<Void>> sendEmail(@RequestBody EmailModel emailModel) throws MessagingException, IOException, InterruptedException{
		
		 LOG.info("sendEmail: SUCCESS");
	     return emailService.sendEmail(emailModel);	
	}
	
	
}