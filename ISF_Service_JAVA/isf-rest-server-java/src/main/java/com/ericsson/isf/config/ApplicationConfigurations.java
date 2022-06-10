package com.ericsson.isf.config;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
//@PropertySource(value = { "classpath:properties/appConfig.properties" })
public class ApplicationConfigurations {

	private CompositeConfiguration compositeConfiguration;
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfigurations.class);

	//@Value(value = "${config.file.path}")
	private String filePath=System.getenv("CONFIG_FILE_PATH");
	
//	@Value(value = "${app.config.url}")
//	private String appConfigUrl;
	
//	@Value(value = "${environment.os}")
//	private String operatingSystem;

	@Autowired
	private ServletContext servletContext;
	


	/**
	 * This method will initialize property files with apache configurations api.
	 */
//	@PostConstruct
	private void init() {
		
		try {
			CompositeConfiguration config = new CompositeConfiguration();
			String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			rootPath = rootPath.startsWith("/") ? rootPath.substring(1) : rootPath;
            LOG.info("Backup File Path is "+filePath);
			String fileLocation = StringUtils.EMPTY.equals(getEnvironment()) ? rootPath + "/properties"
					: filePath + "properties/" + getEnvironment();

			PropertiesConfiguration configuration = new PropertiesConfiguration(fileLocation + "/app.properties");
			PropertiesConfiguration configuration2 = new PropertiesConfiguration(fileLocation + "/db.properties");

			// Create new FileChangedReloadingStrategy to reload the properties
			configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
			configuration2.setReloadingStrategy(new FileChangedReloadingStrategy());

			// add config sources.
			config.addConfiguration(configuration);
			config.addConfiguration(configuration2);
			compositeConfiguration = config;
			LOG.info("Getting Properties from Local Config");
		} catch (ConfigurationException e) {
			LOG.error("init(): Getting error while initializing configuration files!");
			e.printStackTrace();
		}

	}
	@PostConstruct
	private void appConfigurations() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			CompositeConfiguration config = new CompositeConfiguration();
			String env = getEnvironment();
			String operatingSystem = System.getenv("CONFIG_OS");
			String appConfigUrl;
			if (StringUtils.equalsIgnoreCase(env, "DR") && StringUtils.equalsIgnoreCase(operatingSystem, "Linux")) {
					appConfigUrl = System.getenv("CONFIG_URL_STAGING");
				} else {
					appConfigUrl = System.getenv("CONFIG_URL");
					}
				LOG.info("Reading CONFIG_URL from Server "+appConfigUrl);
				LOG.info("Backup File Path is "+filePath);//To be Removed
			if(!appConfigUrl.endsWith("/")) 
			{
				appConfigUrl=appConfigUrl+"/";
		    }
			String appUrl;
			LOG.info("Reading CONFIG_OS from Server "+operatingSystem);
			if(StringUtils.equalsIgnoreCase(operatingSystem,"Linux")) {
			appUrl=appConfigUrl+env+"/app_lnx";
			}
			else {
			appUrl=appConfigUrl+env+"/app";
			}
			ParameterizedTypeReference<Map<String, Object>> typeRef = new ParameterizedTypeReference<Map<String, Object>>() {};
	        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(appUrl, HttpMethod.GET, null,typeRef);
	        Map<String, Object> finalResponse=response.getBody();
	        List<Map<String,Object>> listResponse=(List<Map<String, Object>>) finalResponse.get("propertySources");
	        Map<String,Object> newResponse=(Map<String, Object>) listResponse.get(0).get("source");
	        for(Entry<String, Object> entry:newResponse.entrySet()) {
	        	config.addProperty(entry.getKey(), newResponse.get(entry.getKey()));
	        }
	        String dbUrl =appConfigUrl+env+"/db";
	        ResponseEntity<Map<String, Object>> response2 = restTemplate.exchange(dbUrl, HttpMethod.GET, null,typeRef);
	        Map<String, Object> finalResponse2=response2.getBody();
	        List<Map<String,Object>> listResponse2=(List<Map<String, Object>>) finalResponse2.get("propertySources");
	        Map<String,Object> newResponse2=(Map<String, Object>) listResponse2.get(0).get("source");
	        
	        for(Entry<String, Object> entry:newResponse2.entrySet()) {
	        	config.addProperty(entry.getKey(), newResponse2.get(entry.getKey()));
	        }
	        compositeConfiguration = config;
	        LOG.info("Getting Properties from App Config(CLOUD)");
		}
		catch(Exception e) {
			//Get Properties file from Local if getting any exception
			   init();	
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
	
	public String[] getStringArrayProperty(String key) {
		return compositeConfiguration.getStringArray(key);
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
	 * This method will return Integer value of a property key from all
	 * configuration files.
	 * 
	 * @param key
	 * @return long
	 */
	
	public long getLongProperty(String key) {
		return compositeConfiguration.getLong(key);
	}

	/**
	 * This method will return environment name on which application has been
	 * deployed.
	 * 
	 * @return String
	 */
	public String getEnvironment() {
		String splitArray[] = new File(servletContext.getRealPath("/")).getName().split("-java");
		if (splitArray.length == 1) {
			return StringUtils.EMPTY;
		} else {
			return splitArray[1].substring(1, splitArray[1].length());
		}
	}

}
