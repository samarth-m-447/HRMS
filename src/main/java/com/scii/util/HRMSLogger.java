package com.scii.util;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HRMSLogger {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Before("execution(* com.scii.controller.*.*(..))"
			+ "|| execution(* com.scii.service.*.*(..))"
			+ "|| execution(* com.scii.util.*.*(..))"
			+ "|| execution(* com.scii.auth.*.*(..))")
	public void logBefore(JoinPoint point) {
		logger.debug(point.getTarget().getClass() + " - " + point.getSignature().getName() + " Entering...");
	}
	
	@After("execution(* com.scii.controller.*.*(..))"
			+ "|| execution(* com.scii.service.*.*(..))"
			+ "|| execution(* com.scii.util.*.*(..))"
			+ "|| execution(* com.scii.auth.*.*(..))")
	public void logAfter(JoinPoint point) {
		logger.debug(point.getTarget().getClass() + " - " + point.getSignature().getName() + " Exiting!!!");
	}
	
	@AfterThrowing(pointcut = "execution(* com.scii.controller.*.*(..))"
			+ "|| execution(* com.scii.service.*.*(..))"
			+ "|| execution(* com.scii.util.*.*(..))"
			+ "|| execution(* com.scii.auth.*.*(..))", throwing = "ex")
	public void logError(JoinPoint point, Exception ex) {
		logger.error(point.getTarget().getClass() + " - " + point.getSignature().getName() + ex);
	}
	
}
