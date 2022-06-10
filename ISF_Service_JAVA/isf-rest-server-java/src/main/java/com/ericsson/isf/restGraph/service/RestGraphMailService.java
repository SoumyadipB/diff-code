package com.ericsson.isf.restGraph.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.EnvironmentPropertyModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.botstore.TblAdhocBooking;
import com.ericsson.isf.model.botstore.TblAdhocBookingActivity;
import com.ericsson.isf.model.botstore.TblProjects;
import com.ericsson.isf.restGraph.dao.RestGraphMailDAO;
import com.ericsson.isf.restGraph.model.Attachment;
import com.ericsson.isf.restGraph.model.Attendee;
import com.ericsson.isf.restGraph.model.AzureAppDetailsEnum;
import com.ericsson.isf.restGraph.model.Message;
import com.ericsson.isf.restGraph.model.PagedResult;
import com.ericsson.isf.restGraph.model.RequestMessageBody;
import com.ericsson.isf.restGraph.model.TokenRequest;
import com.ericsson.isf.restGraph.model.TokenResponse;
import com.ericsson.isf.service.AdhocActivityService;
import com.ericsson.isf.service.EnvironmentPropertyService;
import com.ericsson.isf.service.OutlookAndEmailService;
import com.ericsson.isf.service.RpaService;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.ericsson.isf.util.DateTimeUtil;

import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;

/**
 * 
 * @author eakinhm
 *
 */
@Service
public class RestGraphMailService {

	private static final String ERROR_IN_PROCESSING_MEETING = "Error in processing meeting";

	private static final String ERROR_CODE = "errorCode";

	@Autowired
	private ApplicationConfigurations configurations;

	@Autowired
	private RestGraphMailDAO restGraphMailDAO;

	@Autowired
	private AdhocActivityService adhocActivityService;

	@Autowired
	RpaService rpaService;

	@Autowired
	private ActivityMasterDAO activityMasterDAO;

	@Autowired
	private OutlookAndEmailService outlookAndEmailService;
	
	@Autowired
	private EnvironmentPropertyService environmentPropertyService;

	private static final Logger LOGGER = LoggerFactory.getLogger(RestGraphMailService.class);
	// Retrieve messages from the inbox
	private static final String INBOX = "Inbox";
	private static final String DELETED_ITEMS = "deletedItems";
	private static final String MESSAGE_FILTER_QUERY = "isRead eq false";
	private static final String CREATE_WO_MAIL_SUBJECT = "create work order";
	// Only return the properties we care about
	// private static final String MESSAGE_SELECT_QUERY =
	// "receivedDateTime,from,subject,hasAttachments,bodyPreview,body";
	private static final String EVENT_FILTER_QUERY = "isRead eq false";
	private static final String MEETING_REQUEST = "meetingRequest";
	private static final String MEETING_CANCEL = "meetingCancelled";
	private static final String ISF_CONTENTS_START = "@@@@@@@@@@@@@@@@DO NOT CHANGE BELOW TEXT@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";
	private static final String ISF_CONTENTS_END = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";
	private static final String COL_PROJ_ID = "ProjectID";
	private static final String COL_TYPE = "Type";
	private static final String COL_SUBTYPE = "Activity";
	private static final String COL_DESCRIPTION = "Description";
	private static final String BOOKING_STATUS_COMPLETED = "COMPLETED";
	private static final String[] EXCEL_MIME_TYPES = {
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" };
	private static final String VALID_INCOMING_DOMAIN = "@ericsson.com";

	/**
	 * This method generate refresh token by using given information in tokenRequest
	 * and simultaneously inactivate previous active refresh_token
	 * 
	 * 
	 * @param tokenRequest
	 * @return
	 */
	@Transactional("transactionManager")
	public Response<Void> generateRefreshToken(TokenRequest tokenRequest) {

		Response<Void> apiResponse = new Response<Void>();
		String refreshToken = saveRefreshToken(tokenRequest);
		if (StringUtils.isBlank(refreshToken)) {
			apiResponse.addFormError("Unable to generate refresh Token Please try again!");
		} else {
			apiResponse.addFormMessage("Refresh Token " + refreshToken + " has been generated successfully!");
		}
		return apiResponse;
	}

	/**
	 * This method generate refresh token by using given information in tokenRequest
	 * and simultaneously inactivate previous active refresh_token
	 * 
	 * @param tokenRequest
	 * @return
	 */
	@Transactional("transactionManager")
	private String saveRefreshToken(TokenRequest tokenRequest) {

		AzureAppDetailsEnum azureApp = AzureAppDetailsEnum
				.getEnumValue(configurations.getStringProperty(ConfigurationFilesConstants.AZURE_APP_NAME));

		// Get active refresh_token details
		Map<String, Object> activeTokenDetails = restGraphMailDAO.getActiveRefreshToken();

		// Get refresh_token from Auth Code
		TokenResponse tokenResponse = AuthHelper.getTokenFromAuthCode(tokenRequest, azureApp);
		if (tokenResponse == null) {

			LOGGER.error("Auth Code is invalid!");
			return StringUtils.EMPTY;
		}

		if (MapUtils.isNotEmpty(activeTokenDetails)) {

			// Inactivate previous active refresh_token
			restGraphMailDAO.inactivateRefreshTokenByID((int) activeTokenDetails.get("ID"));
		}
		boolean isSuccess = restGraphMailDAO.saveRefreshToken(tokenResponse);
		if (isSuccess) {

			return tokenResponse.getRefreshToken();
		}
		return StringUtils.EMPTY;
	}

	/**
	 * This method will run after every 5 minutes and will read all unread mails &
	 * events and will process them
	 * 
	 * @throws Exception
	 */
	//@Scheduled(fixedDelay = 300000)
	public void readMailbox() {

		if (!configurations.getBooleanProperty(ConfigurationFilesConstants.REST_GRAPH_EMAIL_SCHEDULER_ENABLED)) {
			return;
		}
		LOGGER.info("Trying to fetch mails...");

		AzureAppDetailsEnum azureApp = AzureAppDetailsEnum
				.getEnumValue(configurations.getStringProperty(ConfigurationFilesConstants.AZURE_APP_NAME));
		String userPrincipalName = configurations.getStringProperty(ConfigurationFilesConstants.EMAIL_MAILBOX);
		boolean isSendMailForRefreshToken = false;
		boolean isMessagesProcessed = true;
		boolean isEventMessagesProcessed = true;

		// Get active refresh_token from DB
		String refreshToken = getRefreshToken();
		if (StringUtils.isBlank(refreshToken)) {

			isSendMailForRefreshToken = true;
		} else {

			// Get access_token
			TokenResponse tokenResponse = getTokenResponse(refreshToken, azureApp);

			if (tokenResponse == null) {

				// Send mail if there is an error while access_token generation
				LOGGER.info("Getting error while generating access token!");
				isSendMailForRefreshToken = true;
			} else {

				// Set requestBody values to update mail after process has been done
				RequestMessageBody requestMessageBody = new RequestMessageBody(true);

				// Get OutlookService via Retrofit
				OutlookService outlookService = OutlookServiceBuilder.getOutlookService(tokenResponse.getAccessToken());

				try {

					isMessagesProcessed = processMessages(outlookService, userPrincipalName, requestMessageBody);
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}

				//Event message functionality
				//Commenting this Functionality as per Requirement
//				try {
//					isEventMessagesProcessed = processEventMessages(outlookService, userPrincipalName,requestMessageBody);
//				} catch (Exception e) {
//					LOGGER.error(e.getMessage());
//				}

			}

		}

		// Send mail if any error occurred while working on refresh_token
		if (isSendMailForRefreshToken) {

			Map<String, Object> data = new HashMap<>();
			data.put(StringUtils.lowerCase(AppConstants.ERROR),
					"Mail API is getting error while creating aceess token. Either refresh_token is inactive or expired. Please Generate refresh_token Soon!");
			data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO,
					configurations.getStringProperty(ConfigurationFilesConstants.ISF_SUPPORT_MAIL));
			outlookAndEmailService.sendMail(AppConstants.MAIL_API_TOKEN_ERROR, data);
		}

		
		if (!isMessagesProcessed || !isEventMessagesProcessed) {

			Map<String, Object> data = new HashMap<>();
			data.put(StringUtils.lowerCase(AppConstants.ERROR),
					"Mail API is getting error while creating aceess token. Either refresh_token is inactive or expired. Please Generate refresh_token Soon!");
			data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO,
					configurations.getStringProperty(ConfigurationFilesConstants.ISF_SUPPORT_MAIL));
			outlookAndEmailService.sendMail(AppConstants.MESSAGE_PROCESS_ERROR, data);
			
		}

		LOGGER.info("Finished mail processing...");

	}

	/**
	 * This method fetch unread events and process them and mark them as read
	 * 
	 * @param outlookService
	 * @param userPrincipalName
	 * @param requestMessageBody
	 * @throws IOException
	 */
	private boolean processEventMessages(OutlookService outlookService, String userPrincipalName,
			RequestMessageBody requestMessageBody) throws IOException {

		/*
		 * Sometimes meeting request goes to delete folder, in that case we have to
		 * fetch all unread mails from inbox and deleted items folders
		 */

		PagedResult<Message> inboxEventMessages = outlookService
				.getMessages(userPrincipalName, INBOX, EVENT_FILTER_QUERY).execute().body();
		PagedResult<Message> deletedItemsEventMessages = outlookService
				.getMessages(userPrincipalName, DELETED_ITEMS, EVENT_FILTER_QUERY).execute().body();

		if (inboxEventMessages == null || deletedItemsEventMessages == null) {
			return false;
		}

		Message[] eventMessages = ArrayUtils.addAll(inboxEventMessages.getValue(),
				deletedItemsEventMessages.getValue());

		int errorCode = AppConstants.EMAIL_NO_ERROR;

		for (Message eventMessage : eventMessages) {

			try {

				String meetingMessageType = eventMessage.getMeetingMessageType();
				if (StringUtils.isNotBlank(meetingMessageType) && (MEETING_REQUEST.equalsIgnoreCase(meetingMessageType)
						|| MEETING_CANCEL.equalsIgnoreCase(meetingMessageType))) {

					// Get event details from message
					Message event = outlookService.getEvent(userPrincipalName, eventMessage.getId()).execute().body();

					// Process event
					boolean isSuccess = processEvent(event);
					if (!isSuccess) {

						errorCode = AppConstants.EMAIL_ERROR_MEETING_NOT_PROCESSED;
					}

					// Send mail if error occurred
					if (errorCode == AppConstants.EMAIL_ERROR_MEETING_NOT_PROCESSED) {

						sendMail(errorCode, eventMessage.getFrom().getEmailAddress().getAddress(),
								AppConstants.NOTIFICATION_ID_NOT_PROCESSED_MEETING,
								AppConstants.NOTIFICATION_SUB_NOT_PROCESSED_MEETING);
					}

					// Update message and mark message as Read
					outlookService.updateMessage(userPrincipalName, eventMessage.getId(), requestMessageBody).execute();
				}
			} catch (Exception e) {

				sendMail(AppConstants.EMAIL_ERROR_MEETING_NOT_PROCESSED, eventMessage.getFrom().getEmailAddress().getAddress(),
						AppConstants.NOTIFICATION_ID_NOT_PROCESSED_MEETING,
						AppConstants.NOTIFICATION_SUB_NOT_PROCESSED_MEETING);
				
				continue;
			}

		}

		return true;
	}

	/**
	 * This method fetch unread mails and process them and mark them as read
	 * 
	 * @param outlookService
	 * @param userPrincipalName
	 * @param requestMessageBody
	 * @throws IOException
	 * @throws ServiceLocalException
	 */
	private boolean processMessages(OutlookService outlookService, String userPrincipalName,
			RequestMessageBody requestMessageBody) throws IOException, ServiceLocalException {

		int errorCode = AppConstants.EMAIL_NO_ERROR;
		// Get unread mails
		PagedResult<Message> messages = outlookService.getMessages(userPrincipalName, INBOX, MESSAGE_FILTER_QUERY)
				.execute().body();

		if (messages == null) {
			return false;
		}

		for (Message message : messages.getValue()) {

			try {

				// Check if message is meeting type
				if (StringUtils.isNotBlank(message.getMeetingMessageType())) {
					continue;
				}

				// Check if email is valid for processing
				if (isEmailProcessignRequired(message)) {
					// Check if email has attachment
					if (message.getHasAttachments()) {

						// Get attachment details for message
						PagedResult<Attachment> attachments = outlookService
								.getAttachments(userPrincipalName, message.getId()).execute().body();
						for (Attachment attachment : attachments.getValue()) {

							// Process Attachment
							processAttachementData(message, attachment);
						}

					} else {
						errorCode = AppConstants.EMAIL_ERROR_ATTACHEMENT;
					}
				} else {
					errorCode = AppConstants.EMAIL_ERROR_NO_PROCESS_ERROR;
				}

				// Send mail if error occurred
				if (errorCode != AppConstants.EMAIL_NO_ERROR || configurations.getBooleanProperty(ConfigurationFilesConstants.WORKORDER_MAIL_ENABLED, false)) {

					sendMail(errorCode, message.getFrom().getEmailAddress().getAddress(),
							AppConstants.NOTIFICATION_ID_NOT_PROCESSED_EMAIL,
							AppConstants.NOTIFICATION_SUB_NOT_PROCESSED_EMAIL);
					
				}

				// Update message and mark message as Read
				outlookService.updateMessage(userPrincipalName, message.getId(), requestMessageBody).execute();

			} catch (Exception e) {
				
				LOGGER.error("Exception in rest graph mail service in method readMailbox() : {} ", e.getMessage());

					sendMail(AppConstants.EMAIL_ERROR_NO_PROCESS_ERROR, message.getFrom().getEmailAddress().getAddress(),
							AppConstants.NOTIFICATION_ID_NOT_PROCESSED_EMAIL,
							AppConstants.NOTIFICATION_SUB_NOT_PROCESSED_EMAIL);
					
				
				continue;
			}

		}

		return true;
	}

	/**
	 * This method will generate access_token from refresh_token and set it in
	 * spring session
	 * 
	 * @param refreshToken
	 * @param azureApp
	 * @return
	 */
	private TokenResponse getTokenResponse(String refreshToken, AzureAppDetailsEnum azureApp) {

		// Generate access_token from refresh_token
		TokenResponse tokenResponse = AuthHelper.getAccessTokenFromRefreshToken(refreshToken, azureApp);
		return tokenResponse;
	}

	/**
	 * Get active refresh_token from DB
	 * 
	 * @return
	 */
	private String getRefreshToken() {

		// Get active refresh_token from DB
		Map<String, Object> activeTokenDetails = restGraphMailDAO.getActiveRefreshToken();
		String refreshToken = StringUtils.EMPTY;
		if (MapUtils.isEmpty(activeTokenDetails)) {

			LOGGER.info("No Active Refresh Token Found. Please Generate Refresh Token!");
		} else {

			refreshToken = ((String) activeTokenDetails.get("RefreshToken"));
		}

		return refreshToken;
	}

	/**
	 * This method will process event
	 * 
	 * @param eventMessage
	 * @return
	 */
	public boolean processEvent(Message eventMessage) {
		try {

			adhocActivityService.updateAdhocActivityActiveByMeetingId(eventMessage.getId(), false);
			if (MEETING_CANCEL.equals(eventMessage.getMeetingMessageType())) {
				return true;
				// in case cancelled meeting no need to process further contents just deactivate
				// existing adhoc booking
			}
			Map<String, String> meetingDetails = parseEventMessegeContent(eventMessage.getBody().getContent());
			TblProjects proj = null;
			try {
				if (!("0".equals(meetingDetails.get(COL_PROJ_ID)) || "".equals(meetingDetails.get(COL_PROJ_ID))
						|| "na".equalsIgnoreCase(meetingDetails.get(COL_PROJ_ID)))) {
					proj = new TblProjects();
					proj.setProjectId(Integer.parseInt(meetingDetails.get(COL_PROJ_ID)));
				}
			} catch (Exception e) {
				LOGGER.error(ERROR_IN_PROCESSING_MEETING, e);
			}
			TblAdhocBookingActivity activity = adhocActivityService
					.getBookingActivityByActivityAndType(meetingDetails.get(COL_TYPE), meetingDetails.get(COL_SUBTYPE));
			EmployeeModel creator = activityMasterDAO
					.getEmployeeByEmail(eventMessage.getEvent().getOrganizer().getEmailAddress().getAddress());

			Set<String> meetingParticipants = new HashSet<>();

			for (Attendee attendee : eventMessage.getEvent().getAttendees()) {

				meetingParticipants.add(attendee.getEmailAddress().getAddress());
			}
			meetingParticipants.add(eventMessage.getEvent().getOrganizer().getEmailAddress().getAddress());
			for (String e : meetingParticipants) {
				EmployeeModel empDetails = activityMasterDAO.getEmployeeByEmail(e);
				if (empDetails == null)
				{
					continue;
				}
				TblAdhocBooking tblAdhocBooking = new TblAdhocBooking();
				EnvironmentPropertyModel environmentPropertyModel = environmentPropertyService.getEnvironmentPropertyModelByKey("TimeZone").get(0);
				 
				tblAdhocBooking.setStartDate(DateTimeUtil.changeTimeZone(
						eventMessage.getEvent().getStart().getDateTime(), environmentPropertyModel.getValue(), AppConstants.UI_DATE_FORMAT));
				tblAdhocBooking.setActualEndDate(DateTimeUtil.changeTimeZone(
						eventMessage.getEvent().getEnd().getDateTime(), environmentPropertyModel.getValue(), AppConstants.UI_DATE_FORMAT));
				tblAdhocBooking.setSignumID(empDetails.getSignum());
				tblAdhocBooking.setOutlookMeetingId(eventMessage.getConversationId());

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
			LOGGER.error(ERROR_IN_PROCESSING_MEETING, e);
			return false;
		}
		return true;

	}

	/**
	 * 
	 * @param content
	 * @return
	 */
	private Map<String, String> parseEventMessegeContent(String content) {
		Map<String, String> response = new HashMap<>();
		content = content.substring(content.lastIndexOf(ISF_CONTENTS_START) + ISF_CONTENTS_START.length());
		content = content.substring(0, content.lastIndexOf(ISF_CONTENTS_END));
		content = content.replaceAll("###", AppConstants.CSV_CHAR_COMMA);
		content = content.replaceAll("<[^>]*>", StringUtils.EMPTY);
		content = content.replaceAll("&nbsp;", StringUtils.EMPTY);
		content = content.replace("&amp;", "and");
		content = content.replaceAll("\r", StringUtils.EMPTY);
		content = content.replaceAll("\n", StringUtils.EMPTY);
		String[] tokens = content.split(AppConstants.CSV_CHAR_COMMA);
		for (String t : tokens) {
			if (StringUtils.EMPTY.equals(t.trim())) {
				continue;
			}
			String[] v = t.split(":");
			response.put(v[0].trim().trim(), v[1].trim().trim());
		}

		return response;
	}

	/**
	 * 
	 * @param message
	 * @return
	 * @throws ServiceLocalException
	 */
	private boolean isEmailProcessignRequired(Message message) throws ServiceLocalException {
		return (StringUtils.isNotBlank(message.getSubject())
				&& CREATE_WO_MAIL_SUBJECT.equalsIgnoreCase(message.getSubject())
				&& message.getFrom().getEmailAddress().getAddress().endsWith(VALID_INCOMING_DOMAIN));
	}

	/**
	 * 
	 * @param message
	 * @param attachment
	 * @return
	 */
	public boolean processAttachementData(Message message, Attachment attachment) {
		try {

			if (Arrays.asList(EXCEL_MIME_TYPES).contains(attachment.getContentType())) {
				byte[] data = attachment.getContentBytes();
				rpaService.processBulkWorkOrderAttachement(data, message.getFrom().getEmailAddress().getAddress(),
						attachment.getName());
			}
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return false;

	}

	private void sendMail(int errorCode, String toAddress, String templateId, String subject) {

		Map<String, Object> data = new HashMap<>();
		data.put(ERROR_CODE, errorCode);
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, toAddress);
		outlookAndEmailService.sendMail(templateId, subject, data);
	}
}
