
package com.ericsson.isf.iva.aop;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ericsson.isf.iva.dao.IvaManagementDAO;

//AOP //Configuration

@Aspect

@Configuration
public class UserAccessAspect {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IvaManagementDAO ivaManagementDAO;



	/*
	 * @After("execution(* com.ericsson.isf.iva.service.*.*(..))") // public void
	 * returnJwtToken(JoinPoint joinPoint) {
	 * 
	 * }
	 */
 }
