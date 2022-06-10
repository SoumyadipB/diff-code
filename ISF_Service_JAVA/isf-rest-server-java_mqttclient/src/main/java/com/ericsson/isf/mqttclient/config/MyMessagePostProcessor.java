package com.ericsson.isf.mqttclient.config;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
public class MyMessagePostProcessor implements MessagePostProcessor{

	@Override
	public Message postProcessMessage(Message message) throws AmqpException {
		// TODO Auto-generated method stub
		return null;
	}

}
