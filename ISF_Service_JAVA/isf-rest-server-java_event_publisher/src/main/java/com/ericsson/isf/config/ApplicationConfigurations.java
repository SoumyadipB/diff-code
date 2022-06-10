package com.ericsson.isf.config;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * With help of this class developer will be able to get latest property value
 * from any of the configuration file. If a property value is given in multiple
 * configurations then latest value will override all previous values for that
 * key
 * 
 * @author eakinhm
 *
 */
@Component
@PropertySource(value = { "classpath:properties/app.properties" })
public class ApplicationConfigurations {

	private CompositeConfiguration compositeConfiguration;
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfigurations.class);

	@Value(value = "${event.publisher.file.path}")
	private String filePath;

	@Autowired
	private ServletContext servletContext;

	/**
	 * This method will initialize property files with apache configurations api.
	 */
	@PostConstruct
	private void init() {
		try {
			CompositeConfiguration config = new CompositeConfiguration();
			String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			rootPath = rootPath.startsWith("/") ? rootPath.substring(1) : rootPath;
			String fileLocation = StringUtils.EMPTY.equals(getEnvironment()) ? rootPath + "/properties"
					: filePath + "properties/" + getEnvironment();
			
			PropertiesConfiguration configuration = new PropertiesConfiguration(fileLocation + "/app.properties");

			// Create new FileChangedReloadingStrategy to reload the properties
			// file based on the given time interval
			configuration.setReloadingStrategy(new FileChangedReloadingStrategy());

			// add config sources.
			config.addConfiguration(configuration);
			
			compositeConfiguration = config;
		} catch (ConfigurationException e) {
			LOG.error("init(): Getting error while initializing configuration files!");
			e.printStackTrace();
		}

	}

	/**
	 * This method will return String value of a property key from all configuration
	 * files.
	 * 
	 * @param key
	 * @return String
	 */
	public String getStringProperty(String key) {
		return compositeConfiguration.getString(key);
	}

	/**
	 * This method will return String value of a property key from all configuration
	 * files and if value is missing in configuration file then return default
	 * value.
	 * 
	 * @param key
	 * @return String
	 */
	public String getStringProperty(String key, String defaultValue) {
		return compositeConfiguration.getString(key, defaultValue);
	}

	/**
	 * This method will return Integer value of a property key from all
	 * configuration files.
	 * 
	 * @param key
	 * @return int
	 */
	public int getIntegerProperty(String key) {
		return compositeConfiguration.getInt(key);
	}

	/**
	 * This method will return Integer value of a property key from all
	 * configuration files and if value is missing in configuration file then return
	 * default value.
	 * 
	 * @param key
	 * @return int
	 */
	public int getIntegerProperty(String key, int defaultValue) {
		return compositeConfiguration.getInt(key, defaultValue);
	}

	/**
	 * This method will return Boolean value of a property key from all
	 * configuration files.
	 * 
	 * @param key
	 * @return boolean
	 */
	public boolean getBooleanProperty(String key) {
		return compositeConfiguration.getBoolean(key, false);
	}

	/**
	 * This method will return Boolean value of a property key from all
	 * configuration files and if value is missing in configuration file then return
	 * default value.
	 * 
	 * @param key
	 * @return boolean
	 */
	public boolean getBooleanProperty(String key, boolean defaultValue) {
		return compositeConfiguration.getBoolean(key, defaultValue);
	}

	/**
	 * This method will return environment name on which application has been
	 * deployed.
	 * 
	 * @return String
	 */
	private String getEnvironment() {
		String splitArray[] = new File(servletContext.getRealPath("/")).getName().split("-java");
		if (splitArray.length == 1) {
			return StringUtils.EMPTY;
		} else {
			return splitArray[1].substring(1, splitArray[1].length());
		}
	}

}
