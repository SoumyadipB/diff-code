package com.ericsson.isf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ WebConfig.class, PropertiesConfig.class, DataSourceConfig.class })
public class ServletConfig {
	
}
