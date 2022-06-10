package com.ericsson.isf.mqttclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;



@SpringBootApplication(scanBasePackages = {"com.ericsson.isf.mqttclient"},exclude = {DataSourceAutoConfiguration.class,RabbitAutoConfiguration.class })
public class IsfRestServerJavaMqttApplication extends SpringBootServletInitializer{
	private static final Logger LOG = LoggerFactory.getLogger(IsfRestServerJavaMqttApplication.class);
	public static void main(String[] args) {
		LOG.info("Log 4j implemented!!!!!!!!!!!!!!!!");
		LOG.debug("Log 4j implemented with debug!!!!!!!!!!!!!!!!1");
		SpringApplication.run(IsfRestServerJavaMqttApplication.class, args);
	}
	@Autowired
	private static ConfigurableApplicationContext context;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	    return builder.sources(IsfRestServerJavaMqttApplication.class);
	}
	public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);
 
        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(IsfRestServerJavaMqttApplication.class, args.getSourceArgs());
        });
 
        thread.setDaemon(false);
        thread.start();
    }
}
