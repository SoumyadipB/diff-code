/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.activation.URLDataSource;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.AppUtilDAO;
import com.ericsson.isf.dao.CRManagementDAO;
import com.ericsson.isf.dao.ProjectDAO;
import com.ericsson.isf.dao.ResourceRequestDAO;
import com.ericsson.isf.dao.WOExecutionDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.AllocatedResourceModel;
import com.ericsson.isf.model.CRManagementModel;
import com.ericsson.isf.model.CRManagementResultModel;
import com.ericsson.isf.model.DeliveryResponsibleModel;
import com.ericsson.isf.model.EmailModel;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.EnvironmentPropertyModel;
import com.ericsson.isf.model.FeedbackStatusUpdateModel;
import com.ericsson.isf.model.MailModel;
import com.ericsson.isf.model.ProjectFilterModel;
import com.ericsson.isf.model.ProjectsModel;
import com.ericsson.isf.model.RaiseCRMannagmentModel;
import com.ericsson.isf.model.ResigReqModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.SearchResourceByFilterModel;
import com.ericsson.isf.model.SharePointModel;
import com.ericsson.isf.model.Signum;
import com.ericsson.isf.model.UserWorkFlowProficencyModel;
import com.ericsson.isf.model.WorkEffortModel;
import com.ericsson.isf.model.WorkFlowFeedbackModel;
import com.ericsson.isf.model.WorkflowProficiencyModel;
import com.ericsson.isf.model.botstore.BotDetail;
import com.ericsson.isf.model.botstore.TblAdhocBooking;
import com.ericsson.isf.model.botstore.TblAdhocBookingActivity;
import com.ericsson.isf.model.botstore.TblProjects;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AppUtil;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.ericsson.isf.util.IsfSharepointFileUpload;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.autodiscover.exception.AutodiscoverLocalException;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.item.MeetingMessage;
import microsoft.exchange.webservices.data.core.service.item.MeetingRequest;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.Attachment;
import microsoft.exchange.webservices.data.property.complex.AttachmentCollection;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.FileAttachment;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.Mailbox;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;

/**
 *
 * @author edhhklu
 */
@Service
public class OutlookAndEmailService {

	private static final String VALUE_ADDED_SUCCESS_FULLY = "Value Added SuccessFully!!";

	private static final String VALUE_UPDATED_SUCCESS_FULLY = "Value Updated SuccessFully!!";

	private static final String SITE_NAME_IS_ALREADY_EXIST_FOR_OTHER_MARKET_AREA_PLEASE_PROVIDE_DIFFERENT_SITE_NAME = "SiteName already Exist. Please provide different SiteName!!";

	private static final String ACCESS_GRANTED = "Access Granted!!";

	private static final String UPLOADED_BY = "uploadedBy";

	private static final String UPLOADED_ON = "uploadedOn";

	private static final String SUBJECT = "subject";
	
	private static final String CUSTOMER_NAME = "customer";

	private static final String COUNTRY_NAME = "country";
	
	private static final String EMAIL_SENT_SUCCESSFULLY = "Email sent successfully!!";
	
	

	@Autowired
	private AdhocActivityService adhocActivityService;

	@Autowired
	private AppUtilDAO appUtilDAO;

	@Autowired
	private ResourceRequestDAO resourceRequestDAO;

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private ActivityMasterDAO activityMasterDAO;

	@Autowired
	private CRManagementDAO crManagementDAO;

	@Autowired
	private AccessManagementService accessManagementService;

	@Autowired
	ProjectService projectService;

	@Autowired
	RpaService rpaService;

	@Autowired /* Bind to bean/pojo */
	private EnvironmentPropertyService environmentPropertyService;

	@Autowired
	private ApplicationConfigurations configurations;

	@Autowired
	private IsfSharepointFileUpload sharePointFileUpload;
	
	@Autowired
	private WOExecutionDAO wOExecutionDAO;

	private String pass;

	private static final String ISF_HOME_LINK_KEY = "ISF_HOME_LINK";

	private static final Logger LOGGER = LoggerFactory.getLogger(OutlookAndEmailService.class);

	private static final String EMAIL_DATE_FORMAT = "dd-MM-yyyy";
	private static final String EMAIL_DATETIME_FORMAT = "dd-MM-yyyy HH:mm";
	private static final String VELOCITY_TEMPLATE_DATE_FORMAT_REFERENCE = "date";
	private static final String VELOCITY_TEMPLATE_DATETIME_FORMAT_REFERENCE = "dateTime";
	private static final String VELOCITY_TEMPLATE_DATA_BASE_REFERENCE = "v";
	private static final String VALOCITY_TEMPLATES_FOLDER = "templates/";
	private static final String VALOCITY_TEMPLATE_EXTENSION = ".vm";
	private static final String USER_INFO = "User Information";
	private static final String CU = "CU";
	private static final String NETWORK_ELEMENT_UPLOADED = "Network Elements Uploaded Successfully";
	private static final String SUCCESS_COUNT = "successCount";

	private EmailModel getMailDetails(String templateId, Map<String, Object> placeholders) throws ParseException {

		if (!placeholders.containsKey(AppConstants.NOTIFICATION_URL_PATH)) {
			placeholders.put(AppConstants.NOTIFICATION_URL_PATH, "");
		}
		placeholders.put(ISF_HOME_LINK_KEY,
				configurations.getStringProperty(ConfigurationFilesConstants.DEPLOYED_ENVIRONMENT));
		VelocityEngine velocityEngine = new VelocityEngine();

		velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
				"org.apache.velocity.runtime.log.Log4JLogChute");
		velocityEngine.setProperty("runtime.log.logsystem.log4j.logger", "velocity");

		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG, "/var/tmp/logs/velocity.log");
		velocityEngine.init();

		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put(VELOCITY_TEMPLATE_DATA_BASE_REFERENCE, placeholders);
		velocityContext.put(VELOCITY_TEMPLATE_DATE_FORMAT_REFERENCE, new SimpleDateFormat(EMAIL_DATE_FORMAT));
		velocityContext.put(VELOCITY_TEMPLATE_DATETIME_FORMAT_REFERENCE, new SimpleDateFormat(EMAIL_DATETIME_FORMAT));

		EmailModel emailDetails = appUtilDAO.getNotificationsDetailsByTemplateName(templateId);

		RuntimeServices velocityRuntimeService = RuntimeSingleton.getRuntimeServices();
		Template template = null;
		if (emailDetails == null) {
			emailDetails = new EmailModel();
			template = velocityEngine.getTemplate(VALOCITY_TEMPLATES_FOLDER + templateId + VALOCITY_TEMPLATE_EXTENSION);
		} else {

			if (StringUtils.isBlank(emailDetails.getSubject()) && placeholders.containsKey(SUBJECT)) {
				emailDetails.setSubject(String.valueOf(placeholders.get(SUBJECT)));
			}
			StringReader templateText = new StringReader(emailDetails.getTemplateText());

			SimpleNode sn = velocityRuntimeService.parse(templateText, USER_INFO);

			template = new Template();
			template.setData(sn);
			template.setRuntimeServices(velocityRuntimeService);
		}
		template.initDocument();
		StringWriter emailBodyText = new StringWriter();
		template.merge(velocityContext, emailBodyText);
		emailDetails.setBody(emailBodyText.toString());
		return emailDetails;
	}

	@Transactional(value = "transactionManager", propagation = Propagation.REQUIRES_NEW)
	public boolean sendMail(String templateId, Map<String, Object> placeholders) {
		try {
			EmailModel emailDetails = getMailDetails(templateId, placeholders);
			return sendMail(emailDetails, placeholders);
		} catch (Exception e) {
			LOGGER.error("sendMail", e);
		}
		return false;
	}

	public boolean sendMail(String templateId, String subject, Map<String, Object> placeholders) {
		try {
			EmailModel emailDetails = getMailDetails(templateId, placeholders);
			emailDetails.setSubject(subject);
			return sendMail(emailDetails, placeholders);
		} catch (Exception e) {
			LOGGER.error("sendMail", e);
		}
		return false;
	}

	private static final String MAIL_RECIEVER_PM = "PM";
	private static final String MAIL_RECIEVER_FM = "FM";
	private static final String MAIL_RECIEVER_DR = "DR";
	private static final String MAIL_RECIEVER_RPM = "RPM";
	private static final String MAIL_SEPARATOR = ";";
	private static final String MAIL_SENDER_ID = "isfadmin@ericsson.com";
	private static final String DEPLOYED_ENV_KEY = "BaseUrl";

	private boolean sendMail(EmailModel emailDetails, Map<String, Object> placeholders) {
		try {
			EmployeeModel currentUser = null;
			SearchResourceByFilterModel req = new SearchResourceByFilterModel();
			currentUser = getCurrentUser(placeholders, currentUser, req);

			String ccAddress = "";
			String toAddress = "";
			if (emailDetails.getTo() != null) {
				String[] usergroups = emailDetails.getTo().split(",");

				for (String ug : usergroups) {
					if (CU.equalsIgnoreCase(ug)) {
						if (currentUser != null) {
							toAddress += currentUser.getEmployeeEmailId() + MAIL_SEPARATOR;
						}
					} else if (MAIL_RECIEVER_PM.equalsIgnoreCase(ug)) {
						String projectIds = placeholders.get(AppConstants.PROJECT_ID).toString();
						String[] projectIdList = projectIds.split(",");
						for (String projectId : projectIdList) {
							String projectManager = projectDAO.getManagerByProjectId(projectId);
							if (projectManager != null) {
								EmployeeModel result = activityMasterDAO.getEmployeeBySignum(projectManager);
								toAddress += result.getEmployeeEmailId() + MAIL_SEPARATOR;
							}
						}
					} else if (MAIL_RECIEVER_FM.equalsIgnoreCase(ug)) {
						String projectIds = placeholders.get(AppConstants.PROJECT_ID).toString();
						String[] projectIdList = projectIds.split(",");
						for (String projectId : projectIdList) {
							List<String> projectSpocs = projectDAO.getSpocByProjectId(projectId);
							for (String s : projectSpocs) {
								EmployeeModel result = activityMasterDAO.getEmployeeBySignum(s);
								if (result != null) {
									toAddress += result.getEmployeeEmailId() + MAIL_SEPARATOR;
								}
							}
						}
					} else if (MAIL_RECIEVER_DR.equalsIgnoreCase(ug)) {
						String projectIds = placeholders.get(AppConstants.PROJECT_ID).toString();
						String[] projectIdList = projectIds.split(",");
						for (String projectId : projectIdList) {
							List<DeliveryResponsibleModel> drList = projectDAO
									.getDeliveryResponsibleByProject(Integer.parseInt(projectId));
							for (DeliveryResponsibleModel dr : drList) {
								EmployeeModel result = activityMasterDAO.getEmployeeBySignum(dr.getSignumID());
								if (result != null) {
									toAddress += result.getEmployeeEmailId() + MAIL_SEPARATOR;
								}
							}
						}
					} else if (MAIL_RECIEVER_RPM.equalsIgnoreCase(ug)) {
						String projectIds = placeholders.get(AppConstants.PROJECT_ID).toString();
						String[] projectIdList = projectIds.split(",");
						for (String projectId : projectIdList) {
							List<String> rmp = projectDAO.getRPMByProjectId(projectId);
							for (String str : rmp) {
								EmployeeModel result = activityMasterDAO.getEmployeeBySignum(str);
								if (result != null) {
									toAddress += result.getEmployeeEmailId() + MAIL_SEPARATOR;
								}
							}
						}
					} else {
						// else it is group or specific email id
						emailDetails.setReceiverSignum(ug + MAIL_SEPARATOR);
					}
				}
			}
			if (emailDetails.getCc() != null) {
				String[] usergroups = emailDetails.getCc().split(",");

				for (String ug : usergroups) {
					if (CU.equalsIgnoreCase(ug)) {
						if (currentUser != null) {
							ccAddress += currentUser.getEmployeeEmailId() + MAIL_SEPARATOR;
						}

					} else if (MAIL_RECIEVER_PM.equalsIgnoreCase(ug)) {
						String projectIds = placeholders.get(AppConstants.PROJECT_ID).toString();
						String[] projectIdList = projectIds.split(",");
						for (String projectId : projectIdList) {
							String projectManager = projectDAO.getManagerByProjectId(projectId);
							if (projectManager != null) {
								EmployeeModel result = activityMasterDAO.getEmployeeBySignum(projectManager);
								ccAddress += result.getEmployeeEmailId() + MAIL_SEPARATOR;
							}
						}
					} else if (MAIL_RECIEVER_FM.equalsIgnoreCase(ug)) {
						String projectIds = placeholders.get(AppConstants.PROJECT_ID).toString();
						String[] projectIdList = projectIds.split(",");
						for (String projectId : projectIdList) {
							List<String> projectSpocs = projectDAO.getSpocByProjectId(projectId);
							for (String s : projectSpocs) {
								EmployeeModel result = activityMasterDAO.getEmployeeBySignum(s);
								if (result != null) {
									ccAddress += result.getEmployeeEmailId() + MAIL_SEPARATOR;
								}
							}
						}
					} else if (MAIL_RECIEVER_DR.equalsIgnoreCase(ug)) {

						String projectIds = placeholders.get(AppConstants.PROJECT_ID).toString();
						String[] projectIdList = projectIds.split(",");
						for (String projectId : projectIdList) {
							List<DeliveryResponsibleModel> drList = projectDAO
									.getDeliveryResponsibleByProject(Integer.parseInt(projectId));
							for (DeliveryResponsibleModel dr : drList) {
								EmployeeModel result = activityMasterDAO.getEmployeeBySignum(dr.getSignumID());
								if (result != null) {
									ccAddress += result.getEmployeeEmailId() + MAIL_SEPARATOR;
								}
							}
						}
					} else if (MAIL_RECIEVER_RPM.equalsIgnoreCase(ug)) {
						String projectIds = placeholders.get(AppConstants.PROJECT_ID).toString();
						String[] projectIdList = projectIds.split(",");
						for (String projectId : projectIdList) {
							List<String> rmp = projectDAO.getRPMByProjectId(projectId);
							for (String str : rmp) {
								EmployeeModel result = activityMasterDAO.getEmployeeBySignum(str);
								if (result != null) {
									toAddress += result.getEmployeeEmailId() + MAIL_SEPARATOR;
								}
							}
						}
					} else {
						// else it is group or specific email id
						emailDetails.setReceiverSignum(ug + MAIL_SEPARATOR);
					}
				}
			}

			if (placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_CC) != null) {
				ccAddress += placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_CC);
			}
			if (placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_TO) != null) {
				toAddress += placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_TO);
			}
			emailDetails.setTo(toAddress);
			emailDetails.setCc(ccAddress);
			emailDetails.setSenderSignum(MAIL_SENDER_ID);
			List<EnvironmentPropertyModel> environmentPropertyModel = environmentPropertyService
					.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY);
			if ("CLOUD".equalsIgnoreCase(environmentPropertyModel.get(0).getDeployedEnv())) {
				LOGGER.info("Checking sendGridMailUtility block");
				sendGridMailUtility(emailDetails, true);
				LOGGER.info("Checking sendGridMailUtility block pass");
			} else {
				LOGGER.info("Checking sendSmtpMail block");
				sendSmtpMail(emailDetails, true);
				LOGGER.info("Checking sendSmtpMail block pass");
			}

		} catch (Exception e) {
			LOGGER.warn("Error while sending email", e);
		}
		return false;
	}

	public void sendSmtpMail(EmailModel emailDetails, boolean update) {
		Runnable r = () -> {
			try {
				if (configurations.getBooleanProperty(ConfigurationFilesConstants.SEND_KAFKA_MAIL)) {

					if (StringUtils.isBlank(emailDetails.getStatus())) {
						emailDetails.setStatus("SENT");
					}
					sendKafkaMessageQueue(emailDetails);
					if (update) {
						appUtilDAO.sendSmtpMail(emailDetails);
					}
				} else {
					sendSmtpMailThread(emailDetails, update);
				}

			} catch (MessagingException | InterruptedException e) {
				LOGGER.info(AppConstants.ERROR_IN_SENDSMTPMAIL, e.getMessage());
				Thread.currentThread().interrupt();
			} catch (IOException e) {
				LOGGER.info(AppConstants.ERROR_IN_SENDSMTPMAIL, e.getMessage());
			}
		};
		new Thread(r).start();

	}

	public void sendGridMailUtility(EmailModel emailDetails, boolean update) {
		Runnable r = () -> {
			try {
				sendGridMailUtilityThread(emailDetails, update);

			} catch (IOException e) {
				LOGGER.info(AppConstants.EXCEPTION, e.getMessage());
			}
			LOGGER.info("===========================================================");
		};
		new Thread(r).start();
	}

	private void sendKafkaMessageQueue(EmailModel emailDetails) throws IOException {

		RestTemplate restTemplate = new RestTemplate();
		String url = configurations.getStringProperty(ConfigurationFilesConstants.KAFKA_APPLICATION_URL);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<EmailModel> request = new HttpEntity<>(emailDetails, headers);
		restTemplate.exchange(url, HttpMethod.POST, request, String.class);
	}

	public void sendSmtpMailThread(EmailModel emailDetails, boolean update)
			throws MessagingException, IOException, InterruptedException {

		Properties properties = System.getProperties();
		properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_URL,
				configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_URL));
		properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_USER,
				configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_USER));
		properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_PASSWORD,
				configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_PASSWORD));
		properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_STARTTLS,
				configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_STARTTLS));
		properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_PORT,
				configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_PORT));
		// enable authentication
		properties.put(ConfigurationFilesConstants.SMTP_MAIL_AUTH,
				configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_AUTH));

		properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

		// SSL Factory
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(
						configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_USER),
						configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_PASSWORD));
			}
		});

		MimeMessage message = new MimeMessage(session);

		// from
		if (StringUtils.isNotBlank(emailDetails.getSenderSignum())) {
			message.setFrom(new InternetAddress(emailDetails.getSenderSignum().replaceAll("\\s", "")));
		} else {
			emailDetails.setSenderSignum(MAIL_SENDER_ID);
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
		
		      // BCC
				if (StringUtils.isNotBlank(emailDetails.getBcc())) {
					String[] brecipientList1 = emailDetails.getBcc().split(";");
					List<String> brecipientList2 = new ArrayList<String>(Arrays.asList(brecipientList1));
					brecipientList2.removeAll(Arrays.asList("", null));
					String[] brecipientList = brecipientList2.toArray(new String[brecipientList2.size()]);

					InternetAddress[] brecipientAddressCopy = new InternetAddress[brecipientList.length];
					int counter = 0;
					for (String brecipientcopy : brecipientList) {
						brecipientAddressCopy[counter] = new InternetAddress(brecipientcopy.trim());
						counter++;
					}
					message.setRecipients(Message.RecipientType.BCC, (brecipientAddressCopy));
				}

		message.setContent(emailDetails.getBody(), "text/html");
		// to , recipients
		if (StringUtils.isNotBlank(emailDetails.getTo())) {
			String[] toList1 = emailDetails.getTo().split(";");
			List<String> toList2 = new ArrayList<>(Arrays.asList(toList1));
			toList2.removeAll(Arrays.asList("", null));

			String[] toList = toList2.toArray(new String[toList2.size()]);

			InternetAddress[] recipientAddress = new InternetAddress[toList.length];
			int counter = 0;
			for (String recipient : toList) {
				if (StringUtils.isNotBlank(recipient)) {
					recipientAddress[counter] = new InternetAddress(recipient.trim());
					counter++;
				}

			}

			message.setRecipients(Message.RecipientType.TO, (recipientAddress));
			if (emailDetails.getSubject() != null) {
				message.setSubject(emailDetails.getSubject());
			}

			Transport.send(message);
			LOGGER.info("Mail Sent !!!");

		} else {
			LOGGER.info("Mail Not Sent !!!");
		}
	}

	public void sendUnsentSmtpMail(EmailModel emailDetails)
			throws MessagingException, IOException, InterruptedException {
		try {
			sendSmtpMail(emailDetails, false);
			appUtilDAO.sendUnsentSmtpMail(emailDetails);
		} catch (Exception e) {
			LOGGER.error("EXCEPTION IN OutlookAndEmailService.sendUnsentSmtpMail" + e.getMessage());
		}
	}

	/**
	 * 
	 * @param emailModel
	 * @return com.ericsson.isf.model.Response<List<EmailModel>>
	 */
	public com.ericsson.isf.model.Response<List<EmailModel>> getListOfUnsentMails() {

		com.ericsson.isf.model.Response<List<EmailModel>> apiResponse = new com.ericsson.isf.model.Response<List<EmailModel>>();
		try {

			List<EmailModel> unsentMails = appUtilDAO.getListOfUnsentMails();
			List<EmailModel> sentMails = new ArrayList<>();
			for (EmailModel emModel : unsentMails) {
				try {

					sendUnsentSmtpMail(emModel);
					sentMails.add(emModel);
				} catch (Exception e) {
					apiResponse.addFormError("Unable to send mail for email id " + emModel.getId()
							+ " \n Getting below error: " + e.getMessage());
					continue;
				}
			}
			apiResponse.setResponseData(sentMails);
		} catch (ApplicationException e) {
			apiResponse.addFormError(e.getMessage());
		} catch (Exception e) {
			apiResponse.addFormError(e.getMessage());
			LOGGER.error(e.getMessage());

		}

		return apiResponse;
	}

	@Transactional(value = "transactionManager", propagation = Propagation.REQUIRES_NEW)
	public void sendGridMailUtilityThread(EmailModel emailDetails, boolean update) throws IOException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Email from = new Email();
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
		// body
		content.setType("text/html");
		content.setValue(emailDetails.getBody());
		mail.addPersonalization(personalization);
		mail.addContent(content);

		SendGrid sg = new SendGrid(configurations.getStringProperty(ConfigurationFilesConstants.MAIL_SENDGRID_KEY));
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			sg.api(request);
			if (update) {
				appUtilDAO.sendSmtpMail(emailDetails);
			}
		} catch (IOException ex) {
			throw new IOException(ex.getMessage());
		}

	}

	public static final String RESIGN = "RESIGN";
	private static final String DATA_CM = "data";

	public Map<String, Object> enrichMailforCM(Object model, String notificationName) {
		Map<String, Object> data = new HashMap<>();
		String customNotificationCC = "";
		String customNotificationTO = "";

		if (!notificationName.equals(RESIGN)) {
			data.put(AppConstants.NOTIFICATION_URL_PATH, this.accessManagementService.getPathByName("Demand Forecast"));
		}
		try {

			data.put(DATA_CM, model);

			if (AppConstants.NOTIFICATION_TYPE_PROPOSE.equalsIgnoreCase(notificationName)
					|| AppConstants.NOTIFICATION_TYPE_ACCPET_PROPOSE.equalsIgnoreCase(notificationName)
					|| AppConstants.NOTIFICATION_TYPE_REJECT_PROPOSE.equalsIgnoreCase(notificationName)) {

				ProjectFilterModel projectFilterModel = new ProjectFilterModel();
				HashMap<String, List<ProjectsModel>> projectDetails = new HashMap<String, List<ProjectsModel>>();
				List<ProjectsModel> projList = new ArrayList<>();

				List<AllocatedResourceModel> dataModel = (List<AllocatedResourceModel>) model;
				EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(dataModel.get(0).getLoggedInUser());
				String currentUser = eDetails.getSignum();
				Integer projectId = getProjectIdOfWeorkEffort(dataModel.get(0).getWeid() + "");
				for (AllocatedResourceModel a : dataModel) {
					WorkEffortModel weDetails = crManagementDAO.getWorkEffortDetailsByID(a.getWeid());
					if (AppConstants.NOTIFICATION_TYPE_REJECT_PROPOSE.equalsIgnoreCase(notificationName)) {
						// add signum as signum is removed from the database
						weDetails.setSignum(a.getSignum());
					} else if (AppConstants.NOTIFICATION_TYPE_PROPOSE.equalsIgnoreCase(notificationName)) {

						if (a.getSignum() != null) {
							EmployeeModel proposedResourceDetils = activityMasterDAO.getEmployeeBySignum(a.getSignum());
							EmployeeModel ManagerDetails = activityMasterDAO
									.getEmployeeBySignum(proposedResourceDetils.getManagerSignum());
							if (ManagerDetails != null) {
								customNotificationCC += ManagerDetails.getEmployeeEmailId() + ";";
							}
						}
					} else if (AppConstants.NOTIFICATION_TYPE_ACCPET_PROPOSE.equalsIgnoreCase(notificationName)) {

						projectId = activityMasterDAO.getProjectIdByRpID(a.getRpID());

						if (!projectDetails.containsKey(String.valueOf(projectId))) {

							projectFilterModel.setProjectID(projectId);
							projList = projectService.getProjectByFilters(projectFilterModel);
							projectDetails.put(String.valueOf(projectId), projList);
						}

						data.put("projectName", projList.get(0).getProjectName());
						data.put("SPM", projList.get(0).getProjectCreator());
						data.put("marketArea", projList.get(0).getMarketAreaName());

						eDetails = activityMasterDAO.getEmployeeBySignum(weDetails.getSignum());
						EmployeeModel ManagerDetails = activityMasterDAO
								.getEmployeeBySignum(eDetails.getManagerSignum());
						if (ManagerDetails != null) {
							customNotificationCC += ManagerDetails.getEmployeeEmailId() + ";";
						}
					}

					eDetails = activityMasterDAO.getEmployeeBySignum(weDetails.getSignum());
					a.setResourceName(eDetails.getSignum() + " (" + eDetails.getEmployeeName() + ")");
					a.setWeDetails(weDetails);

				}
				data.put(AppConstants.CURRENT_USER, currentUser);

				data.put(AppConstants.PROJECT_ID, projectId);
			} else if (AppConstants.NOTIFICATION_TYPE_RAISE_CR.equalsIgnoreCase(notificationName)
					|| AppConstants.NOTIFICATION_TYPE_RAISE_CR_PM.equalsIgnoreCase(notificationName)) {

				data.put(AppConstants.NOTIFICATION_URL_PATH + 1,
						this.accessManagementService.getPathByName("Manage Project"));
				data.put(AppConstants.NOTIFICATION_URL_PATH + 2, "");

				List<RaiseCRMannagmentModel> dataModel = (List<RaiseCRMannagmentModel>) model;
				List<WorkEffortModel> workEffortModel = null;
				EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(dataModel.get(0).getLoggedInSignum());
				String currentUser = eDetails.getSignum();
				String drSignum = "";
				String fmSignum = "";
				String allocatedSignum = "";
				String spmSignum = "";
				Integer projectId = crManagementDAO.getProjectIdbyRpId(dataModel.get(0).getRpID() + "");
				ProjectFilterModel projectFilterModel = new ProjectFilterModel();
				projectFilterModel.setProjectID(projectId);
				projectFilterModel.setSignum(dataModel.get(0).getSignum());

				List<ProjectsModel> projects = this.projectService.getProjectByFilters(projectFilterModel);
				if (projects != null && projects.size() > 0) {
					spmSignum = projects.get(0).getProjectCreator();
					if (spmSignum != null && spmSignum.trim().length() > 0) {
						eDetails = activityMasterDAO.getEmployeeBySignum(spmSignum);
						customNotificationCC += eDetails.getEmployeeEmailId() + ";";
					}
				}

				for (RaiseCRMannagmentModel a : dataModel) {
					drSignum = a.getSignum();
					WorkEffortModel weDetails = new WorkEffortModel();
					if (drSignum != null && drSignum.trim().length() > 0) {
						eDetails = activityMasterDAO.getEmployeeBySignum(drSignum);
						customNotificationCC += eDetails.getEmployeeEmailId() + ";";
					}
					if (a.getWeid() != null) {
						weDetails = crManagementDAO.getWorkEffortDetailsByID(a.getWeid());
						eDetails = activityMasterDAO.getEmployeeBySignum(a.getSignum());
						a.setSignum(eDetails.getSignum() + " (" + eDetails.getEmployeeName() + ")");

						eDetails = activityMasterDAO.getEmployeeBySignum(weDetails.getSignum());
						weDetails.setSignum(eDetails.getSignum() + " (" + eDetails.getEmployeeName() + ")");
					} else {
						List<WorkEffortModel> weLists = crManagementDAO.getWorkEffortsByRpId(a.getRpID());

						Date maxEndDate = null;
						Date minStartDate = null;

						for (WorkEffortModel we : weLists) {

							if (maxEndDate == null || maxEndDate.before(we.getEndDate())) {
								maxEndDate = we.getEndDate();
							}
							if (minStartDate == null || minStartDate.after(we.getStartDate())) {
								minStartDate = we.getStartDate();
							}

							eDetails = activityMasterDAO.getEmployeeBySignum(we.getSignum());
							a.setSignum(eDetails.getSignum() + " (" + eDetails.getEmployeeName() + ")");
						}
						weDetails.setStartDate(minStartDate);
						weDetails.setEndDate(maxEndDate);
					}
					a.setWeDetails(weDetails);
					a.setStartDate(a.getStartDate().substring(0, a.getStartDate().indexOf(' ')));
					a.setEndDate(a.getEndDate().substring(0, a.getEndDate().indexOf(' ')));

					if (dataModel.get(0).getRpID() != 0) {
						if (crManagementDAO.getWorkEffortsByRpId(dataModel.get(0).getRpID()) != null) {
							workEffortModel = crManagementDAO.getWorkEffortsByRpId(dataModel.get(0).getRpID());
							fmSignum = workEffortModel.get(0).getAllocatedBy();
							allocatedSignum = a.getAllocatedSignum();

							if (fmSignum != null && fmSignum.trim().length() > 0) {
								eDetails = activityMasterDAO.getEmployeeBySignum(fmSignum);
								customNotificationCC += eDetails.getEmployeeEmailId() + ";";
							}
							if (allocatedSignum != null && allocatedSignum.trim().length() > 0) {
								eDetails = activityMasterDAO.getEmployeeBySignum(allocatedSignum);
								customNotificationCC += eDetails.getEmployeeEmailId() + ";";
								a.setAllocatedSignum(eDetails.getSignum() + " (" + eDetails.getEmployeeName() + ")");
							}
						}
					}
				}
				data.put(AppConstants.CURRENT_USER, currentUser);
				data.put(AppConstants.PROJECT_ID, projectId);
			} else if (AppConstants.NOTIFICATION_TYPE_ACCEPT_CR.equalsIgnoreCase(notificationName)
					|| AppConstants.NOTIFICATION_TYPE_REJECT_CR.equalsIgnoreCase(notificationName)) {

				List<CRManagementModel> dataModel = (List<CRManagementModel>) model;
				EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(dataModel.get(0).getCurrentSignumID());
				EmployeeModel proposedeDetails;
				String currentUser = eDetails.getSignum();
				CRManagementResultModel crDetails = crManagementDAO.getCRDetailsByCRID(dataModel.get(0).getcRID());
				Integer projectId = getProjectIdOfWeorkEffort(crDetails.getWorkEffortIdProposed() + "");
				for (CRManagementModel a : dataModel) {
					crDetails = crManagementDAO.getCRDetailsByCRID(a.getcRID());
					WorkEffortModel existingWeDetails = crManagementDAO
							.getWorkEffortDetailsByID(crDetails.getWorkEffortIdExisting());
					WorkEffortModel proposedWeDetails = crManagementDAO
							.getWorkEffortDetailsByID(crDetails.getWorkEffortIdProposed());

					eDetails = activityMasterDAO.getEmployeeBySignum(existingWeDetails.getSignum());
					existingWeDetails.setSignum(eDetails.getSignum() + " (" + eDetails.getEmployeeName() + ")");

					proposedeDetails = activityMasterDAO.getEmployeeBySignum(proposedWeDetails.getSignum());
					proposedeDetails
							.setSignum(proposedeDetails.getSignum() + " (" + proposedeDetails.getEmployeeName() + ")");
					a.setExistingWeDetails(existingWeDetails);
					a.setProposedWeDetails(proposedWeDetails);
				}
				data.put(AppConstants.CR_ACTION_TYPE, crDetails.getActionType());
				data.put(AppConstants.CURRENT_USER, currentUser);
				data.put(AppConstants.PROJECT_ID, projectId);
			} else if (AppConstants.NOTIFICATION_TYPE_CREATE_DR.equalsIgnoreCase(notificationName)) {
				Map<String, Object> dataModel = (Map<String, Object>) model;

				Map<String, Object> domDetails = resourceRequestDAO
						.getDomainDetailsByPorjectScopeId(dataModel.get("ProjScopeDetailID").toString());
				dataModel.putAll(domDetails);
				data.put(AppConstants.CURRENT_USER, dataModel.get(AppConstants.CURRENT_USER));
				data.put(AppConstants.PROJECT_ID, dataModel.get(AppConstants.PROJECT_ID_2));
			} else if (AppConstants.NOTIFICATION_TYPE_CREATE_DR_SPM.equalsIgnoreCase(notificationName)) {
				Map<String, Object> dataModel = (Map<String, Object>) model;

				data.put(AppConstants.CURRENT_USER, dataModel.get(AppConstants.CURRENT_USER));
				data.put(AppConstants.PROJECT_ID, dataModel.get("projectID"));
			} else if (AppConstants.NOTIFICATION_RESIGN_RESOURCE.equalsIgnoreCase(notificationName)) {
				@SuppressWarnings("unchecked")
				Map<String, Object> dataModel = (Map<String, Object>) model;
				ResigReqModel req = (ResigReqModel) dataModel.get("r");

				List<WorkEffortModel> weDetails = (List<WorkEffortModel>) dataModel.get("w");
				String porjectIds = "";

				for (WorkEffortModel we : weDetails) {
					Integer projectId = getProjectIdOfWeorkEffort(we.getWorkEffortID() + "");
					porjectIds = porjectIds + "," + projectId;
				}
				data.put(AppConstants.PROJECT_ID, porjectIds);
				EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(req.getSignum());
				req.setSignum(eDetails.getSignum() + " (" + eDetails.getEmployeeName() + ")");
				data.put(AppConstants.CURRENT_USER, req.getCurrentUser());

				EmployeeModel ManagerDetails = activityMasterDAO.getEmployeeBySignum(eDetails.getManagerSignum());
				if (ManagerDetails != null) {
					customNotificationCC += ManagerDetails.getEmployeeEmailId() + ";";
				}

				if (eDetails != null) {
					customNotificationTO += eDetails.getEmployeeEmailId() + ";";
				}
				data.put("deployedEnv", environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY)
						.get(0).getValue());
				data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, customNotificationTO);

			}
		} catch (Exception e) {
			LOGGER.warn("Error in email enrichment", e);
		}

		data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, customNotificationCC);
		return data;
	}

	private Integer getProjectIdOfWeorkEffort(String workEffort) {
		return crManagementDAO.getProjectIdbyWorkeffort(workEffort);
	}

	private static final String CREATE_WO_MAIL_SUBJECT = "create work order";

	// private static final String MAILBOX_USER="jatin.g.gupta@ericsson.com";

	private static final String VALID_INCOMING_DOMAIN = "@ericsson.com";

	private static final String OUTLOOK_TYPE_MEETING_REQUEST = "MeetingRequest";
	private static final String OUTLOOK_TYPE_MEETING_CANCEL = "MeetingCancellation";

	// @Scheduled(fixedDelay = 300000)
	public void readMailbox() throws Exception {
		if (!configurations.getBooleanProperty(ConfigurationFilesConstants.EMAIL_SCHEDULER_ENABLED)
				|| "".equals(configurations.getStringProperty(ConfigurationFilesConstants.EMAIL_PASS))) {
			return;
		} else if (configurations.getStringProperty(ConfigurationFilesConstants.EMAIL_PASS) != null && pass == null) {
			// decrypt password on first call to this method after system startup
			try {
				pass = AppUtil.DecryptText(configurations.getStringProperty(ConfigurationFilesConstants.EMAIL_PASS),
						"isf@123");
				LOGGER.info("Mailbox setup done");
			} catch (Exception e) {
				LOGGER.error("Error setting up mailbox!", e);
			}
		}
		LOGGER.info("Trying to fetch mails...");
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		ExchangeCredentials credentials = new WebCredentials(
				configurations.getStringProperty(ConfigurationFilesConstants.EMAIL_SCHEDULER_USERNAME), pass);
		service.setCredentials(credentials);
		service.autodiscoverUrl(configurations.getStringProperty(ConfigurationFilesConstants.EMAIL_SCHEDULER_USERNAME),
				new IAutodiscoverRedirectionUrl() {

					@Override
					public boolean autodiscoverRedirectionUrlValidationCallback(String redirectionUrl)
							throws AutodiscoverLocalException {
						return redirectionUrl.toLowerCase().startsWith("https://");
					}
				});

		try {
			SearchFilter searchFilterUnreadMails = new SearchFilter.SearchFilterCollection(LogicalOperator.And,
					new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));

			Mailbox mb = new Mailbox(configurations.getStringProperty(ConfigurationFilesConstants.EMAIL_MAILBOX));
			FolderId folderToAccess = new FolderId(WellKnownFolderName.Inbox, mb);

			Folder folder = Folder.bind(service, folderToAccess);
			FindItemsResults<Item> results = service.findItems(folder.getId(), searchFilterUnreadMails,
					new ItemView(100));
			for (Item item : results) {
				Item itm = Item.bind(service, item.getId(), PropertySet.FirstClassProperties);
				int errorCode = AppConstants.EMAIL_NO_ERROR;
				if (OUTLOOK_TYPE_MEETING_REQUEST.equalsIgnoreCase(itm.getXmlElementName())
						|| OUTLOOK_TYPE_MEETING_CANCEL.equalsIgnoreCase(itm.getXmlElementName())) {
					item.load();
					MeetingMessage meetingMsg = (MeetingMessage) item;
					boolean isSuccess = processOutLookMeeting(meetingMsg);
					if (!isSuccess) {
						errorCode = AppConstants.EMAIL_ERROR_MEETING_NOT_PROCESSED;
					}

				} else {
					EmailMessage emailMessage = EmailMessage.bind(service, itm.getId());

					if (isEmailProcessignRequired(emailMessage)) {
						if (emailMessage.getHasAttachments()) {
							AttachmentCollection attachements = emailMessage.getAttachments();
							for (Attachment attachement : attachements.getItems()) {
								processAttachementData(emailMessage, attachement);
							}
						} else {
							errorCode = AppConstants.EMAIL_ERROR_ATTACHEMENT;
						}
					} else {
						errorCode = AppConstants.EMAIL_ERROR_NO_PROCESS_ERROR;
					}
				}
				if (errorCode == AppConstants.EMAIL_ERROR_MEETING_NOT_PROCESSED) {
					Map<String, Object> data = new HashMap<>();
					data.put("errorCode", errorCode);
					data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, ((EmailMessage) itm).getFrom().getAddress());
					sendMail(AppConstants.NOTIFICATION_ID_NOT_PROCESSED_MEETING,
							AppConstants.NOTIFICATION_SUB_NOT_PROCESSED_MEETING, data);
				} else if (errorCode != AppConstants.EMAIL_NO_ERROR) {
					Map<String, Object> data = new HashMap<>();
					data.put("errorCode", errorCode);
					data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, ((EmailMessage) itm).getFrom().getAddress());
					sendMail(AppConstants.NOTIFICATION_ID_NOT_PROCESSED_EMAIL,
							AppConstants.NOTIFICATION_SUB_NOT_PROCESSED_EMAIL, data);
				}
				((EmailMessage) itm).setIsRead(true);
				((EmailMessage) itm).update(ConflictResolutionMode.AlwaysOverwrite);

			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		LOGGER.info("Finished mail processing...");
	}

	private boolean isEmailProcessignRequired(EmailMessage emailMessage) throws ServiceLocalException {
		return (emailMessage.getSubject() != null
				&& emailMessage.getSubject().toUpperCase().contains(CREATE_WO_MAIL_SUBJECT.toUpperCase())
				&& emailMessage.getFrom().getAddress().endsWith(VALID_INCOMING_DOMAIN));
	}

	private static final String[] EXCEL_MIME_TYPES = {
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" };

	private boolean processAttachementData(EmailMessage emailMessage, Attachment attachment)
			throws ServiceLocalException {
		try {
			FileAttachment fileAttachement = (FileAttachment) attachment;
			fileAttachement.load();

			if (emailMessage.getSubject().toUpperCase().contains(CREATE_WO_MAIL_SUBJECT.toUpperCase())
					&& Arrays.asList(EXCEL_MIME_TYPES).contains(fileAttachement.getContentType())) {
				byte[] data = fileAttachement.getContent();
				rpaService.processBulkWorkOrderAttachement(data, emailMessage.getFrom().getAddress(),
						fileAttachement.getName());
			}
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return false;

	}

	private static final String COL_PROJ_ID = "ProjectID";
	private static final String COL_TYPE = "Type";
	private static final String COL_SUBTYPE = "Activity";
	private static final String COL_DESCRIPTION = "Description";
	private static final String BOOKING_STATUS_COMPLETED = "COMPLETED";

	private boolean processOutLookMeeting(MeetingMessage meeting) throws ServiceLocalException {
		try {
			adhocActivityService.updateAdhocActivityActiveByMeetingId(meeting.getConversationId().getUniqueId(), false);
			if (OUTLOOK_TYPE_MEETING_CANCEL.equals(meeting.getXmlElementName())) {
				return true;
				// in case cancelled meeting no need to process further contents just deactivate
				// existing adhoc booking
			}
			MeetingRequest meetingRequest = (MeetingRequest) meeting;
			Map<String, String> meetingDetails = parseMeetingBody(
					MessageBody.getStringFromMessageBody(meetingRequest.getBody()));
			TblProjects proj = null;
			try {
				if (!("0".equals(meetingDetails.get(COL_PROJ_ID)) || "".equals(meetingDetails.get(COL_PROJ_ID))
						|| "na".equalsIgnoreCase(meetingDetails.get(COL_PROJ_ID)))) {
					proj = new TblProjects();
					proj.setProjectId(Integer.parseInt(meetingDetails.get(COL_PROJ_ID)));
				}
			} catch (Exception e) {
				LOGGER.error("Error in processing meeting", e);
			}
			TblAdhocBookingActivity activity = adhocActivityService
					.getBookingActivityByActivityAndType(meetingDetails.get(COL_TYPE), meetingDetails.get(COL_SUBTYPE));
			EmployeeModel creator = activityMasterDAO.getEmployeeByEmail(meetingRequest.getFrom().getAddress());

			Set<String> meetingParticipants = new HashSet<>();

			for (EmailAddress r : meetingRequest.getToRecipients().getItems()) {
				meetingParticipants.add(r.getAddress());
			}
			meetingParticipants.add(meetingRequest.getFrom().getAddress());
			for (String e : meetingParticipants) {
				EmployeeModel empDetails = activityMasterDAO.getEmployeeByEmail(e);
				if (empDetails == null)
					continue;
				TblAdhocBooking tblAdhocBooking = new TblAdhocBooking();
				tblAdhocBooking.setStartDate(meetingRequest.getStart());
				tblAdhocBooking.setActualEndDate(meetingRequest.getEnd());
				tblAdhocBooking.setSignumID(empDetails.getSignum());
				tblAdhocBooking.setOutlookMeetingId(meetingRequest.getConversationId().getUniqueId());

				tblAdhocBooking.setStatus(BOOKING_STATUS_COMPLETED);
				tblAdhocBooking.setTblProjects(proj);
				tblAdhocBooking.setTblAdhocBookingActivity(activity);
				tblAdhocBooking.setCreatedBy(creator.getSignum());
				tblAdhocBooking.setLastModifiedBy(creator.getSignum());
				tblAdhocBooking.setComment(meetingDetails.get(COL_DESCRIPTION));
				tblAdhocBooking.setActive(true);
				adhocActivityService.saveAdhocBookingForOutlook(tblAdhocBooking);
			}
		} catch (Exception e) {
			LOGGER.error("Error in processing meeting", e);
			return false;
		}
		return true;

	}

	private static final String ISF_CONTENTS_START = "@@@@@@@@@@@@@@@@DO NOT CHANGE BELOW TEXT@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";
	private static final String ISF_CONTENTS_END = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";

	private Map<String, String> parseMeetingBody(String bodyContents) {
		Map<String, String> response = new HashMap<>();
		bodyContents = bodyContents
				.substring(bodyContents.lastIndexOf(ISF_CONTENTS_START) + ISF_CONTENTS_START.length());
		bodyContents = bodyContents.substring(0, bodyContents.lastIndexOf(ISF_CONTENTS_END));
		bodyContents = bodyContents.replaceAll("###", ",");
		bodyContents = bodyContents.replaceAll("<[^>]*>", "");
		bodyContents = bodyContents.replaceAll("&nbsp;", "");
		bodyContents = bodyContents.replace("&amp;", "and");
		String[] tokens = bodyContents.split(",");
		for (String t : tokens) {
			if ("".equals(t.trim())) {
				continue;
			}
			String[] v = t.split(":");
			response.put(v[0].trim().trim(), v[1].trim().trim());
		}

		return response;
	}

	// TODO why this method is here?
	public BotDetail botDownload(BotDetail botDetail) {
		byte[] botJarFile = null;
		String status = "";
		Map<String, Object> data = null;
		//
		try {
			String botNameToDownload = botDetail.getBotId();
			String botDownloadLocation = botDetail.getBotDownloadLocation();

			LOGGER.info("File Name to download--> {} ", botNameToDownload);
			if (null != botDetail.getIsFixedPath() && null != botDetail.getDownloadBasePath()
					&& botDetail.getIsFixedPath().equalsIgnoreCase("n")) {
				botDetail.setpFileName(botDetail.getDownloadBasePath() + botNameToDownload);
			} else {
				botDetail.setpFileName(
						configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_RPA_BOT_DOWNLOAD_BASEPATH)
								+ botNameToDownload);
			}
			LOGGER.info("Downloading file from--> {}" , botDetail.getpFileName());

			appUtilDAO.botDownloadUsingProc(botDetail);
			LOGGER.info("File size in KB is : {}" , (double) botDetail.getpFileContent().length / 1024);
			if (botDetail.getpFileContent().length > 0) {
				status = "Success";
			}
		} catch (Exception e) {
			LOGGER.info("Error in botDownload(): {}" , e.getMessage());
			e.printStackTrace();
			status = "Fail";
		}
		return botDetail;
	}

	public void botUpload(BotDetail botDetail) throws Exception {

		if (botDetail != null && botDetail.getpFileContent() != null && botDetail.getZipFileName() != null) {
			LOGGER.info("In botUpload(), File size in KB is : {}", (double) botDetail.getpFileContent().length / 1024);
			LOGGER.info("In botUpload(), zipFileName : {}", botDetail.getZipFileName());
		}

		if (botDetail != null) {
			botDetail.setpFileNameWithFullPath(configurations
					.getStringProperty(ConfigurationFilesConstants.BOTSTORE_RPA_BOT_BOTOUTPUT_UPLOAD_BASEPATH)
					+ botDetail.getZipFileName());
		}
		appUtilDAO.botUpload(botDetail);
	}

	public Map<String, Object> enrichMailforFeedbackStatus(WorkFlowFeedbackModel workFlowFeedbackModel,
			FeedbackStatusUpdateModel feedbackStatusUpdateModel) {
		Map<String, Object> data = new HashMap<>();

		String subject = feedbackStatusUpdateModel.getFeedbackStatus();

		if (workFlowFeedbackModel.getFeedbackType().equalsIgnoreCase(AppConstants.FEEDBACK_TYPE_STEP)
				&& !feedbackStatusUpdateModel.getFeedbackStatus().equalsIgnoreCase(AppConstants.ACCEPTED)) {
			subject = "Feedback is " + feedbackStatusUpdateModel.getFeedbackStatusNew() + " - "
					+ "Step Feedback (FeedbackID: " + workFlowFeedbackModel.getFeedbackDetailID() + ", ProjectID: "
					+ workFlowFeedbackModel.getProjectID() + ")";
			workFlowFeedbackModel.setFeedbackLevel("STEP");
		} else if (workFlowFeedbackModel.getFeedbackType().equalsIgnoreCase(AppConstants.FEEDBACK_TYPE_WF)
				&& !feedbackStatusUpdateModel.getFeedbackStatus().equalsIgnoreCase(AppConstants.ACCEPTED)) {
			subject = "Feedback is " + feedbackStatusUpdateModel.getFeedbackStatusNew() + " - "
					+ "WF Feedback (FeedbackID: " + workFlowFeedbackModel.getFeedbackDetailID() + ", ProjectID: "
					+ workFlowFeedbackModel.getProjectID() + ")";
			workFlowFeedbackModel.setFeedbackLevel("WF");
		} else {
			if (workFlowFeedbackModel.getFeedbackType().equalsIgnoreCase(AppConstants.FEEDBACK_TYPE_STEP)
					&& feedbackStatusUpdateModel.getFeedbackStatus().equalsIgnoreCase(AppConstants.ACCEPTED)) {
				subject = "Feedback is Accepted and Closed - " + "Step Feedback (FeedbackID: "
						+ workFlowFeedbackModel.getFeedbackDetailID() + ", ProjectID: "
						+ workFlowFeedbackModel.getProjectID() + ")";
				workFlowFeedbackModel.setFeedbackLevel("STEP");
			} else if (workFlowFeedbackModel.getFeedbackType().equalsIgnoreCase(AppConstants.FEEDBACK_TYPE_WF)
					&& feedbackStatusUpdateModel.getFeedbackStatus().equalsIgnoreCase(AppConstants.ACCEPTED)) {
				subject = "Feedback is Accepted and Closed - " + "WF Feedback (FeedbackID: "
						+ workFlowFeedbackModel.getFeedbackDetailID() + ", ProjectID: "
						+ workFlowFeedbackModel.getProjectID() + ")";
				workFlowFeedbackModel.setFeedbackLevel("WF");

			}

		}
		data.put("subject", subject);
		data.put("details", workFlowFeedbackModel);
		data.put("statusUpdate", feedbackStatusUpdateModel);
		String toEmails = StringUtils.EMPTY;
		String ccEmails = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(workFlowFeedbackModel.getSignum())) {

			EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(workFlowFeedbackModel.getSignum());
			if (!feedbackStatusUpdateModel.getFeedbackStatusNew()
					.equalsIgnoreCase(AppConstants.SEND_BACK_TO_PM_Implemented)
					&& !feedbackStatusUpdateModel.getFeedbackStatusNew()
							.equalsIgnoreCase(AppConstants.SEND_BACK_TO_PM_NOT_FEASIBLE)
					&& !feedbackStatusUpdateModel.getFeedbackStatus().equalsIgnoreCase(AppConstants.ACCEPTED)) {
				toEmails = eDetails.getEmployeeEmailId();
			}
		}
		if (workFlowFeedbackModel.getProjectID() != 0) {
			MailModel model = activityMasterDAO
					.getFeedbackMailNotificationDetails(Integer.valueOf(workFlowFeedbackModel.getProjectID()));
			StringJoiner joiner = new StringJoiner(";");
			if (!feedbackStatusUpdateModel.getFeedbackStatusNew()
					.equalsIgnoreCase(AppConstants.SEND_BACK_TO_PM_Implemented)
					&& !feedbackStatusUpdateModel.getFeedbackStatusNew()
							.equalsIgnoreCase(AppConstants.SEND_BACK_TO_PM_NOT_FEASIBLE)
					&& !feedbackStatusUpdateModel.getFeedbackStatus().equalsIgnoreCase(AppConstants.ACCEPTED)) {

				if (StringUtils.isNotEmpty(model.getProjectCreatorEmailID()))
					joiner.add(model.getProjectCreatorEmailID());

				if (CollectionUtils.isNotEmpty(model.getdR_EmailID()))
					joiner.add(String.join(";", model.getdR_EmailID()));

				if (StringUtils.isNotEmpty(model.getOmEmailID()))
					joiner.add(String.join(";", model.getOmEmailID()));

				ccEmails = joiner.toString();

			} else if (feedbackStatusUpdateModel.getFeedbackStatusNew()
					.equalsIgnoreCase(AppConstants.SEND_BACK_TO_PM_Implemented)
					|| feedbackStatusUpdateModel.getFeedbackStatusNew()
							.equalsIgnoreCase(AppConstants.SEND_BACK_TO_PM_NOT_FEASIBLE)
					|| feedbackStatusUpdateModel.getFeedbackStatus().equalsIgnoreCase(AppConstants.ACCEPTED)) {

				EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(workFlowFeedbackModel.getSignum());
				toEmails = eDetails.getEmployeeEmailId();

				if (CollectionUtils.isNotEmpty(model.getdR_EmailID()))
					joiner.add(String.join(";", model.getdR_EmailID()));

				if (StringUtils.isNotEmpty(toEmails))
					joiner.add(String.join(";", toEmails));

				ccEmails = joiner.toString();

			}

			if (feedbackStatusUpdateModel.getFeedbackStatusNew()
					.equalsIgnoreCase(AppConstants.SEND_BACK_TO_PM_Implemented)
					|| feedbackStatusUpdateModel.getFeedbackStatusNew()
							.equalsIgnoreCase(AppConstants.SEND_BACK_TO_PM_NOT_FEASIBLE)
					|| feedbackStatusUpdateModel.getFeedbackStatus().equalsIgnoreCase(AppConstants.ACCEPTED)) {
				StringJoiner joinermail = new StringJoiner(";");

				if (StringUtils.isNotEmpty(model.getProjectCreatorEmailID()))
					joinermail.add(model.getProjectCreatorEmailID());

				if (StringUtils.isNotEmpty(model.getOmEmailID()))
					joinermail.add(String.join(";", model.getOmEmailID()));

				toEmails = joinermail.toString();

			}

		}

		if (StringUtils.isNotBlank(toEmails)) {
			data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, toEmails);
		}
		if (StringUtils.isNotBlank(toEmails)) {
			data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, ccEmails);
		}
		return data;
	}

	@Transactional("transactionManager")
	public com.ericsson.isf.model.Response<Void> uploadSharePointDetails(SharePointModel sharePointModel) {

		com.ericsson.isf.model.Response<Void> response = new com.ericsson.isf.model.Response<>();
		File file = null;
		try {
			// getting Sharepoint details
			SharePointModel store = this.appUtilDAO.getSharePointDetails(sharePointModel.getMarketArea());

			// getting data from View to write in CSV file
			ArrayList<Map<String, Object>> data = this.appUtilDAO.getViewData(store.getViewName(),
					sharePointModel.getClause());
			// byte[] fData = generateXlsFile(data);
			// file = convertBytesToFile(fData);
			// generating CSV file
			String SEPARATOR = sharePointModel.getSeparator();
			String filepath = toCSV(data, SEPARATOR, sharePointModel.getDateFormats(),
					sharePointModel.getFileNamePattern());

//			String filepath = getFilePathForCSV(
//					configurations.getStringProperty(ConfigurationFilesConstants.FILE_PATH));
			// getting CSV file
			file = new File(filepath);
			store.setFileNamePattern(sharePointModel.getFileNamePattern());
			// uploading CSV file

			HttpResponse isfileuploaded = sharePointFileUpload.uploadFile(store, file);
			if (isfileuploaded.getStatusLine().getStatusCode() == HTTPResponse.SC_OK) {
				response.addFormMessage("Response Code : " + isfileuploaded.getStatusLine().getStatusCode()
						+ " File Uploadd Successfully!");
			} else {
				response.addFormError("Response Code : " + isfileuploaded.getStatusLine().getStatusCode() + " "
						+ isfileuploaded.getStatusLine().getReasonPhrase());
			}

		} catch (Exception e) {
			response.addFormError("Error " + e.getMessage());
			e.printStackTrace();
			LOGGER.info(e.getMessage());
		} finally {
			file.delete();
		}
		return response;
	}

	private String getFilePathForCSV(String filepath, String fileNamePattern) {
		if (filepath.endsWith("/")) {
			filepath = filepath + "demo" + "_" + fileNamePattern + "_" + new Date().getTime() + ".csv";
		} else {
			filepath = filepath + "/" + "demo" + "_" + fileNamePattern + "_" + new Date().getTime() + ".csv";
		}
		return filepath;
	}

	private String toCSV(ArrayList<Map<String, Object>> list, String SEPARATOR, Map<String, String> dateFormats,
			String fileNamePattern) {
		List<String> headers = list.stream().flatMap(map -> map.keySet().stream()).distinct()
				.collect(Collectors.toList());
		String headerValue = StringUtils.EMPTY;
		String filepath = getFilePathForCSV(configurations.getStringProperty(ConfigurationFilesConstants.FILE_PATH),
				fileNamePattern);
		try (FileWriter writer = new FileWriter(filepath, true);) {
			for (String string : headers) {
				string = '"' + string + '"';
				headerValue += string;
				headerValue += SEPARATOR;
				// writer.write(string);
				// writer.write(SEPARATOR);
			}
			if (headerValue.endsWith(SEPARATOR)) {
				headerValue = headerValue.substring(0, headerValue.length() - 1);
			}
			writer.write(headerValue);
			writer.write("\r\n");
			for (Map<String, Object> lmap : list) {
				String finalvalue = StringUtils.EMPTY;
				for (Map.Entry<String, Object> string2 : lmap.entrySet()) {
					if (string2.getValue() != null && string2.getKey() != null) {
						String text = null;
						if (StringUtils.isNotBlank(string2.getValue().toString())) {
							if (dateFormats.containsKey(string2.getKey())) {
								SimpleDateFormat simpleformat = new SimpleDateFormat(dateFormats.get(string2.getKey()));
								text = simpleformat.format((Date) string2.getValue());
							} else {
								text = string2.getValue().toString();
								text = text.replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("\"", "");
								text = '"' + text + '"';
							}
							finalvalue += text;
							
						} else {
							finalvalue += StringUtils.EMPTY;
							
						}
					} else {
						finalvalue += StringUtils.EMPTY;
						
					}
					finalvalue += SEPARATOR;
				}
				if (finalvalue.endsWith(SEPARATOR)) {
					finalvalue = finalvalue.substring(0, finalvalue.length() - 1);
				}
				writer.write(finalvalue);
				writer.write("\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filepath;
	}

	public Map<String, Object> enrichMailforUpdateProficiency(WorkflowProficiencyModel workFlowProficiencyModel,
			boolean flag) {

		Map<String, Object> data = new HashMap<>();
		Set<String> uniqueSignum = new HashSet<>();

		if (flag) {
			if (StringUtils.isNotEmpty(workFlowProficiencyModel.getProficiencyLevel())) {

				workFlowProficiencyModel.setPreviousProficiencyLevel(workFlowProficiencyModel.getProficiencyLevel());
			}

			if (StringUtils.equalsIgnoreCase(workFlowProficiencyModel.getProficiencyLevel(),
					AppConstants.EXPERIENCED)) {
				workFlowProficiencyModel.setProficiencyLevel(AppConstants.ASSESSED);
			} else if (StringUtils.equalsIgnoreCase(workFlowProficiencyModel.getProficiencyLevel(),
					AppConstants.ASSESSED)) {
				workFlowProficiencyModel.setProficiencyLevel(AppConstants.EXPERIENCED);
			}
			if (StringUtils.isEmpty(workFlowProficiencyModel.getComments())) {
				workFlowProficiencyModel.setComments(AppConstants.NA);
			}
		}

		StringBuilder subject = new StringBuilder("");

		subject = subject.append("Proficiency Updated to - ").append(workFlowProficiencyModel.getProficiencyLevel())
				.append("(Project ID: " + workFlowProficiencyModel.getProjectId()).append(")");

		data.put(AppConstants.SUBJECT, subject);
		data.put(AppConstants.DETAILS_KEY, workFlowProficiencyModel);
		String toEmails = StringUtils.EMPTY;
		String ccEmails = StringUtils.EMPTY;
		String wfOwner = StringUtils.EMPTY;
		StringJoiner joiner = new StringJoiner(";");
		if (workFlowProficiencyModel.getProjectId() != 0 && workFlowProficiencyModel.getSubActivityId() != 0
				&& workFlowProficiencyModel.getWorkFlowId() != 0
				&& StringUtils.isNotEmpty(workFlowProficiencyModel.getWorkFlowName())) {
			wfOwner = activityMasterDAO.getWfOwnerByWFIDandWFName(workFlowProficiencyModel.getProjectId(),
					workFlowProficiencyModel.getSubActivityId(), workFlowProficiencyModel.getWorkFlowId(),
					workFlowProficiencyModel.getWorkFlowName());
		}
		if (workFlowProficiencyModel.getProjectId() != 0) {
			MailModel model = activityMasterDAO
					.getFeedbackMailNotificationDetails(Integer.valueOf(workFlowProficiencyModel.getProjectId()));

			uniqueSignum.add(StringUtils.upperCase(model.getProjectCreatorSignum()));
			uniqueSignum.add(StringUtils.upperCase(wfOwner));
			uniqueSignum.add(StringUtils.upperCase(workFlowProficiencyModel.getModifiedBy()));
			int count = 0;
			for (String signum : uniqueSignum) {
				EmployeeModel mailID = activityMasterDAO.getEmployeeBySignum(signum);
				ccEmails = mailID.getEmployeeEmailId();
				if (count == 0) {
					joiner.add(ccEmails);
				}
				if (count > 0) {
					joiner.add(String.join(";", ccEmails));
				}
				count++;
			}

		}
		ccEmails = joiner.toString();

		if (StringUtils.isNotBlank(workFlowProficiencyModel.getSignum())) {
			EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(workFlowProficiencyModel.getSignum());
			toEmails = eDetails.getEmployeeEmailId();
		}

		if (StringUtils.isNotBlank(toEmails)) {
			data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, toEmails);
		}
		if (StringUtils.isNotBlank(ccEmails)) {
			data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, ccEmails);
		}
		return data;

	}

	public Map<String, Object> enrichMailforSaveProficiency(UserWorkFlowProficencyModel userWorkFlowProficencyModel,
			String transition, String createdBy) {

		Map<String, Object> data = new HashMap<>();
		Set<String> uniqueSignum = new HashSet<>();
		Set<String> uniqueSignum2 = new HashSet<>();

		StringBuilder subject = new StringBuilder(StringUtils.EMPTY);
		if (StringUtils.equalsIgnoreCase(transition, AppConstants.FORWARD)) {
			data.put(AppConstants.TRANSITION, AppConstants.UPGRADED);
			subject = subject.append(AppConstants.WORKFLOW).append(StringUtils.SPACE)
					.append(AppConstants.OPENING_BRACES).append(userWorkFlowProficencyModel.getWorkFlowName())
					.append(AppConstants.CLOSING_BRACES).append(StringUtils.SPACE).append(AppConstants.PROFICIENCY)
					.append(StringUtils.SPACE).append(AppConstants.UPDATED);
			userWorkFlowProficencyModel.setProficiencyName(AppConstants.EXPERIENCED);
			userWorkFlowProficencyModel.setPrevProficiencyName(AppConstants.ASSESSED);
			data.put(AppConstants.SUBJECT, subject);
		} else if (StringUtils.equalsIgnoreCase(transition, AppConstants.REVERSE)) {
			data.put(AppConstants.TRANSITION, AppConstants.DEGRADED);
			subject = subject.append(AppConstants.WORKFLOW).append(StringUtils.SPACE)
					.append(AppConstants.OPENING_BRACES).append(userWorkFlowProficencyModel.getWorkFlowName())
					.append(AppConstants.CLOSING_BRACES).append(StringUtils.SPACE).append(AppConstants.PROFICIENCY)
					.append(StringUtils.SPACE).append(AppConstants.UPDATED);
			userWorkFlowProficencyModel.setProficiencyName(AppConstants.ASSESSED);
			userWorkFlowProficencyModel.setPrevProficiencyName(AppConstants.EXPERIENCED);
			data.put(AppConstants.SUBJECT, subject);
		}

		data.put(AppConstants.SUBACTIVITY,
				activityMasterDAO.getSubActivityByID(userWorkFlowProficencyModel.getSubActivityID()));
		data.put(AppConstants.DETAILS_KEY, userWorkFlowProficencyModel);
		String toEmails = StringUtils.EMPTY;
		String ccEmails = StringUtils.EMPTY;
		String wfOwner = StringUtils.EMPTY;
		StringJoiner joiner = new StringJoiner(";");

		if (userWorkFlowProficencyModel.getProjectID() != 0 && userWorkFlowProficencyModel.getSubActivityID() != 0
				&& userWorkFlowProficencyModel.getWfId() != 0
				&& StringUtils.isNotEmpty(userWorkFlowProficencyModel.getWorkFlowName())) {
			wfOwner = activityMasterDAO.getWfOwnerByWFIDandWFName(userWorkFlowProficencyModel.getProjectID(),
					userWorkFlowProficencyModel.getSubActivityID(), userWorkFlowProficencyModel.getWfId(),
					userWorkFlowProficencyModel.getWorkFlowName());
		}
		if (userWorkFlowProficencyModel.getProjectID() != 0) {
			MailModel model = activityMasterDAO
					.getFeedbackMailNotificationDetails(Integer.valueOf(userWorkFlowProficencyModel.getProjectID()));

			uniqueSignum.add(model.getProjectCreatorSignum().trim().toLowerCase());
			uniqueSignum.add(wfOwner.toLowerCase().trim());
			uniqueSignum.add(createdBy.toLowerCase().trim());

			int count = 0;
			for (String signum : uniqueSignum) {
				EmployeeModel mailID = activityMasterDAO.getEmployeeBySignum(signum);
				ccEmails = mailID.getEmployeeEmailId();
				if (count == 0) {
					joiner.add(ccEmails);
				}
				if (count > 0) {
					joiner.add(String.join(";", ccEmails));
				}
				count++;
			}
		}
		ccEmails = joiner.toString();

		joiner = new StringJoiner(";");
		if (userWorkFlowProficencyModel.getWoId() != 0) {
			List<Signum> listofSignum = activityMasterDAO.getAllSignumForWoid(userWorkFlowProficencyModel.getWoId());

			for (Signum signum : listofSignum) {
				uniqueSignum2.add(signum.getSignum());
			}

			int count = 0;
			for (String signum : uniqueSignum2) {
				EmployeeModel mailID = activityMasterDAO.getEmployeeBySignum(signum);
				toEmails = mailID.getEmployeeEmailId();
				if (count == 0) {
					joiner.add(toEmails);
				}
				if (count > 0) {
					joiner.add(String.join(";", toEmails));
				}
				count++;
			}
		}
		toEmails = joiner.toString();

		if (StringUtils.isNotBlank(toEmails)) {
			data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, toEmails);
		}
		if (StringUtils.isNotBlank(toEmails)) {
			data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, ccEmails);
		}
		return data;
	}

	public Map<String, Object> enrichMailforNetworkElemnt( String uploadedBy,
			int successCount, String startTime,Set<String> pmSet,Set<String> drSet,HashMap<String,Object> countryCustomer) {
		Map<String, Object> data = new HashMap<>();
		data.put(AppConstants.SUBJECT, NETWORK_ELEMENT_UPLOADED);
		data.put(SUCCESS_COUNT, successCount);
		data.put(UPLOADED_ON, startTime);
		data.put(CUSTOMER_NAME, countryCustomer.get("CustomerName"));
		data.put(COUNTRY_NAME, countryCustomer.get("CountryName"));
		
		if (StringUtils.isNotBlank(uploadedBy)) {
			EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(uploadedBy);
			data.put(UPLOADED_BY, eDetails.getEmployeeName() + AppConstants.OPENING_BRACES + eDetails.getSignum() + AppConstants.CLOSING_BRACES);
		}

		if (CollectionUtils.isNotEmpty(pmSet)) {
			data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, pmSet);
		}
		if (CollectionUtils.isNotEmpty(drSet)) {
			data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, drSet);
		}
		return data;

	}

	@Transactional(value = "transactionManager", propagation = Propagation.REQUIRES_NEW)
	public boolean sendMailWithAttachment(String templateId, Map<String, Object> placeholders, String fileName) {
		try {
			EmailModel emailDetails = getMailDetails(templateId, placeholders);
			sendMailWithAttachment(emailDetails, placeholders, fileName);
		} catch (Exception e) {
			LOGGER.error("sendMail", e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean sendMailWithAttachment(EmailModel emailDetails, Map<String, Object> placeholders, String fileName) {
		try {

			StringJoiner joiner = new StringJoiner(",");
			if (placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_CC) != null) {
				Set<String> ccEmail=(Set<String>) placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_CC);
				for(String str:ccEmail){
					joiner.add(str);
				}

			}
			emailDetails.setCc(joiner.toString());
			StringJoiner joiner1 = new StringJoiner(",");
			if (placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_TO) != null) {
				Set<String> toEmail=(Set<String>) placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_TO);
				for(String str:toEmail){
					joiner1.add(str);
				}
			}
			
			emailDetails.setTo(joiner1.toString());
			emailDetails.setSenderSignum(MAIL_SENDER_ID);
			List<EnvironmentPropertyModel> environmentPropertyModel = environmentPropertyService
					.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY);
			if ("CLOUD".equalsIgnoreCase(environmentPropertyModel.get(0).getDeployedEnv())) {
				LOGGER.info("Checking sendGridMailUtility block");
				sendGridMailUtility(emailDetails, true);
				LOGGER.info("Checking sendGridMailUtility block pass");
			} else {
				LOGGER.info("Checking sendSmtpMail block");
				sendSmtpMailWithAttachment(emailDetails, true, fileName);
				LOGGER.info("Checking sendSmtpMail block pass");
			}

		} catch (Exception e) {
			LOGGER.warn("Error while sending email", e);
		}
		return false;
	}


	private EmployeeModel getCurrentUser(Map<String, Object> placeholders, EmployeeModel currentUser,
			SearchResourceByFilterModel req) {
		if (placeholders.get(AppConstants.CURRENT_USER) != null) {
			req.setSignum(placeholders.get(AppConstants.CURRENT_USER).toString());
			currentUser = activityMasterDAO.getEmployeeBySignum(placeholders.get(AppConstants.CURRENT_USER).toString());
			if (currentUser != null) {
				placeholders.put(AppConstants.CURRENT_USER_NAME,
						currentUser.getSignum() + " (" + currentUser.getEmployeeName() + ")");
			}
		}
		return currentUser;
	}

	public void sendSmtpMailWithAttachment(EmailModel emailDetails, boolean update, String fileName) {
		Runnable r = () -> {
			try {
				if (configurations.getBooleanProperty(ConfigurationFilesConstants.SEND_KAFKA_MAIL)) {

					if (StringUtils.isBlank(emailDetails.getStatus())) {
						emailDetails.setStatus("SENT");
					}
					sendKafkaMessageQueue(emailDetails);
					if (update) {
						appUtilDAO.sendSmtpMail(emailDetails);
					}
				} else {
					sendSmtpMailThreadWithAttachment(emailDetails, fileName);
				}

			} catch (MessagingException e) {
				LOGGER.info(AppConstants.ERROR_IN_SENDSMTPMAIL, e.getMessage());
				Thread.currentThread().interrupt();
			} catch (IOException e) {
				LOGGER.info(AppConstants.ERROR_IN_SENDSMTPMAIL, e.getMessage());
			}
		};
		new Thread(r).start();

	}

	public void sendSmtpMailThreadWithAttachment(EmailModel emailDetails, String filePath) throws MessagingException {

		Properties properties = System.getProperties();
		properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_URL,
				configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_URL));
		properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_USER,
				configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_USER));
		properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_PASSWORD,
				configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_PASSWORD));
		properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_STARTTLS,
				configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_STARTTLS));
		properties.setProperty(ConfigurationFilesConstants.SMTP_MAIL_PORT,
				configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_PORT));
		// enable authentication
		properties.put(ConfigurationFilesConstants.SMTP_MAIL_AUTH,
				configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_AUTH));

		properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			 @Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(
						configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_USER),
						configurations.getStringProperty(ConfigurationFilesConstants.SMTP_MAIL_PASSWORD));
			}
		});

		try {
			// Define message
			Message message = new MimeMessage(session);
			// from
			if (StringUtils.isNotBlank(emailDetails.getSenderSignum())) {
				message.setFrom(new InternetAddress(emailDetails.getSenderSignum().replaceAll("\\s", "")));
			} else {
				emailDetails.setSenderSignum(MAIL_SENDER_ID);
				message.setFrom(new InternetAddress(emailDetails.getSenderSignum().replaceAll("\\s", "")));
			}
			// copy-recepients
			if (StringUtils.isNotBlank(emailDetails.getCc())) {
				String[] str=emailDetails.getCc().split(","); 
				InternetAddress[] address = new InternetAddress[str.length];
				for (int i = 0; i < str.length; i++) {
				    address[i] = new InternetAddress(str[i]);
				}
				message.setRecipients(Message.RecipientType.CC,address);
			}

			// to , recipients
			if (StringUtils.isNotBlank(emailDetails.getTo())) {
				String[] str=emailDetails.getTo().split(","); 
				InternetAddress[] address = new InternetAddress[str.length];
				for (int i = 0; i < str.length; i++) {
				    address[i] = new InternetAddress(str[i]);
				}
				message.setRecipients(Message.RecipientType.TO, address);	
			}
			if (emailDetails.getSubject() != null) {
				message.setSubject(emailDetails.getSubject());
			}

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message

			messageBodyPart.setContent(emailDetails.getBody(), "text/html; charset=utf-8");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			String fileName = Paths.get(new URI(filePath).getPath()).getFileName().toString();
			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			URL url = new URL(filePath);
			URLDataSource uds = new URLDataSource(url);
			messageBodyPart.setDataHandler(new DataHandler(uds));
			messageBodyPart.setDisposition(Part.ATTACHMENT);
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);

			// Put parts in message
			message.setContent(multipart);

			// Send the message
			Transport.send(message);
			LOGGER.info("message sent successfully...");
		} catch (Exception e) {
			LOGGER.error("message not sent successfully...");
		}

	}

	public Response<Void> validateSharePointAccess(SharePointModel sharePointModel) {

		com.ericsson.isf.model.Response<Void> response = new com.ericsson.isf.model.Response<>();
		try {
			LOGGER.info("validateSharePointAccess=======start");

			String isSharePointAccessed = sharePointFileUpload.getSharepointAccess(sharePointModel.getClientId(),
					sharePointModel.getSecretKey(), sharePointModel.getBaseURL());

			if (StringUtils.equalsIgnoreCase(isSharePointAccessed, ACCESS_GRANTED)) {
				response.addFormMessage(ACCESS_GRANTED);
			} else {

				response.addFormError("Access Denied :" + isSharePointAccessed);
			}
			LOGGER.info("validateSharePointAccess=======END");
		}

		catch (Exception e) {
			response.addFormError("Error " + e.getMessage());
			e.printStackTrace();
			LOGGER.info(e.getMessage());
		}
		return response;
	}

	public ResponseEntity<Response<Void>> saveSharePointAccessDetail(SharePointModel sharePointModel) {
		Response<Void> result = new Response<>();
		try {
			LOGGER.info("saveSharePointAccessDetail : Start");
			boolean checkMarketAreaIDAlreadyExist = wOExecutionDAO
					.validateSharePointDetailWithMarketArea(sharePointModel.getMarketAreaID());
			if (checkMarketAreaIDAlreadyExist) {
				// update case
				boolean isupdateSuccessFul = appUtilDAO.updateSharePointDetailsForServerBot(sharePointModel);
				if (!isupdateSuccessFul) {
					result.addFormError(SITE_NAME_IS_ALREADY_EXIST_FOR_OTHER_MARKET_AREA_PLEASE_PROVIDE_DIFFERENT_SITE_NAME);
				} else {
					result.addFormMessage(VALUE_UPDATED_SUCCESS_FULLY);
				}
			} else {
				// insert case
				boolean isInsertSuccessFul = appUtilDAO.insertSharePointDetailsForServerBot(sharePointModel);
				if (!isInsertSuccessFul) {
					result.addFormError(SITE_NAME_IS_ALREADY_EXIST_FOR_OTHER_MARKET_AREA_PLEASE_PROVIDE_DIFFERENT_SITE_NAME);
				} else {
					result.addFormMessage(VALUE_ADDED_SUCCESS_FULLY);
				}
			}

			LOGGER.info("saveSharePointAccessDetail:End");
		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);

	}
	
	public ResponseEntity<Response<Void>> sendEmail(EmailModel emailModel) {
		
		Response<Void> result = new Response<>();
		try {
			LOGGER.info("sendEmail : Start");
			
			sendSmtpMail(emailModel, false);
			result.addFormMessage(EMAIL_SENT_SUCCESSFULLY);
		
			LOGGER.info("sendEmail : End");
		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);

	}
	
	
	
	

}
