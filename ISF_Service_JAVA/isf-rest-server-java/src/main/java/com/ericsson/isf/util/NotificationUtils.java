package com.ericsson.isf.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ericsson.isf.config.ApplicationConfigurations;

@Component
public class NotificationUtils {
	
	private static final String NOTIFICATION = "Notification_";
	@Autowired
	private ApplicationConfigurations configurations;

	public String createKey(String uploadedBy) {
		StringBuilder key=new StringBuilder();
		key.append(NOTIFICATION).append(StringUtils.upperCase(uploadedBy)).append(AppConstants.UNDERSCORE).append(returnActiveProfileName(configurations.getEnvironment()));
		return key.toString();
	}
	private String returnActiveProfileName(String profile) {

		profile = StringUtils.upperCase(profile);
		String env ;
		switch (profile) {

		case "DEV_MAJOR":
			env = "Dev_Major";
			break;

		case "DEV_MINOR":
			env = "Dev_Minor";
			break;

		case "SIT_MAJOR":
			env = "Sit_Major";
			break;

		case "SIT_MINOR":
			env = "Sit_Minor";
			break;
			
		case "PRE_PROD":
			env = "Preprod";
			break;
		case "PT_ENV":
			env = "PerfTest";
			break;
		case "DR_ENV":
			env = "DryRun";
			break;
		case "PROD":
			env = "PROD";
			break;

		default:
			env = "Dev_Major";
		}
		return env;
	}
}
