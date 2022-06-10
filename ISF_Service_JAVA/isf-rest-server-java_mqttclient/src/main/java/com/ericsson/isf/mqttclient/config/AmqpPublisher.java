package com.ericsson.isf.mqttclient.config;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ericsson.isf.mqttclient.dto.AmqpPublishModel;
import com.ericsson.isf.mqttclient.util.AppUtil;

@Service
public class AmqpPublisher {
	private static final Logger logger = LoggerFactory.getLogger(AmqpPublisher.class);

	@Autowired
	@Qualifier(value = "AmqpTemplate")
	private AmqpTemplate amqpTemplate;

	@Autowired
	Binding binding;

	public String send(AmqpPublishModel amqpPublishModel) {
		String qBookingId = StringUtils.EMPTY;
		try {
			String request = AppUtil.convertObjectToJson(amqpPublishModel.getPayload());
			logger.info(request);
			JSONObject mJSONObject = new JSONObject(request);
			if (mJSONObject != null && mJSONObject.has("qBookingId")) {
				logger.info("qBookingId at publisher is: "+mJSONObject.getString("qBookingId"));
				qBookingId = mJSONObject.getString("qBookingId");
			}
			amqpTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), amqpPublishModel);
		} catch (Exception e) {
			logger.info(" error in coverting request!!!!!!!!");
		}
		return qBookingId;
	}

}