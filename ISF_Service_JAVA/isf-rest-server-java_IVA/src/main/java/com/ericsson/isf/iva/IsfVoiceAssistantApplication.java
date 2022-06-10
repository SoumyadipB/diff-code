package com.ericsson.isf.iva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.ericsson.isf.iva"})
public class IsfVoiceAssistantApplication extends SpringBootServletInitializer{
	
	@Autowired
	private static ConfigurableApplicationContext context;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	    return builder.sources(IsfVoiceAssistantApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(IsfVoiceAssistantApplication.class, args);
		
	}
	
	public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);
 
        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(IsfVoiceAssistantApplication.class, args.getSourceArgs());
        });
 
        thread.setDaemon(false);
        thread.start();
    }

}
