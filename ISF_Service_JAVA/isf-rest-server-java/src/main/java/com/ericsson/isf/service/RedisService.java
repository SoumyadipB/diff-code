package com.ericsson.isf.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.isf.common.RedisOperations;

@Service
public class RedisService {
	
	@Autowired
	private RedisOperations redisOpertions;

	public Object getEmployeeSessionData(String employeeEmail) {
		return redisOpertions.getValueBasedOnKey(employeeEmail);
	}

	public void setEmployeeEmailAndSignumFromRedis(String sessionId, String employeeEmail) {
		redisOpertions.setValueBasedOnKey(sessionId, employeeEmail);
	}

	public Set<Object> getAllKeys() {
		return redisOpertions.getAllKey();
	}

	public void setValueBasedOnKey(String key,Object object) {
		redisOpertions.setValueBasedOnKey(key, object);
		
	}

	public Object getDataFromRedis(String key) {
		return redisOpertions.getValueBasedOnKey(key);
	}

	public void deleteDataFromRedis(String key) {
	     redisOpertions.deleteKey(key);
	}
	public void setExpiryTimeOfKey(String key,int expiryTimeInSec) {
		redisOpertions.expireKey(key, expiryTimeInSec);
		
	}
}
