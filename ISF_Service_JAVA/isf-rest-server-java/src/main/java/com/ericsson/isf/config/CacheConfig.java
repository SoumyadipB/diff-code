package com.ericsson.isf.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.ericsson.isf.azure.keyVault.service.ClientSecretKeyValueProvider;
import com.ericsson.isf.exception.RedisCacheErrorHandler;
import com.ericsson.isf.model.AzureAppModel;
import com.ericsson.isf.model.VaultModel;
import com.ericsson.isf.service.AzureService;
import com.ericsson.isf.util.ConfigurationFilesConstants;

import redis.clients.jedis.JedisPoolConfig;

/**
 * Purpose:  This class is used to connect with Azure redis cache.
 * @author ekarmuj
 *
 */

@Configuration
public class CacheConfig extends CachingConfigurerSupport {

	private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);
	
	@Autowired
	private ApplicationConfigurations configurations;
	
	@Autowired
	private AzureService azureService;
	
	@Autowired
	private ClientSecretKeyValueProvider clientSecretKeyValueProvider;
	
	private static String REDIS_HOST_NAME;
	private static int REDIS_PORT;
	private static String REDIS_PASSWORD;
	private static boolean REDIS_USE_SSL;
	/**
	 * @author ekarmuj
	 * This method used to get Secret Authorization Value For Redis cache
	 * @return redis cache secret value.
	 */
	
	private String getSecretAuthorizationValueForRedis() {
		try {

			String keyVaultName = configurations.getStringProperty(ConfigurationFilesConstants.AZURE_REDIS_KEYVAULT_NAME);
			String apiRedisAuthKey =configurations.getStringProperty(ConfigurationFilesConstants.AZURE_REDIS_CONNECTION_STRING);
			VaultModel vaultModel = azureService.getActiveSecretInfoByVaultNameAndSecretType(keyVaultName,
					apiRedisAuthKey);
			AzureAppModel azureAppModel = azureService.getInfoByAzureAppName(
					configurations.getStringProperty(ConfigurationFilesConstants.AZURE_APP_NAME));

			String secretValue = clientSecretKeyValueProvider.getSecretValue(azureAppModel, vaultModel);
			log.info("REDIS security key obtained");
			return secretValue;
		} catch (Exception e) {

			e.printStackTrace();
			log.error("error occured while getting REDIS Auth Key: " + e.getMessage());
			return null;
		}
	}

	/**
	 * @author ekarmuj
	 * This method used to manages the resource pool by using Apache Commons-pool2.
	 * @return JedisPoolConfig
	 */

	public JedisPoolConfig createPoolConfig() {

		JedisPoolConfig poolConfig = new JedisPoolConfig();
		//When negative, there is no limit to the number of objects that can be managed by the pool at one time.
		poolConfig.setMaxTotal(configurations.getIntegerProperty(ConfigurationFilesConstants.JEDIS_POOL_MAX_TOTAL)); //The maximum number of connections that are supported by the pool.Default is 8.
		//Use a negative value to indicate an unlimited number of idle instances
		poolConfig.setMaxIdle(configurations.getIntegerProperty(ConfigurationFilesConstants.JEDIS_POOL_MAX_IDLE));  //The maximum number of idle connections in the pool.Default is 8.
		// true if borrowObject()should block when the pool is exhausted
		poolConfig.setBlockWhenExhausted(true);  //Specifies whether the client must wait when the resource pool is exhausted. The following maxWaitMillis parameter takes effect only when this parameter is set to true.
		//maximum number of milliseconds <code>borrowObject()</code> will block
		poolConfig.setMaxWaitMillis( configurations.getLongProperty(ConfigurationFilesConstants.MAX_WAIT_MILLIS)); //The maximum number of milliseconds that the client needs to wait when no connection is available.A value of -1 specifies that the connection will never time out.
		//poolConfig.setMinIdle(minIdle); //The minimum number of idle connections in the pool.	Default is 0.
		poolConfig.setTestOnBorrow(true); //	Specifies whether connections will be validated by using the PING command before they are borrowed from the pool. Invalid connections will be removed from the pool.By default it is false
		return poolConfig;
	}

	/**
	 * @author ekarmuj
	 * This method used to return RedisConnectionFactory.
	 * @return RedisConnectionFactory
	 */
	
	@Bean
	RedisConnectionFactory jedisConnectionFactory() {
      //  setConnectionProperties(getSecretAuthorizationValueForRedis());
		log.info("=================================================================");
        log.info("redis config : {} : {} ", configurations.getStringProperty(ConfigurationFilesConstants.REDIS_HOST_NAME), 
        		configurations.getIntegerProperty(ConfigurationFilesConstants.REDIS_PORT));
		//log.info("redis config : {} : {} ", REDIS_HOST_NAME, REDIS_PORT);
		log.info("=================================================================");
        
		JedisConnectionFactory jedis = new JedisConnectionFactory();
		//jedis.setHostName(REDIS_HOST_NAME);
		//jedis.setPort(REDIS_PORT);
		//jedis.setPassword(REDIS_PASSWORD);
		jedis.setHostName(configurations.getStringProperty(ConfigurationFilesConstants.REDIS_HOST_NAME));
		jedis.setPort(configurations.getIntegerProperty(ConfigurationFilesConstants.REDIS_PORT));
		jedis.setPassword(configurations.getStringProperty(ConfigurationFilesConstants.REDIS_PASSWORD));
		//jedis.setPassword(getSecretAuthorizationValueForRedis());
		jedis.setPoolConfig(createPoolConfig());
		jedis.setUseSsl(configurations.getBooleanProperty(ConfigurationFilesConstants.REDIS_USE_SSL));
		//jedis.setUseSsl(REDIS_USE_SSL);
		jedis.afterPropertiesSet();

		return jedis;

	}

	private void setConnectionProperties(String connectionString) {
		try {
			if(StringUtils.isNotEmpty(connectionString)) {
				String[] splitBasedOnComma=connectionString.split(",");
				if(splitBasedOnComma.length>3) {
					REDIS_HOST_NAME=(splitBasedOnComma[0]!=null && splitBasedOnComma[0]!="")?splitBasedOnComma[0].split(":")[0]:"";
					REDIS_PORT=(splitBasedOnComma[0]!=null && splitBasedOnComma[0]!="") ?Integer.parseInt(splitBasedOnComma[0].split(":")[1]):0;
					REDIS_PASSWORD=(splitBasedOnComma[1]!=null && splitBasedOnComma[1]!="") ?splitBasedOnComma[1].substring(9):"";
					REDIS_USE_SSL=(splitBasedOnComma[2]!=null && splitBasedOnComma[2]!="") ?Boolean.parseBoolean(splitBasedOnComma[2].substring(4)):false;
				}else {
					log.error("Connection string is not correct.Connection string is " + connectionString);
				}
					
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("error occured while setting properties of redis from key voult: " + e.getMessage());
		}
		
		
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate() {
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
				Object.class);
		try {
			final RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(jedisConnectionFactory());
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
			redisTemplate.setHashKeySerializer(new StringRedisSerializer());
			redisTemplate.afterPropertiesSet();
			return redisTemplate;
		} catch (Exception e) {
			throw new RedisCacheErrorHandler();
		}
		
	}
	
	

//////////////////////
//	     @Bean
//	    public CacheManager cacheManager(@Autowired RedisTemplate<Object, Object> redisTemplate) {
//	        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
//	        cacheManager.setDefaultExpiration(300);
//	        return cacheManager;
//	    }
//	@Override
//	public CacheErrorHandler errorHandler() {
//		return new RedisCacheErrorHandler();
//	}

	
}