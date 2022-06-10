package com.ericsson.isf.common;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;


@Component
public class RedisOperations {

	private static final String KEY_CAN_NOT_BE_NULL = "Key can not be Null/Blank.";
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	private static final Logger LOG = LoggerFactory.getLogger(RedisOperations.class);


	/**
	 * @author ekarmuj
	 * Purpose: Used to set Value based on key.
	 * @param key
	 * @param Object
	 */
	
	public void setValueBasedOnKey(String key, Object object) {
		if(StringUtils.isNotEmpty(key)) {
			redisTemplate.opsForValue().set(key, object);
		}else {
			LOG.info(KEY_CAN_NOT_BE_NULL);
		}
	}

	/**
	 * @author ekarmuj
	 * Purpose: Used to get Value based on key.
	 * @param key
	 * @return Object
	 */
	
	public Object getValueBasedOnKey(final String key) {
		if(StringUtils.isNotEmpty(key)) {
			return redisTemplate.opsForValue().get(key);	
		}else {
			return KEY_CAN_NOT_BE_NULL;
		}
		
	}

	/**
	 * @author ekarmuj
	 * @param key
	 * Purpose: Used to delete Value based on key.
	 */
	
	public void deleteKey(String key) {
		if(StringUtils.isNotEmpty(key)) {
		redisTemplate.delete(key);
		}
		else {
			LOG.info(KEY_CAN_NOT_BE_NULL);
		}
	}

	/**
	 * @author ekarmuj
	 *  Purpose: Used to update Value based on key.
	 * @param key
	 * @param object
	 */
	
	public void updateValueBasedOnKey(String key, Object object) {
		if(StringUtils.isNotEmpty(key)) {
			redisTemplate.opsForValue().set(key, object);	
		}
		else {
			LOG.info(KEY_CAN_NOT_BE_NULL);
		}
		
	}

	/**
	 * @author ekarmuj
	 * Purpose: Bulk deletion of cached data
	 * @param keys
	 */
	
	public void deleteMultipleKey(List<String> keys) {
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		// Serialize template first when performing batch deletion operations
		if(CollectionUtils.isNotEmpty(keys)) {
			redisTemplate.setKeySerializer(jackson2JsonRedisSerializer);
			redisTemplate.delete(keys);
		}
		else {
			LOG.info(KEY_CAN_NOT_BE_NULL);
		}
		
	}
	
	/**
	 * @author ekarmuj
	 * Purpose: Used to expire key after a specific period of time .
	 * @param key
	 * @param timeoutInSecond
	 */
	
	public void expireKey(String key,long timeoutInSecond) {
		if (StringUtils.isNotEmpty(key)) {
			redisTemplate.expire(key, timeoutInSecond, TimeUnit.SECONDS);
		}
		else {
			LOG.info(KEY_CAN_NOT_BE_NULL);
		}
	}
	
	public Set<Object> getAllKey(){
		return redisTemplate.keys("*".getBytes());
	    
	}
}
