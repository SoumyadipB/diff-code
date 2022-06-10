package com.ericsson.isf.mqttclient.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration("amqp")
public class AmqpConfig {
	private static final Logger logger = LoggerFactory.getLogger(AmqpConfig.class);
	
	@Autowired
	private  ApplicationConfigurations configurations;

	@Bean
	Queue queue() {
		String queueName = StringUtils.EMPTY;
		if (configurations.getBooleanProperty("server.bot.execution.aks.enabled")) {
			queueName = configurations.getStringProperty("spring.rabbitmq.queue_aks");
			logger.info("Queue Name is:::::: " + queueName);
			return new Queue(queueName, false);
		} else {
			queueName = configurations.getStringProperty("spring.rabbitmq.queue");
			logger.info("Queue Name is:::::: " + queueName);
			return new Queue(queueName, false);
		}

	}

	@Bean
	DirectExchange exchange() {
		String exchange=configurations.getStringProperty("spring.rabbitmq.exchange");
		logger.info("exchange Name is:::::: "+exchange);
		return new DirectExchange(exchange);
	}

	@Bean
	Binding binding(Queue queue, DirectExchange exchange) {
		String routingkey=configurations.getStringProperty("spring.rabbitmq.routingkey");
		logger.info("routingkey Name is:::::: "+routingkey);
		return BindingBuilder.bind(queue).to(exchange).with(routingkey);
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	@Bean(name="AmqpTemplate")
	public AmqpTemplate rabbitTemplate() {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(rabbitConnectionFactory());
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
	@Bean
	public CachingConnectionFactory rabbitConnectionFactory() {
		
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(configurations.getStringProperty("spring.rabbitmq.host"));
		connectionFactory.setUsername(configurations.getStringProperty("spring.rabbitmq.username"));
		connectionFactory.setPassword(configurations.getStringProperty("spring.rabbitmq.password"));
		connectionFactory.setVirtualHost("/");
		connectionFactory.getRabbitConnectionFactory().setAutomaticRecoveryEnabled(true);
		connectionFactory.setHost(configurations.getStringProperty("spring.rabbitmq.host"));
		connectionFactory.afterPropertiesSet();
		return connectionFactory;
	}
}
