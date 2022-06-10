package com.ericsson.isf.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.WebNotificationModel;
import com.ericsson.isf.service.NotificationService;

/**
 * @author ekarmuj Description : This Controller used for Notification.
 */

@RestController
@RequestMapping("/notificationController")
public class NotificationController {



	@Autowired
	private NotificationService notificationService;

	/**
	 * @author ekarmuj
	 * @param signum
	 * @param start
	 * @param length
	 * @param role   Description : This Api Used to get notification based on
	 *               signum.
	 * @return Response<List<WebNotificationModel>>
	 */

	@RequestMapping(value = "/getNotificationsBySignum", method = RequestMethod.GET)
	public Response<List<WebNotificationModel>> getNotificationsBySignum(@RequestHeader("Signum") String signum,
			@RequestParam("start") int start, @RequestParam("length") int length, @RequestHeader("Role") String role) {
		return notificationService.getNotificationsBySignum(signum, start, length, role);
	}

}
