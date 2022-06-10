package com.ericsson.isf.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ericsson.isf.dao.NotificationDAO;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.WebNotificationModel;
import com.ericsson.isf.util.NotificationUtils;

@Service
public class NotificationService {
	
	private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);
	@Autowired
	private NotificationDAO notificationDAO;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private NotificationUtils notificationUtils;

	public Response<List<WebNotificationModel>> getNotificationsBySignum(String signum, int start, int length,
			String role) {
		Response<List<WebNotificationModel>> response = new Response<>();

		try {
			List<WebNotificationModel> listofwebmodel = notificationDAO.getNotificationsBySignum(signum, start, length,
					role);
			redisService.deleteDataFromRedis(notificationUtils.createKey(signum));
			
			response.setResponseData(listofwebmodel);
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			response.addFormError(ex.getMessage());
		}
		return response;
	}

}
