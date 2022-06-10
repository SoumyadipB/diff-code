package com.ericsson.isf.iva.profiles.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
public class DBConfiguration {
	
	@Value(value = "${spring.datasource.url}")
	private String springDataSourceURL;
	
	@Value(value = "${app.message}")
	private String appMessage;
	
	@Profile("dev-blue")
	@Bean
	public String devDatabaseConnection() {

		System.out.println(springDataSourceURL);
		System.out.println(appMessage);
		return "Db connection for Dev";
	}
	
	@Profile("dev-feature-blue")
	@Bean
	public String devFeatureDatabaseConnection() {

		System.out.println(springDataSourceURL);
		System.out.println(appMessage);
		return "Db connection for Dev Feature";
	}

	@Profile("sit-major")
	@Bean
	public String sitMajorDatabaseConnection() {

		System.out.println(springDataSourceURL);
		System.out.println(appMessage);
		return "Db connection for SIT-MAJOR";
	}
	
	@Profile("sit-minor")
	@Bean
	public String sitMinorDatabaseConnection() {

		System.out.println(springDataSourceURL);
		System.out.println(appMessage);
		return "Db connection for SIT-MINOR";
	}
	
	@Profile("pre-prod")
	@Bean
	public String preProdDatabaseConnection() {

		System.out.println(springDataSourceURL);
		System.out.println(appMessage);
		return "Db connection for pre-prod";
	}
	
	@Profile("dev-minor")
	@Bean
	public String devMinorDatabaseConnection() {

		System.out.println(springDataSourceURL);
		System.out.println(appMessage);
		return "Db connection for dev-minor";
	}
	
	@Profile("prod")
	@Bean
	public String prodDatabaseConnection() {

		System.out.println(springDataSourceURL);
		System.out.println(appMessage);
		return "Db connection for prod";
	}
}
