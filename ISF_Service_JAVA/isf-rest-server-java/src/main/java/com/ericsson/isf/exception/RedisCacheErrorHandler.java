package com.ericsson.isf.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;


public class RedisCacheErrorHandler extends RuntimeException implements CacheErrorHandler{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(RedisCacheErrorHandler.class);

	@Override
	public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
		log.info("Unable to get from cache " + cache.getName() + " : " + exception.getMessage());
	}

	@Override
	public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
		log.info("Unable to put into cache " + cache.getName() + " : " + exception.getMessage());
	}

	@Override
	public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
		log.info("Unable to evict from cache " + cache.getName() + " : " + exception.getMessage());
	}

	@Override
	public void handleCacheClearError(RuntimeException exception, Cache cache) {
		log.info("Unable to clean cache " + cache.getName() + " : " + exception.getMessage());
	}


}
